/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hotelfx.view.feature_reservation.rcr;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXCComboBoxTablePopup;
import hotelfx.HotelFX;
import hotelfx.helper.ClassViewSetting;
import hotelfx.persistence.model.RefFinanceTransactionPaymentType;
import hotelfx.persistence.model.TblReservationPayment;
import hotelfx.view.feature_reservation.ReservationChangeRoomInputController;
import hotelfx.view.feature_reservation.rcr.rctid.RCRRCTICashController;
import hotelfx.view.feature_reservation.rcr.rctid.RCRRCTICekGiroController;
import hotelfx.view.feature_reservation.rcr.rctid.RCRRCTICreditCardController;
import hotelfx.view.feature_reservation.rcr.rctid.RCRRCTIDebitCardController;
import hotelfx.view.feature_reservation.rcr.rctid.RCRRCTIDepositController;
import hotelfx.view.feature_reservation.rcr.rctid.RCRRCTIGuaranteePaymentController;
import hotelfx.view.feature_reservation.rcr.rctid.RCRRCTIReservationVoucherController;
import hotelfx.view.feature_reservation.rcr.rctid.RCRRCTITransferController;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;

/**
 * FXML Controller class
 *
 * @author ANDRI
 */
public class RCRRCTransactionInputController implements Initializable {

    @FXML
    private AnchorPane ancFormRCTransaction;

    @FXML
    private GridPane gpFormDataTransaction;

    @FXML
    private AnchorPane ancPaymentType;

    public JFXCComboBoxTablePopup<RefFinanceTransactionPaymentType> cbpFinanceTransactionPaymentType;

    @FXML
    private JFXButton btnCancel;

    @FXML
    private AnchorPane ancDataInputDetailTransactionLayout;

    private void initFormDataRCTransactionDetail() {
        initDataPopup();
        
        btnCancel.setTooltip(new Tooltip("Batal"));
        btnCancel.setOnAction((e) -> {
            dataRCTransactionDetailCancelHandle();
        });

        initImportantFieldColor();

        cbpFinanceTransactionPaymentType.setDisable(reservationChangeRoomInputController.dataInputRCTransactionInputDetailStatus == 1); //update == '1'
    }

    private void initImportantFieldColor() {
        ClassViewSetting.setImportantField(
                cbpFinanceTransactionPaymentType);
    }

    private void initDataPopup() {
        //Finance Transaction Payment Type
        TableView<RefFinanceTransactionPaymentType> tableFinanceTransactionPaymentType = new TableView<>();

        TableColumn<RefFinanceTransactionPaymentType, String> typeName = new TableColumn<>("Tipe Pembayaran");
        typeName.setCellValueFactory(cellData -> cellData.getValue().typeNameProperty());
        typeName.setMinWidth(250);

        tableFinanceTransactionPaymentType.getColumns().addAll(typeName);

        ObservableList<RefFinanceTransactionPaymentType> ftpTypeItems = FXCollections.observableArrayList(loadAllDataFinanceTransactionPaymentType());

        cbpFinanceTransactionPaymentType = new JFXCComboBoxTablePopup<>(
                RefFinanceTransactionPaymentType.class, tableFinanceTransactionPaymentType, ftpTypeItems, "", "Tipe Pembayaran *", true, 270, 300
        );

        //attached to grid-pane
        AnchorPane.setBottomAnchor(cbpFinanceTransactionPaymentType, 0.0);
        AnchorPane.setLeftAnchor(cbpFinanceTransactionPaymentType, 0.0);
        AnchorPane.setRightAnchor(cbpFinanceTransactionPaymentType, 0.0);
        AnchorPane.setTopAnchor(cbpFinanceTransactionPaymentType, 0.0);
        ancPaymentType.getChildren().clear();
        ancPaymentType.getChildren().add(cbpFinanceTransactionPaymentType);
    }

