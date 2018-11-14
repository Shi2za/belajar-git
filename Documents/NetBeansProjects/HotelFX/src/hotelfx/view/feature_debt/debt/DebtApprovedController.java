/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hotelfx.view.feature_debt.debt;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXCComboBoxPopup;
import com.jfoenix.controls.JFXCComboBoxTablePopup;
import com.jfoenix.controls.JFXTextField;
import hotelfx.HotelFX;
import hotelfx.helper.ClassFormatter;
import hotelfx.helper.ClassMessage;
import hotelfx.helper.ClassViewSetting;
import hotelfx.persistence.model.RefFinanceTransactionPaymentType;
import hotelfx.persistence.model.TblCalendarEmployeeDebt;
import hotelfx.persistence.model.TblCompanyBalance;
import hotelfx.persistence.model.TblCompanyBalanceBankAccount;
import hotelfx.persistence.model.TblEmployee;
import hotelfx.persistence.model.TblEmployeeBankAccount;
import hotelfx.persistence.service.FDebtManager;
import insidefx.undecorator.Undecorator;
import java.io.IOException;
import java.math.BigDecimal;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import static javafx.scene.layout.Region.USE_COMPUTED_SIZE;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

/**
 *
 * @author Andreas
 */
public class DebtApprovedController implements Initializable{
    
    @FXML
    private GridPane gpFormDebtApproved;
    
    @FXML
    private Label lblEmployee;
    
    @FXML
    private AnchorPane ancCbpDebtType;
    private JFXCComboBoxTablePopup<RefFinanceTransactionPaymentType>cbpDebtType;
    
    @FXML
    private JFXTextField txtCompanyBalance;
   /* @FXML
    private AnchorPane cbpCompanyBalanceTypeLayout;
    private JFXCComboBoxTablePopup<TblCompanyBalance>cbpCompanyBalanceType; */
    
    @FXML
    private AnchorPane companyBalanceBankAccountLayout;
    private JFXCComboBoxTablePopup<TblCompanyBalanceBankAccount>cbpCompanyBalanceBankAccount;
    
    @FXML
    private HBox hbReceivedBankAccount;
    
    @FXML
    private AnchorPane ancCbpReceivedBankAccount;
    public JFXCComboBoxTablePopup<TblEmployeeBankAccount>cbpReceivedBankAccount;
    
    @FXML
    private JFXTextField txtNominalEmployeeDebt;
    @FXML
    private JFXButton btnSaveApproved;
    @FXML
    private JFXButton btnCancelApproved;
    @FXML
    private JFXButton btnPlus;
    
    public TblCalendarEmployeeDebt selectedData;
    
    private void initFormDataApproved(){
     // initDataCompanyBalancePopup();
      initDataPopup();
     // initDataCompanyBalanceBankAccountPopup();
      cbpCompanyBalanceBankAccount.setVisible(false);
      hbReceivedBankAccount.setVisible(false);
     /* cbpCompanyBalanceType.valueProperty().addListener((obs,oldVal,newVal)->{
         cbpCompanyBalanceBankAccount.setVisible(newVal!=null && newVal.getIdbalance()==1);
         cbpCompanyBalanceBankAccount.setItems(FXCollections.observableArrayList(loadAllDataCompanyBalanceBankAccount(newVal)));
      });*/
//      cbpCompanyBalanceBankAccount.setVisible(false);
      btnSaveApproved.setOnAction((e)->{
          dataDebtApprovedSaveHandle();
      });
      
      btnCancelApproved.setOnAction((e)->{
         dataDebtApprovedControllerCancelHandle();
      });
      
      btnPlus.setOnAction((e)->{
         showReceivedBankAccountDialog();
      });
      
      initImportantFieldColor();
    }
    
    private void initImportantFieldColor() {
        ClassViewSetting.setImportantField(
        cbpDebtType,cbpCompanyBalanceBankAccount);
    }
    
