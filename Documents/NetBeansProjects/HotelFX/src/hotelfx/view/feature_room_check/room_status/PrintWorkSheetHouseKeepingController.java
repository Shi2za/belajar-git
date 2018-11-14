/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hotelfx.view.feature_room_check.room_status;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXCComboBoxTablePopup;
import hotelfx.helper.ClassDataRoomCheck;
import hotelfx.helper.ClassFilteringTable;
import hotelfx.helper.ClassFormatter;
import hotelfx.helper.ClassMessage;
import hotelfx.helper.ClassPrinter;
import hotelfx.helper.ClassViewSetting;
import hotelfx.helper.ClassWorkSheetSelected;
import hotelfx.helper.PrintModel.ClassPrintWorkSheetHouseKeeping;
import hotelfx.helper.PrintModel.ClassPrintWorkSheetHouseKeepingDetail;
import hotelfx.persistence.model.TblEmployee;
import hotelfx.persistence.model.TblReservationAdditionalItem;
import hotelfx.persistence.model.TblRoomCheckHouseKeepingAttendance;
import hotelfx.persistence.model.TblRoomTypeItem;
import hotelfx.view.feature_room_check.room_status.RoomStatusController.DataRoomStatus;
import java.net.URL;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;
import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.scene.layout.AnchorPane;

/**
 *
 * @author Andreas
 */
public class PrintWorkSheetHouseKeepingController implements Initializable {

    @FXML
    private AnchorPane ancChecker;

    @FXML
    private AnchorPane ancFilterLayout;

    @FXML
    private AnchorPane ancRoomList;

    private ClassFilteringTable<ClassWorkSheetSelected> cft;

    @FXML
    private JFXButton btnClose;

    @FXML
    private JFXButton btnSelectedAll;

    @FXML
    private JFXButton btnNoSelectedAll;

    @FXML
    private JFXButton btnPrint;

    private TableView<ClassWorkSheetSelected> tblWorkSheet = new TableView();

    private JFXCComboBoxTablePopup<TblEmployee> cbpEmployee;

    private void initTablePrintWorkSheet() {
        setTablePrintWorkSheet();
        ancRoomList.getChildren().clear();
        AnchorPane.setTopAnchor(tblWorkSheet, 0.0);
        AnchorPane.setBottomAnchor(tblWorkSheet, 0.0);
        AnchorPane.setLeftAnchor(tblWorkSheet, 0.0);
        AnchorPane.setRightAnchor(tblWorkSheet, 0.0);
        ancRoomList.getChildren().add(tblWorkSheet);
    }

    private void setTablePrintWorkSheet() {
        tblWorkSheet = new TableView();
        tblWorkSheet.setEditable(true);

        TableColumn<ClassWorkSheetSelected, String> roomName = new TableColumn("Kamar");
        roomName.setCellValueFactory((TableColumn.CellDataFeatures<ClassWorkSheetSelected, String> param)
                -> Bindings.createStringBinding(() -> param.getValue().getRoomStatus().getDataRoom().getRoomName(),
                        param.getValue().getRoomStatus().dataRoomProperty()));
        roomName.setMinWidth(120);

        TableColumn<ClassWorkSheetSelected, String> roomType = new TableColumn("Tipe Kamar");
        roomType.setCellValueFactory((TableColumn.CellDataFeatures<ClassWorkSheetSelected, String> param)
                -> Bindings.createStringBinding(() -> param.getValue().getRoomStatus().getDataRoom().getTblRoomType().getRoomTypeName(),
                        param.getValue().getRoomStatus().getDataRoom().tblRoomTypeProperty()));
        roomType.setMinWidth(120);

        TableColumn<ClassWorkSheetSelected, String> building = new TableColumn("Bangunan");
        building.setCellValueFactory((TableColumn.CellDataFeatures<ClassWorkSheetSelected, String> param)
                -> Bindings.createStringBinding(() -> param.getValue().getRoomStatus().getDataRoom().getTblLocation().getTblBuilding().getBuildingName(),
                        param.getValue().getRoomStatus().getDataRoom().tblLocationProperty()));
        building.setMinWidth(100);

        TableColumn<ClassWorkSheetSelected, String> floor = new TableColumn("Lantai");
        floor.setCellValueFactory((TableColumn.CellDataFeatures<ClassWorkSheetSelected, String> param)
                -> Bindings.createStringBinding(() -> param.getValue().getRoomStatus().getDataRoom().getTblLocation().getTblFloor().getFloorName(),
                        param.getValue().getRoomStatus().dataRoomProperty()));
        floor.setMinWidth(100);

        TableColumn<ClassWorkSheetSelected, String> roomStatus = new TableColumn("Status");
        roomStatus.setCellValueFactory((TableColumn.CellDataFeatures<ClassWorkSheetSelected, String> param)
                -> Bindings.createStringBinding(() -> param.getValue().getRoomStatus().getDataRoom().getRefRoomStatus().getStatusName(),
                        param.getValue().getRoomStatus().getDataRoom().refRoomStatusProperty()));
        roomStatus.setMinWidth(100);

        TableColumn<ClassWorkSheetSelected, Boolean> selected = new TableColumn("Pilih");
        selected.setCellValueFactory(cellData -> cellData.getValue().isSelectedProperty());
        selected.setCellFactory(CheckBoxTableCell.forTableColumn(selected));
        selected.setMinWidth(80);
        selected.setEditable(true);

        tblWorkSheet.getColumns().addAll(building, floor, roomType, roomName, roomStatus, selected);

        ObservableList<ClassWorkSheetSelected> itemWorkSheet = FXCollections.observableArrayList(loadAllDataWorkSheet());

        tblWorkSheet.setItems(itemWorkSheet);

        cft = new ClassFilteringTable<>(
                ClassWorkSheetSelected.class,
                tblWorkSheet,
                tblWorkSheet.getItems());

        AnchorPane.setBottomAnchor(cft, 0.0);
        AnchorPane.setLeftAnchor(cft, 15.0);
        AnchorPane.setRightAnchor(cft, 0.0);
        AnchorPane.setTopAnchor(cft, 0.0);
        ancFilterLayout.getChildren().add(cft);
    }

