/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hotelfx.view.feature_balance.cashier_balance;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXCComboBoxTablePopup;
import com.jfoenix.controls.JFXTextField;
import hotelfx.HotelFX;
import hotelfx.helper.ClassFormatter;
import hotelfx.helper.ClassMessage;
import hotelfx.persistence.model.TblCompanyBalanceTransferReceived;
import hotelfx.persistence.model.TblEmployee;
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
import javafx.scene.layout.AnchorPane;

/**
 *
 * @author Andreas
 */
public class CashierBalanceTransferController implements Initializable{
    @FXML
    private AnchorPane ancCbpEmployee;
    private JFXCComboBoxTablePopup cbpEmployee;
    @FXML
    private JFXTextField txtTransferNominal;
    @FXML
    private JFXButton btnClose;
    @FXML
    private JFXButton btnTransfer;
    
     private void initFormCashierBalanceTransfer(){
      initDataPopup();
      
      btnTransfer.setOnAction((e)->{
          cashierBalanceTransferSaveHandle();
      });
      
      btnClose.setOnAction((e)->{
         cashierBalanceController.dialogStageCashierBalanceTransfer.close();
      });
    }
     
    private void initDataPopup(){
       TableView<TblEmployee>tblEmployee = new TableView();
       TableColumn<TblEmployee,String>employeeCode = new TableColumn("ID");
       employeeCode.setMinWidth(90);
       employeeCode.setCellValueFactory(cellData->cellData.getValue().codeEmployeeProperty());
       TableColumn<TblEmployee,String>employeeName = new TableColumn("Karyawan");
       employeeName.setMinWidth(160);
       employeeName.setCellValueFactory((TableColumn.CellDataFeatures<TblEmployee,String>param)
       ->Bindings.createStringBinding(()->param.getValue().getTblPeople().getFullName(),param.getValue().tblPeopleProperty()));
       TableColumn<TblEmployee,String>employeeJob = new TableColumn("Jabatan");
       employeeJob.setCellValueFactory((TableColumn.CellDataFeatures<TblEmployee,String>param)
       ->Bindings.createStringBinding(()->param.getValue().getTblJob().getJobName(),param.getValue().tblJobProperty()));
       tblEmployee.getColumns().addAll(employeeCode,employeeName,employeeJob);
       ObservableList<TblEmployee>listEmployeeItems = FXCollections.observableArrayList(loadAllDataEmployee());
       cbpEmployee = new JFXCComboBoxTablePopup(TblEmployee.class,tblEmployee,listEmployeeItems,"","Pengirim *",false,400,300);
       
       ancCbpEmployee.getChildren().clear();
       AnchorPane.setTopAnchor(cbpEmployee,0.0);
       AnchorPane.setBottomAnchor(cbpEmployee,0.0);
       AnchorPane.setLeftAnchor(cbpEmployee,0.0);
       AnchorPane.setRightAnchor(cbpEmployee,0.0);
       ancCbpEmployee.getChildren().add(cbpEmployee);
   }
    
    private List<TblEmployee>loadAllDataEmployee(){
      return cashierBalanceController.getService().getAllDataEmployee();
    }
    
    private void setSelectedDataToInputForm(){
      cbpEmployee.valueProperty().bindBidirectional(cashierBalanceController.selectedData.tblEmployeeByTransferByProperty());
      Bindings.bindBidirectional(txtTransferNominal.textProperty(),cashierBalanceController.selectedData.nominalProperty(),new ClassFormatter.CBigDecimalStringConverter());
    }
    
    private void cashierBalanceTransferHandle(){
       setSelectedDataToInputForm();
    }
    
    private void cashierBalanceTransferSaveHandle(){
       if(checkDataInput()){
           Alert alert = HotelFX.showAlertConfirm(Alert.AlertType.CONFIRMATION, "KONFIRMASI", "Apakah anda yakin untuk melakukan transfer data ?", "");
           if(alert.getResult()==ButtonType.OK){
              TblCompanyBalanceTransferReceived dummySelected = new TblCompanyBalanceTransferReceived(cashierBalanceController.selectedData);
              switch(cashierBalanceController.dataInputStatus){
                  case 0 :
                       if(cashierBalanceController.getService().insertDataBalanceTransfer(dummySelected)){
                         ClassMessage.showSucceedInsertingDataMessage(null,cashierBalanceController.dialogStageCashierBalanceTransfer);
                         cashierBalanceController.tblCashierBalanceTransfer.getTableView().setItems(FXCollections.observableArrayList(cashierBalanceController.loadAllDataCompanyBalanceTransfer()));
                         cashierBalanceController.dialogStageCashierBalanceTransfer.close();
                       }
                       else{
                          ClassMessage.showFailedInsertingDataMessage(cashierBalanceController.getService().getErrorMessage(),cashierBalanceController.dialogStageCashierBalanceTransfer);
                        }
                  break;
                  
                  case 1 :
                       if(cashierBalanceController.getService().updateDataBalanceTransfer(dummySelected)){
                          ClassMessage.showSucceedInsertingDataMessage(null,cashierBalanceController.dialogStageCashierBalanceTransfer);
                          cashierBalanceController.tblCashierBalanceTransfer.getTableView().setItems(FXCollections.observableArrayList(cashierBalanceController.dialogStageCashierBalanceTransfer));
                          cashierBalanceController.dialogStageCashierBalanceTransfer.close();
                       }
                       else{
                          ClassMessage.showFailedInsertingDataMessage(cashierBalanceController.getService().getErrorMessage(),cashierBalanceController.dialogStageCashierBalanceTransfer);
                        }
                  break;
                }
             
           }
       }
    }
    
    String errDataInput;
    private boolean checkDataInput(){
       boolean check = true;
       errDataInput = "";
       if(txtTransferNominal.getText()==null || txtTransferNominal.equals("")){
          errDataInput+="Nominal Transfer : "+ClassMessage.defaultErrorNullValueMessage+"\n";
          check = false;
        }
       
       if(cbpEmployee.getValue()==null){
          errDataInput+="Pengirim : "+ClassMessage.defaultErrorNullValueMessage+"\n";
          check = false;
       }
      return check;
    }
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
      initFormCashierBalanceTransfer();
      cashierBalanceTransferHandle();
      //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    public CashierBalanceTransferController(CashierBalanceController cashierBalanceController){
      this.cashierBalanceController = cashierBalanceController;
    }
    private final CashierBalanceController cashierBalanceController;
}
