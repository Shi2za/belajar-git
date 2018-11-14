/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hotelfx.view.feature_debt.debt_payment_history;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXCComboBoxTablePopup;

import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXTextArea;
import com.jfoenix.controls.JFXTextField;
import hotelfx.HotelFX;
import hotelfx.helper.ClassFormatter;
import hotelfx.helper.ClassMessage;
import hotelfx.helper.ClassViewSetting;
import hotelfx.persistence.model.RefFinanceTransactionPaymentType;
import hotelfx.persistence.model.RefFinanceTransactionType;
import hotelfx.persistence.model.TblCalendarEmployeePaymentDebtHistory;
import hotelfx.persistence.model.TblCalendarEmployeePaymentHistory;
import hotelfx.persistence.model.TblCompanyBalance;
import hotelfx.persistence.model.TblCompanyBalanceBankAccount;
import hotelfx.persistence.model.TblEmployee;
import hotelfx.persistence.model.TblEmployeeBankAccount;
import hotelfx.persistence.service.FDebtManager;
import insidefx.undecorator.Undecorator;
import java.io.IOException;
import java.math.BigDecimal;
import java.net.URL;
import java.sql.Date;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.binding.Bindings;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.css.PseudoClass;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

/**
 *
 * @author Andreas
 */
public class DebtPaymentController implements Initializable{
    
 /*   @FXML
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
    private JFXTextArea txtPaymentDebtNote; */
    @FXML
    private JFXButton btnCancel;
    @FXML
    private JFXButton btnSave;
    @FXML
    private JFXButton btnPlus;
    @FXML
    private AnchorPane ancPaymentType;
    private JFXCComboBoxTablePopup<RefFinanceTransactionPaymentType>cbpPaymentType;
    
    @FXML
    private AnchorPane ancCompanyBalance;
    private JFXCComboBoxTablePopup<TblCompanyBalance>cbpCompanyBalance;
    
    @FXML
    private AnchorPane ancCompanyBalanceBankAccount;
    private JFXCComboBoxTablePopup<TblCompanyBalanceBankAccount>cbpCompanyBalanceBankAccount;
    
    @FXML
    private AnchorPane ancSenderBankAccount;
    public JFXCComboBoxTablePopup<TblEmployeeBankAccount>cbpSenderBankAccount;
    
    @FXML
    private HBox hbSenderAccount;
    
    @FXML
    private JFXTextField txtPaymentNominal;
    
    @FXML
    private JFXTextField txtCompanyBalance;
    
