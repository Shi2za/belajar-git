/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hotelfx.persistence.service;

import hotelfx.persistence.model.RefItemExpiredDateStatus;
import hotelfx.persistence.model.TblItem;
import hotelfx.persistence.model.TblItemExpiredDate;
import hotelfx.persistence.model.TblItemLocation;
import hotelfx.persistence.model.TblItemTypeHk;
import hotelfx.persistence.model.TblItemTypeWh;
import hotelfx.persistence.model.TblLocationOfBin;
import hotelfx.persistence.model.TblLocationOfLaundry;
import hotelfx.persistence.model.TblLocationOfWarehouse;
import hotelfx.persistence.model.TblRoom;
import hotelfx.persistence.model.TblSupplier;
import hotelfx.persistence.model.TblUnit;
import java.util.Date;
import java.util.List;

/**
 *
 * @author ANDRI
 */
public interface FItemAndAssetManager {
    
    public TblItem insertDataItem(TblItem item);

    public boolean updateDataItem(TblItem item);

    public boolean deleteDataItem(TblItem item);

    public TblItem getDataItem(long id);

    public List<TblItem> getAllDataItem();
    
    //--------------------------------------------------------------------------
    
    public TblUnit getDataUnit(long id);

    public List<TblUnit> getAllDataUnit();
    
    //--------------------------------------------------------------------------
    
    public List<TblItemLocation> getAllDataItemLocation();
    
    public List<TblItemLocation> getAllDataItemLocationByIDItem(long idItem);
    
    //--------------------------------------------------------------------------
    
    public TblRoom getDataRoomByIDLocation(long idLocation);
    
    public TblLocationOfWarehouse getDataLocationOfWarehouseByIDLocation(long idLocation);
    
    public TblLocationOfLaundry getDataLocationOfLaundryByIDLocation(long idLocation);
    
    public TblSupplier getDataSupplierByIDLocation(long idLocation);
    
    public TblLocationOfBin getDataLocationOfBinByIDLocation(long idLocation);
    
    //--------------------------------------------------------------------------
    
    public TblItemExpiredDate getDataItemExpiredDate(long id);

    public List<TblItemExpiredDate> getAllDataItemExpiredDate();
    
    public List<TblItemExpiredDate> getAllDataItemExpiredDateByIDItemAndItemExpiredDate(long idItem,
            Date itemExpiredDate);

    public List<TblItemExpiredDate> getAllDataItemExpiredDateByIDItemAndItemExpiredDateAndItemExpiredDateStatus(long idItem,
            Date itemExpiredDate,
            int idItemExpiredDateStatus);
    
    //--------------------------------------------------------------------------
    
    public RefItemExpiredDateStatus getDataItemExpiredDateStatus(int id);

    public List<RefItemExpiredDateStatus> getAllDataItemExpiredDateStatus();
    
    //--------------------------------------------------------------------------
    
    public TblItemTypeHk insertDataItemTypeHK(TblItemTypeHk itemTypeHK);

    public boolean updateDataItemTypeHK(TblItemTypeHk itemTypeHK);

    public boolean deleteDataItemTypeHK(TblItemTypeHk itemTypeHK);

    public TblItemTypeHk getDataItemTypeHK(long id);

    public List<TblItemTypeHk> getAllDataItemTypeHK();
    
    //--------------------------------------------------------------------------
    
    public TblItemTypeWh insertDataItemTypeWH(TblItemTypeWh itemTypeWH);

    public boolean updateDataItemTypeWH(TblItemTypeWh itemTypeWH);

    public boolean deleteDataItemTypeWH(TblItemTypeWh itemTypeWH);

    public TblItemTypeWh getDataItemTypeWH(long id);

    public List<TblItemTypeWh> getAllDataItemTypeWH();
    
    //--------------------------------------------------------------------------
    
    public String getErrorMessage();
    
}
