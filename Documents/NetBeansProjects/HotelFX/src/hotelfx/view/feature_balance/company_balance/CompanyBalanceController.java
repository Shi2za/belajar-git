/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hotelfx.view.feature_balance.company_balance;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXCComboBoxTablePopup;
import com.jfoenix.controls.JFXRadioButton;
import com.jfoenix.controls.JFXTextField;
import hotelfx.HotelFX;
import hotelfx.helper.ClassFormatter;
import hotelfx.helper.ClassMessage;
import hotelfx.helper.ClassTableWithControl;
import hotelfx.helper.ClassViewSetting;
import hotelfx.persistence.model.TblBank;
import hotelfx.persistence.model.TblBankAccount;
import hotelfx.persistence.model.TblCompanyBalance;
import hotelfx.persistence.model.TblCompanyBalanceBankAccount;
import hotelfx.persistence.model.TblCompanyBalanceTransferReceived;
import hotelfx.persistence.service.FBalanceManager;
import hotelfx.view.feature_balance.FeatureBalanceController;
import insidefx.undecorator.Undecorator;
import java.io.IOException;
import java.math.BigDecimal;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import javafx.beans.binding.Bindings;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Region;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

/**
 * FXML Controller class
 *
 * @author ANDRI
 */
public class CompanyBalanceController implements Initializable {

    @FXML
    private AnchorPane ancFormCompanyBalance;

    @FXML
    private GridPane gpFormDataCompanyBalance;

    @FXML
    private JFXTextField txtBalanceName;

    @FXML
    private JFXTextField txtBalanceNominal;

    @FXML
    private AnchorPane ancBankAccountSourceLayout;
    private JFXCComboBoxTablePopup<TblCompanyBalanceBankAccount> cbpCompanyBalanceBankAccountSource;

    @FXML
    private AnchorPane ancBankAccountDestinationLayout;
    private JFXCComboBoxTablePopup<TblCompanyBalanceBankAccount> cbpCompanyBalanceBankAccountDestination;

    private ToggleGroup groupBalance;
    
    @FXML
    private AnchorPane ancCompanyBalanceLayout;
    private JFXCComboBoxTablePopup<TblCompanyBalance>cbpCompanyBalance;
/*  @FXML
    private JFXRadioButton rdbBalanceHotel; */

/*    @FXML
    private JFXRadioButton rdbBalanceBackOffice; */

/*    @FXML
    private JFXRadioButton rdbBalanceCashier; */

    @FXML
    private JFXTextField txtTransferNominal;

    @FXML
    private JFXButton btnTransfer;

    private void initFormDataCompanyBalance() {

        initDataPopup();
        
        btnTransfer.setTooltip(new Tooltip("Transfer"));
        btnTransfer.setOnAction((e) -> {
            dataCompanyBalanceSaveHandle();
        });

        cbpCompanyBalance.valueProperty().addListener((obs,oldVal,newVal)->{
            if(newVal.getIdbalance()==1){
              cbpCompanyBalanceBankAccountDestination.setVisible(true);
            }
            else{
              cbpCompanyBalanceBankAccountDestination.setVisible(false);
            }
        });
    /*    groupBalance = new ToggleGroup();
        rdbBalanceBackOffice.setToggleGroup(groupBalance);
     //   rdbBalanceCashier.setToggleGroup(groupBalance);
        rdbBalanceHotel.setToggleGroup(groupBalance);

        rdbBalanceHotel.selectedProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                cbpCompanyBalanceBankAccountDestination.setVisible(newVal);
            }
        }); */
        cbpCompanyBalanceBankAccountDestination.setVisible(false);

        initImportantFieldColor();
        
        initNumbericField();
        
    }
    
    private void initImportantFieldColor() {
        ClassViewSetting.setImportantField(
            //    rdbBalanceHotel,
            //    rdbBalanceCashier,
            //    rdbBalanceBackOffice,
                cbpCompanyBalanceBankAccountSource,
                cbpCompanyBalanceBankAccountDestination,
                txtTransferNominal);
    }
    
    private void initNumbericField() {
        ClassFormatter.setToNumericField(
                "BigDecimal",
                txtBalanceNominal,
                txtTransferNominal);
    }

    private TblCompanyBalance selectedBalance;

    private final ObjectProperty<BigDecimal> transferNominal = new SimpleObjectProperty<>(new BigDecimal("0"));

    private TblCompanyBalance loadDataCompanyBalance() {
        return parentController.getFBalanceManager().getDataCompanyBalance();
    }

