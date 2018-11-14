/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hotelfx.view.feature_customer.customer;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXCComboBoxPopup;
import com.jfoenix.controls.JFXCComboBoxTablePopup;
import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXRadioButton;
import hotelfx.helper.ClassMessage;
import hotelfx.helper.ClassPrinter;
import hotelfx.helper.PrintModel.ClassPrintCustomer;
import hotelfx.helper.PrintModel.ClassPrintDetailCustomer;
import hotelfx.helper.PrintModel.ClassPrintIdentitasCustomer;
import hotelfx.helper.PrintModel.ClassPrintKontakCustomer;
import hotelfx.helper.PrintModel.ClassPrintRekeningCustomer;
import hotelfx.persistence.model.RefCountry;
import hotelfx.persistence.model.RefCustomerType;
import hotelfx.persistence.model.TblCustomer;
import hotelfx.persistence.model.TblCustomerBankAccount;
import hotelfx.persistence.model.TblPeople;
import java.net.URL;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.embed.swing.SwingNode;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.CornerRadii;
import static javafx.scene.layout.Region.USE_COMPUTED_SIZE;
import javafx.scene.paint.Color;

/**
 *
 * @author Andreas
 */
public class CustomerReportDialogController implements Initializable {
    @FXML
    private JFXDatePicker dpStartDate;
    @FXML
    private JFXDatePicker dpEndDate;
    @FXML
    private AnchorPane ancPrint;
    @FXML
    private JFXRadioButton rdCustomerType;
    @FXML
    private JFXRadioButton rdCustomerName;
    @FXML
    private JFXRadioButton rdCountry;
    @FXML
    private AnchorPane ancCustomerType;
    @FXML
    private AnchorPane ancCustomerName;
    @FXML
    private AnchorPane ancCountry;
    @FXML
    private JFXButton btnViewPrint;
    @FXML
    private JFXButton btnCancel;
    
    JFXCComboBoxTablePopup<RefCountry>cbpCountry;
    JFXCComboBoxTablePopup<TblCustomer>cbpCustomerName;
    JFXCComboBoxTablePopup<RefCustomerType>cbpCustomerType;
    
    ToggleGroup tgglCustomerGroup;
    
    private void refreshDataItem(){
      ObservableList<RefCustomerType>customerType = FXCollections.observableArrayList(loadAllDataCustomerType());
      cbpCustomerType.setItems(customerType);
      
      ObservableList<RefCountry>countryItem  = FXCollections.observableArrayList(loadAllDataCountry());
      cbpCountry.setItems(countryItem);
    }
    
    private void customerReportDialogInitForm(){
      btnViewPrint.setOnAction((e)->{
         customerReportDialogPrintHandle(); 
      });
      
      btnCancel.setOnAction((e)->{
         customerReportDialogCancelHandle();
      });
      
      tgglCustomerGroup = new ToggleGroup();
      rdCustomerType.setToggleGroup(tgglCustomerGroup);
      rdCustomerName.setToggleGroup(tgglCustomerGroup);
      rdCountry.setToggleGroup(tgglCustomerGroup);
      
      rdCustomerType.selectedProperty().addListener((obs,oldVal,newVal)->{
           if(newVal==true){
             initPopupCustomerType();
           }
           else{
             cbpCustomerType.setVisible(false);
           }
      });
     
      rdCountry.selectedProperty().addListener((obs,oldVal,newVal)->{
           if(newVal==true){
             initPopupCountry();
           }
           else{
             cbpCountry.setVisible(false);
           }
      });
      
      rdCustomerName.selectedProperty().addListener((obs,oldVal,newVal)->{
           if(newVal==true){
             initPopupCustomer();
           }
           else{
             cbpCustomerName.setVisible(false);
           }
      });
      
      //initPopupCountry();
      
      
    }
    
