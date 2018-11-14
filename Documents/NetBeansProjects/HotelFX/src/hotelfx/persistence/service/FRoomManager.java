/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hotelfx.persistence.service;

import hotelfx.helper.ClassDataMutation;
import hotelfx.helper.ClassDataTransferItem;
import hotelfx.helper.ClassInputLocation;
import hotelfx.persistence.model.RefItemMutationType;
import hotelfx.persistence.model.RefItemObsoleteBy;
import hotelfx.persistence.model.RefLocationType;
import hotelfx.persistence.model.RefRoomCleanStatus;
import hotelfx.persistence.model.RefRoomStatus;
import hotelfx.persistence.model.SysDataHardCode;
import hotelfx.persistence.model.TblBuilding;
import hotelfx.persistence.model.TblEmployee;
import hotelfx.persistence.model.TblFloor;
import hotelfx.persistence.model.TblGroup;
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
import hotelfx.persistence.model.TblJob;
import hotelfx.persistence.model.TblLocation;
import hotelfx.persistence.model.TblLocationOfBin;
import hotelfx.persistence.model.TblLocationOfLaundry;
import hotelfx.persistence.model.TblLocationOfWarehouse;
import hotelfx.persistence.model.TblPeople;
import hotelfx.persistence.model.TblPropertyBarcode;
import hotelfx.persistence.model.TblRoom;
import hotelfx.persistence.model.TblRoomService;
import hotelfx.persistence.model.TblRoomType;
import hotelfx.persistence.model.TblRoomTypeItem;
import hotelfx.persistence.model.TblRoomTypeRoomService;
import hotelfx.persistence.model.TblSupplier;
import hotelfx.persistence.model.TblUnit;
import hotelfx.view.feature_room.room_transfer_item.RoomTransferItemController;
import java.util.List;
import javafx.collections.ObservableList;

/**
 *
 * @author ANDRI
 */
public interface FRoomManager {

    public TblRoom insertDataRoom(TblRoom room);

    public boolean updateDataRoom(TblRoom room);

    public boolean deleteDataRoom(TblRoom room);

    public TblRoom getRoom(long id);

    public List<TblRoom> getAllDataRoom();

    //--------------------------------------------------------------------------
    public TblBuilding getBuilding(long id);

    public List<TblBuilding> getAllDataBuilding();

    public TblFloor getFloor(long id);

    public List<TblFloor> getAllDataFloor();

    public TblJob getJob(long id);

    public List<TblJob> getAllDataJob();
    
    public TblGroup getDataGroup(long id);

    public List<TblGroup> getAllDataGroup();
    
    //--------------------------------------------------------------------------
    public TblRoomType insertDataRoomType(TblRoomType roomType,
            List<TblRoomTypeItem> fabList,
            List<TblRoomTypeItem> amenityList,
            List<TblRoomTypeItem> propertyBarcodeList,
            List<TblRoomTypeRoomService> roomServiceList);
    
    public TblRoomType insertDataRoomType(TblRoomType roomType,
            List<TblRoomTypeItem> itemList,
            List<TblRoomTypeRoomService> roomServiceList);

    public boolean updateDataRoomType(TblRoomType roomType,
            List<TblRoomTypeItem> fabList,
            List<TblRoomTypeItem> amenityList,
            List<TblRoomTypeItem> propertyBarcodeList,
            List<TblRoomTypeRoomService> roomServiceList);
    
    public boolean updateDataRoomType(TblRoomType roomType,
            List<TblRoomTypeItem> itemList,
            List<TblRoomTypeRoomService> roomServiceList);

    public boolean deleteDataRoomType(TblRoomType roomType);

    public TblRoomType getRoomType(long id);

    public List<TblRoomType> getAllDataRoomType();

    //--------------------------------------------------------------------------
    public TblRoomService insertDataRoomService(TblRoomService roomService);

    public boolean updateDataRoomService(TblRoomService roomService);

    public boolean deleteDataRoomService(TblRoomService roomService);

    public TblRoomService getRoomService(long id);

    public List<TblRoomService> getAllDataRoomService();

    //--------------------------------------------------------------------------
    public TblLocation getLocation(long id);
    
    public TblLocation getLocationByIDLocationType(int id);

    public List<TblLocation> getAllDataLocation();

    //--------------------------------------------------------------------------
    public RefRoomStatus getRoomStatus(int id);

    public List<RefRoomStatus> getAllDataRoomStatus();

    //--------------------------------------------------------------------------
    public RefRoomCleanStatus getRoomCleanStatus(int id);

    public List<RefRoomCleanStatus> getAllDataRoomCleanStatus();

    //--------------------------------------------------------------------------
    public TblRoomTypeItem getRoomTypeFABList(long idRoomTypeItem);

    public List<TblRoomTypeItem> getAllDataRoomTypeFABList(long idRoomType);

    //--------------------------------------------------------------------------
    public TblRoomTypeItem getRoomTypeAmenityList(long idRoomTypeItem);

    public List<TblRoomTypeItem> getAllDataRoomTypeAmenityList(long idRoomType);

    //--------------------------------------------------------------------------
    public TblRoomTypeItem getRoomTypePropertyBarcodeList(long idRoomTypeItem);

    public List<TblRoomTypeItem> getAllDataRoomTypePropertyBarcodeList(long idRoomType);

    //--------------------------------------------------------------------------
    
