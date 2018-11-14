/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hotelfx.view.feature_employee.employee;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXCComboBoxTablePopup;
import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXTextArea;
import com.jfoenix.controls.JFXTextField;
import hotelfx.HotelFX;
import hotelfx.helper.ClassMessage;
import hotelfx.helper.ClassTableWithControl;
import hotelfx.persistence.model.TblCalendarEmployeeWarningLetter;
import hotelfx.persistence.model.TblEmployee;
import hotelfx.persistence.model.TblEmployeeWarningLetterType;
import hotelfx.view.DashboardController;
import insidefx.undecorator.Undecorator;
import java.io.IOException;
import java.net.URL;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;
import java.util.ResourceBundle;
import javafx.beans.binding.Bindings;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.SplitPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Region;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

/**
 *
 * @author Andreas
 */
public class EmployeeWarningLetterController implements Initializable {
    @FXML
    private SplitPane spDataEmployeeWarningLetter;
    private DoubleProperty dataEmployeeWarningLetterFormShowStatus;
    
    @FXML
    private AnchorPane borderPaneLayout;
    @FXML
    private AnchorPane contentLayout;
    @FXML
    private AnchorPane tableDataEmployeeWarningLetterDisableLayerLayout;
    @FXML
    private Label lblEmployeeWarningLetterStatus;
    
    private void setDataEmployeeWarningLetterSplitPane() {
        //System.out.println("masuk!!");
        dataEmployeeWarningLetterFormShowStatus = new SimpleDoubleProperty(1.0);
        DoubleProperty divPosition = new SimpleDoubleProperty();
        divPosition.bind(new SimpleDoubleProperty(1.0).subtract((ancFormEmployeeWarningLetter.prefWidthProperty().divide(spDataEmployeeWarningLetter.widthProperty()))
                .multiply(dataEmployeeWarningLetterFormShowStatus))
        );

        divPosition.addListener((obs, oldVal, newVal) -> {
            spDataEmployeeWarningLetter.setDividerPositions(newVal.doubleValue());
        });

        SplitPane.Divider div = spDataEmployeeWarningLetter.getDividers().get(0);

        div.positionProperty().addListener((obs, oldVal, newVal) -> {
            div.setPosition(divPosition.get());
        });

        dataEmployeeWarningLetterFormShowStatus.addListener((obs, oldVal, newVal) -> {
                if (newVal.doubleValue() == 0.0) {
                    borderPaneLayout.setDisable(false);
                    tableDataEmployeeWarningLetterDisableLayerLayout.setDisable(true);
                    borderPaneLayout.toFront();
                }

                if (newVal.doubleValue() == 1) {
                    borderPaneLayout.setDisable(true);
                    tableDataEmployeeWarningLetterDisableLayerLayout.setDisable(false);
                    tableDataEmployeeWarningLetterDisableLayerLayout.toFront();
                }
        });

        dataEmployeeWarningLetterFormShowStatus.set(0.0);
    }
    
    @FXML
    private AnchorPane tableDataEmployeeWarningLetterLayout;
    private ClassTableWithControl tableDataEmployeeWarningLetter;
    
    private void initTableEmployeeWarningLetter(){
       setTableDataEmployeeWarningLetter();
       setTableControlEmployeeWarningLetter();
       
       tableDataEmployeeWarningLetterLayout.getChildren().clear();
       AnchorPane.setTopAnchor(tableDataEmployeeWarningLetter,15.0);
       AnchorPane.setBottomAnchor(tableDataEmployeeWarningLetter,15.0);
       AnchorPane.setLeftAnchor(tableDataEmployeeWarningLetter,15.0);
       AnchorPane.setRightAnchor(tableDataEmployeeWarningLetter,15.0);
       tableDataEmployeeWarningLetterLayout.getChildren().add(tableDataEmployeeWarningLetter);
    }
    
