/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hotelfx.view.feature_partner;

import com.jfoenix.controls.JFXButton;
import hotelfx.HotelFX;
import hotelfx.helper.ClassMessage;
import hotelfx.helper.ClassPseudoClassCSS;
import hotelfx.helper.ClassSession;
import hotelfx.persistence.service.FPartnerManager;
import hotelfx.persistence.service.FPartnerManagerImpl;
import hotelfx.view.feature_partner.travel_agent.TravelAgentController;
import hotelfx.view.feature_partner.corporate.CorporateController;
import hotelfx.view.feature_partner.government.GovernmentController;
import hotelfx.view.feature_partner.partner_receivable_and_payment.PartnerReceivableAndPaymentController;
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
public class FeaturePartnerController implements Initializable {

    private final StringProperty selectedSubFeature = new SimpleStringProperty("");

    @FXML
    private AnchorPane subFeatureContent;

    private void setSubFeatureContent(String subFeature) {
        try {
            //loader data (path) & set controller
            FXMLLoader loader = new FXMLLoader();
            switch (subFeature) {
                case "travel_agent":
                    loader.setLocation(HotelFX.class.getResource("view/feature_partner/travel_agent/TravelAgentView.fxml"));

                    TravelAgentController travelAgentController = new TravelAgentController(this);
                    loader.setController(travelAgentController);
                    break;
                case "corporate":
                    loader.setLocation(HotelFX.class.getResource("view/feature_partner/corporate/CorporateView.fxml"));

                    CorporateController corporateController = new CorporateController(this);
                    loader.setController(corporateController);
                    break;
                case "government":
                    loader.setLocation(HotelFX.class.getResource("view/feature_partner/government/GovernmentView.fxml"));

                    GovernmentController governmentController = new GovernmentController(this);
                    loader.setController(governmentController);
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
     * Travel Agent
     */
    @FXML
    private JFXButton btnShowTravelAgent;

    @FXML
    private AnchorPane btnShowTravelAgentLayout;

    /**
     * Corporate
     */
    @FXML
    private JFXButton btnShowCorporate;

    @FXML
    private AnchorPane btnShowCorporateLayout;

    /**
     * Government
     */
    @FXML
    private JFXButton btnShowGovernment;

    @FXML
    private AnchorPane btnShowGovernmentLayout;

    private void setButtonDataShow() {
        subFeatureContent.getStyleClass().add("sub-feature-layout");
        btnShowTravelAgentLayout.getStyleClass().add("sub-feature-layout");
        btnShowCorporateLayout.getStyleClass().add("sub-feature-layout");
        btnShowGovernmentLayout.getStyleClass().add("sub-feature-layout");

        setButtonDataPseudoClass(false, false, false, false);

        btnShowTravelAgent.setOnMouseClicked((e) -> {
            if (!ClassSession.unSavingDataInput.get()) {
                setButtonDataPseudoClass(true, true, false, false);

                selectedSubFeature.set("travel_agent");
            } else {  //unsaving data input
                Alert alert = ClassMessage.showConfirmationUnSavingDataInputMessage("", null);
                if (alert.getResult() == ButtonType.OK) {
                    ClassSession.unSavingDataInput.set(false);

                    setButtonDataPseudoClass(true, true, false, false);

                    selectedSubFeature.set("travel_agent");
                }
            }
        });

        btnShowCorporate.setOnMouseClicked((e) -> {
            if (!ClassSession.unSavingDataInput.get()) {
                setButtonDataPseudoClass(true, false, true, false);

                selectedSubFeature.set("corporate");
            } else {  //unsaving data input
                Alert alert = ClassMessage.showConfirmationUnSavingDataInputMessage("", null);
                if (alert.getResult() == ButtonType.OK) {
                    ClassSession.unSavingDataInput.set(false);

                    setButtonDataPseudoClass(true, false, true, false);

                    selectedSubFeature.set("corporate");
                }
            }
        });

        btnShowGovernment.setOnMouseClicked((e) -> {
            if (!ClassSession.unSavingDataInput.get()) {
                setButtonDataPseudoClass(true, false, false, true);

                selectedSubFeature.set("government");
            } else {  //unsaving data input
                Alert alert = ClassMessage.showConfirmationUnSavingDataInputMessage("", null);
                if (alert.getResult() == ButtonType.OK) {
                    ClassSession.unSavingDataInput.set(false);

                    setButtonDataPseudoClass(true, false, false, true);

                    selectedSubFeature.set("government");
                }
            }
        });
    }

    private void setButtonDataPseudoClass(boolean sfc, boolean travelAgent, boolean corporate, boolean government) {
        subFeatureContent.pseudoClassStateChanged(ClassPseudoClassCSS.selectedSubFeaturePseudoClass, sfc);
        btnShowTravelAgentLayout.pseudoClassStateChanged(ClassPseudoClassCSS.selectedSubFeaturePseudoClass, travelAgent);
        btnShowCorporateLayout.pseudoClassStateChanged(ClassPseudoClassCSS.selectedSubFeaturePseudoClass, corporate);
        btnShowGovernmentLayout.pseudoClassStateChanged(ClassPseudoClassCSS.selectedSubFeaturePseudoClass, government);
    }

    /**
     * SERVICE (FOR ACCESS TO DATABASE)
     */
    private FPartnerManager fPartnerManager;

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

}
