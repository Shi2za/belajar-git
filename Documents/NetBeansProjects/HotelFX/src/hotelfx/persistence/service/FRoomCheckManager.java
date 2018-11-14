/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hotelfx.persistence.service;

import hotelfx.persistence.model.RefItemMutationType;
import hotelfx.persistence.model.RefItemObsoleteBy;
import hotelfx.persistence.model.RefItemType;
import hotelfx.persistence.model.RefLocationType;
import hotelfx.persistence.model.RefReservationBillType;
import hotelfx.persistence.model.RefReservationCheckInOutStatus;
import hotelfx.persistence.model.RefReservationStatus;
import hotelfx.persistence.model.RefRoomCleanStatus;
import hotelfx.persistence.model.RefRoomStatus;
import hotelfx.persistence.model.SysCurrentHotelDate;
import hotelfx.persistence.model.SysDataHardCode;
import hotelfx.persistence.model.TblBuilding;
import hotelfx.persistence.model.TblCustomer;
import hotelfx.persistence.model.TblEmployee;
import hotelfx.persistence.model.TblFixedTangibleAsset;
import hotelfx.persistence.model.TblFloor;
import hotelfx.persistence.model.TblItem;
import hotelfx.persistence.model.TblItemExpiredDate;
import hotelfx.persistence.model.TblItemLocation;
import hotelfx.persistence.model.TblItemLocationItemExpiredDate;
import hotelfx.persistence.model.TblItemLocationPropertyBarcode;
import hotelfx.persistence.model.TblItemMutationHistory;
import hotelfx.persistence.model.TblItemMutationHistoryItemExpiredDate;
import hotelfx.persistence.model.TblItemMutationHistoryPropertyBarcode;
import hotelfx.persistence.model.TblItemTypeHk;
import hotelfx.persistence.model.TblItemTypeWh;
import hotelfx.persistence.model.TblLocation;
import hotelfx.persistence.model.TblLocationOfBin;
import hotelfx.persistence.model.TblLocationOfLaundry;
import hotelfx.persistence.model.TblLocationOfWarehouse;
import hotelfx.persistence.model.TblPeople;
import hotelfx.persistence.model.TblPropertyBarcode;
import hotelfx.persistence.model.TblReservation;
import hotelfx.persistence.model.TblReservationAdditionalItem;
import hotelfx.persistence.model.TblReservationAdditionalService;
import hotelfx.persistence.model.TblReservationBrokenItem;
import hotelfx.persistence.model.TblReservationCheckInOut;
import hotelfx.persistence.model.TblReservationRoomPriceDetail;
import hotelfx.persistence.model.TblReservationRoomTypeDetail;
import hotelfx.persistence.model.TblReservationRoomTypeDetailItem;
import hotelfx.persistence.model.TblReservationRoomTypeDetailRoomPriceDetail;
import hotelfx.persistence.model.TblRoom;
import hotelfx.persistence.model.TblRoomCheck;
import hotelfx.persistence.model.TblRoomCheckHouseKeepingAttendance;
import hotelfx.persistence.model.TblRoomCheckHouseKeepingAttendanceDetail;
import hotelfx.persistence.model.TblRoomCheckItemMutationHistory;
import hotelfx.persistence.model.TblRoomService;
import hotelfx.persistence.model.TblRoomStatusChangeHistory;
import hotelfx.persistence.model.TblRoomStatusDetail;
import hotelfx.persistence.model.TblRoomType;
import hotelfx.persistence.model.TblRoomTypeItem;
import hotelfx.persistence.model.TblSupplier;
import hotelfx.persistence.model.TblUnit;
import hotelfx.view.feature_room_check.room_status.CustomerBrokenItemController;
import java.sql.Date;
import java.sql.Timestamp;
import java.util.List;

/**
 *
 * @author ANDRI
 */
public interface FRoomCheckManager {

    public TblRoomCheck insertDataRoomCheck(TblRoomCheck roomCheck,
            List<TblRoomCheckItemMutationHistory> dataIns,
            List<TblRoomCheckItemMutationHistory> dataOuts,
            List<TblItemMutationHistoryPropertyBarcode> itemMutationHistoryPropertyBarcodes,
            List<TblItemMutationHistoryItemExpiredDate> itemMutationHistoryItemExpiredDates);

    public boolean updateDataRoomCheck(TblRoomCheck roomCheck,
            List<TblRoomCheckItemMutationHistory> dataIns,
            List<TblRoomCheckItemMutationHistory> dataOuts,
            List<TblItemMutationHistoryPropertyBarcode> itemMutationHistoryPropertyBarcodes,
            List<TblItemMutationHistoryItemExpiredDate> itemMutationHistoryItemExpiredDates);

    public boolean deleteDataRoomCheck(TblRoomCheck roomCheck);

