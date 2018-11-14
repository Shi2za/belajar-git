/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hotelfx.view.feature_debt.debt;

import com.jfoenix.controls.JFXButton;

import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXTextArea;
import com.jfoenix.controls.JFXTextField;
import hotelfx.helper.ClassFormatter;
import hotelfx.helper.ClassMessage;
import hotelfx.helper.ClassViewSetting;
import hotelfx.persistence.model.TblCalendarEmployeePaymentDebtHistory;
import hotelfx.persistence.model.TblCompanyBalance;
import java.math.BigDecimal;
import java.net.URL;
import java.sql.Date;
import java.util.List;
import java.util.ResourceBundle;
import javafx.beans.binding.Bindings;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.css.PseudoClass;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;

/**
 *
 * @author Andreas
 */
public class DebtPaymentController implements Initializable{
    
    @FXML
    private AnchorPane formAnchor;
    @FXML
    private ScrollPane spFormDataDebtPayment;
    @FXML
    private GridPane gpFormDataDebtPayment;
    @FXML
    private Label lblEmployee;
    @FXML
    private JFXTextField txtNominalEmployeeDebt;
    @FXML
    private JFXTextField txtNominalRevenueEmployeeDebt;
    @FXML
    private JFXTextField txtCompanyBalanceEmployeeDebt;
    @FXML
    private JFXTextField txtCompanyBankAccountEmployeeDebt;
    @FXML
    private Label lblStatusPaymentDebt;
    @FXML
    private JFXDatePicker dpDatePaymentDebt;
    @FXML
    private JFXTextField txtNominalPaymentDebt;
    @FXML
    private JFXTextArea txtPaymentDebtNote;
    @FXML
    private JFXButton btnCancel;
    @FXML
    private JFXButton btnSave;
    
    private int inputDataStatus;
    
    private BigDecimal nominalEmployeePaymentDebt = new BigDecimal("0");
    private BigDecimal nominalRevenueEmployeeDebt = new BigDecimal("0");
    private BigDecimal totalDebt;
    private final PseudoClass onScrollPseudoClass = PseudoClass.getPseudoClass("onScroll");
    private final BooleanProperty isFormScroll = new SimpleBooleanProperty(false);
    private int scrollCounter = 0;
    private TblCalendarEmployeePaymentDebtHistory selectedDataPayment;
    private void initFormDataDebtPayment(){
      /* isFormScroll.addListener((obs,oldScroll,newScroll)->{
         spFormDataDebtPayment.pseudoClassStateChanged(onScrollPseudoClass,newScroll);
       });
       
       gpFormDataDebtPayment.setOnScroll((ScrollEvent scroll)->{
          isFormScroll.set(true);
          scrollCounter++;
          
          Thread thread = new Thread(()->{
               try{
                   Thread.sleep(1000);
                   Platform.runLater(()->{
                       if(scrollCounter == 1){
                          isFormScroll.set(false);
                        }
                      scrollCounter--;
                    });
                } catch (Exception e) { 
                 System.out.println("err>>"+e.getLocalizedMessage()); 
                // Logger.getLogger(DebtPaymentController.class.getName()).log(Level.SEVERE, null, ex);
              } 
          });
          thread.setDaemon(true);
          thread.start();
       });*/
       
       btnSave.setOnAction((e)->{
          // Alert alertInput = ClassMessage.showConfirmationSavingDataMessage(null,debtController.dialogStagePayment);
          // if(alertInput.getResult()==ButtonType.OK){
             dataDebtPaymentSaveHandle();
       });
       
       btnCancel.setOnAction((e)->{
          dataDebtPaymentControllerCancelHandle();
       });
       
        initCalendarDate();
        initImportantFieldColor();
        initNumbericField();
    }
    
     private void initCalendarDate(){
        ClassFormatter.setDatePickersPattern("dd MMM yyyy",dpDatePaymentDebt);
    }
    private void initImportantFieldColor() {
        ClassViewSetting.setImportantField(
                dpDatePaymentDebt, 
                txtNominalPaymentDebt);
    }
    
    private void initNumbericField() {
        ClassFormatter.setToNumericField(
                "BigDecimal",
                txtNominalEmployeeDebt,
                txtNominalPaymentDebt);
    }
    
