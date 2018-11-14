/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hotelfx.persistence.service;

import hotelfx.persistence.model.RefSystemUserLockStatus;
import hotelfx.persistence.model.RefSystemUserLoginStatus;
import hotelfx.persistence.model.SysDataHardCode;
import hotelfx.persistence.model.TblEmployee;
import hotelfx.persistence.model.TblJob;
import hotelfx.persistence.model.TblPeople;
import hotelfx.persistence.model.TblSystemRole;
import hotelfx.persistence.model.TblSystemUser;
import java.util.List;

/**
 *
 * @author Andreas
 */
public interface FUserAccountManager {
    public TblSystemUser insertDataUserAccount(TblSystemUser user);
    public boolean updateDataUserAccount(TblSystemUser user);
    public boolean deleteDataUserAccount(TblSystemUser user);
    
    public TblSystemUser getDataUserAccount(long id);
    public List<TblSystemUser>getAllDataUserAccount();
    public List<TblEmployee>getAllDataEmployee();
    public List<TblPeople>getAllDataPeople();
    public List<TblSystemRole>getAllDataRole();
    public List<RefSystemUserLoginStatus>getAllDataLoginStatus();
    public List<RefSystemUserLockStatus>getAllDataLockStatus();
    
    public TblEmployee getDataEmployee(long id);
    public TblPeople getDataPeople(long id);
    public TblJob getDataJob(long id);
    
    public RefSystemUserLockStatus getDataLockStatus(int id);
    public RefSystemUserLoginStatus getDataLoginStatus(int id);
    
    //--------------------------------------------------------------------------
    
    public SysDataHardCode getDataSysDataHardCode(long id);
    
    public List<SysDataHardCode> getAllDataSysDataHardCode();
    
    //--------------------------------------------------------------------------
    
    public String getErrorMessage();
}
