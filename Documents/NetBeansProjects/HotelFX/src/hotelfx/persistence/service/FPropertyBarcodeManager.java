/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hotelfx.persistence.service;

import hotelfx.persistence.model.RefFixedTangibleAssetDepreciationStatus;
import hotelfx.persistence.model.RefItemGuestType;
import hotelfx.persistence.model.RefItemType;
import hotelfx.persistence.model.TblFixedTangibleAsset;
import hotelfx.persistence.model.TblItem;
import hotelfx.persistence.model.TblItemLocation;
import hotelfx.persistence.model.TblItemLocationPropertyBarcode;
import hotelfx.persistence.model.TblLocation;
import hotelfx.persistence.model.TblLocationOfBin;
import hotelfx.persistence.model.TblLocationOfLaundry;
import hotelfx.persistence.model.TblLocationOfWarehouse;
import hotelfx.persistence.model.TblPropertyBarcode;
import hotelfx.persistence.model.TblRoom;
import hotelfx.persistence.model.TblSupplier;
import hotelfx.persistence.model.TblUnit;
import java.util.List;

/**
 *
 * @author ANDRI
 */
public interface FPropertyBarcodeManager {
    
    public TblPropertyBarcode insertDataPropertyBarcode(TblPropertyBarcode propertyBarcode);
    
    public boolean updateDataPropertyBarcode(TblPropertyBarcode propertyBarcode);
    
    public boolean deleteDataPropertyBarcode(TblPropertyBarcode propertyBarcode);
    
    public TblPropertyBarcode getPropertyBarcode(long id);
    
    public List<TblPropertyBarcode> getAllDataPropertyBarcode();
    
    public List<TblItemLocationPropertyBarcode>getAllDataPropertyBarcodeStock();
    
    //--------------------------------------------------------------------------
    
    public TblItem insertDataItem(TblItem property);
    
    public boolean updateDataItem(TblItem property);
    
    public boolean deleteDataItem(TblItem property);
    
    public TblItem getItem(long id);
    
    public List<TblItem> getAllDataItem();
    
    public List<TblItemLocation>getAllDataItemStock();
    
    //--------------------------------------------------------------------------
    
    public TblFixedTangibleAsset getFixedTangibleAsset(long id);
    
    public List<TblFixedTangibleAsset> getAllDataFixedTangibleAsset();
    
    //--------------------------------------------------------------------------
    
    public TblUnit getUnit(long id);
    
    public List<TblUnit> getAllDataUnit();
    
    //--------------------------------------------------------------------------
    
    public RefItemType getItemType(int id);
    
    public List<RefItemType> getAllDataItemType();
    
    //--------------------------------------------------------------------------
    
    public RefFixedTangibleAssetDepreciationStatus getFixedTangibleAssetDepreciationStatus(int id);
    
    public List<RefFixedTangibleAssetDepreciationStatus> getAllDataFixedTangibleAssetDepreciationStatus();
    
    //--------------------------------------------------------------------------
    
    public List<RefItemGuestType>getAllGuestType();
    
    //-------------------------------------------------------------------------
    public List<TblLocationOfWarehouse> getWarehouseByIdLocation(long id);
   
    public List<TblRoom> getRoomByIdLocation(long id);
    
    public List<TblLocationOfLaundry> getLaundryByIdLocation(long id);
    
    public List<TblSupplier> getSupplierByIdLocation(long id);
    
    public List<TblLocationOfBin> getBinByIdLocation(long id);
    
    //--------------------------------------------------------------------------
    
    public List<TblItemLocation> getAllDataItemLocationByIDItem(long idItem);
    
    //--------------------------------------------------------------------------
    
    public TblLocation getDataLocation(long id);
    
    public TblItemLocation getDataItemLocation(long id);
    
    public TblItemLocationPropertyBarcode getDataItemLocationPropertyBarcodeByIDPropertyBarcode(long idPropertyBarcode);
    
    public TblRoom getDataRoomByIDLocation(long idLocation);
    
    public TblLocationOfWarehouse getDataWarehouseByIdLocation(long id);
    
    public TblLocationOfLaundry getDataLaundryByIDLocation(long idLocation);
    
    //--------------------------------------------------------------------------
    
    public String getErrorMessage();
    
}
