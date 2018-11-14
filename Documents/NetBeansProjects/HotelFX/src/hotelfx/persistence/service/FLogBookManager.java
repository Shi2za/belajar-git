/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hotelfx.persistence.service;

import hotelfx.persistence.model.TblEmployee;
import hotelfx.persistence.model.TblJob;
import hotelfx.persistence.model.TblPeople;
import hotelfx.persistence.model.TblSystemLogBook;
import hotelfx.persistence.model.TblSystemLogBookJob;
import hotelfx.persistence.model.TblSystemUser;
import java.util.List;

/**
 *
 * @author ANDRI
 */
public interface FLogBookManager {
    
    public TblSystemLogBook insertDataSystemLogBook(TblSystemLogBook dataSystemLogBook, 
            List<TblSystemLogBookJob> systemLogBookJobs);
    
    public boolean updateDataSystemLogBook(TblSystemLogBook dataSystemLogBook, 
            List<TblSystemLogBookJob> systemLogBookJobs);
    
    public boolean deleteDataSystemLogBook(TblSystemLogBook dataSystemLogBook);
    
    public TblSystemLogBook getDataSystemLogBook(long id);
    
    public List<TblSystemLogBook> getAllDataSystemLogBook();
    
    //--------------------------------------------------------------------------
    
    public TblSystemLogBookJob getDataSystemLogBookJob(long id);
    
    public List<TblSystemLogBookJob> getAllDataSystemLogBookJob();
    
    public List<TblSystemLogBookJob> getAllDataSystemLogBookJobByIDSystemLogBook(long idLogBook);
    
    public List<TblSystemLogBookJob> getAllDataSystemLogBookJobByIDJob(long idJob);
    
    //--------------------------------------------------------------------------
    
    public TblSystemUser getDataUser(long id);
    
    public TblEmployee getDataEmployee(long id);
    
    public TblPeople getDataPeople(long id);
    
    //--------------------------------------------------------------------------    
    
    public TblJob getDataJob(long id);
    
    public List<TblJob> getAllDataJob();
    
    //--------------------------------------------------------------------------
    
    public String getErrorMessage();
    
}
