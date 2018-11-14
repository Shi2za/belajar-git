/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hotelfx.view.feature_bank;

import com.jfoenix.controls.JFXButton;
import hotelfx.HotelFX;
import hotelfx.helper.ClassMessage;
import hotelfx.helper.ClassPseudoClassCSS;
import hotelfx.helper.ClassSession;
import hotelfx.persistence.service.FBankManager;
import hotelfx.persistence.service.FBankManagerImpl;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.layout.AnchorPane;
import hotelfx.view.feature_bank.bank.BankController;
import hotelfx.view.feature_bank.bank_edc.BankEDCController;
import hotelfx.view.feature_bank.bank_network_card.BankNetworkCardController;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

/**
 * FXML Controller class
 *
 * @author ANDRI
 */
public class FeatureBankController implements Initializable {

    private final StringProperty selectedSubFeature = new SimpleStringProperty("");

    @FXML
    private AnchorPane subFeatureContent;

    private void setSubFeatureContent(String subFeature) {
        try {
            //loader data (path) & set controller
            FXMLLoader loader = new FXMLLoader();
            switch (subFeature) {
                case "bank":
                    loader.setLocation(HotelFX.class.getResource("view/feature_bank/bank/BankView.fxml"));

                    BankController bankController = new BankController(this);
                    loader.setController(bankController);
                    break;
                case "bank_network_card":
                    loader.setLocation(HotelFX.class.getResource("view/feature_bank/bank_network_card/BankNetworkCardView.fxml"));
//                    loader.setLocation(HotelFX.class.getResource("view/feature_bank/bank_network_card/TestAja.fxml"));

                    BankNetworkCardController bankNetworkCardController = new BankNetworkCardController(this);
                    loader.setController(bankNetworkCardController);
                    break;
                case "bank_edc":
                    loader.setLocation(HotelFX.class.getResource("view/feature_bank/bank_edc/BankEDCView.fxml"));

                    BankEDCController banKEDCController = new BankEDCController(this);
                    loader.setController(banKEDCController);
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
     * Bank
     */
    @FXML
    private JFXButton btnShowBank;

    @FXML
    private AnchorPane btnShowBankLayout;

    /**
     * Bank Network Card
     */
    @FXML
    private JFXButton btnShowBankNetworkCard;

    @FXML
    private AnchorPane btnShowBankNetworkCardLayout;

    /**
     * Bank EDC
     */
    @FXML
    private JFXButton btnShowBankEDC;

    @FXML
    private AnchorPane btnShowBankEDCLayout;

    private void setButtonDataShow() {
        subFeatureContent.getStyleClass().add("sub-feature-layout");
        btnShowBankLayout.getStyleClass().add("sub-feature-layout");
        btnShowBankNetworkCardLayout.getStyleClass().add("sub-feature-layout");
        btnShowBankEDCLayout.getStyleClass().add("sub-feature-layout");

        setButtonDataPseudoClass(false, false, false, false);

        btnShowBank.setOnMouseClicked((e) -> {
            if (!ClassSession.unSavingDataInput.get()) {
                setButtonDataPseudoClass(true, true, false, false);

                selectedSubFeature.set("bank");
            } else {  //unsaving data input
                Alert alert = ClassMessage.showConfirmationUnSavingDataInputMessage("", null);
                if (alert.getResult() == ButtonType.OK) {
                    ClassSession.unSavingDataInput.set(false);

                    setButtonDataPseudoClass(true, true, false, false);

                    selectedSubFeature.set("bank");
                }
            }
        });

        btnShowBankNetworkCard.setOnMouseClicked((e) -> {
            if (!ClassSession.unSavingDataInput.get()) {
                setButtonDataPseudoClass(true, false, true, false);

                selectedSubFeature.set("bank_network_card");
            } else {  //unsaving data input
                Alert alert = ClassMessage.showConfirmationUnSavingDataInputMessage("", null);
                if (alert.getResult() == ButtonType.OK) {
                    ClassSession.unSavingDataInput.set(false);

                    setButtonDataPseudoClass(true, false, true, false);

                    selectedSubFeature.set("bank_network_card");
                }
            }
        });

        btnShowBankEDC.setOnMouseClicked((e) -> {
            if (!ClassSession.unSavingDataInput.get()) {
                setButtonDataPseudoClass(true, false, false, true);

                selectedSubFeature.set("bank_edc");
            } else {  //unsaving data input
                Alert alert = ClassMessage.showConfirmationUnSavingDataInputMessage("", null);
                if (alert.getResult() == ButtonType.OK) {
                    ClassSession.unSavingDataInput.set(false);

                    setButtonDataPseudoClass(true, false, false, true);

                    selectedSubFeature.set("bank_edc");
                }
            }
        });

    }

    private void setButtonDataPseudoClass(boolean sfc, boolean bank, boolean bankNetworkCard, boolean bankEDC) {
        subFeatureContent.pseudoClassStateChanged(ClassPseudoClassCSS.selectedSubFeaturePseudoClass, sfc);
        btnShowBankLayout.pseudoClassStateChanged(ClassPseudoClassCSS.selectedSubFeaturePseudoClass, bank);
        btnShowBankNetworkCardLayout.pseudoClassStateChanged(ClassPseudoClassCSS.selectedSubFeaturePseudoClass, bankNetworkCard);
        btnShowBankEDCLayout.pseudoClassStateChanged(ClassPseudoClassCSS.selectedSubFeaturePseudoClass, bankEDC);
    }

    /**
     * SERVICE (FOR ACCESS TO DATABASE)
     */
    private FBankManager fBankManager;

    /**
     * Initializes the controller class.
     *
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        //set service manager
        fBankManager = new FBankManagerImpl();

        //set button event
        setButtonDataShow();

        //set selected sub feature
        selectedSubFeature.addListener((obs, oldValue, newValue) -> {
            //set sub feature content
            setSubFeatureContent(newValue);
        });
    }

    public FBankManager getFBankManager() {
        return this.fBankManager;
    }

}