    private void initPopupCustomerType(){
     TableView<RefCustomerType>tblCustomerType = new TableView();
     TableColumn<RefCustomerType,String>customerType = new TableColumn("Tipe Customer");
     customerType.setMinWidth(170);
     customerType.setCellValueFactory(cellData -> cellData.getValue().typeNameProperty());
     tblCustomerType.getColumns().addAll(customerType);
     ObservableList<RefCustomerType>customerTypeItem = FXCollections.observableArrayList();
     customerTypeItem.setAll(loadAllDataCustomerType());
     System.out.println(customerTypeItem.size());
     //setFunctionInitPopup(cbpCustomerType,tblCustomerType,customerTypeItem,"tblPeople","Tipe Customer *",500,300); 
     cbpCustomerType = new JFXCComboBoxTablePopup(RefCustomerType.class,tblCustomerType,customerTypeItem,"","Tipe Customer *",true,500,300);
     cbpCustomerType.setItems(customerTypeItem);
     cbpCustomerType.setLabelFloat(false);
     
     ancCustomerType.getChildren().clear();
     AnchorPane.setTopAnchor(cbpCustomerType,0.0);
     AnchorPane.setBottomAnchor(cbpCustomerType,0.0);
     AnchorPane.setLeftAnchor(cbpCustomerType,0.0);
     AnchorPane.setRightAnchor(cbpCustomerType,0.0);
     ancCustomerType.getChildren().add(cbpCustomerType);
    }
    
