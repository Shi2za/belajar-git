/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hotelfx.view.feature_debt.debt;

//import hotelfx.view.feature_debt.FeatureDebtController;
import hotelfx.view.feature_debt.debt_payment_history.DebtPaymentController;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXCComboBoxPopup;
import com.jfoenix.controls.JFXCComboBoxTablePopup;
import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXTextArea;
import com.jfoenix.controls.JFXTextField;
import hotelfx.HotelFX;
import hotelfx.helper.ClassFilteringTable;
import hotelfx.helper.ClassFormatter;
import hotelfx.helper.ClassMessage;
import hotelfx.helper.ClassSession;
import hotelfx.helper.ClassTableWithControl;
import hotelfx.helper.ClassViewSetting;
import hotelfx.persistence.model.RefEmployeeType;
import hotelfx.persistence.model.TblCalendarEmployeeDebt;
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
import java.util.ArrayList;
import java.util.List;
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
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.SplitPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Region;
import static javafx.scene.layout.Region.USE_COMPUTED_SIZE;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;

/**
 *
 * @author Andreas
 */
public class DebtController implements Initializable{
    
    @FXML
    private SplitPane spDataDebt;
    
    public DoubleProperty dataDebtFormShowStatus;
    
    @FXML
    private AnchorPane contentLayout;
    
    @FXML
    private AnchorPane tblDataDebtLayoutDisableLayer;
    
    @FXML
    private AnchorPane ancBorderPane;
    
    private final PseudoClass halfPaymentPseudoClass = PseudoClass.getPseudoClass("halfPayment");
    
    private final PseudoClass fullPaymentPseudoClass = PseudoClass.getPseudoClass("fullPayment");
    
    private boolean isTimeLinePlaying = false;
    
    private void setDataDebtSplitPane(){
        spDataDebt.setDividerPositions(1);
        
       dataDebtFormShowStatus = new SimpleDoubleProperty(1.0);
       
       DoubleProperty divPosition = new SimpleDoubleProperty();
       
       divPosition.bind(new SimpleDoubleProperty(1.0)
                .subtract(dataDebtFormShowStatus)
        );

        divPosition.addListener((obs, oldVal, newVal) -> {
            isTimeLinePlaying = true;
            KeyValue keyValue = new KeyValue(spDataDebt.getDividers().get(0).positionProperty(), newVal);
            Timeline timeline = new Timeline(new KeyFrame(Duration.millis(ClassViewSetting.keyFrameDuration), keyValue));
            timeline.play();
            timeline.setOnFinished((e) -> {
                isTimeLinePlaying = false;
            });
        });
       
       SplitPane.Divider div = spDataDebt.getDividers().get(0);
       
       div.positionProperty().addListener((obs,oldVal,newVal)->{
           if (!isTimeLinePlaying) {
                div.setPosition(divPosition.get());
            }
       });
       
       dataDebtFormShowStatus.addListener((obs,oldVal,newVal)->{
           if(inputDataStatus!=3){
               if(newVal.doubleValue()==0.0){
                 ancBorderPane.setDisable(false);
                 tblDataDebtLayoutDisableLayer.setDisable(true);
                 ancBorderPane.toFront();
               }
               
               if(newVal.doubleValue()==1.0){
                 ancBorderPane.setDisable(true);
                 tblDataDebtLayoutDisableLayer.setDisable(false);
                 tblDataDebtLayoutDisableLayer.toFront();
               }
           }
       });
       
       dataDebtFormShowStatus.set(0);
    }
    
    @FXML
    private AnchorPane tblDataDebtLayout;
    @FXML
    private AnchorPane ancFiltering;
    public ClassTableWithControl tblDataDebt;
    ClassFilteringTable cft;
    
    private void initTableDataDebt(){
       setTableDataDebt();
       setTableControlDataDebt();
       
       tblDataDebtLayout.getChildren().clear();
       AnchorPane.setTopAnchor(tblDataDebt,0.0);
       AnchorPane.setBottomAnchor(tblDataDebt,15.0);
       AnchorPane.setLeftAnchor(tblDataDebt,15.0);
       AnchorPane.setRightAnchor(tblDataDebt,15.0);
       tblDataDebtLayout.getChildren().add(tblDataDebt);
    }
    
