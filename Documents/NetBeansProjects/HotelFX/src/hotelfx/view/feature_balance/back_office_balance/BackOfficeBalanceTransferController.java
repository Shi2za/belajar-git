/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hotelfx.view.feature_balance.back_office_balance;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXCComboBoxTablePopup;
import com.jfoenix.controls.JFXRadioButton;
import com.jfoenix.controls.JFXTextField;
import hotelfx.HotelFX;
import hotelfx.helper.ClassFormatter;
import hotelfx.helper.ClassMessage;
import hotelfx.helper.ClassViewSetting;
import hotelfx.persistence.model.TblCompanyBalance;
import hotelfx.persistence.model.TblCompanyBalanceBankAccount;
import hotelfx.persistence.model.TblCompanyBalanceTransferReceived;
import hotelfx.persistence.model.TblEmployee;
import java.math.BigDecimal;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.AnchorPane;

/**
 *
 * @author Andreas
 */
public class BackOfficeBalanceTransferController implements Initializable{
    @FXML
    private JFXRadioButton rdCompanyBalance;
    
    @FXML
    private AnchorPane ancCompanyBalance;
    private JFXCComboBoxTablePopup<TblCompanyBalance>cbpCompanyBalance;
    
    @FXML
    private AnchorPane ancCompanyBalanceBankAccount;
    private JFXCComboBoxTablePopup<TblCompanyBalanceBankAccount>cbpCompanyBalanceBankAccount;
    
    @FXML
    private JFXRadioButton rdCashierBalance;
    
    @FXML
    private JFXTextField txtNominalTransfer;
    
    @FXML
    private AnchorPane ancSenderName;
    private JFXCComboBoxTablePopup<TblEmployee>cbpEmployee;
    
    private ToggleGroup groupBalance;
    
    @FXML
    private JFXButton btnTransfer;
    
    @FXML
    private JFXButton btnClose;
    
    private TblCompanyBalanceTransferReceived selectedData;
    
    private void initFormDataBackOfficeTransfer(){
      initDataPopupCompanyBalance();
      initDataPopUpEmployee();
      initDataPopUpCompanyBalanceBankAccount();
      
     /* groupBalance = new ToggleGroup();
      rdCompanyBalance.setToggleGroup(groupBalance);
      rdCashierBalance.setToggleGroup(groupBalance);
      
       rdCompanyBalance.selectedProperty().addListener((obs,oldVal,newVal)->{
           if(newVal){
             cbpCompanyBalanceBankAccount.setVisible(true);
             cbpCompanyBalanceBankAccount.valueProperty().set(null);
           }
           else{
             cbpCompanyBalanceBankAccount.setVisible(false);
           }
      });
      cbpCompanyBalanceBankAccount.setVisible(false); */
      
      cbpCompanyBalance.valueProperty().addListener((obs,oldVal,newVal)->{
           if(newVal.getIdbalance()==1){
             cbpCompanyBalanceBankAccount.setVisible(true);
           }
           else{
              cbpCompanyBalanceBankAccount.setVisible(false);
           }
      });
      
      cbpCompanyBalanceBankAccount.setVisible(false);
      btnClose.setOnAction((e)->{
         backOfficeController.dialogStage.close();
      });
      
      btnTransfer.setOnAction((e)->{
          backOfficeTransferSaveHandle();
      });
      
      initColorField();
      initNumericField();
    }
    
    private void initColorField(){
      ClassViewSetting.setImportantField(cbpCompanyBalance,cbpCompanyBalanceBankAccount,txtNominalTransfer,cbpEmployee);
    }
    
