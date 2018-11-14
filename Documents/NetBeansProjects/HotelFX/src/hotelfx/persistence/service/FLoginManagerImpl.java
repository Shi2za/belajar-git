/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hotelfx.persistence.service;

import hotelfx.helper.ClassCoder;
import hotelfx.helper.ClassSession;
import hotelfx.persistence.HibernateUtil;
import hotelfx.persistence.model.SysDataHardCode;
import hotelfx.persistence.model.TblEmployee;
import hotelfx.persistence.model.TblJob;
import hotelfx.persistence.model.TblPeople;
import hotelfx.persistence.model.TblSystemLogBook;
import hotelfx.persistence.model.TblSystemLogBookJob;
import hotelfx.persistence.model.TblSystemRoleSystemFeature;
import hotelfx.persistence.model.TblSystemUser;
import java.sql.Date;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import org.hibernate.Session;
import org.hibernate.query.Query;

/**
 *
 * @author ANDRI
 */
public class FLoginManagerImpl implements FLoginManager {

    private Session session;

    private String errMsg;

    public FLoginManagerImpl() {

    }

    @Override
    public TblSystemUser doLogin(String username, String password) {
        errMsg = "";
        TblSystemUser data = null;
        try {
            session = HibernateUtil.getSessionFactory().getCurrentSession();
            session.beginTransaction();
            List<TblSystemUser> list = (List<TblSystemUser>) session.getNamedQuery("findTblSystemUserByUsernamePassword")
                    .setParameter("codeUser", username)
                    .setParameter("userPassword", ClassCoder.MD5(password))
//                    .setParameter("userPassword", password)
                    .list();
            if (!list.isEmpty()) {
                if (list.get(0).getRefSystemUserLockStatus().getIdstatus() == 1) {   //Unlocked = '1'
                    Query rs = session.getNamedQuery("updateTblSystemUserToLogin")
                            .setParameter("idUser", list.get(0).getIduser());
                    errMsg = (String) rs.uniqueResult();
                    if (!errMsg.equals("")) {
                        if (session.getTransaction().isActive()) {
                            session.getTransaction().rollback();
                        }
                        return null;
                    }
                    data = session.find(TblSystemUser.class, list.get(0).getIduser());
                    session.refresh(data);
//                data.setTblEmployeeByIdemployee(session.find(TblEmployee.class, data.getTblEmployeeByIdemployee().getIdemployee()));
                } else {
                    session.evict(list.get(0));
                    errMsg = "User '" + username + "' tidak diberi izin untuk mengakses sistem..!";
                    if (session.getTransaction().isActive()) {
                        session.getTransaction().rollback();
                    }
                    return null;
                }
            } else {
//                errMsg = "Kombinasi User Name = '"
//                        + username + "' dan Password = '"
//                        + password + "' tidak ditemukan..!";
                errMsg = "Kombinasi User Name dan Password tidak ditemukan..!";
                if (session.getTransaction().isActive()) {
                    session.getTransaction().rollback();
                }
                return null;
            }
            session.getTransaction().commit();
        } catch (Exception e) {
            errMsg = e.getMessage();
            if (session.getTransaction().isActive()) {
                session.getTransaction().rollback();
            }
            return null;
        } finally {
            //session.close();
        }
        return data;
    }

    @Override
    public boolean doLogout(long id) {
        errMsg = "";
        if (ClassSession.checkUserSession()) {  //check user current session
            try {
                session = HibernateUtil.getSessionFactory().getCurrentSession();
                session.beginTransaction();
                Query rs = session.getNamedQuery("updateTblSystemUserToLogout")
                        .setParameter("idUser", id);
                errMsg = (String) rs.uniqueResult();
                if (!errMsg.equals("")) {
                    if (session.getTransaction().isActive()) {
                        session.getTransaction().rollback();
                    }
                    return false;
                }
                session.getTransaction().commit();
            } catch (Exception e) {
                errMsg = e.getMessage();
                if (session.getTransaction().isActive()) {
                    session.getTransaction().rollback();
                }
                return false;
            } finally {
                //session.close();
            }
        } else {
            return false;
        }
        return true;
    }

