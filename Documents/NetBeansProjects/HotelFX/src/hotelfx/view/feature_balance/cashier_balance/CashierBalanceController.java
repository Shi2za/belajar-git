/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hotelfx.view.feature_balance.cashier_balance;

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
import insidefx.undecorator.Undecorator;
import java.io.IOException;
import java.net.URL;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
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
public class CashierBalanceController implements Initializable {

    @FXML
    private AnchorPane ancFormCashierBalance;

    @FXML
    private GridPane gpFormDataCashierBalance;

    @FXML
    private JFXTextField txtBalanceName;

    @FXML
    private JFXTextField txtBalanceNominal;

    @FXML
    private JFXTextField txtMinBalanceNominal;
    
    @FXML
    private AnchorPane tblCashierBalanceTransferLayout;
    public ClassTableWithControl tblCashierBalanceTransfer; 
    
    @FXML
    private AnchorPane tblCashierBalanceReceivedLayout;
    private ClassTableWithControl tblCashierBalanceReceived;

  /*  @FXML
    private AnchorPane ancBankAccountLayout;
    private JFXCComboBoxTablePopup<TblCompanyBalanceBankAccount> cbpCompanyBalanceBankAccountDestination;

    private ToggleGroup groupBalance;

    @FXML
    private JFXRadioButton rdbBalanceCompany;

    @FXML
    private JFXRadioButton rdbBalanceBackOffice;

    @FXML
    private JFXTextField txtTransferNominal;

    @FXML
    private JFXButton btnTransfer; */

    @FXML
    private JFXButton btnUpdateMinNominalBalance;
    public TblCompanyBalance selectedBalance;
    
     private void initTableCashierBalanceTransfer(){
       setTableCashierBalanceTransfer();
       setTableControlDataCashierBalanceTransfer();
       tblCashierBalanceTransferLayout.getChildren().clear();
       AnchorPane.setTopAnchor(tblCashierBalanceTransfer,0.0);
       AnchorPane.setBottomAnchor(tblCashierBalanceTransfer,0.0);
       AnchorPane.setLeftAnchor(tblCashierBalanceTransfer,0.0);
       AnchorPane.setRightAnchor(tblCashierBalanceTransfer,0.0);
       tblCashierBalanceTransferLayout.getChildren().add(tblCashierBalanceTransfer);
    }
    
    private void setTableCashierBalanceTransfer(){
      TableView<TblCompanyBalanceTransferReceived>tblTransfer = new TableView();
      TableColumn<TblCompanyBalanceTransferReceived,String>transferTo = new TableColumn("Tujuan(Kas)");
      transferTo.setMinWidth(120);
      transferTo.setCellValueFactory((TableColumn.CellDataFeatures<TblCompanyBalanceTransferReceived,String>param)
       ->Bindings.createStringBinding(()->param.getValue().getTblCompanyBalanceByIdcompanyBalanceSender().getBalanceName(),param.getValue().tblCompanyBalanceByIdcompanyBalanceSenderProperty()));
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
      tblTransfer.setItems(FXCollections.observableArrayList(loadAllDataCompanyBalanceTransfer()));
      tblCashierBalanceTransfer = new ClassTableWithControl(tblTransfer);
    }
    
    public List<TblCompanyBalanceTransferReceived>loadAllDataCompanyBalanceTransfer(){
      return parentController.getFBalanceManager().getAllDataTransfer(selectedBalance.getIdbalance());
    }
    
    private void setTableControlDataCashierBalanceTransfer(){
       ObservableList<Node>buttonControls = FXCollections.observableArrayList();
       JFXButton buttonControl;
       if(true){
          buttonControl = new JFXButton();
          buttonControl.setText("Tambah");
          buttonControl.setOnMouseClicked((e)->{
             cashierBalanceTransferCreateHandle();
          });
          buttonControls.add(buttonControl);
        }
       
       if(true){
          buttonControl = new JFXButton();
          buttonControl.setText("Ubah");
          buttonControl.setOnMouseClicked((e)->{
             cashierBalanceTransferUpdateHandle();
          });
          buttonControls.add(buttonControl);
       }
       
       if(true){
         buttonControl = new JFXButton();
         buttonControl.setText("Hapus");
         buttonControl.setOnMouseClicked((e)->{
             
         });
       }
       
      tblCashierBalanceTransfer.addButtonControl(buttonControls);
    }
    