    private void refreshDataPopup(){
    /*  ObservableList<TblCompanyBalance>companyBalanceTypeItems = FXCollections.observableArrayList(loadAllDataCompanyBalance());
      cbpCompanyBalanceType.setItems(companyBalanceTypeItems); */
      ObservableList<RefFinanceTransactionPaymentType>debtTypeItems = FXCollections.observableArrayList(loadAllDataDebtType());
      ObservableList<TblCompanyBalanceBankAccount>companyBalanceBankAccountItems = FXCollections.observableArrayList();
      ObservableList<TblEmployeeBankAccount>employeeBankAccountItems = FXCollections.observableArrayList(loadAllDataEmployeeBankAccount(debtController.selectedData.getTblEmployeeByIdemployee()));
      cbpReceivedBankAccount.setItems(employeeBankAccountItems);
     /* cbpDebtType.valueProperty().addListener((obs,oldVal,newVal)->{
         if(newVal!=null){
           companyBalanceBankAccountItems.setAll(loadAllDataCompanyBalanceBankAccount(newVal));
         }
      });
      cbpCompanyBalanceBankAccount.setItems(companyBalanceBankAccountItems); */
    }
    
    private void initDataPopup(){
      TableView<RefFinanceTransactionPaymentType>tblPaymentType = new TableView();
      TableColumn<RefFinanceTransactionPaymentType,String>nameTransactionType = new TableColumn("Tipe Pinjaman");
      nameTransactionType.setCellValueFactory(cellData -> cellData.getValue().typeNameProperty());
      tblPaymentType.getColumns().addAll(nameTransactionType);
      ObservableList<RefFinanceTransactionPaymentType>listPaymentType = FXCollections.observableArrayList(loadAllDataDebtType());
      cbpDebtType = new JFXCComboBoxTablePopup(RefFinanceTransactionPaymentType.class,tblPaymentType,listPaymentType,"","Tipe Pinjaman *",true,400,300);
      cbpDebtType.setItems(listPaymentType);
      
      TableView<TblCompanyBalanceBankAccount>tblCompanyBalanceBankAccount = new TableView();
      TableColumn<TblCompanyBalanceBankAccount,String>companyBalanceCodeBankAccount = new TableColumn("No Rekening");
      companyBalanceCodeBankAccount.setMinWidth(100);
      companyBalanceCodeBankAccount.setCellValueFactory((TableColumn.CellDataFeatures<TblCompanyBalanceBankAccount,String>param)
      ->Bindings.createStringBinding(()->param.getValue().getTblBankAccount().getCodeBankAccount(),param.getValue().tblBankAccountProperty()));
      TableColumn<TblCompanyBalanceBankAccount,String>companyBalanceNameHolderBankAccount = new TableColumn("Pemegang Rekening");
      companyBalanceNameHolderBankAccount.setMinWidth(160);
      companyBalanceNameHolderBankAccount.setCellValueFactory((TableColumn.CellDataFeatures<TblCompanyBalanceBankAccount,String>param)
       ->Bindings.createStringBinding(()->param.getValue().getTblBankAccount().getBankAccountHolderName(),param.getValue().tblBankAccountProperty()));
      TableColumn<TblCompanyBalanceBankAccount,String>companyBalanceNameBankAccount = new TableColumn("Bank");
      companyBalanceNameBankAccount.setMinWidth(120);
      companyBalanceNameBankAccount.setCellValueFactory((TableColumn.CellDataFeatures<TblCompanyBalanceBankAccount,String>param)
       ->Bindings.createStringBinding(()->param.getValue().getTblBankAccount().getTblBank().getBankName(),param.getValue().tblBankAccountProperty()));   
      tblCompanyBalanceBankAccount.getColumns().addAll(companyBalanceCodeBankAccount,companyBalanceNameBankAccount,companyBalanceNameHolderBankAccount);
       ObservableList<TblCompanyBalanceBankAccount>listCompanyBalanceBankAccount = FXCollections.observableArrayList();
      cbpCompanyBalanceBankAccount = new JFXCComboBoxTablePopup(TblCompanyBalanceBankAccount.class,tblCompanyBalanceBankAccount,listCompanyBalanceBankAccount,"","No Rekening Hotel *",true,400,300);
     // cbpCompanyBalanceBankAccount.setItems(listCompanyBalanceBankAccount);
      
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
      cbpReceivedBankAccount = new JFXCComboBoxTablePopup(TblEmployeeBankAccount.class,tblEmployeeBankAccount,listEmployeeBankAccount,"","No Rekening Penerima*",true,400,300);
      cbpReceivedBankAccount.setItems(listEmployeeBankAccount);
      
       ancCbpDebtType.getChildren().clear();
       AnchorPane.setTopAnchor(cbpDebtType,0.0);
       AnchorPane.setBottomAnchor(cbpDebtType,0.0);
       AnchorPane.setLeftAnchor(cbpDebtType,0.0);
       AnchorPane.setRightAnchor(cbpDebtType,0.0);
       ancCbpDebtType.getChildren().add(cbpDebtType);
         
       companyBalanceBankAccountLayout.getChildren().clear();
       AnchorPane.setTopAnchor( cbpCompanyBalanceBankAccount,0.0);
       AnchorPane.setBottomAnchor( cbpCompanyBalanceBankAccount,0.0);
       AnchorPane.setLeftAnchor( cbpCompanyBalanceBankAccount,0.0);
       AnchorPane.setRightAnchor( cbpCompanyBalanceBankAccount,0.0);
       companyBalanceBankAccountLayout.getChildren().add( cbpCompanyBalanceBankAccount);
       
       ancCbpReceivedBankAccount.getChildren().clear();
       AnchorPane.setTopAnchor( cbpReceivedBankAccount,0.0);
       AnchorPane.setBottomAnchor( cbpReceivedBankAccount,0.0);
       AnchorPane.setLeftAnchor( cbpReceivedBankAccount,0.0);
       AnchorPane.setRightAnchor( cbpReceivedBankAccount,0.0);
       ancCbpReceivedBankAccount.getChildren().add(cbpReceivedBankAccount);
      /* TableView<TblCompanyBalance>tblCompanyBalance = new TableView();
       TableColumn<TblCompanyBalance,String>companyBalanceType = new TableColumn("Tipe Kas");
       companyBalanceType.setMinWidth(120);
       companyBalanceType.setCellValueFactory(cellData->cellData.getValue().balanceNameProperty());
       tblCompanyBalance.getColumns().addAll(companyBalanceType);
       ObservableList<TblCompanyBalance>companyBalanceTypeItems = FXCollections.observableArrayList(loadAllDataCompanyBalance());
       
       cbpCompanyBalanceType = new JFXCComboBoxTablePopup<>(TblCompanyBalance.class,tblCompanyBalance,companyBalanceTypeItems,"","Kas *",true,500,300);
       
       cbpCompanyBalanceTypeLayout.getChildren().clear();
       AnchorPane.setTopAnchor(cbpCompanyBalanceType,0.0);
       AnchorPane.setBottomAnchor(cbpCompanyBalanceType,0.0);
       AnchorPane.setLeftAnchor(cbpCompanyBalanceType,0.0);
       AnchorPane.setRightAnchor(cbpCompanyBalanceType,0.0);
       cbpCompanyBalanceTypeLayout.getChildren().add(cbpCompanyBalanceType); */
      // setFunctionPopup(cbpCompanyBalanceType,tblCompanyBalance,companyBalanceTypeItems,"balanceName","Tipe Kas *",400,300);
     //  gpFormDebtApproved.add(cbpCompanyBalanceType,1,2);
    }
    
