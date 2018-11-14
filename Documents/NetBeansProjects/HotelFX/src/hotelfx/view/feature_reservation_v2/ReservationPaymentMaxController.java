/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hotelfx.view.feature_reservation_v2;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXTimePicker;
import hotelfx.helper.ClassDataEmail;
import hotelfx.helper.ClassFolderManager;
import hotelfx.helper.ClassFormatter;
import hotelfx.helper.ClassMessage;
import hotelfx.helper.ClassPrinter;
import hotelfx.helper.ClassSendingEmail;
import hotelfx.helper.ClassSession;
import hotelfx.helper.ClassViewSetting;
import hotelfx.helper.PrintModel.ClassPrintBill;
import hotelfx.helper.PrintModel.ClassPrintBillDetail;
import hotelfx.persistence.model.SysDataHardCode;
import hotelfx.persistence.model.TblReservation;
import hotelfx.persistence.model.TblReservationBill;
import hotelfx.persistence.model.TblReservationRoomTypeDetail;
import hotelfx.view.feature_reservation_v2.ReservationController.ReservationBillPaymentDetail;
import java.net.URL;
import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

/**
 *
 * @author Andreas
 */
public class ReservationPaymentMaxController implements Initializable{
    @FXML
    private JFXDatePicker dpDate;
    @FXML
    private JFXTimePicker tpTime;
    @FXML
    private JFXButton btnSend;
    @FXML
    private JFXButton btnCancel;
    
    private void initFormReservationPaymentMax(){
      dpDate.setValue(getConfirmationDate(reservationController.selectedData));
      tpTime.setValue(LocalTime.of(00,00));
       
      btnSend.setOnAction((e)->{
          reservationPaymentMaxController();
      });
      
      btnCancel.setOnAction((e)->{
         reservationController.paymentMaxStage.close();
      });
      
      initDateFormat();
    }
    
    private void initDateFormat(){
      ClassFormatter.setDatePickersPattern("dd MMM yyyy",dpDate);
      ClassFormatter.setDatePickersEnableDateFrom(dpDate.getValue(),dpDate);
      ClassViewSetting.setImportantField(dpDate,tpTime);
    }
    
    public void reservationPaymentMaxController(){
       
       if(reservationController.selectedData.getRefReservationStatus().getIdstatus()==1){
          if(checkDataInput()){
              Alert alert = ClassMessage.showConfirmationSendingEmail(null, null);
               if(alert.getResult()==ButtonType.OK){
                    ClassDataEmail dataEmail = new ClassDataEmail();
                      //  dataEmail.setDetail(text);
                    dataEmail.setFilePath(getFileNameBill());
                    dataEmail.setReceived(reservationController.selectedData.getTblCustomer().getTblPeople().getEmail());
                    dataEmail.setSender(reservationController.getFReservationManager().getDataSysDataHardCode((long)16).getDataHardCodeValue());

                    detailEmail(reservationController.selectedData.getRefReservationStatus().getIdstatus(),dataEmail);

                     if(ClassSendingEmail.sendEmail(dataEmail)){
                       ClassMessage.showSucceedSendingEmailDataMessage("",null);
                       reservationController.paymentMaxStage.close();
                     }
                     else{
                       ClassMessage.showFailedSendingEmailDataMessage("",null);
                     }
                } 
           }
        }
       else if(reservationController.selectedData.getRefReservationStatus().getIdstatus()==2){
            Alert alert = ClassMessage.showConfirmationSendingEmail(null, null);
            if(alert.getResult()==ButtonType.OK){
               ClassDataEmail dataEmail = new ClassDataEmail();
                 //  dataEmail.setDetail(text);
               dataEmail.setFilePath(getFileNameBill());
               dataEmail.setReceived(reservationController.selectedData.getTblCustomer().getTblPeople().getEmail());
               dataEmail.setSender(reservationController.getFReservationManager().getDataSysDataHardCode((long)16).getDataHardCodeValue());

               detailEmail(reservationController.selectedData.getRefReservationStatus().getIdstatus(),dataEmail);

                System.out.println("data mail" + dataEmail);
               
                if(ClassSendingEmail.sendEmail(dataEmail)){
                  ClassMessage.showSucceedSendingEmailDataMessage("",null);
                  reservationController.paymentMaxStage.close();
                }
                else{
                  ClassMessage.showFailedSendingEmailDataMessage("",null);
                }
            }
       }
      
     
    }
    
