/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hotelfx.view.feature_debt.debt_payment_history;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXCComboBoxPopup;
import com.jfoenix.controls.JFXTextField;
import hotelfx.HotelFX;
import hotelfx.helper.ClassDataDebtPayment;
import hotelfx.helper.ClassDataDebtPaymentDetail;
import hotelfx.helper.ClassFilteringTable;
import hotelfx.helper.ClassFormatter;
import hotelfx.helper.ClassMessage;
import hotelfx.helper.ClassPrinter;
import hotelfx.helper.ClassSession;
import hotelfx.helper.ClassTableWithControl;
import hotelfx.helper.ClassViewSetting;
import hotelfx.helper.PrintModel.ClassPrintDataHutangKaryawan;
import hotelfx.helper.PrintModel.ClassPrintDataHutangKaryawanDetail;
import hotelfx.persistence.model.RefEmployeeType;
import hotelfx.persistence.model.TblCalendarEmployeeDebt;
import hotelfx.persistence.model.TblCalendarEmployeePaymentDebtHistory;
import hotelfx.persistence.model.TblCalendarEmployeePaymentHistory;
import hotelfx.persistence.model.TblEmployee;
import hotelfx.persistence.service.FDebtManager;
import hotelfx.view.DashboardController;
import hotelfx.view.feature_debt.FeatureDebtController;
import insidefx.undecorator.Undecorator;
import java.io.IOException;
import java.math.BigDecimal;
import java.net.URL;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.css.PseudoClass;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.SplitPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Region;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;

/**
 *
 * @author Andreas
 */
public class DebtPaymentHistoryController implements Initializable{
    @FXML
    private SplitPane spDataPaymentHistory;
    public DoubleProperty dataPaymentHistoryFormShowStatus;
    private boolean isTimeLinePlaying = false;
    
    @FXML
    private AnchorPane ancBorderPane;
    @FXML
    private AnchorPane tblDataPaymentHistoryLayoutDisableLayer;
    
    @FXML
    private AnchorPane ancFiltering;
    private ClassFilteringTable<ClassDataDebtPayment>cft;
    
    private void setDataDebtSplitPane(){
        spDataPaymentHistory.setDividerPositions(1);
        
       dataPaymentHistoryFormShowStatus = new SimpleDoubleProperty(1.0);
       
       DoubleProperty divPosition = new SimpleDoubleProperty();
       
       divPosition.bind(new SimpleDoubleProperty(1.0)
                .subtract(dataPaymentHistoryFormShowStatus)
        );

        divPosition.addListener((obs, oldVal, newVal) -> {
            isTimeLinePlaying = true;
            KeyValue keyValue = new KeyValue(spDataPaymentHistory.getDividers().get(0).positionProperty(), newVal);
            Timeline timeline = new Timeline(new KeyFrame(Duration.millis(ClassViewSetting.keyFrameDuration), keyValue));
            timeline.play();
            timeline.setOnFinished((e) -> {
                isTimeLinePlaying = false;
            });
        });
       
       SplitPane.Divider div = spDataPaymentHistory.getDividers().get(0);
       
       div.positionProperty().addListener((obs,oldVal,newVal)->{
           if (!isTimeLinePlaying) {
                div.setPosition(divPosition.get());
            }
       });
       
       dataPaymentHistoryFormShowStatus.addListener((obs,oldVal,newVal)->{
           if(inputDataStatus!=3){
               if(newVal.doubleValue()==0.0){
                 ancBorderPane.setDisable(false);
                 tblDataPaymentHistoryLayoutDisableLayer.setDisable(true);
                 ancBorderPane.toFront();
               }
               
               if(newVal.doubleValue()==1.0){
                 ancBorderPane.setDisable(true);
                 tblDataPaymentHistoryLayoutDisableLayer.setDisable(false);
                 tblDataPaymentHistoryLayoutDisableLayer.toFront();
               }
           }
       });
       
       dataPaymentHistoryFormShowStatus.set(0);
    }
    
    @FXML
    private AnchorPane tblDataDebtPaymentLayout;
    
    
    public ClassTableWithControl tblDataDebtPayment;
    
    private void initTableDataDebtPayment(){
      setTableDataDebtPayment();
      setTableControlDataPaymentHistory();
      
      tblDataDebtPaymentLayout.getChildren().clear();
      AnchorPane.setTopAnchor(tblDataDebtPayment,15.0);
      AnchorPane.setBottomAnchor(tblDataDebtPayment,15.0);
      AnchorPane.setLeftAnchor(tblDataDebtPayment,15.0);
      AnchorPane.setRightAnchor(tblDataDebtPayment,15.0);
      tblDataDebtPaymentLayout.getChildren().add(tblDataDebtPayment);
    }
    