    public TblRoomTypeItem getRoomTypeItemList(long idRoomTypeItem);

    public List<TblRoomTypeItem> getAllDataRoomTypeItemList(long idRoomType);
    
    //--------------------------------------------------------------------------
    
    public TblRoomTypeRoomService getRoomTypeRoomServiceList(long idRoomTypeRoomService);

    public List<TblRoomTypeRoomService> getAllDataRoomTypeRoomServiceList(long idRoomType);

    //--------------------------------------------------------------------------
    public TblItem getItemFoodAndBeverage(long id);

    public List<TblItem> getAllDataItemFoodAndBeverage();

    //--------------------------------------------------------------------------
    public TblItem getItemAmenity(long id);

    public List<TblItem> getAllDataItemAmenity();

    //--------------------------------------------------------------------------
    public TblItem getItemPropertyBarcode(long id);

    public List<TblItem> getAllDataItemPropertyBarcode();

    //--------------------------------------------------------------------------
    public TblPropertyBarcode getPropertyBarcode(long id);

    public List<TblPropertyBarcode> getAllDataPropertyBarcode();

    //--------------------------------------------------------------------------
    public List<TblPropertyBarcode> getAllDataPropertyBarcodeByIdItem(long id);

    public List<TblLocationOfWarehouse> getAllDataWarehouse();

    public List<TblLocationOfLaundry> getAllDataLaundry();

    public boolean updateDataRoomTransferItem(TblItemLocation sourceItemLocation,
            TblItemMutationHistory mutation, ClassInputLocation destinationLocation,
            ObservableList<TblPropertyBarcode> propertyBarcode);

    public List<TblItemLocation> getDataItemLocationByItemAndLocation(long idLoc, long idItem);

    public List<RefItemObsoleteBy> getAllDataObsoleteBy();

    public List<TblItemLocation> getAllDataItemLocation(long id);

    public List<RefLocationType> getAllDataTypeLocation();

    public TblItemLocation getDataItemLocation(long id);

    public TblItem getDataItem(long id);
    
    public List<TblItem> getAllDataItem();

    //--------------------------------------------------------------------------
    public List<ClassDataMutation> getAllDataMutationHistory();

    public List<TblLocationOfWarehouse> getWarehouseByIdLocation(long id);

    public List<TblRoom> getRoomByIdLocation(long id);

    public List<TblLocationOfLaundry> getLaundryByIdLocation(long id);

    public List<ClassDataTransferItem> getAllDataTransferItem(long id);

//    public boolean updateDataRoomTransferItem(ClassDataTransferItem transferItem, TblItemMutationHistory mutation, ClassInputLocation destinationLocation);
    public boolean updateDataRoomTransferItem(ClassDataTransferItem transferItem, 
            TblItemMutationHistory mutation, 
            ClassInputLocation destinationLocation, 
            String status);

    public boolean deleteDataItemLocationPropertyBarcode(TblItemLocationPropertyBarcode itemLocationPropertyBarcode);

    public TblPropertyBarcode getDataPropertyBarcode(long id);

    public TblItemLocationPropertyBarcode getDataItemLocationPropertyBarcode(long id);
    
    public List<TblItemLocationPropertyBarcode> getAllDataItemLocationPropertyBarcodeByIDItemLocation(long idItemLocation);

    public List<TblSupplier> getSupplierByIdLocation(long id);

    public List<TblLocationOfBin> getBinByIdLocation(long id);
    
    public List<TblSupplier> getAllDataSupplier();
    
    public List<TblLocationOfBin> getAllDataBin();
    
    public RefItemMutationType getMutationType(int id);

    //--------------------------------------------------------------------------
    public TblItemLocationItemExpiredDate getDataItemLocationItemExpiredDate(long id);
    
    public List<TblItemLocationItemExpiredDate> getAllDataItemLocationItemExpiredDateByIDItemLocation(long idItemLocation);
    
    public TblItemExpiredDate getDataItemExpiredDate(long id);
    
    public List<TblItemExpiredDate> getAllDataItemExpiredDateByIDItem(long idItem);
     
    //--------------------------------------------------------------------------
    
    public TblUnit getDataUnit(long id);
    
    //--------------------------------------------------------------------------
    
    public TblItemTypeHk getDataItemTypeHK(long id);
    
    public List<TblItemTypeHk> getAllDataItemTypeHK();
    
    public TblItemTypeWh getDataItemTypeWH(long id);
    
    public List<TblItemTypeWh> getAllDataItemTypeWH();
    
    //--------------------------------------------------------------------------
    public List<TblItemMutationHistory> getAllDataItemMutationHistory();
    
    public TblItemMutationHistoryPropertyBarcode getDataItemMutationHistoryPropertyBarcodeByIDItemMutationHistory(long idItemMutation);
    
    public List<TblItemMutationHistoryItemExpiredDate> getAllDataItemMutationHistoryItemExpiredDateByIDItemMutationHistory(long idItemMutation);
    
    //--------------------------------------------------------------------------
    
    public TblEmployee getDataEmployee(long id);
    
    public List<TblEmployee> getAllDataEmployee();
    
    public TblPeople getDataPeople(long id);
    
    //--------------------------------------------------------------------------
    
    public SysDataHardCode getDataSysDataHardCode(long id);
    
    public List<SysDataHardCode> getAllDataSysDataHardCode();
    
    //--------------------------------------------------------------------------
    
    public String getErrorMessage();

}