    private void setTableDataEmployeeWarningLetter(){
      TableView<TblCalendarEmployeeWarningLetter>tableView = new TableView();
      TableColumn<TblCalendarEmployeeWarningLetter,String>nameWarningLetter = new TableColumn("Tipe Surat Peringatan");
      nameWarningLetter.setMinWidth(180);
      nameWarningLetter.setCellValueFactory((TableColumn.CellDataFeatures<TblCalendarEmployeeWarningLetter,String>param)
              ->Bindings.createStringBinding(()->param.getValue().getTblEmployeeWarningLetterType().getNameWarningLetter(),param.getValue().tblEmployeeWarningLetterTypeProperty()));
      TableColumn<TblCalendarEmployeeWarningLetter,String>startDate = new TableColumn("Awal");
      startDate.setMinWidth(120);
      startDate.setCellValueFactory((TableColumn.CellDataFeatures<TblCalendarEmployeeWarningLetter,String>param)
            ->Bindings.createStringBinding(()->new SimpleDateFormat("dd MMM yyyy").format(param.getValue().getStartDate()),param.getValue().endDateProperty()));
      TableColumn<TblCalendarEmployeeWarningLetter,String>endDate = new TableColumn("Akhir");
      endDate.setMinWidth(120);
      endDate.setCellValueFactory((TableColumn.CellDataFeatures<TblCalendarEmployeeWarningLetter,String>param)
            ->Bindings.createStringBinding(()->new SimpleDateFormat("dd MMM yyyy").format(param.getValue().getEndDate()),param.getValue().endDateProperty()));
      TableColumn<TblCalendarEmployeeWarningLetter,String>dateWarningLetter = new TableColumn("Masa Berlaku");
      dateWarningLetter.getColumns().addAll(startDate,endDate);
      TableColumn<TblCalendarEmployeeWarningLetter,String>noteEmployeeWarningLetter = new TableColumn("Keterangan");
      noteEmployeeWarningLetter.setMinWidth(120);
      noteEmployeeWarningLetter.setCellValueFactory(cellData -> cellData.getValue().noteWarningLetterProperty());
      tableView.getColumns().addAll(nameWarningLetter,dateWarningLetter,noteEmployeeWarningLetter);
     // ObservableList<TblCalendarEmployeeWarningLetter>employeeWarningLetterItems = FXCollections.observableArrayList(loadAllDataEmployeeWarningLetter);
      tableView.setItems(loadAllDataEmployeeWarningLetter());
      
      tableDataEmployeeWarningLetter = new ClassTableWithControl(tableView);
    }
    
    private ObservableList<TblCalendarEmployeeWarningLetter>loadAllDataEmployeeWarningLetter(){
        //System.out.println();
      return FXCollections.observableArrayList(employeeController.getService().getAllDataEmployeeWarningLetter(employeeController.selectedData.getIdemployee()));
    }
    
    private void setTableControlEmployeeWarningLetter(){
       ObservableList<Node>buttonControls = FXCollections.observableArrayList();
       JFXButton buttonControl;
       
       if(true){
           buttonControl = new JFXButton("Tambah");
           buttonControl.setOnMouseClicked((e)->{
             employeeWarningLetterCreateHandle();
           });
         buttonControls.add(buttonControl);
       }
       
       if(true){
           buttonControl = new JFXButton("Ubah");
           buttonControl.setOnMouseClicked((e)->{
             employeeWarningLetterUpdateHandle();
           });
           buttonControls.add(buttonControl);
       } 
       
       if(true){
           buttonControl = new JFXButton("Hapus");
           buttonControl.setOnMouseClicked((e)->{
             employeeWarningLetterDeleteHandle(); 
           });
           buttonControls.add(buttonControl);
       }
       
       tableDataEmployeeWarningLetter.addButtonControl(buttonControls);
    }
    
    @FXML
    private AnchorPane ancFormEmployeeWarningLetter;
    @FXML
    private JFXTextField txtNameEmployee;
    @FXML
    private JFXTextField txtEmployeeTypeandJob;
    @FXML
    private JFXTextArea txtNoteEmployeeWarningLetter;
    @FXML
    private AnchorPane ancWarningLetterType;
    @FXML
    private JFXDatePicker dpStartDate;
    @FXML
    private JFXDatePicker dpEndDate;
    @FXML
    private JFXButton btnSave;
    @FXML
    private JFXButton btnCancel;
    
    private JFXCComboBoxTablePopup cbpWarningLetterType;
    public TblCalendarEmployeeWarningLetter selectedData;
    private int dataInputStatus  = 0;
    public String strWarningLetterStatus;
    
    private void initForm(){
       strWarningLetterStatus = "SP ";
       initDataPopup();
       btnSave.setOnAction((e)->{
         employeeWarningLetterSaveHandle();
       });
       
       btnCancel.setOnAction((e)->{
          employeeWarningLetterCancelHandle();
       });
       
       if(employeeController.selectedData.getWarningLetterStatus()==0){
          strWarningLetterStatus += "-"; 
       }
       else{
         strWarningLetterStatus += employeeController.selectedData.getWarningLetterStatus();
       }
       
      lblEmployeeWarningLetterStatus.setText("Status Surat Peringatan : "+strWarningLetterStatus);
      lblEmployeeWarningLetterStatus.setOnMouseClicked((e)->{
         if(e.getClickCount()==2){
           selectedData = new TblCalendarEmployeeWarningLetter(); 
           selectedData.setTblEmployeeByIdemployee(employeeController.selectedData);
           showEmployeeWarningLetterStatusDialog();
           lblEmployeeWarningLetterStatus.setText("Status Surat Peringatan : "+strWarningLetterStatus);
         } 
      });
    }
    
