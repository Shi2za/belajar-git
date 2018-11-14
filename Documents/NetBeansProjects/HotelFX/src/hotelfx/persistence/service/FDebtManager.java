/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hotelfx.persistence.service;

import hotelfx.helper.ClassDataPasswordDeleteDebt;
import hotelfx.persistence.model.RefEmployeeType;
import hotelfx.persistence.model.RefFinanceTransactionPaymentType;
import hotelfx.persistence.model.SysPasswordDeleteDebt;
import hotelfx.persistence.model.TblBank;
import hotelfx.persistence.model.TblCalendarEmployeeDebt;
import hotelfx.persistence.model.TblCalendarEmployeePaymentDebtHistory;
import hotelfx.persistence.model.TblCalendarEmployeePaymentHistory;
import hotelfx.persistence.model.TblCompanyBalance;
import hotelfx.persistence.model.TblCompanyBalanceBankAccount;
import hotelfx.persistence.model.TblEmployee;
import hotelfx.persistence.model.TblEmployeeBankAccount;
import java.math.BigDecimal;
import java.sql.Date;
import java.util.List;

/**
 *
 * @author Andreas
 */
public interface FDebtManager {
   public boolean insertDataDebt(TblCalendarEmployeeDebt employeeDebt);
  //  public boolean insertDataDebt(TblCalendarEmployeeDebt employeeDebt,Date date);
  //  public boolean updateDataDebt(TblCalendarEmployeeDebt employeeDebt,Date date);
    public boolean updateDataDebt(TblCalendarEmployeeDebt employeeDebt);
    public boolean updateSettingPassword(SysPasswordDeleteDebt passwordDeleteOldDebt,SysPasswordDeleteDebt passwordDeleteNewDebt);
    public boolean approvedDataDebt(TblCalendarEmployeeDebt employeeDebt);
     public boolean paymentDataDebt(TblCalendarEmployeePaymentHistory employeePaymentDebt);
  //  public boolean paymentDataDebt(TblCalendarEmployeePaymentDebtHistory employeePaymentDebt,BigDecimal total,Date date);
    public boolean rejectedDataDebt(TblCalendarEmployeeDebt employeeDebt);
    public boolean deleteDataDebt(TblCalendarEmployeeDebt employeeDebt,ClassDataPasswordDeleteDebt dataPasswordDeleteDebt);
    //public boolean deleteDataDebtNotAprroved(TblCalendarEmployeeDebt employeeDebt);
    //public boolean deleteDataDebtApproved(ClassDataPasswordDeleteDebt dataPasswordDeleteDebt);
    
    public List<TblCalendarEmployeeDebt>getAllDataDebt();
    public List<TblCalendarEmployeeDebt>getAllDataDebtByIdEmployee(TblEmployee employee);
    public List<RefEmployeeType> getAllEmployeeType();
    public List<TblEmployee>getAllEmployeeName(RefEmployeeType employeeType);
    public List<TblCompanyBalance>getAllCompanyBalance();
    public List<TblCompanyBalanceBankAccount>getAllCompanyBalanceBankAccount(TblCompanyBalance companyBalance);
   
     public List<TblCalendarEmployeePaymentHistory>getAllEmployeePaymentDebt();
 //   public List<TblCalendarEmployeePaymentDebtHistory>getAllEmployeePaymentDebt();
    public List<TblCalendarEmployeePaymentDebtHistory>getAllEmployeePaymentDebtByIdEmployeeDebt(TblCalendarEmployeeDebt employeeDebt);
    public List<TblCalendarEmployeePaymentDebtHistory>getAllDataEmployeePaymentDebtByIdEmployee(TblEmployee employee);
    public List<TblCalendarEmployeePaymentHistory>getAllEmployeePaymentByIdEmployee(TblEmployee employee);
    public List<RefFinanceTransactionPaymentType>getAllPaymentType();
//------------------------------------------------------------------------------    
    public boolean insertBankAccountSender(TblEmployeeBankAccount employeeBankAccount);
    public List<TblEmployeeBankAccount>getAllBankAccountSender(TblEmployee employee);
    public List<TblBank>getAllDataBank();
    //public List<SysPasswordDeleteDebt>getAllPasswordDeleteDebt(String password);
    public boolean checkPassword(String password);
    public TblCompanyBalance getCompanyBalance(long id);
    public TblCalendarEmployeeDebt getEmployeeDebt(long id);
    //----------------------------------------------------------------------
    public String getErrorMessage();
}
