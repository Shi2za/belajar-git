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
import hotelfx.helper.ClassTableWithControl;
import hotelfx.helper.ClassViewSetting;
import hotelfx.persistence.model.TblCompanyBalance;
import hotelfx.persistence.model.TblCompanyBalanceBankAccount;
import hotelfx.persistence.model.TblCompanyBalanceTransferReceived;
import hotelfx.persistence.service.FBalanceManager;
import hotelfx.view.feature_balance.FeatureBalanceController;
import hotelfx.view.feature_balance.cashier_balance.MinNominalBalanceInputController;
import insidefx.undecorator.Undecorator;
import java.io.IOException;
import java.math.BigDecimal;
import java.net.URL;
import java.text.SimpleDateFormat;
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
public class BackOfficeBalanceController implements Initializable {

    @FXML
    private AnchorPane ancFormBackOfficeBalance;

    @FXML
    private GridPane gpFormDataBackOfficeBalance;

    @FXML
    private JFXTextField txtBalanceName;

    @FXML
    private JFXTextField txtBalanceNominal;

  /*  @FXML
    private AnchorPane ancBankAccountLayout;
    private JFXCComboBoxTablePopup<TblCompanyBalanceBankAccount> cbpCompanyBalanceBankAccountDestination;

    private ToggleGroup groupBalance;

    @FXML
    private JFXRadioButton rdbBalanceCompany;

    @FXML
    private JFXRadioButton rdbBalanceCashier;

    @FXML
    private JFXTextField txtTransferNominal;

    @FXML
    private JFXButton btnTransfer; */

    @FXML
    private AnchorPane tblBackOfficeBalanceTransferLayout;
    public ClassTableWithControl tblBackOfficeBalanceTransfer; 
    
    @FXML
    private AnchorPane tblBackOfficeBalanceReceivedLayout;
    private ClassTableWithControl tblBackOfficeBalanceReceived;
    public TblCompanyBalance selectedBalance;
    
    private void initNumbericField() {
        ClassFormatter.setToNumericField(
        "BigDecimal",txtBalanceNominal);
    }
    
    private void setSelectedDataToInputForm() {

        selectedBalance = loadDataBackOfficeBalance();

        txtBalanceName.textProperty().bindBidirectional(selectedBalance.balanceNameProperty());
        Bindings.bindBidirectional(txtBalanceNominal.textProperty(), selectedBalance.balanceNominalProperty(), new ClassFormatter.CBigDecimalStringConverter());
    }
    
    private TblCompanyBalance loadDataBackOfficeBalance() {
     return parentController.getFBalanceManager().getDataBackOfficeBalance();
    }
     
    private void initTableBackOfficeBalanceTransfer(){
       setTableBackOfficeBalanceTransfer();
       setTableControlDataBackOfficeBalanceTransfer();
       tblBackOfficeBalanceTransferLayout.getChildren().clear();
       AnchorPane.setTopAnchor(tblBackOfficeBalanceTransfer,0.0);
       AnchorPane.setBottomAnchor(tblBackOfficeBalanceTransfer,0.0);
       AnchorPane.setLeftAnchor(tblBackOfficeBalanceTransfer,0.0);
       AnchorPane.setRightAnchor(tblBackOfficeBalanceTransfer,0.0);
       tblBackOfficeBalanceTransferLayout.getChildren().add(tblBackOfficeBalanceTransfer);
    }
    
