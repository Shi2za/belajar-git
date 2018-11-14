/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hotelfx.persistence.service;

import hotelfx.persistence.model.RefBankCardType;
import hotelfx.persistence.model.RefEdcstatus;
import hotelfx.persistence.model.RefEdctransactionStatus;
import java.util.List;
import hotelfx.persistence.model.TblBank;
import hotelfx.persistence.model.TblBankAccount;
import hotelfx.persistence.model.TblBankCard;
import hotelfx.persistence.model.TblBankEdc;
import hotelfx.persistence.model.TblBankEdcBankNetworkCard;
import hotelfx.persistence.model.TblBankEventCard;
import hotelfx.persistence.model.TblBankNetworkCard;
import hotelfx.persistence.model.TblCompanyBalanceBankAccount;
import hotelfx.persistence.model.TblEmployee;
import hotelfx.persistence.model.TblPeople;
import hotelfx.persistence.model.TblReservation;
import hotelfx.persistence.model.TblReservationBill;
import hotelfx.persistence.model.TblReservationPayment;
import hotelfx.persistence.model.TblReservationPaymentWithBankCard;
import hotelfx.persistence.model.TblRoomCardType;
import java.sql.Date;

/**
 *
 * @author ANDRI
 */
public interface FBankManager {
    
    public TblBank insertDataBank(TblBank bank, List<TblBankCard> bankCards);
    
    public boolean updateDataBank(TblBank bank, List<TblBankCard> bankCards);
    
    public boolean deleteDataBank(TblBank bank);
    
    public TblBank getBank(long id);
    
    public List<TblBank> getAllDataBank();
   
    //--------------------------------------------------------------------------
    
    public TblBankNetworkCard insertDataBankNetworkCard(TblBankNetworkCard bankNetworkCard);
    
    public boolean updateDataBankNetworkCard(TblBankNetworkCard bankNetworkCard);
    
    public boolean deleteDataBankNetworkCard(TblBankNetworkCard bankNetworkCard);
    
    public TblBankNetworkCard getBankNetworkCard(long id);
    
    public List<TblBankNetworkCard> getAllDataBankNetworkCard();
    
    //--------------------------------------------------------------------------
    
    public TblBankEdc insertDataBankEDC(TblBankEdc bankEdc, 
            List<TblBankEdcBankNetworkCard> edcNetworkCard);
    
    public boolean updateDataBankEDC(TblBankEdc bankEdc, 
            List<TblBankEdcBankNetworkCard> edcNetworkCard);
    
    public boolean deleteDataBankEDC(TblBankEdc bankEdc);
    
    public TblBankEdc getBankEDC(long id);
    
    public List<TblBankEdc> getAllDataBankEDC();
    
    //--------------------------------------------------------------------------
    
    public TblBankAccount getDataBankAccount(long id);
    
    public List<TblBankAccount> getAllDataBankAccount();
    
    public List<TblCompanyBalanceBankAccount> getAllDataCompanyBalanceBankAccountByIDCompanyBalance(long idBCompanyBalance);
    
    //--------------------------------------------------------------------------
    
    public TblBankEdcBankNetworkCard getDataBankEDCBankNetworkCard(long id);
    
    public List<TblBankEdcBankNetworkCard> getAllDataBankEDCBankNetworkCard();
    
    public List<TblBankEdcBankNetworkCard> getAllDataBankEDCBankNetworkCardByIDBankEDC(long idBankEDC);
    
    //--------------------------------------------------------------------------
    
    public TblBankEventCard insertDataBankEventCard(TblBankEventCard bankEventCard);
    
    public boolean updateDataBankEventCard(TblBankEventCard bankEventCard);
    
    public boolean deleteDataBankEventCard(TblBankEventCard bankEventCard);
    
    public TblBankEventCard getBankEventCard(long id);
    
    public List<TblBankEventCard> getAllDataBankEventCard();
    
    //--------------------------------------------------------------------------
    
    public boolean updateDataRoomCardType(TblRoomCardType roomCardType);
    
    public List<TblRoomCardType> getAllDataRoomCardType();
    
    //--------------------------------------------------------------------------
    
    public TblBankCard getDataBankCard(long id);
    
    public List<TblBankCard> getAllDataBankCard();
    
    public List<TblBankCard> getAllDataBankCardByIDBank(long idBank);
    
    //--------------------------------------------------------------------------
    
    public RefBankCardType getDataBankCardType(int id);
    
    public List<RefBankCardType> getAllDataBankCardType();
    
    //--------------------------------------------------------------------------
    
    public List<TblReservationPaymentWithBankCard> getAllDataReservationPaymentWithBankCardByIDEDCAndPaymentDateInRange(long idEDC, 
            Date beginPeiode, 
            Date endPeriode);
    
    //--------------------------------------------------------------------------
    
    public TblReservationPayment getDataReservationPayment(long id);
    
    public TblReservationBill getDataReservationBill(long id);
    
    public TblReservation getDataReservation(long id);
    
    public TblEmployee getDataEmployee(long id);
    
    public TblPeople getDataPeople(long id);
    
    //--------------------------------------------------------------------------
    public boolean updateSettleEDCDataReservationPaymentWithBankCard(List<TblReservationPaymentWithBankCard> rpwbcs);
    
    //--------------------------------------------------------------------------
    public List<TblReservationPaymentWithBankCard> getAllDataReservationPaymentWithBankCardByIDEDC(long idEDC);
    
    public RefEdctransactionStatus getDataEDCTransactionStatus(int id);
    
    public List<RefEdctransactionStatus> getAllDataEDCTransactionStatus();
    
    //--------------------------------------------------------------------------
    
    public String getErrorMessage();
    
}
