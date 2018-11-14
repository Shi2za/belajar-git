/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hotelfx.persistence.service;

import hotelfx.persistence.model.RefBankAccountHolderStatus;
import hotelfx.persistence.model.RefCountry;
import hotelfx.persistence.model.RefEmployeeSalaryType;
import hotelfx.persistence.model.RefEmployeeStatus;
import hotelfx.persistence.model.RefEmployeeType;
import hotelfx.persistence.model.RefPeopleGender;
import hotelfx.persistence.model.RefPeopleReligion;
import hotelfx.persistence.model.RefPeopleStatus;
import hotelfx.persistence.model.SysDataHardCode;
import hotelfx.persistence.model.TblBank;
import hotelfx.persistence.model.TblBankAccount;
import hotelfx.persistence.model.TblCalendarEmployeeWarningLetter;
import hotelfx.persistence.model.TblEmployee;
import hotelfx.persistence.model.TblEmployeeBankAccount;
import hotelfx.persistence.model.TblEmployeeWarningLetterType;
import hotelfx.persistence.model.TblGroup;
import hotelfx.persistence.model.TblJob;
import hotelfx.persistence.model.TblPeople;
import java.util.List;

/**
 *
 * @author Andreas
 */
public interface FEmployeeManager {

    public TblEmployee insertDataEmployee(TblEmployee employee, 
            String imgExtention, 
            List<TblEmployeeBankAccount> employeeBankAccount);

    public boolean updateDataEmployee(TblEmployee employee, 
            String imgExtention, 
            List<TblEmployeeBankAccount> employeeBankAccount);

    public boolean deleteDataEmployee(TblEmployee employee);

    public TblEmployee getDataEmployee(long id);

    public TblPeople getDataPeople(long id);

    public TblJob getDataJob(long id);

    public TblGroup getDataGroup(long id);

    public RefPeopleGender getDataPeopleGender(int id);

    public RefPeopleReligion getDataPeopleReligion(int id);

    public RefPeopleStatus getDataPeopleStatus(int id);

    public RefEmployeeType getDataEmployeeType(int id);

    public RefEmployeeStatus getDataEmployeeStatus(int id);

    public RefEmployeeSalaryType getDataEmployeeSalaryType(int id);

    public List<TblEmployee> getAllDataEmployee();

    public List<TblPeople> getAllDataPeople();

    public List<TblJob> getAllDataJob();

    public List<TblGroup> getAllDataGroup();

    public List<RefPeopleGender> getAllDataPeopleGender();

    public List<RefPeopleStatus> getAllDataPeopleStatus();

    public List<RefPeopleReligion> getAllDataPeopleReligion();

    public List<RefEmployeeType> getAllDataEmployeeType();

    public List<RefEmployeeStatus> getAllDataEmployeeStatus();

    public List<RefEmployeeSalaryType> getAllDataSalaryType();

  //--------------------------------------------------------------------------
    
    public RefCountry getDataPeopleCountry(int id);
    
    public List<RefCountry> getAllDataPeopleCountry();
    
    //--------------------------------------------------------------------------
    
    public TblEmployeeBankAccount getDataEmployeeBankAccount(long idEmployeeBakAccount);

    public List<TblEmployeeBankAccount> getAllDataEmployeeBankAccount(long idEmployee);

    //--------------------------------------------------------------------------
    public TblBankAccount getDataBankAccount(long idBankAccount);

    public List<TblBankAccount> getAllDataBankAccount();

    //--------------------------------------------------------------------------
    public RefBankAccountHolderStatus getDataBankAccountHolderStatus(int id);

    public List<RefBankAccountHolderStatus> getAllDataBankAccountHolderStatus();

    //--------------------------------------------------------------------------
    public TblBank getDataBank(long id);

    public List<TblBank> getAllDataBank();

    //--------------------------------------------------------------------------
    public boolean insertDataEmployeeWarningLetter(TblCalendarEmployeeWarningLetter employeeWarningLetter);
    public boolean updateDataEmployeeWarningLetter(TblCalendarEmployeeWarningLetter employeeWarningLetter);
    public boolean deleteDataEmployeeWarningLetter(TblCalendarEmployeeWarningLetter employeeWarningLetter);
    public List<TblCalendarEmployeeWarningLetter>getAllDataEmployeeWarningLetter(long id);
    public List<TblEmployeeWarningLetterType>getAllDataWarningLetter();
    public TblCalendarEmployeeWarningLetter getDataEmployeeWarningLetter(long id);
    public boolean updateDataEmployeeWarningLetterStatus(TblEmployee tblEmployee);
    //--------------------------------------------------------------------------
    public SysDataHardCode getDataSysDataHardCode(long id);
    //--------------------------------------------------------------------------
    public String getErrorMessage();
}
