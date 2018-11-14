/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hotelfx.persistence.service;

import hotelfx.helper.ClassCoder;
import hotelfx.helper.ClassSession;
import hotelfx.persistence.HibernateUtil;
import hotelfx.persistence.model.RefBankAccountHolderStatus;
import hotelfx.persistence.model.RefCountry;
import hotelfx.persistence.model.RefEmployeeSalaryType;
import hotelfx.persistence.model.RefEmployeeStatus;
import hotelfx.persistence.model.RefEmployeeType;
import hotelfx.persistence.model.RefPeopleGender;
import hotelfx.persistence.model.RefPeopleIdentifierType;
import hotelfx.persistence.model.RefPeopleReligion;
import hotelfx.persistence.model.RefPeopleStatus;
import hotelfx.persistence.model.RefRecordStatus;
import hotelfx.persistence.model.SysDataHardCode;
import hotelfx.persistence.model.TblBank;
import hotelfx.persistence.model.TblBankAccount;
import hotelfx.persistence.model.TblCalendarEmployeeWarningLetter;
import hotelfx.persistence.model.TblEmployee;
import hotelfx.persistence.model.TblEmployeeBankAccount;
import hotelfx.persistence.model.TblEmployeeWarningLetterType;
import hotelfx.persistence.model.TblGroup;
import hotelfx.persistence.model.TblJob;
import hotelfx.persistence.model.TblPeople;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import org.hibernate.Session;
import org.hibernate.query.Query;

/**
 *
 * @author Andreas
 */
public class FEmployeeManagerImpl implements FEmployeeManager {

    private Session session;

    private String errMessage;

