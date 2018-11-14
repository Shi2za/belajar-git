/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hotelfx.view.feature_attendance;

import com.jfoenix.controls.JFXButton;
import hotelfx.HotelFX;
import hotelfx.helper.ClassMessage;
import hotelfx.helper.ClassPseudoClassCSS;
import hotelfx.helper.ClassSession;
import hotelfx.persistence.service.FAttendanceManager;
import hotelfx.persistence.service.FAttendanceManagerImpl;
import hotelfx.view.feature_attendance.attendance.AttendanceController;
import hotelfx.view.feature_attendance.attendance_info.AttendanceInfoController;
import hotelfx.view.feature_schedule.FeatureScheduleController;
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
 * FXML Controller class
 *
 * @author ANDRI
 */
public class FeatureAttendanceController implements Initializable {

    private final StringProperty selectedSubFeature = new SimpleStringProperty("");

    @FXML
    private AnchorPane subFeatureContent;

    private void setSubFeatureContent(String subFeature) {
        try {
            //loader data (path) & set controller
            FXMLLoader loader = new FXMLLoader();
            switch (subFeature) {
                case "attendance":
                    loader.setLocation(HotelFX.class.getResource("view/feature_attendance/attendance/AttendanceView.fxml"));

                    AttendanceController attendanceController = new AttendanceController(this);
                    loader.setController(attendanceController);
                    break;

                case "employee_attendance":
                    loader.setLocation(HotelFX.class.getResource("view/feature_attendance/attendance_info/AttendanceInfoView.fxml"));

                    AttendanceInfoController attendanceInfoController = new AttendanceInfoController(this);
                    loader.setController(attendanceInfoController);
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

        } catch (IOException ex) {
            Logger.getLogger(FeatureScheduleController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Attendance
     */
    @FXML
    private JFXButton btnShowAttendance;

    @FXML
    private AnchorPane btnShowAttendanceLayout;

    @FXML
    private JFXButton btnShowAttendanceInfo;
    @FXML
    private AnchorPane btnShowAttendanceInfoLayout;

    private void setButtonDataShow() {
        subFeatureContent.getStyleClass().add("sub-feature-layout");
        btnShowAttendanceLayout.getStyleClass().add("sub-feature-layout");
        btnShowAttendanceInfoLayout.getStyleClass().add("sub-feature-layout");

        setButtonDataPseudoClass(false, false, false);

        btnShowAttendance.setOnMouseClicked((e) -> {
            if (!ClassSession.unSavingDataInput.get()) {
                setButtonDataPseudoClass(true, true, false);

                selectedSubFeature.set("attendance");
            } else {  //unsaving data input
                Alert alert = ClassMessage.showConfirmationUnSavingDataInputMessage("", null);
                if (alert.getResult() == ButtonType.OK) {
                    ClassSession.unSavingDataInput.set(false);

                    setButtonDataPseudoClass(true, true, false);

                    selectedSubFeature.set("attendance");
                }
            }
        });

        btnShowAttendanceInfo.setOnMouseClicked((e) -> {
            if (!ClassSession.unSavingDataInput.get()) {
                setButtonDataPseudoClass(true, false, true);

                selectedSubFeature.set("employee_attendance");
            } else {  //unsaving data input
                Alert alert = ClassMessage.showConfirmationUnSavingDataInputMessage("", null);
                if (alert.getResult() == ButtonType.OK) {
                    ClassSession.unSavingDataInput.set(false);

                    setButtonDataPseudoClass(true, false, true);

                    selectedSubFeature.set("employee_attendance");
                }
            }
        });
    }

    private void setButtonDataPseudoClass(boolean sfc, boolean attendance, boolean employeeAttendance) {
        subFeatureContent.pseudoClassStateChanged(ClassPseudoClassCSS.selectedSubFeaturePseudoClass, sfc);
        btnShowAttendanceLayout.pseudoClassStateChanged(ClassPseudoClassCSS.selectedSubFeaturePseudoClass, attendance);
        btnShowAttendanceInfoLayout.pseudoClassStateChanged(ClassPseudoClassCSS.selectedSubFeaturePseudoClass, employeeAttendance);
    }

    /**
     * SERVICE (FOR ACCESS TO DATABASE)
     */
    private FAttendanceManager fAttendanceManager;

    /**
     * Initializes the controller class.
     *
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        //set service manager
        fAttendanceManager = new FAttendanceManagerImpl();

        //set button event
        setButtonDataShow();

        //set selected sub feature
        selectedSubFeature.addListener((obs, oldValue, newValue) -> {
            //set sub feature content
            setSubFeatureContent(newValue);
        });
    }

    public FAttendanceManager getFAttendanceManager() {
        return this.fAttendanceManager;
    }

}
