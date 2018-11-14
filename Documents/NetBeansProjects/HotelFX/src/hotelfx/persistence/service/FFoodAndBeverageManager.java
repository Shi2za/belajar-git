/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hotelfx.persistence.service;

import hotelfx.persistence.model.RefItemExpiredDateStatus;
import hotelfx.persistence.model.RefItemGuestType;
import hotelfx.persistence.model.TblItem;
import hotelfx.persistence.model.TblItemExpiredDate;
import hotelfx.persistence.model.TblItemLocation;
import hotelfx.persistence.model.TblLocationOfBin;
import hotelfx.persistence.model.TblLocationOfLaundry;
import hotelfx.persistence.model.TblLocationOfWarehouse;
import hotelfx.persistence.model.TblRoom;
import hotelfx.persistence.model.TblStockOpnameItemExpiredDate;
import hotelfx.persistence.model.TblStockOpnameItemExpiredDateDetail;
import hotelfx.persistence.model.TblSupplier;
import hotelfx.persistence.model.TblUnit;
import java.util.Date;
import java.util.List;

/**
 *
 * @author ANDRI
 */
public interface FFoodAndBeverageManager {

    public TblItem insertDataFoodAndBeverage(TblItem fab);

    public boolean updateDataFoodAndBeverage(TblItem fab);

    public boolean deleteDataFoodAndBeverage(TblItem fab);

    public TblItem getFoodAndBeverage(long id);

    public List<TblItem> getAllDataFoodAndBeverage();

    public List<TblItemLocation> getAllDataFoodAndBeverageStock();

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
    public List<TblItemExpiredDate> getAllDataItemExpiredDate();

    public List<TblItemExpiredDate> getAllDataItemExpiredDateByIDItemAndItemExpiredDate(long idItem,
            Date itemExpiredDate);

    public List<TblItemExpiredDate> getAllDataItemExpiredDateByIDItemAndItemExpiredDateAndItemExpiredDateStatus(long idItem,
            Date itemExpiredDate,
            int idItemExpiredDateStatus);

    public RefItemExpiredDateStatus getDataItemExpiredDateStatus(int id);
    //--------------------------------------------------------------------------
    public List<RefItemGuestType>getAllDataGuest();
    //--------------------------------------------------------------------------
    public TblStockOpnameItemExpiredDate insertDataStockOpnameItemExpiredDate(
            TblStockOpnameItemExpiredDate stockOpnameItemExpiredDate,
            List<TblStockOpnameItemExpiredDateDetail> stockOpnameItemExpiredDateDetails);

    //--------------------------------------------------------------------------
    public String getErrorMessage();

}
