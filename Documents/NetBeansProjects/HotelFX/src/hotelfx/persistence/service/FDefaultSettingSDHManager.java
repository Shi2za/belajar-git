/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hotelfx.persistence.service;

import hotelfx.persistence.model.SysCurrentHotelDate;
import hotelfx.persistence.model.SysDataHardCode;
import hotelfx.persistence.model.SysPasswordDeleteDebt;
import hotelfx.persistence.model.TblBank;
import hotelfx.persistence.model.TblBankAccount;
import hotelfx.persistence.model.TblCompanyBalanceBankAccount;
import hotelfx.persistence.model.TblItem;
import hotelfx.persistence.model.TblLocation;
import hotelfx.persistence.model.TblLocationOfWarehouse;
import java.util.List;

/**
 *
 * @author ANDRI
 */
public interface FDefaultSettingSDHManager {

    public boolean updateDataSysDataHardcodeHotel(List<SysDataHardCode> listSysDataHotel);

    /* public boolean updateDataSysDataHardcode(List<SysDataHardCode> sysDataHardCodes, 
     SysCurrentHotelDate sysCurrentHotelDate);*/
    public boolean updateDataSysDataHardcode(List<SysDataHardCode> sysDataHardCodes,
            SysCurrentHotelDate sysCurrentHotelDate, SysPasswordDeleteDebt sysPasswordDeleteDebt);

    public SysDataHardCode getDataSysDataHardcode(long id);

    public List<SysDataHardCode> getAllDataSysDataHardcode();

    //--------------------------------------------------------------------------
    public TblItem getDataItem(long id);

    public List<TblItem> getAllDataItem();

    public List<TblItem> getAllDataItemByConsumableAndPropertyStatusAndLeasedStatusAndGuestStatus(
            boolean consumable,
            boolean property,
            boolean leased,
            boolean guest);

    //--------------------------------------------------------------------------
    public TblLocation getDataLocation(long id);

    public List<TblLocation> getAllDataLocation();

    public TblLocationOfWarehouse getDataWarehouse(long id);

    public TblLocationOfWarehouse getDataWarehouseByIDLocation(long idLocation);

    public List<TblLocationOfWarehouse> getAllDataWarehouse();

    //--------------------------------------------------------------------------
    public List<TblCompanyBalanceBankAccount> getAllDataCompanyBalanceBankAccount();

    public TblBankAccount getDataBankAccount(long id);

    public TblBank getDataBank(long id);

    //--------------------------------------------------------------------------
    public SysCurrentHotelDate getDataSysCurrentHotelDate(int id);

    //--------------------------------------------------------------------------
    public SysPasswordDeleteDebt getPasswordDeleteDebt();

    //--------------------------------------------------------------------------
    public String getErrorMessage();

}
