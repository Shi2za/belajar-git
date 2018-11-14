/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hotelfx.view.feature_employee.employee;

import com.jfoenix.controls.JFXButton;
import hotelfx.helper.ClassMessage;
import hotelfx.persistence.model.TblEmployee;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;

/**
 *
 * @author Andreas
 */
public class EmployeeWarningLetterStatusController implements Initializable{
    @FXML
    private Label lblEmployeeName;
    @FXML
    private Spinner<Integer> spnWarningLetter;
    @FXML
    private JFXButton btnSave;
    @FXML
    private JFXButton btnCancel;
    
    private void initFormEmployeeWarningLetterStatus(){
      spnWarningLetter.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0,3));
      
      //System.out.println("id employee :"+employeeWarningLetterController.selectedData.getTblEmployeeByIdemployee().getCodeEmployee());
      dataEmployeeWarningLetterStatusHandle();
      btnSave.setOnAction((e)->{
         dataEmployeeWarningLetterStatusSaveHandle();
      });
      
      btnCancel.setOnAction((e)->{
         dataEmployeeWarningLetterStatusCancelHandle();
      });
    }
    
    private void setSelectedDataToInputForm(){
      spnWarningLetter.getValueFactory().setValue(employeeWarningLetterController.selectedData.getTblEmployeeByIdemployee().getWarningLetterStatus());
      spnWarningLetter.valueProperty().addListener((obs,oldVal,newVal)->{
         employeeWarningLetterController.selectedData.getTblEmployeeByIdemployee().setWarningLetterStatus(newVal);
      });
    }
      
    private void dataEmployeeWarningLetterStatusHandle(){
     lblEmployeeName.setText(employeeWarningLetterController.selectedData.getTblEmployeeByIdemployee().getCodeEmployee()+
                              " - "+employeeWarningLetterController.selectedData.getTblEmployeeByIdemployee().getTblPeople().getFullName());
      setSelectedDataToInputForm();
        
      
    }
    
    private void dataEmployeeWarningLetterStatusSaveHandle(){
      TblEmployee dummySelected = new TblEmployee(employeeWarningLetterController.selectedData.getTblEmployeeByIdemployee());
       if(employeeWarningLetterController.employeeController.getService().updateDataEmployeeWarningLetterStatus(dummySelected)){
         ClassMessage.showSucceedUpdatingDataMessage(null,employeeWarningLetterController.dialogStage);
         if(employeeWarningLetterController.selectedData.getTblEmployeeByIdemployee().getWarningLetterStatus()==0){
            employeeWarningLetterController.strWarningLetterStatus = "SP -";
         }
         else{
            employeeWarningLetterController.strWarningLetterStatus = "SP "+ employeeWarningLetterController.selectedData.getTblEmployeeByIdemployee().getWarningLetterStatus();
         }
         employeeWarningLetterController.dialogStage.close();
       }
       else{
         ClassMessage.showFailedUpdatingDataMessage(employeeWarningLetterController.employeeController.getService().getErrorMessage(),employeeWarningLetterController.dialogStage);
       }
    }
    
    private void dataEmployeeWarningLetterStatusCancelHandle(){
       employeeWarningLetterController.dialogStage.close();
    }
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
      initFormEmployeeWarningLetterStatus();
      
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    public EmployeeWarningLetterStatusController(EmployeeWarningLetterController employeeWarningLetterController){
      this.employeeWarningLetterController = employeeWarningLetterController;
    }
    
    private final EmployeeWarningLetterController employeeWarningLetterController;
}