    private void detailEmail(int type,ClassDataEmail dataEmail){
        String text = "";
        if(type==1){
         dataEmail.setSubject("Invoice #"+reservationController.dataReservationBill.getCodeBill());
         dataEmail.setFileName("INV#"+reservationController.dataReservationBill.getCodeBill());
         text = "<html>\n" +
                "<head>\n" +
                "<meta http-equiv=\"Content-Type\" content=\"text/html; charset=iso-8859-1\">\n" +
                "<title>Untitled Document</title></head>\n" +"\n" +
                "<body>\n" +
                "<blockquote>\n" +
                "<p style=\"color:#000000; font-family:Arial; font-size:12px\">Dear Mr./Ms."+reservationController.selectedData.getTblCustomer().getTblPeople().getFullName()+",</p>\n" +
                "<h4 style=\"color:#000000; font-family:Arial; font-size:14px\">Warm greetings from "+reservationController.getFReservationManager().getDataSysDataHardCode((long)12).getDataHardCodeValue()+"..</h4> \n" +
                "<p style=\"color:#000000; font-family:Arial; font-size:12px\">Thank you for your trust to stay in "+reservationController.getFReservationManager().getDataSysDataHardCode((long)12).getDataHardCodeValue()+".<br>\n" +
                "Here, we pleased to attach reservation invoice as your reference. <br><br>\n" +
                "Please send payment receipt latest on "+new SimpleDateFormat("MMM dd,YYYY",new Locale("en")).format(Date.valueOf(dpDate.getValue()))+" before "+new SimpleDateFormat("hh:mm a",new Locale("id")).format(Time.valueOf(tpTime.getValue()))+" , by including invoice number printed on reservation invoice. .<br>\n" +
                "     <br> 	\n" +
                "Thank you for your attention. If you have any question, don't hesitate to contact us.</p>\n" +
                " <br><br><br>\n" +
                " Regrads, <br>\n" +
                "  <br>\n" + ClassSession.currentUser.getTblEmployeeByIdemployee().getTblPeople().getFullName()+
                "  <br>\n" + (ClassSession.currentUser.getTblEmployeeByIdemployee().getTblJob()==null ? "-" :ClassSession.currentUser.getTblEmployeeByIdemployee().getTblJob().getJobName())+
                "  </p>\n" +
                "  <p style=\"color:#000000; font-family:Arial; font-size:12px\">_________________________________________________________</p>\n" +
                "  <pre style=\"font-family:Courier New; font-size:12px; color:#000000\"><b style=\"color:#000000; font-size:16px; font-family:Courier New;\">"+reservationController.getFReservationManager().getDataSysDataHardCode((long)12).getDataHardCodeValue()+"</b>\n"
                +reservationController.getFReservationManager().getDataSysDataHardCode((long)13).getDataHardCodeValue()+"<br>OFFICE : "+reservationController.getFReservationManager().getDataSysDataHardCode((long)14).getDataHardCodeValue()+"<br>EMAIL  : "+reservationController.getFReservationManager().getDataSysDataHardCode((long)16).getDataHardCodeValue()+"</pre>\n" +
                "</blockquote>\n" +
                "</body>\n" +
                "</html>";
           }
           else if(reservationController.selectedData.getRefReservationStatus().getIdstatus()==2){
              dataEmail.setSubject("Confirmation Payment");
              dataEmail.setFileName("INV#"+reservationController.dataReservationBill.getCodeBill());
              text = "<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Transitional//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd\">\n" +
                     "<html xmlns=\"http://www.w3.org/1999/xhtml\">\n" +
                     "<head>\n" +
                     "<meta http-equiv=\"Content-Type\" content=\"text/html; charset=iso-8859-1\" />\n" +
                     "<title>Untitled Document</title>\n" +
                     "</head>\n" +
                     "\n" +
                     "<body>\n" +
                     "<blockquote>\n" +
                    "  <p style=\"color:#000000; font-family:Arial; font-size:12px\">Dear Mr./Ms. "+reservationController.selectedData.getTblCustomer().getTblPeople().getFullName()+",</p>\n" +
                    "  <p style=\"color:#000000; font-family:Arial; font-size:12px\">Your payment with invoice number #"+reservationController.dataReservationBill.getCodeBill()+" has been received.<br>Here your reservation code :</p>\n" +
                    "  <table width=\"326\" border=\"1\">\n" +
                    "    <tr>\n" +
                    "    <th width=\"316\" height=\"26\">"+reservationController.selectedData.getCodeReservation()+"</th>\n" +
                    "  </tr>\n" +
                    "</table>\n" +
                    "\n" +
                    "  <p style=\"color:#000000; font-family:Arial; font-size:12px\">and we have included details of the payment bill in the attachment.</p>\n" +
                    "  <p style=\"color:#000000; font-family:Arial; font-size:12px\">Thank you for choosing "+reservationController.getFReservationManager().getDataSysDataHardCode((long)12).getDataHardCodeValue()+" to stay. See you at "+reservationController.getFReservationManager().getDataSysDataHardCode((long)12).getDataHardCodeValue()+". <br />\n" +
                    "  </p>\n" +
                    "  <p style=\"color:#000000; font-family:Arial; font-size:12px\"><br />\n" +
                    "    Regrads, <br />\n" +
                    "    <br />\n" +
                    ClassSession.currentUser.getTblEmployeeByIdemployee().getTblPeople().getFullName()+ "<br />\n" +
                    (ClassSession.currentUser.getTblEmployeeByIdemployee().getTblJob()==null ? "-" :ClassSession.currentUser.getTblEmployeeByIdemployee().getTblJob().getJobName())+"</p>\n" +
                    "  <p style=\"color:#000000; font-family:Arial; font-size:12px\">_________________________________________________________</p>\n" +
                    "  <pre style=\"font-family:Courier New; font-size:12px; color:#000000\"><b style=\"color:#000000; font-size:16px; font-family:Courier New;\">"+reservationController.getFReservationManager().getDataSysDataHardCode((long)12).getDataHardCodeValue()+"</b>\n" +
                    reservationController.getFReservationManager().getDataSysDataHardCode((long)13).getDataHardCodeValue()+"<br />OFFICE : "+reservationController.getFReservationManager().getDataSysDataHardCode((long)14).getDataHardCodeValue()+"<br />EMAIL  : "+reservationController.getFReservationManager().getDataSysDataHardCode((long)16).getDataHardCodeValue()+"</pre>\n" +
                    "</blockquote>\n" +
                    "</body>\n" +
                    "</html>"; 
           }
          dataEmail.setDetail(text);
    }
    