    private void setTableBackOfficeBalanceTransfer(){
      TableView<TblCompanyBalanceTransferReceived>tblTransfer = new TableView();
      TableColumn<TblCompanyBalanceTransferReceived,String>transferTo = new TableColumn("Tujuan(Kas)");
      transferTo.setMinWidth(120);
      transferTo.setCellValueFactory((TableColumn.CellDataFeatures<TblCompanyBalanceTransferReceived,String>param)
       ->Bindings.createStringBinding(()->param.getValue().getTblCompanyBalanceByIdcompanyBalanceReceived().getBalanceName(),param.getValue().tblCompanyBalanceByIdcompanyBalanceSenderProperty()));
      TableColumn<TblCompanyBalanceTransferReceived,String>transferDate = new TableColumn("Tanggal\nTransfer");
      transferDate.setMinWidth(90);
      transferDate.setCellValueFactory((TableColumn.CellDataFeatures<TblCompanyBalanceTransferReceived,String>param)
       ->Bindings.createStringBinding(()->new SimpleDateFormat("dd MMM yyyy").format(param.getValue().getTransferDate()),param.getValue().transferDateProperty()));
      TableColumn<TblCompanyBalanceTransferReceived,String>transferNominal = new TableColumn("Nominal\nTransfer");
      transferNominal.setMinWidth(100);
      transferNominal.setCellValueFactory((TableColumn.CellDataFeatures<TblCompanyBalanceTransferReceived,String>param)
       ->Bindings.createStringBinding(()->ClassFormatter.currencyFormat.cFormat(param.getValue().getNominal()),param.getValue().nominalProperty()));
      TableColumn<TblCompanyBalanceTransferReceived,String>senderName = new TableColumn("Nama Pengirim");
      senderName.setMinWidth(160);
      senderName.setCellValueFactory((TableColumn.CellDataFeatures<TblCompanyBalanceTransferReceived,String>param)
      ->Bindings.createStringBinding(()->param.getValue().getTblEmployeeByTransferBy().getTblPeople().getFullName(),param.getValue().tblEmployeeByTransferByProperty()));
      tblTransfer.getColumns().addAll(transferDate,transferTo,transferNominal,senderName);
      tblTransfer.setItems(FXCollections.observableArrayList(loadAllDataTransfer()));
      tblBackOfficeBalanceTransfer = new ClassTableWithControl(tblTransfer);
    }
    
    public List<TblCompanyBalanceTransferReceived>loadAllDataTransfer(){
      return parentController.getFBalanceManager().getAllDataTransfer(selectedBalance.getIdbalance());
    }
    
    private void setTableControlDataBackOfficeBalanceTransfer(){
       ObservableList<Node>buttonControls = FXCollections.observableArrayList();
       JFXButton buttonControl;
       if(true){
          buttonControl = new JFXButton();
          buttonControl.setText("Tambah");
          buttonControl.setOnMouseClicked((e)->{
             dataBackOfficeBalanceCreateHandle();
          });
          buttonControls.add(buttonControl);
       }
       
        if(true){
          buttonControl = new JFXButton();
          buttonControl.setText("Ubah");
          buttonControl.setOnMouseClicked((e)->{
             dataBackOfficeBalanceUpdateHandle();
          });
          buttonControls.add(buttonControl);
       }
        
        if(true){
          buttonControl = new JFXButton();
          buttonControl.setText("Hapus");
          buttonControls.add(buttonControl);
       }
        
        tblBackOfficeBalanceTransfer.addButtonControl(buttonControls);
    }
    
    public TblCompanyBalanceTransferReceived selectedData;
    public int dataInputStatus;
    private void dataBackOfficeBalanceCreateHandle(){
       selectedData = new TblCompanyBalanceTransferReceived();
       dataInputStatus = 0;
       showBackOfficeBalanceTransferDialog();
    }
   
    private void dataBackOfficeBalanceUpdateHandle(){
       if(tblBackOfficeBalanceTransfer.getTableView().getSelectionModel().getSelectedItems().size()==1){
         selectedData = parentController.getFBalanceManager().getDataCompanyBalanceTransferReceived(((TblCompanyBalanceTransferReceived)tblBackOfficeBalanceTransfer.getTableView().getSelectionModel().getSelectedItem()).getIdrelation());
         dataInputStatus = 1;
         showBackOfficeBalanceTransferDialog();
       }
       else{
         ClassMessage.showWarningSelectedDataMessage(null,null);
       }
    }
    
    public Stage dialogStage;
        
