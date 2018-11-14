/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hotelfx.persistence.service;

import hotelfx.persistence.model.RefItemType;
import hotelfx.persistence.model.RefLocationType;
import hotelfx.persistence.model.SysAccount;
import hotelfx.persistence.model.TblEmployee;
import hotelfx.persistence.model.TblItem;
import hotelfx.persistence.model.TblItemExpiredDate;
import hotelfx.persistence.model.TblItemLocation;
import hotelfx.persistence.model.TblItemLocationItemExpiredDate;
import hotelfx.persistence.model.TblItemLocationPropertyBarcode;
import hotelfx.persistence.model.TblItemTypeHk;
import hotelfx.persistence.model.TblItemTypeWh;
import hotelfx.persistence.model.TblLocation;
import hotelfx.persistence.model.TblLocationOfBin;
import hotelfx.persistence.model.TblLocationOfLaundry;
import hotelfx.persistence.model.TblLocationOfWarehouse;
import hotelfx.persistence.model.TblPeople;
import hotelfx.persistence.model.TblPropertyBarcode;
import hotelfx.persistence.model.TblRoom;
import hotelfx.persistence.model.TblStockOpname;
import hotelfx.persistence.model.TblStockOpnameDetail;
import hotelfx.persistence.model.TblStockOpnameDetailItemExpiredDate;
import hotelfx.persistence.model.TblStockOpnameDetailPropertyBarcode;
import hotelfx.persistence.model.TblSupplier;
import hotelfx.persistence.model.TblUnit;
import hotelfx.view.feature_laundry.laundry_stock_opname.LaundryStockOpnameController;
import hotelfx.view.feature_stock_opname.StockOpnameController;
import hotelfx.view.feature_warehouse.warehouse_stock_opname.WarehouseStockOpnameController;
import java.util.List;

/**
 *
 * @author ANDRI
 */
public interface FStockOpnameManager {
    
    public TblStockOpname insertDataStockOpname(TblStockOpname stockOpname, 
            List<StockOpnameController.ClassSODetail> classSODetails);
    
    public TblStockOpname insertDataStockOpnameWarehouse(TblStockOpname stockOpname, 
            List<WarehouseStockOpnameController.ClassSODetail> classSODetails);
    
    public TblStockOpname insertDataStockOpnameLaundry(TblStockOpname stockOpname, 
            List<LaundryStockOpnameController.ClassSODetail> classSODetails);
    
    public boolean updateDataStockOpname(TblStockOpname stockOpname, 
            List<TblStockOpnameDetail> stockOpnameDetails, 
            List<TblStockOpnameDetailItemExpiredDate> stockOpnameDetailItemExpiredDates);
    
    public boolean deleteDataStockOpname(TblStockOpname stockOpname);
    
    public TblStockOpname getDataStockOpname(long id);
    
    public List<TblStockOpname> getAllDataStockOpname();
    
    //--------------------------------------------------------------------------
    
    public TblStockOpnameDetail getDataStockOpnameDetail(long id);
    
    public List<TblStockOpnameDetail> getAllDataStockOpnameDetailByIDStockOpname(long idStockOpname);
    
    //--------------------------------------------------------------------------
    
    public SysAccount getDataAccount(long id);
    
    public List<SysAccount> getAllDataAccount();
    
    //--------------------------------------------------------------------------
    
    public TblLocation getDataLocation(long id);
    
    public List<TblLocation> getAllDataLocation();
    
    public RefLocationType getDataLocationType(int id);
    
    public List<RefLocationType> getAllDataLocationType();
    
    //--------------------------------------------------------------------------
    
    public List<TblLocationOfWarehouse> getAllDataWarehouse();
    
    public List<TblLocationOfLaundry> getAllDataLaundry();
    
    //--------------------------------------------------------------------------
    
    public TblRoom getDataRoomByIDLocation(long idLocation);
    
    public TblLocationOfWarehouse getDataWarehouseByIDLocation(long idLocation);
    
    public TblLocationOfLaundry getDataLaundryByIDLocation(long idLocation);
    
    public TblSupplier getDataSupplierByIDLocation(long idLocation);
    
    public TblLocationOfBin getDataBinByIDLocation(long idLocation);
    
    //--------------------------------------------------------------------------
    
    public TblEmployee getDataEmployee(long id);
    
    public TblPeople getDataPeople(long id);
    
    //--------------------------------------------------------------------------
    
    public List<TblItemLocation> getAllDataItemLocationByIDLocation(long idLocation);
    
    //--------------------------------------------------------------------------
    
    public TblItem getDataItem(long id);
    
    public TblUnit getDataUnit(long id);
    
    public RefItemType getDataItemType(int id);
    
    //--------------------------------------------------------------------------
    
    public TblItemTypeHk getDataItemTypeHK(long id);
    
    public List<TblItemTypeHk> getAllDataItemTypeHK();
    
    public TblItemTypeWh getDataItemTypeWH(long id);
    
    public List<TblItemTypeWh> getAllDataItemTypeWH();
    
    //--------------------------------------------------------------------------
    
    public TblPropertyBarcode getDataPropertyBarcode(long id);
    
    public List<TblItemLocationPropertyBarcode> getAllDataItemLocationPropertyBarcodeByIDItemLocation(long idItemLocation);
    
    public List<TblStockOpnameDetailPropertyBarcode> getAllDataStockOpnameDetailPropertyBarcodeByIDStockOpnameDetail(long idStockOpnameDetail);
    
    //--------------------------------------------------------------------------
    
    public TblItemExpiredDate getDataItemExpiredDate(long id);
    
    public List<TblItemLocationItemExpiredDate> getAllDataItemLocationItemExpiredDateByIDItemLocation(long idItemLocation);
    
    public List<TblStockOpnameDetailItemExpiredDate> getAllDataStockOpnameDetailItemExpiredDateByIDStockOpnameDetail(long idStockOpnameDetail);
    
    //--------------------------------------------------------------------------
    
    public String getErrorMessage();
    
}
