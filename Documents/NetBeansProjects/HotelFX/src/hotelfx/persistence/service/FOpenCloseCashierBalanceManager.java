/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hotelfx.persistence.service;

import hotelfx.persistence.model.LogCompanyBalanceCashFlow;
import hotelfx.persistence.model.RefFinanceTransactionPaymentType;
import hotelfx.persistence.model.RefOpenCloseCashierBalanceStatus;
import hotelfx.persistence.model.TblCompanyBalance;
import hotelfx.persistence.model.TblEmployee;
import hotelfx.persistence.model.TblHotelExpenseTransaction;
import hotelfx.persistence.model.TblHotelExpenseTransactionDetail;
import hotelfx.persistence.model.TblJob;
import hotelfx.persistence.model.TblOpenCloseCashierBalance;
import hotelfx.persistence.model.TblOpenCloseCashierBalanceDetail;
import hotelfx.persistence.model.TblPeople;
import hotelfx.persistence.model.TblReservationPayment;
import java.util.List;

/**
 *
 * @author ANDRI
 */
public interface FOpenCloseCashierBalanceManager {
    
    //open
    public TblOpenCloseCashierBalance insertOpenCloseCashierBalance(TblOpenCloseCashierBalance occb);
    
    //close
    public boolean updateOpenCloseCashierBalance(TblOpenCloseCashierBalance occb);
    
    //not used
    public boolean deleteOpenCloseCashierBalance(TblOpenCloseCashierBalance occb);
    
    public TblOpenCloseCashierBalance getDataOpenCloseCashierBalance(long id);
    
    public List<TblOpenCloseCashierBalance> getAllDataOpenCloseCashierBalance();
    
    public TblOpenCloseCashierBalance getDataOpenCloseCashierBalanceByIDOpenCloseStatus(int idOpenCloseStatus);
    
    //--------------------------------------------------------------------------
    
    public TblOpenCloseCashierBalanceDetail getDataOpenCloseCashierBalanceDetail(long id);
    
    public List<TblOpenCloseCashierBalanceDetail> getAllDataOpenCloseCashierBalanceDetail();
    
    public List<TblOpenCloseCashierBalanceDetail> getAllDataOpenCloseCashierBalanceDetailByIDOpenCloseCashierBalance(long idOCCB);
    
    //--------------------------------------------------------------------------
    
    public TblEmployee getDataEmployee(long id);
    
    public List<TblEmployee> getAllDataEmployee();
    
    public TblPeople getDataPeople(long id);
    
    public TblJob getDataJob(long id);
    
    //--------------------------------------------------------------------------
    
    public RefOpenCloseCashierBalanceStatus getDataOpenCloseCashierBalanceStatus(int id);
    
    //--------------------------------------------------------------------------
    
    public TblReservationPayment getDataReservationPayment(long id);
    
    public TblHotelExpenseTransaction getDataHotelExpenseTransaction(long id);
    
    public LogCompanyBalanceCashFlow getDataLogCompanyBalanceCashFlow(long id);
    
    //--------------------------------------------------------------------------
    
    public TblCompanyBalance getDataCompanyBalance(long id);
    
    //--------------------------------------------------------------------------
    
    public RefFinanceTransactionPaymentType getDataFinanceTransactionPaymentType(int id);
    
    public List<TblHotelExpenseTransactionDetail> getAllDataHotelExpenseTransactionDetailByIDHotelExpenseTransaction(long idHET);
    
    //--------------------------------------------------------------------------
    
    public String getErrorMessage();
    
}