      private void setFunctionInitPopup(JFXCComboBoxPopup cbp, TableView table, ObservableList items, String namedFilter, String promptText, double prefWidth, double prefHeight) {
        table.getSelectionModel().selectedIndexProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal.intValue() != -1) {
                cbp.valueProperty().set(table.getItems().get(newVal.intValue()));
                //System.out.println(newVal);
            }
            cbp.hide();
        });

        cbp.setPropertyNameForFiltered(namedFilter);
        cbp.setItems(items);
        
        cbp.setOnShowing((e) -> {
            table.getSelectionModel().clearSelection();
        });

        table.itemsProperty().bind(cbp.filteredItemsProperty());

        JFXButton button = new JFXButton("▾");
        button.setOnMouseClicked((e) -> cbp.show());

        BorderPane content = new BorderPane(new JFXButton("SHOW"));

        content.setPrefSize(prefWidth, prefHeight);
        content.setBackground(new Background(new BackgroundFill(Color.WHITE, CornerRadii.EMPTY, Insets.EMPTY)));
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
      
    private void initPopupCountry(){ 
     TableView<RefCountry>tblCountry = new TableView();
     TableColumn<RefCountry,String>country = new TableColumn("Negara");
     country.setMinWidth(160);
     country.setCellValueFactory(cellData -> cellData.getValue().countryNameProperty());
     tblCountry.getColumns().addAll(country);
     ObservableList<RefCountry>countryItem = FXCollections.observableArrayList(loadAllDataCountry());
     cbpCountry = new JFXCComboBoxTablePopup(RefCountry.class,tblCountry,countryItem,"","Negara *",true,500,300);
     cbpCountry.setItems(countryItem);
     cbpCountry.setLabelFloat(false);
     
     ancCountry.getChildren().clear();
     AnchorPane.setTopAnchor(cbpCountry,0.0);
     AnchorPane.setBottomAnchor(cbpCountry,0.0);
     AnchorPane.setLeftAnchor(cbpCountry,0.0);
     AnchorPane.setRightAnchor(cbpCountry,0.0);
     ancCountry.getChildren().add(cbpCountry);
    }
    
      private void initPopupCustomer(){ 
     TableView<TblCustomer>tblCustomer = new TableView();
      TableColumn<TblCustomer,String>customerId = new TableColumn("ID Customer");
      customerId.setMinWidth(100);
     customerId.setCellValueFactory(cellData -> cellData.getValue().codeCustomerProperty());
     TableColumn<TblCustomer,String>customerName = new TableColumn("Nama Customer");
     customerName.setMinWidth(160);
     customerName.setCellValueFactory((TableColumn.CellDataFeatures<TblCustomer,String>param)
      ->Bindings.createStringBinding(()->param.getValue().getTblPeople().getFullName(),param.getValue().tblPeopleProperty()));
     tblCustomer.getColumns().addAll(customerId,customerName);
     ObservableList<TblCustomer>customerItem = FXCollections.observableArrayList(loadAllDataPeople());
     cbpCustomerName = new JFXCComboBoxTablePopup(TblCustomer.class,tblCustomer,customerItem,"","Nama Customer *",false,500,300);
     cbpCustomerName.setItems(customerItem);
     cbpCustomerName.setLabelFloat(false);
     
     ancCustomerName.getChildren().clear();
     AnchorPane.setTopAnchor(cbpCustomerName,0.0);
     AnchorPane.setBottomAnchor(cbpCustomerName,0.0);
     AnchorPane.setLeftAnchor(cbpCustomerName,0.0);
     AnchorPane.setRightAnchor(cbpCustomerName,0.0);
     ancCustomerName.getChildren().add(cbpCustomerName);
    }
      
    private List<RefCustomerType>loadAllDataCustomerType(){
       List<RefCustomerType>list = new ArrayList(); 
       RefCustomerType customerType = new RefCustomerType();
       customerType.setIdtype(3);
       customerType.setTypeName("Semua Tipe Customer");
       list.add(customerType);
       list.addAll(customerController.getService().getAllDataCustomerType());
       return list;
    }
    
    private List<RefCountry>loadAllDataCountry(){
       List<RefCountry>list = new ArrayList();
       RefCountry country = new RefCountry();
       country.setIdcountry(0);
       country.setCountryName("Semua Negara");
       list.add(country);
       list.addAll(customerController.getService().getAllDataPeopleCountry());
       return list;
    }
    
     private List<TblCustomer>loadAllDataPeople(){
       List<TblCustomer>list = new ArrayList();
       TblCustomer customer = new TblCustomer();
       TblPeople people = new TblPeople();
       people.setIdpeople(0);
       people.setFullName("Semua Customer");
       customer.setIdcustomer(0);
       customer.setTblPeople(people);
       
       list.add(customer);
       list.addAll(customerController.getService().getAllDataCustomer());
       return list;
    }
     
    private SwingNode printCustomerReport(RefCustomerType customerType,TblCustomer customerName,RefCountry country){
      
        //System.out.println(">>"+customerName.getCodeCustomer());
      List<TblCustomer>listCustomer = customerController.getService().getAllDataCustomerForPrint(Date.valueOf(dpStartDate.getValue()),Date.valueOf(dpEndDate.getValue()),customerType,customerName,country);
        System.out.println("list customer size : "+listCustomer.size());
      List<ClassPrintCustomer>listPrintCustomer = new ArrayList();
      
      for(TblCustomer getCustomer : listCustomer){  
         ClassPrintCustomer printCustomer = new ClassPrintCustomer();
         printCustomer.setIdCustomer(getCustomer.getIdcustomer());
         printCustomer.setInfoCustomer(getCustomer.getCodeCustomer()+" - "+getCustomer.getTblPeople().getFullName());
         printCustomer.setTipeCustomer(getCustomer.getRefCustomerType().getTypeName());
         printCustomer.setTanggalRegistrasi(new SimpleDateFormat("dd MMMM yyyy").format(getCustomer.getRegistrationDate()));
         printCustomer.setNoIdentitas(getCustomer.getTblPeople().getRefPeopleIdentifierType().getTypeName()+":"+getCustomer.getTblPeople().getCodePeople());
         //printCustomer.setNamaCustomer(getCustomer.getTblPeople().getFullName());
         printCustomer.setAgama(getCustomer.getTblPeople().getRefPeopleReligion()!=null ? getCustomer.getTblPeople().getRefPeopleReligion().getReligionName():"-");
         printCustomer.setStatusPerkawinan(getCustomer.getTblPeople().getRefPeopleStatus()!=null ? getCustomer.getTblPeople().getRefPeopleStatus().getStatusName() : "-");
         printCustomer.setJenisKelamin(getCustomer.getTblPeople().getRefPeopleGender()!=null ? getCustomer.getTblPeople().getRefPeopleGender().getGenderName() : "-");
         printCustomer.setTempatTanggalLahir((getCustomer.getTblPeople().getBirthPlace()!=null && getCustomer.getTblPeople().getBirthDate()!=null ? getCustomer.getTblPeople().getBirthPlace()+", "+new SimpleDateFormat("dd MMMM yyyy").format(getCustomer.getTblPeople().getBirthDate()) : "-"));
         printCustomer.setNoTelepon(getCustomer.getTblPeople().getHpnumber()!=null ? getCustomer.getTblPeople().getHpnumber() : "-");
         printCustomer.setEmail(getCustomer.getTblPeople().getEmail()!=null ? getCustomer.getTblPeople().getEmail() : "-");
            
         List<TblCustomerBankAccount>listCustomerBankAccount = customerController.getService().getAllDataCustomerBankAccount(getCustomer.getIdcustomer());
           List<ClassPrintRekeningCustomer>listRekeningCustomer = new ArrayList();
           for(TblCustomerBankAccount getBankAccount : listCustomerBankAccount){
              ClassPrintRekeningCustomer printRekeningCustomer = new ClassPrintRekeningCustomer();
              printRekeningCustomer.setNoRekening(getBankAccount.getTblBankAccount().getCodeBankAccount());
              printRekeningCustomer.setBank(getBankAccount.getTblBankAccount().getTblBank().getBankName());
              printRekeningCustomer.setPemegangRekening(getBankAccount.getTblBankAccount().getBankAccountHolderName());
              listRekeningCustomer.add(printRekeningCustomer);
            }
            
          /* if(listCustomerBankAccount.size()==0){
             ClassPrintRekeningCustomer printRekeningCustomer = new ClassPrintRekeningCustomer();
             printRekeningCustomer.setNoRekening("-");
             printRekeningCustomer.setBank("-");
             printRekeningCustomer.setPemegangRekening("-");
             listRekeningCustomer.add(printRekeningCustomer);
           }*/
           
           printCustomer.setRekeningCustomer(listRekeningCustomer);
           listPrintCustomer.add(printCustomer);
         /*List<ClassPrintDetailCustomer>listDetailCustomer = new ArrayList();
         List<ClassPrintIdentitasCustomer>listIdentitas = new ArrayList();
          List<ClassPrintKontakCustomer>listKontak = new ArrayList();
         if(getCustomer.getIdcustomer() == printCustomer.getIdCustomer()){
            TblCustomer customer = customerController.getService().getCustomer(printCustomer.getIdCustomer());
            ClassPrintIdentitasCustomer identitasCustomer = new ClassPrintIdentitasCustomer();
            identitasCustomer.setNoIdentitas(customer.getTblPeople().getRefPeopleIdentifierType().getTypeName()+":"+customer.getTblPeople().getCodePeople());
            identitasCustomer.setNamaCustomer(customer.getTblPeople().getFullName());
            identitasCustomer.setAgama(customer.getTblPeople().getRefPeopleReligion()!=null ? getCustomer.getTblPeople().getRefPeopleReligion().getReligionName():"-");
            identitasCustomer.setStatusPerkawinan(customer.getTblPeople().getRefPeopleStatus()!=null ? getCustomer.getTblPeople().getRefPeopleStatus().getStatusName() : "-");
            identitasCustomer.setJenisKelamin(customer.getTblPeople().getRefPeopleGender()!=null ? getCustomer.getTblPeople().getRefPeopleGender().getGenderName() : "-");
            identitasCustomer.setTempatTanggalLahir((customer.getTblPeople().getBirthPlace()!=null && getCustomer.getTblPeople().getBirthDate()!=null ? getCustomer.getTblPeople().getBirthPlace()+", \n"+new SimpleDateFormat("dd MMMM yyyy").format(getCustomer.getTblPeople().getBirthDate()) : "-"));
            
            ClassPrintKontakCustomer kontakCustomer = new ClassPrintKontakCustomer();
            kontakCustomer.setAlamat(customer.getTblPeople().getAddress()!=null?customer.getTblPeople().getAddress()+","+customer.getTblPeople().getTown()+(customer.getTblPeople().getRegion()!=null ?","+customer.getTblPeople().getRegion() :"") : "-");
            kontakCustomer.setNegara(customer.getTblPeople().getRefCountry()!=null ? customer.getTblPeople().getRefCountry().getCountryName():"-");
            kontakCustomer.setNoTelepon(customer.getTblPeople().getHpnumber()!=null ? customer.getTblPeople().getHpnumber() : "-");
            kontakCustomer.setEmail(customer.getTblPeople().getEmail()!=null ? customer.getTblPeople().getEmail() : "-");
            
            List<TblCustomerBankAccount>listCustomerBankAccount = customerController.getService().getAllDataCustomerBankAccount(printCustomer.getIdCustomer());
            List<ClassPrintRekeningCustomer>listRekeningCustomer = new ArrayList();
           for(TblCustomerBankAccount getBankAccount : listCustomerBankAccount){
              ClassPrintRekeningCustomer printRekeningCustomer = new ClassPrintRekeningCustomer();
              printRekeningCustomer.setNoRekening(getBankAccount.getTblBankAccount().getCodeBankAccount());
              printRekeningCustomer.setBank(getBankAccount.getTblBankAccount().getTblBank().getBankName());
              printRekeningCustomer.setPemegangRekening(getBankAccount.getTblBankAccount().getBankAccountHolderName());
              listRekeningCustomer.add(printRekeningCustomer);
            }
            
           if(listCustomerBankAccount.size()==0){
             ClassPrintRekeningCustomer printRekeningCustomer = new ClassPrintRekeningCustomer();
             printRekeningCustomer.setNoRekening("-");
             printRekeningCustomer.setBank("-");
             printRekeningCustomer.setPemegangRekening("-");
             listRekeningCustomer.add(printRekeningCustomer);
           }
            
            listKontak.add(kontakCustomer);
            listIdentitas.add(identitasCustomer);
            
            ClassPrintDetailCustomer detailCustomer = new ClassPrintDetailCustomer();
            detailCustomer.setIdentitasCustomer(listIdentitas);
            detailCustomer.setKontakCustomer(listKontak);
            detailCustomer.setRekeningCustomer(listRekeningCustomer);
           
            listDetailCustomer.add(detailCustomer);
            printCustomer.setDetailCustomer(listDetailCustomer);
         }
         listPrintCustomer.add(printCustomer);
         
        
         
         /*TblCustomer customer = new TblCustomer();
        
         
         ClassPrintIdentitasCustomer printIdentitasCustomer = new ClassPrintIdentitasCustomer();
         customer = customerController.getService().getCustomer(printCustomer.getIdCustomer());
         printIdentitasCustomer.setNoIdentitas(customer.getTblPeople().getRefPeopleIdentifierType().getTypeName()+":"+getCustomer.getTblPeople().getCodePeople());
         printIdentitasCustomer.setNamaCustomer(customer.getTblPeople().getFullName());
         printIdentitasCustomer.setAgama(customer.getTblPeople().getRefPeopleReligion()!=null ? getCustomer.getTblPeople().getRefPeopleReligion().getReligionName():"-");
         printIdentitasCustomer.setStatusPerkawinan(customer.getTblPeople().getRefPeopleStatus()!=null ? getCustomer.getTblPeople().getRefPeopleStatus().getStatusName() : "-");
         printIdentitasCustomer.setJenisKelamin(customer.getTblPeople().getRefPeopleGender()!=null ? getCustomer.getTblPeople().getRefPeopleGender().getGenderName() : "-");
         printIdentitasCustomer.setTempatTanggalLahir((customer.getTblPeople().getBirthPlace()!=null && getCustomer.getTblPeople().getBirthDate()!=null ? getCustomer.getTblPeople().getBirthPlace()+", \n"+getCustomer.getTblPeople().getBirthDate() : "-"));
         listIdentitasCustomer.add(printIdentitasCustomer);
           
           ClassPrintKontakCustomer printKontakCustomer = new ClassPrintKontakCustomer();
           // printKontakCustomer.setIdCustomer(getCustomer.getIdcustomer());
            printKontakCustomer.setAlamat("Jalan: "+(customer.getTblPeople().getAddress()!=null ? getCustomer.getTblPeople().getAddress() : "-")+"\n"+
                                        "RT/RW:"+(customer.getTblPeople().getRw()!=null && getCustomer.getTblPeople().getRt()!=null ? getCustomer.getTblPeople().getRt()+"/"+getCustomer.getTblPeople().getRw() : "-")+"\n"+
                                        "Kelurahan: "+(customer.getTblPeople().getKelurahan()!=null ? getCustomer.getTblPeople().getKelurahan() : "-") + "\n" +
                                        "Kecamatan: "+(customer.getTblPeople().getKecamatan()!=null ? getCustomer.getTblPeople().getKecamatan() : "-") + "\n" +
                                        "Kota: " +(customer.getTblPeople().getTown()!=null ? getCustomer.getTblPeople().getTown() : "-")+"\n"+
                                        "Provinsi: "+(customer.getTblPeople().getRegion()!=null ? getCustomer.getTblPeople().getRegion() : "-")+"\n"+
                                        "Negara: "+(customer.getTblPeople().getRefCountry()!=null ? getCustomer.getTblPeople().getRefCountry().getCountryName():"-"));
            printKontakCustomer.setNoTelepon(customer.getTblPeople().getHpnumber());
            printKontakCustomer.setEmail(customer.getTblPeople().getEmail());
            listKontakCustomer.add(printKontakCustomer);
            
           //System.out.println(printCustomer.getKontakCustomer().size());
           List<TblCustomerBankAccount>listCustomerBankAccount = customerController.getService().getAllDataCustomerBankAccount(getCustomer.getIdcustomer());
           List<ClassPrintRekeningCustomer>listRekeningCustomer = new ArrayList();
           for(TblCustomerBankAccount getBankAccount : listCustomerBankAccount){
              ClassPrintRekeningCustomer printRekeningCustomer = new ClassPrintRekeningCustomer();
              printRekeningCustomer.setNoRekening(getBankAccount.getTblBankAccount().getCodeBankAccount());
              printRekeningCustomer.setBank(getBankAccount.getTblBankAccount().getTblBank().getBankName());
              printRekeningCustomer.setPemegangRekening(getBankAccount.getTblBankAccount().getBankAccountHolderName());
              listRekeningCustomer.add(printRekeningCustomer);
            }
            
           if(listCustomerBankAccount.size()==0){
             ClassPrintRekeningCustomer printRekeningCustomer = new ClassPrintRekeningCustomer();
             printRekeningCustomer.setNoRekening("-");
             printRekeningCustomer.setBank("-");
             printRekeningCustomer.setPemegangRekening("-");
             listRekeningCustomer.add(printRekeningCustomer);
           }
           
          printCustomer.setIdentitasCustomer(listIdentitasCustomer); 
          printCustomer.setKontakCustomer(listKontakCustomer);
          printCustomer.setRekeningCustomer(listRekeningCustomer);
          System.out.println(printCustomer.getInfoCustomer()+"-"+printCustomer.getRekeningCustomer().size());*/
                 
       }
        System.out.println("hsl print>>"+listPrintCustomer.size());
        
      
        return ClassPrinter.printCustomer(listPrintCustomer,Date.valueOf(dpStartDate.getValue()),Date.valueOf(dpEndDate.getValue()),customerType,customerName,country);
    }
    
    private void customerReportDialogPrintHandle(){
       if(checkDataInput()){
           SwingNode swingNode = new SwingNode();
           if(rdCustomerType.isSelected()){
             swingNode = printCustomerReport(cbpCustomerType.getValue(),null,null);
           }
       
           if(rdCountry.isSelected()){
             swingNode = printCustomerReport(null,null, cbpCountry.getValue());
            }
       
           if(rdCustomerName.isSelected()){
             swingNode = printCustomerReport(null,cbpCustomerName.getValue(),null);
           }
       
           if(!rdCustomerType.isSelected() && !rdCustomerName.isSelected() && !rdCountry.isSelected()){
             swingNode = printCustomerReport(null,null,null);
            }
       
            ancPrint.getChildren().clear();
            AnchorPane.setTopAnchor(swingNode,0.0);
            AnchorPane.setBottomAnchor(swingNode,0.0);
            AnchorPane.setLeftAnchor(swingNode,0.0);
            AnchorPane.setRightAnchor(swingNode,0.0);
            ancPrint.getChildren().add(swingNode);
       }
       else{
         ClassMessage.showWarningInputDataMessage(errDataInput,customerController.dialogStagePrint);
       }
       
    }
    
    String errDataInput;
    private boolean checkDataInput(){
       errDataInput  = "";
       boolean check = true;
       if(dpStartDate.getValue()==null){
          errDataInput += ClassMessage.defaultErrorNullValueMessage+"\n";
          check = false;
       }
       
       if(dpEndDate.getValue()==null){
          errDataInput +=ClassMessage.defaultErrorNullValueMessage+"\n";
          check = false;
       }
       
       if(rdCustomerType.isSelected()){
           if(cbpCustomerType.getValue()==null){
             errDataInput +=ClassMessage.defaultErrorNullValueMessage+"\n";
             check = false;
           } 
       }
       
       if(rdCustomerName.isSelected()){
           if(cbpCustomerName.getValue()==null){
              errDataInput +=ClassMessage.defaultErrorNullValueMessage+"\n";
              check = false;
           }
       }
       
       if(rdCountry.isSelected()){
           if(cbpCountry.getValue()==null){
             errDataInput += ClassMessage.defaultErrorNullValueMessage+"\n";
             check = false;
           }
       }
       
       return check;
    }
    
    private void customerReportDialogCancelHandle(){
      customerController.dialogStagePrint.close();
    }
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
      
      customerReportDialogInitForm();  
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    public CustomerReportDialogController(CustomerController customerController){
      this.customerController = customerController;
    }
    
    private final CustomerController customerController;
}