    private void setTableDataDebtPayment(){
      TableView<ClassDataDebtPayment>tblDebtPayment = new TableView();
      TableColumn<ClassDataDebtPayment,String>employeeID = new TableColumn("ID");
      employeeID.setMinWidth(100);
      employeeID.setCellValueFactory((TableColumn.CellDataFeatures<ClassDataDebtPayment,String>param)
      ->Bindings.createStringBinding(()->param.getValue().getEmployee().getCodeEmployee(),param.getValue().employeeProperty()));
      TableColumn<ClassDataDebtPayment,String>employeeName = new TableColumn("Nama");
      employeeName.setMinWidth(160);
      employeeName.setCellValueFactory((TableColumn.CellDataFeatures<ClassDataDebtPayment,String>param)
      ->Bindings.createStringBinding(()->param.getValue().getEmployee().getTblPeople().getFullName(),param.getValue().employeeProperty()));   
      TableColumn<ClassDataDebtPayment,String>employeeType = new TableColumn("Tipe");
      employeeType.setMinWidth(140);
      employeeType.setCellValueFactory((TableColumn.CellDataFeatures<ClassDataDebtPayment,String>param)
      ->Bindings.createStringBinding(()->param.getValue().getEmployee().getRefEmployeeType().getTypeName(),param.getValue().employeeProperty()));      
      TableColumn<ClassDataDebtPayment,String>employeeJob = new TableColumn("Jabatan");
      employeeJob.setMinWidth(120);
      employeeJob.setCellValueFactory((TableColumn.CellDataFeatures<ClassDataDebtPayment,String>param)
      ->Bindings.createStringBinding(()->param.getValue().getEmployee().getTblJob().getJobName(),param.getValue().getEmployee().tblJobProperty()));
      TableColumn<ClassDataDebtPayment,String>employee = new TableColumn("Karyawan");
      employee.getColumns().addAll(employeeType,employeeID,employeeName,employeeJob);
      
      TableColumn<ClassDataDebtPayment,String>debtTotal = new TableColumn("Total Tagihan");
      debtTotal.setMinWidth(120);
      debtTotal.setCellValueFactory((TableColumn.CellDataFeatures<ClassDataDebtPayment,String>param)
      ->Bindings.createStringBinding(()->ClassFormatter.currencyFormat.cFormat(param.getValue().getDebtTotal()),param.getValue().debtTotalProperty()));
      TableColumn<ClassDataDebtPayment,String>paymentTotal = new TableColumn("Total Bayar");
      paymentTotal.setMinWidth(120);
      paymentTotal.setCellValueFactory((TableColumn.CellDataFeatures<ClassDataDebtPayment,String>param)
      ->Bindings.createStringBinding(()->ClassFormatter.currencyFormat.cFormat(param.getValue().getPaymentTotal()),param.getValue().paymentTotalProperty()));
      TableColumn<ClassDataDebtPayment,String>balanceTotal = new TableColumn("Sisa Tagihan");
      balanceTotal.setMinWidth(120);
      balanceTotal.setCellValueFactory((TableColumn.CellDataFeatures<ClassDataDebtPayment,String>param)
      ->Bindings.createStringBinding(()->ClassFormatter.currencyFormat.cFormat(param.getValue().getBalance()),param.getValue().balanceProperty()));
     // tblDataDebtPayment = tableView;
      tblDebtPayment.getColumns().addAll(employee,debtTotal,paymentTotal,balanceTotal);
      tblDebtPayment.setItems(FXCollections.observableArrayList(loadAllDataDebtPaymentHistory()));
      tblDebtPayment.setRowFactory(tv->{
          TableRow<ClassDataDebtPayment>row = new TableRow();
          row.setOnMouseClicked((e)->{
               if(e.getClickCount()==2){
                   if(isShowStatus.get()){
                     dataPaymentHistoryUnshowHandle();
                   }
                   else{
                       if(!row.isEmpty()){
                         dataPaymentHistoryShowHandle();
                       }
                   }
               }
          });
          return row;
      });
      tblDataDebtPayment = new ClassTableWithControl(tblDebtPayment);
      
      cft = new ClassFilteringTable<>(ClassDataDebtPayment.class,tblDataDebtPayment.getTableView(),tblDataDebtPayment.getTableView().getItems());
      ancFiltering.getChildren().clear();
      AnchorPane.setBottomAnchor(cft,0.0);
      //AnchorPane.setLeftAnchor(cft,0.0);
      AnchorPane.setRightAnchor(cft,0.0);
      AnchorPane.setTopAnchor(cft,25.0);
      ancFiltering.getChildren().addAll(cft);
    }
    
    
    public List<ClassDataDebtPayment>loadAllDataDebtPaymentHistory(){
       List<TblCalendarEmployeeDebt>list = parentController.getFDebtManager().getAllDataDebt();
       List<ClassDataDebtPayment>listDebtPayment = new ArrayList();
       
       for(TblCalendarEmployeeDebt debtHistory : list){
           if(debtHistory.getEmployeeDebtStatus().equalsIgnoreCase("Disetujui")){
              boolean found = false;
                for(ClassDataDebtPayment debtPayment : listDebtPayment){
                    if(debtHistory.getTblEmployeeByIdemployee().getIdemployee()==debtPayment.getEmployee().getIdemployee()){
                      debtPayment.setDebtTotal(debtTotal(debtPayment.getEmployee()));
                      debtPayment.setPaymentTotal(debtPaymentTotal(debtPayment.getEmployee()));
                       debtPayment.setBalance(debtPayment.getDebtTotal().subtract(debtPayment.getPaymentTotal()));
                      found = true;
                    }
                }
               if(found==false){
                 ClassDataDebtPayment debtPayment = new ClassDataDebtPayment();
                 debtPayment.setEmployee(debtHistory.getTblEmployeeByIdemployee());
                 debtPayment.setDebtTotal(debtTotal(debtHistory.getTblEmployeeByIdemployee()));
                 debtPayment.setPaymentTotal(debtPaymentTotal(debtHistory.getTblEmployeeByIdemployee()));
                 debtPayment.setBalance(debtPayment.getDebtTotal().subtract(debtPayment.getPaymentTotal()));
                 listDebtPayment.add(debtPayment);
               }
           }
           
       }
      return  listDebtPayment;
    }
    
