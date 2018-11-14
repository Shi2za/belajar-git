/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hotelfx.view.feature_reservation;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXCComboBoxTablePopup;
import hotelfx.HotelFX;
import hotelfx.helper.ClassViewSetting;
import hotelfx.persistence.model.RefFinanceTransactionPaymentType;
import hotelfx.persistence.model.TblReservationPayment;
import hotelfx.view.feature_reservation.rctid.RCTICashController;
import hotelfx.view.feature_reservation.rctid.RCTICekGiroController;
import hotelfx.view.feature_reservation.rctid.RCTICreditCardController;
import hotelfx.view.feature_reservation.rctid.RCTIDebitCardController;
import hotelfx.view.feature_reservation.rctid.RCTIDepositController;
import hotelfx.view.feature_reservation.rctid.RCTIGuaranteePaymentController;
import hotelfx.view.feature_reservation.rctid.RCTIReservationVoucherController;
import hotelfx.view.feature_reservation.rctid.RCTITransferController;
import java.math.BigDecimal;
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
public class RCTransactionInputDetailController implements Initializable {

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

        cbpFinanceTransactionPaymentType.setDisable(rcTransactionInputController.dataInputRCTransactionInputDetailStatus == 1); //update == '1'
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
        List<RefFinanceTransactionPaymentType> list = rcTransactionInputController.getParentController().getFReservationManager().getAllDataFinanceTransactionPaymentType();
        for (int i = list.size() - 1; i > -1; i--) {
            if (list.get(i).getIdtype() == 13 //Open Deposit (Cash) = '13'
                    || list.get(i).getIdtype() == 14 //Close Deposit (Cash) = '14'
                    || list.get(i).getIdtype() == 9 //Draw Deposit = '9'
                    || list.get(i).getIdtype() == 16) { //Canceling Fee = '16'
                list.remove(i);
            } else {
                if ((rcTransactionInputController.getParentController().calculationTotalRestOfBill(rcTransactionInputController.getParentController().dataInputTransactionStatus)
                        .compareTo(new BigDecimal("0")) == 0)
                        && list.get(i).getIdtype() != 0) { //Tunai = '0'
                    list.remove(i);
                } else {
                    if (rcTransactionInputController.getParentController().dataInputTransactionStatus.equals("reservation")
                            && rcTransactionInputController.getParentController().dataReservationBill.getTblBankCard() != null) {
                        if (rcTransactionInputController.getParentController().dataReservationBill.getTblBankCard().getRefBankCardType().getIdtype() == 0) {   //Debit = '0'
                            if (list.get(i).getIdtype() != 2) {   //Kartu Debit = '2'
                                list.remove(i);
                            }
                        } else {  //Kredit = '1'
                            if (list.get(i).getIdtype() != 3) {   //Kartu Kredit = '3'
                                list.remove(i);
                            }
                        }
                    } else {
                        if (rcTransactionInputController.getParentController().dataInputTransactionStatus.equals("checkout")
                                && rcTransactionInputController.getParentController().dataCheckOutBill.getTblBankCard() != null) {
                            if (rcTransactionInputController.getParentController().dataCheckOutBill.getTblBankCard().getRefBankCardType().getIdtype() == 0) {   //Debit = '0'
                                if (list.get(i).getIdtype() != 2) {   //Kartu Debit = '2'
                                    list.remove(i);
                                }
                            } else {  //Kredit = '1'
                                if (list.get(i).getIdtype() != 3) {   //Kartu Kredit = '3'
                                    list.remove(i);
                                }
                            }
                        } else {
                            //1 bill, max 1 transaction with voucher : inserting data ('0')
                            if (list.get(i).getIdtype() == 10 //Voucher = '10'
                                    && rcTransactionInputController.dataInputRCTransactionInputDetailStatus == 0) {
                                boolean found = false;
                                //data bill (reservation)
                                for (TblReservationPayment data : rcTransactionInputController.getParentController().dataReservationPayments) {
                                    if (data.getRefFinanceTransactionPaymentType().getIdtype() == 10) {   //Voucher = '10'
                                        found = true;
                                    }
                                }
                                //data bill (checkout)
                                for (TblReservationPayment data : rcTransactionInputController.getParentController().dataCheckOutPayments) {
                                    if (data.getRefFinanceTransactionPaymentType().getIdtype() == 10) {   //Voucher = '10'
                                        found = true;
                                    }
                                }
                                if (found) {
                                    list.remove(i);
                                }
                            } else {
                                //reservation with guarantee payment must be first payment
                                if((list.get(i).getIdtype() == 7 //Guarantee Letter (Corporate)  = 7
                                        || list.get(i).getIdtype() == 8) //Guarantee Letter (Government)  = 8
                                        && (!isFirstPayment())){    //first payment in bill
                                    list.remove(i);
                                }else{
                                    //reservation must be order by 'travel agent'
                                    if (list.get(i).getIdtype() == 6) { //Travel Agent  = 6
                                        if((rcTransactionInputController.getParentController().selectedData.getTblPartner() == null)    //order by travel agent
                                                || (!rcTransactionInputController.getParentController().dataInputTransactionStatus.equals("reservation"))   //reservation bill
                                                || (!isFirstPayment())){    //first payment in bill
                                            list.remove(i);
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        return list;
    }

    private boolean isFirstPayment(){
        boolean firstPayment;
        if(rcTransactionInputController.getParentController().dataInputTransactionStatus.equals("reservation")){    //reservation
            if(rcTransactionInputController.dataInputRCTransactionInputDetailStatus == 0){   //insert
                firstPayment = rcTransactionInputController.getParentController().dataReservationPayments.isEmpty();
            }else{  //update
                firstPayment = rcTransactionInputController.getParentController().dataReservationPayments.size() == 1;
            }
        }else{  //checkout
            if(rcTransactionInputController.dataInputRCTransactionInputDetailStatus == 0){   //insert
                firstPayment = rcTransactionInputController.getParentController().dataCheckOutPayments.isEmpty();
            }else{  //update
                firstPayment = rcTransactionInputController.getParentController().dataCheckOutPayments.size() == 1;
            }
        }
        return firstPayment;
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

        cbpFinanceTransactionPaymentType.valueProperty().bindBidirectional(rcTransactionInputController.getParentController().selectedDataTransaction.refFinanceTransactionPaymentTypeProperty());

        cbpFinanceTransactionPaymentType.hide();

    }

    private void setTransactionTypeInputForm(RefFinanceTransactionPaymentType transactionType) {
        try {
            //loader data (path) & set controller
            FXMLLoader loader = new FXMLLoader();
            switch (transactionType.getIdtype()) {
                case 0:    //Tunai = '0'
                    loader.setLocation(HotelFX.class.getResource("view/feature_reservation/rctid/RCTICash.fxml"));

                    RCTICashController rctiCashController = new RCTICashController(this);
                    loader.setController(rctiCashController);
                    break;
                case 1:    //Transfer = '1'
                    loader.setLocation(HotelFX.class.getResource("view/feature_reservation/rctid/RCTITransfer.fxml"));

                    RCTITransferController rctiTransferController = new RCTITransferController(this);
                    loader.setController(rctiTransferController);
                    break;
                case 2:   //Debit Card = '2'
                    loader.setLocation(HotelFX.class.getResource("view/feature_reservation/rctid/RCTIDebitCard.fxml"));

                    RCTIDebitCardController rctiDebitCardController = new RCTIDebitCardController(this);
                    loader.setController(rctiDebitCardController);
                    break;
                case 3:  //Credit Card = '3'
                    loader.setLocation(HotelFX.class.getResource("view/feature_reservation/rctid/RCTICreditCard.fxml"));

                    RCTICreditCardController rctiCreditCardController = new RCTICreditCardController(this);
                    loader.setController(rctiCreditCardController);
                    break;
                case 4: //Cek = '4'
                    loader.setLocation(HotelFX.class.getResource("view/feature_reservation/rctid/RCTICekGiro.fxml"));

                    RCTICekGiroController rctiCekController = new RCTICekGiroController(this);
                    loader.setController(rctiCekController);
                    break;
                case 5:    //Giro = '5'
                    loader.setLocation(HotelFX.class.getResource("view/feature_reservation/rctid/RCTICekGiro.fxml"));

                    RCTICekGiroController rctiGiroController = new RCTICekGiroController(this);
                    loader.setController(rctiGiroController);
                    break;
                case 6:    //Travel Agent = '6'
                    loader.setLocation(HotelFX.class.getResource("view/feature_reservation/rctid/RCTIGuaranteePayment.fxml"));

                    RCTIGuaranteePaymentController rctiTravelAgentController = new RCTIGuaranteePaymentController(this,
                            "Travel Agent");
                    loader.setController(rctiTravelAgentController);
                    break;
                case 7:    //Guarantee Letter (Corporate) = '7'
                    loader.setLocation(HotelFX.class.getResource("view/feature_reservation/rctid/RCTIGuaranteePayment.fxml"));

                    RCTIGuaranteePaymentController rctiCorporateController = new RCTIGuaranteePaymentController(this,
                            "Guarantee Letter (Corporate)");
                    loader.setController(rctiCorporateController);
                    break;
                case 8:   //Guarantee Letter (Government) = '8'
                    loader.setLocation(HotelFX.class.getResource("view/feature_reservation/rctid/RCTIGuaranteePayment.fxml"));

                    RCTIGuaranteePaymentController rctiGovernmentController = new RCTIGuaranteePaymentController(this,
                            "Guarantee Letter (Government)");
                    loader.setController(rctiGovernmentController);
                    break;
                case 9:    //Draw Deposit = '9'
                    loader.setLocation(HotelFX.class.getResource("view/feature_reservation/rctid/RCTIDeposit.fxml"));

                    RCTIDepositController rctiDrawDepositController = new RCTIDepositController(this);
                    loader.setController(rctiDrawDepositController);
                    break;
                case 10:   //Voucher = '10'
                    loader.setLocation(HotelFX.class.getResource("view/feature_reservation/rctid/RCTIReservationVoucher.fxml"));

                    RCTIReservationVoucherController rctiReservationVoucherController = new RCTIReservationVoucherController(this);
                    loader.setController(rctiReservationVoucherController);
                    break;
                case 13:    //Open Deposit (Cash) = '13'
                    loader.setLocation(HotelFX.class.getResource("view/feature_reservation/rctid/RCTIDeposit.fxml"));

                    RCTIDepositController rctiOpenDepositController = new RCTIDepositController(this);
                    loader.setController(rctiOpenDepositController);
                    break;
                case 14:    //Close Deposit (Cash) = '14'
                    loader.setLocation(HotelFX.class.getResource("view/feature_reservation/rctid/RCTIDeposit.fxml"));

                    RCTIDepositController rctiCloseDepositController = new RCTIDepositController(this);
                    loader.setController(rctiCloseDepositController);
                    break;
//                case 16 : //Canceling Fee = '16'
//                    loader.setLocation(HotelFX.class.getResource("view/feature_bank/bank_event_card/BankEventCardView.fxml"));
//
//                    BankEventCardController bankEventCardController = new BankEventCardController(this);
//                    loader.setController(bankEventCardController);
//                    break;
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

            //set 'sub feature content' into the center of 'feature_bank' contetnt.
            ancDataInputDetailTransactionLayout.getChildren().clear();
            ancDataInputDetailTransactionLayout.getChildren().add(subContent);

        } catch (Exception e) {
            System.out.println("err >> " + e.getMessage());
        }
    }

    private void dataRCTransactionDetailCancelHandle() {
        //close dialog
        rcTransactionInputController.dialogStage.close();
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

    public RCTransactionInputDetailController(RCTransactionInputController parentController) {
        rcTransactionInputController = parentController;
    }

    private final RCTransactionInputController rcTransactionInputController;

    public RCTransactionInputController getParentController() {
        return rcTransactionInputController;
    }

}
