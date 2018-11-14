/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hotelfx.persistence.DAO;

import hotelfx.persistence.model.RefApplicantStatus;
import java.util.List;

/**
 *
 * @author ANDRI
 */
public class RefApplicantStatusDAOImpl extends GenericDAOImpl<RefApplicantStatus, Integer>
        implements RefApplicantStatusDAO {
    
    @Override
    public List<RefApplicantStatus> findAll() {
        return (List<RefApplicantStatus>) getSession().getNamedQuery("findAllRefApplicantStatus").list();
    }
    
}
