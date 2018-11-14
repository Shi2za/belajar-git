/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hotelfx.view.feature_employee;

import com.jfoenix.controls.JFXButton;
import hotelfx.HotelFX;
import hotelfx.helper.ClassMessage;
import hotelfx.helper.ClassPseudoClassCSS;
import hotelfx.helper.ClassSession;
import hotelfx.persistence.service.FEmployeeManager;
import hotelfx.persistence.service.FEmployeeManagerImpl;
import hotelfx.view.feature_employee.employee.EmployeeController;
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
public class FeatureEmployeeController implements Initializable {

    private final StringProperty selectedSubFeature = new SimpleStringProperty("");
    @FXML
    private AnchorPane subFeatureContent;

    private void setSelectedSubFeature(String feature) {
        try {
            FXMLLoader loader = new FXMLLoader();
            switch (feature) {

                case "employee":
                    loader.setLocation(HotelFX.class.getResource("view/feature_employee/employee/EmployeeView.fxml"));
                    EmployeeController employeeController = new EmployeeController(this);
                    loader.setController(employeeController);
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
            Logger.getLogger(FeatureEmployeeController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @FXML
    private JFXButton btnShowEmployee;

    @FXML
    private AnchorPane btnShowEmployeeLayout;

    public void setButtonDataShow() {
        subFeatureContent.getStyleClass().add("sub-feature-layout");
        btnShowEmployeeLayout.getStyleClass().add("sub-feature-layout");

        subFeatureContent.pseudoClassStateChanged(ClassPseudoClassCSS.selectedSubFeaturePseudoClass, false);
        btnShowEmployeeLayout.pseudoClassStateChanged(ClassPseudoClassCSS.selectedSubFeaturePseudoClass, false);

        btnShowEmployee.setOnMouseClicked((e) -> {
            if (!ClassSession.unSavingDataInput.get()) {
                btnShowEmployeeLayout.pseudoClassStateChanged(ClassPseudoClassCSS.selectedSubFeaturePseudoClass, true);
                
                selectedSubFeature.set("employee");
                subFeatureContent.pseudoClassStateChanged(ClassPseudoClassCSS.selectedSubFeaturePseudoClass, true);
            } else {  //unsaving data input
                Alert alert = ClassMessage.showConfirmationUnSavingDataInputMessage("", null);
                if (alert.getResult() == ButtonType.OK) {
                    ClassSession.unSavingDataInput.set(false);
                    
                    btnShowEmployeeLayout.pseudoClassStateChanged(ClassPseudoClassCSS.selectedSubFeaturePseudoClass, true);
                    
                    selectedSubFeature.set("employee");
                    subFeatureContent.pseudoClassStateChanged(ClassPseudoClassCSS.selectedSubFeaturePseudoClass, true);
                }
            }
        });
    }

    private FEmployeeManager fEmployeeManager;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        fEmployeeManager = new FEmployeeManagerImpl();

        setButtonDataShow();

        selectedSubFeature.addListener((obs, oldVal, newVal) -> {
            setSelectedSubFeature(newVal);
        });
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public FEmployeeManager getFEmployeeManager() {
        return this.fEmployeeManager;
    }

}
