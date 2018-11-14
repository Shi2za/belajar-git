/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hotelfx.persistence.service;

import hotelfx.helper.ClassSession;
import hotelfx.persistence.HibernateUtil;
import hotelfx.persistence.model.RefRecordStatus;
import hotelfx.persistence.model.TblEmployee;
import hotelfx.persistence.model.TblEmployeeInsurance;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import org.hibernate.Session;

/**
 *
 * @author Andreas
 */
public class FInsuranceManagerImpl implements FInsuranceManager {

    private Session session;

    private String errMessage;

    @Override
    public TblEmployeeInsurance insertDataInsurance(TblEmployeeInsurance insurance) {
        errMessage = "";
        TblEmployeeInsurance tblInsurance = insurance;
        if (ClassSession.checkUserSession()) {  //check user current sessio
            try {
                session = HibernateUtil.getSessionFactory().getCurrentSession();
                session.beginTransaction();
                tblInsurance.setCreateDate(Timestamp.valueOf(LocalDateTime.now()));
                tblInsurance.setTblEmployeeByCreateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                tblInsurance.setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                tblInsurance.setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                tblInsurance.setRefRecordStatus(session.find(RefRecordStatus.class, 1));
                session.saveOrUpdate(tblInsurance);
                session.getTransaction().commit();
            } catch (Exception e) {
                if (session.getTransaction().isActive()) {
                    session.getTransaction().rollback();
                }
                errMessage = e.getMessage();
                return null;
            }
        } else {
            return null;
        }
        return tblInsurance;
    }

    @Override
    public boolean updateDataInsurance(TblEmployeeInsurance insurance) {
        errMessage = "";
        if (ClassSession.checkUserSession()) {  //check user current sessio
            try {
                session = HibernateUtil.getSessionFactory().getCurrentSession();
                session.beginTransaction();
                insurance.setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                insurance.setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                session.update(insurance);
                session.getTransaction().commit();
            } catch (Exception e) {
                if (session.getTransaction().isActive()) {
                    session.getTransaction().rollback();
                }
                return false;
            }
        } else {
            return false;
        }
        return true;
    }

    @Override
    public boolean deleteDataInsurance(TblEmployeeInsurance insurance) {
        errMessage = "";
        if (ClassSession.checkUserSession()) {  //check user current sessio
            try {
                session = HibernateUtil.getSessionFactory().getCurrentSession();
                session.beginTransaction();
                if (insurance.getRefRecordStatus().getIdstatus() == 1) {
                    insurance.setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                    insurance.setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                    insurance.setRefRecordStatus(session.find(RefRecordStatus.class, 2));
                    session.update(insurance);
                } else {
                    if (session.getTransaction().isActive()) {
                        session.getTransaction().rollback();
                    }
                    errMessage = "Data tidak dapat dihapus!!";
                    return false;
                }
                session.getTransaction().commit();
            } catch (Exception e) {
                if (session.getTransaction().isActive()) {
                    session.getTransaction().rollback();
                }
                return false;
            }
        } else {
            return false;
        }
        return true;
    }

    @Override
    public TblEmployeeInsurance getDataInsurance(long id) {
        errMessage = "";
        TblEmployeeInsurance data = null;
        if (ClassSession.checkUserSession()) {  //check user current sessio
            try {
                session = HibernateUtil.getSessionFactory().getCurrentSession();
                session.beginTransaction();
                data = (TblEmployeeInsurance) session.find(TblEmployeeInsurance.class, id);
                session.getTransaction().commit();
            } catch (Exception e) {
                if (session.getTransaction().isActive()) {
                    session.getTransaction().rollback();
                }
                errMessage = e.getMessage();
            }
        }
        return data;
    }

    @Override
    public List<TblEmployeeInsurance> getAllDataInsurance() {
        errMessage = "";
        List<TblEmployeeInsurance> list = new ArrayList();
        if (ClassSession.checkUserSession()) {  //check user current sessio
            try {
                session = HibernateUtil.getSessionFactory().getCurrentSession();
                session.beginTransaction();
                list = session.getNamedQuery("findAllTblEmployeeInsurance").list();
                session.getTransaction().commit();
            } catch (Exception e) {
                if (session.getTransaction().isActive()) {
                    session.getTransaction().rollback();
                }
                errMessage = e.getMessage();
            }
        }
        return list;
    }

    @Override
    public String getErrorMessage() {
        return errMessage;
    }
}
