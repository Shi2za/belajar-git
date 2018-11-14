/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hotelfx.persistence.service;

import hotelfx.persistence.model.RefFinanceTransactionPaymentType;
import hotelfx.persistence.model.SysDataHardCode;
import hotelfx.persistence.model.TblBank;
import hotelfx.persistence.model.TblBankAccount;
import hotelfx.persistence.model.TblCompanyBalance;
import hotelfx.persistence.model.TblCompanyBalanceBankAccount;
import hotelfx.persistence.model.TblEmployee;
import hotelfx.persistence.model.TblHotelExpenseTransaction;
import hotelfx.persistence.model.TblHotelExpenseTransactionDetail;
import hotelfx.persistence.model.TblOpenCloseCashierBalance;
import hotelfx.persistence.model.TblOpenCloseCashierBalanceDetail;
import hotelfx.persistence.model.TblPeople;
import java.util.List;

/**
 *
 * @author ANDRI
 */
public interface FExpenseTransactionManager {
    
    public TblHotelExpenseTransaction insertDataHotelExpenseTransaction(TblHotelExpenseTransaction het, 
            List<TblHotelExpenseTransactionDetail> hetDetails);
    
    public boolean updateDataHotelExpenseTransaction(TblHotelExpenseTransaction het, 
            List<TblHotelExpenseTransactionDetail> hetDetails);
    
    public boolean deleteDataHotelExpenseTransaction(TblHotelExpenseTransaction het);
    
    public TblHotelExpenseTransaction getDataHotelExpenseTransaction(long id);
    
    public List<TblHotelExpenseTransaction> getAllDataHotelExpenseTransaction();
    
    //--------------------------------------------------------------------------
    
    public TblHotelExpenseTransactionDetail getDataHotelExpenseTransactionDetail(long id);
    
    public List<TblHotelExpenseTransactionDetail> getAllDataHotelExpenseTransactionDetail();
    
    public List<TblHotelExpenseTransactionDetail> getAllDataHotelExpenseTransactionDetailByIDHotelExpenseTransaction(long idHET);
    
    //--------------------------------------------------------------------------
    
    public TblCompanyBalance getDataCompanyBalance(long id);
    
    public List<TblCompanyBalance> getAllDataCompanyBalance();
    
    //--------------------------------------------------------------------------
    
    public TblCompanyBalanceBankAccount getDataCompanyBalanceBankAccountByIDCompanyBalanceAndIDBankAccount(long idCompanyBalance, 
            long idBankAccount);
    
    public List<TblCompanyBalanceBankAccount> getAllDataCompanyBalanceBankAccountByIDCompanyBalance(long idCompanyBalance);
    
    //--------------------------------------------------------------------------
    
    public TblBankAccount getDataBankAccount(long id);
    
    public List<TblBankAccount> getAllDataBankAccount();
    
    //--------------------------------------------------------------------------
    
    public RefFinanceTransactionPaymentType getDataFinanceTransactionType(int id);
    
    //--------------------------------------------------------------------------
    
    public TblBank getDataBank(long id);
    
    public List<TblBank> getAllDataBank();
    
    public TblEmployee getDataEmployee(long id);
    
    public TblPeople getDataPeople(long id);
    
    //--------------------------------------------------------------------------
    
    public SysDataHardCode getDataSysDataHardCode(long id);
    
    //--------------------------------------------------------------------------
    
    public TblOpenCloseCashierBalance getDataOpenCloseCashierBalance(long id);
    
    public TblOpenCloseCashierBalanceDetail getDataOpenCloseCashierBalanceDetailByIDHotelExpenseTransaction(long idHET);
    
    //--------------------------------------------------------------------------
    
    public String getErrorMessage();
    
}