    private TblReservationBill getDataReservationBill(TblReservation reservation){
       List<TblReservationBill>list = reservationController.getFReservationManager().getAllDataReservationBill();
       TblReservationBill dataReservationBill = new TblReservationBill();
       for(TblReservationBill getReservationBill : list){
           if(getReservationBill.getTblReservation().getIdreservation()==reservation.getIdreservation()){
              dataReservationBill = getReservationBill;
           }
       }
      return dataReservationBill;
    }
    
    private String getFileNameBill(){
        List<ReservationBillPaymentDetail> list = reservationController.tableDataRCOTransaction.getTableView().getItems();
        List<ClassPrintBillDetail> listBillDetail = new ArrayList();
        List<ClassPrintBill> listBill = new ArrayList();

        String emailHotel = "";
        SysDataHardCode sdhHotelEmail = reservationController.getFReservationManager().getDataSysDataHardCode((long) 16);
        emailHotel = sdhHotelEmail.getDataHardCodeValue();
        String summaryHotel = "";
        SysDataHardCode sdhHotelSummary = reservationController.getFReservationManager().getDataSysDataHardCode((long) 17);
        summaryHotel = sdhHotelSummary.getDataHardCodeValue();
        String noteHotel = "";
        SysDataHardCode sdhHotelNote = reservationController.getFReservationManager().getDataSysDataHardCode((long) 18);
        noteHotel = sdhHotelNote.getDataHardCodeValue();

        for (ReservationBillPaymentDetail getBillDetail : list) {
            listBillDetail.add(getBillDetail.getBillDetail());
        }
        ClassPrintBill printBill = new ClassPrintBill();
        printBill.setArrivalDate(reservationController.selectedData.getArrivalTime() != null ? new SimpleDateFormat("dd MMM yyyy HH:mm", new Locale("id")).format(reservationController.selectedData.getArrivalTime()) : "-");
        printBill.setCashier(ClassSession.currentUser.getTblEmployeeByIdemployee().getCodeEmployee() + "-" + ClassSession.currentUser.getTblEmployeeByIdemployee().getTblPeople().getFullName());
        printBill.setCodeInvoice(reservationController.dataReservationBill.getCodeBill());
        printBill.setInvoiceDate(new SimpleDateFormat("dd MMMM yyyy", new Locale("id")).format(new java.sql.Date(reservationController.dataReservationBill.getCreateDate().getTime())));
        printBill.setDepartureDate(reservationController.selectedData.getDepartureTime() != null ? new SimpleDateFormat("dd MMM yyyy HH:mm", new Locale("id")).format(reservationController.selectedData.getDepartureTime()) : "-");
        printBill.setEmailHotel(emailHotel);
        printBill.setNote(noteHotel);
        printBill.setSummary(summaryHotel);
        printBill.setCustomerName(reservationController.selectedData.getTblCustomer().getTblPeople().getFullName());
        printBill.setListBonReservasiDetail(listBillDetail);

        listBill.add(printBill);
        return ClassPrinter.nameFileBillReservasi(listBill,reservationController.dataReservationBill);
    }
    