    private void initDataPopup() {
        //Company Balance - Bank Account (source)
        TableView<TblCompanyBalanceBankAccount> tableCompanyBalanceBankAccountSource = new TableView<>();

        TableColumn<TblCompanyBalanceBankAccount, String> bankNameSource = new TableColumn("Bank");
        bankNameSource.setCellValueFactory((TableColumn.CellDataFeatures<TblCompanyBalanceBankAccount, String> param)
                -> Bindings.createStringBinding(() -> param.getValue().getTblBankAccount().getTblBank().getBankName(), param.getValue().getTblBankAccount().tblBankProperty()));
        bankNameSource.setMinWidth(140);

        TableColumn<TblCompanyBalanceBankAccount, String> bankAccountSource = new TableColumn("Nomor Rekening");
        bankAccountSource.setCellValueFactory((TableColumn.CellDataFeatures<TblCompanyBalanceBankAccount, String> param)
                -> Bindings.createStringBinding(() -> param.getValue().getTblBankAccount().getCodeBankAccount(), param.getValue().getTblBankAccount().codeBankAccountProperty()));
        bankAccountSource.setMinWidth(140);

        TableColumn<TblCompanyBalanceBankAccount, String> bankAccountHolderNameSource = new TableColumn("Nama Pemegang Rekening");
        bankAccountHolderNameSource.setCellValueFactory((TableColumn.CellDataFeatures<TblCompanyBalanceBankAccount, String> param)
                -> Bindings.createStringBinding(() -> param.getValue().getTblBankAccount().getBankAccountHolderName(), param.getValue().getTblBankAccount().bankAccountHolderNameProperty()));
        bankAccountHolderNameSource.setMinWidth(200);

        TableColumn<TblCompanyBalanceBankAccount, String> nominalSource = new TableColumn("Nominal");
        nominalSource.setCellValueFactory((TableColumn.CellDataFeatures<TblCompanyBalanceBankAccount, String> param)
                -> Bindings.createStringBinding(() -> ClassFormatter.currencyFormat.cFormat(param.getValue().getCompanyBalanceBankAccountNominal()), param.getValue().companyBalanceBankAccountNominalProperty()));
        nominalSource.setMinWidth(140);

        tableCompanyBalanceBankAccountSource.getColumns().addAll(bankNameSource, bankAccountSource, bankAccountHolderNameSource, nominalSource);

        ObservableList<TblCompanyBalanceBankAccount> companyBalanceBankAccountSourceItems = FXCollections.observableArrayList(loadAllDataCompanyBalanceBankAccount(null));

        cbpCompanyBalanceBankAccountSource = new JFXCComboBoxTablePopup<>(
                TblCompanyBalanceBankAccount.class, tableCompanyBalanceBankAccountSource, companyBalanceBankAccountSourceItems, "", "No. Rek (Sumber) *", true, 640, 320
        );

        //Company Balance - Bank Account (destination)
        TableView<TblCompanyBalanceBankAccount> tableCompanyBalanceBankAccountDestination = new TableView<>();

        TableColumn<TblCompanyBalanceBankAccount, String> bankNameDestination = new TableColumn("Bank");
        bankNameDestination.setCellValueFactory((TableColumn.CellDataFeatures<TblCompanyBalanceBankAccount, String> param)
                -> Bindings.createStringBinding(() -> param.getValue().getTblBankAccount().getTblBank().getBankName(), param.getValue().getTblBankAccount().tblBankProperty()));
        bankNameDestination.setMinWidth(140);

        TableColumn<TblCompanyBalanceBankAccount, String> bankAccountDestination = new TableColumn("Nomor Rekening");
        bankAccountDestination.setCellValueFactory((TableColumn.CellDataFeatures<TblCompanyBalanceBankAccount, String> param)
                -> Bindings.createStringBinding(() -> param.getValue().getTblBankAccount().getCodeBankAccount(), param.getValue().getTblBankAccount().codeBankAccountProperty()));
        bankAccountDestination.setMinWidth(140);

        TableColumn<TblCompanyBalanceBankAccount, String> bankAccountHolderNameDestination = new TableColumn("Nama Pemegang Rekening");
        bankAccountHolderNameDestination.setCellValueFactory((TableColumn.CellDataFeatures<TblCompanyBalanceBankAccount, String> param)
                -> Bindings.createStringBinding(() -> param.getValue().getTblBankAccount().getBankAccountHolderName(), param.getValue().getTblBankAccount().bankAccountHolderNameProperty()));
        bankAccountHolderNameDestination.setMinWidth(200);

        TableColumn<TblCompanyBalanceBankAccount, String> nominalDestination = new TableColumn("Nominal");
        nominalDestination.setCellValueFactory((TableColumn.CellDataFeatures<TblCompanyBalanceBankAccount, String> param)
                -> Bindings.createStringBinding(() -> ClassFormatter.currencyFormat.cFormat(param.getValue().getCompanyBalanceBankAccountNominal()), param.getValue().companyBalanceBankAccountNominalProperty()));
        nominalDestination.setMinWidth(140);

        tableCompanyBalanceBankAccountDestination.getColumns().addAll(bankNameDestination, bankAccountDestination, bankAccountHolderNameDestination, nominalDestination);

        ObservableList<TblCompanyBalanceBankAccount> companyBalanceBankAccountDestinationItems = FXCollections.observableArrayList(loadAllDataCompanyBalanceBankAccount(null));
        
        
        cbpCompanyBalanceBankAccountDestination = new JFXCComboBoxTablePopup<>(
                TblCompanyBalanceBankAccount.class, tableCompanyBalanceBankAccountDestination, companyBalanceBankAccountDestinationItems, "", "No. Rek (Tujuan) *", true, 640, 320
        );
        
        //Balance Type
        TableView<TblCompanyBalance>tblCompanyBalance = new TableView();
        TableColumn<TblCompanyBalance,String>balanceName = new TableColumn("Kas");
        balanceName.setMinWidth(140);
        balanceName.setCellValueFactory(cellData -> cellData.getValue().balanceNameProperty());
        tblCompanyBalance.getColumns().addAll(balanceName);
        ObservableList<TblCompanyBalance>listCompanyBalanceItems = FXCollections.observableArrayList(loadAllDataCompanyBalance());
        cbpCompanyBalance = new JFXCComboBoxTablePopup(TblCompanyBalance.class,tblCompanyBalance,listCompanyBalanceItems,"","Kas (Tujuan)*",true,400,300);
        //attached to grid-pane
        ancBankAccountSourceLayout.getChildren().clear();
        AnchorPane.setBottomAnchor(cbpCompanyBalanceBankAccountSource, 0.0);
        AnchorPane.setLeftAnchor(cbpCompanyBalanceBankAccountSource, 0.0);
        AnchorPane.setRightAnchor(cbpCompanyBalanceBankAccountSource, 0.0);
        AnchorPane.setTopAnchor(cbpCompanyBalanceBankAccountSource, 0.0);
        ancBankAccountSourceLayout.getChildren().add(cbpCompanyBalanceBankAccountSource);
        
        ancBankAccountDestinationLayout.getChildren().clear();
        AnchorPane.setBottomAnchor(cbpCompanyBalanceBankAccountDestination, 0.0);
        AnchorPane.setLeftAnchor(cbpCompanyBalanceBankAccountDestination, 0.0);
        AnchorPane.setRightAnchor(cbpCompanyBalanceBankAccountDestination, 0.0);
        AnchorPane.setTopAnchor(cbpCompanyBalanceBankAccountDestination, 0.0);
        ancBankAccountDestinationLayout.getChildren().add(cbpCompanyBalanceBankAccountDestination);
        
        ancCompanyBalanceLayout.getChildren().clear();
        AnchorPane.setBottomAnchor(cbpCompanyBalance, 0.0);
        AnchorPane.setLeftAnchor(cbpCompanyBalance, 0.0);
        AnchorPane.setRightAnchor(cbpCompanyBalance, 0.0);
        AnchorPane.setTopAnchor(cbpCompanyBalance, 0.0);
        ancCompanyBalanceLayout.getChildren().add(cbpCompanyBalance);
    }

