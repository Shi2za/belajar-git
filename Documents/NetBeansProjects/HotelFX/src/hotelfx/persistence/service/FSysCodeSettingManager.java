/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hotelfx.persistence.service;

import hotelfx.persistence.model.SysCode;
import java.util.List;

/**
 *
 * @author ANDRI
 */
public interface FSysCodeSettingManager {
 
    public SysCode insertDataCode(SysCode sysCode);
    
    public boolean updateDataCode(SysCode sysCode);
    
    public boolean deleteDataCode(SysCode sysCode);
    
    public SysCode getDataCode(long id);
    
    public List<SysCode> getAllDataCode();
    
    public List<SysCode> getAllDataCodeByIDRecordStatus(int idRecordStatus);
   
    //--------------------------------------------------------------------------
    
    public String getErrorMessage();
    
}
