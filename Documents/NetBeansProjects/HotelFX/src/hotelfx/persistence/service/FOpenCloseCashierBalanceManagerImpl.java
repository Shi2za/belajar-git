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
import hotelfx.persistence.model.RefFinanceTransactionPaymentType;
import hotelfx.persistence.model.RefOpenCloseCashierBalanceStatus;
import hotelfx.persistence.model.RefRecordStatus;
import hotelfx.persistence.model.TblCompanyBalance;
import hotelfx.persistence.model.TblEmployee;
import hotelfx.persistence.model.TblHotelExpenseTransaction;
import hotelfx.persistence.model.TblHotelExpenseTransactionDetail;
import hotelfx.persistence.model.TblJob;
import hotelfx.persistence.model.TblOpenCloseCashierBalance;
import hotelfx.persistence.model.TblOpenCloseCashierBalanceDetail;
import hotelfx.persistence.model.TblPeople;
import hotelfx.persistence.model.TblReservationPayment;
import java.math.BigDecimal;
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
public class FOpenCloseCashierBalanceManagerImpl implements FOpenCloseCashierBalanceManager {

    private Session session;

    private String errMsg;

    public FOpenCloseCashierBalanceManagerImpl() {

    }

    //open
    @Override
    public TblOpenCloseCashierBalance insertOpenCloseCashierBalance(TblOpenCloseCashierBalance occb) {
        errMsg = "";
        TblOpenCloseCashierBalance tblOCCB = occb;
        if (ClassSession.checkUserSession()) {  //check user current session
            try {
                session = HibernateUtil.getSessionFactory().getCurrentSession();
                session.beginTransaction();
                //data occb
                tblOCCB.setOpenDateTime(Timestamp.valueOf(LocalDateTime.now()));
                tblOCCB.setRefOpenCloseCashierBalanceStatus(session.find(RefOpenCloseCashierBalanceStatus.class, 1));  //Aktif = '1'
                tblOCCB.setCreateDate(Timestamp.valueOf(LocalDateTime.now()));
                tblOCCB.setTblEmployeeByCreateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                tblOCCB.setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                tblOCCB.setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                tblOCCB.setRefRecordStatus(session.find(RefRecordStatus.class, 1));
                session.saveOrUpdate(tblOCCB);
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
        return tblOCCB;
    }

    //close
    @Override
    public boolean updateOpenCloseCashierBalance(TblOpenCloseCashierBalance occb) {
        errMsg = "";
        if (ClassSession.checkUserSession()) {  //check user current session
            try {
                session = HibernateUtil.getSessionFactory().getCurrentSession();
                session.beginTransaction();
                //data occb
                occb.setCloseDateTime(Timestamp.valueOf(LocalDateTime.now()));
                occb.setRefOpenCloseCashierBalanceStatus(session.find(RefOpenCloseCashierBalanceStatus.class, 0));  //Tidak Aktif = '0'
                occb.setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                occb.setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                session.update(occb);
                //data company balance (cashier:updated)
                TblCompanyBalance companyBalance = session.find(TblCompanyBalance.class, (long) 3);  //Kasir = '3'
                //nominal update
                BigDecimal nominalUpdated = occb.getRealEndBalanceNominal().subtract(companyBalance.getBalanceNominal());
                companyBalance.setBalanceNominal(occb.getRealEndBalanceNominal());
                companyBalance.setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                companyBalance.setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                session.update(companyBalance);
                //data company balance (cash-flow log)
                if (nominalUpdated.compareTo(new BigDecimal("0")) != 0) {
                    LogCompanyBalanceCashFlow logCompanyBalanceCashFlow = new LogCompanyBalanceCashFlow();
                    if (nominalUpdated.compareTo(new BigDecimal("0")) == 1) { //plus balance
                        logCompanyBalanceCashFlow.setTblCompanyBalanceByIdsenderCompanyBalance(null);
                        logCompanyBalanceCashFlow.setTblBankAccountByIdsenderCbbankAccount(null);
                        logCompanyBalanceCashFlow.setTblCompanyBalanceByIdreceiverCompanyBalance(companyBalance);
                        logCompanyBalanceCashFlow.setTblBankAccountByIdreceiverCbbankAccount(null);
                    } else {  //minus balance
                        logCompanyBalanceCashFlow.setTblCompanyBalanceByIdsenderCompanyBalance(companyBalance);
                        logCompanyBalanceCashFlow.setTblBankAccountByIdsenderCbbankAccount(null);
                        logCompanyBalanceCashFlow.setTblCompanyBalanceByIdreceiverCompanyBalance(null);
                        logCompanyBalanceCashFlow.setTblBankAccountByIdreceiverCbbankAccount(null);
                    }
                    logCompanyBalanceCashFlow.setTransferNominal(nominalUpdated);
                    logCompanyBalanceCashFlow.setHistoryDate(Timestamp.valueOf(LocalDateTime.now()));
                    logCompanyBalanceCashFlow.setTblEmployee(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                    logCompanyBalanceCashFlow.setHistoryStystemNote("Penutupan Kas Kasir, \ndengan selisih nilai kas sebesar : " + ClassFormatter.currencyFormat.format(nominalUpdated));
                    session.saveOrUpdate(logCompanyBalanceCashFlow);
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

    //not used
    @Override
    public boolean deleteOpenCloseCashierBalance(TblOpenCloseCashierBalance occb) {
        errMsg = "";
        if (ClassSession.checkUserSession()) {  //check user current session
            try {
                session = HibernateUtil.getSessionFactory().getCurrentSession();
                session.beginTransaction();
                if (occb.getRefRecordStatus().getIdstatus() == 1) {
                    //data occb
                    occb.setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                    occb.setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                    occb.setRefRecordStatus(session.find(RefRecordStatus.class, 2));
                    session.update(occb);
                    //delete all (data occb - detail)
                    Query rs = session.getNamedQuery("deleteAllTblOpenCloseCashierBalanceDetail")
                            .setParameter("idOCCB", occb.getIdopenCloseCashierBalance())
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
    public TblOpenCloseCashierBalance getDataOpenCloseCashierBalance(long id) {
        errMsg = "";
        TblOpenCloseCashierBalance data = null;
        if (ClassSession.checkUserSession()) {  //check user current session
            try {
                session = HibernateUtil.getSessionFactory().getCurrentSession();
                session.beginTransaction();
                data = (TblOpenCloseCashierBalance) session.find(TblOpenCloseCashierBalance.class, id);
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
    public List<TblOpenCloseCashierBalance> getAllDataOpenCloseCashierBalance() {
        errMsg = "";
        List<TblOpenCloseCashierBalance> list = new ArrayList<>();
        if (ClassSession.checkUserSession()) {  //check user current session
            try {
                session = HibernateUtil.getSessionFactory().getCurrentSession();
                session.beginTransaction();
                list = session.getNamedQuery("findAllTblOpenCloseCashierBalance").list();
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
    public TblOpenCloseCashierBalance getDataOpenCloseCashierBalanceByIDOpenCloseStatus(int idOpenCloseStatus) {
        errMsg = "";
        List<TblOpenCloseCashierBalance> list = new ArrayList<>();
        if (ClassSession.checkUserSession()) {  //check user current session
            try {
                session = HibernateUtil.getSessionFactory().getCurrentSession();
                session.beginTransaction();
                list = session.getNamedQuery("findAllTblOpenCloseCashierBalanceByIDOpenCloseStatus")
                        .setParameter("idOpenCloseStatus", idOpenCloseStatus)
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
        return list.isEmpty() ? null : list.get(0);
    }

    //--------------------------------------------------------------------------
    @Override
    public TblOpenCloseCashierBalanceDetail getDataOpenCloseCashierBalanceDetail(long id) {
        errMsg = "";
        TblOpenCloseCashierBalanceDetail data = null;
        if (ClassSession.checkUserSession()) {  //check user current session
            try {
                session = HibernateUtil.getSessionFactory().getCurrentSession();
                session.beginTransaction();
                data = (TblOpenCloseCashierBalanceDetail) session.find(TblOpenCloseCashierBalanceDetail.class, id);
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
    public List<TblOpenCloseCashierBalanceDetail> getAllDataOpenCloseCashierBalanceDetail() {
        errMsg = "";
        List<TblOpenCloseCashierBalanceDetail> list = new ArrayList<>();
        if (ClassSession.checkUserSession()) {  //check user current session
            try {
                session = HibernateUtil.getSessionFactory().getCurrentSession();
                session.beginTransaction();
                list = session.getNamedQuery("findAllTblOpenCloseCashierBalanceDetail").list();
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
    public List<TblOpenCloseCashierBalanceDetail> getAllDataOpenCloseCashierBalanceDetailByIDOpenCloseCashierBalance(long idOCCB) {
        errMsg = "";
        List<TblOpenCloseCashierBalanceDetail> list = new ArrayList<>();
        if (ClassSession.checkUserSession()) {  //check user current session
            try {
                session = HibernateUtil.getSessionFactory().getCurrentSession();
                session.beginTransaction();
                list = session.getNamedQuery("findAllTblOpenCloseCashierBalanceDetailByIDOpenCloseCashierBalance")
                        .setParameter("idOCCB", idOCCB)
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
    public List<TblEmployee> getAllDataEmployee() {
        errMsg = "";
        List<TblEmployee> list = new ArrayList<>();
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
                errMsg = e.getMessage();
            } finally {
                //session.close();
            }
        }
        return list;
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

    //--------------------------------------------------------------------------
    @Override
    public RefOpenCloseCashierBalanceStatus getDataOpenCloseCashierBalanceStatus(int id) {
        errMsg = "";
        RefOpenCloseCashierBalanceStatus data = null;
        if (ClassSession.checkUserSession()) {  //check user current session
            try {
                session = HibernateUtil.getSessionFactory().getCurrentSession();
                session.beginTransaction();
                data = (RefOpenCloseCashierBalanceStatus) session.find(RefOpenCloseCashierBalanceStatus.class, id);
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
    public TblReservationPayment getDataReservationPayment(long id) {
        errMsg = "";
        TblReservationPayment data = null;
        if (ClassSession.checkUserSession()) {  //check user current session
            try {
                session = HibernateUtil.getSessionFactory().getCurrentSession();
                session.beginTransaction();
                data = (TblReservationPayment) session.find(TblReservationPayment.class, id);
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
    public TblHotelExpenseTransaction getDataHotelExpenseTransaction(long id) {
        errMsg = "";
        TblHotelExpenseTransaction data = null;
        if (ClassSession.checkUserSession()) {  //check user current session
            try {
                session = HibernateUtil.getSessionFactory().getCurrentSession();
                session.beginTransaction();
                data = (TblHotelExpenseTransaction) session.find(TblHotelExpenseTransaction.class, id);
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
    public LogCompanyBalanceCashFlow getDataLogCompanyBalanceCashFlow(long id) {
        errMsg = "";
        LogCompanyBalanceCashFlow data = null;
        if (ClassSession.checkUserSession()) {  //check user current session
            try {
                session = HibernateUtil.getSessionFactory().getCurrentSession();
                session.beginTransaction();
                data = (LogCompanyBalanceCashFlow) session.find(LogCompanyBalanceCashFlow.class, id);
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
    public TblCompanyBalance getDataCompanyBalance(long id) {
        errMsg = "";
        TblCompanyBalance data = null;
        if (ClassSession.checkUserSession()) {  //check user current session
            try {
                session = HibernateUtil.getSessionFactory().getCurrentSession();
                session.beginTransaction();
                data = (TblCompanyBalance) session.find(TblCompanyBalance.class, id);
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
    public RefFinanceTransactionPaymentType getDataFinanceTransactionPaymentType(int id) {
        errMsg = "";
        RefFinanceTransactionPaymentType data = null;
        if (ClassSession.checkUserSession()) {  //check user current session
            try {
                session = HibernateUtil.getSessionFactory().getCurrentSession();
                session.beginTransaction();
                data = (RefFinanceTransactionPaymentType) session.find(RefFinanceTransactionPaymentType.class, id);
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
    public List<TblHotelExpenseTransactionDetail> getAllDataHotelExpenseTransactionDetailByIDHotelExpenseTransaction(long idHET) {
        errMsg = "";
        List<TblHotelExpenseTransactionDetail> list = new ArrayList<>();
        if (ClassSession.checkUserSession()) {  //check user current session
            try {
                session = HibernateUtil.getSessionFactory().getCurrentSession();
                session.beginTransaction();
                list = session.getNamedQuery("findAllTblHotelExpenseTransactionDetailByIDHotelExpenseTransaction")
                        .setParameter("idHET", idHET)
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
    public String getErrorMessage() {
        return errMsg;
    }

}