    private List<ClassWorkSheetSelected> loadAllDataWorkSheet() {
        List<ClassWorkSheetSelected> list = new ArrayList();
        for (DataRoomStatus roomStatus : roomStatusController.loadAllDataRoomWithStatus()) {
            TblRoomCheckHouseKeepingAttendance rchka = roomStatusController.getService().getDataRoomCheckHouseKeepingAttendanceByIDRoomAndIDRoomStatusAfterIsNull(roomStatus.getDataRoom().getIdroom());
            if (rchka == null) {
                ClassWorkSheetSelected workSheetSelected = new ClassWorkSheetSelected();
                workSheetSelected.setRoomStatus(roomStatus);
                list.add(workSheetSelected);
            }
        }
        return list;
    }

    private void initForm() {
        initDataPopup();
        btnClose.setOnAction((e) -> {
            //refresh all data and close form data - change room status
            roomStatusController.setSelectedDataToInputForm();
            roomStatusController.dialogStageWorkSheet.close();
        });

        btnSelectedAll.setOnAction((e) -> {
            setAllSelected();
        });

        btnNoSelectedAll.setOnAction((e) -> {
            setNotAllSelected();
        });

        btnPrint.setOnAction((e) -> {
            printWorkSheetHouseKeepingPrintHandle();
        });

        ClassViewSetting.setImportantField(cbpEmployee);
    }

    TblRoomCheckHouseKeepingAttendance selectedData;

    private void initDataPopup() {
        TableView<TblEmployee> tblEmployee = new TableView();
        TableColumn<TblEmployee, String> employeeCode = new TableColumn("ID Karyawan");
        employeeCode.setCellValueFactory(cellData -> cellData.getValue().codeEmployeeProperty());
        TableColumn<TblEmployee, String> employeeName = new TableColumn("Nama Karyawan");
        employeeName.setCellValueFactory((TableColumn.CellDataFeatures<TblEmployee, String> param)
                -> Bindings.createStringBinding(() -> param.getValue().getTblPeople().getFullName(), param.getValue().tblPeopleProperty()));
        TableColumn<TblEmployee, String> employeeJob = new TableColumn("Jabatan");
        employeeJob.setCellValueFactory((TableColumn.CellDataFeatures<TblEmployee, String> param)
                -> Bindings.createStringBinding(() -> param.getValue().getTblJob().getJobName(), param.getValue().tblJobProperty()));
        tblEmployee.getColumns().addAll(employeeCode, employeeName, employeeJob);
        ObservableList<TblEmployee> employeeItems = FXCollections.observableArrayList(loadAllDataEmployee());
        cbpEmployee = new JFXCComboBoxTablePopup(TblEmployee.class, tblEmployee, employeeItems, "", "Pemeriksa *", false, 400, 300);
        cbpEmployee.setLabelFloat(false);

        ancChecker.getChildren().clear();
        AnchorPane.setTopAnchor(cbpEmployee, 0.0);
        AnchorPane.setBottomAnchor(cbpEmployee, 0.0);
        AnchorPane.setLeftAnchor(cbpEmployee, 0.0);
        AnchorPane.setRightAnchor(cbpEmployee, 0.0);
        ancChecker.getChildren().add(cbpEmployee);
    }