    private void initNumericField(){
      ClassFormatter.setToNumericField("BigDecimal",txtNominalTransfer);
    }
    private void initDataPopupCompanyBalance(){
       TableView<TblCompanyBalance>tblCompanyBalance = new TableView();
       TableColumn<TblCompanyBalance,String>companyBalanceName = new TableColumn("Kas");
       companyBalanceName.setCellValueFactory(cellData->cellData.getValue().balanceNameProperty());
       tblCompanyBalance.getColumns().add(companyBalanceName);
       ObservableList<TblCompanyBalance>companyBalanceItems = FXCollections.observableArrayList(loadAllDataCompanyBalance());
       cbpCompanyBalance = new JFXCComboBoxTablePopup(TblCompanyBalance.class,tblCompanyBalance,companyBalanceItems,"","Kas *",false,400,300);
       
       ancCompanyBalance.getChildren().clear();
       AnchorPane.setTopAnchor(cbpCompanyBalance,0.0);
       AnchorPane.setBottomAnchor(cbpCompanyBalance,0.0);
       AnchorPane.setLeftAnchor(cbpCompanyBalance,0.0);
       AnchorPane.setRightAnchor(cbpCompanyBalance,0.0);
       ancCompanyBalance.getChildren().add(cbpCompanyBalance);
    }
    
    private List<TblCompanyBalance> loadAllDataCompanyBalance(){
       List<TblCompanyBalance>list = backOfficeController.getService().getAllDataCompanyBalance();
       for(int i = 0; i<list.size();i++){
           if(list.get(i).getIdbalance()==2){
             list.remove(list.get(i));
           }
           
            if(list.get(i).getIdbalance()==4){
             list.remove(list.get(i));
           }
       }
       return list;
    }
    
    private void initDataPopUpCompanyBalanceBankAccount(){
       TableView<TblCompanyBalanceBankAccount>tblCompanyBalanceBankAccount = new TableView();
       TableColumn<TblCompanyBalanceBankAccount,String>bankName = new TableColumn("Bank");
       bankName.setMinWidth(140);
       bankName.setCellValueFactory((TableColumn.CellDataFeatures<TblCompanyBalanceBankAccount,String>param)
       ->Bindings.createStringBinding(()->param.getValue().getTblBankAccount().getTblBank().getBankName(),param.getValue().tblBankAccountProperty()));
       TableColumn<TblCompanyBalanceBankAccount,String>bankAccountCode = new TableColumn("No.Rekening");
       bankAccountCode.setMinWidth(120);
       bankAccountCode.setCellValueFactory((TableColumn.CellDataFeatures<TblCompanyBalanceBankAccount,String>param)
       ->Bindings.createStringBinding(()->param.getValue().getTblBankAccount().getCodeBankAccount(),param.getValue().tblBankAccountProperty()));
       TableColumn<TblCompanyBalanceBankAccount,String>holderName = new TableColumn("Pemegang Rekening");
       holderName.setMinWidth(140);
       holderName.setCellValueFactory((TableColumn.CellDataFeatures<TblCompanyBalanceBankAccount,String>param)
       ->Bindings.createStringBinding(()->param.getValue().getTblBankAccount().getBankAccountHolderName(),param.getValue().tblBankAccountProperty()));
       tblCompanyBalanceBankAccount.getColumns().addAll(bankAccountCode,holderName,bankName);
       ObservableList<TblCompanyBalanceBankAccount>listDataCompanyBalanceBankAccountItems = FXCollections.observableArrayList(loadAllDataCompanyBalanceBankAccount());
      cbpCompanyBalanceBankAccount = new JFXCComboBoxTablePopup(TblEmployee.class,tblCompanyBalanceBankAccount,listDataCompanyBalanceBankAccountItems,"","No. Rekening *",false,400,300);
       
       ancCompanyBalanceBankAccount.getChildren().clear();
       AnchorPane.setTopAnchor(cbpCompanyBalanceBankAccount,0.0);
       AnchorPane.setBottomAnchor(cbpCompanyBalanceBankAccount,0.0);
       AnchorPane.setLeftAnchor(cbpCompanyBalanceBankAccount,0.0);
       AnchorPane.setRightAnchor(cbpCompanyBalanceBankAccount,0.0);
       ancCompanyBalanceBankAccount.getChildren().add(cbpCompanyBalanceBankAccount);
    }
    
    private List<TblCompanyBalanceBankAccount>loadAllDataCompanyBalanceBankAccount(){
        List<TblCompanyBalanceBankAccount> list = backOfficeController.getService().getAllDataCompanyBalanceBankAccount((long) 1);  //hotel baalnce = '1'
         for (TblCompanyBalanceBankAccount data : list) {
            //set data company balance
            data.setTblCompanyBalance(backOfficeController.getService().getDataCompanyBalance());
            //set data bank account
            data.setTblBankAccount(backOfficeController.getService().getBankAccount(data.getTblBankAccount().getIdbankAccount()));
            //set data bank
            data.getTblBankAccount().setTblBank(backOfficeController.getService().getDataBank(data.getTblBankAccount().getTblBank().getIdbank()));
        }
       return list;
    }
    
