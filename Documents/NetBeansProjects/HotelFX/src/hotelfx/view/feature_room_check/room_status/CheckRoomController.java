/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hotelfx.view.feature_room_check.room_status;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXCComboBoxTablePopup;
import com.jfoenix.controls.JFXTextField;
import hotelfx.helper.ClassDataRoomCheck;
import hotelfx.helper.ClassFormatter;
import hotelfx.helper.ClassMessage;
import hotelfx.helper.ClassViewSetting;
import hotelfx.persistence.model.RefRoomStatus;
import hotelfx.persistence.model.TblReservationAdditionalItem;
import hotelfx.persistence.model.TblRoomCheckHouseKeepingAttendance;
import hotelfx.persistence.model.TblRoomCheckHouseKeepingAttendanceDetail;
import hotelfx.persistence.model.TblRoomTypeItem;
import hotelfx.view.feature_room_check.room_status.RoomStatusController.DataRoomStatus;
import java.math.BigDecimal;
import java.net.URL;
import java.sql.Date;
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
public class CheckRoomController implements Initializable{
    
    @FXML
    private AnchorPane tblRoomCheckLayout;
    private TableView<ClassDataRoomCheck> tblRoomCheck;
    
    private void initTableCheckRoomLayout(){
       setTableCheckRoom();
       tblRoomCheckLayout.getChildren().clear();
       AnchorPane.setBottomAnchor(tblRoomCheck,5.0);
       AnchorPane.setLeftAnchor(tblRoomCheck,15.0);
       AnchorPane.setRightAnchor(tblRoomCheck,15.0);
       AnchorPane.setTopAnchor(tblRoomCheck,5.0);
       tblRoomCheckLayout.getChildren().add(tblRoomCheck);
    }
    
    private void setTableCheckRoom(){
      tblRoomCheck = new TableView();
      tblRoomCheck.setEditable(true);
      TableColumn<ClassDataRoomCheck,String>roomCheckCodeItem = new TableColumn("Kode Barang"); 
      roomCheckCodeItem.setMinWidth(120);
      roomCheckCodeItem.setCellValueFactory((TableColumn.CellDataFeatures<ClassDataRoomCheck,String>param)
      ->Bindings.createStringBinding(()->param.getValue().getItem().getCodeItem(),param.getValue().itemProperty()));
      TableColumn<ClassDataRoomCheck,String>roomCheckCodeItemWorkSheet = new TableColumn("Kode Work Sheet");
      roomCheckCodeItemWorkSheet.setMinWidth(120);
      roomCheckCodeItemWorkSheet.setCellValueFactory(cellData -> cellData.getValue().codeWorkSheetProperty());
      TableColumn<ClassDataRoomCheck,String>roomCheckNameItem = new TableColumn("Nama Barang");
      roomCheckNameItem.setMinWidth(200);
      roomCheckNameItem.setCellValueFactory((TableColumn.CellDataFeatures<ClassDataRoomCheck,String>param)
      ->Bindings.createStringBinding(()->param.getValue().getItem().getItemName(),param.getValue().itemProperty()));
      TableColumn<ClassDataRoomCheck,String>roomCheckQtySysItem = new TableColumn("Jumlah Sistem");
      roomCheckQtySysItem.setMinWidth(100);
      roomCheckQtySysItem.setCellValueFactory((TableColumn.CellDataFeatures<ClassDataRoomCheck,String>param)
      ->Bindings.createStringBinding(()->ClassFormatter.decimalFormat.cFormat(param.getValue().getQtySysItem()),param.getValue().qtySysItemProperty()));
      TableColumn<ClassDataRoomCheck,String>roomCheckQtyRealItem = new TableColumn("Jumlah Sekarang");
      roomCheckQtyRealItem.setMinWidth(120);
      roomCheckQtyRealItem.setCellValueFactory(cellData->cellData.getValue().qtyRealItemProperty().asString());
      Callback<TableColumn<ClassDataRoomCheck,String>,TableCell<ClassDataRoomCheck,String>>cellQtyRealFactory = 
      new Callback<TableColumn<ClassDataRoomCheck,String>,TableCell<ClassDataRoomCheck,String>>(){

          @Override
          public TableCell call(TableColumn p) {
             return new QtyRealItemCell();
           }
          
      };
      roomCheckQtyRealItem.setCellFactory(cellQtyRealFactory);
      roomCheckQtyRealItem.setEditable(true);
      tblRoomCheck.getColumns().addAll(roomCheckCodeItem,roomCheckCodeItemWorkSheet,roomCheckNameItem,roomCheckQtySysItem,roomCheckQtyRealItem);
      ObservableList<ClassDataRoomCheck>listRoomCheck = FXCollections.observableArrayList(loadAllDataRoomCheck(roomStatusController.selectedDataRoomStatus));
      tblRoomCheck.setItems(listRoomCheck);
    }
    
