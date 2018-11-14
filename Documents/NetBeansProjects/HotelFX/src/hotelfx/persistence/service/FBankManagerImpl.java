/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hotelfx.persistence.service;

import hotelfx.helper.ClassCoder;
import hotelfx.helper.ClassSession;
import hotelfx.persistence.HibernateUtil;
import hotelfx.persistence.model.RefBankCardType;
import hotelfx.persistence.model.RefEdctransactionStatus;
import hotelfx.persistence.model.RefRecordStatus;
import java.util.ArrayList;
import java.util.List;
import org.hibernate.Session;
import hotelfx.persistence.model.TblBank;
import hotelfx.persistence.model.TblBankAccount;
import hotelfx.persistence.model.TblBankCard;
import hotelfx.persistence.model.TblBankEdc;
import hotelfx.persistence.model.TblBankEdcBankNetworkCard;
import hotelfx.persistence.model.TblBankEventCard;
import hotelfx.persistence.model.TblBankNetworkCard;
import hotelfx.persistence.model.TblCompanyBalanceBankAccount;
import hotelfx.persistence.model.TblEmployee;
import hotelfx.persistence.model.TblPeople;
import hotelfx.persistence.model.TblReservation;
import hotelfx.persistence.model.TblReservationBill;
import hotelfx.persistence.model.TblReservationPayment;
import hotelfx.persistence.model.TblReservationPaymentWithBankCard;
import hotelfx.persistence.model.TblRoomCardType;
import java.sql.Date;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import org.hibernate.query.Query;

/**
 *
 * @author ANDRI
 */
public class FBankManagerImpl implements FBankManager {

    private Session session;

    private String errMsg;

    public FBankManagerImpl() {

    }