     private void refreshDataPopup() {
        //Company Balance - Bank Account (destination)
        ObservableList<TblCompanyBalanceBankAccount> companyBalanceBankAccountDestinationItems = FXCollections.observableArrayList(loadAllDataCompanyBalanceBankAccount());
        cbpCompanyBalanceBankAccount.setItems(companyBalanceBankAccountDestinationItems);
        ObservableList<TblCompanyBalance>companyBalanceItems = FXCollections.observableArrayList(loadAllDataCompanyBalance());
        cbpCompanyBalance.setItems(companyBalanceItems);
    }
     
    private void initDataPopUpEmployee(){
       TableView<TblEmployee>tblEmployeeSender = new TableView();
       TableColumn<TblEmployee,String>employeeName = new TableColumn("Nama");
       employeeName.setMinWidth(140);
       employeeName.setCellValueFactory((TableColumn.CellDataFeatures<TblEmployee,String>param)
       ->Bindings.createStringBinding(()->param.getValue().getTblPeople().getFullName(),param.getValue().tblPeopleProperty()));
       TableColumn<TblEmployee,String>employeeCode = new TableColumn("Kode");
       employeeCode.setMinWidth(120);
       employeeCode.setCellValueFactory(cellData -> cellData.getValue().codeEmployeeProperty());
       TableColumn<TblEmployee,String>employeeJob = new TableColumn("Jabatan");
       employeeJob.setMinWidth(140);
       employeeJob.setCellValueFactory((TableColumn.CellDataFeatures<TblEmployee,String>param)
       ->Bindings.createStringBinding(()->param.getValue().getTblJob().getJobName(),param.getValue().tblJobProperty()));
       tblEmployeeSender.getColumns().addAll(employeeCode,employeeName,employeeJob);
       ObservableList<TblEmployee>listDataEmployeeItems = FXCollections.observableArrayList(loadAllDataEmployee());
       cbpEmployee = new JFXCComboBoxTablePopup(TblEmployee.class,tblEmployeeSender,listDataEmployeeItems,"","Pengirim",false,400,300);
       
       ancSenderName.getChildren().clear();
       AnchorPane.setTopAnchor(cbpEmployee,0.0);
       AnchorPane.setBottomAnchor(cbpEmployee,0.0);
       AnchorPane.setLeftAnchor(cbpEmployee,0.0);
       AnchorPane.setRightAnchor(cbpEmployee,0.0);
       ancSenderName.getChildren().add(cbpEmployee);
    }
    
    private List<TblEmployee>loadAllDataEmployee(){
      return backOfficeController.getService().getAllDataEmployee();
    }
    
    private void setSelectedDataToInputFormTransfer(){
      refreshDataPopup();
      /* rdCashierBalance.selectedProperty().addListener((obs,oldVal,newVal)->{
          if(newVal){
             selectedData.setTblCompanyBalanceByTblCompanyBalanceSender(backOfficeController.getService().getDataCashierBalance());
          }
       }); */
      
      cbpCompanyBalance.valueProperty().bindBidirectional(backOfficeController.selectedData.tblCompanyBalanceByIdcompanyBalanceReceivedProperty());
      cbpCompanyBalanceBankAccount.valueProperty().bindBidirectional(backOfficeController.selectedData.tblCompanyBalanceBankAccountByIdcbbankAccountReceivedProperty());
      
      cbpEmployee.valueProperty().bindBidirectional(backOfficeController.selectedData.tblEmployeeByTransferByProperty());
      Bindings.bindBidirectional(txtNominalTransfer.textProperty(),backOfficeController.selectedData.nominalProperty(),new ClassFormatter.CBigDecimalStringConverter());
    }
    
