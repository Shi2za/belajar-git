/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hotelfx.persistence.service;

import hotelfx.persistence.model.TblEmployeeInsurance;
import java.util.List;

/**
 *
 * @author Andreas
 */
public interface FInsuranceManager {
    public TblEmployeeInsurance insertDataInsurance(TblEmployeeInsurance insurance);
    public boolean updateDataInsurance(TblEmployeeInsurance insurance);
    public boolean deleteDataInsurance(TblEmployeeInsurance insurance);
    
    public List<TblEmployeeInsurance>getAllDataInsurance();
    public TblEmployeeInsurance getDataInsurance(long id);
    public String getErrorMessage();
}