    public BigDecimal debtTotal(TblEmployee employee){
       BigDecimal totalDebt = new BigDecimal(0);
       List<TblCalendarEmployeeDebt>list = parentController.getFDebtManager().getAllDataDebtByIdEmployee(employee);
       for(TblCalendarEmployeeDebt employeeDebt : list){
           if(employeeDebt.getEmployeeDebtStatus().equals("Disetujui")){
             totalDebt=totalDebt.add(employeeDebt.getEmployeeDebtNominal());
           }
       }
      return totalDebt;
    }
    
    public BigDecimal debtPaymentTotal(TblEmployee employee){
       BigDecimal totalPayment = new BigDecimal(0);
       List<TblCalendarEmployeePaymentHistory>list = parentController.getFDebtManager().getAllEmployeePaymentByIdEmployee(employee);
       for(TblCalendarEmployeePaymentHistory paymentDebt : list){
         totalPayment = totalPayment.add(paymentDebt.getEmployeePaymentDebtNominal());
       }
       return totalPayment;
    }
    
     private void setTableControlDataPaymentHistory(){
      ObservableList<Node>buttonControls = FXCollections.observableArrayList();
       JFXButton buttonControl;
       if(true){
         buttonControl = new JFXButton("Detail Pembayaran");
         buttonControl.setOnMouseClicked((e)->{
           dataPaymentHistoryPaymentHandle();
         });
         buttonControls.add(buttonControl);
       }
       
       if(DashboardController.feature.getSelectedRoleFeature().getPrintData()){
         buttonControl = new JFXButton("Cetak");
         buttonControl.setOnMouseClicked((e)->{
            dataPaymentHistoryPrintHandle();
         });
         buttonControls.add(buttonControl);
       }
       tblDataDebtPayment.addButtonControl(buttonControls);
     }
    
    @FXML
    private JFXTextField txtDebtTotal;
    @FXML
    private JFXTextField txtPaymentTotal;
    @FXML
    private JFXTextField txtBalance;
    @FXML
    private JFXTextField txtEmployee;
    @FXML
    private AnchorPane tblDebtPaymentDetailLayout;
    @FXML
    private JFXButton btnClose;
    
    public ClassTableWithControl tblDataDebtPaymentDetail;
    private int inputDataStatus;
    
    public ClassDataDebtPayment selectedData;
    
    private void initFormDebtPaymentHistory(){
       btnClose.setOnAction((e)->{
          debtPaymentHistoryCancelHandle(); 
       });
       
     
    }
    
