/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hotelfx.persistence.service;

import hotelfx.helper.ClassSession;
import hotelfx.persistence.HibernateUtil;
import hotelfx.persistence.model.RefJobRecruitmentShowStatus;
import hotelfx.persistence.model.RefRecordStatus;
import hotelfx.persistence.model.SysCode;
import hotelfx.persistence.model.TblEmployee;
import hotelfx.persistence.model.TblJob;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import org.hibernate.Session;

/**
 *
 * @author Andreas
 */
public class FJobManagerImpl implements FJobManager {

    Session session;
    String errMessage;

    @Override
    public TblJob insertDataJob(TblJob job, SysCode sysCode) {
        errMessage = "";
        TblJob tbljob = job;
        if (ClassSession.checkUserSession()) {  //check user current sessio
            try {
                session = HibernateUtil.getSessionFactory().getCurrentSession();
                session.beginTransaction();
                //check data name has been used
                List<SysCode> list = session.getNamedQuery("findAllSysCodeByDataName")
                        .setParameter("dataName", sysCode.getDataName())
                        .list();
                if (!list.isEmpty()) {
                    if (session.getTransaction().isActive()) {
                        session.getTransaction().rollback();
                    }
                    errMessage = "Duplikasi Nama Data!!";
                    return null;
                }
                //check prefix has been used
                list = session.getNamedQuery("findAllSysCodeByPrefixCode")
                        .setParameter("codePrefix", sysCode.getCodePrefix())
                        .list();
                if (!list.isEmpty()) {
                    if (session.getTransaction().isActive()) {
                        session.getTransaction().rollback();
                    }
                    errMessage = "Duplikasi Prefix (Kode)!!";
                    return null;
                }
                //data code
                sysCode.setCreateDate(Timestamp.valueOf(LocalDateTime.now()));
                sysCode.setTblEmployeeByCreateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                sysCode.setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                sysCode.setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                sysCode.setRefRecordStatus(session.find(RefRecordStatus.class, 1));
                session.saveOrUpdate(sysCode);
                //data job
                tbljob.setRefJobRecruitmentShowStatus(session.find(RefJobRecruitmentShowStatus.class, 1));  //1 = 'show'
                tbljob.setCreateDate(Timestamp.valueOf(LocalDateTime.now()));
                tbljob.setTblEmployeeByCreateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                tbljob.setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                tbljob.setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                tbljob.setRefRecordStatus(session.find(RefRecordStatus.class, 1));
                session.saveOrUpdate(tbljob);
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
        return tbljob;
    }

    @Override
    public boolean updateDataJob(TblJob job, SysCode sysCode) {
        errMessage = "";
        if (ClassSession.checkUserSession()) {  //check user current sessio
            try {
                session = HibernateUtil.getSessionFactory().getCurrentSession();
                session.beginTransaction();
                //check data name has been used
                List<SysCode> list = session.getNamedQuery("findAllSysCodeByDataName")
                        .setParameter("dataName", sysCode.getDataName())
                        .list();
                if (!list.isEmpty()) {
                    if (list.get(0).getIdcode() != sysCode.getIdcode()) {
                        if (session.getTransaction().isActive()) {
                            session.getTransaction().rollback();
                        }
                        errMessage = "Duplikasi Nama Data!!";
                        return false;
                    } else {
                        session.evict(list.get(0));
                    }
                }
                //check prefix has been used
                list = session.getNamedQuery("findAllSysCodeByPrefixCode")
                        .setParameter("codePrefix", sysCode.getCodePrefix())
                        .list();
                if (!list.isEmpty()) {
                    if (list.get(0).getIdcode() != sysCode.getIdcode()) {
                        if (session.getTransaction().isActive()) {
                            session.getTransaction().rollback();
                        }
                        errMessage = "Duplikasi Prefix (Kode)!!";
                        return false;
                    } else {
                        session.evict(list.get(0));
                    }
                }
                //data code
                sysCode.setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                sysCode.setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                session.update(sysCode);
                //data job
                job.setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                job.setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                session.update(job);
                //data employee (code - employee) ***
                System.out.println("edwidyasoldau;adi;");
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
    public boolean deleteDataJob(TblJob job) {
        errMessage = "";
        if (ClassSession.checkUserSession()) {  //check user current sessio
            try {
                session = HibernateUtil.getSessionFactory().getCurrentSession();
                session.beginTransaction();
                if (job.getRefRecordStatus().getIdstatus() == 1) {
                    //data job
                    job.setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                    job.setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                    job.setRefRecordStatus(session.find(RefRecordStatus.class, 2));
                    session.update(job);
                    //data sys code
                    List<SysCode> list = session.getNamedQuery("findAllSysCodeByDataName")
                            .setParameter("dataName", job.getJobName())
                            .list();
                    if (list.isEmpty()) {
                        if (session.getTransaction().isActive()) {
                            session.getTransaction().rollback();
                        }
                        errMessage = "Data tidak dapat dihapus (data 'prefix (kode)' tidak ditemukan)!!";
                        return false;
                    }
                    list.get(0).setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                    list.get(0).setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                    list.get(0).setRefRecordStatus(session.find(RefRecordStatus.class, 2));
                    session.update(list.get(0));
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
    public TblJob getJob(long id) {
        errMessage = "";
        TblJob data = null;
        if (ClassSession.checkUserSession()) {  //check user current sessio
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
    public List<TblJob> getAllDataJob() {
        errMessage = "";
        List<TblJob> list = new ArrayList();
        if (ClassSession.checkUserSession()) {  //check user current sessio
            try {
                session = HibernateUtil.getSessionFactory().getCurrentSession();
                session.beginTransaction();
                list = session.getNamedQuery("findAllTblJob").list();
                session.getTransaction().commit();
            } catch (Exception e) {
                //e.printStackTrace();
                if (session.getTransaction().isActive()) {
                    session.getTransaction().rollback();
                }
                errMessage = e.getMessage();
            }
        }
        return list;
    }

    @Override
    public List<RefJobRecruitmentShowStatus> getAllJobRecruitmentShowStatus() {
        errMessage = "";
        List<RefJobRecruitmentShowStatus> list = new ArrayList();
        if (ClassSession.checkUserSession()) {  //check user current sessio
            try {
                session = HibernateUtil.getSessionFactory().getCurrentSession();
                session.beginTransaction();
                list = session.getNamedQuery("findAllRefJobRecruitmentShowStatus").list();
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

    //--------------------------------------------------------------------------
    @Override
    public SysCode getDataCodeByDataName(String dataName) {
        errMessage = "";
        List<SysCode> list = new ArrayList();
        if (ClassSession.checkUserSession()) {  //check user current sessio
            try {
                session = HibernateUtil.getSessionFactory().getCurrentSession();
                session.beginTransaction();
                list = session.getNamedQuery("findAllSysCodeByDataName")
                        .setParameter("dataName", dataName)
                        .list();
                session.getTransaction().commit();
            } catch (Exception e) {
                //e.printStackTrace();
                if (session.getTransaction().isActive()) {
                    session.getTransaction().rollback();
                }
                errMessage = e.getMessage();
            }
        }
        return list.isEmpty() ? null : list.get(0);
    }

    //--------------------------------------------------------------------------
    @Override
    public String getErrorMessage() {
        return errMessage;
    }
    /*public static void main(String[] args) {
     List<TblJob>jobs = getAllDataJob();
     for(int i = 0; i<jobs.size();i++)
     {
     TblJob job = jobs.get(i);
     System.out.println(job.getJobName());
     }
     }*/
}