    private void setTableDataDebt(){
       TableView<TblCalendarEmployeeDebt>tableView = new TableView();
       tableView.setEditable(true);
       TableColumn<TblCalendarEmployeeDebt,String>dateCreate = new TableColumn("Buat");
       dateCreate.setMinWidth(90);
       dateCreate.setCellValueFactory((TableColumn.CellDataFeatures<TblCalendarEmployeeDebt,String>param)
       ->Bindings.createStringBinding(()->(new SimpleDateFormat("dd MMM yyyy")).format(new java.sql.Date(param.getValue().getCreatedDate().getTime())),param.getValue().approvedDateProperty()));
       TableColumn<TblCalendarEmployeeDebt,String>dateApproved = new TableColumn("Persetujuan");
       dateApproved.setMinWidth(90);
       dateApproved.setCellValueFactory((TableColumn.CellDataFeatures<TblCalendarEmployeeDebt,String>param)
       ->Bindings.createStringBinding(()->param.getValue().getApprovedDate()!=null ? (new SimpleDateFormat("dd MMM yyyy")).format(param.getValue().getApprovedDate()):"-",param.getValue().approvedDateProperty()));
       TableColumn<TblCalendarEmployeeDebt,String>dateReject = new TableColumn("Pembatalan");
       dateReject.setMinWidth(90);
       dateReject.setCellValueFactory((TableColumn.CellDataFeatures<TblCalendarEmployeeDebt,String>param)
       ->Bindings.createStringBinding(()->param.getValue().getRejectDate()!=null ? (new SimpleDateFormat("dd MMM yyyy")).format(param.getValue().getRejectDate()):"-",param.getValue().rejectDateProperty()));
       TableColumn<TblCalendarEmployeeDebt,String>date = new TableColumn("Tanggal");
       date.getColumns().addAll(dateCreate,dateApproved,dateReject);
       TableColumn<TblCalendarEmployeeDebt,String>employeeCode = new TableColumn("ID");
       employeeCode.setMinWidth(100);
       employeeCode.setCellValueFactory((TableColumn.CellDataFeatures<TblCalendarEmployeeDebt,String>param)
        ->Bindings.createStringBinding(()->param.getValue().getTblEmployeeByIdemployee().getCodeEmployee(),param.getValue().tblEmployeeByIdemployeeProperty()));
       TableColumn<TblCalendarEmployeeDebt,String>employeeType = new TableColumn("Tipe");
       employeeType.setMinWidth(120);
       employeeType.setCellValueFactory((TableColumn.CellDataFeatures<TblCalendarEmployeeDebt,String>param)
        ->Bindings.createStringBinding(()->param.getValue().getTblEmployeeByIdemployee().getRefEmployeeType().getTypeName(),param.getValue().getTblEmployeeByIdemployee().refEmployeeTypeProperty()));
       TableColumn<TblCalendarEmployeeDebt,String>employeeName = new TableColumn("Nama");
       employeeName.setMinWidth(160);
       employeeName.setCellValueFactory((TableColumn.CellDataFeatures<TblCalendarEmployeeDebt,String>param)
        ->Bindings.createStringBinding(()->param.getValue().getTblEmployeeByIdemployee().getTblPeople().getFullName(), param.getValue().tblEmployeeByIdemployeeProperty()));
       TableColumn<TblCalendarEmployeeDebt,String>employeeJob = new TableColumn("Jabatan");
       employeeJob.setMinWidth(120);
       employeeJob.setCellValueFactory((TableColumn.CellDataFeatures<TblCalendarEmployeeDebt,String>param)
        ->Bindings.createStringBinding(()->param.getValue().getTblEmployeeByIdemployee().getTblJob().getJobName(),param.getValue().tblEmployeeByIdemployeeProperty()));
       TableColumn<TblCalendarEmployeeDebt,String>employee = new TableColumn("Karyawan");
       employee.getColumns().addAll(employeeType,employeeCode,employeeName,employeeJob);
       TableColumn<TblCalendarEmployeeDebt,String>employeeNominal = new TableColumn(" Nominal \n Peminjaman");
       employeeNominal.setCellValueFactory((TableColumn.CellDataFeatures<TblCalendarEmployeeDebt,String>param)
        ->Bindings.createStringBinding(()->ClassFormatter.currencyFormat.cFormat(param.getValue().getEmployeeDebtNominal()), param.getValue().employeeDebtNominalProperty()));
       employeeNominal.setMinWidth(120);
       TableColumn<TblCalendarEmployeeDebt,String>employeeDebtStatus = new TableColumn("Status");
       employeeDebtStatus.setCellValueFactory(cellData->cellData.getValue().employeeDebtStatusProperty());
       employeeDebtStatus.setMinWidth(100);
      /* TableColumn<TblCalendarEmployeeDebt,String>employeePaymentDebtStatus = new TableColumn("Pembayaran");
       employeePaymentDebtStatus.setCellValueFactory(cellData->cellData.getValue().employeeDebtPaymentStatusProperty());
       employeePaymentDebtStatus.setMinWidth(120); 
     /*  TableColumn<TblCalendarEmployeeDebt,String>employeeStatus = new TableColumn("Status");
       employeeStatus.getColumns().addAll(employeeDebtStatus,employeePaymentDebtStatus); */
       TableColumn<TblCalendarEmployeeDebt,String>employeeDebtNote = new TableColumn("Keterangan");
       employeeDebtNote.setCellValueFactory((TableColumn.CellDataFeatures<TblCalendarEmployeeDebt,String>param)
        ->Bindings.createStringBinding(()->(param.getValue().getEmployeeDebtNote()!=null ?"Alasan Pinjam :\n"+param.getValue().getEmployeeDebtNote()+"\n":"")+
                                       (param.getValue().getEmployeeRejectNote()!=null ?"Alasan Ditolak:\n"+param.getValue().getEmployeeRejectNote():"") +
                                       (param.getValue().getEmployeeCanceledNote()!=null ? "Alasan Batal:\n"+param.getValue().getEmployeeCanceledNote():""),param.getValue().employeeDebtNoteProperty()));
       employeeDebtNote.setMinWidth(200);
     /*  TableColumn<TblCalendarEmployeeDebt,TblCalendarEmployeeDebt>approved = new TableColumn("Persetujuan");
       approved.setEditable(true);
       approved.setCellValueFactory(param->new ReadOnlyObjectWrapper<>(param.getValue()));
       approved.setCellFactory(new Callback<TableColumn<TblCalendarEmployeeDebt,TblCalendarEmployeeDebt>,TableCell<TblCalendarEmployeeDebt,TblCalendarEmployeeDebt>>(){
           @Override
           public TableCell<TblCalendarEmployeeDebt, TblCalendarEmployeeDebt> call(TableColumn<TblCalendarEmployeeDebt, TblCalendarEmployeeDebt> param) {
               return new ButtonCellApproved();//throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
           }
       });
       approved.setMinWidth(140);*/
       tableView.getColumns().addAll(employeeDebtStatus,employee,employeeNominal,date,employeeDebtNote);
       tableView.setItems(loadAllDataDebt());
       tableView.setRowFactory(tv->{
           TableRow<TblCalendarEmployeeDebt>row = new TableRow();
           row.setOnMouseClicked((e)->{
               if(e.getClickCount()==2){
                   if(isShowStatus.get()){
                      dataDebtUnshowHandle();
                    }
                   else{
                      if(!row.isEmpty()){
                           if(row.getItem().getEmployeeDebtStatus().equals("Dibuat")){
                             dataDebtUpdateHandle();
                            }
                           else{
                             dataDebtShowHandle();
                           }
                       }
                   }
               }
           });
           
          /* row.itemProperty().addListener((obs,oldVal,newVal)->{
              if(newVal!=null){
                 row.pseudoClassStateChanged(halfPaymentPseudoClass,newVal.getEmployeeDebtPaymentStatus().equalsIgnoreCase("Dibayar Sebagian"));
                 row.pseudoClassStateChanged(fullPaymentPseudoClass,newVal.getEmployeeDebtPaymentStatus().equalsIgnoreCase("Sudah Dibayar"));
              }
           });*/
           
           return row;
       });
       tblDataDebt = new ClassTableWithControl(tableView);
       
       cft = new ClassFilteringTable(TblCalendarEmployeeDebt.class,tblDataDebt.getTableView(),tblDataDebt.getTableView().getItems());
       ancFiltering.getChildren().clear();
       AnchorPane.setTopAnchor(cft,25.0);
       AnchorPane.setBottomAnchor(cft,0.0);
       AnchorPane.setRightAnchor(cft,15.0);
       ancFiltering.getChildren().add(cft);
    }
    