    private void refreshDataPopup() {
        //Company Balance - Bank Account (source)
        ObservableList<TblCompanyBalanceBankAccount> companyBalanceBankAccountSourceItems = FXCollections.observableArrayList(loadAllDataCompanyBalanceBankAccount(selectedBalance));
        cbpCompanyBalanceBankAccountSource.setItems(companyBalanceBankAccountSourceItems);

        //Company Balance - Bank Account (destination)
        ObservableList<TblCompanyBalanceBankAccount> companyBalanceBankAccountDestinationItems = FXCollections.observableArrayList(loadAllDataCompanyBalanceBankAccount(selectedBalance));
        cbpCompanyBalanceBankAccountDestination.setItems(companyBalanceBankAccountDestinationItems);
    }
    
     
    private List<TblCompanyBalance>loadAllDataCompanyBalance(){
       List<TblCompanyBalance>list = parentController.getFBalanceManager().getAllDataCompanyBalance();
       for(int i = 0; i<list.size();i++){
           if(list.get(i).getIdbalance()==3){
             list.remove(list.get(i));
           }
           
           if(list.get(i).getIdbalance()==4){
              list.remove(list.get(i));
           }
       }
      return list;
    }
    
    private TblCompanyBalanceTransferReceived selectedData;
    
    private void setSelectedDataToInputForm() {
         selectedBalance = loadDataCompanyBalance();
        System.out.println("selected data : " + selectedBalance);

        txtBalanceName.textProperty().bindBidirectional(selectedBalance.balanceNameProperty());
        Bindings.bindBidirectional(txtBalanceNominal.textProperty(), selectedBalance.balanceNominalProperty(), new ClassFormatter.CBigDecimalStringConverter());

        refreshDataPopup();
        
        cbpCompanyBalance.valueProperty().bindBidirectional(selectedData.tblCompanyBalanceByIdcompanyBalanceReceivedProperty());
        cbpCompanyBalanceBankAccountSource.valueProperty().bindBidirectional(selectedData.tblCompanyBalanceBankAccountByIdcbbankAccountSenderProperty());
        cbpCompanyBalanceBankAccountDestination.valueProperty().bindBidirectional(selectedData.tblCompanyBalanceBankAccountByIdcbbankAccountReceivedProperty());
        
        cbpCompanyBalanceBankAccountSource.hide();
        cbpCompanyBalanceBankAccountDestination.hide();

        transferNominal.set(new BigDecimal("0"));

       // rdbBalanceBackOffice.setSelected(false);
    //    rdbBalanceCashier.setSelected(false);

        Bindings.bindBidirectional(txtTransferNominal.textProperty(),selectedData.nominalProperty(), new ClassFormatter.CBigDecimalStringConverter());

        //data company balance - bank account
        initTableDataCompanyBalanceBankAccount();

    }
    
