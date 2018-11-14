/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hotelfx.persistence.service;

import hotelfx.helper.ClassDataMutation;
import hotelfx.persistence.model.RefItemType;
import hotelfx.persistence.model.TblBank;
import hotelfx.persistence.model.TblBankAccount;
import hotelfx.persistence.model.TblItem;
import hotelfx.persistence.model.TblItemExpiredDate;
import hotelfx.persistence.model.TblItemMutationHistory;
import hotelfx.persistence.model.TblItemMutationHistoryItemExpiredDate;
import hotelfx.persistence.model.TblItemMutationHistoryPropertyBarcode;
import hotelfx.persistence.model.TblItemTypeHk;
import hotelfx.persistence.model.TblItemTypeWh;
import hotelfx.persistence.model.TblLocation;
import hotelfx.persistence.model.TblLocationOfBin;
import hotelfx.persistence.model.TblLocationOfLaundry;
import hotelfx.persistence.model.TblLocationOfWarehouse;
import hotelfx.persistence.model.TblPropertyBarcode;
import hotelfx.persistence.model.TblRoom;
import hotelfx.persistence.model.TblSupplier;
import hotelfx.persistence.model.TblSupplierBankAccount;
import hotelfx.persistence.model.TblSupplierItem;
import java.util.List;

/**
 *
 * @author ANDRI
 */
public interface FSupplierManager {
    
    public TblSupplier insertDataSupplier(TblSupplier supplier, 
            List<TblSupplierBankAccount> supplierBankAccount, 
            List<TblSupplierItem> supplierItem);
    
    public boolean updateDataSupplier(TblSupplier supplier, 
            List<TblSupplierBankAccount> supplierBankAccount, 
            List<TblSupplierItem> supplierItem);
    
    public boolean deleteDataSupplier(TblSupplier supplier);
    
    public TblSupplier getSupplier(long id);
    
    public List<TblSupplier> getAllDataSupplier();
    
    //--------------------------------------------------------------------------
    
    public TblSupplierBankAccount getDataSupplierBankAccount(long id);
    
    public List<TblSupplierBankAccount> getAllDataSupplierBankAccountByIDSupplier(long idSupplier);
    
    //--------------------------------------------------------------------------
    
    public TblBank getDataBank(long id);
    
    public List<TblBank> getAllDataBank();
    
    //--------------------------------------------------------------------------
    
    public TblBankAccount getDataBankAccount(long id);
    
    public List<TblBankAccount> getAllDataBankAccount();
    
    //--------------------------------------------------------------------------
    
    public TblSupplierItem getDataSupplierItem(long id);
    
    public List<TblSupplierItem> getAllDataSupplierItemByIDSupplier(long idSupplier);
    
    //--------------------------------------------------------------------------
    
    public TblItem getDataItem(long id);
    
    public List<TblItem> getAllDataItem();
    
    //--------------------------------------------------------------------------
    
    public RefItemType getDataItemType(int id);
    
    public List<RefItemType> getAllDataItemType();
    
    //--------------------------------------------------------------------------
    
    public TblLocation getLocation(long id);

    public List<TblLocation> getAllDataLocation();
    
    //--------------------------------------------------------------------------
    public List<ClassDataMutation> getAllDataMutationHistory();
    
    public List<TblLocationOfWarehouse> getWarehouseByIdLocation(long id);
    
    public List<TblRoom> getRoomByIdLocation(long id);
    
    public List<TblLocationOfLaundry> getLaundryByIdLocation(long id);
    
    public List<TblSupplier> getSupplierByIdLocation(long id);

    public List<TblLocationOfBin> getBinByIdLocation(long id);
    
    //-------------------------------------------------------------------------
    
    public TblItemTypeHk getDataItemTypeHK(long id);
    
    public List<TblItemTypeHk> getAllDataItemTypeHK();
    
    public TblItemTypeWh getDataItemTypeWH(long id);
    
    public List<TblItemTypeWh> getAllDataItemTypeWH();
    
    //--------------------------------------------------------------------------
    public TblPropertyBarcode getDataPropertyBarcode(long id);
    
    public TblItemExpiredDate getDataItemExpiredDate(long id);
    
    //--------------------------------------------------------------------------
    public List<TblItemMutationHistory> getAllDataItemMutationHistory();
    
    public TblItemMutationHistoryPropertyBarcode getDataItemMutationHistoryPropertyBarcodeByIDItemMutationHistory(long idItemMutation);
    
    public List<TblItemMutationHistoryItemExpiredDate> getAllDataItemMutationHistoryItemExpiredDateByIDItemMutationHistory(long idItemMutation);
    
    //--------------------------------------------------------------------------
    public String getErrorMessage();
    
}
