/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hotelfx.persistence.service;

import hotelfx.helper.ClassComp;
import hotelfx.helper.ClassSession;
import hotelfx.persistence.HibernateUtil;
import hotelfx.persistence.model.RefRecordStatus;
import hotelfx.persistence.model.TblEmployee;
import hotelfx.persistence.model.TblJob;
import hotelfx.persistence.model.TblPeople;
import hotelfx.persistence.model.TblSystemLogBook;
import hotelfx.persistence.model.TblSystemLogBookJob;
import hotelfx.persistence.model.TblSystemUser;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import org.hibernate.Session;
import org.hibernate.query.Query;

/**
 *
 * @author ANDRI
 */
public class FLogBookManagerImpl implements FLogBookManager {

    private Session session;

    private String errMsg;

    public FLogBookManagerImpl() {

    }

    @Override
    public TblSystemLogBook insertDataSystemLogBook(TblSystemLogBook dataSystemLogBook,
            List<TblSystemLogBookJob> systemLogBookJobs) {
        errMsg = "";
        TblSystemLogBook tblSystemLogBook = dataSystemLogBook;
        if (ClassSession.checkUserSession()) {  //check user current session
            try {
                session = HibernateUtil.getSessionFactory().getCurrentSession();
                session.beginTransaction();
                //data log book
                tblSystemLogBook.setLogBookDateTime(Timestamp.valueOf(LocalDateTime.now()));
                tblSystemLogBook.setTblSystemUser(session.find(TblSystemUser.class, ClassSession.currentUser.getIduser()));
                tblSystemLogBook.setTblEmployeeByIdemployee(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                tblSystemLogBook.setHostName(ClassComp.getHostName());
                tblSystemLogBook.setCreateDate(Timestamp.valueOf(LocalDateTime.now()));
                tblSystemLogBook.setTblEmployeeByCreateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                tblSystemLogBook.setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                tblSystemLogBook.setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                tblSystemLogBook.setRefRecordStatus(session.find(RefRecordStatus.class, 1));
                session.saveOrUpdate(tblSystemLogBook);
                //data log book - job
                for (TblSystemLogBookJob systemLogBookJob : systemLogBookJobs) {
                    systemLogBookJob.setCreateDate(Timestamp.valueOf(LocalDateTime.now()));
                    systemLogBookJob.setTblEmployeeByCreateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                    systemLogBookJob.setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                    systemLogBookJob.setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                    systemLogBookJob.setRefRecordStatus(session.find(RefRecordStatus.class, 1));
                    session.saveOrUpdate(systemLogBookJob);
                }
                session.getTransaction().commit();
            } catch (Exception e) {
                if (session.getTransaction().isActive()) {
                    session.getTransaction().rollback();
                }
                errMsg = e.getMessage();
                return null;
            } finally {
                //session.close();
            }
        } else {
            return null;
        }
        return tblSystemLogBook;
    }

    @Override
    public boolean updateDataSystemLogBook(TblSystemLogBook dataSystemLogBook,
            List<TblSystemLogBookJob> systemLogBookJobs) {
        errMsg = "";
        if (ClassSession.checkUserSession()) {  //check user current session
            try {
                session = HibernateUtil.getSessionFactory().getCurrentSession();
                session.beginTransaction();
                //data log book
                dataSystemLogBook.setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                dataSystemLogBook.setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                session.update(dataSystemLogBook);
                //delete all (data log book - job)
                Query rs = session.getNamedQuery("deleteAllTblSystemLogBookJob")
                        .setParameter("idLogBook", dataSystemLogBook.getIdlogBook())
                        .setParameter("idEmployee", ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee());
                errMsg = (String) rs.uniqueResult();
                if (!errMsg.equals("")) {
                    if (session.getTransaction().isActive()) {
                        session.getTransaction().rollback();
                    }
                    return false;
                }
                //insert all data log book - job
                for (TblSystemLogBookJob systemLogBookJob : systemLogBookJobs) {
                    systemLogBookJob.setCreateDate(Timestamp.valueOf(LocalDateTime.now()));
                    systemLogBookJob.setTblEmployeeByCreateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                    systemLogBookJob.setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                    systemLogBookJob.setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                    systemLogBookJob.setRefRecordStatus(session.find(RefRecordStatus.class, 1));
                    session.saveOrUpdate(systemLogBookJob);
                }
                session.getTransaction().commit();
            } catch (Exception e) {
                if (session.getTransaction().isActive()) {
                    session.getTransaction().rollback();
                }
                errMsg = e.getMessage();
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
    public boolean deleteDataSystemLogBook(TblSystemLogBook dataSystemLogBook) {
        errMsg = "";
        if (ClassSession.checkUserSession()) {  //check user current session
            try {
                session = HibernateUtil.getSessionFactory().getCurrentSession();
                session.beginTransaction();
                if (dataSystemLogBook.getRefRecordStatus().getIdstatus() == 1) {
                    //data log book
                    dataSystemLogBook.setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                    dataSystemLogBook.setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                    dataSystemLogBook.setRefRecordStatus(session.find(RefRecordStatus.class, 2));
                    session.update(dataSystemLogBook);
                    //delete all (data log book - job)
                    Query rs = session.getNamedQuery("deleteAllTblSystemLogBookJob")
                            .setParameter("idLogBook", dataSystemLogBook.getIdlogBook())
                            .setParameter("idEmployee", ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee());
                    errMsg = (String) rs.uniqueResult();
                    if (!errMsg.equals("")) {
                        if (session.getTransaction().isActive()) {
                            session.getTransaction().rollback();
                        }
                        return false;
                    }
                } else {
                    if (session.getTransaction().isActive()) {
                        session.getTransaction().rollback();
                    }
                    errMsg = "Data tidak dapat dihapus!!";
                    return false;
                }
                session.getTransaction().commit();
            } catch (Exception e) {
                if (session.getTransaction().isActive()) {
                    session.getTransaction().rollback();
                }
                errMsg = e.getMessage();
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
    public TblSystemLogBook getDataSystemLogBook(long id) {
        errMsg = "";
        TblSystemLogBook data = null;
        if (ClassSession.checkUserSession()) {  //check user current session
            try {
                session = HibernateUtil.getSessionFactory().getCurrentSession();
                session.beginTransaction();
                data = (TblSystemLogBook) session.find(TblSystemLogBook.class, id);
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
    public List<TblSystemLogBook> getAllDataSystemLogBook() {
        errMsg = "";
        List<TblSystemLogBook> list = new ArrayList<>();
        if (ClassSession.checkUserSession()) {  //check user current session
            try {
                session = HibernateUtil.getSessionFactory().getCurrentSession();
                session.beginTransaction();
                list = session.getNamedQuery("findAllTblSystemLogBook").list();
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

    //--------------------------------------------------------------------------
    @Override
    public TblSystemLogBookJob getDataSystemLogBookJob(long id) {
        errMsg = "";
        TblSystemLogBookJob data = null;
        if (ClassSession.checkUserSession()) {  //check user current session
            try {
                session = HibernateUtil.getSessionFactory().getCurrentSession();
                session.beginTransaction();
                data = (TblSystemLogBookJob) session.find(TblSystemLogBookJob.class, id);
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
    public List<TblSystemLogBookJob> getAllDataSystemLogBookJob() {
        errMsg = "";
        List<TblSystemLogBookJob> list = new ArrayList<>();
        if (ClassSession.checkUserSession()) {  //check user current session
            try {
                session = HibernateUtil.getSessionFactory().getCurrentSession();
                session.beginTransaction();
                list = session.getNamedQuery("findAllTblSystemLogBookJob").list();
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
    public List<TblSystemLogBookJob> getAllDataSystemLogBookJobByIDSystemLogBook(long idLogBook) {
        errMsg = "";
        List<TblSystemLogBookJob> list = new ArrayList<>();
        if (ClassSession.checkUserSession()) {  //check user current session
            try {
                session = HibernateUtil.getSessionFactory().getCurrentSession();
                session.beginTransaction();
                list = session.getNamedQuery("findAllTblSystemLogBookJobByIDSystemLogBook")
                        .setParameter("idLogBook", idLogBook)
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
    public List<TblSystemLogBookJob> getAllDataSystemLogBookJobByIDJob(long idJob) {
        errMsg = "";
        List<TblSystemLogBookJob> list = new ArrayList<>();
        if (ClassSession.checkUserSession()) {  //check user current session
            try {
                session = HibernateUtil.getSessionFactory().getCurrentSession();
                session.beginTransaction();
                list = session.getNamedQuery("findAllTblSystemLogBookJobByIDJob")
                        .setParameter("idJob", idJob)
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
    
    //--------------------------------------------------------------------------
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

    //--------------------------------------------------------------------------
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
    public List<TblJob> getAllDataJob() {
        errMsg = "";
        List<TblJob> list = new ArrayList<>();
        if (ClassSession.checkUserSession()) {  //check user current session
            try {
                session = HibernateUtil.getSessionFactory().getCurrentSession();
                session.beginTransaction();
                list = session.getNamedQuery("findAllTblJob").list();
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

    //--------------------------------------------------------------------------
    @Override
    public String getErrorMessage() {
        return errMsg;
    }

}
