/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hotelfx.view.feature_schedule.employee_schedule;

import com.jfoenix.controls.JFXButton;
import hotelfx.HotelFX;
import hotelfx.helper.ClassFormatter;
import hotelfx.persistence.model.SysCalendar;
import hotelfx.persistence.model.SysDataHardCode;
import hotelfx.persistence.model.TblCalendarEmployeeSchedule;
import hotelfx.persistence.service.FScheduleManager;
import hotelfx.view.feature_schedule.FeatureScheduleController;
import insidefx.undecorator.Undecorator;
import java.io.IOException;
import java.net.URL;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.Month;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import static java.time.temporal.ChronoUnit.WEEKS;
import java.time.temporal.WeekFields;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.binding.Bindings;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.css.PseudoClass;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Region;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

/**
 *
 * @author Andreas
 */
public class EmployeeScheduleController implements Initializable{

    //calender handle
    @FXML
    Label lblSelectedMonth;
    @FXML
    JFXButton btnPrev;
    @FXML
    JFXButton btnNext;
    @FXML
    JFXButton btnPrintSchedule;
    @FXML
    GridPane gpDates;
    @FXML
    GridPane gpWeeks;
    private ObjectProperty<YearMonth>month = new SimpleObjectProperty();
    private ObjectProperty<YearMonth>tempMonth = new SimpleObjectProperty();
    private ObjectProperty<Locale>locale = new SimpleObjectProperty<>(Locale.getDefault());
    
    private void initCalenderButton(){
      month.addListener((obs,oldVal,newVal)->{
        showGridDates();  
      });
      
      btnPrev.setTooltip(new Tooltip("Bulan Sebelumnya"));
      btnPrev.setOnAction((e)->{
         if(month.get().isAfter(YearMonth.of(LocalDate.now().getYear(),1)))
         {
           month.set(month.get().minusMonths(1));
         }
        });
      
      btnNext.setTooltip(new Tooltip("Bulan Berikutnya"));
      btnNext.setOnAction((e)->
      {
          int defaultNextYear = 0;
          SysDataHardCode sdhDefaultEmployeeScheduleNextYear = parentController.getScheduleManager().getDataSysDataHardCode(11); //DefaultEmployeeScheduleNextYear = '11'
          if(sdhDefaultEmployeeScheduleNextYear != null
                  && sdhDefaultEmployeeScheduleNextYear.getDataHardCodeValue() != null){
              defaultNextYear = Integer.parseInt(sdhDefaultEmployeeScheduleNextYear.getDataHardCodeValue());
          }
        if(month.get().isBefore(YearMonth.of(LocalDate.now().getYear()+defaultNextYear,12)))
        {
          month.set(month.get().plusMonths(1));
        }
      });
      
      month.set(YearMonth.from(LocalDate.now()));
      lblSelectedMonth.textProperty().bind(Bindings.createStringBinding(()->ClassFormatter.convertMonthToIndonesian(month.getValue().getMonthValue())+"/"+month.get().getYear(),month));
      
      btnPrintSchedule.setOnAction((e)->{
         showEmployeeSchedulePrintDialog(); 
      });
    }
    
