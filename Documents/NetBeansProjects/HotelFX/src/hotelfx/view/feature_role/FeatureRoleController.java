/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hotelfx.view.feature_role;

import com.jfoenix.controls.JFXButton;
import hotelfx.HotelFX;
import hotelfx.helper.ClassMessage;
import hotelfx.helper.ClassPseudoClassCSS;
import hotelfx.helper.ClassSession;
import hotelfx.persistence.service.FRoleManager;
import hotelfx.persistence.service.FRoleManagerImpl;
import hotelfx.view.feature_role.role.RoleController;
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
public class FeatureRoleController implements Initializable {

    private final StringProperty selectedSubFeature = new SimpleStringProperty("");

    @FXML
    private AnchorPane subFeatureContent;

    private void setSubFeatureContent(String subFeature) {
        try {
            FXMLLoader loader = new FXMLLoader();
            switch (subFeature) {
                case "role":
                    loader.setLocation(HotelFX.class.getResource("view/feature_role/role/RoleView.fxml"));
                    RoleController roleController = new RoleController(this);
                    loader.setController(roleController);
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
            Logger.getLogger(FeatureRoleController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @FXML
    private JFXButton btnShowRole;

    @FXML
    private AnchorPane btnShowRoleLayout;

    private void setButtonDataShow() {
        subFeatureContent.getStyleClass().add("sub-feature-layout");
        btnShowRoleLayout.getStyleClass().add("sub-feature-layout");

        setButtonDataPseudoClass(false, false);

        btnShowRole.setOnMouseClicked((e) -> {
            if (!ClassSession.unSavingDataInput.get()) {
                setButtonDataPseudoClass(true, true);

                selectedSubFeature.set("role");
            } else {  //unsaving data input
                Alert alert = ClassMessage.showConfirmationUnSavingDataInputMessage("", null);
                if (alert.getResult() == ButtonType.OK) {
                    ClassSession.unSavingDataInput.set(false);

                    setButtonDataPseudoClass(true, true);

                    selectedSubFeature.set("role");
                }
            }
        });
    }

    private void setButtonDataPseudoClass(boolean sfc, boolean role) {
        subFeatureContent.pseudoClassStateChanged(ClassPseudoClassCSS.selectedSubFeaturePseudoClass, sfc);
        btnShowRoleLayout.pseudoClassStateChanged(ClassPseudoClassCSS.selectedSubFeaturePseudoClass, role);
    }
    
    private FRoleManager fRoleManager;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        fRoleManager = new FRoleManagerImpl();
        setButtonDataShow();
        selectedSubFeature.addListener((obs, oldVal, newVal) -> {
            setSubFeatureContent(newVal);
        });
    }

    public FRoleManager getRoleManager() {
        return this.fRoleManager;
    }
}