    private void refreshDataTableDebtController() {
        tblDataDebtPayment.getTableView().setItems(FXCollections.observableArrayList(loadAllDataDebtPaymentHistory()));
        cft.refreshFilterItems(tblDataDebtPayment.getTableView().getItems());
    }
     
    private void setSelectedDataToInputForm(){
      txtEmployee.setText(selectedData.getEmployee().getCodeEmployee()+"-"+selectedData.getEmployee().getTblPeople().getFullName());
      Bindings.bindBidirectional(txtDebtTotal.textProperty(),selectedData.debtTotalProperty(),new ClassFormatter.CBigDecimalStringConverter());
      Bindings.bindBidirectional(txtPaymentTotal.textProperty(),selectedData.paymentTotalProperty(),new ClassFormatter.CBigDecimalStringConverter());
      Bindings.bindBidirectional(txtBalance.textProperty(),selectedData.balanceProperty(),new ClassFormatter.CBigDecimalStringConverter());
    }
    
    private final BooleanProperty isShowStatus = new SimpleBooleanProperty(false);
    private void dataPaymentHistoryShowHandle(){
      if(tblDataDebtPayment.getTableView().getSelectionModel().getSelectedItems().size()==1){
         inputDataStatus = 3;
         selectedData = (ClassDataDebtPayment)tblDataDebtPayment.getTableView().getSelectionModel().getSelectedItem();
         setSelectedDataToInputForm();
         initTableDataPaymentHistory();
         dataPaymentHistoryFormShowStatus.set(1);
         isShowStatus.set(true);
       }
    }
    
    private void dataPaymentHistoryUnshowHandle(){
      refreshDataTableDebtController();
      dataPaymentHistoryFormShowStatus.set(0);
      isShowStatus.set(false);
    }
    
    private void dataPaymentHistoryPaymentHandle(){
       if(tblDataDebtPayment.getTableView().getSelectionModel().getSelectedItems().size()==1){
         selectedData = (ClassDataDebtPayment)tblDataDebtPayment.getTableView().getSelectionModel().getSelectedItem();
         setSelectedDataToInputForm(); 
         initTableDataPaymentHistory();
         dataPaymentHistoryFormShowStatus.set(0);
         dataPaymentHistoryFormShowStatus.set(1);
        }
       else{
         ClassMessage.showWarningSelectedDataMessage(null, null);
       }
        
    }
    
    private void dataPaymentHistoryPrintHandle(){
       List<ClassPrintDataHutangKaryawan>listDataHutangKaryawan  = new ArrayList();
       ClassPrintDataHutangKaryawan dataHutangKaryawan = new ClassPrintDataHutangKaryawan();
       dataHutangKaryawan.setPrintedBy(ClassSession.currentUser.getTblEmployeeByIdemployee().getTblPeople().getFullName());
       List<ClassPrintDataHutangKaryawanDetail>listDataHutangKaryawanDetail = new ArrayList();
       for(int i = 0; i<tblDataDebtPayment.getTableView().getItems().size();i++){
         ClassDataDebtPayment dataDebtPayment = (ClassDataDebtPayment)tblDataDebtPayment.getTableView().getItems().get(i);
         ClassPrintDataHutangKaryawanDetail dataHutangKaryawanDetail = new ClassPrintDataHutangKaryawanDetail();
         dataHutangKaryawanDetail.setIdKaryawan(dataDebtPayment.getEmployee().getCodeEmployee());
         dataHutangKaryawanDetail.setNamaKaryawan(dataDebtPayment.getEmployee().getTblPeople().getFullName());
         dataHutangKaryawanDetail.setJabatan(dataDebtPayment.getEmployee().getTblJob().getJobName());
         dataHutangKaryawanDetail.setTipeKaryawan(dataDebtPayment.getEmployee().getRefEmployeeType().getTypeName());
         dataHutangKaryawanDetail.setSisaPinjaman(dataDebtPayment.getBalance());
         dataHutangKaryawanDetail.setTotalBayar(dataDebtPayment.getPaymentTotal());
         dataHutangKaryawanDetail.setTotalPinjaman(dataDebtPayment.getDebtTotal());
         listDataHutangKaryawanDetail.add(dataHutangKaryawanDetail);
       }
       dataHutangKaryawan.setListDataHutangKaryawanDetail(listDataHutangKaryawanDetail);
       listDataHutangKaryawan.add(dataHutangKaryawan);
       ClassPrinter.printDataHutangKaryawan(listDataHutangKaryawan);
    }
    