    private List<ClassDataRoomCheck>loadAllDataRoomCheck(DataRoomStatus roomStatus){
      List<ClassDataRoomCheck>list = new ArrayList();
      List<TblRoomTypeItem>listRoomTypeItem = roomStatusController.getService().getAllDataRoomTypeItemByIDRoomType(roomStatus.getDataRoom().getTblRoomType().getIdroomType());
      List<TblReservationAdditionalItem>listReservationAdditionalItem = new ArrayList();
      ClassDataRoomCheck dataRoomCheck = null;
      
       if(roomStatus.getDataPreviousRRTD()!=null){
           System.out.println("previous");
         listReservationAdditionalItem = roomStatusController.getService().getAllDataReservationAdditionalItemByIDReservationRoomTypeDetailAdditionalDateAndBillType(roomStatus.getDataPreviousRRTD().getIddetail(),Date.valueOf(roomStatus.getPreviousDate()));
        }
       else if(roomStatus.getDataNextRRTD()!=null){
           System.out.println("next : " + roomStatus.getDataNextRRTD().getIddetail() + " - " + roomStatus.getNextDate());
          listReservationAdditionalItem = roomStatusController.getService().getAllDataReservationAdditionalItemByIDReservationRoomTypeDetailAdditionalDateAndBillType(roomStatus.getDataNextRRTD().getIddetail(),Date.valueOf(roomStatus.getNextDate()));
           System.out.println("size : " + listReservationAdditionalItem.size());
        }
        
        for(TblRoomTypeItem roomTypeItem : listRoomTypeItem){
           dataRoomCheck = new ClassDataRoomCheck();
           dataRoomCheck.setItem(roomTypeItem.getTblItem());
           dataRoomCheck.setQtySysItem(roomTypeItem.getItemQuantity());
           dataRoomCheck.setQtyRealItem(roomTypeItem.getItemQuantity());
           dataRoomCheck.setCodeWorkSheet(roomTypeItem.getTblItem().getCodeWorkSheet());
           list.add(dataRoomCheck);
        }
        
        for(TblReservationAdditionalItem additionalItem : listReservationAdditionalItem){
             boolean found = false;
           for(ClassDataRoomCheck getDataRoomCheck : list){
               if(additionalItem.getTblItem().getIditem()==getDataRoomCheck.getItem().getIditem()){
                  getDataRoomCheck.setQtySysItem(getDataRoomCheck.getQtySysItem().add(additionalItem.getItemQuantity()));
                  getDataRoomCheck.setQtyRealItem(getDataRoomCheck.getQtySysItem());
                  found = true;
               }
            }
           
           if(found==false){
             dataRoomCheck = new ClassDataRoomCheck();
             dataRoomCheck.setItem(additionalItem.getTblItem());
             dataRoomCheck.setCodeWorkSheet(additionalItem.getTblItem().getCodeWorkSheet());
             dataRoomCheck.setQtySysItem(additionalItem.getItemQuantity());
             dataRoomCheck.setQtyRealItem(dataRoomCheck.getQtySysItem());
             list.add(dataRoomCheck);
            }
        }
        
       return list;
    }
    
     public class QtyRealItemCell extends TableCell<ClassDataRoomCheck,String> {

        private JFXTextField txtQtyRealItem;
       
        public QtyRealItemCell(){
          
        }

        @Override
        public void startEdit() {
           if (!isEmpty() && this.getTableRow() != null) {
                super.startEdit();
                txtQtyRealItem = new JFXTextField();
                Bindings.bindBidirectional( txtQtyRealItem.textProperty(),((ClassDataRoomCheck)this.getTableRow().getItem()).qtyRealItemProperty(),new ClassFormatter.CBigDecimalStringConverter());
                //bindBidirectional(((ClassDataRoomCheck)this.getTableRow().getItem()).qtyRealItemProperty().asString());
                txtQtyRealItem.setPromptText("Jumlah Sekarang");
               // ClassViewSetting.setImportantField(txtCodeWorksheet);
                txtQtyRealItem.setMinWidth(this.getWidth() - this.getGraphicTextGap() * 2);
                //Bindings.createStringBinding(((TblItem)this.getTableRow().getItem()).getCodeWorkSheet(),((TblItem)this.getTableRow().getItem()).codeWorkSheetProperty());
                setText(ClassFormatter.decimalFormat.cFormat(((ClassDataRoomCheck)this.getTableRow().getItem()).getQtySysItem()));
                setGraphic(txtQtyRealItem);
                txtQtyRealItem.selectAll();
                ClassFormatter.setToNumericField("BigDecimal",txtQtyRealItem);
              //  Bindings.bindBidirectional(txtCodeWorksheet.textProperty(),((TblItem)this.getTableRow().getItem(), null);
               if (this.getTableRow().getIndex() % 2 == 0) {
                        getStyleClass().remove("cell-input-even-worksheet");
                        getStyleClass().add("cell-input-even-edit-worksheet");
                } else {
                        getStyleClass().remove("cell-input-odd-worksheet");
                        getStyleClass().add("cell-input-odd-edit-worksheet");
                }
            }
            
        }

