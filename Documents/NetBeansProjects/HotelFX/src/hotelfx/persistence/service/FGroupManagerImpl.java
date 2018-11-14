/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hotelfx.persistence.service;

import hotelfx.helper.ClassCoder;
import hotelfx.helper.ClassSession;
import hotelfx.persistence.HibernateUtil;
import hotelfx.persistence.model.RefRecordStatus;
import hotelfx.persistence.model.TblEmployee;
import hotelfx.persistence.model.TblGroup;
import hotelfx.persistence.model.TblLocation;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import org.hibernate.Session;

/**
 *
 * @author Andreas
 */
public class FGroupManagerImpl implements FGroupManager {

    private String errMessage;

    private Session session;

    @Override
    public TblGroup insertDataGroup(TblGroup group) {
        errMessage = "";
        TblGroup tblGroup = group;
        if (ClassSession.checkUserSession()) {  //check user current sessio
            try {
                session = HibernateUtil.getSessionFactory().getCurrentSession();
                session.beginTransaction();
                tblGroup.setCodeGroup(ClassCoder.getCode("Department", session));
                tblGroup.setCreateDate(Timestamp.valueOf(LocalDateTime.now()));
                tblGroup.setTblEmployeeByCreateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                tblGroup.setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                tblGroup.setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                tblGroup.setRefRecordStatus(session.find(RefRecordStatus.class, 1));
                session.saveOrUpdate(tblGroup);
                session.getTransaction().commit();
            } catch (Exception e) {
                if (session.getTransaction().isActive()) {
                    session.getTransaction().rollback();
                }
                errMessage = e.getMessage();
            }
        } else {
            return null;
        }
        return tblGroup;
    }

    @Override
    public boolean updateDataGroup(TblGroup group) {
        errMessage = "";
        if (ClassSession.checkUserSession()) {  //check user current sessio
            try {
                session = HibernateUtil.getSessionFactory().getCurrentSession();
                session.beginTransaction();
                group.setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                group.setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                session.update(group);
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
    public boolean deleteDataGroup(TblGroup group) {
        errMessage = "";
        if (ClassSession.checkUserSession()) {  //check user current sessio
            try {
                session = HibernateUtil.getSessionFactory().getCurrentSession();
                session.beginTransaction();
                if (group.getRefRecordStatus().getIdstatus() == 1) {
                    group.setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                    group.setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                    group.setRefRecordStatus(session.find(RefRecordStatus.class, 2));
                    session.update(group);
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
    public TblGroup getDataGroup(long id) {
        errMessage = "";
        TblGroup data = null;
        if (ClassSession.checkUserSession()) {  //check user current sessio
            try {
                session = HibernateUtil.getSessionFactory().getCurrentSession();
                session.beginTransaction();
                data = (TblGroup) session.find(TblGroup.class, id);
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
    public List<TblGroup> getAllDataGroup() {
        errMessage = "";
        List<TblGroup> list = new ArrayList();
        if (ClassSession.checkUserSession()) {  //check user current sessio
            try {
                session = HibernateUtil.getSessionFactory().getCurrentSession();
                session.beginTransaction();
                list = session.getNamedQuery("findAllTblGroup").list();
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
    public List<TblLocation> getAllDataLocationByIDGroup(long idGroup){
        errMessage = "";
        List<TblLocation> list = new ArrayList();
        if (ClassSession.checkUserSession()) {  //check user current sessio
            try {
                session = HibernateUtil.getSessionFactory().getCurrentSession();
                session.beginTransaction();
                list = session.getNamedQuery("findAllTblLocationByIDGroup")
                        .setParameter("idGroup", idGroup)
                        .list();
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
