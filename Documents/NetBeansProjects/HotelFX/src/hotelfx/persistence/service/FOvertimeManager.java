/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hotelfx.persistence.service;

import hotelfx.persistence.model.RefEmployeeType;
import hotelfx.persistence.model.TblCalendarEmployeeOvertime;
import hotelfx.persistence.model.TblEmployee;
import java.sql.Date;
import java.util.List;

/**
 *
 * @author Andreas
 */
public interface FOvertimeManager {
    
    public TblCalendarEmployeeOvertime insertDataOvertime(TblCalendarEmployeeOvertime overtime,Date date);
    public boolean updateDataOvertime(TblCalendarEmployeeOvertime overtime,Date date);
    public boolean deleteDataOvertime(TblCalendarEmployeeOvertime overtime);
    
     public List<TblCalendarEmployeeOvertime>getAllDataOvertime();
     public List<RefEmployeeType>getAllDataTypeEmployee();
     public List<TblEmployee>getAllDataEmployee(RefEmployeeType employeeType);
     public TblCalendarEmployeeOvertime getDataOvertime(long id);
     public String getErrorMessage();
}
