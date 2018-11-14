/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hotelfx.persistence.service;

import hotelfx.helper.ClassCoder;
import hotelfx.helper.ClassFormatter;
import hotelfx.helper.ClassSession;
import hotelfx.persistence.HibernateUtil;
import hotelfx.persistence.model.LogCompanyBalanceCashFlow;
import hotelfx.persistence.model.RefFinanceTransactionPaymentType;
import hotelfx.persistence.model.RefRecordStatus;
import hotelfx.persistence.model.SysDataHardCode;
import hotelfx.persistence.model.TblBank;
import hotelfx.persistence.model.TblBankAccount;
import hotelfx.persistence.model.TblCompanyBalance;
import hotelfx.persistence.model.TblCompanyBalanceBankAccount;
import hotelfx.persistence.model.TblEmployee;
import hotelfx.persistence.model.TblHotelExpenseTransaction;
import hotelfx.persistence.model.TblHotelExpenseTransactionDetail;
import hotelfx.persistence.model.TblOpenCloseCashierBalance;
import hotelfx.persistence.model.TblOpenCloseCashierBalanceDetail;
import hotelfx.persistence.model.TblPeople;
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
public class FExpenseTransactionManagerImpl implements FExpenseTransactionManager {

    private Session session;

    private String errMsg;

    public FExpenseTransactionManagerImpl() {

    }