    private List<TblEmployee> loadAllDataEmployee() {
        return roomStatusController.getService().getAllDataEmployee();
    }

    private void setAllSelected() {
        for (int i = 0; i < tblWorkSheet.getItems().size(); i++) {
            tblWorkSheet.getItems().get(i).setIsSelected(true);
        }
    }

    private void setNotAllSelected() {
        for (int i = 0; i < tblWorkSheet.getItems().size(); i++) {
            tblWorkSheet.getItems().get(i).setIsSelected(false);
        }
    }

    private void printWorkSheetHouseKeepingPrintHandle() {
        if (checkDataInput()) {
            List<ClassWorkSheetSelected> listWorkSheet = new ArrayList();
            for (int i = 0; i < tblWorkSheet.getItems().size()-1; i++) {
                ClassWorkSheetSelected workSheetSelected = tblWorkSheet.getItems().get(i);
                if (workSheetSelected.getIsSelected() == true) {
                    listWorkSheet.add(workSheetSelected);
                }
            }

            printWorkSheetHouseKeeping(listWorkSheet);
            printWorkSheetHouseKeepingSaveHandle();
        } else {
            ClassMessage.showWarningInputDataMessage(errDataInput, roomStatusController.dialogStageWorkSheet);
        }
    }

    private void printWorkSheetHouseKeeping(List<ClassWorkSheetSelected> list) {
        List<ClassPrintWorkSheetHouseKeepingDetail> listWorkSheetHouseKeepingDetail = new ArrayList();
        List<ClassPrintWorkSheetHouseKeeping> listWorkSheetHouseKeeping = new ArrayList();
        ClassPrintWorkSheetHouseKeeping workSheetHouseKeeping = new ClassPrintWorkSheetHouseKeeping();
        workSheetHouseKeeping.setNamaChecker(cbpEmployee.getValue().getTblPeople().getFullName());

        for (ClassWorkSheetSelected getWorkSheet : list) {
            System.out.println("room :"+getWorkSheet.getRoomStatus().getDataRoom().getRoomName());
            ClassPrintWorkSheetHouseKeepingDetail workSheetHouseKeepingDetail = new ClassPrintWorkSheetHouseKeepingDetail();
            workSheetHouseKeepingDetail.setNoKamar(getWorkSheet.getRoomStatus().getDataRoom().getRoomName());
            workSheetHouseKeepingDetail.setTipeKamar(getWorkSheet.getRoomStatus().getDataRoom().getTblRoomType().getRoomTypeName());
            workSheetHouseKeepingDetail.setBangunan(getWorkSheet.getRoomStatus().getDataRoom().getTblLocation().getTblBuilding().getBuildingName());
            workSheetHouseKeepingDetail.setLantai(getWorkSheet.getRoomStatus().getDataRoom().getTblLocation().getTblFloor().getFloorName());
            workSheetHouseKeepingDetail.setNoReservasi("-");
            workSheetHouseKeepingDetail.setNamaTamu("-");
            workSheetHouseKeepingDetail.setStatusKamar(getWorkSheet.getRoomStatus().getDataRoom().getRefRoomStatus().getStatusName());
            workSheetHouseKeepingDetail.setCheckIn("-");
            workSheetHouseKeepingDetail.setCheckOut("-");
            workSheetHouseKeepingDetail.setBarang(getDataItem(getWorkSheet));
            if (getWorkSheet.getRoomStatus().getDataPreviousRRTD() != null) {
                //   workSheetHouseKeepingDetail.setJamKeluar(new SimpleDateFormat("HH:mm").format(getWorkSheet.getRoomStatus().getDataPreviousRRTD().getTblReservationCheckInOut().getCheckInDateTime()));
                //   workSheetHouseKeepingDetail.setJamMasuk(new SimpleDateFormat("HH:mm").format(getWorkSheet.getRoomStatus().getDataPreviousRRTD().getCheckOutDateTime()));
                workSheetHouseKeepingDetail.setCheckIn(new SimpleDateFormat("dd MMM yyyy", new Locale("id")).format(getWorkSheet.getRoomStatus().getDataPreviousRRTD().getCheckInDateTime()));
                workSheetHouseKeepingDetail.setCheckOut(new SimpleDateFormat("dd MMM yyyy", new Locale("id")).format(getWorkSheet.getRoomStatus().getDataPreviousRRTD().getCheckOutDateTime()));
                workSheetHouseKeepingDetail.setNamaTamu(getWorkSheet.getRoomStatus().getDataPreviousRRTD().getTblReservation().getTblCustomer().getTblPeople().getFullName());
                workSheetHouseKeepingDetail.setNoReservasi(getWorkSheet.getRoomStatus().getDataPreviousRRTD().getTblReservation().getCodeReservation());
                //   workSheetHouseKeepingDetail.setTanggalDatang(new SimpleDateFormat("dd MMM yyyy").format(getWorkSheet.getRoomStatus().getDataPreviousRRTD().getTblReservation().getArrivalTime()));
                //   workSheetHouseKeepingDetail.setTanggalPergi(new SimpleDateFormat("dd MMM yyyy").format(getWorkSheet.getRoomStatus().getDataPreviousRRTD().getTblReservation().getDepartureTime()));

            }

            if (getWorkSheet.getRoomStatus().getDataNextRRTD() != null && getWorkSheet.getRoomStatus().getDataPreviousRRTD() == null) {
                //   workSheetHouseKeepingDetail.setJamKeluar(new SimpleDateFormat("HH:mm").format(getWorkSheet.getRoomStatus().getDataNextRRTD().getTblReservationCheckInOut().getCheckInDateTime()));
                //   workSheetHouseKeepingDetail.setJamMasuk(new SimpleDateFormat("HH:mm").format(getWorkSheet.getRoomStatus().getDataNextRRTD().getCheckOutDateTime()));
                workSheetHouseKeepingDetail.setCheckIn(new SimpleDateFormat("dd MMM yyyy", new Locale("id")).format(getWorkSheet.getRoomStatus().getDataNextRRTD().getCheckInDateTime()));
                workSheetHouseKeepingDetail.setCheckOut(new SimpleDateFormat("dd MMM yyyy", new Locale("id")).format(getWorkSheet.getRoomStatus().getDataNextRRTD().getCheckOutDateTime()));
                workSheetHouseKeepingDetail.setNamaTamu(getWorkSheet.getRoomStatus().getDataNextRRTD().getTblReservation().getTblCustomer().getTblPeople().getFullName());
                workSheetHouseKeepingDetail.setNoReservasi(getWorkSheet.getRoomStatus().getDataNextRRTD().getTblReservation().getCodeReservation());
                //   workSheetHouseKeepingDetail.setTanggalDatang(new SimpleDateFormat("dd MMM yyyy").format(getWorkSheet.getRoomStatus().getDataNextRRTD().getTblReservation().getArrivalTime()));
                //   workSheetHouseKeepingDetail.setTanggalPergi(new SimpleDateFormat("dd MMM yyyy").format(getWorkSheet.getRoomStatus().getDataNextRRTD().getTblReservation().getDepartureTime()));

            }

            if (getWorkSheet.getRoomStatus().getDataPreviousRRTD() != null && getWorkSheet.getRoomStatus().getDataNextRRTD() != null) {
                //    workSheetHouseKeepingDetail.setJamKeluar(new SimpleDateFormat("HH:mm").format(getWorkSheet.getRoomStatus().getDataPreviousRRTD().getTblReservationCheckInOut().getCheckInDateTime()));
                //    workSheetHouseKeepingDetail.setJamMasuk(new SimpleDateFormat("HH:mm").format(getWorkSheet.getRoomStatus().getDataPreviousRRTD().getCheckOutDateTime()));
                workSheetHouseKeepingDetail.setCheckIn(new SimpleDateFormat("dd MMM yyyy", new Locale("id")).format(getWorkSheet.getRoomStatus().getDataPreviousRRTD().getCheckInDateTime()));
                workSheetHouseKeepingDetail.setCheckOut(new SimpleDateFormat("dd MMM yyyy", new Locale("id")).format(getWorkSheet.getRoomStatus().getDataPreviousRRTD().getCheckOutDateTime()));
                workSheetHouseKeepingDetail.setNamaTamu(getWorkSheet.getRoomStatus().getDataPreviousRRTD().getTblReservation().getTblCustomer().getTblPeople().getFullName());
                workSheetHouseKeepingDetail.setNoReservasi(getWorkSheet.getRoomStatus().getDataPreviousRRTD().getTblReservation().getCodeReservation());
                //   workSheetHouseKeepingDetail.setTanggalDatang(new SimpleDateFormat("dd MMM yyyy").format(getWorkSheet.getRoomStatus().getDataPreviousRRTD().getTblReservation().getArrivalTime()));
                //   workSheetHouseKeepingDetail.setTanggalPergi(new SimpleDateFormat("dd MMM yyyy").format(getWorkSheet.getRoomStatus().getDataPreviousRRTD().getTblReservation().getDepartureTime()));
            }
            listWorkSheetHouseKeepingDetail.add(workSheetHouseKeepingDetail);
        }
        workSheetHouseKeeping.setListWorkSheetHouseKeepingDetail(listWorkSheetHouseKeepingDetail);
        listWorkSheetHouseKeeping.add(workSheetHouseKeeping);
        String houseKeeping = workSheetHouseKeeping.getNamaChecker() + new SimpleDateFormat("dd MMMM yyyy").format(Date.valueOf(LocalDate.now()));
        ClassPrinter.printWorkSheetHouseKeeping(listWorkSheetHouseKeeping, houseKeeping);
    }
    
