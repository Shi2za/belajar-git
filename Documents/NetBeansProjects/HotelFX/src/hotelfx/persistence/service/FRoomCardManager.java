/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hotelfx.persistence.service;

import hotelfx.persistence.model.TblItem;
import hotelfx.persistence.model.TblItemLocation;
import hotelfx.persistence.model.TblLocationOfBin;
import hotelfx.persistence.model.TblLocationOfLaundry;
import hotelfx.persistence.model.TblLocationOfWarehouse;
import hotelfx.persistence.model.TblRoom;
import hotelfx.persistence.model.TblSupplier;
import hotelfx.persistence.model.TblUnit;
import java.util.List;

/**
 *
 * @author ANDRI
 */
public interface FRoomCardManager {
    
    public TblItem insertDataRoomCard(TblItem roomCard);
    
    public boolean updateDataRoomCard(TblItem roomCard);
    
    public boolean deleteDataRoomCard(TblItem roomCard);
    
    public TblItem getDataRoomCard(long id);
    
    public TblItem getDataRoomCard();
    
    public List<TblItem> getAllDataRoomCard();
    
    public List<TblItemLocation>getAllDataRoomCardStock();
    
    //--------------------------------------------------------------------------
    
    public TblUnit getUnit(long id);
    
    public List<TblUnit> getAllDataUnit();
    
    //--------------------------------------------------------------------------
    
    public List<TblLocationOfWarehouse> getWarehouseByIdLocation(long id);
   
    public List<TblRoom> getRoomByIdLocation(long id);
    
    public List<TblLocationOfLaundry> getLaundryByIdLocation(long id);
    
    public List<TblSupplier> getSupplierByIdLocation(long id);
    
    public List<TblLocationOfBin> getBinByIdLocation(long id);
    
    //--------------------------------------------------------------------------
    
    public List<TblItemLocation> getAllDataItemLocationByIDItem(long idItem);
    
    //--------------------------------------------------------------------------
    
    public String getErrorMessage();
    
}
