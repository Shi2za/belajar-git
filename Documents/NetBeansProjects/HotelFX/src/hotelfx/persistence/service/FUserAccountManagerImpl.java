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
import hotelfx.persistence.model.RefSystemUserLockStatus;
import hotelfx.persistence.model.RefSystemUserLoginStatus;
import hotelfx.persistence.model.SysDataHardCode;
import hotelfx.persistence.model.TblEmployee;
import hotelfx.persistence.model.TblJob;
import hotelfx.persistence.model.TblPeople;
import hotelfx.persistence.model.TblSystemRole;
import hotelfx.persistence.model.TblSystemUser;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import org.hibernate.Session;

/**
 *
 * @author Andreas
 */
public class FUserAccountManagerImpl implements FUserAccountManager {

    String errMessage;
    Session session;

    @Override
    public TblSystemUser insertDataUserAccount(TblSystemUser user) {
        errMessage = "";
        TblSystemUser tblUser = user;
        if (ClassSession.checkUserSession()) {  //check user current session
            try {
                session = HibernateUtil.getSessionFactory().getCurrentSession();
                session.beginTransaction();
                //check user name has been used
                List<TblSystemUser> list = session.getNamedQuery("findAllTblSystemUserByCodeUser")
                        .setParameter("codeUser", tblUser.getCodeUser())
                        .list();
                if (!list.isEmpty()) {
                    if (session.getTransaction().isActive()) {
                        session.getTransaction().rollback();
                    }
                    errMessage = "Duplikasi User Name!!";
                    return null;
                }
                //data user
                tblUser.setUserPassword(ClassCoder.MD5(user.getUserPassword()));
                tblUser.setRefSystemUserLoginStatus(session.find(RefSystemUserLoginStatus.class, (int) 0));
                tblUser.setCreateDate(Timestamp.valueOf(LocalDateTime.now()));
                tblUser.setTblEmployeeByCreateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                tblUser.setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                tblUser.setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                tblUser.setRefRecordStatus(session.find(RefRecordStatus.class, 1));
                session.saveOrUpdate(user);
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
        return tblUser;
    }

    @Override
    public boolean updateDataUserAccount(TblSystemUser user) {
        errMessage = "";
        if (ClassSession.checkUserSession()) {  //check user current session
            try {
                session = HibernateUtil.getSessionFactory().getCurrentSession();
                session.beginTransaction();
                //check user name has been used
                List<TblSystemUser> list = session.getNamedQuery("findAllTblSystemUserByCodeUser")
                        .setParameter("codeUser", user.getCodeUser())
                        .list();
                if (!list.isEmpty()
                        && list.get(0).getIduser() != user.getIduser()) {
                    if (session.getTransaction().isActive()) {
                        session.getTransaction().rollback();
                    }
                    errMessage = "Duplikasi User Name!!";
                    return false;
                }
                if (!list.isEmpty()) {
                    session.evict(list.get(0));
                }
                //data user
                user.setUserPassword(ClassCoder.MD5(user.getUserPassword()));
                user.setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                user.setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                session.update(user);
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
    public boolean deleteDataUserAccount(TblSystemUser user) {
        errMessage = "";
        if (ClassSession.checkUserSession()) {  //check user current session
            try {
                session = HibernateUtil.getSessionFactory().getCurrentSession();
                session.beginTransaction();
                if (user.getRefRecordStatus().getIdstatus() == 1) {
                    user.setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                    user.setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                    user.setRefRecordStatus(session.find(RefRecordStatus.class, 2));
                    session.update(user);
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
    public List<TblSystemUser> getAllDataUserAccount() {
        errMessage = "";
        List<TblSystemUser> list = new ArrayList();
        if (ClassSession.checkUserSession()) {  //check user current session
            try {
                session = HibernateUtil.getSessionFactory().getCurrentSession();
                session.beginTransaction();
                list = session.getNamedQuery("findAllTblSystemUser").list();
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
    public List<TblEmployee> getAllDataEmployee() {
        errMessage = "";
        List<TblEmployee> list = new ArrayList();
        if (ClassSession.checkUserSession()) {  //check user current session
            try {
                session = HibernateUtil.getSessionFactory().getCurrentSession();
                session.beginTransaction();
                list = session.getNamedQuery("findAllTblEmployee").list();
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
    public List<TblPeople> getAllDataPeople() {
        errMessage = "";
        List<TblPeople> list = new ArrayList();
        if (ClassSession.checkUserSession()) {  //check user current session
            try {
                session = HibernateUtil.getSessionFactory().getCurrentSession();
                session.beginTransaction();
                list = session.getNamedQuery("findAllTblPeople").list();
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
    public List<RefSystemUserLoginStatus> getAllDataLoginStatus() {
        errMessage = "";
        List<RefSystemUserLoginStatus> list = new ArrayList();
        if (ClassSession.checkUserSession()) {  //check user current session
            try {
                session = HibernateUtil.getSessionFactory().getCurrentSession();
                session.beginTransaction();
                list = session.getNamedQuery("findAllRefSystemUserLoginStatus").list();
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
    public List<RefSystemUserLockStatus> getAllDataLockStatus() {
        errMessage = "";
        List<RefSystemUserLockStatus> list = new ArrayList();
        if (ClassSession.checkUserSession()) {  //check user current session
            try {
                session = HibernateUtil.getSessionFactory().getCurrentSession();
                session.beginTransaction();
                list = session.getNamedQuery("findAllRefSystemUserLockStatus").list();
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
    public TblSystemUser getDataUserAccount(long id) {
        errMessage = "";
        TblSystemUser data = null;
        if (ClassSession.checkUserSession()) {  //check user current session
            try {
                session = HibernateUtil.getSessionFactory().getCurrentSession();
                session.beginTransaction();
                data = (TblSystemUser) session.find(TblSystemUser.class, id);
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
    public TblEmployee getDataEmployee(long id) {
        errMessage = "";
        TblEmployee data = null;
        if (ClassSession.checkUserSession()) {  //check user current session
            try {
                session = HibernateUtil.getSessionFactory().getCurrentSession();
                session.beginTransaction();
                data = (TblEmployee) session.find(TblEmployee.class, id);
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
    public TblPeople getDataPeople(long id) {
        errMessage = "";
        TblPeople data = null;
        if (ClassSession.checkUserSession()) {  //check user current session
            try {
                session = HibernateUtil.getSessionFactory().getCurrentSession();
                session.beginTransaction();
                data = (TblPeople) session.find(TblPeople.class, id);
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
    public TblJob getDataJob(long id) {
        errMessage = "";
        TblJob data = null;
        if (ClassSession.checkUserSession()) {  //check user current session
            try {
                session = HibernateUtil.getSessionFactory().getCurrentSession();
                session.beginTransaction();
                data = (TblJob) session.find(TblJob.class, id);
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
    public RefSystemUserLockStatus getDataLockStatus(int id) {
        errMessage = "";
        RefSystemUserLockStatus data = null;
        if (ClassSession.checkUserSession()) {  //check user current session
            try {
                session = HibernateUtil.getSessionFactory().getCurrentSession();
                session.beginTransaction();
                data = (RefSystemUserLockStatus) session.find(RefSystemUserLockStatus.class, id);
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
    public RefSystemUserLoginStatus getDataLoginStatus(int id) {
        errMessage = "";
        RefSystemUserLoginStatus data = null;
        if (ClassSession.checkUserSession()) {  //check user current session
            try {
                session = HibernateUtil.getSessionFactory().getCurrentSession();
                session.beginTransaction();
                data = (RefSystemUserLoginStatus) session.find(RefSystemUserLoginStatus.class, id);
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
    public SysDataHardCode getDataSysDataHardCode(long id) {
        errMessage = "";
        SysDataHardCode data = null;
        if (ClassSession.checkUserSession()) {  //check user current session
            try {
                session = HibernateUtil.getSessionFactory().getCurrentSession();
                session.beginTransaction();
                data = (SysDataHardCode) session.find(SysDataHardCode.class, id);
                session.getTransaction().commit();
            } catch (Exception e) {
                if (session.getTransaction().isActive()) {
                    session.getTransaction().rollback();
                }
                errMessage = e.getMessage();
            } finally {
                //session.close();
            }
        }
        return data;
    }

    @Override
    public List<SysDataHardCode> getAllDataSysDataHardCode() {
        errMessage = "";
        List<SysDataHardCode> list = new ArrayList<>();
        if (ClassSession.checkUserSession()) {  //check user current session
            try {
                session = HibernateUtil.getSessionFactory().getCurrentSession();
                session.beginTransaction();
                list = session.getNamedQuery("findAllSysDataHardCode").list();
                session.getTransaction().commit();
            } catch (Exception e) {
                if (session.getTransaction().isActive()) {
                    session.getTransaction().rollback();
                }
                errMessage = e.getMessage();
            } finally {
                //session.close();
            }
        }
        return list;
    }

    @Override
    public String getErrorMessage() {
        return errMessage;
    }
}
