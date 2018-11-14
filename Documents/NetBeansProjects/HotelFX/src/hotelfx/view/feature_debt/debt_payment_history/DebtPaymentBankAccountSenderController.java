/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hotelfx.view.feature_debt.debt_payment_history;

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
public class DebtPaymentBankAccountSenderController implements Initializable{
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
    
    private void initFormDataPaymentBankAccount(){
      initDataPopup();
      
      btnCancel.setOnAction((e->{
         debtPaymentController.dialogStage.close();
      }));
      
      btnSave.setOnAction((e)->{
         debtPaymentBankAccountSenderSaveHandle();
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
      return debtPaymentController.getService().getAllDataBank();
    }
    
    private void setSelectedDataToInputForm(){
      txtCodeBankAccount.textProperty().bindBidirectional(selectedData.getTblBankAccount().codeBankAccountProperty());
      txtHolderBankAccount.textProperty().bindBidirectional(selectedData.getTblBankAccount().bankAccountHolderNameProperty());  
      cbpBank.valueProperty().bindBidirectional(selectedData.getTblBankAccount().tblBankProperty());
    }
    
    private void debtPaymentBankAccountSenderCreateHandle(){
      selectedData = new TblEmployeeBankAccount();
      selectedData.setTblEmployeeByIdemployee(debtPaymentController.selectedDataPayment.getTblEmployeeByIdemployee());
      selectedData.setTblBankAccount(new TblBankAccount());
      setSelectedDataToInputForm();
    }
    
    private void debtPaymentBankAccountSenderSaveHandle(){
      if(checkDataInput()){
           Alert alert = ClassMessage.showConfirmationSavingDataMessage("",debtPaymentController.dialogStage);
           TblEmployeeBankAccount dummySelectedData = new TblEmployeeBankAccount(selectedData);
           dummySelectedData.setTblBankAccount(dummySelectedData.getTblBankAccount());
           if(alert.getResult()==ButtonType.OK){
               if(debtPaymentController.getService().insertBankAccountSender(dummySelectedData)){
                 ClassMessage.showSucceedInsertingDataMessage(null,debtPaymentController.dialogStage);
                 debtPaymentController.cbpSenderBankAccount.setItems(FXCollections.observableArrayList(debtPaymentController.loadAllDataEmployeeBankAccount(debtPaymentController.selectedDataPayment.getTblEmployeeByIdemployee())));
                 debtPaymentController.dialogStage.close();
               }
               else{
                 ClassMessage.showFailedInsertingDataMessage(debtPaymentController.getService().getErrorMessage(),debtPaymentController.dialogStage);
               }
           }
       }
      else{
        ClassMessage.showWarningInputDataMessage(errDataInput,debtPaymentController.dialogStage);
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
      initFormDataPaymentBankAccount();
      debtPaymentBankAccountSenderCreateHandle();
      //  throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    public DebtPaymentBankAccountSenderController(DebtPaymentController debtPaymentController){
      this.debtPaymentController = debtPaymentController;
    }
    
    private final DebtPaymentController debtPaymentController;
}