    public ObservableList<TblCalendarEmployeeDebt> loadAllDataDebt(){
      return FXCollections.observableArrayList(parentController.getFDebtManager().getAllDataDebt());
    }
    
    /*public class ButtonCellApproved extends TableCell<TblCalendarEmployeeDebt,TblCalendarEmployeeDebt>{
       JFXButton cellButton;
      
       public ButtonCellApproved(){
         this.cellButton = new JFXButton();
       }
       
       public void updateItem(TblCalendarEmployeeDebt item,boolean empty){
          super.updateItem(item, empty);
           if(!empty){
                if(!isEditing()){
                   if(getTableRow()!=null){
                      Object data = getTableRow().getItem();
                       if(data!=null){
                          if(((TblCalendarEmployeeDebt)data).getEmployeeDebtStatus().equals("Dibuat")){
                               setGraphic(cellButton);
                               cellButton.setPrefSize(20,25);
                               cellButton.setOnAction((e)->{
                                 selectedData = item;
                                 showEmployeeDebtApprovedDialog();
                               }); 
                            }
                            
                            if(((TblCalendarEmployeeDebt)data).getEmployeeDebtStatus().equals("Disetujui")){
                             setGraphic(cellButton);
                             cellButton.setPrefSize(20,25);
                             cellButton.setDisable(true);
                            }
                        }
                    }
                }
            }
           else{
             setGraphic(null);
           }
        }
    }*/
     
    private void setTableControlDataDebt(){
      ObservableList<Node>buttonControls = FXCollections.observableArrayList();
       JFXButton buttonControl;
       if(DashboardController.feature.getSelectedRoleFeature().getCreateData()){
         buttonControl = new JFXButton("Tambah");
         buttonControl.setOnMouseClicked((e)->{
            dataDebtCreateHandle();
         });
         buttonControls.add(buttonControl);
       }
       
       if(DashboardController.feature.getSelectedRoleFeature().getUpdateData()){
         buttonControl = new JFXButton("Ubah");
         buttonControl.setOnMouseClicked((e)->{
           dataDebtUpdateHandle();
         });
         
         buttonControls.add(buttonControl);
       }
       
       if(DashboardController.feature.getSelectedRoleFeature().getDeleteData()){
          buttonControl = new JFXButton("Hapus");
          buttonControl.setOnMouseClicked((e)->{
            dataDebtDeleteHandle();
          });
          buttonControls.add(buttonControl);
       }
       
       if(DashboardController.feature.getSelectedRoleFeature().getApproveData()){
        buttonControl = new JFXButton("Persetujuan");
        buttonControl.setOnMouseClicked((e)->{
          dataDebtApprovedHandle();
        });
        buttonControls.add(buttonControl);
       }
       
       buttonControl = new JFXButton("Tolak");
       buttonControl.setOnMouseClicked((e)->{
         dataDebtRejectHandle(); 
       });
       buttonControls.add(buttonControl);
       
     /*  buttonControl = new JFXButton("Pembayaran");
       buttonControl.setOnMouseClicked((e)->{
         //showEmployeeDebtPaymentDialog();
         dataDebtPaymentHandle();
       });
       buttonControls.add(buttonControl); */
       
       
       tblDataDebt.addButtonControl(buttonControls); 
    }
    
    //form insert Debt
    @FXML
    private AnchorPane formAnchor;
    @FXML
    private ScrollPane spFormDataDebt;
    @FXML
    private GridPane gpFormDataDebt;
    
   /* @FXML
    private Label lblEmployeeDebtStatus;
    @FXML
    private Label lblEmployeePaymentDebtStatus; */
    
    @FXML
    private AnchorPane cbpRefEmployeeTypeLayout;
    private final JFXCComboBoxPopup<RefEmployeeType>cbpRefEmployeeType = new JFXCComboBoxPopup<>(RefEmployeeType.class);
    @FXML
    private AnchorPane cbpEmployeeNameLayout;
    private JFXCComboBoxTablePopup<TblEmployee>cbpEmployeeName;
    
    private int inputDataStatus;
 /*   @FXML
    private JFXDatePicker dpDebt; */
    @FXML 
    private JFXTextField txtNominalDebt;
    @FXML
    private JFXTextArea txtDebtNote;
    @FXML
    private JFXButton btnSave;
    @FXML
    private JFXButton btnCancel;
    
    
    private final PseudoClass onScrollPseudoClass = PseudoClass.getPseudoClass("onScroll");
    private final BooleanProperty isFormScroll = new SimpleBooleanProperty(false);
    private int scrollCounter = 0;
    public TblCalendarEmployeeDebt selectedData;
   
    
    
    private void initFormDataDebt(){
       isFormScroll.addListener((obs,oldScroll,newScroll)->{
         spFormDataDebt.pseudoClassStateChanged(onScrollPseudoClass,newScroll);
       });
       
       gpFormDataDebt.setOnScroll((ScrollEvent scroll)->{
           isFormScroll.set(true);
           
           scrollCounter++;
           
           Thread thread = new Thread(()->{
               try{
                 Thread.sleep(1000);
                 Platform.runLater(()->{
                    if(scrollCounter==1){
                       isFormScroll.set(false);
                    }
                    scrollCounter--;
                 });
               }
               catch(Exception e){
                   System.out.println("err>>"+e.getLocalizedMessage());
               }
           });
           thread.setDaemon(true);
           thread.start();
        });
       
        btnSave.setOnAction((e)->{
            dataDebtSaveHandle();
        });
        
        btnCancel.setOnAction((e)->{
           dataDebtCancelHandle();
        });
        
       initFormDataPopUpEmployeeType();
       initFormDataPopUpEmployeeName();
       
    //   initCalendarDate();
       initImportantFieldColor();
       initNumbericField();
       
    }
    
