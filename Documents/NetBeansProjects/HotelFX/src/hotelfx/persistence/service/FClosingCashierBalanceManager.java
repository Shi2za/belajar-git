/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hotelfx.persistence.service;

import hotelfx.persistence.model.RefFinanceTransactionPaymentType;
import hotelfx.persistence.model.TblCompanyBalance;
import hotelfx.persistence.model.TblEmployee;
import hotelfx.persistence.model.TblPeople;
import hotelfx.persistence.model.TblReservationPayment;
import java.util.List;

/**
 *
 * @author ANDRI
 */
public interface FClosingCashierBalanceManager {
 
    public boolean updateDataTransactionClosingBalance(List<TblReservationPayment> dataReservationPayments);
    
    public TblCompanyBalance getDataCashierBalance();
    
    //--------------------------------------------------------------------------
    
    public TblEmployee getDataEmployee(long id);
    
    public List<TblEmployee> getAllDataEmployee();
    
    public TblPeople getDataPeople(long id);
    
    //--------------------------------------------------------------------------
    
    public List<TblReservationPayment> getAllDataReservationPaymentByIDCashier(long idEmployee);
    
    public RefFinanceTransactionPaymentType getFinanceTransactionPaymentType(int id);
            
    //--------------------------------------------------------------------------
    
    public String getErrorMessage();
    
}
