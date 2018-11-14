/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hotelfx.view.feature_finance_transaction;

import com.jfoenix.controls.JFXButton;
import hotelfx.HotelFX;
import hotelfx.helper.ClassMessage;
import hotelfx.helper.ClassPseudoClassCSS;
import hotelfx.helper.ClassSession;
import hotelfx.persistence.service.FExpenseTransactionManager;
import hotelfx.persistence.service.FExpenseTransactionManagerImpl;
import hotelfx.persistence.service.FFinanceTransactionManager;
import hotelfx.persistence.service.FFinanceTransactionManagerImpl;
import hotelfx.persistence.service.FPartnerManager;
import hotelfx.persistence.service.FPartnerManagerImpl;
import hotelfx.persistence.service.FPurchaseOrderManager;
import hotelfx.persistence.service.FPurchaseOrderManagerImpl;
import hotelfx.persistence.service.FRestoManager;
import hotelfx.persistence.service.FRestoManagerImpl;
import hotelfx.view.feature_expense_transaction.ExpenseTransactionController;
import hotelfx.view.feature_finance_transaction.transaction_settlement.TransactionSettlementController;
import hotelfx.view.feature_partner.partner_receivable_and_payment.PartnerReceivableAndPaymentController;
import hotelfx.view.feature_purchase_order.purchase_order_payable_and_payment.PurchaseOrderPayableAndPaymentController;
import hotelfx.view.feature_resto.resto_payable_and_payment.RestoPayableAndPaymentController;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.layout.AnchorPane;

/**
 * FXML Controller class
 *
 * @author ABC-Programmer
 */
public class FeatureFinanceTransactionController implements Initializable {

    private final StringProperty selectedSubFeature = new SimpleStringProperty("");

    @FXML
    private AnchorPane subFeatureContent;

