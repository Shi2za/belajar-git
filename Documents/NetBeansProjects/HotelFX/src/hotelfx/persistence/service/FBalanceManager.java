/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hotelfx.persistence.service;

import hotelfx.persistence.model.LogCompanyBalanceCashFlow;
import hotelfx.persistence.model.TblBank;
import hotelfx.persistence.model.TblBankAccount;
import hotelfx.persistence.model.TblCompanyBalance;
import hotelfx.persistence.model.TblCompanyBalanceBankAccount;
import hotelfx.persistence.model.TblCompanyBalanceTransferReceived;
import hotelfx.persistence.model.TblEmployee;
import java.math.BigDecimal;
import java.sql.Date;
import java.util.List;

/**
 *
 * @author ANDRI
 */
public interface FBalanceManager {
    
    public boolean updateDataCompanyBalance(TblCompanyBalance companyBalance);
    
    public boolean updateDataBalanceTransfer(TblCompanyBalance selectedBalance, 
            TblCompanyBalance anotherBalance, 
            TblCompanyBalanceBankAccount selectedBalanceBankAccount, 
            TblCompanyBalanceBankAccount anotherBalanceBankAccount, 
            BigDecimal transferNominal);
    
    public TblCompanyBalance getDataCompanyBalance();
    
    public TblCompanyBalance getDataBackOfficeBalance();
    
    public TblCompanyBalance getDataCashierBalance();
    
    public TblCompanyBalance getDataDepositBalance();
    
    //--------------------------------------------------------------------------
    
    public TblCompanyBalanceBankAccount insertDataCompanyBalanceBankAccount(TblCompanyBalanceBankAccount companyBalanceBankAccount);
    
    public boolean updateDataCompanyBalanceBankAccount(TblCompanyBalanceBankAccount companyBalanceBankAccount);
    
    public boolean updateDataCompanyBalanceBankAccountAddFund(TblCompanyBalanceBankAccount companyBalanceBankAccount, 
            BigDecimal addFundNominal, 
            String addFundNote);
    
    public boolean deleteDataCompanyBalanceBankAccount(TblCompanyBalanceBankAccount companyBalanceBankAccount);
    
    public TblCompanyBalanceBankAccount getCompanyBalanceBankAccount(long id);
    
    public List<TblCompanyBalanceBankAccount> getAllDataCompanyBalanceBankAccount(long idCompanyBalance);
    
    //--------------------------------------------------------------------------
    
    public TblBankAccount getBankAccount(long idBankAccount);
    
    public List<TblBankAccount> getAllDataBankAccount();
    
    //--------------------------------------------------------------------------
    
    public TblBank getDataBank(long id);
    
    public List<TblBank> getAllDataBank();
    
    //--------------------------------------------------------------------------
    public List<TblEmployee>getAllDataEmployee();
    public boolean insertDataBalanceTransfer(TblCompanyBalanceTransferReceived companyBalanceTransferReceived);
    public boolean updateDataBalanceTransfer(TblCompanyBalanceTransferReceived companyBalanceTransferReceived);
    public boolean deleteDataBalanceTransfer(TblCompanyBalanceTransferReceived companyBalanceTransferReceived);
    public boolean updateDataBalanceTransferReceived(TblCompanyBalanceTransferReceived companyBalanceTransferReceived);
    public boolean updateDataBalanceReceived(TblCompanyBalanceTransferReceived companyBalanceTransferReceived);  
    public List<TblCompanyBalance>getAllDataCompanyBalance();
    public List<TblCompanyBalanceTransferReceived>getAllDataTransfer(long idSender);
    public List<TblCompanyBalanceTransferReceived>getAllDataReceived(long idReceived);
    public TblCompanyBalanceTransferReceived getDataCompanyBalanceTransferReceived(long id);
    
    //--------------------------------------------------------------------------
    //CASH FLOW
  //  public List<LogCompanyBalanceCashFlow>getAllDataCashFlow();
  //  public List<LogCompanyBalanceCashFlow>getAllDataCashFlowByIdBalance(long id);
     public List<LogCompanyBalanceCashFlow>getAllDataCashFlow(TblCompanyBalance companyBalance,TblBankAccount companyBalanceBankAccount,Date startDate,Date endDate);
     public BigDecimal getBalance(TblCompanyBalance companyBalance,Date startDate,TblBankAccount bankAccount);
    //--------------------------------------------------------------------------
    
    public String getErrorMessage();
    
}