    public int dataInputStatus;
    private void cashierBalanceTransferCreateHandle(){
      selectedData = new TblCompanyBalanceTransferReceived();
      selectedData.setTblCompanyBalanceByIdcompanyBalanceSender(selectedBalance);
      selectedData.setTblCompanyBalanceByIdcompanyBalanceReceived(parentController.getFBalanceManager().getDataBackOfficeBalance());
      showCashierBalanceTransferDialog();
      dataInputStatus = 0;
    }
    
    private void cashierBalanceTransferUpdateHandle(){
       if(tblCashierBalanceTransfer.getTableView().getSelectionModel().getSelectedItems().size()==1){
         selectedData = parentController.getFBalanceManager().getDataCompanyBalanceTransferReceived(((TblCompanyBalanceTransferReceived)tblCashierBalanceTransfer.getTableView().getSelectionModel().getSelectedItem()).getIdrelation());
         showCashierBalanceTransferDialog();
         dataInputStatus = 1;
       }
       else{
         ClassMessage.showWarningSelectedDataMessage(null,null);
       }
    }
    
   private void initTableCashierBalanceReceived(){
       setTableCashierBalanceReceived();
       setTableControlDataCashierBalanceReceived();
       tblCashierBalanceReceivedLayout.getChildren().clear();
       AnchorPane.setTopAnchor(tblCashierBalanceReceived,0.0);
       AnchorPane.setBottomAnchor(tblCashierBalanceReceived,0.0);
       AnchorPane.setLeftAnchor(tblCashierBalanceReceived,0.0);
       AnchorPane.setRightAnchor(tblCashierBalanceReceived,0.0);
       tblCashierBalanceReceivedLayout.getChildren().add(tblCashierBalanceReceived);
    }
      
    private void setTableCashierBalanceReceived(){
      TableView<TblCompanyBalanceTransferReceived>tblReceived = new TableView();
      TableColumn<TblCompanyBalanceTransferReceived,String>receivedFrom = new TableColumn("Sumber(Kas)");
      receivedFrom.setMinWidth(120);
      receivedFrom.setCellValueFactory((TableColumn.CellDataFeatures<TblCompanyBalanceTransferReceived,String>param)
       ->Bindings.createStringBinding(()->param.getValue().getTblCompanyBalanceByIdcompanyBalanceReceived().getBalanceName(),param.getValue().tblEmployeeByReceivedByProperty()));
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
      tblReceived.setItems(FXCollections.observableArrayList(loadAllDataCompanyBalanceReceived()));
      tblCashierBalanceReceived = new ClassTableWithControl(tblReceived);
    }
     
    private List<TblCompanyBalanceTransferReceived>loadAllDataCompanyBalanceReceived(){
       return parentController.getFBalanceManager().getAllDataReceived(selectedBalance.getIdbalance());
    }
    
    private void setTableControlDataCashierBalanceReceived(){
       ObservableList<Node>buttonControls = FXCollections.observableArrayList();
       JFXButton buttonControl;
       if(true){
          buttonControl = new JFXButton();
          buttonControl.setText("Terima");
          buttonControl.setOnMouseClicked((e)->{
             cashierBalanceReceivedHandle();
          });
          buttonControls.add(buttonControl);
       }    
      tblCashierBalanceReceived.addButtonControl(buttonControls);
    }
    
    private void initFormDataCashierBalance(){
        btnUpdateMinNominalBalance.setTooltip(new Tooltip("Ubah Nilai Minimun Nominal Kas"));
        btnUpdateMinNominalBalance.setOnAction((e) -> {
            dataMinNominalBalanceUpdateHandle();
        });
    }
    
    private void initNumbericField() {
      ClassFormatter.setToNumericField(
      "BigDecimal",txtBalanceNominal);
    }
    
    public void setSelectedDataToInputForm() {
        selectedBalance = loadDataCashierBalance();
        txtBalanceName.textProperty().bindBidirectional(selectedBalance.balanceNameProperty());
        Bindings.bindBidirectional(txtBalanceNominal.textProperty(), selectedBalance.balanceNominalProperty(), new ClassFormatter.CBigDecimalStringConverter());
        Bindings.bindBidirectional(txtMinBalanceNominal.textProperty(), selectedBalance.minimalBalanceNominalProperty(), new ClassFormatter.CBigDecimalStringConverter());
    }
    