    private LocalDate getConfirmationDate(TblReservation reservation){
       List<TblReservationRoomTypeDetail>list = reservationController.getFReservationManager().getAllDataReservationRoomTypeDetailByIDReservation(reservation.getIdreservation());
       LocalDate dateTime = null;
       LocalDate checkInDate = null;
       SysDataHardCode getMinDay = reservationController.getFReservationManager().getDataSysDataHardCode((long)33);
        
       for(TblReservationRoomTypeDetail roomTypeDetail : list){
           checkInDate = LocalDate.of(roomTypeDetail.getCheckInDateTime().getYear()+1900,roomTypeDetail.getCheckInDateTime().getMonth()+1,roomTypeDetail.getCheckInDateTime().getDate());
           if(LocalDate.of(roomTypeDetail.getCheckInDateTime().getYear()+1900,roomTypeDetail.getCheckInDateTime().getMonth()+1,roomTypeDetail.getCheckInDateTime().getDate()).isBefore(checkInDate)){
             checkInDate = LocalDate.of(roomTypeDetail.getCheckInDateTime().getYear()+1900,roomTypeDetail.getCheckInDateTime().getMonth()+1,roomTypeDetail.getCheckInDateTime().getDate());
            }  
        }
      
      dateTime = checkInDate.minusDays(Integer.parseInt(getMinDay.getDataHardCodeValue()));
       
   //   LocalDate dateReservation = LocalDate.of(reservation.getReservationDate().getYear()+1900,reservation.getReservationDate().getMonth()+1,reservation.getReservationDate());
       if(reservation.getReservationDate().after(Date.valueOf(dateTime))){
         dateTime =LocalDate.of(reservation.getReservationDate().getYear()+1900,reservation.getReservationDate().getMonth()+1,reservation.getReservationDate().getDate()); 
       }
      return dateTime; 
    }
    
    String errDataInput;
    private boolean checkDataInput(){
      errDataInput = "";
      boolean check = true;
      
       if(dpDate.getValue()==null){
         errDataInput += "Tanggal : "+ClassMessage.defaultErrorNullValueMessage+"\n";
         check = false;
       }
       
       if(tpTime.getValue()==null){
         errDataInput+="Jam : "+ClassMessage.defaultErrorNullValueMessage+"\n";
         check = false;
       }
       
       return check;
    }
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        System.out.println("masuk!!!");
      initFormReservationPaymentMax();
       // throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    public ReservationPaymentMaxController(ReservationController reservationController){
       this.reservationController = reservationController;
    }
    
    private final ReservationController reservationController; 
}
