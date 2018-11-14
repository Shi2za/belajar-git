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
import hotelfx.persistence.model.RefHotelReceivableType;
import hotelfx.persistence.model.RefPartnerType;
import hotelfx.persistence.model.RefReservationStatus;
import hotelfx.persistence.model.SysDataHardCode;
import hotelfx.persistence.model.TblBank;
import hotelfx.persistence.model.TblBankAccount;
import hotelfx.persistence.model.TblCompanyBalance;
import hotelfx.persistence.model.TblCompanyBalanceBankAccount;
import hotelfx.persistence.model.TblEmployee;
import hotelfx.persistence.model.TblGuaranteeLetterItemDetail;
import hotelfx.persistence.model.TblHotelFinanceTransaction;
import hotelfx.persistence.model.TblHotelFinanceTransactionHotelReceivable;
import hotelfx.persistence.model.TblHotelFinanceTransactionWithCash;
import hotelfx.persistence.model.TblHotelFinanceTransactionWithCekGiro;
import hotelfx.persistence.model.TblHotelFinanceTransactionWithTransfer;
import hotelfx.persistence.model.TblHotelInvoice;
import hotelfx.persistence.model.TblHotelReceivable;
import hotelfx.persistence.model.TblPartner;
import hotelfx.persistence.model.TblPartnerBankAccount;
import hotelfx.persistence.model.TblReservation;
import hotelfx.persistence.model.TblReservationBill;
import hotelfx.persistence.model.TblReservationPayment;
import hotelfx.persistence.model.TblReservationPaymentWithGuaranteePayment;
import hotelfx.persistence.model.TblReservationRoomPriceDetail;
import hotelfx.persistence.model.TblReservationRoomTypeDetail;
import hotelfx.persistence.model.TblReservationRoomTypeDetailRoomPriceDetail;
import hotelfx.persistence.model.TblReservationRoomTypeDetailTravelAgentDiscountDetail;
import hotelfx.persistence.model.TblReservationTravelAgentDiscountDetail;
import hotelfx.persistence.model.TblRoom;
import hotelfx.persistence.model.TblRoomType;
import hotelfx.persistence.model.TblTravelAgent;
import hotelfx.persistence.model.TblTravelAgentRoomType;
import java.sql.Date;
import java.util.List;

/**
 *
 * @author ANDRI
 */
public interface FPartnerManager {
    
    public TblTravelAgent insertDataTravelAgent(TblTravelAgent travelAgent, 
            List<TblPartnerBankAccount> partnerBankAccount);
    
//    public TblTravelAgent insertDataTravelAgent(TblTravelAgent travelAgent, 
//            List<TblTravelAgentRoomType> travelAgentRoomTypes);
    
    public boolean updateDataTravelAgent(TblTravelAgent travelAgent, 
            List<TblPartnerBankAccount> partnerBankAccount);
    
//    public boolean updateDataTravelAgent(TblTravelAgent travelAgent, 
//            List<TblTravelAgentRoomType> travelAgentRoomTypes);
    
    public boolean deleteDataTravelAgent(TblTravelAgent travelAgent);
    
    public TblTravelAgent getTravelAgent(long id);
    
    public List<TblTravelAgent> getAllDataTravelAgent();
   
    //--------------------------------------------------------------------------
    
    public boolean updateDataTravelAgentRoomType(TblTravelAgentRoomType travelAgentRoomType);
    
    public boolean updateDataTravelAgentRoomType(List<TblTravelAgentRoomType> travelAgentRoomTypes);
    
    //--------------------------------------------------------------------------
    
    public TblPartner getPartner(long id);
    
    public List<TblPartner> getAllDataPartner();
    
    //--------------------------------------------------------------------------
    
    public TblPartner insertDataCorporate(TblPartner corporate, 
            List<TblPartnerBankAccount> partnerBankAccount);
    
    public boolean updateDataCorporate(TblPartner corporate, 
            List<TblPartnerBankAccount> partnerBankAccount);
    
    public boolean deleteDataCorporate(TblPartner corporate);
    
    public TblPartner getCorporate(long id);
    
    public List<TblPartner> getAllDataCorporate();
   
    //--------------------------------------------------------------------------
    
    public TblPartner insertDataGovernment(TblPartner government, 
            List<TblPartnerBankAccount> partnerBankAccount);
    
    public boolean updateDataGovernment(TblPartner government, 
            List<TblPartnerBankAccount> partnerBankAccount);
    
    public boolean deleteDataGovernment(TblPartner government);
    
    public TblPartner getGovernment(long id);
    
    public List<TblPartner> getAllDataGovernment();
   
    //--------------------------------------------------------------------------
    
    public List<TblTravelAgentRoomType> getAllDataTravelAgentRoomTypeByIDPartner(long idPartner);
    
    public List<TblTravelAgentRoomType> getAllDataTravelAgentRoomTypeByIDPartnerAndAvailableDate(long idPartner, 
            Date availableDate);
    
