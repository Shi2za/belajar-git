/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hotelfx.persistence.service;

import hotelfx.persistence.model.RefItemGuestType;
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
 * @author Andreas
 */
public interface FToolsManager {
    public TblItem insertDataTools(TblItem tools);
    public boolean updateDataTools(TblItem item);
    public boolean deleteDataTools(TblItem tblItem);
    //-------------------------------------------------
    public List<TblItem>getAllDataTools();
    public List<TblItemLocation>getAllDataToolsStock();
    public List<TblItemLocation>getAllDataItemLocationByIDItem(long idItem);
    public List<TblUnit>getAllDataUnit();
    public List<RefItemGuestType>getAllGuestType();
    //---------------------------------------------
     public TblItem getTools(long id);
     public List<TblLocationOfWarehouse> getWarehouseByIdLocation(long id);
     public List<TblRoom> getRoomByIdLocation(long id);
     public List<TblLocationOfLaundry> getLaundryByIdLocation(long id);
     public List<TblSupplier> getSupplierByIdLocation(long id);
     public List<TblLocationOfBin> getBinByIdLocation(long id);
    //---------------------------------------------
    public String getErrMessage();
}