    private String getDataItem(ClassWorkSheetSelected workSheetSelected){
      System.out.println("hsl worksheet :"+workSheetSelected.getRoomStatus().getDataRoom().getRoomName());
      List<ClassDataRoomCheck>list = new ArrayList();
      List<TblRoomTypeItem>listRoomTypeItem = roomStatusController.getService().getAllDataRoomTypeItemByIDRoomType(workSheetSelected.getRoomStatus().getDataRoom().getTblRoomType().getIdroomType());
      System.out.println("size :"+listRoomTypeItem.size());
      List<TblReservationAdditionalItem>listReservationAdditionalItem = new ArrayList();
     // List<TblItem>listItem = new ArrayList(); 
      ClassDataRoomCheck dataRoomCheck = null;
      String listItem = "";
       if(workSheetSelected.getRoomStatus().getDataPreviousRRTD()!=null){
           System.out.println("previous");
         listReservationAdditionalItem = roomStatusController.getService().getAllDataReservationAdditionalItemByIDReservationRoomTypeDetailAdditionalDateAndBillType(workSheetSelected.getRoomStatus().getDataPreviousRRTD().getIddetail(),Date.valueOf(workSheetSelected.getRoomStatus().getPreviousDate()));
        }
       else if(workSheetSelected.getRoomStatus().getDataNextRRTD()!=null){
         // System.out.println("next : " + roomStatus.getDataNextRRTD().getIddetail() + " - " + roomStatus.getNextDate());
          listReservationAdditionalItem = roomStatusController.getService().getAllDataReservationAdditionalItemByIDReservationRoomTypeDetailAdditionalDateAndBillType(workSheetSelected.getRoomStatus().getDataNextRRTD().getIddetail(),Date.valueOf(workSheetSelected.getRoomStatus().getNextDate()));
         //  System.out.println("size : " + listReservationAdditionalItem.size());
        }
        
        for(TblRoomTypeItem roomTypeItem : listRoomTypeItem){
           dataRoomCheck = new ClassDataRoomCheck();
           dataRoomCheck.setItem(roomTypeItem.getTblItem());
           dataRoomCheck.setQtySysItem(roomTypeItem.getItemQuantity());
         //  dataRoomCheck.setQtyRealItem(roomTypeItem.getItemQuantity());
           dataRoomCheck.setCodeWorkSheet(roomTypeItem.getTblItem().getCodeWorkSheet());
           
         //  listItem+=roomTypeItem.getTblItem().getCodeWorkSheet()+"="+roomTypeItem.getItemQuantity();
           list.add(dataRoomCheck);
        }
        
        System.out.println("Item :"+list.size());
        for(TblReservationAdditionalItem additionalItem : listReservationAdditionalItem){
           boolean found = false;
           for(ClassDataRoomCheck getDataRoomCheck : list){
               if(additionalItem.getTblItem().getIditem()==getDataRoomCheck.getItem().getIditem()){
                  getDataRoomCheck.setQtySysItem(getDataRoomCheck.getQtySysItem().add(additionalItem.getItemQuantity()));
   //               getDataRoomCheck.setQtyRealItem(getDataRoomCheck.getQtySysItem());
                  found = true;       
               }
            }
           
           if(found==false){
             dataRoomCheck = new ClassDataRoomCheck();
             dataRoomCheck.setItem(additionalItem.getTblItem());
             dataRoomCheck.setCodeWorkSheet(additionalItem.getTblItem().getCodeWorkSheet());
             dataRoomCheck.setQtySysItem(additionalItem.getItemQuantity());
     //        dataRoomCheck.setQtyRealItem(dataRoomCheck.getQtySysItem());
             list.add(dataRoomCheck);
            }
        }
        
        
        for(int i = 0; i<list.size();i++){
           String temp = list.get(i).getCodeWorkSheet()+"="+ClassFormatter.decimalFormat.cFormat(list.get(i).getQtySysItem());
           boolean check = false;
           if(temp.length()<=7){
              listItem+=temp; 
            }
           
           if(i<list.size()-1){
               if(((i+1)%3) == 0){
                 listItem+="\n";
                }
               else{
                 listItem+="\t";
               }
           }
          
           
        } 
       return listItem;
    }
    
