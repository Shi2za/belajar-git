/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hotelfx.persistence.service;

import hotelfx.persistence.model.LogSystem;
import hotelfx.persistence.model.TblEmployee;
import hotelfx.persistence.model.TblPeople;
import hotelfx.persistence.model.TblSystemUser;
import java.util.List;

/**
 *
 * @author ANDRI
 */
public interface FLogManager {
    
    public LogSystem insertDataLog(LogSystem dataLog);
    
    public boolean updateDataLog(LogSystem dataLog);
    
    public boolean deleteDataLog(LogSystem dataLog);
    
    public LogSystem getDataLog(long id);
    
    public List<LogSystem> getAllDataLog();
    
    //--------------------------------------------------------------------------
    
    public TblSystemUser getDataUser(long id);
    
    public TblEmployee getDataEmployee(long id);
    
    public TblPeople getDataPeople(long id);
    
    //--------------------------------------------------------------------------    
    
    public String getErrorMessage();
    
}
