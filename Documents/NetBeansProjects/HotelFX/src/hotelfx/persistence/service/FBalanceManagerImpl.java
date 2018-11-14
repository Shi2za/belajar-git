/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hotelfx.persistence.service;

import hotelfx.helper.ClassFormatter;
import hotelfx.helper.ClassSession;
import hotelfx.persistence.HibernateUtil;
import hotelfx.persistence.model.LogCompanyBalanceCashFlow;
import hotelfx.persistence.model.RefRecordStatus;
import hotelfx.persistence.model.TblBank;
import hotelfx.persistence.model.TblBankAccount;
import hotelfx.persistence.model.TblCompanyBalance;
import hotelfx.persistence.model.TblCompanyBalanceBankAccount;
import hotelfx.persistence.model.TblCompanyBalanceTransferReceived;
import hotelfx.persistence.model.TblEmployee;
import hotelfx.persistence.model.TblOpenCloseCashierBalance;
import hotelfx.persistence.model.TblOpenCloseCashierBalanceDetail;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.sql.Date;
import java.util.List;
import org.hibernate.Session;
import org.hibernate.query.Query;

/**
 *
 * @author ANDRI
 */
public class FBalanceManagerImpl implements FBalanceManager {

    private Session session;

    private String errMsg;

    public FBalanceManagerImpl() {

    }

