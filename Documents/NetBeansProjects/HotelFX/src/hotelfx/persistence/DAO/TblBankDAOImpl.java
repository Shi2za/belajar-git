/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hotelfx.persistence.DAO;

import hotelfx.persistence.model.TblBank;
import java.util.List;

/**
 *
 * @author ANDRI
 */
public class TblBankDAOImpl extends GenericDAOImpl<TblBank, Long>
        implements TblBankDAO {

    @Override
    public List<TblBank> findAll() {
        return (List<TblBank>) getSession().getNamedQuery("findAllTblBank").list();
    }

}
