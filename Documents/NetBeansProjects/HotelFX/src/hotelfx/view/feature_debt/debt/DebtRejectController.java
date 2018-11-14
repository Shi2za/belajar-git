/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hotelfx.view.feature_debt.debt;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextArea;
import hotelfx.helper.ClassFormatter;
import hotelfx.helper.ClassMessage;
import hotelfx.helper.ClassViewSetting;
import hotelfx.persistence.model.TblCalendarEmployeeDebt;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;

/**
 *
 * @author Andreas
 */
public class DebtRejectController implements Initializable {
    
    @FXML
    private Label lblRejectStatus;
    @FXML
    private Label lblEmployee;
    @FXML
    private Label lblDebtNominal;
    @FXML
    private JFXTextArea txtRejectNote;
    @FXML
    private JFXButton btnSave;
    @FXML
    private JFXButton btnCancel;
    
    private TblCalendarEmployeeDebt selectedData;
    
    private void initFormDataDebtReject(){
       btnSave.setOnAction((e)->{
          dataDebtRejectSaveHandle(); 
       });
       
       btnCancel.setOnAction((e)->{
          dataDebtRejectCancelHandle(); 
       });
       initFieldColor();
    }
    
    private void initFieldColor(){
      ClassViewSetting.setImportantField(txtRejectNote);
    }
    
    private void setSelectedDataToInputForm(){
      lblEmployee.textProperty().setValue(selectedData.getTblEmployeeByIdemployee().getCodeEmployee()+"-"+selectedData.getTblEmployeeByIdemployee().getTblPeople().getFullName());
      lblDebtNominal.setText(String.valueOf(new DecimalFormat("#,###.00").format(selectedData.getEmployeeDebtNominal())));
      lblRejectStatus.textProperty().setValue(selectedData.getEmployeeDebtStatus());
      txtRejectNote.textProperty().bindBidirectional(selectedData.employeeRejectNoteProperty());
    }
    
    private void dataDebtRejectHandle(){
       selectedData = new TblCalendarEmployeeDebt(debtController.selectedData);
       selectedData.setEmployeeDebtStatus("Ditolak");
       setSelectedDataToInputForm();
    }
    
    private void dataDebtRejectSaveHandle(){
       if(checkDataInput()){
           Alert alert = ClassMessage.showConfirmationSavingDataMessage(null,debtController.dialogStageRejectDebt);
           if(alert.getResult()==ButtonType.OK){
               if(debtController.getService().rejectedDataDebt(selectedData)){
                 ClassMessage.showSucceedUpdatingDataMessage(null,debtController.dialogStageRejectDebt);
                 debtController.dialogStageRejectDebt.close();
                 debtController.tblDataDebt.getTableView().setItems(debtController.loadAllDataDebt());
                 debtController.dataDebtFormShowStatus.set(0);
               }
              else{
                 ClassMessage.showFailedUpdatingDataMessage(null,debtController.dialogStageRejectDebt);
               }
           }
       }
       else{
         ClassMessage.showWarningInputDataMessage(errDataInput,debtController.dialogStageRejectDebt);
       }
    }
    
    private void dataDebtRejectCancelHandle(){
       debtController.dialogStageRejectDebt.close();
    }
    
    String errDataInput;
    private boolean checkDataInput(){
       boolean check = true;
       errDataInput = "";
       if(txtRejectNote.getText()==null || txtRejectNote.getText().equals("")){
          check = false;
          errDataInput+="Alasan Ditolak : "+ClassMessage.defaultErrorNullValueMessage+"\n";
       }
       return check;
    }
            
    @Override
    public void initialize(URL location, ResourceBundle resources) {
       initFormDataDebtReject();
       dataDebtRejectHandle();
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    public DebtRejectController(DebtController debtController){
      this.debtController = debtController;
    }
    
    private final DebtController debtController;
}