    @Override
    public boolean updateDataCompanyBalance(TblCompanyBalance companyBalance) {
        errMsg = "";
        if (ClassSession.checkUserSession()) {  //check user current session
            try {
                session = HibernateUtil.getSessionFactory().getCurrentSession();
                session.beginTransaction();
                companyBalance.setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                companyBalance.setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                session.update(companyBalance);
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
    public boolean updateDataBalanceTransfer(TblCompanyBalance selectedBalance,
            TblCompanyBalance anotherBalance,
            TblCompanyBalanceBankAccount selectedBalanceBankAccount,
            TblCompanyBalanceBankAccount anotherBalanceBankAccount,
            BigDecimal transferNominal) {
        errMsg = "";
        if (ClassSession.checkUserSession()) {  //check user current session
            try {
                session = HibernateUtil.getSessionFactory().getCurrentSession();
                session.beginTransaction();
                selectedBalance.setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                selectedBalance.setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                session.update(selectedBalance);
                //selected balance - bank account
                if (selectedBalanceBankAccount != null) {
                    selectedBalanceBankAccount.setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                    selectedBalanceBankAccount.setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                    session.update(selectedBalanceBankAccount);
                }
                //another balance
                if (anotherBalance != null) {
                    anotherBalance.setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                    anotherBalance.setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                    session.update(anotherBalance);
                }
                //another balance - bank account
                if (anotherBalanceBankAccount != null) {
                    anotherBalanceBankAccount.setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                    anotherBalanceBankAccount.setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                    session.update(anotherBalanceBankAccount);
                }
                //history company balance - cash flow
                LogCompanyBalanceCashFlow logCompanyBalanceCashFlow = new LogCompanyBalanceCashFlow();
                logCompanyBalanceCashFlow.setTblCompanyBalanceByIdsenderCompanyBalance(selectedBalance);
                if (anotherBalance != null) {
                    logCompanyBalanceCashFlow.setTblCompanyBalanceByIdreceiverCompanyBalance(anotherBalance);
                } else {
                    logCompanyBalanceCashFlow.setTblCompanyBalanceByIdreceiverCompanyBalance(selectedBalance);
                }
                if (selectedBalanceBankAccount != null) {
                    logCompanyBalanceCashFlow.setTblBankAccountByIdsenderCbbankAccount(selectedBalanceBankAccount.getTblBankAccount());
                } else {
                    logCompanyBalanceCashFlow.setTblBankAccountByIdsenderCbbankAccount(null);
                }
                if (anotherBalanceBankAccount != null) {
                    logCompanyBalanceCashFlow.setTblBankAccountByIdreceiverCbbankAccount(anotherBalanceBankAccount.getTblBankAccount());
                } else {
                    logCompanyBalanceCashFlow.setTblBankAccountByIdreceiverCbbankAccount(null);
                }
                logCompanyBalanceCashFlow.setTransferNominal(transferNominal);
                logCompanyBalanceCashFlow.setHistoryDate(Timestamp.valueOf(LocalDateTime.now()));
                logCompanyBalanceCashFlow.setTblEmployee(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                logCompanyBalanceCashFlow.setHistoryNote("");
                session.saveOrUpdate(logCompanyBalanceCashFlow);
                //open close company balance - detail (cashier)
                if ((logCompanyBalanceCashFlow.getTblCompanyBalanceByIdsenderCompanyBalance() != null
                        && logCompanyBalanceCashFlow.getTblCompanyBalanceByIdsenderCompanyBalance().getIdbalance() == 3) //Kas Kasir = '3'    
                        || (logCompanyBalanceCashFlow.getTblCompanyBalanceByIdreceiverCompanyBalance() != null
                        && logCompanyBalanceCashFlow.getTblCompanyBalanceByIdreceiverCompanyBalance().getIdbalance() == 3)) {    //Kas Kasir = '3'
                    TblOpenCloseCashierBalance dataOpenCloseCashierBalance = getDataOpenCloseCashierBalanceIsActive();
                    if (dataOpenCloseCashierBalance != null) {
                        TblOpenCloseCashierBalanceDetail dataOpenCloseCashierBalanceDetail = new TblOpenCloseCashierBalanceDetail();
                        dataOpenCloseCashierBalanceDetail.setTblOpenCloseCashierBalance(dataOpenCloseCashierBalance);
                        dataOpenCloseCashierBalanceDetail.setLogCompanyBalanceCashFlow(logCompanyBalanceCashFlow);
                        dataOpenCloseCashierBalanceDetail.setDetailData(getDetailData(logCompanyBalanceCashFlow));
                        dataOpenCloseCashierBalanceDetail.setCreateDate(Timestamp.valueOf(LocalDateTime.now()));
                        dataOpenCloseCashierBalanceDetail.setTblEmployeeByCreateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                        dataOpenCloseCashierBalanceDetail.setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                        dataOpenCloseCashierBalanceDetail.setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                        dataOpenCloseCashierBalanceDetail.setRefRecordStatus(session.find(RefRecordStatus.class, 1));
                        session.saveOrUpdate(dataOpenCloseCashierBalanceDetail);
                    }
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

    private TblOpenCloseCashierBalance getDataOpenCloseCashierBalanceIsActive() {
        List<TblOpenCloseCashierBalance> list = session.getNamedQuery("findAllTblOpenCloseCashierBalanceByIDOpenCloseStatus")
                .setParameter("idOpenCloseStatus", 1) //'Aktif'
                .list();
        return list.isEmpty() ? null : list.get(0);
    }

    private String getDetailData(LogCompanyBalanceCashFlow logCompanyBalanceCashFlow) {
        String result = "";
        if (logCompanyBalanceCashFlow != null) {
            if (logCompanyBalanceCashFlow.getTblCompanyBalanceByIdsenderCompanyBalance() != null
                    && logCompanyBalanceCashFlow.getTblCompanyBalanceByIdsenderCompanyBalance().getIdbalance() == 3) {   //Kas Kasir = '3'
                //expense
                result += "Pengurangan Kas via transfer ke : "
                        + logCompanyBalanceCashFlow.getTblCompanyBalanceByIdreceiverCompanyBalance().getBalanceName()
                        + (logCompanyBalanceCashFlow.getTblBankAccountByIdreceiverCbbankAccount() != null
                                ? (logCompanyBalanceCashFlow.getTblBankAccountByIdreceiverCbbankAccount().getCodeBankAccount()
                                + " - "
                                + logCompanyBalanceCashFlow.getTblBankAccountByIdreceiverCbbankAccount().getTblBank().getBankName())
                                : "")
                        + "\n";
                result += "Nominal transfer : " + ClassFormatter.currencyFormat.cFormat(logCompanyBalanceCashFlow.getTransferNominal());
            } else {
                if (logCompanyBalanceCashFlow.getTblCompanyBalanceByIdreceiverCompanyBalance() != null
                        && logCompanyBalanceCashFlow.getTblCompanyBalanceByIdreceiverCompanyBalance().getIdbalance() == 3) { //Kas Kasir = '3'
                    //income
                    result += "Penambahan Kas via transfer dari : "
                            + logCompanyBalanceCashFlow.getTblCompanyBalanceByIdsenderCompanyBalance().getBalanceName()
                            + (logCompanyBalanceCashFlow.getTblBankAccountByIdsenderCbbankAccount() != null
                                    ? (logCompanyBalanceCashFlow.getTblBankAccountByIdsenderCbbankAccount().getCodeBankAccount()
                                    + " - "
                                    + logCompanyBalanceCashFlow.getTblBankAccountByIdsenderCbbankAccount().getTblBank().getBankName())
                                    : "")
                            + "\n";
                    result += "Nominal transfer : " + ClassFormatter.currencyFormat.cFormat(logCompanyBalanceCashFlow.getTransferNominal());
                }
            }
        }
        return result;
    }

    @Override
    public TblCompanyBalance getDataCompanyBalance() {
        errMsg = "";
        TblCompanyBalance data = null;
        if (ClassSession.checkUserSession()) {  //check user current session
            try {
                session = HibernateUtil.getSessionFactory().getCurrentSession();
                session.beginTransaction();
                data = (TblCompanyBalance) session.find(TblCompanyBalance.class, (long) 1);
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
    public TblCompanyBalance getDataBackOfficeBalance() {
        errMsg = "";
        TblCompanyBalance data = null;
        if (ClassSession.checkUserSession()) {  //check user current session
            try {
                session = HibernateUtil.getSessionFactory().getCurrentSession();
                session.beginTransaction();
                data = (TblCompanyBalance) session.find(TblCompanyBalance.class, (long) 2);
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
    public TblCompanyBalance getDataCashierBalance() {
        errMsg = "";
        TblCompanyBalance data = null;
        if (ClassSession.checkUserSession()) {  //check user current session
            try {
                session = HibernateUtil.getSessionFactory().getCurrentSession();
                session.beginTransaction();
                data = (TblCompanyBalance) session.find(TblCompanyBalance.class, (long) 3);
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
    public TblCompanyBalance getDataDepositBalance() {
        errMsg = "";
        TblCompanyBalance data = null;
        if (ClassSession.checkUserSession()) {  //check user current session
            try {
                session = HibernateUtil.getSessionFactory().getCurrentSession();
                session.beginTransaction();
                data = (TblCompanyBalance) session.find(TblCompanyBalance.class, (long) 4);
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
    public TblCompanyBalanceBankAccount insertDataCompanyBalanceBankAccount(TblCompanyBalanceBankAccount companyBalanceBankAccount) {
        errMsg = "";
        TblCompanyBalanceBankAccount tblCompanyBalanceBankAccount = companyBalanceBankAccount;
        if (ClassSession.checkUserSession()) {  //check user current session
            try {
                session = HibernateUtil.getSessionFactory().getCurrentSession();
                session.beginTransaction();
                //data bank account
                tblCompanyBalanceBankAccount.getTblBankAccount().setCreateDate(Timestamp.valueOf(LocalDateTime.now()));
                tblCompanyBalanceBankAccount.getTblBankAccount().setTblEmployeeByCreateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                tblCompanyBalanceBankAccount.getTblBankAccount().setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                tblCompanyBalanceBankAccount.getTblBankAccount().setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                tblCompanyBalanceBankAccount.getTblBankAccount().setRefRecordStatus(session.find(RefRecordStatus.class, 1));
                session.saveOrUpdate(tblCompanyBalanceBankAccount.getTblBankAccount());
                //data company balance - bank account
                tblCompanyBalanceBankAccount.setCreateDate(Timestamp.valueOf(LocalDateTime.now()));
                tblCompanyBalanceBankAccount.setTblEmployeeByCreateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                tblCompanyBalanceBankAccount.setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                tblCompanyBalanceBankAccount.setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                tblCompanyBalanceBankAccount.setRefRecordStatus(session.find(RefRecordStatus.class, 1));
                session.saveOrUpdate(tblCompanyBalanceBankAccount);
                //data company balance (update nominal : +)
                tblCompanyBalanceBankAccount.getTblCompanyBalance().setBalanceNominal(tblCompanyBalanceBankAccount.getTblCompanyBalance().getBalanceNominal()
                        .add(tblCompanyBalanceBankAccount.getCompanyBalanceBankAccountNominal()));
                tblCompanyBalanceBankAccount.getTblCompanyBalance().setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                tblCompanyBalanceBankAccount.getTblCompanyBalance().setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                session.update(tblCompanyBalanceBankAccount.getTblCompanyBalance());
                //history company balance - cash flow
                LogCompanyBalanceCashFlow logCompanyBalanceCashFlow = new LogCompanyBalanceCashFlow();
                logCompanyBalanceCashFlow.setTblCompanyBalanceByIdsenderCompanyBalance(null);
                logCompanyBalanceCashFlow.setTblCompanyBalanceByIdreceiverCompanyBalance(companyBalanceBankAccount.getTblCompanyBalance());
                logCompanyBalanceCashFlow.setTblBankAccountByIdsenderCbbankAccount(null);
                logCompanyBalanceCashFlow.setTblBankAccountByIdreceiverCbbankAccount(companyBalanceBankAccount.getTblBankAccount());
                logCompanyBalanceCashFlow.setTransferNominal(companyBalanceBankAccount.getCompanyBalanceBankAccountNominal());
                logCompanyBalanceCashFlow.setHistoryDate(Timestamp.valueOf(LocalDateTime.now()));
                logCompanyBalanceCashFlow.setTblEmployee(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                logCompanyBalanceCashFlow.setHistoryNote("");
                session.saveOrUpdate(logCompanyBalanceCashFlow);
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
        return tblCompanyBalanceBankAccount;
    }

    @Override
    public boolean updateDataCompanyBalanceBankAccount(TblCompanyBalanceBankAccount companyBalanceBankAccount) {
        errMsg = "";
        if (ClassSession.checkUserSession()) {  //check user current session
            try {
                session = HibernateUtil.getSessionFactory().getCurrentSession();
                session.beginTransaction();
                //data bank account
                companyBalanceBankAccount.getTblBankAccount().setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                companyBalanceBankAccount.getTblBankAccount().setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                session.update(companyBalanceBankAccount.getTblBankAccount());
                //data company balance - bank account
                companyBalanceBankAccount.setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                companyBalanceBankAccount.setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                session.update(companyBalanceBankAccount);
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
    public boolean updateDataCompanyBalanceBankAccountAddFund(
            TblCompanyBalanceBankAccount companyBalanceBankAccount,
            BigDecimal addFundNominal,
            String addFundNote) {
        errMsg = "";
        if (ClassSession.checkUserSession()) {  //check user current session
            try {
                session = HibernateUtil.getSessionFactory().getCurrentSession();
                session.beginTransaction();
                //data company balance
                companyBalanceBankAccount.getTblCompanyBalance().setBalanceNominal(companyBalanceBankAccount.getTblCompanyBalance().getBalanceNominal().add(addFundNominal));
                companyBalanceBankAccount.getTblCompanyBalance().setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                companyBalanceBankAccount.getTblCompanyBalance().setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                session.update(companyBalanceBankAccount.getTblCompanyBalance());
                //data company balance - bank account
                companyBalanceBankAccount.setCompanyBalanceBankAccountNominal(companyBalanceBankAccount.getCompanyBalanceBankAccountNominal().add(addFundNominal));
                companyBalanceBankAccount.setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                companyBalanceBankAccount.setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                session.update(companyBalanceBankAccount);
                //history company balance - cash flow
                LogCompanyBalanceCashFlow logCompanyBalanceCashFlow = new LogCompanyBalanceCashFlow();
                logCompanyBalanceCashFlow.setTblCompanyBalanceByIdsenderCompanyBalance(null);
                logCompanyBalanceCashFlow.setTblCompanyBalanceByIdreceiverCompanyBalance(companyBalanceBankAccount.getTblCompanyBalance());
                logCompanyBalanceCashFlow.setTblBankAccountByIdsenderCbbankAccount(null);
                logCompanyBalanceCashFlow.setTblBankAccountByIdreceiverCbbankAccount(companyBalanceBankAccount.getTblBankAccount());
                logCompanyBalanceCashFlow.setTransferNominal(addFundNominal);
                logCompanyBalanceCashFlow.setHistoryDate(Timestamp.valueOf(LocalDateTime.now()));
                logCompanyBalanceCashFlow.setTblEmployee(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                logCompanyBalanceCashFlow.setHistoryNote(addFundNote);
                session.saveOrUpdate(logCompanyBalanceCashFlow);
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
    public boolean deleteDataCompanyBalanceBankAccount(TblCompanyBalanceBankAccount companyBalanceBankAccount) {
        errMsg = "";
        if (ClassSession.checkUserSession()) {  //check user current session
            try {
                session = HibernateUtil.getSessionFactory().getCurrentSession();
                session.beginTransaction();
                if (companyBalanceBankAccount.getRefRecordStatus().getIdstatus() == 1) {
                    //data company balance - bank account
                    companyBalanceBankAccount.setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                    companyBalanceBankAccount.setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                    companyBalanceBankAccount.setRefRecordStatus(session.find(RefRecordStatus.class, 2));
                    session.update(companyBalanceBankAccount);
                    //data bank account
                    companyBalanceBankAccount.getTblBankAccount().setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                    companyBalanceBankAccount.getTblBankAccount().setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                    companyBalanceBankAccount.getTblBankAccount().setRefRecordStatus(session.find(RefRecordStatus.class, 2));
                    session.update(companyBalanceBankAccount.getTblBankAccount());
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
    public TblCompanyBalanceBankAccount getCompanyBalanceBankAccount(long id) {
        errMsg = "";
        TblCompanyBalanceBankAccount data = null;
        if (ClassSession.checkUserSession()) {  //check user current session
            try {
                session = HibernateUtil.getSessionFactory().getCurrentSession();
                session.beginTransaction();
                data = (TblCompanyBalanceBankAccount) session.find(TblCompanyBalanceBankAccount.class, id);
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
    public List<TblCompanyBalanceBankAccount> getAllDataCompanyBalanceBankAccount(long idCompanyBalance) {
        errMsg = "";
        List<TblCompanyBalanceBankAccount> list = new ArrayList<>();
        if (ClassSession.checkUserSession()) {  //check user current session
            try {
                session = HibernateUtil.getSessionFactory().getCurrentSession();
                session.beginTransaction();
                list = session.getNamedQuery("findAllTblCompanyBalanceBankAccountByIDCompanyBalance")
                        .setParameter("idCompanyBalance", idCompanyBalance)
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
//    @Override
//    public RefBankAccountHolderStatus getBankAccountHolderStatus(int id) {
//        errMsg = "";
//        RefBankAccountHolderStatus data = null;
//        try {
//            session = HibernateUtil.getSessionFactory().getCurrentSession();
//            session.beginTransaction();
//            data = (RefBankAccountHolderStatus) session.find(RefBankAccountHolderStatus.class, id);
//            session.getTransaction().commit();
//        } catch (Exception e) {
//            if (session.getTransaction().isActive()) {
//                session.getTransaction().rollback();
//            }
//            errMsg = e.getMessage();
//        } finally {
//            //session.close();
//        }
//        return data;
//    }
//
//    @Override
//    public List<RefBankAccountHolderStatus> getAllDataBankAccountHolderStatus() {
//        errMsg = "";
//        List<RefBankAccountHolderStatus> list = new ArrayList<>();
//        try {
//            session = HibernateUtil.getSessionFactory().getCurrentSession();
//            session.beginTransaction();
//            list = session.getNamedQuery("findAllRefBankAccountHolderStatus").list();
//            session.getTransaction().commit();
//        } catch (Exception e) {
//            if (session.getTransaction().isActive()) {
//                session.getTransaction().rollback();
//            }
//            errMsg = e.getMessage();
//        } finally {
//            //session.close();
//        }
//        return list;
//    }
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
    
    public List<TblEmployee>getAllDataEmployee(){
      errMsg = "";
      List<TblEmployee>list = new ArrayList();
      if(ClassSession.checkUserSession()){
           try{
             session = HibernateUtil.getSessionFactory().getCurrentSession();
             session.beginTransaction();
             list = session.getNamedQuery("findAllTblEmployee").list();
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
    
    public List<TblCompanyBalance>getAllDataCompanyBalance(){
       errMsg = "";
      List<TblCompanyBalance>list = new ArrayList();
      if(ClassSession.checkUserSession()){
           try{
             session = HibernateUtil.getSessionFactory().getCurrentSession();
             session.beginTransaction();
             list = session.getNamedQuery("findAllTblCompanyBalance").list();
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
    
//TRANSFER - TERIMA
    public boolean insertDataBalanceTransfer(TblCompanyBalanceTransferReceived companyBalanceTransferReceived){
      errMsg = "";
      //System.out.println("--- " + companyBalanceTransferReceived.);
      TblCompanyBalanceTransferReceived transferReceived = companyBalanceTransferReceived;
   //    System.out.println("company Balance Sender :"+transferReceived.getTblCompanyBalanceByIdcompanyBalanceSender());
   //     System.out.println("company Balance Received :"+transferReceived.getTblCompanyBalanceByIdcompanyBalanceReceived());
   //     System.out.println("company Balance Bank Account Sender :"+transferReceived.getTblCompanyBalanceBankAccountByIdcbbankAccountSender());
   //     System.out.println("company Balance Received :"+transferReceived.getTblCompanyBalanceBankAccountByIdcbbankAccountReceived());
      if(ClassSession.checkUserSession()){
           try{
             session = HibernateUtil.getSessionFactory().getCurrentSession();
             session.beginTransaction();
               if(transferReceived.getTblCompanyBalanceByIdcompanyBalanceReceived().getIdbalance()==1){
                 transferReceived.setIsReceived(Boolean.TRUE);
                 transferReceived.getTblCompanyBalanceByIdcompanyBalanceSender().setBalanceNominal(transferReceived.getTblCompanyBalanceByIdcompanyBalanceSender().getBalanceNominal().subtract(transferReceived.getNominal()));
                 transferReceived.getTblCompanyBalanceByIdcompanyBalanceSender().setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                 transferReceived.getTblCompanyBalanceByIdcompanyBalanceSender().setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class,ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                 transferReceived.getTblCompanyBalanceByIdcompanyBalanceSender().setRefRecordStatus(session.find(RefRecordStatus.class,1));
                 session.update(transferReceived.getTblCompanyBalanceByIdcompanyBalanceSender());
                 
                 transferReceived.getTblCompanyBalanceByIdcompanyBalanceReceived().setBalanceNominal(transferReceived.getTblCompanyBalanceByIdcompanyBalanceReceived().getBalanceNominal().add(companyBalanceTransferReceived.getNominal()));
                 transferReceived.getTblCompanyBalanceByIdcompanyBalanceReceived().setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                 transferReceived.getTblCompanyBalanceByIdcompanyBalanceReceived().setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class,ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                 transferReceived.getTblCompanyBalanceByIdcompanyBalanceReceived().setRefRecordStatus(session.find(RefRecordStatus.class,1));
                 session.update(transferReceived.getTblCompanyBalanceByIdcompanyBalanceReceived());
                 
                 transferReceived.getTblCompanyBalanceBankAccountByIdcbbankAccountReceived().setCompanyBalanceBankAccountNominal(transferReceived.getTblCompanyBalanceBankAccountByIdcbbankAccountReceived().getCompanyBalanceBankAccountNominal().add(transferReceived.getNominal()));
                 transferReceived.getTblCompanyBalanceBankAccountByIdcbbankAccountReceived().setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class,ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                 transferReceived.getTblCompanyBalanceBankAccountByIdcbbankAccountReceived().setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                 transferReceived.getTblCompanyBalanceBankAccountByIdcbbankAccountReceived().setRefRecordStatus(session.find(RefRecordStatus.class,1));
                 session.update(transferReceived.getTblCompanyBalanceBankAccountByIdcbbankAccountReceived());
                 
                 LogCompanyBalanceCashFlow cashFlow = new LogCompanyBalanceCashFlow();
                 cashFlow.setTblBankAccountByIdreceiverCbbankAccount(transferReceived.getTblCompanyBalanceBankAccountByIdcbbankAccountReceived()!=null ? transferReceived.getTblCompanyBalanceBankAccountByIdcbbankAccountReceived().getTblBankAccount():null);
                 cashFlow.setTblBankAccountByIdsenderCbbankAccount(transferReceived.getTblCompanyBalanceBankAccountByIdcbbankAccountSender()!=null ? transferReceived.getTblCompanyBalanceBankAccountByIdcbbankAccountSender().getTblBankAccount():null);
                 cashFlow.setTblCompanyBalanceByIdreceiverCompanyBalance(transferReceived.getTblCompanyBalanceByIdcompanyBalanceReceived()!=null ? transferReceived.getTblCompanyBalanceByIdcompanyBalanceReceived():null);
                 cashFlow.setTblCompanyBalanceByIdsenderCompanyBalance(transferReceived.getTblCompanyBalanceByIdcompanyBalanceSender()!=null ? transferReceived.getTblCompanyBalanceByIdcompanyBalanceSender():null);
                 cashFlow.setTblEmployee(session.find(TblEmployee.class,ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                 cashFlow.setTransferNominal(transferReceived.getNominal());
                 cashFlow.setHistoryDate(Timestamp.valueOf(LocalDateTime.now()));
                 session.save(cashFlow);
               }
            // System.out.println("yyy " + transferReceived.getTblCompanyBalanceBankAccountByTblCompanyBalanceSender);
             transferReceived.setCreatedDate(Timestamp.valueOf(LocalDateTime.now()));
             transferReceived.setLastUpdatedDate(Timestamp.valueOf(LocalDateTime.now()));
             transferReceived.setTblEmployeeByCreatedBy(session.find(TblEmployee.class,ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
             transferReceived.setTblEmployeeByLastUpdatedBy(session.find(TblEmployee.class,ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
             transferReceived.setRefRecordStatus(session.find(RefRecordStatus.class,1));
             transferReceived.setTransferDate(Timestamp.valueOf(LocalDateTime.now()));
             session.saveOrUpdate(transferReceived);
             session.getTransaction().commit();
           }
           catch(Exception e){
               if(session.getTransaction().isActive()){
                 session.getTransaction().rollback();
               }
              errMsg = e.getMessage();
              return false;
           }
       }
      return true;
    }
    
    public boolean updateDataBalanceTransfer(TblCompanyBalanceTransferReceived companyBalanceTransferReceived){
      errMsg = "";
      //System.out.println("--- " + companyBalanceTransferReceived.);
       /* System.out.println("company Balance Sender :"+transferReceived.getTblCompanyBalanceByTblCompanyBalanceSender());
        System.out.println("company Balance Received :"+transferReceived.getTblCompanyBalanceByTblCompanyBalanceReceived());
        System.out.println("company Balance Bank Account Sender :"+transferReceived.getTblCompanyBalanceBankAccountByTblCompanyBalanceSender());
        System.out.println("company Balance Received :"+transferReceived.getTblCompanyBalanceBankAccountByTblCompanyBalanceReceived()); */
      if(ClassSession.checkUserSession()){
           try{
             session = HibernateUtil.getSessionFactory().getCurrentSession();
             session.beginTransaction();         
             companyBalanceTransferReceived.setLastUpdatedDate(Timestamp.valueOf(LocalDateTime.now()));
             companyBalanceTransferReceived.setTblEmployeeByLastUpdatedBy(session.find(TblEmployee.class,ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
             companyBalanceTransferReceived.setRefRecordStatus(session.find(RefRecordStatus.class,1));
             companyBalanceTransferReceived.setTransferDate(Timestamp.valueOf(LocalDateTime.now()));
             session.update(companyBalanceTransferReceived);
             session.getTransaction().commit();
           }
           catch(Exception e){
               if(session.getTransaction().isActive()){
                 session.getTransaction().rollback();
               }
              errMsg = e.getMessage();
              return false;
           }
       }
      return true;
    }
    
   public boolean deleteDataBalanceTransfer(TblCompanyBalanceTransferReceived companyBalanceTransferReceived){
      errMsg = "";
      //System.out.println("--- " + companyBalanceTransferReceived.);
       /* System.out.println("company Balance Sender :"+transferReceived.getTblCompanyBalanceByTblCompanyBalanceSender());
        System.out.println("company Balance Received :"+transferReceived.getTblCompanyBalanceByTblCompanyBalanceReceived());
        System.out.println("company Balance Bank Account Sender :"+transferReceived.getTblCompanyBalanceBankAccountByTblCompanyBalanceSender());
        System.out.println("company Balance Received :"+transferReceived.getTblCompanyBalanceBankAccountByTblCompanyBalanceReceived()); */
      if(ClassSession.checkUserSession()){
           try{
             session = HibernateUtil.getSessionFactory().getCurrentSession();
             session.beginTransaction();         
             companyBalanceTransferReceived.setLastUpdatedDate(Timestamp.valueOf(LocalDateTime.now()));
             companyBalanceTransferReceived.setTblEmployeeByLastUpdatedBy(session.find(TblEmployee.class,ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
             companyBalanceTransferReceived.setRefRecordStatus(session.find(RefRecordStatus.class,2));
             companyBalanceTransferReceived.setTransferDate(Timestamp.valueOf(LocalDateTime.now()));
             session.update(companyBalanceTransferReceived);
             session.getTransaction().commit();
           }
           catch(Exception e){
               if(session.getTransaction().isActive()){
                 session.getTransaction().rollback();
               }
              errMsg = e.getMessage();
              return false;
           }
       }
      return true;
    }
    
   public boolean updateDataBalanceTransferReceived(TblCompanyBalanceTransferReceived companyBalanceTransferReceived){
       TblCompanyBalanceTransferReceived transferReceived = companyBalanceTransferReceived;
   //    System.out.println("company Balance Sender :"+transferReceived.getTblCompanyBalanceByIdcompanyBalanceSender());
   //     System.out.println("company Balance Received :"+transferReceived.getTblCompanyBalanceByIdcompanyBalanceReceived());
   //     System.out.println("company Balance Bank Account Sender :"+transferReceived.getTblCompanyBalanceBankAccountByIdcbbankAccountSender());
   //     System.out.println("company Balance Received :"+transferReceived.getTblCompanyBalanceBankAccountByIdcbbankAccountReceived());
      if(ClassSession.checkUserSession()){
           try{
             session = HibernateUtil.getSessionFactory().getCurrentSession();
             session.beginTransaction();
             transferReceived.setIsReceived(Boolean.TRUE);
             transferReceived.getTblCompanyBalanceByIdcompanyBalanceSender().setBalanceNominal(transferReceived.getTblCompanyBalanceByIdcompanyBalanceSender().getBalanceNominal().subtract(transferReceived.getNominal()));
             transferReceived.getTblCompanyBalanceByIdcompanyBalanceSender().setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
             transferReceived.getTblCompanyBalanceByIdcompanyBalanceSender().setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class,ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
             transferReceived.getTblCompanyBalanceByIdcompanyBalanceSender().setRefRecordStatus(session.find(RefRecordStatus.class,1));
             session.update(transferReceived.getTblCompanyBalanceByIdcompanyBalanceSender());
               if(transferReceived.getTblCompanyBalanceByIdcompanyBalanceReceived().getIdbalance()!=1){
                 transferReceived.getTblCompanyBalanceByIdcompanyBalanceReceived().setBalanceNominal(transferReceived.getTblCompanyBalanceByIdcompanyBalanceReceived().getBalanceNominal().add(transferReceived.getNominal()));
                 transferReceived.getTblCompanyBalanceByIdcompanyBalanceReceived().setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                 transferReceived.getTblCompanyBalanceByIdcompanyBalanceReceived().setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class,ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                 transferReceived.getTblCompanyBalanceByIdcompanyBalanceReceived().setRefRecordStatus(session.find(RefRecordStatus.class,1));
                 session.update(transferReceived.getTblCompanyBalanceByIdcompanyBalanceReceived());
               }   
               transferReceived.getTblCompanyBalanceBankAccountByIdcbbankAccountSender().setCompanyBalanceBankAccountNominal(transferReceived.getTblCompanyBalanceBankAccountByIdcbbankAccountSender().getCompanyBalanceBankAccountNominal().subtract(transferReceived.getNominal()));
               transferReceived.getTblCompanyBalanceBankAccountByIdcbbankAccountSender().setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class,ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
               transferReceived.getTblCompanyBalanceBankAccountByIdcbbankAccountSender().setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
               transferReceived.getTblCompanyBalanceBankAccountByIdcbbankAccountSender().setRefRecordStatus(session.find(RefRecordStatus.class,1));
               session.update(transferReceived.getTblCompanyBalanceBankAccountByIdcbbankAccountSender());
               
               if(transferReceived.getTblCompanyBalanceByIdcompanyBalanceReceived().getIdbalance()==1){
                 transferReceived.getTblCompanyBalanceBankAccountByIdcbbankAccountReceived().setCompanyBalanceBankAccountNominal(transferReceived.getTblCompanyBalanceBankAccountByIdcbbankAccountReceived().getCompanyBalanceBankAccountNominal().add(transferReceived.getNominal()));
                 transferReceived.getTblCompanyBalanceBankAccountByIdcbbankAccountReceived().setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class,ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                 transferReceived.getTblCompanyBalanceBankAccountByIdcbbankAccountReceived().setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                 transferReceived.getTblCompanyBalanceBankAccountByIdcbbankAccountReceived().setRefRecordStatus(session.find(RefRecordStatus.class,1));
                 session.update(transferReceived.getTblCompanyBalanceBankAccountByIdcbbankAccountReceived());
               }
               
               LogCompanyBalanceCashFlow cashFlow = new LogCompanyBalanceCashFlow();
               cashFlow.setTblBankAccountByIdreceiverCbbankAccount(transferReceived.getTblCompanyBalanceBankAccountByIdcbbankAccountReceived()!=null ? transferReceived.getTblCompanyBalanceBankAccountByIdcbbankAccountReceived().getTblBankAccount():null);
               cashFlow.setTblBankAccountByIdsenderCbbankAccount(transferReceived.getTblCompanyBalanceBankAccountByIdcbbankAccountSender()!=null ? transferReceived.getTblCompanyBalanceBankAccountByIdcbbankAccountSender().getTblBankAccount():null);
               cashFlow.setTblCompanyBalanceByIdreceiverCompanyBalance(transferReceived.getTblCompanyBalanceByIdcompanyBalanceReceived()!=null ? transferReceived.getTblCompanyBalanceByIdcompanyBalanceReceived():null);
               cashFlow.setTblCompanyBalanceByIdsenderCompanyBalance(transferReceived.getTblCompanyBalanceByIdcompanyBalanceSender()!=null ? transferReceived.getTblCompanyBalanceByIdcompanyBalanceSender():null);
               cashFlow.setTblEmployee(session.find(TblEmployee.class,ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
               cashFlow.setTransferNominal(transferReceived.getNominal());
               cashFlow.setHistoryDate(Timestamp.valueOf(LocalDateTime.now()));
               session.save(cashFlow);
               
             transferReceived.setCreatedDate(Timestamp.valueOf(LocalDateTime.now()));
             transferReceived.setLastUpdatedDate(Timestamp.valueOf(LocalDateTime.now()));
             transferReceived.setTblEmployeeByCreatedBy(session.find(TblEmployee.class,ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
             transferReceived.setTblEmployeeByLastUpdatedBy(session.find(TblEmployee.class,ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
             transferReceived.setRefRecordStatus(session.find(RefRecordStatus.class,1));
             transferReceived.setTransferDate(Timestamp.valueOf(LocalDateTime.now()));
             session.saveOrUpdate(transferReceived);
         
             session.getTransaction().commit();
            }
           catch(Exception e){
               if(session.getTransaction().isActive()){
                   session.getTransaction().rollback();
               }
               errMsg = e.getMessage();
               return false;
           }
       }
      else{
         return false;
      }
      return true;
   }
   
    public boolean updateDataBalanceReceived(TblCompanyBalanceTransferReceived companyBalanceTransferReceived){
      errMsg = "";
      //System.out.println("--- " + companyBalanceTransferReceived.);
       /* System.out.println("company Balance Sender :"+transferReceived.getTblCompanyBalanceByTblCompanyBalanceSender());
        System.out.println("company Balance Received :"+transferReceived.getTblCompanyBalanceByTblCompanyBalanceReceived());
        System.out.println("company Balance Bank Account Sender :"+transferReceived.getTblCompanyBalanceBankAccountByTblCompanyBalanceSender());
        System.out.println("company Balance Received :"+transferReceived.getTblCompanyBalanceBankAccountByTblCompanyBalanceReceived()); */
      if(ClassSession.checkUserSession()){
           try{
             session = HibernateUtil.getSessionFactory().getCurrentSession();
             session.beginTransaction();
             companyBalanceTransferReceived.setIsReceived(Boolean.TRUE);
             companyBalanceTransferReceived.getTblCompanyBalanceByIdcompanyBalanceSender().setBalanceNominal(companyBalanceTransferReceived.getTblCompanyBalanceByIdcompanyBalanceSender().getBalanceNominal().subtract(companyBalanceTransferReceived.getNominal()));
             companyBalanceTransferReceived.getTblCompanyBalanceByIdcompanyBalanceSender().setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
             companyBalanceTransferReceived.getTblCompanyBalanceByIdcompanyBalanceSender().setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class,ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
             companyBalanceTransferReceived.getTblCompanyBalanceByIdcompanyBalanceSender().setRefRecordStatus(session.find(RefRecordStatus.class,1));
             session.update(companyBalanceTransferReceived.getTblCompanyBalanceByIdcompanyBalanceSender());
             
               if(companyBalanceTransferReceived.getTblCompanyBalanceByIdcompanyBalanceSender().getIdbalance()==1){
                 companyBalanceTransferReceived.getTblCompanyBalanceBankAccountByIdcbbankAccountSender().setCompanyBalanceBankAccountNominal(companyBalanceTransferReceived.getTblCompanyBalanceBankAccountByIdcbbankAccountSender().getCompanyBalanceBankAccountNominal().subtract(companyBalanceTransferReceived.getNominal()));
                 companyBalanceTransferReceived.getTblCompanyBalanceBankAccountByIdcbbankAccountSender().setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class,ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                 companyBalanceTransferReceived.getTblCompanyBalanceBankAccountByIdcbbankAccountSender().setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                 companyBalanceTransferReceived.getTblCompanyBalanceBankAccountByIdcbbankAccountSender().setRefRecordStatus(session.find(RefRecordStatus.class,1));
                 session.update(companyBalanceTransferReceived.getTblCompanyBalanceBankAccountByIdcbbankAccountSender());
                }
             companyBalanceTransferReceived.getTblCompanyBalanceByIdcompanyBalanceReceived().setBalanceNominal(companyBalanceTransferReceived.getTblCompanyBalanceByIdcompanyBalanceReceived().getBalanceNominal().add(companyBalanceTransferReceived.getNominal()));
             companyBalanceTransferReceived.getTblCompanyBalanceByIdcompanyBalanceReceived().setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
             companyBalanceTransferReceived.getTblCompanyBalanceByIdcompanyBalanceReceived().setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class,ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
             companyBalanceTransferReceived.getTblCompanyBalanceByIdcompanyBalanceReceived().setRefRecordStatus(session.find(RefRecordStatus.class,1));
             session.update(companyBalanceTransferReceived.getTblCompanyBalanceByIdcompanyBalanceReceived());
             
             LogCompanyBalanceCashFlow cashFlow = new LogCompanyBalanceCashFlow();
             cashFlow.setTblCompanyBalanceByIdreceiverCompanyBalance(companyBalanceTransferReceived.getTblCompanyBalanceByIdcompanyBalanceReceived()!=null ? companyBalanceTransferReceived.getTblCompanyBalanceByIdcompanyBalanceReceived():null);
             cashFlow.setTblCompanyBalanceByIdsenderCompanyBalance(companyBalanceTransferReceived.getTblCompanyBalanceByIdcompanyBalanceSender()!=null ? companyBalanceTransferReceived.getTblCompanyBalanceByIdcompanyBalanceSender():null);
             cashFlow.setTblEmployee(session.find(TblEmployee.class,ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
             cashFlow.setTransferNominal(companyBalanceTransferReceived.getNominal());
             cashFlow.setHistoryDate(Timestamp.valueOf(LocalDateTime.now()));
             session.save(cashFlow);
             
             companyBalanceTransferReceived.setLastUpdatedDate(Timestamp.valueOf(LocalDateTime.now()));
             companyBalanceTransferReceived.setTblEmployeeByLastUpdatedBy(session.find(TblEmployee.class,ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
             companyBalanceTransferReceived.setRefRecordStatus(session.find(RefRecordStatus.class,1));
             companyBalanceTransferReceived.setReceivedDate(Timestamp.valueOf(LocalDateTime.now()));
             companyBalanceTransferReceived.setTblEmployeeByReceivedBy(session.find(TblEmployee.class,ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
             session.update(companyBalanceTransferReceived);
             session.getTransaction().commit();
           }
           catch(Exception e){
               if(session.getTransaction().isActive()){
                 session.getTransaction().rollback();
               }
              errMsg = e.getMessage();
              return false;
           }
       }
      return true;
    }
    
    public List<TblCompanyBalanceTransferReceived>getAllDataTransfer(long idSender){
       List<TblCompanyBalanceTransferReceived>list = new ArrayList();
       errMsg = "";
       if(ClassSession.checkUserSession()){
          try{
             session  = HibernateUtil.getSessionFactory().getCurrentSession();
             session.beginTransaction();
             list = session.getNamedQuery("findAllTblCompanyBalanceTransferReceivedByIDSender").setParameter("idBalance",idSender).list();
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
    
    public List<TblCompanyBalanceTransferReceived>getAllDataReceived(long idReceived){
       List<TblCompanyBalanceTransferReceived>list = new ArrayList();
       errMsg = "";
       if(ClassSession.checkUserSession()){
          try{
             session  = HibernateUtil.getSessionFactory().getCurrentSession();
             session.beginTransaction();
             list = session.getNamedQuery("findAllTblCompanyBalanceTransferReceivedByIDReceived").setParameter("idBalance",idReceived).list();
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
    
    public TblCompanyBalanceTransferReceived getDataCompanyBalanceTransferReceived(long id){
       TblCompanyBalanceTransferReceived companyBalanceTransferReceived = new TblCompanyBalanceTransferReceived();
       errMsg = "";
       if(ClassSession.checkUserSession()){
         try{
            session = HibernateUtil.getSessionFactory().getCurrentSession();
            session.beginTransaction();
            companyBalanceTransferReceived = session.find(TblCompanyBalanceTransferReceived.class,id);
            session.getTransaction().commit();
         }
         catch(Exception e){
            if(session.getTransaction().isActive()){
               session.getTransaction().rollback();
            }
              errMsg = e.getMessage();
            }
        }
       return companyBalanceTransferReceived;
    }
    
    //CASH FLOW
    public List<LogCompanyBalanceCashFlow>getAllDataCashFlow(TblCompanyBalance companyBalance,TblBankAccount companyBalanceBankAccount,Date startDate,Date endDate){
        errMsg = "";
        List<LogCompanyBalanceCashFlow>list = new ArrayList();
        String queryAll = "from LogCompanyBalanceCashFlow t ";
        String dateClause = "";
        String companyBalanceClause = "";
        String companyBalanceBankAccountClause = "";
        String hslQuery = "";
        String queryWhere  = "";
        Query query = null;
        
        if(ClassSession.checkUserSession()){
           try{
              session = HibernateUtil.getSessionFactory().getCurrentSession();
              session.beginTransaction();
               if(startDate!=null && endDate!=null){
                  dateClause = "convert(date,historyDate) between '"+startDate+"' and '"+endDate+"'";  
                  queryWhere += getQueryResult(queryWhere,dateClause);
                }
               
               if(companyBalance!=null){
                  companyBalanceClause = "(t.tblCompanyBalanceByIdsenderCompanyBalance.idbalance="+companyBalance.getIdbalance()+" or t.tblCompanyBalanceByIdreceiverCompanyBalance.idbalance="+companyBalance.getIdbalance()+")";
                  queryWhere += getQueryResult(queryWhere,companyBalanceClause);
                }
                   
                if(companyBalanceBankAccount!=null){
                  companyBalanceBankAccountClause = "(t.tblBankAccountByIdreceiverCbbankAccount.idbankAccount="+companyBalanceBankAccount.getIdbankAccount()+" or t.tblBankAccountByIdsenderCbbankAccount.idbankAccount="+companyBalanceBankAccount.getIdbankAccount()+")";
                  queryWhere += getQueryResult(queryWhere,companyBalanceBankAccountClause);
                }
                
                hslQuery = queryAll+queryWhere;
                System.out.println("hsl Query :"+hslQuery);
                query = session.createQuery(hslQuery);
                list = (List<LogCompanyBalanceCashFlow>)query.list();
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
    
    private String getQueryResult(String whereClause,String clause){
       String hsl = "";
       if(whereClause.equalsIgnoreCase("")){
          hsl = "where "+clause;
        }
       
       if(!whereClause.equalsIgnoreCase("")){
          hsl+=" and "+clause;
       }
       return hsl;
    }
    
    public BigDecimal getBalance(TblCompanyBalance companyBalance,Date startDate,TblBankAccount bankAccount){
       errMsg = "";
       String balance = "";
       Query query = null;
        
       if(ClassSession.checkUserSession()){
          try{
             session = HibernateUtil.getSessionFactory().getCurrentSession();
             session.beginTransaction();
             if(companyBalance.getIdbalance()==1){
                query = session.getNamedQuery("getBalance").setParameter("idBalance",companyBalance.getIdbalance()).setParameter("dateStart",startDate).setParameter("bankAccount",bankAccount.getIdbankAccount());
             }
             else{
                query  = session.getNamedQuery("getBalance").setParameter("idBalance",companyBalance.getIdbalance()).setParameter("dateStart",startDate).setParameter("bankAccount",null);
             }
             balance = (String)query.uniqueResult();
             System.out.println("Saldo :"+balance);
             session.getTransaction().commit();
           }
          catch(Exception e){
               if(session.getTransaction().isActive()){
                 session.getTransaction().rollback();
               }
              errMsg = e.getMessage();
           }
        }
       return new BigDecimal(balance);
    }
    //--------------------------------------------------------------------------
    @Override
    public String getErrorMessage() {
        return errMsg;
    }

}