    private void dataCompanyBalanceTransferHandle(){
      selectedBalance = loadDataCompanyBalance();
      selectedData = new TblCompanyBalanceTransferReceived();
      selectedData.setTblCompanyBalanceByIdcompanyBalanceSender(selectedBalance);
      setSelectedDataToInputForm();
    }
    
    private void dataCompanyBalanceSaveHandle(){
//        System.out.println("hsl >>"+selectedData.getTblCompanyBalanceBankAccountByIdcbbankAccountReceived().getTblBankAccount().getBankAccountHolderName());
        if (checkDataInputDataTransfer()){
            Alert alert = HotelFX.showAlertConfirm(Alert.AlertType.CONFIRMATION, "KONFIRMASI", "Apakah anda yakin untuk melakukan transfer data?", "");
            if (alert.getResult() == ButtonType.OK) {
              TblCompanyBalanceTransferReceived dummySelectedData = new TblCompanyBalanceTransferReceived(selectedData);
            //    System.out.println("hsl dummy :"+dummySelectedData.getTblCompanyBalanceBankAccountByIdcbbankAccountReceived().getTblBankAccount().getBankAccountHolderName());
               if(parentController.getFBalanceManager().updateDataBalanceTransferReceived(dummySelectedData)){
                 ClassMessage.showSucceedInsertingDataMessage(null,null);
                 selectedBalance.setBalanceNominal(selectedBalance.getBalanceNominal().subtract(selectedData.getNominal()));
                 tableDataCompanyBalanceBankAccount.getTableView().setItems(FXCollections.observableArrayList(loadAllDataCompanyBalanceBankAccount(selectedBalance)));
               }
               else{
                 ClassMessage.showFailedInsertingDataMessage(null,null);
               }
                //data dummy selected balance
             /*   TblCompanyBalance dummySelectedBalance = new TblCompanyBalance(selectedBalance);
                //data dummy another balance
                TblCompanyBalance dummyAnotherBalance = null;
            /*    if (rdbBalanceBackOffice.isSelected()) {  //BackOffice
                    dummyAnotherBalance = parentController.getFBalanceManager().getDataBackOfficeBalance();
                } /* else {  //Cashier
                    if (rdbBalanceCashier.isSelected()) {
                        dummyAnotherBalance = parentController.getFBalanceManager().getDataCashierBalance();
                    }
                } 
                //data dummy current balance - bank account
                TblCompanyBalanceBankAccount dummyBalanceBankAccountSource = new TblCompanyBalanceBankAccount(cbpCompanyBalanceBankAccountSource.getValue());
                TblCompanyBalanceBankAccount dummyBalanceBankAccountDestination = null;
                if (dummyAnotherBalance != null) {
                    //data dummy current balance
                    dummySelectedBalance.setBalanceNominal(dummySelectedBalance.getBalanceNominal().subtract(transferNominal.get()));
                    //data dummy current balance - bank account
                    dummyBalanceBankAccountSource.setCompanyBalanceBankAccountNominal(dummyBalanceBankAccountSource.getCompanyBalanceBankAccountNominal().subtract(transferNominal.get()));
                    //data dummy another balance
                    dummyAnotherBalance.setBalanceNominal(dummyAnotherBalance.getBalanceNominal().add(transferNominal.get()));
                } else {
                    //data dummy current balance - bank account
                    dummyBalanceBankAccountSource.setCompanyBalanceBankAccountNominal(dummyBalanceBankAccountSource.getCompanyBalanceBankAccountNominal().subtract(transferNominal.get()));
                    //data dummy another balance - bank account
                    dummyBalanceBankAccountDestination = new TblCompanyBalanceBankAccount(cbpCompanyBalanceBankAccountDestination.getValue());
                    dummyBalanceBankAccountDestination.setCompanyBalanceBankAccountNominal(dummyBalanceBankAccountDestination.getCompanyBalanceBankAccountNominal().add(transferNominal.get()));
                }
                if (parentController.getFBalanceManager().updateDataBalanceTransfer(
                        dummySelectedBalance,
                        dummyAnotherBalance,
                        dummyBalanceBankAccountSource,
                        dummyBalanceBankAccountDestination,
                        transferNominal.get())) {
                    ClassMessage.showSucceedUpdatingDataMessage("", null);
                    //refresh data input
                    setSelectedDataToInputForm();
                } else {
                    ClassMessage.showFailedUpdatingDataMessage(parentController.getFBalanceManager().getErrorMessage(), null);
                }*/
                
                
            }
        } else {
            ClassMessage.showWarningInputDataMessage(errDataInput, null);
        }
    }

