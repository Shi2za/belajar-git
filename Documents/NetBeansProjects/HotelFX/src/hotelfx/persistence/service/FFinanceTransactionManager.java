/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hotelfx.persistence.service;

import hotelfx.persistence.model.RefFinanceTransactionPaymentType;
import hotelfx.persistence.model.TblBank;
import hotelfx.persistence.model.TblBankAccount;
import hotelfx.persistence.model.TblBankEdc;
import hotelfx.persistence.model.TblBankNetworkCard;
import hotelfx.persistence.model.TblCompanyBalanceBankAccount;
import hotelfx.persistence.model.TblCustomer;
import hotelfx.persistence.model.TblEmployee;
import hotelfx.persistence.model.TblPartner;
import hotelfx.persistence.model.TblPeople;
import hotelfx.persistence.model.TblReservation;
import hotelfx.persistence.model.TblReservationBill;
import hotelfx.persistence.model.TblReservationPayment;
import hotelfx.persistence.model.TblReservationPaymentWithBankCard;
import hotelfx.persistence.model.TblReservationPaymentWithCekGiro;
import hotelfx.persistence.model.TblReservationPaymentWithGuaranteePayment;
import hotelfx.persistence.model.TblReservationPaymentWithTransfer;
import java.math.BigDecimal;
import java.util.List;

/**
 *
 * @author ABC-Programmer
 */
public interface FFinanceTransactionManager {
    
    public boolean updateSettleDataTransaction(
            TblReservationPayment dataReservationPayment, 
            TblBankAccount customerBankAccount,
            TblCompanyBalanceBankAccount companyBalanceBankAccount, 
            BigDecimal paymentCharge, 
            BigDecimal paymentDiscount);
    
    public TblCompanyBalanceBankAccount getDataCompanyBalanceBankAccountByIDCompanyBalanceAndIDBankAccount(
            long idCompanyBalance, 
            long idBankAccount);
    
    //--------------------------------------------------------------------------
    
    public TblReservationPayment getDataReservationPayment(long id);
    
    public List<TblReservationPayment> getAllDataReservationPayment();
    
    //--------------------------------------------------------------------------
    
    public TblReservationPaymentWithTransfer getDataReservationPaymentWithTransferByIDReservationPayment(long idReservationPayment);
    
    public TblReservationPaymentWithBankCard getDataReservationPaymentWithbankCardByIDReservationPayment(long idReservationPayment);
    
    public TblReservationPaymentWithCekGiro getDataReservationPaymentWithCekGiroByIDReservationPayment(long idReservationPayment);
    
    public TblReservationPaymentWithGuaranteePayment getDataReservationPaymentWithGuaranteePaymentByIDReservationPayment(long idReservationPayment);
    
    public RefFinanceTransactionPaymentType getDataFinanceTransactionPaymentType(int id);
    
    //-------------------------------------------------------------------------
    
    public TblReservationBill getDataReservationBill(long id);
    
    public TblReservation getDataReservation(long id);
    
    public TblCustomer getDataCustomer(long id);
    
    public TblPartner getDataPartner(long id);
    
    public TblBank getDataBank(long id);
    
    public TblBankNetworkCard getDataBankNetworkCard(long id);
    
    public TblBankEdc getDataBankEDC(long id);
    
    public TblBankAccount getDataBankAccount(long id);
    
    public TblEmployee getDataEmployee(long id);
    
    public TblPeople getDataPeople(long id);
    
    //--------------------------------------------------------------------------
    
    public String getErrorMessage();
    
}
