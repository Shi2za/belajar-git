/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hotelfx.persistence.service;

import hotelfx.helper.ClassDataUserAccess;
import hotelfx.persistence.model.TblSystemFeature;
import hotelfx.persistence.model.TblSystemRole;
import hotelfx.persistence.model.TblSystemRoleSystemFeature;
import java.util.List;

/**
 *
 * @author ANDRI
 */
public interface FUserAccessManager {
    
    public boolean updateDataRoleFeature(TblSystemRole role, List<TblSystemRoleSystemFeature> roleFeatures);
    
    public TblSystemRoleSystemFeature getRoleFeature(long idRoleFeature);
    
    public List<TblSystemRoleSystemFeature> getAllDataRoleFeature();
    
    public List<ClassDataUserAccess> getAllDataRoleFeatureByIdRole(long idRole);
    
    //--------------------------------------------------------------------------
    
    public TblSystemFeature getFeature(long id);
    
    public List<TblSystemFeature> getAllDataFeature();
    
    //--------------------------------------------------------------------------
    
    public TblSystemRole getRole(long id);
    
    public List<TblSystemRole> getAllDataRole();
    
    //--------------------------------------------------------------------------
    
    public String getErrorMessage();
    
}