    private String errDataInput;

    private boolean checkDataInputDataTransfer() {
        boolean dataInput = true;
        errDataInput = "";
        if (cbpCompanyBalanceBankAccountSource.getValue() == null) {
            dataInput = false;
            errDataInput += "No.Rek(Sumber): " + ClassMessage.defaultErrorNullValueMessage + " \n";
        } 
          /*  if (!rdbBalanceBackOffice.isSelected() && !rdbBalanceCashier.isSelected() && !rdbBalanceHotel.isSelected()) {
                dataInput = false;
                errDataInput += "Kas (Tujuan) : " + ClassMessage.defaultErrorNullValueMessage + " \n";
            }
            if (rdbBalanceHotel.isSelected()) {
                if (cbpCompanyBalanceBankAccountDestination.getValue() == null) {
                    dataInput = false;
                    errDataInput += "Kas Besar (Tujuan Nomor Rekening) : " + ClassMessage.defaultErrorNullValueMessage + " \n";
                } else {
                    if (cbpCompanyBalanceBankAccountSource.getValue().getIdrelation()
                            == cbpCompanyBalanceBankAccountDestination.getValue().getIdrelation()) {
                        dataInput = false;
                        errDataInput += "Kas Besar : Sumber dan Tujuan Nomor Rekening tidak boleh sama..!! \n";
                    }
                } */
            
        if(cbpCompanyBalance.getValue()==null){
           dataInput = false;
           errDataInput+="Kas (Tujuan): "+ClassMessage.defaultErrorNullValueMessage+"\n";
        }
        else{
           if(cbpCompanyBalance.getValue().getIdbalance()==1){
               if(cbpCompanyBalanceBankAccountDestination.getValue()==null){
                 errDataInput+="No.Rek (Tujuan) : "+ClassMessage.defaultErrorNullValueMessage+"\n";
                 dataInput = false;
               }
               else{
                   if (cbpCompanyBalanceBankAccountSource.getValue().getIdrelation()
                         == cbpCompanyBalanceBankAccountDestination.getValue().getIdrelation()) {
                        dataInput = false;
                        errDataInput += "Kas Besar : Sumber dan Tujuan Nomor Rekening tidak boleh sama..!! \n";
                    }
               }
           }
        }
            if (txtTransferNominal.getText() == null 
                    || txtTransferNominal.getText().equals("")
                    || txtTransferNominal.getText().equals("-")) {
                dataInput = false;
                errDataInput += "Nominal (Transfer) : " + ClassMessage.defaultErrorNullValueMessage + " \n";
            } else {
                if (selectedData.getNominal().compareTo(new BigDecimal("0")) < 1) {
                    dataInput = false;
                    errDataInput += "Nominal (Transfer) : " + ClassMessage.defaultErrorZeroValueMessage + " \n";
                } else {
                    if (selectedData.getNominal().compareTo(cbpCompanyBalanceBankAccountSource.getValue().getCompanyBalanceBankAccountNominal()) == 1) {
                        dataInput = false;
                        errDataInput += "Nominal (Transfer) : Tidak dapat lebih besar dari nominal kas besar (nomor rekening) yang dipilih..!! \n";
                    }
                }
            }
        
        return dataInput;
    }