    private void initImportantFieldColor() {
        ClassViewSetting.setImportantField(
                cbpRefEmployeeType, 
                cbpEmployeeName, 
             //   dpDebt, 
                txtNominalDebt,
                txtDebtNote);
    }
    
    private void initNumbericField() {
        ClassFormatter.setToNumericField(
                "BigDecimal",
                txtNominalDebt);
    }
    
    private void initFormDataPopUpEmployeeName(){
       initFormDataPopUpEmployeeType();
       setTableFormEmployeeName();
       
       cbpRefEmployeeTypeLayout.getChildren().clear();
       AnchorPane.setTopAnchor(cbpRefEmployeeType,0.0);
       AnchorPane.setBottomAnchor(cbpRefEmployeeType,0.0);
       AnchorPane.setLeftAnchor(cbpRefEmployeeType,0.0);
       AnchorPane.setRightAnchor(cbpRefEmployeeType,0.0);
       cbpRefEmployeeTypeLayout.getChildren().add(cbpRefEmployeeType);
       
       cbpEmployeeNameLayout.getChildren().clear();
       AnchorPane.setTopAnchor(cbpEmployeeName,0.0);
       AnchorPane.setBottomAnchor(cbpEmployeeName,0.0);
       AnchorPane.setLeftAnchor(cbpEmployeeName,0.0);
       AnchorPane.setRightAnchor(cbpEmployeeName,0.0);
       cbpEmployeeNameLayout.getChildren().add(cbpEmployeeName);
    }
    
   /* private void initCalendarDate(){
      ClassFormatter.setDatePickersPattern("dd MMM yyyy",dpDebt);
    } */
    
    private void initFormDataPopUpEmployeeType(){
      TableView<RefEmployeeType>tblEmployeeType = new TableView();
      TableColumn<RefEmployeeType,String>employeeType = new TableColumn("Tipe Karyawan");
      employeeType.setMinWidth(160);
      employeeType.setCellValueFactory(cellData->cellData.getValue().typeNameProperty());
      tblEmployeeType.getColumns().addAll(employeeType);
      ObservableList<RefEmployeeType>employeeTypeItems = FXCollections.observableArrayList(loadAllDataEmployeeType());
      setFunctionPopup(cbpRefEmployeeType,tblEmployeeType,employeeTypeItems,"typeName","Tipe Karyawan *",300,300);
      //gpFormDataDebt.add(cbpRefEmployeeType,1,3);
    }
    