    @Override
    public TblHotelExpenseTransaction insertDataHotelExpenseTransaction(TblHotelExpenseTransaction het,
            List<TblHotelExpenseTransactionDetail> hetDetails) {
        errMsg = "";
        TblHotelExpenseTransaction tblHotelExpenseTransaction = het;
        if (ClassSession.checkUserSession()) {  //check user current session
            try {
                session = HibernateUtil.getSessionFactory().getCurrentSession();
                session.beginTransaction();
                //data hotel expense transaction - bank account (receiver) - transfer
                if (tblHotelExpenseTransaction.getTblBankAccountByIdbankAccountReceiver() != null) {
                    tblHotelExpenseTransaction.getTblBankAccountByIdbankAccountReceiver().setCreateDate(Timestamp.valueOf(LocalDateTime.now()));
                    tblHotelExpenseTransaction.getTblBankAccountByIdbankAccountReceiver().setTblEmployeeByCreateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                    tblHotelExpenseTransaction.getTblBankAccountByIdbankAccountReceiver().setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                    tblHotelExpenseTransaction.getTblBankAccountByIdbankAccountReceiver().setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                    tblHotelExpenseTransaction.getTblBankAccountByIdbankAccountReceiver().setRefRecordStatus(session.find(RefRecordStatus.class, 1));
                    session.saveOrUpdate(tblHotelExpenseTransaction.getTblBankAccountByIdbankAccountReceiver());
                }
                //data hotel expense transaction
                tblHotelExpenseTransaction.setCodeExpenseTransaction(ClassCoder.getCode("Hotel Expense Transaction", session));
                tblHotelExpenseTransaction.setCreateDate(Timestamp.valueOf(LocalDateTime.now()));
                tblHotelExpenseTransaction.setTblEmployeeByCreateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                tblHotelExpenseTransaction.setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                tblHotelExpenseTransaction.setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                tblHotelExpenseTransaction.setRefRecordStatus(session.find(RefRecordStatus.class, 1));
                session.saveOrUpdate(tblHotelExpenseTransaction);
                BigDecimal total = new BigDecimal("0");
                //data detail
                for (TblHotelExpenseTransactionDetail hetDetail : hetDetails) {
                    total = total.add(hetDetail.getItemCost().multiply(hetDetail.getItemQuantity()));
                    hetDetail.setCreateDate(Timestamp.valueOf(LocalDateTime.now()));
                    hetDetail.setTblEmployeeByCreateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                    hetDetail.setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                    hetDetail.setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                    hetDetail.setRefRecordStatus(session.find(RefRecordStatus.class, 1));
                    session.saveOrUpdate(hetDetail);
                }
                //data company balance : minus (updated)
                TblCompanyBalance dataBalance = session.find(TblCompanyBalance.class, het.getTblCompanyBalance().getIdbalance());
                dataBalance.setBalanceNominal(dataBalance.getBalanceNominal().subtract(total));
                dataBalance.setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                dataBalance.setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                session.update(dataBalance);
                //data company balance - bank account : minus (updated)
                if (het.getTblBankAccountByIdbankAccount() != null) {
                    TblCompanyBalanceBankAccount companyBalanceBankAccount = getCompanyBalanceBankAccountByIDCompanyBalanceAndIDBankAccount(het.getTblCompanyBalance().getIdbalance(),
                            het.getTblBankAccountByIdbankAccount().getIdbankAccount());
                    companyBalanceBankAccount.setCompanyBalanceBankAccountNominal(companyBalanceBankAccount.getCompanyBalanceBankAccountNominal().subtract(total));
                    companyBalanceBankAccount.setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                    companyBalanceBankAccount.setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                    session.update(companyBalanceBankAccount);
                    //data company balance (cash-flow log)
                    LogCompanyBalanceCashFlow logCompanyBalanceCashFlow = new LogCompanyBalanceCashFlow();
                    logCompanyBalanceCashFlow.setTblCompanyBalanceByIdsenderCompanyBalance(dataBalance);
                    logCompanyBalanceCashFlow.setTblBankAccountByIdsenderCbbankAccount(companyBalanceBankAccount.getTblBankAccount());
                    logCompanyBalanceCashFlow.setTblCompanyBalanceByIdreceiverCompanyBalance(null);
                    logCompanyBalanceCashFlow.setTblBankAccountByIdreceiverCbbankAccount(het.getTblBankAccountByIdbankAccount());
                    logCompanyBalanceCashFlow.setTransferNominal(total);
                    logCompanyBalanceCashFlow.setHistoryDate(Timestamp.valueOf(LocalDateTime.now()));
                    logCompanyBalanceCashFlow.setTblEmployee(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                    logCompanyBalanceCashFlow.setHistoryStystemNote("Pengeluaran Hotel, \nsebesar : "
                            + ClassFormatter.currencyFormat.format(total) + " ("
                            + "Transfer" + ")");
                    logCompanyBalanceCashFlow.setHistoryNote(het.getExpenseTransactionNote());
                    session.saveOrUpdate(logCompanyBalanceCashFlow);
                } else {
                    //data company balance (cash-flow log)
                    LogCompanyBalanceCashFlow logCompanyBalanceCashFlow = new LogCompanyBalanceCashFlow();
                    logCompanyBalanceCashFlow.setTblCompanyBalanceByIdsenderCompanyBalance(dataBalance);
                    logCompanyBalanceCashFlow.setTblBankAccountByIdsenderCbbankAccount(null);
                    logCompanyBalanceCashFlow.setTblCompanyBalanceByIdreceiverCompanyBalance(null);
                    logCompanyBalanceCashFlow.setTblBankAccountByIdreceiverCbbankAccount(null);
                    logCompanyBalanceCashFlow.setTransferNominal(total);
                    logCompanyBalanceCashFlow.setHistoryDate(Timestamp.valueOf(LocalDateTime.now()));
                    logCompanyBalanceCashFlow.setTblEmployee(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                    logCompanyBalanceCashFlow.setHistoryStystemNote("Pengeluaran Hotel, \nsebesar : "
                            + ClassFormatter.currencyFormat.format(total) + " ("
                            + "Tunai" + ")");
                    logCompanyBalanceCashFlow.setHistoryNote(het.getExpenseTransactionNote());
                    session.saveOrUpdate(logCompanyBalanceCashFlow);
                }
                //open close company balance - detail (cashier)
                if ((tblHotelExpenseTransaction.getTblCompanyBalance() != null
                        && tblHotelExpenseTransaction.getTblCompanyBalance().getIdbalance() == 3)) {    //Kas Kasir = '3'
                    TblOpenCloseCashierBalance dataOpenCloseCashierBalance = getDataOpenCloseCashierBalanceIsActive();
                    if (dataOpenCloseCashierBalance != null) {
                        TblOpenCloseCashierBalanceDetail dataOpenCloseCashierBalanceDetail = new TblOpenCloseCashierBalanceDetail();
                        dataOpenCloseCashierBalanceDetail.setTblOpenCloseCashierBalance(dataOpenCloseCashierBalance);
                        dataOpenCloseCashierBalanceDetail.setTblHotelExpenseTransaction(tblHotelExpenseTransaction);
                        dataOpenCloseCashierBalanceDetail.setDetailData(getDetailData(tblHotelExpenseTransaction, total));
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
                return null;
            } finally {
                //session.close();
            }
        } else {
            return null;
        }
        return tblHotelExpenseTransaction;
    }

    private TblCompanyBalanceBankAccount getCompanyBalanceBankAccountByIDCompanyBalanceAndIDBankAccount(long idCompanyBalance, long idBankAccount) {
        List<TblCompanyBalanceBankAccount> list = session.getNamedQuery("findAllTblCompanyBalanceBankAccountByIDCompanyBalanceAndIDBankAccount")
                .setParameter("idCompanyBalance", idCompanyBalance)
                .setParameter("idBankAccount", idBankAccount)
                .list();
        return list.isEmpty() ? null : list.get(0);
    }

    private TblOpenCloseCashierBalance getDataOpenCloseCashierBalanceIsActive() {
        List<TblOpenCloseCashierBalance> list = session.getNamedQuery("findAllTblOpenCloseCashierBalanceByIDOpenCloseStatus")
                .setParameter("idOpenCloseStatus", 1) //'Aktif'
                .list();
        return list.isEmpty() ? null : list.get(0);
    }

    private String getDetailData(TblHotelExpenseTransaction tblHotelExpenseTransaction,
            BigDecimal total) {
        String result = "";
        if (tblHotelExpenseTransaction != null) {
            result += "Pengurangan Kas dari Pengeluaran Harian No. "
                    + tblHotelExpenseTransaction.getCodeExpenseTransaction()
                    + "\n";
            result += "Nominal pengeluaran harian : " + ClassFormatter.currencyFormat.cFormat(total);
        }
        return result;
    }

    @Override
    public boolean updateDataHotelExpenseTransaction(TblHotelExpenseTransaction het,
            List<TblHotelExpenseTransactionDetail> hetDetails) {
        errMsg = "";
        if (ClassSession.checkUserSession()) {  //check user current session
            try {
                session = HibernateUtil.getSessionFactory().getCurrentSession();
                session.beginTransaction();
                //nominal transaction (new)
                BigDecimal total = new BigDecimal("0");
                for (TblHotelExpenseTransactionDetail hetDetail : hetDetails) {
                    total = total.add(hetDetail.getItemCost().multiply(hetDetail.getItemQuantity()));
                }
                //data hotel expense trsansaction (old)
                TblHotelExpenseTransaction hetOld = session.get(TblHotelExpenseTransaction.class, het.getIdexpenseTransaction());
                //data hotel expense trsansaction - detail (old)
                List<TblHotelExpenseTransactionDetail> hetOldDetails = session.getNamedQuery("findAllTblHotelExpenseTransactionDetailByIDHotelExpenseTransaction")
                        .setParameter("idHET", hetOld.getIdexpenseTransaction())
                        .list();
                //nominal transaction (old)
                BigDecimal oldNominalTransaction = new BigDecimal("0");
                for (TblHotelExpenseTransactionDetail hetOldDetail : hetOldDetails) {
                    oldNominalTransaction = oldNominalTransaction.add(hetOldDetail.getItemCost().multiply(hetOldDetail.getItemQuantity()));
                }
                if (checkIsCompanyBalanceSame(het, hetOld)) {
                    if (checkIsCompanyBalanceBankAccountSame(het, hetOld)) {
                        //update company balance
                        TblCompanyBalance dataBalance = session.find(TblCompanyBalance.class, hetOld.getTblCompanyBalance().getIdbalance());
                        dataBalance.setBalanceNominal(dataBalance.getBalanceNominal().add(oldNominalTransaction).subtract(total));
                        dataBalance.setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                        dataBalance.setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                        session.update(dataBalance);
                        //update company balance - bank account
                        if (hetOld.getTblBankAccountByIdbankAccount() != null) {
                            TblCompanyBalanceBankAccount companyBalanceBankAccount = getCompanyBalanceBankAccountByIDCompanyBalanceAndIDBankAccount(hetOld.getTblCompanyBalance().getIdbalance(),
                                    hetOld.getTblBankAccountByIdbankAccount().getIdbankAccount());
                            companyBalanceBankAccount.setCompanyBalanceBankAccountNominal(companyBalanceBankAccount.getCompanyBalanceBankAccountNominal().add(oldNominalTransaction).subtract(total));
                            companyBalanceBankAccount.setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                            companyBalanceBankAccount.setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                            session.update(companyBalanceBankAccount);
                        }
                    } else {
                        //update company balance
                        TblCompanyBalance dataBalance = session.find(TblCompanyBalance.class, hetOld.getTblCompanyBalance().getIdbalance());
                        dataBalance.setBalanceNominal(dataBalance.getBalanceNominal().add(oldNominalTransaction).subtract(total));
                        dataBalance.setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                        dataBalance.setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                        session.update(dataBalance);
                        //update old company balance - bank account (plus)
                        if (hetOld.getTblBankAccountByIdbankAccount() != null) {
                            TblCompanyBalanceBankAccount oldCompanyBalanceBankAccount = getCompanyBalanceBankAccountByIDCompanyBalanceAndIDBankAccount(hetOld.getTblCompanyBalance().getIdbalance(),
                                    hetOld.getTblBankAccountByIdbankAccount().getIdbankAccount());
                            oldCompanyBalanceBankAccount.setCompanyBalanceBankAccountNominal(oldCompanyBalanceBankAccount.getCompanyBalanceBankAccountNominal().add(oldNominalTransaction));
                            oldCompanyBalanceBankAccount.setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                            oldCompanyBalanceBankAccount.setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                            session.update(oldCompanyBalanceBankAccount);
                        }
                        //update new company balance - bank account (minus)
                        if (het.getTblBankAccountByIdbankAccount() != null) {
                            TblCompanyBalanceBankAccount newCompanyBalanceBankAccount = getCompanyBalanceBankAccountByIDCompanyBalanceAndIDBankAccount(het.getTblCompanyBalance().getIdbalance(),
                                    het.getTblBankAccountByIdbankAccount().getIdbankAccount());
                            newCompanyBalanceBankAccount.setCompanyBalanceBankAccountNominal(newCompanyBalanceBankAccount.getCompanyBalanceBankAccountNominal().subtract(total));
                            newCompanyBalanceBankAccount.setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                            newCompanyBalanceBankAccount.setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                            session.update(newCompanyBalanceBankAccount);
                        }
                    }
                } else {
                    //update old company balance (plus)
                    TblCompanyBalance dataOldBalance = session.find(TblCompanyBalance.class, hetOld.getTblCompanyBalance().getIdbalance());
                    dataOldBalance.setBalanceNominal(dataOldBalance.getBalanceNominal().add(oldNominalTransaction));
                    dataOldBalance.setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                    dataOldBalance.setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                    session.update(dataOldBalance);
                    //update old company balance - bank account (plus)
                    if (hetOld.getTblBankAccountByIdbankAccount() != null) {
                        TblCompanyBalanceBankAccount oldCompanyBalanceBankAccount = getCompanyBalanceBankAccountByIDCompanyBalanceAndIDBankAccount(hetOld.getTblCompanyBalance().getIdbalance(),
                                hetOld.getTblBankAccountByIdbankAccount().getIdbankAccount());
                        oldCompanyBalanceBankAccount.setCompanyBalanceBankAccountNominal(oldCompanyBalanceBankAccount.getCompanyBalanceBankAccountNominal().add(oldNominalTransaction));
                        oldCompanyBalanceBankAccount.setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                        oldCompanyBalanceBankAccount.setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                        session.update(oldCompanyBalanceBankAccount);
                    }
                    //update new company balance (minus)
                    TblCompanyBalance dataNewBalance = session.find(TblCompanyBalance.class, het.getTblCompanyBalance().getIdbalance());
                    dataNewBalance.setBalanceNominal(dataNewBalance.getBalanceNominal().subtract(total));
                    dataNewBalance.setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                    dataNewBalance.setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                    session.update(dataNewBalance);
                    //update new company balance - bank account (minus)
                    if (het.getTblBankAccountByIdbankAccount() != null) {
                        TblCompanyBalanceBankAccount newCompanyBalanceBankAccount = getCompanyBalanceBankAccountByIDCompanyBalanceAndIDBankAccount(het.getTblCompanyBalance().getIdbalance(),
                                het.getTblBankAccountByIdbankAccount().getIdbankAccount());
                        newCompanyBalanceBankAccount.setCompanyBalanceBankAccountNominal(newCompanyBalanceBankAccount.getCompanyBalanceBankAccountNominal().subtract(total));
                        newCompanyBalanceBankAccount.setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                        newCompanyBalanceBankAccount.setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                        session.update(newCompanyBalanceBankAccount);
                    }
                }
                //evict data
                for (TblHotelExpenseTransactionDetail hetOldDetail : hetOldDetails) {
                    session.evict(hetOldDetail);
                }
                session.evict(hetOld);
                //data hotel expense transaction - bank account (receiver) - transfer
                if (het.getTblBankAccountByIdbankAccountReceiver() != null) {
                    het.getTblBankAccountByIdbankAccountReceiver().setCreateDate(Timestamp.valueOf(LocalDateTime.now()));
                    het.getTblBankAccountByIdbankAccountReceiver().setTblEmployeeByCreateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                    het.getTblBankAccountByIdbankAccountReceiver().setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                    het.getTblBankAccountByIdbankAccountReceiver().setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                    het.getTblBankAccountByIdbankAccountReceiver().setRefRecordStatus(session.find(RefRecordStatus.class, 1));
                    session.saveOrUpdate(het.getTblBankAccountByIdbankAccountReceiver());
                }
                //data hotel expense transaction
                het.setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                het.setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                session.update(het);
                //delete all (data detail)
                Query rs = session.getNamedQuery("deleteAllTblHotelExpenseTransactionDetail")
                        .setParameter("idHotelExpenseTransaction", het.getIdexpenseTransaction())
                        .setParameter("idEmployee", ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee());
                errMsg = (String) rs.uniqueResult();
                if (!errMsg.equals("")) {
                    if (session.getTransaction().isActive()) {
                        session.getTransaction().rollback();
                    }
                    return false;
                }
                //insert all data detail
                for (TblHotelExpenseTransactionDetail hetDetail : hetDetails) {
                    hetDetail.setCreateDate(Timestamp.valueOf(LocalDateTime.now()));
                    hetDetail.setTblEmployeeByCreateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                    hetDetail.setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                    hetDetail.setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                    hetDetail.setRefRecordStatus(session.find(RefRecordStatus.class, 1));
                    session.saveOrUpdate(hetDetail);
                }
                //open close company balance - detail (cashier)
                if ((het.getTblCompanyBalance() != null
                        && het.getTblCompanyBalance().getIdbalance() == 3)) {    //Kas Kasir = '3'
                    TblOpenCloseCashierBalance dataOpenCloseCashierBalance = getDataOpenCloseCashierBalanceIsActive();
                    if (dataOpenCloseCashierBalance != null) {
                        TblOpenCloseCashierBalanceDetail dataOpenCloseCashierBalanceDetail = getAllDataOpenCloseCashierBalanceDetailByIDOpenCloseCashierBalanceAndIDHotelExpenseTransaction(
                                dataOpenCloseCashierBalance.getIdopenCloseCashierBalance(),
                                het.getIdexpenseTransaction());
                        if (dataOpenCloseCashierBalanceDetail != null) {
                            dataOpenCloseCashierBalanceDetail.setDetailData(getDetailData(het, total));
                            dataOpenCloseCashierBalanceDetail.setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                            dataOpenCloseCashierBalanceDetail.setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                            session.update(dataOpenCloseCashierBalanceDetail);
                        }
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

    private boolean checkIsCompanyBalanceSame(TblHotelExpenseTransaction het, TblHotelExpenseTransaction hetOld) {
        return het.getTblCompanyBalance().getIdbalance() == hetOld.getTblCompanyBalance().getIdbalance();
    }

    private boolean checkIsCompanyBalanceBankAccountSame(TblHotelExpenseTransaction het, TblHotelExpenseTransaction hetOld) {
        return (het.getTblCompanyBalance().getIdbalance() == hetOld.getTblCompanyBalance().getIdbalance())
                && ((het.getTblBankAccountByIdbankAccount() == null && hetOld.getTblBankAccountByIdbankAccount() == null)
                || (het.getTblBankAccountByIdbankAccount().getIdbankAccount() == hetOld.getTblBankAccountByIdbankAccount().getIdbankAccount()));
    }

    private TblOpenCloseCashierBalanceDetail getAllDataOpenCloseCashierBalanceDetailByIDOpenCloseCashierBalanceAndIDHotelExpenseTransaction(long idOCCB,
            long idHET) {
        List<TblOpenCloseCashierBalanceDetail> list = session.getNamedQuery("findAllTblOpenCloseCashierBalanceDetailByIDOpenCloseCashierBalance")
                .setParameter("idOCCB", idOCCB)
                .list();
        for (TblOpenCloseCashierBalanceDetail data : list) {
            if (data.getTblHotelExpenseTransaction() != null
                    && data.getTblHotelExpenseTransaction().getIdexpenseTransaction() == idHET) {
                return data;
            }
        }
        return null;
    }

//    private void updateOldCompanyBalance(TblHotelExpenseTransaction het) {
//        //data hotel expense trsansaction (old)
//        TblHotelExpenseTransaction hetOld = session.get(TblHotelExpenseTransaction.class, het.getIdexpenseTransaction());
//        List<TblHotelExpenseTransactionDetail> hetDetails = session.getNamedQuery("findAllTblHotelExpenseTransactionDetailByIDHotelExpenseTransaction")
//                .setParameter("idHET", hetOld.getIdexpenseTransaction())
//                .list();
//        BigDecimal oldNominalTransaction = new BigDecimal("0");
//        for (TblHotelExpenseTransactionDetail hetDetail : hetDetails) {
//            oldNominalTransaction = oldNominalTransaction.add(hetDetail.getItemCost().multiply(hetDetail.getItemQuantity()));
//        }
//        System.out.println("old nominal " + oldNominalTransaction);
//        //update old company balance (plus)
//        TblCompanyBalance dataBalance = session.find(TblCompanyBalance.class, hetOld.getTblCompanyBalance().getIdbalance());
//        dataBalance.setBalanceNominal(dataBalance.getBalanceNominal() + (long) oldNominalTransaction);
//        dataBalance.setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
//        dataBalance.setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
//        session.update(dataBalance);
//        //evict data
//        session.evict(dataBalance);
//        //update old company balance - bank account
//        if (hetOld.getTblBankAccount() != null) {
//            TblCompanyBalanceBankAccount companyBalanceBankAccount = getCompanyBalanceBankAccountByIDCompanyBalanceAndIDBankAccount(hetOld.getTblCompanyBalance().getIdbalance(),
//                    hetOld.getTblBankAccount().getIdbankAccount());
//            companyBalanceBankAccount.setCompanyBalanceBankAccountNominal(companyBalanceBankAccount.getCompanyBalanceBankAccountNominal() + (long) oldNominalTransaction);
//            companyBalanceBankAccount.setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
//            companyBalanceBankAccount.setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
//            session.update(companyBalanceBankAccount);
//            System.out.println("sdkadald");
//            //evict data
//            session.evict(companyBalanceBankAccount);
//        }
//        //evict data
//        for (TblHotelExpenseTransactionDetail hetDetail : hetDetails) {
//            session.evict(hetDetail);
//        }
//        session.evict(hetOld);
//    }
    @Override
    public boolean deleteDataHotelExpenseTransaction(TblHotelExpenseTransaction het) {
        errMsg = "";
        if (ClassSession.checkUserSession()) {  //check user current session
            try {
                session = HibernateUtil.getSessionFactory().getCurrentSession();
                session.beginTransaction();
                if (het.getRefRecordStatus().getIdstatus() == 1) {
                    //data hotel expense trsansaction - detail
                    List<TblHotelExpenseTransactionDetail> hetDetails = session.getNamedQuery("findAllTblHotelExpenseTransactionDetailByIDHotelExpenseTransaction")
                            .setParameter("idHET", het.getIdexpenseTransaction())
                            .list();
                    //nominal transaction
                    BigDecimal oldNominalTransaction = new BigDecimal("0");
                    for (TblHotelExpenseTransactionDetail hetDetail : hetDetails) {
                        oldNominalTransaction = oldNominalTransaction.add(hetDetail.getItemCost().multiply(hetDetail.getItemQuantity()));
                    }
                    //update company balance (plus)
                    TblCompanyBalance dataBalance = session.find(TblCompanyBalance.class, het.getTblCompanyBalance().getIdbalance());
                    dataBalance.setBalanceNominal(dataBalance.getBalanceNominal().add(oldNominalTransaction));
                    dataBalance.setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                    dataBalance.setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                    session.update(dataBalance);
                    //update company balance - bank account (plus)
                    if (het.getTblBankAccountByIdbankAccount() != null) {
                        TblCompanyBalanceBankAccount companyBalanceBankAccount = getCompanyBalanceBankAccountByIDCompanyBalanceAndIDBankAccount(het.getTblCompanyBalance().getIdbalance(),
                                het.getTblBankAccountByIdbankAccount().getIdbankAccount());
                        companyBalanceBankAccount.setCompanyBalanceBankAccountNominal(companyBalanceBankAccount.getCompanyBalanceBankAccountNominal().add(oldNominalTransaction));
                        companyBalanceBankAccount.setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                        companyBalanceBankAccount.setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                        session.update(companyBalanceBankAccount);
                    }
                    //data hotel expense  transaction
                    het.setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                    het.setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                    het.setRefRecordStatus(session.find(RefRecordStatus.class, 2));
                    session.update(het);
                    //delete all (data detail)
                    Query rs = session.getNamedQuery("deleteAllTblHotelExpenseTransactionDetail")
                            .setParameter("idHotelExpenseTransaction", het.getIdexpenseTransaction())
                            .setParameter("idEmployee", ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee());
                    errMsg = (String) rs.uniqueResult();
                    if (!errMsg.equals("")) {
                        if (session.getTransaction().isActive()) {
                            session.getTransaction().rollback();
                        }
                        return false;
                    }
                    //open close company balance - detail (cashier)
                    if ((het.getTblCompanyBalance() != null
                            && het.getTblCompanyBalance().getIdbalance() == 3)) {    //Kas Kasir = '3'
                        TblOpenCloseCashierBalance dataOpenCloseCashierBalance = getDataOpenCloseCashierBalanceIsActive();
                        if (dataOpenCloseCashierBalance != null) {
                            TblOpenCloseCashierBalanceDetail dataOpenCloseCashierBalanceDetail = getAllDataOpenCloseCashierBalanceDetailByIDOpenCloseCashierBalanceAndIDHotelExpenseTransaction(
                                    dataOpenCloseCashierBalance.getIdopenCloseCashierBalance(),
                                    het.getIdexpenseTransaction());
                            if (dataOpenCloseCashierBalanceDetail != null) {
                                dataOpenCloseCashierBalanceDetail.setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                                dataOpenCloseCashierBalanceDetail.setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                                dataOpenCloseCashierBalanceDetail.setRefRecordStatus(session.find(RefRecordStatus.class, 2));
                                session.update(dataOpenCloseCashierBalanceDetail);
                            }
                        }
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
    public List<TblHotelExpenseTransaction> getAllDataHotelExpenseTransaction() {
        errMsg = "";
        List<TblHotelExpenseTransaction> list = new ArrayList<>();
        if (ClassSession.checkUserSession()) {  //check user current session
            try {
                session = HibernateUtil.getSessionFactory().getCurrentSession();
                session.beginTransaction();
                list = session.getNamedQuery("findAllTblHotelExpenseTransaction").list();
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
    public TblHotelExpenseTransactionDetail getDataHotelExpenseTransactionDetail(long id) {
        errMsg = "";
        TblHotelExpenseTransactionDetail data = null;
        if (ClassSession.checkUserSession()) {  //check user current session
            try {
                session = HibernateUtil.getSessionFactory().getCurrentSession();
                session.beginTransaction();
                data = (TblHotelExpenseTransactionDetail) session.find(TblHotelExpenseTransactionDetail.class, id);
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
    public List<TblHotelExpenseTransactionDetail> getAllDataHotelExpenseTransactionDetail() {
        errMsg = "";
        List<TblHotelExpenseTransactionDetail> list = new ArrayList<>();
        if (ClassSession.checkUserSession()) {  //check user current session
            try {
                session = HibernateUtil.getSessionFactory().getCurrentSession();
                session.beginTransaction();
                list = session.getNamedQuery("findAllTblHotelExpenseTransactionDetail").list();
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

    @Override
    public List<TblCompanyBalance> getAllDataCompanyBalance() {
        errMsg = "";
        List<TblCompanyBalance> list = new ArrayList<>();
        if (ClassSession.checkUserSession()) {  //check user current session
            try {
                session = HibernateUtil.getSessionFactory().getCurrentSession();
                session.beginTransaction();
                list = session.getNamedQuery("findAllTblCompanyBalance").list();
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
    public TblCompanyBalanceBankAccount getDataCompanyBalanceBankAccountByIDCompanyBalanceAndIDBankAccount(long idCompanyBalance,
            long idBankAccount) {
        errMsg = "";
        List<TblCompanyBalanceBankAccount> list = new ArrayList<>();
        if (ClassSession.checkUserSession()) {  //check user current session
            try {
                session = HibernateUtil.getSessionFactory().getCurrentSession();
                session.beginTransaction();
                list = session.getNamedQuery("findAllTblCompanyBalanceBankAccountByIDCompanyBalanceAndIDBankAccount")
                        .setParameter("idCompanyBalance", idCompanyBalance)
                        .setParameter("idBankAccount", idBankAccount)
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

    @Override
    public List<TblCompanyBalanceBankAccount> getAllDataCompanyBalanceBankAccountByIDCompanyBalance(long idCompanyBalance) {
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
    public TblBankAccount getDataBankAccount(long id) {
        errMsg = "";
        TblBankAccount data = null;
        if (ClassSession.checkUserSession()) {  //check user current session
            try {
                session = HibernateUtil.getSessionFactory().getCurrentSession();
                session.beginTransaction();
                data = (TblBankAccount) session.find(TblBankAccount.class, id);
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
    public RefFinanceTransactionPaymentType getDataFinanceTransactionType(int id){
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

    //--------------------------------------------------------------------------
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
    public TblOpenCloseCashierBalanceDetail getDataOpenCloseCashierBalanceDetailByIDHotelExpenseTransaction(long idHET) {
        errMsg = "";
        List<TblOpenCloseCashierBalanceDetail> list = new ArrayList<>();
        if (ClassSession.checkUserSession()) {  //check user current session
            try {
                session = HibernateUtil.getSessionFactory().getCurrentSession();
                session.beginTransaction();
                list = session.getNamedQuery("findAllTblOpenCloseCashierBalanceDetailByIDHotelExpenseTransaction")
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
        return list.isEmpty() ? null : list.get(0);
    }

    //--------------------------------------------------------------------------
    @Override
    public String getErrorMessage() {
        return errMsg;
    }

}