    /**
     * TABLE DATA BALANCE (KAS BESAR) - BANK ACCOUNT
     */
    @FXML
    private AnchorPane tableDataCompanyBalanceBankAccountLayout;

    public ClassTableWithControl tableDataCompanyBalanceBankAccount;

    private void initTableDataCompanyBalanceBankAccount() {
        //set table
        setTableDataCompanyBalanceBankAccount();
        //set control
        setTableControlDataCompanyBalanceBankAccount();
        //set table-control to content-view
        tableDataCompanyBalanceBankAccountLayout.getChildren().clear();
        AnchorPane.setBottomAnchor(tableDataCompanyBalanceBankAccount, 0.0);
        AnchorPane.setLeftAnchor(tableDataCompanyBalanceBankAccount, 0.0);
        AnchorPane.setRightAnchor(tableDataCompanyBalanceBankAccount, 0.0);
        AnchorPane.setTopAnchor(tableDataCompanyBalanceBankAccount, 0.0);
        tableDataCompanyBalanceBankAccountLayout.getChildren().add(tableDataCompanyBalanceBankAccount);
    }

    private void setTableDataCompanyBalanceBankAccount() {
        TableView<TblCompanyBalanceBankAccount> tableView = new TableView();

        TableColumn<TblCompanyBalanceBankAccount, String> bankName = new TableColumn("Bank");
        bankName.setCellValueFactory((TableColumn.CellDataFeatures<TblCompanyBalanceBankAccount, String> param)
                -> Bindings.createStringBinding(() -> param.getValue().getTblBankAccount().getTblBank().getBankName(), param.getValue().getTblBankAccount().tblBankProperty()));
        bankName.setMinWidth(140);

        TableColumn<TblCompanyBalanceBankAccount, String> bankAccount = new TableColumn("Nomor Rekening");
        bankAccount.setCellValueFactory((TableColumn.CellDataFeatures<TblCompanyBalanceBankAccount, String> param)
                -> Bindings.createStringBinding(() -> param.getValue().getTblBankAccount().getCodeBankAccount(), param.getValue().getTblBankAccount().codeBankAccountProperty()));
        bankAccount.setMinWidth(140);

        TableColumn<TblCompanyBalanceBankAccount, String> bankAccountHolderName = new TableColumn("Nama Pemegang Rekening");
        bankAccountHolderName.setCellValueFactory((TableColumn.CellDataFeatures<TblCompanyBalanceBankAccount, String> param)
                -> Bindings.createStringBinding(() -> param.getValue().getTblBankAccount().getBankAccountHolderName(), param.getValue().getTblBankAccount().bankAccountHolderNameProperty()));
        bankAccountHolderName.setMinWidth(200);
//        TableColumn<TblCompanyBalanceBankAccount, String> bankAccountStatus = new TableColumn("Status");
//        bankAccountStatus.setCellValueFactory((TableColumn.CellDataFeatures<TblCompanyBalanceBankAccount, String> param)
//                -> Bindings.createStringBinding(() -> param.getValue().getRefBankAccountHolderStatus().getStatusName(), param.getValue().refBankAccountHolderStatusProperty()));

        TableColumn<TblCompanyBalanceBankAccount, String> nominal = new TableColumn("Nominal");
        nominal.setCellValueFactory((TableColumn.CellDataFeatures<TblCompanyBalanceBankAccount, String> param)
                -> Bindings.createStringBinding(() -> ClassFormatter.currencyFormat.cFormat(param.getValue().getCompanyBalanceBankAccountNominal()), param.getValue().companyBalanceBankAccountNominalProperty()));
        nominal.setMinWidth(140);

        tableView.getColumns().addAll(bankName, bankAccount, bankAccountHolderName, nominal);
        tableView.setItems(FXCollections.observableArrayList(loadAllDataCompanyBalanceBankAccount(selectedBalance)));
        tableDataCompanyBalanceBankAccount = new ClassTableWithControl(tableView);
    }

