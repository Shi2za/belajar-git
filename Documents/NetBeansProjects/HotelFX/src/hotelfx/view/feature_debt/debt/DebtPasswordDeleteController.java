/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hotelfx.view.feature_debt.debt;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextArea;
import hotelfx.HotelFX;
import hotelfx.helper.ClassDataPasswordDeleteDebt;
import hotelfx.helper.ClassMessage;
import hotelfx.helper.ClassViewSetting;
import hotelfx.persistence.model.SysPasswordDeleteDebt;
import hotelfx.persistence.model.TblCalendarEmployeeDebt;
import hotelfx.persistence.model.TblCompanyBalance;
import insidefx.undecorator.Undecorator;
import java.io.IOException;
import java.math.BigDecimal;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.layout.Region;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

/**
 *
 * @author Andreas
 */
public class DebtPasswordDeleteController implements Initializable{
    @FXML
    private JFXPasswordField txtPasswordDelete;
    @FXML
    private JFXTextArea txtPasswordDeleteNote;
    @FXML
    private JFXButton btnSave;
    @FXML
    private JFXButton btnCancel;
    @FXML
    private JFXButton btnSetting;
    
    public ClassDataPasswordDeleteDebt selectedDataPasswordDeleteDebt;
    
    private void initFormDebtPasswordDelete(){
      btnSave.setOnAction((e)->{
          debtPasswordDeleteSaveHandle();
      });
      
      btnCancel.setOnAction((e)->{
         debtPasswordDeleteCancelHandle();
      });
      
/*      btnSetting.setOnAction((e)->{
          showDebtPasswordDeleteSettingPasswordDialog();
      });*/
      
        initImportantFieldColor();
    }
    
    private void initImportantFieldColor() {
        ClassViewSetting.setImportantField(
                txtPasswordDelete);
    }
    
    private void setSelectedDataToInputForm(){
      txtPasswordDelete.textProperty().bindBidirectional(selectedDataPasswordDeleteDebt.getPasswordDeleteDebt().passwordValueProperty());
      txtPasswordDeleteNote.textProperty().bindBidirectional(selectedDataPasswordDeleteDebt.getEmployeeDebt().employeeCanceledNoteProperty());
// selectedDataPasswordDeleteDebt.getEmployeeDebt().getTblCompanyBalance().setBalanceNominal(totalCompanyBalance());
        // selectedDataPasswordDeleteDebt.getPasswordDeleteDebt().setValue(debtController.selectedData.getEmployeeDebtNominal());
    }
    
     private BigDecimal totalCompanyBalance(TblCompanyBalance companyBalance){
       return companyBalance.getBalanceNominal().add(selectedDataPasswordDeleteDebt.getEmployeeDebt().getEmployeeDebtNominal());
    }
    
    private void debtPasswordDeleteCreateHandle(){
       selectedDataPasswordDeleteDebt = new ClassDataPasswordDeleteDebt();
       selectedDataPasswordDeleteDebt.setPasswordDeleteDebt(new SysPasswordDeleteDebt());
       selectedDataPasswordDeleteDebt.setEmployeeDebt(debtController.selectedData);
       selectedDataPasswordDeleteDebt.getEmployeeDebt().setEmployeeDebtStatus("Batal");
       setSelectedDataToInputForm();
    } 
    
    private void debtPasswordDeleteSaveHandle(){
       if(checkDataInput()){
           selectedDataPasswordDeleteDebt.getEmployeeDebt().getTblCompanyBalance().setBalanceNominal(totalCompanyBalance(selectedDataPasswordDeleteDebt.getEmployeeDebt().getTblCompanyBalance()));
           Alert alertInput = ClassMessage.showConfirmationSavingDataMessage("",debtController.dialogStagePasswordDebt);
        if(alertInput.getResult()==ButtonType.OK){
          ClassDataPasswordDeleteDebt dummySelectedDataPassword = new ClassDataPasswordDeleteDebt(selectedDataPasswordDeleteDebt);
           TblCalendarEmployeeDebt dummySelectedDataDebt = new TblCalendarEmployeeDebt(debtController.selectedData);
           if(debtController.getService().deleteDataDebt(dummySelectedDataDebt,dummySelectedDataPassword)){
             debtController.dialogStagePasswordDebt.close();
             debtController.tblDataDebt.getTableView().setItems(debtController.loadAllDataDebt());
             debtController.dataDebtFormShowStatus.set(0);
             debtController.refeshDataFiltering();
           }
           else{
             ClassMessage.showFailedInsertingDataMessage(debtController.getService().getErrorMessage(),debtController.dialogStagePasswordDebt);
           }
        }
        else{
          debtController.dialogStagePasswordDebt.showingProperty();
        }
       }
       else{
          ClassMessage.showWarningInputDataMessage(errDataInput,debtController.dialogStagePasswordDebt);
       }
    }
    
    String errDataInput;
    private boolean checkDataInput(){
       boolean check = true;
       errDataInput = "";
       if(txtPasswordDelete.getText()==null || txtPasswordDelete.getText().equalsIgnoreCase("")){
         check = false;
         errDataInput += "Password:"+ClassMessage.defaultErrorNullValueMessage+"\n"; 
       }
       else{
         //listPassword = debtController.getService().getAllPasswordDeleteDebt(txtPasswordDelete.getText());
           System.out.println("input password : "+selectedDataPasswordDeleteDebt.getPasswordDeleteDebt().getPasswordValue());
           if(!debtController.getService().checkPassword(selectedDataPasswordDeleteDebt.getPasswordDeleteDebt().getPasswordValue())){
              check = false;
              errDataInput +="Password salah!! \n";
            }
       }
       return check;
    }
    
    private void debtPasswordDeleteCancelHandle(){
      debtController.dialogStagePasswordDebt.close();
      debtController.refeshDataFiltering();
    }
    
    public Stage dialogStageSettingPassword;
    private void showDebtPasswordDeleteSettingPasswordDialog(){
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(HotelFX.class.getResource("view/feature_debt/debt/SettingPassword.fxml"));
            
            DebtSettingPasswordController debtSettingPasswordController = new DebtSettingPasswordController(this);
            loader.setController(debtSettingPasswordController);
            
            Region page = loader.load();
            
            dialogStageSettingPassword = new Stage();
            dialogStageSettingPassword.initModality(Modality.WINDOW_MODAL);
            dialogStageSettingPassword.initOwner(debtController.dialogStagePasswordDebt);
            
            Undecorator undecorator = new Undecorator(dialogStageSettingPassword,page);
            undecorator.getStylesheets().add("/skin/undecorator.css");
            undecorator.getMenuButton().setVisible(false);
            undecorator.getMaximizeButton().setVisible(false);
            undecorator.getMinimizeButton().setVisible(false);
            undecorator.getCloseButton().setVisible(false);
            undecorator.getFullScreenButton().setVisible(false);
            
            Scene scene = new Scene(undecorator);
            scene.setFill(Color.TRANSPARENT);
            
            dialogStageSettingPassword.initStyle(StageStyle.TRANSPARENT);
            dialogStageSettingPassword.setScene(scene);
            dialogStageSettingPassword.setResizable(false);
            
            dialogStageSettingPassword.showAndWait();
        } catch (IOException ex) {
            Logger.getLogger(DebtPasswordDeleteController.class.getName()).log(Level.SEVERE, null, ex);
        }
       
    }
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
      initFormDebtPasswordDelete();
      debtPasswordDeleteCreateHandle();
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    public DebtPasswordDeleteController(DebtController parentController){
        debtController = parentController;
    }
    
    public final DebtController debtController;
}