    private void showGridDates(){
      gpDates.getChildren().clear();
      WeekFields weekFields = WeekFields.of(locale.get());
      LocalDate firstDate = month.get().atDay(1);
      System.out.println("pake local date:"+firstDate.getDayOfWeek().getValue());
      int firstDayDateOfWeek = firstDate.get(weekFields.dayOfWeek());
       LocalDate firstDisplayDateInCalendar = firstDate.minusDays(firstDayDateOfWeek-1);
        System.out.println("pake week Fields:"+firstDayDateOfWeek);
      LocalDate lastDate = month.get().atEndOfMonth();
      //int lastDayDateOfWeek = lastDate.getDayOfWeek().getValue();
      int lastDayDateOfWeek = lastDate.get(weekFields.dayOfWeek());
        System.out.println("last Day Date of week:"+lastDayDateOfWeek);
      LocalDate lastDisplayDateInCalendar = lastDate.plusDays(7-lastDayDateOfWeek);
      
      
      while(WEEKS.between(firstDisplayDateInCalendar,lastDisplayDateInCalendar)<5)
      {
        lastDisplayDateInCalendar = lastDisplayDateInCalendar.plusDays(7);
      }
      
      List<SysCalendar>dataCalendar =  parentController.getScheduleManager().getAllCalendar(Date.valueOf(firstDisplayDateInCalendar),Date.valueOf(lastDisplayDateInCalendar),null);
     
      PseudoClass weekendPseudoClass = PseudoClass.getPseudoClass("weekend");
      String cellClass = "month-cell";
      
      for(SysCalendar getDataCalendar : dataCalendar){
        BorderPane cell  = new BorderPane();
        cell.getStyleClass().add(cellClass);
        cell.pseudoClassStateChanged(weekendPseudoClass,getDataCalendar.getCalendarDate().getDay()==0 || getDataCalendar.getCalendarDate().getDay()==6);
        Label label = new Label(String.format("%td",getDataCalendar.getCalendarDate())+"/"+String.format("%tm",getDataCalendar.getCalendarDate())+"/"+String.format("%tY",getDataCalendar.getCalendarDate()));
        label.getStyleClass().add("date");
        //cell.setRight(label);
        LocalDate date = ((Date)getDataCalendar.getCalendarDate()).toLocalDate();
        //label.setDisable(date.isBefore(firstDate)||date.isAfter(lastDate));
        cell.setDisable(date.isBefore(firstDate) || date.isAfter(lastDate));
        cell.setOnMouseClicked((e)->{
           if(e.getClickCount()==2){
             //System.out.println("Hasil:"+getDataCalendar.getCalendarDate());
             cell.setStyle("-fx-background-color:gold");
             createDataEmployeeScheduleHandle(getDataCalendar);
               //System.out.println("Hasil:"+selectedData.getSysCalendar().getCalendarDate());
             showEmployeeScheduleDialog();
           }
        });
        int dayOfWeek = date.get(weekFields.dayOfWeek());
        int daySinceFirstDisplayDate = (int) firstDisplayDateInCalendar.until(date,ChronoUnit.DAYS);
        int weeksSinceFirstDisplayDate = daySinceFirstDisplayDate/7;
        
        cell.setRight(label);
        BorderPane.setMargin(label, new Insets(4, 4, 4, 4));
        
        gpDates.add(cell,dayOfWeek-1, weeksSinceFirstDisplayDate);
      
      }
      /* for(LocalDate date=firstDisplayDateInCalendar;!date.isAfter(lastDisplayDateInCalendar);date=date.plusDays(1))
      {
        BorderPane cell = new BorderPane();
        Label labelDate = new Label(String.format("%ta",date)+" "+String.format("%te",date)+"/"+String.format("%tm",date)+"/"+String.format("%ty",date));
        int dayOfWeek = date.get(weekFields.dayOfWeek());
        int daySinceFirstDisplayDate = (int) firstDisplayDateInCalendar.until(date,ChronoUnit.DAYS);
        int weeksSinceFirstDisplayDate = daySinceFirstDisplayDate/7;
        cell.setTop(labelDate);
        
        labelDate.setDisable(date.isBefore(firstDate)||date.isAfter(lastDate));
        
        if(!date.isBefore(firstDate)&&!date.isAfter(lastDate)){
         SysCalendar dataCalendar = new SysCalendar();
         dataCalendar.setCalendarDate(Date.valueOf(date));
         parentController.getScheduleManager().insertCalendar(dataCalendar);
        }
        
        cell.setOnMouseClicked((e)->{
          if(e.getClickCount()==2)
          {
            Date x = Date.valueOf(labelDate.getText());
            System.out.println(">>"+x.getDate());
          }
        });
        
        BorderPane.setMargin(labelDate, new Insets(4, 4, 4, 4));
        
        gpDates.add(cell, dayOfWeek-1, weeksSinceFirstDisplayDate);
        gpDates.setGridLinesVisible(true);
      }*/
    }
    
    public Stage dialogStagePrint;
    private void showEmployeeSchedulePrintDialog(){
      try{
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(HotelFX.class.getResource("view/feature_schedule/employee_schedule/EmployeeSchedulePrintDialog.fxml"));
        EmployeeSchedulePrintDialogController employeeSchedulePrintDialogController = new EmployeeSchedulePrintDialogController(this);
        loader.setController(employeeSchedulePrintDialogController);
        
        Region page = loader.load();
        
        dialogStagePrint = new Stage();
        dialogStagePrint.initModality(Modality.WINDOW_MODAL);
        dialogStagePrint.initOwner(HotelFX.primaryStage);
        
        Undecorator undecorator = new Undecorator(dialogStagePrint,page);
        undecorator.getStylesheets().add("skin/undecorator.css");
        undecorator.getMenuButton().setVisible(false);
        undecorator.getMaximizeButton().setVisible(false);
        undecorator.getMinimizeButton().setVisible(false);
        undecorator.getFullScreenButton().setVisible(false);
        undecorator.getCloseButton().setVisible(false);
        
        Scene scene = new Scene(undecorator);
        scene.setFill(Color.TRANSPARENT);
        
        dialogStagePrint.initStyle(StageStyle.TRANSPARENT);
        dialogStagePrint.setScene(scene);
        dialogStagePrint.setResizable(false);
        
        dialogStagePrint.showAndWait();
      }
      catch (IOException ex) {
            Logger.getLogger(EmployeeScheduleController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public Stage dialogStage;
    private void showEmployeeScheduleDialog(){
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(HotelFX.class.getResource("view/feature_schedule/employee_schedule/EmployeeScheduleInputDialog.fxml"));
            EmployeeScheduleInputDialogController employeeScheduleInputDialogController = new EmployeeScheduleInputDialogController(this);
            loader.setController(employeeScheduleInputDialogController);
            
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
            Logger.getLogger(EmployeeScheduleController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public TblCalendarEmployeeSchedule selectedData;
    
    private SysCalendar calendar;
    
    private void createDataEmployeeScheduleHandle(SysCalendar calendar)
    {
      selectedData = new TblCalendarEmployeeSchedule();
      selectedData.setSysCalendar(calendar);
    }
    
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
      initCalenderButton();
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    public EmployeeScheduleController(FeatureScheduleController parentController)
    {
      this.parentController = parentController;
    }
    private final FeatureScheduleController parentController;
    
    public FScheduleManager getService(){
        return parentController.getScheduleManager();
    }
}
