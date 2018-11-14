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
import hotelfx.persistence.model.TblEmployeeAllowance;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import org.hibernate.Session;

/**
 *
 * @author Andreas
 */
public class FAllowanceManagerImpl implements FAllowanceManager {

    private String errMessage;

    private Session session;

    @Override
    public TblEmployeeAllowance insertDataAllowance(TblEmployeeAllowance allowance) {
        errMessage = "";
        TblEmployeeAllowance tblallowance = allowance;
        if (ClassSession.checkUserSession()) {  //check user current session
            try {
                session = HibernateUtil.getSessionFactory().getCurrentSession();
                session.beginTransaction();
                tblallowance.setCreateDate(Timestamp.valueOf(LocalDateTime.now()));
                tblallowance.setTblEmployeeByCreateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                tblallowance.setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                tblallowance.setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                tblallowance.setRefRecordStatus(session.find(RefRecordStatus.class, 1));
                session.saveOrUpdate(tblallowance);
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
        return tblallowance;
    }

    @Override
    public boolean updateDataAllowance(TblEmployeeAllowance allowance) {
        errMessage = "";
        if (ClassSession.checkUserSession()) {  //check user current session
            try {
                session = HibernateUtil.getSessionFactory().getCurrentSession();
                session.beginTransaction();
                allowance.setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                allowance.setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                session.update(allowance);
                session.getTransaction().commit();
            } catch (Exception e) {
                if (session.getTransaction().isActive()) {
                    session.getTransaction().rollback();
                }
                errMessage = e.getMessage();
                return false;
            }
        } else {
            return false;
        }
        return true;
    }

    @Override
    public boolean deleteDataAllowance(TblEmployeeAllowance allowance) {
        errMessage = "";
        if (ClassSession.checkUserSession()) {  //check user current session
            try {
                session = HibernateUtil.getSessionFactory().getCurrentSession();
                session.beginTransaction();
                if (allowance.getRefRecordStatus().getIdstatus() == 1) {
                    allowance.setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                    allowance.setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                    allowance.setRefRecordStatus(session.find(RefRecordStatus.class, 2));
                    session.update(allowance);
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
                errMessage = e.getMessage();
                return false;
            }
        } else {
            return false;
        }
        return true;
    }

    @Override
    public TblEmployeeAllowance getDataAllowance(long id) {
        errMessage = "";
        TblEmployeeAllowance data = null;
        if (ClassSession.checkUserSession()) {  //check user current session
            try {
                session = HibernateUtil.getSessionFactory().getCurrentSession();
                session.beginTransaction();
                data = (TblEmployeeAllowance) session.find(TblEmployeeAllowance.class, id);
            //jika keterangna <> ''
//            
//            isi data table ngambil nama detail item
//            
                // data.item_nama keganti sesuai item yg kit mau
                session.getTransaction().commit();
            } catch (Exception e) {
                if (session.getTransaction().isActive()) {
                    session.getTransaction().rollback();
                }
            }
        }
        return data;
    }

    @Override
    public List<TblEmployeeAllowance> getAllDataAllowance() {
        List<TblEmployeeAllowance> list = new ArrayList();
        if (ClassSession.checkUserSession()) {  //check user current session
            try {
                session = HibernateUtil.getSessionFactory().getCurrentSession();
                session.beginTransaction();
                list = session.getNamedQuery("findAllTblEmployeeAllowance").list();
                session.getTransaction().commit();
            } catch (Exception e) {
                if (session.getTransaction().isActive()) {
                    session.getTransaction().rollback();
                }
            }
        }
        return list;
    }

    @Override
    public String getErrorMessage() {
        return errMessage;
    }
}