    private void setTableFormEmployeeName(){
      TableView<TblEmployee>tblEmployee = new TableView();
      TableColumn<TblEmployee,String>codeEmployee = new TableColumn("ID");
      codeEmployee.setMinWidth(100);
      codeEmployee.setCellValueFactory(cellData->cellData.getValue().codeEmployeeProperty());
      TableColumn<TblEmployee,String>typeEmployee = new TableColumn("Tipe");
      typeEmployee.setMinWidth(120);
      typeEmployee.setCellValueFactory((TableColumn.CellDataFeatures<TblEmployee,String>param)
        ->Bindings.createStringBinding(()->param.getValue().getRefEmployeeType().getTypeName(),param.getValue().refEmployeeTypeProperty()));
      TableColumn<TblEmployee,String>nameEmployee = new TableColumn("Nama");
      nameEmployee.setMinWidth(160);
      nameEmployee.setCellValueFactory((TableColumn.CellDataFeatures<TblEmployee,String>param)
        ->Bindings.createStringBinding(()->param.getValue().getTblPeople().getFullName(),param.getValue().tblPeopleProperty()));
      TableColumn<TblEmployee,String>jobEmployee = new TableColumn("Jabatan");
      jobEmployee.setMinWidth(140);
      jobEmployee.setCellValueFactory((TableColumn.CellDataFeatures<TblEmployee,String>param)
         ->Bindings.createStringBinding(()->param.getValue().getTblJob().getJobName(),param.getValue().tblJobProperty()));
      tblEmployee.getColumns().addAll(typeEmployee,codeEmployee,nameEmployee,jobEmployee);
      ObservableList<TblEmployee>employeeNameItems = FXCollections.observableArrayList();
      cbpRefEmployeeType.valueProperty().addListener((obs,oldEmployeeType,newEmployeeType)->{
           if(newEmployeeType!=null){
             employeeNameItems.setAll(loadAllDataEmployeeName(newEmployeeType));
           }
      });
      
      cbpEmployeeName = new JFXCComboBoxTablePopup<>(TblEmployee.class,tblEmployee,employeeNameItems,"","Nama Karyawan *",true,500,300);
     // setFunctionPopup(cbpEmployeeName,tblEmployee,employeeNameItems,"tblPeople","Nama Karyawan *",500,300);
      
      //ObservableList<TblEmployee>employeeNameItems = FXCollections.observableArrayList(loadAllDataEmployeeType)
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
    
    private List<RefEmployeeType>loadAllDataEmployeeType(){
       List<RefEmployeeType>listEmployeeType = new ArrayList();
       RefEmployeeType employeeTypeAll = new RefEmployeeType();
       employeeTypeAll.setIdtype(3);
       employeeTypeAll.setTypeName("Semua Tipe Karyawan");
       listEmployeeType.add(employeeTypeAll);
       listEmployeeType.addAll(parentController.getFDebtManager().getAllEmployeeType());
       return listEmployeeType;
    }
    
    private List<TblEmployee>loadAllDataEmployeeName(RefEmployeeType employeeType){
      return parentController.getFDebtManager().getAllEmployeeName(employeeType);
    }
    
    private void refreshDataPopup(){
       ObservableList<RefEmployeeType>listEmployeeType = FXCollections.observableArrayList(loadAllDataEmployeeType());
       cbpRefEmployeeType.setItems(listEmployeeType);
       
       ObservableList<TblEmployee>listEmployee = FXCollections.observableArrayList();
       cbpRefEmployeeType.valueProperty().addListener((obs,oldVal,newVal)->{
           if(newVal!=null){
             listEmployee.setAll(loadAllDataEmployeeName(newVal));
           }
       });
       cbpEmployeeName.setItems(listEmployee);
    }
    
    private void refreshDataPopupUpdate(){
       RefEmployeeType employeeType = new RefEmployeeType();
       employeeType.setIdtype(3);
       employeeType.setTypeName("Semua Tipe Karyawan");
       ObservableList<TblEmployee>listEmployee = FXCollections.observableArrayList();
       listEmployee.setAll(loadAllDataEmployeeName(employeeType));
       cbpEmployeeName.setItems(listEmployee);
    }
    
    public void refeshDataFiltering(){
      tblDataDebt.getTableView().setItems(loadAllDataDebt());
      cft.refreshFilterItems(tblDataDebt.getTableView().getItems());
    }
    
    private void selectedDataToInputForm(){
      refreshDataPopup();
       
      cbpRefEmployeeType.setDisable(false);
      cbpEmployeeName.setDisable(false);
       cbpRefEmployeeType.setValue(null);
    //   dpDebt.setValue(null);
       cbpRefEmployeeType.valueProperty().addListener((obs,oldVal,newVal)->{
           if(newVal!=null && inputDataStatus == 0){
              cbpEmployeeName.setValue(null);
            }
        });
      
     // lblEmployeeDebtStatus.textProperty().bindBidirectional(selectedData.employeeDebtStatusProperty());
     // lblEmployeePaymentDebtStatus.textProperty().bindBidirectional(selectedData.employeeDebtPaymentStatusProperty());
      cbpEmployeeName.valueProperty().bindBidirectional(selectedData.tblEmployeeByIdemployeeProperty());
      cbpEmployeeName.hide();
      
      Bindings.bindBidirectional(txtNominalDebt.textProperty(),selectedData.employeeDebtNominalProperty(),new ClassFormatter.CBigDecimalStringConverter());
      txtDebtNote.textProperty().bindBidirectional(selectedData.employeeDebtNoteProperty());
      selectedDataInputFormFunctionalComponent();
    }
    
    private void selectedDataToUpdateForm(){
      refreshDataPopupUpdate();
      cbpRefEmployeeType.setDisable(true);
      cbpEmployeeName.setDisable(true);
      cbpRefEmployeeType.setValue(selectedData.getTblEmployeeByIdemployee().getRefEmployeeType());
    //  lblEmployeeDebtStatus.textProperty().bindBidirectional(selectedData.employeeDebtStatusProperty());
    //  lblEmployeePaymentDebtStatus.textProperty().bindBidirectional(selectedData.employeeDebtPaymentStatusProperty());
      cbpEmployeeName.valueProperty().bindBidirectional(selectedData.tblEmployeeByIdemployeeProperty());
      cbpEmployeeName.hide();
    
       cbpEmployeeName.valueProperty().addListener((obs,oldVal,newVal)->{
            if(newVal!=null && inputDataStatus==1){
              cbpRefEmployeeType.setValue(newVal.getRefEmployeeType());
            }
        });
           
      /* if(selectedData.!=null){
          dpDebt.setValue(((java.sql.Date)selectedData.getSysCalendar().getCalendarDate()).toLocalDate());
       } */
       
        
        Bindings.bindBidirectional(txtNominalDebt.textProperty(),selectedData.employeeDebtNominalProperty(),new ClassFormatter.CBigDecimalStringConverter());
        txtDebtNote.textProperty().bindBidirectional(selectedData.employeeDebtNoteProperty());
        
        selectedDataInputFormFunctionalComponent();
    }
    
    private void selectedDataInputFormFunctionalComponent(){
       //gpFormDataDebt.setDisable(inputDataStatus==3);
       ClassViewSetting.setDisableForAllInputNode(gpFormDataDebt,inputDataStatus==3,cbpRefEmployeeType,cbpEmployeeName);
       btnSave.setVisible(inputDataStatus!=3);
      // btnCancel.setVisible(inputDataStatus!=3);
    }
    
    private void dataDebtCreateHandle(){
       selectedData = new TblCalendarEmployeeDebt();
       selectedData.setEmployeeDebtStatus("Dibuat");
       selectedData.setEmployeeDebtPaymentStatus("Belum Dibayar");
       selectedData.setEmployeeDebtNominal(new BigDecimal(0));
       inputDataStatus = 0;
       selectedDataToInputForm();
       
       dataDebtFormShowStatus.set(0);
       dataDebtFormShowStatus.set(1);
       //set unsaving data input -> 'true'
       ClassSession.unSavingDataInput.set(true);
     
    }
    
    private void dataDebtApprovedHandle(){
       if(tblDataDebt.getTableView().getSelectionModel().getSelectedItems().size()==1){
          if(((TblCalendarEmployeeDebt)tblDataDebt.getTableView().getSelectionModel().getSelectedItem()).getEmployeeDebtStatus().equals("Dibuat")){
            selectedData = parentController.getFDebtManager().getEmployeeDebt(((TblCalendarEmployeeDebt)tblDataDebt.getTableView().getSelectionModel().getSelectedItem()).getIdemployeeDebt());
            showEmployeeDebtApprovedDialog();
          }
          else{
            HotelFX.showAlert(Alert.AlertType.WARNING,"SALAH PILIH DATA","Silahkan pilih data dengan status Peminjaman 'Dibuat' !!",null);
          }
       }
       else{
         ClassMessage.showWarningSelectedDataMessage(null,null);
       }
    }
    
  /*  private void dataDebtPaymentHandle(){
       if(tblDataDebt.getTableView().getSelectionModel().getSelectedItems().size()==1){
           if(((TblCalendarEmployeeDebt)tblDataDebt.getTableView().getSelectionModel().getSelectedItem()).getEmployeeDebtStatus().equals("Disetujui") && !((TblCalendarEmployeeDebt)tblDataDebt.getTableView().getSelectionModel().getSelectedItem()).getEmployeeDebtPaymentStatus().equals("Sudah Dibayar")){
             selectedData = parentController.getFDebtManager().getEmployeeDebt(((TblCalendarEmployeeDebt)tblDataDebt.getTableView().getSelectionModel().getSelectedItem()).getIdemployeeDebt());
            // showEmployeeDebtPaymentDialog(); 
            }
           else{
             HotelFX.showAlert(Alert.AlertType.WARNING,"SALAH PILIH DATA","Silahkan pilih data dengan status \n"
                     + " Peminjaman : 'Disetujui' \n"
                     + " Pembayaran : 'Belum Dibayar / Dibayar Sebagian' !!",null);
           }
       }
       else{
          ClassMessage.showWarningSelectedDataMessage(null,null);
       }
       //selectedData = parentController.getFDebtManager().getEmployeeDebt(((TblCalendarEmployeeDebt)tblDataDebt.getTableView().getSelectionModel().getSelectedItem()).getIdemployeeDebt());
    } */
    
    private void dataDebtUpdateHandle(){
       if(tblDataDebt.getTableView().getSelectionModel().getSelectedItems().size()==1){
           if(((TblCalendarEmployeeDebt)tblDataDebt.getTableView().getSelectionModel().getSelectedItem()).getEmployeeDebtStatus().equals("Dibuat")){
              selectedData = parentController.getFDebtManager().getEmployeeDebt(((TblCalendarEmployeeDebt)tblDataDebt.getTableView().getSelectionModel().getSelectedItem()).getIdemployeeDebt());
              inputDataStatus = 1;
              selectedDataToUpdateForm();
              dataDebtFormShowStatus.set(0);
              dataDebtFormShowStatus.set(1);
              //set unsaving data input -> 'true'
              ClassSession.unSavingDataInput.set(true);
            }
           else{
             HotelFX.showAlert(Alert.AlertType.WARNING,"SALAH PILIH DATA","Silahkan pilih data dengan status pinjaman 'Dibuat' !!",null);
           }
        }
       else{
         ClassMessage.showWarningSelectedDataMessage(null,null);
       }
    }
    
    private final BooleanProperty isShowStatus = new SimpleBooleanProperty(false);
    private void dataDebtShowHandle(){
       if(tblDataDebt.getTableView().getSelectionModel().getSelectedItems().size()==1){
          inputDataStatus = 3;
          selectedData = parentController.getFDebtManager().getEmployeeDebt(((TblCalendarEmployeeDebt)tblDataDebt.getTableView().getSelectionModel().getSelectedItem()).getIdemployeeDebt());
          selectedDataToUpdateForm();
          dataDebtFormShowStatus.set(1);
          isShowStatus.set(true);
       }
    }
    
    private void dataDebtUnshowHandle(){
        tblDataDebt.getTableView().setItems(loadAllDataDebt());
        dataDebtFormShowStatus.set(0);
        isShowStatus.set(false);
    }
    
    //Pinjaman ditolak
    private void dataDebtRejectHandle(){
       if(tblDataDebt.getTableView().getSelectionModel().getSelectedItems().size()==1){
           if(((TblCalendarEmployeeDebt)tblDataDebt.getTableView().getSelectionModel().getSelectedItem()).getEmployeeDebtStatus().equalsIgnoreCase("Dibuat")){
             selectedData = parentController.getFDebtManager().getEmployeeDebt(((TblCalendarEmployeeDebt)tblDataDebt.getTableView().getSelectionModel().getSelectedItem()).getIdemployeeDebt());
             //selectedData.setEmployeeDebtStatus("Ditolak");
             showEmployeeRejectDebtDialog();
             //showEmployeePasswordDebtDialog();  
           }
           else{
             HotelFX.showAlert(Alert.AlertType.WARNING,"SALAH PILIH DATA","Silahkan pilih data dengan status pinjaman 'Dibuat' !!",null);
           }
       }
       else{
         ClassMessage.showWarningSelectedDataMessage(null,null);
       }
    }
    
    private void dataDebtDeleteHandle(){
       if(tblDataDebt.getTableView().getSelectionModel().getSelectedItems().size()==1){
          if(((TblCalendarEmployeeDebt)tblDataDebt.getTableView().getSelectionModel().getSelectedItem()).getEmployeeDebtStatus().equals("Dibuat")){
              Alert alertDelete = ClassMessage.showConfirmationDeletingDataMessage(null,null);
               if(alertDelete.getResult()==ButtonType.OK){
                   TblCalendarEmployeeDebt dummySelected = new TblCalendarEmployeeDebt((TblCalendarEmployeeDebt)tblDataDebt.getTableView().getSelectionModel().getSelectedItem());
                       if(parentController.getFDebtManager().deleteDataDebt(dummySelected,null)){
                          ClassMessage.showSucceedDeletingDataMessage(null,null);
                          tblDataDebt.getTableView().setItems(loadAllDataDebt());
                          inputDataStatus = 0;
                          refeshDataFiltering();
                        }   
                   }
            }
          else{
             HotelFX.showAlert(Alert.AlertType.WARNING,"DATA YANG DIPILIH SALAH","Silahkan pilih data dengan status 'Dibuat' !!",null);  
           }
       }
           /*if(((TblCalendarEmployeeDebt)tblDataDebt.getTableView().getSelectionModel().getSelectedItem()).getEmployeeDebtPaymentStatus().equalsIgnoreCase("Belum Dibayar")){
               Alert alertDelete = ClassMessage.showConfirmationDeletingDataMessage(null,null);
               if(alertDelete.getResult()==ButtonType.OK){
                   if(((TblCalendarEmployeeDebt)tblDataDebt.getTableView().getSelectionModel().getSelectedItem()).getEmployeeDebtStatus().equals("Dibuat")){
                        TblCalendarEmployeeDebt dummySelected = new TblCalendarEmployeeDebt((TblCalendarEmployeeDebt)tblDataDebt.getTableView().getSelectionModel().getSelectedItem());
                       if(parentController.getFDebtManager().deleteDataDebt(dummySelected,null)){
                          ClassMessage.showSucceedDeletingDataMessage(null,null);
                          tblDataDebt.getTableView().setItems(loadAllDataDebt());
                          inputDataStatus = 0;
                          refeshDataFiltering();
                        }  
                    }
                  /* else if(((TblCalendarEmployeeDebt)tblDataDebt.getTableView().getSelectionModel().getSelectedItem()).getEmployeeDebtStatus().equals("Disetujui")){
                       selectedData = parentController.getFDebtManager().getEmployeeDebt(((TblCalendarEmployeeDebt)tblDataDebt.getTableView().getSelectionModel().getSelectedItem()).getIdemployeeDebt());
                       showEmployeePasswordDebtDialog();  
                    } */
              //}
          
           
           /*if(((TblCalendarEmployeeDebt)tblDataDebt.getTableView().getSelectionModel().getSelectedItem()).getEmployeeDebtStatus().equals("Dibuat")){
               Alert alertDelete = ClassMessage.showConfirmationDeletingDataMessage(null,null);
               if(alertDelete.getResult()==ButtonType.OK){
                       TblCalendarEmployeeDebt dummySelected = new TblCalendarEmployeeDebt((TblCalendarEmployeeDebt)tblDataDebt.getTableView().getSelectionModel().getSelectedItem());
                       if(parentController.getFDebtManager().deleteDataDebt(dummySelected,null)){
                         ClassMessage.showSucceedDeletingDataMessage(null,null);
                         tblDataDebt.getTableView().setItems(loadAllDataDebt());
                         inputDataStatus = 0;
                       }  
                   }
                }
                else if(((TblCalendarEmployeeDebt)tblDataDebt.getTableView().getSelectionModel().getSelectedItem()).getEmployeeDebtStatus().equals("Disetujui")){
                  
            }
           else{
             HotelFX.showAlert(Alert.AlertType.WARNING,"SALAH PILIH DATA","Silahkan pilih data dengan status belum dibayar !!",null);
           }*/
        //}
       else{
         ClassMessage.showWarningSelectedDataMessage(null,null);
        }
    }
    
    private void dataDebtSaveHandle(){
       if(dataDebtCheckDataInput()){
           Alert alertInput = ClassMessage.showConfirmationSavingDataMessage("",null);
           if(alertInput.getResult()==ButtonType.OK){
                TblCalendarEmployeeDebt dummySelectedData = new TblCalendarEmployeeDebt(selectedData);
                switch(inputDataStatus){
                    case 0 :
                        if(parentController.getFDebtManager().insertDataDebt(dummySelectedData)){
                           ClassMessage.showSucceedInsertingDataMessage("",null);
                           tblDataDebt.getTableView().setItems(loadAllDataDebt());
                           dataDebtFormShowStatus.set(0);
                           refeshDataFiltering();
                           //set unsaving data input -> 'false'
                            ClassSession.unSavingDataInput.set(false);
                         }
                         else{
                          ClassMessage.showFailedInsertingDataMessage(parentController.getFDebtManager().getErrorMessage(),null);
                         }
                     break;

                     case 1 :
                         if(parentController.getFDebtManager().updateDataDebt(dummySelectedData)){
                           ClassMessage.showSucceedUpdatingDataMessage("",null);
                           tblDataDebt.getTableView().setItems(loadAllDataDebt());
                           dataDebtFormShowStatus.set(0);
                           refeshDataFiltering();
                           //set unsaving data input -> 'false'
                            ClassSession.unSavingDataInput.set(false);
                         }
                        else{
                          ClassMessage.showFailedUpdatingDataMessage(parentController.getFDebtManager().getErrorMessage(),null);
                         }
                     break;
                 }
           }
        }
       else{
         ClassMessage.showWarningInputDataMessage(errDataInput,null);
       }
    }
    
    private String errDataInput;
    private boolean dataDebtCheckDataInput(){
       boolean check = true;
       errDataInput = "";
       if(cbpRefEmployeeType.getValue()==null){
         errDataInput+="Tipe Karyawan :"+ClassMessage.defaultErrorNullValueMessage+"\n";
         check = false;
        }
       
       if(cbpEmployeeName.getValue()==null){
         errDataInput+="Nama Karyawan  :"+ClassMessage.defaultErrorNullValueMessage+"\n";
         check = false;
        }
       
     /*  if(dpDebt.getValue()==null){
          errDataInput+="Tanggal Peminjaman :"+ClassMessage.defaultErrorNullValueMessage+"\n";
          check = false;
       } */
       
       if(txtNominalDebt.getText() == null
               || txtNominalDebt.getText().equals("")
               || txtNominalDebt.getText().equals("-")){
           errDataInput+="Nominal Peminjaman :"+ClassMessage.defaultErrorNullValueMessage+"\n";
          check = false;
       }else{
        if(selectedData.getEmployeeDebtNominal()
                .compareTo(new BigDecimal("0")) < 1){
          errDataInput+="Nominal Peminjaman :"+ClassMessage.defaultErrorZeroValueMessage+"\n";
          check = false;
        }
       }
       
       if(txtDebtNote.getText()==null || txtDebtNote.getText().equalsIgnoreCase("")){
          errDataInput+="Alasan Pinjam :"+ClassMessage.defaultErrorNullValueMessage+"\n";
          check = false;
       }
       return check;
    }
    
    private void dataDebtCancelHandle(){
       tblDataDebt.getTableView().setItems(loadAllDataDebt());
       dataDebtFormShowStatus.set(0);
       refeshDataFiltering();
       isShowStatus.set(false);
       //set unsaving data input -> 'false'
       ClassSession.unSavingDataInput.set(false);
    }
    
    //approved Form
    public Stage dialogStageApproved;
    private void showEmployeeDebtApprovedDialog(){
        try {
            FXMLLoader loader = new FXMLLoader();//"view/feature_debt/debt/DebtApproved.fxml");
            loader.setLocation(HotelFX.class.getResource("view/feature_debt/debt/DebtApproved.fxml"));
            
            DebtApprovedController debtApprovedController = new DebtApprovedController(this);
            loader.setController(debtApprovedController);
            
            Region page = loader.load();
            
            dialogStageApproved = new Stage();
            dialogStageApproved.initModality(Modality.WINDOW_MODAL);
            dialogStageApproved.initOwner(HotelFX.primaryStage);
            
            Undecorator undecorator = new Undecorator(dialogStageApproved,page);
            undecorator.getStylesheets().add("/skin/undecorator.css");
            undecorator.getMenuButton().setVisible(false);
            undecorator.getMaximizeButton().setVisible(false);
            undecorator.getMinimizeButton().setVisible(false);
            undecorator.getFullScreenButton().setVisible(false);
            undecorator.getCloseButton().setVisible(false);
            
            Scene scene = new Scene(undecorator);
            scene.setFill(Color.TRANSPARENT);
            
            dialogStageApproved.initStyle(StageStyle.TRANSPARENT);
            dialogStageApproved.setScene(scene);
            dialogStageApproved.setResizable(false);
            
            dialogStageApproved.showAndWait();
        } catch (IOException ex) {
            Logger.getLogger(DebtController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
     //payment Form
    public Stage dialogStagePayment;
  /*  private void showEmployeeDebtPaymentDialog(){
        try {
            FXMLLoader loader = new FXMLLoader();//"view/feature_debt/debt/DebtApproved.fxml");
            loader.setLocation(HotelFX.class.getResource("view/feature_debt/debt/DebtPayment.fxml"));
            
            DebtPaymentController debtPaymentController = new DebtPaymentController(this);
            loader.setController(debtPaymentController);
            
            Region page = loader.load();
            
            dialogStagePayment = new Stage();
            dialogStagePayment.initModality(Modality.WINDOW_MODAL);
            dialogStagePayment.initOwner(HotelFX.primaryStage);
            
            Undecorator undecorator = new Undecorator(dialogStagePayment,page);
            undecorator.getStylesheets().add("/skin/undecorator.css");
            undecorator.getMenuButton().setVisible(false);
            undecorator.getMaximizeButton().setVisible(false);
            undecorator.getMinimizeButton().setVisible(false);
            undecorator.getFullScreenButton().setVisible(false);
            undecorator.getCloseButton().setVisible(false);
            
            Scene scene = new Scene(undecorator);
            scene.setFill(Color.TRANSPARENT);
            
            dialogStagePayment.initStyle(StageStyle.TRANSPARENT);
            dialogStagePayment.setScene(scene);
            dialogStagePayment.setResizable(false);
            
            dialogStagePayment.showAndWait();
        } catch (IOException ex) {
            Logger.getLogger(DebtController.class.getName()).log(Level.SEVERE, null, ex);
        }
    } */
    
    //pinjaman karyawan ditolak
    public Stage dialogStageRejectDebt;
    private void showEmployeeRejectDebtDialog(){
      try{
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(HotelFX.class.getResource("view/feature_debt/debt/DebtRejectView.fxml"));
        
        DebtRejectController debtRejectController = new DebtRejectController(this);
        loader.setController(debtRejectController);
        
        Region page = loader.load();
        
        dialogStageRejectDebt = new Stage();
        dialogStageRejectDebt.initModality(Modality.WINDOW_MODAL);
        dialogStageRejectDebt.initOwner(HotelFX.primaryStage);
        
         Undecorator undecorator = new Undecorator(dialogStageRejectDebt,page);
         undecorator.getStylesheets().add("/skin/undecorator.css");
         undecorator.getMenuButton().setVisible(false);
         undecorator.getMaximizeButton().setVisible(false);
         undecorator.getMinimizeButton().setVisible(false);
         undecorator.getFullScreenButton().setVisible(false);
         undecorator.getCloseButton().setVisible(false);
         
         Scene scene = new Scene(undecorator);
         scene.setFill(Color.TRANSPARENT);
         
         dialogStageRejectDebt.initStyle(StageStyle.TRANSPARENT);
         dialogStageRejectDebt.setScene(scene);
         dialogStageRejectDebt.setResizable(false);
         
         dialogStageRejectDebt.showAndWait();
        }
        catch(IOException ex){
          Logger.getLogger(DebtController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    //password debt Form
    public Stage dialogStagePasswordDebt;
    private void showEmployeePasswordDebtDialog(){
        try {
            FXMLLoader loader = new FXMLLoader();//"view/feature_debt/debt/DebtApproved.fxml");
            loader.setLocation(HotelFX.class.getResource("view/feature_debt/debt/DebtPasswordDeleteView.fxml"));
            
            DebtPasswordDeleteController debtPasswordDeleteController = new DebtPasswordDeleteController(this);
            loader.setController(debtPasswordDeleteController);
            
            Region page = loader.load();
            
            dialogStagePasswordDebt = new Stage();
            dialogStagePasswordDebt.initModality(Modality.WINDOW_MODAL);
            dialogStagePasswordDebt.initOwner(HotelFX.primaryStage);
            
            Undecorator undecorator = new Undecorator(dialogStagePasswordDebt,page);
            undecorator.getStylesheets().add("/skin/undecorator.css");
            undecorator.getMenuButton().setVisible(false);
            undecorator.getMaximizeButton().setVisible(false);
            undecorator.getMinimizeButton().setVisible(false);
            undecorator.getFullScreenButton().setVisible(false);
            undecorator.getCloseButton().setVisible(false);
            
            Scene scene = new Scene(undecorator);
            scene.setFill(Color.TRANSPARENT);
            
            dialogStagePasswordDebt.initStyle(StageStyle.TRANSPARENT);
            dialogStagePasswordDebt.setScene(scene);
            dialogStagePasswordDebt.setResizable(false);
            
            dialogStagePasswordDebt.showAndWait();
        } catch (IOException ex) {
            Logger.getLogger(DebtController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        setDataDebtSplitPane();
        
        initTableDataDebt();
        
        initFormDataDebt();
        
        spDataDebt.widthProperty().addListener((obs,oldVal,newVal)->{
           dataDebtFormShowStatus.set(0);
        });
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    public DebtController(FeatureDebtController parentController){
       this.parentController = parentController;
    }
    
    private final FeatureDebtController parentController;
    
    public FDebtManager getService(){
       return parentController.getFDebtManager();
    }
}