    private void initTableDataPaymentHistory(){
      setTableDataPaymentHistoryDetail();
      setTableControlPaymentHistoryDetail();
      
      tblDebtPaymentDetailLayout.getChildren().clear();
      AnchorPane.setTopAnchor( tblDataDebtPaymentDetail,15.0);
      AnchorPane.setBottomAnchor( tblDataDebtPaymentDetail,15.0);
      AnchorPane.setLeftAnchor( tblDataDebtPaymentDetail,15.0);
      AnchorPane.setRightAnchor( tblDataDebtPaymentDetail,15.0);
      tblDebtPaymentDetailLayout.getChildren().add( tblDataDebtPaymentDetail);
    }
    
    private void setTableDataPaymentHistoryDetail(){
       TableView<ClassDataDebtPaymentDetail>tblDebtPaymentDetail = new TableView();
       TableColumn<ClassDataDebtPaymentDetail,String>dateDebt = new TableColumn("Tanggal");
       dateDebt.setMinWidth(100);
       dateDebt.setCellValueFactory((TableColumn.CellDataFeatures<ClassDataDebtPaymentDetail,String>param)
       ->Bindings.createStringBinding(()->new SimpleDateFormat("dd MMM yyyy",new Locale("id")).format(param.getValue().getDateDebt()),param.getValue().dateDebtProperty()));
       TableColumn<ClassDataDebtPaymentDetail,String>description = new TableColumn("Deskripsi");
       description.setMinWidth(200);
       description.setCellValueFactory(cellData->cellData.getValue().descriptionProperty());
       TableColumn<ClassDataDebtPaymentDetail,String>debtNominal = new TableColumn("Pinjaman");
       debtNominal.setMinWidth(120);
       debtNominal.setCellValueFactory((TableColumn.CellDataFeatures<ClassDataDebtPaymentDetail,String>param)
       ->Bindings.createStringBinding(()->ClassFormatter.currencyFormat.cFormat(param.getValue().getDebtNominal()),param.getValue().debtNominalProperty()));
       TableColumn<ClassDataDebtPaymentDetail,String>paymentNominal = new TableColumn("Bayar");
       paymentNominal.setMinWidth(120);
       paymentNominal.setCellValueFactory((TableColumn.CellDataFeatures<ClassDataDebtPaymentDetail,String>param)
       ->Bindings.createStringBinding(()->ClassFormatter.currencyFormat.cFormat(param.getValue().getPaymentNominal()),param.getValue().paymentNominalProperty()));
       TableColumn<ClassDataDebtPaymentDetail,String>balanceNominal = new TableColumn("Saldo");
       balanceNominal.setMinWidth(120);
       balanceNominal.setCellValueFactory((TableColumn.CellDataFeatures<ClassDataDebtPaymentDetail,String>param)
       ->Bindings.createStringBinding(()->ClassFormatter.currencyFormat.cFormat(param.getValue().getBalance()),param.getValue().balanceProperty()));
       tblDebtPaymentDetail.getColumns().addAll(dateDebt,description,debtNominal,paymentNominal,balanceNominal);
       tblDebtPaymentDetail.setItems(FXCollections.observableArrayList(loadAllDataDebtPaymentDetail()));
       tblDataDebtPaymentDetail = new ClassTableWithControl(tblDebtPaymentDetail);
    }
    