    private void selectedDataToInputForm(){
        lblEmployee.textProperty().setValue(selectedDataPayment.getTblCalendarEmployeeDebt().getTblEmployeeByIdemployee().getCodeEmployee()+"-"+selectedDataPayment.getTblCalendarEmployeeDebt().getTblEmployeeByIdemployee().getTblPeople().getFullName());
        Bindings.bindBidirectional(txtNominalEmployeeDebt.textProperty(),selectedDataPayment.getTblCalendarEmployeeDebt().employeeDebtNominalProperty(),new ClassFormatter.CBigDecimalStringConverter());
        List<TblCalendarEmployeePaymentDebtHistory>list = debtController.getService().getAllEmployeePaymentDebtByIdEmployeeDebt(selectedDataPayment.getTblCalendarEmployeeDebt());
        
        nominalEmployeePaymentDebt = totalEmployeePaymentDebt(list);
        nominalRevenueEmployeeDebt = selectedDataPayment.getTblCalendarEmployeeDebt().getEmployeeDebtNominal().subtract(nominalEmployeePaymentDebt);
        
        txtNominalRevenueEmployeeDebt.setText(String.valueOf(nominalRevenueEmployeeDebt));
        
        Bindings.bindBidirectional(txtNominalPaymentDebt.textProperty(),selectedDataPayment.employeePaymentDebtNominalProperty(),new ClassFormatter.CBigDecimalStringConverter());
        
        //System.out.println("pambayaran:"+selectedDataPayment.getEmployeePaymentDebtNominal());
        txtCompanyBalanceEmployeeDebt.textProperty().set(selectedDataPayment.getTblCalendarEmployeeDebt().getTblCompanyBalance().getBalanceName());
        //txtCompanyBalanceEmployeeDebt.textProperty().bindBidirectional(debtController.selectedData.getTblCompanyBalance().balanceNameProperty());
        if(debtController.selectedData.getTblCompanyBalanceBankAccount()!=null){
          txtCompanyBankAccountEmployeeDebt.textProperty().set(selectedDataPayment.getTblCalendarEmployeeDebt().getTblCompanyBalanceBankAccount().getTblBankAccount().getCodeBankAccount()+"-"+debtController.selectedData.getTblCompanyBalanceBankAccount().getTblBankAccount().getTblBank().getBankName()+"-"+debtController.selectedData.getTblCompanyBalanceBankAccount().getTblBankAccount().getBankAccountHolderName());
        }
        
        lblStatusPaymentDebt.textProperty().setValue(selectedDataPayment.getTblCalendarEmployeeDebt().getEmployeeDebtPaymentStatus());
        txtPaymentDebtNote.textProperty().bindBidirectional(selectedDataPayment.employeePaymentDebtNoteProperty());
        selectedDataPayment.setTblCompanyBalance(selectedDataPayment.getTblCalendarEmployeeDebt().getTblCompanyBalance());
        selectedDataPayment.setTblCompanyBalanceBankAccount(selectedDataPayment.getTblCalendarEmployeeDebt().getTblCompanyBalanceBankAccount());
        //selectedDataPayment.getTblCompanyBalance().setBalanceNominal(totalCompanyBalance(selectedDataPayment.getTblCompanyBalance()));
        //totalDebt = nominalRevenueEmployeeDebt-selectedDataPayment.getEmployeePaymentDebtNominal();
        /*if(totalDebt==0){
          selectedDataPayment.getTblCalendarEmployeeDebt().setEmployeeDebtPaymentStatus("Dibayar");S
        }
        else{
          selectedDataPayment.getTblCalendarEmployeeDebt().setEmployeeDebtPaymentStatus("Dibayar Sebagian");
        }
        selectedDataPayment.setTblCalendarEmployeeDebt(debtController.selectedData);*/
       // lblStatusPaymentDebt.textProperty().set(selectedDataPayment.getTblCalendarEmployeeDebt().getEmployeeDebtPaymentStatus());//.bindBidirectional(selectedDataPayment.getTblCalendarEmployeeDebt().employeeDebtPaymentStatusProperty());
    }
    
