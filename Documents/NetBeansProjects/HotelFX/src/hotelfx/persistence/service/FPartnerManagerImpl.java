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
import hotelfx.persistence.model.RefHotelReceivableType;
import hotelfx.persistence.model.RefPartnerType;
import hotelfx.persistence.model.RefRecordStatus;
import hotelfx.persistence.model.RefReservationStatus;
import hotelfx.persistence.model.SysDataHardCode;
import hotelfx.persistence.model.TblBank;
import hotelfx.persistence.model.TblBankAccount;
import hotelfx.persistence.model.TblCompanyBalance;
import hotelfx.persistence.model.TblCompanyBalanceBankAccount;
import hotelfx.persistence.model.TblEmployee;
import hotelfx.persistence.model.TblGuaranteeLetterItemDetail;
import hotelfx.persistence.model.TblHotelFinanceTransaction;
import hotelfx.persistence.model.TblHotelFinanceTransactionHotelReceivable;
import hotelfx.persistence.model.TblHotelFinanceTransactionWithCash;
import hotelfx.persistence.model.TblHotelFinanceTransactionWithCekGiro;
import hotelfx.persistence.model.TblHotelFinanceTransactionWithTransfer;
import hotelfx.persistence.model.TblHotelInvoice;
import hotelfx.persistence.model.TblHotelReceivable;
import hotelfx.persistence.model.TblPartner;
import hotelfx.persistence.model.TblPartnerBankAccount;
import hotelfx.persistence.model.TblReservation;
import hotelfx.persistence.model.TblReservationBill;
import hotelfx.persistence.model.TblReservationPayment;
import hotelfx.persistence.model.TblReservationPaymentWithGuaranteePayment;
import hotelfx.persistence.model.TblReservationRoomPriceDetail;
import hotelfx.persistence.model.TblReservationRoomTypeDetail;
import hotelfx.persistence.model.TblReservationRoomTypeDetailRoomPriceDetail;
import hotelfx.persistence.model.TblReservationRoomTypeDetailTravelAgentDiscountDetail;
import hotelfx.persistence.model.TblReservationTravelAgentDiscountDetail;
import hotelfx.persistence.model.TblRoom;
import hotelfx.persistence.model.TblRoomType;
import hotelfx.persistence.model.TblTravelAgent;
import hotelfx.persistence.model.TblTravelAgentRoomType;
import java.sql.Date;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import org.hibernate.Session;
import org.hibernate.query.Query;

/**
 *
 * @author ANDRI
 */
public class FPartnerManagerImpl implements FPartnerManager {

    private Session session;

    private String errMsg;

    public FPartnerManagerImpl() {

    }