    private void refreshDataPopup() {
        //Finance Transaction Payment Type
        ObservableList<RefFinanceTransactionPaymentType> ftpTypeItems = FXCollections.observableArrayList(loadAllDataFinanceTransactionPaymentType());
        cbpFinanceTransactionPaymentType.setItems(ftpTypeItems);
    }

    private List<RefFinanceTransactionPaymentType> loadAllDataFinanceTransactionPaymentType() {
        List<RefFinanceTransactionPaymentType> list = reservationChangeRoomInputController.getParentController().getFReservationManager().getAllDataFinanceTransactionPaymentType();
        for (int i = list.size() - 1; i > -1; i--) {
            if (list.get(i).getIdtype() == 6 //Travel Agent = '6'
                    || list.get(i).getIdtype() == 13 //Open Deposit (Cash) = '13'
                    || list.get(i).getIdtype() == 14 //Close Deposit (Cash) = '14'
                    || list.get(i).getIdtype() == 9 //Draw Deposit = '9'
                    || list.get(i).getIdtype() == 16) { //Canceling Fee = '16'
                list.remove(i);
            } else {
//                if (rcTransactionInputController.getParentController().calculationTotalRestOfBill(rcTransactionInputController.getParentController().dataInputTransactionStatus) == 0
//                        && list.get(i).getIdtype() != 0) { //Tunai = '0'
//                    list.remove(i);
//                } else {
//                    if (rcTransactionInputController.getParentController().dataInputTransactionStatus.equals("reservation")
//                            && rcTransactionInputController.getParentController().dataReservationBill.getTblBankCard() != null) {
                if (reservationChangeRoomInputController.getParentController().dataReservationBill.getTblBankCard() != null) {
                    if (reservationChangeRoomInputController.getParentController().dataReservationBill.getTblBankCard().getRefBankCardType().getIdtype() == 0) {   //Debit = '0'
                        if (list.get(i).getIdtype() != 2) {   //Kartu Debit = '2'
                            list.remove(i);
                        }
                    } else {  //Kredit = '1'
                        if (list.get(i).getIdtype() != 3) {   //Kartu Kredit = '3'
                            list.remove(i);
                        }
                    }
                } else {
//                        if (rcTransactionInputController.getParentController().dataInputTransactionStatus.equals("checkout")
//                                && rcTransactionInputController.getParentController().dataCheckOutBill.getTblBankCard() != null) {
//                            if (rcTransactionInputController.getParentController().dataCheckOutBill.getTblBankCard().getRefBankCardType().getIdtype() == 0) {   //Debit = '0'
//                                if (list.get(i).getIdtype() != 2) {   //Kartu Debit = '2'
//                                    list.remove(i);
//                                }
//                            } else {  //Kredit = '1'
//                                if (list.get(i).getIdtype() != 3) {   //Kartu Kredit = '3'
//                                    list.remove(i);
//                                }
//                            }
//                        } else {
                    //1 bill, max 1 transaction with voucher : inserting data ('0')
                    if (list.get(i).getIdtype() == 10 //Voucher = '10'
                            && reservationChangeRoomInputController.dataInputRCTransactionInputDetailStatus == 0) { //insert
                        boolean found = false;
                        //data bill (reservation)
                        for (TblReservationPayment data : reservationChangeRoomInputController.getParentController().dataReservationPayments) {
                            if (data.getRefFinanceTransactionPaymentType().getIdtype() == 10) {   //Voucher = '10'
                                found = true;
                            }
                        }
                        if (!found) {
                            for (TblReservationPayment data : reservationChangeRoomInputController.getParentController().selectedDataTransactions) {
                                if (data.getRefFinanceTransactionPaymentType().getIdtype() == 10) {   //Voucher = '10'
                                    found = true;
                                }
                            }
                        }
//                        //data bill (checkout)
//                        for (TblReservationPayment data : rcTransactionInputController.getParentController().dataCheckOutPayments) {
//                            if (data.getRefFinanceTransactionPaymentType().getIdtype() == 10) {   //Voucher = '10'
//                                found = true;
//                            }
//                        }
                        if (found) {
                            list.remove(i);
                        }
                    } else {
                        //reservation with guarantee payment must be first payment
                        if ((list.get(i).getIdtype() == 7 //Guarantee Letter (Corporate)  = 7
                                || list.get(i).getIdtype() == 8) //Guarantee Letter (Government)  = 8
                                && (!isFirstPayment())) {    //first payment in bill
                            list.remove(i);
                        }
                    }
//                        }
                }
//                }
            }
        }
        return list;
    }

