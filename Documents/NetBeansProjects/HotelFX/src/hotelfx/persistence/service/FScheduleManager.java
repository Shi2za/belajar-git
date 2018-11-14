/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hotelfx.persistence.service;

import hotelfx.persistence.model.RefEmployeeType;
import hotelfx.persistence.model.SysCalendar;
import hotelfx.persistence.model.SysDataHardCode;
import hotelfx.persistence.model.TblCalendarEmployeeSchedule;
import hotelfx.persistence.model.TblEmployee;
import hotelfx.persistence.model.TblEmployeeSchedule;
import hotelfx.persistence.model.TblSystemRole;
import java.sql.Date;
import java.util.List;

/**
 *
 * @author Andreas
 */
public interface FScheduleManager {
    public TblEmployeeSchedule insertDataSchedule(TblEmployeeSchedule schedule);
    public boolean updateDataSchedule(TblEmployeeSchedule schedule);
    public boolean deleteDataSchedule(TblEmployeeSchedule schedule);
    
    public boolean insertDataEmployeeSchedule(List<TblCalendarEmployeeSchedule>dataEmployeeSchedule,long id);
     
    public List<TblEmployeeSchedule>getAllDataSchedule();
    public List<SysCalendar> getAllCalendar(Date startDate,Date endDate,String note);
    public List<TblEmployee>getAllDataEmployee(long id);
    public List<TblCalendarEmployeeSchedule>getAllDataEmployeeSchedule(long id);
    public List<RefEmployeeType>getAllDataEmployeeType();
    public List<TblCalendarEmployeeSchedule>getAllEmployeeScheduleByTypeEmployeeAndCalendarOneMoreDay(Date startDate,Date endDate,RefEmployeeType employeeType);
   // public List<TblCalendarEmployeeSchedule>getAllEmployeeScheduleByTypeEmployeeAndCalendarOneDay(SysCalendar calendar,RefEmployeeType employeeType);
     public List<TblCalendarEmployeeSchedule>getAllEmployeeSchedulePrint(SysCalendar calendar,RefEmployeeType employeeType,TblEmployeeSchedule schedule);
    public TblEmployeeSchedule getSchedule(long id);
    public TblCalendarEmployeeSchedule getEmployeeSchedule(long id);
    public TblEmployee getEmployee(long id);
    
    //--------------------------------------------------------------------------
    
    public SysDataHardCode getDataSysDataHardCode(long id);
    
    public List<SysDataHardCode> getAllDataSysDataHardCode();
    
    //--------------------------------------------------------------------------
    
    public String getErrorMessage();

    //public List<TblCalendarEmployeeSchedule> getAllEmployeeScheduleByTypeEmployeeAndCalendar(Date startDate, Date endDate, RefEmployeeType employeeType);
}