    public TblCalendarEmployeePaymentHistory selectedDataPayment;
/*    private int inputDataStatus;
    
    private BigDecimal nominalEmployeePaymentDebt = new BigDecimal("0");
    private BigDecimal nominalRevenueEmployeeDebt = new BigDecimal("0");
    private BigDecimal totalDebt;
    private final PseudoClass onScrollPseudoClass = PseudoClass.getPseudoClass("onScroll");
    private final BooleanProperty isFormScroll = new SimpleBooleanProperty(false);
    private int scrollCounter = 0;
    private TblCalendarEmployeePaymentDebtHistory selectedDataPayment; */
    
    
    private void initFormDataDebtPayment(){
     initDataPopup();
     hbSenderAccount.setVisible(false);
     cbpCompanyBalanceBankAccount.setVisible(false);
     
     cbpPaymentType.valueProperty().addListener((obs,oldVal,newVal)->{
       if(newVal!=null){
           if(newVal.getIdtype()==1){
              hbSenderAccount.setVisible(true);
           }
           else{
             hbSenderAccount.setVisible(false);
           }
        }
     });
     
   /* cbpCompanyBalance.valueProperty().addListener((obs,oldVal,newVal)->{
         if(newVal.getIdbalance()==1){
           cbpCompanyBalanceBankAccount.setVisible(true);
         }
         else{
           cbpCompanyBalanceBankAccount.setVisible(false);
         }
     });
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
       
      btnPlus.setOnAction((e)->{
         showEmployeeBankAccountSenderDialog();
       });
      
       btnSave.setOnAction((e)->{
         dataDebtPaymentControllerSaveHandle();
          // Alert alertInput = ClassMessage.showConfirmationSavingDataMessage(null,debtController.dialogStagePayment);
          // if(alertInput.getResult()==ButtonType.OK){
         //dataDebtPaymentSaveHandle();
       });
       
       btnCancel.setOnAction((e)->{
         dataDebtPaymentControllerCancelHandle();
       });
       
      // setSelectedDataToInputForm();
    //    initCalendarDate();
    //    initImportantFieldColor();
    //    initNumbericField();
       initNumbericField();
       initImportantFieldColor();
    }
    
  /*   private void initCalendarDate(){
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
    } */
    
     private void initNumbericField(){
       ClassFormatter.setToNumericField("BigDecimal",txtPaymentNominal);
     }
     
     private void initImportantFieldColor(){
        ClassViewSetting.setImportantField(cbpPaymentType,cbpCompanyBalanceBankAccount,
        txtPaymentNominal,cbpSenderBankAccount);
     }
     
    private void initDataPopup(){
      TableView<RefFinanceTransactionPaymentType>tblPaymentType = new TableView();
      TableColumn<RefFinanceTransactionPaymentType,String>namePaymentType = new TableColumn("Tipe Pembayaran");
      namePaymentType.setCellValueFactory(cellData -> cellData.getValue().typeNameProperty());
      tblPaymentType.getColumns().addAll(namePaymentType);
      ObservableList<RefFinanceTransactionPaymentType>listPaymentType = FXCollections.observableArrayList(loadAllDataPaymentType());
      cbpPaymentType = new JFXCComboBoxTablePopup(RefFinanceTransactionPaymentType.class,tblPaymentType,listPaymentType,"","Tipe Pembayaran *",true,400,300);
      cbpPaymentType.setItems(listPaymentType);
      
    /*  TableView<TblCompanyBalance>tblCompanyBalance = new TableView();
      TableColumn<TblCompanyBalance,String>nameCompanyBalance = new TableColumn("Kas");
      nameCompanyBalance.setCellValueFactory(cellData -> cellData.getValue().balanceNameProperty());
      tblCompanyBalance.getColumns().addAll(nameCompanyBalance);
      ObservableList<TblCompanyBalance>listCompanyBalance = FXCollections.observableArrayList(loadAllDataCompanyBalance());
      cbpCompanyBalance = new JFXCComboBoxTablePopup(TblCompanyBalance.class,tblCompanyBalance,listCompanyBalance,"","Kas *",true,400,300);
      cbpCompanyBalance.setItems(listCompanyBalance); */
      
      TableView<TblCompanyBalanceBankAccount>tblCompanyBalanceBankAccount = new TableView();
      TableColumn<TblCompanyBalanceBankAccount,String>codeBankAccount = new TableColumn("No Rekening");
      codeBankAccount.setMinWidth(100);
      codeBankAccount.setCellValueFactory((TableColumn.CellDataFeatures<TblCompanyBalanceBankAccount,String>param)
      ->Bindings.createStringBinding(()->param.getValue().getTblBankAccount().getCodeBankAccount(),param.getValue().tblBankAccountProperty()));
      TableColumn<TblCompanyBalanceBankAccount,String>holderBankAccount = new TableColumn("Pemegang Rekening");
      holderBankAccount.setMinWidth(160);
      holderBankAccount.setCellValueFactory((TableColumn.CellDataFeatures<TblCompanyBalanceBankAccount,String>param)
      ->Bindings.createStringBinding(()->param.getValue().getTblBankAccount().getBankAccountHolderName(),param.getValue().tblBankAccountProperty()));
      TableColumn<TblCompanyBalanceBankAccount,String>bank = new TableColumn("Bank");
      bank.setMinWidth(140);
      bank.setCellValueFactory((TableColumn.CellDataFeatures<TblCompanyBalanceBankAccount,String>param)
      ->Bindings.createStringBinding(()->param.getValue().getTblBankAccount().getTblBank().getBankName(),param.getValue().getTblBankAccount().tblBankProperty()));
      tblCompanyBalanceBankAccount.getColumns().addAll(codeBankAccount,holderBankAccount,bank);
      ObservableList<TblCompanyBalanceBankAccount>listCompanyBalanceBankAccount = FXCollections.observableArrayList();
      cbpCompanyBalanceBankAccount = new JFXCComboBoxTablePopup(TblCompanyBalanceBankAccount.class,tblCompanyBalanceBankAccount,listCompanyBalanceBankAccount,"","No Rekening Hotel *",true,400,300);
      cbpCompanyBalanceBankAccount.setItems(listCompanyBalanceBankAccount);
      
      TableView<TblEmployeeBankAccount>tblEmployeeBankAccount = new TableView();
      TableColumn<TblEmployeeBankAccount,String>codeEmployeeBankAccount = new TableColumn("No Rekening");
      codeEmployeeBankAccount.setMinWidth(100);
      codeEmployeeBankAccount.setCellValueFactory((TableColumn.CellDataFeatures<TblEmployeeBankAccount,String>param)
      ->Bindings.createStringBinding(()->param.getValue().getTblBankAccount().getCodeBankAccount(),param.getValue().tblBankAccountProperty()));
      TableColumn<TblEmployeeBankAccount,String>holderEmployeeBankAccount = new TableColumn("Pemegang Rekening");
      holderEmployeeBankAccount.setMinWidth(160);
      holderEmployeeBankAccount.setCellValueFactory((TableColumn.CellDataFeatures<TblEmployeeBankAccount,String>param)
      ->Bindings.createStringBinding(()->param.getValue().getTblBankAccount().getBankAccountHolderName(),param.getValue().tblBankAccountProperty()));
      TableColumn<TblEmployeeBankAccount,String>employeeBank = new TableColumn("Bank");
      employeeBank.setMinWidth(140);
      employeeBank.setCellValueFactory((TableColumn.CellDataFeatures<TblEmployeeBankAccount,String>param)
      ->Bindings.createStringBinding(()->param.getValue().getTblBankAccount().getTblBank().getBankName(),param.getValue().getTblBankAccount().tblBankProperty()));
      tblEmployeeBankAccount.getColumns().addAll(codeEmployeeBankAccount,holderEmployeeBankAccount,employeeBank);
      ObservableList<TblEmployeeBankAccount>listEmployeeBankAccount = FXCollections.observableArrayList();
      cbpSenderBankAccount = new JFXCComboBoxTablePopup(TblEmployeeBankAccount.class,tblEmployeeBankAccount,listEmployeeBankAccount,"","No Rekening Pengirim*",true,400,300);
      cbpSenderBankAccount.setItems(listEmployeeBankAccount);
      
       ancPaymentType.getChildren().clear();
       AnchorPane.setTopAnchor(cbpPaymentType,0.0);
       AnchorPane.setBottomAnchor(cbpPaymentType,0.0);
       AnchorPane.setLeftAnchor(cbpPaymentType,0.0);
       AnchorPane.setRightAnchor(cbpPaymentType,0.0);
       ancPaymentType.getChildren().add(cbpPaymentType);
       
    /* ancCompanyBalance.getChildren().clear();
       AnchorPane.setTopAnchor( cbpCompanyBalance,0.0);
       AnchorPane.setBottomAnchor( cbpCompanyBalance,0.0);
       AnchorPane.setLeftAnchor( cbpCompanyBalance,0.0);
       AnchorPane.setRightAnchor( cbpCompanyBalance,0.0);
       ancCompanyBalance.getChildren().add( cbpCompanyBalance); */
       
       ancCompanyBalanceBankAccount.getChildren().clear();
       AnchorPane.setTopAnchor( cbpCompanyBalanceBankAccount,0.0);
       AnchorPane.setBottomAnchor( cbpCompanyBalanceBankAccount,0.0);
       AnchorPane.setLeftAnchor( cbpCompanyBalanceBankAccount,0.0);
       AnchorPane.setRightAnchor( cbpCompanyBalanceBankAccount,0.0);
       ancCompanyBalanceBankAccount.getChildren().add( cbpCompanyBalanceBankAccount);
       
       ancSenderBankAccount.getChildren().clear();
       AnchorPane.setTopAnchor( cbpSenderBankAccount,0.0);
       AnchorPane.setBottomAnchor(cbpSenderBankAccount,0.0);
       AnchorPane.setLeftAnchor( cbpSenderBankAccount,0.0);
       AnchorPane.setRightAnchor(cbpSenderBankAccount,0.0);
      ancSenderBankAccount.getChildren().add(cbpSenderBankAccount);
    }
    
    private List<RefFinanceTransactionPaymentType>loadAllDataPaymentType(){
       List<RefFinanceTransactionPaymentType>list = debtPaymentController.getService().getAllPaymentType();
       for(int i = list.size()-1; i>0;i--){
          RefFinanceTransactionPaymentType paymentType = list.get(i);
           if(paymentType.getIdtype()>1){
             list.remove(paymentType);
           }
       }
      return list;
    }
    
    public List<TblEmployeeBankAccount>loadAllDataEmployeeBankAccount(TblEmployee employee){
      return debtPaymentController.getService().getAllBankAccountSender(employee);
    }
    
    private List<TblCompanyBalanceBankAccount>loadAllDataCompanyBalanceBankAccount(TblCompanyBalance companyBalance){
     return debtPaymentController.getService().getAllCompanyBalanceBankAccount(companyBalance);
    }
    
    private TblCompanyBalance getDataCompanyBalance(RefFinanceTransactionPaymentType paymentType){  
      TblCompanyBalance companyBalance = null;
       if(paymentType.getIdtype()==0){
         companyBalance = debtPaymentController.getService().getCompanyBalance((long)2);
        }
       
        if(paymentType.getIdtype()==1){
          companyBalance = debtPaymentController.getService().getCompanyBalance((long)1);  
        }
    /*  List<TblCompanyBalance>list = debtPaymentController.getService().getAllCompanyBalance();
       for(int i = 0; i<list.size();i++){
           if(list.get(i).getIdbalance()!=0){
             list.remove(list.get(i));
           }
       } */
     return companyBalance;
    }
    
    private void refreshDataPopup(){
       ObservableList<RefFinanceTransactionPaymentType>listPaymentType = FXCollections.observableArrayList(loadAllDataPaymentType());
       cbpPaymentType.setItems(listPaymentType);
       
     /*  ObservableList<TblCompanyBalance>listCompanyBalance = FXCollections.observableArrayList(loadAllDataCompanyBalance());
       cbpCompanyBalance.setItems(listCompanyBalance); */
       
       ObservableList<TblCompanyBalanceBankAccount>listCompanyBalanceBankAccount = FXCollections.observableArrayList();
     /*  cbpCompanyBalance.valueProperty().addListener((obs,oldVal,newVal)->{
           if(newVal!=null){
             listCompanyBalanceBankAccount.setAll(loadAllDataCompanyBalanceBankAccount(newVal));
           }
       }); */
       
       ObservableList<TblEmployeeBankAccount>listEmployeeBankAccount = FXCollections.observableArrayList(loadAllDataEmployeeBankAccount(debtPaymentController.selectedData.getEmployee()));
       cbpSenderBankAccount.setItems(listEmployeeBankAccount);
    }
    
    private void setSelectedDataToInputForm(){
      refreshDataPopup();
      cbpPaymentType.valueProperty().bindBidirectional(selectedDataPayment.refFinanceTransactionPaymentTypeProperty());
     // txtCompanyBalance.textProperty().bindBidirectional(selectedDataPayment.getTblCompanyBalance().balanceNameProperty());
     // cbpCompanyBalance.valueProperty().bindBidirectional(selectedDataPayment.tblCompanyBalanceProperty());
      cbpPaymentType.valueProperty().addListener((obs,oldVal,newVal)->{
           if(newVal!=null){
             selectedDataPayment.setTblCompanyBalance(getDataCompanyBalance(newVal));
             txtCompanyBalance.setText(selectedDataPayment.getTblCompanyBalance().getBalanceName());
             cbpCompanyBalanceBankAccount.setVisible(selectedDataPayment.getTblCompanyBalance().getIdbalance()==1);
             cbpCompanyBalanceBankAccount.setItems(FXCollections.observableArrayList(loadAllDataCompanyBalanceBankAccount(selectedDataPayment.getTblCompanyBalance())));
             cbpCompanyBalanceBankAccount.setValue(null);
             cbpSenderBankAccount.setValue(null);
            }
      });
      
      
      cbpCompanyBalanceBankAccount.valueProperty().bindBidirectional(selectedDataPayment.tblCompanyBalanceBankAccountProperty());
      cbpSenderBankAccount.valueProperty().bindBidirectional(selectedDataPayment.tblEmployeeBankAccountProperty());
      Bindings.bindBidirectional(txtPaymentNominal.textProperty(),selectedDataPayment.employeePaymentDebtNominalProperty(),new ClassFormatter.CBigDecimalStringConverter());
    }
    
    private void dataDebtPaymentControllerCreateHandle(){
      selectedDataPayment = new TblCalendarEmployeePaymentHistory();
     // System.out.println("Hsl Payment : "+debtPaymentController.selectedData.getEmployee().getCodeEmployee());
      selectedDataPayment.setTblEmployeeByIdemployee(debtPaymentController.selectedData.getEmployee());
      cbpSenderBankAccount.setItems(FXCollections.observableArrayList(loadAllDataEmployeeBankAccount(selectedDataPayment.getTblEmployeeByIdemployee())));
      selectedDataPayment.setEmployeePaymentDebtNominal(new BigDecimal(0));
      setSelectedDataToInputForm();
    }
    
    private void dataDebtPaymentControllerSaveHandle(){
       if(checkDataInput()){
          Alert alert = ClassMessage.showConfirmationSavingDataMessage("",debtPaymentController.dialogStage);
          if(alert.getResult()==ButtonType.OK){
             TblCalendarEmployeePaymentHistory dummySelectedData = new TblCalendarEmployeePaymentHistory(selectedDataPayment);
          //   System.out.println("hsl data :"+);
               if(debtPaymentController.getService().paymentDataDebt(dummySelectedData)){
                 ClassMessage.showSucceedInsertingDataMessage(null,debtPaymentController.dialogStage);
                 debtPaymentController.tblDataDebtPaymentDetail.getTableView().setItems(FXCollections.observableArrayList(debtPaymentController.loadAllDataDebtPaymentDetail()));
                 debtPaymentController.tblDataDebtPayment.getTableView().setItems(FXCollections.observableArrayList(debtPaymentController.loadAllDataDebtPaymentHistory()));
                 debtPaymentController.selectedData.setPaymentTotal(debtPaymentController.debtPaymentTotal(debtPaymentController.selectedData.getEmployee()));
                 debtPaymentController.selectedData.setBalance(debtPaymentController.selectedData.getDebtTotal().subtract(debtPaymentController.selectedData.getPaymentTotal()));
                 debtPaymentController.dialogStage.close();
               }
               else{
                 ClassMessage.showFailedInsertingDataMessage(null,debtPaymentController.dialogStage);
               }
           }
        }
       else{
         ClassMessage.showWarningInputDataMessage(errDataInput,debtPaymentController.dialogStage);
       }
    }
    
    private void dataDebtPaymentControllerCancelHandle(){
      debtPaymentController.dialogStage.close();
     // debtController.refeshDataFiltering();
    }
    
    private String errDataInput;
    
    private boolean checkDataInput(){
       errDataInput = "";
       boolean check = true;
       if(cbpPaymentType.getValue()==null){
         errDataInput+="Tipe Pembayaran :"+ClassMessage.defaultErrorNullValueMessage+"\n";
         check = false;
       }
       else{
           if(cbpPaymentType.getValue().getIdtype()==1){
               if(cbpSenderBankAccount.getValue()==null){
                 errDataInput+="No.Rekening Pengirim :"+ClassMessage.defaultErrorNullValueMessage+"\n";
                 check = false;
               }
               
               if(cbpCompanyBalanceBankAccount.getValue()==null){
                 errDataInput+="No.Rekening Hotel :"+ClassMessage.defaultErrorNullValueMessage+"\n";
                 check = false;
               }
           }
       }
       
       if(txtPaymentNominal.getText()==null || txtPaymentNominal.getText().equalsIgnoreCase("")){
           errDataInput+="Nominal Pembayaran : "+ClassMessage.defaultErrorNullValueMessage+"\n";
           check = false;
       }
       else{
           if(selectedDataPayment.getEmployeePaymentDebtNominal().doubleValue()==0){
             errDataInput+="Nominal Pembayaran : "+ClassMessage.defaultErrorZeroValueMessage+"\n";
             check = false;
           }
           else{
               if(selectedDataPayment.getEmployeePaymentDebtNominal().compareTo(debtPaymentController.selectedData.getBalance())==1){
                 errDataInput+="Nominal Pembayaran : tidak boleh melebihi sisa pinjaman"+"\n";
                 check = false;
               }
           }
       }
     /*  if(.getValue()==null){
         errDataInput+="Kas :"+ClassMessage.defaultErrorNullValueMessage+"\n";
         check = false;
       }
       else{
           if(cbpCompanyBalance.getValue().getIdbalance()==1){
               if(cbpCompanyBalanceBankAccount.getValue()==null){
                 errDataInput+="No.Rekening Hotel :"+ClassMessage.defaultErrorNullValueMessage+"\n";
                 check = false;
               }
           }
        } */
      return check;
    }
   /* private void setSelectedDataToInputForm(){
      txtEmployee.setText(selectedData.getEmployee().getCodeEmployee()+"-"+selectedData.getEmployee().getTblPeople().getFullName());
      Bindings.bindBidirectional(txtDebtTotal.textProperty(),selectedData.debtTotalProperty(),new ClassFormatter.CBigDecimalStringConverter());
      Bindings.bindBidirectional(txtPaymentTotal.textProperty(),selectedData.paymentTotalProperty(),new ClassFormatter.CBigDecimalStringConverter());
      Bindings.bindBidirectional(txtBalance.textProperty(),selectedData.balanceProperty(),new ClassFormatter.CBigDecimalStringConverter());
    }
    private void dataDebtPaymentCreateHandle(){
      selectedDataPayment = new TblCalendarEmployeePaymentHistory();
      selectedDataToInputForm();
    }
  /*  private void selectedDataToInputForm(){
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
//    }
    
 /*   private BigDecimal totalCompanyBalance(TblCompanyBalance companyBalance){
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
                if(debtController.getService().paymentDataDebt(dummySelected,totalDebt,Date.valueOf(dpDatePaymentDebt.getValue()))){
                   ClassMessage.showSucceedInsertingDataMessage(null,debtController.dialogStagePayment);
                   debtController.dialogStagePayment.close();
                   debtController.tblDataDebt.getTableView().setItems(debtController.loadAllDataDebt());
                   debtController.dataDebtFormShowStatus.set(0.0);
                   debtController.refeshDataFiltering();
                }
                else{
                  ClassMessage.showFailedInsertingDataMessage(debtController.getService().getErrorMessage(),debtController.dialogStagePayment);
                }
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
    } */
    
    public Stage dialogStage;
    private void showEmployeeBankAccountSenderDialog(){
        try {
            FXMLLoader loader = new FXMLLoader();//"view/feature_debt/debt/DebtApproved.fxml");
            loader.setLocation(HotelFX.class.getResource("view/feature_debt/debt_payment_history/DebtPaymentBankAccountSenderDialog.fxml"));
            
            DebtPaymentBankAccountSenderController debtPaymentBankAccountController = new DebtPaymentBankAccountSenderController(this);
            loader.setController(debtPaymentBankAccountController);
            
            Region page = loader.load();
            
            dialogStage = new Stage();
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.initOwner(debtPaymentController.dialogStage);
            
            Undecorator undecorator = new Undecorator(dialogStage,page);
            undecorator.getStylesheets().add("/skin/undecorator.css");
            undecorator.getMenuButton().setVisible(false);
            undecorator.getMaximizeButton().setVisible(false);
            undecorator.getMinimizeButton().setVisible(false);
            undecorator.getFullScreenButton().setVisible(false);
            undecorator.getCloseButton().setVisible(false);
            
            Scene scene = new Scene(undecorator);
            scene.setFill(Color.TRANSPARENT);
            
            dialogStage.initStyle(StageStyle.TRANSPARENT);
            dialogStage.setScene(scene);
            dialogStage.setResizable(false);
            
            dialogStage.showAndWait();
        } catch (IOException ex) {
            Logger.getLogger(DebtPaymentHistoryController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initFormDataDebtPayment();
        dataDebtPaymentControllerCreateHandle();
      //  dataDebtPaymentCreateHandle();
        //dataDebtPaymentCreateHandle();
       // System.out.println("hsl>>"+debtController.selectedData.getTblCompanyBalance().getBalanceName());
      //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    public DebtPaymentController(DebtPaymentHistoryController parentController){
       debtPaymentController = parentController;
    }
    
    public final DebtPaymentHistoryController debtPaymentController;
    
      public FDebtManager getService(){
       return debtPaymentController.getService();
    }
}
