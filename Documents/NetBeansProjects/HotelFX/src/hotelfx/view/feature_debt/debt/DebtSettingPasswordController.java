/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hotelfx.view.feature_debt.debt;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXPasswordField;
import hotelfx.helper.ClassMessage;
import hotelfx.helper.ClassViewSetting;
import hotelfx.persistence.model.SysPasswordDeleteDebt;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

/**
 *
 * @author Andreas
 */
public class DebtSettingPasswordController implements Initializable {
    
    @FXML
    private JFXPasswordField txtOldPassword;
    @FXML
    private JFXPasswordField txtNewPassword;
    @FXML
    private JFXButton btnSave;
    @FXML
    private JFXButton btnCancel;
    
    private SysPasswordDeleteDebt selectedDataSettingNewPassword;
    
    private SysPasswordDeleteDebt selectedDataSettingOldPassword;
    
    private void initFormDebtSettingPassword(){
       btnSave.setOnAction((e)->{
         debtSettingPasswordSaveHandle();
       });
       
       btnCancel.setOnAction((e)->{
          debtSettingPasswordCancelHandle();
       });
       
        initImportantFieldColor();
    }
    
    private void initImportantFieldColor() {
        ClassViewSetting.setImportantField(
                txtOldPassword, 
                txtNewPassword);
    }
    
    private void setSelectedDataToInputForm(){
       txtOldPassword.textProperty().bindBidirectional(selectedDataSettingOldPassword.passwordValueProperty());
       txtNewPassword.textProperty().bindBidirectional(selectedDataSettingNewPassword.passwordValueProperty());
    }
    
    private void debtSettingPasswordUpdateHandle(){
       //debtPasswordController.selectedDataPasswordDeleteDebt.setPasswordDeleteDebt(new SysPasswordDeleteDebt());
       selectedDataSettingOldPassword = new SysPasswordDeleteDebt();
       selectedDataSettingNewPassword = new SysPasswordDeleteDebt();
       //selectedDataSettingNewPassword.setPasswordValue(new String());
       setSelectedDataToInputForm();
    }
    
    private void debtSettingPasswordSaveHandle(){
        if(checkDataInputSettingPassword()){
            Alert alert = ClassMessage.showConfirmationSavingDataMessage("", debtPasswordController.dialogStageSettingPassword);
            if (alert.getResult() == ButtonType.OK) {
            //System.out.println("password:"+selectedDataSettingOldPassword.getPasswordValue());
            //System.out.println("password:"+debtPasswordController.selectedDataPasswordDeleteDebt.getPasswordDeleteDebt().getPasswordValue());
            //List<SysPasswordDeleteDebt>list = debtPasswordController.debtController.getService().getAllPasswordDeleteDebt()
           SysPasswordDeleteDebt dummySelectedNewPassword = new SysPasswordDeleteDebt(selectedDataSettingNewPassword);
           SysPasswordDeleteDebt dummySelectedOldPassword = new SysPasswordDeleteDebt(selectedDataSettingOldPassword);
           if(debtPasswordController.debtController.getService().updateSettingPassword(dummySelectedOldPassword,dummySelectedNewPassword)){
             ClassMessage.showSucceedUpdatingDataMessage(null,debtPasswordController.dialogStageSettingPassword);
             debtPasswordController.dialogStageSettingPassword.close();
            }
           else{
              ClassMessage.showFailedDeletingDataMessage(debtPasswordController.debtController.getService().getErrorMessage(),debtPasswordController.dialogStageSettingPassword);
            }
            }
        }
        else{
          ClassMessage.showWarningInputDataMessage(errDataInput,debtPasswordController.dialogStageSettingPassword);
        }
    }
    
    private String errDataInput;
    private boolean checkDataInputSettingPassword(){
       // System.out.println("password lama :"+selectedDataSettingOldPassword.getPasswordValue());
        boolean check = true;
        errDataInput = "";
       if(txtOldPassword.getText()==null || txtOldPassword.getText().equalsIgnoreCase("")){
          check = false;
          errDataInput+="Password Lama:"+ClassMessage.defaultErrorNullValueMessage+"\n";
       }
       else{
           //List<SysPasswordDeleteDebt>listOldPassword = debtPasswordController.debtController.getService().getAllPasswordDeleteDebt(selectedDataSettingOld);
           if(!debtPasswordController.debtController.getService().checkPassword(selectedDataSettingOldPassword.getPasswordValue())){
             check = false;
             errDataInput+="Password Lama : password tidak cocok !! \n";
           }
       }
       
       if(txtNewPassword.getText()==null || txtNewPassword.getText().equalsIgnoreCase("")){
          check = false;
          errDataInput+="Password Baru:"+ClassMessage.defaultErrorNullValueMessage+"\n";
       }
       
       if((txtOldPassword.getText()!=null || txtOldPassword.getText().equalsIgnoreCase("")) && (txtNewPassword.getText()!=null || txtNewPassword.getText().equalsIgnoreCase(""))){
           if(txtOldPassword.getText().equalsIgnoreCase(txtNewPassword.getText())){
             check = false;
             errDataInput+="Password sama !! \n";
            }
       }
       
       return check;
    }
    
    private void debtSettingPasswordCancelHandle(){
       debtPasswordController.dialogStageSettingPassword.close();
    }
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initFormDebtSettingPassword();
        debtSettingPasswordUpdateHandle();
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    public DebtSettingPasswordController(DebtPasswordDeleteController parentController){
        debtPasswordController = parentController;
    }
    private final DebtPasswordDeleteController debtPasswordController;
}
