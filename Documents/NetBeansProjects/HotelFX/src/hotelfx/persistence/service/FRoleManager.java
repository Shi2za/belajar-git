/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hotelfx.persistence.service;

import hotelfx.persistence.model.TblSystemRole;
import java.util.List;

/**
 *
 * @author Andreas
 */
public interface FRoleManager {
    public TblSystemRole insertDataRole(TblSystemRole role);
    public boolean updateDataRole(TblSystemRole role);
    public boolean deleteDataRole(TblSystemRole role);
    
    public List<TblSystemRole>getAllDataRole();
    public String getErrorMessage();
    public TblSystemRole getDataRole(long id);
}