     private List<RefFinanceTransactionPaymentType>loadAllDataDebtType(){
       List<RefFinanceTransactionPaymentType>list = debtController.getService().getAllPaymentType();
       for(int i = list.size()-1; i>0;i--){
          RefFinanceTransactionPaymentType paymentType = list.get(i);
           if(paymentType.getIdtype()>1){
             list.remove(paymentType);
           }
       }
      return list;
    }
  /*  private void initDataCompanyBalanceBankAccountPopup(){
       setDataCompanyBalanceBankAccount();
      
       ClassViewSetting.setImportantField(cbpCompanyBalanceBankAccount);
    } */
    
  /*  private void initDataCompanyBalanceBankAccountPopup(){

      cbpCompanyBalanceBankAccount = new JFXCComboBoxTablePopup<>(TblCompanyBalanceBankAccount.class,tblCompanyBalanceBankAccount,companyBalanceBankAccountItems,"","Rekening *",true,500,300);
       companyBalanceBankAccountLayout.getChildren().clear();
       AnchorPane.setTopAnchor(cbpCompanyBalanceBankAccount,0.0);
       AnchorPane.setBottomAnchor(cbpCompanyBalanceBankAccount,0.0);
       AnchorPane.setLeftAnchor(cbpCompanyBalanceBankAccount,0.0);
       AnchorPane.setRightAnchor(cbpCompanyBalanceBankAccount,0.0);
       companyBalanceBankAccountLayout.getChildren().add(cbpCompanyBalanceBankAccount);
        //setFunctionPopup(cbpCompanyBalanceBankAccount,tblCompanyBalanceBankAccount,companyBalanceBankAccountItems,"tblBankAccount","No Rekening *",500,300);
    } */
    
