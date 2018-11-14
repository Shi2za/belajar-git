/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hotelfx.persistence.service;

import hotelfx.persistence.model.RefFinanceTransactionPaymentType;
import hotelfx.persistence.model.RefFinanceTransactionStatus;
import hotelfx.persistence.model.RefFinanceTransactionType;
import hotelfx.persistence.model.RefHotelInvoiceType;
import hotelfx.persistence.model.RefHotelPayableType;
import hotelfx.persistence.model.SysDataHardCode;
import hotelfx.persistence.model.TblBank;
import hotelfx.persistence.model.TblBankAccount;
import hotelfx.persistence.model.TblCompanyBalance;
import hotelfx.persistence.model.TblCompanyBalanceBankAccount;
import hotelfx.persistence.model.TblHotelFinanceTransaction;
import hotelfx.persistence.model.TblHotelFinanceTransactionHotelPayable;
import hotelfx.persistence.model.TblHotelFinanceTransactionWithCash;
import hotelfx.persistence.model.TblHotelFinanceTransactionWithCekGiro;
import hotelfx.persistence.model.TblHotelFinanceTransactionWithTransfer;
import hotelfx.persistence.model.TblHotelInvoice;
import hotelfx.persistence.model.TblHotelPayable;
import hotelfx.persistence.model.TblReservationAdditionalService;
import hotelfx.persistence.model.TblResto;
import hotelfx.persistence.model.TblRestoBankAccount;
import hotelfx.persistence.model.TblRoomService;
import java.util.List;

/**
 *
 * @author ABC-Programmer
 */
public interface FRestoManager {
    
    public TblResto getDataResto(long id);
    
    public List<TblResto> getAllDataResto();
    
    //--------------------------------------------------------------------------
    
    public TblReservationAdditionalService getDataReservationAdditionalService(long id);
    
    public List<TblReservationAdditionalService> getAllDataReservationAdditionalService();
    
    public TblRoomService getDataRoomService(long id);
    
    //--------------------------------------------------------------------------
    public TblCompanyBalance getDataCompanyBalance(long id);

    //--------------------------------------------------------------------------
    public TblHotelPayable getDataHotelPayable(long id);

    public RefHotelPayableType getDataHotelPayableType(int id);

    public RefFinanceTransactionStatus getDataFinanceTransactionStatus(int id);

    //--------------------------------------------------------------------------
    public TblHotelFinanceTransaction insertDataHotelFinanceTransaction(TblHotelFinanceTransaction hotelFinanceTransaction,
            TblHotelFinanceTransactionWithCash hotelFinanceTransactionWithCash,
            List<TblHotelFinanceTransactionHotelPayable> hfthps, 
            String restoName);

    public TblHotelFinanceTransaction insertDataHotelFinanceTransaction(TblHotelFinanceTransaction hotelFinanceTransaction,
            TblHotelFinanceTransactionWithTransfer hotelFinanceTransactionWithTransfer,
            List<TblHotelFinanceTransactionHotelPayable> hfthps, 
            String restoName);

    public TblHotelFinanceTransaction insertDataHotelFinanceTransaction(TblHotelFinanceTransaction hotelFinanceTransaction,
            TblHotelFinanceTransactionWithCekGiro hotelFinanceTransactionWithCekGiro,
            List<TblHotelFinanceTransactionHotelPayable> hfthps, 
            String restoName);
    
    public RefFinanceTransactionType getDataFinanceTransactionType(int id);

    //--------------------------------------------------------------------------
    public TblRestoBankAccount insertDataRestoBankAccount(TblRestoBankAccount restoBankAccount);

    public TblRestoBankAccount getDataRestoBankAccount(long id);

    public List<TblRestoBankAccount> getAllDataRestoBankAccount();

    public List<TblRestoBankAccount> getAllDataRestoBankAccountByIDResto(long idResto);

    //--------------------------------------------------------------------------
    public List<TblCompanyBalanceBankAccount> getAllDataCompanyBalanceBankAccount(long idCompanyBalance);

    //--------------------------------------------------------------------------
    public TblBankAccount getBankAccount(long id);

    public List<TblBankAccount> getAllDataBankAccount();

    //--------------------------------------------------------------------------
    public TblBank getDataBank(long id);

    public List<TblBank> getAllDataBank();

    //--------------------------------------------------------------------------
    public SysDataHardCode getDataSysDataHardCode(long id);

    public List<SysDataHardCode> getAllDataSysDataHardCode();

    //--------------------------------------------------------------------------
    public TblHotelInvoice insertDataHotelInvoice(TblHotelInvoice hotelInvoice,
            TblReservationAdditionalService ras);

    public TblHotelInvoice insertDataHotelInvoice(TblReservationAdditionalService ras);

    public TblHotelInvoice getDataHotelInvoice(long id);

    public RefHotelInvoiceType getDataHotelInvoiceType(int id);

    //--------------------------------------------------------------------------
    public TblHotelFinanceTransaction getDataHotelFinanceTransaction(long id);

    public List<TblHotelFinanceTransactionHotelPayable> getAllDataHotelFinanceTransactionHotelPayableByIDHotelFinanceTransaction(long idHotelFinanceTransaction);

    public List<TblHotelFinanceTransactionHotelPayable> getAllDataHotelFinanceTransactionHotelPayableByIDHotelPayable(long idHotelPayable);

    //--------------------------------------------------------------------------
    public TblReservationAdditionalService getDataReservationAdditionalServiceByIDHotelPayable(long idHotelPayable);

    //--------------------------------------------------------------------------
    public RefFinanceTransactionPaymentType getDataFinanceTransactionPaymentType(int id);

    public List<RefFinanceTransactionPaymentType> getAllDataFinanceTransactionPaymentType();

    //--------------------------------------------------------------------------
    public TblCompanyBalanceBankAccount getDataCompanyBalanceBankAccountByIDCompanyBalanceAndIDBankAccount(
            long idCompanyBalance,
            long idBankAccount);

    //--------------------------------------------------------------------------
    public TblHotelFinanceTransactionWithTransfer getDataHotelFinanceTransactionWithTransferByIDHotelFinanceTransaction(long idHotelFinanceTransaction);

    public TblHotelFinanceTransactionWithCekGiro getDataHotelFinanceTransactionWithCekGiroByIDHotelFinanceTransaction(long idHotelFinanceTransaction);

    //--------------------------------------------------------------------------
    public List<TblCompanyBalance> getAllDataCompanyBalance();
    
    //--------------------------------------------------------------------------
    public String getErrorMessage();
    
}