    private void setTableControlDataCompanyBalanceBankAccount() {
        //set control from feature
        ObservableList<Node> buttonControls = FXCollections.observableArrayList();
        JFXButton buttonControl;
        if (true) {
            buttonControl = new JFXButton();
            buttonControl.setText("Tambah");
            buttonControl.setOnMouseClicked((e) -> {
                //listener add
                dataCompanyBalanceBankAccountCreateHandle();
            });
            buttonControls.add(buttonControl);
        }
        if (true) {
            buttonControl = new JFXButton();
            buttonControl.setText("Ubah");
            buttonControl.setOnMouseClicked((e) -> {
                //listener update
                dataCompanyBalanceBankAccountUpdateHandle();
            });
            buttonControls.add(buttonControl);
        }
        if (true) {
            buttonControl = new JFXButton();
            buttonControl.setText("Hapus");
            buttonControl.setOnMouseClicked((e) -> {
                //listener delete
                dataCompanyBalanceBankAccountDeleteHandle();
            });
            buttonControls.add(buttonControl);
        }
        if (true) {
            buttonControl = new JFXButton();
            buttonControl.setText("Tambah Dana");
            buttonControl.setOnMouseClicked((e) -> {
                //listener add funds
                dataCompanyBalanceBankAccountAddFundHandle();
            });
            buttonControls.add(buttonControl);
        }
        tableDataCompanyBalanceBankAccount.addButtonControl(buttonControls);
    }

    private List<TblCompanyBalanceBankAccount> loadAllDataCompanyBalanceBankAccount(TblCompanyBalance companyBalance) {
        List<TblCompanyBalanceBankAccount> list = new ArrayList<>();
        if (companyBalance != null) {
            list = parentController.getFBalanceManager().getAllDataCompanyBalanceBankAccount(companyBalance.getIdbalance());
            for (TblCompanyBalanceBankAccount data : list) {
                //set data company balance
                data.setTblCompanyBalance(parentController.getFBalanceManager().getDataCompanyBalance());
                //set data bank account
                data.setTblBankAccount(parentController.getFBalanceManager().getBankAccount(data.getTblBankAccount().getIdbankAccount()));
                //set data bank
                data.getTblBankAccount().setTblBank(parentController.getFBalanceManager().getDataBank(data.getTblBankAccount().getTblBank().getIdbank()));
            }
        }
        return list;
    }
   
    
    public TblCompanyBalanceBankAccount selectedDataCompanyBalanceBankAccount;

    /**
     * HANDLE FROM DATA INPUT
     */
    //0 = 'insert'
    //1 = 'update'
    public int dataInputCompanyBalanceBankAccountStatus = 0;

    public Stage dialogStage;

    public void dataCompanyBalanceBankAccountCreateHandle() {
        dataInputCompanyBalanceBankAccountStatus = 0;
        selectedDataCompanyBalanceBankAccount = new TblCompanyBalanceBankAccount();
        selectedDataCompanyBalanceBankAccount.setTblCompanyBalance(selectedBalance);
        selectedDataCompanyBalanceBankAccount.setTblBankAccount(new TblBankAccount());
        selectedDataCompanyBalanceBankAccount.setCompanyBalanceBankAccountNominal(new BigDecimal("0"));
        selectedDataCompanyBalanceBankAccount.setCompanyBalanceBankAccountStatus(0);
        //open form data company balance bank account
        showCompanyBalanceBankAccountDialog();
    }

    public void dataCompanyBalanceBankAccountUpdateHandle() {
        if (tableDataCompanyBalanceBankAccount.getTableView().getSelectionModel().getSelectedItems().size() == 1) {
            dataInputCompanyBalanceBankAccountStatus = 1;
            selectedDataCompanyBalanceBankAccount = new TblCompanyBalanceBankAccount((TblCompanyBalanceBankAccount) tableDataCompanyBalanceBankAccount.getTableView().getSelectionModel().getSelectedItem());
            //data bank account
            selectedDataCompanyBalanceBankAccount.setTblBankAccount(new TblBankAccount(selectedDataCompanyBalanceBankAccount.getTblBankAccount()));
            //data bank
            selectedDataCompanyBalanceBankAccount.getTblBankAccount().setTblBank(new TblBank(selectedDataCompanyBalanceBankAccount.getTblBankAccount().getTblBank()));
            //open form data company balance bank account
            showCompanyBalanceBankAccountDialog();
        } else {
            ClassMessage.showWarningSelectedDataMessage("", null);
        }
    }

