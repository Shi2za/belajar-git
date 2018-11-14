/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hotelfx.persistence.service;

import hotelfx.persistence.model.RefEndOfDayDataStatus;
import hotelfx.persistence.model.RefReservationCheckInOutStatus;
import hotelfx.persistence.model.RefReservationStatus;
import hotelfx.persistence.model.RefRoomStatus;
import hotelfx.persistence.model.SysCurrentHotelDate;
import hotelfx.persistence.model.SysDataHardCode;
import hotelfx.persistence.model.TblCustomer;
import hotelfx.persistence.model.TblPeople;
import hotelfx.persistence.model.TblReservation;
import hotelfx.persistence.model.TblReservationAdditionalItem;
import hotelfx.persistence.model.TblReservationAdditionalService;
import hotelfx.persistence.model.TblReservationBill;
import hotelfx.persistence.model.TblReservationBrokenItem;
import hotelfx.persistence.model.TblReservationCheckInOut;
import hotelfx.persistence.model.TblReservationRoomPriceDetail;
import hotelfx.persistence.model.TblReservationRoomTypeDetail;
import hotelfx.persistence.model.TblReservationRoomTypeDetailRoomPriceDetail;
import hotelfx.persistence.model.TblReservationRoomTypeDetailTravelAgentDiscountDetail;
import hotelfx.persistence.model.TblReservationTravelAgentDiscountDetail;
import hotelfx.persistence.model.TblRoom;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

/**
 *
 * @author ANDRI
 */
public interface FEndOfDayManager {
    
    public SysCurrentHotelDate getDataSysCurrentHotelDate(int id);
    
    public boolean closingEndOfDay(
            SysCurrentHotelDate schd, 
            List<TblReservationRoomTypeDetailRoomPriceDetail> rrtdrpds, 
            List<TblReservationAdditionalItem> rAdditionalItems, 
            List<TblReservationAdditionalService> rAdditionalServices, 
            List<TblReservationBrokenItem> rBrokenItems);
    
    public RefEndOfDayDataStatus getDataEoDStatusData(int id);
    
    //-------------------------------------------------------------------------
    
    public List<TblReservation> getAllDataReservation();
    
    public List<TblReservation> getAllDataReservationByIDReservationStatus(int idStatus);
    
    public RefReservationStatus getDataReservationStatus(int id);
    
    //--------------------------------------------------------------------------
    
    public List<TblReservationRoomTypeDetailRoomPriceDetail> getAllDataReservationRoomTypeDetailRoomPriceDetailByIDReservationRoomTypeDetail(long idReservationRoomTypeDetail);
    
    public List<TblReservationRoomTypeDetailRoomPriceDetail> getAllDataReservationRoomTypeDetailRoomPriceDetailByDetailDate(Date detailDate);
    
    public TblReservationRoomPriceDetail getDataReservationRoomPriceDetail(long id);
    
    //--------------------------------------------------------------------------
    
    public List<TblReservationRoomTypeDetailTravelAgentDiscountDetail> getAllDataReservationRoomTypeDetailTravelAgentDiscountDetailByIDReservationRoomTypeDetail(long idRoomTypeDetail);
    
    public TblReservationTravelAgentDiscountDetail getDataReservationTravelAgentDiscountDetail(long id);
    
    //--------------------------------------------------------------------------
    
    public TblReservationRoomTypeDetail getDataReservationRoomTypeDetail(long id);
    
    public List<TblReservationRoomTypeDetail> getAllDataReservationRoomTypeDetailByIDReservation(long idReservation);
    
    public List<TblReservationRoomTypeDetail> getAllDataReservationRoomTypeDetailByFirstCheckInDateAndEndCheckOutDate(Timestamp firstDate, Timestamp endDate);
    
    public TblReservationCheckInOut getDataReservationCheckInOut(long id);
    
    public RefReservationCheckInOutStatus getDataReservationCheckInOutStatus(int id);
    
    //--------------------------------------------------------------------------
    
    public List<TblReservationRoomPriceDetail> getAllDataReservationRoomPriceDetailByDetailDate(Date detailDate);
    
    public TblReservationRoomTypeDetailRoomPriceDetail getDataReservationRoomTypeDetailRoomPriceDetailByIDRoomPriceDetail(long idRoomPriceDetail);
    
    //--------------------------------------------------------------------------
            
    public List<TblRoom> getAllDataRoom();
    
    public TblRoom getDataRoom(long id);
    
    public RefRoomStatus getDataRoomStatus(int id);
    
    //--------------------------------------------------------------------------
    
    public TblReservation getDataReservation(long id);
    
    public TblCustomer getDataCustomer(long id);
    
    public TblPeople getDataPeople(long id);
    
    //--------------------------------------------------------------------------
    
    public List<TblReservationAdditionalItem> getAllDataReservationAdditionalItemByIDReservationRoomTypeDetail(long idReservationRoomTypeDetail);
    
    //--------------------------------------------------------------------------
    
    public List<TblReservationAdditionalService> getAllDataReservationAdditionalServiceByIDReservationRoomTypeDetail(long idReservationRoomTypeDetail);
    
    public List<TblReservationAdditionalService> getAllDataReservationAdditionalServiceByIDRoomServiceAndAdditionalDate(long idRoomService, Date additionalDate);
    
    //--------------------------------------------------------------------------
    
    public List<TblReservationBrokenItem> getAllDataReservationBrokenItemByIDReservationRoomTypeDetail(long idReservationRoomTypeDetail);
    
    public List<TblReservationBrokenItem> getAllDataReservationBrokenItemByCreateDate(Date createDate);
    
    //--------------------------------------------------------------------------
    
    public TblReservationBill getDataReservationBillByIDReservationAndIDReservationBillType(
            long idReservation, 
            int idReservationBillType);
    //--------------------------------------------------------------------------
    
    public SysDataHardCode getDataSysDataHardCode(long id);
    
    //--------------------------------------------------------------------------
    
    public String getErrorMessage();
    
}
