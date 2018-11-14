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
import hotelfx.persistence.model.RefBankAccountHolderStatus;
import hotelfx.persistence.model.RefFinanceTransactionPaymentType;
import hotelfx.persistence.model.RefFinanceTransactionStatus;
import hotelfx.persistence.model.RefFinanceTransactionType;
import hotelfx.persistence.model.RefHotelInvoiceType;
import hotelfx.persistence.model.RefHotelPayableType;
import hotelfx.persistence.model.RefRecordStatus;
import hotelfx.persistence.model.SysDataHardCode;
import hotelfx.persistence.model.TblBank;
import hotelfx.persistence.model.TblBankAccount;
import hotelfx.persistence.model.TblCompanyBalance;
import hotelfx.persistence.model.TblCompanyBalanceBankAccount;
import hotelfx.persistence.model.TblEmployee;
import hotelfx.persistence.model.TblHotelFinanceTransaction;
import hotelfx.persistence.model.TblHotelFinanceTransactionHotelPayable;
import hotelfx.persistence.model.TblHotelFinanceTransactionWithCash;
import hotelfx.persistence.model.TblHotelFinanceTransactionWithCekGiro;
import hotelfx.persistence.model.TblHotelFinanceTransactionWithTransfer;
import hotelfx.persistence.model.TblHotelInvoice;
import hotelfx.persistence.model.TblHotelPayable;
import hotelfx.persistence.model.TblReservationAdditionalService;
import hotelfx.persistence.model.TblResto;
import hotelfx.persistence.model.TblRestoBankAccount;
import hotelfx.persistence.model.TblRoomService;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import org.hibernate.Session;

/**
 *
 * @author ABC-Programmer
 */
public class FRestoManagerImpl implements FRestoManager {

    private Session session;

    private String errMsg;

    public FRestoManagerImpl() {

    }