    int dataInputStatus;
    private void backOfficeTransferHandle(){
      //selectedData = new TblCompanyBalanceTransferReceived();
      backOfficeController.selectedData.setTblCompanyBalanceByIdcompanyBalanceSender(backOfficeController.selectedBalance);
      setSelectedDataToInputFormTransfer();
      dataInputStatus = 0;
    }
    
    private void backOfficeTransferSaveHandle(){
       if(checkDataInput()){
           Alert alert = HotelFX.showAlertConfirm(Alert.AlertType.CONFIRMATION, "KONFIRMASI", "Apakah anda yakin untuk melakukan transfer data?", "");
           if (alert.getResult() == ButtonType.OK) {
               TblCompanyBalanceTransferReceived dummySelectedData = new TblCompanyBalanceTransferReceived(backOfficeController.selectedData);
               switch(backOfficeController.dataInputStatus){
                   case 0 :
                       if(backOfficeController.getService().insertDataBalanceTransfer(dummySelectedData)){
                         ClassMessage.showSucceedInsertingDataMessage(null,backOfficeController.dialogStage);
                         backOfficeController.tblBackOfficeBalanceTransfer.getTableView().setItems(FXCollections.observableArrayList(backOfficeController.loadAllDataTransfer()));
                         backOfficeController.dialogStage.close();
                       }
                      else{
                         ClassMessage.showFailedInsertingDataMessage(backOfficeController.getService().getErrorMessage(),backOfficeController.dialogStage);
                        }
                   break;
                   case 1 :
                       if(backOfficeController.getService().updateDataBalanceTransfer(dummySelectedData)){
                          ClassMessage.showSucceedUpdatingDataMessage(null,backOfficeController.dialogStage);
                          backOfficeController.tblBackOfficeBalanceTransfer.getTableView().setItems(FXCollections.observableArrayList(backOfficeController.loadAllDataTransfer()));
                          backOfficeController.dialogStage.close();
                       }
                       else{
                         ClassMessage.showFailedUpdatingDataMessage(backOfficeController.getService().getErrorMessage(),backOfficeController.dialogStage);
                       }
                   break;
                }
           }
       }
       else{
         ClassMessage.showWarningInputDataMessage(errDataInput,backOfficeController.dialogStage);
       }
    }
    
    String errDataInput;
    private boolean checkDataInput(){
       boolean check = true;
       errDataInput = "";
       if(cbpCompanyBalance.getValue()==null){
         errDataInput+="Kas :"+ClassMessage.defaultErrorNullValueMessage+"\n";
         check = false;
       }
       else{
           if(cbpCompanyBalance.getValue().getIdbalance()==1){
               if(cbpCompanyBalanceBankAccount.getValue()==null){
                 errDataInput+="No Rekening :"+ClassMessage.defaultErrorNullValueMessage+"\n";
                 check = false;
               }
           }
           
       }
       
       
       if(txtNominalTransfer.getText()==null || 
          txtNominalTransfer.getText().equalsIgnoreCase("") || 
          txtNominalTransfer.getText().equalsIgnoreCase("-")){
          errDataInput+="Nominal (Transfer): "+ClassMessage.defaultErrorNullValueMessage+"\n";
          check = false; 
       }
       else{
          if (backOfficeController.selectedData.getNominal().compareTo(new BigDecimal("0")) < 1) {
                check = false;
                errDataInput += "Nominal (Transfer) : " + ClassMessage.defaultErrorZeroValueMessage + " \n";
            } else {
                if (backOfficeController.selectedData.getNominal().compareTo(backOfficeController.selectedBalance.getBalanceNominal()) == 1) {
                    check = false;
                    errDataInput += "Nominal (Transfer) : Tidak dapat lebih besar dari nominal 'Kas Back-Office'..!! \n";
                }
            }
       }
       
       if(cbpEmployee.getValue()==null){
          errDataInput+="Pengirim :"+ClassMessage.defaultErrorNullValueMessage+"\n";
          check = false;
       }
       
      return check;
    }
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
      initFormDataBackOfficeTransfer();
      backOfficeTransferHandle();
      //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    public BackOfficeBalanceTransferController(BackOfficeBalanceController backOfficeController){
       this.backOfficeController = backOfficeController;
    }
    
    private final BackOfficeBalanceController backOfficeController;
}
