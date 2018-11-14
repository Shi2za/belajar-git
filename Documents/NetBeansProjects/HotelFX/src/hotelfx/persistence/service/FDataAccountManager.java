/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hotelfx.persistence.service;

import hotelfx.persistence.model.RefAccountType;
import hotelfx.persistence.model.SysAccount;
import java.util.List;

/**
 *
 * @author ANDRI
 */
public interface FDataAccountManager {

    public SysAccount insertDataAccount(SysAccount account);

    public boolean updateDataAccount(SysAccount account);

    public boolean deleteDataAccount(SysAccount account);

    public SysAccount getDataAccount(long id);

    public List<SysAccount> getAllDataAccount();

    //--------------------------------------------------------------------------
    public RefAccountType getDataAccountType(int id);

    public List<RefAccountType> getAllDataAccountType();

    //--------------------------------------------------------------------------
    public String getErrorMessage();

}
