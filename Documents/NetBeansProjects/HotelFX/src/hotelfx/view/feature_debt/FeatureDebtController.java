/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hotelfx.view.feature_debt;

import com.jfoenix.controls.JFXButton;
import hotelfx.HotelFX;
import hotelfx.helper.ClassMessage;
import hotelfx.helper.ClassPseudoClassCSS;
import hotelfx.helper.ClassSession;
import hotelfx.persistence.service.FDebtManager;
import hotelfx.persistence.service.FDebtManagerImpl;
import hotelfx.view.feature_debt.debt.DebtController;
import hotelfx.view.feature_debt.debt_payment_history.DebtPaymentHistoryController;
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
public class FeatureDebtController implements Initializable {

    private final StringProperty selectedSubFeature = new SimpleStringProperty("");

    @FXML
    private AnchorPane subFeatureContent;

    private void setSubFeatureContent(String subFeature) {
        try {
            //loader data (path) & set controller
            FXMLLoader loader = new FXMLLoader();
            switch (subFeature) {
                case "hutang":
                    loader.setLocation(HotelFX.class.getResource("view/feature_debt/debt/DebtView.fxml"));
                    DebtController debtController = new DebtController(this);
                    //CustomerController customerController = new CustomerController(this);
                    loader.setController(debtController);
                    break;
                case "history_pembayaran":
                    loader.setLocation(HotelFX.class.getResource("view/feature_debt/debt_payment_history/DebtPaymentHistoryView.fxml"));
                    DebtPaymentHistoryController debtPaymentHistoryController = new DebtPaymentHistoryController(this);
                    loader.setController(debtPaymentHistoryController);
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

            //set 'sub feature content' into the center of 'feature_customer' contetnt.
            subFeatureContent.getChildren().clear();
            subFeatureContent.getChildren().add(subContent);

        } catch (Exception e) {
            System.out.println("err >> " + e.getMessage());
        }
    }

    /**
     * Customer
     */
    @FXML
    private JFXButton btnShowDebt;

    @FXML
    private AnchorPane btnShowDebtLayout;

    @FXML
    private JFXButton btnShowPaymentDebtHistory;

    @FXML
    private AnchorPane btnShowPaymentDebtHistoryLayout;

    private void setButtonDataShow() {
        subFeatureContent.getStyleClass().add("sub-feature-layout");
        btnShowDebtLayout.getStyleClass().add("sub-feature-layout");
        btnShowPaymentDebtHistoryLayout.getStyleClass().add("sub-feature-layout");

        setButtonDataPseudoClass(false, false, false);

        btnShowDebt.setOnMouseClicked((e) -> {
            if (!ClassSession.unSavingDataInput.get()) {
                setButtonDataPseudoClass(true, true, false);

                selectedSubFeature.set("hutang");
            } else {  //unsaving data input
                Alert alert = ClassMessage.showConfirmationUnSavingDataInputMessage("", null);
                if (alert.getResult() == ButtonType.OK) {
                    ClassSession.unSavingDataInput.set(false);

                    setButtonDataPseudoClass(true, true, false);

                    selectedSubFeature.set("hutang");
                }
            }
        });

        btnShowPaymentDebtHistory.setOnMouseClicked((e) -> {
            if (!ClassSession.unSavingDataInput.get()) {
                setButtonDataPseudoClass(true, false, true);

                selectedSubFeature.set("history_pembayaran");
            } else {  //unsaving data input
                Alert alert = ClassMessage.showConfirmationUnSavingDataInputMessage("", null);
                if (alert.getResult() == ButtonType.OK) {
                    ClassSession.unSavingDataInput.set(false);

                    setButtonDataPseudoClass(true, false, true);

                    selectedSubFeature.set("history_pembayaran");
                }
            }
        });
    }

    private void setButtonDataPseudoClass(boolean sfc, boolean hutang, boolean historyPembayaran) {
        subFeatureContent.pseudoClassStateChanged(ClassPseudoClassCSS.selectedSubFeaturePseudoClass, sfc);
        btnShowDebtLayout.pseudoClassStateChanged(ClassPseudoClassCSS.selectedSubFeaturePseudoClass, hutang);
        btnShowPaymentDebtHistoryLayout.pseudoClassStateChanged(ClassPseudoClassCSS.selectedSubFeaturePseudoClass, historyPembayaran);
    }

    /**
     * SERVICE (FOR ACCESS TO DATABASE)
     */
    private FDebtManager fDebtManager;

    /**
     * Initializes the controller class.
     *
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        //set service manager
        fDebtManager = new FDebtManagerImpl();

        //set button event
        setButtonDataShow();

        //set selected sub feature
        selectedSubFeature.addListener((obs, oldValue, newValue) -> {
            //set sub feature content
            setSubFeatureContent(newValue);
        });
    }

    public FDebtManager getFDebtManager() {
        return this.fDebtManager;
    }

}
