/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hotelfx.view.feature_room_check.room_status;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXCComboBoxTablePopup;
import com.jfoenix.controls.JFXTextField;
import hotelfx.helper.ClassFilteringTable;
import hotelfx.helper.ClassFormatter;
import hotelfx.helper.ClassMessage;
import hotelfx.helper.ClassViewSetting;
import hotelfx.persistence.model.TblItem;
import hotelfx.persistence.model.TblRoomCheckHouseKeepingAttendanceDetail;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.AnchorPane;
import javafx.util.Callback;

/**
 *
 * @author Andreas
 */
public class CodeItemWorkSheetController implements Initializable{
    
    @FXML
    private AnchorPane cbpItemLayout;
    private JFXCComboBoxTablePopup<TblItem>cbpItem;
    @FXML
    private JFXTextField txtCodeItemWorkSheet;
    @FXML
    private JFXButton btnSave;
    @FXML
    private JFXButton btnCancel;
    private TblRoomCheckHouseKeepingAttendanceDetail selectedData;
    private TableView<TblItem>tblItem;
    @FXML
    private AnchorPane tblItemLayout;
    @FXML
    private AnchorPane ancFilteringTable;
    private ClassFilteringTable<TblItem>cft;
    
    private void setTableItemWorkSheet(){
      tblItem = new TableView();
      tblItem.setEditable(dataInputStatus != 3);
      TableColumn<TblItem,String>codeItem = new TableColumn("Kode Barang \n(Sistem)");
      codeItem.setMinWidth(120);
      codeItem.setCellValueFactory(cellData -> cellData.getValue().codeItemProperty());
      TableColumn<TblItem,String>nameItem = new TableColumn("Nama Barang");
      nameItem.setMinWidth(200);
      nameItem.setCellValueFactory(cellData -> cellData.getValue().itemNameProperty());
      TableColumn<TblItem,String>codeWorksheet = new TableColumn("Kode Barang \n(Worksheet)");
      codeWorksheet.setMinWidth(120);
      codeWorksheet.setCellValueFactory(cellData -> cellData.getValue().codeWorkSheetProperty());
      Callback<TableColumn<TblItem,String>,TableCell<TblItem,String>>cellCodeWorksheetFactory = 
      new Callback<TableColumn<TblItem,String>,TableCell<TblItem,String>>(){

          @Override
          public TableCell call(TableColumn p) {
             return new CodeItemWorksheetCell();
           }
          
      };
      codeWorksheet.setCellFactory(cellCodeWorksheetFactory);
      codeWorksheet.setEditable(true);
      
      ObservableList<TblItem>itemList = FXCollections.observableArrayList(loadAllDataItem());
      tblItem.getColumns().addAll(codeItem,nameItem,codeWorksheet);
      tblItem.setItems(itemList);
      
     cft = new ClassFilteringTable<>(TblItem.class,tblItem,tblItem.getItems());
      
      ancFilteringTable.getChildren().clear();
      AnchorPane.setBottomAnchor(cft,0.0);
      AnchorPane.setLeftAnchor(cft,15.0);
      AnchorPane.setRightAnchor(cft,0.0);
      AnchorPane.setTopAnchor(cft,0.0);
      ancFilteringTable.getChildren().add(cft);
    }
    
    private void initTableCodeItemWorkSheet(){
      setTableItemWorkSheet();
      tblItemLayout.getChildren().clear();
      AnchorPane.setBottomAnchor(tblItem,15.0);
      AnchorPane.setLeftAnchor(tblItem,15.0);
      AnchorPane.setRightAnchor(tblItem,15.0);
      AnchorPane.setTopAnchor(tblItem,0.0);
      tblItemLayout.getChildren().add(tblItem);
    }
    
    int dataInputStatus = 0;
    public class CodeItemWorksheetCell extends TableCell<TblItem,String> {

        private JFXTextField txtCodeWorksheet;
       
        public CodeItemWorksheetCell(){
          
        }

        @Override
        public void startEdit() {
            if (!isEmpty()) {
                super.startEdit();
                txtCodeWorksheet = new JFXTextField();
                txtCodeWorksheet.textProperty().bindBidirectional(((TblItem)this.getTableRow().getItem()).codeWorkSheetProperty());
                txtCodeWorksheet.setPromptText("Kode Worksheet");
               // ClassViewSetting.setImportantField(txtCodeWorksheet);
                txtCodeWorksheet.setMinWidth(this.getWidth() - this.getGraphicTextGap() * 2);
               // Bindings.createStringBinding(()->((TblItem)this.getTableRow().getItem()).getCodeWorkSheet(),((TblItem)this.getTableRow().getItem()).codeWorkSheetProperty());
                setText(null);
                setGraphic(txtCodeWorksheet);
                txtCodeWorksheet.selectAll();
              //  ClassFormatter.setToNumericField("BigDecimal",txtCodeWorksheet);
              //  Bindings.bindBidirectional(txtCodeWorksheet.textProperty(),((TblItem)this.getTableRow().getItem(), null);
                 if (this.getTableRow() != null) {
                    if (this.getTableRow().getIndex() % 2 == 0) {
                        getStyleClass().remove("cell-input-even-worksheet");
                        getStyleClass().add("cell-input-even-edit-worksheet");
                    } else {
                        getStyleClass().remove("cell-input-odd-worksheet");
                        getStyleClass().add("cell-input-odd-edit-worksheet");
                    }
                }
                 else{
                    getStyleClass().remove("cell-input-even-worksheet");
                    getStyleClass().remove("cell-input-odd-worksheet");
                    setText(null);
                    setGraphic(null);
                 } 
            }
        }