    public TblRoomCheck getDataRoomCheck(long id);

    public List<TblRoomCheck> getAllDataRoomCheck();

    //--------------------------------------------------------------------------
    public TblRoom getDataRoom(long id);

    public List<TblRoom> getAllDataRoom();

    //--------------------------------------------------------------------------
    public TblRoomType getDataRoomType(long id);

    public RefRoomStatus getDataRoomStatus(int id);

    public List<RefRoomStatus> getAllDataRoomStatus();

    public RefRoomCleanStatus getDataRoomCleanStatus(int id);

    //--------------------------------------------------------------------------
    public TblEmployee getDataEmployee(long id);

    public List<TblEmployee> getAllDataEmployee();

    //--------------------------------------------------------------------------
    public TblPeople getDataPeople(long id);

    public List<TblPeople> getAllDataPeople();

    //--------------------------------------------------------------------------
    public TblRoom getDataRoomByIDLocation(long idLocation);

    public TblLocationOfWarehouse getDataWarehouseByIDLocation(long idLcoation);

    public TblLocationOfLaundry getDataLaundryByIDLocation(long idLocation);

    public TblSupplier getDataSupplierByIDLocation(long idLocation);

    public TblLocationOfBin getDataBinByIDLocation(long idLocation);

    public TblLocationOfBin getDataBin();

    //--------------------------------------------------------------------------
    public List<TblRoomCheckItemMutationHistory> getAllDataRoomCheckItemMutationHistoryByIDRoomCheck(long idRoomCheck);

    //--------------------------------------------------------------------------
    public TblItemMutationHistory getDataItemMutationHistory(long id);

    public TblItem getDataItem(long id);

    public List<TblItem> getAllDataItem();

    public TblLocation getDataLocation(long id);

    public TblBuilding getDataBuilding(long id);

    public TblFloor getDataFloor(long id);

    public RefLocationType getDataLocationType(int id);

    public RefItemMutationType getDataMutationType(int id);

    public RefItemObsoleteBy getDataObsoleteBy(int id);

    //--------------------------------------------------------------------------
    public List<RefLocationType> getAllDataLocationType();

    public List<TblLocationOfWarehouse> getAllDataWarehouse();

    public List<TblLocationOfLaundry> getAllDataLaundry();

    public List<TblLocationOfBin> getAllDataBin();

    public List<TblItemLocation> getAllDataItemLocationByIDLocation(long idLocation);

    //--------------------------------------------------------------------------
    public RefItemType getDataItemType(int id);

    public TblPropertyBarcode getDataPropertyBarcode(long id);

    public TblFixedTangibleAsset getDataFixedTangibleAsset(long id);

    public TblItemLocation getDataItemLocationByIDLocationAndIDItem(long idLocation, long idItem);

    public List<TblItemLocation> getAllDataItemLocationByIDLocationTypeAndItemQuantityNotZero(int idLocationType);

    public List<TblItemLocationPropertyBarcode> getAllDataItemLocationPropertyBarcodeByIDItemLocation(long idItemLocation);

    public List<TblItemMutationHistoryPropertyBarcode> getAllDataItemMutationHistoryPropertyBarcodeByIDItemMutationHistory(long idItemMutationHistory);

    //--------------------------------------------------------------------------
    public List<RefItemMutationType> getAllDataItemMutationType();

    public List<RefItemObsoleteBy> getAllDataItemObsoleteBy();

    //--------------------------------------------------------------------------
    public TblItemMutationHistoryItemExpiredDate getDataItemMutationHistoryItemExpiredDate(long id);

    public List<TblItemMutationHistoryItemExpiredDate> getAllDataItemMutationHistoryItemExpiredDate();

    public List<TblItemLocationItemExpiredDate> getAllDataItemLocationItemExpiredDateByIDItemLocation(long idItemLocation);

    public List<TblItemMutationHistoryItemExpiredDate> getAllDataItemMutationHistoryItemExpiredDateByIDItemMutationHistory(long idItemMutationHistory);

    //--------------------------------------------------------------------------
    public TblItemExpiredDate getDataItemExpiredDate(long id);

    public List<TblItemExpiredDate> getAllDataItemExpiredDate();

    public List<TblItemExpiredDate> getAllDataItemExpiredDateByIDItem(long idItem);

    //--------------------------------------------------------------------------
    public boolean updateDataRoomStatus(TblRoom room);

    public boolean updateDataRoomStatusDetail(TblRoom room,
            TblRoomStatusDetail roomStatusDetail);

    //--------------------------------------------------------------------------
    public TblRoomStatusDetail insertDataRoomStatusDetail(TblRoomStatusDetail roomStatusDetail);

    public boolean updateDataRoomStatusDetail(TblRoomStatusDetail roomStatusDetail);

    public boolean deleteDataRoomStatusDetail(TblRoomStatusDetail roomStatusDetail);

