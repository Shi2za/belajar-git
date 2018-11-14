/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hotelfx.view.feature_balance;

import com.jfoenix.controls.JFXButton;
import hotelfx.HotelFX;
import hotelfx.helper.ClassMessage;
import hotelfx.helper.ClassPseudoClassCSS;
import hotelfx.helper.ClassSession;
import hotelfx.persistence.service.FBalanceManager;
import hotelfx.persistence.service.FBalanceManagerImpl;
import hotelfx.view.feature_balance.back_office_balance.BackOfficeBalanceController;
import hotelfx.view.feature_balance.cash_flow.CashFlowController;
import hotelfx.view.feature_balance.cashier_balance.CashierBalanceController;
import hotelfx.view.feature_balance.company_balance.CompanyBalanceController;
import hotelfx.view.feature_balance.deposit_balance.DepositBalanceController;
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
 * @author ANDRI
 */
public class FeatureBalanceController implements Initializable {

    private final StringProperty selectedSubFeature = new SimpleStringProperty("");

    @FXML
    private AnchorPane subFeatureContent;

    private void setSubFeatureContent(String subFeature) {
        try {
            //loader data (path) & set controller
            FXMLLoader loader = new FXMLLoader();
            switch (subFeature) {
                case "company":
                    loader.setLocation(HotelFX.class.getResource("view/feature_balance/company_balance/CompanyBalanceView.fxml"));

                    CompanyBalanceController companyBalanceController = new CompanyBalanceController(this);
                    loader.setController(companyBalanceController);
                    break;
                case "cashier":
                    loader.setLocation(HotelFX.class.getResource("view/feature_balance/cashier_balance/CashierBalanceView.fxml"));

                    CashierBalanceController cashierBalanceController = new CashierBalanceController(this);
                    loader.setController(cashierBalanceController);
                    break;
                case "back_office":
                    loader.setLocation(HotelFX.class.getResource("view/feature_balance/back_office_balance/BackOfficeBalanceView.fxml"));

                    BackOfficeBalanceController backOfficeBalanceController = new BackOfficeBalanceController(this);
                    loader.setController(backOfficeBalanceController);
                    break;
                case "deposit":
                    loader.setLocation(HotelFX.class.getResource("view/feature_balance/deposit_balance/DepositBalanceView.fxml"));

                    DepositBalanceController depositBalanceController = new DepositBalanceController(this);
                    loader.setController(depositBalanceController);
                    break;
                case "cash_flow":
                    loader.setLocation(HotelFX.class.getResource("view/feature_balance/cash_flow/CashFlowView.fxml"));
                    CashFlowController cashFlowController = new CashFlowController(this);
                    loader.setController(cashFlowController);
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

            //set 'sub feature content' into the center of 'feature_bank' contetnt.
            subFeatureContent.getChildren().clear();
            subFeatureContent.getChildren().add(subContent);

        } catch (Exception e) {
            System.out.println("err >> " + e.getMessage());
        }
    }

    /**
     * Company Balance
     */
    @FXML
    private JFXButton btnShowCompanyBalance;

    @FXML
    private AnchorPane btnShowCompanyBalanceLayout;

    /**
     * Cashier Balance
     */
    @FXML
    private JFXButton btnShowCashierBalance;

    @FXML
    private AnchorPane btnShowCashierBalanceLayout;

    /**
     * BackOffice Balance
     */
    @FXML
    private JFXButton btnShowBackOfficeBalance;

    @FXML
    private AnchorPane btnShowBackOfficeBalanceLayout;

    /**
     * Deposit Balance
     */
    @FXML
    private JFXButton btnShowDepositBalance;

    @FXML
    private AnchorPane btnShowDepositBalanceLayout;
    
    /**
     * Deposit Balance
     */
    @FXML
    private JFXButton btnShowCashFlow;

    @FXML
    private AnchorPane btnShowCashFlowLayout;
    
    private void setButtonDataShow() {
        subFeatureContent.getStyleClass().add("sub-feature-layout");
        btnShowCompanyBalanceLayout.getStyleClass().add("sub-feature-layout");
        btnShowCashierBalanceLayout.getStyleClass().add("sub-feature-layout");
        btnShowBackOfficeBalanceLayout.getStyleClass().add("sub-feature-layout");
        btnShowDepositBalanceLayout.getStyleClass().add("sub-feature-layout");
        btnShowCashFlowLayout.getStyleClass().add("sub-feature-layout");
        
        setButtonDataPseudoClass(false, false, false, false, false,false);

        btnShowCompanyBalance.setOnMouseClicked((e) -> {
            if (!ClassSession.unSavingDataInput.get()) {
                setButtonDataPseudoClass(true, true, false, false, false,false);

                selectedSubFeature.set("company");
            } else {  //unsaving data input
                Alert alert = ClassMessage.showConfirmationUnSavingDataInputMessage("", null);
                if (alert.getResult() == ButtonType.OK) {
                    ClassSession.unSavingDataInput.set(false);

                    setButtonDataPseudoClass(true, true, false, false, false,false);

                    selectedSubFeature.set("company");
                }
            }
        });

        btnShowCashierBalance.setOnMouseClicked((e) -> {
            if (!ClassSession.unSavingDataInput.get()) {
                setButtonDataPseudoClass(true, false, true, false, false,false);

                selectedSubFeature.set("cashier");
            } else {  //unsaving data input
                Alert alert = ClassMessage.showConfirmationUnSavingDataInputMessage("", null);
                if (alert.getResult() == ButtonType.OK) {
                    ClassSession.unSavingDataInput.set(false);

                    setButtonDataPseudoClass(true, false, true, false, false,false);

                    selectedSubFeature.set("cashier");
                }
            }
        });

        btnShowBackOfficeBalance.setOnMouseClicked((e) -> {
            if (!ClassSession.unSavingDataInput.get()) {
                setButtonDataPseudoClass(true, false, false, true, false,false);

                selectedSubFeature.set("back_office");
            } else {  //unsaving data input
                Alert alert = ClassMessage.showConfirmationUnSavingDataInputMessage("", null);
                if (alert.getResult() == ButtonType.OK) {
                    ClassSession.unSavingDataInput.set(false);

                    setButtonDataPseudoClass(true, false, false, true, false,false);

                    selectedSubFeature.set("back_office");
                }
            }
        });

        btnShowDepositBalance.setOnMouseClicked((e) -> {
            if (!ClassSession.unSavingDataInput.get()) {
                setButtonDataPseudoClass(true, false, false, false, true,false);

                selectedSubFeature.set("deposit");
            } else {  //unsaving data input
                Alert alert = ClassMessage.showConfirmationUnSavingDataInputMessage("", null);
                if (alert.getResult() == ButtonType.OK) {
                    ClassSession.unSavingDataInput.set(false);

                    setButtonDataPseudoClass(true, false, false, false, true,false);

                    selectedSubFeature.set("deposit");
                }
            }
        });
        
        btnShowCashFlow.setOnMouseClicked((e)->{
            if(!ClassSession.unSavingDataInput.get()){
              setButtonDataPseudoClass(true,false,false,false,false,true);
              selectedSubFeature.set("cash_flow");
            }
            else{
              Alert alert = ClassMessage.showConfirmationUnSavingDataInputMessage("", null);
              if(alert.getResult()==ButtonType.OK){
                 ClassSession.unSavingDataInput.set(false);
                  setButtonDataPseudoClass(true,false,false,false,false,true);
                  selectedSubFeature.set("cash_flow");
              }
            }
        });
    }

    private void setButtonDataPseudoClass(boolean sfc, boolean company, boolean cashier, boolean backOffice, boolean deposit,boolean cashFlow) {
        subFeatureContent.pseudoClassStateChanged(ClassPseudoClassCSS.selectedSubFeaturePseudoClass, sfc);
        btnShowCompanyBalanceLayout.pseudoClassStateChanged(ClassPseudoClassCSS.selectedSubFeaturePseudoClass, company);
        btnShowCashierBalanceLayout.pseudoClassStateChanged(ClassPseudoClassCSS.selectedSubFeaturePseudoClass, cashier);
        btnShowBackOfficeBalanceLayout.pseudoClassStateChanged(ClassPseudoClassCSS.selectedSubFeaturePseudoClass, backOffice);
        btnShowDepositBalanceLayout.pseudoClassStateChanged(ClassPseudoClassCSS.selectedSubFeaturePseudoClass, deposit);
        btnShowCashFlowLayout.pseudoClassStateChanged(ClassPseudoClassCSS.selectedSubFeaturePseudoClass,cashFlow);
    }

    /**
     * SERVICE (FOR ACCESS TO DATABASE)
     */
    private FBalanceManager fBalanceManager;

    /**
     * Initializes the controller class.
     *
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        //set service manager
        fBalanceManager = new FBalanceManagerImpl();

        //set button event
        setButtonDataShow();

        //set selected sub feature
        selectedSubFeature.addListener((obs, oldValue, newValue) -> {
            //set sub feature content
            setSubFeatureContent(newValue);
        });
    }

    public FBalanceManager getFBalanceManager() {
        return this.fBalanceManager;
    }

}
