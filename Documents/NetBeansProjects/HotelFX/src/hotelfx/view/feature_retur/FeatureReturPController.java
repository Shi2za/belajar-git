/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hotelfx.view.feature_retur;

import com.jfoenix.controls.JFXButton;
import hotelfx.HotelFX;
import hotelfx.helper.ClassMessage;
import hotelfx.helper.ClassPseudoClassCSS;
import hotelfx.helper.ClassSession;
import hotelfx.persistence.service.FReturManager;
import hotelfx.persistence.service.FReturManagerImpl;
import hotelfx.view.feature_retur.retur_purchasing.ReturPController;
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
public class FeatureReturPController implements Initializable {

    private final StringProperty selectedSubFeature = new SimpleStringProperty("");

    @FXML
    private AnchorPane subFeatureContent;

    private void setSubFeatureContent(String subFeature) {
        try {
            //loader data (path) & set controller
            FXMLLoader loader = new FXMLLoader();
            switch (subFeature) {
                case "retur_p":
                    loader.setLocation(HotelFX.class.getResource("view/feature_retur/retur_purchasing/ReturPView.fxml"));

                    ReturPController rpController = new ReturPController(this);
                    loader.setController(rpController);
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

            //set 'sub feature content' into the center of 'feature_puchase_order' contetnt.
            subFeatureContent.getChildren().clear();
            subFeatureContent.getChildren().add(subContent);

        } catch (Exception e) {
            System.out.println("err >> " + e.getMessage());
        }
    }

    /**
     * Retur - Purchasing
     */
    @FXML
    private JFXButton btnShowReturP;

    @FXML
    private AnchorPane btnShowReturPLayout;

    private void setButtonDataShow() {
        subFeatureContent.getStyleClass().add("sub-feature-layout");
        btnShowReturPLayout.getStyleClass().add("sub-feature-layout");

        setButtonDataPseudoClass(false, false);

        btnShowReturP.setOnMouseClicked((e) -> {
            if (!ClassSession.unSavingDataInput.get()) {
                setButtonDataPseudoClass(true, true);

                selectedSubFeature.set("retur_p");
            } else {  //unsaving data input
                Alert alert = ClassMessage.showConfirmationUnSavingDataInputMessage("", null);
                if (alert.getResult() == ButtonType.OK) {
                    ClassSession.unSavingDataInput.set(false);

                    setButtonDataPseudoClass(true, true);

                    selectedSubFeature.set("retur_p");
                }
            }
        });
    }

    private void setButtonDataPseudoClass(boolean sfc,
            boolean returP) {
        subFeatureContent.pseudoClassStateChanged(ClassPseudoClassCSS.selectedSubFeaturePseudoClass, sfc);
        btnShowReturPLayout.pseudoClassStateChanged(ClassPseudoClassCSS.selectedSubFeaturePseudoClass, returP);
    }
    
    /**
     * SERVICE (FOR ACCESS TO DATABASE)
     */
    private FReturManager fReturManager;
    
    /**
     * Initializes the controller class.
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        //set service manager
        fReturManager = new FReturManagerImpl();

        //set button event
        setButtonDataShow();

        //set selected sub feature
        selectedSubFeature.addListener((obs, oldValue, newValue) -> {
            //set sub feature content
            setSubFeatureContent(newValue);
        });
    }    
    
    public FReturManager getFReturManager() {
        return this.fReturManager;
    }
    
}