    public TblCompanyBalanceTransferReceived selectedData;
    
   private void cashierBalanceReceivedHandle(){
       if(tblCashierBalanceReceived.getTableView().getSelectionModel().getSelectedItems().size()==1){
         selectedData = parentController.getFBalanceManager().getDataCompanyBalanceTransferReceived(((TblCompanyBalanceTransferReceived)tblCashierBalanceReceived.getTableView().getSelectionModel().getSelectedItem()).getIdrelation());
         TblCompanyBalanceTransferReceived dummySelected = new TblCompanyBalanceTransferReceived(selectedData);
         Alert alert = HotelFX.showAlertConfirm(Alert.AlertType.CONFIRMATION, "KONFIRMASI", "Apakah anda yakin untuk menerima data ini?", "");
           if(alert.getResult() == ButtonType.OK) {
               if(parentController.getFBalanceManager().updateDataBalanceReceived(dummySelected)){
                 selectedBalance.setBalanceNominal(selectedBalance.getBalanceNominal().add(selectedData.getNominal()));
                 ClassMessage.showSucceedInsertingDataMessage(null,null);
                 tblCashierBalanceReceived.getTableView().setItems(FXCollections.observableArrayList(loadAllDataCompanyBalanceReceived()));
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
      
    private TblCompanyBalance loadDataCashierBalance() {
      return parentController.getFBalanceManager().getDataCashierBalance();
    }
    
    private void dataMinNominalBalanceUpdateHandle() {
        //open form data input dialog
        showMinNominalBalanceInputDialog();
    }
    
    public Stage dialogStage;
        
    private void showMinNominalBalanceInputDialog() {
        try {
            // Load the fxml file and create a new stage for the popup dialog.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(HotelFX.class.getResource("view/feature_balance/cashier_balance/MinNominalBalanceInputDialog.fxml"));

            MinNominalBalanceInputController controller = new MinNominalBalanceInputController(this);
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
    
   
    
    public Stage dialogStageCashierBalanceTransfer;
    
    private void showCashierBalanceTransferDialog() {
        try {
            // Load the fxml file and create a new stage for the popup dialog.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(HotelFX.class.getResource("view/feature_balance/cashier_balance/CashierBalanceTransferDialog.fxml"));

            CashierBalanceTransferController controller = new CashierBalanceTransferController(this);
            loader.setController(controller);

            Region page = loader.load();

            // Create the dialog Stage.
            dialogStageCashierBalanceTransfer = new Stage();
            dialogStageCashierBalanceTransfer.initModality(Modality.WINDOW_MODAL);
            dialogStageCashierBalanceTransfer.initOwner(HotelFX.primaryStage);

            //undecorated
            Undecorator undecorator = new Undecorator(dialogStageCashierBalanceTransfer,page);
            undecorator.getStylesheets().add("skin/undecorator.css");
            undecorator.getMenuButton().setVisible(false);
            undecorator.getMaximizeButton().setVisible(false);
            undecorator.getMinimizeButton().setVisible(false);
            undecorator.getFullScreenButton().setVisible(false);
            undecorator.getCloseButton().setVisible(false);

            Scene scene = new Scene(undecorator);
            scene.setFill(Color.TRANSPARENT);

            dialogStageCashierBalanceTransfer.initStyle(StageStyle.TRANSPARENT);
            dialogStageCashierBalanceTransfer.setScene(scene);
            dialogStageCashierBalanceTransfer.setResizable(false);

            // Show the dialog and wait until the user closes it
            dialogStageCashierBalanceTransfer.showAndWait();
        } catch (IOException e) {
            System.out.println("Err >> " + e.toString());
        }
    }
  /*  private void initFormDataCashierBalance() {

        initDataPopup();
        
        btnTransfer.setTooltip(new Tooltip("Transfer"));
        btnTransfer.setOnAction((e) -> {
            dataTransferHandle();
        });

        btnUpdateMinNominalBalance.setTooltip(new Tooltip("Ubah Nilai Minimun Nominal Kas"));
        btnUpdateMinNominalBalance.setOnAction((e) -> {
            dataMinNominalBalanceUpdateHandle();
        });

        groupBalance = new ToggleGroup();
        rdbBalanceCompany.setToggleGroup(groupBalance);
        rdbBalanceBackOffice.setToggleGroup(groupBalance);

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
                rdbBalanceBackOffice,
                cbpCompanyBalanceBankAccountDestination,
                txtTransferNominal);
    }

    private void initNumbericField() {
        ClassFormatter.setToNumericField(
                "BigDecimal",
                txtBalanceNominal,
                txtMinBalanceNominal,
                txtTransferNominal);
    }

    public TblCompanyBalance selectedBalance;

    private final ObjectProperty<BigDecimal> transferNominal = new SimpleObjectProperty<>(new BigDecimal("0"));

    private TblCompanyBalance loadDataCashierBalance() {
        return parentController.getFBalanceManager().getDataCashierBalance();
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

    public void setSelectedDataToInputForm() {

        selectedBalance = loadDataCashierBalance();

        txtBalanceName.textProperty().bindBidirectional(selectedBalance.balanceNameProperty());
        Bindings.bindBidirectional(txtBalanceNominal.textProperty(), selectedBalance.balanceNominalProperty(), new ClassFormatter.CBigDecimalStringConverter());
        Bindings.bindBidirectional(txtMinBalanceNominal.textProperty(), selectedBalance.minimalBalanceNominalProperty(), new ClassFormatter.CBigDecimalStringConverter());

        refreshDataPopup();

        cbpCompanyBalanceBankAccountDestination.setValue(null);

        cbpCompanyBalanceBankAccountDestination.hide();

        transferNominal.set(new BigDecimal("0"));

        rdbBalanceCompany.setSelected(false);
        rdbBalanceBackOffice.setSelected(false);

        Bindings.bindBidirectional(txtTransferNominal.textProperty(), transferNominal, new ClassFormatter.CBigDecimalStringConverter());

    }

    public Stage dialogStage;

    private void dataMinNominalBalanceUpdateHandle() {
        //open form data input dialog
        showMinNominalBalanceInputDialog();
    }

    private void showMinNominalBalanceInputDialog() {
        try {
            // Load the fxml file and create a new stage for the popup dialog.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(HotelFX.class.getResource("view/feature_balance/cashier_balance/MinNominalBalanceInputDialog.fxml"));

            MinNominalBalanceInputController controller = new MinNominalBalanceInputController(this);
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

    private void dataTransferHandle() {
        if (checkDataInputDataTransfer()) {
            Alert alert = HotelFX.showAlertConfirm(Alert.AlertType.CONFIRMATION, "KONFIRMASI", "Apakah anda yakin untuk melakukan transfer data?", "");
            if (alert.getResult() == ButtonType.OK) {
                //data dummy selected balance
                TblCompanyBalance dummySelectedBalance = new TblCompanyBalance(selectedBalance);
                //data dummy another balance
                TblCompanyBalance dummyAnotherBalance;
                if (rdbBalanceBackOffice.isSelected()) {  //BackOffice
                    dummyAnotherBalance = parentController.getFBalanceManager().getDataBackOfficeBalance();
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
        if (!rdbBalanceCompany.isSelected() && !rdbBalanceBackOffice.isSelected()) {
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
                    errDataInput += "Nominal (Transfer) : Tidak dapat lebih besar dari nominal 'Kas Kasir'..!! \n";
                } else {
                    if ((selectedBalance.getBalanceNominal().subtract(transferNominal.get())).compareTo(selectedBalance.getMinimalBalanceNominal()) == -1) {
                        dataInput = false;
                        errDataInput += "Nominal (Transfer) : Sisa nominal 'Kas Kasir' tidak boleh lebih kecil dari minimum nominal 'Kas Kasir'..!! \n";
                    }
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
       initTableCashierBalanceTransfer();
       initTableCashierBalanceReceived();
       initFormDataCashierBalance();
        //init form input
    //    initFormDataCashierBalance();
        //refresh data form input (transfer)
    //    setSelectedDataToInputForm();
    }

    public CashierBalanceController(FeatureBalanceController parentController) {
        this.parentController = parentController;
    }

    private final FeatureBalanceController parentController;

    public FBalanceManager getService() {
        return parentController.getFBalanceManager();
    }

}