    @Override
    public boolean updateDataUserAccount(TblSystemUser user,
            String imgExtention) {
        errMsg = "";
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
                    errMsg = "Duplikasi User Name!!";
                    return false;
                }
                if (!list.isEmpty()) {
                    session.evict(list.get(0));
                }
                //data user
                user.setUserPassword(ClassCoder.MD5(user.getUserPassword()));
                user.setUserUrlImage("UA_" + user.getIduser() + "." + imgExtention);
                user.setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                user.setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                session.update(user);
                session.getTransaction().commit();
            } catch (Exception e) {
                if (session.getTransaction().isActive()) {
                    session.getTransaction().rollback();
                }
                errMsg = e.getMessage();
                return false;
            }
        } else {
            return false;
        }
        return true;
    }

    @Override
    public TblSystemUser getDataUserAccount(long id) {
        errMsg = "";
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
                errMsg = e.getMessage();
            }
        }
        return data;
    }

    @Override
    public TblSystemUser getDataUserAccountByIDUserAccountAndGUID(long idUser,
            String guid) {
        errMsg = "";
        TblSystemUser data = null;
//        if (ClassSession.checkUserSession()) {  //check user current session
        try {
            session = HibernateUtil.getSessionFactory().getCurrentSession();
            session.beginTransaction();
            data = (TblSystemUser) session.find(TblSystemUser.class, idUser);
//            if (data != null
//                    && (!data.getUserGuid().equals(guid)
//                    || data.getRefSystemUserLockStatus().getIdstatus() == 0)) {   //Locked = '0'
            if (data != null
                    && !data.getUserGuid().equals(guid)) {
                data = null;
            }
            session.getTransaction().commit();
        } catch (Exception e) {
            if (session.getTransaction().isActive()) {
                session.getTransaction().rollback();
            }
            errMsg = e.getMessage();
        }
//        }
        return data;
    }

    @Override
    public List<TblSystemRoleSystemFeature> getRoleFeatureByRole(long idRole) {
        errMsg = "";
        List<TblSystemRoleSystemFeature> list = new ArrayList<>();
        if (ClassSession.checkUserSession()) {  //check user current session
            try {
                session = HibernateUtil.getSessionFactory().getCurrentSession();
                session.beginTransaction();
                list = session.getNamedQuery("findAllTblSystemRoleSystemFeatureByIdRole")
                        .setParameter("idRole", idRole)
                        .list();
                session.getTransaction().commit();
            } catch (Exception e) {
                if (session.getTransaction().isActive()) {
                    session.getTransaction().rollback();
                }
                errMsg = e.getMessage();
            } finally {
                //session.close();
            }
        }
        return list;
    }

    @Override
    public List<TblSystemRoleSystemFeature> getRoleFeatureByRoleOrderByIDFeature(long idRole) {
        errMsg = "";
        List<TblSystemRoleSystemFeature> list = new ArrayList<>();
        if (ClassSession.checkUserSession()) {  //check user current session
            try {
                session = HibernateUtil.getSessionFactory().getCurrentSession();
                session.beginTransaction();
                list = session.getNamedQuery("findAllTblSystemRoleSystemFeatureByIdRoleOrderByIdFeature")
                        .setParameter("idRole", idRole)
                        .list();
                session.getTransaction().commit();
            } catch (Exception e) {
                if (session.getTransaction().isActive()) {
                    session.getTransaction().rollback();
                }
                errMsg = e.getMessage();
            } finally {
                //session.close();
            }
        }
        return list;
    }

    @Override
    public SysDataHardCode getDataSysDataHardCode(long id) {
        errMsg = "";
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
                errMsg = e.getMessage();
            } finally {
                //session.close();
            }
        }
        return data;
    }

    @Override
    public TblPeople getDataPeopleByUserAccount(TblSystemUser userAccount) {
        errMsg = "";
        TblPeople data = null;
        if (ClassSession.checkUserSession()) {  //check user current session
            try {
                session = HibernateUtil.getSessionFactory().getCurrentSession();
                session.beginTransaction();
                TblEmployee dataEmployee = session.find(TblEmployee.class, userAccount.getTblEmployeeByIdemployee().getIdemployee());
                data = (TblPeople) session.find(TblPeople.class, dataEmployee.getTblPeople().getIdpeople());
                session.getTransaction().commit();
            } catch (Exception e) {
                if (session.getTransaction().isActive()) {
                    session.getTransaction().rollback();
                }
                errMsg = e.getMessage();
            } finally {
                //session.close();
            }
        }
        return data;
    }

    @Override
    public List<TblSystemLogBook> getAllDataCurrentReminder(TblSystemUser userAccount) {
        errMsg = "";
        List<TblSystemLogBook> list = new ArrayList<>();
        if (ClassSession.checkUserSession()) {  //check user current session
            try {
                session = HibernateUtil.getSessionFactory().getCurrentSession();
                session.beginTransaction();
                if (userAccount.getTblEmployeeByIdemployee().getTblJob() == null) {
                    list = session.getNamedQuery("findAllTblSystemLogBookByIsReminderAndReminderDateInDate")
                            .setParameter("isReminder", true)
                            .setParameter("reminderDate", Date.valueOf(LocalDate.now()))
                            .list();
                } else {
                    List<TblSystemLogBookJob> systemLogBookJobs = session.getNamedQuery("findAllTblSystemLogBookJobByIDJobAndIsReminderAndReminderDateInDate")
                            .setParameter("idJob", userAccount.getTblEmployeeByIdemployee().getTblJob().getIdjob())
                            .setParameter("isReminder", true)
                            .setParameter("reminderDate", Date.valueOf(LocalDate.now()))
                            .list();
                    for(TblSystemLogBookJob systemLogBookJob : systemLogBookJobs){
                        list.add(session.find(TblSystemLogBook.class, systemLogBookJob.getTblSystemLogBook().getIdlogBook()));
                    }
                }
                session.getTransaction().commit();
            } catch (Exception e) {
                if (session.getTransaction().isActive()) {
                    session.getTransaction().rollback();
                }
                errMsg = e.getMessage();
            } finally {
                //session.close();
            }
        }
        return list;
    }

    @Override
    public List<TblSystemLogBook> getAllDataCurrentReminderBySelectedDate(TblSystemUser userAccount, 
            LocalDate selectedDate) {
        errMsg = "";
        List<TblSystemLogBook> list = new ArrayList<>();
        if (ClassSession.checkUserSession()) {  //check user current session
            try {
                session = HibernateUtil.getSessionFactory().getCurrentSession();
                session.beginTransaction();
                if (userAccount.getTblEmployeeByIdemployee().getTblJob() == null) {
                    list = session.getNamedQuery("findAllTblSystemLogBookByIsReminderAndReminderDateInDate")
                            .setParameter("isReminder", true)
                            .setParameter("reminderDate", Date.valueOf(selectedDate))
                            .list();
                } else {
                    List<TblSystemLogBookJob> systemLogBookJobs = session.getNamedQuery("findAllTblSystemLogBookJobByIDJobAndIsReminderAndReminderDateInDate")
                            .setParameter("idJob", userAccount.getTblEmployeeByIdemployee().getTblJob().getIdjob())
                            .setParameter("isReminder", true)
                            .setParameter("reminderDate", Date.valueOf(selectedDate))
                            .list();
                    for(TblSystemLogBookJob systemLogBookJob : systemLogBookJobs){
                        list.add(session.find(TblSystemLogBook.class, systemLogBookJob.getTblSystemLogBook().getIdlogBook()));
                    }
                }
                session.getTransaction().commit();
            } catch (Exception e) {
                if (session.getTransaction().isActive()) {
                    session.getTransaction().rollback();
                }
                errMsg = e.getMessage();
            } finally {
                //session.close();
            }
        }
        return list;
    }
    
    @Override
    public TblSystemUser getDataUser(long id) {
        errMsg = "";
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
                errMsg = e.getMessage();
            } finally {
                //session.close();
            }
        }
        return data;
    }

    @Override
    public TblEmployee getDataEmployee(long id) {
        errMsg = "";
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
                errMsg = e.getMessage();
            } finally {
                //session.close();
            }
        }
        return data;
    }

    @Override
    public TblJob getDataJob(long id) {
        errMsg = "";
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
                errMsg = e.getMessage();
            } finally {
                //session.close();
            }
        }
        return data;
    }

    @Override
    public TblPeople getDataPeople(long id) {
        errMsg = "";
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
                errMsg = e.getMessage();
            } finally {
                //session.close();
            }
        }
        return data;
    }

    @Override
    public String getErrorMessage() {
        return errMsg;
    }

}