    private boolean isFirstPayment() {
        if (reservationChangeRoomInputController.dataInputRCTransactionInputDetailStatus == 0) {  //insert
            return reservationChangeRoomInputController.getParentController().selectedDataTransactions.isEmpty();
        } else {  //update
            return reservationChangeRoomInputController.getParentController().selectedDataTransactions.size() == 1;
        }
    }

    private void setSelectedDataToInputForm() {
        refreshDataPopup();

        //unvisible (data input transaction)
        for (Node node : ancDataInputDetailTransactionLayout.getChildren()) {
            node.setVisible(false);
        }

        cbpFinanceTransactionPaymentType.valueProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                setTransactionTypeInputForm(newVal);
            }
        });

        cbpFinanceTransactionPaymentType.valueProperty().bindBidirectional(reservationChangeRoomInputController.tempSelectedDataTransaction.refFinanceTransactionPaymentTypeProperty());

        cbpFinanceTransactionPaymentType.hide();

    }

    private void setTransactionTypeInputForm(RefFinanceTransactionPaymentType transactionType) {
        try {
            //loader data (path) & set controller
            FXMLLoader loader = new FXMLLoader();
            switch (transactionType.getIdtype()) {
                case 0:    //Tunai = '0'
                    loader.setLocation(HotelFX.class.getResource("view/feature_reservation/rcr/rctid/RCRRCTICash.fxml"));

                    RCRRCTICashController rcrrctiCashController = new RCRRCTICashController(this);
                    loader.setController(rcrrctiCashController);
                    break;
                case 1:    //Transfer = '1'
                    loader.setLocation(HotelFX.class.getResource("view/feature_reservation/rcr/rctid/RCRRCTITransfer.fxml"));

                    RCRRCTITransferController rcrrctiTransferController = new RCRRCTITransferController(this);
                    loader.setController(rcrrctiTransferController);
                    break;
                case 2:   //Debit Card = '2'
                    loader.setLocation(HotelFX.class.getResource("view/feature_reservation/rcr/rctid/RCRRCTIDebitCard.fxml"));

                    RCRRCTIDebitCardController rcrrctiDebitCardController = new RCRRCTIDebitCardController(this);
                    loader.setController(rcrrctiDebitCardController);
                    break;
                case 3:  //Credit Card = '3'
                    loader.setLocation(HotelFX.class.getResource("view/feature_reservation/rcr/rctid/RCRRCTICreditCard.fxml"));

                    RCRRCTICreditCardController rcrrctiCreditCardController = new RCRRCTICreditCardController(this);
                    loader.setController(rcrrctiCreditCardController);
                    break;
                case 4: //Cek = '4'
                    loader.setLocation(HotelFX.class.getResource("view/feature_reservation/rcr/rctid/RCRRCTICekGiro.fxml"));

                    RCRRCTICekGiroController rcrrctiCekController = new RCRRCTICekGiroController(this);
                    loader.setController(rcrrctiCekController);
                    break;
                case 5:    //Giro = '5'
                    loader.setLocation(HotelFX.class.getResource("view/feature_reservation/rcr/rctid/RCRRCTICekGiro.fxml"));

                    RCRRCTICekGiroController rcrrctiGiroController = new RCRRCTICekGiroController(this);
                    loader.setController(rcrrctiGiroController);
                    break;
                case 6:    //Travel Agent = '6'
                    loader.setLocation(HotelFX.class.getResource("view/feature_reservation/rcr/rctid/RCRRCTIGuaranteePayment.fxml"));

                    RCRRCTIGuaranteePaymentController rcrrctiTravelAgentController = new RCRRCTIGuaranteePaymentController(this,
                            "Travel Agent");
                    loader.setController(rcrrctiTravelAgentController);
                    break;
                case 7:    //Guarantee Letter (Corporate) = '7'
                    loader.setLocation(HotelFX.class.getResource("view/feature_reservation/rcr/rctid/RCRRCTIGuaranteePayment.fxml"));

                    RCRRCTIGuaranteePaymentController rcrrctiCorporateController = new RCRRCTIGuaranteePaymentController(this,
                            "Guarantee Letter (Corporate)");
                    loader.setController(rcrrctiCorporateController);
                    break;
                case 8:   //Guarantee Letter (Government) = '8'
                    loader.setLocation(HotelFX.class.getResource("view/feature_reservation/rcr/rctid/RCRRCTIGuaranteePayment.fxml"));

                    RCRRCTIGuaranteePaymentController rcrrctiGovernmentController = new RCRRCTIGuaranteePaymentController(this,
                            "Guarantee Letter (Government)");
                    loader.setController(rcrrctiGovernmentController);
                    break;
                case 9:    //Draw Deposit = '9'
                    loader.setLocation(HotelFX.class.getResource("view/feature_reservation/rcr/rctid/RCRRCTIDeposit.fxml"));

                    RCRRCTIDepositController rcrrctiDrawDepositController = new RCRRCTIDepositController(this);
                    loader.setController(rcrrctiDrawDepositController);
                    break;
                case 10:   //Voucher = '10'
                    loader.setLocation(HotelFX.class.getResource("view/feature_reservation/rcr/rctid/RCRRCTIReservationVoucher.fxml"));

                    RCRRCTIReservationVoucherController rcrrctiReservationVoucherController = new RCRRCTIReservationVoucherController(this);
                    loader.setController(rcrrctiReservationVoucherController);
                    break;
                case 13:    //Open Deposit (Cash) = '13'
                    loader.setLocation(HotelFX.class.getResource("view/feature_reservation/rcr/rctid/RCRRCTIDeposit.fxml"));

                    RCRRCTIDepositController rcrrctiOpenDepositController = new RCRRCTIDepositController(this);
                    loader.setController(rcrrctiOpenDepositController);
                    break;
                case 14:    //Close Deposit (Cash) = '14'
                    loader.setLocation(HotelFX.class.getResource("view/feature_reservation/rcr/rctid/RCRRCTIDeposit.fxml"));

                    RCRRCTIDepositController rcrrctiCloseDepositController = new RCRRCTIDepositController(this);
                    loader.setController(rcrrctiCloseDepositController);
                    break;
                default:
                    loader.setLocation(HotelFX.class.getResource(""));
                    break;
            }

            //load loader to node
            Node subContent = loader.load();

            //set anchor position
            AnchorPane.setTopAnchor(subContent, 0.0);
            AnchorPane.setLeftAnchor(subContent, 0.0);
            AnchorPane.setRightAnchor(subContent, 0.0);
            AnchorPane.setBottomAnchor(subContent, 0.0);

            //set 'sub feature content' into the center of 'feature' contetnt.
            ancDataInputDetailTransactionLayout.getChildren().clear();
            ancDataInputDetailTransactionLayout.getChildren().add(subContent);

        } catch (Exception e) {
            System.out.println("err >> " + e.getMessage());
        }
    }

    private void dataRCTransactionDetailCancelHandle() {
        //close dialog
        reservationChangeRoomInputController.dialogStage.close();
    }

    /**
     * Initializes the controller class.
     *
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        //init form input
        initFormDataRCTransactionDetail();
        //refresh data form input
        setSelectedDataToInputForm();
    }

    public RCRRCTransactionInputController(ReservationChangeRoomInputController parentController) {
        reservationChangeRoomInputController = parentController;
    }

    private final ReservationChangeRoomInputController reservationChangeRoomInputController;

    public ReservationChangeRoomInputController getParentController() {
        return reservationChangeRoomInputController;
    }

}
