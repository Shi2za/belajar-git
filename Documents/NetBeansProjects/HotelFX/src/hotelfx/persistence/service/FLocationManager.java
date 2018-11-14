/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hotelfx.persistence.service;

import hotelfx.persistence.model.RefLocationType;
import hotelfx.persistence.model.TblBuilding;
import hotelfx.persistence.model.TblFloor;
import hotelfx.persistence.model.TblLocation;
import java.util.List;

/**
 *
 * @author ANDRI
 */
public interface FLocationManager {
    
    public TblLocation insertDataLocation(TblLocation location);
    
    public boolean updateDataLocation(TblLocation location);
    
    public boolean deleteDataLocation(TblLocation location);
    
    public TblLocation getLocation(long id);
    
    public List<TblLocation> getAllDataLocation();
    
    //--------------------------------------------------------------------------
    
    public TblBuilding insertDataBuilding(TblBuilding building);
    
    public boolean updateDataBuilding(TblBuilding building);
    
    public boolean deleteDataBuilding(TblBuilding building);
    
    public TblBuilding getBuilding(long id);
    
    public List<TblBuilding> getAllDataBuilding();
    
    //--------------------------------------------------------------------------
    
    public TblFloor insertDataFloor(TblFloor floor);
    
    public boolean updateDataFloor(TblFloor floor);
    
    public boolean deleteDataFloor(TblFloor floor);
    
    public TblFloor getFloor(long id);
    
    public List<TblFloor> getAllDataFloor();
    
    //--------------------------------------------------------------------------
    
    public RefLocationType getLocationType(int id);
    
    public List<RefLocationType> getAllDataLocationType();
    
    //--------------------------------------------------------------------------
    
    public String getErrorMessage();
    
}