    private void setSubFeatureContent(String subFeature) {
        try {
            //loader data (path) & set controller
            FXMLLoader loader = new FXMLLoader();
            switch (subFeature) {
                case "resto_payable_and_payment":
                    loader.setLocation(HotelFX.class.getResource("view/feature_resto/resto_payable_and_payment/RestoPayableAndPaymentView.fxml"));

                    RestoPayableAndPaymentController rpapController = new RestoPayableAndPaymentController(this);
                    loader.setController(rpapController);
                    break;
                case "partner_receivable_and_payment":
                    loader.setLocation(HotelFX.class.getResource("view/feature_partner/partner_receivable_and_payment/PartnerReceivableAndPaymentView.fxml"));

                    PartnerReceivableAndPaymentController partnerReceivableAndPaymentController = new PartnerReceivableAndPaymentController(this);
                    loader.setController(partnerReceivableAndPaymentController);
                    break;
                case "purchase_order_payable_and_payment":
                    loader.setLocation(HotelFX.class.getResource("view/feature_purchase_order/purchase_order_payable_and_payment/PurchaseOrderPayableAndPaymentView.fxml"));

                    PurchaseOrderPayableAndPaymentController popapController = new PurchaseOrderPayableAndPaymentController(this);
                    loader.setController(popapController);
                    break;
                case "expense_transaction":
                    loader.setLocation(HotelFX.class.getResource("view/feature_expense_transaction/ExpenseTransactionView.fxml"));

                    ExpenseTransactionController expenseTransactionController = new ExpenseTransactionController(this);
                    loader.setController(expenseTransactionController);
                    break;
                case "settle_transaction":
                    loader.setLocation(HotelFX.class.getResource("view/feature_finance_transaction/transaction_settlement/TransactionSettlementView.fxml"));

                    TransactionSettlementController transactionSettlementController = new TransactionSettlementController(this);
                    loader.setController(transactionSettlementController);
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

            //set 'sub feature content' into the center of 'feature_location' contetnt.
            subFeatureContent.getChildren().clear();
            subFeatureContent.getChildren().add(subContent);

        } catch (Exception e) {
            System.out.println("err >> " + e.getMessage());
        }
    }

    /**
     * Resto - Payable - payment
     */
    @FXML
    private JFXButton btnShowRestoPayablePayment;

    @FXML
    private AnchorPane btnShowRestoPayablePaymentLayout;
    
    /**
     * Partner - Receivable - Payment
     */
    @FXML
    private JFXButton btnShowPartnerReceivablePayment;

    @FXML
    private AnchorPane btnShowPartnerReceivablePaymentLayout;

    /**
     * PO - Payable - payment
     */
    @FXML
    private JFXButton btnShowPOPayablePayment;

    @FXML
    private AnchorPane btnShowPOPayablePaymentLayout;

    /**
     * Expense Transaction
     */
    @FXML
    private JFXButton btnShowExpenseTransaction;

    @FXML
    private AnchorPane btnShowExpenseTransactionLayout;
    
    /**
     * Transaction  Settlement
     */
    @FXML
    private JFXButton btnShowTransactionSettlement;

    @FXML
    private AnchorPane btnShowTransactionSettlementLayout;

    private void setButtonDataShow() {
        subFeatureContent.getStyleClass().add("sub-feature-layout");
        btnShowRestoPayablePaymentLayout.getStyleClass().add("sub-feature-layout");
        btnShowPartnerReceivablePaymentLayout.getStyleClass().add("sub-feature-layout");
        btnShowPOPayablePaymentLayout.getStyleClass().add("sub-feature-layout");
        btnShowExpenseTransactionLayout.getStyleClass().add("sub-feature-layout");
        btnShowTransactionSettlementLayout.getStyleClass().add("sub-feature-layout");

        setButtonDataPseudoClass(false, false, false, false, false, false);

        btnShowRestoPayablePayment.setOnMouseClicked((e) -> {
            if (!ClassSession.unSavingDataInput.get()) {
                setButtonDataPseudoClass(true, true, false, false, false, false);

                selectedSubFeature.set("resto_payable_and_payment");
            } else {  //unsaving data input
                Alert alert = ClassMessage.showConfirmationUnSavingDataInputMessage("", null);
                if (alert.getResult() == ButtonType.OK) {
                    ClassSession.unSavingDataInput.set(false);

                    setButtonDataPseudoClass(true, true, false, false, false, false);

                    selectedSubFeature.set("resto_payable_and_payment");
                }
            }
        });
        
        btnShowPartnerReceivablePayment.setOnMouseClicked((e) -> {
            if (!ClassSession.unSavingDataInput.get()) {
                setButtonDataPseudoClass(true, false, true, false, false, false);

                selectedSubFeature.set("partner_receivable_and_payment");
            } else {  //unsaving data input
                Alert alert = ClassMessage.showConfirmationUnSavingDataInputMessage("", null);
                if (alert.getResult() == ButtonType.OK) {
                    ClassSession.unSavingDataInput.set(false);

                    setButtonDataPseudoClass(true, false, true, false, false, false);

                    selectedSubFeature.set("partner_receivable_and_payment");
                }
            }
        });

        btnShowPOPayablePayment.setOnMouseClicked((e) -> {
            if (!ClassSession.unSavingDataInput.get()) {
                setButtonDataPseudoClass(true, false, false, true, false, false);

                selectedSubFeature.set("purchase_order_payable_and_payment");
            } else {  //unsaving data input
                Alert alert = ClassMessage.showConfirmationUnSavingDataInputMessage("", null);
                if (alert.getResult() == ButtonType.OK) {
                    ClassSession.unSavingDataInput.set(false);

                    setButtonDataPseudoClass(true, false, false, true, false, false);

                    selectedSubFeature.set("purchase_order_payable_and_payment");
                }
            }
        });

        btnShowExpenseTransaction.setOnMouseClicked((e) -> {
            if (!ClassSession.unSavingDataInput.get()) {
                setButtonDataPseudoClass(true, false, false, false, true, false);

                selectedSubFeature.set("expense_transaction");
            } else {  //unsaving data input
                Alert alert = ClassMessage.showConfirmationUnSavingDataInputMessage("", null);
                if (alert.getResult() == ButtonType.OK) {
                    ClassSession.unSavingDataInput.set(false);

                    setButtonDataPseudoClass(true, false, false, false, true, false);

                    selectedSubFeature.set("expense_transaction");
                }
            }
        });
        
        btnShowTransactionSettlement.setOnMouseClicked((e) -> {
            if (!ClassSession.unSavingDataInput.get()) {
                setButtonDataPseudoClass(true, false, false, false,false, true);

                selectedSubFeature.set("settle_transaction");
            } else {  //unsaving data input
                Alert alert = ClassMessage.showConfirmationUnSavingDataInputMessage("", null);
                if (alert.getResult() == ButtonType.OK) {
                    ClassSession.unSavingDataInput.set(false);

                    setButtonDataPseudoClass(true, false, false, false, false, true);

                    selectedSubFeature.set("settle_transaction");
                }
            }
        });
        
        btnShowRestoPayablePayment.setText("Pembayaran \n   (Resto)");
        btnShowPartnerReceivablePayment.setText("Pembayaran \n   (Partner)");
    }

    private void setButtonDataPseudoClass(boolean sfc, boolean restoPayablePayment, boolean partnerReceivablePayment, boolean poPayablePayment, boolean expenseTransaction, boolean transactionSettlement) {
        subFeatureContent.pseudoClassStateChanged(ClassPseudoClassCSS.selectedSubFeaturePseudoClass, sfc);
        btnShowRestoPayablePaymentLayout.pseudoClassStateChanged(ClassPseudoClassCSS.selectedSubFeaturePseudoClass, restoPayablePayment);
        btnShowPartnerReceivablePaymentLayout.pseudoClassStateChanged(ClassPseudoClassCSS.selectedSubFeaturePseudoClass, partnerReceivablePayment);
        btnShowPOPayablePaymentLayout.pseudoClassStateChanged(ClassPseudoClassCSS.selectedSubFeaturePseudoClass, poPayablePayment);
        btnShowExpenseTransactionLayout.pseudoClassStateChanged(ClassPseudoClassCSS.selectedSubFeaturePseudoClass, expenseTransaction);
        btnShowTransactionSettlementLayout.pseudoClassStateChanged(ClassPseudoClassCSS.selectedSubFeaturePseudoClass, transactionSettlement);
    }

    /**
     * SERVICE (FOR ACCESS TO DATABASE)
     */
    private FPartnerManager fPartnerManager;

    private FPurchaseOrderManager fPurchaseOrderManager;

    private FExpenseTransactionManager fExpenseTransactionManager;
    
    private FRestoManager fRestoManager;
    
    private FFinanceTransactionManager fFinanceTransactionManager;

    /**
     * Initializes the controller class.
     *
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        //set service manager
        fPartnerManager = new FPartnerManagerImpl();
        fPurchaseOrderManager = new FPurchaseOrderManagerImpl();
        fExpenseTransactionManager = new FExpenseTransactionManagerImpl();
        fRestoManager = new FRestoManagerImpl();
        fFinanceTransactionManager = new FFinanceTransactionManagerImpl();

        //set button event
        setButtonDataShow();

        //set selected sub feature
        selectedSubFeature.addListener((obs, oldValue, newValue) -> {
            //set sub feature content
            setSubFeatureContent(newValue);
        });
    }

    public FPartnerManager getFPartnerManager() {
        return this.fPartnerManager;
    }

    public FPurchaseOrderManager getFPurchaseOrderManager() {
        return this.fPurchaseOrderManager;
    }

    public FExpenseTransactionManager getFExpenseTransactionManager() {
        return this.fExpenseTransactionManager;
    }
    
    public FRestoManager getFRestoManager() {
        return this.fRestoManager;
    }

    public FFinanceTransactionManager getFFinanceTransactionManager() {
        return this.fFinanceTransactionManager;
    }
    
}