    private List<TblRoomCheckHouseKeepingAttendance> getDataHouseKeepingAttendance() {
//       List<TblRoomCheckHouseKeepingAttendance>list = roomStatusController.getService().getAllHouseKeepingAttendance();
        List<TblRoomCheckHouseKeepingAttendance> listHouseKeepingAttendance = new ArrayList();
        for (ClassWorkSheetSelected getWorkSheet : tblWorkSheet.getItems()) {
            if (getWorkSheet.getIsSelected() == true) {
             TblRoomCheckHouseKeepingAttendance roomCheckHouseKeepingAttendance = new TblRoomCheckHouseKeepingAttendance();
//               if(list.size()>0){
//                 for(TblRoomCheckHouseKeepingAttendance getRoomCheckHouseKeepingAttendance : list){
//                    if(getRoomCheckHouseKeepingAttendance.getTblRoom().getIdroom()==getWorkSheet.getRoomStatus().getDataRoom().getIdroom()){
//                      roomCheckHouseKeepingAttendance = getRoomCheckHouseKeepingAttendance;
//                      roomCheckHouseKeepingAttendance.setTblEmployeeByIdchecker(cbpEmployee.getValue());
//                      break;
//                    }
//                    else{
//                      roomCheckHouseKeepingAttendance.setTblEmployeeByIdchecker(cbpEmployee.getValue());
//                      roomCheckHouseKeepingAttendance.setTblRoom(getWorkSheet.getRoomStatus().getDataRoom());
//                    }
//                 }
//               }
//               else{
                roomCheckHouseKeepingAttendance.setTblEmployeeByIdchecker(cbpEmployee.getValue());
                roomCheckHouseKeepingAttendance.setTblRoom(getWorkSheet.getRoomStatus().getDataRoom());
                roomCheckHouseKeepingAttendance.setRefRoomStatusByRoomStatusBefore(getWorkSheet.getRoomStatus().getDataRoom().getRefRoomStatus());
//               }
                listHouseKeepingAttendance.add(roomCheckHouseKeepingAttendance);
            }
        }
        return listHouseKeepingAttendance;
    }
    
    private void printWorkSheetHouseKeepingSaveHandle() {
        List<TblRoomCheckHouseKeepingAttendance> list = getDataHouseKeepingAttendance();
        roomStatusController.getService().insertWorkSheetAttendance(list);
        //refresh data table work-sheet
        refreshDataTableWorkSheetKeepingAttendance();
    }

    private String errDataInput;

    private boolean checkDataInput() {
        errDataInput = "";
        boolean check = true;

        if (cbpEmployee.getValue() == null) {
            errDataInput += "Nama Pemeriksa :" + ClassMessage.defaultErrorNullValueMessage;
            check = false;
        }

        return check;
    }

    public void refreshDataTableWorkSheetKeepingAttendance() {
        tblWorkSheet.setItems(FXCollections.observableArrayList(loadAllDataWorkSheet()));
        cft.refreshFilterItems(tblWorkSheet.getItems());
    }
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initTablePrintWorkSheet();
        initForm();
        // throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public PrintWorkSheetHouseKeepingController(RoomStatusController roomStatusController) {
        this.roomStatusController = roomStatusController;
    }

    private final RoomStatusController roomStatusController;
}