    public TblRoomType getDataRoomType(long id);
    
    public List<TblRoomType> getAllDataRoomType();
    
    //--------------------------------------------------------------------------
    
    public List<TblRoom> getAllDataRoomByIDRoomType(long idRoomType);

//    public TblTravelAgentRoomType getTravelAgentRoomTypeByIDRoomTypeAndIDPartner(long idRoomType, long idPartner);
//    
//    public TblTravelAgentRoomType getTravelAgentRoomTypeByIDRoomTypeAndIDPartnerAndAvailableDate(long idRoomType, long idPartner, Date availableDate);

    public List<TblTravelAgentRoomType> getAllDataTravelAgentRoomTypeByIDRoomType(long idRoomType);
    
    public List<TblTravelAgentRoomType> getAllDataTravelAgentRoomTypeByIDRoomTypeAndAvailableDate(long idRoomType, 
            Date availableDate);

    //--------------------------------------------------------------------------
    
    public int getMaxNumberRoomTypeNumberHasBeenUsedByCustomer(TblRoomType roomType, Date detailDate);
    
    public int getMaxNumberRoomTypeNumberHasBeenUsedByTravelAgent(TblRoomType roomType, Date detailDate, TblPartner partner);
    
    //--------------------------------------------------------------------------
    
    public TblPartner getDataPartner(long id);
    
    public TblTravelAgentRoomType getDataTravelAgentRoomTypeByIDRoomTypeAndIDPartner(long idRoomType, 
            long idPartner);
    
    public TblTravelAgentRoomType getDataTravelAgentRoomTypeByIDRoomTypeAndIDPartnerAndAvailableDate(long idRoomType, 
            long idPartner, 
            Date availableDate);
    
    //--------------------------------------------------------------------------
    public TblPartnerBankAccount getPartnerBankAccount(long id);
    
    public List<TblPartnerBankAccount> getAllDataPartnerBankAccount();
    
    public List<TblPartnerBankAccount> getAllDataPartnerBankAccountByIDPartner(long idPartner);
    
    //--------------------------------------------------------------------------
    
    public TblBankAccount getBankAccount(long id);
    
    public List<TblBankAccount> getAllDataBankAccount();
    
    //--------------------------------------------------------------------------
    
//    public RefBankAccountHolderStatus getBankAccountHolderStatus(int id);
//    
//    public List<RefBankAccountHolderStatus> getAllDataBankAccountHolderStatus();
    
    //--------------------------------------------------------------------------
    
    public TblBank getDataBank(long id);
    
    public List<TblBank> getAllDataBank();
    
    //--------------------------------------------------------------------------
            
    public TblHotelReceivable getDataHotelReceivable(long id);
    
    public RefHotelReceivableType getDataHotelReceivableType(int id);
    
    public RefFinanceTransactionStatus getDataFinanceTransactionStatus(int id);
    
    public TblReservationPaymentWithGuaranteePayment getDataReservationPaymentWithGuaranteePayment(long id);
    
    public RefPartnerType getDataPartnerType(int id);
    
    public TblReservationPayment getDataReservationPayment(long id);
    
    public TblEmployee getDataEmployee(long id);
    
    //--------------------------------------------------------------------------
    
    public TblHotelFinanceTransaction insertDataHotelFinanceTransaction(TblHotelFinanceTransaction hotelFinanceTransaction);
    
    public TblHotelFinanceTransaction insertDataHotelFinanceTransaction(TblHotelFinanceTransaction hotelFinanceTransaction,
            TblHotelFinanceTransactionWithCash hotelFinanceTransactionWithCash,
            List<TblHotelFinanceTransactionHotelReceivable> hfthrs, 
            String partnerName);
    
    public TblHotelFinanceTransaction insertDataHotelFinanceTransaction(TblHotelFinanceTransaction hotelFinanceTransaction,
            TblHotelFinanceTransactionWithTransfer hotelFinanceTransactionWithTransfer,
            List<TblHotelFinanceTransactionHotelReceivable> hfthrs, 
            String partnerName);

    public TblHotelFinanceTransaction insertDataHotelFinanceTransaction(TblHotelFinanceTransaction hotelFinanceTransaction,
            TblHotelFinanceTransactionWithCekGiro hotelFinanceTransactionWithCekGiro,
            List<TblHotelFinanceTransactionHotelReceivable> hfthrs, 
            String partnerName);
    
    public List<TblHotelFinanceTransaction> getAllDataHotelFinanceTransactionByIDHotelReceivable(long idHotelReceivable);
    
    public RefFinanceTransactionType getDataFinanceTransactionType(int id);
    
    //--------------------------------------------------------------------------
    
    public List<TblCompanyBalanceBankAccount> getAllDataCompanyBalanceBankAccount(long idCompanyBalance);
    
