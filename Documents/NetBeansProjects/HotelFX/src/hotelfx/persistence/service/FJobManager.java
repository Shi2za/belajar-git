/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hotelfx.persistence.service;

import hotelfx.persistence.model.RefJobRecruitmentShowStatus;
import hotelfx.persistence.model.SysCode;
import hotelfx.persistence.model.TblJob;
import java.util.List;

/**
 *
 * @author Andreas
 */
public interface FJobManager {
    public TblJob insertDataJob(TblJob job, SysCode sysCode);
    public boolean updateDataJob(TblJob job, SysCode sysCode);
    public boolean deleteDataJob(TblJob job);
    
    public TblJob getJob(long id);
    public List<RefJobRecruitmentShowStatus>getAllJobRecruitmentShowStatus();
    public List<TblJob>getAllDataJob();
    
    //------------------------
    public SysCode getDataCodeByDataName(String dataName);
    
    //------------------------
    public String getErrorMessage();
}