        @Override
        public void cancelEdit() {
            super.cancelEdit();
            
            System.out.println("text : " + txtCodeWorksheet);
            System.out.println("object : "+ this.getTableRow().getItem());
            
            txtCodeWorksheet.textProperty().unbindBidirectional(((TblItem) this.getTableRow().getItem()).codeWorkSheetProperty());

            setText((String) getItem());
            setGraphic(null);

            //cell input color
            if (this.getTableRow() != null) {
                if (this.getTableRow().getIndex() % 2 == 0) {
                    getStyleClass().remove("cell-input-even-edit-worksheet");
                    getStyleClass().add("cell-input-even-worksheet");
                } else {
                    getStyleClass().remove("cell-input-odd-edit-worksheet");
                    getStyleClass().add("cell-input-odd-worksheet");
                }
            }
            else{
              getStyleClass().remove("cell-input-even-worksheet");
              getStyleClass().remove("cell-input-odd-worksheet");
              setText(null);
                    setGraphic(null);
            }
        }
        
         @Override
     public void updateItem(String item, boolean empty) {
      super.updateItem(item, empty);
            if (!empty) {
              if(!isEditing()){
                   if (this.getTableRow() != null) {
                        Object data = this.getTableRow().getItem();
                        if (data != null) {
                           setText(((TblItem)data).getCodeWorkSheet());
 
                           if (this.getTableRow().getIndex() % 2 == 0) {
                              getStyleClass().add("cell-input-even-worksheet");
                            } 
                            else {
                              getStyleClass().add("cell-input-odd-worksheet");
                            }
                           // setGraphic(null);
                        } else {
                            getStyleClass().remove("cell-input-odd-worksheet");
                            getStyleClass().remove("cell-input-even-worksheet");
                            setText(null);
                            setGraphic(null);
                        }
                        //cell input color
                      
                    } else {
                        getStyleClass().remove("cell-input-odd-worksheet");
                        getStyleClass().remove("cell-input-even-worksheet");
                        setText(null);
                        setGraphic(null);
                    }
               }  
            }
            else{
              getStyleClass().remove("cell-input-odd-worksheet");
              getStyleClass().remove("cell-input-even-worksheet");
              setText(null);
              setGraphic(null);
            }
        }
    }
    
    private void initFormCodeItemWorkSheet(){
      //initDataPopup();
      
     // initTableCodeItemWorkSheet();
      btnCancel.setOnAction((e)->{
         roomStatusController.dialogStageCodeItemWorkSheet.close();
      });
      
      btnSave.setOnAction((e)->{
          codeItemWorkSheetSaveHandle();
      });
    }
    
    private void initDataPopup(){
      TableView<TblItem>tblItem = new TableView();
      TableColumn<TblItem,String>codeItem = new TableColumn("Kode Barang");
      codeItem.setMinWidth(120);
      codeItem.setCellValueFactory(cellData -> cellData.getValue().codeItemProperty());
      TableColumn<TblItem,String>nameItem = new TableColumn("Nama Barang");
      nameItem.setMinWidth(200);
      nameItem.setCellValueFactory(cellData -> cellData.getValue().itemNameProperty());
      tblItem.getColumns().addAll(codeItem,nameItem);
      ObservableList<TblItem>listItem = FXCollections.observableArrayList(loadAllDataItem());
      cbpItem  = new JFXCComboBoxTablePopup(TblItem.class,tblItem,listItem,"","Barang *",true,500,300);
      
      cbpItemLayout.getChildren().clear();
      AnchorPane.setBottomAnchor(cbpItem,0.0);
      AnchorPane.setLeftAnchor(cbpItem,0.0);
      AnchorPane.setRightAnchor(cbpItem,0.0);
      AnchorPane.setTopAnchor(cbpItem,0.0);
      cbpItemLayout.getChildren().add(cbpItem);
    }
    
    private List<TblItem>loadAllDataItem(){
      return roomStatusController.getService().getAllDataItem();
    }
    
    private void setSelectedDataToInputForm(){
       
    }
    
    private void codeItemWorkSheetSaveHandle(){
       
       List<TblItem>list =new ArrayList();
       list.addAll(tblItem.getItems());
       Alert alert  = ClassMessage.showConfirmationSavingDataMessage(null, null);
       
       if(alert.getResult() == ButtonType.OK){
           if(roomStatusController.getService().updateItemCodeWorkSheet(list)){
             ClassMessage.showSucceedUpdatingDataMessage(null, null);
            }
           else{
             ClassMessage.showFailedUpdatingDataMessage(roomStatusController.getService().getErrorMessage(), null);
            }
        }
      
    }
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
     initTableCodeItemWorkSheet();
     initFormCodeItemWorkSheet();
      // throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    public CodeItemWorkSheetController(RoomStatusController roomStatusController){
       this.roomStatusController = roomStatusController;
    }
    private final RoomStatusController roomStatusController;
}
