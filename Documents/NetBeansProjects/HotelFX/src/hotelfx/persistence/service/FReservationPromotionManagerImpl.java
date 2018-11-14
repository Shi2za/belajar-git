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
import hotelfx.persistence.model.RefRecordStatus;
import hotelfx.persistence.model.RefVoucherStatus;
import hotelfx.persistence.model.SysDataHardCode;
import hotelfx.persistence.model.TblBank;
import hotelfx.persistence.model.TblBankCard;
import hotelfx.persistence.model.TblBankEventCard;
import hotelfx.persistence.model.TblEmployee;
import hotelfx.persistence.model.TblHotelEvent;
import hotelfx.persistence.model.TblItem;
import hotelfx.persistence.model.TblReservationVoucher;
import hotelfx.persistence.model.TblRoomService;
import hotelfx.persistence.model.TblRoomType;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import org.hibernate.Session;

/**
 *
 * @author ANDRI
 */
public class FReservationPromotionManagerImpl implements FReservationPromotionManager {

    private Session session;

    private String errMsg;

    public FReservationPromotionManagerImpl() {

    }

    @Override
    public TblReservationVoucher insertDataVoucher(TblReservationVoucher voucher) {
        errMsg = "";
        TblReservationVoucher tblVoucher = voucher;
        if (ClassSession.checkUserSession()) {  //check user current session
            try {
                session = HibernateUtil.getSessionFactory().getCurrentSession();
                session.beginTransaction();
                tblVoucher.setCodeVoucher(ClassCoder.getCode("Reservation Voucher", session));
                tblVoucher.setRefVoucherStatus(session.find(RefVoucherStatus.class, 0));
                tblVoucher.setCreateDate(Timestamp.valueOf(LocalDateTime.now()));
                tblVoucher.setTblEmployeeByCreateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                tblVoucher.setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                tblVoucher.setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                tblVoucher.setRefRecordStatus(session.find(RefRecordStatus.class, 1));
                session.saveOrUpdate(tblVoucher);
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
        return tblVoucher;
    }

    @Override
    public boolean insertDataVoucher(List<TblReservationVoucher> vouchers) {
        errMsg = "";
        if (ClassSession.checkUserSession()) {  //check user current session
            try {
                session = HibernateUtil.getSessionFactory().getCurrentSession();
                session.beginTransaction();
                for (TblReservationVoucher voucher : vouchers) {
                    voucher.setCodeVoucher(ClassCoder.getCode("Reservation Voucher", session));
                    voucher.setRefVoucherStatus(session.find(RefVoucherStatus.class, 0));
                    voucher.setCreateDate(Timestamp.valueOf(LocalDateTime.now()));
                    voucher.setTblEmployeeByCreateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                    voucher.setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                    voucher.setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                    voucher.setRefRecordStatus(session.find(RefRecordStatus.class, 1));
                    session.saveOrUpdate(voucher);
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
    public boolean updateDataVoucher(TblReservationVoucher voucher) {
        errMsg = "";
        if (ClassSession.checkUserSession()) {  //check user current session
            try {
                session = HibernateUtil.getSessionFactory().getCurrentSession();
                session.beginTransaction();
                voucher.setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                voucher.setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                session.update(voucher);
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
    public boolean updateDataVoucherToReadyToUsed(TblReservationVoucher voucher) {
        errMsg = "";
        if (ClassSession.checkUserSession()) {  //check user current session
            try {
                session = HibernateUtil.getSessionFactory().getCurrentSession();
                session.beginTransaction();
                voucher.setRefVoucherStatus(session.find(RefVoucherStatus.class, 1));
                voucher.setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                voucher.setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                session.update(voucher);
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
    public boolean updateDataVoucherToObsolete(TblReservationVoucher voucher) {
        errMsg = "";
        if (ClassSession.checkUserSession()) {  //check user current session
            try {
                session = HibernateUtil.getSessionFactory().getCurrentSession();
                session.beginTransaction();
                voucher.setRefVoucherStatus(session.find(RefVoucherStatus.class, 2));
                voucher.setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                voucher.setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                session.update(voucher);
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
    public boolean deleteDataVoucher(TblReservationVoucher voucher) {
        errMsg = "";
        if (ClassSession.checkUserSession()) {  //check user current session
            try {
                session = HibernateUtil.getSessionFactory().getCurrentSession();
                session.beginTransaction();
                if (voucher.getRefRecordStatus().getIdstatus() == 1) {
                    voucher.setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                    voucher.setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                    voucher.setRefRecordStatus(session.find(RefRecordStatus.class, 2));
                    session.update(voucher);
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
    public TblReservationVoucher getVoucher(long id) {
        errMsg = "";
        TblReservationVoucher data = null;
        if (ClassSession.checkUserSession()) {  //check user current session
            try {
                session = HibernateUtil.getSessionFactory().getCurrentSession();
                session.beginTransaction();
                data = (TblReservationVoucher) session.find(TblReservationVoucher.class, id);
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
    public List<TblReservationVoucher> getAllDataVoucher() {
        errMsg = "";
        List<TblReservationVoucher> list = new ArrayList<>();
        if (ClassSession.checkUserSession()) {  //check user current session
            try {
                session = HibernateUtil.getSessionFactory().getCurrentSession();
                session.beginTransaction();
                list = session.getNamedQuery("findAllTblReservationVoucher").list();
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
    public RefVoucherStatus getDataVoucherStatus(int id) {
        errMsg = "";
        RefVoucherStatus data = null;
        if (ClassSession.checkUserSession()) {  //check user current session
            try {
                session = HibernateUtil.getSessionFactory().getCurrentSession();
                session.beginTransaction();
                data = (RefVoucherStatus) session.find(RefVoucherStatus.class, id);
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
    public TblHotelEvent insertDataHotelEvent(TblHotelEvent hotelEvent) {
        errMsg = "";
        TblHotelEvent tblHotelEvent = hotelEvent;
        if (ClassSession.checkUserSession()) {  //check user current session
            try {
                session = HibernateUtil.getSessionFactory().getCurrentSession();
                session.beginTransaction();
                tblHotelEvent.setCodeEvent(ClassCoder.getCode("Hotel Event", session));
                tblHotelEvent.setCreateDate(Timestamp.valueOf(LocalDateTime.now()));
                tblHotelEvent.setTblEmployeeByCreateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                tblHotelEvent.setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                tblHotelEvent.setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                tblHotelEvent.setRefRecordStatus(session.find(RefRecordStatus.class, 1));
                session.saveOrUpdate(tblHotelEvent);
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
        return tblHotelEvent;
    }

    @Override
    public boolean updateDataHotelEvent(TblHotelEvent hotelEvent) {
        errMsg = "";
        if (ClassSession.checkUserSession()) {  //check user current session
            try {
                session = HibernateUtil.getSessionFactory().getCurrentSession();
                session.beginTransaction();
                hotelEvent.setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                hotelEvent.setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                session.update(hotelEvent);
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
    public boolean deleteDataHotelEvent(TblHotelEvent hotelEvent) {
        errMsg = "";
        if (ClassSession.checkUserSession()) {  //check user current session
            try {
                session = HibernateUtil.getSessionFactory().getCurrentSession();
                session.beginTransaction();
                if (hotelEvent.getRefRecordStatus().getIdstatus() == 1) {
                    hotelEvent.setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                    hotelEvent.setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                    hotelEvent.setRefRecordStatus(session.find(RefRecordStatus.class, 2));
                    session.update(hotelEvent);
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
    public TblHotelEvent getHotelEvent(long id) {
        errMsg = "";
        TblHotelEvent data = null;
        if (ClassSession.checkUserSession()) {  //check user current session
            try {
                session = HibernateUtil.getSessionFactory().getCurrentSession();
                session.beginTransaction();
                data = (TblHotelEvent) session.find(TblHotelEvent.class, id);
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
    public List<TblHotelEvent> getAllDataHotelEvent() {
        errMsg = "";
        List<TblHotelEvent> list = new ArrayList<>();
        if (ClassSession.checkUserSession()) {  //check user current session
            try {
                session = HibernateUtil.getSessionFactory().getCurrentSession();
                session.beginTransaction();
                list = session.getNamedQuery("findAllTblHotelEvent").list();
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
    public boolean updateDataDiscountable(List<TblRoomType> roomTypes,
            List<TblRoomService> roomServices,
            List<TblItem> items) {
        errMsg = "";
        if (ClassSession.checkUserSession()) {  //check user current session
            try {
                session = HibernateUtil.getSessionFactory().getCurrentSession();
                session.beginTransaction();
                //data room type
                for (TblRoomType data : roomTypes) {
                    data.setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                    data.setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                    session.update(data);
                }
                //data room service
                for (TblRoomService data : roomServices) {
                    data.setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                    data.setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                    session.update(data);
                }
                //data item
                for (TblItem data : items) {
                    data.setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                    data.setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                    session.update(data);
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
    public List<TblRoomType> getAllDataRoomType() {
        errMsg = "";
        List<TblRoomType> list = new ArrayList<>();
        if (ClassSession.checkUserSession()) {  //check user current session
            try {
                session = HibernateUtil.getSessionFactory().getCurrentSession();
                session.beginTransaction();
                list = session.getNamedQuery("findAllTblRoomType").list();
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
    public List<TblRoomService> getAllDataRoomService() {
        errMsg = "";
        List<TblRoomService> list = new ArrayList<>();
        if (ClassSession.checkUserSession()) {  //check user current session
            try {
                session = HibernateUtil.getSessionFactory().getCurrentSession();
                session.beginTransaction();
                list = session.getNamedQuery("findAllTblRoomService").list();
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
    public List<TblItem> getAllDataItem() {
        errMsg = "";
        List<TblItem> list = new ArrayList<>();
        if (ClassSession.checkUserSession()) {  //check user current session
            try {
                session = HibernateUtil.getSessionFactory().getCurrentSession();
                session.beginTransaction();
                list = session.getNamedQuery("findAllTblItem").list();
                for (int i = list.size() - 1; i > -1; i--) {
                    if (!list.get(i).getGuestStatus()) {   //!Guest
                        list.remove(i);
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
    public String getErrorMessage() {
        return errMsg;
    }

}