    //--------------------------------------------------------------------------
    
    public TblPartnerBankAccount insertDataPartnerBankAccount(TblPartnerBankAccount partnerBankAccount);
    
    //--------------------------------------------------------------------------
    
    public TblReservation getDataReservation(long id);
    
    public TblReservationBill getDataReservationBill(long id);
    
    public RefReservationStatus getDataReservationStatus(int id);
    
    //--------------------------------------------------------------------------
    
    public TblReservationRoomTypeDetail getDataReservationRoomTypeDetail(long id);
    
    //--------------------------------------------------------------------------
    
    public List<TblReservationRoomPriceDetail> getAllDataReservationRoomPriceDetailByDetailDate(Date date);

    public TblReservationRoomTypeDetailRoomPriceDetail getDataReservationRoomTypeDetailRoomPriceDetail(long id);
    
    public TblReservationRoomTypeDetailRoomPriceDetail getDataReservationRoomTypeDetailRoomPriceDetailByIDRoomPriceDetail(long idRoomPriceDetail);
    
    //--------------------------------------------------------------------------
    
    public TblReservationTravelAgentDiscountDetail getDataReservationTravelAgentDiscountDetail(long id);

    public TblReservationRoomTypeDetailTravelAgentDiscountDetail getDataReservationRoomTypeDetailTravelAgentDiscountDetailByIDReservationRoomTypeDetail(long idRoomTypeDetail);

    public List<TblReservationRoomTypeDetailTravelAgentDiscountDetail> getAllDataReservationRoomTypeDetailTravelAgentDiscountDetailByIDReservationRoomTypeDetail(long idRoomTypeDetail);
    
    //--------------------------------------------------------------------------
    
//    public TblHotelInvoice insertDataHotelInvoice(TblHotelInvoice hotelInvoice, 
//            List<TblHotelInvoiceReceivable> hotelInvoiceReceivables);
//    
//    public boolean updateDataHotelInvoice(TblHotelInvoice hotelInvoice, 
//            List<TblHotelInvoiceReceivable> hotelInvoiceReceivables);
//    
//    public boolean deleteDataHotelInvoice(TblHotelInvoice hotelInvoice);
    
    public TblHotelInvoice insertDataHotelInvoice(TblReservationPaymentWithGuaranteePayment dataRPWGP);
    
    public TblHotelInvoice getDataHotelInvoice(long id);
    
    public List<TblHotelInvoice> getAllDataHotelInvoice();
    
    public List<TblHotelInvoice> getAllDataHotelInvoiceByIDPartnerNotNullAndIDHotelInvoiceType(int idHotelInvoiceType);
    
    //--------------------------------------------------------------------------
    
    public RefHotelInvoiceType getDataHotelInvoiceType(int id);
    
    public List<RefHotelInvoiceType> getAllDataHotelInvoiceType();
    
    //--------------------------------------------------------------------------
    
    public List<TblGuaranteeLetterItemDetail> getAllDataGuaranteeLetterItemDetailByIDGuaranteeLetter(long idGuaranteeLetter);
    
    //--------------------------------------------------------------------------
    
    public SysDataHardCode getDataSysDataHardCode(long id);
    
    public List<SysDataHardCode> getAllDataSysDataHardCode();
    
    //--------------------------------------------------------------------------
    
    public List<TblReservationPaymentWithGuaranteePayment> getAllDataReservationPaymentWithGuaranteePayment();
    
    //--------------------------------------------------------------------------
    
    public List<TblHotelFinanceTransactionHotelReceivable> getAllDataHotelFinanceTransactionHotelReceivableByIDHotelFinanceTransaction(long idHotelFinanceTransaction);
    
    public List<TblHotelFinanceTransactionHotelReceivable> getAllDataHotelFinanceTransactionHotelReceivableByIDHotelReceivable(long idHotelReceivable);
    
    public TblHotelFinanceTransaction getDataHotelFinanceTransaction(long id);
    
    public TblReservationPaymentWithGuaranteePayment getDataReservationPaymentWithGuaranteePaymentByIDHotelReceivable(long idHotelReceivable);
    
    public TblReservationPayment getDataResrevationPayment(long id);
    
    //--------------------------------------------------------------------------
    
    public List<TblReservationPaymentWithGuaranteePayment> getAllDataReservationPaymentWithGuaranteePaymentByIDPartner(long idPartner);
    
    //--------------------------------------------------------------------------
    public RefFinanceTransactionPaymentType getDataFinanceTransactionPaymentType(int id);

    public List<RefFinanceTransactionPaymentType> getAllDataFinanceTransactionPaymentType();
    
    //--------------------------------------------------------------------------
    
    public TblCompanyBalance getDataCompanyBalance(long id);
    
    public List<TblCompanyBalance> getAllDataCompanyBalance();
    
    //--------------------------------------------------------------------------
    
    public String getErrorMessage();
    
}
