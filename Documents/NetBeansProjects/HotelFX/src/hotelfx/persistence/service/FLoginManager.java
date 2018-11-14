/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hotelfx.persistence.service;

import hotelfx.persistence.model.SysDataHardCode;
import hotelfx.persistence.model.TblEmployee;
import hotelfx.persistence.model.TblJob;
import hotelfx.persistence.model.TblPeople;
import hotelfx.persistence.model.TblSystemLogBook;
import hotelfx.persistence.model.TblSystemRoleSystemFeature;
import hotelfx.persistence.model.TblSystemUser;
import java.time.LocalDate;
import java.util.List;

/**
 *
 * @author ANDRI
 */
public interface FLoginManager {
    
    public TblSystemUser doLogin(String username, String password);
    
    public boolean doLogout(long idUser);
    
    public boolean updateDataUserAccount(TblSystemUser user, 
            String imgExtention);
    
    public TblSystemUser getDataUserAccount(long id);
    
    public TblSystemUser getDataUserAccountByIDUserAccountAndGUID(long idUser, String guid);
    
    public List<TblSystemRoleSystemFeature> getRoleFeatureByRole(long idRole);
    
    public List<TblSystemRoleSystemFeature> getRoleFeatureByRoleOrderByIDFeature(long idRole);
    
    public SysDataHardCode getDataSysDataHardCode(long id);
    
    public TblPeople getDataPeopleByUserAccount(TblSystemUser userAccount);
    
    public List<TblSystemLogBook> getAllDataCurrentReminder(TblSystemUser userAccount);
    
    public List<TblSystemLogBook> getAllDataCurrentReminderBySelectedDate(
            TblSystemUser userAccount, 
            LocalDate selectedDate);
    
    public TblSystemUser getDataUser(long id);
    
    public TblEmployee getDataEmployee(long id);
    
    public TblJob getDataJob(long id);
    
    public TblPeople getDataPeople(long id);
    
    public String getErrorMessage();
    
}