    @Override
    public TblEmployee insertDataEmployee(TblEmployee employee,
            String imgExtention,
            List<TblEmployeeBankAccount> employeeBankAccount) {
        errMessage = "";
        TblEmployee tblEmployee = employee;
        if (ClassSession.checkUserSession()) {  //check user current session
            try {
                session = HibernateUtil.getSessionFactory().getCurrentSession();
                session.beginTransaction();
                //data people
                tblEmployee.getTblPeople().setRefPeopleIdentifierType(session.find(RefPeopleIdentifierType.class, 0));  //KTP = '0'
                tblEmployee.getTblPeople().setCreateDate(Timestamp.valueOf(LocalDateTime.now()));
                tblEmployee.getTblPeople().setTblEmployeeByCreateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                tblEmployee.getTblPeople().setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                tblEmployee.getTblPeople().setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                tblEmployee.getTblPeople().setRefRecordStatus(session.find(RefRecordStatus.class, 1));
                session.saveOrUpdate(tblEmployee.getTblPeople());
                employee.getTblPeople().setImageUrl(employee.getTblPeople().getIdpeople() + "." + imgExtention);
                session.update(tblEmployee.getTblPeople());
                //data employee
                tblEmployee.setCodeEmployee(ClassCoder.getCode(tblEmployee.getTblJob().getJobName(), session));
                tblEmployee.setCreateDate(Timestamp.valueOf(LocalDateTime.now()));
                tblEmployee.setTblEmployeeByCreateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                tblEmployee.setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                tblEmployee.setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                tblEmployee.setRefRecordStatus(session.find(RefRecordStatus.class, 1));
                session.saveOrUpdate(tblEmployee);
            //data employee bank account
                //save or update data bank account n employee bank acccount
                for (TblEmployeeBankAccount eba : employeeBankAccount) {
                    //data bank account
                    eba.getTblBankAccount().setCreateDate(Timestamp.valueOf(LocalDateTime.now()));
                    eba.getTblBankAccount().setTblEmployeeByCreateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                    eba.getTblBankAccount().setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                    eba.getTblBankAccount().setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                    eba.getTblBankAccount().setRefRecordStatus(session.find(RefRecordStatus.class, 1));
                    session.saveOrUpdate(eba.getTblBankAccount());
                    //data employee bank account
                    eba.setRefBankAccountHolderStatus(session.find(RefBankAccountHolderStatus.class, 0));
                    eba.setCreateDate(Timestamp.valueOf(LocalDateTime.now()));
                    eba.setTblEmployeeByCreateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                    eba.setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                    eba.setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                    eba.setRefRecordStatus(session.find(RefRecordStatus.class, 1));
                    session.saveOrUpdate(eba);
                }
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
        return tblEmployee;
    }

    @Override
    public boolean updateDataEmployee(TblEmployee employee,
            String imgExtention,
            List<TblEmployeeBankAccount> employeeBankAccount) {
        errMessage = "";
        if (ClassSession.checkUserSession()) {  //check user current session
            try {
                session = HibernateUtil.getSessionFactory().getCurrentSession();
                session.beginTransaction();
                //check, job has been change?, if changed, update employee code
                TblEmployee attemptEmployee = session.find(TblEmployee.class, employee.getIdemployee());
                attemptEmployee.setTblJob(session.find(TblJob.class, attemptEmployee.getTblJob().getIdjob()));
                if (employee.getTblJob().getIdjob() != attemptEmployee.getTblJob().getIdjob()) {
                    employee.setCodeEmployee(ClassCoder.getCode(employee.getTblJob().getJobName(), session));
                }
                session.evict(attemptEmployee);
                //data employee
                employee.setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                employee.setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                session.update(employee);
                //data people
                employee.getTblPeople().setImageUrl(employee.getTblPeople().getIdpeople() + "." + imgExtention);
                employee.getTblPeople().setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                employee.getTblPeople().setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                session.update(employee.getTblPeople());
            //data employee bank account
                //delete all
                Query rs = session.getNamedQuery("deleteAllTblEmployeeBankAccount")
                        .setParameter("idEmployeeHolder", employee.getIdemployee())
                        .setParameter("idEmployee", ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee());
                errMessage = (String) rs.uniqueResult();
                if (!errMessage.equals("")) {
                    if (session.getTransaction().isActive()) {
                        session.getTransaction().rollback();
                    }
                    return false;
                }
                //save or update data bank account n employee bank acccount
                for (TblEmployeeBankAccount eba : employeeBankAccount) {
                    //data bank account
                    eba.getTblBankAccount().setCreateDate(Timestamp.valueOf(LocalDateTime.now()));
                    eba.getTblBankAccount().setTblEmployeeByCreateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                    eba.getTblBankAccount().setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                    eba.getTblBankAccount().setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                    eba.getTblBankAccount().setRefRecordStatus(session.find(RefRecordStatus.class, 1));
                    session.saveOrUpdate(eba.getTblBankAccount());
                    //data employee bank account
                    eba.setCreateDate(Timestamp.valueOf(LocalDateTime.now()));
                    eba.setTblEmployeeByCreateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                    eba.setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                    eba.setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                    eba.setRefRecordStatus(session.find(RefRecordStatus.class, 1));
                    session.saveOrUpdate(eba);
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
    public boolean deleteDataEmployee(TblEmployee employee) {
        errMessage = "";
        if (ClassSession.checkUserSession()) {  //check user current session
            try {
                session = HibernateUtil.getSessionFactory().getCurrentSession();
                session.beginTransaction();
                if (employee.getRefRecordStatus().getIdstatus() == 1) {
                    //data people
                    employee.getTblPeople().setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                    employee.getTblPeople().setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                    employee.getTblPeople().setRefRecordStatus(session.find(RefRecordStatus.class, 2));
                    session.update(employee.getTblPeople());
                    //data employee
                    employee.setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                    employee.setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                    employee.setRefRecordStatus(session.find(RefRecordStatus.class, 2));
                    session.update(employee);
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
    public TblGroup getDataGroup(long id) {
        errMessage = "";
        TblGroup data = null;
        if (ClassSession.checkUserSession()) {  //check user current session
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
    public RefPeopleGender getDataPeopleGender(int id) {
        errMessage = "";
        RefPeopleGender data = null;
        if (ClassSession.checkUserSession()) {  //check user current session
            try {
                session = HibernateUtil.getSessionFactory().getCurrentSession();
                session.beginTransaction();
                data = (RefPeopleGender) session.find(RefPeopleGender.class, id);
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
    public RefPeopleReligion getDataPeopleReligion(int id) {
        errMessage = "";
        RefPeopleReligion data = null;
        if (ClassSession.checkUserSession()) {  //check user current session
            try {
                session = HibernateUtil.getSessionFactory().getCurrentSession();
                session.beginTransaction();
                data = (RefPeopleReligion) session.find(RefPeopleReligion.class, id);
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
    public RefPeopleStatus getDataPeopleStatus(int id) {
        errMessage = "";
        RefPeopleStatus data = null;
        if (ClassSession.checkUserSession()) {  //check user current session
            try {
                session = HibernateUtil.getSessionFactory().getCurrentSession();
                session.beginTransaction();
                data = (RefPeopleStatus) session.find(RefPeopleStatus.class, id);
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
    public RefEmployeeType getDataEmployeeType(int id) {
        errMessage = "";
        RefEmployeeType data = null;
        if (ClassSession.checkUserSession()) {  //check user current session
            try {
                session = HibernateUtil.getSessionFactory().getCurrentSession();
                session.beginTransaction();
                data = (RefEmployeeType) session.find(RefEmployeeType.class, id);
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
    public RefEmployeeStatus getDataEmployeeStatus(int id) {
        errMessage = "";
        RefEmployeeStatus data = null;
        if (ClassSession.checkUserSession()) {  //check user current session
            try {
                session = HibernateUtil.getSessionFactory().getCurrentSession();
                session.beginTransaction();
                data = (RefEmployeeStatus) session.find(RefEmployeeStatus.class, id);
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
    public RefEmployeeSalaryType getDataEmployeeSalaryType(int id) {
        errMessage = "";
        RefEmployeeSalaryType data = null;
        if (ClassSession.checkUserSession()) {  //check user current session
            try {
                session = HibernateUtil.getSessionFactory().getCurrentSession();
                session.beginTransaction();
                data = (RefEmployeeSalaryType) session.find(RefEmployeeSalaryType.class, id);
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
    public List<TblJob> getAllDataJob() {
        errMessage = "";
        List<TblJob> list = new ArrayList();
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
                errMessage = e.getMessage();
            }
        }
        return list;
    }

    @Override
    public List<TblGroup> getAllDataGroup() {
        errMessage = "";
        List<TblGroup> list = new ArrayList();
        if (ClassSession.checkUserSession()) {  //check user current session
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
    public List<RefPeopleGender> getAllDataPeopleGender() {
        errMessage = "";
        List<RefPeopleGender> list = new ArrayList();
        if (ClassSession.checkUserSession()) {  //check user current session
            try {
                session = HibernateUtil.getSessionFactory().getCurrentSession();
                session.beginTransaction();
                list = session.getNamedQuery("findAllRefPeopleGender").list();
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
    public List<RefPeopleStatus> getAllDataPeopleStatus() {
        errMessage = "";
        List<RefPeopleStatus> list = new ArrayList();
        if (ClassSession.checkUserSession()) {  //check user current session
            try {
                session = HibernateUtil.getSessionFactory().getCurrentSession();
                session.beginTransaction();
                list = session.getNamedQuery("findAllRefPeopleStatus").list();
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
    public List<RefPeopleReligion> getAllDataPeopleReligion() {
        errMessage = "";
        List<RefPeopleReligion> list = new ArrayList();
        if (ClassSession.checkUserSession()) {  //check user current session
            try {
                session = HibernateUtil.getSessionFactory().getCurrentSession();
                session.beginTransaction();
                list = session.getNamedQuery("findAllRefPeopleReligion").list();
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
    public List<RefEmployeeType> getAllDataEmployeeType() {
        errMessage = "";
        List<RefEmployeeType> list = new ArrayList();
        if (ClassSession.checkUserSession()) {  //check user current session
            try {
                session = HibernateUtil.getSessionFactory().getCurrentSession();
                session.beginTransaction();
                list = session.getNamedQuery("findAllRefEmployeeType").list();
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
    public List<RefEmployeeStatus> getAllDataEmployeeStatus() {
        errMessage = "";
        List<RefEmployeeStatus> list = new ArrayList();
        if (ClassSession.checkUserSession()) {  //check user current session
            try {
                session = HibernateUtil.getSessionFactory().getCurrentSession();
                session.beginTransaction();
                list = session.getNamedQuery("findAllRefEmployeeStatus").list();
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
    public List<RefEmployeeSalaryType> getAllDataSalaryType() {
        errMessage = "";
        List<RefEmployeeSalaryType> list = new ArrayList();
        if (ClassSession.checkUserSession()) {  //check user current session
            try {
                session = HibernateUtil.getSessionFactory().getCurrentSession();
                session.beginTransaction();
                list = session.getNamedQuery("findAllRefEmployeeSalaryType").list();
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

            }
        }
        return list;
    }

    //--------------------------------------------------------------------------
    @Override
    public RefCountry getDataPeopleCountry(int id) {
        errMessage = "";
        RefCountry data = null;
        if (ClassSession.checkUserSession()) {  //check user current session
            try {
                session = HibernateUtil.getSessionFactory().getCurrentSession();
                session.beginTransaction();
                data = (RefCountry) session.find(RefCountry.class, id);
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
    public List<RefCountry> getAllDataPeopleCountry() {
        errMessage = "";
        List<RefCountry> list = new ArrayList();
        if (ClassSession.checkUserSession()) {  //check user current session
            try {
                session = HibernateUtil.getSessionFactory().getCurrentSession();
                session.beginTransaction();
                list = session.getNamedQuery("findAllRefCountry").list();
                session.getTransaction().commit();
            } catch (Exception e) {
                if (session.getTransaction().isActive()) {
                    session.getTransaction().rollback();
                }

            }
        }
        return list;
    }

    //--------------------------------------------------------------------------
    @Override
    public TblEmployeeBankAccount getDataEmployeeBankAccount(long idEmployeeBankAccount) {
        errMessage = "";
        TblEmployeeBankAccount data = null;
        if (ClassSession.checkUserSession()) {  //check user current session
            try {
                session = HibernateUtil.getSessionFactory().getCurrentSession();
                session.beginTransaction();
                data = (TblEmployeeBankAccount) session.find(TblEmployeeBankAccount.class, idEmployeeBankAccount);
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
    public List<TblEmployeeBankAccount> getAllDataEmployeeBankAccount(long idEmployee) {
        errMessage = "";
        List<TblEmployeeBankAccount> list = new ArrayList<>();
        if (ClassSession.checkUserSession()) {  //check user current session
            try {
                session = HibernateUtil.getSessionFactory().getCurrentSession();
                session.beginTransaction();
                list = session.getNamedQuery("findAllTblEmployeeBankAccountByIDEmployee")
                        .setParameter("idEmployee", idEmployee)
                        .list();
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

    //--------------------------------------------------------------------------
    @Override
    public TblBankAccount getDataBankAccount(long idBankAccount) {
        errMessage = "";
        TblBankAccount data = null;
        if (ClassSession.checkUserSession()) {  //check user current session
            try {
                session = HibernateUtil.getSessionFactory().getCurrentSession();
                session.beginTransaction();
                data = (TblBankAccount) session.find(TblBankAccount.class, idBankAccount);
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
    public List<TblBankAccount> getAllDataBankAccount() {
        errMessage = "";
        List<TblBankAccount> list = new ArrayList<>();
        if (ClassSession.checkUserSession()) {  //check user current session
            try {
                session = HibernateUtil.getSessionFactory().getCurrentSession();
                session.beginTransaction();
                list = session.getNamedQuery("findAllTblBankAccount").list();
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

    //--------------------------------------------------------------------------
    @Override
    public RefBankAccountHolderStatus getDataBankAccountHolderStatus(int id) {
        errMessage = "";
        RefBankAccountHolderStatus data = null;
        if (ClassSession.checkUserSession()) {  //check user current session
            try {
                session = HibernateUtil.getSessionFactory().getCurrentSession();
                session.beginTransaction();
                data = (RefBankAccountHolderStatus) session.find(RefBankAccountHolderStatus.class, id);
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
    public List<RefBankAccountHolderStatus> getAllDataBankAccountHolderStatus() {
        errMessage = "";
        List<RefBankAccountHolderStatus> list = new ArrayList<>();
        if (ClassSession.checkUserSession()) {  //check user current session
            try {
                session = HibernateUtil.getSessionFactory().getCurrentSession();
                session.beginTransaction();
                list = session.getNamedQuery("findAllRefBankAccountHolderStatus").list();
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

    //--------------------------------------------------------------------------
    @Override
    public TblBank getDataBank(long id) {
        errMessage = "";
        TblBank data = null;
        if (ClassSession.checkUserSession()) {  //check user current session
            try {
                session = HibernateUtil.getSessionFactory().getCurrentSession();
                session.beginTransaction();
                data = (TblBank) session.find(TblBank.class, id);
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
    public List<TblBank> getAllDataBank() {
        errMessage = "";
        List<TblBank> list = new ArrayList<>();
        if (ClassSession.checkUserSession()) {  //check user current session
            try {
                session = HibernateUtil.getSessionFactory().getCurrentSession();
                session.beginTransaction();
                list = session.getNamedQuery("findAllTblBank").list();
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
    
    //--------------------------------------------------------------------------
    
    public boolean insertDataEmployeeWarningLetter(TblCalendarEmployeeWarningLetter employeeWarningLetter){
       errMessage = "";
       TblCalendarEmployeeWarningLetter tblCalendarEmployeeWarningLetter = employeeWarningLetter;
       if(ClassSession.checkUserSession()){
           try{
              session = HibernateUtil.getSessionFactory().getCurrentSession();
              session.beginTransaction();
              tblCalendarEmployeeWarningLetter.setCreatedDate(Timestamp.valueOf(LocalDateTime.now()));
              tblCalendarEmployeeWarningLetter.setTblEmployeeByCreatedBy(session.find(TblEmployee.class,ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
              tblCalendarEmployeeWarningLetter.setTblEmployeeByLastUpdatedBy(session.find(TblEmployee.class,ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
              tblCalendarEmployeeWarningLetter.setRefRecordStatus(session.find(RefRecordStatus.class,1));
              session.saveOrUpdate(tblCalendarEmployeeWarningLetter);
              tblCalendarEmployeeWarningLetter.setTblEmployeeByIdemployee(employeeWarningLetter.getTblEmployeeByIdemployee());
              tblCalendarEmployeeWarningLetter.getTblEmployeeByIdemployee().setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class,ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
              tblCalendarEmployeeWarningLetter.getTblEmployeeByIdemployee().setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
              tblCalendarEmployeeWarningLetter.getTblEmployeeByIdemployee().setRefRecordStatus(session.find(RefRecordStatus.class,1));
              session.update(tblCalendarEmployeeWarningLetter.getTblEmployeeByIdemployee());
              session.getTransaction().commit();
            }
            catch(Exception e){
               if(session.getTransaction().isActive()){
                  session.getTransaction().rollback();
                }
               errMessage = e.getMessage();
               return false;
            }
        }
       else{
         return false;
       }
      return true;
    }
    
    public boolean updateDataEmployeeWarningLetter(TblCalendarEmployeeWarningLetter employeeWarningLetter){
       errMessage = "";
       
       if(ClassSession.checkUserSession()){
           try{
             session = HibernateUtil.getSessionFactory().getCurrentSession();
             session.beginTransaction();
             employeeWarningLetter.setLastUpdatedDate(Timestamp.valueOf(LocalDateTime.now()));
             employeeWarningLetter.setTblEmployeeByLastUpdatedBy(session.find(TblEmployee.class,ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
             employeeWarningLetter.setRefRecordStatus(session.find(RefRecordStatus.class,1));
             session.update(employeeWarningLetter);
             employeeWarningLetter.getTblEmployeeByIdemployee().setWarningLetterStatus(employeeWarningLetter.getTblEmployeeByIdemployee().getWarningLetterStatus());
             employeeWarningLetter.getTblEmployeeByIdemployee().setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class,ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
             employeeWarningLetter.getTblEmployeeByIdemployee().setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
             employeeWarningLetter.getTblEmployeeByIdemployee().setRefRecordStatus(session.find(RefRecordStatus.class,1));
             session.update(employeeWarningLetter.getTblEmployeeByIdemployee());
             session.getTransaction().commit();
           }
          catch(Exception e){
               if(session.getTransaction().isActive()){
                 session.getTransaction().rollback();
               }
              errMessage = e.getMessage();
             return false;
           }
       }
       else{
         return false; 
       }
       return true;
    }
    
    public boolean deleteDataEmployeeWarningLetter(TblCalendarEmployeeWarningLetter employeeWarningLetter){
       errMessage = "";
       
       if(ClassSession.checkUserSession()){
          try{
             session = HibernateUtil.getSessionFactory().getCurrentSession();
             session.beginTransaction();
             employeeWarningLetter.setTblEmployeeByLastUpdatedBy(session.find(TblEmployee.class,ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
             employeeWarningLetter.setLastUpdatedDate(Timestamp.valueOf(LocalDateTime.now()));
             employeeWarningLetter.setRefRecordStatus(session.find(RefRecordStatus.class,2));
             session.update(employeeWarningLetter);
             session.getTransaction().commit();
           }
          catch(Exception e){
               if(session.getTransaction().isActive()){
                 session.getTransaction().rollback();
               }
             errMessage = e.getMessage();
             return false;
           }
        }
       else{
         return false;
       }
      return true;
    }
    
    public List<TblCalendarEmployeeWarningLetter>getAllDataEmployeeWarningLetter(long id){
      errMessage = "";
      List<TblCalendarEmployeeWarningLetter>list = new ArrayList();
      if(ClassSession.checkUserSession()){
           try{
             session = HibernateUtil.getSessionFactory().getCurrentSession();
             session.beginTransaction();
             list = session.getNamedQuery("findAllTblCalendarEmployeeWarningLetterByIDEmployee").setParameter("idEmployee",id).list();
             session.getTransaction().commit();
            }
           catch(Exception e){
               if(session.getTransaction().isActive()){
                 session.getTransaction().rollback();
               }
            } 
         }
        return list;
    }
    
    public List<TblEmployeeWarningLetterType>getAllDataWarningLetter(){
       errMessage = "";
       List<TblEmployeeWarningLetterType>list = new ArrayList();
       if(ClassSession.checkUserSession()){
           try{
             session = HibernateUtil.getSessionFactory().getCurrentSession();
             session.beginTransaction();
             list = session.getNamedQuery("findAllTblEmployeeWarningLetterType").list();
             session.getTransaction().commit();
            }
           catch(Exception e){
               if(session.getTransaction().isActive()){
                 session.getTransaction().rollback();
               }
              errMessage = e.getMessage();
           }
       }
       return list;
    }
    
    public TblCalendarEmployeeWarningLetter getDataEmployeeWarningLetter(long id){
       errMessage = "";
       TblCalendarEmployeeWarningLetter data = null;
       
       if(ClassSession.checkUserSession()){
          try{
             session = HibernateUtil.getSessionFactory().getCurrentSession();
             session.beginTransaction();
             data = session.find(TblCalendarEmployeeWarningLetter.class,id);
             session.getTransaction().commit();
           }
          catch(Exception e){
               if(session.getTransaction().isActive()){
                 session.getTransaction().rollback();
               }
              errMessage = e.getMessage();
           }
       }
       return data;
    }
    
    public boolean updateDataEmployeeWarningLetterStatus(TblEmployee tblEmployee){
       errMessage = "";
       if(ClassSession.checkUserSession()){
          try{
            session = HibernateUtil.getSessionFactory().getCurrentSession();
            session.beginTransaction();
            tblEmployee.setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
            tblEmployee.setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class,ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
            tblEmployee.setRefRecordStatus(session.find(RefRecordStatus.class,1));
            session.update(tblEmployee);
            session.getTransaction().commit();
          }
          catch(Exception e){
            if(session.getTransaction().isActive()){
              session.getTransaction().rollback();
            }
            errMessage = e.getMessage();
            return false;
          }
       }
       else{
         return false;  
       }
      return true;
    }
    //--------------------------------------------------------------------------
    
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
    
    //--------------------------------------------------------------------------
    @Override
    public String getErrorMessage() {
        return errMessage;
    }
    /*public static void main(String[] args) {
     List<TblEmployee>employee = getAllDataEmployee();
     System.out.println(employee.size());
     for(int i = 0; i<employee.size();i++){
     TblEmployee em = employee.get(i);
     System.out.println(em.getTblPeople().getFullName());
     }
     }*/

}
