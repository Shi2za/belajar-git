/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hotelfx.persistence.service;

import hotelfx.persistence.model.TblEmployeeWarningLetterType;
import java.util.List;

/**
 *
 * @author Andreas
 */
public interface FWarningLetterManager {
    public boolean insertDataWarningLetter(TblEmployeeWarningLetterType tblEmployeeWarningLetter);
    public boolean updateDataWarningLetter(TblEmployeeWarningLetterType employeeWarningLetterType);
    public boolean deleteDataWarningLetterType(TblEmployeeWarningLetterType employeeWarningLetterType);
    public List<TblEmployeeWarningLetterType>getAllDataWarningLetter();
    public TblEmployeeWarningLetterType getDataWarningLetter(long id);
    public String getErrorMessage();
}