    private void initDataPopup(){
     TableView<TblEmployeeWarningLetterType>tblWarningLetterType = new TableView();
     TableColumn<TblEmployeeWarningLetterType,String>warningLetterType = new TableColumn("Tipe Surat Peringatan");
     warningLetterType.setMinWidth(180);
     warningLetterType.setCellValueFactory(cellData -> cellData.getValue().nameWarningLetterProperty());
     tblWarningLetterType.getColumns().add(warningLetterType);
     
     ObservableList<TblEmployeeWarningLetterType>warningLetterItems = FXCollections.observableArrayList(loadAllDataWarningLetter());
     
     cbpWarningLetterType = new JFXCComboBoxTablePopup(TblEmployeeWarningLetterType.class,tblWarningLetterType,warningLetterItems,"","Tipe Surat Peringatan *",true,500,300);
     
     ancWarningLetterType.getChildren().clear();
     AnchorPane.setTopAnchor(cbpWarningLetterType,0.0);
     AnchorPane.setBottomAnchor(cbpWarningLetterType,0.0);
     AnchorPane.setLeftAnchor(cbpWarningLetterType,0.0);
     AnchorPane.setRightAnchor(cbpWarningLetterType,0.0);
     ancWarningLetterType.getChildren().add(cbpWarningLetterType);
    }
    
    private List<TblEmployeeWarningLetterType>loadAllDataWarningLetter(){
      List<TblEmployeeWarningLetterType>list = employeeController.getService().getAllDataWarningLetter();
      return list;
    }
    
    private void setSelectedDataToInputForm(){
      txtNameEmployee.setText(selectedData.getTblEmployeeByIdemployee().getTblPeople().getFullName());
      txtEmployeeTypeandJob.setText(selectedData.getTblEmployeeByIdemployee().getRefEmployeeType().getTypeName()+"-"+selectedData.getTblEmployeeByIdemployee().getTblJob().getJobName());
      cbpWarningLetterType.valueProperty().bindBidirectional(selectedData.tblEmployeeWarningLetterTypeProperty());
       
      selectedData.setStartDate(Date.valueOf(dpStartDate.getValue()));
      dpStartDate.valueProperty().addListener((obs,oldVal,newVal)->{
          selectedData.setStartDate(Date.valueOf(newVal));
      });
      
      selectedData.setEndDate(Date.valueOf(dpEndDate.getValue()));
      dpEndDate.valueProperty().addListener((obs,oldVal,newVal)->{
         selectedData.setEndDate(Date.valueOf(newVal));
      });
      
      //setEmployeeWarningLetter(selectedData.getTblEmployeeByIdemployee());
      txtNoteEmployeeWarningLetter.textProperty().bindBidirectional(selectedData.noteWarningLetterProperty());
    }
    
    /*private void setEmployeeWarningLetter(TblEmployee employee){
       List<TblCalendarEmployeeWarningLetter>list = loadAllDataEmployeeWarningLetter();
      
       for(TblCalendarEmployeeWarningLetter tblEmployeeWarningLetter : list){
           if(employee.getEmployeeWarningLetterStatus()==null){
             employee.setEmployeeWarningLetterStatus(tblEmployeeWarningLetter.getTblEmployeeWarningLetterType().getNameWarningLetter());
           }
           else{
               if(tblEmployeeWarningLetter.getStartDate().toString().equalsIgnoreCase(Date.valueOf(LocalDate.now()).toString())){
                 employee.setEmployeeWarningLetterStatus(tblEmployeeWarningLetter.getTblEmployeeWarningLetterType().getNameWarningLetter());
               }
           }  
       }
    }*/
    
    private void employeeWarningLetterCreateHandle(){
     selectedData = new TblCalendarEmployeeWarningLetter(); 
     selectedData.setTblEmployeeByIdemployee(employeeController.selectedData);
     dataInputStatus = 0;
     dpStartDate.setValue(LocalDate.now());
     dpEndDate.setValue(LocalDate.now());
     setSelectedDataToInputForm();
     
     dataEmployeeWarningLetterFormShowStatus.set(1);
    }
    