   /* private List<TblCompanyBalance>loadAllDataCompanyBalance(){
       List<TblCompanyBalance>listCompanyBalance = new ArrayList<>();
       listCompanyBalance = debtController.getService().getAllCompanyBalance();
       for(int i = 0; i<listCompanyBalance.size();i++){
           if(listCompanyBalance.get(i).getIdbalance()==4){
             listCompanyBalance.remove(listCompanyBalance.get(i));
           }
       }
       return listCompanyBalance;//debtController.getService().getAllCompanyBalance();
    } */
    
    private List<TblCompanyBalanceBankAccount>loadAllDataCompanyBalanceBankAccount(TblCompanyBalance companyBalance){
        return debtController.getService().getAllCompanyBalanceBankAccount(companyBalance);
    }
    
    public List<TblEmployeeBankAccount>loadAllDataEmployeeBankAccount(TblEmployee employee){
      return debtController.getService().getAllBankAccountSender(employee);
    }
    
    private void setFunctionPopup(JFXCComboBoxPopup cbp,
            TableView table,
            ObservableList items,
            String nameFiltered,
            String promptText,double prefWidth,double prefHeight) {
        table.getSelectionModel().selectedIndexProperty().addListener(
                (observable, oldValue, newValue) -> {
                    if (newValue.intValue() != -1) {
                        cbp.valueProperty().set(table.getItems().get(newValue.intValue()));
                    }
                    cbp.hide();
                });

        cbp.setPropertyNameForFiltered(nameFiltered);

        cbp.setItems(items);
        
        cbp.setOnShowing((e) -> {
            table.getSelectionModel().clearSelection();
        });

        // Add observable list data to the table
        table.itemsProperty().bind(cbp.filteredItemsProperty());

        //popup button
        JFXButton button = new JFXButton("▾");
        button.setOnMouseClicked((e) -> cbp.show());

        //popup content
        BorderPane content = new BorderPane(new JFXButton("SHOW"));
        content.setBackground(new Background(new BackgroundFill(Color.WHITE, CornerRadii.EMPTY, Insets.EMPTY)));
        content.setPrefSize(prefWidth,prefHeight);

        content.setCenter(table);

        cbp.setPopupEditor(true);
        cbp.promptTextProperty().set(promptText);
        cbp.setLabelFloat(true);
        cbp.setPopupButton(button);
        cbp.settArrowButton(true);
        cbp.setPopupContent(content);

        cbp.setMinSize(USE_COMPUTED_SIZE, USE_COMPUTED_SIZE);
        cbp.setPrefSize(USE_COMPUTED_SIZE, USE_COMPUTED_SIZE);
        cbp.setMaxSize(USE_COMPUTED_SIZE, USE_COMPUTED_SIZE);
    }
    
