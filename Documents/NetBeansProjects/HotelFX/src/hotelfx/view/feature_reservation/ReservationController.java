/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hotelfx.view.feature_reservation;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXCComboBoxPopup;
import com.jfoenix.controls.JFXCComboBoxTablePopup;
import com.jfoenix.controls.JFXTextArea;
import com.jfoenix.controls.JFXTextField;
import hotelfx.HotelFX;
import hotelfx.helper.ClassFilteringTable;
import hotelfx.helper.ClassFolderManager;
import hotelfx.helper.ClassFormatter;
import hotelfx.helper.ClassMessage;
import hotelfx.helper.ClassPrinter;
import hotelfx.helper.ClassSession;
import hotelfx.helper.ClassTableWithControl;
import hotelfx.helper.ClassViewSetting;
import hotelfx.persistence.model.RefFinanceTransactionPaymentType;
import hotelfx.persistence.model.RefRecordStatus;
import hotelfx.persistence.model.RefReservationBillDiscountType;
import hotelfx.persistence.model.RefReservationBillType;
import hotelfx.persistence.model.RefReservationCheckInOutStatus;
import hotelfx.persistence.model.RefReservationOrderByType;
import hotelfx.persistence.model.RefReservationStatus;
import hotelfx.persistence.model.RefRoomStatus;
import hotelfx.persistence.model.RefVoucherStatus;
import hotelfx.persistence.model.SysDataHardCode;
import hotelfx.persistence.model.TblBank;
import hotelfx.persistence.model.TblBankAccount;
import hotelfx.persistence.model.TblBankCard;
import hotelfx.persistence.model.TblBankEdc;
import hotelfx.persistence.model.TblBankEdcBankNetworkCard;
import hotelfx.persistence.model.TblBankEventCard;
import hotelfx.persistence.model.TblBankNetworkCard;
import hotelfx.persistence.model.TblCustomer;
import hotelfx.persistence.model.TblEmployee;
import hotelfx.persistence.model.TblGuaranteeLetterItemDetail;
import hotelfx.persistence.model.TblHotelEvent;
import hotelfx.persistence.model.TblItem;
import hotelfx.persistence.model.TblPartner;
import hotelfx.persistence.model.TblPeople;
import hotelfx.persistence.model.TblReservation;
import hotelfx.persistence.model.TblReservationAdditionalItem;
import hotelfx.persistence.model.TblReservationAdditionalService;
import hotelfx.persistence.model.TblReservationBill;
import hotelfx.persistence.model.TblReservationBrokenItem;
import hotelfx.persistence.model.TblReservationCanceled;
import hotelfx.persistence.model.TblReservationCheckInOut;
import hotelfx.persistence.model.TblReservationPayment;
import hotelfx.persistence.model.TblReservationPaymentWithBankCard;
import hotelfx.persistence.model.TblReservationPaymentWithCekGiro;
import hotelfx.persistence.model.TblReservationPaymentWithGuaranteePayment;
import hotelfx.persistence.model.TblReservationPaymentWithReservationVoucher;
import hotelfx.persistence.model.TblReservationPaymentWithTransfer;
import hotelfx.persistence.model.TblReservationRoomPriceDetail;
import hotelfx.persistence.model.TblReservationRoomTypeDetail;
import hotelfx.persistence.model.TblReservationRoomTypeDetailChildDetail;
import hotelfx.persistence.model.TblReservationRoomTypeDetailRoomPriceDetail;
import hotelfx.persistence.model.TblReservationRoomTypeDetailRoomPriceDetailChangeRoomHistory;
import hotelfx.persistence.model.TblReservationRoomTypeDetailTravelAgentDiscountDetail;
import hotelfx.persistence.model.TblReservationTravelAgentDiscountDetail;
import hotelfx.persistence.model.TblReservationVoucher;
import hotelfx.persistence.model.TblRoom;
import hotelfx.persistence.model.TblRoomService;
import hotelfx.persistence.model.TblRoomType;
import hotelfx.persistence.model.TblTravelAgent;
import hotelfx.persistence.service.FReservationManager;
import hotelfx.persistence.service.FReservationManagerImpl;
import hotelfx.view.DashboardController;
import insidefx.undecorator.Undecorator;
import java.io.IOException;
import java.math.BigDecimal;
import java.net.URL;
import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.beans.binding.Bindings;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Separator;
import javafx.scene.control.SplitPane;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.TitledPane;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.CornerRadii;
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
public class ReservationController implements Initializable {

    /**
     * SPLITPANE BETWEEN TABLE AND FORM, CONTENT LAYOUT
     */
    @FXML
    private SplitPane spDataReservation;

    private DoubleProperty dataReservationFormShowStatus;

    @FXML
    private AnchorPane contentLayout;

    @FXML
    private AnchorPane tableDataReservationLayoutDisableLayer;

    private boolean isTimeLinePlaying = false;

    private void setDataReservationSplitpane() {
        spDataReservation.setDividerPositions(1);

        dataReservationFormShowStatus = new SimpleDoubleProperty(1.0);

        DoubleProperty divPosition = new SimpleDoubleProperty();

//        divPosition.bind(new SimpleDoubleProperty(1.0)
//                .subtract((formAnchor.prefWidthProperty().divide(spDataReservation.widthProperty()))
//                        .multiply(dataReservationFormShowStatus)
//                )
//        );
        divPosition.bind(new SimpleDoubleProperty(1.0)
                .subtract(dataReservationFormShowStatus)
        );

        divPosition.addListener((obs, oldVal, newVal) -> {
            isTimeLinePlaying = true;
            KeyValue keyValue = new KeyValue(spDataReservation.getDividers().get(0).positionProperty(), newVal);
            Timeline timeline = new Timeline(new KeyFrame(Duration.millis(ClassViewSetting.keyFrameDuration), keyValue));
            timeline.play();
            timeline.setOnFinished((e) -> {
                isTimeLinePlaying = false;
            });
        });

        SplitPane.Divider div = spDataReservation.getDividers().get(0);
        div.positionProperty().addListener((obs, oldValue, newValue) -> {
            if (!isTimeLinePlaying) {
                div.setPosition(divPosition.get());
            }
        });

        dataReservationFormShowStatus.addListener((obs, oldVal, newVal) -> {
            if (dataInputStatus != 3) {
                if (newVal.doubleValue() == 0.0) {    //enable
                    tableDataReservationLayout.setDisable(false);
                    tableDataReservationLayoutDisableLayer.setDisable(true);
                    tableDataReservationLayout.toFront();
                }
                if (newVal.doubleValue() == 1) {  //disable
                    tableDataReservationLayout.setDisable(true);
                    tableDataReservationLayoutDisableLayer.setDisable(false);
                    tableDataReservationLayoutDisableLayer.toFront();
                }
            }
        });

        dataReservationFormShowStatus.set(0.0);
    }

    /**
     * TABLE DATA RESERVATION
     */
    @FXML
    private AnchorPane tableDataReservationLayout;

    private ClassFilteringTable<TblReservation> cft;

    @FXML
    private AnchorPane ancHeaderLayout;

    @FXML
    private AnchorPane ancBodyLayout;

    private ClassTableWithControl tableDataReservation;

    private void initTableDataReservation() {
        //set table
        setTableDataReservation();
        //set control
        setTableControlDataReservation();
        //set table-control to content-view
        ancBodyLayout.getChildren().clear();
        AnchorPane.setTopAnchor(tableDataReservation, 15.0);
        AnchorPane.setLeftAnchor(tableDataReservation, 15.0);
        AnchorPane.setBottomAnchor(tableDataReservation, 15.0);
        AnchorPane.setRightAnchor(tableDataReservation, 15.0);

        ancBodyLayout.getChildren().add(tableDataReservation);
    }

    private void setTableDataReservation() {
        TableView<TblReservation> tableView = new TableView();

        TableColumn<TblReservation, String> codeReservation = new TableColumn("No. Reservasi");
        codeReservation.setCellValueFactory(cellData -> cellData.getValue().codeReservationProperty());
        codeReservation.setMinWidth(120);

        TableColumn<TblReservation, String> reservationDate = new TableColumn("Tanggal Reservasi");
        reservationDate.setCellValueFactory((TableColumn.CellDataFeatures<TblReservation, String> param)
                -> Bindings.createStringBinding(() -> ClassFormatter.dateTimeFormate.format(param.getValue().getReservationDate()), param.getValue().reservationDateProperty()));
        reservationDate.setMinWidth(150);

        TableColumn<TblReservation, String> peopleName = new TableColumn<>("Nama");
        peopleName.setCellValueFactory((TableColumn.CellDataFeatures<TblReservation, String> param)
                -> Bindings.createStringBinding(() -> param.getValue().getTblCustomer().getTblPeople().getFullName(), param.getValue().tblCustomerProperty()));
        peopleName.setMinWidth(150);

        TableColumn<TblReservation, String> peopleID = new TableColumn<>("No. Identitas");
        peopleID.setCellValueFactory((TableColumn.CellDataFeatures<TblReservation, String> param)
                -> Bindings.createStringBinding(() -> param.getValue().getTblCustomer().getTblPeople().getRefPeopleIdentifierType().getTypeName()
                        + " : " + param.getValue().getTblCustomer().getTblPeople().getCodePeople(),
                        param.getValue().tblCustomerProperty()));
        peopleID.setMinWidth(220);

        TableColumn<TblReservation, String> peopleHPNumber = new TableColumn<>("No. HP");
        peopleHPNumber.setCellValueFactory((TableColumn.CellDataFeatures<TblReservation, String> param)
                -> Bindings.createStringBinding(() -> param.getValue().getTblCustomer().getTblPeople().getHpnumber() != null
                                ? param.getValue().getTblCustomer().getTblPeople().getHpnumber() : "-",
                        param.getValue().tblCustomerProperty()));
        peopleHPNumber.setMinWidth(140);

        TableColumn<TblReservation, String> peopleEmail = new TableColumn<>("Email");
        peopleEmail.setCellValueFactory((TableColumn.CellDataFeatures<TblReservation, String> param)
                -> Bindings.createStringBinding(() -> param.getValue().getTblCustomer().getTblPeople().getEmail() != null
                                ? param.getValue().getTblCustomer().getTblPeople().getEmail() : "-",
                        param.getValue().tblCustomerProperty()));
        peopleEmail.setMinWidth(220);

        TableColumn<TblReservation, String> customerTitle = new TableColumn("Customer");
        customerTitle.getColumns().addAll(peopleID, peopleName, peopleHPNumber, peopleEmail);

        TableColumn<TblReservation, String> travelAgentName = new TableColumn<>("Travel Agent");
        travelAgentName.setCellValueFactory((TableColumn.CellDataFeatures<TblReservation, String> param)
                -> Bindings.createStringBinding(() -> param.getValue().getTblPartner() == null ? "-" : param.getValue().getTblPartner().getPartnerName(), param.getValue().tblPartnerProperty()));
        travelAgentName.setMinWidth(140);

        TableColumn<TblReservation, String> reservationStatus = new TableColumn<>("Status");
        reservationStatus.setCellValueFactory((TableColumn.CellDataFeatures<TblReservation, String> param)
                -> Bindings.createStringBinding(() -> param.getValue().getRefReservationStatus().getStatusName(), param.getValue().refReservationStatusProperty()));
        reservationStatus.setMinWidth(140);

        tableView.getColumns().addAll(codeReservation, reservationDate, customerTitle, travelAgentName, reservationStatus);
        tableView.setItems(loadAllDataReservation());

        tableView.setRowFactory(tv -> {
            TableRow<TblReservation> row = new TableRow<>();
            row.setOnMouseClicked((e) -> {
                if (e.getClickCount() == 2) {
                    if (isShowStatus.get()) {
                        dataReservationUnshowHandle();
                    } else {
                        if ((!row.isEmpty())) {
                            if (DashboardController.feature.getSelectedRoleFeature().getUpdateData()) {
                                selectedData = fReservationManager.getReservation(((TblReservation) row.getItem()).getIdreservation());
                                selectedData.setRefReservationStatus(fReservationManager.getReservationStatus(selectedData.getRefReservationStatus().getIdstatus()));
                                if (selectedData.getRefReservationStatus().getIdstatus() == 1 //Reserved = '1'
                                        || selectedData.getRefReservationStatus().getIdstatus() == 2) {  //Booked = '2'
                                    dataReservationUpdateHandleDetail();
                                } else {
                                    dataReservationShowHandle();
                                }
                            } else {
                                dataReservationShowHandle();
                            }
                        }
                    }
//                } else {
//                    if (!row.isEmpty()) {
//                        if (isShowStatus.get()) {
//                            if (DashboardController.feature.getSelectedRoleFeature().getUpdateData()) {
//                                selectedData = fReservationManager.getReservation(((TblReservation) row.getItem()).getIdreservation());
//                                selectedData.setRefReservationStatus(fReservationManager.getReservationStatus(selectedData.getRefReservationStatus().getIdstatus()));
//                                if (selectedData.getRefReservationStatus().getIdstatus() == 1 //Reserved = '1'
//                                        || selectedData.getRefReservationStatus().getIdstatus() == 2) {  //Booked = '2'
//                                    dataReservationUpdateHandleDetail();
//                                } else {
//                                    dataReservationShowHandle();
//                                }
//                            } else {
//                                dataReservationShowHandle();
//                            }
//                        }
//                    }
                }
            });
            return row;
        });

        tableDataReservation = new ClassTableWithControl(tableView);

        //set filter
        cft = new ClassFilteringTable<>(
                TblReservation.class,
                tableDataReservation.getTableView(),
                tableDataReservation.getTableView().getItems());

        AnchorPane.setBottomAnchor(cft, 12.5);
//        AnchorPane.setLeftAnchor(cft, 15.0);
        AnchorPane.setRightAnchor(cft, 15.0);
        AnchorPane.setTopAnchor(cft, 12.5);
        ancHeaderLayout.getChildren().clear();
        ancHeaderLayout.getChildren().add(cft);

    }

    private void setTableControlDataReservation() {
        //set control from feature
        ObservableList<Node> buttonControls = FXCollections.observableArrayList();
        JFXButton buttonControl;
        if (DashboardController.feature.getSelectedRoleFeature().getCreateData()) {
            buttonControl = new JFXButton();
            buttonControl.setText("Tambah");
            buttonControl.setOnMouseClicked((e) -> {
                //listener add
                dataReservationRoomTypeDetailReservationTypeChooseHandle();
            });
            buttonControls.add(buttonControl);
        }
        if (DashboardController.feature.getSelectedRoleFeature().getUpdateData()) {
            buttonControl = new JFXButton();
            buttonControl.setText("Ubah");
            buttonControl.setOnMouseClicked((e) -> {
                //listener update
                dataReservationUpdateHandle();
            });
            buttonControls.add(buttonControl);
        }
        if (DashboardController.feature.getSelectedRoleFeature().getDeleteData()) {
            buttonControl = new JFXButton();
            buttonControl.setText("Batal");
            buttonControl.setOnMouseClicked((e) -> {
                //listener delete
                dataReservationDeleteHandle();
            });
            buttonControls.add(buttonControl);
        }
//        if (DashboardController.feature.getSelectedRoleFeature().getPrintData()) {
//            buttonControl = new JFXButton();
//            buttonControl.setText("Cetak");
//            buttonControl.setOnMouseClicked((e) -> {
//                //listener print
//                dataReservationPrintHandle();
//            });
//            buttonControls.add(buttonControl);
//        }
//        if (DashboardController.feature.getSelectedRoleFeature().getPrintData()) {
//            buttonControl = new JFXButton();
//            buttonControl.setText("Log-Book");
//            buttonControl.setOnMouseClicked((e) -> {
//                //listener log book
//                dataReservationLogBookHandle();
//            });
//            buttonControls.add(buttonControl);
//        }
        tableDataReservation.addButtonControl(buttonControls);
    }

    private ObservableList<TblReservation> loadAllDataReservation() {
        List<TblReservation> list = fReservationManager.getAllDataReservation();
        for (TblReservation data : list) {
            //data reservation status
            data.setRefReservationStatus(fReservationManager.getReservationStatus(data.getRefReservationStatus().getIdstatus()));
        }
        return FXCollections.observableArrayList(list);
    }

    /**
     * FORM INPUT RESERVATION
     */
    @FXML
    private AnchorPane formAnchor;

    @FXML
    private TabPane tabPaneFormLayout;

    @FXML
    private ScrollPane scrDataReservation;

    @FXML
    private Tab tabDataReservation;

    @FXML
    private Tab tabDataCustomer;

    @FXML
    private Tab tabReservationBill;

    @FXML
    private Tab tabCheckOutBill;

    @FXML
    private Tab tabRCOBill;

    //Data Reservation
    @FXML
    private GridPane gpFormDataReservation;

    //Data Customer
    @FXML
    private GridPane gpFormDataReservationCustomer;

//    @FXML
//    private JFXTextField txtReservationCode;
//
//    @FXML
//    private JFXTextField txtReservationStatus;
    @FXML
    private Label lblReservationCodeStatus;

    @FXML
    private JFXTextArea txtReservationNote;

    @FXML
    private JFXTextArea txtReservationCanceledReason;

    @FXML
    private JFXTextField txtCanceledDate;

    @FXML
    private JFXTextField txtCanceledTransactionNumber;

    @FXML
    private JFXButton btnPrintCanceledReceipt;

    @FXML
    private Separator sprtReservationCanceled;

//    @FXML
//    private JFXTextField txtCustomerDeposit;
//
//    @FXML
//    private JFXButton btnAddDeposit;
//
//    @FXML
//    private JFXButton btnRemoveDeposit;
    //Data Customer
//    @FXML
//    private JFXTextField txtCustomerCode;
    @FXML
    private AnchorPane ancCustomerIDLayout;
    private JFXCComboBoxTablePopup<TblCustomer> cbpCustomer;

    @FXML
    private AnchorPane ancBrokenItemInfoLayout;
    private final JFXCComboBoxPopup<TblReservationBrokenItem> cbpReservationBrokenItem = new JFXCComboBoxPopup<>(TblReservationBrokenItem.class);

//    private final JFXCComboBoxPopup cbpReservationBillTotalCostWithDiscount = new JFXCComboBoxPopup(null);
//
//    private final JFXCComboBoxPopup cbpReservationBillTotalCostWithoutDiscount = new JFXCComboBoxPopup(null);
//
//    private final JFXCComboBoxPopup cbpReservationBillTotalCostWithoutDiscountAndWithoutTax = new JFXCComboBoxPopup(null);
//
//    private final JFXCComboBoxPopup cbpCheckOutBillTotalCostWithDiscount = new JFXCComboBoxPopup(null);
//
//    private final JFXCComboBoxPopup cbpCheckOutBillTotalCostWithoutDiscount = new JFXCComboBoxPopup(null);
//
//    private final JFXCComboBoxPopup cbpCheckOutBillTotalCostWithoutDiscountAndWithoutTax = new JFXCComboBoxPopup(null);
    @FXML
    private ImageView imgPeople;

    @FXML
    private JFXTextField txtPeopleIndentityNumber;

//    @FXML
//    private JFXTextField txtKTPNumber;
//
//    @FXML
//    private JFXTextField txtPassportNumber;
//
//    @FXML
//    private JFXTextField txtSIMNumber;
    @FXML
    private JFXTextField txtPeopleName;

    @FXML
    private JFXTextField txtPeopleHPNumber;

    @FXML
    private JFXButton btnAddCustomer;

    @FXML
    private JFXButton btnUpdateCustomer;

    @FXML
    private JFXButton btnSaveDataReservation;

    //Data Bill (Reservation)
    @FXML
    private GridPane gpFormDataReservationBill;

//    @FXML
//    private JFXTextField txtTotalReservationRoom;
//    @FXML
//    private JFXTextField txtTotalReservationDiscount;
//    @FXML
//    private JFXTextField txtTotalAdditionalInReservationBill;
//    @FXML
//    private JFXTextField txtTotalServiceChargeInReservationBill;
    @FXML
    private Label lblServiceChargePercentageInReservationBill;

//    @FXML
//    private JFXTextField txtTotalTaxInReservationBill;
    @FXML
    private Label lblTaxPercentageInReservationBill;

//    @FXML
//    private JFXTextField txtTotalBillReservation;
//    private JFXTextField txtTotalRoomCostWithDiscountAndTaxR;
//
//    private JFXTextField txtTotalAdditionalServiceCostWithDiscountAndTaxR;
//
//    private JFXTextField txtTotalAdditionalItemCostWithDiscountAndTaxR;
//    @FXML
//    private JFXTextField txtTotalCostWithDiscountR;
    @FXML
    private JFXTextField txtTotalRoomCostR;

    @FXML
    private JFXTextField txtTotalRoomCostComplimentR;

    @FXML
    private JFXTextField txtTotalAdditionalCostR;

    @FXML
    private JFXTextField txtTotalDiscountR;

//    @FXML
//    private JFXTextField txtTotalCostAfterDiscountR;
//    private JFXTextField txtTotalRoomCostWithoutDiscountAndWithTaxR;
//
//    private JFXTextField txtTotalAdditionalServiceCostWithoutDiscountAndWithTaxR;
//
//    private JFXTextField txtTotalAdditionalItemCostWithoutDiscountAndWithTaxR;
//
//    @FXML
//    private JFXTextField txtTotalCostWithoutDiscountAndWithTaxR;
//    @FXML
//    private JFXTextField txtTotalCostWithTaxR;
    @FXML
    private JFXTextField txtTotalServiceChargeR;

    @FXML
    private JFXTextField txtTotalTaxR;

//    @FXML
//    private JFXTextField txtTotalCostAfterTaxR;
//
//    private JFXTextField txtTotalAdditionalItemWithoutDiscountAndWithoutTaxR;
//
//    @FXML
//    private JFXTextField txtTotalCostWithoutDiscountAndWithoutTaxR;
    @FXML
    private JFXTextField txtReservationBill;

    @FXML
    private Label lblRoundingValueInReservationBill;

    @FXML
    private JFXTextField txtTotalTransactionPaymentR;

//    @FXML
//    private JFXTextField txtTotalTransactionPaymentDiscountR;
    @FXML
    private JFXButton btnPrintReservationBill;

    @FXML
    private JFXButton btnPrintReservationReceipt;

    @FXML
    private AnchorPane ancTableTransactionReservationLayout;

    //Data Bill (CheckOut)
    @FXML
    private GridPane gpFormDataCheckOutBill;

//    @FXML
//    private JFXTextField txtTotalCheckOutDiscount;
//    @FXML
//    private JFXTextField txtTotalAdditionalInCheckOutBill;
//    @FXML
//    private JFXTextField txtTotalBrokenItemCostCO;
//    @FXML
//    private JFXTextField txtTotalServiceChargeInCheckOutBill;
    @FXML
    private Label lblServiceChargePercentageInCheckOutBill;

//    @FXML
//    private JFXTextField txtTotalTaxInCheckOutBill;
    @FXML
    private Label lblTaxPercentageInCheckOutBill;

//    @FXML
//    private JFXTextField txtTotalMissedCardChargeInCheckOutBill;
//    @FXML
//    private Label lblMissedCardNumberInCheckOutBill;
//    @FXML
//    private JFXTextField txtTotalBillCheckOut;
//    private JFXTextField txtTotalAdditionalServiceCostWithDiscountAndTaxCO;
//
//    private JFXTextField txtTotalAdditionalItemCostWithDiscountAndTaxCO;
//
//    @FXML
//    private JFXTextField txtTotalCostWithDiscountCO;
    @FXML
    private JFXTextField txtTotalAdditionalCostCO;

    @FXML
    private JFXTextField txtTotalDiscountCO;

//    @FXML
//    private JFXTextField txtTotalCostAfterDiscountCO;
//    private JFXTextField txtTotalAdditionalServiceCostWithoutDiscountAndWithTaxCO;
//
//    private JFXTextField txtTotalAdditionalItemCostWithoutDiscountAndWithTaxCO;
//
//    @FXML
//    private JFXTextField txtTotalCostWithoutDiscountCO;
    @FXML
    private JFXTextField txtTotalBrokenItemCostCO;

//    @FXML
//    private JFXTextField txtTotalCostWithTaxCO;
    @FXML
    private JFXTextField txtTotalServiceChargeCO;

    @FXML
    private JFXTextField txtTotalTaxCO;

//    @FXML
//    private JFXTextField txtTotalCostAfterTaxCO;
//    private JFXTextField txtTotalAdditionalItemWithoutDiscountAndWithoutTaxCO;
//
//    @FXML
//    private JFXTextField txtTotalCostWithoutDiscountAndWithoutTaxCO;
    @FXML
    private JFXTextField txtCheckOutBill;

    @FXML
    private Label lblRoundingValueInCheckOutBill;

    @FXML
    private JFXTextField txtTotalTransactionPaymentCO;

    @FXML
    private JFXTextField txtTotalTransactionPaymentDiscountCO;

    @FXML
    private JFXButton btnPrintCheckOutBill;

    @FXML
    private JFXButton btnPrintCheckOutReceipt;

    @FXML
    private JFXTextField txtUsedRoomCardNumberCONote;

    @FXML
    private JFXTextField txtMissedRoomCardNumberCONote;

    @FXML
    private JFXTextField txtReturnedRoomCardNumberCONote;

    @FXML
    private AnchorPane ancTableTransactionCheckOutLayout;

    //Data Bill (Reservation)
    @FXML
    private GridPane gpFormDataRCOBill;

    @FXML
    private Label lblServiceChargePercentageInRCOBill;

    @FXML
    private Label lblTaxPercentageInRCOBill;

    @FXML
    private JFXTextField txtTotalRoomCostRCO;

    @FXML
    private JFXTextField txtTotalRoomCostComplimentRCO;

    @FXML
    private JFXTextField txtTotalAdditionalCostRCO;

    @FXML
    private JFXTextField txtTotalDiscountRCO;

    @FXML
    private JFXTextField txtTotalBrokenItemCostRCO;

    @FXML
    private AnchorPane ancBrokenItemInfoRCOLayout;
    private final JFXCComboBoxPopup<TblReservationBrokenItem> cbpReservationBrokenItemRCO = new JFXCComboBoxPopup<>(TblReservationBrokenItem.class);

    @FXML
    private JFXTextField txtTotalServiceChargeRCO;

    @FXML
    private JFXTextField txtTotalTaxRCO;

    @FXML
    private JFXTextField txtRCOBill;

    @FXML
    private Label lblRoundingValueInRCOBill;

    @FXML
    private JFXTextField txtTotalTransactionPaymentRCO;

//    @FXML
//    private JFXButton btnPrintReservationBill;~~~
//
//    @FXML
//    private JFXButton btnPrintReservationReceipt;~~~
    @FXML
    private AnchorPane ancTableTransactionRCOLayout;

    //Data Reservation Room Type Detail
    @FXML
    private AnchorPane ancReservationRoomTypeDetailLayout;

    //Data Reservation Additional Items
    @FXML
    private AnchorPane ancReservationRoomAdditionalItemLayout;

    //Data Reservation Additional Services
    @FXML
    private AnchorPane ancReservationRoomAdditionalServiceLayout;

    @FXML
    public TitledPane titledPaneDiscount;

    @FXML
    public GridPane gpDiscount;

    //Discount Type - Reservation Bill
    @FXML
    public JFXButton btnReservationBillDiscountType;

    //Discount Type - CheckOut Bill
    @FXML
    public JFXButton btnCheckOutBillDiscountType;

    @FXML
    private JFXButton btnSave;

    @FXML
    private JFXButton btnCancel;

    public Time defaultCheckInTime;

    public Time defaultCheckOutTime;

    public TblReservation selectedData;

    public TblReservationBill dataReservationBill;

    public TblReservationBill dataCheckOutBill;

    public List<TblReservationRoomTypeDetail> dataReservationRoomTypeDetails = new ArrayList<>();

    public List<TblReservationAdditionalItem> dataReservationAdditionalItems = new ArrayList<>();

    public List<TblReservationAdditionalService> dataReservationAdditionalServices = new ArrayList<>();

    public List<TblReservationBrokenItem> dataReservationBrokenItems = new ArrayList<>();

//    public List<TblReservationBrokenItemPropertyBarcode> dataReservationBrokenItemPropertyBarcodes;
    public List<TblReservationPayment> dataReservationPayments = new ArrayList<>();

    public List<TblReservationPayment> dataCheckOutPayments = new ArrayList<>();

    public List<ReservationBillPaymentDetail> dataRCOPayments;

    public List<TblReservationPaymentWithTransfer> dataReservationPaymentWithTransfers;

    public List<TblReservationPaymentWithBankCard> dataReservationPaymentWithBankCards;

    public List<TblReservationPaymentWithCekGiro> dataReservationPaymentWithCekGiros;

    public List<TblReservationPaymentWithGuaranteePayment> dataReservationPaymentWithGuaranteePayments;

    public List<TblGuaranteeLetterItemDetail> dataGuaranteeLetterItemDetails;

    public List<TblReservationPaymentWithReservationVoucher> dataReservationPaymentWithReservationVouchers;

    public List<TblReservationRoomTypeDetailRoomPriceDetail> dataReservationRoomTypeDetailRoomPriceDetails;

    public List<TblReservationRoomTypeDetailTravelAgentDiscountDetail> dataReservationRoomTypeDetailTravelAgentDiscountDetails;

    private void initFormDataReservation() {
        //init data popup
        initDataPopup();
        //init form data reservation (customer)
        initFormDataReservationCustomer();
        //init form data bill and transaction reservation
        initFormDataBillTransactionReservation();
        //init form data bill and transaction checkout
        initFormDataBillTransactionCheckInOut();
        //init form data reservation room type detail
        initFormDataReservationRoomTypeDetail();
        //init form data reservation room additional
        initFormDataReservationRoomAdditional();

        btnReservationBillDiscountType.setTooltip(new Tooltip("Ubah (Data Tipe Diskon (Tagihan Reservasi))"));
        btnReservationBillDiscountType.setOnAction((e) -> {
            dataReservationBillDiscountTypeUpdateHandle();
        });

        btnCheckOutBillDiscountType.setTooltip(new Tooltip("Ubah (Data Tipe Diskon (Tagihan CheckOut))"));
        btnCheckOutBillDiscountType.setOnAction((e) -> {
            dataCheckOutBillDiscountTypeUpdateHandle();
        });

        btnSave.setTooltip(new Tooltip("Simpan (Data Reservasi)"));
        btnSave.setOnAction((e) -> {
            Alert alert = ClassMessage.showConfirmationSavingDataMessage("", null);
            if (alert.getResult() == ButtonType.OK) {
                dataReservationSaveHandle(0);
            }
        });

        btnSaveDataReservation.setTooltip(new Tooltip("Simpan (Data Reservasi)"));
        btnSaveDataReservation.setOnAction((e) -> {
            Alert alert = ClassMessage.showConfirmationSavingDataMessage("", null);
            if (alert.getResult() == ButtonType.OK) {
                dataReservationSaveHandle(1);
            }
        });

        btnPrintCanceledReceipt.setTooltip(new Tooltip("Cetak (Bukti Pengembalian Uang)"));
        btnPrintCanceledReceipt.setOnMouseClicked((e) -> {
            //print listener (canceling receipt)
            printDataType = "CancelingReceipt";
            prePrintHandle();
        });

        btnCancel.setTooltip(new Tooltip("Kembali/Batal"));
        btnCancel.setOnAction((e) -> {
            dataReservationCancelHandle();
        });

        btnPrintReservationBill.setTooltip(new Tooltip("Cetak (Tagihan Reservasi)"));
        btnPrintReservationBill.setOnAction((e) -> {
            printDataType = "ReservationBill";
            prePrintHandle();
        });

        btnPrintReservationReceipt.setTooltip(new Tooltip("Cetak (Bukti Pembayaran)"));
        btnPrintReservationReceipt.setOnAction((e) -> {
            printDataType = "ReservationReceipt";
            prePrintHandle();
        });

        btnPrintCheckOutBill.setTooltip(new Tooltip("Cetak (Tagihan CheckOut)"));
        btnPrintCheckOutBill.setOnAction((e) -> {
            printDataType = "CheckOutBill";
            prePrintHandle();
        });

        btnPrintCheckOutReceipt.setTooltip(new Tooltip("Cetak (Bukti Pembayaran)"));
        btnPrintCheckOutReceipt.setOnAction((e) -> {
            printDataType = "CheckOutReceipt";
            prePrintHandle();
        });

        initImportantFieldColor();

//        txtTotalTransactionPaymentDiscountR.setVisible(false);
        txtTotalTransactionPaymentDiscountCO.setVisible(false);
    }

    private void initImportantFieldColor() {
        ClassViewSetting.setImportantField(
                cbpCustomer);
    }

    private void initFormDataReservationCustomer() {
        //data reservation

        //data customer
        btnAddCustomer.setTooltip(new Tooltip("Tambah Data Customer"));
        btnAddCustomer.setOnMouseClicked((e) -> {
            dataCustomerCreateHandle();
        });

        //data customer
        btnUpdateCustomer.setTooltip(new Tooltip("Ubah Data Customer"));
        btnUpdateCustomer.setOnMouseClicked((e) -> {
            dataCustomerUpdateHandle();
        });

//        //data deposit
//        txtCustomerDeposit.setVisible(false);
//
//        //data deposit
//        btnAddDeposit.setTooltip(new Tooltip("Tambah Data Deposit"));
//        btnAddDeposit.setOnMouseClicked((e) -> {
//            dataDepositCreateHandle();
//        });
//        btnAddDeposit.setVisible(false);
//
//        //data deposit
//        btnRemoveDeposit.setTooltip(new Tooltip("Hapus Data Deposit"));
//        btnRemoveDeposit.setOnMouseClicked((e) -> {
//            dataDepositRemoveHandle();
//        });
//        btnRemoveDeposit.setVisible(false);
//        //data bill checkout (miss card)
//        txtTotalMissedCardChargeInCheckOutBill.setVisible(false);
//        lblMissedCardNumberInCheckOutBill.setVisible(false);
    }

    private void initFormDataBillTransactionReservation() {

    }

    private void initFormDataBillTransactionCheckInOut() {

    }

    private void initFormDataReservationRoomTypeDetail() {

    }

    private void initFormDataReservationRoomAdditional() {

    }

    private void initDataPopup() {
        //Customer
        TableView<TblCustomer> tableCustomer = new TableView<>();

        TableColumn<TblCustomer, String> codeCustomer = new TableColumn<>("ID Customer");
        codeCustomer.setCellValueFactory(cellData -> cellData.getValue().codeCustomerProperty());
        codeCustomer.setMinWidth(120);

        TableColumn<TblCustomer, String> identityNumber = new TableColumn("Nomor Identitas");
        identityNumber.setCellValueFactory((TableColumn.CellDataFeatures<TblCustomer, String> param)
                -> Bindings.createStringBinding(() -> param.getValue().getTblPeople().getCodePeople() + " ("
                        + (param.getValue().getTblPeople().getRefPeopleIdentifierType() != null ? param.getValue().getTblPeople().getRefPeopleIdentifierType().getTypeName() : "-") + ")",
                        param.getValue().tblPeopleProperty()));
        identityNumber.setMinWidth(180);

//        TableColumn<TblCustomer, String> ktpNumber = new TableColumn<>("Nomor KTP");
//        ktpNumber.setCellValueFactory((TableColumn.CellDataFeatures<TblCustomer, String> param)
//                -> Bindings.createStringBinding(() -> param.getValue().getTblPeople().getKtpnumber(), param.getValue().tblPeopleProperty()));
//        ktpNumber.setMinWidth(140);
//
//        TableColumn<TblCustomer, String> passportNumber = new TableColumn<>("Nomor Passport");
//        passportNumber.setCellValueFactory((TableColumn.CellDataFeatures<TblCustomer, String> param)
//                -> Bindings.createStringBinding(() -> param.getValue().getTblPeople().getPassportNumber(), param.getValue().tblPeopleProperty()));
//        passportNumber.setMinWidth(140);
//
//        TableColumn<TblCustomer, String> simNumber = new TableColumn<>("Nomor SIM");
//        simNumber.setCellValueFactory((TableColumn.CellDataFeatures<TblCustomer, String> param)
//                -> Bindings.createStringBinding(() -> param.getValue().getTblPeople().getSimnumber(), param.getValue().tblPeopleProperty()));
//        simNumber.setMinWidth(140);
        TableColumn<TblCustomer, String> name = new TableColumn<>("Nama Lengkap");
        name.setCellValueFactory((TableColumn.CellDataFeatures<TblCustomer, String> param)
                -> Bindings.createStringBinding(() -> param.getValue().getTblPeople().getFullName(), param.getValue().tblPeopleProperty()));
        name.setMinWidth(140);

        TableColumn<TblCustomer, String> hpNumber = new TableColumn<>("Nomor HP");
        hpNumber.setCellValueFactory((TableColumn.CellDataFeatures<TblCustomer, String> param)
                -> Bindings.createStringBinding(() -> param.getValue().getTblPeople().getHpnumber(), param.getValue().tblPeopleProperty()));
        hpNumber.setMinWidth(140);

        tableCustomer.getColumns().addAll(codeCustomer, identityNumber,
                //                ktpNumber, passportNumber, simNumber, 
                name, hpNumber);

        ObservableList<TblCustomer> customerItems = FXCollections.observableArrayList(fReservationManager.getAllDataCustomer());

        cbpCustomer = new JFXCComboBoxTablePopup<>(
                TblCustomer.class, tableCustomer, customerItems, "", "ID Customer *", true, 600, 400
        );

        //Reservation Broken Item
        TableView<TblReservationBrokenItem> tableBroken = new TableView<>();

        TableColumn<TblReservationBrokenItem, String> itemName = new TableColumn<>("Barang");
        itemName.setCellValueFactory((TableColumn.CellDataFeatures<TblReservationBrokenItem, String> param)
                -> Bindings.createStringBinding(() -> param.getValue().getTblItem().getItemName(), param.getValue().tblItemProperty()));
        itemName.setMinWidth(140);

        TableColumn<TblReservationBrokenItem, String> itemCharge = new TableColumn<>("Biaya Kerusakan");
        itemCharge.setCellValueFactory((TableColumn.CellDataFeatures<TblReservationBrokenItem, String> param)
                -> Bindings.createStringBinding(() -> ClassFormatter.currencyFormat.cFormat(param.getValue().getItemCharge()), param.getValue().itemChargeProperty()));
        itemCharge.setMinWidth(160);

        TableColumn<TblReservationBrokenItem, String> itemQuantity = new TableColumn<>("Jumlah");
        itemQuantity.setCellValueFactory((TableColumn.CellDataFeatures<TblReservationBrokenItem, String> param)
                -> Bindings.createStringBinding(() -> ClassFormatter.decimalFormat.cFormat(param.getValue().getItemQuantity()), param.getValue().itemQuantityProperty()));
        itemQuantity.setMinWidth(120);

        TableColumn<TblReservationBrokenItem, String> itemUnit = new TableColumn<>("Satuan");
        itemUnit.setCellValueFactory((TableColumn.CellDataFeatures<TblReservationBrokenItem, String> param)
                -> Bindings.createStringBinding(() -> param.getValue().getTblItem().getTblUnit().getUnitName(),
                        param.getValue().getTblItem().tblUnitProperty()));
        itemUnit.setMinWidth(120);

        TableColumn<TblReservationBrokenItem, String> totalCharge = new TableColumn<>("Total Biaya Kerusakan");
        totalCharge.setCellValueFactory((TableColumn.CellDataFeatures<TblReservationBrokenItem, String> param)
                -> Bindings.createStringBinding(() -> ClassFormatter.currencyFormat.cFormat(param.getValue().getItemCharge().multiply(param.getValue().getItemQuantity())),
                        param.getValue().itemQuantityProperty()));
        totalCharge.setMinWidth(180);

        tableBroken.getColumns().addAll(itemName, itemCharge, itemQuantity, itemUnit, totalCharge);

        ObservableList<TblReservationBrokenItem> brokenItems = FXCollections.observableArrayList();

        setReservationBrokenPopupInfo(cbpReservationBrokenItem, tableBroken, brokenItems);

        //Reservation Broken Item -RCO
        TableView<TblReservationBrokenItem> tableBrokenRCO = new TableView<>();

        TableColumn<TblReservationBrokenItem, String> itemNameRCO = new TableColumn<>("Barang");
        itemNameRCO.setCellValueFactory((TableColumn.CellDataFeatures<TblReservationBrokenItem, String> param)
                -> Bindings.createStringBinding(() -> param.getValue().getTblItem().getItemName(), param.getValue().tblItemProperty()));
        itemNameRCO.setMinWidth(140);

        TableColumn<TblReservationBrokenItem, String> itemChargeRCO = new TableColumn<>("Biaya Kerusakan");
        itemChargeRCO.setCellValueFactory((TableColumn.CellDataFeatures<TblReservationBrokenItem, String> param)
                -> Bindings.createStringBinding(() -> ClassFormatter.currencyFormat.cFormat(param.getValue().getItemCharge()), param.getValue().itemChargeProperty()));
        itemChargeRCO.setMinWidth(160);

        TableColumn<TblReservationBrokenItem, String> itemQuantityRCO = new TableColumn<>("Jumlah");
        itemQuantityRCO.setCellValueFactory((TableColumn.CellDataFeatures<TblReservationBrokenItem, String> param)
                -> Bindings.createStringBinding(() -> ClassFormatter.decimalFormat.cFormat(param.getValue().getItemQuantity()), param.getValue().itemQuantityProperty()));
        itemQuantityRCO.setMinWidth(120);

        TableColumn<TblReservationBrokenItem, String> itemUnitRCO = new TableColumn<>("Satuan");
        itemUnitRCO.setCellValueFactory((TableColumn.CellDataFeatures<TblReservationBrokenItem, String> param)
                -> Bindings.createStringBinding(() -> param.getValue().getTblItem().getTblUnit().getUnitName(),
                        param.getValue().getTblItem().tblUnitProperty()));
        itemUnitRCO.setMinWidth(120);

        TableColumn<TblReservationBrokenItem, String> totalChargeRCO = new TableColumn<>("Total Biaya Kerusakan");
        totalChargeRCO.setCellValueFactory((TableColumn.CellDataFeatures<TblReservationBrokenItem, String> param)
                -> Bindings.createStringBinding(() -> ClassFormatter.currencyFormat.cFormat(param.getValue().getItemCharge().multiply(param.getValue().getItemQuantity())),
                        param.getValue().itemQuantityProperty()));
        totalChargeRCO.setMinWidth(180);

        tableBrokenRCO.getColumns().addAll(itemNameRCO, itemChargeRCO, itemQuantityRCO, itemUnitRCO, totalChargeRCO);

        ObservableList<TblReservationBrokenItem> brokenItemRCOs = FXCollections.observableArrayList();

        setReservationBrokenPopupInfo(cbpReservationBrokenItemRCO, tableBrokenRCO, brokenItemRCOs);

//        //Reservation Bill - 'Total Harga Kena Diskon'
//        setReservationBillTotalCostWithDiscount(cbpReservationBillTotalCostWithDiscount);
//
//        //Reservation Bill - 'Total Harga Tidak Kena Diskon'
//        setReservationBillTotalCostWithoutDiscount(cbpReservationBillTotalCostWithoutDiscount);
//
//        //Reservation Bill - 'Total Harga Tidak Kena Diskon dan Tidak Kena Pajak'
//        setReservationBillTotalCostWithoutDiscountAndWithoutTax(cbpReservationBillTotalCostWithoutDiscountAndWithoutTax);
//
//        //Check Out Bill - 'Total Harga Kena Diskon'
//        setCheckOutBillTotalCostWithDiscount(cbpCheckOutBillTotalCostWithDiscount);
//
//        //Check Out Bill - 'Total Harga Tidak Kena Diskon'
//        setCheckOutBillTotalCostWithoutDiscount(cbpCheckOutBillTotalCostWithoutDiscount);
//
//        //Check Out Bill - 'Total Harga Tidak Kena Diskon dan Tidak Kena Pajak'
//        setCheckOutBillTotalCostWithoutDiscountAndWithoutTax(cbpCheckOutBillTotalCostWithoutDiscountAndWithoutTax);
        //attached to grid-pane
        ancCustomerIDLayout.getChildren().clear();
        AnchorPane.setBottomAnchor(cbpCustomer, 0.0);
        AnchorPane.setLeftAnchor(cbpCustomer, 0.0);
        AnchorPane.setRightAnchor(cbpCustomer, 0.0);
        AnchorPane.setTopAnchor(cbpCustomer, 0.0);
        ancCustomerIDLayout.getChildren().add(cbpCustomer);
        ancBrokenItemInfoLayout.getChildren().clear();
        AnchorPane.setBottomAnchor(cbpReservationBrokenItem, 0.0);
        AnchorPane.setLeftAnchor(cbpReservationBrokenItem, 0.0);
        AnchorPane.setRightAnchor(cbpReservationBrokenItem, 0.0);
        AnchorPane.setTopAnchor(cbpReservationBrokenItem, 0.0);
        ancBrokenItemInfoLayout.getChildren().add(cbpReservationBrokenItem);

        ancBrokenItemInfoRCOLayout.getChildren().clear();
        AnchorPane.setBottomAnchor(cbpReservationBrokenItemRCO, 0.0);
        AnchorPane.setLeftAnchor(cbpReservationBrokenItemRCO, 0.0);
        AnchorPane.setRightAnchor(cbpReservationBrokenItemRCO, 0.0);
        AnchorPane.setTopAnchor(cbpReservationBrokenItemRCO, 0.0);
        ancBrokenItemInfoRCOLayout.getChildren().add(cbpReservationBrokenItemRCO);

//        gpFormDataReservationBill.add(cbpReservationBillTotalCostWithDiscount, 3, 1);
//        gpFormDataReservationBill.add(cbpReservationBillTotalCostWithoutDiscount, 3, 3);
//        gpFormDataReservationBill.add(cbpReservationBillTotalCostWithoutDiscountAndWithoutTax, 3, 6);
//
//        gpFormDataCheckOutBill.add(cbpCheckOutBillTotalCostWithDiscount, 3, 1);
//        gpFormDataCheckOutBill.add(cbpCheckOutBillTotalCostWithoutDiscount, 3, 3);
//        gpFormDataCheckOutBill.add(cbpCheckOutBillTotalCostWithoutDiscountAndWithoutTax, 3, 7);
    }

    public void refreshDataPopup() {
        //Customer
        List<TblCustomer> dataCustomers = fReservationManager.getAllDataCustomer();
        for (TblCustomer data : dataCustomers) {
            data.setTblPeople(fReservationManager.getPeople(data.getTblPeople().getIdpeople()));
        }
        ObservableList<TblCustomer> customerItems = FXCollections.observableArrayList(dataCustomers);
        cbpCustomer.setItems(customerItems);

        //Reservation Broken Item
        ObservableList<TblReservationBrokenItem> brokenItems = FXCollections.observableArrayList(dataReservationBrokenItems);
        cbpReservationBrokenItem.setItems(brokenItems);

        //Reservation Broken Item - RCO
        ObservableList<TblReservationBrokenItem> brokenItemRCOs = FXCollections.observableArrayList(dataReservationBrokenItems);
        cbpReservationBrokenItemRCO.setItems(brokenItemRCOs);
    }

    private void setReservationBrokenPopupInfo(JFXCComboBoxPopup cbp,
            TableView table,
            ObservableList items) {

        table.setItems(items);

        cbp.setPropertyNameForFiltered("idbroken");

        cbp.setItems(items);

        // Add observable list data to the table
        table.itemsProperty().bind(cbp.filteredItemsProperty());

        //popup button
        JFXButton button = new JFXButton(" ");
        button.setOnMouseClicked((e) -> cbp.show());

        button.setPrefSize(30, 30);
        button.setButtonType(JFXButton.ButtonType.RAISED);
        button.getStyleClass().add("button-view");
        button.setTooltip(new Tooltip("Data Barang Rusak"));

        //popup content
        BorderPane content = new BorderPane(new JFXButton("SHOW"));
        content.setBackground(new Background(new BackgroundFill(Color.WHITE, CornerRadii.EMPTY, Insets.EMPTY)));
        content.setPrefSize(740, 250);

        content.setCenter(table);

        cbp.setPopupEditor(false);
        cbp.setLabelFloat(false);
        cbp.setPopupButton(button);
        cbp.settArrowButton(true);
        cbp.setPopupContent(content);

    }

//    private void setReservationBillTotalCostWithDiscount(JFXCComboBoxPopup cbp) {
//
//        //popup button
//        JFXButton button = new JFXButton(" ");
//        button.setOnMouseClicked((e) -> cbp.show());
//
//        button.setPrefSize(30, 30);
//        button.setButtonType(JFXButton.ButtonType.RAISED);
//        button.getStyleClass().add("button-view");
//        button.setTooltip(new Tooltip("Detail Data"));
//
//        //content
//        AnchorPane ancPane = new AnchorPane();
//        GridPane gp = new GridPane();
//        AnchorPane.setBottomAnchor(gp, 20.0);
//        AnchorPane.setLeftAnchor(gp, 20.);
//        AnchorPane.setRightAnchor(gp, 20.);
//        AnchorPane.setTopAnchor(gp, 20.);
//        ancPane.getChildren().add(gp);
//
//        txtTotalRoomCostWithDiscountAndTaxR = new JFXTextField();
//        txtTotalRoomCostWithDiscountAndTaxR.setPromptText("Total Harga Kamar Kena Diskon");
//        txtTotalRoomCostWithDiscountAndTaxR.setLabelFloat(true);
//        txtTotalRoomCostWithDiscountAndTaxR.setEditable(false);
//        txtTotalRoomCostWithDiscountAndTaxR.setDisable(true);
//
//        txtTotalAdditionalServiceCostWithDiscountAndTaxR = new JFXTextField();
//        txtTotalAdditionalServiceCostWithDiscountAndTaxR.setPromptText("Total Harga Tambahan Layanan Kena Diskon");
//        txtTotalAdditionalServiceCostWithDiscountAndTaxR.setLabelFloat(true);
//        txtTotalAdditionalServiceCostWithDiscountAndTaxR.setEditable(false);
//        txtTotalAdditionalServiceCostWithDiscountAndTaxR.setDisable(true);
//
//        txtTotalAdditionalItemCostWithDiscountAndTaxR = new JFXTextField();
//        txtTotalAdditionalItemCostWithDiscountAndTaxR.setPromptText("Total Harga Tambahan Barang Kena Diskon");
//        txtTotalAdditionalItemCostWithDiscountAndTaxR.setLabelFloat(true);
//        txtTotalAdditionalItemCostWithDiscountAndTaxR.setEditable(false);
//        txtTotalAdditionalItemCostWithDiscountAndTaxR.setDisable(true);
//
//        gp.setHgap(15);
//        gp.setVgap(20);
//
//        gp.getColumnConstraints().add(new ColumnConstraints(375));
//
//        gp.getRowConstraints().add(new RowConstraints(30));
//        gp.getRowConstraints().add(new RowConstraints(30));
//        gp.getRowConstraints().add(new RowConstraints(30));
//
//        gp.add(txtTotalRoomCostWithDiscountAndTaxR, 0, 0);
//        gp.add(txtTotalAdditionalServiceCostWithDiscountAndTaxR, 0, 1);
//        gp.add(txtTotalAdditionalItemCostWithDiscountAndTaxR, 0, 2);
//
//        //popup content
//        BorderPane content = new BorderPane(new JFXButton("SHOW"));
//        content.setBackground(new Background(new BackgroundFill(Color.WHITE, CornerRadii.EMPTY, Insets.EMPTY)));
//        content.setPrefSize(415, 170);
//
//        content.setCenter(ancPane);
//
//        cbp.setPopupEditor(false);
//        cbp.setLabelFloat(false);
//        cbp.setPopupButton(button);
//        cbp.settArrowButton(true);
//        cbp.setPopupContent(content);
//    }
//
//    private void setReservationBillTotalCostWithoutDiscount(JFXCComboBoxPopup cbp) {
//        //popup button
//        JFXButton button = new JFXButton(" ");
//        button.setOnMouseClicked((e) -> cbp.show());
//
//        button.setPrefSize(30, 30);
//        button.setButtonType(JFXButton.ButtonType.RAISED);
//        button.getStyleClass().add("button-view");
//        button.setTooltip(new Tooltip("Detail Data"));
//
//        //content
//        AnchorPane ancPane = new AnchorPane();
//        GridPane gp = new GridPane();
//        AnchorPane.setBottomAnchor(gp, 20.0);
//        AnchorPane.setLeftAnchor(gp, 20.);
//        AnchorPane.setRightAnchor(gp, 20.);
//        AnchorPane.setTopAnchor(gp, 20.);
//        ancPane.getChildren().add(gp);
//
//        txtTotalRoomCostWithoutDiscountAndWithTaxR = new JFXTextField();
//        txtTotalRoomCostWithoutDiscountAndWithTaxR.setPromptText("Total Harga Kamar Tidak Kena Diskon");
//        txtTotalRoomCostWithoutDiscountAndWithTaxR.setLabelFloat(true);
//        txtTotalRoomCostWithoutDiscountAndWithTaxR.setEditable(false);
//        txtTotalRoomCostWithoutDiscountAndWithTaxR.setDisable(true);
//
//        txtTotalAdditionalServiceCostWithoutDiscountAndWithTaxR = new JFXTextField();
//        txtTotalAdditionalServiceCostWithoutDiscountAndWithTaxR.setPromptText("Total Harga Tambahan Layanan Tidak Kena Diskon");
//        txtTotalAdditionalServiceCostWithoutDiscountAndWithTaxR.setLabelFloat(true);
//        txtTotalAdditionalServiceCostWithoutDiscountAndWithTaxR.setEditable(false);
//        txtTotalAdditionalServiceCostWithoutDiscountAndWithTaxR.setDisable(true);
//
//        txtTotalAdditionalItemCostWithoutDiscountAndWithTaxR = new JFXTextField();
//        txtTotalAdditionalItemCostWithoutDiscountAndWithTaxR.setPromptText("Total Harga Tambahan Barang Tidak Kena Diskon");
//        txtTotalAdditionalItemCostWithoutDiscountAndWithTaxR.setLabelFloat(true);
//        txtTotalAdditionalItemCostWithoutDiscountAndWithTaxR.setEditable(false);
//        txtTotalAdditionalItemCostWithoutDiscountAndWithTaxR.setDisable(true);
//
//        gp.setHgap(15);
//        gp.setVgap(20);
//
//        gp.getColumnConstraints().add(new ColumnConstraints(375));
//
//        gp.getRowConstraints().add(new RowConstraints(30));
//        gp.getRowConstraints().add(new RowConstraints(30));
//        gp.getRowConstraints().add(new RowConstraints(30));
//
//        gp.add(txtTotalRoomCostWithoutDiscountAndWithTaxR, 0, 0);
//        gp.add(txtTotalAdditionalServiceCostWithoutDiscountAndWithTaxR, 0, 1);
//        gp.add(txtTotalAdditionalItemCostWithoutDiscountAndWithTaxR, 0, 2);
//
//        //popup content
//        BorderPane content = new BorderPane(new JFXButton("SHOW"));
//        content.setBackground(new Background(new BackgroundFill(Color.WHITE, CornerRadii.EMPTY, Insets.EMPTY)));
//        content.setPrefSize(415, 170);
//
//        content.setCenter(ancPane);
//
//        cbp.setPopupEditor(false);
//        cbp.setLabelFloat(false);
//        cbp.setPopupButton(button);
//        cbp.settArrowButton(true);
//        cbp.setPopupContent(content);
//
//    }
//
//    private void setReservationBillTotalCostWithoutDiscountAndWithoutTax(JFXCComboBoxPopup cbp) {
//        //popup button
//        JFXButton button = new JFXButton(" ");
//        button.setOnMouseClicked((e) -> cbp.show());
//
//        button.setPrefSize(30, 30);
//        button.setButtonType(JFXButton.ButtonType.RAISED);
//        button.getStyleClass().add("button-view");
//        button.setTooltip(new Tooltip("Detail Data"));
//
//        //content
//        AnchorPane ancPane = new AnchorPane();
//        GridPane gp = new GridPane();
//        AnchorPane.setBottomAnchor(gp, 20.0);
//        AnchorPane.setLeftAnchor(gp, 20.0);
//        AnchorPane.setRightAnchor(gp, 20.0);
//        AnchorPane.setTopAnchor(gp, 20.0);
//        ancPane.getChildren().add(gp);
//
//        txtTotalAdditionalItemWithoutDiscountAndWithoutTaxR = new JFXTextField();
//        txtTotalAdditionalItemWithoutDiscountAndWithoutTaxR.setPromptText("Total Harga Tambahan Barang Tidak Kena Diskon dan Tidak Kena Pajak");
//        txtTotalAdditionalItemWithoutDiscountAndWithoutTaxR.setLabelFloat(true);
//        txtTotalAdditionalItemWithoutDiscountAndWithoutTaxR.setEditable(false);
//        txtTotalAdditionalItemWithoutDiscountAndWithoutTaxR.setDisable(true);
//
//        gp.setHgap(15);
//        gp.setVgap(20);
//
//        gp.getColumnConstraints().add(new ColumnConstraints(375));
//
//        gp.getRowConstraints().add(new RowConstraints(30));
//
//        gp.add(txtTotalAdditionalItemWithoutDiscountAndWithoutTaxR, 0, 0);
//
//        //popup content
//        BorderPane content = new BorderPane(new JFXButton("SHOW"));
//        content.setBackground(new Background(new BackgroundFill(Color.WHITE, CornerRadii.EMPTY, Insets.EMPTY)));
//        content.setPrefSize(415, 70);
//
//        content.setCenter(ancPane);
//
//        cbp.setPopupEditor(false);
//        cbp.setLabelFloat(false);
//        cbp.setPopupButton(button);
//        cbp.settArrowButton(true);
//        cbp.setPopupContent(content);
//    }
//
//    private void setCheckOutBillTotalCostWithDiscount(JFXCComboBoxPopup cbp) {
//
//        //popup button
//        JFXButton button = new JFXButton(" ");
//        button.setOnMouseClicked((e) -> cbp.show());
//
//        button.setPrefSize(30, 30);
//        button.setButtonType(JFXButton.ButtonType.RAISED);
//        button.getStyleClass().add("button-view");
//        button.setTooltip(new Tooltip("Detail Data"));
//
//        //content
//        AnchorPane ancPane = new AnchorPane();
//        GridPane gp = new GridPane();
//        AnchorPane.setBottomAnchor(gp, 20.0);
//        AnchorPane.setLeftAnchor(gp, 20.);
//        AnchorPane.setRightAnchor(gp, 20.);
//        AnchorPane.setTopAnchor(gp, 20.);
//        ancPane.getChildren().add(gp);
//
//        txtTotalAdditionalServiceCostWithDiscountAndTaxCO = new JFXTextField();
//        txtTotalAdditionalServiceCostWithDiscountAndTaxCO.setPromptText("Total Harga Tambahan Layanan Kena Diskon");
//        txtTotalAdditionalServiceCostWithDiscountAndTaxCO.setLabelFloat(true);
//        txtTotalAdditionalServiceCostWithDiscountAndTaxCO.setEditable(false);
//        txtTotalAdditionalServiceCostWithDiscountAndTaxCO.setDisable(true);
//
//        txtTotalAdditionalItemCostWithDiscountAndTaxCO = new JFXTextField();
//        txtTotalAdditionalItemCostWithDiscountAndTaxCO.setPromptText("Total Harga Tambahan Barang Kena Diskon");
//        txtTotalAdditionalItemCostWithDiscountAndTaxCO.setLabelFloat(true);
//        txtTotalAdditionalItemCostWithDiscountAndTaxCO.setEditable(false);
//        txtTotalAdditionalItemCostWithDiscountAndTaxCO.setDisable(true);
//
//        gp.setHgap(15);
//        gp.setVgap(20);
//
//        gp.getColumnConstraints().add(new ColumnConstraints(375));
//
//        gp.getRowConstraints().add(new RowConstraints(30));
//        gp.getRowConstraints().add(new RowConstraints(30));
//
//        gp.add(txtTotalAdditionalServiceCostWithDiscountAndTaxCO, 0, 0);
//        gp.add(txtTotalAdditionalItemCostWithDiscountAndTaxCO, 0, 1);
//
//        //popup content
//        BorderPane content = new BorderPane(new JFXButton("SHOW"));
//        content.setBackground(new Background(new BackgroundFill(Color.WHITE, CornerRadii.EMPTY, Insets.EMPTY)));
//        content.setPrefSize(415, 120);
//
//        content.setCenter(ancPane);
//
//        cbp.setPopupEditor(false);
//        cbp.setLabelFloat(false);
//        cbp.setPopupButton(button);
//        cbp.settArrowButton(true);
//        cbp.setPopupContent(content);
//    }
//
//    private void setCheckOutBillTotalCostWithoutDiscount(JFXCComboBoxPopup cbp) {
//        //popup button
//        JFXButton button = new JFXButton(" ");
//        button.setOnMouseClicked((e) -> cbp.show());
//
//        button.setPrefSize(30, 30);
//        button.setButtonType(JFXButton.ButtonType.RAISED);
//        button.getStyleClass().add("button-view");
//        button.setTooltip(new Tooltip("Detail Data"));
//
//        //content
//        AnchorPane ancPane = new AnchorPane();
//        GridPane gp = new GridPane();
//        AnchorPane.setBottomAnchor(gp, 20.0);
//        AnchorPane.setLeftAnchor(gp, 20.);
//        AnchorPane.setRightAnchor(gp, 20.);
//        AnchorPane.setTopAnchor(gp, 20.);
//        ancPane.getChildren().add(gp);
//
//        txtTotalAdditionalServiceCostWithoutDiscountAndWithTaxCO = new JFXTextField();
//        txtTotalAdditionalServiceCostWithoutDiscountAndWithTaxCO.setPromptText("Total Harga Tambahan Layanan Tidak Kena Diskon");
//        txtTotalAdditionalServiceCostWithoutDiscountAndWithTaxCO.setLabelFloat(true);
//        txtTotalAdditionalServiceCostWithoutDiscountAndWithTaxCO.setEditable(false);
//        txtTotalAdditionalServiceCostWithoutDiscountAndWithTaxCO.setDisable(true);
//
//        txtTotalAdditionalItemCostWithoutDiscountAndWithTaxCO = new JFXTextField();
//        txtTotalAdditionalItemCostWithoutDiscountAndWithTaxCO.setPromptText("Total Harga Tambahan Barang Tidak Kena Diskon");
//        txtTotalAdditionalItemCostWithoutDiscountAndWithTaxCO.setLabelFloat(true);
//        txtTotalAdditionalItemCostWithoutDiscountAndWithTaxCO.setEditable(false);
//        txtTotalAdditionalItemCostWithoutDiscountAndWithTaxCO.setDisable(true);
//
//        gp.setHgap(15);
//        gp.setVgap(20);
//
//        gp.getColumnConstraints().add(new ColumnConstraints(375));
//
//        gp.getRowConstraints().add(new RowConstraints(30));
//        gp.getRowConstraints().add(new RowConstraints(30));
//
//        gp.add(txtTotalAdditionalServiceCostWithoutDiscountAndWithTaxCO, 0, 0);
//        gp.add(txtTotalAdditionalItemCostWithoutDiscountAndWithTaxCO, 0, 1);
//
//        //popup content
//        BorderPane content = new BorderPane(new JFXButton("SHOW"));
//        content.setBackground(new Background(new BackgroundFill(Color.WHITE, CornerRadii.EMPTY, Insets.EMPTY)));
//        content.setPrefSize(415, 120);
//
//        content.setCenter(ancPane);
//
//        cbp.setPopupEditor(false);
//        cbp.setLabelFloat(false);
//        cbp.setPopupButton(button);
//        cbp.settArrowButton(true);
//        cbp.setPopupContent(content);
//
//    }
//
//    private void setCheckOutBillTotalCostWithoutDiscountAndWithoutTax(JFXCComboBoxPopup cbp) {
//        //popup button
//        JFXButton button = new JFXButton(" ");
//        button.setOnMouseClicked((e) -> cbp.show());
//
//        button.setPrefSize(30, 30);
//        button.setButtonType(JFXButton.ButtonType.RAISED);
//        button.getStyleClass().add("button-view");
//        button.setTooltip(new Tooltip("Detail Data"));
//
//        //content
//        AnchorPane ancPane = new AnchorPane();
//        GridPane gp = new GridPane();
//        AnchorPane.setBottomAnchor(gp, 20.0);
//        AnchorPane.setLeftAnchor(gp, 20.);
//        AnchorPane.setRightAnchor(gp, 20.);
//        AnchorPane.setTopAnchor(gp, 20.);
//        ancPane.getChildren().add(gp);
//
//        txtTotalAdditionalItemWithoutDiscountAndWithoutTaxCO = new JFXTextField();
//        txtTotalAdditionalItemWithoutDiscountAndWithoutTaxCO.setPromptText("Total Harga Tambahan Barang Tidak Kena Diskon dan Tidak Kena Pajak");
//        txtTotalAdditionalItemWithoutDiscountAndWithoutTaxCO.setLabelFloat(true);
//        txtTotalAdditionalItemWithoutDiscountAndWithoutTaxCO.setEditable(false);
//        txtTotalAdditionalItemWithoutDiscountAndWithoutTaxCO.setDisable(true);
//
//        gp.setHgap(15);
//        gp.setVgap(20);
//
//        gp.getColumnConstraints().add(new ColumnConstraints(375));
//
//        gp.getRowConstraints().add(new RowConstraints(30));
//
//        gp.add(txtTotalAdditionalItemWithoutDiscountAndWithoutTaxCO, 0, 0);
//
//        //popup content
//        BorderPane content = new BorderPane(new JFXButton("SHOW"));
//        content.setBackground(new Background(new BackgroundFill(Color.WHITE, CornerRadii.EMPTY, Insets.EMPTY)));
//        content.setPrefSize(415, 70);
//
//        content.setCenter(ancPane);
//
//        cbp.setPopupEditor(false);
//        cbp.setLabelFloat(false);
//        cbp.setPopupButton(button);
//        cbp.settArrowButton(true);
//        cbp.setPopupContent(content);
//    }
    private void setSelectedDataToInputForm() {
        //tab setting position
        if (dataInputStatus == 0) {   //insert
            tabPaneFormLayout.getTabs().clear();
            tabDataCustomer.setText("Data Customer & Keterangan");
            tabPaneFormLayout.getTabs().addAll(tabDataReservation, tabDataCustomer);
        } else {  //another
            if (tabPaneFormLayout.getTabs().size() != 4
                    || !tabDataCustomer.getText().equals("Data Customer & Keterangan")) {
                tabPaneFormLayout.getTabs().clear();
                tabDataCustomer.setText("Data Customer & Keterangan");
                tabPaneFormLayout.getTabs().addAll(tabDataReservation, tabReservationBill, tabCheckOutBill, tabRCOBill, tabDataCustomer);
//                tabPaneFormLayout.getTabs().addAll(tabDataReservation, tabReservationBill, tabCheckOutBill, tabDataCustomer);
            }
        }

        //titled pane - discount - closed
        titledPaneDiscount.setExpanded(false);

        //button save (show-setting)
        btnSave.setVisible(dataInputStatus == 0);
        btnSaveDataReservation.setVisible(dataInputStatus == 1);

        //button reservattion/check-out bill discount type (info)
        btnReservationBillDiscountType.setText("Diskon (Reservasi) : "
                + (dataReservationBill.getTblBankCard() != null
                        ? "Kartu (" + dataReservationBill.getTblBankCard().getBankCardName() + ")"
                        : (dataReservationBill.getRefReservationBillDiscountType() != null
                                ? dataReservationBill.getRefReservationBillDiscountType().getTypeName()
                                : "-")));
        btnCheckOutBillDiscountType.setText("Diskon (Check Out) : " + (dataCheckOutBill.getTblBankCard() != null
                ? "Kartu (" + dataCheckOutBill.getTblBankCard().getBankCardName() + ")"
                : (dataCheckOutBill.getRefReservationBillDiscountType() != null
                        ? dataCheckOutBill.getRefReservationBillDiscountType().getTypeName()
                        : "-")));
        refreshDataPopup();

        //data reservation
        if (selectedData.getCodeReservation() != null) {
            lblReservationCodeStatus.setText(selectedData.getCodeReservation()
                    + " - " + selectedData.getTblCustomer().getTblPeople().getFullName()
                    + " : " + selectedData.getRefReservationStatus().getStatusName());
        } else {
            lblReservationCodeStatus.setText("");
        }
        selectedData.codeReservationProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                lblReservationCodeStatus.setText(selectedData.getCodeReservation()
                        + " - " + selectedData.getTblCustomer().getTblPeople().getFullName()
                        + " : " + selectedData.getRefReservationStatus().getStatusName());
            } else {
                lblReservationCodeStatus.setText("");
            }
        });
//        txtReservationCode.textProperty().bind(selectedData.codeReservationProperty());
//        txtReservationStatus.textProperty().bind(selectedData.getRefReservationStatus().statusNameProperty());
        txtReservationNote.textProperty().bindBidirectional(selectedData.noteProperty());
        if (selectedData.getRefReservationStatus() != null
                && selectedData.getRefReservationStatus().getIdstatus() == 6) {  //Canceled = '6'
            TblReservationCanceled dataCanceled = fReservationManager.getDataReservationCanceledByIDReservation(selectedData.getIdreservation());
            if (dataCanceled != null) {
                txtCanceledDate.setText(ClassFormatter.dateTimeFormate.format(dataCanceled.getCancelDateTime()));
                TblReservationPayment dataCanceledTransaction = fReservationManager.getDataReservationPaymentByIDReservationAndIDReservationBillType(
                        selectedData.getIdreservation(),
                        3); //Canceling Fee = '3'
                if (dataCanceledTransaction != null) {
                    txtCanceledTransactionNumber.setText(dataCanceledTransaction.getCodePayment());
                } else {
                    txtCanceledTransactionNumber.setText("");
                }
                txtReservationCanceledReason.setText(dataCanceled.getCancelNote());
            } else {
                txtCanceledDate.setText("");
                txtCanceledTransactionNumber.setText("");
                txtReservationCanceledReason.setText("");
            }
            txtCanceledDate.setVisible(true);
            txtCanceledTransactionNumber.setVisible(true);
            btnPrintCanceledReceipt.setVisible(true);
            txtReservationCanceledReason.setVisible(true);
            sprtReservationCanceled.setVisible(true);
        } else {
            txtCanceledDate.setVisible(false);
            txtCanceledTransactionNumber.setVisible(false);
            btnPrintCanceledReceipt.setVisible(false);
            txtReservationCanceledReason.setVisible(false);
            sprtReservationCanceled.setVisible(false);
        }

        //data reservation (customer)
        imgPeople.setPreserveRatio(false);
        if (selectedData.getTblCustomer().getTblPeople().getImageUrl() == null
                || selectedData.getTblCustomer().getTblPeople().getImageUrl().equals("")) {
            String imgSourcePath = ClassFolderManager.imageSystemRootPath + "/" + "no_profile_img.gif";
            imgPeople.setImage(new Image("file:///" + imgSourcePath));
        } else {
            String imgSourcePath = ClassFolderManager.imageClientRootPath + "/" + selectedData.getTblCustomer().getTblPeople().getImageUrl();
            imgPeople.setImage(new Image("file:///" + imgSourcePath));
        }
        txtPeopleIndentityNumber.setText(selectedData.getTblCustomer().getTblPeople().getCodePeople());
//        txtKTPNumber.setText(selectedData.getTblCustomer().getTblPeople().getKtpnumber());
//        txtPassportNumber.setText(selectedData.getTblCustomer().getTblPeople().getPassportNumber());
//        txtSIMNumber.setText(selectedData.getTblCustomer().getTblPeople().getSimnumber());
        txtPeopleName.setText(selectedData.getTblCustomer().getTblPeople().getFullName());
        txtPeopleHPNumber.setText(selectedData.getTblCustomer().getTblPeople().getHpnumber());
//        txtCustomerDeposit.setText(ClassFormatter.currencyFormat.cFormat(selectedData.getTblCustomer().getDeposit()));

        cbpCustomer.valueProperty().bindBidirectional(selectedData.tblCustomerProperty());

        selectedData.tblCustomerProperty().addListener((obs, oldVal, newVal) -> {
            imgPeople.setPreserveRatio(false);
            if (selectedData.getTblCustomer().getTblPeople().getImageUrl() == null
                    || selectedData.getTblCustomer().getTblPeople().getImageUrl().equals("")) {
                String imgSourcePath = ClassFolderManager.imageSystemRootPath + "/" + "no_profile_img.gif";
                imgPeople.setImage(new Image("file:///" + imgSourcePath));
            } else {
                String imgSourcePath = ClassFolderManager.imageClientRootPath + "/" + selectedData.getTblCustomer().getTblPeople().getImageUrl();
                imgPeople.setImage(new Image("file:///" + imgSourcePath));
            }
            txtPeopleIndentityNumber.setText(selectedData.getTblCustomer().getTblPeople().getCodePeople());
//            txtKTPNumber.setText(selectedData.getTblCustomer().getTblPeople().getKtpnumber());
//            txtPassportNumber.setText(selectedData.getTblCustomer().getTblPeople().getPassportNumber());
//            txtSIMNumber.setText(selectedData.getTblCustomer().getTblPeople().getSimnumber());
            txtPeopleName.setText(selectedData.getTblCustomer().getTblPeople().getFullName());
            txtPeopleHPNumber.setText(selectedData.getTblCustomer().getTblPeople().getHpnumber());
//            txtCustomerDeposit.setText(ClassFormatter.currencyFormat.cFormat(selectedData.getTblCustomer().getDeposit()));
            //set unsaving data input -> 'true'
            ClassSession.unSavingDataInput.set(true);
        });

        cbpCustomer.hide();
        cbpReservationBrokenItem.hide();
        cbpReservationBrokenItemRCO.hide();

        //data bill and transaction reservation
        refreshDataBill("reservation");
        initTableDataReservationTransaction();
        //data bill and transaction checkout
        refreshDataBill("checkout");
        initTableDataCheckOutTransaction();
        //data bill and transaction
        refreshDataBill("rco");
        initTableDataRCOTransaction();
        //data reservation room type detail
        initTableDataReservationRoomTypeDetail();
        //data reservation room additional
        initTableDataReservationAdditionalItem();
        initTableDataReservationAdditionalService();

        setSelectedDataToInputFormFunctionalComponent();
    }

    private void setSelectedDataToInputFormFunctionalComponent() {
//        gpFormDataReservationCustomer.setDisable(dataInputStatus == 3);
//        gpFormDataReservationBill.setDisable(dataInputStatus == 3);
//        gpFormDataCheckOutBill.setDisable(dataInputStatus == 3);
//        txtReservationCode.setDisable(true);
//        txtReservationStatus.setDisable(true);
        ClassViewSetting.setDisableForAllInputNode(gpDiscount,
                dataInputStatus == 3);

        ClassViewSetting.setDisableForAllInputNode(gpFormDataReservation,
                dataInputStatus == 3);

        txtPeopleIndentityNumber.setDisable(true);
        txtPeopleName.setDisable(true);
        txtPeopleHPNumber.setDisable(true);
        txtCanceledDate.setDisable(true);
        txtCanceledTransactionNumber.setDisable(true);
        txtReservationCanceledReason.setDisable(true);
        ClassViewSetting.setDisableForAllInputNode(gpFormDataReservationCustomer,
                dataInputStatus == 3,
                //                txtReservationCode,
                //                txtReservationStatus,
                txtPeopleIndentityNumber,
                txtPeopleName,
                txtPeopleHPNumber,
                txtCanceledDate,
                txtCanceledTransactionNumber,
                txtReservationCanceledReason);

        txtTotalRoomCostR.setDisable(true);
        txtTotalRoomCostComplimentR.setDisable(true);
        txtTotalAdditionalCostR.setDisable(true);
        txtTotalDiscountR.setDisable(true);
        txtTotalServiceChargeR.setDisable(true);
        txtTotalTaxR.setDisable(true);
        txtReservationBill.setDisable(true);
        txtTotalTransactionPaymentR.setDisable(true);
        ClassViewSetting.setDisableForAllInputNode(gpFormDataReservationBill,
                dataInputStatus == 3,
                txtTotalRoomCostR,
                txtTotalRoomCostComplimentR,
                txtTotalAdditionalCostR,
                txtTotalDiscountR,
                txtTotalServiceChargeR,
                txtTotalTaxR,
                txtReservationBill,
                txtTotalTransactionPaymentR);

        txtTotalAdditionalCostCO.setDisable(true);
        txtTotalDiscountCO.setDisable(true);
        txtTotalBrokenItemCostCO.setDisable(true);
        txtTotalServiceChargeCO.setDisable(true);
        txtTotalTaxCO.setDisable(true);
        txtCheckOutBill.setDisable(true);
        txtTotalTransactionPaymentCO.setDisable(true);
        txtUsedRoomCardNumberCONote.setDisable(true);
        txtMissedRoomCardNumberCONote.setDisable(true);
        txtReturnedRoomCardNumberCONote.setDisable(true);
        ClassViewSetting.setDisableForAllInputNode(gpFormDataCheckOutBill,
                dataInputStatus == 3,
                txtTotalAdditionalCostCO,
                txtTotalDiscountCO,
                txtTotalBrokenItemCostCO,
                txtTotalServiceChargeCO,
                txtTotalTaxCO,
                txtCheckOutBill,
                txtTotalTransactionPaymentCO,
                txtUsedRoomCardNumberCONote,
                txtMissedRoomCardNumberCONote,
                txtReturnedRoomCardNumberCONote);

        txtTotalRoomCostRCO.setDisable(true);
        txtTotalRoomCostComplimentRCO.setDisable(true);
        txtTotalAdditionalCostRCO.setDisable(true);
        txtTotalDiscountRCO.setDisable(true);
        txtTotalServiceChargeRCO.setDisable(true);
        txtTotalTaxRCO.setDisable(true);
        txtRCOBill.setDisable(true);
        txtTotalTransactionPaymentRCO.setDisable(true);
        ClassViewSetting.setDisableForAllInputNode(gpFormDataRCOBill,
                dataInputStatus == 3,
                txtTotalRoomCostRCO,
                txtTotalRoomCostComplimentRCO,
                txtTotalAdditionalCostRCO,
                txtTotalDiscountRCO,
                txtTotalServiceChargeRCO,
                txtTotalTaxRCO,
                txtRCOBill,
                txtTotalTransactionPaymentRCO);

        btnPrintReservationBill.setDisable(false);
        btnPrintCheckOutBill.setDisable(false);

        btnPrintReservationReceipt.setDisable(false);
        btnPrintCheckOutReceipt.setDisable(false);

        btnPrintCanceledReceipt.setDisable(false);

        btnSave.setVisible((dataInputStatus != 1) && (dataInputStatus != 3));
        btnCancel.setDisable(false);
    }

    public BigDecimal calculationTotalReservationRoom() {
        BigDecimal result = new BigDecimal("0");
        for (TblReservationRoomTypeDetailRoomPriceDetail d : dataReservationRoomTypeDetailRoomPriceDetails) {
            result = result.add(d.getTblReservationRoomPriceDetail().getDetailPrice());
        }
        return result;
    }

    public BigDecimal calculationTotalReservationRoomCompliment() {
        BigDecimal result = new BigDecimal("0");
        for (TblReservationRoomTypeDetailRoomPriceDetail d : dataReservationRoomTypeDetailRoomPriceDetails) {
            result = result.add(d.getTblReservationRoomPriceDetail().getDetailComplimentary());
        }
        return result;
    }

//    public double calculationTotalReservationRoomWithDiscount() {
//        double result = 0;
//        for (TblReservationRoomTypeDetailRoomPriceDetail d : dataReservationRoomTypeDetailRoomPriceDetails) {
//            if (d.getTblReservationRoomPriceDetail().getDiscountable()) {
//                result += d.getTblReservationRoomPriceDetail().getDetailPrice();
//            }
//        }
//        return result;
//    }
//
//    public double calculationTotalReservationRoomWithoutDiscount() {
//        double result = 0;
//        for (TblReservationRoomTypeDetailRoomPriceDetail d : dataReservationRoomTypeDetailRoomPriceDetails) {
//            if (!d.getTblReservationRoomPriceDetail().getDiscountable()) {
//                result += d.getTblReservationRoomPriceDetail().getDetailPrice();
//            }
//        }
//        return result;
//    }
    public BigDecimal calculationTotalAdditional(String billType) {
        BigDecimal result = new BigDecimal("0");
        switch (billType) {
            case "reservation":
                for (TblReservationAdditionalItem data : dataReservationAdditionalItems) {
                    if (data.getRefReservationBillType().getIdtype() == 0) {    //Reservasi = '0'
                        result = result.add(data.getItemCharge().multiply(data.getItemQuantity()));
                    }
                }
                for (TblReservationAdditionalService data : dataReservationAdditionalServices) {
                    if (data.getRefReservationBillType().getIdtype() == 0) { //Reservasi = '0'
                        result = result.add(data.getPrice());
                    }
                }
                break;
            case "checkout":
                for (TblReservationAdditionalItem data : dataReservationAdditionalItems) {
                    if (data.getRefReservationBillType().getIdtype() == 1) {    //Check Out = '1'
                        result = result.add(data.getItemCharge().multiply(data.getItemQuantity()));
                    }
                }
                for (TblReservationAdditionalService data : dataReservationAdditionalServices) {
                    if (data.getRefReservationBillType().getIdtype() == 1) {    //Check Out = '1'
                        result = result.add(data.getPrice());
                    }
                }
                break;
            case "rco":
                for (TblReservationAdditionalItem data : dataReservationAdditionalItems) {
                    if (data.getRefReservationBillType().getIdtype() == 0 //Reservasi = '0'
                            || data.getRefReservationBillType().getIdtype() == 1) {    //Check Out = '1'
                        result = result.add(data.getItemCharge().multiply(data.getItemQuantity()));
                    }
                }
                for (TblReservationAdditionalService data : dataReservationAdditionalServices) {
                    if (data.getRefReservationBillType().getIdtype() == 0 //Reservasi = '0'
                            || data.getRefReservationBillType().getIdtype() == 1) {    //Check Out = '1'
                        result = result.add(data.getPrice());
                    }
                }
                break;
            default:
                break;
        }
        return result;
    }

//    public double calculationTotalAdditionalServiceWithDiscount(String billType) {
//        double result = 0;
//        switch (billType) {
//            case "reservation":
//                for (TblReservationAdditionalService data : dataReservationAdditionalServices) {
//                    if (data.getRefReservationBillType().getIdtype() == 0 //Reservasi = '0'
//                            && data.getDiscountable()) {
//                        result += data.getPrice();
//                    }
//                }
//                break;
//            case "checkout":
//                for (TblReservationAdditionalService data : dataReservationAdditionalServices) {
//                    if (data.getRefReservationBillType().getIdtype() == 1 //Check Out = '1'
//                            && data.getDiscountable()) {
//                        result += data.getPrice();
//                    }
//                }
//                break;
//            default:
//                break;
//        }
//        return result;
//    }
//
//    public double calculationTotalAdditionalServiceWithoutDiscount(String billType) {
//        double result = 0;
//        switch (billType) {
//            case "reservation":
//                for (TblReservationAdditionalService data : dataReservationAdditionalServices) {
//                    if (data.getRefReservationBillType().getIdtype() == 0 //Reservasi = '0'
//                            && !data.getDiscountable()) {
//                        result += data.getPrice();
//                    }
//                }
//                break;
//            case "checkout":
//                for (TblReservationAdditionalService data : dataReservationAdditionalServices) {
//                    if (data.getRefReservationBillType().getIdtype() == 1 //Check Out = '1'
//                            && !data.getDiscountable()) {
//                        result += data.getPrice();
//                    }
//                }
//                break;
//            default:
//                break;
//        }
//        return result;
//    }
//
//    public double calculationTotalAdditionalItemWithDiscountAndWithTax(String billType) {
//        double result = 0;
//        switch (billType) {
//            case "reservation":
//                for (TblReservationAdditionalItem data : dataReservationAdditionalItems) {
//                    if (data.getRefReservationBillType().getIdtype() == 0 //Reservasi = '0'
//                            && data.getDiscountable()
//                            && data.getTaxable()) {
//                        result += data.getItemCharge() * data.getItemQuantity().doubleValue();
//                    }
//                }
//                break;
//            case "checkout":
//                for (TblReservationAdditionalItem data : dataReservationAdditionalItems) {
//                    if (data.getRefReservationBillType().getIdtype() == 1 //Check Out = '1'
//                            && data.getDiscountable()
//                            && data.getTaxable()) {
//                        result += data.getItemCharge() * data.getItemQuantity().doubleValue();
//                    }
//                }
//                break;
//            default:
//                break;
//        }
//        return result;
//    }
//
//    public double calculationTotalAdditionalItemWithoutDiscountAndWithTax(String billType) {
//        double result = 0;
//        switch (billType) {
//            case "reservation":
//                for (TblReservationAdditionalItem data : dataReservationAdditionalItems) {
//                    if (data.getRefReservationBillType().getIdtype() == 0 //Reservasi = '0'
//                            && !data.getDiscountable()
//                            && data.getTaxable()) {
//                        result += data.getItemCharge() * data.getItemQuantity().doubleValue();
//                    }
//                }
//                break;
//            case "checkout":
//                for (TblReservationAdditionalItem data : dataReservationAdditionalItems) {
//                    if (data.getRefReservationBillType().getIdtype() == 1 //Check Out = '1'
//                            && !data.getDiscountable()
//                            && data.getTaxable()) {
//                        result += data.getItemCharge() * data.getItemQuantity().doubleValue();
//                    }
//                }
//                break;
//            default:
//                break;
//        }
//        return result;
//    }
//
//    public double calculationTotalAdditionalItemWithoutDiscountAndWithoutTax(String billType) {
//        double result = 0;
//        switch (billType) {
//            case "reservation":
//                for (TblReservationAdditionalItem data : dataReservationAdditionalItems) {
//                    if (data.getRefReservationBillType().getIdtype() == 0 //Reservasi = '0'
//                            && !data.getDiscountable()
//                            && !data.getTaxable()) {
//                        result += data.getItemCharge() * data.getItemQuantity().doubleValue();
//                    }
//                }
//                break;
//            case "checkout":
//                for (TblReservationAdditionalItem data : dataReservationAdditionalItems) {
//                    if (data.getRefReservationBillType().getIdtype() == 1 //Check Out = '1'
//                            && !data.getDiscountable()
//                            && !data.getTaxable()) {
//                        result += data.getItemCharge() * data.getItemQuantity().doubleValue();
//                    }
//                }
//                break;
//            default:
//                break;
//        }
//        return result;
//    }
//
//    public double calculationTotalWithDiscount(String billType) {
//        double result = 0;
//        switch (billType) {
//            case "reservation":
//                result += calculationTotalReservationRoomWithDiscount()
//                        + calculationTotalAdditionalServiceWithDiscount(billType)
//                        + calculationTotalAdditionalItemWithDiscountAndWithTax(billType);
//                break;
//            case "checkout":
//                result += calculationTotalAdditionalServiceWithDiscount(billType)
//                        + calculationTotalAdditionalItemWithDiscountAndWithTax(billType);
//                break;
//            default:
//                break;
//        }
//        return result;
//    }
//
//    public double calculationTotalDiscount(String billType, double discountPercentage) {
//        double result = 0;
//        switch (billType) {
//            case "reservation":
////                if (dataReservationBill.getRefReservationBillDiscountType() != null) {
//                for (TblReservationRoomTypeDetailRoomPriceDetail d : dataReservationRoomTypeDetailRoomPriceDetails) {
//                    if (d.getTblReservationRoomPriceDetail().getDiscountable()) {
//                        result += d.getTblReservationRoomPriceDetail().getDetailPrice();
//                    }
//                }
//                for (TblReservationAdditionalItem data : dataReservationAdditionalItems) {
//                    if (data.getRefReservationBillType().getIdtype() == 0 //Reservasi = '0'
//                            && data.getDiscountable()) {
//                        result += data.getItemCharge() * data.getItemQuantity().doubleValue();
//                    }
//                }
//                for (TblReservationAdditionalService data : dataReservationAdditionalServices) {
//                    if (data.getRefReservationBillType().getIdtype() == 0 //Reservasi = '0'
//                            && data.getDiscountable()) {
//                        result += data.getPrice();
//                    }
//                }
////                    result = result * dataReservationBill.getDiscountPercentage().doubleValue();
//                result = result * (discountPercentage / 100);
////                }
//                break;
//            case "checkout":
////                if (dataCheckOutBill.getRefReservationBillDiscountType() != null) {
//                for (TblReservationAdditionalItem data : dataReservationAdditionalItems) {
//                    if (data.getRefReservationBillType().getIdtype() == 1 //Check Out = '1'
//                            && data.getDiscountable()) {
//                        result += data.getItemCharge() * data.getItemQuantity().doubleValue();
//                    }
//                }
//                for (TblReservationAdditionalService data : dataReservationAdditionalServices) {
//                    if (data.getRefReservationBillType().getIdtype() == 1 //Check Out = '1'
//                            && data.getDiscountable()) {
//                        result += data.getPrice();
//                    }
//                }
////                    result = result * dataCheckOutBill.getDiscountPercentage().doubleValue();
//                result = result * (discountPercentage / 100);
////                }
//                break;
//            default:
//                break;
//        }
//        return result;
//    }
    public BigDecimal calculationTotalDiscount(String billType) {
        BigDecimal result = new BigDecimal("0");
        switch (billType) {
            case "reservation":
                for (TblReservationRoomTypeDetailRoomPriceDetail d : dataReservationRoomTypeDetailRoomPriceDetails) {
                    result = result.add(d.getTblReservationRoomPriceDetail().getDetailPrice().multiply(d.getTblReservationRoomPriceDetail().getDetailDiscountPercentage().divide(new BigDecimal("100"))));
                }
                for (TblReservationAdditionalItem data : dataReservationAdditionalItems) {
                    if (data.getRefReservationBillType().getIdtype() == 0) { //Reservasi = '0'
                        result = result.add((data.getItemCharge().multiply(data.getItemQuantity())).multiply(data.getDiscountPercentage().divide(new BigDecimal("100"))));
                    }
                }
                for (TblReservationAdditionalService data : dataReservationAdditionalServices) {
                    if (data.getRefReservationBillType().getIdtype() == 0) { //Reservasi = '0'
                        result = result.add(data.getPrice().multiply(data.getDiscountPercentage().divide(new BigDecimal("100"))));
                    }
                }
                break;
            case "checkout":
                for (TblReservationAdditionalItem data : dataReservationAdditionalItems) {
                    if (data.getRefReservationBillType().getIdtype() == 1) { //Check Out = '1'
                        result = result.add((data.getItemCharge().multiply(data.getItemQuantity())).multiply(data.getDiscountPercentage().divide(new BigDecimal("100"))));
                    }
                }
                for (TblReservationAdditionalService data : dataReservationAdditionalServices) {
                    if (data.getRefReservationBillType().getIdtype() == 1) { //Check Out = '1'                            
                        result = result.add(data.getPrice().multiply((data.getDiscountPercentage().divide(new BigDecimal("100")))));
                    }
                }
                break;
            case "rco":
                for (TblReservationRoomTypeDetailRoomPriceDetail d : dataReservationRoomTypeDetailRoomPriceDetails) {
                    result = result.add(d.getTblReservationRoomPriceDetail().getDetailPrice().multiply(d.getTblReservationRoomPriceDetail().getDetailDiscountPercentage().divide(new BigDecimal("100"))));
                }
                for (TblReservationAdditionalItem data : dataReservationAdditionalItems) {
                    if (data.getRefReservationBillType().getIdtype() == 0 //Reservasi = '0'
                            || data.getRefReservationBillType().getIdtype() == 1) { //Check Out = '1'
                        result = result.add((data.getItemCharge().multiply(data.getItemQuantity())).multiply(data.getDiscountPercentage().divide(new BigDecimal("100"))));
                    }
                }
                for (TblReservationAdditionalService data : dataReservationAdditionalServices) {
                    if (data.getRefReservationBillType().getIdtype() == 0 //Reservasi = '0'
                            || data.getRefReservationBillType().getIdtype() == 1) { //Check Out = '1'                            
                        result = result.add(data.getPrice().multiply((data.getDiscountPercentage().divide(new BigDecimal("100")))));
                    }
                }
                break;
            default:
                break;
        }
        return result;
    }

//    public double calculationTotalAfterDiscount(String billType, double discountPercentage) {
//        double result = 0;
//        result += calculationTotalWithDiscount(billType)
//                - calculationTotalDiscount(billType, discountPercentage);
//        return result;
//    }
//
//    public double calculationTotalWithoutDiscountAndWithTax(String billType) {
//        double result = 0;
//        switch (billType) {
//            case "reservation":
//                result += calculationTotalReservationRoomWithoutDiscount()
//                        + calculationTotalAdditionalServiceWithoutDiscount(billType)
//                        + calculationTotalAdditionalItemWithoutDiscountAndWithTax(billType);
//                break;
//            case "checkout":
//                result += calculationTotalAdditionalServiceWithoutDiscount(billType)
//                        + calculationTotalAdditionalItemWithoutDiscountAndWithTax(billType);
//                break;
//            default:
//                break;
//        }
//        return result;
//    }
//
//    public double calculationTotalWithTax(String billType, double discountPercentage) {
//        double result = 0;
//        switch (billType) {
//            case "reservation":
//                result += calculationTotalAfterDiscount(billType, discountPercentage)
//                        + calculationTotalReservationRoomWithoutDiscount()
//                        + calculationTotalAdditionalServiceWithoutDiscount(billType)
//                        + calculationTotalAdditionalItemWithoutDiscountAndWithTax(billType);
//                break;
//            case "checkout":
//                result += calculationTotalAfterDiscount(billType, discountPercentage)
//                        + calculationTotalAdditionalServiceWithoutDiscount(billType)
//                        + calculationTotalAdditionalItemWithoutDiscountAndWithTax(billType);
//                break;
//            default:
//                break;
//        }
//        return result;
//    }
//
//    public double calculationTotalServiceCharge(String billType, double discountPercentage) {
//        double result = 0;
//        switch (billType) {
//            case "reservation":
//                result += calculationTotalReservationRoom()
//                        + calculationTotalAdditional(billType)
//                        - calculationTotalDiscount(billType, discountPercentage);
//                result = result * dataReservationBill.getServiceChargePercentage().doubleValue();
//                break;
//            case "checkout":
//                result += calculationTotalAdditional(billType)
//                        - calculationTotalDiscount(billType, discountPercentage)
//                        + calculationTotalBroken();
//                result = result * dataCheckOutBill.getServiceChargePercentage().doubleValue();
//                break;
//            default:
//                break;
//        }
//        return result;
//    }
//    public double calculationTotalServiceCharge(String billType, double discountPercentage) {
//        double result = 0;
//        switch (billType) {
//            case "reservation":
//                result = calculationTotalWithTax(billType, discountPercentage);
//                result = result * dataReservationBill.getServiceChargePercentage().doubleValue();
//                break;
//            case "checkout":
//                result += calculationTotalWithTax(billType, discountPercentage)
//                        + calculationTotalBroken();
//                result = result * dataCheckOutBill.getServiceChargePercentage().doubleValue();
//                break;
//            default:
//                break;
//        }
//        return result;
//    }
    public BigDecimal calculationTotalServiceCharge(String billType) {
        BigDecimal result = new BigDecimal("0");
        switch (billType) {
            case "reservation":
                result = calculationTotalReservationRoom()
                        .subtract(calculationTotalReservationRoomCompliment())
                        .add(calculationTotalAdditional(billType))
                        .subtract(calculationTotalDiscount(billType));
                result = result.multiply(dataReservationBill.getServiceChargePercentage());
                break;
            case "checkout":
                result = result.add(calculationTotalAdditional(billType)
                        .subtract(calculationTotalDiscount(billType))
                        .add(calculationTotalBroken()));
                result = result.multiply(dataCheckOutBill.getServiceChargePercentage());
                break;
            case "rco":
                result = calculationTotalReservationRoom()
                        .subtract(calculationTotalReservationRoomCompliment())
                        .add(calculationTotalAdditional(billType))
                        .subtract(calculationTotalDiscount(billType)
                                .add(calculationTotalBroken()));
                result = result.multiply(dataReservationBill.getServiceChargePercentage());
                break;
            default:
                break;
        }
        return result;
    }
//    public double calculationTotalTax(String billType, double discountPercentage) {
//        double result = 0;
//        switch (billType) {
//            case "reservation":
//                result += calculationTotalReservationRoom()
//                        + calculationTotalAdditional(billType)
//                        - calculationTotalDiscount(billType, discountPercentage)
//                        + calculationTotalServiceCharge(billType, discountPercentage);
//                result = result * dataReservationBill.getTaxPercentage().doubleValue();
//                break;
//            case "checkout":
//                result += calculationTotalAdditional(billType)
//                        - calculationTotalDiscount(billType, discountPercentage)
//                        + calculationTotalBroken()
//                        + calculationTotalServiceCharge(billType, discountPercentage);
//                result = result * dataCheckOutBill.getTaxPercentage().doubleValue();
//                break;
//            default:
//                break;
//        }
//        return result;
//    }
//    public double calculationTotalTax(String billType, double discountPercentage) {
//        double result = 0;
//        switch (billType) {
//            case "reservation":
//                result += calculationTotalWithTax(billType, discountPercentage)
//                        + calculationTotalServiceCharge(billType, discountPercentage);
//                result = result * dataReservationBill.getTaxPercentage().doubleValue();
//                break;
//            case "checkout":
//                result += calculationTotalWithTax(billType, discountPercentage)
//                        + calculationTotalBroken()
//                        + calculationTotalServiceCharge(billType, discountPercentage);
//                result = result * dataCheckOutBill.getTaxPercentage().doubleValue();
//                break;
//            default:
//                break;
//        }
//        return result;
//    }

    public BigDecimal calculationTotalTax(String billType) {
        BigDecimal result = new BigDecimal("0");
        switch (billType) {
            case "reservation":
                result = calculationTotalReservationRoom()
                        .subtract(calculationTotalReservationRoomCompliment())
                        .add(calculationTotalAdditional(billType))
                        .subtract(calculationTotalDiscount(billType));
                result = result.add(calculationTotalServiceCharge(billType));
                result = result.multiply(dataReservationBill.getTaxPercentage());
                break;
            case "checkout":
                result = result.add(calculationTotalAdditional(billType)
                        .subtract(calculationTotalDiscount(billType))
                        .add(calculationTotalBroken()));
                result = result.add(calculationTotalServiceCharge(billType));
                result = result.multiply(dataCheckOutBill.getTaxPercentage());
                break;
            case "rco":
                result = calculationTotalReservationRoom()
                        .subtract(calculationTotalReservationRoomCompliment())
                        .add(calculationTotalAdditional(billType))
                        .subtract(calculationTotalDiscount(billType))
                        .add(calculationTotalBroken());
                result = result.add(calculationTotalServiceCharge(billType));
                result = result.multiply(dataReservationBill.getTaxPercentage());
                break;
            default:
                break;
        }
        return result;
    }

//    public double calculationTotalAfterTax(String billType, double discountPercentage) {
//        double result = 0;
//        switch (billType) {
//            case "reservation":
//                result += calculationTotalWithTax(billType, discountPercentage)
//                        + calculationTotalServiceCharge(billType, discountPercentage)
//                        + calculationTotalTax(billType, discountPercentage);
//                break;
//            case "checkout":
//                result += calculationTotalWithTax(billType, discountPercentage)
//                        + calculationTotalBroken()
//                        + calculationTotalServiceCharge(billType, discountPercentage)
//                        + calculationTotalTax(billType, discountPercentage);
//                break;
//            default:
//                break;
//        }
//        return result;
//    }
//
//    public double calculationTotalWithoutDiscountAndWithoutTax(String billType) {
//        double result = 0;
//        switch (billType) {
//            case "reservation":
//                result += calculationTotalAdditionalItemWithoutDiscountAndWithoutTax(billType);
//                break;
//            case "checkout":
//                result += calculationTotalAdditionalItemWithoutDiscountAndWithoutTax(billType);
//                break;
//            default:
//                break;
//        }
//        return result;
//    }
//
//    public double calculationTotalBill(String billType, double discountPercentage) {
//        double result = 0;
//        switch (billType) {
//            case "reservation":
//                result += calculationTotalReservationRoom()
//                        + calculationTotalAdditional(billType)
//                        - calculationTotalDiscount(billType, discountPercentage)
//                        + calculationTotalServiceCharge(billType, discountPercentage)
//                        + calculationTotalTax(billType, discountPercentage);
//                break;
//            case "checkout":
//                result += calculationTotalAdditional(billType)
//                        - calculationTotalDiscount(billType, discountPercentage)
//                        + calculationTotalBroken()
//                        + calculationTotalServiceCharge(billType, discountPercentage)
//                        + calculationTotalTax(billType, discountPercentage);
////                        + calculationTotalMissedCardCharge();
//                break;
//            default:
//                break;
//        }
//        return result;
//    }
//    public double calculationTotalBill(String billType, double discountPercentage) {
//        double result = 0;
//        result += calculationTotalAfterTax(billType, discountPercentage)
//                + calculationTotalAdditionalItemWithoutDiscountAndWithoutTax(billType);
//        return result;
//    }
    public BigDecimal calculationTotalBill(String billType) {
        BigDecimal result = new BigDecimal("0");
        switch (billType) {
            case "reservation":
                result = result.add(calculationTotalReservationRoom()
                        .subtract(calculationTotalReservationRoomCompliment())
                        .add(calculationTotalAdditional(billType))
                        .subtract(calculationTotalDiscount(billType))
                        .add(calculationTotalServiceCharge(billType))
                        .add(calculationTotalTax(billType)));
                break;
            case "checkout":
                result = result.add(calculationTotalAdditional(billType)
                        .subtract(calculationTotalDiscount(billType))
                        .add(calculationTotalBroken())
                        .add(calculationTotalServiceCharge(billType))
                        .add(calculationTotalTax(billType)));
                break;
            case "rco":
                result = result.add(calculationTotalReservationRoom()
                        .subtract(calculationTotalReservationRoomCompliment())
                        .add(calculationTotalAdditional(billType))
                        .subtract(calculationTotalDiscount(billType))
                        .add(calculationTotalBroken())
                        .add(calculationTotalServiceCharge(billType))
                        .add(calculationTotalTax(billType)));
                break;
            default:
                break;
        }
        return result;
    }

    public BigDecimal calculationTotalBillAfterRounding(String billType) {
        BigDecimal result = new BigDecimal("0");
        switch (billType) {
            case "reservation":
                //total bill
                result = calculationTotalBill(billType);
                //total rounding value
                result = result.add(calculationTotalReservationTransactionRoundingValue(billType));
                break;
            case "checkout":
                //total bill
                result = calculationTotalBill(billType);
                //total rounding value
                result = result.add(calculationTotalReservationTransactionRoundingValue(billType));
                break;
            case "rco":
                //total bill
                result = calculationTotalBill(billType);
                //total rounding value
                result = result.add(calculationTotalReservationTransactionRoundingValue(billType));
                break;
            default:
                break;
        }
        return result;
    }

    public BigDecimal calculationTotalBroken() {
        BigDecimal result = new BigDecimal("0");
        //items
        for (TblReservationBrokenItem data : dataReservationBrokenItems) {
            result = result.add(data.getItemCharge().multiply(data.getItemQuantity()));
        }
        return result;
    }

    public int getTotalMissedCard() {
        int result = 0;
        for (TblReservationRoomTypeDetail data : dataReservationRoomTypeDetails) {
            if (data.getTblReservationCheckInOut() != null) {
                result += data.getTblReservationCheckInOut().getCardMissed();
            }
        }
        return result;
    }

    public BigDecimal calculationTotalMissedCardCharge() {
        BigDecimal result = new BigDecimal("0");
        for (TblReservationRoomTypeDetail data : dataReservationRoomTypeDetails) {
            if (data.getTblReservationCheckInOut() != null) {
                result = result.add(((new BigDecimal(data.getTblReservationCheckInOut().getCardMissed())).multiply(data.getTblReservationCheckInOut().getBrokenCardCharge())));
            }
        }
        return result;
    }

    public BigDecimal calculationTotalReservationTransaction(String billType) {
        BigDecimal result = new BigDecimal("0");
        switch (billType) {
            case "reservation":
                for (TblReservationPayment data : dataReservationPayments) {
                    result = result.add(data.getUnitNominal());
                }
                break;
            case "checkout":
                for (TblReservationPayment data : dataCheckOutPayments) {
                    result = result.add(data.getUnitNominal());
                }
                break;
            case "rco":
                for (TblReservationPayment data : dataReservationPayments) {
                    result = result.add(data.getUnitNominal());
                }
                for (TblReservationPayment data : dataCheckOutPayments) {
                    result = result.add(data.getUnitNominal());
                }
                break;
            default:
                break;
        }
        return result;
    }

    public BigDecimal calculationTotalReservationTransactionRoundingValue(String billType) {
        BigDecimal result = new BigDecimal("0");
        switch (billType) {
            case "reservation":
                for (TblReservationPayment data : dataReservationPayments) {
                    result = result.add(data.getRoundingValue());
                }
                break;
            case "checkout":
                for (TblReservationPayment data : dataCheckOutPayments) {
                    result = result.add(data.getRoundingValue());
                }
                break;
            case "rco":
                for (TblReservationPayment data : dataReservationPayments) {
                    result = result.add(data.getRoundingValue());
                }
                for (TblReservationPayment data : dataCheckOutPayments) {
                    result = result.add(data.getRoundingValue());
                }
                break;
            default:
                break;
        }
        return result;
    }

    public BigDecimal calculationTotalReservationTransactionDisountPayment(String billType) {
        BigDecimal result = new BigDecimal("0");
        switch (billType) {
            case "reservation":
                for (TblReservationPaymentWithBankCard data : dataReservationPaymentWithBankCards) {
                    if (data.getTblReservationPayment().getRefReservationBillType().getIdtype() == 0) {   //Rservation = '0'
                        result = result.add(data.getPaymentDiscount());
                    }
                }
                break;
            case "checkout":
                for (TblReservationPaymentWithBankCard data : dataReservationPaymentWithBankCards) {
                    if (data.getTblReservationPayment().getRefReservationBillType().getIdtype() == 1) {   //CheckOut = '1'
                        result = result.add(data.getPaymentDiscount());
                    }
                }
                break;
            case "rco":
                for (TblReservationPaymentWithBankCard data : dataReservationPaymentWithBankCards) {
                    if (data.getTblReservationPayment().getRefReservationBillType().getIdtype() == 0 //Rservation = '0'
                            || data.getTblReservationPayment().getRefReservationBillType().getIdtype() == 1) {   //CheckOut = '1'
                        result = result.add(data.getPaymentDiscount());
                    }
                }
                break;
            default:
                break;
        }
        return result;
    }

//    public double calculationTotalRestOfBill(String billType, double discountPercentage) {
//        return calculationTotalBill(billType, discountPercentage)
//                - calculationTotalReservationTransaction(billType)
//                - calculationTotalReservationTransactionDisountPayment(billType);
//    }
    public BigDecimal calculationTotalRestOfBill(String billType) {
        return calculationTotalBillAfterRounding(billType)
                .subtract(calculationTotalReservationTransaction(billType))
                .subtract(calculationTotalReservationTransactionDisountPayment(billType));
    }

    public BigDecimal calculationTotalDepositNeeded() {
        BigDecimal result = new BigDecimal("0");
        for (TblReservationRoomTypeDetail rtd : dataReservationRoomTypeDetails) {
            if (rtd.getTblReservationCheckInOut() != null
                    && (rtd.getTblReservationCheckInOut().getRefReservationCheckInOutStatus().getIdstatus() == 0 //Ready to Check In = '0'
                    || rtd.getTblReservationCheckInOut().getRefReservationCheckInOutStatus().getIdstatus() == 1)) {    //Checked In = '1'
                result = result.add(((new BigDecimal(rtd.getTblReservationCheckInOut().getCardUsed())).multiply(rtd.getTblReservationCheckInOut().getBrokenCardCharge())));
            }
        }
        if (result.compareTo(selectedData.getTblCustomer().getDeposit()) == 1) {
            result = result.subtract(selectedData.getTblCustomer().getDeposit());
        } else {
            result = new BigDecimal("0");
        }
        return result;
    }

    public BigDecimal calculationRoomTypeDetailCost(TblReservationRoomTypeDetail dataDetail) {
        BigDecimal result = new BigDecimal("0");
        if (dataInputStatus == 0) {   //insert
            for (TblReservationRoomTypeDetailRoomPriceDetail data : dataReservationRoomTypeDetailRoomPriceDetails) {
                if (data.getTblReservationRoomTypeDetail().equals(dataDetail)) {
                    result = result.add(data.getTblReservationRoomPriceDetail().getDetailPrice());
                }
            }
        } else {  //update
            for (TblReservationRoomTypeDetailRoomPriceDetail data : dataReservationRoomTypeDetailRoomPriceDetails) {
                if (data.getTblReservationRoomTypeDetail().getIddetail() == dataDetail.getIddetail()) {
                    result = result.add(data.getTblReservationRoomPriceDetail().getDetailPrice());
                }
            }
        }
        return result;
    }

    public BigDecimal calculationRoomTypeDetailDiscount(TblReservationRoomTypeDetail dataDetail) {
        BigDecimal result = new BigDecimal("0");
        if (dataInputStatus == 0) {   //insert
            for (TblReservationRoomTypeDetailRoomPriceDetail data : dataReservationRoomTypeDetailRoomPriceDetails) {
                if (data.getTblReservationRoomTypeDetail().equals(dataDetail)) {
                    result = result.add(data.getTblReservationRoomPriceDetail().getDetailPrice()
                            .multiply(data.getTblReservationRoomPriceDetail().getDetailDiscountPercentage().divide(new BigDecimal("100"))));
                }
            }
        } else {  //update
            for (TblReservationRoomTypeDetailRoomPriceDetail data : dataReservationRoomTypeDetailRoomPriceDetails) {
                if (data.getTblReservationRoomTypeDetail().getIddetail() == dataDetail.getIddetail()) {
                    result = result.add(data.getTblReservationRoomPriceDetail().getDetailPrice()
                            .multiply(data.getTblReservationRoomPriceDetail().getDetailDiscountPercentage().divide(new BigDecimal("100"))));
                }
            }
        }
        return result;
    }

    private BigDecimal calculationRoomTypeDetailCompliment(TblReservationRoomTypeDetail dataDetail) {
        BigDecimal result = new BigDecimal("0");
        if (dataInputStatus == 0) {   //insert
            for (TblReservationRoomTypeDetailRoomPriceDetail data : dataReservationRoomTypeDetailRoomPriceDetails) {
                if (data.getTblReservationRoomTypeDetail().equals(dataDetail)) {
                    result = result.add(data.getTblReservationRoomPriceDetail().getDetailComplimentary());
                }
            }
        } else {  //update
            for (TblReservationRoomTypeDetailRoomPriceDetail data : dataReservationRoomTypeDetailRoomPriceDetails) {
                if (data.getTblReservationRoomTypeDetail().getIddetail() == dataDetail.getIddetail()) {
                    result = result.add(data.getTblReservationRoomPriceDetail().getDetailComplimentary());
                }
            }
        }
        return result;
    }

    public void refreshDataBill(String billType) {
        switch (billType) {
            case "reservation":
//                txtTotalReservationRoom.setText(ClassFormatter.currencyFormat.cFormat(calculationTotalReservationRoom()));
//                txtTotalReservationDiscount.setText(ClassFormatter.currencyFormat.cFormat(calculationTotalDiscount(billType, dataReservationBill.getDiscountPercentage().doubleValue())));
//                txtTotalAdditionalInReservationBill.setText(ClassFormatter.currencyFormat.cFormat(calculationTotalAdditional(billType)));
//                txtTotalServiceChargeInReservationBill.setText(ClassFormatter.currencyFormat.cFormat(calculationTotalServiceCharge(billType, dataReservationBill.getDiscountPercentage().doubleValue())));
                lblServiceChargePercentageInReservationBill.setText("(" + ClassFormatter.decimalFormat.cFormat((dataReservationBill.getServiceChargePercentage().multiply(new BigDecimal("100")))) + "%)");
//                txtTotalTaxInReservationBill.setText(ClassFormatter.currencyFormat.cFormat(calculationTotalTax(billType, dataReservationBill.getDiscountPercentage().doubleValue())));
                lblTaxPercentageInReservationBill.setText("(" + ClassFormatter.decimalFormat.cFormat((dataReservationBill.getTaxPercentage().multiply(new BigDecimal("100")))) + "%)");
//                txtTotalBillReservation.setText(ClassFormatter.currencyFormat.cFormat(calculationTotalBill(billType, dataReservationBill.getDiscountPercentage().doubleValue())));
                txtTotalRoomCostR.setText(ClassFormatter.currencyFormat.cFormat(calculationTotalReservationRoom()));
                txtTotalRoomCostComplimentR.setText(ClassFormatter.currencyFormat.cFormat(calculationTotalReservationRoomCompliment()));
                txtTotalAdditionalCostR.setText(ClassFormatter.currencyFormat.cFormat(calculationTotalAdditional(billType)));
//                txtTotalRoomCostWithDiscountAndTaxR.setText(ClassFormatter.currencyFormat.cFormat(calculationTotalReservationRoomWithDiscount()));
//                txtTotalAdditionalServiceCostWithDiscountAndTaxR.setText(ClassFormatter.currencyFormat.cFormat(calculationTotalAdditionalServiceWithDiscount(billType)));
//                txtTotalAdditionalItemCostWithDiscountAndTaxR.setText(ClassFormatter.currencyFormat.cFormat(calculationTotalAdditionalItemWithDiscountAndWithTax(billType)));
//                txtTotalCostWithDiscountR.setText(ClassFormatter.currencyFormat.cFormat(calculationTotalWithDiscount(billType)));
                txtTotalDiscountR.setText(ClassFormatter.currencyFormat.cFormat(calculationTotalDiscount(billType)));
//                txtTotalCostAfterDiscountR.setText(ClassFormatter.currencyFormat.cFormat(calculationTotalAfterDiscount(billType, dataReservationBill.getDiscountPercentage().doubleValue())));
//                txtTotalRoomCostWithoutDiscountAndWithTaxR.setText(ClassFormatter.currencyFormat.cFormat(calculationTotalReservationRoomWithoutDiscount()));
//                txtTotalAdditionalServiceCostWithoutDiscountAndWithTaxR.setText(ClassFormatter.currencyFormat.cFormat(calculationTotalAdditionalServiceWithoutDiscount(billType)));
//                txtTotalAdditionalItemCostWithoutDiscountAndWithTaxR.setText(ClassFormatter.currencyFormat.cFormat(calculationTotalAdditionalItemWithoutDiscountAndWithTax(billType)));
//                txtTotalCostWithoutDiscountAndWithTaxR.setText(ClassFormatter.currencyFormat.cFormat(calculationTotalWithoutDiscountAndWithTax(billType)));
//                txtTotalCostWithTaxR.setText(ClassFormatter.currencyFormat.cFormat(calculationTotalWithTax(billType, dataReservationBill.getDiscountPercentage().doubleValue())));
                txtTotalServiceChargeR.setText(ClassFormatter.currencyFormat.cFormat(calculationTotalServiceCharge(billType)));
                txtTotalTaxR.setText(ClassFormatter.currencyFormat.cFormat(calculationTotalTax(billType)));
//                txtTotalCostAfterTaxR.setText(ClassFormatter.currencyFormat.cFormat(calculationTotalAfterTax(billType, dataReservationBill.getDiscountPercentage().doubleValue())));
//                txtTotalAdditionalItemWithoutDiscountAndWithoutTaxR.setText(ClassFormatter.currencyFormat.cFormat(calculationTotalAdditionalItemWithoutDiscountAndWithoutTax(billType)));
//                txtTotalCostWithoutDiscountAndWithoutTaxR.setText(ClassFormatter.currencyFormat.cFormat(calculationTotalWithoutDiscountAndWithoutTax(billType)));
                txtReservationBill.setText(ClassFormatter.currencyFormat.cFormat(calculationTotalBillAfterRounding(billType)));
                lblRoundingValueInReservationBill.setText("(Pembulatan : " + ClassFormatter.currencyFormat.cFormat(calculationTotalReservationTransactionRoundingValue(billType)) + ")");
                txtTotalTransactionPaymentR.setText(ClassFormatter.currencyFormat.cFormat(calculationTotalReservationTransaction(billType)));
//                txtTotalTransactionPaymentDiscountR.setText(ClassFormatter.currencyFormat.cFormat(calculationTotalReservationTransactionDisountPayment(billType)));
                break;
            case "checkout":
//                txtTotalAdditionalInCheckOutBill.setText(ClassFormatter.currencyFormat.cFormat(calculationTotalAdditional(billType)));
//                txtTotalCheckOutDiscount.setText(ClassFormatter.currencyFormat.cFormat(calculationTotalDiscount(billType, dataCheckOutBill.getDiscountPercentage().doubleValue())));
//                txtTotalBrokenInCheckOutBill.setText(ClassFormatter.currencyFormat.cFormat(calculationTotalBroken()));
//                txtTotalServiceChargeInCheckOutBill.setText(ClassFormatter.currencyFormat.cFormat(calculationTotalServiceCharge(billType, dataCheckOutBill.getDiscountPercentage().doubleValue())));
                lblServiceChargePercentageInCheckOutBill.setText("(" + ClassFormatter.decimalFormat.cFormat((dataCheckOutBill.getServiceChargePercentage().multiply(new BigDecimal("100")))) + "%)");
//                txtTotalTaxInCheckOutBill.setText(ClassFormatter.currencyFormat.cFormat(calculationTotalTax(billType, dataCheckOutBill.getDiscountPercentage().doubleValue())));
                lblTaxPercentageInCheckOutBill.setText("(" + ClassFormatter.decimalFormat.cFormat((dataCheckOutBill.getTaxPercentage().multiply(new BigDecimal("100")))) + "%)");
//                txtTotalMissedCardChargeInCheckOutBill.setText(ClassFormatter.currencyFormat.cFormat(calculationTotalMissedCardCharge()));
//                lblMissedCardNumberInCheckOutBill.setText("(Missed Card : " + getTotalMissedCard() + ")");
//                txtTotalBillCheckOut.setText(ClassFormatter.currencyFormat.cFormat(calculationTotalBill(billType, dataCheckOutBill.getDiscountPercentage().doubleValue())));
//                txtTotalAdditionalServiceCostWithDiscountAndTaxCO.setText(ClassFormatter.currencyFormat.cFormat(calculationTotalAdditionalServiceWithDiscount(billType)));
//                txtTotalAdditionalItemCostWithDiscountAndTaxCO.setText(ClassFormatter.currencyFormat.cFormat(calculationTotalAdditionalItemWithDiscountAndWithTax(billType)));
//                txtTotalCostWithDiscountCO.setText(ClassFormatter.currencyFormat.cFormat(calculationTotalWithDiscount(billType)));
                txtTotalAdditionalCostCO.setText(ClassFormatter.currencyFormat.cFormat(calculationTotalAdditional(billType)));
                txtTotalDiscountCO.setText(ClassFormatter.currencyFormat.cFormat(calculationTotalDiscount(billType)));
//                txtTotalCostAfterDiscountCO.setText(ClassFormatter.currencyFormat.cFormat(calculationTotalAfterDiscount(billType, dataCheckOutBill.getDiscountPercentage().doubleValue())));
//                txtTotalAdditionalServiceCostWithoutDiscountAndWithTaxCO.setText(ClassFormatter.currencyFormat.cFormat(calculationTotalAdditionalServiceWithoutDiscount(billType)));
//                txtTotalAdditionalItemCostWithoutDiscountAndWithTaxCO.setText(ClassFormatter.currencyFormat.cFormat(calculationTotalAdditionalItemWithoutDiscountAndWithTax(billType)));
//                txtTotalCostWithoutDiscountCO.setText(ClassFormatter.currencyFormat.cFormat(calculationTotalWithoutDiscountAndWithTax(billType)));
                txtTotalBrokenItemCostCO.setText(ClassFormatter.currencyFormat.cFormat(calculationTotalBroken()));
//                txtTotalCostWithTaxCO.setText(ClassFormatter.currencyFormat.cFormat(calculationTotalWithTax(billType, dataCheckOutBill.getDiscountPercentage().doubleValue())));
                txtTotalServiceChargeCO.setText(ClassFormatter.currencyFormat.cFormat(calculationTotalServiceCharge(billType)));
                txtTotalTaxCO.setText(ClassFormatter.currencyFormat.cFormat(calculationTotalTax(billType)));
//                txtTotalCostAfterTaxCO.setText(ClassFormatter.currencyFormat.cFormat(calculationTotalAfterTax(billType, dataCheckOutBill.getDiscountPercentage().doubleValue())));
//                txtTotalAdditionalItemWithoutDiscountAndWithoutTaxCO.setText(ClassFormatter.currencyFormat.cFormat(calculationTotalAdditionalItemWithoutDiscountAndWithoutTax(billType)));
//                txtTotalCostWithoutDiscountAndWithoutTaxCO.setText(ClassFormatter.currencyFormat.cFormat(calculationTotalWithoutDiscountAndWithoutTax(billType)));
                txtCheckOutBill.setText(ClassFormatter.currencyFormat.cFormat(calculationTotalBillAfterRounding(billType)));
                lblRoundingValueInCheckOutBill.setText("(Pembulatan : " + ClassFormatter.currencyFormat.cFormat(calculationTotalReservationTransactionRoundingValue(billType)) + ")");
                txtTotalTransactionPaymentCO.setText(ClassFormatter.currencyFormat.cFormat(calculationTotalReservationTransaction(billType)));
                txtTotalTransactionPaymentDiscountCO.setText(ClassFormatter.currencyFormat.cFormat(calculationTotalReservationTransactionDisountPayment(billType)));
                txtUsedRoomCardNumberCONote.setText(String.valueOf(getTotalUsedRoomCardNumber()));
                txtMissedRoomCardNumberCONote.setText(String.valueOf(getTotalMissedRoomCardNumber()));
                txtReturnedRoomCardNumberCONote.setText(String.valueOf(getTotalReturnedRoomCardNumber()));
                break;
            case "rco":
                lblServiceChargePercentageInRCOBill.setText("(" + ClassFormatter.decimalFormat.cFormat((dataReservationBill.getServiceChargePercentage().multiply(new BigDecimal("100")))) + "%)");
                lblTaxPercentageInRCOBill.setText("(" + ClassFormatter.decimalFormat.cFormat((dataReservationBill.getTaxPercentage().multiply(new BigDecimal("100")))) + "%)");
                txtTotalRoomCostRCO.setText(ClassFormatter.currencyFormat.cFormat(calculationTotalReservationRoom()));
                txtTotalRoomCostComplimentRCO.setText(ClassFormatter.currencyFormat.cFormat(calculationTotalReservationRoomCompliment()));
                txtTotalAdditionalCostRCO.setText(ClassFormatter.currencyFormat.cFormat(calculationTotalAdditional(billType)));
                txtTotalDiscountRCO.setText(ClassFormatter.currencyFormat.cFormat(calculationTotalDiscount(billType)));
                txtTotalBrokenItemCostRCO.setText(ClassFormatter.currencyFormat.cFormat(calculationTotalBroken()));
                txtTotalServiceChargeRCO.setText(ClassFormatter.currencyFormat.cFormat(calculationTotalServiceCharge(billType)));
                txtTotalTaxRCO.setText(ClassFormatter.currencyFormat.cFormat(calculationTotalTax(billType)));
                txtRCOBill.setText(ClassFormatter.currencyFormat.cFormat(calculationTotalBillAfterRounding(billType)));
                lblRoundingValueInRCOBill.setText("(Pembulatan : " + ClassFormatter.currencyFormat.cFormat(calculationTotalReservationTransactionRoundingValue(billType)) + ")");
                txtTotalTransactionPaymentRCO.setText(ClassFormatter.currencyFormat.cFormat(calculationTotalReservationTransaction(billType)));
                break;
            default:
                break;
        }
    }

    private int getTotalUsedRoomCardNumber() {
        int result = 0;
        for (TblReservationRoomTypeDetail data : dataReservationRoomTypeDetails) {
            if (data.getTblReservationCheckInOut() != null) {
                result += data.getTblReservationCheckInOut().getCardUsed();
            }
        }
        return result;
    }

    private int getTotalMissedRoomCardNumber() {
        int result = 0;
        for (TblReservationRoomTypeDetail data : dataReservationRoomTypeDetails) {
            if (data.getTblReservationCheckInOut() != null) {
                result += data.getTblReservationCheckInOut().getCardMissed();
            }
        }
        return result;
    }

    private int getTotalReturnedRoomCardNumber() {
        int result = 0;
        for (TblReservationRoomTypeDetail data : dataReservationRoomTypeDetails) {
            if (data.getTblReservationCheckInOut() != null) {
                if (data.getTblReservationCheckInOut().getRefReservationCheckInOutStatus().getIdstatus() == 3) {   //Checked Out = '3'
                    result += data.getTblReservationCheckInOut().getCardUsed()
                            - data.getTblReservationCheckInOut().getCardMissed();
                }
            }
        }
        return result;
    }

    /**
     * TABLE DATA RESERVATION TRANSACTION
     */
    public ClassTableWithControl tableDataReservationTransaction;

    private void initTableDataReservationTransaction() {
        //set table
        setTableDataReservationTransaction();
        //set control
        setTableControlDataReservationTransaction();
        //set table-control to content-view
        ancTableTransactionReservationLayout.getChildren().clear();
        AnchorPane.setTopAnchor(tableDataReservationTransaction, 15.0);
        AnchorPane.setLeftAnchor(tableDataReservationTransaction, 15.0);
        AnchorPane.setBottomAnchor(tableDataReservationTransaction, 15.0);
        AnchorPane.setRightAnchor(tableDataReservationTransaction, 15.0);

        ancTableTransactionReservationLayout.getChildren().add(tableDataReservationTransaction);
    }

    private void setTableDataReservationTransaction() {
        TableView<TblReservationPayment> tableView = new TableView();

        TableColumn<TblReservationPayment, String> paymentDate = new TableColumn("Tanggal");
        paymentDate.setCellValueFactory((TableColumn.CellDataFeatures<TblReservationPayment, String> param)
                -> Bindings.createStringBinding(() -> ClassFormatter.dateTimeFormate.format(param.getValue().getPaymentDate()), param.getValue().paymentDateProperty()));
        paymentDate.setMinWidth(160);

        TableColumn<TblReservationPayment, String> transactionNominal = new TableColumn("Nominal");
        transactionNominal.setCellValueFactory((TableColumn.CellDataFeatures<TblReservationPayment, String> param)
                -> Bindings.createStringBinding(() -> ClassFormatter.currencyFormat.cFormat(param.getValue().getUnitNominal()), param.getValue().unitNominalProperty()));
        transactionNominal.setMinWidth(160);

        TableColumn<TblReservationPayment, String> paymentTypeName = new TableColumn("Tipe Pembayaran");
        paymentTypeName.setCellValueFactory((TableColumn.CellDataFeatures<TblReservationPayment, String> param)
                -> Bindings.createStringBinding(() -> param.getValue().getRefFinanceTransactionPaymentType().getTypeName(), param.getValue().refFinanceTransactionPaymentTypeProperty()));
        paymentTypeName.setMinWidth(180);

        tableView.getColumns().addAll(paymentDate, transactionNominal, paymentTypeName);
        tableView.setItems(loadAllDataReservationTransaction());
        tableDataReservationTransaction = new ClassTableWithControl(tableView);

    }

    private void setTableControlDataReservationTransaction() {
        //set control from feature
        ObservableList<Node> buttonControls = FXCollections.observableArrayList();
        JFXButton buttonControl;
        if (true) {
            buttonControl = new JFXButton();
            buttonControl.setText("Tambah");
            buttonControl.setOnMouseClicked((e) -> {
                //listener add
                dataReservationTransactionCreateHandle();
            });
            buttonControls.add(buttonControl);
        }
//        if (true) {
//            buttonControl = new JFXButton();
//            buttonControl.setText("Cetak");
//            buttonControl.setOnMouseClicked((e) -> {
//                //listener print
//                dataReservationTransactionPrintHandle();
//            });
//            buttonControls.add(buttonControl);
//        }
        tableDataReservationTransaction.addButtonControl(buttonControls);
    }

    public ObservableList<TblReservationPayment> loadAllDataReservationTransaction() {
        return FXCollections.observableArrayList(dataReservationPayments);
    }

    /**
     * TABLE DATA CHECK-OUT TRANSACTION
     */
    public ClassTableWithControl tableDataCheckOutTransaction;

    public void initTableDataCheckOutTransaction() {
        //set table
        setTableDataCheckOutTransaction();
        //set control
        setTableControlDataCheckOutTransaction();
        //set table-control to content-view
        ancTableTransactionCheckOutLayout.getChildren().clear();
        AnchorPane.setTopAnchor(tableDataCheckOutTransaction, 15.0);
        AnchorPane.setLeftAnchor(tableDataCheckOutTransaction, 15.0);
        AnchorPane.setBottomAnchor(tableDataCheckOutTransaction, 15.0);
        AnchorPane.setRightAnchor(tableDataCheckOutTransaction, 15.0);

        ancTableTransactionCheckOutLayout.getChildren().add(tableDataCheckOutTransaction);
    }

    private void setTableDataCheckOutTransaction() {
        TableView<TblReservationPayment> tableView = new TableView();

        TableColumn<TblReservationPayment, String> paymentDate = new TableColumn("Tanggal");
        paymentDate.setCellValueFactory((TableColumn.CellDataFeatures<TblReservationPayment, String> param)
                -> Bindings.createStringBinding(() -> ClassFormatter.dateTimeFormate.format(param.getValue().getPaymentDate()), param.getValue().paymentDateProperty()));
        paymentDate.setMinWidth(160);

        TableColumn<TblReservationPayment, String> transactionNominal = new TableColumn("Nominal");
        transactionNominal.setCellValueFactory((TableColumn.CellDataFeatures<TblReservationPayment, String> param)
                -> Bindings.createStringBinding(() -> ClassFormatter.currencyFormat.cFormat(param.getValue().getUnitNominal()), param.getValue().unitNominalProperty()));
        transactionNominal.setMinWidth(160);

        TableColumn<TblReservationPayment, String> paymentTypeName = new TableColumn("Tipe Pembayaran");
        paymentTypeName.setCellValueFactory((TableColumn.CellDataFeatures<TblReservationPayment, String> param)
                -> Bindings.createStringBinding(() -> param.getValue().getRefFinanceTransactionPaymentType().getTypeName(), param.getValue().refFinanceTransactionPaymentTypeProperty()));
        paymentTypeName.setMinWidth(180);

        tableView.getColumns().addAll(paymentDate, transactionNominal, paymentTypeName);
        tableView.setItems(loadAllDataCheckOutTransaction());
        tableDataCheckOutTransaction = new ClassTableWithControl(tableView);

    }

    private void setTableControlDataCheckOutTransaction() {
        //set control from feature
        ObservableList<Node> buttonControls = FXCollections.observableArrayList();
        JFXButton buttonControl;
        if (true) {
            buttonControl = new JFXButton();
            buttonControl.setText("Tambah");
            buttonControl.setOnMouseClicked((e) -> {
                //listener add
                dataCheckOutTransactionCreateHandle();
            });
            buttonControls.add(buttonControl);
        }
//        if (true) {
//            buttonControl = new JFXButton();
//            buttonControl.setText("Cetak");
//            buttonControl.setOnMouseClicked((e) -> {
//                //listener add
//                dataCheckOutTransactionPrintHandle();
//            });
//            buttonControls.add(buttonControl);
//        }
        tableDataCheckOutTransaction.addButtonControl(buttonControls);
    }

    public ObservableList<TblReservationPayment> loadAllDataCheckOutTransaction() {
        return FXCollections.observableArrayList(dataCheckOutPayments);
    }

    /**
     * TABLE DATA RCO TRANSACTION
     */
    public ClassTableWithControl tableDataRCOTransaction;

    private void initTableDataRCOTransaction() {
        //set table
        setTableDataRCOTransaction();
        //set control
        setTableControlDataRCOTransaction();
        //set table-control to content-view
        ancTableTransactionRCOLayout.getChildren().clear();
        AnchorPane.setTopAnchor(tableDataRCOTransaction, 15.0);
        AnchorPane.setLeftAnchor(tableDataRCOTransaction, 15.0);
        AnchorPane.setBottomAnchor(tableDataRCOTransaction, 15.0);
        AnchorPane.setRightAnchor(tableDataRCOTransaction, 15.0);

        ancTableTransactionRCOLayout.getChildren().add(tableDataRCOTransaction);
    }

    private void setTableDataRCOTransaction() {
        TableView<ReservationBillPaymentDetail> tableView = new TableView();

//        TableColumn<ReservationBillPaymentDetail, Boolean> selectedStatus = new TableColumn("Status");
//        selectedStatus.setCellValueFactory(cellData -> cellData.getValue().detailSelectedProperty());
//        selectedStatus.setCellFactory(CheckBoxTableCell.forTableColumn(selectedStatus));
//        selectedStatus.setMinWidth(70);
//        selectedStatus.setEditable(true);
        TableColumn<ReservationBillPaymentDetail, String> detailDate = new TableColumn("Tanggal");
        detailDate.setCellValueFactory((TableColumn.CellDataFeatures<ReservationBillPaymentDetail, String> param)
                -> Bindings.createStringBinding(() -> ClassFormatter.dateTimeFormate.format(param.getValue().getDetailDate()),
                        param.getValue().detailDateProperty()));
        detailDate.setMinWidth(120);

        TableColumn<ReservationBillPaymentDetail, String> detailDescription = new TableColumn("Keterangan");
        detailDescription.setCellValueFactory((TableColumn.CellDataFeatures<ReservationBillPaymentDetail, String> param)
                -> Bindings.createStringBinding(() -> param.getValue().getDetailDescription(),
                        param.getValue().detailDescriptionProperty()));
        detailDescription.setMinWidth(280);

        TableColumn<ReservationBillPaymentDetail, String> detailDebit = new TableColumn("Debit");
        detailDebit.setCellValueFactory((TableColumn.CellDataFeatures<ReservationBillPaymentDetail, String> param)
                -> Bindings.createStringBinding(() -> ClassFormatter.currencyFormat.cFormat(param.getValue().getDetailDebit()),
                        param.getValue().detailDebitProperty()));
        detailDebit.setMinWidth(120);

        TableColumn<ReservationBillPaymentDetail, String> detailCreadit = new TableColumn("Kredit");
        detailCreadit.setCellValueFactory((TableColumn.CellDataFeatures<ReservationBillPaymentDetail, String> param)
                -> Bindings.createStringBinding(() -> ClassFormatter.currencyFormat.cFormat(param.getValue().getDetailCreadit()),
                        param.getValue().detailCreaditProperty()));
        detailCreadit.setMinWidth(120);

        tableView.getColumns().addAll(detailDate, detailDescription, detailDebit, detailCreadit);
        tableView.setItems(FXCollections.observableArrayList(dataRCOPayments));
        tableDataRCOTransaction = new ClassTableWithControl(tableView);

    }

    private void setTableControlDataRCOTransaction() {
        //set control from feature
        ObservableList<Node> buttonControls = FXCollections.observableArrayList();
        JFXButton buttonControl;
        if (true) {
            buttonControl = new JFXButton();
            buttonControl.setText("Buat Transaksi Pembayaran");
            buttonControl.setOnMouseClicked((e) -> {
                //listener add
                dataRCOTransactionCreateHandle();
            });
            buttonControls.add(buttonControl);
        }
//        if (true) {
//            buttonControl = new JFXButton();
//            buttonControl.setText("Cetak");
//            buttonControl.setOnMouseClicked((e) -> {
//                //listener print
//                dataRCOTransactionPrintHandle();
//            });
//            buttonControls.add(buttonControl);
//        }
        tableDataRCOTransaction.addButtonControl(buttonControls);
    }

    public ObservableList<ReservationBillPaymentDetail> loadAllDataRCOTransaction() {
        List<ReservationBillPaymentDetail> list = new ArrayList<>();
//        //rrtd
//        for (TblReservationRoomTypeDetail data : dataReservationRoomTypeDetails) {
//            list.add(new ReservationBillPaymentDetail(data));
//        }
        //rrtd - rpd (room price detail)
        for (TblReservationRoomTypeDetailRoomPriceDetail data : dataReservationRoomTypeDetailRoomPriceDetails) {
            list.add(new ReservationBillPaymentDetail(data));
        }
        //additional service
        for (TblReservationAdditionalService data : dataReservationAdditionalServices) {
            if (data.getRefReservationBillType().getIdtype() == 0 //Reservasi = '0'
                    || data.getRefReservationBillType().getIdtype() == 1) {  //Check Out = '1'
                list.add(new ReservationBillPaymentDetail(data));
            }
        }
        //additional item
        for (TblReservationAdditionalItem data : dataReservationAdditionalItems) {
            if (data.getRefReservationBillType().getIdtype() == 0 //Reservasi = '0'
                    || data.getRefReservationBillType().getIdtype() == 1) {  //Check Out = '1'
                list.add(new ReservationBillPaymentDetail(data));
            }
        }
        //broken item
        for (TblReservationBrokenItem data : dataReservationBrokenItems) {
            list.add(new ReservationBillPaymentDetail(data));
        }
        //payment
        for (TblReservationPayment data : dataReservationPayments) {
            list.add(new ReservationBillPaymentDetail(data));
        }
        for (TblReservationPayment data : dataCheckOutPayments) {
            list.add(new ReservationBillPaymentDetail(data));
        }
        //sorting by date
        sortingReservationBillPaymentDetailByDateASC(list);
        return FXCollections.observableArrayList(list);
    }

    private void sortingReservationBillPaymentDetailByDateASC(List<ReservationBillPaymentDetail> arr) {
        boolean swapped = true;
        int j = 0;
        ReservationBillPaymentDetail tmp;
        while (swapped) {
            swapped = false;
            j++;
            for (int i = 0; i < arr.size() - j; i++) {
//                LocalDate date1 = LocalDate.of(arr.get(i).getDetailDate().getYear() + 1900, 
//                        arr.get(i).getDetailDate().getMonth() + 1, 
//                        arr.get(i).getDetailDate().getDate());
//                LocalDate date2 = LocalDate.of(arr.get(i+1).getDetailDate().getYear() + 1900, 
//                        arr.get(i+1).getDetailDate().getMonth() + 1, 
//                        arr.get(i+1).getDetailDate().getDate());
//                if (date1.isAfter(date2)) {
                if (arr.get(i).getDetailDate().after(arr.get(i + 1).getDetailDate())) {
                    tmp = arr.get(i);
                    arr.set(i, arr.get(i + 1));
                    arr.set(i + 1, tmp);
                    swapped = true;
                }
            }
        }
    }

    public class ReservationBillPaymentDetail {

        private final BooleanProperty detailSelected = new SimpleBooleanProperty();

        private final BooleanProperty detailEnableToSelect = new SimpleBooleanProperty();

        private final ObjectProperty<java.util.Date> detailDate = new SimpleObjectProperty<>();

        private final StringProperty detailDescription = new SimpleStringProperty();

        private final ObjectProperty<BigDecimal> detailDebit = new SimpleObjectProperty<>();

        private final ObjectProperty<BigDecimal> detailCreadit = new SimpleObjectProperty<>();

        private final ObjectProperty<TblReservationRoomTypeDetail> detailRRD = new SimpleObjectProperty<>();

        private final ObjectProperty<TblReservationRoomTypeDetailRoomPriceDetail> detailRRDRPD = new SimpleObjectProperty<>();

        private final ObjectProperty<TblReservationAdditionalItem> detailAddtionalItem = new SimpleObjectProperty<>();

        private final ObjectProperty<TblReservationAdditionalService> detailAddtionalService = new SimpleObjectProperty<>();

        private final ObjectProperty<TblReservationBrokenItem> detailBrokenItem = new SimpleObjectProperty<>();

        private final ObjectProperty<TblReservationPayment> detailPayment = new SimpleObjectProperty<>();

        public ReservationBillPaymentDetail(TblReservationRoomTypeDetail rrtd) {
            //data selected

            //data date
            detailDate.set(rrtd.getCreateDate());
            //data description~~~
            detailDescription.set("K");
            //data debit
            BigDecimal nominal = calculationRoomTypeDetailCost(rrtd).subtract(calculationRoomTypeDetailCompliment(rrtd)).subtract(calculationRoomTypeDetailDiscount(rrtd));
            BigDecimal serviceCharge = nominal.multiply((dataReservationBill.getServiceChargePercentage()));
            BigDecimal tax = (nominal.add(serviceCharge)).multiply((dataReservationBill.getTaxPercentage()));
            detailDebit.set(nominal.add(serviceCharge).add(tax));
            //data creadit
            detailCreadit.set(new BigDecimal("0"));
            //data rrtd

        }

        public ReservationBillPaymentDetail(TblReservationRoomTypeDetailRoomPriceDetail rrtdrpd) {
            //data selected

            //data date
            detailDate.set(rrtdrpd.getCreateDate());
            //data description~~~
            String roomID = (rrtdrpd.getTblReservationRoomTypeDetail().getTblReservationCheckInOut() != null
                    && rrtdrpd.getTblReservationRoomTypeDetail().getTblReservationCheckInOut().getTblRoom() != null)
                            ? rrtdrpd.getTblReservationRoomTypeDetail().getTblReservationCheckInOut().getTblRoom().getRoomName()
                            : rrtdrpd.getTblReservationRoomTypeDetail().getCodeDetail();
            detailDescription.set("1 Kamar "
                    + rrtdrpd.getTblReservationRoomTypeDetail().getTblRoomType().getRoomTypeName() + " - "
                    + ClassFormatter.dateFormate.format(rrtdrpd.getTblReservationRoomPriceDetail().getDetailDate())
                    + "\n"
                    + "[ID Kamar:" + roomID
                    + (rrtdrpd.getTblReservationRoomPriceDetail().getDetailDiscountPercentage().compareTo(new BigDecimal("0")) == 1
                            ? (", Diskon:" + ClassFormatter.decimalFormat.cFormat(rrtdrpd.getTblReservationRoomPriceDetail().getDetailDiscountPercentage()) + "%") : "")
                    + (rrtdrpd.getTblReservationRoomPriceDetail().getDetailComplimentary().compareTo(new BigDecimal("0")) == 1
                            ? (", Compliment:" + ClassFormatter.currencyFormat.cFormat(rrtdrpd.getTblReservationRoomPriceDetail().getDetailComplimentary())) : "")
                    + "]"
            );
            //data debit
            BigDecimal nominal = ((new BigDecimal("1")).subtract((rrtdrpd.getTblReservationRoomPriceDetail().getDetailDiscountPercentage().divide(new BigDecimal("100")))))
                    .multiply(rrtdrpd.getTblReservationRoomPriceDetail().getDetailPrice().subtract(rrtdrpd.getTblReservationRoomPriceDetail().getDetailComplimentary()));
            BigDecimal serviceCharge = nominal.multiply((dataReservationBill.getServiceChargePercentage()));
            BigDecimal tax = (nominal.add(serviceCharge)).multiply((dataReservationBill.getTaxPercentage()));
            detailDebit.set(nominal.add(serviceCharge).add(tax));
            //data creadit
            detailCreadit.set(new BigDecimal("0"));
            //data rrtd

        }

        public ReservationBillPaymentDetail(TblReservationAdditionalItem additionalItem) {
            //data selected

            //data date
            detailDate.set(additionalItem.getCreateDate());
            //data description
            String roomID = (additionalItem.getTblReservationRoomTypeDetail().getTblReservationCheckInOut() != null
                    && additionalItem.getTblReservationRoomTypeDetail().getTblReservationCheckInOut().getTblRoom() != null)
                            ? additionalItem.getTblReservationRoomTypeDetail().getTblReservationCheckInOut().getTblRoom().getRoomName()
                            : additionalItem.getTblReservationRoomTypeDetail().getCodeDetail();
            detailDescription.set(ClassFormatter.decimalFormat.cFormat(additionalItem.getItemQuantity()) + " "
                    + additionalItem.getTblItem().getItemName() + " - "
                    + ClassFormatter.dateFormate.format(additionalItem.getAdditionalDate())
                    + "\n"
                    + "[ID Kamar:" + roomID
                    + (additionalItem.getDiscountPercentage().compareTo(new BigDecimal("0")) == 1
                            ? (", Diskon:" + ClassFormatter.decimalFormat.cFormat(additionalItem.getDiscountPercentage()) + "%") : "")
                    + "]"
            );
            //data debit
            BigDecimal nominal = ((new BigDecimal("1")).subtract((additionalItem.getDiscountPercentage().divide(new BigDecimal("100")))))
                    .multiply((additionalItem.getItemCharge().multiply(additionalItem.getItemQuantity())));
            BigDecimal serviceCharge = nominal.multiply((dataReservationBill.getServiceChargePercentage()));
            BigDecimal tax = (nominal.add(serviceCharge)).multiply((dataReservationBill.getTaxPercentage()));
            detailDebit.set(nominal.add(serviceCharge).add(tax));
            //data creadit
            detailCreadit.set(new BigDecimal("0"));
            //data additional item

        }

        public ReservationBillPaymentDetail(TblReservationAdditionalService additionalService) {
            //data selected

            //data date
            detailDate.set(additionalService.getCreateDate());
            //data description
            String roomID = (additionalService.getTblReservationRoomTypeDetail().getTblReservationCheckInOut() != null
                    && additionalService.getTblReservationRoomTypeDetail().getTblReservationCheckInOut().getTblRoom() != null)
                            ? additionalService.getTblReservationRoomTypeDetail().getTblReservationCheckInOut().getTblRoom().getRoomName()
                            : additionalService.getTblReservationRoomTypeDetail().getCodeDetail();
            LocalDate date = LocalDate.of(additionalService.getAdditionalDate().getYear() + 1900,
                    additionalService.getAdditionalDate().getMonth() + 1,
                    additionalService.getAdditionalDate().getDate());
            if (additionalService.getTblRoomService().getIdroomService() == 1) {   //Breakfast = '1'
                date = date.plusDays(1);
            }
            detailDescription.set("1 "
                    + additionalService.getTblRoomService().getServiceName() + " - "
                    + ClassFormatter.dateFormate.format(Date.valueOf(date))
                    + "\n"
                    + "[ID Kamar:" + roomID
                    + (additionalService.getDiscountPercentage().compareTo(new BigDecimal("0")) == 1
                            ? (", Diskon:" + ClassFormatter.decimalFormat.cFormat(additionalService.getDiscountPercentage()) + "%") : "")
                    + "]"
            );
            //data debit
            BigDecimal nominal = ((new BigDecimal("1")).subtract((additionalService.getDiscountPercentage().divide(new BigDecimal("100")))))
                    .multiply(additionalService.getPrice());
            BigDecimal serviceCharge = nominal.multiply((dataReservationBill.getServiceChargePercentage()));
            BigDecimal tax = (nominal.add(serviceCharge)).multiply((dataReservationBill.getTaxPercentage()));
            detailDebit.set(nominal.add(serviceCharge).add(tax));
            //data creadit
            detailCreadit.set(new BigDecimal("0"));
            //data additional service

        }

        public ReservationBillPaymentDetail(TblReservationBrokenItem brokenItem) {
            //data selected

            //data date
            detailDate.set(brokenItem.getCreateDate());
            //data description
            String roomID = (brokenItem.getTblReservationRoomTypeDetail().getTblReservationCheckInOut() != null
                    && brokenItem.getTblReservationRoomTypeDetail().getTblReservationCheckInOut().getTblRoom() != null)
                            ? brokenItem.getTblReservationRoomTypeDetail().getTblReservationCheckInOut().getTblRoom().getRoomName()
                            : brokenItem.getTblReservationRoomTypeDetail().getCodeDetail();
            detailDescription.set(ClassFormatter.decimalFormat.cFormat(brokenItem.getItemQuantity()) + " "
                    + brokenItem.getTblItem().getItemName() + " - "
                    + ClassFormatter.dateFormate.format(brokenItem.getCreateDate())
                    + "\n"
                    + "[ID Kamar:" + roomID
                    + "]"
            );
            //data debit
            BigDecimal nominal = (new BigDecimal("1"))
                    .multiply((brokenItem.getItemCharge().multiply(brokenItem.getItemQuantity())));
            BigDecimal serviceCharge = nominal.multiply((dataReservationBill.getServiceChargePercentage()));
            BigDecimal tax = (nominal.add(serviceCharge)).multiply((dataReservationBill.getTaxPercentage()));
            detailDebit.set(nominal.add(serviceCharge).add(tax));
            //data creadit
            detailCreadit.set(new BigDecimal("0"));
            //data broken item

        }

        public ReservationBillPaymentDetail(TblReservationPayment payment) {
            //data selected
            detailSelected.set(true);
            detailEnableToSelect.set(false);
            //data date
            detailDate.set(payment.getPaymentDate());
            //data description
            String additionDescription = "";
            switch (payment.getRefFinanceTransactionPaymentType().getIdtype()) {
                case 1:    //Transfer = '1'
                    TblReservationPaymentWithTransfer dataTransfer = fReservationManager.getReservationPaymentWithTransferByIDPayment(payment.getIdpayment());
                    additionDescription = "#" + dataTransfer.getIddetail();
                    break;
                case 2:    //Kartu Debit = '2'
                    TblReservationPaymentWithBankCard dataDebitCard = fReservationManager.getReservationPaymentWithBankCardByIDPayment(payment.getIdpayment());
                    additionDescription = "#XX" + dataDebitCard.getBankCardNumber().substring(dataDebitCard.getBankCardNumber().length() - 3, dataDebitCard.getBankCardNumber().length());
                    break;
                case 3:    //Kartu Kredit = '3'
                    TblReservationPaymentWithBankCard dataCreaditCard = fReservationManager.getReservationPaymentWithBankCardByIDPayment(payment.getIdpayment());
                    additionDescription = "#XX" + dataCreaditCard.getBankCardNumber().substring(dataCreaditCard.getBankCardNumber().length() - 3, dataCreaditCard.getBankCardNumber().length());
                    break;
            }
            detailDescription.set("Pembayaran : " + payment.getRefFinanceTransactionPaymentType().getTypeName()
                    + additionDescription);
            //data debit
            detailDebit.set(payment.getRoundingValue());
            //data creadit
            detailCreadit.set(payment.getUnitNominal());
            //data payment

        }

        public BooleanProperty detailSelectedProperty() {
            return detailSelected;
        }

        public boolean getDetailSelected() {
            return detailSelectedProperty().get();
        }

        public void setDetailSelected(boolean selected) {
            detailSelectedProperty().set(selected);
        }

        public ObjectProperty<java.util.Date> detailDateProperty() {
            return detailDate;
        }

        public java.util.Date getDetailDate() {
            return detailDateProperty().get();
        }

        public void setDetailDate(java.util.Date date) {
            detailDateProperty().set(date);
        }

        public StringProperty detailDescriptionProperty() {
            return detailDescription;
        }

        public String getDetailDescription() {
            return detailDescriptionProperty().get();
        }

        public void setDetailDescription(String description) {
            detailDescriptionProperty().set(description);
        }

        public ObjectProperty<BigDecimal> detailDebitProperty() {
            return detailDebit;
        }

        public BigDecimal getDetailDebit() {
            return detailDebitProperty().get();
        }

        public void setDetailDebit(BigDecimal debit) {
            detailDebitProperty().set(debit);
        }

        public ObjectProperty<BigDecimal> detailCreaditProperty() {
            return detailCreadit;
        }

        public BigDecimal getDetailCreadit() {
            return detailCreaditProperty().get();
        }

        public void setDetailCreadit(BigDecimal creadit) {
            detailCreaditProperty().set(creadit);
        }

        public ObjectProperty<TblReservationRoomTypeDetail> detailRRDProperty() {
            return detailRRD;
        }

        public TblReservationRoomTypeDetail getDetailRRD() {
            return detailRRDProperty().get();
        }

        public void setDetailRRD(TblReservationRoomTypeDetail rrtd) {
            detailRRDProperty().set(rrtd);
        }

        public ObjectProperty<TblReservationRoomTypeDetailRoomPriceDetail> detailRRDRPDProperty() {
            return detailRRDRPD;
        }

        public TblReservationRoomTypeDetailRoomPriceDetail getDetailRRDRPD() {
            return detailRRDRPDProperty().get();
        }

        public void setDetailRRDRPD(TblReservationRoomTypeDetailRoomPriceDetail rrtdrpd) {
            detailRRDRPDProperty().set(rrtdrpd);
        }

        public ObjectProperty<TblReservationAdditionalItem> detailAddtionalItemProperty() {
            return detailAddtionalItem;
        }

        public TblReservationAdditionalItem getDetailAddtionalItem() {
            return detailAddtionalItemProperty().get();
        }

        public void setDetailAddtionalItem(TblReservationAdditionalItem additionalItem) {
            detailAddtionalItemProperty().set(additionalItem);
        }

        public ObjectProperty<TblReservationAdditionalService> detailAddtionalServiceProperty() {
            return detailAddtionalService;
        }

        public TblReservationAdditionalService getDetailAddtionalService() {
            return detailAddtionalServiceProperty().get();
        }

        public void setDetailAddtionalService(TblReservationAdditionalService additionalService) {
            detailAddtionalServiceProperty().set(additionalService);
        }

        public ObjectProperty<TblReservationBrokenItem> detailBrokenItemProperty() {
            return detailBrokenItem;
        }

        public TblReservationBrokenItem getDetailBrokenItem() {
            return detailBrokenItemProperty().get();
        }

        public void setDetailBrokenItem(TblReservationBrokenItem brokenItem) {
            detailBrokenItemProperty().set(brokenItem);
        }

        public ObjectProperty<TblReservationPayment> detailPaymentProperty() {
            return detailPayment;
        }

        public TblReservationPayment getDetailPayment() {
            return detailPaymentProperty().get();
        }

        public void setDetailPayment(TblReservationPayment payment) {
            detailPaymentProperty().set(payment);
        }

    }

    /**
     * TABLE DATA RESERVATION ROOM TYPE DETAIL
     */
    public ClassTableWithControl tableDataReservationRoomTypeDetail;

    private void initTableDataReservationRoomTypeDetail() {
        //set table
        setTableDataReservationRoomTypeDetail();
        //set control
        setTableControlDataReservationRoomTypeDetail();
        //set table-control to content-view
        ancReservationRoomTypeDetailLayout.getChildren().clear();
        AnchorPane.setTopAnchor(tableDataReservationRoomTypeDetail, 0.0);
        AnchorPane.setLeftAnchor(tableDataReservationRoomTypeDetail, 0.0);
        AnchorPane.setBottomAnchor(tableDataReservationRoomTypeDetail, 0.0);
        AnchorPane.setRightAnchor(tableDataReservationRoomTypeDetail, 0.0);

        ancReservationRoomTypeDetailLayout.getChildren().add(tableDataReservationRoomTypeDetail);
    }

    private void setTableDataReservationRoomTypeDetail() {
        TableView<TblReservationRoomTypeDetail> tableView = new TableView();

        TableColumn<TblReservationRoomTypeDetail, String> codeDetail = new TableColumn("Kode");
        codeDetail.setCellValueFactory((TableColumn.CellDataFeatures<TblReservationRoomTypeDetail, String> param)
                -> Bindings.createStringBinding(() -> param.getValue().getCodeDetail(), param.getValue().codeDetailProperty()));
        codeDetail.setMinWidth(60);

        TableColumn<TblReservationRoomTypeDetail, String> roomTypeName = new TableColumn("Tipe Kamar");
        roomTypeName.setCellValueFactory((TableColumn.CellDataFeatures<TblReservationRoomTypeDetail, String> param)
                -> Bindings.createStringBinding(() -> param.getValue().getTblRoomType().getRoomTypeName(), param.getValue().tblRoomTypeProperty()));
        roomTypeName.setMinWidth(120);

        TableColumn<TblReservationRoomTypeDetail, String> roomName = new TableColumn("Kamar");
        roomName.setCellValueFactory((TableColumn.CellDataFeatures<TblReservationRoomTypeDetail, String> param)
                -> Bindings.createStringBinding(() -> param.getValue().getTblReservationCheckInOut() == null ? ""
                                : param.getValue().getTblReservationCheckInOut().getTblRoom() != null
                                        ? param.getValue().getTblReservationCheckInOut().getTblRoom().getRoomName() : "",
                        param.getValue().tblReservationCheckInOutProperty()));
        roomName.setMinWidth(120);

        TableColumn<TblReservationRoomTypeDetail, String> checkInOutStatus = new TableColumn("Status");
        checkInOutStatus.setCellValueFactory((TableColumn.CellDataFeatures<TblReservationRoomTypeDetail, String> param)
                -> Bindings.createStringBinding(() -> param.getValue().getTblReservationCheckInOut() == null ? ""
                                : param.getValue().getTblReservationCheckInOut().getRefReservationCheckInOutStatus().getStatusName(), param.getValue().tblReservationCheckInOutProperty()));
        checkInOutStatus.setMinWidth(100);

        TableColumn<TblReservationRoomTypeDetail, String> checkInOutStatusInfo = new TableColumn("Keterangan");
        checkInOutStatusInfo.setCellValueFactory((TableColumn.CellDataFeatures<TblReservationRoomTypeDetail, String> param)
                -> Bindings.createStringBinding(() -> getCheckInOutStatusInfo(param.getValue()), param.getValue().tblReservationCheckInOutProperty()));
        checkInOutStatusInfo.setMinWidth(200);

        TableColumn<TblReservationRoomTypeDetail, String> adultNumber = new TableColumn("Dewasa");
        adultNumber.setCellValueFactory((TableColumn.CellDataFeatures<TblReservationRoomTypeDetail, String> param)
                -> Bindings.createStringBinding(() -> param.getValue().getTblReservationCheckInOut() == null ? "0"
                                : param.getValue().getTblReservationCheckInOut().getAdultNumber().toString(), param.getValue().tblReservationCheckInOutProperty()));
        adultNumber.setMinWidth(80);

        TableColumn<TblReservationRoomTypeDetail, String> childNumber = new TableColumn("Anak");
        childNumber.setCellValueFactory((TableColumn.CellDataFeatures<TblReservationRoomTypeDetail, String> param)
                -> Bindings.createStringBinding(() -> param.getValue().getTblReservationCheckInOut() == null ? "0"
                                : param.getValue().getTblReservationCheckInOut().getChildNumber().toString(), param.getValue().tblReservationCheckInOutProperty()));
        childNumber.setMinWidth(80);

        TableColumn<TblReservationRoomTypeDetail, String> acNumberTitle = new TableColumn("Jumlah");
        acNumberTitle.getColumns().addAll(adultNumber, childNumber);

        TableColumn<TblReservationRoomTypeDetail, String> cardUsedNumber = new TableColumn("Digunakan");
        cardUsedNumber.setCellValueFactory((TableColumn.CellDataFeatures<TblReservationRoomTypeDetail, String> param)
                -> Bindings.createStringBinding(() -> param.getValue().getTblReservationCheckInOut() == null ? "0"
                                : param.getValue().getTblReservationCheckInOut().getCardUsed().toString(), param.getValue().tblReservationCheckInOutProperty()));
        cardUsedNumber.setMinWidth(100);

        TableColumn<TblReservationRoomTypeDetail, String> cardMissedNumber = new TableColumn("Rusak");
        cardMissedNumber.setCellValueFactory((TableColumn.CellDataFeatures<TblReservationRoomTypeDetail, String> param)
                -> Bindings.createStringBinding(() -> param.getValue().getTblReservationCheckInOut() == null ? "0"
                                : param.getValue().getTblReservationCheckInOut().getCardMissed().toString(), param.getValue().tblReservationCheckInOutProperty()));
        cardMissedNumber.setMinWidth(100);

        TableColumn<TblReservationRoomTypeDetail, String> ubGuestCardNumberTitle = new TableColumn("Jumlah Kartu");
        ubGuestCardNumberTitle.getColumns().addAll(cardUsedNumber, cardMissedNumber);

        TableColumn<TblReservationRoomTypeDetail, String> cardDeposit = new TableColumn("Deposit");
        cardDeposit.setCellValueFactory((TableColumn.CellDataFeatures<TblReservationRoomTypeDetail, String> param)
                -> Bindings.createStringBinding(() -> param.getValue().getTblReservationCheckInOut() == null ? "0"
                                : ClassFormatter.currencyFormat.cFormat((new BigDecimal(param.getValue().getTblReservationCheckInOut().getCardUsed()))
                                        .multiply(param.getValue().getTblReservationCheckInOut().getBrokenCardCharge())),
                        param.getValue().tblReservationCheckInOutProperty()));
        cardDeposit.setMinWidth(140);

        TableColumn<TblReservationRoomTypeDetail, String> cardCharge = new TableColumn("Biaya Kartu");
        cardCharge.setCellValueFactory((TableColumn.CellDataFeatures<TblReservationRoomTypeDetail, String> param)
                -> Bindings.createStringBinding(() -> param.getValue().getTblReservationCheckInOut() == null ? "0"
                                : ClassFormatter.currencyFormat.cFormat((new BigDecimal(param.getValue().getTblReservationCheckInOut().getCardMissed()))
                                        .multiply(param.getValue().getTblReservationCheckInOut().getBrokenCardCharge())),
                        param.getValue().tblReservationCheckInOutProperty()));
        cardCharge.setMinWidth(140);

        TableColumn<TblReservationRoomTypeDetail, String> checkInDate = new TableColumn("Check In");
        checkInDate.setCellValueFactory((TableColumn.CellDataFeatures<TblReservationRoomTypeDetail, String> param)
                -> Bindings.createStringBinding(() -> ClassFormatter.dateTimeFormate.format(param.getValue().getCheckInDateTime()), param.getValue().checkInDateTimeProperty()));
        checkInDate.setMinWidth(125);

        TableColumn<TblReservationRoomTypeDetail, String> checkOutDate = new TableColumn("Check Out");
        checkOutDate.setCellValueFactory((TableColumn.CellDataFeatures<TblReservationRoomTypeDetail, String> param)
                -> Bindings.createStringBinding(() -> ClassFormatter.dateTimeFormate.format(param.getValue().getCheckOutDateTime()), param.getValue().checkOutDateTimeProperty()));
        checkOutDate.setMinWidth(125);

        TableColumn<TblReservationRoomTypeDetail, String> ioDateTitle = new TableColumn("Tanggal");
        ioDateTitle.getColumns().addAll(checkInDate, checkOutDate);

        TableColumn<TblReservationRoomTypeDetail, String> roomTypeDetailCost = new TableColumn("Harga");
        roomTypeDetailCost.setCellValueFactory((TableColumn.CellDataFeatures<TblReservationRoomTypeDetail, String> param)
                -> Bindings.createStringBinding(() -> ClassFormatter.currencyFormat.cFormat(calculationRoomTypeDetailCost(param.getValue())),
                        param.getValue().iddetailProperty()));
        roomTypeDetailCost.setMinWidth(100);

        TableColumn<TblReservationRoomTypeDetail, String> roomTypeDetailDiscount = new TableColumn("Diskon");
        roomTypeDetailDiscount.setCellValueFactory((TableColumn.CellDataFeatures<TblReservationRoomTypeDetail, String> param)
                -> Bindings.createStringBinding(() -> ClassFormatter.currencyFormat.cFormat(calculationRoomTypeDetailDiscount(param.getValue())),
                        param.getValue().lastUpdateDateProperty()));
        roomTypeDetailDiscount.setMinWidth(100);

        TableColumn<TblReservationRoomTypeDetail, String> roomTypeDetailCompliment = new TableColumn("Compliment");
        roomTypeDetailCompliment.setCellValueFactory((TableColumn.CellDataFeatures<TblReservationRoomTypeDetail, String> param)
                -> Bindings.createStringBinding(() -> ClassFormatter.currencyFormat.cFormat(calculationRoomTypeDetailCompliment(param.getValue())),
                        param.getValue().iddetailProperty()));
        roomTypeDetailCompliment.setMinWidth(100);

        tableView.getColumns().addAll(codeDetail, roomTypeName,
                ioDateTitle,
                roomTypeDetailCost, roomTypeDetailDiscount, roomTypeDetailCompliment,
                //                roomName, checkInOutStatus, acNumberTitle,
                //                ubGuestCardNumberTitle
                checkInOutStatusInfo);
        tableView.setItems(loadAllDataReservationRoomTypeDetail());
        tableDataReservationRoomTypeDetail = new ClassTableWithControl(tableView);

    }

    private String getCheckInOutStatusInfo(TblReservationRoomTypeDetail dataReservationDetail) {
        String result = "";
        if (dataReservationDetail.getTblReservationCheckInOut() != null) {
            result += "Status : " + dataReservationDetail.getTblReservationCheckInOut().getRefReservationCheckInOutStatus().getStatusName();
            if (dataReservationDetail.getTblReservationCheckInOut().getTblRoom() != null) {
                result += "\n";
                result += "Kamar : " + dataReservationDetail.getTblReservationCheckInOut().getTblRoom().getRoomName();
                if (dataReservationDetail.getTblReservationCheckInOut().getRefReservationCheckInOutStatus().getIdstatus() != 0) {  //Ready to Check In = '0'
                    result += "\n";
                    result += "Jumlah Dewasa/Anak : " + dataReservationDetail.getTblReservationCheckInOut().getAdultNumber() + "/" + dataReservationDetail.getTblReservationCheckInOut().getChildNumber();
                    result += "\n";
                    result += "Jumlah Kartu Digunakan : " + dataReservationDetail.getTblReservationCheckInOut().getCardUsed();
                    result += "\n";
                    result += "Jumlah Kartu Rusak : " + dataReservationDetail.getTblReservationCheckInOut().getCardMissed();
                }
            } else {
                result += "";
            }
        }
        return result;
    }

    private void setTableControlDataReservationRoomTypeDetail() {
        //set control from feature
        ObservableList<Node> buttonControls = FXCollections.observableArrayList();
        JFXButton buttonControl;
        if (dataInputStatus == 0 //insert
                || selectedData.getRefReservationStatus().getIdstatus() == 1) { //Reserved = '1'
            buttonControl = new JFXButton();
            buttonControl.setText("Tambah");
            buttonControl.setOnMouseClicked((e) -> {
                //listener add
//                dataReservationRoomTypeDetailReservationTypeChooseHandle();
                dataReservationRoomTypeDetailCreateHandle();
            });
            buttonControls.add(buttonControl);
        }

//        if (selectedData.getRefReservationStatus().getIdstatus() == 1) {  //Reserved = '1'
//            buttonControl = new JFXButton();
//            buttonControl.setText("Ubah");
//            buttonControl.setOnMouseClicked((e) -> {
//                //listener update
//                dataReservationRoomTypeDetailUpdateHandle();
//            });
//            buttonControls.add(buttonControl);
//        }
        if (dataInputStatus == 0 //insert
                || selectedData.getRefReservationStatus().getIdstatus() == 1) { //Reserved = '1'
            buttonControl = new JFXButton();
            buttonControl.setText("Hapus");
            buttonControl.setOnMouseClicked((e) -> {
                //listener delete
                dataReservationRoomTypeDetailDeleteHandle();
            });
            buttonControls.add(buttonControl);
        }

//        if (true) {
//            buttonControl = new JFXButton();
//            buttonControl.setText("Ubah Tipe Kamar");
//            buttonControl.setOnMouseClicked((e) -> {
//                //listener change room type (before checkin)
//                dataReservationRoomTypeDetailChangeRoomTypeHandle();
//            });
//            buttonControls.add(buttonControl);
//        }
        if (dataInputStatus == 1 //update
                && selectedData.getRefReservationStatus().getIdstatus() == 2) { //Booked = '2'
            buttonControl = new JFXButton();
            buttonControl.setText("Pindah Kamar");
            buttonControl.setOnMouseClicked((e) -> {
                //listener change room (after checked in)
                dataReservationRoomTypeDetailChangeRoomHandle();
            });
            buttonControls.add(buttonControl);
        }

        if (dataInputStatus == 1 //update
                && selectedData.getRefReservationStatus().getIdstatus() == 2) { //Booked = '2'
            buttonControl = new JFXButton();
            buttonControl.setText("Check-In");
            buttonControl.setOnMouseClicked((e) -> {
                //listener check in
                dataReservationRoomTypeDetailCheckInHandle();
            });
            buttonControls.add(buttonControl);
        }

        if (dataInputStatus == 1 //update
                && selectedData.getRefReservationStatus().getIdstatus() == 2) { //Booked = '2'
            buttonControl = new JFXButton();
            buttonControl.setText("Tambah Kartu Kamar");
            buttonControl.setOnMouseClicked((e) -> {
                //listener add card used - number
                dataReservationRoomTypeDetailCheckInAddCardUsedNumberHandle();
            });
            buttonControls.add(buttonControl);
        }

        if (dataInputStatus == 1 //update
                && selectedData.getRefReservationStatus().getIdstatus() == 2) { //Booked = '2'
            buttonControl = new JFXButton();
            buttonControl.setText("Extend Kamar");
            buttonControl.setOnMouseClicked((e) -> {
                //listener extend room
                dataReservationRoomTypeDetailCheckInExtendRoomHandle();
            });
            buttonControls.add(buttonControl);
        }

        if (dataInputStatus == 1 //update
                && selectedData.getRefReservationStatus().getIdstatus() == 2) { //Booked = '2'
            buttonControl = new JFXButton();
            buttonControl.setText("Check-Out");
            buttonControl.setOnMouseClicked((e) -> {
                //listener check out
                dataReservationRoomTypeDetailCheckOutHandle();
            });
            buttonControls.add(buttonControl);
        }

        if (dataInputStatus == 1 //update
                && selectedData.getRefReservationStatus().getIdstatus() == 2) { //Booked = '2'
            buttonControl = new JFXButton();
            buttonControl.setText("Multi Check-Out");
            buttonControl.setOnMouseClicked((e) -> {
                //listener multi-check out
                dataReservationRoomTypeDetailMultiCheckOutHandle();
            });
            buttonControls.add(buttonControl);
        }

        tableDataReservationRoomTypeDetail.addButtonControl(buttonControls);
    }

    private ObservableList<TblReservationRoomTypeDetail> loadAllDataReservationRoomTypeDetail() {
        return FXCollections.observableArrayList(dataReservationRoomTypeDetails);
    }

    /**
     * TABLE DATA RESERVATION ADDITIONAL ITEM
     */
    public ClassTableWithControl tableDataReservationAdditionalItem;

    private void initTableDataReservationAdditionalItem() {
        //set table
        setTableDataReservationAdditionalItem();
        //set control
        setTableControlDataReservationAdditionalItem();
        //set table-control to content-view
        ancReservationRoomAdditionalItemLayout.getChildren().clear();
        AnchorPane.setTopAnchor(tableDataReservationAdditionalItem, 0.0);
        AnchorPane.setLeftAnchor(tableDataReservationAdditionalItem, 0.0);
        AnchorPane.setBottomAnchor(tableDataReservationAdditionalItem, 0.0);
        AnchorPane.setRightAnchor(tableDataReservationAdditionalItem, 0.0);

        ancReservationRoomAdditionalItemLayout.getChildren().add(tableDataReservationAdditionalItem);
    }

    private void setTableDataReservationAdditionalItem() {
        TableView<TblReservationAdditionalItem> tableView = new TableView();

        TableColumn<TblReservationAdditionalItem, String> codeDetail = new TableColumn("Kode");
        codeDetail.setCellValueFactory((TableColumn.CellDataFeatures<TblReservationAdditionalItem, String> param)
                -> Bindings.createStringBinding(() -> param.getValue().getTblReservationRoomTypeDetail().getCodeDetail(), param.getValue().tblReservationRoomTypeDetailProperty()));
        codeDetail.setMinWidth(60);

        TableColumn<TblReservationAdditionalItem, String> additionalDate = new TableColumn("Tanggal");
        additionalDate.setCellValueFactory((TableColumn.CellDataFeatures<TblReservationAdditionalItem, String> param)
                -> Bindings.createStringBinding(() -> ClassFormatter.dateFormate.format(param.getValue().getAdditionalDate()), param.getValue().additionalDateProperty()));
        additionalDate.setMinWidth(100);

        TableColumn<TblReservationAdditionalItem, String> itemName = new TableColumn("Barang");
        itemName.setCellValueFactory((TableColumn.CellDataFeatures<TblReservationAdditionalItem, String> param)
                -> Bindings.createStringBinding(() -> param.getValue().getTblItem().getItemName(), param.getValue().tblItemProperty()));
        itemName.setMinWidth(140);

        TableColumn<TblReservationAdditionalItem, String> itemQuantity = new TableColumn("Jumlah");
        itemQuantity.setCellValueFactory((TableColumn.CellDataFeatures<TblReservationAdditionalItem, String> param)
                -> Bindings.createStringBinding(() -> ClassFormatter.decimalFormat.cFormat(param.getValue().getItemQuantity()), param.getValue().itemQuantityProperty()));
        itemQuantity.setMinWidth(120);

        TableColumn<TblReservationAdditionalItem, String> itemUnit = new TableColumn("Satuan");
        itemUnit.setCellValueFactory((TableColumn.CellDataFeatures<TblReservationAdditionalItem, String> param)
                -> Bindings.createStringBinding(() -> param.getValue().getTblItem().getTblUnit().getUnitName(),
                        param.getValue().getTblItem().tblUnitProperty()));
        itemUnit.setMinWidth(120);

        TableColumn<TblReservationAdditionalItem, String> additionalCharge = new TableColumn("Biaya");
        additionalCharge.setCellValueFactory((TableColumn.CellDataFeatures<TblReservationAdditionalItem, String> param)
                -> Bindings.createStringBinding(() -> ClassFormatter.currencyFormat.cFormat(param.getValue().getItemCharge()), param.getValue().itemChargeProperty()));
        additionalCharge.setMinWidth(100);

        TableColumn<TblReservationAdditionalItem, String> totalAdditionalCharge = new TableColumn("Total Biaya");
        totalAdditionalCharge.setCellValueFactory((TableColumn.CellDataFeatures<TblReservationAdditionalItem, String> param)
                -> Bindings.createStringBinding(() -> ClassFormatter.currencyFormat.cFormat(param.getValue().getItemCharge().multiply(param.getValue().getItemQuantity())),
                        param.getValue().idadditionalProperty()));
        totalAdditionalCharge.setMinWidth(100);

        TableColumn<TblReservationAdditionalItem, String> totalAdditionalDiscount = new TableColumn("Total Diskon");
        totalAdditionalDiscount.setCellValueFactory((TableColumn.CellDataFeatures<TblReservationAdditionalItem, String> param)
                -> Bindings.createStringBinding(() -> ClassFormatter.currencyFormat.cFormat((param.getValue().getItemCharge().multiply(param.getValue().getItemQuantity()))
                                .multiply(param.getValue().getDiscountPercentage().divide(new BigDecimal("100")))),
                        param.getValue().idadditionalProperty()));
        totalAdditionalDiscount.setMinWidth(100);

        TableColumn<TblReservationAdditionalItem, String> additionalType = new TableColumn("Status");
        additionalType.setCellValueFactory((TableColumn.CellDataFeatures<TblReservationAdditionalItem, String> param)
                -> Bindings.createStringBinding(() -> param.getValue().getRefReservationBillType().getTypeName(), param.getValue().refReservationBillTypeProperty()));
        additionalType.setMinWidth(100);

        tableView.getColumns().addAll(codeDetail, additionalDate, itemName, additionalCharge, itemQuantity, itemUnit, totalAdditionalCharge, totalAdditionalDiscount, additionalType);
        tableView.setItems(loadAllDataReservationAdditionalItem());
        tableDataReservationAdditionalItem = new ClassTableWithControl(tableView);

    }

    private void setTableControlDataReservationAdditionalItem() {
        //set control from feature
        ObservableList<Node> buttonControls = FXCollections.observableArrayList();
        JFXButton buttonControl;
        if (true) {
            buttonControl = new JFXButton();
            buttonControl.setText("Tambah");
            buttonControl.setOnMouseClicked((e) -> {
                //listener add
                dataReservationAdditionalItemCreateHandle();
            });
            buttonControls.add(buttonControl);
        }

//        if (true) {
//            buttonControl = new JFXButton();
//            buttonControl.setText("Ubah");
//            buttonControl.setOnMouseClicked((e) -> {
//                //listener update
//                dataReservationAdditionalItemUpdateHandle();
//            });
//            buttonControls.add(buttonControl);
//        }
        if (true) {
            buttonControl = new JFXButton();
            buttonControl.setText("Hapus");
            buttonControl.setOnMouseClicked((e) -> {
                //listener delete
                dataReservationAdditionalItemDeleteHandle();
            });
            buttonControls.add(buttonControl);
        }

        tableDataReservationAdditionalItem.addButtonControl(buttonControls);
    }

    private ObservableList<TblReservationAdditionalItem> loadAllDataReservationAdditionalItem() {
        return FXCollections.observableArrayList(dataReservationAdditionalItems);
    }

    /**
     * TABLE DATA RESERVATION ADDITIONAL SERVICE
     */
    public ClassTableWithControl tableDataReservationAdditionalService;

    private void initTableDataReservationAdditionalService() {
        //set table
        setTableDataReservationAdditionalService();
        //set control
        setTableControlDataReservationAdditionalService();
        //set table-control to content-view
        ancReservationRoomAdditionalServiceLayout.getChildren().clear();
        AnchorPane.setTopAnchor(tableDataReservationAdditionalService, 0.0);
        AnchorPane.setLeftAnchor(tableDataReservationAdditionalService, 0.0);
        AnchorPane.setBottomAnchor(tableDataReservationAdditionalService, 0.0);
        AnchorPane.setRightAnchor(tableDataReservationAdditionalService, 0.0);

        ancReservationRoomAdditionalServiceLayout.getChildren().add(tableDataReservationAdditionalService);
    }

    private void setTableDataReservationAdditionalService() {
        TableView<TblReservationAdditionalService> tableView = new TableView();

        TableColumn<TblReservationAdditionalService, String> codeDetail = new TableColumn("Kode");
        codeDetail.setCellValueFactory((TableColumn.CellDataFeatures<TblReservationAdditionalService, String> param)
                -> Bindings.createStringBinding(() -> param.getValue().getTblReservationRoomTypeDetail().getCodeDetail(), param.getValue().tblReservationRoomTypeDetailProperty()));
        codeDetail.setMinWidth(60);

        TableColumn<TblReservationAdditionalService, String> additionalDate = new TableColumn("Tanggal");
        additionalDate.setCellValueFactory((TableColumn.CellDataFeatures<TblReservationAdditionalService, String> param)
                -> Bindings.createStringBinding(()
                        -> param.getValue().getTblRoomService().getIdroomService() != 1
                                ? ClassFormatter.dateFormate.format(param.getValue().getAdditionalDate())
                                : ClassFormatter.dateFormate.format(Date.valueOf(
                                                LocalDate.of(
                                                        param.getValue().getAdditionalDate().getYear() + 1900,
                                                        param.getValue().getAdditionalDate().getMonth() + 1,
                                                        param.getValue().getAdditionalDate().getDate())
                                                .plusDays(1))),
                        param.getValue().additionalDateProperty()));
        additionalDate.setMinWidth(100);

        TableColumn<TblReservationAdditionalService, String> serviceName = new TableColumn("Layanan");
        serviceName.setCellValueFactory((TableColumn.CellDataFeatures<TblReservationAdditionalService, String> param)
                -> Bindings.createStringBinding(() -> param.getValue().getTblRoomService().getServiceName(), param.getValue().tblRoomServiceProperty()));
        serviceName.setMinWidth(140);

        TableColumn<TblReservationAdditionalService, String> additionalCharge = new TableColumn("Biaya");
        additionalCharge.setCellValueFactory((TableColumn.CellDataFeatures<TblReservationAdditionalService, String> param)
                -> Bindings.createStringBinding(() -> ClassFormatter.currencyFormat.cFormat(param.getValue().getPrice()), param.getValue().priceProperty()));
        additionalCharge.setMinWidth(100);

        TableColumn<TblReservationAdditionalService, String> additionalDiscount = new TableColumn("Diskon");
        additionalDiscount.setCellValueFactory((TableColumn.CellDataFeatures<TblReservationAdditionalService, String> param)
                -> Bindings.createStringBinding(() -> ClassFormatter.currencyFormat.cFormat(param.getValue().getPrice()
                                .multiply(param.getValue().getDiscountPercentage().divide(new BigDecimal("100")))),
                        param.getValue().idadditionalProperty()));
        additionalDiscount.setMinWidth(100);

        TableColumn<TblReservationAdditionalService, String> additionalType = new TableColumn("Status");
        additionalType.setCellValueFactory((TableColumn.CellDataFeatures<TblReservationAdditionalService, String> param)
                -> Bindings.createStringBinding(() -> param.getValue().getRefReservationBillType().getTypeName(), param.getValue().refReservationBillTypeProperty()));
        additionalType.setMinWidth(100);

        TableColumn<TblReservationAdditionalService, String> additionalNote = new TableColumn("Keterangan");
        additionalNote.setCellValueFactory((TableColumn.CellDataFeatures<TblReservationAdditionalService, String> param)
                -> Bindings.createStringBinding(() -> param.getValue().getNote() != null
                                ? param.getValue().getNote() : "-",
                        param.getValue().noteProperty()));
        additionalNote.setMinWidth(200);

        tableView.getColumns().addAll(codeDetail, additionalDate, serviceName, additionalCharge, additionalDiscount, additionalType, additionalNote);
        tableView.setItems(loadAllDataReservationAdditionalService());
        tableDataReservationAdditionalService = new ClassTableWithControl(tableView);

    }

    private void setTableControlDataReservationAdditionalService() {
        //set control from feature
        ObservableList<Node> buttonControls = FXCollections.observableArrayList();
        JFXButton buttonControl;
        if (true) {
            buttonControl = new JFXButton();
            buttonControl.setText("Tambah");
            buttonControl.setOnMouseClicked((e) -> {
                //listener add
                dataReservationAdditionalServiceCreateHandle();
            });
            buttonControls.add(buttonControl);
        }
//        if (true) {
//            buttonControl = new JFXButton();
//            buttonControl.setText("Ubah");
//            buttonControl.setOnMouseClicked((e) -> {
//                //listener update
//                dataReservationAdditionalServiceUpdateHandle();
//            });
//            buttonControls.add(buttonControl);
//        }
        if (true) {
            buttonControl = new JFXButton();
            buttonControl.setText("Hapus");
            buttonControl.setOnMouseClicked((e) -> {
                //listener delete
                dataReservationAdditionalServiceDeleteHandle();
            });
            buttonControls.add(buttonControl);
        }

        tableDataReservationAdditionalService.addButtonControl(buttonControls);
    }

    private ObservableList<TblReservationAdditionalService> loadAllDataReservationAdditionalService() {
        return FXCollections.observableArrayList(dataReservationAdditionalServices);
    }

    /**
     * HANDLE DIALOG
     */
    public Stage dialogStage;

    /**
     * HANDLE FOR DATA INPUT CUSTOMER
     */
    //0 = 'insert'
    //1 = 'update'
    public TblCustomer selectedDataCustomer;

    public int dataCustomerInputStatus;

    private void dataCustomerCreateHandle() {
        dataCustomerInputStatus = 0;
        selectedDataCustomer = new TblCustomer();
        selectedDataCustomer.setRegistrationDate(Date.valueOf(LocalDate.now()));
        selectedDataCustomer.setTblPeople(new TblPeople());
        selectedDataCustomer.setDeposit(new BigDecimal("0"));
        //open form data customer dialog
        showCustomerDialog();
    }

    private void dataCustomerUpdateHandle() {
        if (selectedData.getTblCustomer().getTblPeople().getFullName() != null) {
            dataCustomerInputStatus = 1;
            selectedDataCustomer = fReservationManager.getCustomer(selectedData.getTblCustomer().getIdcustomer());
            selectedDataCustomer.setTblPeople(fReservationManager.getPeople(selectedDataCustomer.getTblPeople().getIdpeople()));
            //open form data customer dialog
            showCustomerDialog();
        } else {
            HotelFX.showAlert(Alert.AlertType.WARNING, "TIDAK ADA DATA CUSTOMER YANG DIPILIH", "Pilih data customer terlebih dahulu..!", null);
        }
    }

    private void showCustomerDialog() {
        try {
            // Load the fxml file and create a new stage for the popup dialog.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(HotelFX.class.getResource("view/feature_reservation/CustomerInputDialog.fxml"));

            CustomerInputController controller = new CustomerInputController(this);
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
     * HANDLE FOR DATA INPUT DEPOSIT
     */
    private void dataDepositCreateHandle() {
        if (selectedData.getTblCustomer() != null) {
            //data customer
            selectedDataCustomer = fReservationManager.getCustomer(selectedData.getTblCustomer().getIdcustomer());
            selectedDataCustomer.setTblPeople(fReservationManager.getPeople(selectedDataCustomer.getIdcustomer()));
            //data transaction (open:deposit)
            selectedDataTransaction = new TblReservationPayment();
            selectedDataTransaction.setUnitNominal(new BigDecimal("0"));
            selectedDataTransaction.setRoundingValue(new BigDecimal("0"));
            //open deposit (cash)
            selectedDataTransaction.setRefFinanceTransactionPaymentType(fReservationManager.getFinanceTransactionPaymentType(13));
            //open form data deposit dialog
            showOpenDepositDialog();
        } else {
            HotelFX.showAlert(Alert.AlertType.WARNING, "TIDAK DATA CUSTOMER YANG DIPILIH", "Pilih data customer terlebih dahulu..!", null);
        }
    }

    private void showOpenDepositDialog() {
        try {
            // Load the fxml file and create a new stage for the popup dialog.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(HotelFX.class.getResource("view/feature_reservation/DepositInputDialog.fxml"));

            DepositInputController controller = new DepositInputController(this);
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

    private void dataDepositRemoveHandle() {
        if (selectedData.getTblCustomer() != null) {
            //data customer
            selectedDataCustomer = fReservationManager.getCustomer(selectedData.getTblCustomer().getIdcustomer());
            selectedDataCustomer.setTblPeople(fReservationManager.getPeople(selectedDataCustomer.getIdcustomer()));
            //data transaction (close:deposit)
            selectedDataTransaction = new TblReservationPayment();
            selectedDataTransaction.setUnitNominal(new BigDecimal("0"));
            selectedDataTransaction.setRoundingValue(new BigDecimal("0"));
            //close deposit (cash)
            selectedDataTransaction.setRefFinanceTransactionPaymentType(fReservationManager.getFinanceTransactionPaymentType(14));
            //open form data close deposit dialog
            showCloseDepositDialog();
        } else {
            HotelFX.showAlert(Alert.AlertType.WARNING, "TIDAK DATA CUSTOMER YANG DIPILIH", "Pilih data customer terlebih dahulu..!", null);
        }
    }

    private void showCloseDepositDialog() {
        try {
            // Load the fxml file and create a new stage for the popup dialog.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(HotelFX.class.getResource("view/feature_reservation/CloseDepositInputDialog.fxml"));

            CloseDepositInputController controller = new CloseDepositInputController(this);
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
     * HANDLE FOR DATA INPUT RESERVATION-CHECKOUT DISCOUNT TYPE
     */
    //'reservation' or 'checkout' or 'rco'
    public String dataInputBillStatus;

    private void dataReservationBillDiscountTypeUpdateHandle() {
        if (selectedData.getTblPartner() == null) {
            if (selectedData.getRefReservationStatus().getIdstatus() == 1) {    //Reserved = '1'
                dataInputBillStatus = "reservation";
                //open form data reservation-checkout bill discount type dialog
                showReservationBillDiscountTypeDialog();
            } else {
                HotelFX.showAlert(Alert.AlertType.WARNING, "TIDAK DAPAT MENGUBAH DATA TIPE DISKON (TAGIHAN RESERVASI)",
                        "Perubahan data tipe diskon (reservasi) hanya dapat dilakukan \n pada saat status reservasi : 'Reserved'..!", null);
            }
        } else {
            HotelFX.showAlert(Alert.AlertType.WARNING, "TIDAK DAPAT MENGUBAH DATA TIPE DISKON (TAGIHAN RESERVASI)",
                    "Perubahan data tipe diskon (reservasi) tidak dapat dilakukan \n pada data reservasi oleh travel agent ..!", null);
        }
    }

    private void dataCheckOutBillDiscountTypeUpdateHandle() {
        if (selectedData.getTblPartner() == null) {
            if (selectedData.getRefReservationStatus().getIdstatus() == 2) {    //Booked = '2'
                dataInputBillStatus = "checkout";
                //open form data reservation-checkout bill discount type dialog
                showReservationBillDiscountTypeDialog();
            } else {
                HotelFX.showAlert(Alert.AlertType.WARNING, "TIDAK DAPAT MENGUBAH DATA TIPE DISKON (TAGIHAN CHECK-OUT)",
                        "Perubahan data tipe diskon (check-out) hanya dapat dilakukan \n pada saat status reservasi : 'Booked'..!", null);
            }
        } else {
            HotelFX.showAlert(Alert.AlertType.WARNING, "TIDAK DAPAT MENGUBAH DATA TIPE DISKON (TAGIHAN RESERVASI)",
                    "Perubahan data tipe diskon (reservasi) tidak dapat dilakukan \n pada data reservasi oleh travel agent ..!", null);
        }
    }

    //RCO###
    private void dataRCOBillDiscountTypeUpdateHandle() {
        if (selectedData.getTblPartner() == null) {
            if (selectedData.getRefReservationStatus().getIdstatus() == 1) {    //Reserved = '1'
                dataInputBillStatus = "reservation";
                //open form data reservation-checkout bill discount type dialog
                showReservationBillDiscountTypeDialog();
            } else {
                HotelFX.showAlert(Alert.AlertType.WARNING, "TIDAK DAPAT MENGUBAH DATA TIPE DISKON (TAGIHAN RESERVASI)",
                        "Perubahan data tipe diskon (reservasi) hanya dapat dilakukan \n pada saat status reservasi : 'Reserved'..!", null);
            }
        } else {
            HotelFX.showAlert(Alert.AlertType.WARNING, "TIDAK DAPAT MENGUBAH DATA TIPE DISKON (TAGIHAN RESERVASI)",
                    "Perubahan data tipe diskon (reservasi) tidak dapat dilakukan \n pada data reservasi oleh travel agent ..!", null);
        }
    }

    private void showReservationBillDiscountTypeDialog() {
        try {
            // Load the fxml file and create a new stage for the popup dialog.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(HotelFX.class.getResource("view/feature_reservation/ReservationBillDiscountTypeInputDialog.fxml"));

            ReservationBillDiscountTypeInputController controller = new ReservationBillDiscountTypeInputController(this);
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
     * HANDLE FOR DATA INPUT RESERVATION-CHECKOUT TRANSACTION
     */
    //'reservation' or 'checkout'
    public String dataInputTransactionStatus;

    public TblReservationPayment selectedDataTransaction;

    public TblReservationPaymentWithTransfer selectedDataTransactionWithTransfer;

    public TblReservationPaymentWithBankCard selectedDataTransactionWithBankCard;

    public TblReservationPaymentWithCekGiro selectedDataTransactionWithCekGiro;

    public TblReservationPaymentWithGuaranteePayment selectedDataTransactionWithGuaranteePayment;

    public TblReservationPaymentWithReservationVoucher selectedDataTransactionWithReservationVoucher;

    private void dataReservationTransactionCreateHandle() {
        if (dataInputStatus == 1) { //update
            if (selectedData.getRefReservationStatus().getIdstatus() == 1 //Reserved = '1'
                    || (selectedData.getRefReservationStatus().getIdstatus() == 2 //Booked = '1'
                    && calculationTotalRestOfBill("reservation").compareTo(new BigDecimal("0")) == 1)) {   //need something to pay 
                dataInputTransactionStatus = "reservation";
                resetDataTransaction(dataInputTransactionStatus);
                //open form data reservation-checkout transaction dialog
                showReservationTransactionDialog();
            } else {
                HotelFX.showAlert(Alert.AlertType.WARNING, "TIDAK DAPAT MENAMBAH DATA TRANSAKSI (RESERVASI)",
                        "Penambahan data transaksi (reservasi) hanya dapat dilakukan \n pada saat status reservasi : 'Reserved'..!", null);
            }
        } else {
            HotelFX.showAlert(Alert.AlertType.WARNING, "SIMPAN DATA TERLEBIH DAHULU", "Silahkan simpan data reservasi terlebih dahulu..!", null);
        }
    }

    public void dataReservationTransactionPrintHandle(int language) {
        if (dataReservationBill.getTblReservation().getIdreservation() != 0L) {
            String hotelName = "";
            SysDataHardCode sdhHotelName = fReservationManager.getDataSysDataHardCode((long) 12);  //HotelName = '12'
            if (sdhHotelName != null
                    && sdhHotelName.getDataHardCodeValue() != null) {
                hotelName = sdhHotelName.getDataHardCodeValue();
            }
            String hotelAddress = "";
            SysDataHardCode sdhHotelAddress = fReservationManager.getDataSysDataHardCode((long) 13);  //HotelAddress = '13'
            if (sdhHotelAddress != null
                    && sdhHotelAddress.getDataHardCodeValue() != null) {
                hotelAddress = sdhHotelAddress.getDataHardCodeValue();
            }
            String hotelPhoneNumber = "";
            SysDataHardCode sdhHotelPhoneNumber = fReservationManager.getDataSysDataHardCode((long) 14);  //HotelPhoneNumber = '14'
            if (sdhHotelPhoneNumber != null
                    && sdhHotelPhoneNumber.getDataHardCodeValue() != null) {
                hotelPhoneNumber = sdhHotelPhoneNumber.getDataHardCodeValue();
            }
            String hotelLogoName = "";
            SysDataHardCode sdhHotelLogoName = fReservationManager.getDataSysDataHardCode((long) 15);  //HotelLogoName = '15'
            if (sdhHotelLogoName != null
                    && sdhHotelLogoName.getDataHardCodeValue() != null) {
                hotelLogoName = sdhHotelLogoName.getDataHardCodeValue();
            }
            try {
                ClassPrinter.printReceipt(hotelName,
                        hotelAddress,
                        hotelPhoneNumber,
                        hotelLogoName,
                        dataReservationBill,
                        language);
            } catch (Throwable ex) {
                Logger.getLogger(ReservationController.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else {
            HotelFX.showAlert(Alert.AlertType.WARNING, "SIMPAN DATA TERLEBIH DAHULU", "Silahkan simpan data reservasi terlebih dahulu..!", null);
        }
    }

    public void dataReservationCancelingTransactionPrintHandle(int language) {
        if (selectedData.getRefReservationStatus().getIdstatus() == 6) {    //Canceled = '6'
            String hotelName = "";
            SysDataHardCode sdhHotelName = fReservationManager.getDataSysDataHardCode((long) 12);  //HotelName = '12'
            if (sdhHotelName != null
                    && sdhHotelName.getDataHardCodeValue() != null) {
                hotelName = sdhHotelName.getDataHardCodeValue();
            }
            String hotelAddress = "";
            SysDataHardCode sdhHotelAddress = fReservationManager.getDataSysDataHardCode((long) 13);  //HotelAddress = '13'
            if (sdhHotelAddress != null
                    && sdhHotelAddress.getDataHardCodeValue() != null) {
                hotelAddress = sdhHotelAddress.getDataHardCodeValue();
            }
            String hotelPhoneNumber = "";
            SysDataHardCode sdhHotelPhoneNumber = fReservationManager.getDataSysDataHardCode((long) 14);  //HotelPhoneNumber = '14'
            if (sdhHotelPhoneNumber != null
                    && sdhHotelPhoneNumber.getDataHardCodeValue() != null) {
                hotelPhoneNumber = sdhHotelPhoneNumber.getDataHardCodeValue();
            }
            String hotelLogoName = "";
            SysDataHardCode sdhHotelLogoName = fReservationManager.getDataSysDataHardCode((long) 15);  //HotelLogoName = '15'
            if (sdhHotelLogoName != null
                    && sdhHotelLogoName.getDataHardCodeValue() != null) {
                hotelLogoName = sdhHotelLogoName.getDataHardCodeValue();
            }
            try {
                ClassPrinter.printCanceledReceipt(hotelName,
                        hotelAddress,
                        hotelPhoneNumber,
                        hotelLogoName,
                        selectedData,
                        language);
            } catch (Throwable ex) {
                Logger.getLogger(ReservationController.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else {
            HotelFX.showAlert(Alert.AlertType.WARNING, "STATUS DATA", "Status Data harus 'Canceled'..!", null);
        }
    }

    private void dataCheckOutTransactionCreateHandle() {
        if (dataInputStatus == 1) { //update
            if (selectedData.getRefReservationStatus().getIdstatus() == 2) {  //Booked = '2'
                if (checkAllReservationRoomHasBeenCheckOut()) {
                    dataInputTransactionStatus = "checkout";
                    resetDataTransaction(dataInputTransactionStatus);
                    //open form data reservation-checkout transaction dialog
                    showReservationTransactionDialog();
                } else {
                    HotelFX.showAlert(Alert.AlertType.WARNING, "TIDAK DAPAT MENAMBAH DATA TRANSAKSI (CHECK-OUT)", "Semua reservasi kamar harus di 'checkout'-kan terlebih dahulu..!", null);
                }
            } else {
                HotelFX.showAlert(Alert.AlertType.WARNING, "TIDAK DAPAT MENAMBAH DATA TRANSAKSI (CHECK-OUT)",
                        "Penambahan data transaksi (reservasi) hanya dapat dilakukan \n pada saat status reservasi : 'Booked'..!", null);
            }
        } else {
            HotelFX.showAlert(Alert.AlertType.WARNING, "SIMPAN DATA TERLEBIH DAHULU", "Silahkan simpan data reservasi terlebih dahulu..!", null);
        }
    }

    public void dataCheckOutTransactionPrintHandle(int language) {
        if (dataCheckOutBill.getTblReservation().getIdreservation() != 0L) {
            String hotelName = "";
            SysDataHardCode sdhHotelName = fReservationManager.getDataSysDataHardCode((long) 12);  //HotelName = '12'
            if (sdhHotelName != null
                    && sdhHotelName.getDataHardCodeValue() != null) {
                hotelName = sdhHotelName.getDataHardCodeValue();
            }
            String hotelAddress = "";
            SysDataHardCode sdhHotelAddress = fReservationManager.getDataSysDataHardCode((long) 13);  //HotelAddress = '13'
            if (sdhHotelAddress != null
                    && sdhHotelAddress.getDataHardCodeValue() != null) {
                hotelAddress = sdhHotelAddress.getDataHardCodeValue();
            }
            String hotelPhoneNumber = "";
            SysDataHardCode sdhHotelPhoneNumber = fReservationManager.getDataSysDataHardCode((long) 14);  //HotelPhoneNumber = '14'
            if (sdhHotelPhoneNumber != null
                    && sdhHotelPhoneNumber.getDataHardCodeValue() != null) {
                hotelPhoneNumber = sdhHotelPhoneNumber.getDataHardCodeValue();
            }
            String hotelLogoName = "";
            SysDataHardCode sdhHotelLogoName = fReservationManager.getDataSysDataHardCode((long) 15);  //HotelLogoName = '15'
            if (sdhHotelLogoName != null
                    && sdhHotelLogoName.getDataHardCodeValue() != null) {
                hotelLogoName = sdhHotelLogoName.getDataHardCodeValue();
            }
            try {
                ClassPrinter.printReceipt(hotelName,
                        hotelAddress,
                        hotelPhoneNumber,
                        hotelLogoName,
                        dataCheckOutBill,
                        language);
            } catch (Throwable ex) {
                Logger.getLogger(ReservationController.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else {
            HotelFX.showAlert(Alert.AlertType.WARNING, "SIMPAN DATA TERLEBIH DAHULU", "Silahkan simpan data reservasi terlebih dahulu..!", null);
        }
    }

    private boolean checkAllReservationRoomHasBeenCheckOut() {
        for (TblReservationRoomTypeDetail dataReservationRoomTypeDetail : dataReservationRoomTypeDetails) {
            if (dataReservationRoomTypeDetail.getTblReservationCheckInOut() == null
                    || dataReservationRoomTypeDetail.getTblReservationCheckInOut().getRefReservationCheckInOutStatus().getIdstatus() != 3) {
                return false;
            }
        }
        return true;
    }

    public boolean isFirstPayment;

    private void dataRCOTransactionCreateHandle() {
        if (dataInputStatus == 1) { //update
            if (selectedData.getRefReservationStatus().getIdstatus() == 1) {//Reserved = '1'
                isFirstPayment = true;
                dataInputTransactionStatus = "rco";
                resetDataTransaction(dataInputTransactionStatus);
                //open form data reservation-checkout transaction dialog
                showReservationTransactionDialog();
            } else {
                if ((selectedData.getRefReservationStatus().getIdstatus() == 2 //Booked = '1'
                        && calculationTotalRestOfBill("rco").compareTo(new BigDecimal("0")) == 1)) {   //need something to pay 
                    isFirstPayment = false;
                    dataInputTransactionStatus = "rco";
                    resetDataTransaction(dataInputTransactionStatus);
                    //open form data reservation-checkout transaction dialog
                    showReservationTransactionDialog();
                } else {
                    HotelFX.showAlert(Alert.AlertType.WARNING, "TIDAK DAPAT MENAMBAH DATA TRANSAKSI (RESERVASI)",
                            "Penambahan data transaksi (reservasi) hanya dapat dilakukan \n pada saat status reservasi : 'Reserved'..!", null);
                }
            }
        } else {
            HotelFX.showAlert(Alert.AlertType.WARNING, "SIMPAN DATA TERLEBIH DAHULU", "Silahkan simpan data reservasi terlebih dahulu..!", null);
        }
    }

    //RCO###
    public void resetDataTransaction(String billType) {
        switch (billType) {
            case "reservation":
                selectedDataTransaction = new TblReservationPayment();
                selectedDataTransaction.setTblReservationBill(dataReservationBill);
                selectedDataTransaction.setUnitNominal(new BigDecimal("0"));
                selectedDataTransaction.setRoundingValue(new BigDecimal("0"));
//                selectedDataTransaction.setRefFinanceTransactionPaymentType(new RefFinanceTransactionPaymentType());
                selectedDataTransactionWithTransfer = new TblReservationPaymentWithTransfer();
                selectedDataTransactionWithTransfer.setTblReservationPayment(selectedDataTransaction);
                selectedDataTransactionWithTransfer.setTblBankAccountByReceiverBankAccount(getDataDefaultBankAccountForGuestTransaction());
                selectedDataTransactionWithBankCard = new TblReservationPaymentWithBankCard();
                selectedDataTransactionWithBankCard.setTblReservationPayment(selectedDataTransaction);
                selectedDataTransactionWithBankCard.setPaymentDiscount(new BigDecimal("0"));
                selectedDataTransactionWithBankCard.setPaymentCharge(new BigDecimal("0"));
                selectedDataTransactionWithCekGiro = new TblReservationPaymentWithCekGiro();
                selectedDataTransactionWithCekGiro.setTblReservationPayment(selectedDataTransaction);
                selectedDataTransactionWithCekGiro.setTblBankAccountByReceiverBankAccount(getDataDefaultBankAccountForGuestTransaction());
                selectedDataTransactionWithGuaranteePayment = new TblReservationPaymentWithGuaranteePayment();
                selectedDataTransactionWithGuaranteePayment.setTblReservationPayment(selectedDataTransaction);
                selectedDataTransactionWithGuaranteePayment.setTblBankAccountByReceiverBankAccount(getDataDefaultBankAccountForGuestTransaction());
                selectedDataTransactionWithReservationVoucher = new TblReservationPaymentWithReservationVoucher();
                selectedDataTransactionWithReservationVoucher.setTblReservationPayment(selectedDataTransaction);
                break;
            case "checkout":
                selectedDataTransaction = new TblReservationPayment();
                selectedDataTransaction.setTblReservationBill(dataCheckOutBill);
                selectedDataTransaction.setUnitNominal(new BigDecimal("0"));
                selectedDataTransaction.setRoundingValue(new BigDecimal("0"));
//                selectedDataTransaction.setRefFinanceTransactionPaymentType(new RefFinanceTransactionPaymentType());
                selectedDataTransactionWithTransfer = new TblReservationPaymentWithTransfer();
                selectedDataTransactionWithTransfer.setTblReservationPayment(selectedDataTransaction);
                selectedDataTransactionWithTransfer.setTblBankAccountByReceiverBankAccount(getDataDefaultBankAccountForGuestTransaction());
                selectedDataTransactionWithBankCard = new TblReservationPaymentWithBankCard();
                selectedDataTransactionWithBankCard.setTblReservationPayment(selectedDataTransaction);
                selectedDataTransactionWithBankCard.setPaymentDiscount(new BigDecimal("0"));
                selectedDataTransactionWithBankCard.setPaymentCharge(new BigDecimal("0"));
                selectedDataTransactionWithCekGiro = new TblReservationPaymentWithCekGiro();
                selectedDataTransactionWithCekGiro.setTblReservationPayment(selectedDataTransaction);
                selectedDataTransactionWithCekGiro.setTblBankAccountByReceiverBankAccount(getDataDefaultBankAccountForGuestTransaction());
                selectedDataTransactionWithGuaranteePayment = new TblReservationPaymentWithGuaranteePayment();
                selectedDataTransactionWithGuaranteePayment.setTblReservationPayment(selectedDataTransaction);
                selectedDataTransactionWithGuaranteePayment.setTblBankAccountByReceiverBankAccount(getDataDefaultBankAccountForGuestTransaction());
                selectedDataTransactionWithReservationVoucher = new TblReservationPaymentWithReservationVoucher();
                selectedDataTransactionWithReservationVoucher.setTblReservationPayment(selectedDataTransaction);
                break;
            case "rco":
                selectedDataTransaction = new TblReservationPayment();
                selectedDataTransaction.setTblReservationBill(dataReservationBill);
                selectedDataTransaction.setUnitNominal(new BigDecimal("0"));
                selectedDataTransaction.setRoundingValue(new BigDecimal("0"));
//                selectedDataTransaction.setRefFinanceTransactionPaymentType(new RefFinanceTransactionPaymentType());
                selectedDataTransactionWithTransfer = new TblReservationPaymentWithTransfer();
                selectedDataTransactionWithTransfer.setTblReservationPayment(selectedDataTransaction);
                selectedDataTransactionWithTransfer.setTblBankAccountByReceiverBankAccount(getDataDefaultBankAccountForGuestTransaction());
                selectedDataTransactionWithBankCard = new TblReservationPaymentWithBankCard();
                selectedDataTransactionWithBankCard.setTblReservationPayment(selectedDataTransaction);
                selectedDataTransactionWithBankCard.setPaymentDiscount(new BigDecimal("0"));
                selectedDataTransactionWithBankCard.setPaymentCharge(new BigDecimal("0"));
                selectedDataTransactionWithCekGiro = new TblReservationPaymentWithCekGiro();
                selectedDataTransactionWithCekGiro.setTblReservationPayment(selectedDataTransaction);
                selectedDataTransactionWithCekGiro.setTblBankAccountByReceiverBankAccount(getDataDefaultBankAccountForGuestTransaction());
                selectedDataTransactionWithGuaranteePayment = new TblReservationPaymentWithGuaranteePayment();
                selectedDataTransactionWithGuaranteePayment.setTblReservationPayment(selectedDataTransaction);
                selectedDataTransactionWithGuaranteePayment.setTblBankAccountByReceiverBankAccount(getDataDefaultBankAccountForGuestTransaction());
                selectedDataTransactionWithReservationVoucher = new TblReservationPaymentWithReservationVoucher();
                selectedDataTransactionWithReservationVoucher.setTblReservationPayment(selectedDataTransaction);
                break;
            default:
                break;
        }
    }

    public TblBankAccount getDataDefaultBankAccountForGuestTransaction() {
        String defaultBankAccountForGuestTransaction = "";
        SysDataHardCode sdhDefaultBankAccountForGuestTransaction = getFReservationManager().getDataSysDataHardCode((long) 25);  //DefaultBankAccountForGuestTransaction = '25'
        if (sdhDefaultBankAccountForGuestTransaction != null
                && sdhDefaultBankAccountForGuestTransaction.getDataHardCodeValue() != null) {
            defaultBankAccountForGuestTransaction = sdhDefaultBankAccountForGuestTransaction.getDataHardCodeValue();
        }
        if (!defaultBankAccountForGuestTransaction.equals("")) {
            return getFReservationManager().getBankAccount(Long.parseLong(defaultBankAccountForGuestTransaction));
        }
        return null;
    }

    private void showReservationTransactionDialog() {
        try {
            // Load the fxml file and create a new stage for the popup dialog.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(HotelFX.class.getResource("view/feature_reservation/RCTransactionInputDialog.fxml"));

            RCTransactionInputController controller = new RCTransactionInputController(this);
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
     * HANDLE FOR DATA INPUT RESERVATION ROOM TYPE DETAIL
     */
    public RefReservationOrderByType reservationType;

    public TblTravelAgent currentTravelAgent;

    public List<TblReservationRoomTypeDetail> selectedDataRoomTypeDetails;

    public List<TblReservationRoomTypeDetailRoomPriceDetail> selectedDataRoomTypeDetailRoomPriceDetails;

    public List<TblReservationRoomTypeDetailTravelAgentDiscountDetail> selectedDataRoomTypeDetailTravelAgentDiscountDetails;

    public List<TblReservationAdditionalService> selectedDataAdditionalServices;

    public List<TblReservationAdditionalItem> selectedDataAdditionalItems;

    public void dataReservationRoomTypeDetailCreateHandle() {
        if (selectedData.getRefReservationStatus().getIdstatus() == 1) {    //Reserved = '1'
            showReservationRoomTypeDetailDialog();
        } else {
            HotelFX.showAlert(Alert.AlertType.WARNING, "TIDAK DAPAT MENAMBAHKAN RESERVASI KAMAR",
                    "Penambahan data reservasi kamar hanya dapat dilakukan \n pada saat status reservasi : 'Reserved'..!", null);
        }
    }

    private void dataReservationRoomTypeDetailReservationTypeChooseHandle() {
        reservationType = new RefReservationOrderByType();
        currentTravelAgent = new TblTravelAgent();
//        currentTravelAgent.setRoomTypeDiscountPercentage(new BigDecimal("0"));  //'BIGDECIMAL=NULL'
        dataReservationFormShowStatus.set(0.0);
        //open form data reservation room type detail reservation type dialog
        showReservationRoomTypeDetailReservationTypeDialog();
    }

    private void dataReservationRoomTypeDetailUpdateHandle() {
        //do nothing
    }

    private void dataReservationRoomTypeDetailDeleteHandle() {
        if (selectedData.getRefReservationStatus().getIdstatus() == 1) {    //Reserved = '1'
            if (tableDataReservationRoomTypeDetail.getTableView().getSelectionModel().getSelectedItems().size() == 1) {
                Alert alert = ClassMessage.showConfirmationDeletingDataMessage("", null);
                if (alert.getResult() == ButtonType.OK) {
                    //reservation room type detail
                    selectedDataRoomTypeDetail = (TblReservationRoomTypeDetail) tableDataReservationRoomTypeDetail.getTableView().getSelectionModel().getSelectedItem();
                    if (dataInputStatus == 1) {   //update
                        //save data to database
                        dataReservationSaveHandle(4);
                    } else {
                        ClassMessage.showSucceedDeletingDataMessage("", null);
                        //reservation room type detail - reservation room price detail
                        for (int i = dataReservationRoomTypeDetailRoomPriceDetails.size() - 1; i > -1; i--) {
                            if (dataReservationRoomTypeDetailRoomPriceDetails.get(i).getTblReservationRoomTypeDetail().equals(selectedDataRoomTypeDetail)) {
                                dataReservationRoomTypeDetailRoomPriceDetails.remove(i);
                            }
                        }
                        //reservation room type detail - reservation travel agent discount detail
                        for (int i = dataReservationRoomTypeDetailTravelAgentDiscountDetails.size() - 1; i > -1; i--) {
                            if (dataReservationRoomTypeDetailTravelAgentDiscountDetails.get(i).getTblReservationRoomTypeDetail().equals(selectedDataRoomTypeDetail)) {
                                dataReservationRoomTypeDetailTravelAgentDiscountDetails.remove(i);
                            }
                        }
                        //reservation additional item
                        for (int i = dataReservationAdditionalItems.size() - 1; i > -1; i--) {
                            if (dataReservationAdditionalItems.get(i).getTblReservationRoomTypeDetail().equals(selectedDataRoomTypeDetail)) {
                                dataReservationAdditionalItems.remove(i);
                            }
                        }
                        tableDataReservationAdditionalItem.getTableView().setItems(FXCollections.observableArrayList(dataReservationAdditionalItems));
                        //reservation additional service
                        for (int i = dataReservationAdditionalServices.size() - 1; i > -1; i--) {
                            if (dataReservationAdditionalServices.get(i).getTblReservationRoomTypeDetail().equals(selectedDataRoomTypeDetail)) {
                                dataReservationAdditionalServices.remove(i);
                            }
                        }
                        tableDataReservationAdditionalService.getTableView().setItems(FXCollections.observableArrayList(dataReservationAdditionalServices));
                        //reservation room type detail
                        for (int i = dataReservationRoomTypeDetails.size() - 1; i > -1; i--) {
                            if (dataReservationRoomTypeDetails.get(i).equals(selectedDataRoomTypeDetail)) {
                                dataReservationRoomTypeDetails.remove(i);
                            }
                        }
                        tableDataReservationRoomTypeDetail.getTableView().setItems(FXCollections.observableArrayList(dataReservationRoomTypeDetails));
                        refreshDataBill(selectedData.getRefReservationStatus().getIdstatus() == 1 //Reserved = '1'
                                ? "reservation" : "checkout");
                        refreshDataBill("rco");
                        //data reservation discount type
                        if (dataReservationRoomTypeDetails.isEmpty()) {
                            dataReservationBill.setTblBankCard(null);
                            dataReservationBill.setRefReservationBillDiscountType(null);
                        }
                    }
                }
            } else {
                ClassMessage.showWarningSelectedDataMessage("", null);
            }
        } else {
            HotelFX.showAlert(Alert.AlertType.WARNING, "TIDAK DAPAT MENGHAPUS DATA RESERVASI KAMAR",
                    "Penghapusan data reservasi kamar hanya dapat dilakukan \n pada saat status reservasi : 'Reserved'..!", null);
        }
    }

    public void dataReservationRoomTypeDetailChangeRoomTypeHandle() {
        if (dataInputStatus == 1) { //update
            if (tableDataReservationRoomTypeDetail.getTableView().getSelectionModel().getSelectedItems().size() == 1) {
                if (((TblReservationRoomTypeDetail) tableDataReservationRoomTypeDetail.getTableView().getSelectionModel().getSelectedItem()).getTblReservationCheckInOut() == null) {
                    //change room (before checked in)
                    selectedDataRoomTypeDetail = (TblReservationRoomTypeDetail) tableDataReservationRoomTypeDetail.getTableView().getSelectionModel().getSelectedItem();
                    showReservationChangeRoomTypeDialog();
                } else {
                    HotelFX.showAlert(Alert.AlertType.WARNING, "DATA TIDAK SESUAI", "Data telah berstatus 'Checked In' atau 'Checked Out'..!", null);
                }
            } else {
                ClassMessage.showWarningSelectedDataMessage("", null);
            }
        } else {
            HotelFX.showAlert(Alert.AlertType.WARNING, "SIMPAN DATA TERLEBIH DAHULU", "Silahkan simpan data reservasi terlebih dahulu..!", null);
        }
    }

    public TblReservationRoomTypeDetail selectedDataRoomTypeDetail;

    public TblReservationCheckInOut selectedDataCheckIn;

    public List<TblReservationRoomTypeDetailChildDetail> selectedChildDetails;

    private void dataReservationRoomTypeDetailCheckInHandle() {
        if (dataInputStatus == 1) { //update
            if (selectedData.getRefReservationStatus().getIdstatus() == 2) {  //Booked = '2'
                if (tableDataReservationRoomTypeDetail.getTableView().getSelectionModel().getSelectedItems().size() == 1) {
//                    if (((TblReservationRoomTypeDetail) tableDataReservationRoomTypeDetail.getTableView().getSelectionModel().getSelectedItem()).getTblReservationCheckInOut() == null
//                            || ((TblReservationRoomTypeDetail) tableDataReservationRoomTypeDetail.getTableView().getSelectionModel().getSelectedItem()).getTblReservationCheckInOut().getTblRoom() == null) {   //Room hasn't been selected yet
//                        HotelFX.showAlert(Alert.AlertType.WARNING, "TIDAK DAPAT MELAKUKAN CHECK-IN",
//                                "Data Kamar kosong, silahkan pilih data kamar terlebih dahulu..!", null);
                    if (((TblReservationRoomTypeDetail) tableDataReservationRoomTypeDetail.getTableView().getSelectionModel().getSelectedItem()).getTblReservationCheckInOut() == null) {
                        HotelFX.showAlert(Alert.AlertType.WARNING, "TIDAK DAPAT MELAKUKAN CHECK-IN",
                                "@_@..!", null);
                    } else {
                        if (((TblReservationRoomTypeDetail) tableDataReservationRoomTypeDetail.getTableView().getSelectionModel().getSelectedItem()).getTblReservationCheckInOut().getRefReservationCheckInOutStatus().getIdstatus() == 0) {    //Ready to Check In = '0'
                            selectedDataRoomTypeDetail = (TblReservationRoomTypeDetail) tableDataReservationRoomTypeDetail.getTableView().getSelectionModel().getSelectedItem();
//                            selectedDataCheckIn = new TblReservationCheckInOut();
                            selectedDataCheckIn = fReservationManager.getReservationCheckInOut(selectedDataRoomTypeDetail.getTblReservationCheckInOut().getIdcheckInOut());
                            if (selectedDataCheckIn.getTblRoom() != null) {
                                selectedDataCheckIn.setTblRoom(fReservationManager.getRoom(selectedDataCheckIn.getTblRoom().getIdroom()));
                            }
                            selectedDataCheckIn.setRefReservationCheckInOutStatus(fReservationManager.getReservationCheckInOutStatus(selectedDataCheckIn.getRefReservationCheckInOutStatus().getIdstatus()));
                            selectedDataCheckIn.setAdultNumber(selectedDataRoomTypeDetail.getTblRoomType().getAdultNumber());
                            selectedDataCheckIn.setChildNumber(selectedDataRoomTypeDetail.getTblRoomType().getChildNumber());
                            SysDataHardCode defaultGuestCardUsedNumber = fReservationManager.getDataSysDataHardCode((long) 3); //DefaultGuestCardUsedNumber = '3'
                            selectedDataCheckIn.setCardUsed(defaultGuestCardUsedNumber != null
                                    && defaultGuestCardUsedNumber.getDataHardCodeValue() != null
                                            ? Integer.parseInt(defaultGuestCardUsedNumber.getDataHardCodeValue())
                                            : 0);
                            SysDataHardCode defaultGuestCardBrokenCharge = fReservationManager.getDataSysDataHardCode((long) 4); //DefaultGuestCardBrokenCharge = '4'
                            selectedDataCheckIn.setBrokenCardCharge(defaultGuestCardBrokenCharge != null
                                    && defaultGuestCardBrokenCharge.getDataHardCodeValue() != null
                                            ? new BigDecimal(defaultGuestCardBrokenCharge.getDataHardCodeValue())
                                            : new BigDecimal("0"));
                            selectedDataCheckIn.setCardMissed(0);
                            selectedDataCheckIn.setAdditionalCardCharge(new BigDecimal("0"));
                            selectedDataCheckIn.setCardAdditional(0);
                            selectedChildDetails = new ArrayList<>();
                            if (!LocalDateTime.now().isBefore(LocalDateTime.of(
                                    selectedDataRoomTypeDetail.getCheckInDateTime().getYear() + 1900, selectedDataRoomTypeDetail.getCheckInDateTime().getMonth() + 1, selectedDataRoomTypeDetail.getCheckInDateTime().getDate(),
                                    defaultCheckInTime.getHours(), defaultCheckInTime.getMinutes())
                                    .minusDays(1))) {
                                if (!LocalDateTime.now().isBefore(LocalDateTime.of(
                                        selectedDataRoomTypeDetail.getCheckInDateTime().getYear() + 1900, selectedDataRoomTypeDetail.getCheckInDateTime().getMonth() + 1, selectedDataRoomTypeDetail.getCheckInDateTime().getDate(),
                                        defaultCheckInTime.getHours(), defaultCheckInTime.getMinutes()))) {
                                    showReservationCheckInDialog();
                                } else {
                                    selectedAdditionalService = new TblReservationAdditionalService();
                                    selectedAdditionalService.setTblReservationRoomTypeDetail((TblReservationRoomTypeDetail) tableDataReservationRoomTypeDetail.getTableView().getSelectionModel().getSelectedItem());
                                    selectedAdditionalService.setPrice(new BigDecimal("0"));
                                    selectedAdditionalService.setDiscountPercentage(new BigDecimal("0"));
                                    TblRoomService roomService = fReservationManager.getRoomService(2);     //early checkin : '2'
                                    selectedAdditionalService.setTblRoomService(roomService);
//                                setAdditionalServiceDiscount(selectedAdditionalService, dataCheckOutBill);  //must be checkout bill @@@+++
                                    showReservationEarlyCheckInDialog();
                                }
                            } else {
                                HotelFX.showAlert(Alert.AlertType.WARNING, "DATA TIDAK SESUAI", "Tidak dapat melakukan check-in pada tanggal reservasi dengan perbedaan waktu lebih dari 1 hari..!", null);
                            }
                        } else {
                            if (((TblReservationRoomTypeDetail) tableDataReservationRoomTypeDetail.getTableView().getSelectionModel().getSelectedItem()).getTblReservationCheckInOut().getRefReservationCheckInOutStatus().getIdstatus() == 1 //Checked In = '1'
                                    || ((TblReservationRoomTypeDetail) tableDataReservationRoomTypeDetail.getTableView().getSelectionModel().getSelectedItem()).getTblReservationCheckInOut().getRefReservationCheckInOutStatus().getIdstatus() == 2) {    //Ready to Check Out = '2'
                                HotelFX.showAlert(Alert.AlertType.WARNING, "DATA TIDAK SESUAI", "Status data telah check-in, silahkan pilih data lainnya..!", null);
                            } else {    //Checked Out = '3'
                                HotelFX.showAlert(Alert.AlertType.WARNING, "DATA TIDAK SESUAI", "Status data telah check-out, silahkan pilih data lainnya..!", null);
                            }
                        }
                    }
                } else {
                    ClassMessage.showWarningSelectedDataMessage("", null);
                }
            } else {
                HotelFX.showAlert(Alert.AlertType.WARNING, "TIDAK DAPAT MELAKUKAN CHECK-IN",
                        "Check-In kamar hanya dapat dilakukan \n pada saat status reservasi : 'Booked'..!", null);
            }
        } else {
            HotelFX.showAlert(Alert.AlertType.WARNING, "SIMPAN DATA TERLEBIH DAHULU", "Silahkan simpan data reservasi terlebih dahulu..!", null);
        }
    }

    public IntegerProperty additionalCardUsedNumber = new SimpleIntegerProperty();

    private void dataReservationRoomTypeDetailCheckInAddCardUsedNumberHandle() {
        if (dataInputStatus == 1) { //update
            if (tableDataReservationRoomTypeDetail.getTableView().getSelectionModel().getSelectedItems().size() == 1) {
                if (((TblReservationRoomTypeDetail) tableDataReservationRoomTypeDetail.getTableView().getSelectionModel().getSelectedItem()).getTblReservationCheckInOut() != null
                        && (((TblReservationRoomTypeDetail) tableDataReservationRoomTypeDetail.getTableView().getSelectionModel().getSelectedItem()).getTblReservationCheckInOut().getRefReservationCheckInOutStatus().getIdstatus() == 1 //Checked In = '1'
                        || ((TblReservationRoomTypeDetail) tableDataReservationRoomTypeDetail.getTableView().getSelectionModel().getSelectedItem()).getTblReservationCheckInOut().getRefReservationCheckInOutStatus().getIdstatus() == 2)) {   //Ready to Check Out = '2'
                    selectedDataRoomTypeDetail = (TblReservationRoomTypeDetail) tableDataReservationRoomTypeDetail.getTableView().getSelectionModel().getSelectedItem();
                    selectedDataCheckIn = selectedDataRoomTypeDetail.getTblReservationCheckInOut();
                    //data additional card used number
                    additionalCardUsedNumber = new SimpleIntegerProperty();
                    additionalCardUsedNumber.set(0);
                    showReservationCheckInAddCardUsedNumberDialog();
                } else {
                    HotelFX.showAlert(Alert.AlertType.WARNING, "DATA TIDAK SESUAI", "Silahkan pilih data berstatus 'Checked In'..!", null);
                }
            } else {
                ClassMessage.showWarningSelectedDataMessage("", null);
            }
        } else {
            HotelFX.showAlert(Alert.AlertType.WARNING, "SIMPAN DATA TERLEBIH DAHULU", "Silahkan simpan data reservasi terlebih dahulu..!", null);
        }
    }

    public ObjectProperty<LocalDate> extendRoomCheckOutDate = new SimpleObjectProperty<>();

    private void dataReservationRoomTypeDetailCheckInExtendRoomHandle() {
        if (dataInputStatus == 1) { //update
            if (tableDataReservationRoomTypeDetail.getTableView().getSelectionModel().getSelectedItems().size() == 1) {
                if (((TblReservationRoomTypeDetail) tableDataReservationRoomTypeDetail.getTableView().getSelectionModel().getSelectedItem()).getTblReservationCheckInOut() != null
                        && (((TblReservationRoomTypeDetail) tableDataReservationRoomTypeDetail.getTableView().getSelectionModel().getSelectedItem()).getTblReservationCheckInOut().getRefReservationCheckInOutStatus().getIdstatus() == 1 //Checked In = '1'
                        || ((TblReservationRoomTypeDetail) tableDataReservationRoomTypeDetail.getTableView().getSelectionModel().getSelectedItem()).getTblReservationCheckInOut().getRefReservationCheckInOutStatus().getIdstatus() == 2)) {   //Ready to Check Out = '2'
                    selectedDataRoomTypeDetail = (TblReservationRoomTypeDetail) tableDataReservationRoomTypeDetail.getTableView().getSelectionModel().getSelectedItem();
                    selectedDataCheckIn = selectedDataRoomTypeDetail.getTblReservationCheckInOut();
                    //data extend room - check out date
                    extendRoomCheckOutDate = new SimpleObjectProperty<>();
                    extendRoomCheckOutDate.set(LocalDate.of(
                            selectedDataRoomTypeDetail.getCheckOutDateTime().getYear() + 1900,
                            selectedDataRoomTypeDetail.getCheckOutDateTime().getMonth() + 1,
                            selectedDataRoomTypeDetail.getCheckOutDateTime().getDate()));
                    //data reservation room type detail - details
                    selectedDataRoomTypeDetailRoomPriceDetails = new ArrayList<>();
                    selectedDataRoomTypeDetailTravelAgentDiscountDetails = new ArrayList<>();
                    //open dialog input data extend room
                    showReservationCheckInExtendRoomDialog();
                } else {
                    HotelFX.showAlert(Alert.AlertType.WARNING, "DATA TIDAK SESUAI", "Silahkan pilih data berstatus 'Checked In' atau 'Ready to Check Out'..!", null);
                }
            } else {
                ClassMessage.showWarningSelectedDataMessage("", null);
            }
        } else {
            HotelFX.showAlert(Alert.AlertType.WARNING, "SIMPAN DATA TERLEBIH DAHULU", "Silahkan simpan data reservasi terlebih dahulu..!", null);
        }
    }

    public LocalDateTime currentDateForChangeRoomProcess;

    public List<TblReservationPayment> selectedDataTransactions;

    public List<TblReservationPaymentWithTransfer> selectedDataTransactionWithTransfers;

    public List<TblReservationPaymentWithBankCard> selectedDataTransactionWithBankCards;

    public List<TblReservationPaymentWithCekGiro> selectedDataTransactionWithCekGiros;

    public List<TblReservationPaymentWithGuaranteePayment> selectedDataTransactionWithGuaranteePayments;

    public List<TblGuaranteeLetterItemDetail> selectedDataGuaranteeLetterItemDetails;

    public List<TblReservationPaymentWithReservationVoucher> selectedDataTransactionWithReservationVouchers;

    public List<TblReservationRoomTypeDetailRoomPriceDetailChangeRoomHistory> selectedDataReservationRoomTypeDetailRoomPriceDetailChangeRoomHistories;

    private void dataReservationRoomTypeDetailChangeRoomHandle() {
        if (dataInputStatus == 1) { //update
            if (tableDataReservationRoomTypeDetail.getTableView().getSelectionModel().getSelectedItems().size() == 1) {
                if (selectedData.getRefReservationStatus().getIdstatus() == 2) { //Booked = '2'
                    if (((TblReservationRoomTypeDetail) tableDataReservationRoomTypeDetail.getTableView().getSelectionModel().getSelectedItem()).getTblReservationCheckInOut() == null
                            || ((TblReservationRoomTypeDetail) tableDataReservationRoomTypeDetail.getTableView().getSelectionModel().getSelectedItem()).getTblReservationCheckInOut().getRefReservationCheckInOutStatus().getIdstatus() != 3) {   //Checked Out = '3'
                        //change room (after checked in)
                        //@@@000
                        currentDateForChangeRoomProcess = LocalDateTime.of(LocalDate.now(), LocalTime.now());
                        if (currentDateForChangeRoomProcess.isAfter(
                                LocalDateTime.of(LocalDate.now(), LocalTime.of(
                                                defaultCheckOutTime.getHours(),
                                                defaultCheckOutTime.getMinutes(),
                                                0)))) {
                            //current day
                            currentDateForChangeRoomProcess = LocalDateTime.of(LocalDate.now(), LocalTime.of(
                                    defaultCheckOutTime.getHours(),
                                    defaultCheckOutTime.getMinutes(),
                                    0));
                        } else {
                            //one day before
                            currentDateForChangeRoomProcess = LocalDateTime.of(LocalDate.now(), LocalTime.of(
                                    defaultCheckOutTime.getHours(),
                                    defaultCheckOutTime.getMinutes(),
                                    0)).minusDays(1);
                        }
                        selectedDataRoomTypeDetail = (TblReservationRoomTypeDetail) tableDataReservationRoomTypeDetail.getTableView().getSelectionModel().getSelectedItem();
                        if (currentDateForChangeRoomProcess.isBefore( //(cannot change room at second time ***)
                                LocalDateTime.of(selectedDataRoomTypeDetail.getCheckOutDateTime().getYear() + 1900,
                                        selectedDataRoomTypeDetail.getCheckOutDateTime().getMonth() + 1,
                                        selectedDataRoomTypeDetail.getCheckOutDateTime().getDate(),
                                        selectedDataRoomTypeDetail.getCheckOutDateTime().getHours(),
                                        selectedDataRoomTypeDetail.getCheckOutDateTime().getMinutes(),
                                        0))) {
                            if (currentDateForChangeRoomProcess.isBefore(
                                    LocalDateTime.of(selectedDataRoomTypeDetail.getCheckInDateTime().getYear() + 1900,
                                            selectedDataRoomTypeDetail.getCheckInDateTime().getMonth() + 1,
                                            selectedDataRoomTypeDetail.getCheckInDateTime().getDate(),
                                            selectedDataRoomTypeDetail.getCheckInDateTime().getHours(),
                                            selectedDataRoomTypeDetail.getCheckInDateTime().getMinutes(),
                                            0))) {
                                currentDateForChangeRoomProcess = LocalDateTime.of(selectedDataRoomTypeDetail.getCheckInDateTime().getYear() + 1900,
                                        selectedDataRoomTypeDetail.getCheckInDateTime().getMonth() + 1,
                                        selectedDataRoomTypeDetail.getCheckInDateTime().getDate(),
                                        selectedDataRoomTypeDetail.getCheckInDateTime().getHours(),
                                        selectedDataRoomTypeDetail.getCheckInDateTime().getMinutes(),
                                        0);
                            }
                            //data room type detail
                            selectedDataRoomTypeDetails = new ArrayList<>();
                            selectedDataRoomTypeDetailRoomPriceDetails = new ArrayList<>();
                            selectedDataRoomTypeDetailTravelAgentDiscountDetails = new ArrayList<>();
                            //data additional
                            selectedAdditionalItems = fReservationManager.getAllDataReservationAdditionalItemByIDReservationRoomTypeDetail(selectedDataRoomTypeDetail.getIddetail());
                            for (int i = selectedAdditionalItems.size() - 1; i > -1; i--) {
                                if ((LocalDateTime.of(selectedAdditionalItems.get(i).getAdditionalDate().getYear() + 1900,
                                        selectedAdditionalItems.get(i).getAdditionalDate().getMonth() + 1,
                                        selectedAdditionalItems.get(i).getAdditionalDate().getDate(),
                                        defaultCheckInTime.getHours(),
                                        defaultCheckInTime.getMinutes(),
                                        0)).isBefore(currentDateForChangeRoomProcess)
                                        //                                        || selectedAdditionalItems.get(i).getRefReservationBillType().getIdtype() != 0) {   //Reservasi = '0'
                                        || (selectedAdditionalItems.get(i).getRefReservationBillType().getIdtype() != 0 //Reservasi = '0'
                                        && selectedAdditionalItems.get(i).getRefReservationBillType().getIdtype() != 1)) {   //Check Out = '1'
                                    selectedAdditionalItems.remove(i);
                                } else {
                                    //data room type detail
                                    selectedAdditionalItems.get(i).setTblReservationRoomTypeDetail(fReservationManager.getReservationRoomTypeDetail(selectedAdditionalItems.get(i).getTblReservationRoomTypeDetail().getIddetail()));
                                    //data reservation
                                    selectedAdditionalItems.get(i).getTblReservationRoomTypeDetail().setTblReservation(fReservationManager.getReservation(selectedAdditionalItems.get(i).getTblReservationRoomTypeDetail().getTblReservation().getIdreservation()));
                                    //data room type
                                    selectedAdditionalItems.get(i).getTblReservationRoomTypeDetail().setTblRoomType(fReservationManager.getRoomType(selectedAdditionalItems.get(i).getTblReservationRoomTypeDetail().getTblRoomType().getIdroomType()));
                                    //data reservation order by
                                    selectedAdditionalItems.get(i).getTblReservationRoomTypeDetail().setRefReservationOrderByType(fReservationManager.getReservationOrderByType(selectedAdditionalItems.get(i).getTblReservationRoomTypeDetail().getRefReservationOrderByType().getIdtype()));
                                    //data item
                                    selectedAdditionalItems.get(i).setTblItem(fReservationManager.getDataItem(selectedAdditionalItems.get(i).getTblItem().getIditem()));
                                    //data item type hk
                                    if (selectedAdditionalItems.get(i).getTblItem().getTblItemTypeHk() != null) {
                                        selectedAdditionalItems.get(i).getTblItem().setTblItemTypeHk(fReservationManager.getDataItemTypeHK(selectedAdditionalItems.get(i).getTblItem().getTblItemTypeHk().getIditemTypeHk()));
                                    }
                                    //data item type wh
                                    if (selectedAdditionalItems.get(i).getTblItem().getTblItemTypeWh() != null) {
                                        selectedAdditionalItems.get(i).getTblItem().setTblItemTypeWh(fReservationManager.getDataItemTypeWH(selectedAdditionalItems.get(i).getTblItem().getTblItemTypeWh().getIditemTypeWh()));
                                    }
                                    //data hotel event
                                    if (selectedAdditionalItems.get(i).getTblHotelEvent() != null) {
                                        selectedAdditionalItems.get(i).setTblHotelEvent(fReservationManager.getHotelEvent(selectedAdditionalItems.get(i).getTblHotelEvent().getIdevent()));
                                    }
                                    //data card event
                                    if (selectedAdditionalItems.get(i).getTblBankEventCard() != null) {
                                        selectedAdditionalItems.get(i).setTblBankEventCard(fReservationManager.getBankEventCard(selectedAdditionalItems.get(i).getTblBankEventCard().getIdevent()));
                                    }
                                    //data reservation bill type
                                    selectedAdditionalItems.get(i).setRefReservationBillType(fReservationManager.getDataReservationBillType(selectedAdditionalItems.get(i).getRefReservationBillType().getIdtype()));
                                }
                            }
                            selectedAdditionalServices = fReservationManager.getAllDataReservationAdditionalServiceByIDReservationRoomTypeDetail(selectedDataRoomTypeDetail.getIddetail());
                            for (int i = selectedAdditionalServices.size() - 1; i > -1; i--) {
                                if ((LocalDateTime.of(selectedAdditionalServices.get(i).getAdditionalDate().getYear() + 1900,
                                        selectedAdditionalServices.get(i).getAdditionalDate().getMonth() + 1,
                                        selectedAdditionalServices.get(i).getAdditionalDate().getDate(),
                                        defaultCheckInTime.getHours(),
                                        defaultCheckInTime.getMinutes(),
                                        0)).isBefore(currentDateForChangeRoomProcess)
                                        //                                        || selectedAdditionalServices.get(i).getRefReservationBillType().getIdtype() != 0 //Reservasi = '0'
                                        || (selectedAdditionalServices.get(i).getRefReservationBillType().getIdtype() != 0 //Reservasi = '0'
                                        && selectedAdditionalServices.get(i).getRefReservationBillType().getIdtype() != 1) //Check Out = '1'
                                        || selectedAdditionalServices.get(i).getTblRoomService().getIdroomService() == 2 //Early Checkin = '2'
                                        || selectedAdditionalServices.get(i).getTblRoomService().getIdroomService() == 3 //Late Checkout = '3'
                                        || selectedAdditionalServices.get(i).getTblRoomService().getIdroomService() == 4    //Lainnya = '4'
                                        || selectedAdditionalServices.get(i).getTblRoomService().getIdroomService() == 5) {   //Canceling Fee = '5'
                                    selectedAdditionalServices.remove(i);
                                } else {
                                    //data room type detail
                                    selectedAdditionalServices.get(i).setTblReservationRoomTypeDetail(fReservationManager.getReservationRoomTypeDetail(selectedAdditionalServices.get(i).getTblReservationRoomTypeDetail().getIddetail()));
                                    //data reservation
                                    selectedAdditionalServices.get(i).getTblReservationRoomTypeDetail().setTblReservation(fReservationManager.getReservation(selectedAdditionalServices.get(i).getTblReservationRoomTypeDetail().getTblReservation().getIdreservation()));
                                    //data room type
                                    selectedAdditionalServices.get(i).getTblReservationRoomTypeDetail().setTblRoomType(fReservationManager.getRoomType(selectedAdditionalServices.get(i).getTblReservationRoomTypeDetail().getTblRoomType().getIdroomType()));
                                    //data reservation order by
                                    selectedAdditionalServices.get(i).getTblReservationRoomTypeDetail().setRefReservationOrderByType(fReservationManager.getReservationOrderByType(selectedAdditionalServices.get(i).getTblReservationRoomTypeDetail().getRefReservationOrderByType().getIdtype()));
                                    //data room service
                                    selectedAdditionalServices.get(i).setTblRoomService(fReservationManager.getRoomService(selectedAdditionalServices.get(i).getTblRoomService().getIdroomService()));
                                    //data hotel event
                                    if (selectedAdditionalServices.get(i).getTblHotelEvent() != null) {
                                        selectedAdditionalServices.get(i).setTblHotelEvent(fReservationManager.getHotelEvent(selectedAdditionalServices.get(i).getTblHotelEvent().getIdevent()));
                                    }
                                    //data card event
                                    if (selectedAdditionalServices.get(i).getTblBankEventCard() != null) {
                                        selectedAdditionalServices.get(i).setTblBankEventCard(fReservationManager.getBankEventCard(selectedAdditionalServices.get(i).getTblBankEventCard().getIdevent()));
                                    }
                                    //data reservation bill type
                                    selectedAdditionalServices.get(i).setRefReservationBillType(fReservationManager.getDataReservationBillType(selectedAdditionalServices.get(i).getRefReservationBillType().getIdtype()));
                                }
                            }
                            //data transaction
                            selectedDataTransactions = new ArrayList<>();
                            selectedDataTransactionWithTransfers = new ArrayList<>();
                            selectedDataTransactionWithBankCards = new ArrayList<>();
                            selectedDataTransactionWithCekGiros = new ArrayList<>();
                            selectedDataTransactionWithGuaranteePayments = new ArrayList<>();
                            selectedDataGuaranteeLetterItemDetails = new ArrayList<>();
                            selectedDataTransactionWithReservationVouchers = new ArrayList<>();
                            //data change room history
                            selectedDataReservationRoomTypeDetailRoomPriceDetailChangeRoomHistories = new ArrayList<>();
                            //show dialog input
                            showReservationChangeRoomDialog();
                        } else {
                            HotelFX.showAlert(Alert.AlertType.WARNING, "DATA TIDAK SESUAI", "Tidak dapat melakukan 'pindah kamar' pada data reservasi kamar yang telah lewat "
                                    + "\n (waktu sekarang (tanggal/jam) melebihi waktu check-out yang ada (tanggal/jam))..!", null);
                        }
                    } else {
                        HotelFX.showAlert(Alert.AlertType.WARNING, "DATA TIDAK SESUAI", "Tidak dapat melakukan 'pindah kamar' pada data berstatus 'Checked Out'..!", null);
                    }
                } else {
                    HotelFX.showAlert(Alert.AlertType.WARNING, "TIDAK DAPAT MELAKUKAN PERUBAHAN DATA KAMAR", "Pidah kamar hanya dapat dilakukan pada saat status reservasi = 'Booked'..!", null);
                }
            } else {
                ClassMessage.showWarningSelectedDataMessage("", null);
            }
        } else {
            HotelFX.showAlert(Alert.AlertType.WARNING, "SIMPAN DATA TERLEBIH DAHULU", "Silahkan simpan data reservasi terlebih dahulu..!", null);
        }
    }

    public TblReservationCheckInOut selectedDataCheckOut;

//    public TblItemLocation selectedItemLocationForMissedCard;
    private void dataReservationRoomTypeDetailCheckOutHandle() {
        if (dataInputStatus == 1) { //update
            if (selectedData.getRefReservationStatus().getIdstatus() == 2) {  //Booked = '2'
                if (calculationTotalRestOfBill("reservation")
                        .compareTo(new BigDecimal("0")) == 0) {
                    if (tableDataReservationRoomTypeDetail.getTableView().getSelectionModel().getSelectedItems().size() == 1) {
                        if (((TblReservationRoomTypeDetail) tableDataReservationRoomTypeDetail.getTableView().getSelectionModel().getSelectedItem()).getTblReservationCheckInOut() != null
                                && ((TblReservationRoomTypeDetail) tableDataReservationRoomTypeDetail.getTableView().getSelectionModel().getSelectedItem()).getTblReservationCheckInOut().getRefReservationCheckInOutStatus().getIdstatus() != 0) { //Ready to Check In = '0'
                            if (((TblReservationRoomTypeDetail) tableDataReservationRoomTypeDetail.getTableView().getSelectionModel().getSelectedItem()).getTblReservationCheckInOut().getRefReservationCheckInOutStatus().getIdstatus() != 3) {   //Checked Out = '3'
                                selectedDataRoomTypeDetail = (TblReservationRoomTypeDetail) tableDataReservationRoomTypeDetail.getTableView().getSelectionModel().getSelectedItem();
                                selectedDataCheckOut = selectedDataRoomTypeDetail.getTblReservationCheckInOut();
//                            selectedItemLocationForMissedCard = null;
                                if (!LocalDateTime.now().isAfter(LocalDateTime.of(
                                        selectedDataRoomTypeDetail.getCheckOutDateTime().getYear() + 1900, selectedDataRoomTypeDetail.getCheckOutDateTime().getMonth() + 1, selectedDataRoomTypeDetail.getCheckOutDateTime().getDate(),
                                        defaultCheckOutTime.getHours(), defaultCheckOutTime.getMinutes()))) {
                                    showReservationCheckOutDialog();
                                } else {
                                    selectedAdditionalService = new TblReservationAdditionalService();
                                    selectedAdditionalService.setTblReservationRoomTypeDetail((TblReservationRoomTypeDetail) tableDataReservationRoomTypeDetail.getTableView().getSelectionModel().getSelectedItem());
                                    selectedAdditionalService.setPrice(new BigDecimal("0"));
                                    selectedAdditionalService.setDiscountPercentage(new BigDecimal("0"));
                                    TblRoomService roomService = fReservationManager.getRoomService(3);     //early checkin : '3'
                                    selectedAdditionalService.setTblRoomService(roomService);
//                                setAdditionalServiceDiscount(selectedAdditionalService, dataCheckOutBill);  //must be in checkout bill @@@+++
                                    showReservationLateCheckOutDialog();
                                }
                            } else {
                                HotelFX.showAlert(Alert.AlertType.WARNING, "DATA TIDAK SESUAI", "Status data telah check-out, silahkan pilih data lainnya..!", null);
                            }
                        } else {
                            HotelFX.showAlert(Alert.AlertType.WARNING, "DATA TIDAK SESUAI", "Status data belum check-in, silahkan pilih data lainnya..!", null);
                        }
                    } else {
                        ClassMessage.showWarningSelectedDataMessage("", null);
                    }
                } else {
                    HotelFX.showAlert(Alert.AlertType.WARNING, "TIDAK DAPAT MELAKUKAN CHECK-OUT",
                            "Masih terdapat tagihan (reservasi) yang belum dibayar..!", null);
                }
            } else {
                HotelFX.showAlert(Alert.AlertType.WARNING, "TIDAK DAPAT MELAKUKAN CHECK-OUT",
                        "Check-Out kamar hanya dapat dilakukan \n pada saat status reservasi : 'Booked'..!", null);
            }
        } else {
            HotelFX.showAlert(Alert.AlertType.WARNING, "SIMPAN DATA TERLEBIH DAHULU", "Silahkan simpan data reservasi terlebih dahulu..!", null);
        }
    }

    private void dataReservationRoomTypeDetailMultiCheckOutHandle() {
        if (dataInputStatus == 1) { //update
            if (selectedData.getRefReservationStatus().getIdstatus() == 2) {  //Booked = '2'
                if (calculationTotalRestOfBill("reservation")
                        .compareTo(new BigDecimal("0")) == 0) {
                    //set selected data details
                    setSelectedDataRoomTypeDetailsWithAvailableToCheckOut();
                    //show dialog mutli checkout
                    showReservationMultiCheckOutDialog();

                } else {
                    HotelFX.showAlert(Alert.AlertType.WARNING, "TIDAK DAPAT MELAKUKAN CHECK-OUT",
                            "Masih terdapat tagihan (reservasi) yang belum dibayar..!", null);
                }
            } else {
                HotelFX.showAlert(Alert.AlertType.WARNING, "TIDAK DAPAT MELAKUKAN CHECK-OUT",
                        "Check-Out kamar hanya dapat dilakukan \n pada saat status reservasi : 'Booked'..!", null);
            }
        } else {
            HotelFX.showAlert(Alert.AlertType.WARNING, "SIMPAN DATA TERLEBIH DAHULU", "Silahkan simpan data reservasi terlebih dahulu..!", null);
        }
    }

    private void setSelectedDataRoomTypeDetailsWithAvailableToCheckOut() {
        selectedDataRoomTypeDetails = new ArrayList<>();
        for (TblReservationRoomTypeDetail dataReservationRoomTypeDetail : dataReservationRoomTypeDetails) {
            if (dataReservationRoomTypeDetail.getTblReservationCheckInOut() != null
                    && (dataReservationRoomTypeDetail.getTblReservationCheckInOut().getRefReservationCheckInOutStatus().getIdstatus() == 1 //Checked In = '1'
                    || dataReservationRoomTypeDetail.getTblReservationCheckInOut().getRefReservationCheckInOutStatus().getIdstatus() == 2) //Ready to Check Out = '2'                    
                    && (!LocalDateTime.now().isAfter(LocalDateTime.of(
                                    dataReservationRoomTypeDetail.getCheckOutDateTime().getYear() + 1900, dataReservationRoomTypeDetail.getCheckOutDateTime().getMonth() + 1, dataReservationRoomTypeDetail.getCheckOutDateTime().getDate(),
                                    defaultCheckOutTime.getHours(), defaultCheckOutTime.getMinutes())))) {
                selectedDataRoomTypeDetails.add(dataReservationRoomTypeDetail);
            }
        }
    }

//    private void setAdditionalServiceDiscount(TblReservationAdditionalService dataAdditionalService,
//            TblReservationBill dataRCOBill) {
//        if (dataRCOBill.getRefReservationBillDiscountType() != null) {
//            if (dataRCOBill.getRefReservationBillDiscountType().getIdtype() == 0) { //Hotel-Event = '0'
//                if (dataAdditionalService.getTblRoomService().getHotelDiscountable()) {
//                    List<TblHotelEvent> dataHotelEvents = fReservationManager.getAllDataHotelEventByDateInRangeOrderByMaxServiceDiscountPercentage(dataAdditionalService.getAdditionalDate());
//                    if (!dataHotelEvents.isEmpty()) {
//                        for (TblHotelEvent dataHotelEvent : dataHotelEvents) {
//                            if (dataAdditionalService.getPrice() >= dataHotelEvent.getMinTransaction()) {
//                                if ((dataAdditionalService.getPrice() * (dataHotelEvent.getServiceDiscountPercentage().doubleValue() / 100)) > dataHotelEvent.getDiscountNominal()) {
//                                    dataAdditionalService.setTblHotelEvent(dataHotelEvent);
//                                    dataAdditionalService.setDiscountPercentage(dataHotelEvent.getServiceDiscountPercentage());
//                                    break;
//                                } else {
//                                    dataAdditionalService.setTblHotelEvent(dataHotelEvent);
//                                    dataAdditionalService.setDiscountPercentage(dataHotelEvent.getServiceDiscountPercentage());
//                                    break;
//                                }
//                            }
//                        }
//                    }
//                }
//            } else {
//                if (dataRCOBill.getRefReservationBillDiscountType().getIdtype() == 1) { //Card-Event = '1'
//                    if (dataAdditionalService.getTblRoomService().getCardDiscountable()) {
//                        List<TblBankEventCard> dataCardEvents = fReservationManager.getAllDataBankEventCardByDateInRangeAndIDBankCardAllowNULLOrderByMaxServiceDiscountPercentage(
//                                dataAdditionalService.getAdditionalDate(),
//                                dataRCOBill.getTblBankCard().getIdbankCard());
//                        if (!dataCardEvents.isEmpty()) {
//                            for (TblBankEventCard dataCardEvent : dataCardEvents) {
//                                if (dataAdditionalService.getPrice() >= dataCardEvent.getMinTransaction()) {
//                                    if ((dataAdditionalService.getPrice() * (dataCardEvent.getServiceDiscountPercentage().doubleValue() / 100)) > dataCardEvent.getDiscountNominal()) {
//                                        dataAdditionalService.setTblBankEventCard(dataCardEvent);
//                                        dataAdditionalService.setDiscountPercentage(dataCardEvent.getServiceDiscountPercentage());
//                                        break;
//                                    } else {
//                                        dataAdditionalService.setTblBankEventCard(dataCardEvent);
//                                        dataAdditionalService.setDiscountPercentage(dataCardEvent.getServiceDiscountPercentage());
//                                        break;
//                                    }
//                                }
//                            }
//                        }
//                    }
//                }
//            }
//        }
//    }
    private void showReservationRoomTypeDetailReservationTypeDialog() {
        try {
            // Load the fxml file and create a new stage for the popup dialog.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(HotelFX.class.getResource("view/feature_reservation/ReservationRoomTypeDetailReservationTypeInputDialog.fxml"));

            ReservationRoomTypeDetailReservationTypeInputController controller = new ReservationRoomTypeDetailReservationTypeInputController(this);
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

    private void showReservationRoomTypeDetailDialog() {
        try {
            // Load the fxml file and create a new stage for the popup dialog.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(HotelFX.class.getResource("view/feature_reservation/ReservationRoomTypeDetailInputDialog.fxml"));

            ReservationRoomTypeDetailInputController controller = new ReservationRoomTypeDetailInputController(this);
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

    private void showReservationChangeRoomTypeDialog() {
        try {
            // Load the fxml file and create a new stage for the popup dialog.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(HotelFX.class.getResource("view/feature_reservation/ReservationChangeRoomTypeInputDialog.fxml"));

            ReservationChangeRoomTypeInputController controller = new ReservationChangeRoomTypeInputController(this);
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

    private void showReservationChangeRoomDialog() {
        try {
            // Load the fxml file and create a new stage for the popup dialog.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(HotelFX.class.getResource("view/feature_reservation/ReservationChangeRoomInputDialog.fxml"));

            ReservationChangeRoomInputController controller = new ReservationChangeRoomInputController(this);
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

    private void showReservationCheckInDialog() {
        try {
            // Load the fxml file and create a new stage for the popup dialog.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(HotelFX.class.getResource("view/feature_reservation/ReservationCheckInInputDialog.fxml"));

            ReservationCheckInInputController controller = new ReservationCheckInInputController(this);
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

    private void showReservationEarlyCheckInDialog() {
        try {
            // Load the fxml file and create a new stage for the popup dialog.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(HotelFX.class.getResource("view/feature_reservation/ReservationEarlyCheckInInputDialog.fxml"));

            ReservationEarlyCheckInInputController controller = new ReservationEarlyCheckInInputController(this);
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

    private void showReservationCheckInAddCardUsedNumberDialog() {
        try {
            // Load the fxml file and create a new stage for the popup dialog.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(HotelFX.class.getResource("view/feature_reservation/ReservationCheckInAddCardUsedInputDialog.fxml"));

            ReservationCheckInAddCardUsedInputController controller = new ReservationCheckInAddCardUsedInputController(this);
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

    private void showReservationCheckInExtendRoomDialog() {
        try {
            // Load the fxml file and create a new stage for the popup dialog.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(HotelFX.class.getResource("view/feature_reservation/ReservationCheckInExtendRoomDialog.fxml"));

            ReservationCheckInExtendRoomController controller = new ReservationCheckInExtendRoomController(this);
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

    private void showReservationCheckOutDialog() {
        try {
            // Load the fxml file and create a new stage for the popup dialog.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(HotelFX.class.getResource("view/feature_reservation/ReservationCheckOutInputDialog.fxml"));

            ReservationCheckOutInputController controller = new ReservationCheckOutInputController(this);
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

    private void showReservationLateCheckOutDialog() {
        try {
            // Load the fxml file and create a new stage for the popup dialog.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(HotelFX.class.getResource("view/feature_reservation/ReservationLateCheckOutInputDialog.fxml"));

            ReservationLateCheckOutInputController controller = new ReservationLateCheckOutInputController(this);
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

    private void showReservationMultiCheckOutDialog() {
        try {
            // Load the fxml file and create a new stage for the popup dialog.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(HotelFX.class.getResource("view/feature_reservation/ReservationMultiCheckOutInputDialog.fxml"));

            ReservationMultiCheckOutInputController controller = new ReservationMultiCheckOutInputController(this);
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
     * HANDLE FOR DATA INPUT RESERVATION ADDITIONAL ITEM
     */
    public TblReservationAdditionalItem selectedAdditionalItem;

    public List<TblReservationAdditionalItem> selectedAdditionalItems;

    private void dataReservationAdditionalItemCreateHandle() {
        if (dataInputStatus == 1) { //update
            if (selectedData.getRefReservationStatus().getIdstatus() == 1 //Reserved = '1'
                    || selectedData.getRefReservationStatus().getIdstatus() == 2) {   //Booked = '2'
//                if (tableDataReservationRoomTypeDetail.getTableView().getSelectionModel().getSelectedItems().size() == 1) {
                //set selected data
                selectedAdditionalItem = new TblReservationAdditionalItem();
//                    selectedAdditionalItem.setTblReservationRoomTypeDetail((TblReservationRoomTypeDetail) tableDataReservationRoomTypeDetail.getTableView().getSelectionModel().getSelectedItem());
                selectedAdditionalItem.setItemCharge(new BigDecimal("0"));
                selectedAdditionalItem.setItemQuantity(new BigDecimal("0"));
                selectedAdditionalItem.setDiscountPercentage(new BigDecimal("0"));
                if (selectedData.getRefReservationStatus().getIdstatus() == 1) {  //Reserved = '1'
                    selectedAdditionalItem.setRefReservationBillType(fReservationManager.getDataReservationBillType(0));    //Reservasi = '0'
                }
//                    selectedAditionalItem.setTblItem(new TblItem());
//                    selectedAditionalItem.getTblItem().setRefItemType(new TblItemType());
                //open form data reservation additional item dialog
                showReservationAdditionalItemDialog();
//                } else {
//                    ClassMessage.showWarningSelectedDataMessage("", null);
//                }
            } else {
                HotelFX.showAlert(Alert.AlertType.WARNING, "TIDAK DAPAT MENAMBAHKAN DATA TAMBAHAN BARANG",
                        "Penambahan data tambahan barang hanya dapat dilakukan \n pada saat status reservasi : 'Reserved' atau 'Booked'..!", null);
            }
        } else {
            HotelFX.showAlert(Alert.AlertType.WARNING, "SIMPAN DATA TERLEBIH DAHULU", "Silahkan simpan data reservasi terlebih dahulu..!", null);
        }
    }

    private void dataReservationAdditionalItemUpdateHandle() {
        //do nothing
    }

    private void dataReservationAdditionalItemDeleteHandle() {
//        if (dataInputStatus == 1) { //update
        if (selectedData.getRefReservationStatus().getIdstatus() != 5) {   //Checked Out = '5'
            if (tableDataReservationAdditionalItem.getTableView().getSelectionModel().getSelectedItems().size() == 1) {
                selectedAdditionalItem = (TblReservationAdditionalItem) tableDataReservationAdditionalItem.getTableView().getSelectionModel().getSelectedItem();
                if ((selectedData.getRefReservationStatus().getIdstatus() != 1 //Reserved = '1'
                        && selectedData.getRefReservationStatus().getIdstatus() != 2)) { //Booked = '2'
//                        && selectedAdditionalItem.getRefReservationBillType().getIdtype() == 0) {    //Reservasi = '0'
                    HotelFX.showAlert(Alert.AlertType.WARNING, "TIDAK DAPAT MENGHAPUS DATA TAMBAHAN BARANG",
                            "Penghapusan data tambahan barang hanya dapat dilakukan \n pada saat status reservasi : 'Reserved' atau 'Booked'..!", null);
                } else {
//                        dataReservationAdditionalItems.remove(selectedAditionalItem);
//                        tableDataReservationAdditionalItem.getTableView().setItems(FXCollections.observableArrayList(dataReservationAdditionalItems));
//                        refreshDataBill(selectedData.getRefReservationStatus().getIdstatus() == 1 //Reserved = '1'
//                                ? "reservation" : "checkout");
                    if (selectedAdditionalItem.getRefReservationBillType().getIdtype() == 4) {    //Include
                        HotelFX.showAlert(Alert.AlertType.WARNING, "TIDAK DAPAT MENGHAPUS DATA TAMBAHAN BARANG",
                                "Tidak dapat menghapus data tambahan barang dengan status 'Include'..!", null);
                    } else {
                        if (selectedData.getRefReservationStatus().getIdstatus() == 2 //Booked = '2'
                                && selectedAdditionalItem.getRefReservationBillType().getIdtype() == 0 //Reservasi = '0'
                                && ((calculationTotalRestOfBill("reservation")
                                .subtract(calculationTotalAdditionalItem(selectedAdditionalItem, dataReservationBill))).compareTo(new BigDecimal("0")) == -1)) {
                            HotelFX.showAlert(Alert.AlertType.WARNING, "TIDAK DAPAT MENGHAPUS DATA TAMBAHAN BARANG",
                                    "Data tambahan barang yang telah dibayar tidak dapat dihapus..!", null);
                        } else {
                            if (!(LocalDateTime.of(selectedAdditionalItem.getAdditionalDate().getYear() + 1900,
                                    selectedAdditionalItem.getAdditionalDate().getMonth() + 1,
                                    selectedAdditionalItem.getAdditionalDate().getDate(),
                                    defaultCheckOutTime.getHours(),
                                    defaultCheckOutTime.getMinutes(),
                                    0).plusDays(1)).isBefore(LocalDateTime.now())) {
                                Alert alert = ClassMessage.showConfirmationDeletingDataMessage("", null);
                                if (alert.getResult() == ButtonType.OK) {
                                    //save data to database
                                    dataReservationSaveHandle(8);
                                }
                            } else {
                                HotelFX.showAlert(Alert.AlertType.WARNING, "DATA TIDAK DAPAT DIHAPUS", "Data yang sudah lewat (hari pemesanan) tidak dapat dihapus..!", null);
                            }
                        }
                    }
                }
            } else {
                ClassMessage.showWarningSelectedDataMessage("", null);
            }
        } else {
            HotelFX.showAlert(Alert.AlertType.WARNING, "TIDAK DAPAT MENGHAPUS DATA TAMBAHAN BARANG",
                    "Penghapusan data tambahan barang hanya dapat dilakukan \n pada saat status reservasi : 'Reserved' atau 'Booked'..!", null);
        }
//        } else {
//            HotelFX.showAlert(Alert.AlertType.WARNING, "SIMPAN DATA TERLEBIH DAHULU", "Silahkan simpan data reservasi terlebih dahulu..!", null);
//        }
    }

    private BigDecimal calculationTotalAdditionalItem(TblReservationAdditionalItem addtionalItem,
            TblReservationBill bill) {
        BigDecimal result = new BigDecimal("0");
        if (addtionalItem != null) {
            result = ((new BigDecimal("1")).subtract(addtionalItem.getDiscountPercentage()))
                    .multiply(addtionalItem.getItemQuantity().multiply(addtionalItem.getItemCharge()));
            BigDecimal service = bill.getServiceChargePercentage().multiply(result);
            BigDecimal tax = bill.getTaxPercentage().multiply((result.add(service)));
            result = result.add(service).add(tax);
        }
        return result;
    }

    private void showReservationAdditionalItemDialog() {
        try {
            // Load the fxml file and create a new stage for the popup dialog.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(HotelFX.class.getResource("view/feature_reservation/ReservationAdditionalItemInputDialog.fxml"));

            ReservationAdditionalItemInputController controller = new ReservationAdditionalItemInputController(this);
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
     * HANDLE FOR DATA INPUT RESERVATION ADDITIONAL SERVICE
     */
    public TblReservationAdditionalService selectedAdditionalService;

    public List<TblReservationAdditionalService> selectedAdditionalServices;

    private void dataReservationAdditionalServiceCreateHandle() {
        if (dataInputStatus == 1) { //update
            if (selectedData.getRefReservationStatus().getIdstatus() == 1 //Reserved = '1'
                    || selectedData.getRefReservationStatus().getIdstatus() == 2) {   //Booked = '2'
//                if (tableDataReservationRoomTypeDetail.getTableView().getSelectionModel().getSelectedItems().size() == 1) {
                //set selected data
                selectedAdditionalService = new TblReservationAdditionalService();
//                    selectedAdditionalService.setTblReservationRoomTypeDetail((TblReservationRoomTypeDetail) tableDataReservationRoomTypeDetail.getTableView().getSelectionModel().getSelectedItem());
                selectedAdditionalService.setPrice(new BigDecimal("0"));
                selectedAdditionalService.setDiscountPercentage(new BigDecimal("0"));
                if (selectedData.getRefReservationStatus().getIdstatus() == 1) {  //Reserved = '1'
                    selectedAdditionalService.setRefReservationBillType(fReservationManager.getDataReservationBillType(0));    //Reservasi = '0'
                }
//                    selectedAdditionalService.setTblRoomService(new TblRoomService());
                //open form data reservation additional service dialog
                showReservationAdditionalServiceDialog();
//                } else {
//                    ClassMessage.showWarningSelectedDataMessage("", null);
//                }
            } else {
                HotelFX.showAlert(Alert.AlertType.WARNING, "TIDAK DAPAT MENAMBAHKAN DATA TAMBAHAN LAYANAN",
                        "Penambahan data tambahan layanan hanya dapat dilakukan \n pada saat status reservasi : 'Reserved' atau 'Booked'..!", null);
            }
        } else {
            HotelFX.showAlert(Alert.AlertType.WARNING, "SIMPAN DATA TERLEBIH DAHULU", "Silahkan simpan data reservasi terlebih dahulu..!", null);
        }
    }

    private void dataReservationAdditionalServiceUpdateHandle() {
        //do nothing
    }

    private void dataReservationAdditionalServiceDeleteHandle() {
//        if (dataInputStatus == 1) { //update
        if (selectedData.getRefReservationStatus().getIdstatus() != 5) {   //Checked Out = '5'
            if (tableDataReservationAdditionalService.getTableView().getSelectionModel().getSelectedItems().size() == 1) {
                selectedAdditionalService = (TblReservationAdditionalService) tableDataReservationAdditionalService.getTableView().getSelectionModel().getSelectedItem();
                if ((selectedData.getRefReservationStatus().getIdstatus() != 1 //Reserved = '1'
                        && selectedData.getRefReservationStatus().getIdstatus() != 2)) { //Booked = '2'
//                        && selectedAdditionalService.getRefReservationBillType().getIdtype() == 0) { //Reservasi = '0'
                    HotelFX.showAlert(Alert.AlertType.WARNING, "TIDAK DAPAT MENGHAPUS DATA TAMBAHAN LAYANAN",
                            "Penghapusan data tambahan layanan hanya dapat dilakukan \n pada saat status reservasi : 'Reserved' atau 'Booked'..!", null);
                } else {
//                        dataReservationAdditionalServices.remove(selectedAdditionalService);
//                        tableDataReservationAdditionalService.getTableView().setItems(FXCollections.observableArrayList(dataReservationAdditionalServices));
//                        refreshDataBill(selectedData.getRefReservationStatus().getIdstatus() == 1 //Reserved = '1'
//                                ? "reservation" : "checkout");
                    if (selectedAdditionalService.getRefReservationBillType().getIdtype() == 4) { //Include = '4'
                        HotelFX.showAlert(Alert.AlertType.WARNING, "TIDAK DAPAT MENGHAPUS DATA TAMBAHAN LAYANAN",
                                "Tidak dapat menghapus data tambahan layanan dengan status 'Include'..!", null);
                    } else {
                        if (selectedData.getRefReservationStatus().getIdstatus() == 2 //Booked = '2'
                                && selectedAdditionalService.getRefReservationBillType().getIdtype() == 0 //Reservasi = '0'
                                && ((calculationTotalRestOfBill("reservation")
                                .subtract(calculationTotalAdditionalService(selectedAdditionalService, dataReservationBill))).compareTo(new BigDecimal("0")) == -1)) {
                            HotelFX.showAlert(Alert.AlertType.WARNING, "TIDAK DAPAT MENGHAPUS DATA TAMBAHAN LAYANAN",
                                    "Data tambahan layanan yang telah dibayar tidak dapat dihapus..!", null);
                        } else {
                            if (!(LocalDateTime.of(selectedAdditionalService.getAdditionalDate().getYear() + 1900,
                                    selectedAdditionalService.getAdditionalDate().getMonth() + 1,
                                    selectedAdditionalService.getAdditionalDate().getDate(),
                                    defaultCheckOutTime.getHours(),
                                    defaultCheckOutTime.getMinutes(),
                                    0).plusDays(1)).isBefore(LocalDateTime.now())) {
                                if (selectedAdditionalService.getTblRoomService().getIdroomService() != 2 //early checkin = '2'
                                        && selectedAdditionalService.getTblRoomService().getIdroomService() != 3 //late checkout = '3'
                                        && selectedAdditionalService.getTblRoomService().getIdroomService() != 4    //bouns voucher = '4'
                                        && selectedAdditionalService.getTblRoomService().getIdroomService() != 5) { //canceling fee = '5'
                                    Alert alert = ClassMessage.showConfirmationDeletingDataMessage("", null);
                                    if (alert.getResult() == ButtonType.OK) {
                                        if (dataInputStatus == 1) {   //update
                                            //save data to database
                                            dataReservationSaveHandle(10);  //@@@+++
                                        } else {  //insert (0)
                                            ClassMessage.showSucceedDeletingDataMessage("", null);
                                            //reservation additional service (breakfast) : remove
                                            dataReservationAdditionalServices.remove(selectedAdditionalService);
                                            tableDataReservationAdditionalService.getTableView().setItems(FXCollections.observableArrayList(dataReservationAdditionalServices));
                                            refreshDataBill(selectedData.getRefReservationStatus().getIdstatus() == 1 //Reserved = '1'
                                                    ? "reservation" : "checkout");
                                            refreshDataBill("rco");
                                        }
                                    }
                                } else {
                                    HotelFX.showAlert(Alert.AlertType.WARNING, "DATA TIDAK DAPAT DIHAPUS", "Data buatan sistem tidak dapat dihapus (tambahan biaya 'early checkin', 'late checkout', atau 'voucher(bonus)')..!", null);
                                }
                            } else {
                                HotelFX.showAlert(Alert.AlertType.WARNING, "DATA TIDAK DAPAT DIHAPUS", "Data yang sudah lewat (hari pemesanan) tidak dapat dihapus..!", null);
                            }
                        }
                    }
                }
            } else {
                ClassMessage.showWarningSelectedDataMessage("", null);
            }
        } else {
            HotelFX.showAlert(Alert.AlertType.WARNING, "TIDAK DAPAT MENGHAPUS DATA TAMBAHAN LAYANAN",
                    "Penghapusan data tambahan layanan hanya dapat dilakukan \n pada saat status reservasi : 'Reserved' atau 'Booked'..!", null);
        }
//        } else {
//            HotelFX.showAlert(Alert.AlertType.WARNING, "SIMPAN DATA TERLEBIH DAHULU", "Silahkan simpan data reservasi terlebih dahulu..!", null);
//        }
    }

    private BigDecimal calculationTotalAdditionalService(TblReservationAdditionalService addtionalService,
            TblReservationBill bill) {
        BigDecimal result = new BigDecimal("0");
        if (addtionalService != null) {
            result = ((new BigDecimal("1")).subtract(addtionalService.getDiscountPercentage()))
                    .multiply(addtionalService.getPrice());
            BigDecimal service = bill.getServiceChargePercentage().multiply(result);
            BigDecimal tax = bill.getTaxPercentage().multiply((result.add(service)));
            result = result.add(service).add(tax);
        }
        return result;
    }

    private void showReservationAdditionalServiceDialog() {
        try {
            // Load the fxml file and create a new stage for the popup dialog.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(HotelFX.class.getResource("view/feature_reservation/ReservationAdditionalServiceInputDialog.fxml"));

            ReservationAdditionalServiceInputController controller = new ReservationAdditionalServiceInputController(this);
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
     * HANDLE FOR DATA INPUT RESERVATION
     */
    //0 = 'insert'
    //1 = 'update'
    public int dataInputStatus = 0;

    public void dataReservationCreateHandle() {
        dataInputStatus = 0;
        generateDataReservation();
        setSelectedDataToInputForm();
        tabPaneFormLayout.getSelectionModel().select(0);
        scrDataReservation.setVvalue(0.0);
        //open form data reservation
        dataReservationFormShowStatus.set(0.0);
        dataReservationFormShowStatus.set(1.0);
        //set unsaving data input -> 'true'
        ClassSession.unSavingDataInput.set(true);
    }

    private void setReservationBillData() {
        dataReservationBill = new TblReservationBill();
        dataReservationBill.setTblReservation(selectedData);
        BigDecimal serviceChargePercentage = new BigDecimal("0");
        SysDataHardCode sdhDefaultServiceChargePercentage = fReservationManager.getDataSysDataHardCode((long) 8);  //DefaultServiceChargePercentage = '8'
        if (sdhDefaultServiceChargePercentage != null
                && sdhDefaultServiceChargePercentage.getDataHardCodeValue() != null) {
            double defaultServiceChargePercentage = Double.parseDouble(sdhDefaultServiceChargePercentage.getDataHardCodeValue()) / 100;
            serviceChargePercentage = new BigDecimal(String.valueOf(defaultServiceChargePercentage));
        }
        dataReservationBill.setServiceChargePercentage(serviceChargePercentage);
        BigDecimal taxPercentage = new BigDecimal("0");
        SysDataHardCode sdhDefaultTaxPercentage = fReservationManager.getDataSysDataHardCode((long) 9);  //DefaultTaxPercentage = '9'
        if (sdhDefaultTaxPercentage != null
                && sdhDefaultTaxPercentage.getDataHardCodeValue() != null) {
            double defaultTaxPercentage = Double.parseDouble(sdhDefaultTaxPercentage.getDataHardCodeValue()) / 100;
            taxPercentage = new BigDecimal(String.valueOf(defaultTaxPercentage));
        }
        dataReservationBill.setTaxPercentage(taxPercentage);
        dataReservationBill.setRefReservationBillType(fReservationManager.getDataReservationBillType(0)); //Reservation = '0'
    }

    private void setCheckOutBillData() {
        dataCheckOutBill = new TblReservationBill();
        dataCheckOutBill.setTblReservation(selectedData);
        BigDecimal serviceChargePercentage = new BigDecimal("0");
        SysDataHardCode sdhDefaultServiceChargePercentage = fReservationManager.getDataSysDataHardCode((long) 8);  //DefaultServiceChargePercentage = '8'
        if (sdhDefaultServiceChargePercentage != null
                && sdhDefaultServiceChargePercentage.getDataHardCodeValue() != null) {
            double defaultServiceChargePercentage = Double.parseDouble(sdhDefaultServiceChargePercentage.getDataHardCodeValue()) / 100;
            serviceChargePercentage = new BigDecimal(String.valueOf(defaultServiceChargePercentage));
        }
        dataCheckOutBill.setServiceChargePercentage(serviceChargePercentage);
        BigDecimal taxPercentage = new BigDecimal("0");
        SysDataHardCode sdhDefaultTaxPercentage = fReservationManager.getDataSysDataHardCode((long) 9);  //DefaultTaxPercentage = '9'
        if (sdhDefaultTaxPercentage != null
                && sdhDefaultTaxPercentage.getDataHardCodeValue() != null) {
            double defaultTaxPercentage = Double.parseDouble(sdhDefaultTaxPercentage.getDataHardCodeValue()) / 100;
            taxPercentage = new BigDecimal(String.valueOf(defaultTaxPercentage));
        }
        dataCheckOutBill.setTaxPercentage(taxPercentage);
        dataCheckOutBill.setRefReservationBillType(fReservationManager.getDataReservationBillType(1));
    }

    private void generateDataReservation() {
        //sys data hardcode (default checkin time)
        defaultCheckInTime = new Time(0, 0, 0);
        SysDataHardCode sdhDefaultCheckInTime = fReservationManager.getDataSysDataHardCode((long) 6);    //DefaultCheckInTime = '6'
        if (sdhDefaultCheckInTime != null
                && sdhDefaultCheckInTime.getDataHardCodeValue() != null) {
            String[] dataCheckInTime = sdhDefaultCheckInTime.getDataHardCodeValue().split(":");
            defaultCheckInTime = new Time(Integer.parseInt(dataCheckInTime[0]),
                    Integer.parseInt(dataCheckInTime[1]),
                    Integer.parseInt(dataCheckInTime[2]));
        }
        //sys data hardcode (default checkout time)
        defaultCheckOutTime = new Time(0, 0, 0);
        SysDataHardCode sdhDefaultCheckOutTime = fReservationManager.getDataSysDataHardCode((long) 7);    //DefaultCheckOutTime = '7'
        if (sdhDefaultCheckOutTime != null
                && sdhDefaultCheckOutTime.getDataHardCodeValue() != null) {
            String[] dataCheckOutTime = sdhDefaultCheckOutTime.getDataHardCodeValue().split(":");
            defaultCheckOutTime = new Time(Integer.parseInt(dataCheckOutTime[0]),
                    Integer.parseInt(dataCheckOutTime[1]),
                    Integer.parseInt(dataCheckOutTime[2]));
        }
        //data customer
        TblCustomer dataCustomer = new TblCustomer();
        dataCustomer.setTblPeople(new TblPeople());
        dataCustomer.setDeposit(new BigDecimal("0"));
        //data reservation
        selectedData = new TblReservation();
        selectedData.setReservationDate(Timestamp.valueOf(LocalDateTime.now()));
        selectedData.setTblCustomer(dataCustomer);
        selectedData.setRefReservationStatus(fReservationManager.getReservationStatus(1)); //Reserved = '1'

        //data reservation bill
        setReservationBillData();

        //data checkout bill
        setCheckOutBillData();

        //data reservation room type detail
        dataReservationRoomTypeDetails = new ArrayList<>();
        //data reservation additional item
        dataReservationAdditionalItems = new ArrayList<>();
//        //data reservation additional item property barcode
//        dataReservationAdditionalItemPropertyBarcodes = new ArrayList<>();
        //data reservation additional service
        dataReservationAdditionalServices = new ArrayList<>();
        //data reservation broken item
        dataReservationBrokenItems = new ArrayList<>();
//        //data reservation broken item property barcode
//        dataReservationBrokenItemPropertyBarcodes = new ArrayList<>();
        //data reservation payment
        dataReservationPayments = new ArrayList<>();
        dataCheckOutPayments = new ArrayList<>();
        dataRCOPayments = new ArrayList<>();
        //data reservation payment (detail)
        dataReservationPaymentWithTransfers = new ArrayList<>();
        dataReservationPaymentWithBankCards = new ArrayList<>();
        dataReservationPaymentWithCekGiros = new ArrayList<>();
        dataReservationPaymentWithGuaranteePayments = new ArrayList<>();
        dataGuaranteeLetterItemDetails = new ArrayList<>();
        dataReservationPaymentWithReservationVouchers = new ArrayList<>();
        //data reservation room type detail - room price detail
        dataReservationRoomTypeDetailRoomPriceDetails = new ArrayList<>();
        //data reservation room type detail - travel agent discount detail
        dataReservationRoomTypeDetailTravelAgentDiscountDetails = new ArrayList<>();
        //data travel agent
        if (reservationType.getIdtype() == 1) { //Travel Agent = '1'
            selectedData.setTblPartner(currentTravelAgent.getTblPartner());
        }
        //data reservation payment (RCO)
        dataRCOPayments = new ArrayList<>();
    }

    private void dataReservationUpdateHandle() {
        if (tableDataReservation.getTableView().getSelectionModel().getSelectedItems().size() == 1) {
            selectedData = fReservationManager.getReservation(((TblReservation) tableDataReservation.getTableView().getSelectionModel().getSelectedItem()).getIdreservation());
            selectedData.setRefReservationStatus(fReservationManager.getReservationStatus(selectedData.getRefReservationStatus().getIdstatus()));
            if (selectedData.getRefReservationStatus().getIdstatus() == 1 //Reserved = '1'
                    || selectedData.getRefReservationStatus().getIdstatus() == 2) {  //Booked = '2'
                dataReservationUpdateHandleDetail();
            } else {
                HotelFX.showAlert(Alert.AlertType.WARNING, "DATA RESERVASI TIDAK DAPAT DIUBAH",
                        "Perubahan data reservasi hanya dapat dilakukan \n pada saat status reservasi : 'Reserved' atau 'Booked'..!", null);
            }
        } else {
            ClassMessage.showWarningSelectedDataMessage("", null);
        }
    }

    private void dataReservationUpdateHandleDetail() {
        dataInputStatus = 1;
        loadDataReservation(((TblReservation) tableDataReservation.getTableView().getSelectionModel().getSelectedItem()).getIdreservation());
        setSelectedDataToInputForm();
        tabPaneFormLayout.getSelectionModel().select(0);
        scrDataReservation.setVvalue(0.0);
        //open form data reservation
        dataReservationFormShowStatus.set(0.0);
        dataReservationFormShowStatus.set(1.0);
        //set unsaving data input -> 'false'
        ClassSession.unSavingDataInput.set(false);
    }

    private void loadDataReservation(long idReservation) {
        //sys data hardcode (default checkin time)
        defaultCheckInTime = new Time(0, 0, 0);
        SysDataHardCode sdhDefaultCheckInTime = fReservationManager.getDataSysDataHardCode((long) 6);    //DefaultCheckInTime = '6'
        if (sdhDefaultCheckInTime != null
                && sdhDefaultCheckInTime.getDataHardCodeValue() != null) {
            String[] dataCheckInTime = sdhDefaultCheckInTime.getDataHardCodeValue().split(":");
            defaultCheckInTime = new Time(Integer.parseInt(dataCheckInTime[0]),
                    Integer.parseInt(dataCheckInTime[1]),
                    Integer.parseInt(dataCheckInTime[2]));
        }
        //sys data hardcode (default checkout time)
        defaultCheckOutTime = new Time(0, 0, 0);
        SysDataHardCode sdhDefaultCheckOutTime = fReservationManager.getDataSysDataHardCode((long) 7);    //DefaultCheckOutTime = '7'
        if (sdhDefaultCheckOutTime != null
                && sdhDefaultCheckOutTime.getDataHardCodeValue() != null) {
            String[] dataCheckOutTime = sdhDefaultCheckOutTime.getDataHardCodeValue().split(":");
            defaultCheckOutTime = new Time(Integer.parseInt(dataCheckOutTime[0]),
                    Integer.parseInt(dataCheckOutTime[1]),
                    Integer.parseInt(dataCheckOutTime[2]));
        }
        //data reservation
        selectedData = fReservationManager.getReservation(idReservation);
        selectedData.setTblCustomer(fReservationManager.getCustomer(selectedData.getTblCustomer().getIdcustomer()));
        selectedData.getTblCustomer().setTblPeople(fReservationManager.getPeople(selectedData.getTblCustomer().getTblPeople().getIdpeople()));
        selectedData.setRefReservationStatus(fReservationManager.getReservationStatus(selectedData.getRefReservationStatus().getIdstatus()));
        //data reservation bill
        dataReservationBill = fReservationManager.getReservationBillByIDReservation(selectedData.getIdreservation());
        dataReservationBill.setTblReservation(selectedData);
        if (dataReservationBill.getTblBankCard() != null) {
            dataReservationBill.setTblBankCard(fReservationManager.getDataBankCard(dataReservationBill.getTblBankCard().getIdbankCard()));
            dataReservationBill.getTblBankCard().setTblBank(fReservationManager.getBank(dataReservationBill.getTblBankCard().getTblBank().getIdbank()));
            dataReservationBill.getTblBankCard().setRefBankCardType(fReservationManager.getDataBankCardType(dataReservationBill.getTblBankCard().getRefBankCardType().getIdtype()));
        }
        if (dataReservationBill.getRefReservationBillDiscountType() != null) {
            dataReservationBill.setRefReservationBillDiscountType(fReservationManager.getReservationBillDiscountType(dataReservationBill.getRefReservationBillDiscountType().getIdtype()));
        }
        //data checkout bill
        dataCheckOutBill = fReservationManager.getCheckOutBillByIDReservation(selectedData.getIdreservation());
        dataCheckOutBill.setTblReservation(selectedData);
        if (dataCheckOutBill.getTblBankCard() != null) {
            dataCheckOutBill.setTblBankCard(fReservationManager.getDataBankCard(dataCheckOutBill.getTblBankCard().getIdbankCard()));
            dataCheckOutBill.getTblBankCard().setTblBank(fReservationManager.getBank(dataCheckOutBill.getTblBankCard().getTblBank().getIdbank()));
            dataCheckOutBill.getTblBankCard().setRefBankCardType(fReservationManager.getDataBankCardType(dataCheckOutBill.getTblBankCard().getRefBankCardType().getIdtype()));
        }
        if (dataCheckOutBill.getRefReservationBillDiscountType() != null) {
            dataCheckOutBill.setRefReservationBillDiscountType(fReservationManager.getReservationBillDiscountType(dataCheckOutBill.getRefReservationBillDiscountType().getIdtype()));
        }
        //data reservation room type detail
        dataReservationRoomTypeDetails = fReservationManager.getAllDataReservationRoomTypeDetailByIDReservation(selectedData.getIdreservation());
        for (TblReservationRoomTypeDetail data : dataReservationRoomTypeDetails) {
            if (data.getTblReservationCheckInOut() != null) {
                data.setTblReservationCheckInOut(fReservationManager.getReservationCheckInOut(data.getTblReservationCheckInOut().getIdcheckInOut()));
                if (data.getTblReservationCheckInOut().getTblRoom() != null) {
                    data.getTblReservationCheckInOut().setTblRoom(fReservationManager.getRoom(data.getTblReservationCheckInOut().getTblRoom().getIdroom()));
                }
            }
        }
        //data reservation additional item
        dataReservationAdditionalItems = new ArrayList<>();
        for (TblReservationRoomTypeDetail data : dataReservationRoomTypeDetails) {
            dataReservationAdditionalItems.addAll(fReservationManager.getAllDataReservationAdditionalItemByIDReservationRoomTypeDetail(data.getIddetail()));
        }
//        //data reservation additional item property barcode
//        dataReservationAdditionalItemPropertyBarcodes = new ArrayList<>();
        //data reservation additional service
        dataReservationAdditionalServices = new ArrayList<>();
        for (TblReservationRoomTypeDetail data : dataReservationRoomTypeDetails) {
            dataReservationAdditionalServices.addAll(fReservationManager.getAllDataReservationAdditionalServiceByIDReservationRoomTypeDetail(data.getIddetail()));
        }
        //data reservation broken item
        dataReservationBrokenItems = new ArrayList<>();
        for (TblReservationRoomTypeDetail data : dataReservationRoomTypeDetails) {
            List<TblReservationBrokenItem> listData = fReservationManager.getAllDataReservationBrokenItemByIDReservationRoomTypeDetail(data.getIddetail());
            for (TblReservationBrokenItem ld : listData) {
                //data item
                ld.setTblItem(fReservationManager.getDataItem(ld.getTblItem().getIditem()));
                //data unit
                ld.getTblItem().setTblUnit(fReservationManager.getDataUnit(ld.getTblItem().getTblUnit().getIdunit()));
            }
            dataReservationBrokenItems.addAll(listData);
        }
//        //data reservation broken item property barcode
//        dataReservationBrokenItemPropertyBarcodes = new ArrayList<>();
        //data reservation payment
        dataReservationPayments = fReservationManager.getAllDataReservationPaymentByIDReservationBill(dataReservationBill.getIdbill());
        dataCheckOutPayments = fReservationManager.getAllDataCheckOutPaymentByIDReservationBill(dataCheckOutBill.getIdbill());
        //data reservation payment (detail)
        dataReservationPaymentWithTransfers = new ArrayList<>();
        dataReservationPaymentWithBankCards = new ArrayList<>();
        dataReservationPaymentWithCekGiros = new ArrayList<>();
        dataReservationPaymentWithGuaranteePayments = new ArrayList<>();
        dataGuaranteeLetterItemDetails = new ArrayList<>();
        dataReservationPaymentWithReservationVouchers = new ArrayList<>();
        for (TblReservationPayment dataReservationPayment : dataReservationPayments) {
            switch (dataReservationPayment.getRefFinanceTransactionPaymentType().getIdtype()) {
                case 1:    //Transfer = '1'
                    dataReservationPaymentWithTransfers.add(fReservationManager.getReservationPaymentWithTransferByIDPayment(dataReservationPayment.getIdpayment()));
                    break;
                case 2:   //Kartu Debit = '2'
                    dataReservationPaymentWithBankCards.add(fReservationManager.getReservationPaymentWithBankCardByIDPayment(dataReservationPayment.getIdpayment()));
                    break;
                case 3:  //Kartu Kredit '3'
                    dataReservationPaymentWithBankCards.add(fReservationManager.getReservationPaymentWithBankCardByIDPayment(dataReservationPayment.getIdpayment()));
                    break;
                case 4:     //Cek = '4'
                    dataReservationPaymentWithCekGiros.add(fReservationManager.getReservationPaymentWithCekGiroByIDPayment(dataReservationPayment.getIdpayment()));
                    break;
                case 5:    //Giro = '5'
                    dataReservationPaymentWithCekGiros.add(fReservationManager.getReservationPaymentWithCekGiroByIDPayment(dataReservationPayment.getIdpayment()));
                    break;
                case 6:    //Travel Agent = '6'
                    TblReservationPaymentWithGuaranteePayment dataTAGL = fReservationManager.getReservationPaymentWithGuaranteePaymentByIDPayment(dataReservationPayment.getIdpayment());
                    dataReservationPaymentWithGuaranteePayments.add(dataTAGL);
                    dataGuaranteeLetterItemDetails.addAll(fReservationManager.getAllDataGuaranteeLetterItemDetailByIDGuaranteeLetter(dataTAGL.getIddetail()));
                    break;
                case 7:    //Guarantee Letter (Corporate) = '7'
                    TblReservationPaymentWithGuaranteePayment dataCGL = fReservationManager.getReservationPaymentWithGuaranteePaymentByIDPayment(dataReservationPayment.getIdpayment());
                    dataReservationPaymentWithGuaranteePayments.add(fReservationManager.getReservationPaymentWithGuaranteePaymentByIDPayment(dataReservationPayment.getIdpayment()));
                    dataGuaranteeLetterItemDetails.addAll(fReservationManager.getAllDataGuaranteeLetterItemDetailByIDGuaranteeLetter(dataCGL.getIddetail()));
                    break;
                case 8:   //Guarantee Letter (Government) = '8'
                    TblReservationPaymentWithGuaranteePayment dataGGL = fReservationManager.getReservationPaymentWithGuaranteePaymentByIDPayment(dataReservationPayment.getIdpayment());
                    dataReservationPaymentWithGuaranteePayments.add(fReservationManager.getReservationPaymentWithGuaranteePaymentByIDPayment(dataReservationPayment.getIdpayment()));
                    dataGuaranteeLetterItemDetails.addAll(fReservationManager.getAllDataGuaranteeLetterItemDetailByIDGuaranteeLetter(dataGGL.getIddetail()));
                    break;
                case 10: //Voucher = '10'
                    dataReservationPaymentWithReservationVouchers.add(fReservationManager.getReservationPaymentWithReservationVoucherByIDPayment(dataReservationPayment.getIdpayment()));
                    break;
                default:
                    break;
            }
        }
        //data reservation room type detail - room price detail
        dataReservationRoomTypeDetailRoomPriceDetails = new ArrayList<>();
        for (TblReservationRoomTypeDetail data : dataReservationRoomTypeDetails) {
            dataReservationRoomTypeDetailRoomPriceDetails.addAll(fReservationManager.getAllDataReservationRoomTypeDetailRoomPriceDetailByIDReservationRoomTypeDetail(data.getIddetail()));
        }
        for (TblReservationRoomTypeDetailRoomPriceDetail data : dataReservationRoomTypeDetailRoomPriceDetails) {
            data.setTblReservationRoomTypeDetail(fReservationManager.getReservationRoomTypeDetail(data.getTblReservationRoomTypeDetail().getIddetail()));
            data.setTblReservationRoomPriceDetail(fReservationManager.getReservationRoomPriceDetail(data.getTblReservationRoomPriceDetail().getIddetail()));
        }
        //data reservation room type detail - travel agent discount detail
        dataReservationRoomTypeDetailTravelAgentDiscountDetails = new ArrayList<>();
        for (TblReservationRoomTypeDetail data : dataReservationRoomTypeDetails) {
            dataReservationRoomTypeDetailTravelAgentDiscountDetails.addAll(fReservationManager.getAllDataReservationRoomTypeDetailTravelAgentDiscountDetailByIDReservationRoomTypeDetail(data.getIddetail()));
        }
        for (TblReservationRoomTypeDetailTravelAgentDiscountDetail data : dataReservationRoomTypeDetailTravelAgentDiscountDetails) {
            data.setTblReservationRoomTypeDetail(fReservationManager.getReservationRoomTypeDetail(data.getTblReservationRoomTypeDetail().getIddetail()));
            data.setTblReservationTravelAgentDiscountDetail(fReservationManager.getReservationTravelAgentDiscountDetail(data.getTblReservationTravelAgentDiscountDetail().getIddetail()));
        }
        //Travel Agent
        if (selectedData.getTblPartner() != null) {   //Travel Agent
            selectedData.setTblPartner(fReservationManager.getPartner(selectedData.getTblPartner().getIdpartner()));
            currentTravelAgent = fReservationManager.getTravelAgentByIDPartner(selectedData.getTblPartner().getIdpartner());
            reservationType = fReservationManager.getReservationOrderByType(1);
        } else {  //Customer
            currentTravelAgent = null;
            reservationType = fReservationManager.getReservationOrderByType(0);
        }
        //data reservation payment (RCO)
        dataRCOPayments = loadAllDataRCOTransaction();
    }

    private final BooleanProperty isShowStatus = new SimpleBooleanProperty(false);

    private void dataReservationShowHandle() {
        if (tableDataReservation.getTableView().getSelectionModel().getSelectedItems().size() == 1) {
            dataInputStatus = 3;
            loadDataReservation(((TblReservation) tableDataReservation.getTableView().getSelectionModel().getSelectedItem()).getIdreservation());
            setSelectedDataToInputForm();
            tabPaneFormLayout.getSelectionModel().select(0);
            scrDataReservation.setVvalue(0.0);
            dataReservationFormShowStatus.set(1.0);
            isShowStatus.set(true);
        }
    }

    private void dataReservationUnshowHandle() {
//        tableDataReservation.getTableView().setItems(loadAllDataReservation());
//        cft.refreshFilterItems(tableDataReservation.getTableView().getItems());
        refreshDataTableReservation();
        dataReservationFormShowStatus.set(0);
        isShowStatus.set(false);
    }

    private void dataReservationDeleteHandle() {
        if (tableDataReservation.getTableView().getSelectionModel().getSelectedItems().size() == 1) {
            selectedData = fReservationManager.getReservation(((TblReservation) tableDataReservation.getTableView().getSelectionModel().getSelectedItem()).getIdreservation());
            selectedData.setRefReservationStatus(fReservationManager.getReservationStatus(selectedData.getRefReservationStatus().getIdstatus()));
            if (selectedData.getRefReservationStatus().getIdstatus() == 1 //Reserved = '1'
                    || selectedData.getRefReservationStatus().getIdstatus() == 2) {   //Booked = '2'
                if (!checkReservationHasBeenOrStillCheckIn(selectedData.getIdreservation())) {
                    Alert alert = HotelFX.showAlertConfirm(Alert.AlertType.CONFIRMATION, "KONFIRMASI", "Apakah anda yakin membatalkan reservasi?", null);
                    if (alert.getResult() == ButtonType.OK) {
                        //Hmmm....
                        loadDataReservation(((TblReservation) tableDataReservation.getTableView().getSelectionModel().getSelectedItem()).getIdreservation());
                        //data reservation
                        selectedData.setTblCustomer(fReservationManager.getCustomer(selectedData.getTblCustomer().getIdcustomer()));
                        selectedData.getTblCustomer().setTblPeople(fReservationManager.getPeople(selectedData.getTblCustomer().getTblPeople().getIdpeople()));
                        //data reservation canceled
                        selectedDataReservationCanceled = new TblReservationCanceled();
                        selectedDataReservationCanceled.setTblReservation(selectedData);
                        //data reservation bill (canceling fee)
                        TblReservationBill selectedDataBill = new TblReservationBill();
                        selectedDataBill.setTblReservation(selectedData);
                        selectedDataBill.setRefReservationBillType(fReservationManager.getDataReservationBillType(3));   //Canceling Fee = '3'
                        selectedDataBill.setTaxPercentage(new BigDecimal("0"));
                        selectedDataBill.setServiceChargePercentage(new BigDecimal("0"));
                        //data reservation transaction payment
                        selectedDataTransaction = new TblReservationPayment();
                        selectedDataTransaction.setTblReservationBill(selectedDataBill);
                        selectedDataTransaction.setUnitNominal(new BigDecimal("0"));
                        selectedDataTransaction.setRoundingValue(new BigDecimal("0"));
                        dataReservationBill = fReservationManager.getReservationBillByIDReservation(selectedDataReservationCanceled.getTblReservation().getIdreservation());
                        dataCheckOutBill = fReservationManager.getCheckOutBillByIDReservation(selectedDataReservationCanceled.getTblReservation().getIdreservation());
                        dataReservationPayments = fReservationManager.getAllDataReservationPaymentByIDReservationBill(dataReservationBill.getIdbill());
                        dataCheckOutPayments = fReservationManager.getAllDataCheckOutPaymentByIDReservationBill(dataCheckOutBill.getIdbill());
                        dataRCOPayments = loadAllDataRCOTransaction();
                        //show dialog input reservation canceled
                        showReservationCanceledDialog();
                    }
                } else {
                    HotelFX.showAlert(Alert.AlertType.WARNING, "DATA RESERVASI TIDAK DAPAT DIBATALKAN",
                            "Reservasi tidak dapat dibatalkan, \n apabila terdapat data reservasi kamar dengan status 'checked in'..!", null);
                }
            } else {
                HotelFX.showAlert(Alert.AlertType.WARNING, "DATA RESERVASI TIDAK DAPAT DIBATALKAN",
                        "Pembatalan reservasi hanya dapat dilakukan \n pada saat status reservasi : 'Reserved' atau 'Booked'..!", null);
            }
        } else {
            ClassMessage.showWarningSelectedDataMessage("", null);
        }
    }

    private boolean checkReservationHasBeenOrStillCheckIn(long idReservation) {
        boolean hasBeenOrStillCheckIn = false;
        List<TblReservationRoomTypeDetail> tempDataRRTDs = fReservationManager.getAllDataReservationRoomTypeDetailByIDReservation(idReservation);
        for (TblReservationRoomTypeDetail tempDataRRTD : tempDataRRTDs) {
            if (tempDataRRTD.getTblReservationCheckInOut() != null) {
                if (tempDataRRTD.getTblReservationCheckInOut().getRefReservationCheckInOutStatus().getIdstatus() == 1 //Checked In = '1'
                        || tempDataRRTD.getTblReservationCheckInOut().getRefReservationCheckInOutStatus().getIdstatus() == 2    //Ready to Check Out = '2'
                        || tempDataRRTD.getTblReservationCheckInOut().getRefReservationCheckInOutStatus().getIdstatus() == 3) {  //Checked Out = '3'
                    hasBeenOrStillCheckIn = true;
                    break;
                }
            }
        }
        return hasBeenOrStillCheckIn;
    }

    public TblReservationCanceled selectedDataReservationCanceled;

    private void showReservationCanceledDialog() {
        try {
            // Load the fxml file and create a new stage for the popup dialog.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(HotelFX.class.getResource("view/feature_reservation/ReservationCanceledInputDialog.fxml"));

            ReservationCanceledInputController controller = new ReservationCanceledInputController(this);
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

    private void dataReservationPrintHandle() {
//        if (tableDataReservation.getTableView().getSelectionModel().getSelectedItems().size() == 1) {
//            String hotelName = "";
//            SysDataHardCode sdhHotelName = fReservationManager.getDataSysDataHardCode(12);  //HotelName = '12'
//            if(sdhHotelName != null
//                    && sdhHotelName.getDataHardCodeValue() != null){
//                hotelName = sdhHotelName.getDataHardCodeValue();
//            }
//            String hotelAddress = "";
//            SysDataHardCode sdhHotelAddress = fReservationManager.getDataSysDataHardCode(13);  //HotelAddress = '13'
//            if(sdhHotelAddress != null
//                    && sdhHotelAddress.getDataHardCodeValue() != null){
//                hotelAddress = sdhHotelAddress.getDataHardCodeValue();
//            }
//            String hotelPhoneNumber = "";
//            SysDataHardCode sdhHotelPhoneNumber = fReservationManager.getDataSysDataHardCode(14);  //HotelPhoneNumber = '14'
//            if(sdhHotelPhoneNumber != null
//                    && sdhHotelPhoneNumber.getDataHardCodeValue() != null){
//                hotelPhoneNumber = sdhHotelPhoneNumber.getDataHardCodeValue();
//            }
//            String hotelLogoName = "";
//            SysDataHardCode sdhHotelLogoName = fReservationManager.getDataSysDataHardCode(15);  //HotelLogoName = '15'
//            if(sdhHotelLogoName != null
//                    && sdhHotelLogoName.getDataHardCodeValue() != null){
//                hotelLogoName = sdhHotelLogoName.getDataHardCodeValue();
//            }
//            try {
//                ClassPrinter.printBon(hotelName,
//                        hotelAddress, 
//                        hotelPhoneNumber, 
//                        hotelLogoName,
//                        ((TblReservation) tableDataReservation.getTableView().getSelectionModel().getSelectedItem()).getIdreservation(),
//                        0); //Reservation = '0'
//            } catch (Throwable ex) {
//                Logger.getLogger(ReservationController.class.getName()).log(Level.SEVERE, null, ex);
//            }
//        } else {
//            ClassMessage.showWarningSelectedDataMessage("", null);
//        }
//        dataReservationCancelingTransactionPrintHandle();
    }

    private void dataReservationLogBookHandle() {
        showReservationLogBookDialog();
    }

    private void showReservationLogBookDialog() {
        try {
            // Load the fxml file and create a new stage for the popup dialog.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(HotelFX.class.getResource("view/feature_reservation/ReservationLogBookView.fxml"));

            ReservationLogBookController controller = new ReservationLogBookController(this);
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

    private void dataReservationBillPrintHandle(TblReservationBill dataBill, int language) {
        if (dataBill.getTblReservation().getIdreservation() != 0L) {
            String hotelName = "";
            SysDataHardCode sdhHotelName = fReservationManager.getDataSysDataHardCode((long) 12);  //HotelName = '12'
            if (sdhHotelName != null
                    && sdhHotelName.getDataHardCodeValue() != null) {
                hotelName = sdhHotelName.getDataHardCodeValue();
            }
            String hotelAddress = "";
            SysDataHardCode sdhHotelAddress = fReservationManager.getDataSysDataHardCode((long) 13);  //HotelAddress = '13'
            if (sdhHotelAddress != null
                    && sdhHotelAddress.getDataHardCodeValue() != null) {
                hotelAddress = sdhHotelAddress.getDataHardCodeValue();
            }
            String hotelPhoneNumber = "";
            SysDataHardCode sdhHotelPhoneNumber = fReservationManager.getDataSysDataHardCode((long) 14);  //HotelPhoneNumber = '14'
            if (sdhHotelPhoneNumber != null
                    && sdhHotelPhoneNumber.getDataHardCodeValue() != null) {
                hotelPhoneNumber = sdhHotelPhoneNumber.getDataHardCodeValue();
            }
            String hotelLogoName = "";
            SysDataHardCode sdhHotelLogoName = fReservationManager.getDataSysDataHardCode((long) 15);  //HotelLogoName = '15'
            if (sdhHotelLogoName != null
                    && sdhHotelLogoName.getDataHardCodeValue() != null) {
                hotelLogoName = sdhHotelLogoName.getDataHardCodeValue();
            }
            try {
                ClassPrinter.printBon(hotelName,
                        hotelAddress,
                        hotelPhoneNumber,
                        "",
                        hotelLogoName,
                        dataBill,
                        language);
            } catch (Throwable ex) {
                Logger.getLogger(ReservationController.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else {
            HotelFX.showAlert(Alert.AlertType.WARNING, "SIMPAN DATA TERLEBIH DAHULU", "Silahkan simpan data reservasi terlebih dahulu..!", null);
        }
    }

    //- 1 = data reservation 
    //- 2 = data transaction 
    //- 3 = data reservation room type detail (insert) 
    //- 4 = data reservation room type detail (delete) 
    //- 5 = data reservation room type detail (update, insert:checkinout) 
    //- 6 = data reservation room type detail (update, update:checkinout) 
    //- 7 = data reservation additional item (insert)
    //- 8 = data reservation additional item (delete)
    //- 9 = data reservation additional service (insert) 
    //-10 = data reservation additional service (delete)
    //-11 = data reservation room type detail (update, insert:checkinout) + reservation additional service (insert : 'early check in')
    //-12 = data reservation room type detail (update, update:checkinout) + reservation additional service (insert : 'late check out')
    //-13 = data reservation room type detail (update, update : checkinout -> card used number)
    //-14 = data reservation bill (update : discount type) + reservation room type detail (room price detail), additional item, additional service
    //-15 = data reservation room type detail (update : room type)
    //-16 = data reservation room type detail (update : checkout date, data checkin/out) + reservation room type detail (new), reservation additional item/service (new), reservation transaction (new), reservation transaction detail (new), data change room & data additional discount
    //-17 = 
    //-18 = data reservation room type detail (update : checkout date), reservation room checkin/out (udapte : status), reservation room type detail - room price detail (new), reservation additional item/service (new)
    public boolean dataReservationSaveHandle(int updateType) {
        boolean statusSave = false;
        //dummy entry
        TblReservation dummySelectedData = new TblReservation(selectedData);
        dummySelectedData.setTblCustomer(new TblCustomer(dummySelectedData.getTblCustomer()));
        dummySelectedData.getTblCustomer().setTblPeople(new TblPeople(dummySelectedData.getTblCustomer().getTblPeople()));
        if (dummySelectedData.getTblPartner() != null) {
            dummySelectedData.setTblPartner(new TblPartner(dummySelectedData.getTblPartner()));
        }
        dummySelectedData.setRefReservationStatus(new RefReservationStatus(dummySelectedData.getRefReservationStatus()));
        switch (dataInputStatus) {
            case 0:
                if (checkDataInputDataReservation()) {
                    //dummy entry
                    TblReservationBill dummyDataReservationBill0 = new TblReservationBill(dataReservationBill);
                    dummyDataReservationBill0.setTblReservation(dummySelectedData);
                    dummyDataReservationBill0.setRefReservationBillType(new RefReservationBillType(dummyDataReservationBill0.getRefReservationBillType()));
                    if (dummyDataReservationBill0.getTblBankCard() != null) {
                        dummyDataReservationBill0.setTblBankCard(new TblBankCard(dummyDataReservationBill0.getTblBankCard()));
                    }
                    if (dummyDataReservationBill0.getRefReservationBillDiscountType() != null) {
                        dummyDataReservationBill0.setRefReservationBillDiscountType(new RefReservationBillDiscountType(dummyDataReservationBill0.getRefReservationBillDiscountType()));
                    }
                    TblReservationBill dummyDataCheckOutBill0 = new TblReservationBill(dataCheckOutBill);
                    dummyDataCheckOutBill0.setTblReservation(dummySelectedData);
                    dummyDataCheckOutBill0.setRefReservationBillType(new RefReservationBillType(dummyDataCheckOutBill0.getRefReservationBillType()));
                    if (dummyDataCheckOutBill0.getTblBankCard() != null) {
                        dummyDataCheckOutBill0.setTblBankCard(new TblBankCard(dummyDataCheckOutBill0.getTblBankCard()));
                    }
                    if (dummyDataCheckOutBill0.getRefReservationBillDiscountType() != null) {
                        dummyDataCheckOutBill0.setRefReservationBillDiscountType(new RefReservationBillDiscountType(dummyDataCheckOutBill0.getRefReservationBillDiscountType()));
                    }
                    List<TblReservationRoomTypeDetail> dummyDataRoomTypeDetails0 = new ArrayList<>();
                    List<TblReservationRoomTypeDetailRoomPriceDetail> dummyDataRoomTypeDetailRoomPriceDetails0 = new ArrayList<>();
                    List<TblReservationRoomTypeDetailTravelAgentDiscountDetail> dummyDataRoomTypeDetailTravelAgentDiscountDetails0 = new ArrayList<>();
                    List<TblReservationAdditionalItem> dummyDataReservationAdditionalItems0 = new ArrayList<>();
                    List<TblReservationAdditionalService> dummyDataReservationAdditionalServices0 = new ArrayList<>();
                    for (TblReservationRoomTypeDetail dataRoomTypeDetail0 : dataReservationRoomTypeDetails) {
                        TblReservationRoomTypeDetail dummyDataRoomTypeDetail0 = new TblReservationRoomTypeDetail(dataRoomTypeDetail0);
                        dummyDataRoomTypeDetail0.setTblReservation(dummySelectedData);
                        dummyDataRoomTypeDetail0.setTblRoomType(new TblRoomType(dummyDataRoomTypeDetail0.getTblRoomType()));
                        if (dummyDataRoomTypeDetail0.getTblReservationCheckInOut() != null) {
                            dummyDataRoomTypeDetail0.setTblReservationCheckInOut(new TblReservationCheckInOut(dummyDataRoomTypeDetail0.getTblReservationCheckInOut()));
                            if (dummyDataRoomTypeDetail0.getTblReservationCheckInOut().getTblRoom() != null) {
                                dummyDataRoomTypeDetail0.getTblReservationCheckInOut().setTblRoom(new TblRoom(dummyDataRoomTypeDetail0.getTblReservationCheckInOut().getTblRoom()));
                            }
                            if (dummyDataRoomTypeDetail0.getTblReservationCheckInOut().getRefReservationCheckInOutStatus() != null) {
                                dummyDataRoomTypeDetail0.getTblReservationCheckInOut().setRefReservationCheckInOutStatus(new RefReservationCheckInOutStatus(dummyDataRoomTypeDetail0.getTblReservationCheckInOut().getRefReservationCheckInOutStatus()));
                            }
                        }
                        for (TblReservationRoomTypeDetailRoomPriceDetail dataRoomTypeDetailRoomPriceDetail0 : dataReservationRoomTypeDetailRoomPriceDetails) {
                            if (dataRoomTypeDetailRoomPriceDetail0.getTblReservationRoomTypeDetail().equals(dataRoomTypeDetail0)) {
                                TblReservationRoomTypeDetailRoomPriceDetail dummyDataRoomTypeDetailRoomPriceDetail0 = new TblReservationRoomTypeDetailRoomPriceDetail(dataRoomTypeDetailRoomPriceDetail0);
                                dummyDataRoomTypeDetailRoomPriceDetail0.setTblReservationRoomTypeDetail(dummyDataRoomTypeDetail0);
                                dummyDataRoomTypeDetailRoomPriceDetail0.setTblReservationRoomPriceDetail(new TblReservationRoomPriceDetail(dummyDataRoomTypeDetailRoomPriceDetail0.getTblReservationRoomPriceDetail()));
                                if (dummyDataRoomTypeDetailRoomPriceDetail0.getTblReservationRoomPriceDetail().getTblHotelEvent() != null) {
                                    dummyDataRoomTypeDetailRoomPriceDetail0.getTblReservationRoomPriceDetail().setTblHotelEvent(new TblHotelEvent(dummyDataRoomTypeDetailRoomPriceDetail0.getTblReservationRoomPriceDetail().getTblHotelEvent()));
                                }
                                if (dummyDataRoomTypeDetailRoomPriceDetail0.getTblReservationRoomPriceDetail().getTblBankEventCard() != null) {
                                    dummyDataRoomTypeDetailRoomPriceDetail0.getTblReservationRoomPriceDetail().setTblBankEventCard(new TblBankEventCard(dummyDataRoomTypeDetailRoomPriceDetail0.getTblReservationRoomPriceDetail().getTblBankEventCard()));
                                }
                                dummyDataRoomTypeDetailRoomPriceDetails0.add(dummyDataRoomTypeDetailRoomPriceDetail0);
                            }
                        }
                        for (TblReservationRoomTypeDetailTravelAgentDiscountDetail dataRoomTypeDetailTravelAgentDiscountDetail0 : dataReservationRoomTypeDetailTravelAgentDiscountDetails) {
                            if (dataRoomTypeDetailTravelAgentDiscountDetail0.getTblReservationRoomTypeDetail().equals(dataRoomTypeDetail0)) {
                                TblReservationRoomTypeDetailTravelAgentDiscountDetail dummyDataRoomTypeDetailTravelAgentDiscountDetail0 = new TblReservationRoomTypeDetailTravelAgentDiscountDetail(dataRoomTypeDetailTravelAgentDiscountDetail0);
                                dummyDataRoomTypeDetailTravelAgentDiscountDetail0.setTblReservationRoomTypeDetail(dummyDataRoomTypeDetail0);
                                dummyDataRoomTypeDetailTravelAgentDiscountDetail0.setTblReservationTravelAgentDiscountDetail(new TblReservationTravelAgentDiscountDetail(dummyDataRoomTypeDetailTravelAgentDiscountDetail0.getTblReservationTravelAgentDiscountDetail()));
                                dummyDataRoomTypeDetailTravelAgentDiscountDetail0.getTblReservationTravelAgentDiscountDetail().setTblPartner(new TblPartner(dummyDataRoomTypeDetailTravelAgentDiscountDetail0.getTblReservationTravelAgentDiscountDetail().getTblPartner()));
                                dummyDataRoomTypeDetailTravelAgentDiscountDetails0.add(dummyDataRoomTypeDetailTravelAgentDiscountDetail0);
                            }
                        }
                        for (TblReservationAdditionalItem dataReservationAdditionalItem0 : dataReservationAdditionalItems) {
                            if (dataReservationAdditionalItem0.getTblReservationRoomTypeDetail().equals(dataRoomTypeDetail0)) {
                                TblReservationAdditionalItem dummyDataReservationAdditionalItem0 = new TblReservationAdditionalItem(dataReservationAdditionalItem0);
                                dummyDataReservationAdditionalItem0.setTblReservationRoomTypeDetail(dummyDataRoomTypeDetail0);
                                dummyDataReservationAdditionalItem0.setTblItem(new TblItem(dummyDataReservationAdditionalItem0.getTblItem()));
                                dummyDataReservationAdditionalItem0.setRefReservationBillType(new RefReservationBillType(dummyDataReservationAdditionalItem0.getRefReservationBillType()));
                                if (dummyDataReservationAdditionalItem0.getTblHotelEvent() != null) {
                                    dummyDataReservationAdditionalItem0.setTblHotelEvent(new TblHotelEvent(dummyDataReservationAdditionalItem0.getTblHotelEvent()));
                                }
                                if (dummyDataReservationAdditionalItem0.getTblBankEventCard() != null) {
                                    dummyDataReservationAdditionalItem0.setTblBankEventCard(new TblBankEventCard(dummyDataReservationAdditionalItem0.getTblBankEventCard()));
                                }
                                dummyDataReservationAdditionalItems0.add(dummyDataReservationAdditionalItem0);
                            }
                        }
                        for (TblReservationAdditionalService dataReservationAdditionalService0 : dataReservationAdditionalServices) {
                            if (dataReservationAdditionalService0.getTblReservationRoomTypeDetail().equals(dataRoomTypeDetail0)) {
                                TblReservationAdditionalService dummyDataReservationAdditionalService0 = new TblReservationAdditionalService(dataReservationAdditionalService0);
                                dummyDataReservationAdditionalService0.setTblReservationRoomTypeDetail(dummyDataRoomTypeDetail0);
                                dummyDataReservationAdditionalService0.setTblRoomService(new TblRoomService(dummyDataReservationAdditionalService0.getTblRoomService()));
                                dummyDataReservationAdditionalService0.setRefReservationBillType(new RefReservationBillType(dummyDataReservationAdditionalService0.getRefReservationBillType()));
                                if (dummyDataReservationAdditionalService0.getTblHotelEvent() != null) {
                                    dummyDataReservationAdditionalService0.setTblHotelEvent(new TblHotelEvent(dummyDataReservationAdditionalService0.getTblHotelEvent()));
                                }
                                if (dummyDataReservationAdditionalService0.getTblBankEventCard() != null) {
                                    dummyDataReservationAdditionalService0.setTblBankEventCard(new TblBankEventCard(dummyDataReservationAdditionalService0.getTblBankEventCard()));
                                }
                                dummyDataReservationAdditionalServices0.add(dummyDataReservationAdditionalService0);
                            }
                        }
                        dummyDataRoomTypeDetails0.add(dummyDataRoomTypeDetail0);
                    }
                    if (fReservationManager.insertDataReservation(dummySelectedData,
                            dummyDataReservationBill0,
                            dummyDataCheckOutBill0,
                            dummyDataRoomTypeDetails0,
                            dummyDataRoomTypeDetailRoomPriceDetails0,
                            dummyDataRoomTypeDetailTravelAgentDiscountDetails0,
                            dummyDataReservationAdditionalItems0,
                            dummyDataReservationAdditionalServices0
                    ) != null) {
                        //reload data (with data saved)
                        ClassMessage.showSucceedInsertingDataMessage("", null);
                        dataInputStatus = 1;
                        loadDataReservation(dummySelectedData.getIdreservation());
                        setSelectedDataToInputForm();
//                        //refresh data from table & close form data reservation
//                        tableDataReservation.getTableView().setItems(loadAllDataReservation());
//                        dataReservationFormShowStatus.set(0.0);
//                        //print data reservation bill
//                        dataReservationBillPrintHandle(dataReservationBill); //Reservation = '0'
                        //set unsaving data input -> 'false'
                        ClassSession.unSavingDataInput.set(false);
                        //set status save
                        statusSave = true;
                    } else {
                        ClassMessage.showFailedInsertingDataMessage(fReservationManager.getErrorMessage(), null);
                    }
                } else {
                    ClassMessage.showWarningInputDataMessage(errDataInput, null);
                }
                break;
            case 1:
                boolean updated = false;
                switch (updateType) {
                    case 1: //(update selected data)
                        //dummy entry..
                        updated = fReservationManager.updateDataReservation(dummySelectedData);
                        break;
                    case 2: //(update : insert whole data)
                        List<TblReservationRoomTypeDetail> dummyDataRoomTypeDetails1 = new ArrayList<>();
                        for (TblReservationRoomTypeDetail dataRoomTypeDetail : dataReservationRoomTypeDetails) {
                            TblReservationRoomTypeDetail dummyDataRoomTypeDetail1 = new TblReservationRoomTypeDetail(dataRoomTypeDetail);
                            dummyDataRoomTypeDetail1.setTblReservation(dummySelectedData);
                            dummyDataRoomTypeDetail1.setTblRoomType(new TblRoomType(dummyDataRoomTypeDetail1.getTblRoomType()));
                            if (dummyDataRoomTypeDetail1.getTblReservationCheckInOut() != null) {
                                dummyDataRoomTypeDetail1.setTblReservationCheckInOut(new TblReservationCheckInOut(dummyDataRoomTypeDetail1.getTblReservationCheckInOut()));
                                if (dummyDataRoomTypeDetail1.getTblReservationCheckInOut().getTblRoom() != null) {
                                    dummyDataRoomTypeDetail1.getTblReservationCheckInOut().setTblRoom(new TblRoom(dummyDataRoomTypeDetail1.getTblReservationCheckInOut().getTblRoom()));
                                }
                                if (dummyDataRoomTypeDetail1.getTblReservationCheckInOut().getRefReservationCheckInOutStatus() != null) {
                                    dummyDataRoomTypeDetail1.getTblReservationCheckInOut().setRefReservationCheckInOutStatus(new RefReservationCheckInOutStatus(dummyDataRoomTypeDetail1.getTblReservationCheckInOut().getRefReservationCheckInOutStatus()));
                                }
                            }
                            dummyDataRoomTypeDetails1.add(dummyDataRoomTypeDetail1);
                        }
                        if (dataInputTransactionStatus.equals("reservation")) {   //reservation
                            //dummy entry
                            TblReservationBill dummyDataReservationBill2 = new TblReservationBill(dataReservationBill);
                            dummyDataReservationBill2.setTblReservation(dummySelectedData);
                            dummyDataReservationBill2.setRefReservationBillType(new RefReservationBillType(dummyDataReservationBill2.getRefReservationBillType()));
                            if (dummyDataReservationBill2.getTblBankCard() != null) {
                                dummyDataReservationBill2.setTblBankCard(new TblBankCard(dummyDataReservationBill2.getTblBankCard()));
                            }
                            if (dummyDataReservationBill2.getRefReservationBillDiscountType() != null) {
                                dummyDataReservationBill2.setRefReservationBillDiscountType(new RefReservationBillDiscountType(dummyDataReservationBill2.getRefReservationBillDiscountType()));
                            }
                            List<TblReservationPayment> dummyDataReservationPayments2 = new ArrayList<>();
                            List<TblReservationPaymentWithTransfer> dummyDataReservationPaymentWithTransfers2R = new ArrayList<>();
                            List<TblReservationPaymentWithBankCard> dummyDataReservationPaymentWithBankCards2R = new ArrayList<>();
                            List<TblReservationPaymentWithCekGiro> dummyDataReservationPaymentWithCekGiros2R = new ArrayList<>();
                            List<TblReservationPaymentWithGuaranteePayment> dummyDataReservationPaymentWithGuaranteePayments2R = new ArrayList<>();
                            List<TblGuaranteeLetterItemDetail> dummyDataGuaranteeLetterItemDetails2R = new ArrayList<>();
                            List<TblReservationPaymentWithReservationVoucher> dummyDataReservationPaymentWithReservationVouchers2R = new ArrayList<>();
                            for (TblReservationPayment dataReservationPayment2 : dataReservationPayments) {
                                TblReservationPayment dummyDataReservationPayment2 = new TblReservationPayment(dataReservationPayment2);
                                dummyDataReservationPayment2.setTblReservationBill(dummyDataReservationBill2);
                                dummyDataReservationPayment2.setTblEmployeeByIdcashier(new TblEmployee(dummyDataReservationPayment2.getTblEmployeeByIdcashier()));
                                dummyDataReservationPayment2.setRefReservationBillType(new RefReservationBillType(dummyDataReservationPayment2.getRefReservationBillType()));
                                dummyDataReservationPayment2.setRefFinanceTransactionPaymentType(new RefFinanceTransactionPaymentType(dummyDataReservationPayment2.getRefFinanceTransactionPaymentType()));
                                for (TblReservationPaymentWithTransfer dataReservationPaymentWithTransfer2R : dataReservationPaymentWithTransfers) {
                                    if (dataReservationPaymentWithTransfer2R.getTblReservationPayment().equals(dataReservationPayment2)) {
                                        TblReservationPaymentWithTransfer dummyDataReservationPaymentWithTransfer2R = new TblReservationPaymentWithTransfer(dataReservationPaymentWithTransfer2R);
                                        dummyDataReservationPaymentWithTransfer2R.setTblReservationPayment(dummyDataReservationPayment2);
                                        dummyDataReservationPaymentWithTransfer2R.setTblBankAccountBySenderBankAccount(new TblBankAccount(dummyDataReservationPaymentWithTransfer2R.getTblBankAccountBySenderBankAccount()));
                                        dummyDataReservationPaymentWithTransfer2R.setTblBankAccountByReceiverBankAccount(new TblBankAccount(dummyDataReservationPaymentWithTransfer2R.getTblBankAccountByReceiverBankAccount()));
                                        dummyDataReservationPaymentWithTransfers2R.add(dummyDataReservationPaymentWithTransfer2R);
                                    }
                                }
                                for (TblReservationPaymentWithBankCard dataReservationPaymentWithBankCard2R : dataReservationPaymentWithBankCards) {
                                    if (dataReservationPaymentWithBankCard2R.getTblReservationPayment().equals(dataReservationPayment2)) {
                                        TblReservationPaymentWithBankCard dummyDataReservationPaymentWithBankCard2R = new TblReservationPaymentWithBankCard(dataReservationPaymentWithBankCard2R);
                                        dummyDataReservationPaymentWithBankCard2R.setTblReservationPayment(dummyDataReservationPayment2);
                                        dummyDataReservationPaymentWithBankCard2R.setTblBank(new TblBank(dummyDataReservationPaymentWithBankCard2R.getTblBank()));
                                        dummyDataReservationPaymentWithBankCard2R.setTblBankEdc(new TblBankEdc(dummyDataReservationPaymentWithBankCard2R.getTblBankEdc()));
                                        dummyDataReservationPaymentWithBankCard2R.setTblBankNetworkCard(new TblBankNetworkCard(dummyDataReservationPaymentWithBankCard2R.getTblBankNetworkCard()));
                                        if (dummyDataReservationPaymentWithBankCard2R.getTblBankEdcBankNetworkCard() != null) {
                                            dummyDataReservationPaymentWithBankCard2R.setTblBankEdcBankNetworkCard(new TblBankEdcBankNetworkCard(dummyDataReservationPaymentWithBankCard2R.getTblBankEdcBankNetworkCard()));
                                        }
                                        if (dummyDataReservationPaymentWithBankCard2R.getTblBankEventCard() != null) {
                                            dummyDataReservationPaymentWithBankCard2R.setTblBankEventCard(new TblBankEventCard(dummyDataReservationPaymentWithBankCard2R.getTblBankEventCard()));
                                        }
                                        dummyDataReservationPaymentWithBankCard2R.setTblBankAccount(new TblBankAccount(dummyDataReservationPaymentWithBankCard2R.getTblBankAccount()));
                                        dummyDataReservationPaymentWithBankCards2R.add(dummyDataReservationPaymentWithBankCard2R);
                                    }
                                }
                                for (TblReservationPaymentWithCekGiro dataReservationPaymentWithCekGiro2R : dataReservationPaymentWithCekGiros) {
                                    if (dataReservationPaymentWithCekGiro2R.getTblReservationPayment().equals(dataReservationPayment2)) {
                                        TblReservationPaymentWithCekGiro dummyDataReservationPaymentWithCekGiro2R = new TblReservationPaymentWithCekGiro(dataReservationPaymentWithCekGiro2R);
                                        dummyDataReservationPaymentWithCekGiro2R.setTblReservationPayment(dummyDataReservationPayment2);
                                        dummyDataReservationPaymentWithCekGiro2R.setTblBank(new TblBank(dummyDataReservationPaymentWithCekGiro2R.getTblBank()));
//                                        dummyDataReservationPaymentWithCekGiro2R.setTblBankAccountBySenderBankAccount(new TblBankAccount(dummyDataReservationPaymentWithCekGiro2R.getTblBankAccountBySenderBankAccount()));
                                        dummyDataReservationPaymentWithCekGiro2R.setTblBankAccountByReceiverBankAccount(new TblBankAccount(dummyDataReservationPaymentWithCekGiro2R.getTblBankAccountByReceiverBankAccount()));
                                        dummyDataReservationPaymentWithCekGiros2R.add(dummyDataReservationPaymentWithCekGiro2R);
                                    }
                                }
                                for (TblReservationPaymentWithGuaranteePayment dataReservationPaymentWithGuaranteePayment2R : dataReservationPaymentWithGuaranteePayments) {
                                    if (dataReservationPaymentWithGuaranteePayment2R.getTblReservationPayment().equals(dataReservationPayment2)) {
                                        TblReservationPaymentWithGuaranteePayment dummyDataReservationPaymentWithGuaranteePayment2R = new TblReservationPaymentWithGuaranteePayment(dataReservationPaymentWithGuaranteePayment2R);
                                        dummyDataReservationPaymentWithGuaranteePayment2R.setTblReservationPayment(dummyDataReservationPayment2);
                                        dummyDataReservationPaymentWithGuaranteePayment2R.setTblPartner(new TblPartner(dummyDataReservationPaymentWithGuaranteePayment2R.getTblPartner()));
                                        if (dummyDataReservationPaymentWithGuaranteePayment2R.getTblBankAccountBySenderBankAccount() != null) {
                                            dummyDataReservationPaymentWithGuaranteePayment2R.setTblBankAccountBySenderBankAccount(new TblBankAccount(dummyDataReservationPaymentWithGuaranteePayment2R.getTblBankAccountBySenderBankAccount()));
                                        }
                                        if (dummyDataReservationPaymentWithGuaranteePayment2R.getTblBankAccountByReceiverBankAccount() != null) {
                                            dummyDataReservationPaymentWithGuaranteePayment2R.setTblBankAccountByReceiverBankAccount(new TblBankAccount(dummyDataReservationPaymentWithGuaranteePayment2R.getTblBankAccountByReceiverBankAccount()));
                                        }
                                        dummyDataReservationPaymentWithGuaranteePayments2R.add(dummyDataReservationPaymentWithGuaranteePayment2R);
                                        //guarantee letter - item detail
                                        for (TblGuaranteeLetterItemDetail dataGuaranteeLetterItemDetail2R : dataGuaranteeLetterItemDetails) {
                                            if (dataGuaranteeLetterItemDetail2R.getTblReservationPaymentWithGuaranteePayment().equals(dataReservationPaymentWithGuaranteePayment2R)) {
                                                TblGuaranteeLetterItemDetail dummyDataGuaranteeLetterItemDetail2R = new TblGuaranteeLetterItemDetail(dataGuaranteeLetterItemDetail2R);
                                                dummyDataGuaranteeLetterItemDetail2R.setTblReservationPaymentWithGuaranteePayment(dummyDataReservationPaymentWithGuaranteePayment2R);
                                                dummyDataGuaranteeLetterItemDetails2R.add(dummyDataGuaranteeLetterItemDetail2R);
                                            }
                                        }
                                    }
                                }
                                for (TblReservationPaymentWithReservationVoucher dataReservationPaymentWithReservationVoucher2R : dataReservationPaymentWithReservationVouchers) {
                                    if (dataReservationPaymentWithReservationVoucher2R.getTblReservationPayment().equals(dataReservationPayment2)) {
                                        TblReservationPaymentWithReservationVoucher dummyDataReservationPaymentWithReservationVoucher2R = new TblReservationPaymentWithReservationVoucher(dataReservationPaymentWithReservationVoucher2R);
                                        dummyDataReservationPaymentWithReservationVoucher2R.setTblReservationPayment(dummyDataReservationPayment2);
                                        dummyDataReservationPaymentWithReservationVoucher2R.setTblReservationVoucher(new TblReservationVoucher(dummyDataReservationPaymentWithReservationVoucher2R.getTblReservationVoucher()));
                                        dummyDataReservationPaymentWithReservationVoucher2R.getTblReservationVoucher().setRefVoucherStatus(new RefVoucherStatus(dummyDataReservationPaymentWithReservationVoucher2R.getTblReservationVoucher().getRefVoucherStatus()));
                                        dummyDataReservationPaymentWithReservationVouchers2R.add(dummyDataReservationPaymentWithReservationVoucher2R);
                                    }
                                }
                                dummyDataReservationPayments2.add(dummyDataReservationPayment2);
                            }
                            updated = fReservationManager.updateDataReservationTransactionPayments(dummySelectedData, dummyDataRoomTypeDetails1,
                                    dummyDataReservationBill2, dummyDataReservationPayments2,
                                    dummyDataReservationPaymentWithTransfers2R, dummyDataReservationPaymentWithBankCards2R, dummyDataReservationPaymentWithCekGiros2R,
                                    dummyDataReservationPaymentWithGuaranteePayments2R, dummyDataReservationPaymentWithReservationVouchers2R,
                                    dummyDataGuaranteeLetterItemDetails2R);
                        } else {  //checkout
                            //dummy entry
                            TblReservationBill dummyDataCheckOutBill2 = new TblReservationBill(dataCheckOutBill);
                            dummyDataCheckOutBill2.setTblReservation(dummySelectedData);
                            dummyDataCheckOutBill2.setRefReservationBillType(new RefReservationBillType(dummyDataCheckOutBill2.getRefReservationBillType()));
                            if (dummyDataCheckOutBill2.getTblBankCard() != null) {
                                dummyDataCheckOutBill2.setTblBankCard(new TblBankCard(dummyDataCheckOutBill2.getTblBankCard()));
                            }
                            if (dummyDataCheckOutBill2.getRefReservationBillDiscountType() != null) {
                                dummyDataCheckOutBill2.setRefReservationBillDiscountType(new RefReservationBillDiscountType(dummyDataCheckOutBill2.getRefReservationBillDiscountType()));
                            }
                            List<TblReservationPayment> dummyDataCheckOutPayments2 = new ArrayList<>();
                            List<TblReservationPaymentWithTransfer> dummyDataReservationPaymentWithTransfers2R = new ArrayList<>();
                            List<TblReservationPaymentWithBankCard> dummyDataReservationPaymentWithBankCards2R = new ArrayList<>();
                            List<TblReservationPaymentWithCekGiro> dummyDataReservationPaymentWithCekGiros2R = new ArrayList<>();
                            List<TblReservationPaymentWithGuaranteePayment> dummyDataReservationPaymentWithGuaranteePayments2R = new ArrayList<>();
                            List<TblGuaranteeLetterItemDetail> dummyDataGuaranteeLetterItemDetails2R = new ArrayList<>();
                            List<TblReservationPaymentWithReservationVoucher> dummyDataReservationPaymentWithReservationVouchers2R = new ArrayList<>();
                            for (TblReservationPayment dataCheckOutPayment2 : dataCheckOutPayments) {
                                TblReservationPayment dummyDataCheckOutPayment2 = new TblReservationPayment(dataCheckOutPayment2);
                                dummyDataCheckOutPayment2.setTblReservationBill(dummyDataCheckOutBill2);
                                dummyDataCheckOutPayment2.setTblEmployeeByIdcashier(new TblEmployee(dummyDataCheckOutPayment2.getTblEmployeeByIdcashier()));
                                dummyDataCheckOutPayment2.setRefReservationBillType(new RefReservationBillType(dummyDataCheckOutPayment2.getRefReservationBillType()));
                                dummyDataCheckOutPayment2.setRefFinanceTransactionPaymentType(new RefFinanceTransactionPaymentType(dummyDataCheckOutPayment2.getRefFinanceTransactionPaymentType()));
                                for (TblReservationPaymentWithTransfer dataReservationPaymentWithTransfer2R : dataReservationPaymentWithTransfers) {
                                    if (dataReservationPaymentWithTransfer2R.getTblReservationPayment().equals(dataCheckOutPayment2)) {
                                        TblReservationPaymentWithTransfer dummyDataReservationPaymentWithTransfer2R = new TblReservationPaymentWithTransfer(dataReservationPaymentWithTransfer2R);
                                        dummyDataReservationPaymentWithTransfer2R.setTblReservationPayment(dummyDataCheckOutPayment2);
                                        dummyDataReservationPaymentWithTransfer2R.setTblBankAccountBySenderBankAccount(new TblBankAccount(dummyDataReservationPaymentWithTransfer2R.getTblBankAccountBySenderBankAccount()));
                                        dummyDataReservationPaymentWithTransfer2R.setTblBankAccountByReceiverBankAccount(new TblBankAccount(dummyDataReservationPaymentWithTransfer2R.getTblBankAccountByReceiverBankAccount()));
                                        dummyDataReservationPaymentWithTransfers2R.add(dummyDataReservationPaymentWithTransfer2R);
                                    }
                                }
                                for (TblReservationPaymentWithBankCard dataReservationPaymentWithBankCard2R : dataReservationPaymentWithBankCards) {
                                    if (dataReservationPaymentWithBankCard2R.getTblReservationPayment().equals(dataCheckOutPayment2)) {
                                        TblReservationPaymentWithBankCard dummyDataReservationPaymentWithBankCard2R = new TblReservationPaymentWithBankCard(dataReservationPaymentWithBankCard2R);
                                        dummyDataReservationPaymentWithBankCard2R.setTblReservationPayment(dummyDataCheckOutPayment2);
                                        dummyDataReservationPaymentWithBankCard2R.setTblBank(new TblBank(dummyDataReservationPaymentWithBankCard2R.getTblBank()));
                                        dummyDataReservationPaymentWithBankCard2R.setTblBankEdc(new TblBankEdc(dummyDataReservationPaymentWithBankCard2R.getTblBankEdc()));
                                        dummyDataReservationPaymentWithBankCard2R.setTblBankNetworkCard(new TblBankNetworkCard(dummyDataReservationPaymentWithBankCard2R.getTblBankNetworkCard()));
                                        if (dummyDataReservationPaymentWithBankCard2R.getTblBankEdcBankNetworkCard() != null) {
                                            dummyDataReservationPaymentWithBankCard2R.setTblBankEdcBankNetworkCard(new TblBankEdcBankNetworkCard(dummyDataReservationPaymentWithBankCard2R.getTblBankEdcBankNetworkCard()));
                                        }
                                        if (dummyDataReservationPaymentWithBankCard2R.getTblBankEventCard() != null) {
                                            dummyDataReservationPaymentWithBankCard2R.setTblBankEventCard(new TblBankEventCard(dummyDataReservationPaymentWithBankCard2R.getTblBankEventCard()));
                                        }
                                        dummyDataReservationPaymentWithBankCard2R.setTblBankAccount(new TblBankAccount(dummyDataReservationPaymentWithBankCard2R.getTblBankAccount()));
                                        dummyDataReservationPaymentWithBankCards2R.add(dummyDataReservationPaymentWithBankCard2R);
                                    }
                                }
                                for (TblReservationPaymentWithCekGiro dataReservationPaymentWithCekGiro2R : dataReservationPaymentWithCekGiros) {
                                    if (dataReservationPaymentWithCekGiro2R.getTblReservationPayment().equals(dataCheckOutPayment2)) {
                                        TblReservationPaymentWithCekGiro dummyDataReservationPaymentWithCekGiro2R = new TblReservationPaymentWithCekGiro(dataReservationPaymentWithCekGiro2R);
                                        dummyDataReservationPaymentWithCekGiro2R.setTblReservationPayment(dummyDataCheckOutPayment2);
                                        dummyDataReservationPaymentWithCekGiro2R.setTblBank(new TblBank(dummyDataReservationPaymentWithCekGiro2R.getTblBank()));
//                                        dummyDataReservationPaymentWithCekGiro2R.setTblBankAccountBySenderBankAccount(new TblBankAccount(dummyDataReservationPaymentWithCekGiro2R.getTblBankAccountBySenderBankAccount()));
                                        dummyDataReservationPaymentWithCekGiro2R.setTblBankAccountByReceiverBankAccount(new TblBankAccount(dummyDataReservationPaymentWithCekGiro2R.getTblBankAccountByReceiverBankAccount()));
                                        dummyDataReservationPaymentWithCekGiros2R.add(dummyDataReservationPaymentWithCekGiro2R);
                                    }
                                }
                                for (TblReservationPaymentWithGuaranteePayment dataReservationPaymentWithGuaranteePayment2R : dataReservationPaymentWithGuaranteePayments) {
                                    if (dataReservationPaymentWithGuaranteePayment2R.getTblReservationPayment().equals(dataCheckOutPayment2)) {
                                        TblReservationPaymentWithGuaranteePayment dummyDataReservationPaymentWithGuaranteePayment2R = new TblReservationPaymentWithGuaranteePayment(dataReservationPaymentWithGuaranteePayment2R);
                                        dummyDataReservationPaymentWithGuaranteePayment2R.setTblReservationPayment(dummyDataCheckOutPayment2);
                                        dummyDataReservationPaymentWithGuaranteePayment2R.setTblPartner(new TblPartner(dummyDataReservationPaymentWithGuaranteePayment2R.getTblPartner()));
                                        if (dummyDataReservationPaymentWithGuaranteePayment2R.getTblBankAccountBySenderBankAccount() != null) {
                                            dummyDataReservationPaymentWithGuaranteePayment2R.setTblBankAccountBySenderBankAccount(new TblBankAccount(dummyDataReservationPaymentWithGuaranteePayment2R.getTblBankAccountBySenderBankAccount()));
                                        }
                                        if (dummyDataReservationPaymentWithGuaranteePayment2R.getTblBankAccountByReceiverBankAccount() != null) {
                                            dummyDataReservationPaymentWithGuaranteePayment2R.setTblBankAccountByReceiverBankAccount(new TblBankAccount(dummyDataReservationPaymentWithGuaranteePayment2R.getTblBankAccountByReceiverBankAccount()));
                                        }
                                        dummyDataReservationPaymentWithGuaranteePayments2R.add(dummyDataReservationPaymentWithGuaranteePayment2R);
                                        for (TblGuaranteeLetterItemDetail dataGuaranteeLetterItemDetail2R : dataGuaranteeLetterItemDetails) {
                                            if (dataGuaranteeLetterItemDetail2R.getTblReservationPaymentWithGuaranteePayment().equals(dataReservationPaymentWithGuaranteePayment2R)) {
                                                TblGuaranteeLetterItemDetail dummyDataGuaranteeLetterItemDetail2R = new TblGuaranteeLetterItemDetail(dataGuaranteeLetterItemDetail2R);
                                                dummyDataGuaranteeLetterItemDetail2R.setTblReservationPaymentWithGuaranteePayment(dummyDataReservationPaymentWithGuaranteePayment2R);
                                                dummyDataGuaranteeLetterItemDetails2R.add(dummyDataGuaranteeLetterItemDetail2R);
                                            }
                                        }
                                    }
                                }
                                for (TblReservationPaymentWithReservationVoucher dataReservationPaymentWithReservationVoucher2R : dataReservationPaymentWithReservationVouchers) {
                                    if (dataReservationPaymentWithReservationVoucher2R.getTblReservationPayment().equals(dataCheckOutPayment2)) {
                                        TblReservationPaymentWithReservationVoucher dummyDataReservationPaymentWithReservationVoucher2R = new TblReservationPaymentWithReservationVoucher(dataReservationPaymentWithReservationVoucher2R);
                                        dummyDataReservationPaymentWithReservationVoucher2R.setTblReservationPayment(dummyDataCheckOutPayment2);
                                        dummyDataReservationPaymentWithReservationVoucher2R.setTblReservationVoucher(new TblReservationVoucher(dummyDataReservationPaymentWithReservationVoucher2R.getTblReservationVoucher()));
                                        dummyDataReservationPaymentWithReservationVoucher2R.getTblReservationVoucher().setRefVoucherStatus(new RefVoucherStatus(dummyDataReservationPaymentWithReservationVoucher2R.getTblReservationVoucher().getRefVoucherStatus()));
                                        dummyDataReservationPaymentWithReservationVouchers2R.add(dummyDataReservationPaymentWithReservationVoucher2R);
                                    }
                                }
                                dummyDataCheckOutPayments2.add(dummyDataCheckOutPayment2);
                            }
                            //~~~
                            updated = fReservationManager.updateDataReservationTransactionPayments(dummySelectedData, dummyDataRoomTypeDetails1,
                                    dummyDataCheckOutBill2, dummyDataCheckOutPayments2,
                                    dummyDataReservationPaymentWithTransfers2R, dummyDataReservationPaymentWithBankCards2R, dummyDataReservationPaymentWithCekGiros2R,
                                    dummyDataReservationPaymentWithGuaranteePayments2R, dummyDataReservationPaymentWithReservationVouchers2R,
                                    dummyDataGuaranteeLetterItemDetails2R);
                        }
                        break;
                    case 3: //(update : insert whole selected data)
                        //dummy entry
                        TblReservationBill dummyDataReservationBill3 = new TblReservationBill(dataReservationBill);
                        dummyDataReservationBill3.setTblReservation(dummySelectedData);
                        dummyDataReservationBill3.setRefReservationBillType(new RefReservationBillType(dummyDataReservationBill3.getRefReservationBillType()));
                        if (dummyDataReservationBill3.getTblBankCard() != null) {
                            dummyDataReservationBill3.setTblBankCard(new TblBankCard(dummyDataReservationBill3.getTblBankCard()));
                        }
                        if (dummyDataReservationBill3.getRefReservationBillDiscountType() != null) {
                            dummyDataReservationBill3.setRefReservationBillDiscountType(new RefReservationBillDiscountType(dummyDataReservationBill3.getRefReservationBillDiscountType()));
                        }
                        List<TblReservationRoomTypeDetail> dummySelectedDataRoomTypeDetails3 = new ArrayList<>();
                        List<TblReservationRoomTypeDetailRoomPriceDetail> dummySelectedDataRoomTypeDetailRoomPriceDetails3 = new ArrayList<>();
                        List<TblReservationRoomTypeDetailTravelAgentDiscountDetail> dummySelectedDataRoomTypeDetailTravelAgentDiscountDetails3 = new ArrayList<>();
                        List<TblReservationAdditionalItem> dummySelectedDataReservationAdditionalItems3 = new ArrayList<>();
                        List<TblReservationAdditionalService> dummySelectedDataReservationAdditionalServices3 = new ArrayList<>();
                        for (TblReservationRoomTypeDetail selectedDataRoomTypeDetail3 : selectedDataRoomTypeDetails) {
                            TblReservationRoomTypeDetail dummySelectedDataRoomTypeDetail3 = new TblReservationRoomTypeDetail(selectedDataRoomTypeDetail3);
                            dummySelectedDataRoomTypeDetail3.setTblReservation(dummySelectedData);
                            dummySelectedDataRoomTypeDetail3.setTblRoomType(new TblRoomType(dummySelectedDataRoomTypeDetail3.getTblRoomType()));
                            if (dummySelectedDataRoomTypeDetail3.getTblReservationCheckInOut() != null) {
                                dummySelectedDataRoomTypeDetail3.setTblReservationCheckInOut(new TblReservationCheckInOut(dummySelectedDataRoomTypeDetail3.getTblReservationCheckInOut()));
                                if (dummySelectedDataRoomTypeDetail3.getTblReservationCheckInOut().getTblRoom() != null) {
                                    dummySelectedDataRoomTypeDetail3.getTblReservationCheckInOut().setTblRoom(new TblRoom(dummySelectedDataRoomTypeDetail3.getTblReservationCheckInOut().getTblRoom()));
                                    dummySelectedDataRoomTypeDetail3.getTblReservationCheckInOut().getTblRoom().setRefRoomStatus(new RefRoomStatus(dummySelectedDataRoomTypeDetail3.getTblReservationCheckInOut().getTblRoom().getRefRoomStatus()));
                                }
                                dummySelectedDataRoomTypeDetail3.getTblReservationCheckInOut().setRefReservationCheckInOutStatus(new RefReservationCheckInOutStatus(dummySelectedDataRoomTypeDetail3.getTblReservationCheckInOut().getRefReservationCheckInOutStatus()));
                            }
                            for (TblReservationRoomTypeDetailRoomPriceDetail selectedDataRoomTypeDetailRoomPriceDetail3 : selectedDataRoomTypeDetailRoomPriceDetails) {
                                if (selectedDataRoomTypeDetailRoomPriceDetail3.getTblReservationRoomTypeDetail().equals(selectedDataRoomTypeDetail3)) {
                                    TblReservationRoomTypeDetailRoomPriceDetail dummySelectedDataRoomTypeDetailRoomPriceDetail3 = new TblReservationRoomTypeDetailRoomPriceDetail(selectedDataRoomTypeDetailRoomPriceDetail3);
                                    dummySelectedDataRoomTypeDetailRoomPriceDetail3.setTblReservationRoomTypeDetail(dummySelectedDataRoomTypeDetail3);
                                    dummySelectedDataRoomTypeDetailRoomPriceDetail3.setTblReservationRoomPriceDetail(new TblReservationRoomPriceDetail(dummySelectedDataRoomTypeDetailRoomPriceDetail3.getTblReservationRoomPriceDetail()));
                                    if (dummySelectedDataRoomTypeDetailRoomPriceDetail3.getTblReservationRoomPriceDetail().getTblHotelEvent() != null) {
                                        dummySelectedDataRoomTypeDetailRoomPriceDetail3.getTblReservationRoomPriceDetail().setTblHotelEvent(new TblHotelEvent(dummySelectedDataRoomTypeDetailRoomPriceDetail3.getTblReservationRoomPriceDetail().getTblHotelEvent()));
                                    }
                                    if (dummySelectedDataRoomTypeDetailRoomPriceDetail3.getTblReservationRoomPriceDetail().getTblBankEventCard() != null) {
                                        dummySelectedDataRoomTypeDetailRoomPriceDetail3.getTblReservationRoomPriceDetail().setTblBankEventCard(new TblBankEventCard(dummySelectedDataRoomTypeDetailRoomPriceDetail3.getTblReservationRoomPriceDetail().getTblBankEventCard()));
                                    }
                                    dummySelectedDataRoomTypeDetailRoomPriceDetails3.add(dummySelectedDataRoomTypeDetailRoomPriceDetail3);
                                }
                            }
                            for (TblReservationRoomTypeDetailTravelAgentDiscountDetail selectedDataRoomTypeDetailTravelAgentDiscountDetail3 : selectedDataRoomTypeDetailTravelAgentDiscountDetails) {
                                if (selectedDataRoomTypeDetailTravelAgentDiscountDetail3.getTblReservationRoomTypeDetail().equals(selectedDataRoomTypeDetail3)) {
                                    TblReservationRoomTypeDetailTravelAgentDiscountDetail dummySelectedDataRoomTypeDetailTravelAgentDiscountDetail3 = new TblReservationRoomTypeDetailTravelAgentDiscountDetail(selectedDataRoomTypeDetailTravelAgentDiscountDetail3);
                                    dummySelectedDataRoomTypeDetailTravelAgentDiscountDetail3.setTblReservationRoomTypeDetail(dummySelectedDataRoomTypeDetail3);
                                    dummySelectedDataRoomTypeDetailTravelAgentDiscountDetail3.setTblReservationTravelAgentDiscountDetail(new TblReservationTravelAgentDiscountDetail(dummySelectedDataRoomTypeDetailTravelAgentDiscountDetail3.getTblReservationTravelAgentDiscountDetail()));
                                    dummySelectedDataRoomTypeDetailTravelAgentDiscountDetail3.getTblReservationTravelAgentDiscountDetail().setTblPartner(new TblPartner(dummySelectedDataRoomTypeDetailTravelAgentDiscountDetail3.getTblReservationTravelAgentDiscountDetail().getTblPartner()));
                                    dummySelectedDataRoomTypeDetailTravelAgentDiscountDetails3.add(dummySelectedDataRoomTypeDetailTravelAgentDiscountDetail3);
                                }
                            }
                            for (TblReservationAdditionalItem dataSelectedReservationAdditionalItem3 : selectedDataAdditionalItems) {
                                if (dataSelectedReservationAdditionalItem3.getTblReservationRoomTypeDetail().equals(selectedDataRoomTypeDetail3)) {
                                    TblReservationAdditionalItem dummySelectedDataReservationAdditionalItem3 = new TblReservationAdditionalItem(dataSelectedReservationAdditionalItem3);
                                    dummySelectedDataReservationAdditionalItem3.setTblReservationRoomTypeDetail(dummySelectedDataRoomTypeDetail3);
                                    dummySelectedDataReservationAdditionalItem3.setTblItem(new TblItem(dummySelectedDataReservationAdditionalItem3.getTblItem()));
                                    dummySelectedDataReservationAdditionalItem3.setRefReservationBillType(new RefReservationBillType(dummySelectedDataReservationAdditionalItem3.getRefReservationBillType()));
                                    if (dummySelectedDataReservationAdditionalItem3.getTblHotelEvent() != null) {
                                        dummySelectedDataReservationAdditionalItem3.setTblHotelEvent(new TblHotelEvent(dummySelectedDataReservationAdditionalItem3.getTblHotelEvent()));
                                    }
                                    if (dummySelectedDataReservationAdditionalItem3.getTblBankEventCard() != null) {
                                        dummySelectedDataReservationAdditionalItem3.setTblBankEventCard(new TblBankEventCard(dummySelectedDataReservationAdditionalItem3.getTblBankEventCard()));
                                    }
                                    dummySelectedDataReservationAdditionalItems3.add(dummySelectedDataReservationAdditionalItem3);
                                }
                            }
                            for (TblReservationAdditionalService dataSelectedReservationAdditionalService3 : selectedDataAdditionalServices) {
                                if (dataSelectedReservationAdditionalService3.getTblReservationRoomTypeDetail().equals(selectedDataRoomTypeDetail3)) {
                                    TblReservationAdditionalService dummySelectedDataReservationAdditionalService3 = new TblReservationAdditionalService(dataSelectedReservationAdditionalService3);
                                    dummySelectedDataReservationAdditionalService3.setTblReservationRoomTypeDetail(dummySelectedDataRoomTypeDetail3);
                                    dummySelectedDataReservationAdditionalService3.setTblRoomService(new TblRoomService(dummySelectedDataReservationAdditionalService3.getTblRoomService()));
                                    dummySelectedDataReservationAdditionalService3.setRefReservationBillType(new RefReservationBillType(dummySelectedDataReservationAdditionalService3.getRefReservationBillType()));
                                    if (dummySelectedDataReservationAdditionalService3.getTblHotelEvent() != null) {
                                        dummySelectedDataReservationAdditionalService3.setTblHotelEvent(new TblHotelEvent(dummySelectedDataReservationAdditionalService3.getTblHotelEvent()));
                                    }
                                    if (dummySelectedDataReservationAdditionalService3.getTblBankEventCard() != null) {
                                        dummySelectedDataReservationAdditionalService3.setTblBankEventCard(new TblBankEventCard(dummySelectedDataReservationAdditionalService3.getTblBankEventCard()));
                                    }
                                    dummySelectedDataReservationAdditionalServices3.add(dummySelectedDataReservationAdditionalService3);
                                }
                            }
                            dummySelectedDataRoomTypeDetails3.add(dummySelectedDataRoomTypeDetail3);
                        }
                        updated = fReservationManager.insertDataReservationRoomTypeDetails(
                                dummySelectedData, dummyDataReservationBill3,
                                dummySelectedDataRoomTypeDetails3,
                                dummySelectedDataRoomTypeDetailRoomPriceDetails3,
                                dummySelectedDataRoomTypeDetailTravelAgentDiscountDetails3,
                                dummySelectedDataReservationAdditionalItems3,
                                dummySelectedDataReservationAdditionalServices3);
                        break;
                    case 4: //(update : delete selected data)
                        //dummy entry
                        TblReservationBill dummyDataReservationBill4 = new TblReservationBill(dataReservationBill);
                        dummyDataReservationBill4.setTblReservation(dummySelectedData);
                        dummyDataReservationBill4.setRefReservationBillType(new RefReservationBillType(dummyDataReservationBill4.getRefReservationBillType()));
                        if (dummyDataReservationBill4.getTblBankCard() != null) {
                            dummyDataReservationBill4.setTblBankCard(new TblBankCard(dummyDataReservationBill4.getTblBankCard()));
                        }
                        if (dummyDataReservationBill4.getRefReservationBillDiscountType() != null) {
                            dummyDataReservationBill4.setRefReservationBillDiscountType(new RefReservationBillDiscountType(dummyDataReservationBill4.getRefReservationBillDiscountType()));
                        }
                        TblReservationRoomTypeDetail dummySelectedDataRoomTypeDetail4 = new TblReservationRoomTypeDetail(selectedDataRoomTypeDetail);
                        dummySelectedDataRoomTypeDetail4.setTblReservation(dummySelectedData);
                        dummySelectedDataRoomTypeDetail4.setTblRoomType(new TblRoomType(dummySelectedDataRoomTypeDetail4.getTblRoomType()));
                        if (dummySelectedDataRoomTypeDetail4.getTblReservationCheckInOut() != null) {
                            dummySelectedDataRoomTypeDetail4.setTblReservationCheckInOut(new TblReservationCheckInOut(dummySelectedDataRoomTypeDetail4.getTblReservationCheckInOut()));
                            if (dummySelectedDataRoomTypeDetail4.getTblReservationCheckInOut().getTblRoom() != null) {
                                dummySelectedDataRoomTypeDetail4.getTblReservationCheckInOut().setTblRoom(new TblRoom(dummySelectedDataRoomTypeDetail4.getTblReservationCheckInOut().getTblRoom()));
                            }
                            if (dummySelectedDataRoomTypeDetail4.getTblReservationCheckInOut().getRefReservationCheckInOutStatus() != null) {
                                dummySelectedDataRoomTypeDetail4.getTblReservationCheckInOut().setRefReservationCheckInOutStatus(new RefReservationCheckInOutStatus(dummySelectedDataRoomTypeDetail4.getTblReservationCheckInOut().getRefReservationCheckInOutStatus()));
                            }
                        }
                        //delete..
                        dummySelectedDataRoomTypeDetail4.setRefRecordStatus(new RefRecordStatus(dummySelectedDataRoomTypeDetail4.getRefRecordStatus()));
                        updated = fReservationManager.deleteDataReservationRoomTypeDetail(dummySelectedData,
                                dummyDataReservationBill4,
                                dummySelectedDataRoomTypeDetail4);
                        break;
                    case 5: //(update : update selected data)
                        //dummy entry
                        TblReservationRoomTypeDetail dummySelectedDataReservationRoomTypeDetail5 = new TblReservationRoomTypeDetail(selectedDataRoomTypeDetail);
                        dummySelectedDataReservationRoomTypeDetail5.setTblReservation(dummySelectedData);
                        dummySelectedDataReservationRoomTypeDetail5.setTblRoomType(new TblRoomType(dummySelectedDataReservationRoomTypeDetail5.getTblRoomType()));
                        dummySelectedDataReservationRoomTypeDetail5.setTblReservationCheckInOut(new TblReservationCheckInOut(dummySelectedDataReservationRoomTypeDetail5.getTblReservationCheckInOut()));
                        dummySelectedDataReservationRoomTypeDetail5.getTblReservationCheckInOut().setTblRoom(new TblRoom(dummySelectedDataReservationRoomTypeDetail5.getTblReservationCheckInOut().getTblRoom()));
                        dummySelectedDataReservationRoomTypeDetail5.getTblReservationCheckInOut().getTblRoom().setRefRoomStatus(new RefRoomStatus(dummySelectedDataReservationRoomTypeDetail5.getTblReservationCheckInOut().getTblRoom().getRefRoomStatus()));
                        dummySelectedDataReservationRoomTypeDetail5.getTblReservationCheckInOut().setRefReservationCheckInOutStatus(new RefReservationCheckInOutStatus(dummySelectedDataReservationRoomTypeDetail5.getTblReservationCheckInOut().getRefReservationCheckInOutStatus()));
                        List<TblReservationRoomTypeDetailChildDetail> dummyReservationChildInfos5 = new ArrayList<>();
                        for (TblReservationRoomTypeDetailChildDetail reservationChildInfo5 : selectedChildDetails) {
                            TblReservationRoomTypeDetailChildDetail dummyReservationChildInfo5 = new TblReservationRoomTypeDetailChildDetail(reservationChildInfo5);
                            dummyReservationChildInfo5.setTblReservationRoomTypeDetail(dummySelectedDataReservationRoomTypeDetail5);
                            dummyReservationChildInfos5.add(dummyReservationChildInfo5);
                        }
                        updated = fReservationManager.updateDataReservationRoomTypeDetailCheckIn(dummySelectedData, dummySelectedDataReservationRoomTypeDetail5, dummyReservationChildInfos5);
                        break;
                    case 6: //(update : update selected data)
                        //dummy entry
                        TblReservationRoomTypeDetail dummySelectedDataReservationRoomTypeDetail6 = new TblReservationRoomTypeDetail(selectedDataRoomTypeDetail);
                        dummySelectedDataReservationRoomTypeDetail6.setTblReservation(dummySelectedData);
                        dummySelectedDataReservationRoomTypeDetail6.setTblRoomType(new TblRoomType(dummySelectedDataReservationRoomTypeDetail6.getTblRoomType()));
                        dummySelectedDataReservationRoomTypeDetail6.setTblReservationCheckInOut(new TblReservationCheckInOut(dummySelectedDataReservationRoomTypeDetail6.getTblReservationCheckInOut()));
                        dummySelectedDataReservationRoomTypeDetail6.getTblReservationCheckInOut().setTblRoom(new TblRoom(dummySelectedDataReservationRoomTypeDetail6.getTblReservationCheckInOut().getTblRoom()));
                        dummySelectedDataReservationRoomTypeDetail6.getTblReservationCheckInOut().getTblRoom().setRefRoomStatus(new RefRoomStatus(dummySelectedDataReservationRoomTypeDetail6.getTblReservationCheckInOut().getTblRoom().getRefRoomStatus()));
                        if (dummySelectedDataReservationRoomTypeDetail6.getTblReservationCheckInOut().getRefReservationCheckInOutStatus() != null) {
                            dummySelectedDataReservationRoomTypeDetail6.getTblReservationCheckInOut().setRefReservationCheckInOutStatus(new RefReservationCheckInOutStatus(dummySelectedDataReservationRoomTypeDetail6.getTblReservationCheckInOut().getRefReservationCheckInOutStatus()));
                        }
                        TblReservationBill dummyDataCheckOutBill6 = new TblReservationBill(dataCheckOutBill);
                        dummyDataCheckOutBill6.setTblReservation(dummySelectedData);
                        dummyDataCheckOutBill6.setRefReservationBillType(new RefReservationBillType(dummyDataCheckOutBill6.getRefReservationBillType()));
                        if (dummyDataCheckOutBill6.getTblBankCard() != null) {
                            dummyDataCheckOutBill6.setTblBankCard(new TblBankCard(dummyDataCheckOutBill6.getTblBankCard()));
                        }
                        if (dummyDataCheckOutBill6.getRefReservationBillDiscountType() != null) {
                            dummyDataCheckOutBill6.setRefReservationBillDiscountType(new RefReservationBillDiscountType(dummyDataCheckOutBill6.getRefReservationBillDiscountType()));
                        }
                        updated = fReservationManager.updateDataReservationRoomTypeDetailCheckOut(dummySelectedData,
                                dummySelectedDataReservationRoomTypeDetail6,
                                dummyDataCheckOutBill6,
                                calculationTotalBillAfterRounding("checkout"));
//                        TblItemLocation dummySelectedItemLocationForMissedCard6 = null;
//                        if (selectedItemLocationForMissedCard != null) {
//                            dummySelectedItemLocationForMissedCard6 = new TblItemLocation(selectedItemLocationForMissedCard);
//                            dummySelectedItemLocationForMissedCard6.setTblItem(new TblItem(dummySelectedItemLocationForMissedCard6.getTblItem()));
//                            dummySelectedItemLocationForMissedCard6.setTblLocation(new TblLocation(dummySelectedItemLocationForMissedCard6.getTblLocation()));
//                        }
                        break;
                    case 7: //(update : insert whole data)
                        //dummy entry
                        List<TblReservationAdditionalItem> dummyReservationAdditionalItems7 = new ArrayList<>();
                        for (TblReservationAdditionalItem selectedAdditionalItem7 : selectedAdditionalItems) {
                            TblReservationAdditionalItem dummyReservationAdditionalItem7 = new TblReservationAdditionalItem(selectedAdditionalItem7);
                            dummyReservationAdditionalItem7.setTblReservationRoomTypeDetail(new TblReservationRoomTypeDetail(dummyReservationAdditionalItem7.getTblReservationRoomTypeDetail()));
                            dummyReservationAdditionalItem7.getTblReservationRoomTypeDetail().setTblReservation(dummySelectedData);
                            dummyReservationAdditionalItem7.setTblItem(new TblItem(dummyReservationAdditionalItem7.getTblItem()));
                            dummyReservationAdditionalItems7.add(dummyReservationAdditionalItem7);
                        }
                        updated = fReservationManager.insertDataReservationAddtionalItems(dummySelectedData, dummyReservationAdditionalItems7);
                        break;
                    case 8: //(update : delete selected data)
                        //dummy entry
                        TblReservationAdditionalItem dummySelectedAdditionalItem8 = new TblReservationAdditionalItem(selectedAdditionalItem);
                        dummySelectedAdditionalItem8.setTblReservationRoomTypeDetail(new TblReservationRoomTypeDetail(dummySelectedAdditionalItem8.getTblReservationRoomTypeDetail()));
                        dummySelectedAdditionalItem8.getTblReservationRoomTypeDetail().setTblReservation(dummySelectedData);
                        dummySelectedAdditionalItem8.setTblItem(new TblItem(dummySelectedAdditionalItem8.getTblItem()));
                        //delete..
                        dummySelectedAdditionalItem8.setRefRecordStatus(new RefRecordStatus(dummySelectedAdditionalItem8.getRefRecordStatus()));
                        updated = fReservationManager.deleteDataReservationAddtionalItem(dummySelectedData, dummySelectedAdditionalItem8);
                        break;
                    case 9: //(udate : insert whole data)
                        //dummy entry
                        List<TblReservationAdditionalService> dummyReservationAdditionalServices9 = new ArrayList<>();
                        for (TblReservationAdditionalService selectedAdditionalService9 : selectedAdditionalServices) {
                            TblReservationAdditionalService dummyReservationAdditionalService9 = new TblReservationAdditionalService(selectedAdditionalService9);
                            dummyReservationAdditionalService9.setTblReservationRoomTypeDetail(new TblReservationRoomTypeDetail(dummyReservationAdditionalService9.getTblReservationRoomTypeDetail()));
                            dummyReservationAdditionalService9.getTblReservationRoomTypeDetail().setTblReservation(dummySelectedData);
                            dummyReservationAdditionalService9.setTblRoomService(new TblRoomService(dummyReservationAdditionalService9.getTblRoomService()));
                            dummyReservationAdditionalServices9.add(dummyReservationAdditionalService9);
                        }
                        updated = fReservationManager.insertDataReservationAddtionalServices(dummySelectedData, dummyReservationAdditionalServices9);
                        break;
                    case 10: //(udate : delete selcted data)
                        //dummy entry
                        TblReservationAdditionalService dummySelectedAdditionalService10 = new TblReservationAdditionalService(selectedAdditionalService);
                        dummySelectedAdditionalService10.setTblReservationRoomTypeDetail(new TblReservationRoomTypeDetail(dummySelectedAdditionalService10.getTblReservationRoomTypeDetail()));
                        dummySelectedAdditionalService10.getTblReservationRoomTypeDetail().setTblReservation(dummySelectedData);
                        dummySelectedAdditionalService10.setTblRoomService(new TblRoomService(dummySelectedAdditionalService10.getTblRoomService()));
                        //delete..
                        dummySelectedAdditionalService10.setRefRecordStatus(new RefRecordStatus(dummySelectedAdditionalService10.getRefRecordStatus()));
                        updated = fReservationManager.deleteDataReservationAddtionalService(dummySelectedData, dummySelectedAdditionalService10);
                        break;
                    case 11: //(update : update selected data)
                        //dummy entry
                        TblReservationRoomTypeDetail dummySelectedDataReservationRoomTypeDetail11 = new TblReservationRoomTypeDetail(selectedDataRoomTypeDetail);
                        dummySelectedDataReservationRoomTypeDetail11.setTblReservation(dummySelectedData);
                        dummySelectedDataReservationRoomTypeDetail11.setTblRoomType(new TblRoomType(dummySelectedDataReservationRoomTypeDetail11.getTblRoomType()));
                        dummySelectedDataReservationRoomTypeDetail11.setTblReservationCheckInOut(new TblReservationCheckInOut(dummySelectedDataReservationRoomTypeDetail11.getTblReservationCheckInOut()));
                        dummySelectedDataReservationRoomTypeDetail11.getTblReservationCheckInOut().setTblRoom(new TblRoom(dummySelectedDataReservationRoomTypeDetail11.getTblReservationCheckInOut().getTblRoom()));
                        dummySelectedDataReservationRoomTypeDetail11.getTblReservationCheckInOut().getTblRoom().setRefRoomStatus(new RefRoomStatus(dummySelectedDataReservationRoomTypeDetail11.getTblReservationCheckInOut().getTblRoom().getRefRoomStatus()));
                        dummySelectedDataReservationRoomTypeDetail11.getTblReservationCheckInOut().setRefReservationCheckInOutStatus(new RefReservationCheckInOutStatus(dummySelectedDataReservationRoomTypeDetail11.getTblReservationCheckInOut().getRefReservationCheckInOutStatus()));
                        List<TblReservationRoomTypeDetailChildDetail> dummyReservationChildInfos11 = new ArrayList<>();
                        for (TblReservationRoomTypeDetailChildDetail reservationChildInfo11 : selectedChildDetails) {
                            TblReservationRoomTypeDetailChildDetail dummyReservationChildInfo11 = new TblReservationRoomTypeDetailChildDetail(reservationChildInfo11);
                            dummyReservationChildInfo11.setTblReservationRoomTypeDetail(dummySelectedDataReservationRoomTypeDetail11);
                            dummyReservationChildInfos11.add(dummyReservationChildInfo11);
                        }
                        TblReservationAdditionalService dummySelectedAdditionalService11 = new TblReservationAdditionalService(selectedAdditionalService);
                        dummySelectedAdditionalService11.setTblReservationRoomTypeDetail(dummySelectedDataReservationRoomTypeDetail11);
                        dummySelectedAdditionalService11.setTblRoomService(new TblRoomService(dummySelectedAdditionalService11.getTblRoomService()));
                        updated = fReservationManager.updateDataReservationRoomTypeDetailEarlyCheckIn(dummySelectedData, dummySelectedDataReservationRoomTypeDetail11, dummyReservationChildInfos11, dummySelectedAdditionalService11);
                        break;
                    case 12: //(update : update selected data)
                        //dummy entry
                        TblReservationRoomTypeDetail dummySelectedDataReservationRoomTypeDetail12 = new TblReservationRoomTypeDetail(selectedDataRoomTypeDetail);
                        dummySelectedDataReservationRoomTypeDetail12.setTblReservation(dummySelectedData);
                        dummySelectedDataReservationRoomTypeDetail12.setTblRoomType(new TblRoomType(dummySelectedDataReservationRoomTypeDetail12.getTblRoomType()));
                        dummySelectedDataReservationRoomTypeDetail12.setTblReservationCheckInOut(new TblReservationCheckInOut(dummySelectedDataReservationRoomTypeDetail12.getTblReservationCheckInOut()));
                        dummySelectedDataReservationRoomTypeDetail12.getTblReservationCheckInOut().setTblRoom(new TblRoom(dummySelectedDataReservationRoomTypeDetail12.getTblReservationCheckInOut().getTblRoom()));
                        dummySelectedDataReservationRoomTypeDetail12.getTblReservationCheckInOut().getTblRoom().setRefRoomStatus(new RefRoomStatus(dummySelectedDataReservationRoomTypeDetail12.getTblReservationCheckInOut().getTblRoom().getRefRoomStatus()));
                        dummySelectedDataReservationRoomTypeDetail12.getTblReservationCheckInOut().setRefReservationCheckInOutStatus(new RefReservationCheckInOutStatus(dummySelectedDataReservationRoomTypeDetail12.getTblReservationCheckInOut().getRefReservationCheckInOutStatus()));
                        if (dummySelectedDataReservationRoomTypeDetail12.getTblReservationCheckInOut().getRefReservationCheckInOutStatus() != null) {
                            dummySelectedDataReservationRoomTypeDetail12.getTblReservationCheckInOut().setRefReservationCheckInOutStatus(new RefReservationCheckInOutStatus(dummySelectedDataReservationRoomTypeDetail12.getTblReservationCheckInOut().getRefReservationCheckInOutStatus()));
                        }
//                        TblItemLocation dummySelectedItemLocationForMissedCard12 = null;
//                        if (selectedItemLocationForMissedCard != null) {
//                            dummySelectedItemLocationForMissedCard12 = new TblItemLocation(selectedItemLocationForMissedCard);
//                            dummySelectedItemLocationForMissedCard12.setTblItem(new TblItem(dummySelectedItemLocationForMissedCard12.getTblItem()));
//                            dummySelectedItemLocationForMissedCard12.setTblLocation(new TblLocation(dummySelectedItemLocationForMissedCard12.getTblLocation()));
//                        }
                        TblReservationAdditionalService dummySelectedAdditionalService12 = new TblReservationAdditionalService(selectedAdditionalService);
                        dummySelectedAdditionalService12.setTblReservationRoomTypeDetail(dummySelectedDataReservationRoomTypeDetail12);
                        dummySelectedAdditionalService12.setTblRoomService(new TblRoomService(dummySelectedAdditionalService12.getTblRoomService()));
                        TblReservationBill dummyDataCheckOutBill12 = new TblReservationBill(dataCheckOutBill);
                        dummyDataCheckOutBill12.setTblReservation(dummySelectedData);
                        dummyDataCheckOutBill12.setRefReservationBillType(new RefReservationBillType(dummyDataCheckOutBill12.getRefReservationBillType()));
                        if (dummyDataCheckOutBill12.getTblBankCard() != null) {
                            dummyDataCheckOutBill12.setTblBankCard(new TblBankCard(dummyDataCheckOutBill12.getTblBankCard()));
                        }
                        if (dummyDataCheckOutBill12.getRefReservationBillDiscountType() != null) {
                            dummyDataCheckOutBill12.setRefReservationBillDiscountType(new RefReservationBillDiscountType(dummyDataCheckOutBill12.getRefReservationBillDiscountType()));
                        }
                        updated = fReservationManager.updateDataReservationRoomTypeDetailLateCheckOut(dummySelectedData,
                                dummySelectedDataReservationRoomTypeDetail12,
                                dummySelectedAdditionalService12,
                                dummyDataCheckOutBill12,
                                calculationTotalBillAfterRounding("checkout"));
                        break;
                    case 13: //(update : update selected data)
                        //dummy entry
                        TblReservationRoomTypeDetail dummySelectedDataReservationRoomTypeDetail13 = new TblReservationRoomTypeDetail(selectedDataRoomTypeDetail);
                        dummySelectedDataReservationRoomTypeDetail13.setTblReservation(dummySelectedData);
                        dummySelectedDataReservationRoomTypeDetail13.setTblRoomType(new TblRoomType(dummySelectedDataReservationRoomTypeDetail13.getTblRoomType()));
                        dummySelectedDataReservationRoomTypeDetail13.setTblReservationCheckInOut(new TblReservationCheckInOut(dummySelectedDataReservationRoomTypeDetail13.getTblReservationCheckInOut()));
                        updated = fReservationManager.updateDataReservationRoomTypeDetailCheckInAddCardUsedNumber(dummySelectedData, dummySelectedDataReservationRoomTypeDetail13, additionalCardUsedNumber.get());
                        break;
                    case 14: //(update : update whole data)
                        //dummy entry
                        TblReservationBill dummyDataReservationBill14 = new TblReservationBill(dataReservationBill);
                        dummyDataReservationBill14.setTblReservation(dummySelectedData);
                        dummyDataReservationBill14.setRefReservationBillType(new RefReservationBillType(dummyDataReservationBill14.getRefReservationBillType()));
                        if (dummyDataReservationBill14.getTblBankCard() != null) {
                            dummyDataReservationBill14.setTblBankCard(new TblBankCard(dummyDataReservationBill14.getTblBankCard()));
                        }
                        if (dummyDataReservationBill14.getRefReservationBillDiscountType() != null) {
                            dummyDataReservationBill14.setRefReservationBillDiscountType(new RefReservationBillDiscountType(dummyDataReservationBill14.getRefReservationBillDiscountType()));
                        }
                        TblReservationBill dummyDataCheckOutBill14 = new TblReservationBill(dataCheckOutBill);
                        dummyDataCheckOutBill14.setTblReservation(dummySelectedData);
                        dummyDataCheckOutBill14.setRefReservationBillType(new RefReservationBillType(dummyDataCheckOutBill14.getRefReservationBillType()));
                        if (dummyDataCheckOutBill14.getTblBankCard() != null) {
                            dummyDataCheckOutBill14.setTblBankCard(new TblBankCard(dummyDataCheckOutBill14.getTblBankCard()));
                        }
                        if (dummyDataCheckOutBill14.getRefReservationBillDiscountType() != null) {
                            dummyDataCheckOutBill14.setRefReservationBillDiscountType(new RefReservationBillDiscountType(dummyDataCheckOutBill14.getRefReservationBillDiscountType()));
                        }
                        List<TblReservationRoomTypeDetail> dummyDataRoomTypeDetails14 = new ArrayList<>();
                        List<TblReservationRoomTypeDetailRoomPriceDetail> dummyDataRoomTypeDetailRoomPriceDetails14 = new ArrayList<>();
                        List<TblReservationRoomTypeDetailTravelAgentDiscountDetail> dummyDataRoomTypeDetailTravelAgentDiscountDetails14 = new ArrayList<>();
                        List<TblReservationAdditionalItem> dummyDataReservationAdditionalItems14 = new ArrayList<>();
                        List<TblReservationAdditionalService> dummyDataReservationAdditionalServices14 = new ArrayList<>();
                        for (TblReservationRoomTypeDetail dataRoomTypeDetail14 : dataReservationRoomTypeDetails) {
                            TblReservationRoomTypeDetail dummyDataRoomTypeDetail14 = new TblReservationRoomTypeDetail(dataRoomTypeDetail14);
                            dummyDataRoomTypeDetail14.setTblReservation(dummySelectedData);
                            dummyDataRoomTypeDetail14.setTblRoomType(new TblRoomType(dummyDataRoomTypeDetail14.getTblRoomType()));
                            if (dummyDataRoomTypeDetail14.getTblReservationCheckInOut() != null) {
                                dummyDataRoomTypeDetail14.setTblReservationCheckInOut(new TblReservationCheckInOut(dummyDataRoomTypeDetail14.getTblReservationCheckInOut()));
                                if (dummyDataRoomTypeDetail14.getTblReservationCheckInOut().getTblRoom() != null) {
                                    dummyDataRoomTypeDetail14.getTblReservationCheckInOut().setTblRoom(new TblRoom(dummyDataRoomTypeDetail14.getTblReservationCheckInOut().getTblRoom()));
                                }
                                if (dummyDataRoomTypeDetail14.getTblReservationCheckInOut().getRefReservationCheckInOutStatus() != null) {
                                    dummyDataRoomTypeDetail14.getTblReservationCheckInOut().setRefReservationCheckInOutStatus(new RefReservationCheckInOutStatus(dummyDataRoomTypeDetail14.getTblReservationCheckInOut().getRefReservationCheckInOutStatus()));
                                }
                            }
                            for (TblReservationRoomTypeDetailRoomPriceDetail dataRoomTypeDetailRoomPriceDetail14 : dataReservationRoomTypeDetailRoomPriceDetails) {
                                if (dataRoomTypeDetailRoomPriceDetail14.getTblReservationRoomTypeDetail().getIddetail() == dataRoomTypeDetail14.getIddetail()) {
                                    TblReservationRoomTypeDetailRoomPriceDetail dummyDataRoomTypeDetailRoomPriceDetail14 = new TblReservationRoomTypeDetailRoomPriceDetail(dataRoomTypeDetailRoomPriceDetail14);
                                    dummyDataRoomTypeDetailRoomPriceDetail14.setTblReservationRoomTypeDetail(dummyDataRoomTypeDetail14);
                                    dummyDataRoomTypeDetailRoomPriceDetail14.setTblReservationRoomPriceDetail(new TblReservationRoomPriceDetail(dummyDataRoomTypeDetailRoomPriceDetail14.getTblReservationRoomPriceDetail()));
                                    if (dummyDataRoomTypeDetailRoomPriceDetail14.getTblReservationRoomPriceDetail().getTblHotelEvent() != null) {
                                        dummyDataRoomTypeDetailRoomPriceDetail14.getTblReservationRoomPriceDetail().setTblHotelEvent(new TblHotelEvent(dummyDataRoomTypeDetailRoomPriceDetail14.getTblReservationRoomPriceDetail().getTblHotelEvent()));
                                    }
                                    if (dummyDataRoomTypeDetailRoomPriceDetail14.getTblReservationRoomPriceDetail().getTblBankEventCard() != null) {
                                        dummyDataRoomTypeDetailRoomPriceDetail14.getTblReservationRoomPriceDetail().setTblBankEventCard(new TblBankEventCard(dummyDataRoomTypeDetailRoomPriceDetail14.getTblReservationRoomPriceDetail().getTblBankEventCard()));
                                    }
                                    dummyDataRoomTypeDetailRoomPriceDetails14.add(dummyDataRoomTypeDetailRoomPriceDetail14);
                                }
                            }
                            for (TblReservationRoomTypeDetailTravelAgentDiscountDetail dataRoomTypeDetailTravelAgentDiscountDetail14 : dataReservationRoomTypeDetailTravelAgentDiscountDetails) {
                                if (dataRoomTypeDetailTravelAgentDiscountDetail14.getTblReservationRoomTypeDetail().getIddetail() == dataRoomTypeDetail14.getIddetail()) {
                                    TblReservationRoomTypeDetailTravelAgentDiscountDetail dummyDataRoomTypeDetailTravelAgentDiscountDetail14 = new TblReservationRoomTypeDetailTravelAgentDiscountDetail(dataRoomTypeDetailTravelAgentDiscountDetail14);
                                    dummyDataRoomTypeDetailTravelAgentDiscountDetail14.setTblReservationRoomTypeDetail(dummyDataRoomTypeDetail14);
                                    dummyDataRoomTypeDetailTravelAgentDiscountDetail14.setTblReservationTravelAgentDiscountDetail(new TblReservationTravelAgentDiscountDetail(dummyDataRoomTypeDetailTravelAgentDiscountDetail14.getTblReservationTravelAgentDiscountDetail()));
                                    dummyDataRoomTypeDetailTravelAgentDiscountDetail14.getTblReservationTravelAgentDiscountDetail().setTblPartner(new TblPartner(dummyDataRoomTypeDetailTravelAgentDiscountDetail14.getTblReservationTravelAgentDiscountDetail().getTblPartner()));
                                    dummyDataRoomTypeDetailTravelAgentDiscountDetails14.add(dummyDataRoomTypeDetailTravelAgentDiscountDetail14);
                                }
                            }
                            for (TblReservationAdditionalItem dataReservationAdditionalItem14 : dataReservationAdditionalItems) {
                                if (dataReservationAdditionalItem14.getTblReservationRoomTypeDetail().getIddetail() == dataRoomTypeDetail14.getIddetail()) {
                                    TblReservationAdditionalItem dummyDataReservationAdditionalItem14 = new TblReservationAdditionalItem(dataReservationAdditionalItem14);
                                    dummyDataReservationAdditionalItem14.setTblReservationRoomTypeDetail(dummyDataRoomTypeDetail14);
                                    dummyDataReservationAdditionalItem14.setTblItem(new TblItem(dummyDataReservationAdditionalItem14.getTblItem()));
                                    dummyDataReservationAdditionalItem14.setRefReservationBillType(new RefReservationBillType(dummyDataReservationAdditionalItem14.getRefReservationBillType()));
                                    if (dummyDataReservationAdditionalItem14.getTblHotelEvent() != null) {
                                        dummyDataReservationAdditionalItem14.setTblHotelEvent(new TblHotelEvent(dummyDataReservationAdditionalItem14.getTblHotelEvent()));
                                    }
                                    if (dummyDataReservationAdditionalItem14.getTblBankEventCard() != null) {
                                        dummyDataReservationAdditionalItem14.setTblBankEventCard(new TblBankEventCard(dummyDataReservationAdditionalItem14.getTblBankEventCard()));
                                    }
                                    dummyDataReservationAdditionalItems14.add(dummyDataReservationAdditionalItem14);
                                }
                            }
                            for (TblReservationAdditionalService dataReservationAdditionalService14 : dataReservationAdditionalServices) {
                                if (dataReservationAdditionalService14.getTblReservationRoomTypeDetail().getIddetail() == dataRoomTypeDetail14.getIddetail()) {
                                    TblReservationAdditionalService dummyDataReservationAdditionalService14 = new TblReservationAdditionalService(dataReservationAdditionalService14);
                                    dummyDataReservationAdditionalService14.setTblReservationRoomTypeDetail(dummyDataRoomTypeDetail14);
                                    dummyDataReservationAdditionalService14.setTblRoomService(new TblRoomService(dummyDataReservationAdditionalService14.getTblRoomService()));
                                    dummyDataReservationAdditionalService14.setRefReservationBillType(new RefReservationBillType(dummyDataReservationAdditionalService14.getRefReservationBillType()));
                                    if (dummyDataReservationAdditionalService14.getTblHotelEvent() != null) {
                                        dummyDataReservationAdditionalService14.setTblHotelEvent(new TblHotelEvent(dummyDataReservationAdditionalService14.getTblHotelEvent()));
                                    }
                                    if (dummyDataReservationAdditionalService14.getTblBankEventCard() != null) {
                                        dummyDataReservationAdditionalService14.setTblBankEventCard(new TblBankEventCard(dummyDataReservationAdditionalService14.getTblBankEventCard()));
                                    }
                                    dummyDataReservationAdditionalServices14.add(dummyDataReservationAdditionalService14);
                                }
                            }
                            dummyDataRoomTypeDetails14.add(dummyDataRoomTypeDetail14);
                        }
                        updated = fReservationManager.updateDataReservationBillDiscountType(
                                dummySelectedData,
                                dummyDataReservationBill14,
                                dummyDataCheckOutBill14,
                                dummyDataRoomTypeDetails14,
                                dummyDataRoomTypeDetailRoomPriceDetails14,
                                dummyDataRoomTypeDetailTravelAgentDiscountDetails14,
                                dummyDataReservationAdditionalItems14,
                                dummyDataReservationAdditionalServices14
                        );
                        break;
                    case 15: //(update : update selected data)
                        //dummy entry
                        TblReservationRoomTypeDetail dummySelectedDataReservationRoomTypeDetail15 = new TblReservationRoomTypeDetail(selectedDataRoomTypeDetail);
                        dummySelectedDataReservationRoomTypeDetail15.setTblReservation(dummySelectedData);
                        dummySelectedDataReservationRoomTypeDetail15.setTblRoomType(new TblRoomType(dummySelectedDataReservationRoomTypeDetail15.getTblRoomType()));
                        if (dummySelectedDataReservationRoomTypeDetail15.getTblReservationCheckInOut() != null) {
                            dummySelectedDataReservationRoomTypeDetail15.setTblReservationCheckInOut(new TblReservationCheckInOut(dummySelectedDataReservationRoomTypeDetail15.getTblReservationCheckInOut()));
                            if (dummySelectedDataReservationRoomTypeDetail15.getTblReservationCheckInOut().getTblRoom() != null) {
                                dummySelectedDataReservationRoomTypeDetail15.getTblReservationCheckInOut().setTblRoom(new TblRoom(dummySelectedDataReservationRoomTypeDetail15.getTblReservationCheckInOut().getTblRoom()));
                            }
                            if (dummySelectedDataReservationRoomTypeDetail15.getTblReservationCheckInOut().getRefReservationCheckInOutStatus() != null) {
                                dummySelectedDataReservationRoomTypeDetail15.getTblReservationCheckInOut().setRefReservationCheckInOutStatus(new RefReservationCheckInOutStatus(dummySelectedDataReservationRoomTypeDetail15.getTblReservationCheckInOut().getRefReservationCheckInOutStatus()));
                            }
                        }
                        updated = fReservationManager.updateDataReservationRoomTypeDetailChangeRoomType(dummySelectedData, dummySelectedDataReservationRoomTypeDetail15);
                        break;
                    case 16:   //(update : update selected data, insert selected new data(s))
                        //dummy entry
                        TblReservationRoomTypeDetail dummySelectedDataReservationRoomTypeDetail16 = new TblReservationRoomTypeDetail(selectedDataRoomTypeDetail);
                        dummySelectedDataReservationRoomTypeDetail16.setTblReservation(dummySelectedData);
                        dummySelectedDataReservationRoomTypeDetail16.setTblRoomType(new TblRoomType(dummySelectedDataReservationRoomTypeDetail16.getTblRoomType()));
                        if (dummySelectedDataReservationRoomTypeDetail16.getTblReservationCheckInOut() != null) {
                            dummySelectedDataReservationRoomTypeDetail16.setTblReservationCheckInOut(new TblReservationCheckInOut(dummySelectedDataReservationRoomTypeDetail16.getTblReservationCheckInOut()));
                            if (dummySelectedDataReservationRoomTypeDetail16.getTblReservationCheckInOut().getTblRoom() != null) {
                                dummySelectedDataReservationRoomTypeDetail16.getTblReservationCheckInOut().setTblRoom(new TblRoom(dummySelectedDataReservationRoomTypeDetail16.getTblReservationCheckInOut().getTblRoom()));
                            }
                            if (dummySelectedDataReservationRoomTypeDetail16.getTblReservationCheckInOut().getRefReservationCheckInOutStatus() != null) {
                                dummySelectedDataReservationRoomTypeDetail16.getTblReservationCheckInOut().setRefReservationCheckInOutStatus(new RefReservationCheckInOutStatus(dummySelectedDataReservationRoomTypeDetail16.getTblReservationCheckInOut().getRefReservationCheckInOutStatus()));
                            }
                        }
                        if (dummySelectedDataReservationRoomTypeDetail16.getTblReservationCheckInOut() != null) {
                            dummySelectedDataReservationRoomTypeDetail16.setTblReservationCheckInOut(new TblReservationCheckInOut(dummySelectedDataReservationRoomTypeDetail16.getTblReservationCheckInOut()));
                            if (dummySelectedDataReservationRoomTypeDetail16.getTblReservationCheckInOut().getTblRoom() != null) {
                                dummySelectedDataReservationRoomTypeDetail16.getTblReservationCheckInOut().setTblRoom(new TblRoom(dummySelectedDataReservationRoomTypeDetail16.getTblReservationCheckInOut().getTblRoom()));
                                dummySelectedDataReservationRoomTypeDetail16.getTblReservationCheckInOut().getTblRoom().setRefRoomStatus(new RefRoomStatus(dummySelectedDataReservationRoomTypeDetail16.getTblReservationCheckInOut().getTblRoom().getRefRoomStatus()));
                            }
                            dummySelectedDataReservationRoomTypeDetail16.getTblReservationCheckInOut().setRefReservationCheckInOutStatus(new RefReservationCheckInOutStatus(dummySelectedDataReservationRoomTypeDetail16.getTblReservationCheckInOut().getRefReservationCheckInOutStatus()));
                        }
                        List<TblReservationRoomTypeDetail> dummySelectedDataRoomTypeDetails16 = new ArrayList<>();
                        List<TblReservationRoomTypeDetailRoomPriceDetail> dummySelectedDataRoomTypeDetailRoomPriceDetails16 = new ArrayList<>();
                        List<TblReservationRoomTypeDetailTravelAgentDiscountDetail> dummySelectedDataRoomTypeDetailTravelAgentDiscountDetails16 = new ArrayList<>();
                        List<TblReservationAdditionalItem> dummySelectedDataReservationAdditionalItems16 = new ArrayList<>();
                        List<TblReservationAdditionalService> dummySelectedDataReservationAdditionalServices16 = new ArrayList<>();
                        List<TblReservationRoomTypeDetailRoomPriceDetailChangeRoomHistory> dummySelectedDataReservationRoomTypeDetailRoomPriceDetailChangeRoomHistories16 = new ArrayList<>();
                        for (TblReservationRoomTypeDetail selectedDataRoomTypeDetail16 : selectedDataRoomTypeDetails) {
                            TblReservationRoomTypeDetail dummySelectedDataRoomTypeDetail16 = new TblReservationRoomTypeDetail(selectedDataRoomTypeDetail16);
                            dummySelectedDataRoomTypeDetail16.setTblReservation(dummySelectedData);
                            dummySelectedDataRoomTypeDetail16.setTblRoomType(new TblRoomType(dummySelectedDataRoomTypeDetail16.getTblRoomType()));
                            if (dummySelectedDataRoomTypeDetail16.getTblReservationCheckInOut() != null) {
                                dummySelectedDataRoomTypeDetail16.setTblReservationCheckInOut(new TblReservationCheckInOut(dummySelectedDataRoomTypeDetail16.getTblReservationCheckInOut()));
                                if (dummySelectedDataRoomTypeDetail16.getTblReservationCheckInOut().getTblRoom() != null) {
                                    dummySelectedDataRoomTypeDetail16.getTblReservationCheckInOut().setTblRoom(new TblRoom(dummySelectedDataRoomTypeDetail16.getTblReservationCheckInOut().getTblRoom()));
                                    dummySelectedDataRoomTypeDetail16.getTblReservationCheckInOut().getTblRoom().setRefRoomStatus(new RefRoomStatus(dummySelectedDataRoomTypeDetail16.getTblReservationCheckInOut().getTblRoom().getRefRoomStatus()));
                                }
                                dummySelectedDataRoomTypeDetail16.getTblReservationCheckInOut().setRefReservationCheckInOutStatus(new RefReservationCheckInOutStatus(dummySelectedDataRoomTypeDetail16.getTblReservationCheckInOut().getRefReservationCheckInOutStatus()));
                            }
                            for (TblReservationRoomTypeDetailRoomPriceDetail selectedDataRoomTypeDetailRoomPriceDetail16 : selectedDataRoomTypeDetailRoomPriceDetails) {
                                if (selectedDataRoomTypeDetailRoomPriceDetail16.getTblReservationRoomTypeDetail().equals(selectedDataRoomTypeDetail16)) {
                                    TblReservationRoomTypeDetailRoomPriceDetail dummySelectedDataRoomTypeDetailRoomPriceDetail16 = new TblReservationRoomTypeDetailRoomPriceDetail(selectedDataRoomTypeDetailRoomPriceDetail16);
                                    dummySelectedDataRoomTypeDetailRoomPriceDetail16.setTblReservationRoomTypeDetail(dummySelectedDataRoomTypeDetail16);
                                    dummySelectedDataRoomTypeDetailRoomPriceDetail16.setTblReservationRoomPriceDetail(new TblReservationRoomPriceDetail(dummySelectedDataRoomTypeDetailRoomPriceDetail16.getTblReservationRoomPriceDetail()));
                                    if (dummySelectedDataRoomTypeDetailRoomPriceDetail16.getTblReservationRoomPriceDetail().getTblHotelEvent() != null) {
                                        dummySelectedDataRoomTypeDetailRoomPriceDetail16.getTblReservationRoomPriceDetail().setTblHotelEvent(new TblHotelEvent(dummySelectedDataRoomTypeDetailRoomPriceDetail16.getTblReservationRoomPriceDetail().getTblHotelEvent()));
                                    }
                                    if (dummySelectedDataRoomTypeDetailRoomPriceDetail16.getTblReservationRoomPriceDetail().getTblBankEventCard() != null) {
                                        dummySelectedDataRoomTypeDetailRoomPriceDetail16.getTblReservationRoomPriceDetail().setTblBankEventCard(new TblBankEventCard(dummySelectedDataRoomTypeDetailRoomPriceDetail16.getTblReservationRoomPriceDetail().getTblBankEventCard()));
                                    }
                                    for (TblReservationRoomTypeDetailRoomPriceDetailChangeRoomHistory selectedDataReservationRoomTypeDetailRoomPriceDetailChangeRoomHistory16 : selectedDataReservationRoomTypeDetailRoomPriceDetailChangeRoomHistories) {
                                        if (selectedDataReservationRoomTypeDetailRoomPriceDetailChangeRoomHistory16.getTblReservationRoomTypeDetailRoomPriceDetailByIdrelationNew().equals(selectedDataRoomTypeDetailRoomPriceDetail16)) {
                                            TblReservationRoomTypeDetailRoomPriceDetailChangeRoomHistory dummySelectedDataReservationRoomTypeDetailRoomPriceDetailChangeRoomHistory16 = new TblReservationRoomTypeDetailRoomPriceDetailChangeRoomHistory(selectedDataReservationRoomTypeDetailRoomPriceDetailChangeRoomHistory16);
                                            dummySelectedDataReservationRoomTypeDetailRoomPriceDetailChangeRoomHistory16.setTblReservationRoomTypeDetailRoomPriceDetailByIdrelationOld(new TblReservationRoomTypeDetailRoomPriceDetail(dummySelectedDataReservationRoomTypeDetailRoomPriceDetailChangeRoomHistory16.getTblReservationRoomTypeDetailRoomPriceDetailByIdrelationOld()));
                                            dummySelectedDataReservationRoomTypeDetailRoomPriceDetailChangeRoomHistory16.getTblReservationRoomTypeDetailRoomPriceDetailByIdrelationOld().setTblReservationRoomTypeDetail(new TblReservationRoomTypeDetail(dummySelectedDataReservationRoomTypeDetailRoomPriceDetailChangeRoomHistory16.getTblReservationRoomTypeDetailRoomPriceDetailByIdrelationOld().getTblReservationRoomTypeDetail()));
                                            dummySelectedDataReservationRoomTypeDetailRoomPriceDetailChangeRoomHistory16.getTblReservationRoomTypeDetailRoomPriceDetailByIdrelationOld().getTblReservationRoomTypeDetail().setTblReservation(new TblReservation(dummySelectedDataReservationRoomTypeDetailRoomPriceDetailChangeRoomHistory16.getTblReservationRoomTypeDetailRoomPriceDetailByIdrelationOld().getTblReservationRoomTypeDetail().getTblReservation()));
                                            dummySelectedDataReservationRoomTypeDetailRoomPriceDetailChangeRoomHistory16.getTblReservationRoomTypeDetailRoomPriceDetailByIdrelationOld().getTblReservationRoomTypeDetail().setTblRoomType(new TblRoomType(dummySelectedDataReservationRoomTypeDetailRoomPriceDetailChangeRoomHistory16.getTblReservationRoomTypeDetailRoomPriceDetailByIdrelationOld().getTblReservationRoomTypeDetail().getTblRoomType()));
                                            dummySelectedDataReservationRoomTypeDetailRoomPriceDetailChangeRoomHistory16.getTblReservationRoomTypeDetailRoomPriceDetailByIdrelationOld().getTblReservationRoomTypeDetail().setRefReservationOrderByType(new RefReservationOrderByType(dummySelectedDataReservationRoomTypeDetailRoomPriceDetailChangeRoomHistory16.getTblReservationRoomTypeDetailRoomPriceDetailByIdrelationOld().getTblReservationRoomTypeDetail().getRefReservationOrderByType()));
                                            dummySelectedDataReservationRoomTypeDetailRoomPriceDetailChangeRoomHistory16.setTblReservationRoomTypeDetailRoomPriceDetailByIdrelationNew(dummySelectedDataRoomTypeDetailRoomPriceDetail16);
                                            dummySelectedDataReservationRoomTypeDetailRoomPriceDetailChangeRoomHistories16.add(dummySelectedDataReservationRoomTypeDetailRoomPriceDetailChangeRoomHistory16);
                                        }
                                    }
                                    dummySelectedDataRoomTypeDetailRoomPriceDetails16.add(dummySelectedDataRoomTypeDetailRoomPriceDetail16);
                                }
                            }
                            for (TblReservationRoomTypeDetailTravelAgentDiscountDetail selectedDataRoomTypeDetailTravelAgentDiscountDetail16 : selectedDataRoomTypeDetailTravelAgentDiscountDetails) {
                                if (selectedDataRoomTypeDetailTravelAgentDiscountDetail16.getTblReservationRoomTypeDetail().equals(selectedDataRoomTypeDetail16)) {
                                    TblReservationRoomTypeDetailTravelAgentDiscountDetail dummySelectedDataRoomTypeDetailTravelAgentDiscountDetail16 = new TblReservationRoomTypeDetailTravelAgentDiscountDetail(selectedDataRoomTypeDetailTravelAgentDiscountDetail16);
                                    dummySelectedDataRoomTypeDetailTravelAgentDiscountDetail16.setTblReservationRoomTypeDetail(dummySelectedDataRoomTypeDetail16);
                                    dummySelectedDataRoomTypeDetailTravelAgentDiscountDetail16.setTblReservationTravelAgentDiscountDetail(new TblReservationTravelAgentDiscountDetail(dummySelectedDataRoomTypeDetailTravelAgentDiscountDetail16.getTblReservationTravelAgentDiscountDetail()));
                                    dummySelectedDataRoomTypeDetailTravelAgentDiscountDetail16.getTblReservationTravelAgentDiscountDetail().setTblPartner(new TblPartner(dummySelectedDataRoomTypeDetailTravelAgentDiscountDetail16.getTblReservationTravelAgentDiscountDetail().getTblPartner()));
                                    dummySelectedDataRoomTypeDetailTravelAgentDiscountDetails16.add(dummySelectedDataRoomTypeDetailTravelAgentDiscountDetail16);
                                }
                            }
                            for (TblReservationAdditionalItem dataSelectedReservationAdditionalItem16 : selectedAdditionalItems) {
                                if (dataSelectedReservationAdditionalItem16.getTblReservationRoomTypeDetail().equals(selectedDataRoomTypeDetail16)) {
                                    TblReservationAdditionalItem dummySelectedDataReservationAdditionalItem16 = new TblReservationAdditionalItem(dataSelectedReservationAdditionalItem16);
                                    dummySelectedDataReservationAdditionalItem16.setTblReservationRoomTypeDetail(dummySelectedDataRoomTypeDetail16);
                                    dummySelectedDataReservationAdditionalItem16.setTblItem(new TblItem(dummySelectedDataReservationAdditionalItem16.getTblItem()));
                                    dummySelectedDataReservationAdditionalItem16.setRefReservationBillType(new RefReservationBillType(dummySelectedDataReservationAdditionalItem16.getRefReservationBillType()));
                                    if (dummySelectedDataReservationAdditionalItem16.getTblHotelEvent() != null) {
                                        dummySelectedDataReservationAdditionalItem16.setTblHotelEvent(new TblHotelEvent(dummySelectedDataReservationAdditionalItem16.getTblHotelEvent()));
                                    }
                                    if (dummySelectedDataReservationAdditionalItem16.getTblBankEventCard() != null) {
                                        dummySelectedDataReservationAdditionalItem16.setTblBankEventCard(new TblBankEventCard(dummySelectedDataReservationAdditionalItem16.getTblBankEventCard()));
                                    }
                                    dummySelectedDataReservationAdditionalItems16.add(dummySelectedDataReservationAdditionalItem16);
                                }
                            }
                            for (TblReservationAdditionalService dataSelectedReservationAdditionalService16 : selectedAdditionalServices) {
                                if (dataSelectedReservationAdditionalService16.getTblReservationRoomTypeDetail().equals(selectedDataRoomTypeDetail16)) {
                                    TblReservationAdditionalService dummySelectedDataReservationAdditionalService16 = new TblReservationAdditionalService(dataSelectedReservationAdditionalService16);
                                    dummySelectedDataReservationAdditionalService16.setTblReservationRoomTypeDetail(dummySelectedDataRoomTypeDetail16);
                                    dummySelectedDataReservationAdditionalService16.setTblRoomService(new TblRoomService(dummySelectedDataReservationAdditionalService16.getTblRoomService()));
                                    dummySelectedDataReservationAdditionalService16.setRefReservationBillType(new RefReservationBillType(dummySelectedDataReservationAdditionalService16.getRefReservationBillType()));
                                    if (dummySelectedDataReservationAdditionalService16.getTblHotelEvent() != null) {
                                        dummySelectedDataReservationAdditionalService16.setTblHotelEvent(new TblHotelEvent(dummySelectedDataReservationAdditionalService16.getTblHotelEvent()));
                                    }
                                    if (dummySelectedDataReservationAdditionalService16.getTblBankEventCard() != null) {
                                        dummySelectedDataReservationAdditionalService16.setTblBankEventCard(new TblBankEventCard(dummySelectedDataReservationAdditionalService16.getTblBankEventCard()));
                                    }
                                    dummySelectedDataReservationAdditionalServices16.add(dummySelectedDataReservationAdditionalService16);
                                }
                            }
                            dummySelectedDataRoomTypeDetails16.add(dummySelectedDataRoomTypeDetail16);
                        }
                        for (TblReservationAdditionalService dataSelectedReservationAdditionalService16 : selectedAdditionalServices) {
                            if (dataSelectedReservationAdditionalService16.getTblReservationRoomTypeDetail().equals(selectedDataRoomTypeDetail)) {
                                TblReservationAdditionalService dummySelectedDataReservationAdditionalService16 = new TblReservationAdditionalService(dataSelectedReservationAdditionalService16);
                                dummySelectedDataReservationAdditionalService16.setTblReservationRoomTypeDetail(dummySelectedDataReservationRoomTypeDetail16);
                                dummySelectedDataReservationAdditionalService16.setTblRoomService(new TblRoomService(dummySelectedDataReservationAdditionalService16.getTblRoomService()));
                                dummySelectedDataReservationAdditionalService16.setRefReservationBillType(new RefReservationBillType(dummySelectedDataReservationAdditionalService16.getRefReservationBillType()));
                                if (dummySelectedDataReservationAdditionalService16.getTblHotelEvent() != null) {
                                    dummySelectedDataReservationAdditionalService16.setTblHotelEvent(new TblHotelEvent(dummySelectedDataReservationAdditionalService16.getTblHotelEvent()));
                                }
                                if (dummySelectedDataReservationAdditionalService16.getTblBankEventCard() != null) {
                                    dummySelectedDataReservationAdditionalService16.setTblBankEventCard(new TblBankEventCard(dummySelectedDataReservationAdditionalService16.getTblBankEventCard()));
                                }
                                dummySelectedDataReservationAdditionalServices16.add(dummySelectedDataReservationAdditionalService16);
                            }
                        }
                        List<TblReservationPayment> dummySelectedDataReservationPayments16 = new ArrayList<>();
                        List<TblReservationPaymentWithTransfer> dummySelectedDataReservationPaymentWithTransfers16R = new ArrayList<>();
                        List<TblReservationPaymentWithBankCard> dummySelectedDataReservationPaymentWithBankCards16R = new ArrayList<>();
                        List<TblReservationPaymentWithCekGiro> dummySelectedDataReservationPaymentWithCekGiros16R = new ArrayList<>();
                        List<TblReservationPaymentWithGuaranteePayment> dummySelectedDataReservationPaymentWithGuaranteePayments16R = new ArrayList<>();
                        List<TblGuaranteeLetterItemDetail> dummySelectedDataGuaranteeLetterItemDetails16R = new ArrayList<>();
                        List<TblReservationPaymentWithReservationVoucher> dummySelectedDataReservationPaymentWithReservationVouchers16R = new ArrayList<>();
                        for (TblReservationPayment selectedDataReservationPayment16 : selectedDataTransactions) {
                            TblReservationPayment dummySelectedDataReservationPayment16 = new TblReservationPayment(selectedDataReservationPayment16);
                            dummySelectedDataReservationPayment16.setTblReservationBill(new TblReservationBill(dummySelectedDataReservationPayment16.getTblReservationBill()));
                            dummySelectedDataReservationPayment16.setTblEmployeeByIdcashier(new TblEmployee(dummySelectedDataReservationPayment16.getTblEmployeeByIdcashier()));
                            dummySelectedDataReservationPayment16.setRefReservationBillType(new RefReservationBillType(dummySelectedDataReservationPayment16.getRefReservationBillType()));
                            dummySelectedDataReservationPayment16.setRefFinanceTransactionPaymentType(new RefFinanceTransactionPaymentType(dummySelectedDataReservationPayment16.getRefFinanceTransactionPaymentType()));
                            for (TblReservationPaymentWithTransfer selectedDataReservationPaymentWithTransfer16R : selectedDataTransactionWithTransfers) {
                                if (selectedDataReservationPaymentWithTransfer16R.getTblReservationPayment().equals(selectedDataReservationPayment16)) {
                                    TblReservationPaymentWithTransfer dummySelectedDataReservationPaymentWithTransfer16R = new TblReservationPaymentWithTransfer(selectedDataReservationPaymentWithTransfer16R);
                                    dummySelectedDataReservationPaymentWithTransfer16R.setTblReservationPayment(dummySelectedDataReservationPayment16);
                                    dummySelectedDataReservationPaymentWithTransfer16R.setTblBankAccountBySenderBankAccount(new TblBankAccount(dummySelectedDataReservationPaymentWithTransfer16R.getTblBankAccountBySenderBankAccount()));
                                    dummySelectedDataReservationPaymentWithTransfer16R.setTblBankAccountByReceiverBankAccount(new TblBankAccount(dummySelectedDataReservationPaymentWithTransfer16R.getTblBankAccountByReceiverBankAccount()));
                                    dummySelectedDataReservationPaymentWithTransfers16R.add(dummySelectedDataReservationPaymentWithTransfer16R);
                                }
                            }
                            for (TblReservationPaymentWithBankCard selectedDataReservationPaymentWithBankCard16R : selectedDataTransactionWithBankCards) {
                                if (selectedDataReservationPaymentWithBankCard16R.getTblReservationPayment().equals(selectedDataReservationPayment16)) {
                                    TblReservationPaymentWithBankCard dummySelectedDataReservationPaymentWithBankCard16R = new TblReservationPaymentWithBankCard(selectedDataReservationPaymentWithBankCard16R);
                                    dummySelectedDataReservationPaymentWithBankCard16R.setTblReservationPayment(dummySelectedDataReservationPayment16);
                                    dummySelectedDataReservationPaymentWithBankCard16R.setTblBank(new TblBank(dummySelectedDataReservationPaymentWithBankCard16R.getTblBank()));
                                    dummySelectedDataReservationPaymentWithBankCard16R.setTblBankEdc(new TblBankEdc(dummySelectedDataReservationPaymentWithBankCard16R.getTblBankEdc()));
                                    dummySelectedDataReservationPaymentWithBankCard16R.setTblBankNetworkCard(new TblBankNetworkCard(dummySelectedDataReservationPaymentWithBankCard16R.getTblBankNetworkCard()));
                                    if (dummySelectedDataReservationPaymentWithBankCard16R.getTblBankEdcBankNetworkCard() != null) {
                                        dummySelectedDataReservationPaymentWithBankCard16R.setTblBankEdcBankNetworkCard(new TblBankEdcBankNetworkCard(dummySelectedDataReservationPaymentWithBankCard16R.getTblBankEdcBankNetworkCard()));
                                    }
                                    if (dummySelectedDataReservationPaymentWithBankCard16R.getTblBankEventCard() != null) {
                                        dummySelectedDataReservationPaymentWithBankCard16R.setTblBankEventCard(new TblBankEventCard(dummySelectedDataReservationPaymentWithBankCard16R.getTblBankEventCard()));
                                    }
                                    dummySelectedDataReservationPaymentWithBankCard16R.setTblBankAccount(new TblBankAccount(dummySelectedDataReservationPaymentWithBankCard16R.getTblBankAccount()));
                                    dummySelectedDataReservationPaymentWithBankCards16R.add(dummySelectedDataReservationPaymentWithBankCard16R);
                                }
                            }
                            for (TblReservationPaymentWithCekGiro selectedDataReservationPaymentWithCekGiro16R : selectedDataTransactionWithCekGiros) {
                                if (selectedDataReservationPaymentWithCekGiro16R.getTblReservationPayment().equals(selectedDataReservationPayment16)) {
                                    TblReservationPaymentWithCekGiro dummySelectedDataReservationPaymentWithCekGiro16R = new TblReservationPaymentWithCekGiro(selectedDataReservationPaymentWithCekGiro16R);
                                    dummySelectedDataReservationPaymentWithCekGiro16R.setTblReservationPayment(dummySelectedDataReservationPayment16);
                                    dummySelectedDataReservationPaymentWithCekGiro16R.setTblBank(new TblBank(dummySelectedDataReservationPaymentWithCekGiro16R.getTblBank()));
//                                        dummySelectedDataReservationPaymentWithCekGiro16R.setTblBankAccountBySenderBankAccount(new TblBankAccount(dummySelectedDataReservationPaymentWithCekGiro16R.getTblBankAccountBySenderBankAccount()));
                                    dummySelectedDataReservationPaymentWithCekGiro16R.setTblBankAccountByReceiverBankAccount(new TblBankAccount(dummySelectedDataReservationPaymentWithCekGiro16R.getTblBankAccountByReceiverBankAccount()));
                                    dummySelectedDataReservationPaymentWithCekGiros16R.add(dummySelectedDataReservationPaymentWithCekGiro16R);
                                }
                            }
                            for (TblReservationPaymentWithGuaranteePayment selectedDataReservationPaymentWithGuaranteePayment16R : selectedDataTransactionWithGuaranteePayments) {
                                if (selectedDataReservationPaymentWithGuaranteePayment16R.getTblReservationPayment().equals(selectedDataReservationPayment16)) {
                                    TblReservationPaymentWithGuaranteePayment dummySelectedDataReservationPaymentWithGuaranteePayment16R = new TblReservationPaymentWithGuaranteePayment(selectedDataReservationPaymentWithGuaranteePayment16R);
                                    dummySelectedDataReservationPaymentWithGuaranteePayment16R.setTblReservationPayment(dummySelectedDataReservationPayment16);
                                    dummySelectedDataReservationPaymentWithGuaranteePayment16R.setTblPartner(new TblPartner(dummySelectedDataReservationPaymentWithGuaranteePayment16R.getTblPartner()));
                                    if (dummySelectedDataReservationPaymentWithGuaranteePayment16R.getTblBankAccountBySenderBankAccount() != null) {
                                        dummySelectedDataReservationPaymentWithGuaranteePayment16R.setTblBankAccountBySenderBankAccount(new TblBankAccount(dummySelectedDataReservationPaymentWithGuaranteePayment16R.getTblBankAccountBySenderBankAccount()));
                                    }
                                    if (dummySelectedDataReservationPaymentWithGuaranteePayment16R.getTblBankAccountByReceiverBankAccount() != null) {
                                        dummySelectedDataReservationPaymentWithGuaranteePayment16R.setTblBankAccountByReceiverBankAccount(new TblBankAccount(dummySelectedDataReservationPaymentWithGuaranteePayment16R.getTblBankAccountByReceiverBankAccount()));
                                    }
                                    dummySelectedDataReservationPaymentWithGuaranteePayments16R.add(dummySelectedDataReservationPaymentWithGuaranteePayment16R);
                                    //guarantee letter - item detail
                                    for (TblGuaranteeLetterItemDetail selectedDataGuaranteeLetterItemDetail16R : selectedDataGuaranteeLetterItemDetails) {
                                        if (selectedDataGuaranteeLetterItemDetail16R.getTblReservationPaymentWithGuaranteePayment().equals(selectedDataReservationPaymentWithGuaranteePayment16R)) {
                                            TblGuaranteeLetterItemDetail dummySelectedDataGuaranteeLetterItemDetail16R = new TblGuaranteeLetterItemDetail(selectedDataGuaranteeLetterItemDetail16R);
                                            dummySelectedDataGuaranteeLetterItemDetail16R.setTblReservationPaymentWithGuaranteePayment(dummySelectedDataReservationPaymentWithGuaranteePayment16R);
                                            dummySelectedDataGuaranteeLetterItemDetails16R.add(dummySelectedDataGuaranteeLetterItemDetail16R);
                                        }
                                    }
                                }
                            }
                            for (TblReservationPaymentWithReservationVoucher selectedDataReservationPaymentWithReservationVoucher16R : selectedDataTransactionWithReservationVouchers) {
                                if (selectedDataReservationPaymentWithReservationVoucher16R.getTblReservationPayment().equals(selectedDataReservationPayment16)) {
                                    TblReservationPaymentWithReservationVoucher dummySelectedDataReservationPaymentWithReservationVoucher16R = new TblReservationPaymentWithReservationVoucher(selectedDataReservationPaymentWithReservationVoucher16R);
                                    dummySelectedDataReservationPaymentWithReservationVoucher16R.setTblReservationPayment(dummySelectedDataReservationPayment16);
                                    dummySelectedDataReservationPaymentWithReservationVoucher16R.setTblReservationVoucher(new TblReservationVoucher(dummySelectedDataReservationPaymentWithReservationVoucher16R.getTblReservationVoucher()));
                                    dummySelectedDataReservationPaymentWithReservationVoucher16R.getTblReservationVoucher().setRefVoucherStatus(new RefVoucherStatus(dummySelectedDataReservationPaymentWithReservationVoucher16R.getTblReservationVoucher().getRefVoucherStatus()));
                                    dummySelectedDataReservationPaymentWithReservationVouchers16R.add(dummySelectedDataReservationPaymentWithReservationVoucher16R);
                                }
                            }
                            dummySelectedDataReservationPayments16.add(dummySelectedDataReservationPayment16);
                        }
                        updated = fReservationManager.updateDataReservationRoomTypeDetailChangeRoom(
                                dummySelectedData,
                                dummySelectedDataReservationRoomTypeDetail16,
                                dummySelectedDataRoomTypeDetails16,
                                dummySelectedDataRoomTypeDetailRoomPriceDetails16,
                                dummySelectedDataRoomTypeDetailTravelAgentDiscountDetails16,
                                dummySelectedDataReservationAdditionalItems16,
                                dummySelectedDataReservationAdditionalServices16,
                                dummySelectedDataReservationPayments16,
                                dummySelectedDataReservationPaymentWithTransfers16R,
                                dummySelectedDataReservationPaymentWithBankCards16R,
                                dummySelectedDataReservationPaymentWithCekGiros16R,
                                dummySelectedDataReservationPaymentWithGuaranteePayments16R,
                                dummySelectedDataReservationPaymentWithReservationVouchers16R,
                                dummySelectedDataGuaranteeLetterItemDetails16R,
                                dummySelectedDataReservationRoomTypeDetailRoomPriceDetailChangeRoomHistories16,
                                currentDateForChangeRoomProcess,
                                defaultCheckInTime);
                        break;
                    case 17:   //(update : update selected data)
                        //dummy entry
                        List<TblReservationRoomTypeDetail> dummySelectedDataRoomTypeDetails17 = new ArrayList<>();
                        for (TblReservationRoomTypeDetail selectedDataRoomTypeDetail17 : selectedDataRoomTypeDetails) {
                            TblReservationRoomTypeDetail dummySelectedDataRoomTypeDetail17 = new TblReservationRoomTypeDetail(selectedDataRoomTypeDetail17);
                            dummySelectedDataRoomTypeDetail17.setTblReservation(dummySelectedData);
                            dummySelectedDataRoomTypeDetail17.setTblRoomType(new TblRoomType(dummySelectedDataRoomTypeDetail17.getTblRoomType()));
                            dummySelectedDataRoomTypeDetail17.setTblReservationCheckInOut(new TblReservationCheckInOut(dummySelectedDataRoomTypeDetail17.getTblReservationCheckInOut()));
                            dummySelectedDataRoomTypeDetail17.getTblReservationCheckInOut().setTblRoom(new TblRoom(dummySelectedDataRoomTypeDetail17.getTblReservationCheckInOut().getTblRoom()));
                            dummySelectedDataRoomTypeDetail17.getTblReservationCheckInOut().getTblRoom().setRefRoomStatus(new RefRoomStatus(dummySelectedDataRoomTypeDetail17.getTblReservationCheckInOut().getTblRoom().getRefRoomStatus()));
                            if (dummySelectedDataRoomTypeDetail17.getTblReservationCheckInOut().getRefReservationCheckInOutStatus() != null) {
                                dummySelectedDataRoomTypeDetail17.getTblReservationCheckInOut().setRefReservationCheckInOutStatus(new RefReservationCheckInOutStatus(dummySelectedDataRoomTypeDetail17.getTblReservationCheckInOut().getRefReservationCheckInOutStatus()));
                            }
                            dummySelectedDataRoomTypeDetails17.add(dummySelectedDataRoomTypeDetail17);
                        }
                        TblReservationBill dummyDataCheckOutBill17 = new TblReservationBill(dataCheckOutBill);
                        dummyDataCheckOutBill17.setTblReservation(dummySelectedData);
                        dummyDataCheckOutBill17.setRefReservationBillType(new RefReservationBillType(dummyDataCheckOutBill17.getRefReservationBillType()));
                        if (dummyDataCheckOutBill17.getTblBankCard() != null) {
                            dummyDataCheckOutBill17.setTblBankCard(new TblBankCard(dummyDataCheckOutBill17.getTblBankCard()));
                        }
                        if (dummyDataCheckOutBill17.getRefReservationBillDiscountType() != null) {
                            dummyDataCheckOutBill17.setRefReservationBillDiscountType(new RefReservationBillDiscountType(dummyDataCheckOutBill17.getRefReservationBillDiscountType()));
                        }
                        updated = fReservationManager.updateDataReservationRoomTypeDetailMultiCheckOut(dummySelectedData,
                                dummySelectedDataRoomTypeDetails17,
                                dummyDataCheckOutBill17,
                                calculationTotalBillAfterRounding("checkout"));
                        break;
                    case 18:
                        //dummy entry
                        TblReservationBill dummyDataReservationBill18 = new TblReservationBill(dataReservationBill);
                        dummyDataReservationBill18.setTblReservation(dummySelectedData);
                        dummyDataReservationBill18.setRefReservationBillType(new RefReservationBillType(dummyDataReservationBill18.getRefReservationBillType()));
                        if (dummyDataReservationBill18.getTblBankCard() != null) {
                            dummyDataReservationBill18.setTblBankCard(new TblBankCard(dummyDataReservationBill18.getTblBankCard()));
                        }
                        if (dummyDataReservationBill18.getRefReservationBillDiscountType() != null) {
                            dummyDataReservationBill18.setRefReservationBillDiscountType(new RefReservationBillDiscountType(dummyDataReservationBill18.getRefReservationBillDiscountType()));
                        }
                        TblReservationRoomTypeDetail dummySelectedDataRoomTypeDetail18 = new TblReservationRoomTypeDetail(selectedDataRoomTypeDetail);
                        dummySelectedDataRoomTypeDetail18.setTblReservation(dummySelectedData);
                        dummySelectedDataRoomTypeDetail18.setTblRoomType(new TblRoomType(dummySelectedDataRoomTypeDetail18.getTblRoomType()));
                        if (dummySelectedDataRoomTypeDetail18.getTblReservationCheckInOut() != null) {
                            dummySelectedDataRoomTypeDetail18.setTblReservationCheckInOut(new TblReservationCheckInOut(dummySelectedDataRoomTypeDetail18.getTblReservationCheckInOut()));
                            if (dummySelectedDataRoomTypeDetail18.getTblReservationCheckInOut().getTblRoom() != null) {
                                dummySelectedDataRoomTypeDetail18.getTblReservationCheckInOut().setTblRoom(new TblRoom(dummySelectedDataRoomTypeDetail18.getTblReservationCheckInOut().getTblRoom()));
                                dummySelectedDataRoomTypeDetail18.getTblReservationCheckInOut().getTblRoom().setRefRoomStatus(new RefRoomStatus(dummySelectedDataRoomTypeDetail18.getTblReservationCheckInOut().getTblRoom().getRefRoomStatus()));
                            }
                            dummySelectedDataRoomTypeDetail18.getTblReservationCheckInOut().setRefReservationCheckInOutStatus(new RefReservationCheckInOutStatus(dummySelectedDataRoomTypeDetail18.getTblReservationCheckInOut().getRefReservationCheckInOutStatus()));
                        }
                        List<TblReservationRoomTypeDetailRoomPriceDetail> dummySelectedDataRoomTypeDetailRoomPriceDetails18 = new ArrayList<>();
                        List<TblReservationAdditionalItem> dummySelectedDataReservationAdditionalItems18 = new ArrayList<>();
                        List<TblReservationAdditionalService> dummySelectedDataReservationAdditionalServices18 = new ArrayList<>();
                        for (TblReservationRoomTypeDetailRoomPriceDetail selectedDataRoomTypeDetailRoomPriceDetail18 : selectedDataRoomTypeDetailRoomPriceDetails) {
                            TblReservationRoomTypeDetailRoomPriceDetail dummySelectedDataRoomTypeDetailRoomPriceDetail18 = new TblReservationRoomTypeDetailRoomPriceDetail(selectedDataRoomTypeDetailRoomPriceDetail18);
                            dummySelectedDataRoomTypeDetailRoomPriceDetail18.setTblReservationRoomTypeDetail(dummySelectedDataRoomTypeDetail18);
                            dummySelectedDataRoomTypeDetailRoomPriceDetail18.setTblReservationRoomPriceDetail(new TblReservationRoomPriceDetail(dummySelectedDataRoomTypeDetailRoomPriceDetail18.getTblReservationRoomPriceDetail()));
                            if (dummySelectedDataRoomTypeDetailRoomPriceDetail18.getTblReservationRoomPriceDetail().getTblHotelEvent() != null) {
                                dummySelectedDataRoomTypeDetailRoomPriceDetail18.getTblReservationRoomPriceDetail().setTblHotelEvent(new TblHotelEvent(dummySelectedDataRoomTypeDetailRoomPriceDetail18.getTblReservationRoomPriceDetail().getTblHotelEvent()));
                            }
                            if (dummySelectedDataRoomTypeDetailRoomPriceDetail18.getTblReservationRoomPriceDetail().getTblBankEventCard() != null) {
                                dummySelectedDataRoomTypeDetailRoomPriceDetail18.getTblReservationRoomPriceDetail().setTblBankEventCard(new TblBankEventCard(dummySelectedDataRoomTypeDetailRoomPriceDetail18.getTblReservationRoomPriceDetail().getTblBankEventCard()));
                            }
                            dummySelectedDataRoomTypeDetailRoomPriceDetails18.add(dummySelectedDataRoomTypeDetailRoomPriceDetail18);
                        }
                        for (TblReservationAdditionalItem dataSelectedReservationAdditionalItem18 : selectedDataAdditionalItems) {
                            TblReservationAdditionalItem dummySelectedDataReservationAdditionalItem18 = new TblReservationAdditionalItem(dataSelectedReservationAdditionalItem18);
                            dummySelectedDataReservationAdditionalItem18.setTblReservationRoomTypeDetail(dummySelectedDataRoomTypeDetail18);
                            dummySelectedDataReservationAdditionalItem18.setTblItem(new TblItem(dummySelectedDataReservationAdditionalItem18.getTblItem()));
                            dummySelectedDataReservationAdditionalItem18.setRefReservationBillType(new RefReservationBillType(dummySelectedDataReservationAdditionalItem18.getRefReservationBillType()));
                            if (dummySelectedDataReservationAdditionalItem18.getTblHotelEvent() != null) {
                                dummySelectedDataReservationAdditionalItem18.setTblHotelEvent(new TblHotelEvent(dummySelectedDataReservationAdditionalItem18.getTblHotelEvent()));
                            }
                            if (dummySelectedDataReservationAdditionalItem18.getTblBankEventCard() != null) {
                                dummySelectedDataReservationAdditionalItem18.setTblBankEventCard(new TblBankEventCard(dummySelectedDataReservationAdditionalItem18.getTblBankEventCard()));
                            }
                            dummySelectedDataReservationAdditionalItems18.add(dummySelectedDataReservationAdditionalItem18);
                        }
                        for (TblReservationAdditionalService dataSelectedReservationAdditionalService18 : selectedDataAdditionalServices) {
                            TblReservationAdditionalService dummySelectedDataReservationAdditionalService18 = new TblReservationAdditionalService(dataSelectedReservationAdditionalService18);
                            dummySelectedDataReservationAdditionalService18.setTblReservationRoomTypeDetail(dummySelectedDataRoomTypeDetail18);
                            dummySelectedDataReservationAdditionalService18.setTblRoomService(new TblRoomService(dummySelectedDataReservationAdditionalService18.getTblRoomService()));
                            dummySelectedDataReservationAdditionalService18.setRefReservationBillType(new RefReservationBillType(dummySelectedDataReservationAdditionalService18.getRefReservationBillType()));
                            if (dummySelectedDataReservationAdditionalService18.getTblHotelEvent() != null) {
                                dummySelectedDataReservationAdditionalService18.setTblHotelEvent(new TblHotelEvent(dummySelectedDataReservationAdditionalService18.getTblHotelEvent()));
                            }
                            if (dummySelectedDataReservationAdditionalService18.getTblBankEventCard() != null) {
                                dummySelectedDataReservationAdditionalService18.setTblBankEventCard(new TblBankEventCard(dummySelectedDataReservationAdditionalService18.getTblBankEventCard()));
                            }
                            dummySelectedDataReservationAdditionalServices18.add(dummySelectedDataReservationAdditionalService18);
                        }
                        updated = fReservationManager.updateDataReservationRoomTypeDetailExtendRoom(
                                dummySelectedData,
                                dummyDataReservationBill18,
                                dummySelectedDataRoomTypeDetail18,
                                dummySelectedDataRoomTypeDetailRoomPriceDetails18,
                                dummySelectedDataReservationAdditionalItems18,
                                dummySelectedDataReservationAdditionalServices18);
                        break;
                    default:
                        break;
                }
                if (updated) {
                    refreshDataSelectedReservation();
//                    loadDataReservation(dummySelectedData.getIdreservation());
//                    setSelectedDataToInputForm();
//                        //refresh data from table & close form data reservation
//                        tableDataReservation.getTableView().setItems(loadAllDataReservation());
//                        dataReservationFormShowStatus.set(0.0);
                    if (dialogStage != null && dialogStage.isShowing()) {
                        ClassMessage.showSucceedUpdatingDataMessage("", dialogStage);
                    } else {
                        ClassMessage.showSucceedUpdatingDataMessage("", null);
                    }
                    statusSave = true;
                    //set unsaving data input -> 'false'
                    ClassSession.unSavingDataInput.set(false);
                } else {
                    if (dialogStage != null && dialogStage.isShowing()) {
                        ClassMessage.showFailedUpdatingDataMessage(fReservationManager.getErrorMessage(), dialogStage);
                    } else {
                        ClassMessage.showFailedUpdatingDataMessage(fReservationManager.getErrorMessage(), null);
                    }
                }
//                refreshDataSelectedReservation();
                break;
            default:
                break;
        }
        return statusSave;
    }

    public void refreshDataSelectedReservation() {
        loadDataReservation(selectedData.getIdreservation());
        setSelectedDataToInputForm();
    }

    public void refreshDataTableReservation() {
        tableDataReservation.getTableView().setItems(loadAllDataReservation());
        cft.refreshFilterItems(tableDataReservation.getTableView().getItems());
    }

    private void dataReservationCancelHandle() {
        //refresh data from table & close form data reservation
        refreshDataTableReservation();
        dataReservationFormShowStatus.set(0.0);
        isShowStatus.set(false);
        //set unsaving data input -> 'false'
        ClassSession.unSavingDataInput.set(false);
    }

    private String errDataInput;

    private boolean checkDataInputDataReservation() {
        boolean dataInput = true;
        errDataInput = "";
//        if (selectedData.getTblCustomer() == null
//                || selectedData.getTblCustomer().getTblPeople() == null
//                || selectedData.getTblCustomer().getTblPeople().getKtpnumber() == null) {
//            dataInput = false;
//            
//        }
        if (cbpCustomer.getValue() == null
                || selectedData.getTblCustomer() == null
                || selectedData.getTblCustomer().getIdcustomer() == 0L) {
            dataInput = false;
            errDataInput += "Customer : " + ClassMessage.defaultErrorNullValueMessage + " \n";
        }
        if (dataReservationRoomTypeDetails.isEmpty()) {
            dataInput = false;
            errDataInput += "Reservasi Kamar : " + ClassMessage.defaultErrorNullValueMessage + " \n";
        }
        return dataInput;
    }

    /**
     * PRINT HANDLE (ALL)
     */
    String printDataType = "";

    private void prePrintHandle() {
        showReservationPrintDialog();
    }

    private void showReservationPrintDialog() {
        try {
            // Load the fxml file and create a new stage for the popup dialog.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(HotelFX.class.getResource("view/feature_reservation/ReservationPrintDialog.fxml"));

            ReservationPrintController controller = new ReservationPrintController(this);
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

    //English = '0'
    //Indonesian = '1'
    public void printHandle(int language) {
        switch (printDataType) {
            case "ReservationBill":
                dataReservationBillPrintHandle(dataReservationBill, language); //Reservation = '0'
                break;
            case "CheckOutBill":
                dataReservationBillPrintHandle(dataCheckOutBill, language); //CheckOut = '1'
                break;
            case "ReservationReceipt":
                dataReservationTransactionPrintHandle(language);
                break;
            case "CheckOutReceipt":
                dataCheckOutTransactionPrintHandle(language);
                break;
            case "CancelingReceipt":
                dataReservationCancelingTransactionPrintHandle(language);
                break;
            default:
                break;
        }
    }

    /**
     * FORM INPUT RESERVATION CANCELED
     */
    /**
     * SERVICE (FOR ACCESS TO DATABASE)
     */
    private FReservationManager fReservationManager;

    /**
     * Initializes the controller class.
     *
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        //set service manager
        fReservationManager = new FReservationManagerImpl();

        //set splitpane
        setDataReservationSplitpane();

        //init table
        initTableDataReservation();

        //init form
        initFormDataReservation();

        spDataReservation.widthProperty().addListener((obs, oldVal, newVal) -> {
            dataReservationFormShowStatus.set(0.0);
        });
    }

    public FReservationManager getFReservationManager() {
        return this.fReservationManager;
    }

}
