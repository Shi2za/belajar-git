/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hotelfx.persistence.service;

import hotelfx.persistence.model.TblUnit;
import java.util.List;

/**
 *
 * @author ANDRI
 */
public interface FUnitManager {
    
    public TblUnit insertDataUnit(TblUnit unit);
    
    public boolean updateDataUnit(TblUnit unit);
    
    public boolean deleteDataUnit(TblUnit unit);
    
    public TblUnit getUnit(long id);
    
    public List<TblUnit> getAllDataUnit();
    
    //--------------------------------------------------------------------------
    
    public String getErrorMessage();
    
}