    private int inputDataStatus;
    private void setSelectedDataApprovedToInputForm(){
       refreshDataPopup();
       lblEmployee.textProperty().set(selectedData.getTblEmployeeByIdemployee().getCodeEmployee()+"-"+selectedData.getTblEmployeeByIdemployee().getTblPeople().getFullName());
       txtNominalEmployeeDebt.setDisable(true);
       cbpCompanyBalanceBankAccount.valueProperty().bindBidirectional(selectedData.tblCompanyBalanceBankAccountProperty());
       cbpReceivedBankAccount.valueProperty().bindBidirectional(selectedData.tblEmployeeBankAccountProperty());
       cbpDebtType.valueProperty().bindBidirectional(selectedData.refFinanceTransactionPaymentTypeProperty());
       Bindings.bindBidirectional(txtNominalEmployeeDebt.textProperty(),selectedData.employeeDebtNominalProperty(),new ClassFormatter.CBigDecimalStringConverter());
    //   cbpCompanyBalanceType.valueProperty().bindBidirectional(selectedData.tblCompanyBalanceProperty());
     /*  cbpCompanyBalanceType.valueProperty().addListener((obs,oldVal,newVal)->{
          cbpCompanyBalanceBankAccount.setVisible(newVal.getIdbalance()==1);
       }); */
       
       
      
     /*  cbpCompanyBalanceType.valueProperty().addListener((obs,oldCompanyBalance,newCompanyBalance)->{
           if(newCompanyBalance!=null){
               cbpCompanyBalanceBankAccount.setValue(null);
               //txtNominalEmployeeDebt.setText(null);
               if(newCompanyBalance.getIdbalance()!=1){
                 Bindings.bindBidirectional(txtNominalEmployeeDebt.textProperty(),selectedData.employeeDebtNominalProperty(),new ClassFormatter.CBigDecimalStringConverter());
                }
            }
       });
       
     cbpCompanyBalanceBankAccount.valueProperty().addListener((obs,oldCompanyBalanceBankAccount,newCompanyBalanceBankAccount)->{
           if(newCompanyBalanceBankAccount!=null){
              Bindings.bindBidirectional(txtNominalEmployeeDebt.textProperty(),selectedData.employeeDebtNominalProperty(),new ClassFormatter.CBigDecimalStringConverter());
            }
       }); */
       
    }
    
    private BigDecimal totalCompanyBalance(TblCompanyBalance companyBalance){
      return companyBalance.getBalanceNominal().subtract(selectedData.getEmployeeDebtNominal());
    }
    
     private TblCompanyBalance getDataCompanyBalance(RefFinanceTransactionPaymentType paymentType){  
      TblCompanyBalance companyBalance = null;
       if(paymentType.getIdtype()==0){
         companyBalance = debtController.getService().getCompanyBalance((long)2);
        }
        
        if(paymentType.getIdtype()==1){
          companyBalance = debtController.getService().getCompanyBalance((long)1);  
        }
    /*  List<TblCompanyBalance>list = debtPaymentController.getService().getAllCompanyBalance();
       for(int i = 0; i<list.size();i++){
           if(list.get(i).getIdbalance()!=0){
             list.remove(list.get(i));
           }
       } */
     return companyBalance;
    }
     
    private void dataDebtApprovedHandle(){
       selectedData = new TblCalendarEmployeeDebt(debtController.selectedData);
       selectedData.setEmployeeDebtStatus("Disetujui");
       cbpDebtType.valueProperty().addListener((obs,oldVal,newVal)->{
         selectedData.setTblCompanyBalance(getDataCompanyBalance(newVal));
         txtCompanyBalance.setText(selectedData.getTblCompanyBalance().getBalanceName());
         cbpCompanyBalanceBankAccount.setItems(FXCollections.observableArrayList(loadAllDataCompanyBalanceBankAccount(selectedData.getTblCompanyBalance())));
         cbpCompanyBalanceBankAccount.setValue(null);
         cbpCompanyBalanceBankAccount.setVisible(selectedData.getTblCompanyBalance().getIdbalance()==1);
         hbReceivedBankAccount.setVisible(selectedData.getTblCompanyBalance().getIdbalance()==1);
         cbpReceivedBankAccount.setValue(null);
       });
       
       setSelectedDataApprovedToInputForm();
    }
    private void dataDebtApprovedSaveHandle(){
       if(checkDataInputDebtApproved()){
           selectedData.getTblCompanyBalance().setBalanceNominal(totalCompanyBalance(selectedData.getTblCompanyBalance()));
           Alert alertInput = ClassMessage.showConfirmationSavingDataMessage("", debtController.dialogStageApproved);
            if(alertInput.getResult()==ButtonType.OK){
              TblCalendarEmployeeDebt dummySelectedData = new TblCalendarEmployeeDebt(selectedData);
               if(debtController.getService().approvedDataDebt(dummySelectedData)){
                 ClassMessage.showSucceedUpdatingDataMessage("", debtController.dialogStageApproved);
                 debtController.dialogStageApproved.close();
                 debtController.tblDataDebt.getTableView().setItems(debtController.loadAllDataDebt());
                 debtController.dataDebtFormShowStatus.set(0);
                 debtController.refeshDataFiltering();
               }
               else{
                 ClassMessage.showFailedUpdatingDataMessage(debtController.getService().getErrorMessage(),debtController.dialogStageApproved);
               }
            }
        }else{
         ClassMessage.showWarningInputDataMessage(errDataInput,debtController.dialogStageApproved);
       }
    }
    
