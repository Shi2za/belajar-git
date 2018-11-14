/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hotelfx.view.feature_user_account;

import com.jfoenix.controls.JFXButton;
import hotelfx.HotelFX;
import hotelfx.helper.ClassMessage;
import hotelfx.helper.ClassPseudoClassCSS;
import hotelfx.helper.ClassSession;
import hotelfx.persistence.service.FUserAccountManager;
import hotelfx.persistence.service.FUserAccountManagerImpl;
import hotelfx.view.feature_user_account.user_account.UserAccountController;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
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
 *
 * @author Andreas
 */
public class FeatureUserAccountController implements Initializable {

    private final StringProperty selectedSubFeature = new SimpleStringProperty("");

    @FXML
    private AnchorPane subFeatureContent;

    private void setSubFeatureContent(String feature) {
        try {
            FXMLLoader loader = new FXMLLoader();
            switch (feature) {
                case "user_account":
                    loader.setLocation(HotelFX.class.getResource("view/feature_user_account/user_account/UserAccountView.fxml"));
                    UserAccountController userAccountController = new UserAccountController(this);
                    loader.setController(userAccountController);
                    break;
                default:
                    loader.setLocation(HotelFX.class.getResource(""));
                    break;
            }

            Node subContent = loader.load();

            AnchorPane.setTopAnchor(subContent, 0.0);
            AnchorPane.setLeftAnchor(subContent, 0.0);
            AnchorPane.setBottomAnchor(subContent, 0.0);
            AnchorPane.setRightAnchor(subContent, 0.0);

            subFeatureContent.getChildren().clear();
            subFeatureContent.getChildren().add(subContent);
        } catch (IOException ex) {
            Logger.getLogger(FeatureUserAccountController.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    @FXML
    private JFXButton btnShowUserAccount;

    @FXML
    private AnchorPane btnShowUserAccountLayout;

    private void setButtonDataShow() {
        subFeatureContent.getStyleClass().add("sub-feature-layout");
        btnShowUserAccountLayout.getStyleClass().add("sub-feature-layout");

        setButtonDataPseudoClass(false, false);

        btnShowUserAccount.setOnMouseClicked((e) -> {
            if (!ClassSession.unSavingDataInput.get()) {
                setButtonDataPseudoClass(true, true);

                selectedSubFeature.set("user_account");
            } else {  //unsaving data input
                Alert alert = ClassMessage.showConfirmationUnSavingDataInputMessage("", null);
                if (alert.getResult() == ButtonType.OK) {
                    ClassSession.unSavingDataInput.set(false);

                    setButtonDataPseudoClass(true, true);

                    selectedSubFeature.set("user_account");
                }
            }
        });
    }

    private void setButtonDataPseudoClass(boolean sfc, boolean userAccount) {
        subFeatureContent.pseudoClassStateChanged(ClassPseudoClassCSS.selectedSubFeaturePseudoClass, sfc);
        btnShowUserAccountLayout.pseudoClassStateChanged(ClassPseudoClassCSS.selectedSubFeaturePseudoClass, userAccount);
    }
    
    private FUserAccountManager fUserAccountManager;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        fUserAccountManager = new FUserAccountManagerImpl();

        setButtonDataShow();

        //setSubFeatureContent("user_account");
        selectedSubFeature.addListener((obs, oldVal, newVal) -> {
            setSubFeatureContent(newVal);
        });
    }

    public FUserAccountManager getFUserAccountManager() {
        return this.fUserAccountManager;
    }

}
