/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hotelfx.persistence.service;

//import hotelfx.helper.ClassDataSchedule;
import hotelfx.persistence.model.RefEmployeeAttendanceEndStatus;
import hotelfx.persistence.model.RefEmployeeAttendanceStartStatus;
import hotelfx.persistence.model.RefEmployeeAttendanceStatus;
import hotelfx.persistence.model.RefEmployeeType;
import hotelfx.persistence.model.SysCalendar;
import hotelfx.persistence.model.TblCalendarEmployeeAttendance;
import hotelfx.persistence.model.TblCalendarEmployeeLeave;
import hotelfx.persistence.model.TblCalendarEmployeeOvertime;
import hotelfx.persistence.model.TblCalendarEmployeeSchedule;
import hotelfx.persistence.model.TblEmployee;
import java.sql.Date;
import java.util.List;

/**
 *
 * @author Andreas
 */
public interface FAttendanceManager {
    public boolean updateCodeFinger(TblEmployee employee);
    public boolean insertDataAttendance(List<TblCalendarEmployeeAttendance>listAttendance);
    public boolean updateDataAttendance(TblCalendarEmployeeAttendance employeeAttendance);
    public List<SysCalendar>getSysCalendarByDate(Date startPeriode,Date endPeriode,Date date,String status);
    //public List<SysCalendar>getSysCalendarByDate(Date dateAttendance);
    public List<TblEmployee>getAllEmployeeByFingerPrint(String codeFingerPrint);
    public List<TblEmployee>getAllEmployee();
    public List<RefEmployeeType>getTypeEmployee();
    public List<TblCalendarEmployeeAttendance>getAllEmployeeAttendance(int id,int idList,Date startDate,Date endDate);
    // public List<TblCalendarEmployeeAttendance>getAllEmployeeAttendanceByIdEmployeeTypeAndOvertimePeriode(int id,boolean isOvertime,Date startDate,Date endDate);
    //public List<ClassDataSchedule>getAllDataScheduleByPeriode(Date startDate,Date endDate);
    //public List<TblCalendarEmployeeSchedule>getAllDataScheduleByPeriode(Date startDate,Date endDate);
    public List<TblCalendarEmployeeSchedule>getAllDataScheduleByPeriode(Date startDate,Date endDate);
    public List<TblCalendarEmployeeOvertime>getAllDataOvertimeByPeriode(Date startDate,Date endDate);
    public List<RefEmployeeAttendanceStatus>getAllAttendanceStatus();
    public List<RefEmployeeAttendanceStartStatus>getAllAttendanceStartStatus();
    public List<RefEmployeeAttendanceEndStatus>getAllAttendanceEndStatus();
    public List<TblCalendarEmployeeLeave>getAllEmployeeLeaveByDateAndIdEmployee(Date date,long id);
    
    public TblEmployee getEmployee(long id);
    public RefEmployeeAttendanceStatus getAttendanceStatus(int id);
    public RefEmployeeAttendanceStartStatus getAttendanceStartStatus(int id);
    public RefEmployeeAttendanceEndStatus getAttendanceEndStatus(int id);
    public TblCalendarEmployeeAttendance getEmployeeAttendance(long id);
     
     //----------------------------------------------------------
     public String getErrorMessage();
}
