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
import hotelfx.persistence.model.RefCustomerStatus;
import hotelfx.persistence.model.RefCustomerType;
import hotelfx.persistence.model.RefPeopleGender;
import hotelfx.persistence.model.RefPeopleIdentifierType;
import hotelfx.persistence.model.RefPeopleReligion;
import hotelfx.persistence.model.RefPeopleStatus;
import hotelfx.persistence.model.RefRecordStatus;
import hotelfx.persistence.model.SysDataHardCode;
import hotelfx.persistence.model.TblBank;
import hotelfx.persistence.model.TblBankAccount;
import hotelfx.persistence.model.TblCustomer;
import hotelfx.persistence.model.TblCustomerBankAccount;
import hotelfx.persistence.model.TblEmployee;
import hotelfx.persistence.model.TblPeople;
import java.sql.Date;
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
public class FCustomerManagerImpl implements FCustomerManager {

    private Session session;

    private String errMsg;

    public FCustomerManagerImpl() {

    }

    @Override
    public TblCustomer insertDataCustomer(TblCustomer customer,
            String imgExtention,
            List<TblCustomerBankAccount> customerBankAccount) {
        errMsg = "";
        TblCustomer tblCustomer = customer;
        if (ClassSession.checkUserSession()) {  //check user current session
            try {
                session = HibernateUtil.getSessionFactory().getCurrentSession();
                session.beginTransaction();
                //data people
                customer.getTblPeople().setCreateDate(Timestamp.valueOf(LocalDateTime.now()));
                customer.getTblPeople().setTblEmployeeByCreateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                customer.getTblPeople().setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                customer.getTblPeople().setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                customer.getTblPeople().setRefRecordStatus(session.find(RefRecordStatus.class, 1));
                session.saveOrUpdate(tblCustomer.getTblPeople());
                tblCustomer.getTblPeople().setImageUrl(tblCustomer.getTblPeople().getIdpeople() + "." + imgExtention);
                session.update(tblCustomer.getTblPeople());
                //data customer
                tblCustomer.setCodeCustomer(ClassCoder.getCode("Customer", session));
                tblCustomer.setCreateDate(Timestamp.valueOf(LocalDateTime.now()));
                tblCustomer.setTblEmployeeByCreateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                tblCustomer.setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                tblCustomer.setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                tblCustomer.setRefRecordStatus(session.find(RefRecordStatus.class, 1));
                session.saveOrUpdate(tblCustomer);
            //data customer bank account
                //save or update data bank account n customer bank acccount
                for (TblCustomerBankAccount cba : customerBankAccount) {
                    //data bank account
//                if (cba.getTblBankAccount().getId() == null) {
//                    //get data bank account by id bank (generate id)
//                    List<TblBankAccount> bankAccounts = session.getNamedQuery("findAllTblBankAccountByIDBank")
//                            .setParameter("idBank", cba.getTblBankAccount().getTblBank().getIdbank())
//                            .list();
//                    long lastIdAccount = 0;
//                    if (!bankAccounts.isEmpty()) {
//                        lastIdAccount = bankAccounts.get(bankAccounts.size() - 1).getId().getIdaccount();
//                    }
//                    cba.getTblBankAccount().setId(new TblBankAccountId(cba.getTblBankAccount().getTblBank().getIdbank(), lastIdAccount + 1));
//                }
                    cba.getTblBankAccount().setCreateDate(Timestamp.valueOf(LocalDateTime.now()));
                    cba.getTblBankAccount().setTblEmployeeByCreateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                    cba.getTblBankAccount().setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                    cba.getTblBankAccount().setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                    cba.getTblBankAccount().setRefRecordStatus(session.find(RefRecordStatus.class, 1));
                    session.saveOrUpdate(cba.getTblBankAccount());
                    //data customer bank account
//                if (cba.getId() == null) {
//                    cba.setId(new TblCustomerBankAccountId(cba.getTblCustomer().getIdcustomer(),
//                            cba.getTblBankAccount().getId().getIdbank(),
//                            cba.getTblBankAccount().getId().getIdaccount()));
//                }
                    cba.setRefBankAccountHolderStatus(session.find(RefBankAccountHolderStatus.class, 0));
                    cba.setCreateDate(Timestamp.valueOf(LocalDateTime.now()));
                    cba.setTblEmployeeByCreateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                    cba.setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                    cba.setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                    cba.setRefRecordStatus(session.find(RefRecordStatus.class, 1));
                    session.saveOrUpdate(cba);
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
        return tblCustomer;
    }

    @Override
    public boolean updateDataCustomer(TblCustomer customer,
            String imgExtention,
            List<TblCustomerBankAccount> customerBankAccount) {
        errMsg = "";
        if (ClassSession.checkUserSession()) {  //check user current session
            try {
                session = HibernateUtil.getSessionFactory().getCurrentSession();
                session.beginTransaction();
                //data people
                customer.getTblPeople().setImageUrl(customer.getTblPeople().getIdpeople() + "." + imgExtention);
                customer.getTblPeople().setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                customer.getTblPeople().setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                session.update(customer.getTblPeople());
                //data customer
                customer.setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                customer.setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                session.update(customer);
            //data customer bank account
                //delete all
                Query rs = session.getNamedQuery("deleteAllTblCustomerBankAccount")
                        .setParameter("idCustomer", customer.getIdcustomer())
                        .setParameter("idEmployee", ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee());
                errMsg = (String) rs.uniqueResult();
                if (!errMsg.equals("")) {
                    if (session.getTransaction().isActive()) {
                        session.getTransaction().rollback();
                    }
                    return false;
                }
                //save or update data bank account n customer bank acccount
                for (TblCustomerBankAccount cba : customerBankAccount) {
                    //data bank account
//                if (cba.getTblBankAccount().getId() == null) {
//                    //get data bank account by id bank (generate id)
//                    List<TblBankAccount> bankAccounts = session.getNamedQuery("findAllTblBankAccountByIDBank")
//                            .setParameter("idBank", cba.getTblBankAccount().getTblBank().getIdbank())
//                            .list();
//                    long lastIdAccount = 0;
//                    if (!bankAccounts.isEmpty()) {
//                        lastIdAccount = bankAccounts.get(bankAccounts.size() - 1).getId().getIdaccount();
//                    }
//                    cba.getTblBankAccount().setId(new TblBankAccountId(cba.getTblBankAccount().getTblBank().getIdbank(), lastIdAccount + 1));
//                }
                    cba.getTblBankAccount().setCreateDate(Timestamp.valueOf(LocalDateTime.now()));
                    cba.getTblBankAccount().setTblEmployeeByCreateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                    cba.getTblBankAccount().setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                    cba.getTblBankAccount().setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                    cba.getTblBankAccount().setRefRecordStatus(session.find(RefRecordStatus.class, 1));
                    session.saveOrUpdate(cba.getTblBankAccount());
                    //data customer bank account
//                if (cba.getId() == null) {
//                    cba.setId(new TblCustomerBankAccountId(cba.getTblCustomer().getIdcustomer(),
//                            cba.getTblBankAccount().getId().getIdbank(),
//                            cba.getTblBankAccount().getId().getIdaccount()));
//                }
                    cba.setCreateDate(Timestamp.valueOf(LocalDateTime.now()));
                    cba.setTblEmployeeByCreateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                    cba.setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                    cba.setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                    cba.setRefRecordStatus(session.find(RefRecordStatus.class, 1));
                    session.saveOrUpdate(cba);
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
    public boolean deleteDataCustomer(TblCustomer customer) {
        errMsg = "";
        if (ClassSession.checkUserSession()) {  //check user current session
            try {
                session = HibernateUtil.getSessionFactory().getCurrentSession();
                session.beginTransaction();
                if (customer.getRefRecordStatus().getIdstatus() == 1) {
                    //data people
                    customer.getTblPeople().setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                    customer.getTblPeople().setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                    customer.getTblPeople().setRefRecordStatus(session.find(RefRecordStatus.class, 2));
                    session.update(customer.getTblPeople());
                    //data customer
                    customer.setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                    customer.setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                    customer.setRefRecordStatus(session.find(RefRecordStatus.class, 2));
                    session.update(customer);
                    //data customer - bank account
                    Query rs = session.getNamedQuery("deleteAllTblCustomerBankAccount")
                            .setParameter("idCustomer", customer.getIdcustomer())
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
    public TblCustomer getCustomer(long id) {
        errMsg = "";
        TblCustomer data = null;
        if (ClassSession.checkUserSession()) {  //check user current session
            try {
                session = HibernateUtil.getSessionFactory().getCurrentSession();
                session.beginTransaction();
                data = (TblCustomer) session.find(TblCustomer.class, id);
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
    public List<TblCustomer> getAllDataCustomer() {
        errMsg = "";
        List<TblCustomer> list = new ArrayList<>();
        if (ClassSession.checkUserSession()) {  //check user current session
            try {
                session = HibernateUtil.getSessionFactory().getCurrentSession();
                session.beginTransaction();
                list = session.getNamedQuery("findAllTblCustomer").list();
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
    public TblPeople getPeople(long id) {
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
    public List<TblPeople> getAllDataPeople() {
        errMsg = "";
        List<TblPeople> list = new ArrayList<>();
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
                errMsg = e.getMessage();
            } finally {
                //session.close();
            }
        }
        return list;
    }

    //--------------------------------------------------------------------------
    @Override
    public RefCustomerType getCustomerType(int id) {
        errMsg = "";
        RefCustomerType data = null;
        if (ClassSession.checkUserSession()) {  //check user current session
            try {
                session = HibernateUtil.getSessionFactory().getCurrentSession();
                session.beginTransaction();
                data = (RefCustomerType) session.find(RefCustomerType.class, id);
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
    public List<RefCustomerType> getAllDataCustomerType() {
        errMsg = "";
        List<RefCustomerType> list = new ArrayList<>();
        if (ClassSession.checkUserSession()) {  //check user current session
            try {
                session = HibernateUtil.getSessionFactory().getCurrentSession();
                session.beginTransaction();
                list = session.getNamedQuery("findAllRefCustomerType").list();
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
    public RefCustomerStatus getCustomerStatus(int id) {
        errMsg = "";
        RefCustomerStatus data = null;
        if (ClassSession.checkUserSession()) {  //check user current session
            try {
                session = HibernateUtil.getSessionFactory().getCurrentSession();
                session.beginTransaction();
                data = (RefCustomerStatus) session.find(RefCustomerStatus.class, id);
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
    public List<RefCustomerStatus> getAllDataCustomerStatus() {
        errMsg = "";
        List<RefCustomerStatus> list = new ArrayList<>();
        if (ClassSession.checkUserSession()) {  //check user current session
            try {
                session = HibernateUtil.getSessionFactory().getCurrentSession();
                session.beginTransaction();
                list = session.getNamedQuery("findAllRefCustomerStatus").list();
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
    public RefPeopleIdentifierType getPeopleIdentifierType(int id) {
        errMsg = "";
        RefPeopleIdentifierType data = null;
        if (ClassSession.checkUserSession()) {  //check user current session
            try {
                session = HibernateUtil.getSessionFactory().getCurrentSession();
                session.beginTransaction();
                data = (RefPeopleIdentifierType) session.find(RefPeopleIdentifierType.class, id);
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
    public List<RefPeopleIdentifierType> getAllDataPeopleIdentifierType() {
        errMsg = "";
        List<RefPeopleIdentifierType> list = new ArrayList<>();
        if (ClassSession.checkUserSession()) {  //check user current session
            try {
                session = HibernateUtil.getSessionFactory().getCurrentSession();
                session.beginTransaction();
                list = session.getNamedQuery("findAllRefPeopleIdentifierType").list();
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
    public RefPeopleGender getPeopleGender(int id) {
        errMsg = "";
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
                errMsg = e.getMessage();
            } finally {
                //session.close();
            }
        }
        return data;
    }

    @Override
    public List<RefPeopleGender> getAllDataPeopleGender() {
        errMsg = "";
        List<RefPeopleGender> list = new ArrayList<>();
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
                errMsg = e.getMessage();
            } finally {
                //session.close();
            }
        }
        return list;
    }

    //--------------------------------------------------------------------------
    @Override
    public RefPeopleReligion getPeopleReligion(int id) {
        errMsg = "";
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
                errMsg = e.getMessage();
            } finally {
                //session.close();
            }
        }
        return data;
    }

    @Override
    public List<RefPeopleReligion> getAllDataPeopleReligion() {
        errMsg = "";
        List<RefPeopleReligion> list = new ArrayList<>();
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
                errMsg = e.getMessage();
            } finally {
                //session.close();
            }
        }
        return list;
    }

    //--------------------------------------------------------------------------
    @Override
    public RefPeopleStatus getPeopleStatus(int id) {
        errMsg = "";
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
                errMsg = e.getMessage();
            } finally {
                //session.close();
            }
        }
        return data;
    }

    @Override
    public List<RefPeopleStatus> getAllDataPeopleStatus() {
        errMsg = "";
        List<RefPeopleStatus> list = new ArrayList<>();
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
                errMsg = e.getMessage();
            } finally {
                //session.close();
            }
        }
        return list;
    }

    //--------------------------------------------------------------------------
    @Override
    public RefCountry getDataPeopleCountry(int id) {
        errMsg = "";
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
                errMsg = e.getMessage();
            }
        }
        return data;
    }

    @Override
    public List<RefCountry> getAllDataPeopleCountry() {
        errMsg = "";
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
    public TblCustomerBankAccount getCustomerBankAccount(long idCustomerBankAccount) {
        errMsg = "";
        TblCustomerBankAccount data = null;
        if (ClassSession.checkUserSession()) {  //check user current session
            try {
                session = HibernateUtil.getSessionFactory().getCurrentSession();
                session.beginTransaction();
                data = (TblCustomerBankAccount) session.find(TblCustomerBankAccount.class, idCustomerBankAccount);
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
    public List<TblCustomerBankAccount> getAllDataCustomerBankAccount(long idCustomer) {
        errMsg = "";
        List<TblCustomerBankAccount> list = new ArrayList<>();
        if (ClassSession.checkUserSession()) {  //check user current session
            try {
                session = HibernateUtil.getSessionFactory().getCurrentSession();
                session.beginTransaction();
                list = session.getNamedQuery("findAllTblCustomerBankAccountByIDCustomer")
                        .setParameter("idCustomer", idCustomer)
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
    public TblBankAccount getBankAccount(long idBankAccount) {
        errMsg = "";
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
                errMsg = e.getMessage();
            } finally {
                //session.close();
            }
        }
        return data;
    }

    @Override
    public List<TblBankAccount> getAllDataBankAccount() {
        errMsg = "";
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
                errMsg = e.getMessage();
            } finally {
                //session.close();
            }
        }
        return list;
    }

    //--------------------------------------------------------------------------
    @Override
    public RefBankAccountHolderStatus getBankAccountHolderStatus(int id) {
        errMsg = "";
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
                errMsg = e.getMessage();
            } finally {
                //session.close();
            }
        }
        return data;
    }

    @Override
    public List<RefBankAccountHolderStatus> getAllDataBankAccountHolderStatus() {
        errMsg = "";
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
                errMsg = e.getMessage();
            } finally {
                //session.close();
            }
        }
        return list;
    }

    //--------------------------------------------------------------------------
    @Override
    public TblBank getDataBank(long id) {
        errMsg = "";
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
                errMsg = e.getMessage();
            } finally {
                //session.close();
            }
        }
        return data;
    }

    @Override
    public List<TblBank> getAllDataBank() {
        errMsg = "";
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
                errMsg = e.getMessage();
            } finally {
                //session.close();
            }
        }
        return list;
    }

    //--------------------------------------------------------------------------
   
    public List<TblCustomer>getAllDataCustomerForPrint(Date startDate,Date endDate,RefCustomerType customerType,TblCustomer customerName,RefCountry country){
       errMsg = "";
       
       List<TblCustomer>list = new ArrayList();
       
       if(ClassSession.checkUserSession()){
           try{
             session = HibernateUtil.getSessionFactory().getCurrentSession();
             session.beginTransaction();
             
               if(customerType!=null){
                   if(customerType.getIdtype()!=3){
                     list = session.getNamedQuery("findAllTblCustomerForPrintPeriodeAndCustomerType").
                            setParameter("typeCustomer",customerType.getIdtype()).
                            setParameter("startDate",startDate).setParameter("endDate", endDate)
                             .list();
                    }
                   else{
                      list = session.getNamedQuery("findAllTblCustomerForPrintPeriode").
                             setParameter("startDate",startDate).setParameter("endDate",endDate)
                             .list();
                   }
               }
               
               if(country!=null){
                   if(country.getIdcountry()!=0){
                     list = session.getNamedQuery("findAllTblCustomerForPrintPeriodeAndCountry").
                            setParameter("country",country.getIdcountry()).
                            setParameter("startDate",startDate).setParameter("endDate",endDate)
                            .list();
                    }
                   else{
                     list = session.getNamedQuery("findAllTblCustomerForPrintPeriode").
                     setParameter("startDate",startDate).
                     setParameter("endDate",endDate).list();
                   }
               }
               
                if(customerName!=null){
                    System.out.println("customer :"+customerName.getCodeCustomer());
                   if(customerName.getIdcustomer()!=0){
                     list = session.getNamedQuery("findAllTblCustomerForPrintPeriodeAndCustomerName").
                            setParameter("customer",customerName.getIdcustomer()).
                            setParameter("startDate",startDate).setParameter("endDate",endDate)
                            .list();
                    }
                   else{
                     list = session.getNamedQuery("findAllTblCustomerForPrintPeriode").
                     setParameter("startDate",startDate).
                     setParameter("endDate",endDate).list();
                   }
               }
               
               if(customerType==null && customerName==null && country==null){
                 list = session.getNamedQuery("findAllTblCustomerForPrintPeriode").
                    setParameter("startDate",startDate).
                    setParameter("endDate",endDate).list();
               }
           
             session.getTransaction().commit();
           }
           catch(Exception e){
               if(session.getTransaction().isActive()){
                 session.getTransaction().rollback();
               }
              errMsg = e.getMessage();
           }
       }
      return list;
    }
    
    //--------------------------------------------------------------------------
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
    
 //-----------------------------------------------------------------------------
    @Override
    public String getErrorMessage() {
        return errMsg;
    }

}