    String errDataInput;
    private boolean checkDataInputDebtApproved(){
       errDataInput = "";
       boolean check = true;
       
       if(cbpDebtType.getValue()==null){
          errDataInput+="Tipe Pinjaman :"+ClassMessage.defaultErrorNullValueMessage+"\n";
          check = false;
       }
       else{
           if(cbpDebtType.getValue().getIdtype()==1){
               if(cbpCompanyBalanceBankAccount.getValue()==null){
                  errDataInput+="No Rekening Hotel :"+ClassMessage.defaultErrorNullValueMessage+"\n";
                  check = false;
                }
                
               if(cbpReceivedBankAccount.getValue()==null){
                  errDataInput+="No Rekening Penerima :"+ClassMessage.defaultErrorNullValueMessage+"\n";
                  check = false;
                }
               
               if(cbpCompanyBalanceBankAccount.getValue()!=null && cbpReceivedBankAccount.getValue()!=null){
                   if(selectedData.getEmployeeDebtNominal().doubleValue()>selectedData.getTblCompanyBalanceBankAccount().getCompanyBalanceBankAccountNominal().doubleValue()){
                      errDataInput+="Kas Tidak Mencukupi!!";
                      check = false;
                    }
               }
           }
           else{
               if(selectedData.getEmployeeDebtNominal().doubleValue()>selectedData.getTblCompanyBalance().getBalanceNominal().doubleValue()){
                  errDataInput+="Kas Tidak Mencukupi!!";
                  check = false;
                }
                     
           }
       }
      /* if(cbpCompanyBalanceType.getValue()==null){
         errDataInput+="Tipe Kas : "+ClassMessage.defaultErrorNullValueMessage+"\n";
         check = false;
       } */
       
      /* if(cbp){
           if(cbpCompanyBalanceType.getValue().getIdbalance()==1){
               if(cbpCompanyBalanceBankAccount.getValue()==null){
                  check = false;
                  errDataInput+="Rekening : "+ClassMessage.defaultErrorNullValueMessage+"\n";
               }
               else{
                   if(cbpCompanyBalanceBankAccount.getValue()!=null){
                       if(selectedData.getEmployeeDebtNominal().doubleValue()>selectedData.getTblCompanyBalanceBankAccount().getCompanyBalanceBankAccountNominal().doubleValue()){
                         errDataInput+="Kas Tidak Mencukupi!!";
                         check = false;
                       }
                   }
               }
           }
           else{
               if(selectedData.getEmployeeDebtNominal().doubleValue()>selectedData.getTblCompanyBalance().getBalanceNominal().doubleValue()){
                  errDataInput+="Kas Tidak Mencukupi!!";
                  check = false;
                }
           }
       }
       
      /* if(txtNominalEmployeeDebt.getText()==null || txtNominalEmployeeDebt.getText().equals("")){
         errDataInput+="Nominal :"+ClassMessage.defaultErrorNullValueMessage+"\n";
         check = false;
       }*/
       
       return check;
    }
    
    private void dataDebtApprovedControllerCancelHandle(){
      debtController.dialogStageApproved.close();
      debtController.refeshDataFiltering();
    }
    
    public Stage dialogStage;
    
    private void showReceivedBankAccountDialog(){
      try{
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(HotelFX.class.getResource("view/feature_debt/debt/DebtBankAccountReceivedDialog.fxml"));
        
        DebtBankAccountReceivedController debtBankAccountReceivedController = new DebtBankAccountReceivedController(this);
        loader.setController(debtBankAccountReceivedController);
        
        Region page = loader.load();
        
        dialogStage = new Stage();
        dialogStage.initModality(Modality.WINDOW_MODAL);
        dialogStage.initOwner(debtController.dialogStageApproved);
        
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
            Logger.getLogger(DebtApprovedController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
    //  initDataCompanyBalanceBankAccountPopup(); 
      initFormDataApproved();
      dataDebtApprovedHandle();
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    public DebtApprovedController(DebtController parentController){
       debtController = parentController;
    }
    
    public final DebtController debtController;
    
     public FDebtManager getService(){
       return debtController.getService();
    }
}