    @Override
    public TblResto getDataResto(long id){
        errMsg = "";
        TblResto data = null;
        if (ClassSession.checkUserSession()) {  //check user current session
            try {
                session = HibernateUtil.getSessionFactory().getCurrentSession();
                session.beginTransaction();
                data = (TblResto) session.find(TblResto.class, id);
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
    public List<TblResto> getAllDataResto(){
        errMsg = "";
        List<TblResto> list = new ArrayList<>();
        if (ClassSession.checkUserSession()) {  //check user current session
            try {
                session = HibernateUtil.getSessionFactory().getCurrentSession();
                session.beginTransaction();
                list = session.getNamedQuery("findAllTblResto").list();
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
    public TblReservationAdditionalService getDataReservationAdditionalService(long id) {
        errMsg = "";
        TblReservationAdditionalService data = null;
        if (ClassSession.checkUserSession()) {  //check user current session
            try {
                session = HibernateUtil.getSessionFactory().getCurrentSession();
                session.beginTransaction();
                data = (TblReservationAdditionalService) session.find(TblReservationAdditionalService.class, id);
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
    public List<TblReservationAdditionalService> getAllDataReservationAdditionalService() {
        errMsg = "";
        List<TblReservationAdditionalService> list = new ArrayList<>();
        if (ClassSession.checkUserSession()) {  //check user current session
            try {
                session = HibernateUtil.getSessionFactory().getCurrentSession();
                session.beginTransaction();
                list = session.getNamedQuery("findAllTblReservationAdditionalService").list();
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
    public TblRoomService getDataRoomService(long id) {
        errMsg = "";
        TblRoomService data = null;
        if (ClassSession.checkUserSession()) {  //check user current session
            try {
                session = HibernateUtil.getSessionFactory().getCurrentSession();
                session.beginTransaction();
                data = (TblRoomService) session.find(TblRoomService.class, id);
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
    public TblHotelPayable getDataHotelPayable(long id) {
        errMsg = "";
        TblHotelPayable data = null;
        if (ClassSession.checkUserSession()) {  //check user current session
            try {
                session = HibernateUtil.getSessionFactory().getCurrentSession();
                session.beginTransaction();
                data = (TblHotelPayable) session.find(TblHotelPayable.class, id);
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
    public RefHotelPayableType getDataHotelPayableType(int id) {
        errMsg = "";
        RefHotelPayableType data = null;
        if (ClassSession.checkUserSession()) {  //check user current session
            try {
                session = HibernateUtil.getSessionFactory().getCurrentSession();
                session.beginTransaction();
                data = (RefHotelPayableType) session.find(RefHotelPayableType.class, id);
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
    public RefFinanceTransactionStatus getDataFinanceTransactionStatus(int id) {
        errMsg = "";
        RefFinanceTransactionStatus data = null;
        if (ClassSession.checkUserSession()) {  //check user current session
            try {
                session = HibernateUtil.getSessionFactory().getCurrentSession();
                session.beginTransaction();
                data = (RefFinanceTransactionStatus) session.find(RefFinanceTransactionStatus.class, id);
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
    private TblCompanyBalanceBankAccount getCompanyBalanceBankAccountByIDCompanyBalanceAndIDBankAccount(long idCompanyBalance, long idBankAccount) {
        List<TblCompanyBalanceBankAccount> list = session.getNamedQuery("findAllTblCompanyBalanceBankAccountByIDCompanyBalanceAndIDBankAccount")
                .setParameter("idCompanyBalance", idCompanyBalance)
                .setParameter("idBankAccount", idBankAccount)
                .list();
        return list.isEmpty() ? null : list.get(0);
    }

    @Override
    public TblHotelFinanceTransaction insertDataHotelFinanceTransaction(
            TblHotelFinanceTransaction hotelFinanceTransaction,
            TblHotelFinanceTransactionWithCash hotelFinanceTransactionWithCash,
            List<TblHotelFinanceTransactionHotelPayable> hfthps, 
            String restoName) {
        errMsg = "";
        TblHotelFinanceTransaction tblHotelFinanceTransaction = hotelFinanceTransaction;
        if (ClassSession.checkUserSession()) {  //check user current session
            try {
                session = HibernateUtil.getSessionFactory().getCurrentSession();
                session.beginTransaction();
                //data hotel finnace transaction
                tblHotelFinanceTransaction.setCodeTransaction(ClassCoder.getCode("Payable - Payment", session));
                tblHotelFinanceTransaction.setCreateDate(Timestamp.valueOf(LocalDateTime.now()));
                tblHotelFinanceTransaction.setTblEmployeeByCreateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                tblHotelFinanceTransaction.setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                tblHotelFinanceTransaction.setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                tblHotelFinanceTransaction.setRefRecordStatus(session.find(RefRecordStatus.class, 1));
                session.saveOrUpdate(tblHotelFinanceTransaction);
                //data hotel finance transaction - with transfer
                hotelFinanceTransactionWithCash.setCreateDate(Timestamp.valueOf(LocalDateTime.now()));
                hotelFinanceTransactionWithCash.setTblEmployeeByCreateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                hotelFinanceTransactionWithCash.setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                hotelFinanceTransactionWithCash.setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                hotelFinanceTransactionWithCash.setRefRecordStatus(session.find(RefRecordStatus.class, 1));
                session.saveOrUpdate(hotelFinanceTransactionWithCash);
                //data hotel finnace transaction - hotel payable
                for (TblHotelFinanceTransactionHotelPayable hfthp : hfthps) {
                    hfthp.setCreateDate(Timestamp.valueOf(LocalDateTime.now()));
                    hfthp.setTblEmployeeByCreateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                    hfthp.setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                    hfthp.setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                    hfthp.setRefRecordStatus(session.find(RefRecordStatus.class, 1));
                    session.saveOrUpdate(hfthp);
                    //data hotel payable
                    hfthp.getTblHotelPayable().setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                    hfthp.getTblHotelPayable().setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                    session.saveOrUpdate(hfthp.getTblHotelPayable());
                    //data reservation additonal service - resto bill
                    TblReservationAdditionalService ras = getReservationAdditionalServiceByIDHotelPayable(hfthp.getTblHotelPayable().getIdhotelPayable());
                    if (ras != null) {
                        ras.setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                        ras.setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                        session.saveOrUpdate(ras);
                    }
                }
                //@@@%%%
                //data company balance (kas) : minus/plus (updated)
                TblCompanyBalance dataBalance = session.find(TblCompanyBalance.class, hotelFinanceTransactionWithCash.getTblCompanyBalance().getIdbalance());
                if (hotelFinanceTransaction.getIsReturnTransaction()) {   //plus
                    dataBalance.setBalanceNominal(dataBalance.getBalanceNominal().add(hotelFinanceTransaction.getTransactionNominal()));
                } else {  //minus
                    dataBalance.setBalanceNominal(dataBalance.getBalanceNominal().subtract(hotelFinanceTransaction.getTransactionNominal()));
                }
                dataBalance.setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                dataBalance.setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                session.update(dataBalance);
                //data company balance (cash-flow log)
                LogCompanyBalanceCashFlow logCompanyBalanceCashFlow = new LogCompanyBalanceCashFlow();
                if (hotelFinanceTransaction.getIsReturnTransaction()) {   //in
                    logCompanyBalanceCashFlow.setTblCompanyBalanceByIdsenderCompanyBalance(null);
                    logCompanyBalanceCashFlow.setTblBankAccountByIdsenderCbbankAccount(null);
                    logCompanyBalanceCashFlow.setTblCompanyBalanceByIdreceiverCompanyBalance(dataBalance);
                    logCompanyBalanceCashFlow.setTblBankAccountByIdreceiverCbbankAccount(null);
                } else {  //out
                    logCompanyBalanceCashFlow.setTblCompanyBalanceByIdsenderCompanyBalance(dataBalance);
                    logCompanyBalanceCashFlow.setTblBankAccountByIdsenderCbbankAccount(null);
                    logCompanyBalanceCashFlow.setTblCompanyBalanceByIdreceiverCompanyBalance(null);
                    logCompanyBalanceCashFlow.setTblBankAccountByIdreceiverCbbankAccount(null);
                }
                logCompanyBalanceCashFlow.setTransferNominal(hotelFinanceTransaction.getTransactionNominal());
                logCompanyBalanceCashFlow.setHistoryDate(Timestamp.valueOf(LocalDateTime.now()));
                logCompanyBalanceCashFlow.setTblEmployee(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                if (hotelFinanceTransaction.getIsReturnTransaction()) {
                    logCompanyBalanceCashFlow.setHistoryStystemNote("Pengembalian pembayaran utang oleh " + restoName + ", \nsebesar : "
                            + ClassFormatter.currencyFormat.format(hotelFinanceTransaction.getTransactionNominal()) + " ("
                            + tblHotelFinanceTransaction.getRefFinanceTransactionPaymentType().getTypeName() + ")");
                } else {
                    logCompanyBalanceCashFlow.setHistoryStystemNote("Pembayaran utang kepada " + restoName + ", \nsebesar : "
                            + ClassFormatter.currencyFormat.format(hotelFinanceTransaction.getTransactionNominal()) + " ("
                            + tblHotelFinanceTransaction.getRefFinanceTransactionPaymentType().getTypeName() + ")");
                }
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
        return tblHotelFinanceTransaction;
    }

    @Override
    public TblHotelFinanceTransaction insertDataHotelFinanceTransaction(
            TblHotelFinanceTransaction hotelFinanceTransaction,
            TblHotelFinanceTransactionWithTransfer hotelFinanceTransactionWithTransfer,
            List<TblHotelFinanceTransactionHotelPayable> hfthps, 
            String restoName) {
        errMsg = "";
        TblHotelFinanceTransaction tblHotelFinanceTransaction = hotelFinanceTransaction;
        if (ClassSession.checkUserSession()) {  //check user current session
            try {
                session = HibernateUtil.getSessionFactory().getCurrentSession();
                session.beginTransaction();
                //data hotel finnace transaction
                tblHotelFinanceTransaction.setCodeTransaction(ClassCoder.getCode("Payable - Payment", session));
                tblHotelFinanceTransaction.setCreateDate(Timestamp.valueOf(LocalDateTime.now()));
                tblHotelFinanceTransaction.setTblEmployeeByCreateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                tblHotelFinanceTransaction.setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                tblHotelFinanceTransaction.setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                tblHotelFinanceTransaction.setRefRecordStatus(session.find(RefRecordStatus.class, 1));
                session.saveOrUpdate(tblHotelFinanceTransaction);
                //data hotel finance transaction - with transfer
                hotelFinanceTransactionWithTransfer.setCreateDate(Timestamp.valueOf(LocalDateTime.now()));
                hotelFinanceTransactionWithTransfer.setTblEmployeeByCreateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                hotelFinanceTransactionWithTransfer.setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                hotelFinanceTransactionWithTransfer.setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                hotelFinanceTransactionWithTransfer.setRefRecordStatus(session.find(RefRecordStatus.class, 1));
                session.saveOrUpdate(hotelFinanceTransactionWithTransfer);
                //data hotel finnace transaction - hotel payable
                for (TblHotelFinanceTransactionHotelPayable hfthp : hfthps) {
                    hfthp.setCreateDate(Timestamp.valueOf(LocalDateTime.now()));
                    hfthp.setTblEmployeeByCreateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                    hfthp.setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                    hfthp.setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                    hfthp.setRefRecordStatus(session.find(RefRecordStatus.class, 1));
                    session.saveOrUpdate(hfthp);
                    //data hotel payable
                    hfthp.getTblHotelPayable().setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                    hfthp.getTblHotelPayable().setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                    session.saveOrUpdate(hfthp.getTblHotelPayable());
                    //data reservation additional service - resto bill
                    TblReservationAdditionalService ras = getReservationAdditionalServiceByIDHotelPayable(hfthp.getTblHotelPayable().getIdhotelPayable());
                    if (ras != null) {
                        ras.setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                        ras.setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                        session.saveOrUpdate(ras);
                    }
                }
                //@@@%%%
                //data company balance (kas besar) : minus/plus (updated)
                TblCompanyBalance dataBalance = session.find(TblCompanyBalance.class, (long) 1);    //Kas Besar = '1'
                if (hotelFinanceTransaction.getIsReturnTransaction()) {   //plus
                    dataBalance.setBalanceNominal(dataBalance.getBalanceNominal().add(hotelFinanceTransaction.getTransactionNominal()));
                } else {  //minus
                    dataBalance.setBalanceNominal(dataBalance.getBalanceNominal().subtract(hotelFinanceTransaction.getTransactionNominal()));
                }
                dataBalance.setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                dataBalance.setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                session.update(dataBalance);
                //data company balance (kas besar) : nominal bank account : minus/plus (updated)
                TblCompanyBalanceBankAccount companyBalanceBankAccount;
                if (hotelFinanceTransaction.getIsReturnTransaction()) {
                    companyBalanceBankAccount = getCompanyBalanceBankAccountByIDCompanyBalanceAndIDBankAccount(
                            (long) 1, //Kas Besar = '1'
                            hotelFinanceTransactionWithTransfer.getTblBankAccountByReceiverBankAccount().getIdbankAccount());
                } else {
                    companyBalanceBankAccount = getCompanyBalanceBankAccountByIDCompanyBalanceAndIDBankAccount(
                            (long) 1, //Kas Besar = '1'
                            hotelFinanceTransactionWithTransfer.getTblBankAccountBySenderBankAccount().getIdbankAccount());
                }
                if (hotelFinanceTransaction.getIsReturnTransaction()) {   //plus
                    companyBalanceBankAccount.setCompanyBalanceBankAccountNominal(companyBalanceBankAccount.getCompanyBalanceBankAccountNominal().add(hotelFinanceTransaction.getTransactionNominal()));
                } else {  //minus
                    companyBalanceBankAccount.setCompanyBalanceBankAccountNominal(companyBalanceBankAccount.getCompanyBalanceBankAccountNominal().subtract(hotelFinanceTransaction.getTransactionNominal()));
                }
                companyBalanceBankAccount.setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                companyBalanceBankAccount.setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                session.update(companyBalanceBankAccount);
                //data company balance (cash-flow log)
                LogCompanyBalanceCashFlow logCompanyBalanceCashFlow = new LogCompanyBalanceCashFlow();
                if (hotelFinanceTransaction.getIsReturnTransaction()) {   //in
                    logCompanyBalanceCashFlow.setTblCompanyBalanceByIdsenderCompanyBalance(null);
                    logCompanyBalanceCashFlow.setTblBankAccountByIdsenderCbbankAccount(hotelFinanceTransactionWithTransfer.getTblBankAccountBySenderBankAccount());
                    logCompanyBalanceCashFlow.setTblCompanyBalanceByIdreceiverCompanyBalance(dataBalance);
                    logCompanyBalanceCashFlow.setTblBankAccountByIdreceiverCbbankAccount(companyBalanceBankAccount.getTblBankAccount());
                } else {  //out
                    logCompanyBalanceCashFlow.setTblCompanyBalanceByIdsenderCompanyBalance(dataBalance);
                    logCompanyBalanceCashFlow.setTblBankAccountByIdsenderCbbankAccount(companyBalanceBankAccount.getTblBankAccount());
                    logCompanyBalanceCashFlow.setTblCompanyBalanceByIdreceiverCompanyBalance(null);
                    logCompanyBalanceCashFlow.setTblBankAccountByIdreceiverCbbankAccount(hotelFinanceTransactionWithTransfer.getTblBankAccountByReceiverBankAccount());
                }
                logCompanyBalanceCashFlow.setTransferNominal(hotelFinanceTransaction.getTransactionNominal());
                logCompanyBalanceCashFlow.setHistoryDate(Timestamp.valueOf(LocalDateTime.now()));
                logCompanyBalanceCashFlow.setTblEmployee(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                if (hotelFinanceTransaction.getIsReturnTransaction()) {
                    logCompanyBalanceCashFlow.setHistoryStystemNote("Pengembalian pembayaran utang oleh " + restoName + ", \nsebesar : "
                            + ClassFormatter.currencyFormat.format(hotelFinanceTransaction.getTransactionNominal()) + " ("
                            + tblHotelFinanceTransaction.getRefFinanceTransactionPaymentType().getTypeName() + ")");
                } else {
                    logCompanyBalanceCashFlow.setHistoryStystemNote("Pembayaran utang kepada " + restoName + ", \nsebesar : "
                            + ClassFormatter.currencyFormat.format(hotelFinanceTransaction.getTransactionNominal()) + " ("
                            + tblHotelFinanceTransaction.getRefFinanceTransactionPaymentType().getTypeName() + ")");
                }
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
        return tblHotelFinanceTransaction;
    }

    @Override
    public TblHotelFinanceTransaction insertDataHotelFinanceTransaction(
            TblHotelFinanceTransaction hotelFinanceTransaction,
            TblHotelFinanceTransactionWithCekGiro hotelFinanceTransactionWithCekGiro,
            List<TblHotelFinanceTransactionHotelPayable> hfthps, 
            String restoName) {
        errMsg = "";
        TblHotelFinanceTransaction tblHotelFinanceTransaction = hotelFinanceTransaction;
        if (ClassSession.checkUserSession()) {  //check user current session
            try {
                session = HibernateUtil.getSessionFactory().getCurrentSession();
                session.beginTransaction();
                //data hotel finnace transaction
                tblHotelFinanceTransaction.setCodeTransaction(ClassCoder.getCode("Payable - Payment", session));
                tblHotelFinanceTransaction.setCreateDate(Timestamp.valueOf(LocalDateTime.now()));
                tblHotelFinanceTransaction.setTblEmployeeByCreateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                tblHotelFinanceTransaction.setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                tblHotelFinanceTransaction.setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                tblHotelFinanceTransaction.setRefRecordStatus(session.find(RefRecordStatus.class, 1));
                session.saveOrUpdate(tblHotelFinanceTransaction);
                //data hotel finance transaction - with cek/giro
                hotelFinanceTransactionWithCekGiro.setCreateDate(Timestamp.valueOf(LocalDateTime.now()));
                hotelFinanceTransactionWithCekGiro.setTblEmployeeByCreateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                hotelFinanceTransactionWithCekGiro.setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                hotelFinanceTransactionWithCekGiro.setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                hotelFinanceTransactionWithCekGiro.setRefRecordStatus(session.find(RefRecordStatus.class, 1));
                session.saveOrUpdate(hotelFinanceTransactionWithCekGiro);
                //data hotel finnace transaction - hotel payable
                for (TblHotelFinanceTransactionHotelPayable hfthp : hfthps) {
                    hfthp.setCreateDate(Timestamp.valueOf(LocalDateTime.now()));
                    hfthp.setTblEmployeeByCreateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                    hfthp.setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                    hfthp.setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                    hfthp.setRefRecordStatus(session.find(RefRecordStatus.class, 1));
                    session.saveOrUpdate(hfthp);
                    //data hotel payable
                    hfthp.getTblHotelPayable().setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                    hfthp.getTblHotelPayable().setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                    session.saveOrUpdate(hfthp.getTblHotelPayable());
                    //data reservation additional service - resto bill
                    TblReservationAdditionalService ras = getReservationAdditionalServiceByIDHotelPayable(hfthp.getTblHotelPayable().getIdhotelPayable());
                    if (ras != null) {
                        ras.setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                        ras.setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                        session.saveOrUpdate(ras);
                    }
                }
                //@@@%%%
                //data company balance (kas besar) : minus/plus (updated)
                TblCompanyBalance dataBalance = session.find(TblCompanyBalance.class, (long) 1);    //Kas Besar = '1'
                if (hotelFinanceTransaction.getIsReturnTransaction()) {   //plus
                    dataBalance.setBalanceNominal(dataBalance.getBalanceNominal().add(hotelFinanceTransaction.getTransactionNominal()));
                } else {  //minus
                    dataBalance.setBalanceNominal(dataBalance.getBalanceNominal().subtract(hotelFinanceTransaction.getTransactionNominal()));
                }
                dataBalance.setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                dataBalance.setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                session.update(dataBalance);
                //data company balance (kas besar) : nominal bank account : minus/plus (updated)
                TblCompanyBalanceBankAccount companyBalanceBankAccount;
                if (hotelFinanceTransaction.getIsReturnTransaction()) {
                    companyBalanceBankAccount = getCompanyBalanceBankAccountByIDCompanyBalanceAndIDBankAccount(
                            (long) 1, //Kas Besar = '1'
                            hotelFinanceTransactionWithCekGiro.getTblBankAccountByReceiverBankAccount().getIdbankAccount());
                } else {
                    companyBalanceBankAccount = getCompanyBalanceBankAccountByIDCompanyBalanceAndIDBankAccount(
                            (long) 1, //Kas Besar = '1'
                            hotelFinanceTransactionWithCekGiro.getTblBankAccountBySenderBankAccount().getIdbankAccount());
                }
                if (hotelFinanceTransaction.getIsReturnTransaction()) {   //plus
                    companyBalanceBankAccount.setCompanyBalanceBankAccountNominal(companyBalanceBankAccount.getCompanyBalanceBankAccountNominal().add(hotelFinanceTransaction.getTransactionNominal()));
                } else {  //minus
                    companyBalanceBankAccount.setCompanyBalanceBankAccountNominal(companyBalanceBankAccount.getCompanyBalanceBankAccountNominal().subtract(hotelFinanceTransaction.getTransactionNominal()));
                }
                companyBalanceBankAccount.setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                companyBalanceBankAccount.setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                session.update(companyBalanceBankAccount);
                //data company balance (cash-flow log)
                LogCompanyBalanceCashFlow logCompanyBalanceCashFlow = new LogCompanyBalanceCashFlow();
                if (hotelFinanceTransaction.getIsReturnTransaction()) {   //in
                    logCompanyBalanceCashFlow.setTblCompanyBalanceByIdsenderCompanyBalance(null);
                    logCompanyBalanceCashFlow.setTblBankAccountByIdsenderCbbankAccount(hotelFinanceTransactionWithCekGiro.getTblBankAccountBySenderBankAccount());
                    logCompanyBalanceCashFlow.setTblCompanyBalanceByIdreceiverCompanyBalance(dataBalance);
                    logCompanyBalanceCashFlow.setTblBankAccountByIdreceiverCbbankAccount(companyBalanceBankAccount.getTblBankAccount());
                } else {  //out
                    logCompanyBalanceCashFlow.setTblCompanyBalanceByIdsenderCompanyBalance(dataBalance);
                    logCompanyBalanceCashFlow.setTblBankAccountByIdsenderCbbankAccount(companyBalanceBankAccount.getTblBankAccount());
                    logCompanyBalanceCashFlow.setTblCompanyBalanceByIdreceiverCompanyBalance(null);
                    logCompanyBalanceCashFlow.setTblBankAccountByIdreceiverCbbankAccount(hotelFinanceTransactionWithCekGiro.getTblBankAccountByReceiverBankAccount());
                }
                logCompanyBalanceCashFlow.setTransferNominal(hotelFinanceTransaction.getTransactionNominal());
                logCompanyBalanceCashFlow.setHistoryDate(Timestamp.valueOf(LocalDateTime.now()));
                logCompanyBalanceCashFlow.setTblEmployee(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                if (hotelFinanceTransaction.getIsReturnTransaction()) {
                    logCompanyBalanceCashFlow.setHistoryStystemNote("Pengembalian pembayaran utang oleh " + restoName + ", \nsebesar : "
                            + ClassFormatter.currencyFormat.format(hotelFinanceTransaction.getTransactionNominal()) + " ("
                            + tblHotelFinanceTransaction.getRefFinanceTransactionPaymentType().getTypeName() + ")");
                } else {
                    logCompanyBalanceCashFlow.setHistoryStystemNote("Pembayaran utang kepada " + restoName + ", \nsebesar : "
                            + ClassFormatter.currencyFormat.format(hotelFinanceTransaction.getTransactionNominal()) + " ("
                            + tblHotelFinanceTransaction.getRefFinanceTransactionPaymentType().getTypeName() + ")");
                }
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
        return tblHotelFinanceTransaction;
    }

    private TblReservationAdditionalService getReservationAdditionalServiceByIDHotelPayable(long idHotelPayable) {
        List<TblReservationAdditionalService> list = session.getNamedQuery("findAllTblReservationAdditionalServiceByIDHotelPayable")
                .setParameter("idHotelPayable", idHotelPayable)
                .list();
        return list.isEmpty() ? null : list.get(0);
    }

//    private String getFirstRestoName() {
//        List<TblResto> list = session.getNamedQuery("findAllTblResto")
//                .list();
//        return list.isEmpty() ? "Resto" : list.get(0).getRestoName();
//    }

    @Override
    public RefFinanceTransactionType getDataFinanceTransactionType(int id) {
        errMsg = "";
        RefFinanceTransactionType data = null;
        if (ClassSession.checkUserSession()) {  //check user current session
            try {
                session = HibernateUtil.getSessionFactory().getCurrentSession();
                session.beginTransaction();
                data = (RefFinanceTransactionType) session.find(RefFinanceTransactionType.class, id);
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
    public TblRestoBankAccount insertDataRestoBankAccount(TblRestoBankAccount restoBankAccount) {
        errMsg = "";
        TblRestoBankAccount tblRestoBankAccount = restoBankAccount;
        if (ClassSession.checkUserSession()) {  //check user current session
            try {
                session = HibernateUtil.getSessionFactory().getCurrentSession();
                session.beginTransaction();
                //save data bank account n resto bank acccount
                //data bank account
                tblRestoBankAccount.getTblBankAccount().setCreateDate(Timestamp.valueOf(LocalDateTime.now()));
                tblRestoBankAccount.getTblBankAccount().setTblEmployeeByCreateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                tblRestoBankAccount.getTblBankAccount().setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                tblRestoBankAccount.getTblBankAccount().setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                tblRestoBankAccount.getTblBankAccount().setRefRecordStatus(session.find(RefRecordStatus.class, 1));
                session.saveOrUpdate(tblRestoBankAccount.getTblBankAccount());
                //data resto - bank account
                tblRestoBankAccount.setRefBankAccountHolderStatus(session.find(RefBankAccountHolderStatus.class, 0));
                tblRestoBankAccount.setCreateDate(Timestamp.valueOf(LocalDateTime.now()));
                tblRestoBankAccount.setTblEmployeeByCreateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                tblRestoBankAccount.setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                tblRestoBankAccount.setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                tblRestoBankAccount.setRefRecordStatus(session.find(RefRecordStatus.class, 1));
                session.saveOrUpdate(tblRestoBankAccount);
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
        return tblRestoBankAccount;
    }

    @Override
    public TblRestoBankAccount getDataRestoBankAccount(long id) {
        errMsg = "";
        TblRestoBankAccount data = null;
        if (ClassSession.checkUserSession()) {  //check user current session
            try {
                session = HibernateUtil.getSessionFactory().getCurrentSession();
                session.beginTransaction();
                data = (TblRestoBankAccount) session.find(TblRestoBankAccount.class, id);
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
    public List<TblRestoBankAccount> getAllDataRestoBankAccount() {
        errMsg = "";
        List<TblRestoBankAccount> list = new ArrayList<>();
        if (ClassSession.checkUserSession()) {  //check user current session
            try {
                session = HibernateUtil.getSessionFactory().getCurrentSession();
                session.beginTransaction();
                list = session.getNamedQuery("findAllTblRestoBankAccount").list();
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
    public List<TblRestoBankAccount> getAllDataRestoBankAccountByIDResto(long idResto) {
        errMsg = "";
        List<TblRestoBankAccount> list = new ArrayList<>();
        if (ClassSession.checkUserSession()) {  //check user current session
            try {
                session = HibernateUtil.getSessionFactory().getCurrentSession();
                session.beginTransaction();
                list = session.getNamedQuery("findAllTblRestoBankAccountByIDResto")
                        .setParameter("idResto", idResto)
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
    public TblBankAccount getBankAccount(long id) {
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
    public List<SysDataHardCode> getAllDataSysDataHardCode() {
        errMsg = "";
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
                errMsg = e.getMessage();
            } finally {
                //session.close();
            }
        }
        return list;
    }

    //--------------------------------------------------------------------------
    @Override
    public TblHotelInvoice insertDataHotelInvoice(TblHotelInvoice hotelInvoice,
            TblReservationAdditionalService ras) {
        errMsg = "";
        TblHotelInvoice tblHotelInvoice = hotelInvoice;
        if (ClassSession.checkUserSession()) {  //check user current session
            try {
                session = HibernateUtil.getSessionFactory().getCurrentSession();
                session.beginTransaction();
                //data hotel invoice
                tblHotelInvoice.setCreateDate(Timestamp.valueOf(LocalDateTime.now()));
                tblHotelInvoice.setTblEmployeeByCreateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                tblHotelInvoice.setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                tblHotelInvoice.setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                tblHotelInvoice.setRefRecordStatus(session.find(RefRecordStatus.class, 1));
                session.saveOrUpdate(tblHotelInvoice);
                //data hotel payable (insert)
                if (ras.getTblHotelPayable().getIdhotelPayable() == 0L) {
                    ras.getTblHotelPayable().setCreateDate(Timestamp.valueOf(LocalDateTime.now()));
                    ras.getTblHotelPayable().setTblEmployeeByCreateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                    ras.getTblHotelPayable().setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                    ras.getTblHotelPayable().setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                    ras.getTblHotelPayable().setRefRecordStatus(session.find(RefRecordStatus.class, 1));
                    session.saveOrUpdate(ras.getTblHotelPayable());
                } else {
                    ras.getTblHotelPayable().setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                    ras.getTblHotelPayable().setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                    session.saveOrUpdate(ras.getTblHotelPayable());
                }
                //data reservation additional service - resto bill (update)
                ras.setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                ras.setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                session.update(ras);
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
        return tblHotelInvoice;
    }

    @Override
    public TblHotelInvoice insertDataHotelInvoice(TblReservationAdditionalService ras) {
        errMsg = "";
        TblReservationAdditionalService tblRAS = ras;
        if (ClassSession.checkUserSession()) {  //check user current session
            try {
                session = HibernateUtil.getSessionFactory().getCurrentSession();
                session.beginTransaction();
                //data hotel invoice
                if (tblRAS.getTblHotelPayable().getTblHotelInvoice().getIdhotelInvoice() == 0L) {
                    tblRAS.getTblHotelPayable().getTblHotelInvoice().setCreateDate(Timestamp.valueOf(LocalDateTime.now()));
                    tblRAS.getTblHotelPayable().getTblHotelInvoice().setTblEmployeeByCreateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                    tblRAS.getTblHotelPayable().getTblHotelInvoice().setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                    tblRAS.getTblHotelPayable().getTblHotelInvoice().setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                    tblRAS.getTblHotelPayable().getTblHotelInvoice().setRefRecordStatus(session.find(RefRecordStatus.class, 1));
                    session.saveOrUpdate(tblRAS.getTblHotelPayable().getTblHotelInvoice());
                } else {
                    tblRAS.getTblHotelPayable().getTblHotelInvoice().setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                    tblRAS.getTblHotelPayable().getTblHotelInvoice().setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                    session.saveOrUpdate(tblRAS.getTblHotelPayable().getTblHotelInvoice());
                }
                //data hotel payable (insert)
                if (tblRAS.getTblHotelPayable().getIdhotelPayable() == 0L) {
                    tblRAS.getTblHotelPayable().setCreateDate(Timestamp.valueOf(LocalDateTime.now()));
                    tblRAS.getTblHotelPayable().setTblEmployeeByCreateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                    tblRAS.getTblHotelPayable().setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                    tblRAS.getTblHotelPayable().setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                    tblRAS.getTblHotelPayable().setRefRecordStatus(session.find(RefRecordStatus.class, 1));
                    session.saveOrUpdate(tblRAS.getTblHotelPayable());
                } else {
                    tblRAS.getTblHotelPayable().setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                    tblRAS.getTblHotelPayable().setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                    session.saveOrUpdate(tblRAS.getTblHotelPayable());
                }
                //data reservation additonal service - resto bill (update)
                tblRAS.setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                tblRAS.setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                session.update(tblRAS);
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
        return tblRAS.getTblHotelPayable().getTblHotelInvoice();
    }

    @Override
    public TblHotelInvoice getDataHotelInvoice(long id) {
        errMsg = "";
        TblHotelInvoice data = null;
        if (ClassSession.checkUserSession()) {  //check user current session
            try {
                session = HibernateUtil.getSessionFactory().getCurrentSession();
                session.beginTransaction();
                data = (TblHotelInvoice) session.find(TblHotelInvoice.class, id);
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
    public RefHotelInvoiceType getDataHotelInvoiceType(int id) {
        errMsg = "";
        RefHotelInvoiceType data = null;
        if (ClassSession.checkUserSession()) {  //check user current session
            try {
                session = HibernateUtil.getSessionFactory().getCurrentSession();
                session.beginTransaction();
                data = (RefHotelInvoiceType) session.find(RefHotelInvoiceType.class, id);
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
    public TblHotelFinanceTransaction getDataHotelFinanceTransaction(long id) {
        errMsg = "";
        TblHotelFinanceTransaction data = null;
        if (ClassSession.checkUserSession()) {  //check user current session
            try {
                session = HibernateUtil.getSessionFactory().getCurrentSession();
                session.beginTransaction();
                data = (TblHotelFinanceTransaction) session.find(TblHotelFinanceTransaction.class, id);
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
    public List<TblHotelFinanceTransactionHotelPayable> getAllDataHotelFinanceTransactionHotelPayableByIDHotelFinanceTransaction(long idHotelFinanceTransaction) {
        errMsg = "";
        List<TblHotelFinanceTransactionHotelPayable> list = new ArrayList<>();
        if (ClassSession.checkUserSession()) {  //check user current session
            try {
                session = HibernateUtil.getSessionFactory().getCurrentSession();
                session.beginTransaction();
                list = session.getNamedQuery("findAllTblHotelFinanceTransactionHotelPayableByIDHotelFinanceTransaction")
                        .setParameter("idHotelFinanceTransaction", idHotelFinanceTransaction)
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
    public List<TblHotelFinanceTransactionHotelPayable> getAllDataHotelFinanceTransactionHotelPayableByIDHotelPayable(long idHotelPayable) {
        errMsg = "";
        List<TblHotelFinanceTransactionHotelPayable> list = new ArrayList<>();
        if (ClassSession.checkUserSession()) {  //check user current session
            try {
                session = HibernateUtil.getSessionFactory().getCurrentSession();
                session.beginTransaction();
                list = session.getNamedQuery("findAllTblHotelFinanceTransactionHotelPayableByIDHotelPayable")
                        .setParameter("idHotelPayable", idHotelPayable)
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
    public TblReservationAdditionalService getDataReservationAdditionalServiceByIDHotelPayable(long idHotelPayable) {
        errMsg = "";
        List<TblReservationAdditionalService> list = new ArrayList<>();
        if (ClassSession.checkUserSession()) {  //check user current session
            try {
                session = HibernateUtil.getSessionFactory().getCurrentSession();
                session.beginTransaction();
                list = session.getNamedQuery("findAllTblReservationAdditionalServiceByIDHotelPayable")
                        .setParameter("idHotelPayable", idHotelPayable)
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
    public List<RefFinanceTransactionPaymentType> getAllDataFinanceTransactionPaymentType() {
        errMsg = "";
        List<RefFinanceTransactionPaymentType> list = new ArrayList<>();
        if (ClassSession.checkUserSession()) {  //check user current session
            try {
                session = HibernateUtil.getSessionFactory().getCurrentSession();
                session.beginTransaction();
                list = session.getNamedQuery("findAllRefFinanceTransactionPaymentType").list();
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
    public TblCompanyBalanceBankAccount getDataCompanyBalanceBankAccountByIDCompanyBalanceAndIDBankAccount(
            long idCompanyBalance,
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

    //--------------------------------------------------------------------------
    @Override
    public TblHotelFinanceTransactionWithTransfer getDataHotelFinanceTransactionWithTransferByIDHotelFinanceTransaction(long idHotelFinanceTransaction) {
        errMsg = "";
        List<TblHotelFinanceTransactionWithTransfer> list = new ArrayList<>();
        if (ClassSession.checkUserSession()) {  //check user current session
            try {
                session = HibernateUtil.getSessionFactory().getCurrentSession();
                session.beginTransaction();
                list = session.getNamedQuery("findAllTblHotelFinanceTransactionWithTransferByIDHotelFinanceTransactio")
                        .setParameter("idHotelFinanceTransaction", idHotelFinanceTransaction)
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
    public TblHotelFinanceTransactionWithCekGiro getDataHotelFinanceTransactionWithCekGiroByIDHotelFinanceTransaction(long idHotelFinanceTransaction) {
        errMsg = "";
        List<TblHotelFinanceTransactionWithCekGiro> list = new ArrayList<>();
        if (ClassSession.checkUserSession()) {  //check user current session
            try {
                session = HibernateUtil.getSessionFactory().getCurrentSession();
                session.beginTransaction();
                list = session.getNamedQuery("findAllTblHotelFinanceTransactionWithCekGiroByIDHotelFinanceTransaction")
                        .setParameter("idHotelFinanceTransaction", idHotelFinanceTransaction)
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
    public String getErrorMessage() {
        return errMsg;
    }

}
