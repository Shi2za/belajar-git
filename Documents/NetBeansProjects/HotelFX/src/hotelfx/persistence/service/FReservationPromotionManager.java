/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hotelfx.persistence.service;

import hotelfx.persistence.model.RefBankCardType;
import hotelfx.persistence.model.RefVoucherStatus;
import hotelfx.persistence.model.SysDataHardCode;
import hotelfx.persistence.model.TblBank;
import hotelfx.persistence.model.TblBankCard;
import hotelfx.persistence.model.TblBankEventCard;
import hotelfx.persistence.model.TblHotelEvent;
import hotelfx.persistence.model.TblItem;
import hotelfx.persistence.model.TblReservationVoucher;
import hotelfx.persistence.model.TblRoomService;
import hotelfx.persistence.model.TblRoomType;
import java.util.List;

/**
 *
 * @author ANDRI
 */
public interface FReservationPromotionManager {
    
    public TblReservationVoucher insertDataVoucher(TblReservationVoucher voucher);
    
    public boolean insertDataVoucher(List<TblReservationVoucher> vouchers);
    
    public boolean updateDataVoucher(TblReservationVoucher voucher);
    
    public boolean updateDataVoucherToReadyToUsed(TblReservationVoucher voucher);
    
    public boolean updateDataVoucherToObsolete(TblReservationVoucher voucher);
    
    public boolean deleteDataVoucher(TblReservationVoucher voucher);
    
    public TblReservationVoucher getVoucher(long id);
    
    public List<TblReservationVoucher> getAllDataVoucher();
    
    //--------------------------------------------------------------------------
    
    public RefVoucherStatus getDataVoucherStatus(int id);
    
    //--------------------------------------------------------------------------
    
    public TblHotelEvent insertDataHotelEvent(TblHotelEvent hotelEvent);
    
    public boolean updateDataHotelEvent(TblHotelEvent hotelEvent);
    
    public boolean deleteDataHotelEvent(TblHotelEvent hotelEvent);
    
    public TblHotelEvent getHotelEvent(long id);
    
    public List<TblHotelEvent> getAllDataHotelEvent();
    
    //--------------------------------------------------------------------------
    
    public TblBankEventCard insertDataBankEventCard(TblBankEventCard bankEventCard);
    
    public boolean updateDataBankEventCard(TblBankEventCard bankEventCard);
    
    public boolean deleteDataBankEventCard(TblBankEventCard bankEventCard);
    
    public TblBankEventCard getBankEventCard(long id);
    
    public List<TblBankEventCard> getAllDataBankEventCard();
    
    //--------------------------------------------------------------------------
    
    public TblBank getDataBank(long id);
    
    public List<TblBank> getAllDataBank();
    
    //--------------------------------------------------------------------------
    
    public boolean updateDataDiscountable(List<TblRoomType> roomTypes, 
            List<TblRoomService> roomServices, 
            List<TblItem> items);
    
    public List<TblRoomType> getAllDataRoomType();
    
    public List<TblRoomService> getAllDataRoomService();
    
    public List<TblItem> getAllDataItem();
    
    //--------------------------------------------------------------------------
    
    public TblBankCard getDataBankCard(long id);
    
    public List<TblBankCard> getAllDataBankCard();
    
    public List<TblBankCard> getAllDataBankCardByIDBank(long idBank);
    
    //--------------------------------------------------------------------------
    
    public RefBankCardType getDataBankCardType(int id);
    
    public List<RefBankCardType> getAllDataBankCardType();
    
    //--------------------------------------------------------------------------
    
    public SysDataHardCode getDataSysDataHardCode(long id);
    
    public List<SysDataHardCode> getAllDataSysDataHardCode();
    
    //--------------------------------------------------------------------------
    
    public String getErrorMessage();
    
}
