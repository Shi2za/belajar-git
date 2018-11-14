/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hotelfx.view.feature_schedule.employee_schedule;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXCComboBoxPopup;
import com.jfoenix.controls.JFXCComboBoxTablePopup;
import hotelfx.HotelFX;
import hotelfx.helper.ClassFilteringTable;
import hotelfx.helper.ClassFormatter;
import hotelfx.helper.ClassMessage;
import hotelfx.helper.ClassPrinter;
import hotelfx.helper.ClassViewSetting;
import hotelfx.persistence.model.RefEmployeeType;
import hotelfx.persistence.model.SysCalendar;
import hotelfx.persistence.model.TblCalendarEmployeeSchedule;
import hotelfx.persistence.model.TblEmployee;
import hotelfx.persistence.model.TblEmployeeSchedule;
import hotelfx.persistence.model.TblPeople;
import insidefx.undecorator.Undecorator;
import java.io.IOException;
import java.net.URL;
import java.sql.Date;
import java.sql.Time;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import java.time.YearMonth;
import java.time.temporal.WeekFields;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.binding.Bindings;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.Tooltip;
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
import javafx.util.Callback;

/**
 *
 * @author Andreas
 */
public class EmployeeScheduleInputDialogController implements Initializable{
     @FXML
     private Label lblDate;
     @FXML
     private AnchorPane ancFiltering;
     
     ClassFilteringTable<TblCalendarEmployeeSchedule>cft;
     
     private void statusScheduleHandle(String status){
       switch(status){
         case "home":
         ancDisableLayerLayout.toFront();
         gpFormSchedule.setVisible(false);
         btnAddSchedule.setVisible(false);
         btnSaveSchedule.setVisible(false);
         btnPrintSchedule.setVisible(false);
         break;
         
         case "form":
         ancForm.toFront();
         gpFormSchedule.setVisible(true); 
         break;
       
         case "viewSchedule":
         ancDisableLayerLayout.toFront();
         gpFormSchedule.setVisible(false);
         btnAddSchedule.setVisible(true);
         btnSaveSchedule.setVisible(true);
         btnPrintSchedule.setVisible(true);
         break;
        }
    }
    
    //table handle
    @FXML
    AnchorPane tableEmployeeScheduleLayout;
    TableView tableEmployeeSchedule = new TableView();
    ObservableList<TblCalendarEmployeeSchedule>listEmployeeSchedule = FXCollections.observableArrayList();
    
    private void initTableEmployeeSchedule(){
     setTableListEmployeeSchedule();
     tableEmployeeScheduleLayout.getChildren().clear();
     AnchorPane.setTopAnchor(tableEmployeeSchedule,15.0);
     AnchorPane.setBottomAnchor(tableEmployeeSchedule,15.0);
     AnchorPane.setLeftAnchor(tableEmployeeSchedule,15.0);
     AnchorPane.setRightAnchor(tableEmployeeSchedule,15.0);
     tableEmployeeScheduleLayout.getChildren().add(tableEmployeeSchedule);
    }
    