    @Override
    public TblTravelAgent insertDataTravelAgent(TblTravelAgent travelAgent,
            List<TblPartnerBankAccount> partnerBankAccount) {
        errMsg = "";
        TblTravelAgent tblTravelAgent = travelAgent;
        if (ClassSession.checkUserSession()) {  //check user current session
            try {
                session = HibernateUtil.getSessionFactory().getCurrentSession();
                session.beginTransaction();
                //partner
                tblTravelAgent.getTblPartner().setCodePartner(ClassCoder.getCode("Partner", session));
                tblTravelAgent.getTblPartner().setRefPartnerType(session.find(RefPartnerType.class, 0));
                tblTravelAgent.getTblPartner().setCreateDate(Timestamp.valueOf(LocalDateTime.now()));
                tblTravelAgent.getTblPartner().setTblEmployeeByCreateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                tblTravelAgent.getTblPartner().setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                tblTravelAgent.getTblPartner().setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                tblTravelAgent.getTblPartner().setRefRecordStatus(session.find(RefRecordStatus.class, 1));
                session.saveOrUpdate(tblTravelAgent.getTblPartner());
                //travel agent
                tblTravelAgent.setCreateDate(Timestamp.valueOf(LocalDateTime.now()));
                tblTravelAgent.setTblEmployeeByCreateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                tblTravelAgent.setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                tblTravelAgent.setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                tblTravelAgent.setRefRecordStatus(session.find(RefRecordStatus.class, 1));
                session.saveOrUpdate(tblTravelAgent);
                //data partner bank account
                //save or update data bank account n partner bank acccount
                for (TblPartnerBankAccount pba : partnerBankAccount) {
                    //data bank account
                    pba.getTblBankAccount().setCreateDate(Timestamp.valueOf(LocalDateTime.now()));
                    pba.getTblBankAccount().setTblEmployeeByCreateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                    pba.getTblBankAccount().setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                    pba.getTblBankAccount().setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                    pba.getTblBankAccount().setRefRecordStatus(session.find(RefRecordStatus.class, 1));
                    session.saveOrUpdate(pba.getTblBankAccount());
                    //data partner bank account
                    pba.setRefBankAccountHolderStatus(session.find(RefBankAccountHolderStatus.class, 0));
                    pba.setCreateDate(Timestamp.valueOf(LocalDateTime.now()));
                    pba.setTblEmployeeByCreateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                    pba.setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                    pba.setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                    pba.setRefRecordStatus(session.find(RefRecordStatus.class, 1));
                    session.saveOrUpdate(pba);
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
        return tblTravelAgent;
    }

//    @Override
//    public TblTravelAgent insertDataTravelAgent(TblTravelAgent travelAgent,
//            List<TblTravelAgentRoomType> travelAgentRoomTypes) {
//        TblTravelAgent tblTravelAgent = travelAgent;
//        errMsg = "";
//        try {
//            session = HibernateUtil.getSessionFactory().getCurrentSession();
//            session.beginTransaction();
//            //partner
//            tblTravelAgent.getTblPartner().setCodePartner(ClassCoder.getCode("Partner", session));
//            tblTravelAgent.getTblPartner().setRefPartnerType(session.find(RefPartnerType.class, 0));
//            tblTravelAgent.getTblPartner().setCreateDate(Timestamp.valueOf(LocalDateTime.now()));
//            tblTravelAgent.getTblPartner().setTblEmployeeByCreateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
//            tblTravelAgent.getTblPartner().setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
//            tblTravelAgent.getTblPartner().setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
//            tblTravelAgent.getTblPartner().setRefRecordStatus(session.find(RefRecordStatus.class, 1));
//            session.saveOrUpdate(tblTravelAgent.getTblPartner());
//            //travel agent
//            tblTravelAgent.setCreateDate(Timestamp.valueOf(LocalDateTime.now()));
//            tblTravelAgent.setTblEmployeeByCreateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
//            tblTravelAgent.setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
//            tblTravelAgent.setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
//            tblTravelAgent.setRefRecordStatus(session.find(RefRecordStatus.class, 1));
//            session.saveOrUpdate(tblTravelAgent);
//            //travel agent - room type
//            for (TblTravelAgentRoomType travelAgentRoomType : travelAgentRoomTypes) {
//                travelAgentRoomType.setCreateDate(Timestamp.valueOf(LocalDateTime.now()));
//                travelAgentRoomType.setTblEmployeeByCreateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
//                travelAgentRoomType.setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
//                travelAgentRoomType.setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
//                travelAgentRoomType.setRefRecordStatus(session.find(RefRecordStatus.class, 1));
//                session.saveOrUpdate(travelAgentRoomType);
//            }
//            session.getTransaction().commit();
//        } catch (Exception e) {
//            if (session.getTransaction().isActive()) {
//                session.getTransaction().rollback();
//            }
//            errMsg = e.getMessage();
//            return null;
//        } finally {
//            //session.close();
//        }
//        return tblTravelAgent;
//    }
    @Override
    public boolean updateDataTravelAgent(TblTravelAgent travelAgent,
            List<TblPartnerBankAccount> partnerBankAccount) {
        errMsg = "";
        if (ClassSession.checkUserSession()) {  //check user current session
            try {
                session = HibernateUtil.getSessionFactory().getCurrentSession();
                session.beginTransaction();
                //partner
                travelAgent.getTblPartner().setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                travelAgent.getTblPartner().setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                session.update(travelAgent.getTblPartner());
                //travel agent
                travelAgent.setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                travelAgent.setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                session.update(travelAgent);
                //delete all partner bank account
                Query rs = session.getNamedQuery("deleteAllTblPartnerBankAccount")
                        .setParameter("idPartner", travelAgent.getTblPartner().getIdpartner())
                        .setParameter("idEmployee", ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee());
                errMsg = (String) rs.uniqueResult();
                if (!errMsg.equals("")) {
                    if (session.getTransaction().isActive()) {
                        session.getTransaction().rollback();
                    }
                    return false;
                }
                //save or update data bank account n partner bank acccount
                for (TblPartnerBankAccount pba : partnerBankAccount) {
                    //data bank account
                    pba.getTblBankAccount().setCreateDate(Timestamp.valueOf(LocalDateTime.now()));
                    pba.getTblBankAccount().setTblEmployeeByCreateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                    pba.getTblBankAccount().setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                    pba.getTblBankAccount().setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                    pba.getTblBankAccount().setRefRecordStatus(session.find(RefRecordStatus.class, 1));
                    session.saveOrUpdate(pba.getTblBankAccount());
                    //data partner bank account
                    pba.setRefBankAccountHolderStatus(session.find(RefBankAccountHolderStatus.class, 0));
                    pba.setCreateDate(Timestamp.valueOf(LocalDateTime.now()));
                    pba.setTblEmployeeByCreateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                    pba.setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                    pba.setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                    pba.setRefRecordStatus(session.find(RefRecordStatus.class, 1));
                    session.saveOrUpdate(pba);
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

//    @Override
//    public boolean updateDataTravelAgent(TblTravelAgent travelAgent,
//            List<TblTravelAgentRoomType> travelAgentRoomTypes) {
//        errMsg = "";
//        try {
//            session = HibernateUtil.getSessionFactory().getCurrentSession();
//            session.beginTransaction();
//            //partner
//            travelAgent.getTblPartner().setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
//            travelAgent.getTblPartner().setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
//            session.update(travelAgent.getTblPartner());
//            //travel agent
//            travelAgent.setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
//            travelAgent.setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
//            session.update(travelAgent);
//            //travel agent - room type
//            for (TblTravelAgentRoomType travelAgentRoomType : travelAgentRoomTypes) {
//                travelAgentRoomType.setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
//                travelAgentRoomType.setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
//                session.update(travelAgentRoomType);
//            }
//            session.getTransaction().commit();
//        } catch (Exception e) {
//            if (session.getTransaction().isActive()) {
//                session.getTransaction().rollback();
//            }
//            errMsg = e.getMessage();
//            return false;
//        } finally {
//            //session.close();
//        }
//        return true;
//    }
    @Override
    public boolean deleteDataTravelAgent(TblTravelAgent travelAgent) {
        errMsg = "";
        if (ClassSession.checkUserSession()) {  //check user current session
            try {
                session = HibernateUtil.getSessionFactory().getCurrentSession();
                session.beginTransaction();
                if (travelAgent.getRefRecordStatus().getIdstatus() == 1) {
                    //data partner
                    travelAgent.getTblPartner().setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                    travelAgent.getTblPartner().setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                    travelAgent.getTblPartner().setRefRecordStatus(session.find(RefRecordStatus.class, 2));
                    session.update(travelAgent.getTblPartner());
                    //data travel agent
                    travelAgent.setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                    travelAgent.setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                    travelAgent.setRefRecordStatus(session.find(RefRecordStatus.class, 2));
                    session.update(travelAgent);
                    //data partner - bank account
                    Query rs = session.getNamedQuery("deleteAllTblPartnerBankAccount")
                            .setParameter("idPartner", travelAgent.getTblPartner().getIdpartner())
                            .setParameter("idEmployee", ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee());
                    errMsg = (String) rs.uniqueResult();
                    if (!errMsg.equals("")) {
                        if (session.getTransaction().isActive()) {
                            session.getTransaction().rollback();
                        }
                        return false;
                    }
                    //data travel agent - room type
                    rs = session.getNamedQuery("deleteAllTblTravelAgentRoomTypeByIDPartner")
                            .setParameter("idPartner", travelAgent.getTblPartner().getIdpartner())
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

                //data partner
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
    public TblTravelAgent getTravelAgent(long id) {
        errMsg = "";
        TblTravelAgent data = null;
        if (ClassSession.checkUserSession()) {  //check user current session
            try {
                session = HibernateUtil.getSessionFactory().getCurrentSession();
                session.beginTransaction();
                data = (TblTravelAgent) session.find(TblTravelAgent.class, id);
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
    public List<TblTravelAgent> getAllDataTravelAgent() {
        errMsg = "";
        List<TblTravelAgent> list = new ArrayList<>();
        if (ClassSession.checkUserSession()) {  //check user current session
            try {
                session = HibernateUtil.getSessionFactory().getCurrentSession();
                session.beginTransaction();
                list = session.getNamedQuery("findAllTblTravelAgent").list();
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
    public boolean updateDataTravelAgentRoomType(TblTravelAgentRoomType travelAgentRoomType) {
        errMsg = "";
        if (ClassSession.checkUserSession()) {  //check user current session
            try {
                session = HibernateUtil.getSessionFactory().getCurrentSession();
                session.beginTransaction();
                if (checkRoomAvailable(travelAgentRoomType)) {
                    if (travelAgentRoomType.getIdrelation() == 0L) { //insert
                        travelAgentRoomType.setCreateDate(Timestamp.valueOf(LocalDateTime.now()));
                        travelAgentRoomType.setTblEmployeeByCreateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                        travelAgentRoomType.setRefRecordStatus(session.find(RefRecordStatus.class, 1));
                    }
                    travelAgentRoomType.setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                    travelAgentRoomType.setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                    session.saveOrUpdate(travelAgentRoomType);
                } else {
                    if (session.getTransaction().isActive()) {
                        session.getTransaction().rollback();
                    }
                    errMsg = "Terjadi kesalahan pada data jumlah kamar, silahkan periksa kembali data jumlah kamar..!";
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
    public boolean updateDataTravelAgentRoomType(List<TblTravelAgentRoomType> travelAgentRoomTypes) {
        errMsg = "";
        if (ClassSession.checkUserSession()) {  //check user current session
            try {
                session = HibernateUtil.getSessionFactory().getCurrentSession();
                session.beginTransaction();
                for (TblTravelAgentRoomType travelAgentRoomType : travelAgentRoomTypes) {
                    if (checkRoomAvailable(travelAgentRoomType)) {
                        if (travelAgentRoomType.getIdrelation() == 0L) { //insert
                            travelAgentRoomType.setCreateDate(Timestamp.valueOf(LocalDateTime.now()));
                            travelAgentRoomType.setTblEmployeeByCreateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                            travelAgentRoomType.setRefRecordStatus(session.find(RefRecordStatus.class, 1));
                        }
                        travelAgentRoomType.setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                        travelAgentRoomType.setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                        session.saveOrUpdate(travelAgentRoomType);
                    } else {
                        if (session.getTransaction().isActive()) {
                            session.getTransaction().rollback();
                        }
                        errMsg = "Terjadi kesalahan pada data jumlah kamar, silahkan periksa kembali data jumlah kamar..!";
                        return false;
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

    private boolean checkRoomAvailable(TblTravelAgentRoomType dataTravelAgentRoomType) {
        LocalDate date = LocalDate.of(dataTravelAgentRoomType.getAvailableDate().getYear() + 1900,
                dataTravelAgentRoomType.getAvailableDate().getMonth() + 1,
                dataTravelAgentRoomType.getAvailableDate().getDate());
        int availableMinNumber = getRoomReservedNumber(dataTravelAgentRoomType.getTblRoomType(), date, dataTravelAgentRoomType.getTblPartner());
        int availableMaxNumber = getRoomAvailableNumber(dataTravelAgentRoomType.getTblRoomType(), date, dataTravelAgentRoomType.getTblPartner())
                + (getRoomAvailableNumber(dataTravelAgentRoomType.getTblRoomType(), date, null)
                - getRoomReservedNumber(dataTravelAgentRoomType.getTblRoomType(), date, null));
        return (dataTravelAgentRoomType.getRoomNumber() >= availableMinNumber && dataTravelAgentRoomType.getRoomNumber() <= availableMaxNumber);
    }

    private int getRoomReservedNumber(TblRoomType roomType, LocalDate date, TblPartner partner) {
        int result = 0;
        if (partner == null) {
            List<TblReservationRoomPriceDetail> roomPriceDetails = session.getNamedQuery("findAllTblRoomPriceDetailByDetailDate")
                    .setParameter("detailDate", Date.valueOf(date))
                    .list();
            for (TblReservationRoomPriceDetail roomPriceDetail : roomPriceDetails) {
                List<TblReservationRoomTypeDetailRoomPriceDetail> tempList = session.getNamedQuery("findAllTblReservationRoomTypeDetailRoomPriceDetailByIDRoomPriceDetail")
                        .setParameter("idRoomPriceDetail", roomPriceDetail.getIddetail())
                        .list();
                TblReservationRoomTypeDetailRoomPriceDetail relation = tempList.isEmpty() ? null : tempList.get(0);
                if (relation != null) {
                    TblReservationRoomTypeDetail roomTypeDetail = (TblReservationRoomTypeDetail) session.find(TblReservationRoomTypeDetail.class, relation.getTblReservationRoomTypeDetail().getIddetail());
                    roomTypeDetail.setTblReservation(session.find(TblReservation.class, roomTypeDetail.getTblReservation().getIdreservation()));
                    roomTypeDetail.getTblReservation().setRefReservationStatus(session.find(RefReservationStatus.class, roomTypeDetail.getTblReservation().getRefReservationStatus().getIdstatus()));
                    if (roomTypeDetail.getTblRoomType().getIdroomType() == roomType.getIdroomType()
                            && roomTypeDetail.getRefReservationOrderByType().getIdtype() == 0 //Customer = '0'
                            && (roomTypeDetail.getTblReservation().getRefReservationStatus().getIdstatus() == 1 //Reserved = '1'
                            || roomTypeDetail.getTblReservation().getRefReservationStatus().getIdstatus() == 2)) {    //Booked = '2'
                        result++;
                    }
                    session.evict(roomTypeDetail.getTblReservation().getRefReservationStatus());
                    session.evict(roomTypeDetail.getTblReservation());
                    session.evict(roomTypeDetail);
                    session.evict(relation);
                }
            }
        } else {  //Travel Agent
            List<TblReservationRoomPriceDetail> roomPriceDetails = session.getNamedQuery("findAllTblRoomPriceDetailByDetailDate")
                    .setParameter("detailDate", Date.valueOf(date))
                    .list();
            for (TblReservationRoomPriceDetail roomPriceDetail : roomPriceDetails) {
                List<TblReservationRoomTypeDetailRoomPriceDetail> tempList = session.getNamedQuery("findAllTblReservationRoomTypeDetailRoomPriceDetailByIDRoomPriceDetail")
                        .setParameter("idRoomPriceDetail", roomPriceDetail.getIddetail())
                        .list();
                TblReservationRoomTypeDetailRoomPriceDetail relation = tempList.isEmpty() ? null : tempList.get(0);
                if (relation != null) {
                    TblReservationRoomTypeDetail roomTypeDetail = (TblReservationRoomTypeDetail) session.find(TblReservationRoomTypeDetail.class, relation.getTblReservationRoomTypeDetail().getIddetail());
                    roomTypeDetail.setTblReservation((TblReservation) session.find(TblReservation.class, roomTypeDetail.getTblReservation().getIdreservation()));
                    roomTypeDetail.getTblReservation().setRefReservationStatus((RefReservationStatus) session.find(RefReservationStatus.class, roomTypeDetail.getTblReservation().getRefReservationStatus().getIdstatus()));
                    if (roomTypeDetail.getTblRoomType().getIdroomType() == roomType.getIdroomType()
                            && roomTypeDetail.getRefReservationOrderByType().getIdtype() == 1 //Travel Agent = '1'
                            && (roomTypeDetail.getTblReservation().getRefReservationStatus().getIdstatus() == 1 //Reserved = '1'
                            || roomTypeDetail.getTblReservation().getRefReservationStatus().getIdstatus() == 2)) {    //Booked = '2'
                        List<TblReservationRoomTypeDetailTravelAgentDiscountDetail> tempList1 = session.getNamedQuery("findAllTblReservationRoomTypeDetailTravelAgentDiscountDetailByIDRoomTypeDetail")
                                .setParameter("idRoomTypeDetail", roomTypeDetail.getIddetail())
                                .list();
                        TblReservationRoomTypeDetailTravelAgentDiscountDetail relation1 = tempList1.isEmpty() ? null : tempList1.get(0);
                        if (relation1 != null) {
                            TblReservationTravelAgentDiscountDetail travelAgentDiscountDetail
                                    = (TblReservationTravelAgentDiscountDetail) session.find(TblReservationTravelAgentDiscountDetail.class, relation1.getTblReservationTravelAgentDiscountDetail().getIddetail());
                            if (partner.getIdpartner() == travelAgentDiscountDetail.getTblPartner().getIdpartner()) {
                                result++;
                            }
                            session.evict(travelAgentDiscountDetail);
                            session.evict(relation1);
                        }
                    }
                    session.evict(roomTypeDetail.getTblReservation().getRefReservationStatus());
                    session.evict(roomTypeDetail.getTblReservation());
                    session.evict(roomTypeDetail);
                    session.evict(relation);
                }
            }
        }
        return result;
    }

    private int getRoomAvailableNumber(TblRoomType roomType, LocalDate date, TblPartner partner) {
        int result = 0;
        if (partner == null) {
            List<TblRoom> listRoom = session.getNamedQuery("findAllTblRoomByIDRoomType")
                    .setParameter("idRoomType", roomType.getIdroomType())
                    .list();
            for (int i = listRoom.size() - 1; i > -1; i--) {
                if (listRoom.get(i).getRefRoomStatus().getIdstatus() == 6) {  //Out Of Order = '6'
                    listRoom.remove(i);
                }
            }
            result = listRoom.size();
            List<TblTravelAgentRoomType> travelAgentRoomTypes = session.getNamedQuery("findAllTblTravelAgentRoomTypeByIDRoomTypeAndAvailableDate")
                    .setParameter("idRoomType", roomType.getIdroomType())
                    .setParameter("availableDate", Date.valueOf(date))
                    .list();
            for (TblTravelAgentRoomType travelAgentRoomType : travelAgentRoomTypes) {
                result -= travelAgentRoomType.getRoomNumber();
                session.evict(travelAgentRoomType);
            }
        } else {  //Travel Agent
            List<TblTravelAgentRoomType> tempList = session.getNamedQuery("findAllTblTravelAgentRoomTypeByIDRoomTypeAndIDPartnerAndAvailableDate")
                    .setParameter("idRoomType", roomType.getIdroomType())
                    .setParameter("idPartner", partner.getIdpartner())
                    .setParameter("availableDate", Date.valueOf(date))
                    .list();
            TblTravelAgentRoomType travelAgentRoomType = tempList.isEmpty() ? null : tempList.get(0);
            if (travelAgentRoomType != null) {
                result = travelAgentRoomType.getRoomNumber();
                session.evict(travelAgentRoomType);
            }
        }
        return result;
    }

    //--------------------------------------------------------------------------
    @Override
    public TblPartner getPartner(long id) {
        errMsg = "";
        TblPartner data = null;
        if (ClassSession.checkUserSession()) {  //check user current session
            try {
                session = HibernateUtil.getSessionFactory().getCurrentSession();
                session.beginTransaction();
                data = (TblPartner) session.find(TblPartner.class, id);
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
    public List<TblPartner> getAllDataPartner() {
        errMsg = "";
        List<TblPartner> list = new ArrayList<>();
        if (ClassSession.checkUserSession()) {  //check user current session
            try {
                session = HibernateUtil.getSessionFactory().getCurrentSession();
                session.beginTransaction();
                list = session.getNamedQuery("findAllTblPartner").list();
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
    public TblPartner insertDataCorporate(TblPartner corporate,
            List<TblPartnerBankAccount> partnerBankAccount) {
        errMsg = "";
        TblPartner tblCorporate = corporate;
        if (ClassSession.checkUserSession()) {  //check user current session
            try {
                session = HibernateUtil.getSessionFactory().getCurrentSession();
                session.beginTransaction();
                //data partner (corporate)
                tblCorporate.setCodePartner(ClassCoder.getCode("Partner", session));
                tblCorporate.setRefPartnerType(session.find(RefPartnerType.class, 1));
                tblCorporate.setCreateDate(Timestamp.valueOf(LocalDateTime.now()));
                tblCorporate.setTblEmployeeByCreateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                tblCorporate.setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                tblCorporate.setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                tblCorporate.setRefRecordStatus(session.find(RefRecordStatus.class, 1));
                session.saveOrUpdate(tblCorporate);
                //data partner bank account
                //save or update data bank account n partner bank acccount
                for (TblPartnerBankAccount pba : partnerBankAccount) {
                    //data bank account
                    pba.getTblBankAccount().setCreateDate(Timestamp.valueOf(LocalDateTime.now()));
                    pba.getTblBankAccount().setTblEmployeeByCreateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                    pba.getTblBankAccount().setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                    pba.getTblBankAccount().setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                    pba.getTblBankAccount().setRefRecordStatus(session.find(RefRecordStatus.class, 1));
                    session.saveOrUpdate(pba.getTblBankAccount());
                    //data partner bank account
                    pba.setRefBankAccountHolderStatus(session.find(RefBankAccountHolderStatus.class, 0));
                    pba.setCreateDate(Timestamp.valueOf(LocalDateTime.now()));
                    pba.setTblEmployeeByCreateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                    pba.setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                    pba.setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                    pba.setRefRecordStatus(session.find(RefRecordStatus.class, 1));
                    session.saveOrUpdate(pba);
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
        return tblCorporate;
    }

    @Override
    public boolean updateDataCorporate(TblPartner corporate,
            List<TblPartnerBankAccount> partnerBankAccount) {
        errMsg = "";
        if (ClassSession.checkUserSession()) {  //check user current session
            try {
                session = HibernateUtil.getSessionFactory().getCurrentSession();
                session.beginTransaction();
                //data partner (corporate)
                corporate.setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                corporate.setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                session.update(corporate);
                //delete all partner bank account
                Query rs = session.getNamedQuery("deleteAllTblPartnerBankAccount")
                        .setParameter("idPartner", corporate.getIdpartner())
                        .setParameter("idEmployee", ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee());
                errMsg = (String) rs.uniqueResult();
                if (!errMsg.equals("")) {
                    if (session.getTransaction().isActive()) {
                        session.getTransaction().rollback();
                    }
                    return false;
                }
                //save or update data bank account n partner bank acccount
                for (TblPartnerBankAccount pba : partnerBankAccount) {
                    //data bank account
                    pba.getTblBankAccount().setCreateDate(Timestamp.valueOf(LocalDateTime.now()));
                    pba.getTblBankAccount().setTblEmployeeByCreateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                    pba.getTblBankAccount().setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                    pba.getTblBankAccount().setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                    pba.getTblBankAccount().setRefRecordStatus(session.find(RefRecordStatus.class, 1));
                    session.saveOrUpdate(pba.getTblBankAccount());
                    //data partner bank account
                    pba.setRefBankAccountHolderStatus(session.find(RefBankAccountHolderStatus.class, 0));
                    pba.setCreateDate(Timestamp.valueOf(LocalDateTime.now()));
                    pba.setTblEmployeeByCreateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                    pba.setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                    pba.setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                    pba.setRefRecordStatus(session.find(RefRecordStatus.class, 1));
                    session.saveOrUpdate(pba);
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
    public boolean deleteDataCorporate(TblPartner corporate) {
        errMsg = "";
        if (ClassSession.checkUserSession()) {  //check user current session
            try {
                session = HibernateUtil.getSessionFactory().getCurrentSession();
                session.beginTransaction();
                if (corporate.getRefRecordStatus().getIdstatus() == 1) {
                    //data partner (corporate)
                    corporate.setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                    corporate.setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                    corporate.setRefRecordStatus(session.find(RefRecordStatus.class, 2));
                    session.update(corporate);
                    //data partner - bank account
                    Query rs = session.getNamedQuery("deleteAllTblPartnerBankAccount")
                            .setParameter("idPartner", corporate.getIdpartner())
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
    public TblPartner getCorporate(long id) {
        errMsg = "";
        TblPartner data = null;
        if (ClassSession.checkUserSession()) {  //check user current session
            try {
                session = HibernateUtil.getSessionFactory().getCurrentSession();
                session.beginTransaction();
                data = (TblPartner) session.find(TblPartner.class, id);
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
    public List<TblPartner> getAllDataCorporate() {
        errMsg = "";
        List<TblPartner> list = new ArrayList<>();
        if (ClassSession.checkUserSession()) {  //check user current session
            try {
                session = HibernateUtil.getSessionFactory().getCurrentSession();
                session.beginTransaction();
                list = session.getNamedQuery("findAllTblPartnerByIDPartnerType")
                        .setParameter("idType", 1)
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
    public TblPartner insertDataGovernment(TblPartner government,
            List<TblPartnerBankAccount> partnerBankAccount) {
        errMsg = "";
        TblPartner tblGovernment = government;
        if (ClassSession.checkUserSession()) {  //check user current session
            try {
                session = HibernateUtil.getSessionFactory().getCurrentSession();
                session.beginTransaction();
                //data partner (government)
                tblGovernment.setCodePartner(ClassCoder.getCode("Partner", session));
                tblGovernment.setRefPartnerType(session.find(RefPartnerType.class, 2));
                tblGovernment.setCreateDate(Timestamp.valueOf(LocalDateTime.now()));
                tblGovernment.setTblEmployeeByCreateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                tblGovernment.setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                tblGovernment.setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                tblGovernment.setRefRecordStatus(session.find(RefRecordStatus.class, 1));
                session.saveOrUpdate(tblGovernment);
                //data partner bank account
                //save or update data bank account n partner bank acccount
                for (TblPartnerBankAccount pba : partnerBankAccount) {
                    //data bank account
                    pba.getTblBankAccount().setCreateDate(Timestamp.valueOf(LocalDateTime.now()));
                    pba.getTblBankAccount().setTblEmployeeByCreateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                    pba.getTblBankAccount().setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                    pba.getTblBankAccount().setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                    pba.getTblBankAccount().setRefRecordStatus(session.find(RefRecordStatus.class, 1));
                    session.saveOrUpdate(pba.getTblBankAccount());
                    //data partner bank account
                    pba.setRefBankAccountHolderStatus(session.find(RefBankAccountHolderStatus.class, 0));
                    pba.setCreateDate(Timestamp.valueOf(LocalDateTime.now()));
                    pba.setTblEmployeeByCreateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                    pba.setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                    pba.setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                    pba.setRefRecordStatus(session.find(RefRecordStatus.class, 1));
                    session.saveOrUpdate(pba);
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
        return tblGovernment;
    }

    @Override
    public boolean updateDataGovernment(TblPartner government,
            List<TblPartnerBankAccount> partnerBankAccount) {
        errMsg = "";
        if (ClassSession.checkUserSession()) {  //check user current session
            try {
                session = HibernateUtil.getSessionFactory().getCurrentSession();
                session.beginTransaction();
                //data partner (government)
                government.setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                government.setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                session.update(government);
                //delete all partner bank account
                Query rs = session.getNamedQuery("deleteAllTblPartnerBankAccount")
                        .setParameter("idPartner", government.getIdpartner())
                        .setParameter("idEmployee", ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee());
                errMsg = (String) rs.uniqueResult();
                if (!errMsg.equals("")) {
                    if (session.getTransaction().isActive()) {
                        session.getTransaction().rollback();
                    }
                    return false;
                }
                //save or update data bank account n partner bank acccount
                for (TblPartnerBankAccount pba : partnerBankAccount) {
                    //data bank account
                    pba.getTblBankAccount().setCreateDate(Timestamp.valueOf(LocalDateTime.now()));
                    pba.getTblBankAccount().setTblEmployeeByCreateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                    pba.getTblBankAccount().setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                    pba.getTblBankAccount().setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                    pba.getTblBankAccount().setRefRecordStatus(session.find(RefRecordStatus.class, 1));
                    session.saveOrUpdate(pba.getTblBankAccount());
                    //data partner bank account
                    pba.setRefBankAccountHolderStatus(session.find(RefBankAccountHolderStatus.class, 0));
                    pba.setCreateDate(Timestamp.valueOf(LocalDateTime.now()));
                    pba.setTblEmployeeByCreateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                    pba.setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                    pba.setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                    pba.setRefRecordStatus(session.find(RefRecordStatus.class, 1));
                    session.saveOrUpdate(pba);
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
    public boolean deleteDataGovernment(TblPartner government) {
        errMsg = "";
        if (ClassSession.checkUserSession()) {  //check user current session
            try {
                session = HibernateUtil.getSessionFactory().getCurrentSession();
                session.beginTransaction();
                if (government.getRefRecordStatus().getIdstatus() == 1) {
                    //data partner (government)
                    government.setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                    government.setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                    government.setRefRecordStatus(session.find(RefRecordStatus.class, 2));
                    session.update(government);
                    //data partner - bank account
                    Query rs = session.getNamedQuery("deleteAllTblPartnerBankAccount")
                            .setParameter("idPartner", government.getIdpartner())
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
    public TblPartner getGovernment(long id) {
        errMsg = "";
        TblPartner data = null;
        if (ClassSession.checkUserSession()) {  //check user current session
            try {
                session = HibernateUtil.getSessionFactory().getCurrentSession();
                session.beginTransaction();
                data = (TblPartner) session.find(TblPartner.class, id);
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
    public List<TblPartner> getAllDataGovernment() {
        errMsg = "";
        List<TblPartner> list = new ArrayList<>();
        if (ClassSession.checkUserSession()) {  //check user current session
            try {
                session = HibernateUtil.getSessionFactory().getCurrentSession();
                session.beginTransaction();
                list = session.getNamedQuery("findAllTblPartnerByIDPartnerType")
                        .setParameter("idType", 2)
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
    public List<TblTravelAgentRoomType> getAllDataTravelAgentRoomTypeByIDPartner(long idPartner) {
        errMsg = "";
        List<TblTravelAgentRoomType> list = new ArrayList<>();
        if (ClassSession.checkUserSession()) {  //check user current session
            try {
                session = HibernateUtil.getSessionFactory().getCurrentSession();
                session.beginTransaction();
                list = session.getNamedQuery("findAllTblTravelAgentRoomTypeByIDPartner")
                        .setParameter("idPartner", idPartner)
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
    public List<TblTravelAgentRoomType> getAllDataTravelAgentRoomTypeByIDPartnerAndAvailableDate(long idPartner,
            Date availableDate) {
        errMsg = "";
        List<TblTravelAgentRoomType> list = new ArrayList<>();
        if (ClassSession.checkUserSession()) {  //check user current session
            try {
                session = HibernateUtil.getSessionFactory().getCurrentSession();
                session.beginTransaction();
                list = session.getNamedQuery("findAllTblTravelAgentRoomTypeByIDPartnerAndAvailableDate")
                        .setParameter("idPartner", idPartner)
                        .setParameter("availableDate", availableDate)
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
    public TblRoomType getDataRoomType(long id) {
        errMsg = "";
        TblRoomType data = null;
        if (ClassSession.checkUserSession()) {  //check user current session
            try {
                session = HibernateUtil.getSessionFactory().getCurrentSession();
                session.beginTransaction();
                data = (TblRoomType) session.find(TblRoomType.class, id);
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

    //--------------------------------------------------------------------------
    @Override
    public List<TblRoom> getAllDataRoomByIDRoomType(long idRoomType) {
        errMsg = "";
        List<TblRoom> list = new ArrayList<>();
        if (ClassSession.checkUserSession()) {  //check user current session
            try {
                session = HibernateUtil.getSessionFactory().getCurrentSession();
                session.beginTransaction();
                list = session.getNamedQuery("findAllTblRoomByIDRoomType")
                        .setParameter("idRoomType", idRoomType)
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

//    @Override
//    public TblTravelAgentRoomType getTravelAgentRoomTypeByIDRoomTypeAndIDPartner(long idRoomType, long idPartner) {
//        errMsg = "";
//        List<TblTravelAgentRoomType> list = new ArrayList<>();
//        try {
//            session = HibernateUtil.getSessionFactory().getCurrentSession();
//            session.beginTransaction();
//            list = session.getNamedQuery("findAllTblTravelAgentRoomTypeByIDRoomTypeAndIDPartner")
//                    .setParameter("idRoomType", idRoomType)
//                    .setParameter("idPartner", idPartner)
//                    .list();
//            session.getTransaction().commit();
//        } catch (Exception e) {
//            if (session.getTransaction().isActive()) {
//                session.getTransaction().rollback();
//            }
//            errMsg = e.getMessage();
//        } finally {
//            //session.close();
//        }
//        return list.isEmpty() ? null : list.get(0);
//    }
//
//    @Override
//    public TblTravelAgentRoomType getTravelAgentRoomTypeByIDRoomTypeAndIDPartnerAndAvailableDate(long idRoomType, long idPartner, Date availableDate) {
//        errMsg = "";
//        List<TblTravelAgentRoomType> list = new ArrayList<>();
//        try {
//            session = HibernateUtil.getSessionFactory().getCurrentSession();
//            session.beginTransaction();
//            list = session.getNamedQuery("findAllTblTravelAgentRoomTypeByIDRoomTypeAndIDPartnerAndAvailableDate")
//                    .setParameter("idRoomType", idRoomType)
//                    .setParameter("idPartner", idPartner)
//                    .setParameter("availableDate", availableDate)
//                    .list();
//            session.getTransaction().commit();
//        } catch (Exception e) {
//            if (session.getTransaction().isActive()) {
//                session.getTransaction().rollback();
//            }
//            errMsg = e.getMessage();
//        } finally {
//            //session.close();
//        }
//        return list.isEmpty() ? null : list.get(0);
//    }
    @Override
    public List<TblTravelAgentRoomType> getAllDataTravelAgentRoomTypeByIDRoomType(long idRoomType) {
        errMsg = "";
        List<TblTravelAgentRoomType> list = new ArrayList<>();
        if (ClassSession.checkUserSession()) {  //check user current session
            try {
                session = HibernateUtil.getSessionFactory().getCurrentSession();
                session.beginTransaction();
                list = session.getNamedQuery("findAllTblTravelAgentRoomTypeByIDRoomType")
                        .setParameter("idRoomType", idRoomType)
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
    public List<TblTravelAgentRoomType> getAllDataTravelAgentRoomTypeByIDRoomTypeAndAvailableDate(long idRoomType,
            Date availableDate) {
        errMsg = "";
        List<TblTravelAgentRoomType> list = new ArrayList<>();
        if (ClassSession.checkUserSession()) {  //check user current session
            try {
                session = HibernateUtil.getSessionFactory().getCurrentSession();
                session.beginTransaction();
                list = session.getNamedQuery("findAllTblTravelAgentRoomTypeByIDRoomTypeAndAvailableDate")
                        .setParameter("idRoomType", idRoomType)
                        .setParameter("availableDate", availableDate)
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
    public int getMaxNumberRoomTypeNumberHasBeenUsedByCustomer(
            TblRoomType roomType,
            Date detailDate) {
        errMsg = "";
        String maxNumber = "0";
        if (ClassSession.checkUserSession()) {  //check user current session
            try {
                session = HibernateUtil.getSessionFactory().getCurrentSession();
                session.beginTransaction();
                Query rs = session.getNamedQuery("getMaxRoomTypeNumberHasBeenUsedByCustomer")
                        .setParameter("idRoomType", roomType.getIdroomType())
                        .setParameter("detailDate", detailDate);
//                    .setParameter("idRoomType", (long)1)
//                    .setParameter("detailDate", "2017-09-27");
                maxNumber = (String) rs.uniqueResult();
                if (maxNumber == null) {
                    if (session.getTransaction().isActive()) {
                        session.getTransaction().rollback();
                    }
                    return 0;
                }
                session.getTransaction().commit();
            } catch (Exception e) {
                if (session.getTransaction().isActive()) {
                    session.getTransaction().rollback();
                }
                errMsg = e.getMessage();
                return -1;
            } finally {
                //session.close();
            }
        }
        return Integer.parseInt(maxNumber);
    }

    @Override
    public int getMaxNumberRoomTypeNumberHasBeenUsedByTravelAgent(TblRoomType roomType,
            Date detailDate,
            TblPartner partner) {
        errMsg = "";
        String maxNumber = "0";
        if (ClassSession.checkUserSession()) {  //check user current session
            try {
                session = HibernateUtil.getSessionFactory().getCurrentSession();
                session.beginTransaction();
                Query rs = session.getNamedQuery("getMaxRoomTypeNumberHasBeenUsedByTravelAgent")
                        .setParameter("idRoomType", roomType.getIdroomType())
                        .setParameter("detailDate", detailDate)
                        .setParameter("idPartner", partner.getIdpartner());
//                    .setParameter("idRoomType", (long)1)
//                    .setParameter("detailDate", "2017-09-27")
//                    .setParameter("idPartner", (long)1);
                maxNumber = (String) rs.uniqueResult();
                if (maxNumber == null) {
                    if (session.getTransaction().isActive()) {
                        session.getTransaction().rollback();
                    }
                    return 0;
                }
                session.getTransaction().commit();
            } catch (Exception e) {
                if (session.getTransaction().isActive()) {
                    session.getTransaction().rollback();
                }
                errMsg = e.getMessage();
                System.out.println("err " + errMsg);
                return -1;
            } finally {
                //session.close();
            }
        }
        return Integer.parseInt(maxNumber);
    }

    //--------------------------------------------------------------------------
    @Override
    public TblPartner getDataPartner(long id) {
        errMsg = "";
        TblPartner data = null;
        if (ClassSession.checkUserSession()) {  //check user current session
            try {
                session = HibernateUtil.getSessionFactory().getCurrentSession();
                session.beginTransaction();
                data = (TblPartner) session.find(TblPartner.class, id);
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
    public TblTravelAgentRoomType getDataTravelAgentRoomTypeByIDRoomTypeAndIDPartner(long idRoomType,
            long idPartner) {
        errMsg = "";
        List<TblTravelAgentRoomType> list = new ArrayList<>();
        if (ClassSession.checkUserSession()) {  //check user current session
            try {
                session = HibernateUtil.getSessionFactory().getCurrentSession();
                session.beginTransaction();
                list = session.getNamedQuery("findAllTblTravelAgentRoomTypeByIDRoomTypeAndIDPartner")
                        .setParameter("idRoomType", idRoomType)
                        .setParameter("idPartner", idPartner)
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
    public TblTravelAgentRoomType getDataTravelAgentRoomTypeByIDRoomTypeAndIDPartnerAndAvailableDate(long idRoomType, long idPartner, Date availableDate) {
        errMsg = "";
        List<TblTravelAgentRoomType> list = new ArrayList<>();
        if (ClassSession.checkUserSession()) {  //check user current session
            try {
                session = HibernateUtil.getSessionFactory().getCurrentSession();
                session.beginTransaction();
                list = session.getNamedQuery("findAllTblTravelAgentRoomTypeByIDRoomTypeAndIDPartnerAndAvailableDate")
                        .setParameter("idRoomType", idRoomType)
                        .setParameter("idPartner", idPartner)
                        .setParameter("availableDate", availableDate)
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
    public TblPartnerBankAccount getPartnerBankAccount(long id) {
        errMsg = "";
        TblPartnerBankAccount data = null;
        if (ClassSession.checkUserSession()) {  //check user current session
            try {
                session = HibernateUtil.getSessionFactory().getCurrentSession();
                session.beginTransaction();
                data = (TblPartnerBankAccount) session.find(TblPartnerBankAccount.class, id);
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
    public List<TblPartnerBankAccount> getAllDataPartnerBankAccount() {
        errMsg = "";
        List<TblPartnerBankAccount> list = new ArrayList<>();
        if (ClassSession.checkUserSession()) {  //check user current session
            try {
                session = HibernateUtil.getSessionFactory().getCurrentSession();
                session.beginTransaction();
                list = session.getNamedQuery("findAllTblPartnerBankAccount").list();
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
    public List<TblPartnerBankAccount> getAllDataPartnerBankAccountByIDPartner(long idPartner) {
        errMsg = "";
        List<TblPartnerBankAccount> list = new ArrayList<>();
        if (ClassSession.checkUserSession()) {  //check user current session
            try {
                session = HibernateUtil.getSessionFactory().getCurrentSession();
                session.beginTransaction();
                list = session.getNamedQuery("findAllTblPartnerBankAccountByIDPartner")
                        .setParameter("idPartner", idPartner)
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

    //--------------------------------------------------------------------------
    @Override
    public TblHotelReceivable getDataHotelReceivable(long id) {
        errMsg = "";
        TblHotelReceivable data = null;
        if (ClassSession.checkUserSession()) {  //check user current session
            try {
                session = HibernateUtil.getSessionFactory().getCurrentSession();
                session.beginTransaction();
                data = (TblHotelReceivable) session.find(TblHotelReceivable.class, id);
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
    public RefHotelReceivableType getDataHotelReceivableType(int id) {
        errMsg = "";
        RefHotelReceivableType data = null;
        if (ClassSession.checkUserSession()) {  //check user current session
            try {
                session = HibernateUtil.getSessionFactory().getCurrentSession();
                session.beginTransaction();
                data = (RefHotelReceivableType) session.find(RefHotelReceivableType.class, id);
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

    @Override
    public TblReservationPaymentWithGuaranteePayment getDataReservationPaymentWithGuaranteePayment(long id) {
        errMsg = "";
        TblReservationPaymentWithGuaranteePayment data = null;
        if (ClassSession.checkUserSession()) {  //check user current session
            try {
                session = HibernateUtil.getSessionFactory().getCurrentSession();
                session.beginTransaction();
                data = (TblReservationPaymentWithGuaranteePayment) session.find(TblReservationPaymentWithGuaranteePayment.class, id);
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
    public RefPartnerType getDataPartnerType(int id) {
        errMsg = "";
        RefPartnerType data = null;
        if (ClassSession.checkUserSession()) {  //check user current session
            try {
                session = HibernateUtil.getSessionFactory().getCurrentSession();
                session.beginTransaction();
                data = (RefPartnerType) session.find(RefPartnerType.class, id);
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

    //--------------------------------------------------------------------------
    @Override
    public TblHotelFinanceTransaction insertDataHotelFinanceTransaction(TblHotelFinanceTransaction hotelFinanceTransaction) {
        errMsg = "";
        TblHotelFinanceTransaction tblHotelFinanceTransaction = hotelFinanceTransaction;
        if (ClassSession.checkUserSession()) {  //check user current session
            try {
                session = HibernateUtil.getSessionFactory().getCurrentSession();
                session.beginTransaction();
//                //data hotel finnace transaction
//                tblHotelFinanceTransaction.setCodeTransaction(ClassCoder.getCode("Receivable - Payment", session));
//                tblHotelFinanceTransaction.setCreateDate(Timestamp.valueOf(LocalDateTime.now()));
//                tblHotelFinanceTransaction.setTblEmployeeByCreateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
//                tblHotelFinanceTransaction.setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
//                tblHotelFinanceTransaction.setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
//                tblHotelFinanceTransaction.setRefRecordStatus(session.find(RefRecordStatus.class, 1));
//                session.saveOrUpdate(tblHotelFinanceTransaction);
//                //@@@%%%
//                //data hotel receivable (finance transaction status : updated)
//                tblHotelFinanceTransaction.getTblHotelReceivable().setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
//                tblHotelFinanceTransaction.getTblHotelReceivable().setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
//                session.update(tblHotelFinanceTransaction.getTblHotelReceivable());
//                //data company balance (kas besar) : plus (updated)
//                TblCompanyBalance dataBalance = session.find(TblCompanyBalance.class, (long) 1);
//                dataBalance.setBalanceNominal(dataBalance.getBalanceNominal().add(hotelFinanceTransaction.getTransactionNominal()));
//                dataBalance.setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
//                dataBalance.setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
//                session.update(dataBalance);
//                //data company balance (kas besar) : nominal bank account : plus (updated)
//                TblCompanyBalanceBankAccount companyBalanceBankAccount = getCompanyBalanceBankAccountByIDCompanyBalanceAndIDBankAccount((long) 1, hotelFinanceTransaction.getTblBankAccountByReceiverBankAccount().getIdbankAccount());    //hotel balance = '1'
//                companyBalanceBankAccount.setCompanyBalanceBankAccountNominal(companyBalanceBankAccount.getCompanyBalanceBankAccountNominal().add(hotelFinanceTransaction.getTransactionNominal()));
//                companyBalanceBankAccount.setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
//                companyBalanceBankAccount.setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
//                session.update(companyBalanceBankAccount);
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

    private TblCompanyBalanceBankAccount getCompanyBalanceBankAccountByIDCompanyBalanceAndIDBankAccount(long idCompanyBalance, long idBankAccount) {
        List<TblCompanyBalanceBankAccount> list = session.getNamedQuery("findAllTblCompanyBalanceBankAccountByIDCompanyBalanceAndIDBankAccount")
                .setParameter("idCompanyBalance", idCompanyBalance)
                .setParameter("idBankAccount", idBankAccount)
                .list();
        return list.isEmpty() ? null : list.get(0);
    }

    @Override
    public TblHotelFinanceTransaction insertDataHotelFinanceTransaction(TblHotelFinanceTransaction hotelFinanceTransaction,
            TblHotelFinanceTransactionWithCash hotelFinanceTransactionWithCash,
            List<TblHotelFinanceTransactionHotelReceivable> hfthrs, 
            String partnerName) {
        errMsg = "";
        TblHotelFinanceTransaction tblHotelFinanceTransaction = hotelFinanceTransaction;
        if (ClassSession.checkUserSession()) {  //check user current session
            try {
                session = HibernateUtil.getSessionFactory().getCurrentSession();
                session.beginTransaction();
                //data hotel finnace transaction
                tblHotelFinanceTransaction.setCodeTransaction(ClassCoder.getCode("Receivable - Payment", session));
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
                //data hotel finnace transaction - hotel receivable
                for (TblHotelFinanceTransactionHotelReceivable hfthr : hfthrs) {
                    hfthr.setCreateDate(Timestamp.valueOf(LocalDateTime.now()));
                    hfthr.setTblEmployeeByCreateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                    hfthr.setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                    hfthr.setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                    hfthr.setRefRecordStatus(session.find(RefRecordStatus.class, 1));
                    session.saveOrUpdate(hfthr);
                    //data hotel receivable
                    hfthr.getTblHotelReceivable().setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                    hfthr.getTblHotelReceivable().setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                    session.saveOrUpdate(hfthr.getTblHotelReceivable());
                    //data reservation payment with guarantee payment...???
//                    TblReservationPaymentWithGuaranteePayment rpwgp = getReservationPaymentWithGuaranteePaymentByIDHotelReceivable(hfthr.getTblHotelReceivable().getIdhotelReceivable());
//                    if (rpwgp != null) {
//                        if(hfthr.getTblHotelReceivable().getRefFinanceTransactionStatus().getIdstatus() == 1){ //Dibayar Sebagian = '1'
//                            rpwgp.setRefReturPaymentStatus(session.find(RefReturPaymentStatus.class, 1));   //Dibayar Sebagian = '1'
//                        }else{  //Sudah Dibayar = '2'
//                            rpwgp.setRefReturPaymentStatus(session.find(RefReturPaymentStatus.class, 2));   //Dibayar Sebagian = '2'
//                        }
//                        rpwgp.setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
//                        rpwgp.setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
//                        session.saveOrUpdate(rpwgp);
//                    }
                }
                //@@@%%%
                //data company balance (kas) : plus/minus (updated)
                TblCompanyBalance dataBalance = session.find(TblCompanyBalance.class, hotelFinanceTransactionWithCash.getTblCompanyBalance().getIdbalance());
                if(hotelFinanceTransaction.getIsReturnTransaction()){   //minus
                    dataBalance.setBalanceNominal(dataBalance.getBalanceNominal().subtract(hotelFinanceTransaction.getTransactionNominal()));
                }else{  //plus
                    dataBalance.setBalanceNominal(dataBalance.getBalanceNominal().add(hotelFinanceTransaction.getTransactionNominal()));
                }
                dataBalance.setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                dataBalance.setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                session.update(dataBalance);
                //data company balance (cash-flow log)
                LogCompanyBalanceCashFlow logCompanyBalanceCashFlow = new LogCompanyBalanceCashFlow();
                if(hotelFinanceTransaction.getIsReturnTransaction()){   //out
                    logCompanyBalanceCashFlow.setTblCompanyBalanceByIdsenderCompanyBalance(dataBalance);
                    logCompanyBalanceCashFlow.setTblBankAccountByIdsenderCbbankAccount(null);
                    logCompanyBalanceCashFlow.setTblCompanyBalanceByIdreceiverCompanyBalance(null);
                    logCompanyBalanceCashFlow.setTblBankAccountByIdreceiverCbbankAccount(null);
                }else{  //in
                    logCompanyBalanceCashFlow.setTblCompanyBalanceByIdsenderCompanyBalance(null);
                    logCompanyBalanceCashFlow.setTblBankAccountByIdsenderCbbankAccount(null);
                    logCompanyBalanceCashFlow.setTblCompanyBalanceByIdreceiverCompanyBalance(dataBalance);
                    logCompanyBalanceCashFlow.setTblBankAccountByIdreceiverCbbankAccount(null);
                }
                logCompanyBalanceCashFlow.setTransferNominal(hotelFinanceTransaction.getTransactionNominal());
                logCompanyBalanceCashFlow.setHistoryDate(Timestamp.valueOf(LocalDateTime.now()));
                logCompanyBalanceCashFlow.setTblEmployee(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                if(hotelFinanceTransaction.getIsReturnTransaction()){
                    logCompanyBalanceCashFlow.setHistoryStystemNote("Pengembalian pembayaran piutang kepada " + partnerName + ", \nsebesar : " 
                        + ClassFormatter.currencyFormat.format(hotelFinanceTransaction.getTransactionNominal()) + " (" 
                        + tblHotelFinanceTransaction.getRefFinanceTransactionPaymentType().getTypeName() + ")");
                }else{
                    logCompanyBalanceCashFlow.setHistoryStystemNote("Pembayaran piutang oleh " + partnerName + ", \nsebesar : " 
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
    public TblHotelFinanceTransaction insertDataHotelFinanceTransaction(TblHotelFinanceTransaction hotelFinanceTransaction,
            TblHotelFinanceTransactionWithTransfer hotelFinanceTransactionWithTransfer,
            List<TblHotelFinanceTransactionHotelReceivable> hfthrs, 
            String partnerName) {
        errMsg = "";
        TblHotelFinanceTransaction tblHotelFinanceTransaction = hotelFinanceTransaction;
        if (ClassSession.checkUserSession()) {  //check user current session
            try {
                session = HibernateUtil.getSessionFactory().getCurrentSession();
                session.beginTransaction();
                //data hotel finnace transaction
                tblHotelFinanceTransaction.setCodeTransaction(ClassCoder.getCode("Receivable - Payment", session));
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
                //data hotel finnace transaction - hotel receivable
                for (TblHotelFinanceTransactionHotelReceivable hfthr : hfthrs) {
                    hfthr.setCreateDate(Timestamp.valueOf(LocalDateTime.now()));
                    hfthr.setTblEmployeeByCreateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                    hfthr.setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                    hfthr.setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                    hfthr.setRefRecordStatus(session.find(RefRecordStatus.class, 1));
                    session.saveOrUpdate(hfthr);
                    //data hotel receivable
                    hfthr.getTblHotelReceivable().setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                    hfthr.getTblHotelReceivable().setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                    session.saveOrUpdate(hfthr.getTblHotelReceivable());
                    //data reservation payment with guarantee payment...???
//                    TblReservationPaymentWithGuaranteePayment rpwgp = getReservationPaymentWithGuaranteePaymentByIDHotelReceivable(hfthr.getTblHotelReceivable().getIdhotelReceivable());
//                    if (rpwgp != null) {
//                        if(hfthr.getTblHotelReceivable().getRefFinanceTransactionStatus().getIdstatus() == 1){ //Dibayar Sebagian = '1'
//                            rpwgp.setRefReturPaymentStatus(session.find(RefReturPaymentStatus.class, 1));   //Dibayar Sebagian = '1'
//                        }else{  //Sudah Dibayar = '2'
//                            rpwgp.setRefReturPaymentStatus(session.find(RefReturPaymentStatus.class, 2));   //Dibayar Sebagian = '2'
//                        }
//                        rpwgp.setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
//                        rpwgp.setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
//                        session.saveOrUpdate(rpwgp);
//                    }
                }
                //@@@%%%
                //data company balance (kas besar) : plus/minus (updated)
                TblCompanyBalance dataBalance = session.find(TblCompanyBalance.class, (long) 1);    //Kas Besar = '1'
                if(hotelFinanceTransaction.getIsReturnTransaction()){   //minus
                    dataBalance.setBalanceNominal(dataBalance.getBalanceNominal().subtract(hotelFinanceTransaction.getTransactionNominal()));
                }else{  //plus
                    dataBalance.setBalanceNominal(dataBalance.getBalanceNominal().add(hotelFinanceTransaction.getTransactionNominal()));
                }
                dataBalance.setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                dataBalance.setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                session.update(dataBalance);
                //data company balance (kas besar) : nominal bank account : plus/minus (updated)
                TblCompanyBalanceBankAccount companyBalanceBankAccount;
                if(hotelFinanceTransaction.getIsReturnTransaction()){
                    companyBalanceBankAccount = getCompanyBalanceBankAccountByIDCompanyBalanceAndIDBankAccount(
                        (long) 1, //Kas Besar = '1'
                        hotelFinanceTransactionWithTransfer.getTblBankAccountBySenderBankAccount().getIdbankAccount());
                }else{
                    companyBalanceBankAccount = getCompanyBalanceBankAccountByIDCompanyBalanceAndIDBankAccount(
                        (long) 1, //Kas Besar = '1'
                        hotelFinanceTransactionWithTransfer.getTblBankAccountByReceiverBankAccount().getIdbankAccount());
                }
                if(hotelFinanceTransaction.getIsReturnTransaction()){   //minus
                    companyBalanceBankAccount.setCompanyBalanceBankAccountNominal(companyBalanceBankAccount.getCompanyBalanceBankAccountNominal().subtract(hotelFinanceTransaction.getTransactionNominal()));
                }else{  //plus
                    companyBalanceBankAccount.setCompanyBalanceBankAccountNominal(companyBalanceBankAccount.getCompanyBalanceBankAccountNominal().add(hotelFinanceTransaction.getTransactionNominal()));
                }
                companyBalanceBankAccount.setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                companyBalanceBankAccount.setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                session.update(companyBalanceBankAccount);
                //data company balance (cash-flow log)
                LogCompanyBalanceCashFlow logCompanyBalanceCashFlow = new LogCompanyBalanceCashFlow();
                if(hotelFinanceTransaction.getIsReturnTransaction()){   //out
                    logCompanyBalanceCashFlow.setTblCompanyBalanceByIdsenderCompanyBalance(dataBalance);
                    logCompanyBalanceCashFlow.setTblBankAccountByIdsenderCbbankAccount(companyBalanceBankAccount.getTblBankAccount());
                    logCompanyBalanceCashFlow.setTblCompanyBalanceByIdreceiverCompanyBalance(null);
                    logCompanyBalanceCashFlow.setTblBankAccountByIdreceiverCbbankAccount(hotelFinanceTransactionWithTransfer.getTblBankAccountByReceiverBankAccount());
                }else{  //in
                    logCompanyBalanceCashFlow.setTblCompanyBalanceByIdsenderCompanyBalance(null);
                    logCompanyBalanceCashFlow.setTblBankAccountByIdsenderCbbankAccount(hotelFinanceTransactionWithTransfer.getTblBankAccountBySenderBankAccount());
                    logCompanyBalanceCashFlow.setTblCompanyBalanceByIdreceiverCompanyBalance(dataBalance);
                    logCompanyBalanceCashFlow.setTblBankAccountByIdreceiverCbbankAccount(companyBalanceBankAccount.getTblBankAccount());
                }
                logCompanyBalanceCashFlow.setTransferNominal(hotelFinanceTransaction.getTransactionNominal());
                logCompanyBalanceCashFlow.setHistoryDate(Timestamp.valueOf(LocalDateTime.now()));
                logCompanyBalanceCashFlow.setTblEmployee(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                if(hotelFinanceTransaction.getIsReturnTransaction()){
                    logCompanyBalanceCashFlow.setHistoryStystemNote("Pengembalian pembayaran piutang kepada " + partnerName + ", \nsebesar : " 
                        + ClassFormatter.currencyFormat.format(hotelFinanceTransaction.getTransactionNominal()) + " (" 
                        + tblHotelFinanceTransaction.getRefFinanceTransactionPaymentType().getTypeName() + ")");
                }else{
                    logCompanyBalanceCashFlow.setHistoryStystemNote("Pembayaran piutang oleh " + partnerName + ", \nsebesar : " 
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
    public TblHotelFinanceTransaction insertDataHotelFinanceTransaction(TblHotelFinanceTransaction hotelFinanceTransaction,
            TblHotelFinanceTransactionWithCekGiro hotelFinanceTransactionWithCekGiro,
            List<TblHotelFinanceTransactionHotelReceivable> hfthrs, 
            String partnerName) {
        errMsg = "";
        TblHotelFinanceTransaction tblHotelFinanceTransaction = hotelFinanceTransaction;
        if (ClassSession.checkUserSession()) {  //check user current session
            try {
                session = HibernateUtil.getSessionFactory().getCurrentSession();
                session.beginTransaction();
                //data hotel finnace transaction
                tblHotelFinanceTransaction.setCodeTransaction(ClassCoder.getCode("Receivable - Payment", session));
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
                //data hotel finnace transaction - hotel receivable
                for (TblHotelFinanceTransactionHotelReceivable hfthr : hfthrs) {
                    hfthr.setCreateDate(Timestamp.valueOf(LocalDateTime.now()));
                    hfthr.setTblEmployeeByCreateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                    hfthr.setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                    hfthr.setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                    hfthr.setRefRecordStatus(session.find(RefRecordStatus.class, 1));
                    session.saveOrUpdate(hfthr);
                    //data hotel receivable
                    hfthr.getTblHotelReceivable().setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                    hfthr.getTblHotelReceivable().setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                    session.saveOrUpdate(hfthr.getTblHotelReceivable());
                    //data reservation payment with guarantee payment...???
//                    TblReservationPaymentWithGuaranteePayment rpwgp = getReservationPaymentWithGuaranteePaymentByIDHotelReceivable(hfthr.getTblHotelReceivable().getIdhotelReceivable());
//                    if (rpwgp != null) {
//                        if(hfthr.getTblHotelReceivable().getRefFinanceTransactionStatus().getIdstatus() == 1){ //Dibayar Sebagian = '1'
//                            rpwgp.setRefReturPaymentStatus(session.find(RefReturPaymentStatus.class, 1));   //Dibayar Sebagian = '1'
//                        }else{  //Sudah Dibayar = '2'
//                            rpwgp.setRefReturPaymentStatus(session.find(RefReturPaymentStatus.class, 2));   //Dibayar Sebagian = '2'
//                        }
//                        rpwgp.setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
//                        rpwgp.setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
//                        session.saveOrUpdate(rpwgp);
//                    }
                }
                //@@@%%%
                //data company balance (kas besar) : plus/minus (updated)
                TblCompanyBalance dataBalance = session.find(TblCompanyBalance.class, (long) 1);    //Kas Besar = '1'
                if(hotelFinanceTransaction.getIsReturnTransaction()){   //minus
                    dataBalance.setBalanceNominal(dataBalance.getBalanceNominal().subtract(hotelFinanceTransaction.getTransactionNominal()));
                }else{  //plus
                    dataBalance.setBalanceNominal(dataBalance.getBalanceNominal().add(hotelFinanceTransaction.getTransactionNominal()));
                }
                dataBalance.setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                dataBalance.setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                session.update(dataBalance);
                //data company balance (kas besar) : nominal bank account : plus/minus (updated)
                TblCompanyBalanceBankAccount companyBalanceBankAccount;
                if(hotelFinanceTransaction.getIsReturnTransaction()){
                    companyBalanceBankAccount = getCompanyBalanceBankAccountByIDCompanyBalanceAndIDBankAccount(
                        (long) 1, //Kas Besar = '1'
                        hotelFinanceTransactionWithCekGiro.getTblBankAccountBySenderBankAccount().getIdbankAccount());
                }else{
                    companyBalanceBankAccount = getCompanyBalanceBankAccountByIDCompanyBalanceAndIDBankAccount(
                        (long) 1, //Kas Besar = '1'
                        hotelFinanceTransactionWithCekGiro.getTblBankAccountByReceiverBankAccount().getIdbankAccount());
                }
                if(hotelFinanceTransaction.getIsReturnTransaction()){   //minus
                    companyBalanceBankAccount.setCompanyBalanceBankAccountNominal(companyBalanceBankAccount.getCompanyBalanceBankAccountNominal().subtract(hotelFinanceTransaction.getTransactionNominal()));
                }else{  //plus
                    companyBalanceBankAccount.setCompanyBalanceBankAccountNominal(companyBalanceBankAccount.getCompanyBalanceBankAccountNominal().add(hotelFinanceTransaction.getTransactionNominal()));
                }
                companyBalanceBankAccount.setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                companyBalanceBankAccount.setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                session.update(companyBalanceBankAccount);
                //data company balance (cash-flow log)
                LogCompanyBalanceCashFlow logCompanyBalanceCashFlow = new LogCompanyBalanceCashFlow();
                if(hotelFinanceTransaction.getIsReturnTransaction()){   //out
                    logCompanyBalanceCashFlow.setTblCompanyBalanceByIdsenderCompanyBalance(dataBalance);
                    logCompanyBalanceCashFlow.setTblBankAccountByIdsenderCbbankAccount(companyBalanceBankAccount.getTblBankAccount());
                    logCompanyBalanceCashFlow.setTblCompanyBalanceByIdreceiverCompanyBalance(null);
                    logCompanyBalanceCashFlow.setTblBankAccountByIdreceiverCbbankAccount(hotelFinanceTransactionWithCekGiro.getTblBankAccountByReceiverBankAccount());
                }else{  //in
                    logCompanyBalanceCashFlow.setTblCompanyBalanceByIdsenderCompanyBalance(null);
                    logCompanyBalanceCashFlow.setTblBankAccountByIdsenderCbbankAccount(hotelFinanceTransactionWithCekGiro.getTblBankAccountBySenderBankAccount());
                    logCompanyBalanceCashFlow.setTblCompanyBalanceByIdreceiverCompanyBalance(dataBalance);
                    logCompanyBalanceCashFlow.setTblBankAccountByIdreceiverCbbankAccount(companyBalanceBankAccount.getTblBankAccount());
                }
                logCompanyBalanceCashFlow.setTransferNominal(hotelFinanceTransaction.getTransactionNominal());
                logCompanyBalanceCashFlow.setHistoryDate(Timestamp.valueOf(LocalDateTime.now()));
                logCompanyBalanceCashFlow.setTblEmployee(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                if(hotelFinanceTransaction.getIsReturnTransaction()){
                    logCompanyBalanceCashFlow.setHistoryStystemNote("Pengembalian pembayaran piutang kepada " + partnerName + ", \nsebesar : " 
                        + ClassFormatter.currencyFormat.format(hotelFinanceTransaction.getTransactionNominal()) + " (" 
                        + tblHotelFinanceTransaction.getRefFinanceTransactionPaymentType().getTypeName() + ")");
                }else{
                    logCompanyBalanceCashFlow.setHistoryStystemNote("Pembayaran piutang oleh " + partnerName + ", \nsebesar : " 
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

    private TblReservationPaymentWithGuaranteePayment getReservationPaymentWithGuaranteePaymentByIDHotelReceivable(long idHotelReceivable) {
        List<TblReservationPaymentWithGuaranteePayment> list = session.getNamedQuery("findAllTblReservationPaymentWithGuaranteePaymentByIDHotelReceivable")
                .setParameter("idHotelReceivable", idHotelReceivable)
                .list();
        return list.isEmpty() ? null : list.get(0);
    }

    @Override
    public List<TblHotelFinanceTransaction> getAllDataHotelFinanceTransactionByIDHotelReceivable(long idHotelReceivable) {
        errMsg = "";
        List<TblHotelFinanceTransaction> list = new ArrayList<>();
        if (ClassSession.checkUserSession()) {  //check user current session
            try {
                session = HibernateUtil.getSessionFactory().getCurrentSession();
                session.beginTransaction();
                list = session.getNamedQuery("findAllTblHotelFinanceTransactionByIDHotelReceivable")
                        .setParameter("idHotelReceivable", idHotelReceivable)
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
    public TblPartnerBankAccount insertDataPartnerBankAccount(TblPartnerBankAccount partnerBankAccount) {
        errMsg = "";
        TblPartnerBankAccount tblPartnerBankAccount = partnerBankAccount;
        if (ClassSession.checkUserSession()) {  //check user current session
            try {
                session = HibernateUtil.getSessionFactory().getCurrentSession();
                session.beginTransaction();
                //save data bank account n partner bank acccount
                //data bank account
                tblPartnerBankAccount.getTblBankAccount().setCreateDate(Timestamp.valueOf(LocalDateTime.now()));
                tblPartnerBankAccount.getTblBankAccount().setTblEmployeeByCreateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                tblPartnerBankAccount.getTblBankAccount().setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                tblPartnerBankAccount.getTblBankAccount().setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                tblPartnerBankAccount.getTblBankAccount().setRefRecordStatus(session.find(RefRecordStatus.class, 1));
                session.saveOrUpdate(tblPartnerBankAccount.getTblBankAccount());
                //data partner bank account
                tblPartnerBankAccount.setRefBankAccountHolderStatus(session.find(RefBankAccountHolderStatus.class, 0));
                tblPartnerBankAccount.setCreateDate(Timestamp.valueOf(LocalDateTime.now()));
                tblPartnerBankAccount.setTblEmployeeByCreateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                tblPartnerBankAccount.setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                tblPartnerBankAccount.setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                tblPartnerBankAccount.setRefRecordStatus(session.find(RefRecordStatus.class, 1));
                session.saveOrUpdate(tblPartnerBankAccount);
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
        return tblPartnerBankAccount;
    }

    //--------------------------------------------------------------------------
    @Override
    public TblReservation getDataReservation(long id) {
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
    public TblReservationBill getDataReservationBill(long id) {
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
    public RefReservationStatus getDataReservationStatus(int id) {
        errMsg = "";
        RefReservationStatus data = null;
        if (ClassSession.checkUserSession()) {  //check user current session
            try {
                session = HibernateUtil.getSessionFactory().getCurrentSession();
                session.beginTransaction();
                data = (RefReservationStatus) session.find(RefReservationStatus.class, id);
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
    public TblReservationRoomTypeDetail getDataReservationRoomTypeDetail(long id) {
        errMsg = "";
        TblReservationRoomTypeDetail data = null;
        if (ClassSession.checkUserSession()) {  //check user current session
            try {
                session = HibernateUtil.getSessionFactory().getCurrentSession();
                session.beginTransaction();
                data = (TblReservationRoomTypeDetail) session.find(TblReservationRoomTypeDetail.class, id);
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
    public List<TblReservationRoomPriceDetail> getAllDataReservationRoomPriceDetailByDetailDate(Date date) {
        errMsg = "";
        List<TblReservationRoomPriceDetail> list = new ArrayList<>();
        if (ClassSession.checkUserSession()) {  //check user current session
            try {
                session = HibernateUtil.getSessionFactory().getCurrentSession();
                session.beginTransaction();
                list = session.getNamedQuery("findAllTblRoomPriceDetailByDetailDate")
                        .setParameter("detailDate", date)
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
    public TblReservationRoomTypeDetailRoomPriceDetail getDataReservationRoomTypeDetailRoomPriceDetail(long id) {
        errMsg = "";
        TblReservationRoomTypeDetailRoomPriceDetail data = null;
        if (ClassSession.checkUserSession()) {  //check user current session
            try {
                session = HibernateUtil.getSessionFactory().getCurrentSession();
                session.beginTransaction();
                data = (TblReservationRoomTypeDetailRoomPriceDetail) session.find(TblReservationRoomTypeDetailRoomPriceDetail.class, id);
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
    public TblReservationRoomTypeDetailRoomPriceDetail getDataReservationRoomTypeDetailRoomPriceDetailByIDRoomPriceDetail(long idRoomPriceDetail) {
        errMsg = "";
        List<TblReservationRoomTypeDetailRoomPriceDetail> list = new ArrayList<>();
        if (ClassSession.checkUserSession()) {  //check user current session
            try {
                session = HibernateUtil.getSessionFactory().getCurrentSession();
                session.beginTransaction();
                list = session.getNamedQuery("findAllTblReservationRoomTypeDetailRoomPriceDetailByIDRoomPriceDetail")
                        .setParameter("idRoomPriceDetail", idRoomPriceDetail)
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
    public TblReservationTravelAgentDiscountDetail getDataReservationTravelAgentDiscountDetail(long id) {
        errMsg = "";
        TblReservationTravelAgentDiscountDetail data = null;
        if (ClassSession.checkUserSession()) {  //check user current session
            try {
                session = HibernateUtil.getSessionFactory().getCurrentSession();
                session.beginTransaction();
                data = (TblReservationTravelAgentDiscountDetail) session.find(TblReservationTravelAgentDiscountDetail.class, id);
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
    public TblReservationRoomTypeDetailTravelAgentDiscountDetail getDataReservationRoomTypeDetailTravelAgentDiscountDetailByIDReservationRoomTypeDetail(long idRoomTypeDetail) {
        errMsg = "";
        List<TblReservationRoomTypeDetailTravelAgentDiscountDetail> list = new ArrayList<>();
        if (ClassSession.checkUserSession()) {  //check user current session
            try {
                session = HibernateUtil.getSessionFactory().getCurrentSession();
                session.beginTransaction();
                list = session.getNamedQuery("findAllTblReservationRoomTypeDetailTravelAgentDiscountDetailByIDRoomTypeDetail")
                        .setParameter("idRoomTypeDetail", idRoomTypeDetail)
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
    public List<TblReservationRoomTypeDetailTravelAgentDiscountDetail> getAllDataReservationRoomTypeDetailTravelAgentDiscountDetailByIDReservationRoomTypeDetail(long idRoomTypeDetail) {
        errMsg = "";
        List<TblReservationRoomTypeDetailTravelAgentDiscountDetail> list = new ArrayList<>();
        if (ClassSession.checkUserSession()) {  //check user current session
            try {
                session = HibernateUtil.getSessionFactory().getCurrentSession();
                session.beginTransaction();
                list = session.getNamedQuery("findAllTblReservationRoomTypeDetailTravelAgentDiscountDetailByIDRoomTypeDetail")
                        .setParameter("idRoomTypeDetail", idRoomTypeDetail)
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
//    @Override
//    public TblHotelInvoice insertDataHotelInvoice(TblHotelInvoice hotelInvoice,
//            List<TblHotelInvoiceReceivable> hotelInvoiceReceivables) {
//        errMsg = "";
//        TblHotelInvoice tblHotelInvoice = hotelInvoice;
//        if (ClassSession.checkUserSession()) {  //check user current session
//            try {
//                session = HibernateUtil.getSessionFactory().getCurrentSession();
//                session.beginTransaction();
//                //data hotel invoice
//                tblHotelInvoice.setCodeHotelInvoice(ClassCoder.getCode("Hotel Invoice", session));
//                tblHotelInvoice.setCreateDate(Timestamp.valueOf(LocalDateTime.now()));
//                tblHotelInvoice.setTblEmployeeByCreateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
//                tblHotelInvoice.setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
//                tblHotelInvoice.setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
//                tblHotelInvoice.setRefRecordStatus(session.find(RefRecordStatus.class, 1));
//                session.saveOrUpdate(tblHotelInvoice);
//                //insert all data hotel invoice receivable
//                for (TblHotelInvoiceReceivable hotelInvoiceReceivable : hotelInvoiceReceivables) {
//                    hotelInvoiceReceivable.setCreateDate(Timestamp.valueOf(LocalDateTime.now()));
//                    hotelInvoiceReceivable.setTblEmployeeByCreateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
//                    hotelInvoiceReceivable.setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
//                    hotelInvoiceReceivable.setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
//                    hotelInvoiceReceivable.setRefRecordStatus(session.find(RefRecordStatus.class, 1));
//                    session.saveOrUpdate(hotelInvoiceReceivable);
//                }
//                session.getTransaction().commit();
//            } catch (Exception e) {
//                if (session.getTransaction().isActive()) {
//                    session.getTransaction().rollback();
//                }
//                errMsg = e.getMessage();
//                return null;
//            } finally {
//                //session.close();
//            }
//        } else {
//            return null;
//        }
//        return tblHotelInvoice;
//    }
//
//    @Override
//    public boolean updateDataHotelInvoice(TblHotelInvoice hotelInvoice,
//            List<TblHotelInvoiceReceivable> hotelInvoiceReceivables) {
//        errMsg = "";
//        if (ClassSession.checkUserSession()) {  //check user current session
//            try {
//                session = HibernateUtil.getSessionFactory().getCurrentSession();
//                session.beginTransaction();
//                //data hotel invoice
//                hotelInvoice.setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
//                hotelInvoice.setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
//                session.update(hotelInvoice);
//                //delete all (data hotel invoice receivable)
//                Query rs = session.getNamedQuery("deleteAllTblHotelInvoiceReceivable")
//                        .setParameter("idHotelInvoice", hotelInvoice.getIdhotelInvoice())
//                        .setParameter("idEmployee", ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee());
//                errMsg = (String) rs.uniqueResult();
//                if (!errMsg.equals("")) {
//                    if (session.getTransaction().isActive()) {
//                        session.getTransaction().rollback();
//                    }
//                    return false;
//                }
//                //insert all data hotel invoice receivable
//                for (TblHotelInvoiceReceivable hotelInvoiceReceivable : hotelInvoiceReceivables) {
//                    hotelInvoiceReceivable.setCreateDate(Timestamp.valueOf(LocalDateTime.now()));
//                    hotelInvoiceReceivable.setTblEmployeeByCreateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
//                    hotelInvoiceReceivable.setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
//                    hotelInvoiceReceivable.setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
//                    hotelInvoiceReceivable.setRefRecordStatus(session.find(RefRecordStatus.class, 1));
//                    session.saveOrUpdate(hotelInvoiceReceivable);
//                }
//                session.getTransaction().commit();
//            } catch (Exception e) {
//                if (session.getTransaction().isActive()) {
//                    session.getTransaction().rollback();
//                }
//                errMsg = e.getMessage();
//                return false;
//            } finally {
//                //session.close();
//            }
//        } else {
//            return false;
//        }
//        return true;
//    }
//
//    @Override
//    public boolean deleteDataHotelInvoice(TblHotelInvoice hotelInvoice) {
//        errMsg = "";
//        if (ClassSession.checkUserSession()) {  //check user current session
//            try {
//                session = HibernateUtil.getSessionFactory().getCurrentSession();
//                session.beginTransaction();
//                if (hotelInvoice.getRefRecordStatus().getIdstatus() == 1) {
//                    hotelInvoice.setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
//                    hotelInvoice.setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
//                    hotelInvoice.setRefRecordStatus(session.find(RefRecordStatus.class, 2));
//                    session.update(hotelInvoice);
//                    //delete all (data hotel invoice receivable)
//                    Query rs = session.getNamedQuery("deleteAllTblHotelInvoiceReceivable")
//                            .setParameter("idHotelInvoice", hotelInvoice.getIdhotelInvoice())
//                            .setParameter("idEmployee", ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee());
//                    errMsg = (String) rs.uniqueResult();
//                    if (!errMsg.equals("")) {
//                        if (session.getTransaction().isActive()) {
//                            session.getTransaction().rollback();
//                        }
//                        return false;
//                    }
//                } else {
//                    if (session.getTransaction().isActive()) {
//                        session.getTransaction().rollback();
//                    }
//                    errMsg = "Data tidak dapat dihapus!!";
//                    return false;
//                }
//                session.getTransaction().commit();
//            } catch (Exception e) {
//                if (session.getTransaction().isActive()) {
//                    session.getTransaction().rollback();
//                }
//                errMsg = e.getMessage();
//                return false;
//            } finally {
//                //session.close();
//            }
//        } else {
//            return false;
//        }
//        return true;
//    }
    @Override
    public TblHotelInvoice insertDataHotelInvoice(TblReservationPaymentWithGuaranteePayment dataRPWGP) {
        errMsg = "";
        TblReservationPaymentWithGuaranteePayment tblRPWG = dataRPWGP;
        if (ClassSession.checkUserSession()) {  //check user current session
            try {
                session = HibernateUtil.getSessionFactory().getCurrentSession();
                session.beginTransaction();
                //data hotel invoice (update)
                tblRPWG.getTblHotelReceivable().getTblHotelInvoice().setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                tblRPWG.getTblHotelReceivable().getTblHotelInvoice().setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                session.saveOrUpdate(tblRPWG.getTblHotelReceivable().getTblHotelInvoice());
                //data hotel payable (update)
                tblRPWG.getTblHotelReceivable().setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                tblRPWG.getTblHotelReceivable().setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                session.saveOrUpdate(tblRPWG.getTblHotelReceivable());
                //data reservation payment with guarantee payment (update)
                tblRPWG.setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                tblRPWG.setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                session.update(tblRPWG);
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
        return tblRPWG.getTblHotelReceivable().getTblHotelInvoice();
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
    public List<TblHotelInvoice> getAllDataHotelInvoice() {
        errMsg = "";
        List<TblHotelInvoice> list = new ArrayList<>();
        if (ClassSession.checkUserSession()) {  //check user current session
            try {
                session = HibernateUtil.getSessionFactory().getCurrentSession();
                session.beginTransaction();
                list = session.getNamedQuery("findAllTblHotelInvoice").list();
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
    public List<TblHotelInvoice> getAllDataHotelInvoiceByIDPartnerNotNullAndIDHotelInvoiceType(int idHotelInvoiceType) {
        errMsg = "";
        List<TblHotelInvoice> list = new ArrayList<>();
        if (ClassSession.checkUserSession()) {  //check user current session
            try {
                session = HibernateUtil.getSessionFactory().getCurrentSession();
                session.beginTransaction();
                list = session.getNamedQuery("findAllTblHotelInvoiceByIDPartnerNotNullAndIDHotelInvoiceType")
                        .setParameter("idHotelInvoiceType", idHotelInvoiceType)
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

    @Override
    public List<RefHotelInvoiceType> getAllDataHotelInvoiceType() {
        errMsg = "";
        List<RefHotelInvoiceType> list = new ArrayList<>();
        if (ClassSession.checkUserSession()) {  //check user current session
            try {
                session = HibernateUtil.getSessionFactory().getCurrentSession();
                session.beginTransaction();
                list = session.getNamedQuery("findAllRefHotelInvoiceType").list();
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
    public List<TblGuaranteeLetterItemDetail> getAllDataGuaranteeLetterItemDetailByIDGuaranteeLetter(long idGuaranteeLetter) {
        errMsg = "";
        List<TblGuaranteeLetterItemDetail> list = new ArrayList<>();
        if (ClassSession.checkUserSession()) {  //check user current session
            try {
                session = HibernateUtil.getSessionFactory().getCurrentSession();
                session.beginTransaction();
                list = session.getNamedQuery("findAllTblGuaranteeLetterItemDetailByIDGuaranteeLetter")
                        .setParameter("idGuaranteeLetter", idGuaranteeLetter)
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
    public List<TblReservationPaymentWithGuaranteePayment> getAllDataReservationPaymentWithGuaranteePayment() {
        errMsg = "";
        List<TblReservationPaymentWithGuaranteePayment> list = new ArrayList<>();
        if (ClassSession.checkUserSession()) {  //check user current session
            try {
                session = HibernateUtil.getSessionFactory().getCurrentSession();
                session.beginTransaction();
                list = session.getNamedQuery("findAllTblReservationPaymentWithGuaranteePayment").list();
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
    public List<TblHotelFinanceTransactionHotelReceivable> getAllDataHotelFinanceTransactionHotelReceivableByIDHotelFinanceTransaction(long idHotelFinanceTransaction) {
        errMsg = "";
        List<TblHotelFinanceTransactionHotelReceivable> list = new ArrayList<>();
        if (ClassSession.checkUserSession()) {  //check user current session
            try {
                session = HibernateUtil.getSessionFactory().getCurrentSession();
                session.beginTransaction();
                list = session.getNamedQuery("findAllTblHotelFinanceTransactionHotelReceivableByIDHotelFinanceTransaction")
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
    public List<TblHotelFinanceTransactionHotelReceivable> getAllDataHotelFinanceTransactionHotelReceivableByIDHotelReceivable(long idHotelReceivable) {
        errMsg = "";
        List<TblHotelFinanceTransactionHotelReceivable> list = new ArrayList<>();
        if (ClassSession.checkUserSession()) {  //check user current session
            try {
                session = HibernateUtil.getSessionFactory().getCurrentSession();
                session.beginTransaction();
                list = session.getNamedQuery("findAllTblHotelFinanceTransactionHotelReceivableByIDHotelReceivable")
                        .setParameter("idHotelReceivable", idHotelReceivable)
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
    public TblReservationPaymentWithGuaranteePayment getDataReservationPaymentWithGuaranteePaymentByIDHotelReceivable(long idHotelReceivable) {
        errMsg = "";
        List<TblReservationPaymentWithGuaranteePayment> list = new ArrayList<>();
        if (ClassSession.checkUserSession()) {  //check user current session
            try {
                session = HibernateUtil.getSessionFactory().getCurrentSession();
                session.beginTransaction();
                list = session.getNamedQuery("findAllTblReservationPaymentWithGuaranteePaymentByIDHotelReceivable")
                        .setParameter("idHotelReceivable", idHotelReceivable)
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
    public TblReservationPayment getDataResrevationPayment(long id) {
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

    //--------------------------------------------------------------------------
    @Override
    public List<TblReservationPaymentWithGuaranteePayment> getAllDataReservationPaymentWithGuaranteePaymentByIDPartner(long idPartner) {
        errMsg = "";
        List<TblReservationPaymentWithGuaranteePayment> list = new ArrayList<>();
        if (ClassSession.checkUserSession()) {  //check user current session
            try {
                session = HibernateUtil.getSessionFactory().getCurrentSession();
                session.beginTransaction();
                list = session.getNamedQuery("findAllTblReservationPaymentWithGuaranteePaymentByIDPartner")
                        .setParameter("idPartner", idPartner)
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
    public String getErrorMessage() {
        return errMsg;
    }

}
