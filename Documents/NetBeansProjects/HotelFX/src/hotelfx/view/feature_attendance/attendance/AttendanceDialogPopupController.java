/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hotelfx.view.feature_attendance.attendance;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXCComboBoxPopup;
import com.jfoenix.controls.JFXTextField;
import hotelfx.HotelFX;
import hotelfx.helper.ClassMessage;
import hotelfx.helper.ClassViewSetting;
import hotelfx.persistence.model.TblEmployee;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.GridPane;
import static javafx.scene.layout.Region.USE_COMPUTED_SIZE;

/**
 *
 * @author Andreas
 */
public class AttendanceDialogPopupController implements Initializable{
    
    @FXML
    private GridPane gpSettingFingerPrint;
    @FXML
    private AnchorPane ancEmployee;
    @FXML
    private JFXTextField txtCodeFingerPrint;
    @FXML
    private JFXButton btnSave;
    @FXML
    private JFXButton btnCancel;
    
    private JFXCComboBoxPopup<TblEmployee> cbpEmployee = new JFXCComboBoxPopup(TblEmployee.class);
    private TblEmployee selectedData;
    
    private void initForm(){
      btnSave.setOnAction((e)->{
        attendanceDialogPopupSaveHandle();
      });
      btnCancel.setOnAction((e)->{
        attendanceController.dialogStage.close();
      });
      
     // initPopupDialog();
      initPopupDialogLayout();
      
      initImportantFieldColor();
    }
    
    private void initImportantFieldColor() {
        ClassViewSetting.setImportantField(
                cbpEmployee,
                txtCodeFingerPrint);
    }
    
    private void initPopupDialogLayout(){
       initPopupDialog(); 
       ancEmployee.getChildren().clear();
       AnchorPane.setTopAnchor(cbpEmployee,0.0);
       AnchorPane.setBottomAnchor(cbpEmployee,0.0);
       AnchorPane.setLeftAnchor(cbpEmployee,0.0);
       AnchorPane.setRightAnchor(cbpEmployee,0.0);
       ancEmployee.getChildren().add(cbpEmployee);
    }
    
    private void initPopupDialog(){
      TableView<TblEmployee>tblEmployee = new TableView();
      TableColumn<TblEmployee,String>idEmployee = new TableColumn("ID");
      idEmployee.setMinWidth(100);
      idEmployee.setCellValueFactory(cellData->cellData.getValue().codeEmployeeProperty());
      TableColumn<TblEmployee,String>nameEmployee = new TableColumn("Nama");
      nameEmployee.setMinWidth(160);
      nameEmployee.setCellValueFactory((TableColumn.CellDataFeatures<TblEmployee,String>param)
        ->Bindings.createStringBinding(()->param.getValue().getTblPeople().getFullName(),param.getValue().getTblPeople().fullNameProperty()));
      TableColumn<TblEmployee,String>typeEmployee = new TableColumn("Tipe");
      typeEmployee.setMinWidth(120);
      typeEmployee.setCellValueFactory((TableColumn.CellDataFeatures<TblEmployee,String>param)
        ->Bindings.createStringBinding(()->param.getValue().getRefEmployeeType().getTypeName(),param.getValue().getRefEmployeeType().typeNameProperty()));
      
      tblEmployee.getColumns().addAll(idEmployee,typeEmployee,nameEmployee);
      ObservableList<TblEmployee> employeeItems = FXCollections.observableArrayList(attendanceController.getService().getAllEmployee());
        setFunctionPopup(cbpEmployee,tblEmployee,employeeItems, "tblPeople", "Nama Karyawan *",400,300);
        
       // gpSettingFingerPrint.add(cbpEmployee,1,1);
    }
    
    private void setFunctionPopup(JFXCComboBoxPopup cbp, TableView table, ObservableList items, String namedFiltered, String promptText,double prefWidth,double prefHeight) {
        table.getSelectionModel().selectedIndexProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal.intValue() != -1) {
                cbp.valueProperty().set(table.getItems().get(newVal.intValue()));
            }
            cbp.hide();
        });

        cbp.setPropertyNameForFiltered(namedFiltered);
        cbp.setItems(items);
        
        cbp.setOnShowing((e) -> {
            table.getSelectionModel().clearSelection();
        });

        table.itemsProperty().bind(cbp.filteredItemsProperty());

        JFXButton button = new JFXButton("▾");
        button.setOnMouseClicked((e) -> cbp.show());

        BorderPane content = new BorderPane(new JFXButton("SHOW"));
        content.setBackground(new Background(new BackgroundFill(javafx.scene.paint.Color.WHITE, CornerRadii.EMPTY, Insets.EMPTY)));

        content.setPrefSize(prefWidth, prefHeight);

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
    
    private void refreshDataPopUp(){
      ObservableList<TblEmployee>employeeItems = FXCollections.observableArrayList(attendanceController.getService().getAllEmployee());
      cbpEmployee.setItems(employeeItems);
    }
    
    private void updateEmployeeFingerPrint(){
      refreshDataPopUp();
      cbpEmployee.valueProperty().addListener((obs,oldVal,newVal)->{
         if(newVal!=null){
           selectedData = attendanceController.getService().getEmployee(newVal.getIdemployee());
           txtCodeFingerPrint.textProperty().bindBidirectional(selectedData.codeFingerPrintProperty());
         }    
      });
     
    }
    
    private void attendanceDialogPopupSaveHandle(){
        if(checkDataInput()){
           Alert alert = ClassMessage.showConfirmationSavingDataMessage("", null);
           if (alert.getResult() == ButtonType.OK) {
                TblEmployee employee = new TblEmployee(selectedData);
                System.out.println(">>"+employee.getCodeFingerPrint());
                if(attendanceController.getService().updateCodeFinger(employee)){
                    ClassMessage.showSucceedUpdatingDataMessage(null,attendanceController.dialogStage);
                    //HotelFX.showAlert(Alert.AlertType.INFORMATION, "BERHASIL", "Update data berhasil!!",null,attendanceController.dialogStage);
                    attendanceController.dialogStage.close();
                }
                else{
                  ClassMessage.showFailedUpdatingDataMessage(attendanceController.getService().getErrorMessage(),attendanceController.dialogStage);
                }
            }
        }
        else{
          ClassMessage.showWarningInputDataMessage(errDataInput,attendanceController.dialogStage);
        }
        
    }
    
    String errDataInput;
    private boolean checkDataInput(){
       boolean check = true;
       errDataInput = "";
       if(cbpEmployee.getValue()==null){
          errDataInput += "Nama Karyawan : "+ClassMessage.defaultErrorNullValueMessage+"\n";
          check = false;
       }
       
       if(txtCodeFingerPrint.getText()==null || txtCodeFingerPrint.getText().equalsIgnoreCase("")){
         errDataInput += "Kode Finger Print : "+ClassMessage.defaultErrorNullValueMessage+"\n";
         check = false;
       }
       
       return check;
    }
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
      initForm();
      updateEmployeeFingerPrint();
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    public AttendanceDialogPopupController(AttendanceController parentController){
       this.attendanceController = parentController;
    }
    
    private final AttendanceController attendanceController;
}
