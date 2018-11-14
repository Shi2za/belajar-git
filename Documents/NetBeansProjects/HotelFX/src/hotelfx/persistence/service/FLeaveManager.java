/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hotelfx.persistence.service;

import hotelfx.persistence.model.RefEmployeeLeaveType;
import hotelfx.persistence.model.RefEmployeeType;
import hotelfx.persistence.model.SysCalendar;
import hotelfx.persistence.model.TblCalendarEmployeeLeave;
import hotelfx.persistence.model.TblCalendarEmployeeSchedule;
import hotelfx.persistence.model.TblEmployee;
import java.sql.Date;
import java.time.LocalDate;
import java.util.List;

/**
 *
 * @author Andreas
 */
public interface FLeaveManager {
   //public boolean insertDataEmployeeLeaveMoreOneDay(List<TblCalendarEmployeeLeave>listEmployeeLeave,TblCalendarEmployeeLeave employeeLeave);
  // public TblCalendarEmployeeLeave insertDataEmployeeLeaveOneDay(TblCalendarEmployeeLeave leave,Date date,int count);
   public boolean insertDataEmployeeLeave(List<TblCalendarEmployeeLeave>listEmployeeLeave,TblCalendarEmployeeLeave employeeLeave,String status);
   public boolean insertDataEmployeeAllLeave(List<TblCalendarEmployeeLeave>listEmployeeLeave,int amountLeave,TblCalendarEmployeeLeave dataEmployeeLeave,String status);
   public boolean updateDataLeave(TblCalendarEmployeeLeave employeeLeave,Date date);
   public boolean deleteDataLeave(TblCalendarEmployeeLeave employeeLeave);
   
   public List<TblCalendarEmployeeLeave>getAllDataLeave();
   public List<RefEmployeeType>getAllEmployeeType();
   public List<RefEmployeeLeaveType>getAllLeaveType();
   public List<TblEmployee>getAllDataEmployee(RefEmployeeType employeeType);
   public List<TblCalendarEmployeeSchedule>getAllDataEmployeeScheduleByIdEmployee(long id);
   public List<TblCalendarEmployeeSchedule>getAllDataEmployeeScheduleByIdEmployeeAndDate(long id,Date date);
   public List<TblCalendarEmployeeSchedule>sortEmployeeSchedule(List<TblCalendarEmployeeSchedule>list);
   //public List<TblCalendarEmployeeSchedule>getAllDataEmployeeScheduleByIdEmployee(long id,String status,Date date,Date startDate,Date endDate);
   public List<TblCalendarEmployeeLeave>getAllDataEmployeeLeaveByDateAndLeaveType(Date date);
   public List<TblCalendarEmployeeLeave>getAllDataEmployeeLeaveByDateAndIdEmployee(TblEmployee employee,Date date);
   public List<TblCalendarEmployeeLeave>getAllDataEmployeeLeaveByStartDateEndDateAndTypeLeave(Date startDate,Date endDate);
   public List<TblCalendarEmployeeLeave>getAllDataEmployeeLeaveByStartDateEndDateAndIdEmployee(TblEmployee employee,Date startDate,Date endDate);
   public List<SysCalendar>getAllSysCalendar(Date date,Date startLeave,Date endLeave,String status);
   public TblCalendarEmployeeLeave getDataEmployeeLeave(long id);
   public List<TblCalendarEmployeeLeave> getAllDataLeaveByDateAndIdEmployee(SysCalendar calendar,TblEmployee employee);
   public List<SysCalendar>getCalendarByDate(String status,Date date,Date startDate,Date endDate);
   public String getErrorMessage();
}