    public List<ClassDataDebtPaymentDetail>loadAllDataDebtPaymentDetail(){
      List<TblCalendarEmployeeDebt>listDebt = parentController.getFDebtManager().getAllDataDebtByIdEmployee(selectedData.getEmployee());
      List<TblCalendarEmployeePaymentHistory>listPayment = parentController.getFDebtManager().getAllEmployeePaymentByIdEmployee(selectedData.getEmployee());
    //  System.out.println("size payment :"+listPayment.size());
      List<ClassDataDebtPaymentDetail>listDataDebtPaymentDetail = new ArrayList();
      
      BigDecimal totalBalance = new BigDecimal(0);
      
      for(TblCalendarEmployeeDebt debtDetail : listDebt){
           if(debtDetail.getEmployeeDebtStatus().equalsIgnoreCase("Disetujui")){
             ClassDataDebtPaymentDetail debtPaymentDetail = new ClassDataDebtPaymentDetail();
             debtPaymentDetail.setDateDebt(new java.sql.Date(debtDetail.getApprovedDate().getTime()));
             debtPaymentDetail.setDescription("Pinjaman");
             debtPaymentDetail.setDebtNominal(debtDetail.getEmployeeDebtNominal());
             debtPaymentDetail.setPaymentNominal(BigDecimal.ZERO);
             // totalBalance = totalBalance.add(debtPaymentDetail.getPaymentNominal().subtract(debtPaymentDetail.getDebtNominal()));
             // debtPaymentDetail.setBalance(totalBalance);
             listDataDebtPaymentDetail.add(debtPaymentDetail);
            }
        }
      
      for(TblCalendarEmployeePaymentHistory paymentDetail : listPayment){
           if(paymentDetail.getEmployeePaymentDebtNominal().doubleValue()>0){
             ClassDataDebtPaymentDetail debtPaymentDetail = new ClassDataDebtPaymentDetail();
             debtPaymentDetail.setDateDebt(new java.sql.Date(paymentDetail.getHistoryDate().getTime()));
             debtPaymentDetail.setDescription("Pembayaran : "+paymentDetail.getRefFinanceTransactionPaymentType().getTypeName());
             debtPaymentDetail.setDebtNominal(BigDecimal.ZERO);
             debtPaymentDetail.setPaymentNominal(paymentDetail.getEmployeePaymentDebtNominal());
          //   totalBalance = totalBalance.add(debtPaymentDetail.getPaymentNominal().subtract(debtPaymentDetail.getDebtNominal()));
          //   debtPaymentDetail.setBalance(totalBalance);
             listDataDebtPaymentDetail.add(debtPaymentDetail);
           }
       }
      
      Collections.sort(listDataDebtPaymentDetail,new Comparator<ClassDataDebtPaymentDetail>(){
          @Override
          public int compare(ClassDataDebtPaymentDetail o1, ClassDataDebtPaymentDetail o2) {
             return o1.getDateDebt().compareTo(o2.getDateDebt());
              //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
          }
          
      });
      
      for(ClassDataDebtPaymentDetail paymentDetail : listDataDebtPaymentDetail){
         totalBalance = totalBalance.add(paymentDetail.getDebtNominal().subtract(paymentDetail.getPaymentNominal()));
         paymentDetail.setBalance(totalBalance);
      }
      
      return listDataDebtPaymentDetail;
    }
    
    private void setTableControlPaymentHistoryDetail(){
       ObservableList<Node>buttonControls = FXCollections.observableArrayList();
       JFXButton buttonControl;
       if(DashboardController.feature.getSelectedRoleFeature().getCreateData()){
         buttonControl = new JFXButton("Bayar");
         buttonControl.setOnMouseClicked((e)->{
             System.out.println("hsl :"+selectedData.getBalance());
             //  selectedData = (ClassDataDebtPayment)tblDataDebtPayment.getTableView().getSelectionModel().getSelectedItem();
               if(selectedData.getBalance().doubleValue()!=0){
                 showEmployeeDebtPaymentDialog();
                }
               else{
                  HotelFX.showAlert(Alert.AlertType.WARNING, "DATA MASUKAN TIDAK SESUAI", "Tidak ada sisa hutang..!!",null);
               }
            
           // dataDebtCreateHandle();
         });
         buttonControls.add(buttonControl);
       }
       tblDataDebtPaymentDetail.addButtonControl(buttonControls);
    }
     
    public Stage dialogStage;
    private void showEmployeeDebtPaymentDialog(){
        try {
            FXMLLoader loader = new FXMLLoader();//"view/feature_debt/debt/DebtApproved.fxml");
            loader.setLocation(HotelFX.class.getResource("view/feature_debt/debt_payment_history/DebtPaymentView.fxml"));
            
            DebtPaymentController debtPaymentController = new DebtPaymentController(this);
            loader.setController(debtPaymentController);
            
            Region page = loader.load();
            
            dialogStage = new Stage();
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.initOwner(HotelFX.primaryStage);
            
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
    
    private void debtPaymentHistoryCancelHandle(){
       tblDataDebtPayment.getTableView().setItems(FXCollections.observableArrayList(loadAllDataDebtPaymentHistory())); 
       dataPaymentHistoryFormShowStatus.set(0);
       isShowStatus.set(false);
    }
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        setDataDebtSplitPane();
        initTableDataDebtPayment();
        initFormDebtPaymentHistory();
      //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    public DebtPaymentHistoryController(FeatureDebtController parentController){
       this.parentController = parentController;
    }
    
    private final FeatureDebtController parentController;
    
    public FDebtManager getService(){
       return parentController.getFDebtManager();
    }
}
