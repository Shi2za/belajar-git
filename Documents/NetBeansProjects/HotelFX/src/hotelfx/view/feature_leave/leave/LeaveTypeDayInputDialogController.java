/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hotelfx.view.feature_leave.leave;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXRadioButton;
import hotelfx.HotelFX;
import hotelfx.helper.ClassViewSetting;
import hotelfx.view.feature_schedule.employee_schedule.EmployeeScheduleController;
import insidefx.undecorator.Undecorator;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.Region;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

/**
 *
 * @author Andreas
 */
public class LeaveTypeDayInputDialogController implements Initializable{
    @FXML
    JFXRadioButton rbOneDay;
    @FXML
    JFXRadioButton rbMoreOneDay;
    @FXML
    JFXButton btnNext;
    @FXML
    JFXButton btnCancel;
    
    ToggleGroup tggLeaveTypeDay;
    private void initForm(){
      btnNext.setOnAction((e)->{
        setSelectedForm();
        //showInputDialog()
        leaveController.dataLeaveCreateHandle();
        leaveController.dialogStage.close();
      });
      
      btnCancel.setOnAction((e)->{
        leaveController.dialogStage.close();
      });
      
        initImportantFieldColor();
      
      tggLeaveTypeDay = new ToggleGroup();
      rbOneDay.setToggleGroup(tggLeaveTypeDay);
      rbMoreOneDay.setToggleGroup(tggLeaveTypeDay);
      
      rbOneDay.setSelected(false);
      rbMoreOneDay.setSelected(false);
    }
    
    private void initImportantFieldColor() {
        ClassViewSetting.setImportantField(
                rbOneDay, 
                rbMoreOneDay);
    }
    
    //radio button handle
    private void setSelectedForm(){
       if(rbOneDay.isSelected()){
        leaveController.statusForm = "one day"; 
      }
       else{
         leaveController.statusForm = "one more day";
         
       }
    }
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
      initForm();
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    public LeaveTypeDayInputDialogController(LeaveController leaveController){
      this.leaveController = leaveController; 
    }
    
    private final LeaveController leaveController;
    
   
}
