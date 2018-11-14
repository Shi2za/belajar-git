/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hotelfx.view.feature_debt.debt;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXCComboBoxTablePopup;
import com.jfoenix.controls.JFXTextField;
import hotelfx.helper.ClassMessage;
import hotelfx.helper.ClassViewSetting;
import hotelfx.persistence.model.TblBank;
import hotelfx.persistence.model.TblBankAccount;
import hotelfx.persistence.model.TblEmployeeBankAccount;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.AnchorPane;

/**
 *
 * @author Andreas
 */
public class DebtBankAccountReceivedController implements Initializable{
    @FXML
    private JFXTextField txtCodeBankAccount;
    @FXML
    private JFXTextField txtHolderBankAccount;
    @FXML
    private AnchorPane ancCbpBank;
    private JFXCComboBoxTablePopup cbpBank;
    @FXML
    private JFXButton btnSave;
    @FXML
    private JFXButton btnCancel;
    
    private TblEmployeeBankAccount selectedData;
    
    private void initFormDataDebtBankAccount(){
      initDataPopup();
      
      btnCancel.setOnAction((e->{
         debtApprovedController.dialogStage.close();
      }));
      
      btnSave.setOnAction((e)->{
         debtBankAccountReceivedSaveHandle();
      });
      
      initFieldColour();
    }
    
    private void initFieldColour(){
       ClassViewSetting.setImportantField(cbpBank,txtCodeBankAccount,txtHolderBankAccount);
    }
    
    private void initDataPopup(){
      TableView<TblBank>tblBank = new TableView();
      TableColumn<TblBank,String>bankName = new TableColumn("Bank");
      bankName.setCellValueFactory(cellData -> cellData.getValue().bankNameProperty());
      tblBank.getColumns().addAll(bankName);
      ObservableList<TblBank>listBankItems = FXCollections.observableArrayList(loadAllDataBank());
      System.out.println("size bank : "+listBankItems.size());
      cbpBank = new JFXCComboBoxTablePopup(TblBank.class,tblBank,listBankItems,"","Bank *",true,400,300);
      cbpBank.setItems(listBankItems);
      
      ancCbpBank.getChildren().clear();
      AnchorPane.setTopAnchor(cbpBank,0.0);
      AnchorPane.setBottomAnchor(cbpBank,0.0);
      AnchorPane.setLeftAnchor(cbpBank,0.0);
      AnchorPane.setRightAnchor(cbpBank,0.0);
      ancCbpBank.getChildren().addAll(cbpBank);
    }
    
    private List<TblBank>loadAllDataBank(){
      return debtApprovedController.getService().getAllDataBank();
    }
    
    private void setSelectedDataToInputForm(){
      txtCodeBankAccount.textProperty().bindBidirectional(selectedData.getTblBankAccount().codeBankAccountProperty());
      txtHolderBankAccount.textProperty().bindBidirectional(selectedData.getTblBankAccount().bankAccountHolderNameProperty());  
      cbpBank.valueProperty().bindBidirectional(selectedData.getTblBankAccount().tblBankProperty());
    }
    
    private void debtBankAccountReceivedCreateHandle(){
      selectedData = new TblEmployeeBankAccount();
      selectedData.setTblEmployeeByIdemployee(debtApprovedController.selectedData.getTblEmployeeByIdemployee());
//      selectedData.setTblEmployeeByIdemployee(debtPaymentController.selectedDataPayment.getTblEmployeeByIdemployee());
      selectedData.setTblBankAccount(new TblBankAccount());
      setSelectedDataToInputForm();
    }
    
    private void debtBankAccountReceivedSaveHandle(){
      if(checkDataInput()){
           Alert alert = ClassMessage.showConfirmationSavingDataMessage("",debtApprovedController.dialogStage);
           TblEmployeeBankAccount dummySelectedData = new TblEmployeeBankAccount(selectedData);
           dummySelectedData.setTblBankAccount(dummySelectedData.getTblBankAccount());
           if(alert.getResult()==ButtonType.OK){
               if(debtApprovedController.getService().insertBankAccountSender(dummySelectedData)){
                 ClassMessage.showSucceedInsertingDataMessage(null,debtApprovedController.dialogStage);
                 debtApprovedController.cbpReceivedBankAccount.setItems(FXCollections.observableArrayList(debtApprovedController.loadAllDataEmployeeBankAccount(debtApprovedController.selectedData.getTblEmployeeByIdemployee())));
                 debtApprovedController.dialogStage.close();
               }
               else{
                 ClassMessage.showFailedInsertingDataMessage(debtApprovedController.getService().getErrorMessage(),debtApprovedController.dialogStage);
               }
           }
       }
      else{
        ClassMessage.showWarningInputDataMessage(errDataInput,debtApprovedController.dialogStage);
      }
    }
    
    private String errDataInput;
    private boolean checkDataInput(){
          errDataInput = "";
      boolean check = true;
      
       if(txtCodeBankAccount.getText()==null || txtCodeBankAccount.getText().equalsIgnoreCase("")){
         errDataInput+="No.Rekening : "+ClassMessage.defaultErrorNullValueMessage+"\n";
         check = false;
       }
       
       if(txtHolderBankAccount.getText()==null || txtHolderBankAccount.getText().equalsIgnoreCase("")){
         errDataInput+="Pemegang Rekening : "+ClassMessage.defaultErrorNullValueMessage+"\n";
         check = false;
       }
       
       if(cbpBank.getValue()==null){
         errDataInput+="Bank : "+ClassMessage.defaultErrorNullValueMessage+"\n";
         check = false;
       }
       return check;
    }
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
      initFormDataDebtBankAccount();
      debtBankAccountReceivedCreateHandle();
      //  throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    public DebtBankAccountReceivedController(DebtApprovedController debtApprovedController){
      this.debtApprovedController = debtApprovedController;
    }
    
    private final DebtApprovedController debtApprovedController;
}