    private void showBackOfficeBalanceTransferDialog() {
        try {
            // Load the fxml file and create a new stage for the popup dialog.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(HotelFX.class.getResource("view/feature_balance/back_office_balance/BackOfficeBalanceTransferDialog.fxml"));

            BackOfficeBalanceTransferController controller = new BackOfficeBalanceTransferController(this);
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
    
    private void initTableBackOfficeBalanceReceived(){
       setTableBackOfficeBalanceReceived();
       setTableControlDataBackOfficeBalanceReceived();
       tblBackOfficeBalanceReceivedLayout.getChildren().clear();
       AnchorPane.setTopAnchor(tblBackOfficeBalanceReceived,0.0);
       AnchorPane.setBottomAnchor(tblBackOfficeBalanceReceived,0.0);
       AnchorPane.setLeftAnchor(tblBackOfficeBalanceReceived,0.0);
       AnchorPane.setRightAnchor(tblBackOfficeBalanceReceived,0.0);
       tblBackOfficeBalanceReceivedLayout.getChildren().add(tblBackOfficeBalanceReceived);
    }
      
     private void setTableBackOfficeBalanceReceived(){
      TableView<TblCompanyBalanceTransferReceived>tblReceived = new TableView();
      TableColumn<TblCompanyBalanceTransferReceived,String>receivedFrom = new TableColumn("Sumber(Kas)");
      receivedFrom.setMinWidth(120);
      receivedFrom.setCellValueFactory((TableColumn.CellDataFeatures<TblCompanyBalanceTransferReceived,String>param)
       ->Bindings.createStringBinding(()->param.getValue().getTblCompanyBalanceByIdcompanyBalanceReceived().getBalanceName(),param.getValue().tblCompanyBalanceByIdcompanyBalanceReceivedProperty()));
      TableColumn<TblCompanyBalanceTransferReceived,String>senderDate = new TableColumn("Tanggal\nKirim");
      senderDate.setMinWidth(90);
      senderDate.setCellValueFactory((TableColumn.CellDataFeatures<TblCompanyBalanceTransferReceived,String>param)
       ->Bindings.createStringBinding(()->new SimpleDateFormat("dd MMM yyyy").format(param.getValue().getTransferDate()),param.getValue().receivedDateProperty()));
      TableColumn<TblCompanyBalanceTransferReceived,String>receivedNominal = new TableColumn("Nominal");
      receivedNominal.setMinWidth(100);
      receivedNominal.setCellValueFactory((TableColumn.CellDataFeatures<TblCompanyBalanceTransferReceived,String>param)
       ->Bindings.createStringBinding(()->ClassFormatter.currencyFormat.cFormat(param.getValue().getNominal()),param.getValue().nominalProperty()));
      TableColumn<TblCompanyBalanceTransferReceived,String>senderName = new TableColumn("Nama Pengirim");
      senderName.setMinWidth(160);
      senderName.setCellValueFactory((TableColumn.CellDataFeatures<TblCompanyBalanceTransferReceived,String>param)
      ->Bindings.createStringBinding(()->param.getValue().getTblEmployeeByTransferBy().getTblPeople().getFullName(),param.getValue().tblEmployeeByTransferByProperty()));
      tblReceived.getColumns().addAll(senderDate,receivedFrom,receivedNominal,senderName);
      tblReceived.setItems(FXCollections.observableArrayList(loadAllDataReceived()));
      tblBackOfficeBalanceReceived = new ClassTableWithControl(tblReceived);
    }
    
     private List<TblCompanyBalanceTransferReceived>loadAllDataReceived(){
       return parentController.getFBalanceManager().getAllDataReceived(selectedBalance.getIdbalance());
     }
     
     private void setTableControlDataBackOfficeBalanceReceived(){
       ObservableList<Node>buttonControls = FXCollections.observableArrayList();
       JFXButton buttonControl;
       if(true){
          buttonControl = new JFXButton();
          buttonControl.setText("Terima");
          buttonControl.setOnMouseClicked((e)->{
            backOfficeBalanceReceivedHandle();
          });
          buttonControls.add(buttonControl);
       }
        
        tblBackOfficeBalanceReceived.addButtonControl(buttonControls);
    }
    
    private void backOfficeBalanceReceivedHandle(){
       if(tblBackOfficeBalanceReceived.getTableView().getSelectionModel().getSelectedItems().size()==1){
         selectedData = parentController.getFBalanceManager().getDataCompanyBalanceTransferReceived(((TblCompanyBalanceTransferReceived)tblBackOfficeBalanceReceived.getTableView().getSelectionModel().getSelectedItem()).getIdrelation());
         TblCompanyBalanceTransferReceived dummySelected = new TblCompanyBalanceTransferReceived(selectedData);
         Alert alert = HotelFX.showAlertConfirm(Alert.AlertType.CONFIRMATION, "KONFIRMASI", "Apakah anda yakin untuk menerima data?", "");
           if(alert.getResult() == ButtonType.OK) {
               if(parentController.getFBalanceManager().updateDataBalanceReceived(dummySelected)){
                 selectedBalance.setBalanceNominal(selectedBalance.getBalanceNominal().add(selectedData.getNominal()));
                 ClassMessage.showSucceedInsertingDataMessage(null,null);
                 tblBackOfficeBalanceReceived.getTableView().setItems(FXCollections.observableArrayList(loadAllDataReceived()));
               }
               else{
                  ClassMessage.showFailedInsertingDataMessage(null,null);
               }
           }
        }
       else{
         ClassMessage.showWarningSelectedDataMessage(null,null);
       }
    }
      
 /*  private void initFormDataBackOfficeBalance() {

        initDataPopup();
        
        btnTransfer.setTooltip(new Tooltip("Transfer"));
        btnTransfer.setOnAction((e) -> {
            dataTransferHandle();
        });

        groupBalance = new ToggleGroup();
        rdbBalanceCompany.setToggleGroup(groupBalance);
        rdbBalanceCashier.setToggleGroup(groupBalance);

        rdbBalanceCompany.selectedProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                cbpCompanyBalanceBankAccountDestination.setVisible(newVal);
            }
        });
        cbpCompanyBalanceBankAccountDestination.setVisible(false);

        initImportantFieldColor();
        
        initNumbericField();

    }

    private void initImportantFieldColor() {
        ClassViewSetting.setImportantField(
                rdbBalanceCompany,
                rdbBalanceCashier,
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
    
    private TblCompanyBalance loadDataBackOfficeBalance() {
        return parentController.getFBalanceManager().getDataBackOfficeBalance();
    }

    private void initDataPopup() {
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

        ObservableList<TblCompanyBalanceBankAccount> companyBalanceBankAccountDestinationItems = FXCollections.observableArrayList(loadAllDataCompanyBalanceBankAccount());

        cbpCompanyBalanceBankAccountDestination = new JFXCComboBoxTablePopup<>(
                TblCompanyBalanceBankAccount.class, tableCompanyBalanceBankAccountDestination, companyBalanceBankAccountDestinationItems, "", "No. Rek (Tujuan) *", true, 640, 300
        );

        //attached to grid-pane
        ancBankAccountLayout.getChildren().clear();
        AnchorPane.setBottomAnchor(cbpCompanyBalanceBankAccountDestination, 0.0);
        AnchorPane.setLeftAnchor(cbpCompanyBalanceBankAccountDestination, 0.0);
        AnchorPane.setRightAnchor(cbpCompanyBalanceBankAccountDestination, 0.0);
        AnchorPane.setTopAnchor(cbpCompanyBalanceBankAccountDestination, 0.0);
        ancBankAccountLayout.getChildren().add(cbpCompanyBalanceBankAccountDestination);
    }

    private List<TblCompanyBalanceBankAccount> loadAllDataCompanyBalanceBankAccount() {
        List<TblCompanyBalanceBankAccount> list = parentController.getFBalanceManager().getAllDataCompanyBalanceBankAccount((long) 1);  //hotel baalnce = '1'
        for (TblCompanyBalanceBankAccount data : list) {
            //set data company balance
            data.setTblCompanyBalance(parentController.getFBalanceManager().getDataCompanyBalance());
            //set data bank account
            data.setTblBankAccount(parentController.getFBalanceManager().getBankAccount(data.getTblBankAccount().getIdbankAccount()));
            //set data bank
            data.getTblBankAccount().setTblBank(parentController.getFBalanceManager().getDataBank(data.getTblBankAccount().getTblBank().getIdbank()));
        }
        return list;
    }

    private void refreshDataPopup() {
        //Company Balance - Bank Account (destination)
        ObservableList<TblCompanyBalanceBankAccount> companyBalanceBankAccountDestinationItems = FXCollections.observableArrayList(loadAllDataCompanyBalanceBankAccount());
        cbpCompanyBalanceBankAccountDestination.setItems(companyBalanceBankAccountDestinationItems);
    }

    private void setSelectedDataToInputForm() {

        selectedBalance = loadDataBackOfficeBalance();

        txtBalanceName.textProperty().bindBidirectional(selectedBalance.balanceNameProperty());
        Bindings.bindBidirectional(txtBalanceNominal.textProperty(), selectedBalance.balanceNominalProperty(), new ClassFormatter.CBigDecimalStringConverter());

        refreshDataPopup();

        cbpCompanyBalanceBankAccountDestination.setValue(null);

        cbpCompanyBalanceBankAccountDestination.hide();

        transferNominal.set(new BigDecimal("0"));

        rdbBalanceCompany.setSelected(false);
        rdbBalanceCashier.setSelected(false);

        Bindings.bindBidirectional(txtTransferNominal.textProperty(), transferNominal, new ClassFormatter.CBigDecimalStringConverter());

    }

    private void dataTransferHandle() {
        if (checkDataInputDataTransfer()){
            Alert alert = HotelFX.showAlertConfirm(Alert.AlertType.CONFIRMATION, "KONFIRMASI", "Apakah anda yakin untuk melakukan transfer data?", "");
            if (alert.getResult() == ButtonType.OK) {
                //data dummy selected balance
                TblCompanyBalance dummySelectedBalance = new TblCompanyBalance(selectedBalance);
                //data dummy another balance
                TblCompanyBalance dummyAnotherBalance;
                if (rdbBalanceCashier.isSelected()) {  //Cashier
                    dummyAnotherBalance = parentController.getFBalanceManager().getDataCashierBalance();
                } else {  //Company (Hotel)
                    dummyAnotherBalance = parentController.getFBalanceManager().getDataCompanyBalance();
                }
                //data dummy current balance
                dummySelectedBalance.setBalanceNominal(dummySelectedBalance.getBalanceNominal().subtract(transferNominal.get()));
                //data dummy another balance
                dummyAnotherBalance.setBalanceNominal(dummyAnotherBalance.getBalanceNominal().add(transferNominal.get()));
                //data dummy another balance - bank account
                TblCompanyBalanceBankAccount dummyBalanceBankAccountDestination = null;
                if (rdbBalanceCompany.isSelected()) {
                    dummyBalanceBankAccountDestination = new TblCompanyBalanceBankAccount(cbpCompanyBalanceBankAccountDestination.getValue());
                    dummyBalanceBankAccountDestination.setCompanyBalanceBankAccountNominal(dummyBalanceBankAccountDestination.getCompanyBalanceBankAccountNominal().add(transferNominal.get()));
                }
                if (parentController.getFBalanceManager().updateDataBalanceTransfer(dummySelectedBalance,
                        dummyAnotherBalance,
                        null,
                        dummyBalanceBankAccountDestination,
                        transferNominal.get())) {
                    ClassMessage.showSucceedUpdatingDataMessage("", null);
                    //refresh data input
                    setSelectedDataToInputForm();
                } else {
                    ClassMessage.showFailedUpdatingDataMessage(parentController.getFBalanceManager().getErrorMessage(), null);
                }
            }
        } else {
            ClassMessage.showWarningInputDataMessage(errDataInput, null);
        }
    }

    private String errDataInput;

    private boolean checkDataInputDataTransfer() {
        boolean dataInput = true;
        errDataInput = "";
        if (!rdbBalanceCompany.isSelected() && !rdbBalanceCashier.isSelected()) {
            dataInput = false;
            errDataInput += "Kas (Tujuan) : " + ClassMessage.defaultErrorNullValueMessage + " \n";
        }
        if (rdbBalanceCompany.isSelected()) {
            if (cbpCompanyBalanceBankAccountDestination.getValue() == null) {
                dataInput = false;
                errDataInput += "Kas Besar (Tujuan Nomor Rekening) : " + ClassMessage.defaultErrorNullValueMessage + " \n";
            }
        }
        if (txtTransferNominal.getText() == null 
                || txtTransferNominal.getText().equals("")
                || txtTransferNominal.getText().equals("-")) {
            dataInput = false;
            errDataInput += "Nominal (Transfer) : " + ClassMessage.defaultErrorNullValueMessage + " \n";
        } else {
            if (transferNominal.get().compareTo(new BigDecimal("0")) < 1) {
                dataInput = false;
                errDataInput += "Nominal (Transfer) : " + ClassMessage.defaultErrorZeroValueMessage + " \n";
            } else {
                if (transferNominal.get().compareTo(selectedBalance.getBalanceNominal()) == 1) {
                    dataInput = false;
                    errDataInput += "Nominal (Transfer) : Tidak dapat lebih besar dari nominal 'Kas Back-Office'..!! \n";
                }
            }
        }
        return dataInput;
    } */

    /**
     * Initializes the controller class.
     *
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
       setSelectedDataToInputForm();
       initTableBackOfficeBalanceTransfer();
       initTableBackOfficeBalanceReceived();
        //init form input
     //   initFormDataBackOfficeBalance();
        //refresh data form input (transfer)
     //   setSelectedDataToInputForm();
    }

    public BackOfficeBalanceController(FeatureBalanceController parentController) {
        this.parentController = parentController;
    }
    
    private final FeatureBalanceController parentController;
    
    public FBalanceManager getService() {
        return parentController.getFBalanceManager();
    }
}
