/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hotelfx.persistence.service;

import hotelfx.persistence.model.TblGroup;
import hotelfx.persistence.model.TblLocation;
import java.util.List;

/**
 *
 * @author Andreas
 */
public interface FGroupManager {
    public TblGroup insertDataGroup(TblGroup group);
    public boolean updateDataGroup(TblGroup group);
    public boolean deleteDataGroup(TblGroup group);
    
    public List<TblGroup>getAllDataGroup();
    public TblGroup getDataGroup(long id);
    
    public List<TblLocation> getAllDataLocationByIDGroup(long idGroup);
    
    public String getErrorMessage();
}