    private void employeeWarningLetterUpdateHandle(){
       if(tableDataEmployeeWarningLetter.getTableView().getSelectionModel().getSelectedItems().size()==1){
         selectedData = employeeController.getService().getDataEmployeeWarningLetter(((TblCalendarEmployeeWarningLetter)tableDataEmployeeWarningLetter.getTableView().getSelectionModel().getSelectedItem()).getIdrelation());
         dataInputStatus = 1;
         dpStartDate.setValue(((java.sql.Date)selectedData.getStartDate()).toLocalDate());
         dpEndDate.setValue(((java.sql.Date)selectedData.getEndDate()).toLocalDate());
         setSelectedDataToInputForm();
         
         dataEmployeeWarningLetterFormShowStatus.set(1);
       }
    }
    
    private void employeeWarningLetterDeleteHandle(){
       if(tableDataEmployeeWarningLetter.getTableView().getSelectionModel().getSelectedItems().size()==1){
          Alert alert = ClassMessage.showConfirmationDeletingDataMessage(null, null);
          selectedData = employeeController.getService().getDataEmployeeWarningLetter(((TblCalendarEmployeeWarningLetter)tableDataEmployeeWarningLetter.getTableView().getSelectionModel().getSelectedItem()).getIdrelation());
           if(alert.getResult()==ButtonType.OK){
               if(employeeController.getService().deleteDataEmployeeWarningLetter(selectedData)){
                 ClassMessage.showSucceedDeletingDataMessage(null, null);
                 tableDataEmployeeWarningLetter.getTableView().setItems(loadAllDataEmployeeWarningLetter());
                 dataEmployeeWarningLetterFormShowStatus.set(0);
               }
               else{
                 ClassMessage.showFailedDeletingDataMessage(employeeController.getService().getErrorMessage(), null);
               }
           }
       }
    }
    
    Stage dialogStage;
    private void showEmployeeWarningLetterStatusDialog(){
      try {
            // Load the fxml file and create a new stage for the popup dialog.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(HotelFX.class.getResource("view/feature_employee/employee/EmployeeWarningLetterStatusDialog.fxml"));

            EmployeeWarningLetterStatusController controller = new EmployeeWarningLetterStatusController(this);
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
    
    private void employeeWarningLetterSaveHandle(){
       TblCalendarEmployeeWarningLetter dummySelectedData = new TblCalendarEmployeeWarningLetter(selectedData);
       if(checkDataInput()){
            Alert alert = ClassMessage.showConfirmationSavingDataMessage(null, null);
            if(alert.getResult()==ButtonType.OK){
                switch(dataInputStatus){
                    case 0 :
                        if(employeeController.getService().insertDataEmployeeWarningLetter(dummySelectedData)){
                         ClassMessage.showSucceedInsertingDataMessage(null, null);
                         tableDataEmployeeWarningLetter.getTableView().setItems(loadAllDataEmployeeWarningLetter());
                         dataEmployeeWarningLetterFormShowStatus.set(0);
                        }
                        else{
                          ClassMessage.showFailedInsertingDataMessage(employeeController.getService().getErrorMessage(), null);
                        }
                    break;
                        
                    case 1 :
                       if(employeeController.getService().updateDataEmployeeWarningLetter(dummySelectedData)){
                         ClassMessage.showSucceedUpdatingDataMessage(null, null);
                         tableDataEmployeeWarningLetter.getTableView().setItems(loadAllDataEmployeeWarningLetter());
                         dataEmployeeWarningLetterFormShowStatus.set(0);
                       }
                       else{
                         ClassMessage.showFailedUpdatingDataMessage(employeeController.getService().getErrorMessage(), null);
                       }
                    break;
                }
            }
       }
    }  
               
    private void employeeWarningLetterCancelHandle(){
      tableDataEmployeeWarningLetter.getTableView().setItems(loadAllDataEmployeeWarningLetter());
      dataEmployeeWarningLetterFormShowStatus.set(0);
    }
    
    private String errDataInput;
    private boolean checkDataInput(){
       errDataInput= "";
       boolean check = true;
       if(cbpWarningLetterType.getValue()==null){
         errDataInput+=ClassMessage.defaultErrorNullValueMessage+"\n";
         check = false;
       }
      return check;
    }
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
      System.out.println("hsl>>"+employeeController.selectedData.getTblPeople().getFullName());
      setDataEmployeeWarningLetterSplitPane();
      initTableEmployeeWarningLetter();
      initForm();
      spDataEmployeeWarningLetter.widthProperty().addListener((obs,oldVal,newVal)->{
         dataEmployeeWarningLetterFormShowStatus.set(0);
      });
      
     
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    public EmployeeWarningLetterController(EmployeeController employeeController){
      this.employeeController = employeeController;
    }
    
    public final EmployeeController employeeController;
}