    public void dataCompanyBalanceBankAccountDeleteHandle() {
        if (tableDataCompanyBalanceBankAccount.getTableView().getSelectionModel().getSelectedItems().size() == 1) {
            if (((TblCompanyBalanceBankAccount) tableDataCompanyBalanceBankAccount.getTableView().getSelectionModel().getSelectedItem()).getCompanyBalanceBankAccountNominal().compareTo(new BigDecimal("0")) == 0) {
                Alert alert = ClassMessage.showConfirmationDeletingDataMessage("", null);
                if (alert.getResult() == ButtonType.OK) {
                    //delete data company balance - bamk account
                    if (parentController.getFBalanceManager().deleteDataCompanyBalanceBankAccount((TblCompanyBalanceBankAccount) tableDataCompanyBalanceBankAccount.getTableView().getSelectionModel().getSelectedItem())) {
                        ClassMessage.showSucceedDeletingDataMessage("", null);
                        //refresh data company balance - bank account
                        refreshDataTableCompanyBalanceBankAccount();
                    } else {
                        ClassMessage.showFailedDeletingDataMessage(parentController.getFBalanceManager().getErrorMessage(), null);
                    }
                }
            } else {
                HotelFX.showAlert(Alert.AlertType.WARNING, "DATA MASUKAN TIDAK SESUAI", "Nominal pada Nomor Rekening tidak boleh '0'..!", null);
            }
        } else {
            ClassMessage.showWarningSelectedDataMessage("", null);
        }
    }

    private void dataCompanyBalanceBankAccountAddFundHandle() {
        if (tableDataCompanyBalanceBankAccount.getTableView().getSelectionModel().getSelectedItems().size() == 1) {
            dataInputCompanyBalanceBankAccountStatus = 1;
            selectedDataCompanyBalanceBankAccount = new TblCompanyBalanceBankAccount((TblCompanyBalanceBankAccount) tableDataCompanyBalanceBankAccount.getTableView().getSelectionModel().getSelectedItem());
            //data bank account
            selectedDataCompanyBalanceBankAccount.setTblBankAccount(new TblBankAccount(selectedDataCompanyBalanceBankAccount.getTblBankAccount()));
            //data bank
            selectedDataCompanyBalanceBankAccount.getTblBankAccount().setTblBank(new TblBank(selectedDataCompanyBalanceBankAccount.getTblBankAccount().getTblBank()));
            //open form data company balance bank account - add fund
            showCompanyBalanceBankAccountAddFundDialog();
        } else {
            ClassMessage.showWarningSelectedDataMessage("", null);
        }
    }

    private void showCompanyBalanceBankAccountDialog() {
        try {
            // Load the fxml file and create a new stage for the popup dialog.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(HotelFX.class.getResource("view/feature_balance/company_balance/CompanyBalanceBankAccountDialog.fxml"));

            CompanyBalanceBankAccountController controller = new CompanyBalanceBankAccountController(this);
            loader.setController(controller);

            Region page = loader.load();

            // Create the dialog Stage.
            dialogStage = new Stage();
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.initOwner(HotelFX.primaryStage);

            //undecorated
            Undecorator undecorator = new Undecorator(dialogStage, page);
            undecorator.getStylesheets().add("skin/undecorator.css");
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

            // Show the dialog and wait until the user closes it
            dialogStage.showAndWait();
        } catch (IOException e) {
            System.out.println("Err >> " + e.toString());
        }
    }

    private void showCompanyBalanceBankAccountAddFundDialog() {
        try {
            // Load the fxml file and create a new stage for the popup dialog.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(HotelFX.class.getResource("view/feature_balance/company_balance/CompanyBalanceBankAccountAddFundDialog.fxml"));

            CompanyBalanceBankAccountAddFundController controller = new CompanyBalanceBankAccountAddFundController(this);
            loader.setController(controller);

            Region page = loader.load();

            // Create the dialog Stage.
            dialogStage = new Stage();
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.initOwner(HotelFX.primaryStage);

            //undecorated
            Undecorator undecorator = new Undecorator(dialogStage, page);
            undecorator.getStylesheets().add("skin/undecorator.css");
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

            // Show the dialog and wait until the user closes it
            dialogStage.showAndWait();
        } catch (IOException e) {
            System.out.println("Err >> " + e.toString());
        }
    }

    public void refreshDataTableCompanyBalanceBankAccount() {
        //refresh data input
        setSelectedDataToInputForm();
        //data popup company abalnce - bank account
        refreshDataPopup();
        //data table company balance - bank account
        tableDataCompanyBalanceBankAccount.getTableView().setItems(FXCollections.observableArrayList(loadAllDataCompanyBalanceBankAccount(selectedBalance)));
    }

    /**
     * Initializes the controller class.
     *
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        //init form input
        initFormDataCompanyBalance();
        //refresh data form input (transfer)
        dataCompanyBalanceTransferHandle();
    }

    public CompanyBalanceController(FeatureBalanceController parentController) {
        this.parentController = parentController;
    }

    private final FeatureBalanceController parentController;

    public FBalanceManager getService() {
        return parentController.getFBalanceManager();
    }

}
