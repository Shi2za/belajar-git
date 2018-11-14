/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hotelfx.view.feature_schedule;

import com.jfoenix.controls.JFXButton;
import hotelfx.HotelFX;
import hotelfx.helper.ClassMessage;
import hotelfx.helper.ClassPseudoClassCSS;
import hotelfx.helper.ClassSession;
import hotelfx.persistence.service.FScheduleManager;
import hotelfx.persistence.service.FScheduleManagerImpl;
import hotelfx.view.feature_schedule.employee_schedule.EmployeeScheduleController;
import hotelfx.view.feature_schedule.schedule.ScheduleController;
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
public class FeatureScheduleController implements Initializable {

    private final StringProperty selectedSubFeature = new SimpleStringProperty("");

    @FXML
    private AnchorPane subFeatureContent;

    private void setSubFeatureContent(String subFeature) {
        try {
            FXMLLoader loader = new FXMLLoader();
            switch (subFeature) {
                case "Schedule":
                    loader.setLocation(HotelFX.class.getResource("view/feature_schedule/schedule/ScheduleView.fxml"));
                    ScheduleController scheduleController = new ScheduleController(this);
                    loader.setController(scheduleController);
                    break;
                case "EmployeeSchedule":
                    loader.setLocation(HotelFX.class.getResource("view/feature_schedule/employee_schedule/EmployeeScheduleView.fxml"));
                    EmployeeScheduleController employeeScheduleController = new EmployeeScheduleController(this);
                    loader.setController(employeeScheduleController);
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
            Logger.getLogger(FeatureScheduleController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @FXML
    private JFXButton btnShowSchedule;

    @FXML
    private AnchorPane btnShowScheduleLayout;

    @FXML
    private JFXButton btnShowEmployeeSchedule;

    @FXML
    private AnchorPane btnShowEmployeeScheduleLayout;

    private void setButtonDataShow() {
        subFeatureContent.getStyleClass().add("sub-feature-layout");
        btnShowScheduleLayout.getStyleClass().add("sub-feature-layout");
        btnShowEmployeeScheduleLayout.getStyleClass().add("sub-feature-layout");

        setButtonDataPseudoClass(false, false, false);

        btnShowSchedule.setOnMouseClicked((e) -> {
            if (!ClassSession.unSavingDataInput.get()) {
                setButtonDataPseudoClass(true, true, false);

                selectedSubFeature.set("Schedule");
            } else {  //unsaving data input
                Alert alert = ClassMessage.showConfirmationUnSavingDataInputMessage("", null);
                if (alert.getResult() == ButtonType.OK) {
                    ClassSession.unSavingDataInput.set(false);

                    setButtonDataPseudoClass(true, true, false);

                    selectedSubFeature.set("Schedule");
                }
            }
        });

        btnShowEmployeeSchedule.setText("Penjadwalan \n Karyawan");
        btnShowEmployeeSchedule.setOnMouseClicked((e) -> {
            if (!ClassSession.unSavingDataInput.get()) {
                setButtonDataPseudoClass(true, false, true);

                selectedSubFeature.set("EmployeeSchedule");
            } else {  //unsaving data input
                Alert alert = ClassMessage.showConfirmationUnSavingDataInputMessage("", null);
                if (alert.getResult() == ButtonType.OK) {
                    ClassSession.unSavingDataInput.set(false);

                    setButtonDataPseudoClass(true, false, true);

                    selectedSubFeature.set("EmployeeSchedule");
                }
            }
        });
    }

    private void setButtonDataPseudoClass(boolean sfc, boolean schedule, boolean employeeSchedule) {
        subFeatureContent.pseudoClassStateChanged(ClassPseudoClassCSS.selectedSubFeaturePseudoClass, sfc);
        btnShowScheduleLayout.pseudoClassStateChanged(ClassPseudoClassCSS.selectedSubFeaturePseudoClass, schedule);
                    btnShowEmployeeScheduleLayout.pseudoClassStateChanged(ClassPseudoClassCSS.selectedSubFeaturePseudoClass, employeeSchedule);
    }
    
    private FScheduleManager fScheduleManager;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        fScheduleManager = new FScheduleManagerImpl();
        setButtonDataShow();
        selectedSubFeature.addListener((obs, oldVal, newVal) -> {
            setSubFeatureContent(newVal);
        });
    }

    public FScheduleManager getScheduleManager() {
        return this.fScheduleManager;
    }
}
