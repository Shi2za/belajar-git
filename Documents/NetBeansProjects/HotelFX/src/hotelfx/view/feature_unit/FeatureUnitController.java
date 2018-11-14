/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hotelfx.view.feature_unit;

import com.jfoenix.controls.JFXButton;
import hotelfx.HotelFX;
import hotelfx.helper.ClassMessage;
import hotelfx.helper.ClassPseudoClassCSS;
import hotelfx.helper.ClassSession;
import hotelfx.persistence.service.FUnitManager;
import hotelfx.persistence.service.FUnitManagerImpl;
import hotelfx.view.feature_unit.unit.UnitController;
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
public class FeatureUnitController implements Initializable {

    private final StringProperty selectedSubFeature = new SimpleStringProperty("");
    
    @FXML
    private AnchorPane subFeatureContent;

    private void setSubFeatureContent(String subFeature) {
        try {
            //loader data (path) & set controller
            FXMLLoader loader = new FXMLLoader();
            switch (subFeature) {
                case "unit":
                    loader.setLocation(HotelFX.class.getResource("view/feature_unit/unit/UnitView.fxml"));

                    UnitController unitController = new UnitController(this);
                    loader.setController(unitController);
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

            //set 'sub feature content' into the center of 'feature_unit' contetnt.
            subFeatureContent.getChildren().clear();
            subFeatureContent.getChildren().add(subContent);

        } catch (Exception e) {
            System.out.println("err >> " + e.getMessage());
        }
    }

    /**
     * Unit
     */
    @FXML
    private JFXButton btnShowUnit;

    @FXML
    private AnchorPane btnShowUnitLayout;

    private void setButtonDataShow() {
        subFeatureContent.getStyleClass().add("sub-feature-layout");
        btnShowUnitLayout.getStyleClass().add("sub-feature-layout");
        
        setButtonDataPseudoClass(false, false);
        
        btnShowUnit.setOnMouseClicked((e) -> {
            if (!ClassSession.unSavingDataInput.get()) {
                setButtonDataPseudoClass(true, true);
                
                selectedSubFeature.set("unit");
            } else {  //unsaving data input
                Alert alert = ClassMessage.showConfirmationUnSavingDataInputMessage("", null);
                if (alert.getResult() == ButtonType.OK) {
                    ClassSession.unSavingDataInput.set(false);
                    
                    setButtonDataPseudoClass(true, true);
                    
                    selectedSubFeature.set("unit");
                }
            }
        });
    }
    
    private void setButtonDataPseudoClass(boolean sfc, boolean unit) {
        subFeatureContent.pseudoClassStateChanged(ClassPseudoClassCSS.selectedSubFeaturePseudoClass, sfc);
        btnShowUnitLayout.pseudoClassStateChanged(ClassPseudoClassCSS.selectedSubFeaturePseudoClass, unit);
    }
    
    /**
     * SERVICE (FOR ACCESS TO DATABASE)
     */
    private FUnitManager fUnitManager;
    
    /**
     * Initializes the controller class.
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        //set service manager
        fUnitManager = new FUnitManagerImpl();

        //set button event
        setButtonDataShow();

        //set selected sub feature
        selectedSubFeature.addListener((obs, oldValue, newValue) -> {
            //set sub feature content
            setSubFeatureContent(newValue);
        });
    }    
    
    public void setFUnitManager(FUnitManager fUnitManager) {
        this.fUnitManager = fUnitManager;
    }
    
    public FUnitManager getFUnitManager() {
        return this.fUnitManager;
    }
    
}