    private BigDecimal totalCompanyBalance(TblCompanyBalance companyBalance){
       return companyBalance.getBalanceNominal().add(selectedDataPayment.getEmployeePaymentDebtNominal());
    }
    
//total dibayar karyawan    
    private BigDecimal totalEmployeePaymentDebt(List<TblCalendarEmployeePaymentDebtHistory> list){
        BigDecimal total = new BigDecimal("0");
        for(TblCalendarEmployeePaymentDebtHistory getEmployeePaymentDebtHistory : list){
          total = total.add(getEmployeePaymentDebtHistory.getEmployeePaymentDebtNominal());  
        }
        return total;
    }
    
    private void dataDebtPaymentCreateHandle(){
      selectedDataPayment = new TblCalendarEmployeePaymentDebtHistory();
      selectedDataPayment.setTblCalendarEmployeeDebt(debtController.selectedData);
      selectedDataPayment.setEmployeePaymentDebtNominal(new BigDecimal("0"));
        //System.out.println("status bayar:"+selectedDataPayment.getTblCalendarEmployeeDebt().getEmployeeDebtPaymentStatus());
      selectedDataToInputForm();
    }
    
    private void dataDebtPaymentSaveHandle(){
       if(checkDataDebtPaymentInput()){
           selectedDataPayment.getTblCompanyBalance().setBalanceNominal(totalCompanyBalance(selectedDataPayment.getTblCompanyBalance()));
           //System.out.println("kas:"+selectedDataPayment.getTblCompanyBalance().getBalanceNominal());
           Alert alert = ClassMessage.showConfirmationSavingDataMessage("", debtController.dialogStagePayment);
            if (alert.getResult() == ButtonType.OK) {
                TblCalendarEmployeePaymentDebtHistory dummySelected = new TblCalendarEmployeePaymentDebtHistory(selectedDataPayment);
                totalDebt = nominalRevenueEmployeeDebt.subtract(dummySelected.getEmployeePaymentDebtNominal());
//                if(debtController.getService().paymentDataDebt(dummySelected,totalDebt,Date.valueOf(dpDatePaymentDebt.getValue()))){
//                   ClassMessage.showSucceedInsertingDataMessage(null,debtController.dialogStagePayment);
//                   debtController.dialogStagePayment.close();
//                   debtController.tblDataDebt.getTableView().setItems(debtController.loadAllDataDebt());
//                   debtController.dataDebtFormShowStatus.set(0.0);
//                   debtController.refeshDataFiltering();
//                }
//                else{
//                  ClassMessage.showFailedInsertingDataMessage(debtController.getService().getErrorMessage(),debtController.dialogStagePayment);
//                }
           }
       }
       else{
         ClassMessage.showWarningInputDataMessage(errDataInput,debtController.dialogStagePayment);
       }
    }
    
    String errDataInput;
    private boolean checkDataDebtPaymentInput(){
        boolean check = true;
        errDataInput = "";
       if(dpDatePaymentDebt.getValue()==null){
          check = false;
          errDataInput+="Tanggal Pembayaran:"+ClassMessage.defaultErrorNullValueMessage+"\n";
       }
       
       if(txtNominalPaymentDebt.getText() == null 
               || txtNominalPaymentDebt.getText().equals("-")
               || txtNominalPaymentDebt.getText().equals("-")){
           check = false; 
           errDataInput+="Nominal Pembayaran:"+ClassMessage.defaultErrorZeroValueMessage+"\n";
       }else{
        if(txtNominalPaymentDebt.getText().equalsIgnoreCase("0")){
         check = false; 
         errDataInput+="Nominal Pembayaran:"+ClassMessage.defaultErrorZeroValueMessage+"\n";
        }
        else if(selectedDataPayment.getEmployeePaymentDebtNominal()
                .compareTo(nominalRevenueEmployeeDebt) == 1){
          check = false;
          errDataInput+="Nominal Pembayaran: nominal yang diinput melebihi sisa pinjaman!! \n";
        }
       }
       return check;
    }
    
    private void dataDebtPaymentControllerCancelHandle(){
      debtController.dialogStagePayment.close();
      debtController.refeshDataFiltering();
    }
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initFormDataDebtPayment();
        dataDebtPaymentCreateHandle();
        //dataDebtPaymentCreateHandle();
        System.out.println("hsl>>"+debtController.selectedData.getTblCompanyBalance().getBalanceName());
      //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    public DebtPaymentController(DebtController parentController){
       debtController = parentController;
    }
    
    private final DebtController debtController;
}
