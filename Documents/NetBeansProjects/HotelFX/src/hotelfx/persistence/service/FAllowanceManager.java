/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hotelfx.persistence.service;

import hotelfx.persistence.model.TblEmployeeAllowance;
import java.util.List;

/**
 *
 * @author Andreas
 */
public interface FAllowanceManager {
    public TblEmployeeAllowance insertDataAllowance(TblEmployeeAllowance allowance);
    public boolean updateDataAllowance(TblEmployeeAllowance allowance);
    public boolean deleteDataAllowance(TblEmployeeAllowance allowance);
    
    public TblEmployeeAllowance getDataAllowance(long id);
    public List<TblEmployeeAllowance>getAllDataAllowance();
    public String getErrorMessage();
}