        @Override
        public void cancelEdit() {
            super.cancelEdit();
            
            txtQtyRealItem.textProperty().unbindBidirectional(((ClassDataRoomCheck) this.getTableRow().getItem()).qtyRealItemProperty().asString());

            setText(ClassFormatter.decimalFormat.cFormat(((ClassDataRoomCheck)this.getTableRow().getItem()).getQtyRealItem()));
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
                          setText(ClassFormatter.decimalFormat.cFormat(((ClassDataRoomCheck) data).getQtySysItem()));
                          /*  if (((ClassDataRoomCheck) data).getQtyRealItem() != null) {
                                
                            } else {
                                setText("");
                            }
                            setGraphic(null); */
                            
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
                        setText(null);
                        setGraphic(null);
                    }
               }
            }
        }
    }
    @FXML
    private JFXTextField txtRoom;
    @FXML
    private AnchorPane roomStatusLayout;
    private JFXCComboBoxTablePopup<RefRoomStatus>cbpRoomStatus;
    @FXML
    private JFXButton btnCancel;
    @FXML
    private JFXButton btnSave;
    private TblRoomCheckHouseKeepingAttendance selectedData;
    
    private void initFormCheckRoom(){
      initDataPopup();
      
      btnCancel.setOnAction((e)->{
         roomStatusController.dialogStageCheckRoom.close();
      });
      
      btnSave.setOnAction((e)->{
          checkRoomSaveHandle();
      });
      
      initImportantField();
    }
    
    private void initImportantField(){
      ClassViewSetting.setImportantField(cbpRoomStatus);
    }
    
    private void initDataPopup(){
      TableView<RefRoomStatus>tblRoomStatus = new TableView();
      TableColumn<RefRoomStatus,String>roomStatusName = new TableColumn("Status Kamar");
      roomStatusName.setMinWidth(150);
      roomStatusName.setCellValueFactory(cellData -> cellData.getValue().statusNameProperty());
      TableColumn<RefRoomStatus,String>roomStatusNote = new TableColumn("Keterangan");
      roomStatusNote.setMinWidth(200);
      roomStatusNote.setCellValueFactory((cellData -> cellData.getValue().statusNoteProperty()));
      tblRoomStatus.getColumns().addAll(roomStatusName,roomStatusNote);
      ObservableList<RefRoomStatus>listRoomStatusItems = FXCollections.observableArrayList(loadAllRoomStatus());
      cbpRoomStatus = new JFXCComboBoxTablePopup(RefRoomStatus.class,tblRoomStatus,listRoomStatusItems,"","Status Kamar *",true,400,300);
      
      roomStatusLayout.getChildren().clear();
      AnchorPane.setBottomAnchor(cbpRoomStatus,0.0);
      AnchorPane.setLeftAnchor(cbpRoomStatus,0.0);
      AnchorPane.setRightAnchor(cbpRoomStatus,0.0);
      AnchorPane.setTopAnchor(cbpRoomStatus,0.0);
      roomStatusLayout.getChildren().add(cbpRoomStatus);
    }
    
    private List<RefRoomStatus>loadAllRoomStatus(){
       List<RefRoomStatus>list = new ArrayList();
           if(roomStatusController.selectedDataRoomStatus.getDataRoom().getRefRoomStatus().getStatusName().charAt(0)=='O'){
             System.out.println("occupied");
             list = getRoomStatus(1);
            }
       
       
       if(roomStatusController.selectedDataRoomStatus.getDataRoom().getRefRoomStatus().getStatusName().charAt(0)=='V'){
          list = getRoomStatus(2);
          System.out.println("vacant");
       }
      return list;
    }
    
    private List<RefRoomStatus>getRoomStatus(int status){
        List<RefRoomStatus>list = roomStatusController.getService().getAllDataRoomStatus();
        System.out.println("size list :"+list.size());
        List<RefRoomStatus>listRoomStatus = new ArrayList();
       switch(status){
           case 1 :
               listRoomStatus.clear();
               for(int i = list.size()-1; i>=0 ; i--){
                   if(list.get(i).getStatusName().charAt(0)=='O'){
                       if(list.get(i).getIdstatus()!=6 && list.get(i).getIdstatus()!=2){
                          listRoomStatus.add(list.get(i));
                       }
                    }
               }
           break;
           case 2 : 
               listRoomStatus.clear();
               for(int i = list.size()-1; i>=0 ; i--){ 
                   if(list.get(i).getStatusName().charAt(0)=='V'){
                       if(list.get(i).getIdstatus()!=4){
                         listRoomStatus.add(list.get(i));
                        }
                    }
                    
                    if(list.get(i).getIdstatus()==6){
                      listRoomStatus.add(list.get(i));
                    }
                   
               }
           break;
       }
      return listRoomStatus;
    }
    
    private void setSelectedDataToInputForm(){
      txtRoom.textProperty().bindBidirectional(roomStatusController.selectedDataRoomStatus.getDataRoom().roomNameProperty());
      cbpRoomStatus.valueProperty().bindBidirectional(selectedData.refRoomStatusByRoomStatusAfterProperty());
    }
    
    private void checkRoomCreateHandle(){
      // System.out.println("room status :"+roomStatusController.selectedDataRoomStatus.getPreviousDate());
       System.out.println("house keeping attendance :"+roomStatusController.selectedDataHouseKeepingAttendance.getTblRoom().getRoomName());
       selectedData = new TblRoomCheckHouseKeepingAttendance();
       selectedData = roomStatusController.selectedDataHouseKeepingAttendance;
       setSelectedDataToInputForm();
    }
    
    private void checkRoomSaveHandle(){
       if(checkDataInput()){
           TblRoomCheckHouseKeepingAttendance dummySelectedData = new TblRoomCheckHouseKeepingAttendance(selectedData); 
           dummySelectedData.setTblRoom(selectedData.getTblRoom());
           dummySelectedData.getTblRoom().setRefRoomStatus(cbpRoomStatus.getValue());
           List<ClassDataRoomCheck>list = (List<ClassDataRoomCheck>)tblRoomCheck.getItems();
           System.out.println("size list :"+list.size());
           List<TblRoomCheckHouseKeepingAttendanceDetail>listHouseKeepingAttendanceDetail = new ArrayList();
            for(ClassDataRoomCheck roomCheck : list){
              TblRoomCheckHouseKeepingAttendanceDetail houseKeepingAttendanceDetail = new TblRoomCheckHouseKeepingAttendanceDetail();
              houseKeepingAttendanceDetail.setCodeWorkSheet(roomCheck.getCodeWorkSheet());
              houseKeepingAttendanceDetail.setQtyReal(roomCheck.getQtyRealItem());
              houseKeepingAttendanceDetail.setQtySistem(roomCheck.getQtySysItem());
              houseKeepingAttendanceDetail.setTblRoomCheckHouseKeepingAttendance(new TblRoomCheckHouseKeepingAttendance(selectedData));
              houseKeepingAttendanceDetail.getTblRoomCheckHouseKeepingAttendance().setRefRoomStatusByRoomStatusAfter(cbpRoomStatus.getValue());
              houseKeepingAttendanceDetail.setTblItem(roomCheck.getItem());
              listHouseKeepingAttendanceDetail.add(houseKeepingAttendanceDetail);
            }

           Alert alert = ClassMessage.showConfirmationSavingDataMessage(null, null);
           if(alert.getResult()==ButtonType.OK){
               if(roomStatusController.getService().insertHouseKeepingAttendanceDetail(listHouseKeepingAttendanceDetail,dummySelectedData)){
                 ClassMessage.showSucceedInsertingDataMessage(null, null);
                 roomStatusController.setSelectedDataToInputForm();
                 roomStatusController.dialogStageCheckRoom.close();

               }
               else{
                 ClassMessage.showFailedInsertingDataMessage(null, null);
               }
           }
       }
       else{
         ClassMessage.showWarningInputDataMessage(errDataInput, null);
       }
    }
    
    String errDataInput;
    private boolean checkDataInput(){
       errDataInput = "";
       boolean check = true;
       if(cbpRoomStatus.getValue()==null){
         errDataInput += "Status Kamar : "+ClassMessage.defaultErrorNullValueMessage+"\n";
         check = false;
        }
       return check;
    }
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
      initFormCheckRoom();
      checkRoomCreateHandle();
      initTableCheckRoomLayout();
      //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    public CheckRoomController(RoomStatusController roomStatusController){
       this.roomStatusController = roomStatusController;
    }
    
  //  private DataRoomStatus selectedDataRoomStatus; 
    private final RoomStatusController roomStatusController;
}
