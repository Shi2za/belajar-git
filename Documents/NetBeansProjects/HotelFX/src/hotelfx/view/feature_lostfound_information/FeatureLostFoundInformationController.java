/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hotelfx.view.feature_lostfound_information;

import com.jfoenix.controls.JFXButton;
import hotelfx.HotelFX;
import hotelfx.helper.ClassMessage;
import hotelfx.helper.ClassPseudoClassCSS;
import hotelfx.helper.ClassSession;
import hotelfx.persistence.service.FLostFoundInformationManager;
import hotelfx.persistence.service.FLostFoundInformationManagerImpl;
import hotelfx.view.feature_lostfound_information.found_information.FoundInformationController;
import hotelfx.view.feature_lostfound_information.lost_information.LostInformationController;
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
public class FeatureLostFoundInformationController implements Initializable {

    private final StringProperty selectedSubFeature = new SimpleStringProperty("");

    @FXML
    private AnchorPane subFeatureContent;

    private void setSubFeatureContent(String subFeature) {
        try {
            FXMLLoader loader = new FXMLLoader();
            switch (subFeature) {
                case "LostInformation":
                    loader.setLocation(HotelFX.class.getResource("view/feature_lostfound_information/lost_information/LostInformationView.fxml"));
                    LostInformationController lostInformationController = new LostInformationController(this);
                    loader.setController(lostInformationController);
                    break;
                case "FoundInformation":
                    loader.setLocation(HotelFX.class.getResource("view/feature_lostfound_information/found_information/FoundInformationView.fxml"));
                    FoundInformationController foundInformationController = new FoundInformationController(this);
                    loader.setController(foundInformationController);
                    break;
            }

            Node subContent = loader.load();

            AnchorPane.setTopAnchor(subContent, 0.0);
            AnchorPane.setBottomAnchor(subContent, 0.0);
            AnchorPane.setLeftAnchor(subContent, 0.0);
            AnchorPane.setRightAnchor(subContent, 0.0);

            subFeatureContent.getChildren().clear();
            subFeatureContent.getChildren().add(subContent);

        } catch (IOException ex) {
            Logger.getLogger(FeatureLostFoundInformationController.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    @FXML
    private JFXButton btnShowLostInformation;
    @FXML
    private AnchorPane btnShowLostInformationLayout;

    @FXML
    private JFXButton btnShowFoundInformation;
    @FXML
    private AnchorPane btnShowFoundInformationLayout;

    private void setButtonDataShow() {
        subFeatureContent.getStyleClass().add("sub-feature-layout");
        btnShowLostInformationLayout.getStyleClass().add("sub-feature-layout");
        btnShowFoundInformationLayout.getStyleClass().add("sub-feature-layout");

        setButtonDataPseudoClass(false, false, false);

        btnShowLostInformation.setOnMouseClicked((e) -> {
            if (!ClassSession.unSavingDataInput.get()) {
                setButtonDataPseudoClass(true, true, false);

                selectedSubFeature.set("LostInformation");
            } else {  //unsaving data input
                Alert alert = ClassMessage.showConfirmationUnSavingDataInputMessage("", null);
                if (alert.getResult() == ButtonType.OK) {
                    ClassSession.unSavingDataInput.set(false);

                    setButtonDataPseudoClass(true, true, false);

                    selectedSubFeature.set("LostInformation");
                }
            }
        });

        btnShowFoundInformation.setOnMouseClicked((e) -> {
            if (!ClassSession.unSavingDataInput.get()) {
                setButtonDataPseudoClass(true, false, true);

                selectedSubFeature.set("FoundInformation");
            } else {  //unsaving data input
                Alert alert = ClassMessage.showConfirmationUnSavingDataInputMessage("", null);
                if (alert.getResult() == ButtonType.OK) {
                    ClassSession.unSavingDataInput.set(false);

                    setButtonDataPseudoClass(true, false, true);

                    selectedSubFeature.set("FoundInformation");
                }
            }
        });
    }

    private void setButtonDataPseudoClass(boolean sfc, boolean lostInformation, boolean foundInformation) {
        subFeatureContent.pseudoClassStateChanged(ClassPseudoClassCSS.selectedSubFeaturePseudoClass, sfc);
        btnShowLostInformationLayout.pseudoClassStateChanged(ClassPseudoClassCSS.selectedSubFeaturePseudoClass, lostInformation);
                    btnShowFoundInformationLayout.pseudoClassStateChanged(ClassPseudoClassCSS.selectedSubFeaturePseudoClass, foundInformation);
    }
    
    private FLostFoundInformationManager fLostFoundInformation;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        setButtonDataShow();

        fLostFoundInformation = new FLostFoundInformationManagerImpl();

        selectedSubFeature.addListener((obs, oldVal, newVal) -> {
            //set sub feature content
            setSubFeatureContent(newVal);
        });
    }

    public FLostFoundInformationManager getFLostFoundInformation() {
        return this.fLostFoundInformation;
    }

}
