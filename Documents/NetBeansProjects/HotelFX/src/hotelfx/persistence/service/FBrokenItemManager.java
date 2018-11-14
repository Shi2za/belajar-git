/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hotelfx.persistence.service;

import hotelfx.persistence.model.RefLocationType;
import hotelfx.persistence.model.SysDataHardCode;
import hotelfx.persistence.model.TblBrokenItem;
import hotelfx.persistence.model.TblBrokenItemDetail;
import hotelfx.persistence.model.TblBrokenItemDetailItemMutationHistory;
import hotelfx.persistence.model.TblEmployee;
import hotelfx.persistence.model.TblGroup;
import hotelfx.persistence.model.TblItem;
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
import hotelfx.persistence.model.TblRoom;
import hotelfx.persistence.model.TblUnit;
import hotelfx.view.feature_broken_item.BrokenItemController;
import java.util.List;

/**
 *
 * @author ABC-Programmer
 */
public interface FBrokenItemManager {

    public TblBrokenItem insertDataBrokenItem(TblBrokenItem bi,
            List<BrokenItemController.BIDetailItemMutationHistory> icdps);

    public TblBrokenItem getDataBrokenItem(long id);

    public List<TblBrokenItem> getAllDataBrokenItem();

    //--------------------------------------------------------------------------
    public List<TblBrokenItemDetail> getAllDataBrokenItemDetailByIDBrokenItem(long idBrokenItem);

    public TblBrokenItemDetailItemMutationHistory getDataBrokenItemDetailItemMutationHistoryByIDBrokenItemDetail(long idBrokenItemDetail);

    //--------------------------------------------------------------------------
    public TblLocationOfBin getDataBinByIdLocation(long idLocation);

    //--------------------------------------------------------------------------
    public TblItemLocation getDataItemLocationByIDItemAndIDLocation(long idItem, long idLocation);

    public TblItemLocation getDataItemLocationByIDLocationAndIDItem(long idLocation, long idItem);

    public TblItemLocationPropertyBarcode getDataItemLocationPropertyBarcodeByIDItemLocationAndIDPropertyBarcode(long idItemLocation, long idPropertyBarcode);

    public List<TblItemLocationPropertyBarcode> getAllDataItemLocationPropertyBarcodeByIDItemLocation(long idItemLocation);
    
    public TblItemLocationItemExpiredDate getDataItemLocationItemExpiredDateByIDItemLocationAndIDItemExpiredDate(long idItemLocation, long idItemExpiredDate);

    public List<TblItemLocationItemExpiredDate> getAllDataItemLocationItemExpiredDateByIDItemLocation(long idItemLocation);

    //--------------------------------------------------------------------------
    public TblItemMutationHistory getDataItemMutationHistory(long id);

    public List<TblItemMutationHistoryPropertyBarcode> getAllDataItemMutationHistoryPropertyBarcodeByIDItemMutationHistory(long idItemMutationHistory);

    public List<TblItemMutationHistoryItemExpiredDate> getAllDataItemMutationHistoryItemExpiredDateByIDItemMutationHistory(long idItemMutationHistory);

    //--------------------------------------------------------------------------
    public TblLocation getDataLocation(long id);

    public TblLocation getDataLocationByIDLocationType(int idLocationType);

    public List<TblLocation> getAllDataLocation();

    public List<TblLocation> getAllDataLocationByIDLocationType(int idLocationType);

    public RefLocationType getDataLocationType(int id);

    public TblRoom getDataRoomByIDLocation(long idLocation);

    public TblLocationOfWarehouse getDataWarehouseByIdLocation(long idLocation);

    public TblLocationOfLaundry getDataLaundryByIDLocation(long idLocation);

    //--------------------------------------------------------------------------
    public TblItem getDataItem(long id);

    public List<TblItem> getAllDataItem();

    public TblItemTypeHk getDataItemTypeHK(long id);

    public TblItemTypeWh getDataItemTypeWH(long id);

    public TblUnit getDataUnit(long id);

    //--------------------------------------------------------------------------
    public TblEmployee getDataEmployee(long id);

    public TblGroup getDataGroup(long id);

    //--------------------------------------------------------------------------
    public SysDataHardCode getDataSysDataHardCode(long id);

    public List<SysDataHardCode> getAllDataSysDataHardCode();

    //--------------------------------------------------------------------------
    public String getErrorMessage();

}
