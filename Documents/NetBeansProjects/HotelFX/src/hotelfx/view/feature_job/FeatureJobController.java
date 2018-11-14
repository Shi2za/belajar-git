/*
 * To change this license header, choose License Headers in Project Properties
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hotelfx.view.feature_job;

import com.jfoenix.controls.JFXButton;
import hotelfx.HotelFX;
import hotelfx.helper.ClassMessage;
import hotelfx.helper.ClassPseudoClassCSS;
import hotelfx.helper.ClassSession;
import hotelfx.persistence.service.FJobManager;
import hotelfx.persistence.service.FJobManagerImpl;
import hotelfx.view.feature_job.job.JobController;
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
public class FeatureJobController implements Initializable{

    private final StringProperty selectedSubFeature = new SimpleStringProperty("");
    
    @FXML
    private AnchorPane subFeatureContent;
    
    private void setSubFeatureContent(String subFeature)
    {
        try {
            FXMLLoader loader = new FXMLLoader();
            
            switch(subFeature)
            {
                case "Job":
                    loader.setLocation(HotelFX.class.getResource("view/feature_job/job/JobView.fxml"));
                    JobController jobController = new JobController(this);
                    loader.setController(jobController);
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
            Logger.getLogger(FeatureJobController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    @FXML
    private JFXButton btnShowJob;
    
    @FXML
    private AnchorPane btnShowJobLayout;
    
    private void setButtonDataShow()
    {
        subFeatureContent.getStyleClass().add("sub-feature-layout");
        btnShowJobLayout.getStyleClass().add("sub-feature-layout");
        
        setButtonDataPseudoClass(false, false);
        
       btnShowJob.setOnMouseClicked((e)->{
           if (!ClassSession.unSavingDataInput.get()) {
                setButtonDataPseudoClass(true, true);
                
                selectedSubFeature.set("Job");
            } else {  //unsaving data input
                Alert alert = ClassMessage.showConfirmationUnSavingDataInputMessage("", null);
                if (alert.getResult() == ButtonType.OK) {
                    ClassSession.unSavingDataInput.set(false);
                    
                    setButtonDataPseudoClass(true, true);
                    
                    selectedSubFeature.set("Job");
                }
            }
       });
    }
    
    private void setButtonDataPseudoClass(boolean sfc, boolean job) {
        subFeatureContent.pseudoClassStateChanged(ClassPseudoClassCSS.selectedSubFeaturePseudoClass, sfc);
        btnShowJobLayout.pseudoClassStateChanged(ClassPseudoClassCSS.selectedSubFeaturePseudoClass, job);
    }
    
    private FJobManager fJobManager;
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        fJobManager = new FJobManagerImpl();
        
        setButtonDataShow();
        
        selectedSubFeature.addListener((obs,oldVal,newVal)->{
            //set sub feature content
            setSubFeatureContent(newVal);
        });
        
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    public FJobManager getFJobManager()
    {
        return this.fJobManager;
    }
}