    @Override
    public TblBank insertDataBank(TblBank bank,
            List<TblBankCard> bankCards) {
        errMsg = "";
        TblBank tblBank = bank;
        if (ClassSession.checkUserSession()) {  //check user current session
            try {
                session = HibernateUtil.getSessionFactory().getCurrentSession();
                session.beginTransaction();
                //check bank name has been used
                List<TblBank> list = session.getNamedQuery("findAllTblBankByBankName")
                        .setParameter("bankName", tblBank.getBankName())
                        .list();
                if (!list.isEmpty()) {
                    if (session.getTransaction().isActive()) {
                        session.getTransaction().rollback();
                    }
                    errMsg = "Duplikasi Nama Bank!!";
                    return null;
                }
                //data bank
                tblBank.setCreateDate(Timestamp.valueOf(LocalDateTime.now()));
                tblBank.setTblEmployeeByCreateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                tblBank.setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                tblBank.setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                tblBank.setRefRecordStatus(session.find(RefRecordStatus.class, 1));
                session.saveOrUpdate(tblBank);
                //data bank card
                for (TblBankCard bankCard : bankCards) {
                    bankCard.setCreateDate(Timestamp.valueOf(LocalDateTime.now()));
                    bankCard.setTblEmployeeByCreateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                    bankCard.setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                    bankCard.setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                    bankCard.setRefRecordStatus(session.find(RefRecordStatus.class, 1));
                    session.saveOrUpdate(bankCard);
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
        return tblBank;
    }

    @Override
    public boolean updateDataBank(TblBank bank,
            List<TblBankCard> bankCards) {
        errMsg = "";
        if (ClassSession.checkUserSession()) {  //check user current session
            try {
                session = HibernateUtil.getSessionFactory().getCurrentSession();
                session.beginTransaction();
                //check bank name has been used
                List<TblBank> list = session.getNamedQuery("findAllTblBankByBankName")
                        .setParameter("bankName", bank.getBankName())
                        .list();
                if (!list.isEmpty()
                        && list.get(0).getIdbank() != bank.getIdbank()) {
                    if (session.getTransaction().isActive()) {
                        session.getTransaction().rollback();
                    }
                    errMsg = "Duplikasi Nama Bank!!";
                    return false;
                }
                if (!list.isEmpty()) {
                    session.evict(list.get(0));
                }
                //data bank
                bank.setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                bank.setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                session.update(bank);
                //delete all (data bank card)
                Query rs = session.getNamedQuery("deleteAllTblBankCard")
                        .setParameter("idBank", bank.getIdbank())
                        .setParameter("idEmployee", ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee());
                errMsg = (String) rs.uniqueResult();
                if (!errMsg.equals("")) {
                    if (session.getTransaction().isActive()) {
                        session.getTransaction().rollback();
                    }
                    return false;
                }
                //insert all data bank card
                for (TblBankCard bankCard : bankCards) {
                    bankCard.setCreateDate(Timestamp.valueOf(LocalDateTime.now()));
                    bankCard.setTblEmployeeByCreateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                    bankCard.setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                    bankCard.setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                    bankCard.setRefRecordStatus(session.find(RefRecordStatus.class, 1));
                    session.saveOrUpdate(bankCard);
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
    public boolean deleteDataBank(TblBank bank) {
        errMsg = "";
        if (ClassSession.checkUserSession()) {  //check user current session
            try {
                session = HibernateUtil.getSessionFactory().getCurrentSession();
                session.beginTransaction();
                if (bank.getRefRecordStatus().getIdstatus() == 1) {
                    bank.setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                    bank.setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                    bank.setRefRecordStatus(session.find(RefRecordStatus.class, 2));
                    session.update(bank);
                    //delete all (data bank card)
                    Query rs = session.getNamedQuery("deleteAllTblBankCard")
                            .setParameter("idBank", bank.getIdbank())
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
    public TblBank getBank(long id) {
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
    public TblBankNetworkCard insertDataBankNetworkCard(TblBankNetworkCard bankNetworkCard) {
        errMsg = "";
        TblBankNetworkCard tblBankNetworkCard = bankNetworkCard;
        if (ClassSession.checkUserSession()) {  //check user current session
            try {
                session = HibernateUtil.getSessionFactory().getCurrentSession();
                session.beginTransaction();
                //check network card name has been used
                List<TblBankNetworkCard> list = session.getNamedQuery("findAllTblBankNetworkCardByNetworkCardName")
                        .setParameter("networkCardName", tblBankNetworkCard.getNetworkCardName())
                        .list();
                if (!list.isEmpty()) {
                    if (session.getTransaction().isActive()) {
                        session.getTransaction().rollback();
                    }
                    errMsg = "Duplikasi Nama Jaringan Kartu Bank!!";
                    return null;
                }
                //data bank network card
                tblBankNetworkCard.setCreateDate(Timestamp.valueOf(LocalDateTime.now()));
                tblBankNetworkCard.setTblEmployeeByCreateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                tblBankNetworkCard.setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                tblBankNetworkCard.setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                tblBankNetworkCard.setRefRecordStatus(session.find(RefRecordStatus.class, 1));
                session.saveOrUpdate(tblBankNetworkCard);
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
        return tblBankNetworkCard;
    }

    @Override
    public boolean updateDataBankNetworkCard(TblBankNetworkCard bankNetworkCard) {
        errMsg = "";
        if (ClassSession.checkUserSession()) {  //check user current session
            try {
                session = HibernateUtil.getSessionFactory().getCurrentSession();
                session.beginTransaction();
                //check network card name has been used
                List<TblBankNetworkCard> list = session.getNamedQuery("findAllTblBankNetworkCardByNetworkCardName")
                        .setParameter("networkCardName", bankNetworkCard.getNetworkCardName())
                        .list();
                if (!list.isEmpty()
                        && list.get(0).getIdnetworkCard() != bankNetworkCard.getIdnetworkCard()) {
                    if (session.getTransaction().isActive()) {
                        session.getTransaction().rollback();
                    }
                    errMsg = "Duplikasi Nama Jaringan Kartu Bank!!";
                    return false;
                }
                if (!list.isEmpty()) {
                    session.evict(list.get(0));
                }
                //data bank network card
                bankNetworkCard.setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                bankNetworkCard.setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                session.update(bankNetworkCard);
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
    public boolean deleteDataBankNetworkCard(TblBankNetworkCard bankNetworkCard) {
        errMsg = "";
        if (ClassSession.checkUserSession()) {  //check user current session
            try {
                session = HibernateUtil.getSessionFactory().getCurrentSession();
                session.beginTransaction();
                if (bankNetworkCard.getRefRecordStatus().getIdstatus() == 1) {
                    bankNetworkCard.setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                    bankNetworkCard.setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                    bankNetworkCard.setRefRecordStatus(session.find(RefRecordStatus.class, 2));
                    session.update(bankNetworkCard);
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
    public TblBankNetworkCard getBankNetworkCard(long id) {
        errMsg = "";
        TblBankNetworkCard data = null;
        if (ClassSession.checkUserSession()) {  //check user current session
            try {
                session = HibernateUtil.getSessionFactory().getCurrentSession();
                session.beginTransaction();
                data = (TblBankNetworkCard) session.find(TblBankNetworkCard.class, id);
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
    public List<TblBankNetworkCard> getAllDataBankNetworkCard() {
        errMsg = "";
        List<TblBankNetworkCard> list = new ArrayList<>();
        if (ClassSession.checkUserSession()) {  //check user current session
            try {
                session = HibernateUtil.getSessionFactory().getCurrentSession();
                session.beginTransaction();
                list = session.getNamedQuery("findAllTblBankNetworkCard").list();
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
    public TblBankEdc insertDataBankEDC(TblBankEdc bankEdc,
            List<TblBankEdcBankNetworkCard> edcNetworkCard) {
        errMsg = "";
        TblBankEdc tblBankEdc = bankEdc;
        if (ClassSession.checkUserSession()) {  //check user current session
            try {
                session = HibernateUtil.getSessionFactory().getCurrentSession();
                session.beginTransaction();
                //data edc
                tblBankEdc.setCreateDate(Timestamp.valueOf(LocalDateTime.now()));
                tblBankEdc.setTblEmployeeByCreateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                tblBankEdc.setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                tblBankEdc.setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                tblBankEdc.setRefRecordStatus(session.find(RefRecordStatus.class, 1));
                session.saveOrUpdate(tblBankEdc);
//            //data edc balance
//            TblBankEdcbalance dataBalance = new TblBankEdcbalance();
//            dataBalance.setTblBankEdc(tblBankEdc);
//            session.saveOrUpdate(dataBalance);
                //save or update data edc - network card            
                for (TblBankEdcBankNetworkCard data : edcNetworkCard) {
                    //data edc - network card
                    data.setCreateDate(Timestamp.valueOf(LocalDateTime.now()));
                    data.setTblEmployeeByCreateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                    data.setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                    data.setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                    data.setRefRecordStatus(session.find(RefRecordStatus.class, 1));
                    session.saveOrUpdate(data);
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
        return tblBankEdc;
    }

    @Override
    public boolean updateDataBankEDC(TblBankEdc bankEdc,
            List<TblBankEdcBankNetworkCard> edcNetworkCard) {
        errMsg = "";
        if (ClassSession.checkUserSession()) {  //check user current session
            try {
                session = HibernateUtil.getSessionFactory().getCurrentSession();
                session.beginTransaction();
                //data edc
                bankEdc.setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                bankEdc.setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                session.update(bankEdc);
                //delete all edc - network card
                Query rs = session.getNamedQuery("deleteAllTblBankEdcBankNetworkCard")
                        .setParameter("idEDC", bankEdc.getIdedc())
                        .setParameter("idEmployee", ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee());
                errMsg = (String) rs.uniqueResult();
                if (!errMsg.equals("")) {
                    if (session.getTransaction().isActive()) {
                        session.getTransaction().rollback();
                    }
                    return false;
                }
                //save or update data edc - network card
                for (TblBankEdcBankNetworkCard data : edcNetworkCard) {
                    //data edc - network card
                    data.setCreateDate(Timestamp.valueOf(LocalDateTime.now()));
                    data.setTblEmployeeByCreateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                    data.setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                    data.setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                    data.setRefRecordStatus(session.find(RefRecordStatus.class, 1));
                    session.saveOrUpdate(data);
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
    public boolean deleteDataBankEDC(TblBankEdc bankEdc) {
        errMsg = "";
        if (ClassSession.checkUserSession()) {  //check user current session
            try {
                session = HibernateUtil.getSessionFactory().getCurrentSession();
                session.beginTransaction();
                if (bankEdc.getRefRecordStatus().getIdstatus() == 1) {
                //data edc balance
                    //...
                    //data edc
                    bankEdc.setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                    bankEdc.setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                    bankEdc.setRefRecordStatus(session.find(RefRecordStatus.class, 2));
                    session.update(bankEdc);
                    //delete all edc - network card
                    Query rs = session.getNamedQuery("deleteAllTblBankEdcBankNetworkCard")
                            .setParameter("idEDC", bankEdc.getIdedc())
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
    public TblBankEdc getBankEDC(long id) {
        errMsg = "";
        TblBankEdc data = null;
        if (ClassSession.checkUserSession()) {  //check user current session
            try {
                session = HibernateUtil.getSessionFactory().getCurrentSession();
                session.beginTransaction();
                data = (TblBankEdc) session.find(TblBankEdc.class, id);
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
    public List<TblBankEdc> getAllDataBankEDC() {
        errMsg = "";
        List<TblBankEdc> list = new ArrayList<>();
        if (ClassSession.checkUserSession()) {  //check user current session
            try {
                session = HibernateUtil.getSessionFactory().getCurrentSession();
                session.beginTransaction();
                list = session.getNamedQuery("findAllTblBankEdc").list();
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
    public TblBankEdcBankNetworkCard getDataBankEDCBankNetworkCard(long id) {
        errMsg = "";
        TblBankEdcBankNetworkCard data = null;
        if (ClassSession.checkUserSession()) {  //check user current session
            try {
                session = HibernateUtil.getSessionFactory().getCurrentSession();
                session.beginTransaction();
                data = (TblBankEdcBankNetworkCard) session.find(TblBankEdcBankNetworkCard.class, id);
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
    public List<TblBankEdcBankNetworkCard> getAllDataBankEDCBankNetworkCard() {
        errMsg = "";
        List<TblBankEdcBankNetworkCard> list = new ArrayList<>();
        if (ClassSession.checkUserSession()) {  //check user current session
            try {
                session = HibernateUtil.getSessionFactory().getCurrentSession();
                session.beginTransaction();
                list = session.getNamedQuery("findAllTblBankEdcBankNetworkCard").list();
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
    public List<TblBankEdcBankNetworkCard> getAllDataBankEDCBankNetworkCardByIDBankEDC(long idBankEDC) {
        errMsg = "";
        List<TblBankEdcBankNetworkCard> list = new ArrayList<>();
        if (ClassSession.checkUserSession()) {  //check user current session
            try {
                session = HibernateUtil.getSessionFactory().getCurrentSession();
                session.beginTransaction();
                list = session.getNamedQuery("findAllTblBankEdcBankNetworkCardByIDBankEdc")
                        .setParameter("idBankEDC", idBankEDC)
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
    public TblBankEventCard insertDataBankEventCard(TblBankEventCard bankEventCard) {
        errMsg = "";
        TblBankEventCard tblBankEventCard = bankEventCard;
        if (ClassSession.checkUserSession()) {  //check user current session
            try {
                session = HibernateUtil.getSessionFactory().getCurrentSession();
                session.beginTransaction();
                tblBankEventCard.setCodeEvent(ClassCoder.getCode("Event Card", session));
                tblBankEventCard.setCreateDate(Timestamp.valueOf(LocalDateTime.now()));
                tblBankEventCard.setTblEmployeeByCreateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                tblBankEventCard.setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                tblBankEventCard.setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                tblBankEventCard.setRefRecordStatus(session.find(RefRecordStatus.class, 1));
                session.saveOrUpdate(tblBankEventCard);
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
        return tblBankEventCard;
    }

    @Override
    public boolean updateDataBankEventCard(TblBankEventCard bankEventCard) {
        errMsg = "";
        if (ClassSession.checkUserSession()) {  //check user current session
            try {
                session = HibernateUtil.getSessionFactory().getCurrentSession();
                session.beginTransaction();
                bankEventCard.setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                bankEventCard.setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                session.update(bankEventCard);
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
    public boolean deleteDataBankEventCard(TblBankEventCard bankEventCard) {
        errMsg = "";
        if (ClassSession.checkUserSession()) {  //check user current session
            try {
                session = HibernateUtil.getSessionFactory().getCurrentSession();
                session.beginTransaction();
                if (bankEventCard.getRefRecordStatus().getIdstatus() == 1) {
                    bankEventCard.setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                    bankEventCard.setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                    bankEventCard.setRefRecordStatus(session.find(RefRecordStatus.class, 2));
                    session.update(bankEventCard);
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
    public TblBankEventCard getBankEventCard(long id) {
        errMsg = "";
        TblBankEventCard data = null;
        if (ClassSession.checkUserSession()) {  //check user current session
            try {
                session = HibernateUtil.getSessionFactory().getCurrentSession();
                session.beginTransaction();
                data = (TblBankEventCard) session.find(TblBankEventCard.class, id);
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
    public List<TblBankEventCard> getAllDataBankEventCard() {
        errMsg = "";
        List<TblBankEventCard> list = new ArrayList<>();
        if (ClassSession.checkUserSession()) {  //check user current session
            try {
                session = HibernateUtil.getSessionFactory().getCurrentSession();
                session.beginTransaction();
                list = session.getNamedQuery("findAllTblBankEventCard").list();
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
    public boolean updateDataRoomCardType(TblRoomCardType bank) {
        errMsg = "";
        if (ClassSession.checkUserSession()) {  //check user current session
            try {
                session = HibernateUtil.getSessionFactory().getCurrentSession();
                session.beginTransaction();
                bank.setCreateDate(Timestamp.valueOf(LocalDateTime.now()));
                bank.setTblEmployeeByCreateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                bank.setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                bank.setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                bank.setRefRecordStatus(session.find(RefRecordStatus.class, 1));
                session.saveOrUpdate(bank);
                session.getTransaction().commit();
            } catch (Exception e) {
                System.out.println("execpt - : - " + e);
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
    public List<TblRoomCardType> getAllDataRoomCardType() {
        errMsg = "";
        List<TblRoomCardType> list = new ArrayList<>();
        if (ClassSession.checkUserSession()) {  //check user current session
            try {
                session = HibernateUtil.getSessionFactory().getCurrentSession();
                session.beginTransaction();
                list = session.getNamedQuery("findAllTblRoomCardType").list();
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
    public TblBankCard getDataBankCard(long id) {
        errMsg = "";
        TblBankCard data = null;
        if (ClassSession.checkUserSession()) {  //check user current session
            try {
                session = HibernateUtil.getSessionFactory().getCurrentSession();
                session.beginTransaction();
                data = (TblBankCard) session.find(TblBankCard.class, id);
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
    public List<TblBankCard> getAllDataBankCard() {
        errMsg = "";
        List<TblBankCard> list = new ArrayList<>();
        if (ClassSession.checkUserSession()) {  //check user current session
            try {
                session = HibernateUtil.getSessionFactory().getCurrentSession();
                session.beginTransaction();
                list = session.getNamedQuery("findAllTblBankCard").list();
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
    public List<TblBankCard> getAllDataBankCardByIDBank(long idBank) {
        errMsg = "";
        List<TblBankCard> list = new ArrayList<>();
        if (ClassSession.checkUserSession()) {  //check user current session
            try {
                session = HibernateUtil.getSessionFactory().getCurrentSession();
                session.beginTransaction();
                list = session.getNamedQuery("findAllTblBankCardByIDBank")
                        .setParameter("idBank", idBank)
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
    public RefBankCardType getDataBankCardType(int id) {
        errMsg = "";
        RefBankCardType data = null;
        if (ClassSession.checkUserSession()) {  //check user current session
            try {
                session = HibernateUtil.getSessionFactory().getCurrentSession();
                session.beginTransaction();
                data = (RefBankCardType) session.find(RefBankCardType.class, id);
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
    public List<RefBankCardType> getAllDataBankCardType() {
        errMsg = "";
        List<RefBankCardType> list = new ArrayList<>();
        if (ClassSession.checkUserSession()) {  //check user current session
            try {
                session = HibernateUtil.getSessionFactory().getCurrentSession();
                session.beginTransaction();
                list = session.getNamedQuery("findAllRefBankCardType").list();
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
    public List<TblReservationPaymentWithBankCard> getAllDataReservationPaymentWithBankCardByIDEDCAndPaymentDateInRange(
            long idEDC, 
            Date beginPeiode, 
            Date endPeriode){
        errMsg = "";
        List<TblReservationPaymentWithBankCard> list = new ArrayList<>();
        if (ClassSession.checkUserSession()) {  //check user current session
            try {
                session = HibernateUtil.getSessionFactory().getCurrentSession();
                session.beginTransaction();
                list = session.getNamedQuery("findAllTblReservationPaymentWithBankCardByIDEDCAndPaymentDateInRange")
                        .setParameter("idEDC", idEDC)
                        .setParameter("beginPeriode", beginPeiode)
                        .setParameter("endPeriode", endPeriode)
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
    public TblReservationPayment getDataReservationPayment(long id){
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
    public TblReservationBill getDataReservationBill(long id){
        errMsg = "";
        TblReservationBill data = null;
        if (ClassSession.checkUserSession()) {  //check user current session
            try {
                session = HibernateUtil.getSessionFactory().getCurrentSession();
                session.beginTransaction();
                data = (TblReservationBill) session.find(TblReservationBill.class, id);
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
    public TblReservation getDataReservation(long id){
        errMsg = "";
        TblReservation data = null;
        if (ClassSession.checkUserSession()) {  //check user current session
            try {
                session = HibernateUtil.getSessionFactory().getCurrentSession();
                session.beginTransaction();
                data = (TblReservation) session.find(TblReservation.class, id);
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
    public TblEmployee getDataEmployee(long id){
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
    public TblPeople getDataPeople(long id){
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
    public boolean updateSettleEDCDataReservationPaymentWithBankCard(List<TblReservationPaymentWithBankCard> rpwbcs){
        errMsg = "";
        if (ClassSession.checkUserSession()) {  //check user current session
            try {
                session = HibernateUtil.getSessionFactory().getCurrentSession();
                session.beginTransaction();
                //save or update data purchase order detail
                for (TblReservationPaymentWithBankCard rpwbc : rpwbcs) {
                    //data reservation payment - with bank card
                    rpwbc.setSettleDate(Timestamp.valueOf(LocalDateTime.now()));
                    rpwbc.setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                    rpwbc.setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                    session.saveOrUpdate(rpwbc);
                    //data reservation payment
                    if(rpwbc.getRefEdctransactionStatus().getIdstatus() != 0){  //Sale = '1', auto settle
                        rpwbc.getTblReservationPayment().setSettleDate(Timestamp.valueOf(LocalDateTime.now()));
                    }
                    rpwbc.getTblReservationPayment().setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                    rpwbc.getTblReservationPayment().setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                    session.saveOrUpdate(rpwbc.getTblReservationPayment());
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
    
    //--------------------------------------------------------------------------
    @Override
    public List<TblReservationPaymentWithBankCard> getAllDataReservationPaymentWithBankCardByIDEDC(long idEDC){
        errMsg = "";
        List<TblReservationPaymentWithBankCard> list = new ArrayList<>();
        if (ClassSession.checkUserSession()) {  //check user current session
            try {
                session = HibernateUtil.getSessionFactory().getCurrentSession();
                session.beginTransaction();
                list = session.getNamedQuery("findAllTblReservationPaymentWithBankCardByIDEDC")
                        .setParameter("idEDC", idEDC)
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
    public RefEdctransactionStatus getDataEDCTransactionStatus(int id){
        errMsg = "";
        RefEdctransactionStatus data = null;
        if (ClassSession.checkUserSession()) {  //check user current session
            try {
                session = HibernateUtil.getSessionFactory().getCurrentSession();
                session.beginTransaction();
                data = (RefEdctransactionStatus) session.find(RefEdctransactionStatus.class, id);
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
    public List<RefEdctransactionStatus> getAllDataEDCTransactionStatus(){
        errMsg = "";
        List<RefEdctransactionStatus> list = new ArrayList<>();
        if (ClassSession.checkUserSession()) {  //check user current session
            try {
                session = HibernateUtil.getSessionFactory().getCurrentSession();
                session.beginTransaction();
                list = session.getNamedQuery("findAllRefEdctransactionStatus")
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