    public TblRoomStatusDetail getDataRoomStatusDetail(long id);

    public List<TblRoomStatusDetail> getAllDataRoomStatusDetail();

    public List<TblRoomStatusDetail> getAllDataRoomStatusDetailByIDRoom(long idRoom);

    public List<TblRoomStatusDetail> getAllDataRoomStatusDetailByIDRoomAndDetailDateBetween(long idRoom,
            Timestamp beginDate,
            Timestamp endDate);

    //--------------------------------------------------------------------------
    public TblRoomStatusChangeHistory getDataRoomStatusChangeHistory(long id);

    public List<TblRoomStatusChangeHistory> getAllDataRoomStatusChangeHistory();

    //--------------------------------------------------------------------------
    public TblUnit getDataUnit(long id);

    //--------------------------------------------------------------------------
    public List<TblRoomTypeItem> getAllDataRoomTypeItemByIDRoomType(long idRoomType);

    //--------------------------------------------------------------------------
    public List<TblReservationRoomPriceDetail> getAllDataReservationRoomPriceDetailByDetailDate(Date date);

    public TblReservationRoomTypeDetailRoomPriceDetail getReservationRoomTypeDetailRoomPriceDetailByIDRoomPriceDetail(long idRoomPriceDetail);

    public TblReservationRoomTypeDetail getDataReservationRoomTypeDetail(long id);

    public TblReservationCheckInOut getDataReservationCheckInOut(long id);

    public TblReservation getDataReservation(long id);

    public TblCustomer getDataCustomer(long id);

    public RefReservationCheckInOutStatus getDataReservationCheckInOutStatus(int id);

    public RefReservationStatus getDataReservationStatus(int id);

    public List<TblReservationRoomTypeDetailItem> getAllDataReservationRoomTypeDetailItemByIDReservationRoomTypeDetail(long idRRTD);

    public List<TblReservationAdditionalItem> getAllDataReservationAdditionalItemByIDReservationRoomTypeDetailAndAdditionalDate(long idRRTD, Date additionalDate);

    public List<TblRoom> getAllDataRoomByIDRoomType(long idRoomType);

    public List<TblRoom> getAllDataRoomByIDRoomStatus(long idRoomStatus);

    public SysCurrentHotelDate getDataCurrentHotelDate(int id);

    //--------------------------------------------------------------------------
    public boolean updateDataRRTDReservationCheckInOutRoom(List<TblReservationRoomTypeDetail> rrtds);

    //--------------------------------------------------------------------------
    public SysDataHardCode getDataSysDataHardCode(long id);

    //--------------------------------------------------------------------------
    public TblReservationBrokenItem insertDataReservationBrokenItem(
            TblReservationBrokenItem brokenItem, 
            CustomerBrokenItemController.ClassItemLocation classItemLocation);
    
    public TblLocation getDataLocationByIDLocationTypeAndFirstData(int idLocationType);
    
    //--------------------------------------------------------------------------
    public TblReservationAdditionalService insertDataReservationAdditionalService(TblReservationAdditionalService ras);
    
    //--------------------------------------------------------------------------
    public TblRoomService getDataRoomService(long id);
    
    public RefReservationBillType getDataReservationBillType(int id);
    
    //--------------------------------------------------------------------------
    public TblItemTypeHk getDataItemTypeHK(long id);

    public List<TblItemTypeHk> getAllDataItemTypeHK();

    public TblItemTypeWh getDataItemTypeWH(long id);

    public List<TblItemTypeWh> getAllDataItemTypeWH();

    //--------------------------------------------------------------------------
    //------------------WORK SHEET ATTENDANCE -----------------------------------
    public List<TblRoomCheckHouseKeepingAttendance> getAllHouseKeepingAttendance();

    public boolean insertWorkSheetAttendance(List<TblRoomCheckHouseKeepingAttendance> listWorkSheet);

    //--------------------------------------------------------------------------
    public TblRoomCheckHouseKeepingAttendance getDataRoomCheckHouseKeepingAttendanceByIDRoomAndIDRoomStatusAfterIsNull(long idRoom);
    
    public boolean updateItemCodeWorkSheet(List<TblItem>list);
    //--------------------------------------------------------------------------
    public List<TblReservationAdditionalItem> getAllDataReservationAdditionalItemByIDReservationRoomTypeDetailAdditionalDateAndBillType(long idRRTD,
    Date additionalDate);
    public boolean insertHouseKeepingAttendanceDetail(List<TblRoomCheckHouseKeepingAttendanceDetail> listRoomCheckHouseKeepingAttendanceDetail,TblRoomCheckHouseKeepingAttendance tblRoomCheckHouseKeepingAttendance);
    
    //--------------------------------------------------------------------------
    
    public String getErrorMessage();

}
