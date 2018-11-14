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
import hotelfx.persistence.model.TblSystemRole;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import org.hibernate.Session;

/**
 *
 * @author Andreas
 */
public class FRoleManagerImpl implements FRoleManager {

    Session session;
    String errMessage;

    @Override
    public TblSystemRole insertDataRole(TblSystemRole role) {
        TblSystemRole tblRole = role;
        errMessage = "";
        if (ClassSession.checkUserSession()) {  //check user current session
            try {
                session = HibernateUtil.getSessionFactory().getCurrentSession();
                session.beginTransaction();
                //check role name has been used
                List<TblSystemRole> list = session.getNamedQuery("findAllTblSystemRoleByRoleName")
                        .setParameter("roleName", tblRole.getRoleName())
                        .list();
                if (!list.isEmpty()) {
                    if (session.getTransaction().isActive()) {
                        session.getTransaction().rollback();
                    }
                    errMessage = "Duplikasi Nama Role!!";
                    return null;
                }
                //data role
                tblRole.setCreateDate(Timestamp.valueOf(LocalDateTime.now()));
                tblRole.setTblEmployeeByCreateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                tblRole.setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                tblRole.setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                tblRole.setRefRecordStatus(session.find(RefRecordStatus.class, 1));
                session.saveOrUpdate(tblRole);
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
        return tblRole;
    }

    @Override
    public boolean updateDataRole(TblSystemRole role) {
        errMessage = "";
        if (ClassSession.checkUserSession()) {  //check user current session
            try {
                session = HibernateUtil.getSessionFactory().getCurrentSession();
                session.beginTransaction();
                //check role name has been used
                List<TblSystemRole> list = session.getNamedQuery("findAllTblSystemRoleByRoleName")
                        .setParameter("roleName", role.getRoleName())
                        .list();
                if (!list.isEmpty()
                        && list.get(0).getIdrole() != role.getIdrole()) {
                    if (session.getTransaction().isActive()) {
                        session.getTransaction().rollback();
                    }
                    errMessage = "Duplikasi Nama Role!!";
                    return false;
                }
                if (!list.isEmpty()) {
                    session.evict(list.get(0));
                }
                //data role
                role.setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                role.setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                session.update(role);
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
    public boolean deleteDataRole(TblSystemRole role) {
        errMessage = "";
        if (ClassSession.checkUserSession()) {  //check user current session
            try {
                session = HibernateUtil.getSessionFactory().getCurrentSession();
                session.beginTransaction();
                if (role.getRefRecordStatus().getIdstatus() == 1) {
                    role.setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                    role.setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                    role.setRefRecordStatus(session.find(RefRecordStatus.class, 2));
                    session.update(role);
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
    public TblSystemRole getDataRole(long id) {
        errMessage = "";
        TblSystemRole data = null;
        if (ClassSession.checkUserSession()) {  //check user current session
            try {
                session = HibernateUtil.getSessionFactory().getCurrentSession();
                session.beginTransaction();
                data = (TblSystemRole) session.find(TblSystemRole.class, id);
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
    public List<TblSystemRole> getAllDataRole() {
        errMessage = "";
        List<TblSystemRole> list = new ArrayList();
        if (ClassSession.checkUserSession()) {  //check user current session
            try {
                session = HibernateUtil.getSessionFactory().getCurrentSession();
                session.beginTransaction();
                list = session.getNamedQuery("findAllTblSystemRole").list();
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