    private void setTableListEmployeeSchedule(){
     //System.out.println("di dalam table:"+employeeScheduleController.selectedData.getSysCalendar().getIdcalendar());
      tableEmployeeSchedule = new TableView();
      tableEmployeeSchedule.setEditable(true);
     TableColumn<TblCalendarEmployeeSchedule,String>idEmployee = new TableColumn<>("ID");
     idEmployee.setMinWidth(100);
     idEmployee.setCellValueFactory((TableColumn.CellDataFeatures<TblCalendarEmployeeSchedule,String>param)
         ->Bindings.createStringBinding(()->param.getValue().getTblEmployeeByIdemployee().getCodeEmployee(),param.getValue().getTblEmployeeByIdemployee().codeEmployeeProperty()));
     TableColumn<TblCalendarEmployeeSchedule,String>nameEmployee = new TableColumn<>("Nama");
     nameEmployee.setMinWidth(160);
     nameEmployee.setCellValueFactory((TableColumn.CellDataFeatures<TblCalendarEmployeeSchedule,String>param)
         ->Bindings.createStringBinding(()->param.getValue().getTblEmployeeByIdemployee().getTblPeople().getFullName(),param.getValue().getTblEmployeeByIdemployee().getTblPeople().fullNameProperty()));
     TableColumn<TblCalendarEmployeeSchedule,String>typeEmployee = new TableColumn("Tipe");
     typeEmployee.setMinWidth(120);
     typeEmployee.setCellValueFactory((TableColumn.CellDataFeatures<TblCalendarEmployeeSchedule,String>param)
         ->Bindings.createStringBinding(()->param.getValue().getTblEmployeeByIdemployee().getRefEmployeeType().getTypeName(),param.getValue().getTblEmployeeByIdemployee().refEmployeeTypeProperty()));
     TableColumn<TblCalendarEmployeeSchedule,String>jobEmployee = new TableColumn("Jabatan");
     jobEmployee.setMinWidth(140);
     jobEmployee.setCellValueFactory((TableColumn.CellDataFeatures<TblCalendarEmployeeSchedule,String>param)
       ->Bindings.createStringBinding(()->param.getValue().getTblEmployeeByIdemployee().getTblJob().getJobName(),param.getValue().tblEmployeeByIdemployeeProperty()));
     TableColumn<TblCalendarEmployeeSchedule,String>employee = new TableColumn("Karyawan");
     employee.getColumns().addAll(typeEmployee,idEmployee,nameEmployee,jobEmployee);
    
     TableColumn<TblCalendarEmployeeSchedule,String>nameSchedule = new TableColumn<>("Jadwal");
     nameSchedule.setMinWidth(160);
     nameSchedule.setCellValueFactory((TableColumn.CellDataFeatures<TblCalendarEmployeeSchedule,String>param)
         ->Bindings.createStringBinding(()->param.getValue().getTblEmployeeSchedule().getScheduleName()+"\n"+param.getValue().getTblEmployeeSchedule().getBeginTime()+"-"+param.getValue().getTblEmployeeSchedule().getEndTime(),param.getValue().getTblEmployeeSchedule().scheduleNameProperty()));
    /* TableColumn<TblCalendarEmployeeSchedule,String>startWork = new TableColumn("Masuk");
     startWork.setMinWidth(80);
     startWork.setCellValueFactory((TableColumn.CellDataFeatures<TblCalendarEmployeeSchedule,String>param)
      ->Bindings.createStringBinding(()->param.getValue().getTblEmployeeSchedule().getBeginTime().toString(),param.getValue().getTblEmployeeSchedule().beginTimeProperty()));
        TableColumn<TblCalendarEmployeeSchedule,String>endWork = new TableColumn("Keluar");
        endWork.setMinWidth(80);
        endWork.setCellValueFactory((TableColumn.CellDataFeatures<TblCalendarEmployeeSchedule,String>param)
      ->Bindings.createStringBinding(()->param.getValue().getTblEmployeeSchedule().getEndTime().toString(),param.getValue().getTblEmployeeSchedule().endTimeProperty()));
        TableColumn<TblCalendarEmployeeSchedule,String>timeWork = new TableColumn("Jam Kerja");
        timeWork.getColumns().addAll(startWork,endWork);*/
     TableColumn<TblCalendarEmployeeSchedule,TblCalendarEmployeeSchedule>update = new TableColumn<>("Ubah");
     update.setMinWidth(50);
     update.setMaxWidth(50);
     update.setEditable(true);
     update.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue()));
     update.setCellFactory(new Callback<TableColumn<TblCalendarEmployeeSchedule,TblCalendarEmployeeSchedule>,TableCell<TblCalendarEmployeeSchedule,TblCalendarEmployeeSchedule>>(){
         @Override
         public TableCell<TblCalendarEmployeeSchedule,TblCalendarEmployeeSchedule> call(TableColumn<TblCalendarEmployeeSchedule,TblCalendarEmployeeSchedule> param) {
          return new ButtonCellUpdate();   
            //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
         }
         
     });
     TableColumn<TblCalendarEmployeeSchedule,TblCalendarEmployeeSchedule>delete = new TableColumn<>("Hapus");
     delete.setMinWidth(50);
     delete.setMaxWidth(50);
     delete.setEditable(true);
     delete.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue()));
     delete.setCellFactory(new Callback<TableColumn<TblCalendarEmployeeSchedule,TblCalendarEmployeeSchedule>,TableCell<TblCalendarEmployeeSchedule,TblCalendarEmployeeSchedule>>(){
         @Override
         public TableCell<TblCalendarEmployeeSchedule,TblCalendarEmployeeSchedule> call(TableColumn<TblCalendarEmployeeSchedule,TblCalendarEmployeeSchedule> param) {
          return new ButtonCellDelete();   
            //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
         }
         
     });
     tableEmployeeSchedule.getColumns().addAll(employee,nameSchedule,update,delete);
     listEmployeeSchedule = loadAllDataEmployeeSchedule(employeeScheduleController.selectedData.getSysCalendar().getIdcalendar());
     tableEmployeeSchedule.setItems(listEmployeeSchedule);
     
     cft = new ClassFilteringTable<>(TblCalendarEmployeeSchedule.class,tableEmployeeSchedule,tableEmployeeSchedule.getItems());
     ancFiltering.getChildren().clear();
     AnchorPane.setTopAnchor(cft,15.0);
     AnchorPane.setLeftAnchor(cft,0.0);
     AnchorPane.setRightAnchor(cft,0.0);
     AnchorPane.setBottomAnchor(cft,15.0);
     ancFiltering.getChildren().add(cft);
     
    }
    
    public class ButtonCellUpdate extends TableCell<TblCalendarEmployeeSchedule,TblCalendarEmployeeSchedule>{
      JFXButton cellButton;
    
      public ButtonCellUpdate(){
          this.cellButton = new JFXButton(); 
        }
    
        @Override
        public void updateItem(TblCalendarEmployeeSchedule item, boolean empty) {
         super.updateItem(item,empty);
           if(employeeScheduleController.selectedData.getSysCalendar().getCalendarDate().after(Date.valueOf(LocalDate.now()))){
               if(!empty){
                  setGraphic(cellButton);
                  cellButton.setAlignment(Pos.CENTER);
                  cellButton.getStyleClass().setAll("button-edit");
                  cellButton.setPrefSize(20,25);
                  cellButton.setOnAction((e)->{
                     employeeScheduleUpdateHandle(item); 
                    //System.out.println(">>"+item.getTblEmployeeByIdemployee().getCodeEmployee());//(Tbl)getTableView().getSelectionModel().getSelectedItem());
                    //employeeScheduleUpdateHandle();
                  });
                }
                else
                {
                 setGraphic(null);
                }
            }
           else{
             setGraphic(null);   
            }
       /*if(!empty){
        setGraphic(cellButton);
        cellButton.getStyleClass().setAll("button-edit");
        cellButton.setPrefSize(20,25);
        cellButton.setOnAction((e)->{
        listDataEmployeeScheduleUpdate.setAll(item);
        employeeScheduleUpdateHandle(item); 
            //System.out.println(">>"+item.getTblEmployeeByIdemployee().getCodeEmployee());//(Tbl)getTableView().getSelectionModel().getSelectedItem());
        //employeeScheduleUpdateHandle();
        });
       
       }
       else
       {
         setGraphic(null);
       }*/
        }
    }    
 
    public class ButtonCellDelete extends TableCell<TblCalendarEmployeeSchedule,TblCalendarEmployeeSchedule>{
     JFXButton cellButton;
    
       public ButtonCellDelete(){
         this.cellButton = new JFXButton();
        }
    
       @Override
       public void updateItem(TblCalendarEmployeeSchedule item, boolean empty) {
          super.updateItem(item,empty);
           if(employeeScheduleController.selectedData.getSysCalendar().getCalendarDate().after(Date.valueOf(LocalDate.now()))){
               if(!empty){
                  setGraphic(cellButton);
                  cellButton.setAlignment(Pos.CENTER);
                  cellButton.getStyleClass().setAll("button-delete");
                  cellButton.setPrefSize(20,25);
                  cellButton.setOnAction((e)->{
                     employeeScheduleDeleteHandle(item);
        //employeeScheduleUpdateHandle(item); 
            //System.out.println(">>"+item.getTblEmployeeByIdemployee().getCodeEmployee());//(Tbl)getTableView().getSelectionModel().getSelectedItem());
        //employeeScheduleUpdateHandle();
                    });
                }
                else
                {
                  setGraphic(null);
                }
            }
            else
            {
              setGraphic(null);
            }
        }
    }    

    private ObservableList<TblCalendarEmployeeSchedule>loadAllDataEmployeeSchedule(long id){
     //List<TblCalendarEmployeeSchedule>list = employeeScheduleController.getService().getAllDataEmployeeSchedule(id);
     return FXCollections.observableArrayList(employeeScheduleController.getService().getAllDataEmployeeSchedule(id));
    }
    
    //button handle
    @FXML
    JFXButton btnCancel;
    @FXML
    JFXButton btnAddSchedule;
    @FXML
    JFXButton btnViewSchedule;
    @FXML
    JFXButton btnSaveSchedule;
    @FXML
    JFXButton btnPrintSchedule;        
            
    StringProperty statusForm = new SimpleStringProperty();
    
    private void initControllButton(){
     //bottom button
      //initDataPopup();
     btnAddSchedule.setTooltip(new Tooltip("Tambah Jadwal"));
     btnAddSchedule.setOnAction((e)->{
       employeeScheduleCreateHandle();
     });
      
     /* btnUpdateSchedule.setOnAction((e)->{
        employeeScheduleUpdateHandle();
      });
      
      btnDeleteSchedule.setOnAction((e)->{
        employeeScheduleDeleteHandle();  
      });*/
     btnSaveSchedule.setTooltip(new Tooltip("Menyimpan jadwal ke database"));
      btnSaveSchedule.setOnAction((e)->{
          employeeScheduleSaveToDataBaseHandle();
      });
      
      //top 
      btnCancel.setTooltip(new Tooltip("close popup dialog"));
      btnCancel.setOnAction((e)->
      {
         employeeScheduleController.dialogStage.close();
      });
      
      btnViewSchedule.setTooltip(new Tooltip("Lihat list Jadwal dari Database"));
      btnViewSchedule.setOnAction((e)->{
       if(employeeScheduleController.selectedData.getSysCalendar().getCalendarDate().after(Date.valueOf(LocalDate.now()))){
         statusScheduleHandle("viewSchedule");
       }
       else{
        statusScheduleHandle("home");
       }
        
        initTableEmployeeSchedule(); 
        
        refreshFiltering();
      });
      
      btnPrintSchedule.setTooltip(new Tooltip("Print Schedule"));
      btnPrintSchedule.setOnAction((e)->{
        employeeSchedulePrintHandle();
      });
      
    }
    
    //input Schedule handle
    @FXML
    GridPane gpFormSchedule;
    @FXML
    JFXButton btnFormSave;
    @FXML
    JFXButton btnFormCancel;
    @FXML
    AnchorPane ancForm;
    @FXML
    AnchorPane cbpEmployeeTypeLayout;
    @FXML
    AnchorPane cbpEmployeeLayout;
    @FXML
    AnchorPane cbpScheduleLayout;
    @FXML
    AnchorPane ancDisableLayerLayout;
    
    private JFXCComboBoxTablePopup<RefEmployeeType>cbpEmployeeType;
    private JFXCComboBoxTablePopup<TblEmployee>cbpEmployee;
    private JFXCComboBoxTablePopup<TblEmployeeSchedule>cbpSchedule;
    ObservableList<TblCalendarEmployeeSchedule>listDataEmployeeScheduleUpdate = FXCollections.observableArrayList();
    
    private TblCalendarEmployeeSchedule selectedDataEmployeeSchedule = null;
    //private TblCalendarEmployeeSchedule getSelectedTable = null;
    
    
    private void initForm(){
        btnFormSave.setOnAction((e)->{
          employeeScheduleSaveFormHandle();
        });
        
        btnFormCancel.setOnAction((e)->{
          employeeScheduleCancelFormHandle();  
        });
        
        initDataPopupLayout();
        initDataPopupEmployeeLayout();
        initImportantFieldColor();
    }
    
    private void initImportantFieldColor() {
        ClassViewSetting.setImportantField(
                cbpEmployeeType, 
                cbpEmployee,
                cbpSchedule);
    }
    
    private void initDataPopupLayout(){
        initDataPopup();
        
       cbpEmployeeTypeLayout.getChildren().clear();
       AnchorPane.setTopAnchor(cbpEmployeeType,0.0);
       AnchorPane.setBottomAnchor(cbpEmployeeType,0.0);
       AnchorPane.setLeftAnchor(cbpEmployeeType,0.0);
       AnchorPane.setRightAnchor(cbpEmployeeType,0.0);
       cbpEmployeeTypeLayout.getChildren().add(cbpEmployeeType);
        
       cbpScheduleLayout.getChildren().clear();
       AnchorPane.setTopAnchor(cbpSchedule,0.0);
       AnchorPane.setBottomAnchor(cbpSchedule,0.0);
       AnchorPane.setLeftAnchor(cbpSchedule,0.0);
       AnchorPane.setRightAnchor(cbpSchedule,0.0);
       cbpScheduleLayout.getChildren().add(cbpSchedule);
    }
    
    private void initDataPopupEmployeeLayout(){
       initDataPopupEmployee();
       cbpEmployeeLayout.getChildren().clear();
       AnchorPane.setTopAnchor(cbpEmployee,0.0);
       AnchorPane.setBottomAnchor(cbpEmployee,0.0);
       AnchorPane.setLeftAnchor(cbpEmployee,0.0);
       AnchorPane.setRightAnchor(cbpEmployee,0.0);
       cbpEmployeeLayout.getChildren().add(cbpEmployee);
    }
    
    private void initDataPopup()
    {
       TableView<RefEmployeeType>tblListEmployeeType = new TableView<>();
      TableColumn<RefEmployeeType,String>nameEmployeeType = new TableColumn<>("Tipe Karyawan");
      nameEmployeeType.setCellValueFactory(cellData->cellData.getValue().typeNameProperty());
      tblListEmployeeType.getColumns().addAll(nameEmployeeType);
      ObservableList<RefEmployeeType>listEmployeeType = FXCollections.observableArrayList(loadAllDataEmployeeType());
      
      TableView<TblEmployeeSchedule>tblListSchedule = new TableView<>();
      TableColumn<TblEmployeeSchedule,String>nameSchedule = new TableColumn<>("Jadwal");
      nameSchedule.setCellValueFactory(cellData->cellData.getValue().scheduleNameProperty());
      nameSchedule.setMinWidth(160);
      TableColumn<TblEmployeeSchedule,Time>startWork = new TableColumn("Masuk");
      startWork.setMinWidth(80);
       startWork.setCellValueFactory(cellData->cellData.getValue().beginTimeProperty());
       TableColumn<TblEmployeeSchedule,Time>endWork = new TableColumn("Keluar");
       endWork.setMinWidth(80);
       endWork.setCellValueFactory(cellData->cellData.getValue().endTimeProperty());
       TableColumn<TblEmployeeSchedule,String>scheduleTime = new TableColumn("Jam Kerja");
       scheduleTime.getColumns().addAll(startWork,endWork);
      tblListSchedule.getColumns().addAll(nameSchedule,scheduleTime);
      ObservableList<TblEmployeeSchedule>listSchedule = FXCollections.observableArrayList(employeeScheduleController.getService().getAllDataSchedule());
      
      cbpEmployeeType = new JFXCComboBoxTablePopup(RefEmployeeType.class,tblListEmployeeType,listEmployeeType,"","Tipe Karyawan *",true,400,300);
      
      cbpSchedule = new JFXCComboBoxTablePopup(TblCalendarEmployeeSchedule.class,tblListSchedule,listSchedule,"","Jadwal *",true,400,300);
    
    }
    
    private void initDataPopupEmployee(){
      TableView<TblEmployee>tblListEmployee = new TableView<>();
      TableColumn<TblEmployee,String>codeEmployee = new TableColumn<>("ID");
      codeEmployee.setMinWidth(100);
      codeEmployee.setCellValueFactory(cellData->cellData.getValue().codeEmployeeProperty());
      TableColumn<TblEmployee,String>nameEmployee = new TableColumn<>("Nama");
      nameEmployee.setMinWidth(160);
      nameEmployee.setCellValueFactory((TableColumn.CellDataFeatures<TblEmployee,String>param)->
      Bindings.createStringBinding(()->param.getValue().getTblPeople().getFullName(),param.getValue().getTblPeople().fullNameProperty()));
      TableColumn<TblEmployee,String>typeEmployee = new TableColumn<>("Tipe ");
      typeEmployee.setMinWidth(120);
      typeEmployee.setCellValueFactory((TableColumn.CellDataFeatures<TblEmployee,String>param)
       ->Bindings.createStringBinding(()->param.getValue().getRefEmployeeType().getTypeName(),param.getValue().refEmployeeTypeProperty()));
      TableColumn<TblEmployee,String>jobEmployee = new TableColumn<>("Jabatan");
      jobEmployee.setMinWidth(120);
      jobEmployee.setCellValueFactory((TableColumn.CellDataFeatures<TblEmployee,String>param)
        ->Bindings.createStringBinding(()->param.getValue().getTblJob().getJobName(),param.getValue().tblJobProperty()));
      tblListEmployee.getColumns().addAll(typeEmployee,codeEmployee,nameEmployee,jobEmployee);
      ObservableList<TblEmployee>listEmployee = FXCollections.observableArrayList();
      cbpEmployeeType.valueProperty().addListener((obs,oldVal,newVal)->{
        if(newVal!=null){
          listEmployee.addAll(loadAllDataEmployee(newVal.getIdtype()));
        }
      });
      
      cbpEmployee = new JFXCComboBoxTablePopup(TblEmployee.class,tblListEmployee,listEmployee,"","Nama Karyawan *",true,500,300);
    }
    
    private List<TblEmployeeSchedule>loadAllDataSchedule(){
      List<TblEmployeeSchedule>listSchedule = employeeScheduleController.getService().getAllDataSchedule();
      return listSchedule;
    }
    
    private List<RefEmployeeType>loadAllDataEmployeeType(){
      List<RefEmployeeType>listEmployeeType = new ArrayList();
      RefEmployeeType empType = new RefEmployeeType();
      empType.setIdtype(3);
      empType.setTypeName("Semua Tipe Karyawan");
      listEmployeeType.add(empType);
      listEmployeeType.addAll(employeeScheduleController.getService().getAllDataEmployeeType());
      return listEmployeeType;
    }
    
    private List<TblEmployee>loadAllDataEmployee(long id){
      List<TblEmployee>listEmployee = employeeScheduleController.getService().getAllDataEmployee(id);
      boolean check = false;
      for(TblCalendarEmployeeSchedule getEmployeeSchedule : listEmployeeSchedule){
          System.out.println("list table:"+getEmployeeSchedule.getTblEmployeeByIdemployee().getCodeEmployee());
      }
      /*for(TblEmployee getEmployee : listEmployee){
       for(TblCalendarEmployeeSchedule getEmployeeSchedule : listEmployeeSchedule){
         if(getEmployee.getIdemployee()==getEmployeeSchedule.getTblEmployeeByIdemployee().getIdemployee() || !listEmployeeSchedule.isEmpty()){
           listEmployee.remove(getEmployee);
         }  
       }   
      }*/
     
     //System.out.println("size:"+tableEmployeeSchedule.getSelectionModel().getSelectedItems().size());
     /*for(int i = listEmployee.size()-1;i>=0 ;i--){
      for(int j = listEmployeeSchedule.size()-1;j>=0;j--){
          System.out.println("----" + j);
         if(listEmployee.get(i).getIdemployee()==listEmployeeSchedule.get(j).getTblEmployeeByIdemployee().getIdemployee()){
           if(selectedDataEmployeeSchedule.getTblEmployeeByIdemployee()==null){
             listEmployee.remove(listEmployee.get(i));
             break;
           }
           else{
             if(listEmployee.get(i).getIdemployee()!=selectedDataEmployeeSchedule.getTblEmployeeByIdemployee().getIdemployee()){
               listEmployee.remove(listEmployee.get(i));
               break;
             }
           }
         }
       }
      
        //System.out.println("list employee:"+listEmployeeSchedule.get(j).getTblEmployeeByIdemployee().getIdemployee());
      }
       /*if(tableEmployeeSchedule.getSelectionModel().getSelectedItems().size()==0){
         if(listEmployee.get(i).getIdemployee()==listEmployeeSchedule.get(j).getTblEmployeeByIdemployee().getIdemployee()){
           listEmployee.remove(listEmployee.get(i));
         }
         
       }
       else{
             //System.out.println("selected table:"+((TblCalendarEmployeeSchedule)tableEmployeeSchedule.getSelectionModel().getSelectedItem()).getTblEmployeeByIdemployee().getIdemployee());
            if(listEmployee.get(i).getIdemployee()==listEmployeeSchedule.get(j).getTblEmployeeByIdemployee().getIdemployee()&&
             listEmployee.get(i).getIdemployee()!=((TblCalendarEmployeeSchedule)tableEmployeeSchedule.getSelectionModel().getSelectedItem()).getTblEmployeeByIdemployee().getIdemployee()){
             listEmployee.remove(listEmployee.get(i));
           }
         }
        //System.out.println("list employee:"+listEmployeeSchedule.get(j).getTblEmployeeByIdemployee().getIdemployee());
      }*/ 
     
      
      return listEmployee;
    }
    
    private void refreshDataPopUp(){
     ObservableList<RefEmployeeType>listEmployeeType = FXCollections.observableArrayList(loadAllDataEmployeeType());
     ObservableList<TblEmployee>listEmployee = FXCollections.observableArrayList();
     cbpEmployeeType.setItems(listEmployeeType);
     cbpEmployeeType.valueProperty().addListener((obs,oldVal,newVal)->{
       if(newVal!=null){
         listEmployee.setAll(loadAllDataEmployee(newVal.getIdtype())); 
       }
     });
     cbpEmployee.setItems(listEmployee);
     ObservableList<TblEmployeeSchedule>listSchedule = FXCollections.observableArrayList(employeeScheduleController.getService().getAllDataSchedule());
     cbpSchedule.setItems(listSchedule);
    }
     
    private void refreshFiltering(){
      tableEmployeeSchedule.setItems(listEmployeeSchedule);
      cft.refreshFilterItems(tableEmployeeSchedule.getItems());
    }
    
     private void refreshDataPopUpUpdate(){
      //ObservableList<RefEmployeeType>employeeTypeItems = FXCollections.observableArrayList(loadAllEmployeeType());
      //cbpTypeEmployee.setItems(employeeTypeItems);
      RefEmployeeType all = new RefEmployeeType();
      all.setTypeName("Semua Tipe Karyawan");
      all.setIdtype(3);
      ObservableList<TblEmployee>employeeItems = FXCollections.observableArrayList(loadAllDataEmployee(all.getIdtype()));
      cbpEmployee.setItems(employeeItems);
    }
     
    private void setSelectedToInputForm(){
      refreshDataPopUp();
      cbpEmployeeType.setDisable(false);
      cbpEmployee.setDisable(false);
      cbpEmployeeType.setValue(null);
      cbpEmployee.setValue(null);
      
       cbpEmployeeType.valueProperty().addListener((obs,oldVal,newVal)->{
           if(newVal!=null && dataInputStatus==0){
             cbpEmployee.setValue(null);
           }
        });
      //cbpEmployee.valueProperty().bindBidirectional(selectedDataEmployeeSchedule.tblEmployeeByIdemployeeProperty());
      cbpSchedule.valueProperty().bindBidirectional(selectedDataEmployeeSchedule.tblEmployeeScheduleProperty());
      selectedDataEmployeeSchedule.setSysCalendar(employeeScheduleController.selectedData.getSysCalendar());
      
      cbpEmployeeType.hide();
      cbpEmployee.hide();
      cbpSchedule.hide();
    }
    
    private void selectedDataToUpdateForm(){
      refreshDataPopUpUpdate();
     
       cbpEmployeeType.setValue(selectedDataEmployeeSchedule.getTblEmployeeByIdemployee().getRefEmployeeType());
       if(selectedDataEmployeeSchedule.getTblEmployeeByIdemployee()!=null){
          cbpEmployeeType.setDisable(true);
          cbpEmployee.setDisable(true);
          cbpEmployee.setValue(selectedDataEmployeeSchedule.getTblEmployeeByIdemployee());
          cbpEmployee.valueProperty().addListener((obs,oldVal,newVal)->{
               if(newVal!=null && dataInputStatus==1){
                 cbpEmployeeType.setValue(newVal.getRefEmployeeType());
               }
           });
        }
      
      //cbpEmployee.valueProperty().bindBidirectional(selectedDataEmployeeSchedule.tblEmployeeByIdemployeeProperty());
      cbpSchedule.valueProperty().bindBidirectional(selectedDataEmployeeSchedule.tblEmployeeScheduleProperty());
      selectedDataEmployeeSchedule.setSysCalendar(employeeScheduleController.selectedData.getSysCalendar());
      
    }
    
     private void setSelectedToOutputForm(){
      cbpEmployee.valueProperty().unbindBidirectional(selectedDataEmployeeSchedule.tblEmployeeByIdemployeeProperty());
      cbpSchedule.valueProperty().unbindBidirectional(selectedDataEmployeeSchedule.tblEmployeeScheduleProperty());
      
      cbpEmployee.hide();
      cbpSchedule.hide();
    }
    
    private int dataInputStatus = 0;
    private void employeeScheduleCreateHandle(){
      selectedDataEmployeeSchedule = new TblCalendarEmployeeSchedule();
      dataInputStatus = 0;
      /*selectedDataEmployeeSchedule.setSysCalendar(new SysCalendar());
      selectedDataEmployeeSchedule.setTblEmployeeByIdemployee(new TblEmployee());
      //selectedDataEmployeeSchedule.getTblEmployeeByIdemployee().setTblPeople(new TblPeople());
      selectedDataEmployeeSchedule.setTblEmployeeSchedule(new TblEmployeeSchedule());*/
      
      setSelectedToInputForm();
      statusScheduleHandle("form");
      
     
      //listEmployeeSchedule.addAll(selectedDataEmployeeSchedule);
      
    }
    
    private int row = 0;
    private void employeeScheduleUpdateHandle(TblCalendarEmployeeSchedule selectedTable){
        if(selectedTable!=null){
          dataInputStatus = 1;
          //getSelectedTable = selectedTable;
         // System.out.println("get selected table:"+getSelectedTable.getTblEmployeeByIdemployee().getCodeEmployee());
          //System.out.println(">>"+selectedTable.getTblEmployeeByIdemployee().getCodeEmployee());
          selectedDataEmployeeSchedule = new TblCalendarEmployeeSchedule();
          selectedDataEmployeeSchedule.setTblEmployeeByIdemployee(selectedTable.getTblEmployeeByIdemployee());
          selectedDataEmployeeSchedule.setTblEmployeeSchedule(selectedTable.getTblEmployeeSchedule());
          selectedDataEmployeeSchedule.setSysCalendar(selectedTable.getSysCalendar());          
          
          for(int i = 0; i< tableEmployeeSchedule.getItems().size();i++){
              if(((TblCalendarEmployeeSchedule)tableEmployeeSchedule.getItems().get(i)).getTblEmployeeByIdemployee().getIdemployee() == selectedTable.getTblEmployeeByIdemployee().getIdemployee()){
                  row = i;  
            }  
          }
          selectedDataToUpdateForm();
          statusScheduleHandle("form");
          
      }
      
      
      /*if(tableEmployeeSchedule.getSelectionModel().getSelectedItems().size()==1){
          
          selectedDataEmployeeSchedule = new TblCalendarEmployeeSchedule();
          selectedDataEmployeeSchedule.setTblEmployeeByIdemployee(((TblCalendarEmployeeSchedule)tableEmployeeSchedule.getSelectionModel().getSelectedItem()).getTblEmployeeByIdemployee());
          selectedDataEmployeeSchedule.setTblEmployeeSchedule(((TblCalendarEmployeeSchedule)tableEmployeeSchedule.getSelectionModel().getSelectedItem()).getTblEmployeeSchedule());
          selectedDataEmployeeSchedule.setSysCalendar(((TblCalendarEmployeeSchedule)tableEmployeeSchedule.getSelectionModel().getSelectedItem()).getSysCalendar());          
          setSelectedToInputForm();
          statusScheduleHandle("form");
          dataInputStatus = 1;
          
          getSelectedTable = new TblCalendarEmployeeSchedule();
          getSelectedTable.setTblEmployeeByIdemployee(((TblCalendarEmployeeSchedule)tableEmployeeSchedule.getSelectionModel().getSelectedItem()).getTblEmployeeByIdemployee());
          getSelectedTable.setTblEmployeeSchedule(((TblCalendarEmployeeSchedule)tableEmployeeSchedule.getSelectionModel().getSelectedItem()).getTblEmployeeSchedule());
          getSelectedTable.setSysCalendar(((TblCalendarEmployeeSchedule)tableEmployeeSchedule.getSelectionModel().getSelectedItem()).getSysCalendar());          
      }*/
    }
    
    private void employeeScheduleDeleteHandle(TblCalendarEmployeeSchedule selectedTable){
      if(selectedTable!=null){
          listEmployeeSchedule.remove(selectedTable);
           statusScheduleHandle("viewSchedule");
      }
    }
    
    private void employeeSchedulePrintHandle(){
       if(loadAllDataEmployeeSchedule(employeeScheduleController.selectedData.getSysCalendar().getIdcalendar()).size()>0){
         showEmployeeSchedulePrintDialog();
         //ClassPrinter.printSchedule(null, null, null);
       }
       else{
         HotelFX.showAlert(Alert.AlertType.WARNING, "DATA MASUKKAN TIDAK SESUAI", "Tidak ada data di database !!",null,employeeScheduleController.dialogStage);
       }
    }
    
   
    private void employeeScheduleSaveFormHandle(){
    //ObservableList<TblCalendarEmployeeSchedule>listEmployeeSchedule = FXCollections.observableArrayList()
        selectedDataEmployeeSchedule.setTblEmployeeByIdemployee(cbpEmployee.getValue());
        if(checkDataInputDataEmployeeSchedule()){   
            switch(dataInputStatus){
              case 0:
              
              ClassMessage.showSucceedAddingDataMessage(null,employeeScheduleController.dialogStage);
              listEmployeeSchedule.add(selectedDataEmployeeSchedule);
              setSelectedToOutputForm();
              
              refreshFiltering();
              statusScheduleHandle("viewSchedule");
              // getSelectedTable = null;
              break;
              case 1: 
               ClassMessage.showSucceedUpdatingDataMessage(null,employeeScheduleController.dialogStage);
              listEmployeeSchedule.set(row,selectedDataEmployeeSchedule);
                //listEmployeeSchedule.add(selectedDataEmployeeSchedule);
              setSelectedToOutputForm();
             
              refreshFiltering();
              statusScheduleHandle("viewSchedule");
              //getSelectedTable = null;
             //System.out.println("hsl>>"+((TblCalendarEmployeeSchedule)tableEmployeeSchedule.getSelectionModel().getSelectedItem()).getTblEmployeeByIdemployee().getCodeEmployee());
              break;
            }
        }
        else
        {
         ClassMessage.showWarningInputDataMessage(errInput,employeeScheduleController.dialogStage);
        //HotelFX.showAlert(Alert.AlertType.WARNING, "INVALID DATA INPUT", "Please check data input..!",errInput,employeeScheduleController.dialogStage);
        }
   }
   
   private void employeeScheduleCancelFormHandle(){
     //tableEmployeeSchedule.setItems(listEmployeeSchedule);
     refreshFiltering();
     statusScheduleHandle("viewSchedule");
   }
    
    private String errInput;
    private boolean checkDataInputDataEmployeeSchedule() {
        boolean dataInput = true;
        errInput = "";
        System.out.println("size list Schedule :"+listEmployeeSchedule.size());
        if(cbpEmployee.getValue()==null){
           dataInput = false;
           errInput+= "ID Karyawan : "+ClassMessage.defaultErrorNullValueMessage+"\n";
        }
        
        if (cbpSchedule.getValue()==null){
            dataInput = false;
            errInput+= " Jadwal: "+ClassMessage.defaultErrorNullValueMessage+"\n";
        }
        
        if(cbpEmployee.getValue()!=null && cbpSchedule.getValue()!=null){
          for(TblCalendarEmployeeSchedule getEmployeeSchedule:listEmployeeSchedule){
                if(getEmployeeSchedule.getTblEmployeeByIdemployee().getIdemployee()==selectedDataEmployeeSchedule.getTblEmployeeByIdemployee().getIdemployee() 
                    && getEmployeeSchedule.getTblEmployeeSchedule().getIdschedule()==selectedDataEmployeeSchedule.getTblEmployeeSchedule().getIdschedule()){
                     dataInput = false;
                     errInput+= "Data yang diinput sama!! \n";
                }
                
              //System.out.println("list save:"+getEmployeeSchedule.getTblEmployeeByIdemployee().getCodeEmployee());
           }
        }
        
        return dataInput;
    }
    
    //save to database
     private void employeeScheduleSaveToDataBaseHandle(){
        if(!listEmployeeSchedule.isEmpty()){
            Alert alert = ClassMessage.showConfirmationSavingDataMessage(null,employeeScheduleController.dialogStage);
            if(alert.getResult()==ButtonType.OK){
               if(employeeScheduleController.getService().insertDataEmployeeSchedule(listEmployeeSchedule,employeeScheduleController.selectedData.getSysCalendar().getIdcalendar())){
                  ClassMessage.showSucceedInsertingDataMessage(null,employeeScheduleController.dialogStage);
                  //HotelFX.showAlert(Alert.AlertType.INFORMATION, "SUCCESSED","Insert data successed..!", null,employeeScheduleController.dialogStage);
                  statusScheduleHandle("viewSchedule");
                 }
             }else{
                statusScheduleHandle("viewSchedule");
             }
        }
        else{
         HotelFX.showAlert(Alert.AlertType.WARNING, "DATA MASUKKAN TIDAK SESUAI", "Tabel jadwal kosong!!",null,employeeScheduleController.dialogStage);
        }
   }
   
   public Stage dialogStage;
   private void showEmployeeSchedulePrintDialog(){
       try {
         FXMLLoader loader = new FXMLLoader();
         loader.setLocation(HotelFX.class.getResource("view/feature_schedule/employee_schedule/EmployeeScheduleInputDialogPrint.fxml"));
         EmployeeScheduleInputDialogPrintController employeeScheduleInputDialogPrintController = new EmployeeScheduleInputDialogPrintController(this);
         loader.setController(employeeScheduleInputDialogPrintController);
             
         Region page = loader.load();
             
         dialogStage = new Stage();
         dialogStage.initModality(Modality.WINDOW_MODAL);
         dialogStage.initOwner(HotelFX.primaryStage);
         
         Undecorator undecorator = new Undecorator(dialogStage,page);
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
         
         dialogStage.showAndWait();
        } catch (IOException ex) {
             Logger.getLogger(EmployeeScheduleInputDialogController.class.getName()).log(Level.SEVERE, null, ex);
         }
   }
   
    @Override
    public void initialize(URL location, ResourceBundle resources) {
       
       lblDate.setText(ClassFormatter.convertDayToIndonesian(((java.sql.Date)employeeScheduleController.selectedData.getSysCalendar().getCalendarDate()).toLocalDate().getDayOfWeek().getValue())+","+employeeScheduleController.selectedData.getSysCalendar().getCalendarDate().getDate()+" "+ClassFormatter.convertMonthToIndonesian(((java.sql.Date)employeeScheduleController.selectedData.getSysCalendar().getCalendarDate()).toLocalDate().getMonthValue())+" "+((java.sql.Date)employeeScheduleController.selectedData.getSysCalendar().getCalendarDate()).toLocalDate().getYear());//String.format("%tA",employeeScheduleController.selectedData.getSysCalendar().getCalendarDate())+","+employeeScheduleController.selectedData.getSysCalendar().getCalendarDate());
        //System.out.println(">>"+employeeScheduleController.selectedData.getSysCalendar().getCalendarDate());
       initControllButton();
       initForm();
       statusScheduleHandle("home");
       // throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    public EmployeeScheduleInputDialogController(EmployeeScheduleController parentController) {
        this.employeeScheduleController = parentController;
    }
    public final EmployeeScheduleController employeeScheduleController;
}

