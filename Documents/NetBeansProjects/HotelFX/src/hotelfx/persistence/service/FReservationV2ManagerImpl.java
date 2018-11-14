/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hotelfx.persistence.service;

import hotelfx.helper.ClassCoder;
import hotelfx.helper.ClassComp;
import hotelfx.helper.ClassFormatter;
import hotelfx.helper.ClassSession;
import hotelfx.persistence.HibernateUtil;
import hotelfx.persistence.model.LogCompanyBalanceCashFlow;
import hotelfx.persistence.model.LogSystem;
import hotelfx.persistence.model.RefBankAccountHolderStatus;
import hotelfx.persistence.model.RefBankCardType;
import hotelfx.persistence.model.RefCountry;
import hotelfx.persistence.model.RefCustomerStatus;
import hotelfx.persistence.model.RefCustomerType;
import hotelfx.persistence.model.RefEdctransactionStatus;
import hotelfx.persistence.model.RefEndOfDayDataStatus;
import hotelfx.persistence.model.RefFinanceTransactionPaymentType;
import hotelfx.persistence.model.RefFinanceTransactionStatus;
import hotelfx.persistence.model.RefHotelInvoiceType;
import hotelfx.persistence.model.RefHotelReceivableType;
import hotelfx.persistence.model.RefItemGuestType;
import hotelfx.persistence.model.RefItemMutationType;
import hotelfx.persistence.model.RefItemObsoleteBy;
import hotelfx.persistence.model.RefItemType;
import hotelfx.persistence.model.RefLocationType;
import hotelfx.persistence.model.RefPeopleGender;
import hotelfx.persistence.model.RefPeopleIdentifierType;
import hotelfx.persistence.model.RefPeopleReligion;
import hotelfx.persistence.model.RefPeopleStatus;
import hotelfx.persistence.model.RefRecordStatus;
import hotelfx.persistence.model.RefReservationAdditionalItemReservedStatus;
import hotelfx.persistence.model.RefReservationBillDiscountType;
import hotelfx.persistence.model.RefReservationBillType;
import hotelfx.persistence.model.RefReservationCheckInOutStatus;
import hotelfx.persistence.model.RefReservationDiscountType;
import hotelfx.persistence.model.RefReservationOrderByType;
import hotelfx.persistence.model.RefReservationStatus;
import hotelfx.persistence.model.RefRoomCleanStatus;
import hotelfx.persistence.model.RefRoomStatus;
import hotelfx.persistence.model.RefVoucherStatus;
import hotelfx.persistence.model.SysDataHardCode;
import hotelfx.persistence.model.TblBank;
import hotelfx.persistence.model.TblBankAccount;
import hotelfx.persistence.model.TblBankCard;
import hotelfx.persistence.model.TblBankEdc;
import hotelfx.persistence.model.TblBankEdcBankNetworkCard;
import hotelfx.persistence.model.TblBankEventCard;
import hotelfx.persistence.model.TblBankNetworkCard;
import hotelfx.persistence.model.TblCompanyBalance;
import hotelfx.persistence.model.TblCompanyBalanceBankAccount;
import hotelfx.persistence.model.TblCustomer;
import hotelfx.persistence.model.TblCustomerBankAccount;
import hotelfx.persistence.model.TblEmployee;
import hotelfx.persistence.model.TblFloor;
import hotelfx.persistence.model.TblGuaranteeLetterItemDetail;
import hotelfx.persistence.model.TblHotelEvent;
import hotelfx.persistence.model.TblHotelFinanceTransactionHotelReceivable;
import hotelfx.persistence.model.TblHotelInvoice;
import hotelfx.persistence.model.TblHotelReceivable;
import hotelfx.persistence.model.TblItem;
import hotelfx.persistence.model.TblItemLocation;
import hotelfx.persistence.model.TblItemMutationHistory;
import hotelfx.persistence.model.TblItemTypeHk;
import hotelfx.persistence.model.TblItemTypeWh;
import hotelfx.persistence.model.TblLocation;
import hotelfx.persistence.model.TblLocationOfBin;
import hotelfx.persistence.model.TblLocationOfLaundry;
import hotelfx.persistence.model.TblLocationOfWarehouse;
import hotelfx.persistence.model.TblOpenCloseCashierBalance;
import hotelfx.persistence.model.TblOpenCloseCashierBalanceDetail;
import hotelfx.persistence.model.TblPartner;
import hotelfx.persistence.model.TblPartnerBankAccount;
import hotelfx.persistence.model.TblPeople;
import hotelfx.persistence.model.TblReservation;
import hotelfx.persistence.model.TblReservationAdditionalItem;
import hotelfx.persistence.model.TblReservationAdditionalItemReserved;
import hotelfx.persistence.model.TblReservationAdditionalService;
import hotelfx.persistence.model.TblReservationBar;
import hotelfx.persistence.model.TblReservationBill;
import hotelfx.persistence.model.TblReservationBrokenItem;
import hotelfx.persistence.model.TblReservationCalendarBar;
import hotelfx.persistence.model.TblReservationCanceled;
import hotelfx.persistence.model.TblReservationCancelingFee;
import hotelfx.persistence.model.TblReservationCheckInOut;
import hotelfx.persistence.model.TblReservationDefaultBar;
import hotelfx.persistence.model.TblReservationPayment;
import hotelfx.persistence.model.TblReservationPaymentWithBankCard;
import hotelfx.persistence.model.TblReservationPaymentWithCekGiro;
import hotelfx.persistence.model.TblReservationPaymentWithDeposit;
import hotelfx.persistence.model.TblReservationPaymentWithGuaranteePayment;
import hotelfx.persistence.model.TblReservationPaymentWithReservationVoucher;
import hotelfx.persistence.model.TblReservationPaymentWithTransfer;
import hotelfx.persistence.model.TblReservationRescheduleCanceled;
import hotelfx.persistence.model.TblReservationRoomPriceDetail;
import hotelfx.persistence.model.TblReservationRoomTypeDetail;
import hotelfx.persistence.model.TblReservationRoomTypeDetailChildDetail;
import hotelfx.persistence.model.TblReservationRoomTypeDetailItem;
import hotelfx.persistence.model.TblReservationRoomTypeDetailRoomPriceDetail;
import hotelfx.persistence.model.TblReservationRoomTypeDetailRoomPriceDetailChangeRoomHistory;
import hotelfx.persistence.model.TblReservationRoomTypeDetailRoomService;
import hotelfx.persistence.model.TblReservationRoomTypeDetailTravelAgentDiscountDetail;
import hotelfx.persistence.model.TblReservationTravelAgentDiscountDetail;
import hotelfx.persistence.model.TblReservationVoucher;
import hotelfx.persistence.model.TblRoom;
import hotelfx.persistence.model.TblRoomService;
import hotelfx.persistence.model.TblRoomStatusChangeHistory;
import hotelfx.persistence.model.TblRoomType;
import hotelfx.persistence.model.TblRoomTypeItem;
import hotelfx.persistence.model.TblRoomTypeRoomService;
import hotelfx.persistence.model.TblSupplier;
import hotelfx.persistence.model.TblSystemUser;
import hotelfx.persistence.model.TblTravelAgent;
import hotelfx.persistence.model.TblTravelAgentRoomType;
import hotelfx.persistence.model.TblUnit;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import org.hibernate.Session;
import org.hibernate.query.Query;

/**
 *
 * @author ANDRI
 */
public class FReservationV2ManagerImpl implements FReservationV2Manager {

    private Session session;

    private String errMsg;

    public FReservationV2ManagerImpl() {

    }

    @Override
    public TblReservation insertDataReservation(TblReservation reservation,
            TblReservationBill reservationBill,
            List<TblReservationRoomTypeDetail> rrtds,
            List<TblReservationRoomTypeDetailRoomPriceDetail> rrtdrpds,
            List<TblReservationRoomTypeDetailTravelAgentDiscountDetail> rrtdtadds,
            List<TblReservationAdditionalItem> rais,
            List<TblReservationAdditionalService> rass) {
        errMsg = "";
        TblReservation tblReservation = reservation;
        if (ClassSession.checkUserSession()) {  //check user current session
            try {
                session = HibernateUtil.getSessionFactory().getCurrentSession();
                session.beginTransaction();
                //data log
                String headerLog = "BUAT DATA RESERVASI";
                String dataLog = "";
                //data reservation
                tblReservation.setCodeReservation(ClassCoder.getCode("Reservation", session));
                tblReservation.setCreateDate(Timestamp.valueOf(LocalDateTime.now()));
                tblReservation.setTblEmployeeByCreateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                tblReservation.setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                tblReservation.setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                tblReservation.setRefRecordStatus(session.find(RefRecordStatus.class, 1));
                session.saveOrUpdate(tblReservation);
                //data log
                dataLog += "No. Reservasi : " + tblReservation.getCodeReservation() + "\n";
                dataLog += "Tgl. Reservasi : " + ClassFormatter.dateFormate.format(tblReservation.getReservationDate()) + "\n";
                dataLog += "Nama Customer : " + tblReservation.getTblCustomer().getTblPeople().getFullName() + "\n";
                dataLog += "Travel Agent : " + (tblReservation.getTblPartner() != null ? tblReservation.getTblPartner().getPartnerName() : "-") + "\n";
                dataLog += "-----------------------------------------------------------------------\n";
                //data reservation-bill
                reservationBill.setCodeBill(ClassCoder.getCode("Reservation Bill", session));
                reservationBill.setCreateDate(Timestamp.valueOf(LocalDateTime.now()));
                reservationBill.setTblEmployeeByCreateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                reservationBill.setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                reservationBill.setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                reservationBill.setRefRecordStatus(session.find(RefRecordStatus.class, 1));
                session.saveOrUpdate(reservationBill);
                //data log
                dataLog += "No. Tagihan (Reservasi) : " + reservationBill.getCodeBill() + "\n";
                dataLog += "Service Charge (%) : " + ClassFormatter.decimalFormat.cFormat((reservationBill.getServiceChargePercentage().multiply(new BigDecimal("100")))) + "% \n";
                dataLog += "Pajak (%) : " + ClassFormatter.decimalFormat.cFormat((reservationBill.getTaxPercentage().multiply(new BigDecimal("100")))) + "% \n";
                dataLog += "Tipe Diskon : " + (reservationBill.getRefReservationBillDiscountType() != null ? reservationBill.getRefReservationBillDiscountType().getTypeName() : "-") + " \n";
                dataLog += "-----------------------------------------------------------------------\n";
                //data reservation room type detail
                int countID = 1;
                for (TblReservationRoomTypeDetail rrtd : rrtds) {
                    String codeDetail = "";
                    for (int i = 0; i < 5; i++) {
                        codeDetail += "0";
                    }
                    codeDetail += String.valueOf(countID);
                    codeDetail = codeDetail.substring(codeDetail.length() - 5);
                    if (checkRoomAvailable(rrtd, tblReservation.getTblPartner())) {
                        //data reservation room type detail - check in/out
                        if (rrtd.getTblReservationCheckInOut() != null) {
                            rrtd.getTblReservationCheckInOut().setCreateDate(Timestamp.valueOf(LocalDateTime.now()));
                            rrtd.getTblReservationCheckInOut().setTblEmployeeByCreateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                            rrtd.getTblReservationCheckInOut().setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                            rrtd.getTblReservationCheckInOut().setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                            rrtd.getTblReservationCheckInOut().setRefRecordStatus(session.find(RefRecordStatus.class, 1));
                            session.saveOrUpdate(rrtd.getTblReservationCheckInOut());
                        }
                        //data reservation room type detail
                        rrtd.setCodeDetail(codeDetail);
                        rrtd.setCreateDate(Timestamp.valueOf(LocalDateTime.now()));
                        rrtd.setTblEmployeeByCreateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                        rrtd.setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                        rrtd.setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                        rrtd.setRefRecordStatus(session.find(RefRecordStatus.class, 1));
                        session.saveOrUpdate(rrtd);
                        //data reservation room type detail - room service
                        List<TblRoomTypeRoomService> roomTypeRoomServices = getAllRoomTypeRoomServiceByIDRoomType(rrtd.getTblRoomType().getIdroomType());
                        for (TblRoomTypeRoomService roomTypeRoomService : roomTypeRoomServices) {
                            TblReservationRoomTypeDetailRoomService reservationRoomTypeDetailRoomService = new TblReservationRoomTypeDetailRoomService();
                            reservationRoomTypeDetailRoomService.setTblReservationRoomTypeDetail(rrtd);
                            reservationRoomTypeDetailRoomService.setTblRoomService(roomTypeRoomService.getTblRoomService());
                            reservationRoomTypeDetailRoomService.setRoomServiceCost(roomTypeRoomService.getTblRoomService().getPrice());
                            reservationRoomTypeDetailRoomService.setPeopleNumber(roomTypeRoomService.getPeopleNumber());
                            reservationRoomTypeDetailRoomService.setCreateDate(Timestamp.valueOf(LocalDateTime.now()));
                            reservationRoomTypeDetailRoomService.setTblEmployeeByCreateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                            reservationRoomTypeDetailRoomService.setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                            reservationRoomTypeDetailRoomService.setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                            reservationRoomTypeDetailRoomService.setRefRecordStatus(session.find(RefRecordStatus.class, 1));
                            session.saveOrUpdate(reservationRoomTypeDetailRoomService);
                        }
                        //data reservation room type detail - item
                        List<TblRoomTypeItem> roomTypeItems = getAllRoomTypeItemByIDRoomType(rrtd.getTblRoomType().getIdroomType());
                        for (TblRoomTypeItem roomTypeItem : roomTypeItems) {
                            TblReservationRoomTypeDetailItem reservationRoomTypeDetailItem = new TblReservationRoomTypeDetailItem();
                            reservationRoomTypeDetailItem.setTblReservationRoomTypeDetail(rrtd);
                            reservationRoomTypeDetailItem.setTblItem(roomTypeItem.getTblItem());
                            reservationRoomTypeDetailItem.setItemAdditionalCharge(roomTypeItem.getTblItem().getAdditionalCharge());
                            reservationRoomTypeDetailItem.setItemBrokenCharge(roomTypeItem.getTblItem().getBrokenCharge());
                            reservationRoomTypeDetailItem.setItemQuantity(roomTypeItem.getItemQuantity());
                            reservationRoomTypeDetailItem.setCreateDate(Timestamp.valueOf(LocalDateTime.now()));
                            reservationRoomTypeDetailItem.setTblEmployeeByCreateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                            reservationRoomTypeDetailItem.setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                            reservationRoomTypeDetailItem.setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                            reservationRoomTypeDetailItem.setRefRecordStatus(session.find(RefRecordStatus.class, 1));
                            session.saveOrUpdate(reservationRoomTypeDetailItem);
                        }
                    } else {
                        if (session.getTransaction().isActive()) {
                            session.getTransaction().rollback();
                        }
                        errMsg = "Jumlah kamar tidak mencukupi..!";
                        return null;
                    }
                    countID++;
                }
                //data reservation room type detail - room price detail
                for (TblReservationRoomTypeDetailRoomPriceDetail rrtdrpd : rrtdrpds) {
                    //data reservation room price detail
                    rrtdrpd.getTblReservationRoomPriceDetail().setCreateDate(Timestamp.valueOf(LocalDateTime.now()));
                    rrtdrpd.getTblReservationRoomPriceDetail().setTblEmployeeByCreateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                    rrtdrpd.getTblReservationRoomPriceDetail().setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                    rrtdrpd.getTblReservationRoomPriceDetail().setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                    rrtdrpd.getTblReservationRoomPriceDetail().setRefRecordStatus(session.find(RefRecordStatus.class, 1));
                    session.saveOrUpdate(rrtdrpd.getTblReservationRoomPriceDetail());
                    //data reservation room type detail - room price detail
                    rrtdrpd.setCreateDate(Timestamp.valueOf(LocalDateTime.now()));
                    rrtdrpd.setTblEmployeeByCreateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                    rrtdrpd.setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                    rrtdrpd.setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                    rrtdrpd.setRefRecordStatus(session.find(RefRecordStatus.class, 1));
                    session.saveOrUpdate(rrtdrpd);
                }
                //data reservation room type detail - travel agent discount detail
                for (TblReservationRoomTypeDetailTravelAgentDiscountDetail rrtdtadd : rrtdtadds) {
                    //data reservation travel agent discount detail
                    rrtdtadd.getTblReservationTravelAgentDiscountDetail().setCreateDate(Timestamp.valueOf(LocalDateTime.now()));
                    rrtdtadd.getTblReservationTravelAgentDiscountDetail().setTblEmployeeByCreateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                    rrtdtadd.getTblReservationTravelAgentDiscountDetail().setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                    rrtdtadd.getTblReservationTravelAgentDiscountDetail().setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                    rrtdtadd.getTblReservationTravelAgentDiscountDetail().setRefRecordStatus(session.find(RefRecordStatus.class, 1));
                    session.saveOrUpdate(rrtdtadd.getTblReservationTravelAgentDiscountDetail());
                    //data reservation room type detail - travel agent discount detail
                    rrtdtadd.setCreateDate(Timestamp.valueOf(LocalDateTime.now()));
                    rrtdtadd.setTblEmployeeByCreateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                    rrtdtadd.setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                    rrtdtadd.setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                    rrtdtadd.setRefRecordStatus(session.find(RefRecordStatus.class, 1));
                    session.saveOrUpdate(rrtdtadd);
                }
                //data log
                dataLog += "Reservasi Kamar : \n";
                for (TblReservationRoomTypeDetail rrtd : rrtds) {
                    dataLog += "- " + rrtd.getCodeDetail() + " - " + rrtd.getCheckInDateTime() + " s/d " + rrtd.getCheckOutDateTime() + " \t "
                            + (getDataRoom(rrtd)) + " \n";
                    for (TblReservationRoomTypeDetailRoomPriceDetail rrtdrpd : rrtdrpds) {
                        if (rrtdrpd.getTblReservationRoomTypeDetail().getIddetail()
                                == rrtd.getIddetail()) {
                            dataLog += "\t - " + ClassFormatter.dateFormate.format(rrtdrpd.getTblReservationRoomPriceDetail().getDetailDate()) + " \t "
                                    + "Harga : " + ClassFormatter.currencyFormat.cFormat(rrtdrpd.getTblReservationRoomPriceDetail().getDetailPrice()) + " \t "
                                    + "Diskon : " + ClassFormatter.decimalFormat.cFormat(rrtdrpd.getTblReservationRoomPriceDetail().getDetailDiscountPercentage()) + "% (" + getDataDiscountEvent(rrtdrpd.getTblReservationRoomPriceDetail()) + ") \t "
                                    + "Compliment : " + ClassFormatter.currencyFormat.cFormat(rrtdrpd.getTblReservationRoomPriceDetail().getDetailComplimentary()) + " \t "
                                    + getDataTravelAgentCommission(rrtd, rrtdtadds) + " \n";
                        }
                    }
                }
                //data log
                dataLog += "-----------------------------------------------------------------------\n";
                dataLog += "Tambahan Layanan : \n";
                //data reservation additional service
                for (TblReservationAdditionalService ras : rass) {
                    //data reservation additional service
                    ras.setCreateDate(Timestamp.valueOf(LocalDateTime.now()));
                    ras.setTblEmployeeByCreateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                    ras.setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                    ras.setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                    ras.setRefRecordStatus(session.find(RefRecordStatus.class, 1));
                    session.saveOrUpdate(ras);
                    //data log
                    dataLog += "- " + ClassFormatter.dateFormate.format(ras.getAdditionalDate()) + " - " + ras.getTblRoomService().getServiceName() + " \t "
                            + "Harga : " + ClassFormatter.currencyFormat.cFormat(ras.getPrice()) + " \t "
                            + "Diskon : " + ClassFormatter.decimalFormat.cFormat(ras.getDiscountPercentage()) + "% (" + getDataDiscountEvent(ras) + ") \n";
                }
                //data log
                dataLog += "-----------------------------------------------------------------------\n";
                dataLog += "Tambahan Barang : \n";
                //data reservation additional item
                for (TblReservationAdditionalItem rai : rais) {
                    //data reservation additional item
                    rai.setCreateDate(Timestamp.valueOf(LocalDateTime.now()));
                    rai.setTblEmployeeByCreateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                    rai.setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                    rai.setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                    rai.setRefRecordStatus(session.find(RefRecordStatus.class, 1));
                    session.saveOrUpdate(rai);
                    //data log
                    dataLog += "- " + ClassFormatter.dateFormate.format(rai.getAdditionalDate()) + " - " + rai.getTblItem().getItemName() + " \t "
                            + "Harga : " + ClassFormatter.currencyFormat.cFormat(rai.getItemCharge()) + " \t "
                            + "Diskon : " + ClassFormatter.decimalFormat.cFormat(rai.getDiscountPercentage()) + "% (" + getDataDiscountEvent(rai) + ") \t "
                            + "Jumlah : " + ClassFormatter.decimalFormat.cFormat(rai.getItemQuantity()) + " \n";
                }
                //data log
                dataLog += "-----------------------------------------------------------------------\n";
                //data log
                LogSystem logSystem = new LogSystem();
                logSystem.setLogDateTime(Timestamp.valueOf(LocalDateTime.now()));
                logSystem.setTblSystemUser(session.find(TblSystemUser.class, ClassSession.currentUser.getIduser()));
                logSystem.setTblEmployee(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                logSystem.setLogHeader(headerLog);
                logSystem.setLogDetail(dataLog);
                logSystem.setHostName(ClassComp.getHostName());
                session.saveOrUpdate(logSystem);
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
        return tblReservation;
    }

    private boolean checkRoomAvailable(TblReservationRoomTypeDetail reservationRoomTypeDetail,
            TblPartner partner) {
        boolean available = true;
        LocalDate dateCount = LocalDate.parse(
                (new SimpleDateFormat("yyyy-MM-dd")).format(reservationRoomTypeDetail.getCheckInDateTime())
                + "T"
                + (new SimpleDateFormat("HH:mm:ss").format(reservationRoomTypeDetail.getCheckInDateTime())),
                DateTimeFormatter.ISO_DATE_TIME);
        while (Date.valueOf(dateCount).before(reservationRoomTypeDetail.getCheckOutDateTime())) {
            int availableNumber = getRoomAvailableNumber(reservationRoomTypeDetail.getTblRoomType(), dateCount, partner)
                    - getRoomReservedNumber(reservationRoomTypeDetail.getTblRoomType(), dateCount, partner);
            if ((availableNumber - 1) < 0) {
                available = false;
                break;
            }
            dateCount = dateCount.plusDays(1);
        }
        return available;
    }

    private boolean checkRoomAvailableMinimalZero(TblReservationRoomTypeDetail reservationRoomTypeDetail,
            TblPartner partner) {
        boolean available = true;
        LocalDate dateCount = LocalDate.parse(
                (new SimpleDateFormat("yyyy-MM-dd")).format(reservationRoomTypeDetail.getCheckInDateTime())
                + "T"
                + (new SimpleDateFormat("HH:mm:ss").format(reservationRoomTypeDetail.getCheckInDateTime())),
                DateTimeFormatter.ISO_DATE_TIME);
        while (Date.valueOf(dateCount).before(reservationRoomTypeDetail.getCheckOutDateTime())) {
            int availableNumber = getRoomAvailableNumber(reservationRoomTypeDetail.getTblRoomType(), dateCount, partner)
                    - getRoomReservedNumber(reservationRoomTypeDetail.getTblRoomType(), dateCount, partner);
            if ((availableNumber) < 0) {
                available = false;
                break;
            }
            dateCount = dateCount.plusDays(1);
        }
        return available;
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
                        //@ctive? + in controller (getRoomReservedNumber())
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
                                //@ctive? + in controller (getRoomReservedNumber())
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

    /**
     * FOR DATA LOG
     */
    private String getDataRoom(TblReservationRoomTypeDetail dataRTDetail) {
        if (dataRTDetail.getTblReservationCheckInOut() != null
                && dataRTDetail.getTblReservationCheckInOut().getTblRoom() != null) {
            return "Kamar : " + dataRTDetail.getTblReservationCheckInOut().getTblRoom().getRoomName() + " \t "
                    + "Status : " + dataRTDetail.getTblReservationCheckInOut().getRefReservationCheckInOutStatus().getStatusName();
        }
        return "";
    }

    private String getDataTravelAgentCommission(TblReservationRoomTypeDetail dataRTDetail,
            List<TblReservationRoomTypeDetailTravelAgentDiscountDetail> dataRTDTADDs) {
        for (TblReservationRoomTypeDetailTravelAgentDiscountDetail dataRTDTADD : dataRTDTADDs) {
            if (dataRTDTADD.getTblReservationRoomTypeDetail().getIddetail()
                    == dataRTDetail.getIddetail()) {
                return "Komisi : " + ClassFormatter.decimalFormat.cFormat(dataRTDTADD.getTblReservationTravelAgentDiscountDetail().getDetailDiscount()) + "% ("
                        + dataRTDetail.getTblReservation().getTblPartner().getPartnerName() + ")";
            }
        }
        return "";
    }

    private String getDataDiscountEvent(TblReservationRoomPriceDetail dataRoomPriceDetail) {
        if (dataRoomPriceDetail.getTblBankEventCard() != null) {
            return dataRoomPriceDetail.getTblBankEventCard().getCodeEvent() + " - " + dataRoomPriceDetail.getTblBankEventCard().getEventName();
        } else {
            if (dataRoomPriceDetail.getTblHotelEvent() != null) {
                return dataRoomPriceDetail.getTblHotelEvent().getCodeEvent() + " - " + dataRoomPriceDetail.getTblHotelEvent().getEventName();
            } else {
                return "-";
            }
        }
    }

    private String getDataDiscountEvent(TblReservationAdditionalService dataAdditionalService) {
        if (dataAdditionalService.getTblBankEventCard() != null) {
            return dataAdditionalService.getTblBankEventCard().getCodeEvent() + " - " + dataAdditionalService.getTblBankEventCard().getEventName();
        } else {
            if (dataAdditionalService.getTblHotelEvent() != null) {
                return dataAdditionalService.getTblHotelEvent().getCodeEvent() + " - " + dataAdditionalService.getTblHotelEvent().getEventName();
            } else {
                return "-";
            }
        }
    }

    private String getDataDiscountEvent(TblReservationAdditionalItem dataAdditionalItem) {
        if (dataAdditionalItem.getTblBankEventCard() != null) {
            return dataAdditionalItem.getTblBankEventCard().getCodeEvent() + " - " + dataAdditionalItem.getTblBankEventCard().getEventName();
        } else {
            if (dataAdditionalItem.getTblHotelEvent() != null) {
                return dataAdditionalItem.getTblHotelEvent().getCodeEvent() + " - " + dataAdditionalItem.getTblHotelEvent().getEventName();
            } else {
                return "-";
            }
        }
    }

    private String getDataLocationOfWarehouseName(TblLocation dataLocation) {
        String result = "-";
        List<TblLocationOfWarehouse> listWarehouse = session.getNamedQuery("findAllTblLocationOfWarehouseByIdLocation")
                .setParameter("id", dataLocation.getIdlocation())
                .list();
        if (!listWarehouse.isEmpty()) {
            result = listWarehouse.get(0).getWarehouseName();
            session.evict(listWarehouse.get(0));
        }
        return result;
    }

    private List<TblRoomTypeRoomService> getAllRoomTypeRoomServiceByIDRoomType(long idRoomType) {
        List<TblRoomTypeRoomService> list = session.getNamedQuery("findAllTblRoomTypeRoomServiceByIDRoomType")
                .setParameter("idRoomType", idRoomType)
                .list();
        return list;
    }

    private List<TblRoomTypeItem> getAllRoomTypeItemByIDRoomType(long idRoomType) {
        List<TblRoomTypeItem> list = session.getNamedQuery("findAllTblRoomTypeItemByIDRoomType")
                .setParameter("idRoomType", idRoomType)
                .list();
        return list;
    }

    //--------------------------------------------------------------------------
    @Override
    public boolean updateDataReservation(TblReservation reservation) {
        errMsg = "";
        if (ClassSession.checkUserSession()) {  //check user current session
            try {
                session = HibernateUtil.getSessionFactory().getCurrentSession();
                session.beginTransaction();
                //data log
                String headerLog = "UBAH DATA RESERVASI";
                String dataLog = "";
                //data reservation
                reservation.setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                reservation.setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                session.update(reservation);
                //data log
                dataLog += "No. Reservasi : " + reservation.getCodeReservation() + "\n";
                dataLog += "Tgl. Reservasi : " + ClassFormatter.dateFormate.format(reservation.getReservationDate()) + "\n";
                dataLog += "Nama Customer : " + reservation.getTblCustomer().getTblPeople().getFullName() + "\n";
                dataLog += "Travel Agent : " + (reservation.getTblPartner() != null ? reservation.getTblPartner().getPartnerName() : "-") + "\n";
                dataLog += "-----------------------------------------------------------------------\n";
                LogSystem logSystem = new LogSystem();
                logSystem.setLogDateTime(Timestamp.valueOf(LocalDateTime.now()));
                logSystem.setTblSystemUser(session.find(TblSystemUser.class, ClassSession.currentUser.getIduser()));
                logSystem.setTblEmployee(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                logSystem.setLogHeader(headerLog);
                logSystem.setLogDetail(dataLog);
                logSystem.setHostName(ClassComp.getHostName());
                session.saveOrUpdate(logSystem);
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
    public boolean updateDataReservationTransactionPayments(
            TblReservation reservation,
            List<TblReservationRoomTypeDetail> reservationRoomTypeDetails,
            TblReservationBill reservationBill,
            List<TblReservationPayment> reservationPayments,
            List<TblReservationPaymentWithTransfer> reservationPaymentWithTransfers,
            List<TblReservationPaymentWithBankCard> reservationPaymentWithBankCards,
            List<TblReservationPaymentWithCekGiro> reservationPaymentWithCekGiros,
            List<TblReservationPaymentWithGuaranteePayment> reservationPaymentWithGuaranteePayments,
            List<TblReservationPaymentWithReservationVoucher> reservationPaymentWithReservationVouchers,
            List<TblGuaranteeLetterItemDetail> guaranteeLetterItemDetails,
            BigDecimal reservationBillNominal) {
        errMsg = "";
        if (ClassSession.checkUserSession()) {  //check user current session
            try {
                session = HibernateUtil.getSessionFactory().getCurrentSession();
                session.beginTransaction();
                //data log
                String headerLog = "BUAT DATA TRANSAKSI PEMBAYARAN RESERVASI";
                String dataLog = "";
                //data reservation
                reservation.setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                reservation.setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                session.update(reservation);
                //data log
                dataLog += "No. Reservasi : " + reservation.getCodeReservation() + "\n";
                dataLog += "Tgl. Reservasi : " + ClassFormatter.dateFormate.format(reservation.getReservationDate()) + "\n";
                dataLog += "Nama Customer : " + reservation.getTblCustomer().getTblPeople().getFullName() + "\n";
                dataLog += "Travel Agent : " + (reservation.getTblPartner() != null ? reservation.getTblPartner().getPartnerName() : "-") + "\n";
                dataLog += "-----------------------------------------------------------------------\n";
                //data reservation room type detail ###
                for (TblReservationRoomTypeDetail reservationRoomTypeDetail : reservationRoomTypeDetails) {
                    if (!checkRoomAvailableMinimalZero(reservationRoomTypeDetail, null)) {
                        if (session.getTransaction().isActive()) {
                            session.getTransaction().rollback();
                        }
                        errMsg = "Jumlah kamar tidak mencukupi..!";
                        return false;
                    }
                }
                //data reservation bill
                reservationBill.setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                reservationBill.setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                session.update(reservationBill);
                //data log
                dataLog += "No. Tagihan (" + reservationBill.getRefReservationBillType().getTypeName() + ") : " + reservationBill.getCodeBill() + "\n";
                dataLog += "Service Charge (%) : " + ClassFormatter.decimalFormat.cFormat((reservationBill.getServiceChargePercentage().multiply(new BigDecimal("100")))) + "% \n";
                dataLog += "Pajak (%) : " + ClassFormatter.decimalFormat.cFormat((reservationBill.getTaxPercentage().multiply(new BigDecimal("100")))) + "% \n";
                dataLog += "Tipe Diskon : " + (reservationBill.getRefReservationBillDiscountType() != null ? reservationBill.getRefReservationBillDiscountType().getTypeName() : "-") + " \n";
                dataLog += "-----------------------------------------------------------------------\n";
                dataLog += "Transaksi Pembayaran : \n";
                //open close company balance
                TblOpenCloseCashierBalance dataOpenCloseCashierBalance = getDataOpenCloseCashierBalanceIsActive();
                //data cashier balance (company)
                TblCompanyBalance dataCashierBalance = session.find(TblCompanyBalance.class, (long) 3);
                //data reservation transaction payment
                for (TblReservationPayment reservationPayment : reservationPayments) {
                    if (reservationPayment.getIdpayment() == 0L) {  //Just save new data (transaction payment)
                        //data payment discount & charge
                        BigDecimal paymentDiscount = new BigDecimal("0");
                        BigDecimal paymentCharge = new BigDecimal("0");
                        reservationPayment.setCodePayment(ClassCoder.getCode("Reservation Payment", session));
                        reservationPayment.setCreateDate(Timestamp.valueOf(LocalDateTime.now()));
                        reservationPayment.setTblEmployeeByCreateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                        reservationPayment.setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                        reservationPayment.setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                        reservationPayment.setRefRecordStatus(session.find(RefRecordStatus.class, 1));
                        session.saveOrUpdate(reservationPayment);
                        //data log
                        dataLog += "- " + reservationPayment.getCodePayment() + " - " + ClassFormatter.dateTimeFormate.format(reservationPayment.getPaymentDate()) + " \t "
                                + "Nominal : " + ClassFormatter.currencyFormat.cFormat(reservationPayment.getUnitNominal()) + " \t "
                                + "Tipe : " + reservationPayment.getRefFinanceTransactionPaymentType().getTypeName() + " \n";
                        //@@@%%%
                        //data bank account (customer)
                        TblBankAccount customerBankAccount = null;
                        //data company balance - bank account
                        TblCompanyBalanceBankAccount companyBalanceBankAccount = null;
                        //data reservation transaction payment - with transfer
                        for (TblReservationPaymentWithTransfer reservationPaymentWithTransfer : reservationPaymentWithTransfers) {
                            if (reservationPaymentWithTransfer.getTblReservationPayment().equals(reservationPayment)) {
                                reservationPaymentWithTransfer.setCreateDate(Timestamp.valueOf(LocalDateTime.now()));
                                reservationPaymentWithTransfer.setTblEmployeeByCreateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                                reservationPaymentWithTransfer.setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                                reservationPaymentWithTransfer.setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                                reservationPaymentWithTransfer.setRefRecordStatus(session.find(RefRecordStatus.class, 1));
                                session.saveOrUpdate(reservationPaymentWithTransfer);
                                //@@@%%%
                                //data bank account (customer)
                                customerBankAccount = reservationPaymentWithTransfer.getTblBankAccountBySenderBankAccount();
                                //data company balance - bank account
                                companyBalanceBankAccount = getCompanyBalanceBankAccountByIDCompanyBalanceAndIDBankAccount((long) 1, reservationPaymentWithTransfer.getTblBankAccountByReceiverBankAccount().getIdbankAccount());    //hotel balance = '1'
                                break;
                            }
                        }
                        //data reservation transaction payment - with bank card
                        for (TblReservationPaymentWithBankCard reservationPaymentWithBankCard : reservationPaymentWithBankCards) {
                            if (reservationPaymentWithBankCard.getTblReservationPayment().equals(reservationPayment)) {
                                reservationPaymentWithBankCard.setRefEdctransactionStatus(session.find(RefEdctransactionStatus.class, 0)); //Sale = '0'
                                reservationPaymentWithBankCard.setCreateDate(Timestamp.valueOf(LocalDateTime.now()));
                                reservationPaymentWithBankCard.setTblEmployeeByCreateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                                reservationPaymentWithBankCard.setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                                reservationPaymentWithBankCard.setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                                reservationPaymentWithBankCard.setRefRecordStatus(session.find(RefRecordStatus.class, 1));
                                session.saveOrUpdate(reservationPaymentWithBankCard);
                                //@@@%%%
                                //data company balance - bank account
                                companyBalanceBankAccount = getCompanyBalanceBankAccountByIDCompanyBalanceAndIDBankAccount((long) 1, reservationPaymentWithBankCard.getTblBankAccount().getIdbankAccount());    //hotel balance = '1'
                                //minus payment discount n payment charge (MDR)
                                if (companyBalanceBankAccount != null) {
//                                    companyBalanceBankAccount.setCompanyBalanceBankAccountNominal(companyBalanceBankAccount.getCompanyBalanceBankAccountNominal()
//                                            .subtract(reservationPaymentWithBankCard.getPaymentDiscount())
//                                            .subtract(reservationPaymentWithBankCard.getPaymentCharge()));
                                    paymentDiscount = paymentDiscount.add(reservationPaymentWithBankCard.getPaymentDiscount());
                                    paymentCharge = paymentCharge.add(reservationPaymentWithBankCard.getPaymentCharge());
                                }
                                break;
                            }
                        }
                        //data reservation transaction payment - with cek/giro
                        for (TblReservationPaymentWithCekGiro reservationPaymentWithCekGiro : reservationPaymentWithCekGiros) {
                            if (reservationPaymentWithCekGiro.getTblReservationPayment().equals(reservationPayment)) {
                                reservationPaymentWithCekGiro.setCreateDate(Timestamp.valueOf(LocalDateTime.now()));
                                reservationPaymentWithCekGiro.setTblEmployeeByCreateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                                reservationPaymentWithCekGiro.setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                                reservationPaymentWithCekGiro.setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                                reservationPaymentWithCekGiro.setRefRecordStatus(session.find(RefRecordStatus.class, 1));
                                session.saveOrUpdate(reservationPaymentWithCekGiro);
                                //@@@%%%
                                //data bank account (customer)
                                customerBankAccount = reservationPaymentWithCekGiro.getTblBankAccountBySenderBankAccount();
                                //data company balance - bank account
                                companyBalanceBankAccount = getCompanyBalanceBankAccountByIDCompanyBalanceAndIDBankAccount((long) 1, reservationPaymentWithCekGiro.getTblBankAccountByReceiverBankAccount().getIdbankAccount());    //hotel balance = '1'
                                break;
                            }
                        }
                        //data reservation transaction payment - with guarantee payment
                        for (TblReservationPaymentWithGuaranteePayment reservationPaymentWithGuaranteePayment : reservationPaymentWithGuaranteePayments) {
                            if (reservationPaymentWithGuaranteePayment.getTblReservationPayment().equals(reservationPayment)) {
                                reservationPaymentWithGuaranteePayment.setCreateDate(Timestamp.valueOf(LocalDateTime.now()));
                                reservationPaymentWithGuaranteePayment.setTblEmployeeByCreateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                                reservationPaymentWithGuaranteePayment.setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                                reservationPaymentWithGuaranteePayment.setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                                reservationPaymentWithGuaranteePayment.setRefRecordStatus(session.find(RefRecordStatus.class, 1));
                                session.saveOrUpdate(reservationPaymentWithGuaranteePayment);
                                //data guarantee letter - item detail
                                for (TblGuaranteeLetterItemDetail guaranteeLetterItemDetail : guaranteeLetterItemDetails) {
                                    if (guaranteeLetterItemDetail.getTblReservationPaymentWithGuaranteePayment().equals(reservationPaymentWithGuaranteePayment)) {
                                        guaranteeLetterItemDetail.setCreateDate(Timestamp.valueOf(LocalDateTime.now()));
                                        guaranteeLetterItemDetail.setTblEmployeeByCreateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                                        guaranteeLetterItemDetail.setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                                        guaranteeLetterItemDetail.setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                                        guaranteeLetterItemDetail.setRefRecordStatus(session.find(RefRecordStatus.class, 1));
                                        session.saveOrUpdate(guaranteeLetterItemDetail);
                                    }
                                }
                                //@@@%%%
                                if (reservationPaymentWithGuaranteePayment.getIsDebt()) {
                                    //data bank account (customer)
                                    customerBankAccount = null;
                                    //data company balance - bank account
                                    companyBalanceBankAccount = null;
                                } else {
                                    //data bank account (customer)
                                    customerBankAccount = reservationPaymentWithGuaranteePayment.getTblBankAccountBySenderBankAccount();
                                    //data company balance - bank account
                                    companyBalanceBankAccount = getCompanyBalanceBankAccountByIDCompanyBalanceAndIDBankAccount((long) 1, reservationPaymentWithGuaranteePayment.getTblBankAccountByReceiverBankAccount().getIdbankAccount());    //hotel balance = '1'
                                }
                                //data reservation with guarantee letter - hotel receivable
                                if (reservationPaymentWithGuaranteePayment.getIsDebt()) {
                                    //data hotel invoice
                                    TblHotelInvoice hotelInvoice = new TblHotelInvoice();
                                    hotelInvoice.setCodeHotelInvoice(ClassCoder.getCode("Hotel Invoice", session));
                                    hotelInvoice.setTblPartner(reservationPaymentWithGuaranteePayment.getTblPartner());
                                    hotelInvoice.setTblSupplier(null);
                                    hotelInvoice.setRefHotelInvoiceType(session.find(RefHotelInvoiceType.class, 1));    //Receivable = '1'
                                    hotelInvoice.setCreateDate(Timestamp.valueOf(LocalDateTime.now()));
                                    hotelInvoice.setTblEmployeeByCreateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                                    hotelInvoice.setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                                    hotelInvoice.setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                                    hotelInvoice.setRefRecordStatus(session.find(RefRecordStatus.class, 1));
                                    session.saveOrUpdate(hotelInvoice);
                                    //data hotel receivable
                                    TblHotelReceivable hotelReceivable = new TblHotelReceivable();
                                    hotelReceivable.setTblHotelInvoice(hotelInvoice);
                                    hotelReceivable.setHotelReceivableNominal(reservationPayment.getUnitNominal());
                                    hotelReceivable.setRefHotelReceivableType(session.find(RefHotelReceivableType.class, 1));   //Guarantee Letter = '1'
                                    hotelReceivable.setRefFinanceTransactionStatus(session.find(RefFinanceTransactionStatus.class, 0));    //Belum Dibayar = '0'
                                    hotelReceivable.setCreateDate(Timestamp.valueOf(LocalDateTime.now()));
                                    hotelReceivable.setTblEmployeeByCreateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                                    hotelReceivable.setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                                    hotelReceivable.setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                                    hotelReceivable.setRefRecordStatus(session.find(RefRecordStatus.class, 1));
                                    session.saveOrUpdate(hotelReceivable);
                                    //data reservation with guarantee payment - (hotel receivable)
                                    reservationPaymentWithGuaranteePayment.setTblHotelReceivable(hotelReceivable);
                                    reservationPaymentWithGuaranteePayment.setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                                    reservationPaymentWithGuaranteePayment.setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                                    session.saveOrUpdate(reservationPaymentWithGuaranteePayment);
                                }
                                break;
                            }
                        }
                        //data reservation transaction payment - with reservation voucher
                        for (TblReservationPaymentWithReservationVoucher reservationPaymentWithReservationVoucher : reservationPaymentWithReservationVouchers) {
                            if (reservationPaymentWithReservationVoucher.getTblReservationPayment().equals(reservationPayment)) {
                                reservationPaymentWithReservationVoucher.setCreateDate(Timestamp.valueOf(LocalDateTime.now()));
                                reservationPaymentWithReservationVoucher.setTblEmployeeByCreateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                                reservationPaymentWithReservationVoucher.setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                                reservationPaymentWithReservationVoucher.setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                                reservationPaymentWithReservationVoucher.setRefRecordStatus(session.find(RefRecordStatus.class, 1));
                                session.saveOrUpdate(reservationPaymentWithReservationVoucher);
                                //data reservation voucher (update:obsolete)
                                reservationPaymentWithReservationVoucher.getTblReservationVoucher().setRefVoucherStatus(session.find(RefVoucherStatus.class, 2));
                                reservationPaymentWithReservationVoucher.getTblReservationVoucher().setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                                reservationPaymentWithReservationVoucher.getTblReservationVoucher().setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                                session.update(reservationPaymentWithReservationVoucher.getTblReservationVoucher());
                                break;
                            }
                        }
                        //@@@%%%
                        //data balance (cashier)
                        if (reservationPayment.getRefFinanceTransactionPaymentType().getIdtype() == (long) 0) {   //plus
//                                || reservationPayment.getRefFinanceTransactionPaymentType().getIdtype() == (long) 13) {   //plus
                            //data cashier balance (company)
                            dataCashierBalance = session.find(TblCompanyBalance.class, (long) 3);
                            dataCashierBalance.setBalanceNominal(dataCashierBalance.getBalanceNominal().add(reservationPayment.getUnitNominal()));
                            dataCashierBalance.setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                            dataCashierBalance.setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                            session.update(dataCashierBalance);
                            //data company balance (cash-flow log)
                            LogCompanyBalanceCashFlow logCompanyBalanceCashFlow = new LogCompanyBalanceCashFlow();
                            logCompanyBalanceCashFlow.setTblCompanyBalanceByIdsenderCompanyBalance(null);
                            logCompanyBalanceCashFlow.setTblBankAccountByIdsenderCbbankAccount(null);
                            logCompanyBalanceCashFlow.setTblCompanyBalanceByIdreceiverCompanyBalance(session.find(TblCompanyBalance.class, (long) 3));  //Kasir = '3'
                            logCompanyBalanceCashFlow.setTblBankAccountByIdreceiverCbbankAccount(null);
                            logCompanyBalanceCashFlow.setTransferNominal(reservationPayment.getUnitNominal());
                            logCompanyBalanceCashFlow.setHistoryDate(Timestamp.valueOf(LocalDateTime.now()));
                            logCompanyBalanceCashFlow.setTblEmployee(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                            logCompanyBalanceCashFlow.setHistoryStystemNote("Pembayaran reservasi oleh " + reservation.getTblCustomer().getTblPeople().getFullName() + ", \nsebesar : "
                                    + ClassFormatter.currencyFormat.format(reservationPayment.getUnitNominal()) + " ("
                                    + reservationPayment.getRefFinanceTransactionPaymentType().getTypeName() + ")");
                            session.saveOrUpdate(logCompanyBalanceCashFlow);
                        }
//                        if (reservationPayment.getRefFinanceTransactionPaymentType().getIdtype() == (long) 14) {   //minus
//                            TblCompanyBalance dataBalance = session.find(TblCompanyBalance.class, (long) 3);
//                            dataBalance.setBalanceNominal(dataBalance.getBalanceNominal() - reservationPayment.getUnitNominal());
//                            dataBalance.setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
//                            dataBalance.setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
//                            session.update(dataBalance);
//                        }
                        //data balance (company)
                        if (reservationPayment.getRefFinanceTransactionPaymentType().getIdtype() > (long) 0
                                && reservationPayment.getRefFinanceTransactionPaymentType().getIdtype() < (long) 9) {   //plus
                            if (companyBalanceBankAccount != null) {
//                                //data balance (company)
//                                TblCompanyBalance dataBalance = session.find(TblCompanyBalance.class, (long) 1);
//                                dataBalance.setBalanceNominal(dataBalance.getBalanceNominal().add(reservationPayment.getUnitNominal())
//                                        .subtract(paymentDiscount).subtract(paymentCharge));
//                                dataBalance.setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
//                                dataBalance.setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
//                                session.update(dataBalance);
//                                //data balance (company) : bank account
//                                companyBalanceBankAccount = session.find(TblCompanyBalanceBankAccount.class, companyBalanceBankAccount.getIdrelation());
//                                companyBalanceBankAccount.setCompanyBalanceBankAccountNominal(companyBalanceBankAccount.getCompanyBalanceBankAccountNominal().add(reservationPayment.getUnitNominal())
//                                        .subtract(paymentDiscount).subtract(paymentCharge));
//                                companyBalanceBankAccount.setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
//                                companyBalanceBankAccount.setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
//                                session.update(companyBalanceBankAccount);
//                                //data company balance (cash-flow log)
//                                LogCompanyBalanceCashFlow logCompanyBalanceCashFlow = new LogCompanyBalanceCashFlow();
//                                logCompanyBalanceCashFlow.setTblCompanyBalanceByIdsenderCompanyBalance(null);
//                                logCompanyBalanceCashFlow.setTblBankAccountByIdsenderCbbankAccount(customerBankAccount);
//                                logCompanyBalanceCashFlow.setTblCompanyBalanceByIdreceiverCompanyBalance(dataBalance);
//                                logCompanyBalanceCashFlow.setTblBankAccountByIdreceiverCbbankAccount(companyBalanceBankAccount.getTblBankAccount());
//                                logCompanyBalanceCashFlow.setTransferNominal(reservationPayment.getUnitNominal()
//                                        .subtract(paymentDiscount).subtract(paymentCharge));
//                                logCompanyBalanceCashFlow.setHistoryDate(Timestamp.valueOf(LocalDateTime.now()));
//                                logCompanyBalanceCashFlow.setTblEmployee(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
//                                logCompanyBalanceCashFlow.setHistoryStystemNote("Pembayaran reservasi oleh " + reservation.getTblCustomer().getTblPeople().getFullName() + ", \nsebesar : "
//                                        + ClassFormatter.currencyFormat.format(reservationPayment.getUnitNominal())
//                                        + ((paymentCharge.compareTo(new BigDecimal("0")) != 0) ? (", MDR:" + ClassFormatter.currencyFormat.format(paymentCharge)) : "")
//                                        + " ("
//                                        + reservationPayment.getRefFinanceTransactionPaymentType().getTypeName() + ")");
//                                session.saveOrUpdate(logCompanyBalanceCashFlow);
                            }
                        }
//                        if (reservationPayment.getRefFinanceTransactionPaymentType().getIdtype() == (long) 16) {   //minus
//                            if (companyBalanceBankAccount != null) {
//                                //data balance (company)
//                                TblCompanyBalance dataBalance = session.find(TblCompanyBalance.class, (long) 1);
//                                dataBalance.setBalanceNominal(dataBalance.getBalanceNominal() - reservationPayment.getUnitNominal());
//                                dataBalance.setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
//                                dataBalance.setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
//                                session.update(dataBalance);
//                                //data balance (company) : bank account
//                                companyBalanceBankAccount = session.find(TblCompanyBalanceBankAccount.class, companyBalanceBankAccount.getIdrelation());
//                                companyBalanceBankAccount.setCompanyBalanceBankAccountNominal(companyBalanceBankAccount.getCompanyBalanceBankAccountNominal() - reservationPayment.getUnitNominal());
//                                companyBalanceBankAccount.setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
//                                companyBalanceBankAccount.setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
//                                session.update(companyBalanceBankAccount);
//                            }
//                        }
                        //auto settle
                        if ((reservationPayment.getRefFinanceTransactionPaymentType().getIdtype() < (long) 1
                                && reservationPayment.getRefFinanceTransactionPaymentType().getIdtype() > (long) 8)
                                || companyBalanceBankAccount == null) { //is debt, payment -> debt (data hasn't been used)
                            //data reservation payment
                            reservationPayment.setSettleDate(Timestamp.valueOf(LocalDateTime.now()));
                            session.update(reservationPayment);
                        }
                        //open close company balance - detail (cashier)
                        if (dataOpenCloseCashierBalance != null) {
                            TblOpenCloseCashierBalanceDetail dataOpenCloseCashierBalanceDetail = new TblOpenCloseCashierBalanceDetail();
                            dataOpenCloseCashierBalanceDetail.setTblOpenCloseCashierBalance(dataOpenCloseCashierBalance);
                            dataOpenCloseCashierBalanceDetail.setTblReservationPayment(reservationPayment);
                            dataOpenCloseCashierBalanceDetail.setDetailData(getDetailData(reservationPayment));
                            dataOpenCloseCashierBalanceDetail.setCreateDate(Timestamp.valueOf(LocalDateTime.now()));
                            dataOpenCloseCashierBalanceDetail.setTblEmployeeByCreateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                            dataOpenCloseCashierBalanceDetail.setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                            dataOpenCloseCashierBalanceDetail.setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                            dataOpenCloseCashierBalanceDetail.setRefRecordStatus(session.find(RefRecordStatus.class, 1));
                            session.saveOrUpdate(dataOpenCloseCashierBalanceDetail);
                        }
                    }
                }
                if (isAllCheckOut(reservation)
                        && (reservationBillNominal
                        .compareTo(new BigDecimal("0")) < 1)) {
                    //data transaction return (auto, -balance cashier balance)
                    if (reservationBillNominal
                            .compareTo(new BigDecimal("0")) == -1) {
                        //data reservation payment (return payment)
                        TblReservationPayment dataReservationReturnPayment = new TblReservationPayment();
                        dataReservationReturnPayment.setCodePayment(ClassCoder.getCode("Reservation Payment", session));
                        dataReservationReturnPayment.setTblReservationBill(reservationBill);
                        dataReservationReturnPayment.setPaymentDate(Timestamp.valueOf(LocalDateTime.now()));
                        dataReservationReturnPayment.setUnitNominal(getNominalAfterRounding(reservationBillNominal.abs()));
                        dataReservationReturnPayment.setRoundingValue(getRoundingValue(reservationBillNominal.abs()));
                        dataReservationReturnPayment.setTblEmployeeByIdcashier(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                        dataReservationReturnPayment.setRefFinanceTransactionPaymentType(session.find(RefFinanceTransactionPaymentType.class, 16));     //Return = '16'
                        dataReservationReturnPayment.setRefReservationBillType(session.find(RefReservationBillType.class, 0));    //reservasi = '0'
                        dataReservationReturnPayment.setSettleDate(Timestamp.valueOf(LocalDateTime.now()));     //auto settle
                        dataReservationReturnPayment.setCreateDate(Timestamp.valueOf(LocalDateTime.now()));
                        dataReservationReturnPayment.setTblEmployeeByCreateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                        dataReservationReturnPayment.setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                        dataReservationReturnPayment.setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                        dataReservationReturnPayment.setRefRecordStatus(session.find(RefRecordStatus.class, 1));
                        session.saveOrUpdate(dataReservationReturnPayment);
                        //data log
                        dataLog += "Pengembalian Pembayaran : " + ClassFormatter.currencyFormat.cFormat(dataReservationReturnPayment.getUnitNominal()) + ")\n";
                        dataLog += "-----------------------------------------------------------------------\n";
                        //open close company balance - detail (cashier)
                        if (dataOpenCloseCashierBalance != null) {
                            TblOpenCloseCashierBalanceDetail dataOpenCloseCashierBalanceDetail = new TblOpenCloseCashierBalanceDetail();
                            dataOpenCloseCashierBalanceDetail.setTblOpenCloseCashierBalance(dataOpenCloseCashierBalance);
                            dataOpenCloseCashierBalanceDetail.setTblReservationPayment(dataReservationReturnPayment);
                            dataOpenCloseCashierBalanceDetail.setDetailData(getDetailData(dataReservationReturnPayment));
                            dataOpenCloseCashierBalanceDetail.setCreateDate(Timestamp.valueOf(LocalDateTime.now()));
                            dataOpenCloseCashierBalanceDetail.setTblEmployeeByCreateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                            dataOpenCloseCashierBalanceDetail.setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                            dataOpenCloseCashierBalanceDetail.setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                            dataOpenCloseCashierBalanceDetail.setRefRecordStatus(session.find(RefRecordStatus.class, 1));
                            session.saveOrUpdate(dataOpenCloseCashierBalanceDetail);
                        }
                        //data company balance (cashier-)
                        dataCashierBalance.setBalanceNominal(dataCashierBalance.getBalanceNominal().subtract(dataReservationReturnPayment.getUnitNominal()));
                        dataCashierBalance.setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                        dataCashierBalance.setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                        session.update(dataCashierBalance);
                        //data company balance (cash-flow log)
                        LogCompanyBalanceCashFlow logCompanyBalanceCashFlow = new LogCompanyBalanceCashFlow();
                        logCompanyBalanceCashFlow.setTblCompanyBalanceByIdsenderCompanyBalance(null);
                        logCompanyBalanceCashFlow.setTblCompanyBalanceByIdsenderCompanyBalance(dataCashierBalance);
                        logCompanyBalanceCashFlow.setTblBankAccountByIdsenderCbbankAccount(null);
                        logCompanyBalanceCashFlow.setTblCompanyBalanceByIdreceiverCompanyBalance(null);
                        logCompanyBalanceCashFlow.setTblBankAccountByIdreceiverCbbankAccount(null);
                        logCompanyBalanceCashFlow.setTransferNominal(dataReservationReturnPayment.getUnitNominal());
                        logCompanyBalanceCashFlow.setHistoryDate(Timestamp.valueOf(LocalDateTime.now()));
                        logCompanyBalanceCashFlow.setTblEmployee(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                        logCompanyBalanceCashFlow.setHistoryStystemNote("Pengembalian pembayaran kepada " + reservation.getTblCustomer().getTblPeople().getFullName() + ", \nsebesar : "
                                + ClassFormatter.currencyFormat.format(dataReservationReturnPayment.getUnitNominal()) + " ("
                                + dataReservationReturnPayment.getRefFinanceTransactionPaymentType().getTypeName() + ")");
                        session.saveOrUpdate(logCompanyBalanceCashFlow);
                    }
                    //data reservation (update:checkout)
                    reservation.setDepartureTime(Timestamp.valueOf(LocalDateTime.now()));
                    reservation.setRefReservationStatus(session.find(RefReservationStatus.class, 5));   //Checked Out = '5'
                    reservation.setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                    reservation.setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                    session.update(reservation);
                }
                //data log
                dataLog += "-----------------------------------------------------------------------\n";
                LogSystem logSystem = new LogSystem();
                logSystem.setLogDateTime(Timestamp.valueOf(LocalDateTime.now()));
                logSystem.setTblSystemUser(session.find(TblSystemUser.class, ClassSession.currentUser.getIduser()));
                logSystem.setTblEmployee(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                logSystem.setLogHeader(headerLog);
                logSystem.setLogDetail(dataLog);
                logSystem.setHostName(ClassComp.getHostName());
                session.saveOrUpdate(logSystem);
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

    private BigDecimal getNominalAfterRounding(BigDecimal nominal) {
        if (nominal != null) {
            BigDecimal nominalAfterRounding = ((nominal.divide(new BigDecimal("100"))).setScale(0, RoundingMode.UP)).multiply(new BigDecimal("100"));
            return nominalAfterRounding;
        } else {
            return new BigDecimal("0");
        }
    }

    private BigDecimal getRoundingValue(BigDecimal nominal) {
        if (nominal != null) {
            BigDecimal nominalAfterRounding = ((nominal.divide(new BigDecimal("100"))).setScale(0, RoundingMode.UP)).multiply(new BigDecimal("100"));
            return nominalAfterRounding.subtract(nominal);
        } else {
            return new BigDecimal("0");
        }
    }

    private String getDetailData(TblReservationPayment tblReservationPayment) {
        String result = "";
        if (tblReservationPayment != null) {
            if (tblReservationPayment.getRefFinanceTransactionPaymentType().getIdtype() == (long) 0) {   //Tunai
                result += "Penambahan Kas dari Transaksi Pembayaran Reservasi No. "
                        + tblReservationPayment.getCodePayment()
                        + " (" + tblReservationPayment.getRefFinanceTransactionPaymentType().getTypeName() + ")"
                        + "\n";
                result += "Nominal pembayaran transaksi reservasi : " + ClassFormatter.currencyFormat.cFormat(tblReservationPayment.getUnitNominal());
            } else {
                if (tblReservationPayment.getRefFinanceTransactionPaymentType().getIdtype() > (long) 0
                        && tblReservationPayment.getRefFinanceTransactionPaymentType().getIdtype() < (long) 9) {
                    result += "Lainnya (Pembayaran) : Transaksi Reservasi No. "
                            + tblReservationPayment.getCodePayment()
                            + " (" + tblReservationPayment.getRefFinanceTransactionPaymentType().getTypeName() + ")"
                            + "\n";
                    result += "Nominal transaksi reservasi : " + ClassFormatter.currencyFormat.cFormat(tblReservationPayment.getUnitNominal());
                } else {
                    if (tblReservationPayment.getRefFinanceTransactionPaymentType().getIdtype() == (long) 16) {
                        result += "Pengurangan Kas karena adanya Transaksi Pengembalian Pembayaran Reservasi \n No. "
                                + tblReservationPayment.getCodePayment()
                                + " (" + tblReservationPayment.getRefFinanceTransactionPaymentType().getTypeName() + ")"
                                + "\n";
                        result += "Nominal pembayaran transaksi reservasi : " + ClassFormatter.currencyFormat.cFormat(tblReservationPayment.getUnitNominal());
                    } else {
                        if (tblReservationPayment.getRefFinanceTransactionPaymentType().getIdtype() == (long) 13) {
                            result += "Lainnya (Penyimpanan Deposit) : Transaksi Reservasi No. "
                                    + tblReservationPayment.getCodePayment()
                                    //                                    + " (" + tblReservationPayment.getRefFinanceTransactionPaymentType().getTypeName() + ")"
                                    + "\n";
                            result += "Nominal transaksi reservasi : " + ClassFormatter.currencyFormat.cFormat(tblReservationPayment.getUnitNominal());
                        } else {
                            if (tblReservationPayment.getRefFinanceTransactionPaymentType().getIdtype() == (long) 14) {
                                result += "Lainnya (Pengembalian Deposit) : Transaksi Reservasi No. "
                                        + tblReservationPayment.getCodePayment()
                                        //                                        + " (" + tblReservationPayment.getRefFinanceTransactionPaymentType().getTypeName() + ")"
                                        + "\n";
                                result += "Nominal transaksi reservasi : " + ClassFormatter.currencyFormat.cFormat(tblReservationPayment.getUnitNominal());
                            }
                        }
                    }
                }
            }
        }
        return result;
    }

    @Override
    public boolean insertDataReservationRoomTypeDetails(
            TblReservation reservation,
            TblReservationBill reservationBill,
            List<TblReservationRoomTypeDetail> reservationRoomTypeDetails,
            List<TblReservationRoomTypeDetailRoomPriceDetail> reservationRoomTypeDetailRoomPriceDetails,
            List<TblReservationRoomTypeDetailTravelAgentDiscountDetail> reservationRoomTypeDetailTravelAgentDiscountDetails,
            List<TblReservationAdditionalItem> reservationAdditionalItems,
            List<TblReservationAdditionalService> reservationAdditionalServices) {
        errMsg = "";
        if (ClassSession.checkUserSession()) {  //check user current session
            try {
                session = HibernateUtil.getSessionFactory().getCurrentSession();
                session.beginTransaction();
                //data log
                String headerLog = "BUAT DATA RESERVASI KAMAR";
                String dataLog = "";
                //data reservation
                reservation.setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                reservation.setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                session.update(reservation);
                //data log
                dataLog += "No. Reservasi : " + reservation.getCodeReservation() + "\n";
                dataLog += "Tgl. Reservasi : " + ClassFormatter.dateFormate.format(reservation.getReservationDate()) + "\n";
                dataLog += "Nama Customer : " + reservation.getTblCustomer().getTblPeople().getFullName() + "\n";
                dataLog += "Travel Agent : " + (reservation.getTblPartner() != null ? reservation.getTblPartner().getPartnerName() : "-") + "\n";
                dataLog += "-----------------------------------------------------------------------\n";
                //data reservation bill
                reservationBill.setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                reservationBill.setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                session.update(reservationBill);
                //data reservation room type detail
                int countID = getReservationRoomTypeDetialLastCode(reservation.getIdreservation()) + 1;
                for (TblReservationRoomTypeDetail reservationRoomTypeDetail : reservationRoomTypeDetails) {
                    if (reservationRoomTypeDetail.getCodeDetail() == null) {
                        String codeDetail = "";
                        for (int i = 0; i < 5; i++) {
                            codeDetail += "0";
                        }
                        codeDetail += String.valueOf(countID);
                        codeDetail = codeDetail.substring(codeDetail.length() - 5);
                        reservationRoomTypeDetail.setCodeDetail(codeDetail);
                        countID++;
                    }
                    //data reservation room type detail - check in/out
                    if (reservationRoomTypeDetail.getTblReservationCheckInOut() != null) {
                        reservationRoomTypeDetail.getTblReservationCheckInOut().setCreateDate(Timestamp.valueOf(LocalDateTime.now()));
                        reservationRoomTypeDetail.getTblReservationCheckInOut().setTblEmployeeByCreateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                        reservationRoomTypeDetail.getTblReservationCheckInOut().setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                        reservationRoomTypeDetail.getTblReservationCheckInOut().setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                        reservationRoomTypeDetail.getTblReservationCheckInOut().setRefRecordStatus(session.find(RefRecordStatus.class, 1));
                        session.saveOrUpdate(reservationRoomTypeDetail.getTblReservationCheckInOut());
                    }
                    //data reservation room type detail
                    reservationRoomTypeDetail.setCreateDate(Timestamp.valueOf(LocalDateTime.now()));
                    reservationRoomTypeDetail.setTblEmployeeByCreateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                    reservationRoomTypeDetail.setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                    reservationRoomTypeDetail.setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                    reservationRoomTypeDetail.setRefRecordStatus(session.find(RefRecordStatus.class, 1));
                    session.saveOrUpdate(reservationRoomTypeDetail);
                    System.out.println("zzz : " + reservationRoomTypeDetail.getIddetail());
                    //data reservation room type detail - room service (delete)
                    Query rs = session.getNamedQuery("deleteAllTblReservationRoomTypeDetailRoomServiceByIDRoomTypeDetail")
                            .setParameter("idRoomTypeDetail", reservationRoomTypeDetail.getIddetail())
                            .setParameter("idEmployee", ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee());
                    errMsg = (String) rs.uniqueResult();
                    if (!errMsg.equals("")) {
                        if (session.getTransaction().isActive()) {
                            session.getTransaction().rollback();
                        }
                        return false;
                    }
                    //data reservation room type detail - room service (insert)
                    List<TblRoomTypeRoomService> roomTypeRoomServices = getAllRoomTypeRoomServiceByIDRoomType(reservationRoomTypeDetail.getTblRoomType().getIdroomType());
                    for (TblRoomTypeRoomService roomTypeRoomService : roomTypeRoomServices) {
                        TblReservationRoomTypeDetailRoomService reservationRoomTypeDetailRoomService = new TblReservationRoomTypeDetailRoomService();
                        reservationRoomTypeDetailRoomService.setTblReservationRoomTypeDetail(reservationRoomTypeDetail);
                        reservationRoomTypeDetailRoomService.setTblRoomService(roomTypeRoomService.getTblRoomService());
                        reservationRoomTypeDetailRoomService.setRoomServiceCost(roomTypeRoomService.getTblRoomService().getPrice());
                        reservationRoomTypeDetailRoomService.setPeopleNumber(roomTypeRoomService.getPeopleNumber());
                        reservationRoomTypeDetailRoomService.setCreateDate(Timestamp.valueOf(LocalDateTime.now()));
                        reservationRoomTypeDetailRoomService.setTblEmployeeByCreateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                        reservationRoomTypeDetailRoomService.setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                        reservationRoomTypeDetailRoomService.setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                        reservationRoomTypeDetailRoomService.setRefRecordStatus(session.find(RefRecordStatus.class, 1));
                        session.saveOrUpdate(reservationRoomTypeDetailRoomService);
                    }
                    //data reservation room type detail - item (delete)
                    rs = session.getNamedQuery("deleteAllTblReservationRoomTypeDetailItemByIDRoomTypeDetail")
                            .setParameter("idRoomTypeDetail", reservationRoomTypeDetail.getIddetail())
                            .setParameter("idEmployee", ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee());
                    errMsg = (String) rs.uniqueResult();
                    if (!errMsg.equals("")) {
                        if (session.getTransaction().isActive()) {
                            session.getTransaction().rollback();
                        }
                        return false;
                    }
                    //data reservation room type detail - item (insert)
                    List<TblRoomTypeItem> roomTypeItems = getAllRoomTypeItemByIDRoomType(reservationRoomTypeDetail.getTblRoomType().getIdroomType());
                    for (TblRoomTypeItem roomTypeItem : roomTypeItems) {
                        TblReservationRoomTypeDetailItem reservationRoomTypeDetailItem = new TblReservationRoomTypeDetailItem();
                        reservationRoomTypeDetailItem.setTblReservationRoomTypeDetail(reservationRoomTypeDetail);
                        reservationRoomTypeDetailItem.setTblItem(roomTypeItem.getTblItem());
                        reservationRoomTypeDetailItem.setItemAdditionalCharge(roomTypeItem.getTblItem().getAdditionalCharge());
                        reservationRoomTypeDetailItem.setItemBrokenCharge(roomTypeItem.getTblItem().getBrokenCharge());
                        reservationRoomTypeDetailItem.setItemQuantity(roomTypeItem.getItemQuantity());
                        reservationRoomTypeDetailItem.setCreateDate(Timestamp.valueOf(LocalDateTime.now()));
                        reservationRoomTypeDetailItem.setTblEmployeeByCreateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                        reservationRoomTypeDetailItem.setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                        reservationRoomTypeDetailItem.setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                        reservationRoomTypeDetailItem.setRefRecordStatus(session.find(RefRecordStatus.class, 1));
                        session.saveOrUpdate(reservationRoomTypeDetailItem);
                    }
                }
                //data reservation room type detail - room price detail
                for (TblReservationRoomTypeDetailRoomPriceDetail reservationRoomTypeDetailRoomPriceDetail : reservationRoomTypeDetailRoomPriceDetails) {
                    //data reservation room price detail
                    reservationRoomTypeDetailRoomPriceDetail.getTblReservationRoomPriceDetail().setCreateDate(Timestamp.valueOf(LocalDateTime.now()));
                    reservationRoomTypeDetailRoomPriceDetail.getTblReservationRoomPriceDetail().setTblEmployeeByCreateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                    reservationRoomTypeDetailRoomPriceDetail.getTblReservationRoomPriceDetail().setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                    reservationRoomTypeDetailRoomPriceDetail.getTblReservationRoomPriceDetail().setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                    reservationRoomTypeDetailRoomPriceDetail.getTblReservationRoomPriceDetail().setRefRecordStatus(session.find(RefRecordStatus.class, 1));
                    session.saveOrUpdate(reservationRoomTypeDetailRoomPriceDetail.getTblReservationRoomPriceDetail());
                    //data reservation room type detail - room price detail
                    reservationRoomTypeDetailRoomPriceDetail.setCreateDate(Timestamp.valueOf(LocalDateTime.now()));
                    reservationRoomTypeDetailRoomPriceDetail.setTblEmployeeByCreateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                    reservationRoomTypeDetailRoomPriceDetail.setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                    reservationRoomTypeDetailRoomPriceDetail.setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                    reservationRoomTypeDetailRoomPriceDetail.setRefRecordStatus(session.find(RefRecordStatus.class, 1));
                    session.saveOrUpdate(reservationRoomTypeDetailRoomPriceDetail);
                }
                //data reservation room type detail - travel agent discount detail
                for (TblReservationRoomTypeDetailTravelAgentDiscountDetail reservationRoomTypeDetailTravelAgentDiscountDetail : reservationRoomTypeDetailTravelAgentDiscountDetails) {
                    //data reservation travel agent discount detail
                    reservationRoomTypeDetailTravelAgentDiscountDetail.getTblReservationTravelAgentDiscountDetail().setCreateDate(Timestamp.valueOf(LocalDateTime.now()));
                    reservationRoomTypeDetailTravelAgentDiscountDetail.getTblReservationTravelAgentDiscountDetail().setTblEmployeeByCreateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                    reservationRoomTypeDetailTravelAgentDiscountDetail.getTblReservationTravelAgentDiscountDetail().setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                    reservationRoomTypeDetailTravelAgentDiscountDetail.getTblReservationTravelAgentDiscountDetail().setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                    reservationRoomTypeDetailTravelAgentDiscountDetail.getTblReservationTravelAgentDiscountDetail().setRefRecordStatus(session.find(RefRecordStatus.class, 1));
                    session.saveOrUpdate(reservationRoomTypeDetailTravelAgentDiscountDetail.getTblReservationTravelAgentDiscountDetail());
                    //data reservation room type detail - travel agent discount detail
                    reservationRoomTypeDetailTravelAgentDiscountDetail.setCreateDate(Timestamp.valueOf(LocalDateTime.now()));
                    reservationRoomTypeDetailTravelAgentDiscountDetail.setTblEmployeeByCreateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                    reservationRoomTypeDetailTravelAgentDiscountDetail.setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                    reservationRoomTypeDetailTravelAgentDiscountDetail.setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                    reservationRoomTypeDetailTravelAgentDiscountDetail.setRefRecordStatus(session.find(RefRecordStatus.class, 1));
                    session.saveOrUpdate(reservationRoomTypeDetailTravelAgentDiscountDetail);
                }
                //data log
                dataLog += "Reservasi Kamar : \n";
                for (TblReservationRoomTypeDetail reservationRoomTypeDetail : reservationRoomTypeDetails) {
                    dataLog += "- " + reservationRoomTypeDetail.getCodeDetail() + " - " + reservationRoomTypeDetail.getCheckInDateTime() + " s/d " + reservationRoomTypeDetail.getCheckOutDateTime() + " \t "
                            + (getDataRoom(reservationRoomTypeDetail)) + " \n";
                    for (TblReservationRoomTypeDetailRoomPriceDetail reservationRoomTypeDetailRoomPriceDetail : reservationRoomTypeDetailRoomPriceDetails) {
                        if (reservationRoomTypeDetailRoomPriceDetail.getTblReservationRoomTypeDetail().getIddetail()
                                == reservationRoomTypeDetail.getIddetail()) {
                            dataLog += "\t - " + ClassFormatter.dateFormate.format(reservationRoomTypeDetailRoomPriceDetail.getTblReservationRoomPriceDetail().getDetailDate()) + " \t "
                                    + "Harga : " + ClassFormatter.currencyFormat.cFormat(reservationRoomTypeDetailRoomPriceDetail.getTblReservationRoomPriceDetail().getDetailPrice()) + " \t "
                                    + "Diskon : " + ClassFormatter.decimalFormat.cFormat(reservationRoomTypeDetailRoomPriceDetail.getTblReservationRoomPriceDetail().getDetailDiscountPercentage()) + "% (" + getDataDiscountEvent(reservationRoomTypeDetailRoomPriceDetail.getTblReservationRoomPriceDetail()) + ") \t "
                                    + "Compliment : " + ClassFormatter.currencyFormat.cFormat(reservationRoomTypeDetailRoomPriceDetail.getTblReservationRoomPriceDetail().getDetailComplimentary()) + " \t "
                                    + getDataTravelAgentCommission(reservationRoomTypeDetail, reservationRoomTypeDetailTravelAgentDiscountDetails) + " \n";
                        }
                    }
                }
                //data log
                dataLog += "-----------------------------------------------------------------------\n";
                //data reservation additional service
                for (TblReservationAdditionalService reservationAdditionalService : reservationAdditionalServices) {
                    //data reservation additional service
                    reservationAdditionalService.setCreateDate(Timestamp.valueOf(LocalDateTime.now()));
                    reservationAdditionalService.setTblEmployeeByCreateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                    reservationAdditionalService.setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                    reservationAdditionalService.setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                    reservationAdditionalService.setRefRecordStatus(session.find(RefRecordStatus.class, 1));
                    session.saveOrUpdate(reservationAdditionalService);
                    //data log
                    dataLog += "Tambahan Layanan : \n";
                    dataLog += "- " + ClassFormatter.dateFormate.format(reservationAdditionalService.getAdditionalDate()) + " - " + reservationAdditionalService.getTblRoomService().getServiceName() + " \t "
                            + "Harga : " + ClassFormatter.currencyFormat.cFormat(reservationAdditionalService.getPrice()) + " \t "
                            + "Diskon : " + ClassFormatter.decimalFormat.cFormat(reservationAdditionalService.getDiscountPercentage()) + "% (" + getDataDiscountEvent(reservationAdditionalService) + ") \n";
                }
                //data log
                dataLog += "-----------------------------------------------------------------------\n";
                dataLog += "Tambahan Barang : \n";
                //data reservation additional item
                for (TblReservationAdditionalItem reservationAdditionalItem : reservationAdditionalItems) {
                    //data reservation additional item
                    reservationAdditionalItem.setCreateDate(Timestamp.valueOf(LocalDateTime.now()));
                    reservationAdditionalItem.setTblEmployeeByCreateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                    reservationAdditionalItem.setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                    reservationAdditionalItem.setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                    reservationAdditionalItem.setRefRecordStatus(session.find(RefRecordStatus.class, 1));
                    session.saveOrUpdate(reservationAdditionalItem);
                    //data log
                    dataLog += "- " + ClassFormatter.dateFormate.format(reservationAdditionalItem.getAdditionalDate()) + " - " + reservationAdditionalItem.getTblItem().getItemName() + " \t "
                            + "Harga : " + ClassFormatter.currencyFormat.cFormat(reservationAdditionalItem.getItemCharge()) + " \t "
                            + "Diskon : " + ClassFormatter.decimalFormat.cFormat(reservationAdditionalItem.getDiscountPercentage()) + "% (" + getDataDiscountEvent(reservationAdditionalItem) + ") \t "
                            + "Jumlah : " + ClassFormatter.decimalFormat.cFormat(reservationAdditionalItem.getItemQuantity()) + " \n";
                }
                //data log
                dataLog += "-----------------------------------------------------------------------\n";
                LogSystem logSystem = new LogSystem();
                logSystem.setLogDateTime(Timestamp.valueOf(LocalDateTime.now()));
                logSystem.setTblSystemUser(session.find(TblSystemUser.class, ClassSession.currentUser.getIduser()));
                logSystem.setTblEmployee(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                logSystem.setLogHeader(headerLog);
                logSystem.setLogDetail(dataLog);
                logSystem.setHostName(ClassComp.getHostName());
                session.saveOrUpdate(logSystem);
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

    private int getReservationRoomTypeDetialLastCode(long idReservation) {
        int lastCode = 0;
        List<TblReservationRoomTypeDetail> list = session.getNamedQuery("findAllTblReservationRoomTypeDetailByIDReservationOrderByIDReservationRoomTypeDetailDescIgnoreRefRecordStatus")
                .setParameter("idReservation", idReservation)
                .list();
        if (!list.isEmpty()) {
            lastCode = Integer.parseInt(list.get(0).getCodeDetail());
            //remove data from cache
            session.evict(list.get(0));
        }
        return lastCode;
    }

    @Override
    public boolean deleteDataReservationRoomTypeDetail(
            TblReservation reservation,
            TblReservationBill reservationBill,
            TblReservationRoomTypeDetail reservationRoomTypeDetail) {
        errMsg = "";
        if (ClassSession.checkUserSession()) {  //check user current session
            try {
                session = HibernateUtil.getSessionFactory().getCurrentSession();
                session.beginTransaction();
                //data log
                String headerLog = "HAPUS DATA RESERVASI KAMAR";
                String dataLog = "";
                if (reservationRoomTypeDetail.getRefRecordStatus().getIdstatus() == 1) {
                    //data reservation
                    reservation.setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                    reservation.setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                    session.update(reservation);
                    //data log
                    dataLog += "No. Reservasi : " + reservation.getCodeReservation() + "\n";
                    dataLog += "Tgl. Reservasi : " + ClassFormatter.dateFormate.format(reservation.getReservationDate()) + "\n";
                    dataLog += "Nama Customer : " + reservation.getTblCustomer().getTblPeople().getFullName() + "\n";
                    dataLog += "Travel Agent : " + (reservation.getTblPartner() != null ? reservation.getTblPartner().getPartnerName() : "-") + "\n";
                    dataLog += "-----------------------------------------------------------------------\n";
                    //data reservation room type detail - room price detail
                    Query rs = session.getNamedQuery("deleteAllTblReservationRoomTypeDetailRoomPriceDetailByIDRoomTypeDetail")
                            .setParameter("idRoomTypeDetail", reservationRoomTypeDetail.getIddetail())
                            .setParameter("idEmployee", ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee());
                    errMsg = (String) rs.uniqueResult();
                    if (!errMsg.equals("")) {
                        if (session.getTransaction().isActive()) {
                            session.getTransaction().rollback();
                        }
                        return false;
                    }
                    //data reservation room type detail - travel agent discount detail
                    rs = session.getNamedQuery("deleteAllTblReservationRoomTypeDetailTravelAgentDiscountDetailByIDRoomTypeDetail")
                            .setParameter("idRoomTypeDetail", reservationRoomTypeDetail.getIddetail())
                            .setParameter("idEmployee", ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee());
                    errMsg = (String) rs.uniqueResult();
                    if (!errMsg.equals("")) {
                        if (session.getTransaction().isActive()) {
                            session.getTransaction().rollback();
                        }
                        return false;
                    }
                    //data log
                    dataLog += "Reservasi Kamar : \n";
                    dataLog += "- " + reservationRoomTypeDetail.getCodeDetail() + " - " + reservationRoomTypeDetail.getCheckInDateTime() + " s/d " + reservationRoomTypeDetail.getCheckOutDateTime() + " \t "
                            + (getDataRoom(reservationRoomTypeDetail)) + " \n";
                    dataLog += "- data tambahan layanan \n";
                    dataLog += "- data tambahan barang \n";
                    //data reservation additional item
                    rs = session.getNamedQuery("deleteAllTblReservationAdditionalItemByIDRoomTypeDetail")
                            .setParameter("idRoomTypeDetail", reservationRoomTypeDetail.getIddetail())
                            .setParameter("idEmployee", ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee());
                    errMsg = (String) rs.uniqueResult();
                    if (!errMsg.equals("")) {
                        if (session.getTransaction().isActive()) {
                            session.getTransaction().rollback();
                        }
                        return false;
                    }
                    //data reservation additional service
                    rs = session.getNamedQuery("deleteAllTblReservationAdditionalServiceByIDRoomTypeDetail")
                            .setParameter("idRoomTypeDetail", reservationRoomTypeDetail.getIddetail())
                            .setParameter("idEmployee", ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee());
                    errMsg = (String) rs.uniqueResult();
                    if (!errMsg.equals("")) {
                        if (session.getTransaction().isActive()) {
                            session.getTransaction().rollback();
                        }
                        return false;
                    }
                    //data reservation room type detail - room service (delete)
                    rs = session.getNamedQuery("deleteAllTblReservationRoomTypeDetailRoomServiceByIDRoomTypeDetail")
                            .setParameter("idRoomTypeDetail", reservationRoomTypeDetail.getIddetail())
                            .setParameter("idEmployee", ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee());
                    errMsg = (String) rs.uniqueResult();
                    if (!errMsg.equals("")) {
                        if (session.getTransaction().isActive()) {
                            session.getTransaction().rollback();
                        }
                        return false;
                    }
                    //data reservation room type detail - item (delete)
                    rs = session.getNamedQuery("deleteAllTblReservationRoomTypeDetailItemByIDRoomTypeDetail")
                            .setParameter("idRoomTypeDetail", reservationRoomTypeDetail.getIddetail())
                            .setParameter("idEmployee", ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee());
                    errMsg = (String) rs.uniqueResult();
                    if (!errMsg.equals("")) {
                        if (session.getTransaction().isActive()) {
                            session.getTransaction().rollback();
                        }
                        return false;
                    }
                    //data reservation additional item - reserved
                    rs = session.getNamedQuery("deleteAllTblReservationAdditionalItemReservedByIDRoomTypeDetail")
                            .setParameter("idRoomTypeDetail", reservationRoomTypeDetail.getIddetail())
                            .setParameter("idEmployee", ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee());
                    errMsg = (String) rs.uniqueResult();
                    if (!errMsg.equals("")) {
                        if (session.getTransaction().isActive()) {
                            session.getTransaction().rollback();
                        }
                        return false;
                    }
                    //data reservation room type detail
                    reservationRoomTypeDetail.setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                    reservationRoomTypeDetail.setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                    reservationRoomTypeDetail.setRefRecordStatus(session.find(RefRecordStatus.class, 2));
                    session.update(reservationRoomTypeDetail);
                    //data reservation - discount type                
                    List<TblReservationRoomTypeDetail> list = session.getNamedQuery("findAllTblReservationRoomTypeDetailByIDReservation")
                            .setParameter("idReservation", reservation.getIdreservation())
                            .list();
                    if (list.isEmpty()) {
                        //data reservation
                        reservationBill.setTblBankCard(null);
                        reservationBill.setRefReservationBillDiscountType(null);
                        reservationBill.setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                        reservationBill.setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                        session.update(reservationBill);
                    }
                } else {
                    if (session.getTransaction().isActive()) {
                        session.getTransaction().rollback();
                    }
                    errMsg = "Data tidak dapat dihapus!!";
                    return false;
                }
                //data log
                LogSystem logSystem = new LogSystem();
                logSystem.setLogDateTime(Timestamp.valueOf(LocalDateTime.now()));
                logSystem.setTblSystemUser(session.find(TblSystemUser.class, ClassSession.currentUser.getIduser()));
                logSystem.setTblEmployee(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                logSystem.setLogHeader(headerLog);
                logSystem.setLogDetail(dataLog);
                logSystem.setHostName(ClassComp.getHostName());
                session.saveOrUpdate(logSystem);
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
    public boolean updateDataReservationRoomTypeDetailCheckIn(
            TblReservation reservation,
            TblReservationRoomTypeDetail reservationRoomTypeDetail,
            List<TblReservationRoomTypeDetailChildDetail> reservationChildInfos) {
        errMsg = "";
        if (ClassSession.checkUserSession()) {  //check user current session
            try {
                session = HibernateUtil.getSessionFactory().getCurrentSession();
                session.beginTransaction();
                //data log
                String headerLog = "BUAT DATA CHECKIN (RESERVASI)";
                String dataLog = "";
                //data reservation
                if (reservation.getArrivalTime() == null) {
                    reservation.setArrivalTime(Timestamp.valueOf(LocalDateTime.now()));
                }
                reservation.setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                reservation.setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                session.update(reservation);
                //data log
                dataLog += "No. Reservasi : " + reservation.getCodeReservation() + "\n";
                dataLog += "Tgl. Reservasi : " + ClassFormatter.dateFormate.format(reservation.getReservationDate()) + "\n";
                dataLog += "Nama Customer : " + reservation.getTblCustomer().getTblPeople().getFullName() + "\n";
                dataLog += "Travel Agent : " + (reservation.getTblPartner() != null ? reservation.getTblPartner().getPartnerName() : "-") + "\n";
                dataLog += "-----------------------------------------------------------------------\n";
                //data reservation checkinout(in)
                reservationRoomTypeDetail.getTblReservationCheckInOut().setCreateDate(Timestamp.valueOf(LocalDateTime.now()));
                reservationRoomTypeDetail.getTblReservationCheckInOut().setTblEmployeeByCreateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                reservationRoomTypeDetail.getTblReservationCheckInOut().setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                reservationRoomTypeDetail.getTblReservationCheckInOut().setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                reservationRoomTypeDetail.getTblReservationCheckInOut().setRefRecordStatus(session.find(RefRecordStatus.class, 1));
                session.saveOrUpdate(reservationRoomTypeDetail.getTblReservationCheckInOut());
                //data reservation room type detail
                reservationRoomTypeDetail.setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                reservationRoomTypeDetail.setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                session.update(reservationRoomTypeDetail);
                //data log
                dataLog += "Reservasi Kamar : \n";
                dataLog += "- " + reservationRoomTypeDetail.getCodeDetail() + " - " + reservationRoomTypeDetail.getCheckInDateTime() + " s/d " + reservationRoomTypeDetail.getCheckOutDateTime() + " \t "
                        + (getDataRoom(reservationRoomTypeDetail)) + " \n";
                dataLog += "-----------------------------------------------------------------------\n";
                //data room
                reservationRoomTypeDetail.getTblReservationCheckInOut().getTblRoom().setRefRoomStatus(session.get(RefRoomStatus.class, 1)); //Occupied Clean = '1'
                //data room status change history
                TblRoomStatusChangeHistory dataRoomStatusChangeHistory = new TblRoomStatusChangeHistory();
                dataRoomStatusChangeHistory.setTblRoom(reservationRoomTypeDetail.getTblReservationCheckInOut().getTblRoom());
                dataRoomStatusChangeHistory.setTblEmployeeByIdemployee(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                dataRoomStatusChangeHistory.setTblRoomStatusDetail(null);
                dataRoomStatusChangeHistory.setChangeRoomStatusDate(Timestamp.valueOf(LocalDateTime.now()));
                dataRoomStatusChangeHistory.setRefRoomStatusByRoomStatusOld(getOldRoomStatus(reservationRoomTypeDetail.getTblReservationCheckInOut().getTblRoom().getIdroom()));
                dataRoomStatusChangeHistory.setRefRoomStatusByRoomStatusNew(reservationRoomTypeDetail.getTblReservationCheckInOut().getTblRoom().getRefRoomStatus());
                dataRoomStatusChangeHistory.setCreateDate(Timestamp.valueOf(LocalDateTime.now()));
                dataRoomStatusChangeHistory.setTblEmployeeByCreateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                dataRoomStatusChangeHistory.setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                dataRoomStatusChangeHistory.setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                dataRoomStatusChangeHistory.setRefRecordStatus(session.find(RefRecordStatus.class, 1));
                session.saveOrUpdate(dataRoomStatusChangeHistory);
                //data room
                reservationRoomTypeDetail.getTblReservationCheckInOut().getTblRoom().setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                reservationRoomTypeDetail.getTblReservationCheckInOut().getTblRoom().setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                session.update(reservationRoomTypeDetail.getTblReservationCheckInOut().getTblRoom());
                //data reservation room type detail child detail
                for (TblReservationRoomTypeDetailChildDetail reservationChildInfo : reservationChildInfos) {
                    reservationChildInfo.setCreateDate(Timestamp.valueOf(LocalDateTime.now()));
                    reservationChildInfo.setTblEmployeeByCreateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                    reservationChildInfo.setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                    reservationChildInfo.setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                    reservationChildInfo.setRefRecordStatus(session.find(RefRecordStatus.class, 1));
                    session.saveOrUpdate(reservationChildInfo);
                }
                //data reservation bill (deposit)
                TblReservationBill dataReservationBill = getReservationBillByIDReservationAndIDReservationBillType(reservation.getIdreservation(),
                        2); //Deposit = '2'
                if (dataReservationBill == null) {  //create and save reservation bill
                    dataReservationBill = new TblReservationBill();
                    dataReservationBill.setCodeBill(ClassCoder.getCode("Reservation Bill", session));
                    dataReservationBill.setTblReservation(reservation);
                    dataReservationBill.setServiceChargePercentage(new BigDecimal("0"));
                    dataReservationBill.setTaxPercentage(new BigDecimal("0"));
                    dataReservationBill.setRefReservationBillType(session.find(RefReservationBillType.class, 2));
                    dataReservationBill.setCreateDate(Timestamp.valueOf(LocalDateTime.now()));
                    dataReservationBill.setTblEmployeeByCreateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                    dataReservationBill.setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                    dataReservationBill.setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                    dataReservationBill.setRefRecordStatus(session.find(RefRecordStatus.class, 1));
                    session.saveOrUpdate(dataReservationBill);
                }
                //data reservation payment (open-deposit)
                TblReservationPayment dataReservationPayment = new TblReservationPayment();
                dataReservationPayment.setCodePayment(ClassCoder.getCode("Reservation Payment", session));
                dataReservationPayment.setTblReservationBill(dataReservationBill);
                dataReservationPayment.setPaymentDate(Timestamp.valueOf(LocalDateTime.now()));
                dataReservationPayment.setUnitNominal((new BigDecimal(reservationRoomTypeDetail.getTblReservationCheckInOut().getCardUsed()))
                        .multiply(reservationRoomTypeDetail.getTblReservationCheckInOut().getBrokenCardCharge()));
                dataReservationPayment.setRoundingValue(new BigDecimal("0"));
                dataReservationPayment.setTblEmployeeByIdcashier(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                dataReservationPayment.setRefFinanceTransactionPaymentType(session.find(RefFinanceTransactionPaymentType.class, 13));
                dataReservationPayment.setRefReservationBillType(session.find(RefReservationBillType.class, 2));
                dataReservationPayment.setSettleDate(Timestamp.valueOf(LocalDateTime.now()));
                dataReservationPayment.setCreateDate(Timestamp.valueOf(LocalDateTime.now()));
                dataReservationPayment.setTblEmployeeByCreateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                dataReservationPayment.setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                dataReservationPayment.setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                dataReservationPayment.setRefRecordStatus(session.find(RefRecordStatus.class, 1));
                session.saveOrUpdate(dataReservationPayment);
                //data log
                dataLog += "Deposit Kartu : " + ClassFormatter.currencyFormat.cFormat(dataReservationPayment.getUnitNominal())
                        + " (" + reservationRoomTypeDetail.getTblReservationCheckInOut().getCardUsed()
                        + " kartu @" + ClassFormatter.currencyFormat.cFormat(reservationRoomTypeDetail.getTblReservationCheckInOut().getBrokenCardCharge()) + ")\n";
                dataLog += "-----------------------------------------------------------------------\n";
                //data reservation payment with deposit
                TblReservationPaymentWithDeposit dataReservationPaymentWithDeposit = new TblReservationPaymentWithDeposit();
                dataReservationPaymentWithDeposit.setTblReservationPayment(dataReservationPayment);
                dataReservationPaymentWithDeposit.setTblReservationCheckInOut(reservationRoomTypeDetail.getTblReservationCheckInOut());
                dataReservationPaymentWithDeposit.setCreateDate(Timestamp.valueOf(LocalDateTime.now()));
                dataReservationPaymentWithDeposit.setTblEmployeeByCreateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                dataReservationPaymentWithDeposit.setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                dataReservationPaymentWithDeposit.setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                dataReservationPaymentWithDeposit.setRefRecordStatus(session.find(RefRecordStatus.class, 1));
                session.saveOrUpdate(dataReservationPaymentWithDeposit);
                //@@@%%%
                //data deposit balance (update:+)
                TblCompanyBalance dataBalance = session.find(TblCompanyBalance.class, (long) 4);    //Deposit = '4'
                dataBalance.setBalanceNominal(dataBalance.getBalanceNominal().add(dataReservationPayment.getUnitNominal()));
                dataBalance.setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                dataBalance.setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                session.update(dataBalance);
                //data company balance (cash-flow log)
                LogCompanyBalanceCashFlow logCompanyBalanceCashFlow = new LogCompanyBalanceCashFlow();
                logCompanyBalanceCashFlow.setTblCompanyBalanceByIdsenderCompanyBalance(null);
                logCompanyBalanceCashFlow.setTblBankAccountByIdsenderCbbankAccount(null);
                logCompanyBalanceCashFlow.setTblCompanyBalanceByIdreceiverCompanyBalance(dataBalance);
                logCompanyBalanceCashFlow.setTblBankAccountByIdreceiverCbbankAccount(null);
                logCompanyBalanceCashFlow.setTransferNominal(dataReservationPayment.getUnitNominal());
                logCompanyBalanceCashFlow.setHistoryDate(Timestamp.valueOf(LocalDateTime.now()));
                logCompanyBalanceCashFlow.setTblEmployee(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                logCompanyBalanceCashFlow.setHistoryStystemNote("Deposit kartu kamar oleh " + reservation.getTblCustomer().getTblPeople().getFullName() + ", \nsebesar : "
                        + ClassFormatter.currencyFormat.format(dataReservationPayment.getUnitNominal()) + " ("
                        + dataReservationPayment.getRefFinanceTransactionPaymentType().getTypeName() + ")");
                session.saveOrUpdate(logCompanyBalanceCashFlow);
                //data log
                LogSystem logSystem = new LogSystem();
                logSystem.setLogDateTime(Timestamp.valueOf(LocalDateTime.now()));
                logSystem.setTblSystemUser(session.find(TblSystemUser.class, ClassSession.currentUser.getIduser()));
                logSystem.setTblEmployee(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                logSystem.setLogHeader(headerLog);
                logSystem.setLogDetail(dataLog);
                logSystem.setHostName(ClassComp.getHostName());
                session.saveOrUpdate(logSystem);
                //open close company balance - detail (cashier)
                TblOpenCloseCashierBalance dataOpenCloseCashierBalance = getDataOpenCloseCashierBalanceIsActive();
                if (dataOpenCloseCashierBalance != null) {
                    TblOpenCloseCashierBalanceDetail dataOpenCloseCashierBalanceDetail = new TblOpenCloseCashierBalanceDetail();
                    dataOpenCloseCashierBalanceDetail.setTblOpenCloseCashierBalance(dataOpenCloseCashierBalance);
                    dataOpenCloseCashierBalanceDetail.setTblReservationPayment(dataReservationPayment);
                    dataOpenCloseCashierBalanceDetail.setDetailData(getDetailData(dataReservationPayment));
                    dataOpenCloseCashierBalanceDetail.setCreateDate(Timestamp.valueOf(LocalDateTime.now()));
                    dataOpenCloseCashierBalanceDetail.setTblEmployeeByCreateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                    dataOpenCloseCashierBalanceDetail.setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                    dataOpenCloseCashierBalanceDetail.setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                    dataOpenCloseCashierBalanceDetail.setRefRecordStatus(session.find(RefRecordStatus.class, 1));
                    session.saveOrUpdate(dataOpenCloseCashierBalanceDetail);
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

    private TblReservationBill getReservationBillByIDReservationAndIDReservationBillType(long idReservation,
            int idReservationBillType) {
        List<TblReservationBill> list = session.getNamedQuery("findAllTblReservationBillByIDReservationAndIDReservationBillType")
                .setParameter("idReservation", idReservation)
                .setParameter("idType", idReservationBillType)
                .list();
        return list.isEmpty() ? null : list.get(0);
    }

    @Override
    public boolean updateDataReservationRoomTypeDetailCheckOut(
            TblReservation reservation,
            TblReservationRoomTypeDetail reservationRoomTypeDetail,
            TblReservationBill reservationBill,
            BigDecimal reservationBillNominal) {
        errMsg = "";
        if (ClassSession.checkUserSession()) {  //check user current session
            try {
                session = HibernateUtil.getSessionFactory().getCurrentSession();
                session.beginTransaction();
                //data log
                String headerLog = "BUAT DATA CHECKOUT (RESERVASI)";
                String dataLog = "";
                //data reservation
                reservation.setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                reservation.setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                session.update(reservation);
                //data log
                dataLog += "No. Reservasi : " + reservation.getCodeReservation() + "\n";
                dataLog += "Tgl. Reservasi : " + ClassFormatter.dateFormate.format(reservation.getReservationDate()) + "\n";
                dataLog += "Nama Customer : " + reservation.getTblCustomer().getTblPeople().getFullName() + "\n";
                dataLog += "Travel Agent : " + (reservation.getTblPartner() != null ? reservation.getTblPartner().getPartnerName() : "-") + "\n";
                dataLog += "-----------------------------------------------------------------------\n";
                //data reservation checkinout(out)
                reservationRoomTypeDetail.getTblReservationCheckInOut().setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                reservationRoomTypeDetail.getTblReservationCheckInOut().setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                session.update(reservationRoomTypeDetail.getTblReservationCheckInOut());
                //data reservation room type detail
                reservationRoomTypeDetail.setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                reservationRoomTypeDetail.setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                session.update(reservationRoomTypeDetail);
                //data log
                dataLog += "Reservasi Kamar : \n";
                dataLog += "- " + reservationRoomTypeDetail.getCodeDetail() + " - " + reservationRoomTypeDetail.getCheckInDateTime() + " s/d " + reservationRoomTypeDetail.getCheckOutDateTime() + " \t "
                        + (getDataRoom(reservationRoomTypeDetail)) + " \n";
                dataLog += "-----------------------------------------------------------------------\n";
                //data room
                reservationRoomTypeDetail.getTblReservationCheckInOut().getTblRoom().setRefRoomStatus(session.get(RefRoomStatus.class, 4)); //Vacant Dirty = '4'
                //data room status change history
                TblRoomStatusChangeHistory dataRoomStatusChangeHistory = new TblRoomStatusChangeHistory();
                dataRoomStatusChangeHistory.setTblRoom(reservationRoomTypeDetail.getTblReservationCheckInOut().getTblRoom());
                dataRoomStatusChangeHistory.setTblEmployeeByIdemployee(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                dataRoomStatusChangeHistory.setTblRoomStatusDetail(null);
                dataRoomStatusChangeHistory.setChangeRoomStatusDate(Timestamp.valueOf(LocalDateTime.now()));
                dataRoomStatusChangeHistory.setRefRoomStatusByRoomStatusOld(getOldRoomStatus(reservationRoomTypeDetail.getTblReservationCheckInOut().getTblRoom().getIdroom()));
                dataRoomStatusChangeHistory.setRefRoomStatusByRoomStatusNew(reservationRoomTypeDetail.getTblReservationCheckInOut().getTblRoom().getRefRoomStatus());
                dataRoomStatusChangeHistory.setCreateDate(Timestamp.valueOf(LocalDateTime.now()));
                dataRoomStatusChangeHistory.setTblEmployeeByCreateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                dataRoomStatusChangeHistory.setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                dataRoomStatusChangeHistory.setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                dataRoomStatusChangeHistory.setRefRecordStatus(session.find(RefRecordStatus.class, 1));
                session.saveOrUpdate(dataRoomStatusChangeHistory);
                //data room
                reservationRoomTypeDetail.getTblReservationCheckInOut().getTblRoom().setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                reservationRoomTypeDetail.getTblReservationCheckInOut().getTblRoom().setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                session.update(reservationRoomTypeDetail.getTblReservationCheckInOut().getTblRoom());
                //data addtioanal item - reserved (release : 'done')
                List<TblReservationAdditionalItemReserved> dataReservationAdditionalItemReserveds = getAllReservationAdditionalItemReservedByIDReservationRoomTypeDetail(reservationRoomTypeDetail.getIddetail());
                for (TblReservationAdditionalItemReserved dataReservationAdditionalItemReserved : dataReservationAdditionalItemReserveds) {
                    dataReservationAdditionalItemReserved.setRefReservationAdditionalItemReservedStatus(session.find(RefReservationAdditionalItemReservedStatus.class, 2)); //Done = '2'
                    dataReservationAdditionalItemReserved.setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                    dataReservationAdditionalItemReserved.setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                    session.update(dataReservationAdditionalItemReserved);
                }
                //data reservation bill (deposit)
                TblReservationBill dataReservationBill = getReservationBillByIDReservationAndIDReservationBillType(reservation.getIdreservation(),
                        2); //Deposit = '2'
                if (dataReservationBill == null) {  //create and save reservation bill
                    dataReservationBill = new TblReservationBill();
                    dataReservationBill.setCodeBill(ClassCoder.getCode("Reservation Bill", session));
                    dataReservationBill.setTblReservation(reservation);
                    dataReservationBill.setServiceChargePercentage(new BigDecimal("0"));
                    dataReservationBill.setTaxPercentage(new BigDecimal("0"));
                    dataReservationBill.setRefReservationBillType(session.find(RefReservationBillType.class, 2));
                    dataReservationBill.setCreateDate(Timestamp.valueOf(LocalDateTime.now()));
                    dataReservationBill.setTblEmployeeByCreateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                    dataReservationBill.setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                    dataReservationBill.setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                    dataReservationBill.setRefRecordStatus(session.find(RefRecordStatus.class, 1));
                    session.saveOrUpdate(dataReservationBill);
                }
                //data reservation payment (close-deposit)
                TblReservationPayment dataReservationPayment = new TblReservationPayment();
                dataReservationPayment.setCodePayment(ClassCoder.getCode("Reservation Payment", session));
                dataReservationPayment.setTblReservationBill(dataReservationBill);
                dataReservationPayment.setPaymentDate(Timestamp.valueOf(LocalDateTime.now()));
//            dataReservationPayment.setUnitNominal((reservationRoomTypeDetail.getTblReservationCheckInOut().getCardUsed()
//                    - reservationRoomTypeDetail.getTblReservationCheckInOut().getCardMissed())
//                    * reservationRoomTypeDetail.getTblReservationCheckInOut().getBrokenCardCharge());
                dataReservationPayment.setUnitNominal((new BigDecimal(reservationRoomTypeDetail.getTblReservationCheckInOut().getCardUsed()))
                        .multiply(reservationRoomTypeDetail.getTblReservationCheckInOut().getBrokenCardCharge()));
                dataReservationPayment.setRoundingValue(new BigDecimal("0"));
                dataReservationPayment.setTblEmployeeByIdcashier(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                dataReservationPayment.setRefFinanceTransactionPaymentType(session.find(RefFinanceTransactionPaymentType.class, 14));
                dataReservationPayment.setRefReservationBillType(session.find(RefReservationBillType.class, 2));    //deposit = '2'
                dataReservationPayment.setSettleDate(Timestamp.valueOf(LocalDateTime.now()));
                dataReservationPayment.setCreateDate(Timestamp.valueOf(LocalDateTime.now()));
                dataReservationPayment.setTblEmployeeByCreateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                dataReservationPayment.setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                dataReservationPayment.setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                dataReservationPayment.setRefRecordStatus(session.find(RefRecordStatus.class, 1));
                session.saveOrUpdate(dataReservationPayment);
                //data log
                dataLog += "Pengembalian Deposit Kartu : " + ClassFormatter.currencyFormat.cFormat(dataReservationPayment.getUnitNominal())
                        + " (" + reservationRoomTypeDetail.getTblReservationCheckInOut().getCardUsed()
                        + " kartu @" + ClassFormatter.currencyFormat.cFormat(reservationRoomTypeDetail.getTblReservationCheckInOut().getBrokenCardCharge()) + ")\n";
                dataLog += "-----------------------------------------------------------------------\n";
                //data reservation payment with deposit
                TblReservationPaymentWithDeposit dataReservationPaymentWithDeposit = new TblReservationPaymentWithDeposit();
                dataReservationPaymentWithDeposit.setTblReservationPayment(dataReservationPayment);
                dataReservationPaymentWithDeposit.setTblReservationCheckInOut(reservationRoomTypeDetail.getTblReservationCheckInOut());
                dataReservationPaymentWithDeposit.setCreateDate(Timestamp.valueOf(LocalDateTime.now()));
                dataReservationPaymentWithDeposit.setTblEmployeeByCreateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                dataReservationPaymentWithDeposit.setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                dataReservationPaymentWithDeposit.setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                dataReservationPaymentWithDeposit.setRefRecordStatus(session.find(RefRecordStatus.class, 1));
                session.saveOrUpdate(dataReservationPaymentWithDeposit);
                //@@@%%%
                //data deposit balance (update:-)
                TblCompanyBalance dataDepositBalance = session.find(TblCompanyBalance.class, (long) 4); //Kas Deposit = '4'
                dataDepositBalance.setBalanceNominal(dataDepositBalance.getBalanceNominal().subtract(dataReservationPayment.getUnitNominal()));
                dataDepositBalance.setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                dataDepositBalance.setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                session.update(dataDepositBalance);
                //data company balance (cash-flow log)
                LogCompanyBalanceCashFlow logCompanyBalanceCashFlow = new LogCompanyBalanceCashFlow();
                logCompanyBalanceCashFlow.setTblCompanyBalanceByIdsenderCompanyBalance(dataDepositBalance);
                logCompanyBalanceCashFlow.setTblBankAccountByIdsenderCbbankAccount(null);
                logCompanyBalanceCashFlow.setTblCompanyBalanceByIdreceiverCompanyBalance(null);
                logCompanyBalanceCashFlow.setTblBankAccountByIdreceiverCbbankAccount(null);
                logCompanyBalanceCashFlow.setTransferNominal(dataReservationPayment.getUnitNominal());
                logCompanyBalanceCashFlow.setHistoryDate(Timestamp.valueOf(LocalDateTime.now()));
                logCompanyBalanceCashFlow.setTblEmployee(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                logCompanyBalanceCashFlow.setHistoryStystemNote("Pengembalian deposit kartu kamar kepada " + reservation.getTblCustomer().getTblPeople().getFullName() + ", \nsebesar : "
                        + ClassFormatter.currencyFormat.format(dataReservationPayment.getUnitNominal()) + " ("
                        + dataReservationPayment.getRefFinanceTransactionPaymentType().getTypeName() + ")");
                session.saveOrUpdate(logCompanyBalanceCashFlow);
                //data company balance (cashier)
                TblCompanyBalance dataCashierBalance = session.find(TblCompanyBalance.class, (long) 3); //Kas Kasir = '3'
                //open close company balance
                TblOpenCloseCashierBalance dataOpenCloseCashierBalance = getDataOpenCloseCashierBalanceIsActive();
                //open close company balance - detail (cashier)
                if (dataOpenCloseCashierBalance != null) {
                    TblOpenCloseCashierBalanceDetail dataOpenCloseCashierBalanceDetail = new TblOpenCloseCashierBalanceDetail();
                    dataOpenCloseCashierBalanceDetail.setTblOpenCloseCashierBalance(dataOpenCloseCashierBalance);
                    dataOpenCloseCashierBalanceDetail.setTblReservationPayment(dataReservationPayment);
                    dataOpenCloseCashierBalanceDetail.setDetailData(getDetailData(dataReservationPayment));
                    dataOpenCloseCashierBalanceDetail.setCreateDate(Timestamp.valueOf(LocalDateTime.now()));
                    dataOpenCloseCashierBalanceDetail.setTblEmployeeByCreateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                    dataOpenCloseCashierBalanceDetail.setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                    dataOpenCloseCashierBalanceDetail.setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                    dataOpenCloseCashierBalanceDetail.setRefRecordStatus(session.find(RefRecordStatus.class, 1));
                    session.saveOrUpdate(dataOpenCloseCashierBalanceDetail);
                }
                //if missed card number > 0
                if (reservationRoomTypeDetail.getTblReservationCheckInOut().getCardMissed() > 0) {
                    //data reservation payment (cash)
                    TblReservationPayment dataReservationPaymentForMissedCard = new TblReservationPayment();
                    dataReservationPaymentForMissedCard.setCodePayment(ClassCoder.getCode("Reservation Payment", session));
                    dataReservationPaymentForMissedCard.setPaymentDate(Timestamp.valueOf(LocalDateTime.now()));
                    dataReservationPaymentForMissedCard.setUnitNominal((new BigDecimal(reservationRoomTypeDetail.getTblReservationCheckInOut().getCardMissed()))
                            .multiply(reservationRoomTypeDetail.getTblReservationCheckInOut().getBrokenCardCharge()));
                    dataReservationPaymentForMissedCard.setRoundingValue(new BigDecimal("0"));
                    dataReservationPaymentForMissedCard.setTblEmployeeByIdcashier(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                    dataReservationPaymentForMissedCard.setRefFinanceTransactionPaymentType(session.find(RefFinanceTransactionPaymentType.class, 0));   //Tunai = '0'
                    dataReservationPaymentForMissedCard.setRefReservationBillType(session.find(RefReservationBillType.class, 2));   //deposit = '2'
                    dataReservationPaymentForMissedCard.setCreateDate(Timestamp.valueOf(LocalDateTime.now()));
                    dataReservationPaymentForMissedCard.setTblEmployeeByCreateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                    dataReservationPaymentForMissedCard.setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                    dataReservationPaymentForMissedCard.setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                    dataReservationPaymentForMissedCard.setRefRecordStatus(session.find(RefRecordStatus.class, 1));
                    session.saveOrUpdate(dataReservationPaymentForMissedCard);
                    //@@@%%%
                    //data cashier balance (update:+)
                    dataCashierBalance.setBalanceNominal(dataCashierBalance.getBalanceNominal().add(dataReservationPaymentForMissedCard.getUnitNominal()));
                    dataCashierBalance.setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                    dataCashierBalance.setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                    session.update(dataCashierBalance);
                    //data item : guest card (perlengkapan - guest)
                    TblItem itemGuestCard;
                    SysDataHardCode sdhItemGuestCard = session.find(SysDataHardCode.class, (long) 1);   //IDGuestCard = '1'
                    if (sdhItemGuestCard == null
                            || sdhItemGuestCard.getDataHardCodeValue() == null) {
                        if (session.getTransaction().isActive()) {
                            session.getTransaction().rollback();
                        }
                        errMsg = "Data Kartu Kamar - Tidak Ditemukan..!";
                        return false;
                    } else {
                        itemGuestCard = session.find(TblItem.class, Long.valueOf(sdhItemGuestCard.getDataHardCodeValue()));
                        if (itemGuestCard == null) {
                            if (session.getTransaction().isActive()) {
                                session.getTransaction().rollback();
                            }
                            errMsg = "Data Kartu Kamar - Tidak Ditemukan..!";
                            return false;
                        }
                    }
                    //data location : front offce
                    TblLocation locationFO;
                    SysDataHardCode sdhLocationFrontOffice = session.find(SysDataHardCode.class, (long) 2);   //IDFrontOffice = '2'
                    if (sdhLocationFrontOffice == null
                            || sdhLocationFrontOffice.getDataHardCodeValue() == null) {
                        if (session.getTransaction().isActive()) {
                            session.getTransaction().rollback();
                        }
                        errMsg = "Data Lokasi Front Office - Tidak Ditemukan..!";
                        return false;
                    } else {
                        locationFO = session.find(TblLocation.class, Long.valueOf(sdhLocationFrontOffice.getDataHardCodeValue()));
                        if (locationFO == null) {
                            if (session.getTransaction().isActive()) {
                                session.getTransaction().rollback();
                            }
                            errMsg = "Data Lokasi Front Office - Tidak Ditemukan..!";
                            return false;
                        }
                    }
                    //data item-location : guest card - front office
                    TblItemLocation itemLocation = getItemLocationByIDLocationAndIDItem(locationFO.getIdlocation(), itemGuestCard.getIditem());
                    if (itemLocation == null
                            || reservationRoomTypeDetail.getTblReservationCheckInOut().getCardMissed() > itemLocation.getItemQuantity().intValue()) {
                        if (session.getTransaction().isActive()) {
                            session.getTransaction().rollback();
                        }
                        errMsg = "Stok Kartu Kamar - Tidak Cukup..!";
                        return false;
                    }
                    TblLocationOfBin bin = getBin();
                    if (bin != null) {
                        if (itemLocation.getItemQuantity()
                                .compareTo(new BigDecimal("0")) == 1) {
                            //data item mutation history
                            TblItemMutationHistory dataMutation = new TblItemMutationHistory();
                            dataMutation.setItemQuantity(new BigDecimal(String.valueOf(reservationRoomTypeDetail.getTblReservationCheckInOut().getCardMissed())));
                            dataMutation.setTblItem(itemLocation.getTblItem());
                            dataMutation.setTblLocationByIdlocationOfSource(itemLocation.getTblLocation());
                            dataMutation.setTblLocationByIdlocationOfDestination(bin.getTblLocation());
                            dataMutation.setMutationDate(Timestamp.valueOf(LocalDateTime.now()));
//                            dataMutation.setTblEmployeeByIdemployee(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                            dataMutation.setRefItemMutationType(session.find(RefItemMutationType.class, 2));
                            dataMutation.setRefItemObsoleteBy(session.find(RefItemObsoleteBy.class, 0));
//                            dataMutation.setTblEmployeeByIdemployee(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                            dataMutation.setCreateDate(Timestamp.valueOf(LocalDateTime.now()));
                            dataMutation.setTblEmployeeByCreateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                            dataMutation.setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                            dataMutation.setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                            dataMutation.setRefRecordStatus(session.find(RefRecordStatus.class, 1));
                            session.saveOrUpdate(dataMutation);
                            //data item location (source)
                            itemLocation.setItemQuantity(itemLocation.getItemQuantity().subtract(dataMutation.getItemQuantity()));
                            itemLocation.setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                            itemLocation.setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                            session.update(itemLocation);
                            //data item location (destination) : bin
                            TblItemLocation destinationItemLocation = getItemLocationByIDLocationAndIDItem(dataMutation.getTblLocationByIdlocationOfDestination().getIdlocation(),
                                    dataMutation.getTblItem().getIditem());
                            if (destinationItemLocation == null) {
                                destinationItemLocation = new TblItemLocation();
                                destinationItemLocation.setTblItem(itemLocation.getTblItem());
                                destinationItemLocation.setTblLocation(getBin().getTblLocation());
                                destinationItemLocation.setItemQuantity(new BigDecimal("0"));
                                destinationItemLocation.setCreateDate(Timestamp.valueOf(LocalDateTime.now()));
                                destinationItemLocation.setTblEmployeeByCreateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                                destinationItemLocation.setRefRecordStatus(session.find(RefRecordStatus.class, 1));
                            }
                            destinationItemLocation.setItemQuantity(destinationItemLocation.getItemQuantity().add(dataMutation.getItemQuantity()));
                            destinationItemLocation.setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                            destinationItemLocation.setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                            session.saveOrUpdate(destinationItemLocation);
                        }
                        session.evict(bin);
                    } else {
                        if (session.getTransaction().isActive()) {
                            session.getTransaction().rollback();
                        }
                        errMsg = "Data Bin - Tidak Ditemukan..!";
                        return false;
                    }
                    //data log
                    dataLog += "Denda Kartu Hilang/Rusak : " + ClassFormatter.currencyFormat.cFormat(dataReservationPaymentForMissedCard.getUnitNominal())
                            + " (" + reservationRoomTypeDetail.getTblReservationCheckInOut().getCardMissed()
                            + " kartu @" + ClassFormatter.currencyFormat.cFormat(reservationRoomTypeDetail.getTblReservationCheckInOut().getBrokenCardCharge()) + ")\n";
                    dataLog += "-----------------------------------------------------------------------\n";
                    dataLog += "Kurangi Stok Kartu : " + reservationRoomTypeDetail.getTblReservationCheckInOut().getCardMissed() + "(jumlah kartu) \n"
                            + "- Data Kartu : " + itemGuestCard.getCodeItem() + " - " + itemGuestCard.getItemName() + " \n"
                            + "- Data Lokasi Kartu : " + getDataLocationOfWarehouseName(locationFO) + " \n";
                    dataLog += "-----------------------------------------------------------------------\n";
                    //open close company balance - detail (cashier)
                    if (dataOpenCloseCashierBalance != null) {
                        TblOpenCloseCashierBalanceDetail dataOpenCloseCashierBalanceDetailMissedCard = new TblOpenCloseCashierBalanceDetail();
                        dataOpenCloseCashierBalanceDetailMissedCard.setTblOpenCloseCashierBalance(dataOpenCloseCashierBalance);
                        dataOpenCloseCashierBalanceDetailMissedCard.setTblReservationPayment(dataReservationPaymentForMissedCard);
                        dataOpenCloseCashierBalanceDetailMissedCard.setDetailData(getDetailData(dataReservationPaymentForMissedCard));
                        dataOpenCloseCashierBalanceDetailMissedCard.setCreateDate(Timestamp.valueOf(LocalDateTime.now()));
                        dataOpenCloseCashierBalanceDetailMissedCard.setTblEmployeeByCreateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                        dataOpenCloseCashierBalanceDetailMissedCard.setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                        dataOpenCloseCashierBalanceDetailMissedCard.setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                        dataOpenCloseCashierBalanceDetailMissedCard.setRefRecordStatus(session.find(RefRecordStatus.class, 1));
                        session.saveOrUpdate(dataOpenCloseCashierBalanceDetailMissedCard);
                    }
                }
                //data log
                LogSystem logSystem = new LogSystem();
                logSystem.setLogDateTime(Timestamp.valueOf(LocalDateTime.now()));
                logSystem.setTblSystemUser(session.find(TblSystemUser.class, ClassSession.currentUser.getIduser()));
                logSystem.setTblEmployee(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                logSystem.setLogHeader(headerLog);
                logSystem.setLogDetail(dataLog);
                logSystem.setHostName(ClassComp.getHostName());
                session.saveOrUpdate(logSystem);
                //123456
                if (isAllCheckOut(reservation)
                        && (reservationBillNominal
                        .compareTo(new BigDecimal("0")) < 1)) {
                    //data transaction return (auto, -balance cashier balance)
                    if (reservationBillNominal
                            .compareTo(new BigDecimal("0")) == -1) {
                        //data reservation payment (return payment)
                        TblReservationPayment dataReservationReturnPayment = new TblReservationPayment();
                        dataReservationReturnPayment.setCodePayment(ClassCoder.getCode("Reservation Payment", session));
                        dataReservationReturnPayment.setTblReservationBill(reservationBill);
                        dataReservationReturnPayment.setPaymentDate(Timestamp.valueOf(LocalDateTime.now()));
                        dataReservationReturnPayment.setUnitNominal(getNominalAfterRounding(reservationBillNominal.abs()));
                        dataReservationReturnPayment.setRoundingValue(getRoundingValue(reservationBillNominal.abs()));
                        dataReservationReturnPayment.setTblEmployeeByIdcashier(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                        dataReservationReturnPayment.setRefFinanceTransactionPaymentType(session.find(RefFinanceTransactionPaymentType.class, 16));     //Return = '16'
                        dataReservationReturnPayment.setRefReservationBillType(session.find(RefReservationBillType.class, 0));    //reservasi = '0'
                        dataReservationReturnPayment.setSettleDate(Timestamp.valueOf(LocalDateTime.now()));     //auto settle
                        dataReservationReturnPayment.setCreateDate(Timestamp.valueOf(LocalDateTime.now()));
                        dataReservationReturnPayment.setTblEmployeeByCreateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                        dataReservationReturnPayment.setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                        dataReservationReturnPayment.setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                        dataReservationReturnPayment.setRefRecordStatus(session.find(RefRecordStatus.class, 1));
                        session.saveOrUpdate(dataReservationReturnPayment);
                        //data log
                        dataLog += "Pengembalian Pembayaran : " + ClassFormatter.currencyFormat.cFormat(dataReservationReturnPayment.getUnitNominal()) + ")\n";
                        dataLog += "-----------------------------------------------------------------------\n";
                        //open close company balance - detail (cashier)
                        if (dataOpenCloseCashierBalance != null) {
                            TblOpenCloseCashierBalanceDetail dataOpenCloseCashierBalanceDetail = new TblOpenCloseCashierBalanceDetail();
                            dataOpenCloseCashierBalanceDetail.setTblOpenCloseCashierBalance(dataOpenCloseCashierBalance);
                            dataOpenCloseCashierBalanceDetail.setTblReservationPayment(dataReservationReturnPayment);
                            dataOpenCloseCashierBalanceDetail.setDetailData(getDetailData(dataReservationReturnPayment));
                            dataOpenCloseCashierBalanceDetail.setCreateDate(Timestamp.valueOf(LocalDateTime.now()));
                            dataOpenCloseCashierBalanceDetail.setTblEmployeeByCreateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                            dataOpenCloseCashierBalanceDetail.setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                            dataOpenCloseCashierBalanceDetail.setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                            dataOpenCloseCashierBalanceDetail.setRefRecordStatus(session.find(RefRecordStatus.class, 1));
                            session.saveOrUpdate(dataOpenCloseCashierBalanceDetail);
                        }
                        //data company balance (cashier-)
                        dataCashierBalance.setBalanceNominal(dataCashierBalance.getBalanceNominal().subtract(dataReservationReturnPayment.getUnitNominal()));
                        dataCashierBalance.setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                        dataCashierBalance.setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                        session.update(dataCashierBalance);
                        //data company balance (cash-flow log)
                        LogCompanyBalanceCashFlow returnLogCompanyBalanceCashFlow = new LogCompanyBalanceCashFlow();
                        returnLogCompanyBalanceCashFlow.setTblCompanyBalanceByIdsenderCompanyBalance(dataCashierBalance);
                        returnLogCompanyBalanceCashFlow.setTblBankAccountByIdsenderCbbankAccount(null);
                        returnLogCompanyBalanceCashFlow.setTblCompanyBalanceByIdreceiverCompanyBalance(null);
                        returnLogCompanyBalanceCashFlow.setTblBankAccountByIdreceiverCbbankAccount(null);
                        returnLogCompanyBalanceCashFlow.setTransferNominal(dataReservationReturnPayment.getUnitNominal());
                        returnLogCompanyBalanceCashFlow.setHistoryDate(Timestamp.valueOf(LocalDateTime.now()));
                        returnLogCompanyBalanceCashFlow.setTblEmployee(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                        returnLogCompanyBalanceCashFlow.setHistoryStystemNote("Pengembalian pembayaran kepada " + reservation.getTblCustomer().getTblPeople().getFullName() + ", \nsebesar : "
                                + ClassFormatter.currencyFormat.format(dataReservationReturnPayment.getUnitNominal()) + " ("
                                + dataReservationReturnPayment.getRefFinanceTransactionPaymentType().getTypeName() + ")");
                        session.saveOrUpdate(returnLogCompanyBalanceCashFlow);
                    }
                    //data reservation (update:checkout)
                    reservation.setDepartureTime(Timestamp.valueOf(LocalDateTime.now()));
                    reservation.setRefReservationStatus(session.find(RefReservationStatus.class, 5));   //Checked Out = '5'
                    reservation.setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                    reservation.setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                    session.update(reservation);
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
    public boolean updateDataReservationRoomTypeDetailMultiCheckOut(
            TblReservation reservation,
            List<TblReservationRoomTypeDetail> reservationRoomTypeDetails,
            TblReservationBill reservationBill,
            BigDecimal reservationBillNominal) {
        errMsg = "";
        if (ClassSession.checkUserSession()) {  //check user current session
            try {
                session = HibernateUtil.getSessionFactory().getCurrentSession();
                session.beginTransaction();
                //data log
                String headerLog = "BUAT DATA CHECKOUT(S) (RESERVASI)";
                String dataLog = "";
                //data reservation
                reservation.setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                reservation.setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                session.update(reservation);
                //data log
                dataLog += "No. Reservasi : " + reservation.getCodeReservation() + "\n";
                dataLog += "Tgl. Reservasi : " + ClassFormatter.dateFormate.format(reservation.getReservationDate()) + "\n";
                dataLog += "Nama Customer : " + reservation.getTblCustomer().getTblPeople().getFullName() + "\n";
                dataLog += "Travel Agent : " + (reservation.getTblPartner() != null ? reservation.getTblPartner().getPartnerName() : "-") + "\n";
                dataLog += "-----------------------------------------------------------------------\n";
                //data company balance (cashier)
                TblCompanyBalance dataCashierBalance = session.find(TblCompanyBalance.class, (long) 3); //Kas Kasir = '3'
                //open close company balance (cashier)
                TblOpenCloseCashierBalance dataOpenCloseCashierBalance = getDataOpenCloseCashierBalanceIsActive();
                //data reservation room type details
                BigDecimal totalDeposit = new BigDecimal("0");
                int totalMissedCard = 0;
                BigDecimal totalBrokenCharge = new BigDecimal("0");
                for (TblReservationRoomTypeDetail reservationRoomTypeDetail : reservationRoomTypeDetails) {
                    //data reservation checkinout(out)
                    reservationRoomTypeDetail.getTblReservationCheckInOut().setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                    reservationRoomTypeDetail.getTblReservationCheckInOut().setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                    session.update(reservationRoomTypeDetail.getTblReservationCheckInOut());
                    //data reservation room type detail
                    reservationRoomTypeDetail.setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                    reservationRoomTypeDetail.setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                    session.update(reservationRoomTypeDetail);
                    //data log
                    dataLog += "Reservasi Kamar : \n";
                    dataLog += "- " + reservationRoomTypeDetail.getCodeDetail() + " - " + reservationRoomTypeDetail.getCheckInDateTime() + " s/d " + reservationRoomTypeDetail.getCheckOutDateTime() + " \t "
                            + (getDataRoom(reservationRoomTypeDetail)) + " \n";
                    //data room
                    reservationRoomTypeDetail.getTblReservationCheckInOut().getTblRoom().setRefRoomStatus(session.get(RefRoomStatus.class, 4)); //Vacant Dirty = '4'
                    //data room status change history
                    TblRoomStatusChangeHistory dataRoomStatusChangeHistory = new TblRoomStatusChangeHistory();
                    dataRoomStatusChangeHistory.setTblRoom(reservationRoomTypeDetail.getTblReservationCheckInOut().getTblRoom());
                    dataRoomStatusChangeHistory.setTblEmployeeByIdemployee(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                    dataRoomStatusChangeHistory.setTblRoomStatusDetail(null);
                    dataRoomStatusChangeHistory.setChangeRoomStatusDate(Timestamp.valueOf(LocalDateTime.now()));
                    dataRoomStatusChangeHistory.setRefRoomStatusByRoomStatusOld(getOldRoomStatus(reservationRoomTypeDetail.getTblReservationCheckInOut().getTblRoom().getIdroom()));
                    dataRoomStatusChangeHistory.setRefRoomStatusByRoomStatusNew(reservationRoomTypeDetail.getTblReservationCheckInOut().getTblRoom().getRefRoomStatus());
                    dataRoomStatusChangeHistory.setCreateDate(Timestamp.valueOf(LocalDateTime.now()));
                    dataRoomStatusChangeHistory.setTblEmployeeByCreateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                    dataRoomStatusChangeHistory.setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                    dataRoomStatusChangeHistory.setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                    dataRoomStatusChangeHistory.setRefRecordStatus(session.find(RefRecordStatus.class, 1));
                    session.saveOrUpdate(dataRoomStatusChangeHistory);
                    //data room
                    reservationRoomTypeDetail.getTblReservationCheckInOut().getTblRoom().setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                    reservationRoomTypeDetail.getTblReservationCheckInOut().getTblRoom().setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                    session.update(reservationRoomTypeDetail.getTblReservationCheckInOut().getTblRoom());
                    //data addtioanal item - reserved (release : 'done')
                    List<TblReservationAdditionalItemReserved> dataReservationAdditionalItemReserveds = getAllReservationAdditionalItemReservedByIDReservationRoomTypeDetail(reservationRoomTypeDetail.getIddetail());
                    for (TblReservationAdditionalItemReserved dataReservationAdditionalItemReserved : dataReservationAdditionalItemReserveds) {
                        dataReservationAdditionalItemReserved.setRefReservationAdditionalItemReservedStatus(session.find(RefReservationAdditionalItemReservedStatus.class, 2)); //Done = '2'
                        dataReservationAdditionalItemReserved.setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                        dataReservationAdditionalItemReserved.setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                        session.update(dataReservationAdditionalItemReserved);
                    }
                    //data reservation bill (deposit)
                    TblReservationBill dataReservationBill = getReservationBillByIDReservationAndIDReservationBillType(reservation.getIdreservation(),
                            2); //Deposit = '2'
                    if (dataReservationBill == null) {  //create and save reservation bill
                        dataReservationBill = new TblReservationBill();
                        dataReservationBill.setCodeBill(ClassCoder.getCode("Reservation Bill", session));
                        dataReservationBill.setTblReservation(reservation);
                        dataReservationBill.setServiceChargePercentage(new BigDecimal("0"));
                        dataReservationBill.setTaxPercentage(new BigDecimal("0"));
                        dataReservationBill.setRefReservationBillType(session.find(RefReservationBillType.class, 2));
                        dataReservationBill.setCreateDate(Timestamp.valueOf(LocalDateTime.now()));
                        dataReservationBill.setTblEmployeeByCreateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                        dataReservationBill.setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                        dataReservationBill.setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                        dataReservationBill.setRefRecordStatus(session.find(RefRecordStatus.class, 1));
                        session.saveOrUpdate(dataReservationBill);
                    }
                    //data reservation payment (close-deposit)
                    TblReservationPayment dataReservationPayment = new TblReservationPayment();
                    dataReservationPayment.setCodePayment(ClassCoder.getCode("Reservation Payment", session));
                    dataReservationPayment.setTblReservationBill(dataReservationBill);
                    dataReservationPayment.setPaymentDate(Timestamp.valueOf(LocalDateTime.now()));
                    dataReservationPayment.setUnitNominal((new BigDecimal(reservationRoomTypeDetail.getTblReservationCheckInOut().getCardUsed()))
                            .multiply(reservationRoomTypeDetail.getTblReservationCheckInOut().getBrokenCardCharge()));
                    dataReservationPayment.setRoundingValue(new BigDecimal("0"));
                    dataReservationPayment.setTblEmployeeByIdcashier(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                    dataReservationPayment.setRefFinanceTransactionPaymentType(session.find(RefFinanceTransactionPaymentType.class, 14));
                    dataReservationPayment.setRefReservationBillType(session.find(RefReservationBillType.class, 2));    //deposit = '2'
                    dataReservationPayment.setSettleDate(Timestamp.valueOf(LocalDateTime.now()));
                    dataReservationPayment.setCreateDate(Timestamp.valueOf(LocalDateTime.now()));
                    dataReservationPayment.setTblEmployeeByCreateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                    dataReservationPayment.setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                    dataReservationPayment.setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                    dataReservationPayment.setRefRecordStatus(session.find(RefRecordStatus.class, 1));
                    session.saveOrUpdate(dataReservationPayment);
                    //data log
                    dataLog += "Pengembalian Deposit Kartu : " + ClassFormatter.currencyFormat.cFormat(dataReservationPayment.getUnitNominal())
                            + " (" + reservationRoomTypeDetail.getTblReservationCheckInOut().getCardUsed()
                            + " kartu @" + ClassFormatter.currencyFormat.cFormat(reservationRoomTypeDetail.getTblReservationCheckInOut().getBrokenCardCharge()) + ")\n";
                    dataLog += "-----------------------------------------------------------------------\n";
                    //data reservation payment with deposit
                    TblReservationPaymentWithDeposit dataReservationPaymentWithDeposit = new TblReservationPaymentWithDeposit();
                    dataReservationPaymentWithDeposit.setTblReservationPayment(dataReservationPayment);
                    dataReservationPaymentWithDeposit.setTblReservationCheckInOut(reservationRoomTypeDetail.getTblReservationCheckInOut());
                    dataReservationPaymentWithDeposit.setCreateDate(Timestamp.valueOf(LocalDateTime.now()));
                    dataReservationPaymentWithDeposit.setTblEmployeeByCreateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                    dataReservationPaymentWithDeposit.setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                    dataReservationPaymentWithDeposit.setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                    dataReservationPaymentWithDeposit.setRefRecordStatus(session.find(RefRecordStatus.class, 1));
                    session.saveOrUpdate(dataReservationPaymentWithDeposit);
                    //open close company balance - detail (cashier)
                    if (dataOpenCloseCashierBalance != null) {
                        TblOpenCloseCashierBalanceDetail dataOpenCloseCashierBalanceDetail = new TblOpenCloseCashierBalanceDetail();
                        dataOpenCloseCashierBalanceDetail.setTblOpenCloseCashierBalance(dataOpenCloseCashierBalance);
                        dataOpenCloseCashierBalanceDetail.setTblReservationPayment(dataReservationPayment);
                        dataOpenCloseCashierBalanceDetail.setDetailData(getDetailData(dataReservationPayment));
                        dataOpenCloseCashierBalanceDetail.setCreateDate(Timestamp.valueOf(LocalDateTime.now()));
                        dataOpenCloseCashierBalanceDetail.setTblEmployeeByCreateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                        dataOpenCloseCashierBalanceDetail.setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                        dataOpenCloseCashierBalanceDetail.setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                        dataOpenCloseCashierBalanceDetail.setRefRecordStatus(session.find(RefRecordStatus.class, 1));
                        session.saveOrUpdate(dataOpenCloseCashierBalanceDetail);
                    }
                    //total
                    totalDeposit = totalDeposit.add((new BigDecimal(reservationRoomTypeDetail.getTblReservationCheckInOut().getCardUsed()))
                            .multiply(reservationRoomTypeDetail.getTblReservationCheckInOut().getBrokenCardCharge()));
                    totalMissedCard += reservationRoomTypeDetail.getTblReservationCheckInOut().getCardMissed();
                    totalBrokenCharge = totalBrokenCharge.add((new BigDecimal(reservationRoomTypeDetail.getTblReservationCheckInOut().getCardMissed()))
                            .multiply(reservationRoomTypeDetail.getTblReservationCheckInOut().getBrokenCardCharge()));
                }
                //@@@%%%
                //data deposit balance (update:-)
                TblCompanyBalance dataDepositBalance = session.find(TblCompanyBalance.class, (long) 4); //Kas Deposit = '4'
                dataDepositBalance.setBalanceNominal(dataDepositBalance.getBalanceNominal().subtract(totalDeposit));
                dataDepositBalance.setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                dataDepositBalance.setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                session.update(dataDepositBalance);
                //data company balance (cash-flow log)
                LogCompanyBalanceCashFlow logCompanyBalanceCashFlow = new LogCompanyBalanceCashFlow();
                logCompanyBalanceCashFlow.setTblCompanyBalanceByIdsenderCompanyBalance(dataDepositBalance);
                logCompanyBalanceCashFlow.setTblBankAccountByIdsenderCbbankAccount(null);
                logCompanyBalanceCashFlow.setTblCompanyBalanceByIdreceiverCompanyBalance(null);
                logCompanyBalanceCashFlow.setTblBankAccountByIdreceiverCbbankAccount(null);
                logCompanyBalanceCashFlow.setTransferNominal(totalDeposit);
                logCompanyBalanceCashFlow.setHistoryDate(Timestamp.valueOf(LocalDateTime.now()));
                logCompanyBalanceCashFlow.setTblEmployee(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                logCompanyBalanceCashFlow.setHistoryStystemNote("Pengembalian deposit kartu kamar kepada " + reservation.getTblCustomer().getTblPeople().getFullName() + ", \nsebesar : "
                        + ClassFormatter.currencyFormat.format(totalDeposit) + " ("
                        + session.find(RefReservationBillType.class, 2) + ")"); //deposit = '2'
                session.saveOrUpdate(logCompanyBalanceCashFlow);
                //if total missed card number > 0
                if (totalMissedCard > 0) {
                    //data reservation payment (cash)
                    TblReservationPayment dataReservationPaymentForMissedCard = new TblReservationPayment();
                    dataReservationPaymentForMissedCard.setCodePayment(ClassCoder.getCode("Reservation Payment", session));
                    dataReservationPaymentForMissedCard.setPaymentDate(Timestamp.valueOf(LocalDateTime.now()));
                    dataReservationPaymentForMissedCard.setUnitNominal(totalBrokenCharge);
                    dataReservationPaymentForMissedCard.setRoundingValue(new BigDecimal("0"));
                    dataReservationPaymentForMissedCard.setTblEmployeeByIdcashier(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                    dataReservationPaymentForMissedCard.setRefFinanceTransactionPaymentType(session.find(RefFinanceTransactionPaymentType.class, 0));   //Tunai = '0'
                    dataReservationPaymentForMissedCard.setRefReservationBillType(session.find(RefReservationBillType.class, 2));   //deposit = '2'
                    dataReservationPaymentForMissedCard.setCreateDate(Timestamp.valueOf(LocalDateTime.now()));
                    dataReservationPaymentForMissedCard.setTblEmployeeByCreateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                    dataReservationPaymentForMissedCard.setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                    dataReservationPaymentForMissedCard.setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                    dataReservationPaymentForMissedCard.setRefRecordStatus(session.find(RefRecordStatus.class, 1));
                    session.saveOrUpdate(dataReservationPaymentForMissedCard);
                    //@@@%%%
                    //data cashier balance (update:+)
                    dataCashierBalance.setBalanceNominal(dataCashierBalance.getBalanceNominal().add(dataReservationPaymentForMissedCard.getUnitNominal()));
                    dataCashierBalance.setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                    dataCashierBalance.setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                    session.update(dataCashierBalance);
                    //data item : guest card (perlengkapan - guest)
                    TblItem itemGuestCard;
                    SysDataHardCode sdhItemGuestCard = session.find(SysDataHardCode.class, (long) 1);   //IDGuestCard = '1'
                    if (sdhItemGuestCard == null
                            || sdhItemGuestCard.getDataHardCodeValue() == null) {
                        if (session.getTransaction().isActive()) {
                            session.getTransaction().rollback();
                        }
                        errMsg = "Data Kartu Kamar - Tidak Ditemukan..!";
                        return false;
                    } else {
                        itemGuestCard = session.find(TblItem.class, Long.valueOf(sdhItemGuestCard.getDataHardCodeValue()));
                        if (itemGuestCard == null) {
                            if (session.getTransaction().isActive()) {
                                session.getTransaction().rollback();
                            }
                            errMsg = "Data Kartu Kamar - Tidak Ditemukan..!";
                            return false;
                        }
                    }
                    //data location : front offce
                    TblLocation locationFO;
                    SysDataHardCode sdhLocationFrontOffice = session.find(SysDataHardCode.class, (long) 2);   //IDFrontOffice = '2'
                    if (sdhLocationFrontOffice == null
                            || sdhLocationFrontOffice.getDataHardCodeValue() == null) {
                        if (session.getTransaction().isActive()) {
                            session.getTransaction().rollback();
                        }
                        errMsg = "Data Lokasi Front Office - Tidak Ditemukan..!";
                        return false;
                    } else {
                        locationFO = session.find(TblLocation.class, Long.valueOf(sdhLocationFrontOffice.getDataHardCodeValue()));
                        if (locationFO == null) {
                            if (session.getTransaction().isActive()) {
                                session.getTransaction().rollback();
                            }
                            errMsg = "Data Lokasi Front Office - Tidak Ditemukan..!";
                            return false;
                        }
                    }
                    //data item-location : guest card - front office
                    TblItemLocation itemLocation = getItemLocationByIDLocationAndIDItem(locationFO.getIdlocation(), itemGuestCard.getIditem());
                    if (itemLocation == null
                            || totalMissedCard > itemLocation.getItemQuantity().intValue()) {
                        if (session.getTransaction().isActive()) {
                            session.getTransaction().rollback();
                        }
                        errMsg = "Stok Kartu Kamar - Tidak Cukup..!";
                        return false;
                    }
                    TblLocationOfBin bin = getBin();
                    if (bin != null) {
                        if (itemLocation.getItemQuantity()
                                .compareTo(new BigDecimal("0")) == 1) {
                            //data item mutation history
                            TblItemMutationHistory dataMutation = new TblItemMutationHistory();
                            dataMutation.setItemQuantity(new BigDecimal(String.valueOf(totalBrokenCharge)));
                            dataMutation.setTblItem(itemLocation.getTblItem());
                            dataMutation.setTblLocationByIdlocationOfSource(itemLocation.getTblLocation());
                            dataMutation.setTblLocationByIdlocationOfDestination(bin.getTblLocation());
                            dataMutation.setMutationDate(Timestamp.valueOf(LocalDateTime.now()));
//                            dataMutation.setTblEmployeeByIdemployee(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                            dataMutation.setRefItemMutationType(session.find(RefItemMutationType.class, 2));
                            dataMutation.setRefItemObsoleteBy(session.find(RefItemObsoleteBy.class, 0));
//                            dataMutation.setTblEmployeeByIdemployee(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                            dataMutation.setCreateDate(Timestamp.valueOf(LocalDateTime.now()));
                            dataMutation.setTblEmployeeByCreateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                            dataMutation.setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                            dataMutation.setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                            dataMutation.setRefRecordStatus(session.find(RefRecordStatus.class, 1));
                            session.saveOrUpdate(dataMutation);
                            //data item location (source)
                            itemLocation.setItemQuantity(itemLocation.getItemQuantity().subtract(dataMutation.getItemQuantity()));
                            itemLocation.setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                            itemLocation.setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                            session.update(itemLocation);
                            //data item location (destination) : bin
                            TblItemLocation destinationItemLocation = getItemLocationByIDLocationAndIDItem(dataMutation.getTblLocationByIdlocationOfDestination().getIdlocation(),
                                    dataMutation.getTblItem().getIditem());
                            if (destinationItemLocation == null) {
                                destinationItemLocation = new TblItemLocation();
                                destinationItemLocation.setTblItem(itemLocation.getTblItem());
                                destinationItemLocation.setTblLocation(getBin().getTblLocation());
                                destinationItemLocation.setItemQuantity(new BigDecimal("0"));
                                destinationItemLocation.setCreateDate(Timestamp.valueOf(LocalDateTime.now()));
                                destinationItemLocation.setTblEmployeeByCreateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                                destinationItemLocation.setRefRecordStatus(session.find(RefRecordStatus.class, 1));
                            }
                            destinationItemLocation.setItemQuantity(destinationItemLocation.getItemQuantity().add(dataMutation.getItemQuantity()));
                            destinationItemLocation.setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                            destinationItemLocation.setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                            session.saveOrUpdate(destinationItemLocation);
                        }
                        session.evict(bin);
                    } else {
                        if (session.getTransaction().isActive()) {
                            session.getTransaction().rollback();
                        }
                        errMsg = "Data Bin - Tidak Ditemukan..!";
                        return false;
                    }
                    //data log
                    dataLog += "Denda Kartu Hilang/Rusak : " + ClassFormatter.currencyFormat.cFormat(dataReservationPaymentForMissedCard.getUnitNominal())
                            + " (" + String.valueOf(totalMissedCard) + ")\n";
                    dataLog += "-----------------------------------------------------------------------\n";
                    dataLog += "Kurangi Stok Kartu : " + String.valueOf(totalMissedCard) + "(jumlah kartu) \n"
                            + "- Data Kartu : " + itemGuestCard.getCodeItem() + " - " + itemGuestCard.getItemName() + " \n"
                            + "- Data Lokasi Kartu : " + getDataLocationOfWarehouseName(locationFO) + " \n";
                    dataLog += "-----------------------------------------------------------------------\n";
                    //open close company balance - detail (cashier)
                    if (dataOpenCloseCashierBalance != null) {
                        TblOpenCloseCashierBalanceDetail dataOpenCloseCashierBalanceDetailMissedCard = new TblOpenCloseCashierBalanceDetail();
                        dataOpenCloseCashierBalanceDetailMissedCard.setTblOpenCloseCashierBalance(dataOpenCloseCashierBalance);
                        dataOpenCloseCashierBalanceDetailMissedCard.setTblReservationPayment(dataReservationPaymentForMissedCard);
                        dataOpenCloseCashierBalanceDetailMissedCard.setDetailData(getDetailData(dataReservationPaymentForMissedCard));
                        dataOpenCloseCashierBalanceDetailMissedCard.setCreateDate(Timestamp.valueOf(LocalDateTime.now()));
                        dataOpenCloseCashierBalanceDetailMissedCard.setTblEmployeeByCreateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                        dataOpenCloseCashierBalanceDetailMissedCard.setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                        dataOpenCloseCashierBalanceDetailMissedCard.setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                        dataOpenCloseCashierBalanceDetailMissedCard.setRefRecordStatus(session.find(RefRecordStatus.class, 1));
                        session.saveOrUpdate(dataOpenCloseCashierBalanceDetailMissedCard);
                    }
                }
                //data log
                LogSystem logSystem = new LogSystem();
                logSystem.setLogDateTime(Timestamp.valueOf(LocalDateTime.now()));
                logSystem.setTblSystemUser(session.find(TblSystemUser.class, ClassSession.currentUser.getIduser()));
                logSystem.setTblEmployee(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                logSystem.setLogHeader(headerLog);
                logSystem.setLogDetail(dataLog);
                logSystem.setHostName(ClassComp.getHostName());
                session.saveOrUpdate(logSystem);
                //123456
                if (isAllCheckOut(reservation)
                        && (reservationBillNominal
                        .compareTo(new BigDecimal("0")) < 1)) {
                    //data transaction return (auto, -balance cashier balance)
                    if (reservationBillNominal
                            .compareTo(new BigDecimal("0")) == -1) {
                        //data reservation payment (return payment)
                        TblReservationPayment dataReservationReturnPayment = new TblReservationPayment();
                        dataReservationReturnPayment.setCodePayment(ClassCoder.getCode("Reservation Payment", session));
                        dataReservationReturnPayment.setTblReservationBill(reservationBill);
                        dataReservationReturnPayment.setPaymentDate(Timestamp.valueOf(LocalDateTime.now()));
                        dataReservationReturnPayment.setUnitNominal(getNominalAfterRounding(reservationBillNominal.abs()));
                        dataReservationReturnPayment.setRoundingValue(getRoundingValue(reservationBillNominal.abs()));
                        dataReservationReturnPayment.setTblEmployeeByIdcashier(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                        dataReservationReturnPayment.setRefFinanceTransactionPaymentType(session.find(RefFinanceTransactionPaymentType.class, 16));     //Return = '16'
                        dataReservationReturnPayment.setRefReservationBillType(session.find(RefReservationBillType.class, 0));    //reservasi = '0'
                        dataReservationReturnPayment.setSettleDate(Timestamp.valueOf(LocalDateTime.now()));     //auto settle
                        dataReservationReturnPayment.setCreateDate(Timestamp.valueOf(LocalDateTime.now()));
                        dataReservationReturnPayment.setTblEmployeeByCreateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                        dataReservationReturnPayment.setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                        dataReservationReturnPayment.setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                        dataReservationReturnPayment.setRefRecordStatus(session.find(RefRecordStatus.class, 1));
                        session.saveOrUpdate(dataReservationReturnPayment);
                        //data log
                        dataLog += "Pengembalian Pembayaran : " + ClassFormatter.currencyFormat.cFormat(dataReservationReturnPayment.getUnitNominal()) + ")\n";
                        dataLog += "-----------------------------------------------------------------------\n";
                        //open close company balance - detail (cashier)
                        if (dataOpenCloseCashierBalance != null) {
                            TblOpenCloseCashierBalanceDetail dataOpenCloseCashierBalanceDetail = new TblOpenCloseCashierBalanceDetail();
                            dataOpenCloseCashierBalanceDetail.setTblOpenCloseCashierBalance(dataOpenCloseCashierBalance);
                            dataOpenCloseCashierBalanceDetail.setTblReservationPayment(dataReservationReturnPayment);
                            dataOpenCloseCashierBalanceDetail.setDetailData(getDetailData(dataReservationReturnPayment));
                            dataOpenCloseCashierBalanceDetail.setCreateDate(Timestamp.valueOf(LocalDateTime.now()));
                            dataOpenCloseCashierBalanceDetail.setTblEmployeeByCreateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                            dataOpenCloseCashierBalanceDetail.setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                            dataOpenCloseCashierBalanceDetail.setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                            dataOpenCloseCashierBalanceDetail.setRefRecordStatus(session.find(RefRecordStatus.class, 1));
                            session.saveOrUpdate(dataOpenCloseCashierBalanceDetail);
                        }
                        //data company balance (cashier-)
                        dataCashierBalance.setBalanceNominal(dataCashierBalance.getBalanceNominal().subtract(dataReservationReturnPayment.getUnitNominal()));
                        dataCashierBalance.setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                        dataCashierBalance.setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                        session.update(dataCashierBalance);
                        //data company balance (cash-flow log)
                        LogCompanyBalanceCashFlow returnLogCompanyBalanceCashFlow = new LogCompanyBalanceCashFlow();
                        returnLogCompanyBalanceCashFlow.setTblCompanyBalanceByIdsenderCompanyBalance(dataCashierBalance);
                        returnLogCompanyBalanceCashFlow.setTblBankAccountByIdsenderCbbankAccount(null);
                        returnLogCompanyBalanceCashFlow.setTblCompanyBalanceByIdreceiverCompanyBalance(null);
                        returnLogCompanyBalanceCashFlow.setTblBankAccountByIdreceiverCbbankAccount(null);
                        returnLogCompanyBalanceCashFlow.setTransferNominal(dataReservationReturnPayment.getUnitNominal());
                        returnLogCompanyBalanceCashFlow.setHistoryDate(Timestamp.valueOf(LocalDateTime.now()));
                        returnLogCompanyBalanceCashFlow.setTblEmployee(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                        returnLogCompanyBalanceCashFlow.setHistoryStystemNote("Pengembalian pembayaran kepada " + reservation.getTblCustomer().getTblPeople().getFullName() + ", \nsebesar : "
                                + ClassFormatter.currencyFormat.format(dataReservationReturnPayment.getUnitNominal()) + " ("
                                + dataReservationReturnPayment.getRefFinanceTransactionPaymentType().getTypeName() + ")");
                        session.saveOrUpdate(returnLogCompanyBalanceCashFlow);
                    }
                    //data reservation (update:checkout)
                    reservation.setDepartureTime(Timestamp.valueOf(LocalDateTime.now()));
                    reservation.setRefReservationStatus(session.find(RefReservationStatus.class, 5));   //Checked Out = '5'
                    reservation.setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                    reservation.setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                    session.update(reservation);
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

    private TblLocationOfBin getBin() {
        List<TblLocationOfBin> list = session.getNamedQuery("findAllTblLocationOfBin").list();
        return list.isEmpty() ? null : list.get(0);
    }

    private TblItemLocation getItemLocationByIDLocationAndIDItem(long idLocation, long idItem) {
        List<TblItemLocation> list = session.getNamedQuery("findAllItemLocationByIdLocationAndIdItem")
                .setParameter("idLocation", idLocation)
                .setParameter("idItem", idItem)
                .list();
        return list.isEmpty() ? null : list.get(0);
    }

    private boolean isAllCheckOut(TblReservation dataReservation) {
        List<TblReservationRoomTypeDetail> dataDetails = session.getNamedQuery("findAllTblReservationRoomTypeDetailByIDReservation")
                .setParameter("idReservation", dataReservation.getIdreservation())
                .list();
        for (TblReservationRoomTypeDetail dataDetail : dataDetails) {
            if (dataDetail.getTblReservationCheckInOut() == null
                    || (dataDetail.getTblReservationCheckInOut().getRefReservationCheckInOutStatus().getIdstatus() != 3 //Checked Out = '3'
                    && dataDetail.getTblReservationCheckInOut().getRefReservationCheckInOutStatus().getIdstatus() != 4)) {    //Tidak Berlaku = '4'
                System.out.println("~~~ " + dataDetail.getIddetail() + " <-> "
                        + dataDetail.getTblReservationCheckInOut().getRefReservationCheckInOutStatus() != null ? dataDetail.getTblReservationCheckInOut().getRefReservationCheckInOutStatus().getIdstatus() : "NULL");
                System.out.println("all check-out : FALSE");
                return false;
            }
        }
        System.out.println("all check-out : TRUE");
        return true;
    }

    private RefRoomStatus getOldRoomStatus(long idRoom) {
        TblRoom dataRoom = session.find(TblRoom.class, idRoom);
        RefRoomStatus dataRoomStatus = session.find(RefRoomStatus.class, dataRoom.getRefRoomStatus().getIdstatus());
        session.evict(dataRoom);
        return dataRoomStatus;
    }

    @Override
    public boolean updateDataReservationRoomTypeDetailExtendRoom(
            TblReservation reservation,
            TblReservationBill reservationBill,
            TblReservationRoomTypeDetail reservationRoomTypeDetail,
            List<TblReservationRoomTypeDetailRoomPriceDetail> reservationRoomTypeDetailRoomPriceDetails,
            List<TblReservationAdditionalItem> rais,
            List<TblReservationAdditionalService> rass) {
        errMsg = "";
        if (ClassSession.checkUserSession()) {  //check user current session
            try {
                session = HibernateUtil.getSessionFactory().getCurrentSession();
                session.beginTransaction();
                //data log
                String headerLog = "BUAT DATA EXTEND KAMAR";
                String dataLog = "";
                //data reservation
                reservation.setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                reservation.setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                session.update(reservation);
                //data log
                dataLog += "No. Reservasi : " + reservation.getCodeReservation() + "\n";
                dataLog += "Tgl. Reservasi : " + ClassFormatter.dateFormate.format(reservation.getReservationDate()) + "\n";
                dataLog += "Nama Customer : " + reservation.getTblCustomer().getTblPeople().getFullName() + "\n";
                dataLog += "-----------------------------------------------------------------------\n";
                //data reservation bill
                reservationBill.setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                reservationBill.setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                session.update(reservationBill);
                //data reservation room type detail
//                reservationRoomTypeDetail.setCheckOutDateTime();    ...???
                reservationRoomTypeDetail.setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                reservationRoomTypeDetail.setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                session.update(reservationRoomTypeDetail);
                //data reservation room type detail - reservation checkin/out
                reservationRoomTypeDetail.getTblReservationCheckInOut().setRefReservationCheckInOutStatus(session.find(RefReservationCheckInOutStatus.class, 1));   //Checked In = '1'
                reservationRoomTypeDetail.getTblReservationCheckInOut().setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                reservationRoomTypeDetail.getTblReservationCheckInOut().setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                session.saveOrUpdate(reservationRoomTypeDetail.getTblReservationCheckInOut());
                //data reservation room type detail - room service (delete)
                Query rs = session.getNamedQuery("deleteAllTblReservationRoomTypeDetailRoomServiceByIDRoomTypeDetail")
                        .setParameter("idRoomTypeDetail", reservationRoomTypeDetail.getIddetail())
                        .setParameter("idEmployee", ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee());
                errMsg = (String) rs.uniqueResult();
                if (!errMsg.equals("")) {
                    if (session.getTransaction().isActive()) {
                        session.getTransaction().rollback();
                    }
                    return false;
                }
                //data reservation room type detail - room service (insert)
                List<TblRoomTypeRoomService> roomTypeRoomServices = getAllRoomTypeRoomServiceByIDRoomType(reservationRoomTypeDetail.getTblRoomType().getIdroomType());
                for (TblRoomTypeRoomService roomTypeRoomService : roomTypeRoomServices) {
                    TblReservationRoomTypeDetailRoomService reservationRoomTypeDetailRoomService = new TblReservationRoomTypeDetailRoomService();
                    reservationRoomTypeDetailRoomService.setTblReservationRoomTypeDetail(reservationRoomTypeDetail);
                    reservationRoomTypeDetailRoomService.setTblRoomService(roomTypeRoomService.getTblRoomService());
                    reservationRoomTypeDetailRoomService.setRoomServiceCost(roomTypeRoomService.getTblRoomService().getPrice());
                    reservationRoomTypeDetailRoomService.setPeopleNumber(roomTypeRoomService.getPeopleNumber());
                    reservationRoomTypeDetailRoomService.setCreateDate(Timestamp.valueOf(LocalDateTime.now()));
                    reservationRoomTypeDetailRoomService.setTblEmployeeByCreateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                    reservationRoomTypeDetailRoomService.setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                    reservationRoomTypeDetailRoomService.setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                    reservationRoomTypeDetailRoomService.setRefRecordStatus(session.find(RefRecordStatus.class, 1));
                    session.saveOrUpdate(reservationRoomTypeDetailRoomService);
                }
                //data reservation room type detail - item (delete)
                rs = session.getNamedQuery("deleteAllTblReservationRoomTypeDetailItemByIDRoomTypeDetail")
                        .setParameter("idRoomTypeDetail", reservationRoomTypeDetail.getIddetail())
                        .setParameter("idEmployee", ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee());
                errMsg = (String) rs.uniqueResult();
                if (!errMsg.equals("")) {
                    if (session.getTransaction().isActive()) {
                        session.getTransaction().rollback();
                    }
                    return false;
                }
                //data reservation room type detail - item (insert)
                List<TblRoomTypeItem> roomTypeItems = getAllRoomTypeItemByIDRoomType(reservationRoomTypeDetail.getTblRoomType().getIdroomType());
                for (TblRoomTypeItem roomTypeItem : roomTypeItems) {
                    TblReservationRoomTypeDetailItem reservationRoomTypeDetailItem = new TblReservationRoomTypeDetailItem();
                    reservationRoomTypeDetailItem.setTblReservationRoomTypeDetail(reservationRoomTypeDetail);
                    reservationRoomTypeDetailItem.setTblItem(roomTypeItem.getTblItem());
                    reservationRoomTypeDetailItem.setItemAdditionalCharge(roomTypeItem.getTblItem().getAdditionalCharge());
                    reservationRoomTypeDetailItem.setItemBrokenCharge(roomTypeItem.getTblItem().getBrokenCharge());
                    reservationRoomTypeDetailItem.setItemQuantity(roomTypeItem.getItemQuantity());
                    reservationRoomTypeDetailItem.setCreateDate(Timestamp.valueOf(LocalDateTime.now()));
                    reservationRoomTypeDetailItem.setTblEmployeeByCreateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                    reservationRoomTypeDetailItem.setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                    reservationRoomTypeDetailItem.setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                    reservationRoomTypeDetailItem.setRefRecordStatus(session.find(RefRecordStatus.class, 1));
                    session.saveOrUpdate(reservationRoomTypeDetailItem);
                }
                //data reservation room type detail - room price detail
                for (TblReservationRoomTypeDetailRoomPriceDetail reservationRoomTypeDetailRoomPriceDetail : reservationRoomTypeDetailRoomPriceDetails) {
                    //data reservation room price detail
                    reservationRoomTypeDetailRoomPriceDetail.getTblReservationRoomPriceDetail().setCreateDate(Timestamp.valueOf(LocalDateTime.now()));
                    reservationRoomTypeDetailRoomPriceDetail.getTblReservationRoomPriceDetail().setTblEmployeeByCreateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                    reservationRoomTypeDetailRoomPriceDetail.getTblReservationRoomPriceDetail().setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                    reservationRoomTypeDetailRoomPriceDetail.getTblReservationRoomPriceDetail().setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                    reservationRoomTypeDetailRoomPriceDetail.getTblReservationRoomPriceDetail().setRefRecordStatus(session.find(RefRecordStatus.class, 1));
                    session.saveOrUpdate(reservationRoomTypeDetailRoomPriceDetail.getTblReservationRoomPriceDetail());
                    //data reservation room type detail - room price detail
                    reservationRoomTypeDetailRoomPriceDetail.setCreateDate(Timestamp.valueOf(LocalDateTime.now()));
                    reservationRoomTypeDetailRoomPriceDetail.setTblEmployeeByCreateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                    reservationRoomTypeDetailRoomPriceDetail.setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                    reservationRoomTypeDetailRoomPriceDetail.setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                    reservationRoomTypeDetailRoomPriceDetail.setRefRecordStatus(session.find(RefRecordStatus.class, 1));
                    session.saveOrUpdate(reservationRoomTypeDetailRoomPriceDetail);
                }
                //data log
                dataLog += "Reservasi Kamar (Extend) : \n";
                dataLog += "- " + reservationRoomTypeDetail.getCodeDetail() + " - " + reservationRoomTypeDetail.getCheckInDateTime() + " s/d " + reservationRoomTypeDetail.getCheckOutDateTime() + " \t "
                        + (getDataRoom(reservationRoomTypeDetail)) + " \n";
                for (TblReservationRoomTypeDetailRoomPriceDetail reservationRoomTypeDetailRoomPriceDetail : reservationRoomTypeDetailRoomPriceDetails) {
                    if (reservationRoomTypeDetailRoomPriceDetail.getTblReservationRoomTypeDetail().getIddetail()
                            == reservationRoomTypeDetail.getIddetail()) {
                        dataLog += "\t - " + ClassFormatter.dateFormate.format(reservationRoomTypeDetailRoomPriceDetail.getTblReservationRoomPriceDetail().getDetailDate()) + " \t "
                                + "Harga : " + ClassFormatter.currencyFormat.cFormat(reservationRoomTypeDetailRoomPriceDetail.getTblReservationRoomPriceDetail().getDetailPrice()) + " \t "
                                + "Diskon : " + ClassFormatter.decimalFormat.cFormat(reservationRoomTypeDetailRoomPriceDetail.getTblReservationRoomPriceDetail().getDetailDiscountPercentage()) + "% (" + getDataDiscountEvent(reservationRoomTypeDetailRoomPriceDetail.getTblReservationRoomPriceDetail()) + ") \t "
                                + "Compliment : " + ClassFormatter.currencyFormat.cFormat(reservationRoomTypeDetailRoomPriceDetail.getTblReservationRoomPriceDetail().getDetailComplimentary()) + " \t "
                                + getDataTravelAgentCommission(reservationRoomTypeDetail, new ArrayList<>()) + " \n";
                    }
                }
                //data log
                dataLog += "-----------------------------------------------------------------------\n";
                //data reservation additional service
                for (TblReservationAdditionalService reservationAdditionalService : rass) {
                    //data reservation additional service
                    reservationAdditionalService.setCreateDate(Timestamp.valueOf(LocalDateTime.now()));
                    reservationAdditionalService.setTblEmployeeByCreateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                    reservationAdditionalService.setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                    reservationAdditionalService.setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                    reservationAdditionalService.setRefRecordStatus(session.find(RefRecordStatus.class, 1));
                    session.saveOrUpdate(reservationAdditionalService);
                    //data log
                    dataLog += "Tambahan Layanan : \n";
                    dataLog += "- " + ClassFormatter.dateFormate.format(reservationAdditionalService.getAdditionalDate()) + " - " + reservationAdditionalService.getTblRoomService().getServiceName() + " \t "
                            + "Harga : " + ClassFormatter.currencyFormat.cFormat(reservationAdditionalService.getPrice()) + " \t "
                            + "Diskon : " + ClassFormatter.decimalFormat.cFormat(reservationAdditionalService.getDiscountPercentage()) + "% (" + getDataDiscountEvent(reservationAdditionalService) + ") \n";
                }
                //data log
                dataLog += "-----------------------------------------------------------------------\n";
                dataLog += "Tambahan Barang : \n";
                //data reservation additional item
                for (TblReservationAdditionalItem reservationAdditionalItem : rais) {
                    //data reservation additional item
                    reservationAdditionalItem.setCreateDate(Timestamp.valueOf(LocalDateTime.now()));
                    reservationAdditionalItem.setTblEmployeeByCreateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                    reservationAdditionalItem.setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                    reservationAdditionalItem.setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                    reservationAdditionalItem.setRefRecordStatus(session.find(RefRecordStatus.class, 1));
                    session.saveOrUpdate(reservationAdditionalItem);
                    //data log
                    dataLog += "- " + ClassFormatter.dateFormate.format(reservationAdditionalItem.getAdditionalDate()) + " - " + reservationAdditionalItem.getTblItem().getItemName() + " \t "
                            + "Harga : " + ClassFormatter.currencyFormat.cFormat(reservationAdditionalItem.getItemCharge()) + " \t "
                            + "Diskon : " + ClassFormatter.decimalFormat.cFormat(reservationAdditionalItem.getDiscountPercentage()) + "% (" + getDataDiscountEvent(reservationAdditionalItem) + ") \t "
                            + "Jumlah : " + ClassFormatter.decimalFormat.cFormat(reservationAdditionalItem.getItemQuantity()) + " \n";
                }
                //data log
                dataLog += "-----------------------------------------------------------------------\n";
                LogSystem logSystem = new LogSystem();
                logSystem.setLogDateTime(Timestamp.valueOf(LocalDateTime.now()));
                logSystem.setTblSystemUser(session.find(TblSystemUser.class, ClassSession.currentUser.getIduser()));
                logSystem.setTblEmployee(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                logSystem.setLogHeader(headerLog);
                logSystem.setLogDetail(dataLog);
                logSystem.setHostName(ClassComp.getHostName());
                session.saveOrUpdate(logSystem);
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
    public boolean insertDataReservationAddtionalItems(
            TblReservation reservation,
            List<TblReservationAdditionalItem> reservationAdditionalItems) {
        errMsg = "";
        if (ClassSession.checkUserSession()) {  //check user current session
            try {
                session = HibernateUtil.getSessionFactory().getCurrentSession();
                session.beginTransaction();
                //data log
                String headerLog = "BUAT DATA TAMBAHAN BARANG (RESERVASI)";
                String dataLog = "";
                //data reservation
                reservation.setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                reservation.setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                session.update(reservation);
                //data log
                dataLog += "No. Reservasi : " + reservation.getCodeReservation() + "\n";
                dataLog += "Tgl. Reservasi : " + ClassFormatter.dateFormate.format(reservation.getReservationDate()) + "\n";
                dataLog += "Nama Customer : " + reservation.getTblCustomer().getTblPeople().getFullName() + "\n";
                dataLog += "Travel Agent : " + (reservation.getTblPartner() != null ? reservation.getTblPartner().getPartnerName() : "-") + "\n";
                dataLog += "-----------------------------------------------------------------------\n";
                dataLog += "Data Tambahan Barang : " + reservation.getCodeReservation() + "\n";
                //data reservation additonal item
                for (TblReservationAdditionalItem reservationAdditionalItem : reservationAdditionalItems) {
                    //data reservation additonal item
                    reservationAdditionalItem.setCreateDate(Timestamp.valueOf(LocalDateTime.now()));
                    reservationAdditionalItem.setTblEmployeeByCreateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                    reservationAdditionalItem.setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                    reservationAdditionalItem.setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                    reservationAdditionalItem.setRefRecordStatus(session.find(RefRecordStatus.class, 1));
                    session.saveOrUpdate(reservationAdditionalItem);
                    //data reservation additonal item - reserved
                    TblReservationAdditionalItemReserved reservationAdditionalItemReserved = getReservationAdditionalItemReservedByIDReservationRoomTypeDetailAndIDItemAndReservedDate(
                            reservationAdditionalItem.getTblReservationRoomTypeDetail().getIddetail(),
                            reservationAdditionalItem.getTblItem().getIditem(),
                            reservationAdditionalItem.getAdditionalDate());
                    if (reservationAdditionalItemReserved != null) {
                        reservationAdditionalItemReserved.setReservedQuantity(reservationAdditionalItemReserved.getReservedQuantity().add(reservationAdditionalItem.getItemQuantity()));
                        reservationAdditionalItemReserved.setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                        reservationAdditionalItemReserved.setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                        session.update(reservationAdditionalItemReserved);
                    } else {
                        reservationAdditionalItemReserved = new TblReservationAdditionalItemReserved();
                        reservationAdditionalItemReserved.setTblReservationRoomTypeDetail(reservationAdditionalItem.getTblReservationRoomTypeDetail());
                        reservationAdditionalItemReserved.setTblItem(reservationAdditionalItem.getTblItem());
                        reservationAdditionalItemReserved.setReservedDate(reservationAdditionalItem.getAdditionalDate());
                        reservationAdditionalItemReserved.setReservedQuantity(reservationAdditionalItem.getItemQuantity());
                        reservationAdditionalItemReserved.setInUsedQuantity(new BigDecimal("0"));
                        reservationAdditionalItemReserved.setDoneQuantity(new BigDecimal("0"));
                        reservationAdditionalItemReserved.setRefReservationAdditionalItemReservedStatus(session.find(RefReservationAdditionalItemReservedStatus.class, 1));     //Reserved = '1'
                        reservationAdditionalItemReserved.setCreateDate(Timestamp.valueOf(LocalDateTime.now()));
                        reservationAdditionalItemReserved.setTblEmployeeByCreateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                        reservationAdditionalItemReserved.setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                        reservationAdditionalItemReserved.setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                        reservationAdditionalItemReserved.setRefRecordStatus(session.find(RefRecordStatus.class, 1));
                        session.saveOrUpdate(reservationAdditionalItemReserved);
                    }
                    //data log
                    dataLog += "- " + ClassFormatter.dateFormate.format(reservationAdditionalItem.getAdditionalDate()) + " - " + reservationAdditionalItem.getTblItem().getItemName() + " \t "
                            + "Harga : " + ClassFormatter.currencyFormat.cFormat(reservationAdditionalItem.getItemCharge()) + " \t "
                            + "Diskon : " + ClassFormatter.decimalFormat.cFormat(reservationAdditionalItem.getDiscountPercentage()) + "% (" + getDataDiscountEvent(reservationAdditionalItem) + ") \t "
                            + "Jumlah : " + ClassFormatter.decimalFormat.cFormat(reservationAdditionalItem.getItemQuantity()) + " \n";
                }
                //data log
                dataLog += "-----------------------------------------------------------------------\n";
                LogSystem logSystem = new LogSystem();
                logSystem.setLogDateTime(Timestamp.valueOf(LocalDateTime.now()));
                logSystem.setTblSystemUser(session.find(TblSystemUser.class, ClassSession.currentUser.getIduser()));
                logSystem.setTblEmployee(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                logSystem.setLogHeader(headerLog);
                logSystem.setLogDetail(dataLog);
                logSystem.setHostName(ClassComp.getHostName());
                session.saveOrUpdate(logSystem);
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

    private TblReservationAdditionalItemReserved getReservationAdditionalItemReservedByIDReservationRoomTypeDetailAndIDItemAndReservedDate(
            long idRRTD,
            long idItem,
            java.util.Date reservedDate) {
        List<TblReservationAdditionalItemReserved> list = session.getNamedQuery("findAllTblReservationAdditionalItemReservedByIDReservationRoomTypeDetailAndIDItemAndReservedDate")
                .setParameter("idRRTD", idRRTD)
                .setParameter("idItem", idItem)
                .setParameter("reservedDate", reservedDate)
                .list();
        return list.isEmpty() ? null : list.get(0);
    }

    @Override
    public boolean deleteDataReservationAddtionalItem(
            TblReservation reservation,
            TblReservationAdditionalItem reservationAdditionalItem) {
        errMsg = "";
        if (ClassSession.checkUserSession()) {  //check user current session
            try {
                session = HibernateUtil.getSessionFactory().getCurrentSession();
                session.beginTransaction();
                //data log
                String headerLog = "HAPUS DATA TAMBAHAN BARANG (RESERVASI)";
                String dataLog = "";
                if (reservationAdditionalItem.getRefRecordStatus().getIdstatus() == 1) {
                    //data reservation
                    reservation.setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                    reservation.setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                    session.update(reservation);
                    //data log
                    dataLog += "No. Reservasi : " + reservation.getCodeReservation() + "\n";
                    dataLog += "Tgl. Reservasi : " + ClassFormatter.dateFormate.format(reservation.getReservationDate()) + "\n";
                    dataLog += "Nama Customer : " + reservation.getTblCustomer().getTblPeople().getFullName() + "\n";
                    dataLog += "Travel Agent : " + (reservation.getTblPartner() != null ? reservation.getTblPartner().getPartnerName() : "-") + "\n";
                    dataLog += "-----------------------------------------------------------------------\n";
                    //data reservation additional item - reserved
                    TblReservationAdditionalItemReserved reservationAdditionalItemReserved = getReservationAdditionalItemReservedByIDReservationRoomTypeDetailAndIDItemAndReservedDate(
                            reservationAdditionalItem.getTblReservationRoomTypeDetail().getIddetail(),
                            reservationAdditionalItem.getTblItem().getIditem(),
                            reservationAdditionalItem.getAdditionalDate());
                    if (reservationAdditionalItemReserved != null) {
                        reservationAdditionalItemReserved.setReservedQuantity(reservationAdditionalItemReserved.getReservedQuantity().subtract(reservationAdditionalItem.getItemQuantity()));
                        reservationAdditionalItemReserved.setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                        reservationAdditionalItemReserved.setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                        session.update(reservationAdditionalItemReserved);
                    } else {
                        //this is error stage
                    }
                    //data reservation additonal item
                    reservationAdditionalItem.setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                    reservationAdditionalItem.setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                    reservationAdditionalItem.setRefRecordStatus(session.find(RefRecordStatus.class, 2));
                    session.update(reservationAdditionalItem);
                    //data log
                    dataLog += "Data Tambahan Barang : " + reservation.getCodeReservation() + "\n";
                    dataLog += "- " + ClassFormatter.dateFormate.format(reservationAdditionalItem.getAdditionalDate()) + " - " + reservationAdditionalItem.getTblItem().getItemName() + " \t "
                            + "Harga : " + ClassFormatter.currencyFormat.cFormat(reservationAdditionalItem.getItemCharge()) + " \t "
                            + "Diskon : " + ClassFormatter.decimalFormat.cFormat(reservationAdditionalItem.getDiscountPercentage()) + "% (" + getDataDiscountEvent(reservationAdditionalItem) + ") \t "
                            + "Jumlah : " + ClassFormatter.decimalFormat.cFormat(reservationAdditionalItem.getItemQuantity()) + " \n";
                    dataLog += "-----------------------------------------------------------------------\n";
                } else {
                    if (session.getTransaction().isActive()) {
                        session.getTransaction().rollback();
                    }
                    errMsg = "Data tidak dapat dihapus!!";
                    return false;
                }
                //data log
                LogSystem logSystem = new LogSystem();
                logSystem.setLogDateTime(Timestamp.valueOf(LocalDateTime.now()));
                logSystem.setTblSystemUser(session.find(TblSystemUser.class, ClassSession.currentUser.getIduser()));
                logSystem.setTblEmployee(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                logSystem.setLogHeader(headerLog);
                logSystem.setLogDetail(dataLog);
                logSystem.setHostName(ClassComp.getHostName());
                session.saveOrUpdate(logSystem);
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
    public boolean insertDataReservationAddtionalServices(
            TblReservation reservation,
            List<TblReservationAdditionalService> reservationAdditionalServices) {
        errMsg = "";
        if (ClassSession.checkUserSession()) {  //check user current session
            try {
                session = HibernateUtil.getSessionFactory().getCurrentSession();
                session.beginTransaction();
                //data log
                String headerLog = "BUAT DATA TAMBAHAN LAYANAN (RESERVASI)";
                String dataLog = "";
                //data reservation
                reservation.setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                reservation.setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                session.update(reservation);
                //data log
                dataLog += "No. Reservasi : " + reservation.getCodeReservation() + "\n";
                dataLog += "Tgl. Reservasi : " + ClassFormatter.dateFormate.format(reservation.getReservationDate()) + "\n";
                dataLog += "Nama Customer : " + reservation.getTblCustomer().getTblPeople().getFullName() + "\n";
                dataLog += "Travel Agent : " + (reservation.getTblPartner() != null ? reservation.getTblPartner().getPartnerName() : "-") + "\n";
                dataLog += "-----------------------------------------------------------------------\n";
                dataLog += "Data Tambahan Layanan : " + reservation.getCodeReservation() + "\n";
                //data reservation additonal service
                for (TblReservationAdditionalService reservationAdditionalService : reservationAdditionalServices) {
                    //data reservation additonal service
                    reservationAdditionalService.setCreateDate(Timestamp.valueOf(LocalDateTime.now()));
                    reservationAdditionalService.setTblEmployeeByCreateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                    reservationAdditionalService.setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                    reservationAdditionalService.setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                    reservationAdditionalService.setRefRecordStatus(session.find(RefRecordStatus.class, 1));
                    session.saveOrUpdate(reservationAdditionalService);
                    //data log
                    dataLog += "- " + ClassFormatter.dateFormate.format(reservationAdditionalService.getAdditionalDate()) + " - " + reservationAdditionalService.getTblRoomService().getIdroomService() + " \t "
                            + "Harga : " + ClassFormatter.currencyFormat.cFormat(reservationAdditionalService.getPrice()) + " \t "
                            + "Diskon : " + ClassFormatter.decimalFormat.cFormat(reservationAdditionalService.getDiscountPercentage()) + "% (" + getDataDiscountEvent(reservationAdditionalService) + ") \n";
                }
                dataLog += "-----------------------------------------------------------------------\n";
                //data log
                LogSystem logSystem = new LogSystem();
                logSystem.setLogDateTime(Timestamp.valueOf(LocalDateTime.now()));
                logSystem.setTblSystemUser(session.find(TblSystemUser.class, ClassSession.currentUser.getIduser()));
                logSystem.setTblEmployee(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                logSystem.setLogHeader(headerLog);
                logSystem.setLogDetail(dataLog);
                logSystem.setHostName(ClassComp.getHostName());
                session.saveOrUpdate(logSystem);
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
    public boolean deleteDataReservationAddtionalService(
            TblReservation reservation,
            TblReservationAdditionalService reservationAdditionalService) {
        errMsg = "";
        if (ClassSession.checkUserSession()) {  //check user current session
            try {
                session = HibernateUtil.getSessionFactory().getCurrentSession();
                session.beginTransaction();
                //data log
                String headerLog = "HAPUS DATA TAMBAHAN LAYANAN (RESERVASI)";
                String dataLog = "";
                if (reservationAdditionalService.getRefRecordStatus().getIdstatus() == 1) {
                    //data reservation
                    reservation.setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                    reservation.setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                    session.update(reservation);
                    //data log
                    dataLog += "No. Reservasi : " + reservation.getCodeReservation() + "\n";
                    dataLog += "Tgl. Reservasi : " + ClassFormatter.dateFormate.format(reservation.getReservationDate()) + "\n";
                    dataLog += "Nama Customer : " + reservation.getTblCustomer().getTblPeople().getFullName() + "\n";
                    dataLog += "Travel Agent : " + (reservation.getTblPartner() != null ? reservation.getTblPartner().getPartnerName() : "-") + "\n";
                    dataLog += "-----------------------------------------------------------------------\n";
                    //data reservation additonal service
                    reservationAdditionalService.setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                    reservationAdditionalService.setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                    reservationAdditionalService.setRefRecordStatus(session.find(RefRecordStatus.class, 2));
                    session.update(reservationAdditionalService);
                    //data log
                    dataLog += "Data Tambahan Layanan : " + reservation.getCodeReservation() + "\n";
                    dataLog += "- " + ClassFormatter.dateFormate.format(reservationAdditionalService.getAdditionalDate()) + " - " + reservationAdditionalService.getTblRoomService().getIdroomService() + " \t "
                            + "Harga : " + ClassFormatter.currencyFormat.cFormat(reservationAdditionalService.getPrice()) + " \t "
                            + "Diskon : " + ClassFormatter.decimalFormat.cFormat(reservationAdditionalService.getDiscountPercentage()) + "% (" + getDataDiscountEvent(reservationAdditionalService) + ") \n";
                    dataLog += "-----------------------------------------------------------------------\n";
                } else {
                    if (session.getTransaction().isActive()) {
                        session.getTransaction().rollback();
                    }
                    errMsg = "Data tidak dapat dihapus!!";
                    return false;
                }
                //data log
                LogSystem logSystem = new LogSystem();
                logSystem.setLogDateTime(Timestamp.valueOf(LocalDateTime.now()));
                logSystem.setTblSystemUser(session.find(TblSystemUser.class, ClassSession.currentUser.getIduser()));
                logSystem.setTblEmployee(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                logSystem.setLogHeader(headerLog);
                logSystem.setLogDetail(dataLog);
                logSystem.setHostName(ClassComp.getHostName());
                session.saveOrUpdate(logSystem);
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
    public boolean updateDataReservationRoomTypeDetailEarlyCheckIn(
            TblReservation reservation,
            TblReservationRoomTypeDetail reservationRoomTypeDetail,
            List<TblReservationRoomTypeDetailChildDetail> reservationChildInfos,
            TblReservationAdditionalService reservationAdditionalService) {
        errMsg = "";
        if (ClassSession.checkUserSession()) {  //check user current session
            try {
                session = HibernateUtil.getSessionFactory().getCurrentSession();
                session.beginTransaction();
                //data log
                String headerLog = "BUAT DATA EARLY-CHECKIN (RESERVASI)";
                String dataLog = "";
                //data reservation
                if (reservation.getArrivalTime() == null) {
                    reservation.setArrivalTime(Timestamp.valueOf(LocalDateTime.now()));
                }
                reservation.setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                reservation.setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                session.update(reservation);
                //data log
                dataLog += "No. Reservasi : " + reservation.getCodeReservation() + "\n";
                dataLog += "Tgl. Reservasi : " + ClassFormatter.dateFormate.format(reservation.getReservationDate()) + "\n";
                dataLog += "Nama Customer : " + reservation.getTblCustomer().getTblPeople().getFullName() + "\n";
                dataLog += "Travel Agent : " + (reservation.getTblPartner() != null ? reservation.getTblPartner().getPartnerName() : "-") + "\n";
                dataLog += "-----------------------------------------------------------------------\n";
                //data reservation checkinout(in)
                reservationRoomTypeDetail.getTblReservationCheckInOut().setCreateDate(Timestamp.valueOf(LocalDateTime.now()));
                reservationRoomTypeDetail.getTblReservationCheckInOut().setTblEmployeeByCreateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                reservationRoomTypeDetail.getTblReservationCheckInOut().setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                reservationRoomTypeDetail.getTblReservationCheckInOut().setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                reservationRoomTypeDetail.getTblReservationCheckInOut().setRefRecordStatus(session.find(RefRecordStatus.class, 1));
                session.saveOrUpdate(reservationRoomTypeDetail.getTblReservationCheckInOut());
                //data reservation room type detail
                reservationRoomTypeDetail.setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                reservationRoomTypeDetail.setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                session.update(reservationRoomTypeDetail);
                //data log
                dataLog += "Reservasi Kamar : \n";
                dataLog += "- " + reservationRoomTypeDetail.getCodeDetail() + " - " + reservationRoomTypeDetail.getCheckInDateTime() + " s/d " + reservationRoomTypeDetail.getCheckOutDateTime() + " \t "
                        + (getDataRoom(reservationRoomTypeDetail)) + " \n";
                dataLog += "-----------------------------------------------------------------------\n";
                //data room
                reservationRoomTypeDetail.getTblReservationCheckInOut().getTblRoom().setRefRoomStatus(session.get(RefRoomStatus.class, 1)); //Occupied Clean = '1'
                //data room status change history
                TblRoomStatusChangeHistory dataRoomStatusChangeHistory = new TblRoomStatusChangeHistory();
                dataRoomStatusChangeHistory.setTblRoom(reservationRoomTypeDetail.getTblReservationCheckInOut().getTblRoom());
                dataRoomStatusChangeHistory.setTblEmployeeByIdemployee(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                dataRoomStatusChangeHistory.setTblRoomStatusDetail(null);
                dataRoomStatusChangeHistory.setChangeRoomStatusDate(Timestamp.valueOf(LocalDateTime.now()));
                dataRoomStatusChangeHistory.setRefRoomStatusByRoomStatusOld(getOldRoomStatus(reservationRoomTypeDetail.getTblReservationCheckInOut().getTblRoom().getIdroom()));
                dataRoomStatusChangeHistory.setRefRoomStatusByRoomStatusNew(reservationRoomTypeDetail.getTblReservationCheckInOut().getTblRoom().getRefRoomStatus());
                dataRoomStatusChangeHistory.setCreateDate(Timestamp.valueOf(LocalDateTime.now()));
                dataRoomStatusChangeHistory.setTblEmployeeByCreateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                dataRoomStatusChangeHistory.setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                dataRoomStatusChangeHistory.setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                dataRoomStatusChangeHistory.setRefRecordStatus(session.find(RefRecordStatus.class, 1));
                session.saveOrUpdate(dataRoomStatusChangeHistory);
                //data room
                reservationRoomTypeDetail.getTblReservationCheckInOut().getTblRoom().setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                reservationRoomTypeDetail.getTblReservationCheckInOut().getTblRoom().setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                session.update(reservationRoomTypeDetail.getTblReservationCheckInOut().getTblRoom());
                //data reservation room type detail child detail
                for (TblReservationRoomTypeDetailChildDetail reservationChildInfo : reservationChildInfos) {
                    reservationChildInfo.setCreateDate(Timestamp.valueOf(LocalDateTime.now()));
                    reservationChildInfo.setTblEmployeeByCreateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                    reservationChildInfo.setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                    reservationChildInfo.setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                    reservationChildInfo.setRefRecordStatus(session.find(RefRecordStatus.class, 1));
                    session.saveOrUpdate(reservationChildInfo);
                }
                //data reservation bill (deposit)
                TblReservationBill dataReservationBill = getReservationBillByIDReservationAndIDReservationBillType(reservation.getIdreservation(),
                        2); //Deposit = '2'
                if (dataReservationBill == null) {  //create and save reservation bill
                    dataReservationBill = new TblReservationBill();
                    dataReservationBill.setCodeBill(ClassCoder.getCode("Reservation Bill", session));
                    dataReservationBill.setTblReservation(reservation);
                    dataReservationBill.setServiceChargePercentage(new BigDecimal("0"));
                    dataReservationBill.setTaxPercentage(new BigDecimal("0"));
                    dataReservationBill.setRefReservationBillType(session.find(RefReservationBillType.class, 2));
                    dataReservationBill.setCreateDate(Timestamp.valueOf(LocalDateTime.now()));
                    dataReservationBill.setTblEmployeeByCreateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                    dataReservationBill.setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                    dataReservationBill.setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                    dataReservationBill.setRefRecordStatus(session.find(RefRecordStatus.class, 1));
                    session.saveOrUpdate(dataReservationBill);
                }
                //data reservation payment (open-deposit)
                TblReservationPayment dataReservationPayment = new TblReservationPayment();
                dataReservationPayment.setCodePayment(ClassCoder.getCode("Reservation Payment", session));
                dataReservationPayment.setTblReservationBill(dataReservationBill);
                dataReservationPayment.setPaymentDate(Timestamp.valueOf(LocalDateTime.now()));
                dataReservationPayment.setUnitNominal((new BigDecimal(reservationRoomTypeDetail.getTblReservationCheckInOut().getCardUsed()))
                        .multiply(reservationRoomTypeDetail.getTblReservationCheckInOut().getBrokenCardCharge()));
                dataReservationPayment.setRoundingValue(new BigDecimal("0"));
                dataReservationPayment.setTblEmployeeByIdcashier(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                dataReservationPayment.setRefFinanceTransactionPaymentType(session.find(RefFinanceTransactionPaymentType.class, 13));
                dataReservationPayment.setRefReservationBillType(session.find(RefReservationBillType.class, 2));
                dataReservationPayment.setSettleDate(Timestamp.valueOf(LocalDateTime.now()));
                dataReservationPayment.setCreateDate(Timestamp.valueOf(LocalDateTime.now()));
                dataReservationPayment.setTblEmployeeByCreateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                dataReservationPayment.setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                dataReservationPayment.setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                dataReservationPayment.setRefRecordStatus(session.find(RefRecordStatus.class, 1));
                session.saveOrUpdate(dataReservationPayment);
                //data log
                dataLog += "Deposit Kartu : " + ClassFormatter.currencyFormat.cFormat(dataReservationPayment.getUnitNominal())
                        + " (" + reservationRoomTypeDetail.getTblReservationCheckInOut().getCardUsed()
                        + " kartu @" + ClassFormatter.currencyFormat.cFormat(reservationRoomTypeDetail.getTblReservationCheckInOut().getBrokenCardCharge()) + ")\n";
                dataLog += "-----------------------------------------------------------------------\n";
                //data reservation payment with deposit
                TblReservationPaymentWithDeposit dataReservationPaymentWithDeposit = new TblReservationPaymentWithDeposit();
                dataReservationPaymentWithDeposit.setTblReservationPayment(dataReservationPayment);
                dataReservationPaymentWithDeposit.setTblReservationCheckInOut(reservationRoomTypeDetail.getTblReservationCheckInOut());
                dataReservationPaymentWithDeposit.setCreateDate(Timestamp.valueOf(LocalDateTime.now()));
                dataReservationPaymentWithDeposit.setTblEmployeeByCreateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                dataReservationPaymentWithDeposit.setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                dataReservationPaymentWithDeposit.setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                dataReservationPaymentWithDeposit.setRefRecordStatus(session.find(RefRecordStatus.class, 1));
                session.saveOrUpdate(dataReservationPaymentWithDeposit);
                //@@@%%%
                //data deposit balance (update:+)
                TblCompanyBalance dataBalance = session.find(TblCompanyBalance.class, (long) 4);
                dataBalance.setBalanceNominal(dataBalance.getBalanceNominal().add(dataReservationPayment.getUnitNominal()));
                dataBalance.setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                dataBalance.setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                session.update(dataBalance);
                //data company balance (cash-flow log)
                LogCompanyBalanceCashFlow logCompanyBalanceCashFlow = new LogCompanyBalanceCashFlow();
                logCompanyBalanceCashFlow.setTblCompanyBalanceByIdsenderCompanyBalance(null);
                logCompanyBalanceCashFlow.setTblBankAccountByIdsenderCbbankAccount(null);
                logCompanyBalanceCashFlow.setTblCompanyBalanceByIdreceiverCompanyBalance(dataBalance);
                logCompanyBalanceCashFlow.setTblBankAccountByIdreceiverCbbankAccount(null);
                logCompanyBalanceCashFlow.setTransferNominal(dataReservationPayment.getUnitNominal());
                logCompanyBalanceCashFlow.setHistoryDate(Timestamp.valueOf(LocalDateTime.now()));
                logCompanyBalanceCashFlow.setTblEmployee(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                logCompanyBalanceCashFlow.setHistoryStystemNote("Deposit kartu kamar oleh " + reservation.getTblCustomer().getTblPeople().getFullName() + ", \nsebesar : "
                        + ClassFormatter.currencyFormat.format(dataReservationPayment.getUnitNominal()) + " ("
                        + dataReservationPayment.getRefFinanceTransactionPaymentType().getTypeName() + ")");
                session.saveOrUpdate(logCompanyBalanceCashFlow);
                System.out.println("dibawah bukan");
//            //data item mutation history : room-card
//            TblItemMutationHistory dataMutationHistory = new TblItemMutationHistory();
//            dataMutationHistory.setTblItem(detailLocation.getTblDetail().getTblItem());
//            dataMutationHistory.setItemQuantity(new BigDecimal(reservationRoomTypeDetail.getTblReservationCheckInOut().getCardUsed().toString()));
//            dataMutationHistory.setTblLocationByIdlocationOfSource(tblMemorandumInvoice.getTblPurchaseOrder().getTblSupplier().getTblLocation());
//            dataMutationHistory.setTblLocationByIdlocationOfDestination(detailLocation.getTblLocationOfWarehouse().getTblLocation());
//            dataMutationHistory.setTblEmployeeByIdemployee(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
//            dataMutationHistory.setMutationDate(Timestamp.valueOf(LocalDateTime.now()));
//            dataMutationHistory.setRefItemMutationType(session.find(RefItemMutationType.class, 0));
//            dataMutationHistory.setCreateDate(Timestamp.valueOf(LocalDateTime.now()));
//            dataMutationHistory.setTblEmployeeByCreateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
//            dataMutationHistory.setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
//            dataMutationHistory.setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
//            dataMutationHistory.setRefRecordStatus(session.find(RefRecordStatus.class, 1));
//            session.saveOrUpdate(dataMutationHistory);
//            //data item location (source, udpate:minus)
//                TblItemLocation tempItemSourceLocation = getItemLocationByIDLocationAndIDItem(dataMutationHistory.getTblLocationByIdlocationOfSource().getIdlocation(),
//                        dataMutationHistory.getTblItem().getIditem());
//                if (tempItemSourceLocation == null) {
//                    errMsg = "Item (" + dataMutationHistory.getTblItem().getItemName() + ") not found in Location..!";
//                    if (session.getTransaction().isActive()) {
//                        session.getTransaction().rollback();
//                    }
//                    return null;
//                }
//                tempItemSourceLocation.setItemQuantity(tempItemSourceLocation.getItemQuantity().subtract(dataMutationHistory.getItemQuantity()));
//                tempItemSourceLocation.setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
//                tempItemSourceLocation.setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
//                if (tempItemSourceLocation.getItemQuantity().compareTo(new BigDecimal("0") == -1) {   //data item quantity < 0
//                    errMsg = "Stock item (in warehouse) not enough for retur..!";
//                    if (session.getTransaction().isActive()) {
//                        session.getTransaction().rollback();
//                    }
//                    return null;
//                }
//                session.update(tempItemSourceLocation);
//            //data item location : room-card (destination, udpate:plus)
//            TblItemLocation tempItemDestinationLocation = getItemLocationByIDLocationAndIDItem(dataMutationHistory.getTblLocationByIdlocationOfDestination().getIdlocation(),
//                    dataMutationHistory.getTblItem().getIditem());
//            if (tempItemDestinationLocation != null) {    //update
//                tempItemDestinationLocation.setItemQuantity(tempItemDestinationLocation.getItemQuantity().add(dataMutationHistory.getItemQuantity()));
//                tempItemDestinationLocation.setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
//                tempItemDestinationLocation.setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
//                session.update(tempItemDestinationLocation);
//            } else {  //create
//                tempItemDestinationLocation = new TblItemLocation();
//                tempItemDestinationLocation.setTblItem(session.find(TblItem.class, dataMutationHistory.getTblItem().getIditem()));
//                tempItemDestinationLocation.setTblLocation(session.find(TblLocation.class, dataMutationHistory.getTblLocationByIdlocationOfDestination().getIdlocation()));
//                tempItemDestinationLocation.setItemQuantity(dataMutationHistory.getItemQuantity());
//                tempItemDestinationLocation.setCreateDate(Timestamp.valueOf(LocalDateTime.now()));
//                tempItemDestinationLocation.setTblEmployeeByCreateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
//                tempItemDestinationLocation.setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
//                tempItemDestinationLocation.setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
//                tempItemDestinationLocation.setRefRecordStatus(session.find(RefRecordStatus.class, 1));
//                session.saveOrUpdate(tempItemDestinationLocation);
//            }
                //data additional service (early checkin)
                reservationAdditionalService.setCreateDate(Timestamp.valueOf(LocalDateTime.now()));
                reservationAdditionalService.setTblEmployeeByCreateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                reservationAdditionalService.setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                reservationAdditionalService.setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                reservationAdditionalService.setRefRecordStatus(session.find(RefRecordStatus.class, 1));
                session.saveOrUpdate(reservationAdditionalService);
                //data log
                dataLog += "Data Tambahan Layanan : \n";
                //data log
                dataLog += "- " + ClassFormatter.dateFormate.format(reservationAdditionalService.getAdditionalDate()) + " - " + reservationAdditionalService.getTblRoomService().getIdroomService() + " \t "
                        + "Harga : " + ClassFormatter.currencyFormat.cFormat(reservationAdditionalService.getPrice()) + " \t "
                        + "Diskon : " + ClassFormatter.decimalFormat.cFormat(reservationAdditionalService.getDiscountPercentage()) + "% (" + getDataDiscountEvent(reservationAdditionalService) + ") \n";
                //data log
                LogSystem logSystem = new LogSystem();
                logSystem.setLogDateTime(Timestamp.valueOf(LocalDateTime.now()));
                logSystem.setTblSystemUser(session.find(TblSystemUser.class, ClassSession.currentUser.getIduser()));
                logSystem.setTblEmployee(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                logSystem.setLogHeader(headerLog);
                logSystem.setLogDetail(dataLog);
                logSystem.setHostName(ClassComp.getHostName());
                session.saveOrUpdate(logSystem);
                //open close company balance - detail (cashier)
                TblOpenCloseCashierBalance dataOpenCloseCashierBalance = getDataOpenCloseCashierBalanceIsActive();
                if (dataOpenCloseCashierBalance != null) {
                    TblOpenCloseCashierBalanceDetail dataOpenCloseCashierBalanceDetail = new TblOpenCloseCashierBalanceDetail();
                    dataOpenCloseCashierBalanceDetail.setTblOpenCloseCashierBalance(dataOpenCloseCashierBalance);
                    dataOpenCloseCashierBalanceDetail.setTblReservationPayment(dataReservationPayment);
                    dataOpenCloseCashierBalanceDetail.setDetailData(getDetailData(dataReservationPayment));
                    dataOpenCloseCashierBalanceDetail.setCreateDate(Timestamp.valueOf(LocalDateTime.now()));
                    dataOpenCloseCashierBalanceDetail.setTblEmployeeByCreateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                    dataOpenCloseCashierBalanceDetail.setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                    dataOpenCloseCashierBalanceDetail.setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                    dataOpenCloseCashierBalanceDetail.setRefRecordStatus(session.find(RefRecordStatus.class, 1));
                    session.saveOrUpdate(dataOpenCloseCashierBalanceDetail);
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
    public boolean updateDataReservationRoomTypeDetailLateCheckOut(
            TblReservation reservation,
            TblReservationRoomTypeDetail reservationRoomTypeDetail,
            TblReservationAdditionalService reservationAdditionalService,
            TblReservationBill reservationBill,
            BigDecimal reservationBillNominal) {
        errMsg = "";
        if (ClassSession.checkUserSession()) {  //check user current session
            try {
                session = HibernateUtil.getSessionFactory().getCurrentSession();
                session.beginTransaction();
                //data log
                String headerLog = "BUAT DATA LATE-CHECKOUT (RESERVASI)";
                String dataLog = "";
                //data reservation
                reservation.setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                reservation.setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                session.update(reservation);
                //data log
                dataLog += "No. Reservasi : " + reservation.getCodeReservation() + "\n";
                dataLog += "Tgl. Reservasi : " + ClassFormatter.dateFormate.format(reservation.getReservationDate()) + "\n";
                dataLog += "Nama Customer : " + reservation.getTblCustomer().getTblPeople().getFullName() + "\n";
                dataLog += "Travel Agent : " + (reservation.getTblPartner() != null ? reservation.getTblPartner().getPartnerName() : "-") + "\n";
                dataLog += "-----------------------------------------------------------------------\n";
                //data reservation checkinout(out)
                reservationRoomTypeDetail.getTblReservationCheckInOut().setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                reservationRoomTypeDetail.getTblReservationCheckInOut().setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                session.update(reservationRoomTypeDetail.getTblReservationCheckInOut());
                //data reservation room type detail
                reservationRoomTypeDetail.setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                reservationRoomTypeDetail.setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                session.update(reservationRoomTypeDetail);
                //data log
                dataLog += "Reservasi Kamar : \n";
                dataLog += "- " + reservationRoomTypeDetail.getCodeDetail() + " - " + reservationRoomTypeDetail.getCheckInDateTime() + " s/d " + reservationRoomTypeDetail.getCheckOutDateTime() + " \t "
                        + (getDataRoom(reservationRoomTypeDetail)) + " \n";
                dataLog += "-----------------------------------------------------------------------\n";
                //data room
                reservationRoomTypeDetail.getTblReservationCheckInOut().getTblRoom().setRefRoomStatus(session.get(RefRoomStatus.class, 4)); //Vacant Dirty = '4'
                //data room status change history
                TblRoomStatusChangeHistory dataRoomStatusChangeHistory = new TblRoomStatusChangeHistory();
                dataRoomStatusChangeHistory.setTblRoom(reservationRoomTypeDetail.getTblReservationCheckInOut().getTblRoom());
                dataRoomStatusChangeHistory.setTblEmployeeByIdemployee(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                dataRoomStatusChangeHistory.setTblRoomStatusDetail(null);
                dataRoomStatusChangeHistory.setChangeRoomStatusDate(Timestamp.valueOf(LocalDateTime.now()));
                dataRoomStatusChangeHistory.setRefRoomStatusByRoomStatusOld(getOldRoomStatus(reservationRoomTypeDetail.getTblReservationCheckInOut().getTblRoom().getIdroom()));
                dataRoomStatusChangeHistory.setRefRoomStatusByRoomStatusNew(reservationRoomTypeDetail.getTblReservationCheckInOut().getTblRoom().getRefRoomStatus());
                dataRoomStatusChangeHistory.setCreateDate(Timestamp.valueOf(LocalDateTime.now()));
                dataRoomStatusChangeHistory.setTblEmployeeByCreateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                dataRoomStatusChangeHistory.setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                dataRoomStatusChangeHistory.setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                dataRoomStatusChangeHistory.setRefRecordStatus(session.find(RefRecordStatus.class, 1));
                session.saveOrUpdate(dataRoomStatusChangeHistory);
                //data room
                reservationRoomTypeDetail.getTblReservationCheckInOut().getTblRoom().setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                reservationRoomTypeDetail.getTblReservationCheckInOut().getTblRoom().setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                session.update(reservationRoomTypeDetail.getTblReservationCheckInOut().getTblRoom());
                //data addtioanal item - reserved (release : 'done')
                List<TblReservationAdditionalItemReserved> dataReservationAdditionalItemReserveds = getAllReservationAdditionalItemReservedByIDReservationRoomTypeDetail(reservationRoomTypeDetail.getIddetail());
                for (TblReservationAdditionalItemReserved dataReservationAdditionalItemReserved : dataReservationAdditionalItemReserveds) {
                    dataReservationAdditionalItemReserved.setRefReservationAdditionalItemReservedStatus(session.find(RefReservationAdditionalItemReservedStatus.class, 2)); //Done = '2'
                    dataReservationAdditionalItemReserved.setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                    dataReservationAdditionalItemReserved.setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                    session.update(dataReservationAdditionalItemReserved);
                }
                //data reservation bill (deposit)
                TblReservationBill dataReservationBill = getReservationBillByIDReservationAndIDReservationBillType(reservation.getIdreservation(),
                        2); //Deposit = '2'
                if (dataReservationBill == null) {  //create and save reservation bill
                    dataReservationBill = new TblReservationBill();
                    dataReservationBill.setCodeBill(ClassCoder.getCode("Reservation Bill", session));
                    dataReservationBill.setTblReservation(reservation);
                    dataReservationBill.setServiceChargePercentage(new BigDecimal("0"));
                    dataReservationBill.setTaxPercentage(new BigDecimal("0"));
                    dataReservationBill.setRefReservationBillType(session.find(RefReservationBillType.class, 2));
                    dataReservationBill.setCreateDate(Timestamp.valueOf(LocalDateTime.now()));
                    dataReservationBill.setTblEmployeeByCreateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                    dataReservationBill.setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                    dataReservationBill.setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                    dataReservationBill.setRefRecordStatus(session.find(RefRecordStatus.class, 1));
                    session.saveOrUpdate(dataReservationBill);
                }
                //data reservation payment (close-deposit)
                TblReservationPayment dataReservationPayment = new TblReservationPayment();
                dataReservationPayment.setCodePayment(ClassCoder.getCode("Reservation Payment", session));
                dataReservationPayment.setTblReservationBill(dataReservationBill);
                dataReservationPayment.setPaymentDate(Timestamp.valueOf(LocalDateTime.now()));
//            dataReservationPayment.setUnitNominal((reservationRoomTypeDetail.getTblReservationCheckInOut().getCardUsed()
//                    - reservationRoomTypeDetail.getTblReservationCheckInOut().getCardMissed())
//                    * reservationRoomTypeDetail.getTblReservationCheckInOut().getBrokenCardCharge());
                dataReservationPayment.setUnitNominal((new BigDecimal(reservationRoomTypeDetail.getTblReservationCheckInOut().getCardUsed()))
                        .multiply(reservationRoomTypeDetail.getTblReservationCheckInOut().getBrokenCardCharge()));
                dataReservationPayment.setRoundingValue(new BigDecimal("0"));
                dataReservationPayment.setTblEmployeeByIdcashier(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                dataReservationPayment.setRefFinanceTransactionPaymentType(session.find(RefFinanceTransactionPaymentType.class, 14));
                dataReservationPayment.setRefReservationBillType(session.find(RefReservationBillType.class, 2));    //deposit = '2'
                dataReservationPayment.setSettleDate(Timestamp.valueOf(LocalDateTime.now()));
                dataReservationPayment.setCreateDate(Timestamp.valueOf(LocalDateTime.now()));
                dataReservationPayment.setTblEmployeeByCreateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                dataReservationPayment.setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                dataReservationPayment.setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                dataReservationPayment.setRefRecordStatus(session.find(RefRecordStatus.class, 1));
                session.saveOrUpdate(dataReservationPayment);
                //data log
                dataLog += "Pengembalian Deposit Kartu : " + ClassFormatter.currencyFormat.cFormat(dataReservationPayment.getUnitNominal())
                        + " (" + reservationRoomTypeDetail.getTblReservationCheckInOut().getCardUsed()
                        + " kartu @" + ClassFormatter.currencyFormat.cFormat(reservationRoomTypeDetail.getTblReservationCheckInOut().getBrokenCardCharge()) + ")\n";
                dataLog += "-----------------------------------------------------------------------\n";
                //data reservation payment with deposit
                TblReservationPaymentWithDeposit dataReservationPaymentWithDeposit = new TblReservationPaymentWithDeposit();
                dataReservationPaymentWithDeposit.setTblReservationPayment(dataReservationPayment);
                dataReservationPaymentWithDeposit.setTblReservationCheckInOut(reservationRoomTypeDetail.getTblReservationCheckInOut());
                dataReservationPaymentWithDeposit.setCreateDate(Timestamp.valueOf(LocalDateTime.now()));
                dataReservationPaymentWithDeposit.setTblEmployeeByCreateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                dataReservationPaymentWithDeposit.setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                dataReservationPaymentWithDeposit.setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                dataReservationPaymentWithDeposit.setRefRecordStatus(session.find(RefRecordStatus.class, 1));
                session.saveOrUpdate(dataReservationPaymentWithDeposit);
                //@@@%%%
                //data deposit balance (update:-)
                TblCompanyBalance dataBalance = session.find(TblCompanyBalance.class, (long) 4); //Kas Deposit = '4'
                dataBalance.setBalanceNominal(dataBalance.getBalanceNominal().subtract(dataReservationPayment.getUnitNominal()));
                dataBalance.setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                dataBalance.setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                session.update(dataBalance);
                //data company balance (cash-flow log)
                LogCompanyBalanceCashFlow logCompanyBalanceCashFlow = new LogCompanyBalanceCashFlow();
                logCompanyBalanceCashFlow.setTblCompanyBalanceByIdsenderCompanyBalance(dataBalance);
                logCompanyBalanceCashFlow.setTblBankAccountByIdsenderCbbankAccount(null);
                logCompanyBalanceCashFlow.setTblCompanyBalanceByIdreceiverCompanyBalance(null);
                logCompanyBalanceCashFlow.setTblBankAccountByIdreceiverCbbankAccount(null);
                logCompanyBalanceCashFlow.setTransferNominal(dataReservationPayment.getUnitNominal());
                logCompanyBalanceCashFlow.setHistoryDate(Timestamp.valueOf(LocalDateTime.now()));
                logCompanyBalanceCashFlow.setTblEmployee(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                logCompanyBalanceCashFlow.setHistoryStystemNote("Pengembalian deposit kartu kamar kepada " + reservation.getTblCustomer().getTblPeople().getFullName() + ", \nsebesar : "
                        + ClassFormatter.currencyFormat.format(dataReservationPayment.getUnitNominal()) + " ("
                        + dataReservationPayment.getRefFinanceTransactionPaymentType().getTypeName() + ")");
                session.saveOrUpdate(logCompanyBalanceCashFlow);
                //data company balance (cashier)
                TblCompanyBalance dataCashierBalance = session.find(TblCompanyBalance.class, (long) 3); //Kas Kasir = '3'
                //open close company balance
                TblOpenCloseCashierBalance dataOpenCloseCashierBalance = getDataOpenCloseCashierBalanceIsActive();
                //open close company balance - detail (cashier)
                if (dataOpenCloseCashierBalance != null) {
                    TblOpenCloseCashierBalanceDetail dataOpenCloseCashierBalanceDetail = new TblOpenCloseCashierBalanceDetail();
                    dataOpenCloseCashierBalanceDetail.setTblOpenCloseCashierBalance(dataOpenCloseCashierBalance);
                    dataOpenCloseCashierBalanceDetail.setTblReservationPayment(dataReservationPayment);
                    dataOpenCloseCashierBalanceDetail.setDetailData(getDetailData(dataReservationPayment));
                    dataOpenCloseCashierBalanceDetail.setCreateDate(Timestamp.valueOf(LocalDateTime.now()));
                    dataOpenCloseCashierBalanceDetail.setTblEmployeeByCreateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                    dataOpenCloseCashierBalanceDetail.setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                    dataOpenCloseCashierBalanceDetail.setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                    dataOpenCloseCashierBalanceDetail.setRefRecordStatus(session.find(RefRecordStatus.class, 1));
                    session.saveOrUpdate(dataOpenCloseCashierBalanceDetail);
                }
                //if missed card number > 0
                if (reservationRoomTypeDetail.getTblReservationCheckInOut().getCardMissed() > 0) {
                    //data reservation payment (cash)
                    TblReservationPayment dataReservationPaymentForMissedCard = new TblReservationPayment();
                    dataReservationPaymentForMissedCard.setCodePayment(ClassCoder.getCode("Reservation Payment", session));
                    dataReservationPaymentForMissedCard.setPaymentDate(Timestamp.valueOf(LocalDateTime.now()));
                    dataReservationPaymentForMissedCard.setUnitNominal((new BigDecimal(reservationRoomTypeDetail.getTblReservationCheckInOut().getCardMissed()))
                            .multiply(reservationRoomTypeDetail.getTblReservationCheckInOut().getBrokenCardCharge()));
                    dataReservationPaymentForMissedCard.setRoundingValue(new BigDecimal("0"));
                    dataReservationPaymentForMissedCard.setTblEmployeeByIdcashier(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                    dataReservationPaymentForMissedCard.setRefFinanceTransactionPaymentType(session.find(RefFinanceTransactionPaymentType.class, 0));
                    dataReservationPaymentForMissedCard.setRefReservationBillType(session.find(RefReservationBillType.class, 2));   //deposit = '2'
                    dataReservationPaymentForMissedCard.setCreateDate(Timestamp.valueOf(LocalDateTime.now()));
                    dataReservationPaymentForMissedCard.setTblEmployeeByCreateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                    dataReservationPaymentForMissedCard.setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                    dataReservationPaymentForMissedCard.setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                    dataReservationPaymentForMissedCard.setRefRecordStatus(session.find(RefRecordStatus.class, 1));
                    session.saveOrUpdate(dataReservationPaymentForMissedCard);
                    //@@@%%%
                    //data cashier balance (update:+)
                    dataCashierBalance.setBalanceNominal(dataCashierBalance.getBalanceNominal().add(dataReservationPaymentForMissedCard.getUnitNominal()));
                    dataCashierBalance.setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                    dataCashierBalance.setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                    session.update(dataCashierBalance);
                    //data item : guest card (perlengkapan - guest)
                    TblItem itemGuestCard;
                    SysDataHardCode sdhItemGuestCard = session.find(SysDataHardCode.class, (long) 1);   //IDGuestCard = '1'
                    if (sdhItemGuestCard == null
                            || sdhItemGuestCard.getDataHardCodeValue() == null) {
                        if (session.getTransaction().isActive()) {
                            session.getTransaction().rollback();
                        }
                        errMsg = "Data Kartu Kamar - Tidak Ditemukan..!";
                        return false;
                    } else {
                        itemGuestCard = session.find(TblItem.class, Long.valueOf(sdhItemGuestCard.getDataHardCodeValue()));
                        if (itemGuestCard == null) {
                            if (session.getTransaction().isActive()) {
                                session.getTransaction().rollback();
                            }
                            errMsg = "Data Kartu Kamar - Tidak Ditemukan..!";
                            return false;
                        }
                    }
                    //data location : front offce
                    TblLocation locationFO;
                    SysDataHardCode sdhLocationFrontOffice = session.find(SysDataHardCode.class, (long) 2);   //IDFrontOffice = '2'
                    if (sdhLocationFrontOffice == null
                            || sdhLocationFrontOffice.getDataHardCodeValue() == null) {
                        if (session.getTransaction().isActive()) {
                            session.getTransaction().rollback();
                        }
                        errMsg = "Data Lokasi Front Office - Tidak Ditemukan..!";
                        return false;
                    } else {
                        locationFO = session.find(TblLocation.class, Long.valueOf(sdhLocationFrontOffice.getDataHardCodeValue()));
                        if (locationFO == null) {
                            if (session.getTransaction().isActive()) {
                                session.getTransaction().rollback();
                            }
                            errMsg = "Data Lokasi Front Office - Tidak Ditemukan..!";
                            return false;
                        }
                    }
                    //data item-location : guest card - front office
                    TblItemLocation itemLocation = getItemLocationByIDLocationAndIDItem(locationFO.getIdlocation(), itemGuestCard.getIditem());
                    if (itemLocation == null
                            || reservationRoomTypeDetail.getTblReservationCheckInOut().getCardMissed() > itemLocation.getItemQuantity().intValue()) {
                        if (session.getTransaction().isActive()) {
                            session.getTransaction().rollback();
                        }
                        errMsg = "Stok Kartu Kamar - Tidak Cukup..!";
                        return false;
                    }
                    TblLocationOfBin bin = getBin();
                    if (bin != null) {
                        if (itemLocation.getItemQuantity()
                                .compareTo(new BigDecimal("0")) == 1) {
                            //data item mutation history
                            TblItemMutationHistory dataMutation = new TblItemMutationHistory();
                            dataMutation.setItemQuantity(new BigDecimal(String.valueOf(reservationRoomTypeDetail.getTblReservationCheckInOut().getCardMissed())));
                            dataMutation.setTblItem(itemLocation.getTblItem());
                            dataMutation.setTblLocationByIdlocationOfSource(itemLocation.getTblLocation());
                            dataMutation.setTblLocationByIdlocationOfDestination(bin.getTblLocation());
                            dataMutation.setMutationDate(Timestamp.valueOf(LocalDateTime.now()));
//                            dataMutation.setTblEmployeeByIdemployee(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                            dataMutation.setRefItemMutationType(session.find(RefItemMutationType.class, 2));
                            dataMutation.setRefItemObsoleteBy(session.find(RefItemObsoleteBy.class, 0));
//                            dataMutation.setTblEmployeeByIdemployee(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                            dataMutation.setCreateDate(Timestamp.valueOf(LocalDateTime.now()));
                            dataMutation.setTblEmployeeByCreateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                            dataMutation.setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                            dataMutation.setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                            dataMutation.setRefRecordStatus(session.find(RefRecordStatus.class, 1));
                            session.saveOrUpdate(dataMutation);
                            //data item location (source)
                            itemLocation.setItemQuantity(itemLocation.getItemQuantity().subtract(dataMutation.getItemQuantity()));
                            itemLocation.setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                            itemLocation.setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                            session.update(itemLocation);
                            //data item location (destination) : bin
                            TblItemLocation destinationItemLocation = getItemLocationByIDLocationAndIDItem(dataMutation.getTblLocationByIdlocationOfDestination().getIdlocation(),
                                    dataMutation.getTblItem().getIditem());
                            if (destinationItemLocation == null) {
                                destinationItemLocation = new TblItemLocation();
                                destinationItemLocation.setTblItem(itemLocation.getTblItem());
                                destinationItemLocation.setTblLocation(getBin().getTblLocation());
                                destinationItemLocation.setItemQuantity(new BigDecimal("0"));
                                destinationItemLocation.setCreateDate(Timestamp.valueOf(LocalDateTime.now()));
                                destinationItemLocation.setTblEmployeeByCreateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                                destinationItemLocation.setRefRecordStatus(session.find(RefRecordStatus.class, 1));
                            }
                            destinationItemLocation.setItemQuantity(destinationItemLocation.getItemQuantity().add(dataMutation.getItemQuantity()));
                            destinationItemLocation.setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                            destinationItemLocation.setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                            session.saveOrUpdate(destinationItemLocation);
                        }
                        session.evict(bin);
                    } else {
                        if (session.getTransaction().isActive()) {
                            session.getTransaction().rollback();
                        }
                        errMsg = "Data Bin - Tidak Ditemukan..!";
                        return false;
                    }
                    //data log
                    dataLog += "Denda Kartu Hilang/Rusak : " + ClassFormatter.currencyFormat.cFormat(dataReservationPaymentForMissedCard.getUnitNominal())
                            + " (" + reservationRoomTypeDetail.getTblReservationCheckInOut().getCardMissed()
                            + " kartu @" + ClassFormatter.currencyFormat.cFormat(reservationRoomTypeDetail.getTblReservationCheckInOut().getBrokenCardCharge()) + ")\n";
                    dataLog += "-----------------------------------------------------------------------\n";
                    dataLog += "Kurangi Stok Kartu : " + reservationRoomTypeDetail.getTblReservationCheckInOut().getCardMissed() + "(jumlah kartu) \n"
                            + "- Data Kartu : " + itemGuestCard.getCodeItem() + " - " + itemGuestCard.getItemName() + " \n"
                            + "- Data Lokasi Kartu : " + getDataLocationOfWarehouseName(locationFO) + " \n";
                    dataLog += "-----------------------------------------------------------------------\n";
                    //open close company balance - detail (cashier)
                    if (dataOpenCloseCashierBalance != null) {
                        TblOpenCloseCashierBalanceDetail dataOpenCloseCashierBalanceDetailMissedCard = new TblOpenCloseCashierBalanceDetail();
                        dataOpenCloseCashierBalanceDetailMissedCard.setTblOpenCloseCashierBalance(dataOpenCloseCashierBalance);
                        dataOpenCloseCashierBalanceDetailMissedCard.setTblReservationPayment(dataReservationPaymentForMissedCard);
                        dataOpenCloseCashierBalanceDetailMissedCard.setDetailData(getDetailData(dataReservationPaymentForMissedCard));
                        dataOpenCloseCashierBalanceDetailMissedCard.setCreateDate(Timestamp.valueOf(LocalDateTime.now()));
                        dataOpenCloseCashierBalanceDetailMissedCard.setTblEmployeeByCreateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                        dataOpenCloseCashierBalanceDetailMissedCard.setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                        dataOpenCloseCashierBalanceDetailMissedCard.setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                        dataOpenCloseCashierBalanceDetailMissedCard.setRefRecordStatus(session.find(RefRecordStatus.class, 1));
                        session.saveOrUpdate(dataOpenCloseCashierBalanceDetailMissedCard);
                    }
                }
                //data additional service (late checkout)
                reservationAdditionalService.setCreateDate(Timestamp.valueOf(LocalDateTime.now()));
                reservationAdditionalService.setTblEmployeeByCreateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                reservationAdditionalService.setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                reservationAdditionalService.setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                reservationAdditionalService.setRefRecordStatus(session.find(RefRecordStatus.class, 1));
                session.saveOrUpdate(reservationAdditionalService);
                //data log
                LogSystem logSystem = new LogSystem();
                logSystem.setLogDateTime(Timestamp.valueOf(LocalDateTime.now()));
                logSystem.setTblSystemUser(session.find(TblSystemUser.class, ClassSession.currentUser.getIduser()));
                logSystem.setTblEmployee(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                logSystem.setLogHeader(headerLog);
                logSystem.setLogDetail(dataLog);
                logSystem.setHostName(ClassComp.getHostName());
                session.saveOrUpdate(logSystem);
                //123456
                if (isAllCheckOut(reservation)
                        && (reservationBillNominal
                        .compareTo(new BigDecimal("0")) < 1)) {
                    //data transaction return (auto, -balance cashier balance)
                    if (reservationBillNominal
                            .compareTo(new BigDecimal("0")) == -1) {
                        //data reservation payment (return payment)
                        TblReservationPayment dataReservationReturnPayment = new TblReservationPayment();
                        dataReservationReturnPayment.setCodePayment(ClassCoder.getCode("Reservation Payment", session));
                        dataReservationReturnPayment.setTblReservationBill(reservationBill);
                        dataReservationReturnPayment.setPaymentDate(Timestamp.valueOf(LocalDateTime.now()));
                        dataReservationReturnPayment.setUnitNominal(getNominalAfterRounding(reservationBillNominal.abs()));
                        dataReservationReturnPayment.setRoundingValue(getRoundingValue(reservationBillNominal.abs()));
                        dataReservationReturnPayment.setTblEmployeeByIdcashier(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                        dataReservationReturnPayment.setRefFinanceTransactionPaymentType(session.find(RefFinanceTransactionPaymentType.class, 16));     //Return = '16'
                        dataReservationReturnPayment.setRefReservationBillType(session.find(RefReservationBillType.class, 0));    //reservasi = '0'
                        dataReservationReturnPayment.setSettleDate(Timestamp.valueOf(LocalDateTime.now()));     //auto settle
                        dataReservationReturnPayment.setCreateDate(Timestamp.valueOf(LocalDateTime.now()));
                        dataReservationReturnPayment.setTblEmployeeByCreateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                        dataReservationReturnPayment.setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                        dataReservationReturnPayment.setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                        dataReservationReturnPayment.setRefRecordStatus(session.find(RefRecordStatus.class, 1));
                        session.saveOrUpdate(dataReservationReturnPayment);
                        //data log
                        dataLog += "Pengembalian Pembayaran : " + ClassFormatter.currencyFormat.cFormat(dataReservationReturnPayment.getUnitNominal()) + ")\n";
                        dataLog += "-----------------------------------------------------------------------\n";
                        //open close company balance - detail (cashier)
                        if (dataOpenCloseCashierBalance != null) {
                            TblOpenCloseCashierBalanceDetail dataOpenCloseCashierBalanceDetail = new TblOpenCloseCashierBalanceDetail();
                            dataOpenCloseCashierBalanceDetail.setTblOpenCloseCashierBalance(dataOpenCloseCashierBalance);
                            dataOpenCloseCashierBalanceDetail.setTblReservationPayment(dataReservationReturnPayment);
                            dataOpenCloseCashierBalanceDetail.setDetailData(getDetailData(dataReservationReturnPayment));
                            dataOpenCloseCashierBalanceDetail.setCreateDate(Timestamp.valueOf(LocalDateTime.now()));
                            dataOpenCloseCashierBalanceDetail.setTblEmployeeByCreateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                            dataOpenCloseCashierBalanceDetail.setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                            dataOpenCloseCashierBalanceDetail.setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                            dataOpenCloseCashierBalanceDetail.setRefRecordStatus(session.find(RefRecordStatus.class, 1));
                            session.saveOrUpdate(dataOpenCloseCashierBalanceDetail);
                        }
                        //data company balance (cashier-)
                        dataCashierBalance.setBalanceNominal(dataCashierBalance.getBalanceNominal().subtract(dataReservationReturnPayment.getUnitNominal()));
                        dataCashierBalance.setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                        dataCashierBalance.setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                        session.update(dataCashierBalance);
                        //data company balance (cash-flow log)
                        LogCompanyBalanceCashFlow returnLogCompanyBalanceCashFlow = new LogCompanyBalanceCashFlow();
                        returnLogCompanyBalanceCashFlow.setTblCompanyBalanceByIdsenderCompanyBalance(dataCashierBalance);
                        returnLogCompanyBalanceCashFlow.setTblBankAccountByIdsenderCbbankAccount(null);
                        returnLogCompanyBalanceCashFlow.setTblCompanyBalanceByIdreceiverCompanyBalance(null);
                        returnLogCompanyBalanceCashFlow.setTblBankAccountByIdreceiverCbbankAccount(null);
                        returnLogCompanyBalanceCashFlow.setTransferNominal(dataReservationReturnPayment.getUnitNominal());
                        returnLogCompanyBalanceCashFlow.setHistoryDate(Timestamp.valueOf(LocalDateTime.now()));
                        returnLogCompanyBalanceCashFlow.setTblEmployee(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                        returnLogCompanyBalanceCashFlow.setHistoryStystemNote("Pengembalian pembayaran kepada " + reservation.getTblCustomer().getTblPeople().getFullName() + ", \nsebesar : "
                                + ClassFormatter.currencyFormat.format(dataReservationReturnPayment.getUnitNominal()) + " ("
                                + dataReservationReturnPayment.getRefFinanceTransactionPaymentType().getTypeName() + ")");
                        session.saveOrUpdate(returnLogCompanyBalanceCashFlow);
                    }
                    //data reservation (update:checkout)
                    reservation.setDepartureTime(Timestamp.valueOf(LocalDateTime.now()));
                    reservation.setRefReservationStatus(session.find(RefReservationStatus.class, 5));   //Checked Out = '5'
                    reservation.setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                    reservation.setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                    session.update(reservation);
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
    public boolean updateDataReservationRoomTypeDetailCheckInAddCardUsedNumber(
            TblReservation reservation,
            TblReservationRoomTypeDetail reservationRoomTypeDetail,
            int additionalCardUsedNumber) {
        errMsg = "";
        if (ClassSession.checkUserSession()) {  //check user current session
            try {
                session = HibernateUtil.getSessionFactory().getCurrentSession();
                session.beginTransaction();
                //data log
                String headerLog = "BUAT DATA TAMBAHAN KARTU KAMAR (RESERVASI)";
                String dataLog = "";
                //data reservation
                reservation.setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                reservation.setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                session.update(reservation);
                //data log
                dataLog += "No. Reservasi : " + reservation.getCodeReservation() + "\n";
                dataLog += "Tgl. Reservasi : " + ClassFormatter.dateFormate.format(reservation.getReservationDate()) + "\n";
                dataLog += "Nama Customer : " + reservation.getTblCustomer().getTblPeople().getFullName() + "\n";
                dataLog += "Travel Agent : " + (reservation.getTblPartner() != null ? reservation.getTblPartner().getPartnerName() : "-") + "\n";
                dataLog += "-----------------------------------------------------------------------\n";
                //data reservation checkinout (+ additional card used number)
                reservationRoomTypeDetail.getTblReservationCheckInOut().setCardUsed(reservationRoomTypeDetail.getTblReservationCheckInOut().getCardUsed()
                        + additionalCardUsedNumber);
                reservationRoomTypeDetail.getTblReservationCheckInOut().setCreateDate(Timestamp.valueOf(LocalDateTime.now()));
                reservationRoomTypeDetail.getTblReservationCheckInOut().setTblEmployeeByCreateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                reservationRoomTypeDetail.getTblReservationCheckInOut().setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                reservationRoomTypeDetail.getTblReservationCheckInOut().setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                reservationRoomTypeDetail.getTblReservationCheckInOut().setRefRecordStatus(session.find(RefRecordStatus.class, 1));
                session.saveOrUpdate(reservationRoomTypeDetail.getTblReservationCheckInOut());
                //data reservation room type detail
                reservationRoomTypeDetail.setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                reservationRoomTypeDetail.setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                session.update(reservationRoomTypeDetail);
                //data log
                dataLog += "Reservasi Kamar : \n";
                dataLog += "- " + reservationRoomTypeDetail.getCodeDetail() + " - " + reservationRoomTypeDetail.getCheckInDateTime() + " s/d " + reservationRoomTypeDetail.getCheckOutDateTime() + " \t "
                        + (getDataRoom(reservationRoomTypeDetail)) + " \n";
                dataLog += "-----------------------------------------------------------------------\n";
                //data reservation bill (deposit)
                TblReservationBill dataReservationBill = getReservationBillByIDReservationAndIDReservationBillType(reservation.getIdreservation(),
                        2); //Deposit = '2'
                if (dataReservationBill == null) {  //create and save reservation bill
                    dataReservationBill = new TblReservationBill();
                    dataReservationBill.setCodeBill(ClassCoder.getCode("Reservation Bill", session));
                    dataReservationBill.setTblReservation(reservation);
                    dataReservationBill.setServiceChargePercentage(new BigDecimal("0"));
                    dataReservationBill.setTaxPercentage(new BigDecimal("0"));
                    dataReservationBill.setRefReservationBillType(session.find(RefReservationBillType.class, 2));
                    dataReservationBill.setCreateDate(Timestamp.valueOf(LocalDateTime.now()));
                    dataReservationBill.setTblEmployeeByCreateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                    dataReservationBill.setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                    dataReservationBill.setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                    dataReservationBill.setRefRecordStatus(session.find(RefRecordStatus.class, 1));
                    session.saveOrUpdate(dataReservationBill);
                }
                //data reservation payment (open-deposit)
                TblReservationPayment dataReservationPayment = new TblReservationPayment();
                dataReservationPayment.setCodePayment(ClassCoder.getCode("Reservation Payment", session));
                dataReservationPayment.setTblReservationBill(dataReservationBill);
                dataReservationPayment.setPaymentDate(Timestamp.valueOf(LocalDateTime.now()));
                dataReservationPayment.setUnitNominal((new BigDecimal(additionalCardUsedNumber))
                        .multiply(reservationRoomTypeDetail.getTblReservationCheckInOut().getBrokenCardCharge()));
                dataReservationPayment.setRoundingValue(new BigDecimal("0"));
                dataReservationPayment.setTblEmployeeByIdcashier(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                dataReservationPayment.setRefFinanceTransactionPaymentType(session.find(RefFinanceTransactionPaymentType.class, 13));
                dataReservationPayment.setRefReservationBillType(session.find(RefReservationBillType.class, 2));
                dataReservationPayment.setSettleDate(Timestamp.valueOf(LocalDateTime.now()));
                dataReservationPayment.setCreateDate(Timestamp.valueOf(LocalDateTime.now()));
                dataReservationPayment.setTblEmployeeByCreateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                dataReservationPayment.setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                dataReservationPayment.setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                dataReservationPayment.setRefRecordStatus(session.find(RefRecordStatus.class, 1));
                session.saveOrUpdate(dataReservationPayment);
                //data log
                dataLog += "Deposit Kartu : " + ClassFormatter.currencyFormat.cFormat(dataReservationPayment.getUnitNominal())
                        + " (" + reservationRoomTypeDetail.getTblReservationCheckInOut().getCardUsed()
                        + " kartu @" + ClassFormatter.currencyFormat.cFormat(reservationRoomTypeDetail.getTblReservationCheckInOut().getBrokenCardCharge()) + ")\n";
                dataLog += "-----------------------------------------------------------------------\n";
                //data reservation payment with deposit
                TblReservationPaymentWithDeposit dataReservationPaymentWithDeposit = new TblReservationPaymentWithDeposit();
                dataReservationPaymentWithDeposit.setTblReservationPayment(dataReservationPayment);
                dataReservationPaymentWithDeposit.setTblReservationCheckInOut(reservationRoomTypeDetail.getTblReservationCheckInOut());
                dataReservationPaymentWithDeposit.setCreateDate(Timestamp.valueOf(LocalDateTime.now()));
                dataReservationPaymentWithDeposit.setTblEmployeeByCreateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                dataReservationPaymentWithDeposit.setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                dataReservationPaymentWithDeposit.setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                dataReservationPaymentWithDeposit.setRefRecordStatus(session.find(RefRecordStatus.class, 1));
                session.saveOrUpdate(dataReservationPaymentWithDeposit);
                //@@@%%%
                //data deposit balance (update:+)
                TblCompanyBalance dataBalance = session.find(TblCompanyBalance.class, (long) 4);
                dataBalance.setBalanceNominal(dataBalance.getBalanceNominal().add(dataReservationPayment.getUnitNominal()));
                dataBalance.setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                dataBalance.setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                session.update(dataBalance);
                //data company balance (cash-flow log)
                LogCompanyBalanceCashFlow logCompanyBalanceCashFlow = new LogCompanyBalanceCashFlow();
                logCompanyBalanceCashFlow.setTblCompanyBalanceByIdsenderCompanyBalance(null);
                logCompanyBalanceCashFlow.setTblBankAccountByIdsenderCbbankAccount(null);
                logCompanyBalanceCashFlow.setTblCompanyBalanceByIdreceiverCompanyBalance(dataBalance);
                logCompanyBalanceCashFlow.setTblBankAccountByIdreceiverCbbankAccount(null);
                logCompanyBalanceCashFlow.setTransferNominal(dataReservationPayment.getUnitNominal());
                logCompanyBalanceCashFlow.setHistoryDate(Timestamp.valueOf(LocalDateTime.now()));
                logCompanyBalanceCashFlow.setTblEmployee(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                logCompanyBalanceCashFlow.setHistoryStystemNote("Deposit kartu kamar oleh " + reservation.getTblCustomer().getTblPeople().getFullName() + ", \nsebesar : "
                        + ClassFormatter.currencyFormat.format(dataReservationPayment.getUnitNominal()) + " ("
                        + dataReservationPayment.getRefFinanceTransactionPaymentType().getTypeName() + ")");
                session.saveOrUpdate(logCompanyBalanceCashFlow);
                //data log
                LogSystem logSystem = new LogSystem();
                logSystem.setLogDateTime(Timestamp.valueOf(LocalDateTime.now()));
                logSystem.setTblSystemUser(session.find(TblSystemUser.class, ClassSession.currentUser.getIduser()));
                logSystem.setTblEmployee(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                logSystem.setLogHeader(headerLog);
                logSystem.setLogDetail(dataLog);
                logSystem.setHostName(ClassComp.getHostName());
                session.saveOrUpdate(logSystem);
                //open close company balance - detail (cashier)
                TblOpenCloseCashierBalance dataOpenCloseCashierBalance = getDataOpenCloseCashierBalanceIsActive();
                if (dataOpenCloseCashierBalance != null) {
                    TblOpenCloseCashierBalanceDetail dataOpenCloseCashierBalanceDetail = new TblOpenCloseCashierBalanceDetail();
                    dataOpenCloseCashierBalanceDetail.setTblOpenCloseCashierBalance(dataOpenCloseCashierBalance);
                    dataOpenCloseCashierBalanceDetail.setTblReservationPayment(dataReservationPayment);
                    dataOpenCloseCashierBalanceDetail.setDetailData(getDetailData(dataReservationPayment));
                    dataOpenCloseCashierBalanceDetail.setCreateDate(Timestamp.valueOf(LocalDateTime.now()));
                    dataOpenCloseCashierBalanceDetail.setTblEmployeeByCreateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                    dataOpenCloseCashierBalanceDetail.setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                    dataOpenCloseCashierBalanceDetail.setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                    dataOpenCloseCashierBalanceDetail.setRefRecordStatus(session.find(RefRecordStatus.class, 1));
                    session.saveOrUpdate(dataOpenCloseCashierBalanceDetail);
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
    public boolean updateDataReservationBillDiscountType(
            TblReservation reservation,
            TblReservationBill reservationBill,
            List<TblReservationRoomTypeDetail> rrtds,
            List<TblReservationRoomTypeDetailRoomPriceDetail> rrtdrpds,
            List<TblReservationRoomTypeDetailTravelAgentDiscountDetail> rrtdtadds,
            List<TblReservationAdditionalItem> rais,
            List<TblReservationAdditionalService> rass) {
        errMsg = "";
        if (ClassSession.checkUserSession()) {  //check user current session
            try {
                session = HibernateUtil.getSessionFactory().getCurrentSession();
                session.beginTransaction();
                //data log
                String headerLog = "UBAH DATA TIPE DISKON (RESERVASI)";
                String dataLog = "";
                //data reservation
                reservation.setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                reservation.setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                session.update(reservation);
                //data log
                dataLog += "No. Reservasi : " + reservation.getCodeReservation() + "\n";
                dataLog += "Tgl. Reservasi : " + ClassFormatter.dateFormate.format(reservation.getReservationDate()) + "\n";
                dataLog += "Nama Customer : " + reservation.getTblCustomer().getTblPeople().getFullName() + "\n";
                dataLog += "Travel Agent : " + (reservation.getTblPartner() != null ? reservation.getTblPartner().getPartnerName() : "-") + "\n";
                dataLog += "-----------------------------------------------------------------------\n";
                //data reservation-bill            
                reservationBill.setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                reservationBill.setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                session.update(reservationBill);
                //data log
                dataLog += "No. Tagihan (" + reservationBill.getRefReservationBillType().getTypeName() + ") : " + reservationBill.getCodeBill() + "\n";
                dataLog += "Service Charge (%) : " + ClassFormatter.decimalFormat.cFormat((reservationBill.getServiceChargePercentage().multiply(new BigDecimal("100")))) + "% \n";
                dataLog += "Pajak (%) : " + ClassFormatter.decimalFormat.cFormat((reservationBill.getTaxPercentage().multiply(new BigDecimal("100")))) + "% \n";
                dataLog += "Tipe Diskon : " + (reservationBill.getRefReservationBillDiscountType() != null ? reservationBill.getRefReservationBillDiscountType().getTypeName() : "-") + " \n";
                dataLog += "-----------------------------------------------------------------------\n";
                //data reservation room type detail
                for (TblReservationRoomTypeDetail rrtd : rrtds) {
                    rrtd.setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                    rrtd.setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                    session.saveOrUpdate(rrtd);
                }
                //data reservation room type detail - room price detail
                for (TblReservationRoomTypeDetailRoomPriceDetail rrtdrpd : rrtdrpds) {
                    //data reservation room price detail
                    rrtdrpd.getTblReservationRoomPriceDetail().setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                    rrtdrpd.getTblReservationRoomPriceDetail().setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                    session.update(rrtdrpd.getTblReservationRoomPriceDetail());
                    //data reservation room type detail - room price detail
                    rrtdrpd.setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                    rrtdrpd.setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                    session.update(rrtdrpd);
                }
                //data reservation room type detail - travel agent discount detail
                for (TblReservationRoomTypeDetailTravelAgentDiscountDetail rrtdtadd : rrtdtadds) {
                    //data reservation travel agent discount detail
                    rrtdtadd.getTblReservationTravelAgentDiscountDetail().setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                    rrtdtadd.getTblReservationTravelAgentDiscountDetail().setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                    session.update(rrtdtadd.getTblReservationTravelAgentDiscountDetail());
                    //data reservation room type detail - travel agent discount detail
                    rrtdtadd.setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                    rrtdtadd.setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                    session.update(rrtdtadd);
                }
                //data log
                dataLog += "Reservasi Kamar : \n";
                for (TblReservationRoomTypeDetail rrtd : rrtds) {
                    dataLog += "- " + rrtd.getCodeDetail() + " - " + rrtd.getCheckInDateTime() + " s/d " + rrtd.getCheckOutDateTime() + " \t "
                            + (getDataRoom(rrtd)) + " \n";
                    for (TblReservationRoomTypeDetailRoomPriceDetail rrtdrpd : rrtdrpds) {
                        if (rrtdrpd.getTblReservationRoomTypeDetail().getIddetail()
                                == rrtd.getIddetail()) {
                            dataLog += "\t - " + ClassFormatter.dateFormate.format(rrtdrpd.getTblReservationRoomPriceDetail().getDetailDate()) + " \t "
                                    + "Harga : " + ClassFormatter.currencyFormat.cFormat(rrtdrpd.getTblReservationRoomPriceDetail().getDetailPrice()) + " \t "
                                    + "Diskon : " + ClassFormatter.decimalFormat.cFormat(rrtdrpd.getTblReservationRoomPriceDetail().getDetailDiscountPercentage()) + "% (" + getDataDiscountEvent(rrtdrpd.getTblReservationRoomPriceDetail()) + ") \t "
                                    + "Compliment : " + ClassFormatter.currencyFormat.cFormat(rrtdrpd.getTblReservationRoomPriceDetail().getDetailComplimentary()) + " \t "
                                    + getDataTravelAgentCommission(rrtd, rrtdtadds) + " \n";
                        }
                    }
                }
                //data log
                dataLog += "-----------------------------------------------------------------------\n";
                dataLog += "Tambahan Barang : \n";
                //data reservation additional item
                for (TblReservationAdditionalItem rai : rais) {
                    //data reservation additional item
                    rai.setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                    rai.setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                    session.update(rai);
                    //data log
                    dataLog += "- " + ClassFormatter.dateFormate.format(rai.getAdditionalDate()) + " - " + rai.getTblItem().getItemName() + " \t "
                            + "Harga : " + ClassFormatter.currencyFormat.cFormat(rai.getItemCharge()) + " \t "
                            + "Diskon : " + ClassFormatter.decimalFormat.cFormat(rai.getDiscountPercentage()) + "% (" + getDataDiscountEvent(rai) + ") \t "
                            + "Jumlah : " + ClassFormatter.decimalFormat.cFormat(rai.getItemQuantity()) + " \n";
                }
                //data log
                dataLog += "-----------------------------------------------------------------------\n";
                dataLog += "Tambahan Layanan : \n";
                //data reservation additional service
                for (TblReservationAdditionalService ras : rass) {
                    //data reservation additional service
                    ras.setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                    ras.setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                    session.update(ras);
                    //data log
                    dataLog += "- " + ClassFormatter.dateFormate.format(ras.getAdditionalDate()) + " - " + ras.getTblRoomService().getServiceName() + " \t "
                            + "Harga : " + ClassFormatter.currencyFormat.cFormat(ras.getPrice()) + " \t "
                            + "Diskon : " + ClassFormatter.decimalFormat.cFormat(ras.getDiscountPercentage()) + "% (" + getDataDiscountEvent(ras) + ") \n";
                }
                //data log
                dataLog += "-----------------------------------------------------------------------\n";
                //data log
                LogSystem logSystem = new LogSystem();
                logSystem.setLogDateTime(Timestamp.valueOf(LocalDateTime.now()));
                logSystem.setTblSystemUser(session.find(TblSystemUser.class, ClassSession.currentUser.getIduser()));
                logSystem.setTblEmployee(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                logSystem.setLogHeader(headerLog);
                logSystem.setLogDetail(dataLog);
                logSystem.setHostName(ClassComp.getHostName());
                session.saveOrUpdate(logSystem);
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
    public boolean updateDataReservationRoomTypeDetailChangeRoomType(
            TblReservation reservation,
            TblReservationRoomTypeDetail reservationRoomTypeDetail) {
        errMsg = "";
        if (ClassSession.checkUserSession()) {  //check user current session
            try {
                session = HibernateUtil.getSessionFactory().getCurrentSession();
                session.beginTransaction();
                //data log
                String headerLog = "UBAH DATA RESERVASI KAMAR (TIPE KAMAR)";
                String dataLog = "";
                //data reservation
                reservation.setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                reservation.setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                session.update(reservation);
                //data log
                dataLog += "No. Reservasi : " + reservation.getCodeReservation() + "\n";
                dataLog += "Tgl. Reservasi : " + ClassFormatter.dateFormate.format(reservation.getReservationDate()) + "\n";
                dataLog += "Nama Customer : " + reservation.getTblCustomer().getTblPeople().getFullName() + "\n";
                dataLog += "Travel Agent : " + (reservation.getTblPartner() != null ? reservation.getTblPartner().getPartnerName() : "-") + "\n";
                dataLog += "-----------------------------------------------------------------------\n";
                //data reservation room type detail
                reservationRoomTypeDetail.setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                reservationRoomTypeDetail.setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                session.update(reservationRoomTypeDetail);
                //data log
                dataLog += "Reservasi Kamar : \n";
                dataLog += "- " + reservationRoomTypeDetail.getCodeDetail() + " - " + reservationRoomTypeDetail.getCheckInDateTime() + " s/d " + reservationRoomTypeDetail.getCheckOutDateTime() + " \t "
                        + (getDataRoom(reservationRoomTypeDetail)) + " \n";
                dataLog += "-----------------------------------------------------------------------\n";
                //data log
                LogSystem logSystem = new LogSystem();
                logSystem.setLogDateTime(Timestamp.valueOf(LocalDateTime.now()));
                logSystem.setTblSystemUser(session.find(TblSystemUser.class, ClassSession.currentUser.getIduser()));
                logSystem.setTblEmployee(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                logSystem.setLogHeader(headerLog);
                logSystem.setLogDetail(dataLog);
                logSystem.setHostName(ClassComp.getHostName());
                session.saveOrUpdate(logSystem);
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
    public boolean updateDataReservationRoomTypeDetailChangeRoom(
            TblReservation reservation,
            TblReservationRoomTypeDetail reservationRoomTypeDetail,
            List<TblReservationRoomTypeDetail> rrtds,
            List<TblReservationRoomTypeDetailRoomPriceDetail> rrtdrpds,
            List<TblReservationRoomTypeDetailTravelAgentDiscountDetail> rrtdtadds,
            List<TblReservationAdditionalItem> rais,
            List<TblReservationAdditionalService> rass,
            List<TblReservationPayment> reservationPayments,
            List<TblReservationPaymentWithTransfer> reservationPaymentWithTransfers,
            List<TblReservationPaymentWithBankCard> reservationPaymentWithBankCards,
            List<TblReservationPaymentWithCekGiro> reservationPaymentWithCekGiros,
            List<TblReservationPaymentWithGuaranteePayment> reservationPaymentWithGuaranteePayments,
            List<TblReservationPaymentWithReservationVoucher> reservationPaymentWithReservationVouchers,
            List<TblGuaranteeLetterItemDetail> guaranteeLetterItemDetails,
            List<TblReservationRoomTypeDetailRoomPriceDetailChangeRoomHistory> changeRoomHistories,
            LocalDateTime currentDateForChangeRoomProcess,
            Time defaultCheckInTime
    ) {
        errMsg = "";
        if (ClassSession.checkUserSession()) {  //check user current session
            try {
                session = HibernateUtil.getSessionFactory().getCurrentSession();
                session.beginTransaction();
                //data log
                String headerLog = "BUAT DATA PINDAH KAMAR (TIPE RESERVASI KAMAR)";
                String dataLog = "";
                Query rs;
                //data reservation
                reservation.setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                reservation.setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                session.update(reservation);
                //data log
                dataLog += "No. Reservasi : " + reservation.getCodeReservation() + "\n";
                dataLog += "Tgl. Reservasi : " + ClassFormatter.dateFormate.format(reservation.getReservationDate()) + "\n";
                dataLog += "Nama Customer : " + reservation.getTblCustomer().getTblPeople().getFullName() + "\n";
                dataLog += "Travel Agent : " + (reservation.getTblPartner() != null ? reservation.getTblPartner().getPartnerName() : "-") + "\n";
                dataLog += "-----------------------------------------------------------------------\n";
                //data reservation room type (update:old)
                reservationRoomTypeDetail.setCheckOutDateTime(Timestamp.valueOf(currentDateForChangeRoomProcess));
                if (LocalDateTime.of(reservationRoomTypeDetail.getCheckInDateTime().getYear() + 1900,
                        reservationRoomTypeDetail.getCheckInDateTime().getMonth() + 1,
                        reservationRoomTypeDetail.getCheckInDateTime().getDate(),
                        reservationRoomTypeDetail.getCheckInDateTime().getHours(),
                        reservationRoomTypeDetail.getCheckInDateTime().getMinutes(),
                        0).isEqual(LocalDateTime.of(reservationRoomTypeDetail.getCheckOutDateTime().getYear() + 1900,
                                        reservationRoomTypeDetail.getCheckOutDateTime().getMonth() + 1,
                                        reservationRoomTypeDetail.getCheckOutDateTime().getDate(),
                                        reservationRoomTypeDetail.getCheckOutDateTime().getHours(),
                                        reservationRoomTypeDetail.getCheckOutDateTime().getMinutes(),
                                        0))
                        && !IsReservationRoomTypeDetailHaveAdditonalService(reservationRoomTypeDetail, rass)) {
                    if (reservationRoomTypeDetail.getTblReservationCheckInOut() == null
                            || reservationRoomTypeDetail.getTblReservationCheckInOut().getRefReservationCheckInOutStatus().getIdstatus() == 0) {    //Ready to Check In = '0'     
                        //data reservation room type detail : delete..
                        reservationRoomTypeDetail.setRefRecordStatus(session.find(RefRecordStatus.class, 2));
                        //data reservation room type detail - room service (delete)
                        rs = session.getNamedQuery("deleteAllTblReservationRoomTypeDetailRoomServiceByIDRoomTypeDetail")
                                .setParameter("idRoomTypeDetail", reservationRoomTypeDetail.getIddetail())
                                .setParameter("idEmployee", ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee());
                        errMsg = (String) rs.uniqueResult();
                        if (!errMsg.equals("")) {
                            if (session.getTransaction().isActive()) {
                                session.getTransaction().rollback();
                            }
                            return false;
                        }
                        //data reservation room type detail - item (delete)
                        rs = session.getNamedQuery("deleteAllTblReservationRoomTypeDetailItemByIDRoomTypeDetail")
                                .setParameter("idRoomTypeDetail", reservationRoomTypeDetail.getIddetail())
                                .setParameter("idEmployee", ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee());
                        errMsg = (String) rs.uniqueResult();
                        if (!errMsg.equals("")) {
                            if (session.getTransaction().isActive()) {
                                session.getTransaction().rollback();
                            }
                            return false;
                        }
                        //data reservation room type detail - travel agent discount detail, reservation travel agent discount detail (delete:old)
                        rs = session.getNamedQuery("deleteAllTblReservationRoomTypeDetailTravelAgentDiscountDetailByIDRoomTypeDetail")
                                .setParameter("idRoomTypeDetail", reservationRoomTypeDetail.getIddetail())
                                .setParameter("idEmployee", ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee());
                        errMsg = (String) rs.uniqueResult();
                        if (!errMsg.equals("")) {
                            if (session.getTransaction().isActive()) {
                                session.getTransaction().rollback();
                            }
                            return false;
                        }
                    } else {
                        //delete all unused data additional (rrtd : old)
                        deleteAllReservationAddtionalByIDReservationRoomTypeDetailAndAfterOrEqualsDate(
                                reservationRoomTypeDetail.getIddetail(),
                                currentDateForChangeRoomProcess,
                                defaultCheckInTime);
                    }
                } else {
                    if (reservationRoomTypeDetail.getTblReservationCheckInOut() == null
                            || reservationRoomTypeDetail.getTblReservationCheckInOut().getRefReservationCheckInOutStatus().getIdstatus() == 0) {     //Ready to Check In = '0'
                        //data reservation check in/out (insert:old)
                        TblReservationCheckInOut dataCheckInOut = new TblReservationCheckInOut();
                        dataCheckInOut.setTblRoom(null);
                        dataCheckInOut.setCheckInDateTime(null);
                        dataCheckInOut.setCheckOutDateTime(null);
                        dataCheckInOut.setAdultNumber(0);
                        dataCheckInOut.setChildNumber(0);
                        dataCheckInOut.setCardUsed(0);
                        dataCheckInOut.setCardMissed(0);
                        dataCheckInOut.setBrokenCardCharge(new BigDecimal("0"));
                        dataCheckInOut.setCardAdditional(0);
                        dataCheckInOut.setAdditionalCardCharge(new BigDecimal("0"));
                        dataCheckInOut.setRefReservationCheckInOutStatus(session.find(RefReservationCheckInOutStatus.class, 4)); //Tidak Berlaku = '4'
                        dataCheckInOut.setCreateDate(Timestamp.valueOf(LocalDateTime.now()));
                        dataCheckInOut.setTblEmployeeByCreateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                        dataCheckInOut.setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                        dataCheckInOut.setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                        dataCheckInOut.setRefRecordStatus(session.find(RefRecordStatus.class, 1));
                        session.saveOrUpdate(dataCheckInOut);
                        //data reservation room type detail
                        reservationRoomTypeDetail.setTblReservationCheckInOut(dataCheckInOut);
                        //delete all unused data additional (rrtd : old)
                        deleteAllReservationAddtionalByIDReservationRoomTypeDetailAndAfterOrEqualsDate(
                                reservationRoomTypeDetail.getIddetail(),
                                currentDateForChangeRoomProcess,
                                defaultCheckInTime);
                    } else {
                        //delete all unused data additional (rrtd : old)
                        deleteAllReservationAddtionalByIDReservationRoomTypeDetailAndAfterOrEqualsDate(
                                reservationRoomTypeDetail.getIddetail(),
                                currentDateForChangeRoomProcess,
                                defaultCheckInTime);
                    }
                }
                reservationRoomTypeDetail.setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                reservationRoomTypeDetail.setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                session.update(reservationRoomTypeDetail);
                //data reservation room type detail - room price detail, reservation room price detail (delete:old)
                rs = session.getNamedQuery("deleteAllTblReservationRoomTypeDetailRoomPriceDetailByIDRoomTypeDetailAndDetailDateAfter")
                        .setParameter("idRoomTypeDetail", reservationRoomTypeDetail.getIddetail())
                        .setParameter("detailDate", Date.valueOf(currentDateForChangeRoomProcess.toLocalDate()))
                        .setParameter("idEmployee", ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee());
                errMsg = (String) rs.uniqueResult();
                if (!errMsg.equals("")) {
                    if (session.getTransaction().isActive()) {
                        session.getTransaction().rollback();
                    }
                    return false;
                }
                //data reservation room type (insert:new)
                for (TblReservationRoomTypeDetail rrtd : rrtds) {
                    if (checkRoomAvailable(rrtd, null)) {
                        //data reservation room type detail - check in/out
                        if (rrtd.getTblReservationCheckInOut() != null) {
                            rrtd.getTblReservationCheckInOut().setCreateDate(Timestamp.valueOf(LocalDateTime.now()));
                            rrtd.getTblReservationCheckInOut().setTblEmployeeByCreateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                            rrtd.getTblReservationCheckInOut().setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                            rrtd.getTblReservationCheckInOut().setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                            rrtd.getTblReservationCheckInOut().setRefRecordStatus(session.find(RefRecordStatus.class, 1));
                            session.saveOrUpdate(rrtd.getTblReservationCheckInOut());
                        }
                        //data reservation room type (insert:new)
                        rrtd.setCreateDate(Timestamp.valueOf(LocalDateTime.now()));
                        rrtd.setTblEmployeeByCreateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                        rrtd.setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                        rrtd.setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                        rrtd.setRefRecordStatus(session.find(RefRecordStatus.class, 1));
                        session.saveOrUpdate(rrtd);
                        //data reservation room type detail - room service (insert)
                        List<TblRoomTypeRoomService> roomTypeRoomServices = getAllRoomTypeRoomServiceByIDRoomType(rrtd.getTblRoomType().getIdroomType());
                        for (TblRoomTypeRoomService roomTypeRoomService : roomTypeRoomServices) {
                            TblReservationRoomTypeDetailRoomService reservationRoomTypeDetailRoomService = new TblReservationRoomTypeDetailRoomService();
                            reservationRoomTypeDetailRoomService.setTblReservationRoomTypeDetail(rrtd);
                            reservationRoomTypeDetailRoomService.setTblRoomService(roomTypeRoomService.getTblRoomService());
                            reservationRoomTypeDetailRoomService.setRoomServiceCost(roomTypeRoomService.getTblRoomService().getPrice());
                            reservationRoomTypeDetailRoomService.setPeopleNumber(roomTypeRoomService.getPeopleNumber());
                            reservationRoomTypeDetailRoomService.setCreateDate(Timestamp.valueOf(LocalDateTime.now()));
                            reservationRoomTypeDetailRoomService.setTblEmployeeByCreateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                            reservationRoomTypeDetailRoomService.setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                            reservationRoomTypeDetailRoomService.setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                            reservationRoomTypeDetailRoomService.setRefRecordStatus(session.find(RefRecordStatus.class, 1));
                            session.saveOrUpdate(reservationRoomTypeDetailRoomService);
                        }
                        //data reservation room type detail - item (insert)
                        List<TblRoomTypeItem> roomTypeItems = getAllRoomTypeItemByIDRoomType(rrtd.getTblRoomType().getIdroomType());
                        for (TblRoomTypeItem roomTypeItem : roomTypeItems) {
                            TblReservationRoomTypeDetailItem reservationRoomTypeDetailItem = new TblReservationRoomTypeDetailItem();
                            reservationRoomTypeDetailItem.setTblReservationRoomTypeDetail(rrtd);
                            reservationRoomTypeDetailItem.setTblItem(roomTypeItem.getTblItem());
                            reservationRoomTypeDetailItem.setItemAdditionalCharge(roomTypeItem.getTblItem().getAdditionalCharge());
                            reservationRoomTypeDetailItem.setItemBrokenCharge(roomTypeItem.getTblItem().getBrokenCharge());
                            reservationRoomTypeDetailItem.setItemQuantity(roomTypeItem.getItemQuantity());
                            reservationRoomTypeDetailItem.setCreateDate(Timestamp.valueOf(LocalDateTime.now()));
                            reservationRoomTypeDetailItem.setTblEmployeeByCreateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                            reservationRoomTypeDetailItem.setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                            reservationRoomTypeDetailItem.setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                            reservationRoomTypeDetailItem.setRefRecordStatus(session.find(RefRecordStatus.class, 1));
                            session.saveOrUpdate(reservationRoomTypeDetailItem);
                        }
                        //insert all used data additional (rrtd : new)
                        insertAllReservationAddtionalReservationRoomTypeDetail(rrtd);
                    } else {
                        if (session.getTransaction().isActive()) {
                            session.getTransaction().rollback();
                        }
                        errMsg = "Jumlah kamar tidak mencukupi..!";
                        return false;
                    }
                }
                //data reservation room type detail - room price detail (insert:new)
                for (TblReservationRoomTypeDetailRoomPriceDetail rrtdrpd : rrtdrpds) {
                    //data reservation room price detail (insert:new)
                    rrtdrpd.getTblReservationRoomPriceDetail().setCreateDate(reservationRoomTypeDetail.getCreateDate());
                    rrtdrpd.getTblReservationRoomPriceDetail().setTblEmployeeByCreateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                    rrtdrpd.getTblReservationRoomPriceDetail().setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                    rrtdrpd.getTblReservationRoomPriceDetail().setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                    rrtdrpd.getTblReservationRoomPriceDetail().setRefRecordStatus(session.find(RefRecordStatus.class, 1));
                    session.saveOrUpdate(rrtdrpd.getTblReservationRoomPriceDetail());
                    //data reservation room type detail - room price detail (insert:new)
                    rrtdrpd.setCreateDate(reservationRoomTypeDetail.getCreateDate());
                    rrtdrpd.setTblEmployeeByCreateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                    rrtdrpd.setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                    rrtdrpd.setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                    rrtdrpd.setRefRecordStatus(session.find(RefRecordStatus.class, 1));
                    session.saveOrUpdate(rrtdrpd);
                    //data guarantee letter (if old data use 'guarantee payment'), <update old data>
                    List<TblGuaranteeLetterItemDetail> dataGuaranteeLetterItemDetails = getAllDataGuaranteeLetterItemDetailByIDReservationAndCodeRTDAndDetailDate(
                            reservationRoomTypeDetail.getTblReservation().getIdreservation(),
                            reservationRoomTypeDetail.getCodeDetail(),
                            rrtdrpd.getTblReservationRoomPriceDetail().getDetailDate());
                    for (TblGuaranteeLetterItemDetail dataGuaranteeLetterItemDetail : dataGuaranteeLetterItemDetails) {
                        dataGuaranteeLetterItemDetail.setCodeRtd(rrtdrpd.getTblReservationRoomTypeDetail().getCodeDetail());
                        dataGuaranteeLetterItemDetail.setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                        dataGuaranteeLetterItemDetail.setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                        session.update(dataGuaranteeLetterItemDetail);
                    }
                }
                //data reservation room type detail - travel agent discount detail (insert:new)
                for (TblReservationRoomTypeDetailTravelAgentDiscountDetail rrtdtadd : rrtdtadds) {
                    //data reservation travel agent discount detail (insert:new)
                    rrtdtadd.getTblReservationTravelAgentDiscountDetail().setCreateDate(reservationRoomTypeDetail.getCreateDate());
                    rrtdtadd.getTblReservationTravelAgentDiscountDetail().setTblEmployeeByCreateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                    rrtdtadd.getTblReservationTravelAgentDiscountDetail().setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                    rrtdtadd.getTblReservationTravelAgentDiscountDetail().setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                    rrtdtadd.getTblReservationTravelAgentDiscountDetail().setRefRecordStatus(session.find(RefRecordStatus.class, 1));
                    session.saveOrUpdate(rrtdtadd.getTblReservationTravelAgentDiscountDetail());
                    //data reservation room type detail - travel agent discount detail (insert:new)
                    rrtdtadd.setCreateDate(reservationRoomTypeDetail.getCreateDate());
                    rrtdtadd.setTblEmployeeByCreateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                    rrtdtadd.setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                    rrtdtadd.setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                    rrtdtadd.setRefRecordStatus(session.find(RefRecordStatus.class, 1));
                    session.saveOrUpdate(rrtdtadd);
                }
                //data log
                dataLog += "Reservasi Kamar : \n";
                for (TblReservationRoomTypeDetail rrtd : rrtds) {
                    dataLog += "- " + rrtd.getCodeDetail() + " - " + rrtd.getCheckInDateTime() + " s/d " + rrtd.getCheckOutDateTime() + " \t "
                            + (getDataRoom(rrtd)) + " \n";
                    for (TblReservationRoomTypeDetailRoomPriceDetail rrtdrpd : rrtdrpds) {
                        if (rrtdrpd.getTblReservationRoomTypeDetail().getIddetail()
                                == rrtd.getIddetail()) {
                            dataLog += "\t - " + ClassFormatter.dateFormate.format(rrtdrpd.getTblReservationRoomPriceDetail().getDetailDate()) + " \t "
                                    + "Harga : " + ClassFormatter.currencyFormat.cFormat(rrtdrpd.getTblReservationRoomPriceDetail().getDetailPrice()) + " \t "
                                    + "Diskon : " + ClassFormatter.decimalFormat.cFormat(rrtdrpd.getTblReservationRoomPriceDetail().getDetailDiscountPercentage()) + "% (" + getDataDiscountEvent(rrtdrpd.getTblReservationRoomPriceDetail()) + ") \t "
                                    + "Compliment : " + ClassFormatter.currencyFormat.cFormat(rrtdrpd.getTblReservationRoomPriceDetail().getDetailComplimentary()) + " \t "
                                    + getDataTravelAgentCommission(rrtd, rrtdtadds) + " \n";
                        }
                    }
                }
                dataLog += "-----------------------------------------------------------------------\n";
                //data reservation additonal item (old:delete)
                rs = session.getNamedQuery("deleteAllTblReservationAdditionalItemByIDRoomTypeDetailAndAdditionalDateAfter")
                        .setParameter("idRoomTypeDetail", reservationRoomTypeDetail.getIddetail())
                        .setParameter("additionalDate", Date.valueOf(currentDateForChangeRoomProcess.toLocalDate()))
                        .setParameter("idEmployee", ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee());
                errMsg = (String) rs.uniqueResult();
                if (!errMsg.equals("")) {
                    if (session.getTransaction().isActive()) {
                        session.getTransaction().rollback();
                    }
                    return false;
                }
                //data reservation additonal item - reserved (old:delete)
                rs = session.getNamedQuery("deleteAllTblReservationAdditionalItemReservedByIDRoomTypeDetailAndReservedDateAfter")
                        .setParameter("idRoomTypeDetail", reservationRoomTypeDetail.getIddetail())
                        .setParameter("reservedDate", Date.valueOf(currentDateForChangeRoomProcess.toLocalDate()))
                        .setParameter("idEmployee", ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee());
                errMsg = (String) rs.uniqueResult();
                if (!errMsg.equals("")) {
                    if (session.getTransaction().isActive()) {
                        session.getTransaction().rollback();
                    }
                    return false;
                }
                //data log
                dataLog += "Tambahan Barang : \n";
                //data reservation additonal item (insert:new)
                for (TblReservationAdditionalItem rai : rais) {
//                    rai.setCreateDate(Timestamp.valueOf(LocalDateTime.now()));
//                    rai.setTblEmployeeByCreateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                    rai.setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                    rai.setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                    rai.setRefRecordStatus(session.find(RefRecordStatus.class, 1));
                    session.saveOrUpdate(rai);
                    //data reservation additonal item - reserved
                    TblReservationAdditionalItemReserved reservationAdditionalItemReserved = getReservationAdditionalItemReservedByIDReservationRoomTypeDetailAndIDItemAndReservedDate(
                            rai.getTblReservationRoomTypeDetail().getIddetail(),
                            rai.getTblItem().getIditem(),
                            rai.getAdditionalDate());
                    if (reservationAdditionalItemReserved != null) {
                        reservationAdditionalItemReserved.setReservedQuantity(reservationAdditionalItemReserved.getReservedQuantity().add(rai.getItemQuantity()));
                        reservationAdditionalItemReserved.setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                        reservationAdditionalItemReserved.setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                        session.update(reservationAdditionalItemReserved);
                    } else {
                        reservationAdditionalItemReserved = new TblReservationAdditionalItemReserved();
                        reservationAdditionalItemReserved.setTblReservationRoomTypeDetail(rai.getTblReservationRoomTypeDetail());
                        reservationAdditionalItemReserved.setTblItem(rai.getTblItem());
                        reservationAdditionalItemReserved.setReservedDate(rai.getAdditionalDate());
                        reservationAdditionalItemReserved.setReservedQuantity(rai.getItemQuantity());
                        reservationAdditionalItemReserved.setInUsedQuantity(getLastInUseStock(
                                reservationRoomTypeDetail,
                                reservationAdditionalItemReserved.getTblItem(),
                                reservationAdditionalItemReserved.getReservedDate()));
                        reservationAdditionalItemReserved.setDoneQuantity(getLastDoneStock(
                                reservationRoomTypeDetail,
                                reservationAdditionalItemReserved.getTblItem(),
                                reservationAdditionalItemReserved.getReservedDate()));
                        reservationAdditionalItemReserved.setRefReservationAdditionalItemReservedStatus(session.find(RefReservationAdditionalItemReservedStatus.class, 1));     //Reserved = '1'
//                        reservationAdditionalItemReserved.setCreateDate(Timestamp.valueOf(LocalDateTime.now()));
//                        reservationAdditionalItemReserved.setTblEmployeeByCreateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                        reservationAdditionalItemReserved.setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                        reservationAdditionalItemReserved.setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                        reservationAdditionalItemReserved.setRefRecordStatus(session.find(RefRecordStatus.class, 1));
                        session.saveOrUpdate(reservationAdditionalItemReserved);
                    }
                    //data log
                    dataLog += "- " + ClassFormatter.dateFormate.format(rai.getAdditionalDate()) + " - " + rai.getTblItem().getItemName() + " \t "
                            + "Harga : " + ClassFormatter.currencyFormat.cFormat(rai.getItemCharge()) + " \t "
                            + "Diskon : " + ClassFormatter.decimalFormat.cFormat(rai.getDiscountPercentage()) + "% (" + getDataDiscountEvent(rai) + ") \t "
                            + "Jumlah : " + ClassFormatter.decimalFormat.cFormat(rai.getItemQuantity()) + " \n";
                }
                //data log
                dataLog += "-----------------------------------------------------------------------\n";
                //data reservation additonal service (old:delete)
                rs = session.getNamedQuery("deleteAllTblReservationAdditionalServiceByIDRoomTypeDetailAndAdditionalDateAfterAndIDRoomServiceNotSpecific")
                        .setParameter("idRoomTypeDetail", reservationRoomTypeDetail.getIddetail())
                        .setParameter("additionalDate", Date.valueOf(currentDateForChangeRoomProcess.toLocalDate()))
                        .setParameter("idEmployee", ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee());
                errMsg = (String) rs.uniqueResult();
                if (!errMsg.equals("")) {
                    if (session.getTransaction().isActive()) {
                        session.getTransaction().rollback();
                    }
                    return false;
                }
                //data log
                dataLog += "Tambahan Layanan : \n";
                //data reservation additonal service (insert:new)
                for (TblReservationAdditionalService ras : rass) {
                    if (ras.getTblRoomService().getIdroomService() == 4) { //Lainnya(Bonus Voucher) = '4'
                        ras.setCreateDate(Timestamp.valueOf(LocalDateTime.now()));
                        ras.setTblEmployeeByCreateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                    }
                    ras.setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                    ras.setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                    ras.setRefRecordStatus(session.find(RefRecordStatus.class, 1));
                    session.saveOrUpdate(ras);
                    //data log
                    dataLog += "- " + ClassFormatter.dateFormate.format(ras.getAdditionalDate()) + " - " + ras.getTblRoomService().getServiceName() + " \t "
                            + "Harga : " + ClassFormatter.currencyFormat.cFormat(ras.getPrice()) + " \t "
                            + "Diskon : " + ClassFormatter.decimalFormat.cFormat(ras.getDiscountPercentage()) + "% (" + getDataDiscountEvent(ras) + ") \n";
                }
                //data log
                dataLog += "-----------------------------------------------------------------------\n";
                dataLog += "Data Pindah Kamar - History : \n";
                //data reservation room type detail - room price detail - change room history (insert:new)
                for (TblReservationRoomTypeDetailRoomPriceDetailChangeRoomHistory changeRoomHistory : changeRoomHistories) {
                    if (reservationRoomTypeDetail.getTblReservationCheckInOut() != null) {
                        //data reservation room type detail - room price detail - change room history (insert:new)
                        changeRoomHistory.setCreateDate(Timestamp.valueOf(LocalDateTime.now()));
                        changeRoomHistory.setTblEmployeeByCreateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                        changeRoomHistory.setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                        changeRoomHistory.setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                        changeRoomHistory.setRefRecordStatus(session.find(RefRecordStatus.class, 1));
                        session.saveOrUpdate(changeRoomHistory);
                        dataLog += "- Dari tipe kamar '" + changeRoomHistory.getTblReservationRoomTypeDetailRoomPriceDetailByIdrelationOld().getTblReservationRoomTypeDetail().getTblRoomType().getRoomTypeName()
                                + "' menjadi '" + changeRoomHistory.getTblReservationRoomTypeDetailRoomPriceDetailByIdrelationNew().getTblReservationRoomTypeDetail().getTblRoomType().getRoomTypeName() + "' \n";
                    }
                }
                //data log
                dataLog += "-----------------------------------------------------------------------\n";
                dataLog += "Transaksi Pembayaran : \n";
                //data reservation transaction (payment)
                for (TblReservationPayment reservationPayment : reservationPayments) {
                    BigDecimal paymentDiscount = new BigDecimal("0");
                    BigDecimal paymentCharge = new BigDecimal("0");
                    reservationPayment.setCodePayment(ClassCoder.getCode("Reservation Payment", session));
                    reservationPayment.setCreateDate(Timestamp.valueOf(LocalDateTime.now()));
                    reservationPayment.setTblEmployeeByCreateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                    reservationPayment.setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                    reservationPayment.setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                    reservationPayment.setRefRecordStatus(session.find(RefRecordStatus.class, 1));
                    session.saveOrUpdate(reservationPayment);
                    //data log
                    dataLog += "- " + reservationPayment.getCodePayment() + " - " + ClassFormatter.dateTimeFormate.format(reservationPayment.getPaymentDate()) + " \t "
                            + "Nominal : " + ClassFormatter.currencyFormat.cFormat(reservationPayment.getUnitNominal()) + " \t "
                            + "Tipe : " + reservationPayment.getRefFinanceTransactionPaymentType().getTypeName() + " \n";
                    //@@@%%%
                    //data bank account (customer)
                    TblBankAccount customerBankAccount = null;
                    //data company balance - bank account
                    TblCompanyBalanceBankAccount companyBalanceBankAccount = null;
                    //data reservation transaction payment - with transfer
                    for (TblReservationPaymentWithTransfer reservationPaymentWithTransfer : reservationPaymentWithTransfers) {
                        if (reservationPaymentWithTransfer.getTblReservationPayment().equals(reservationPayment)) {
                            reservationPaymentWithTransfer.setCreateDate(Timestamp.valueOf(LocalDateTime.now()));
                            reservationPaymentWithTransfer.setTblEmployeeByCreateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                            reservationPaymentWithTransfer.setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                            reservationPaymentWithTransfer.setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                            reservationPaymentWithTransfer.setRefRecordStatus(session.find(RefRecordStatus.class, 1));
                            session.saveOrUpdate(reservationPaymentWithTransfer);
                            //@@@%%%
                            //data bank account (customer)
                            customerBankAccount = reservationPaymentWithTransfer.getTblBankAccountBySenderBankAccount();
                            //data company balance - bank account
                            companyBalanceBankAccount = getCompanyBalanceBankAccountByIDCompanyBalanceAndIDBankAccount((long) 1, reservationPaymentWithTransfer.getTblBankAccountByReceiverBankAccount().getIdbankAccount());    //hotel balance = '1'
                            break;
                        }
                    }
                    //data reservation transaction payment - with bank card
                    for (TblReservationPaymentWithBankCard reservationPaymentWithBankCard : reservationPaymentWithBankCards) {
                        if (reservationPaymentWithBankCard.getTblReservationPayment().equals(reservationPayment)) {
                            reservationPaymentWithBankCard.setRefEdctransactionStatus(session.find(RefEdctransactionStatus.class, 0)); //Sale = '0'
                            reservationPaymentWithBankCard.setCreateDate(Timestamp.valueOf(LocalDateTime.now()));
                            reservationPaymentWithBankCard.setTblEmployeeByCreateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                            reservationPaymentWithBankCard.setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                            reservationPaymentWithBankCard.setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                            reservationPaymentWithBankCard.setRefRecordStatus(session.find(RefRecordStatus.class, 1));
                            session.saveOrUpdate(reservationPaymentWithBankCard);
                            //@@@%%%
                            //data company balance - bank account
                            companyBalanceBankAccount = getCompanyBalanceBankAccountByIDCompanyBalanceAndIDBankAccount((long) 1, reservationPaymentWithBankCard.getTblBankAccount().getIdbankAccount());    //hotel balance = '1'
                            //minus payment discount n payment charge (MDR)
                            if (companyBalanceBankAccount != null) {
//                                companyBalanceBankAccount.setCompanyBalanceBankAccountNominal(companyBalanceBankAccount.getCompanyBalanceBankAccountNominal()
//                                        .subtract(reservationPaymentWithBankCard.getPaymentDiscount())
//                                        .subtract(reservationPaymentWithBankCard.getPaymentCharge()));
                                paymentDiscount = paymentDiscount.add(reservationPaymentWithBankCard.getPaymentDiscount());
                                paymentCharge = paymentCharge.add(reservationPaymentWithBankCard.getPaymentCharge());
                            }
                            break;
                        }
                    }
                    //data reservation transaction payment - with cek/giro
                    for (TblReservationPaymentWithCekGiro reservationPaymentWithCekGiro : reservationPaymentWithCekGiros) {
                        if (reservationPaymentWithCekGiro.getTblReservationPayment().equals(reservationPayment)) {
                            reservationPaymentWithCekGiro.setCreateDate(Timestamp.valueOf(LocalDateTime.now()));
                            reservationPaymentWithCekGiro.setTblEmployeeByCreateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                            reservationPaymentWithCekGiro.setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                            reservationPaymentWithCekGiro.setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                            reservationPaymentWithCekGiro.setRefRecordStatus(session.find(RefRecordStatus.class, 1));
                            session.saveOrUpdate(reservationPaymentWithCekGiro);
                            //@@@%%%
                            //data bank account (customer)
                            customerBankAccount = reservationPaymentWithCekGiro.getTblBankAccountBySenderBankAccount();
                            //data company balance - bank account
                            companyBalanceBankAccount = getCompanyBalanceBankAccountByIDCompanyBalanceAndIDBankAccount((long) 1, reservationPaymentWithCekGiro.getTblBankAccountByReceiverBankAccount().getIdbankAccount());    //hotel balance = '1'
                            break;
                        }
                    }
                    //data reservation transaction payment - with guarantee payment
                    for (TblReservationPaymentWithGuaranteePayment reservationPaymentWithGuaranteePayment : reservationPaymentWithGuaranteePayments) {
                        if (reservationPaymentWithGuaranteePayment.getTblReservationPayment().equals(reservationPayment)) {
                            reservationPaymentWithGuaranteePayment.setCreateDate(Timestamp.valueOf(LocalDateTime.now()));
                            reservationPaymentWithGuaranteePayment.setTblEmployeeByCreateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                            reservationPaymentWithGuaranteePayment.setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                            reservationPaymentWithGuaranteePayment.setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                            reservationPaymentWithGuaranteePayment.setRefRecordStatus(session.find(RefRecordStatus.class, 1));
                            session.saveOrUpdate(reservationPaymentWithGuaranteePayment);
                            //data guarantee letter - item detail
                            for (TblGuaranteeLetterItemDetail guaranteeLetterItemDetail : guaranteeLetterItemDetails) {
                                if (guaranteeLetterItemDetail.getTblReservationPaymentWithGuaranteePayment().equals(reservationPaymentWithGuaranteePayment)) {
                                    guaranteeLetterItemDetail.setCreateDate(Timestamp.valueOf(LocalDateTime.now()));
                                    guaranteeLetterItemDetail.setTblEmployeeByCreateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                                    guaranteeLetterItemDetail.setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                                    guaranteeLetterItemDetail.setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                                    guaranteeLetterItemDetail.setRefRecordStatus(session.find(RefRecordStatus.class, 1));
                                    session.saveOrUpdate(guaranteeLetterItemDetail);
                                }
                            }
                            //@@@%%%
                            if (reservationPaymentWithGuaranteePayment.getIsDebt()) {
                                //data bank account (customer)
                                customerBankAccount = null;
                                //data company balance - bank account
                                companyBalanceBankAccount = null;
                            } else {
                                //data bank account (customer)
                                customerBankAccount = reservationPaymentWithGuaranteePayment.getTblBankAccountBySenderBankAccount();
                                //data company balance - bank account
                                companyBalanceBankAccount = getCompanyBalanceBankAccountByIDCompanyBalanceAndIDBankAccount((long) 1, reservationPaymentWithGuaranteePayment.getTblBankAccountByReceiverBankAccount().getIdbankAccount());    //hotel balance = '1'
                            }
                            //data reservation with guarantee letter - hotel receivable
                            if (reservationPaymentWithGuaranteePayment.getIsDebt()) {
                                //data hotel invoice
                                TblHotelInvoice hotelInvoice = new TblHotelInvoice();
                                hotelInvoice.setCodeHotelInvoice(ClassCoder.getCode("Hotel Invoice", session));
                                hotelInvoice.setTblPartner(reservationPaymentWithGuaranteePayment.getTblPartner());
                                hotelInvoice.setTblSupplier(null);
                                hotelInvoice.setRefHotelInvoiceType(session.find(RefHotelInvoiceType.class, 1));    //Receivable = '1'
                                hotelInvoice.setCreateDate(Timestamp.valueOf(LocalDateTime.now()));
                                hotelInvoice.setTblEmployeeByCreateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                                hotelInvoice.setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                                hotelInvoice.setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                                hotelInvoice.setRefRecordStatus(session.find(RefRecordStatus.class, 1));
                                session.saveOrUpdate(hotelInvoice);
                                //data hotel receivable
                                TblHotelReceivable hotelReceivable = new TblHotelReceivable();
                                hotelReceivable.setTblHotelInvoice(hotelInvoice);
                                hotelReceivable.setHotelReceivableNominal(reservationPayment.getUnitNominal());
                                hotelReceivable.setRefHotelReceivableType(session.find(RefHotelReceivableType.class, 1));   //Guarantee Letter = '1'
                                hotelReceivable.setRefFinanceTransactionStatus(session.find(RefFinanceTransactionStatus.class, 0));    //Belum Dibayar = '0'
                                hotelReceivable.setCreateDate(Timestamp.valueOf(LocalDateTime.now()));
                                hotelReceivable.setTblEmployeeByCreateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                                hotelReceivable.setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                                hotelReceivable.setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                                hotelReceivable.setRefRecordStatus(session.find(RefRecordStatus.class, 1));
                                session.saveOrUpdate(hotelReceivable);
                                //data reservation with guarantee payment - (hotel receivable)
                                reservationPaymentWithGuaranteePayment.setTblHotelReceivable(hotelReceivable);
                                reservationPaymentWithGuaranteePayment.setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                                reservationPaymentWithGuaranteePayment.setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                                session.saveOrUpdate(reservationPaymentWithGuaranteePayment);
                            }
                            break;
                        }
                    }
                    //data reservation transaction payment - with reservation voucher
                    for (TblReservationPaymentWithReservationVoucher reservationPaymentWithReservationVoucher : reservationPaymentWithReservationVouchers) {
                        if (reservationPaymentWithReservationVoucher.getTblReservationPayment().equals(reservationPayment)) {
                            reservationPaymentWithReservationVoucher.setCreateDate(Timestamp.valueOf(LocalDateTime.now()));
                            reservationPaymentWithReservationVoucher.setTblEmployeeByCreateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                            reservationPaymentWithReservationVoucher.setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                            reservationPaymentWithReservationVoucher.setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                            reservationPaymentWithReservationVoucher.setRefRecordStatus(session.find(RefRecordStatus.class, 1));
                            session.saveOrUpdate(reservationPaymentWithReservationVoucher);
                            //data reservation voucher (update:obsolete)
                            reservationPaymentWithReservationVoucher.getTblReservationVoucher().setRefVoucherStatus(session.find(RefVoucherStatus.class, 2));
                            reservationPaymentWithReservationVoucher.getTblReservationVoucher().setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                            reservationPaymentWithReservationVoucher.getTblReservationVoucher().setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                            session.update(reservationPaymentWithReservationVoucher.getTblReservationVoucher());
                            break;
                        }
                    }
                    //@@@%%%
                    //data balance (cashier)
                    if (reservationPayment.getRefFinanceTransactionPaymentType().getIdtype() == (long) 0) {   //plus
//                            || reservationPayment.getRefFinanceTransactionPaymentType().getIdtype() == (long) 13) {   //plus
                        TblCompanyBalance dataBalance = session.find(TblCompanyBalance.class, (long) 3);
                        dataBalance.setBalanceNominal(dataBalance.getBalanceNominal().add(reservationPayment.getUnitNominal()));
                        dataBalance.setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                        dataBalance.setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                        session.update(dataBalance);
                        //data company balance (cash-flow log)
                        LogCompanyBalanceCashFlow logCompanyBalanceCashFlow = new LogCompanyBalanceCashFlow();
                        logCompanyBalanceCashFlow.setTblCompanyBalanceByIdsenderCompanyBalance(null);
                        logCompanyBalanceCashFlow.setTblBankAccountByIdsenderCbbankAccount(null);
                        logCompanyBalanceCashFlow.setTblCompanyBalanceByIdreceiverCompanyBalance(session.find(TblCompanyBalance.class, (long) 3));  //Kasir = '3'
                        logCompanyBalanceCashFlow.setTblBankAccountByIdreceiverCbbankAccount(null);
                        logCompanyBalanceCashFlow.setTransferNominal(reservationPayment.getUnitNominal());
                        logCompanyBalanceCashFlow.setHistoryDate(Timestamp.valueOf(LocalDateTime.now()));
                        logCompanyBalanceCashFlow.setTblEmployee(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                        logCompanyBalanceCashFlow.setHistoryStystemNote("Pembayaran reservasi oleh " + reservation.getTblCustomer().getTblPeople().getFullName() + ", \nsebesar : "
                                + ClassFormatter.currencyFormat.format(reservationPayment.getUnitNominal()) + " ("
                                + reservationPayment.getRefFinanceTransactionPaymentType().getTypeName() + ")");
                        session.saveOrUpdate(logCompanyBalanceCashFlow);
                    }
//                    if (reservationPayment.getRefFinanceTransactionPaymentType().getIdtype() == (long) 14) {   //minus
//                        TblCompanyBalance dataBalance = session.find(TblCompanyBalance.class, (long) 3);
//                        dataBalance.setBalanceNominal(dataBalance.getBalanceNominal() - reservationPayment.getUnitNominal());
//                        dataBalance.setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
//                        dataBalance.setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
//                        session.update(dataBalance);
//                    }
                    //data balance (company)
                    if (reservationPayment.getRefFinanceTransactionPaymentType().getIdtype() > (long) 0
                            && reservationPayment.getRefFinanceTransactionPaymentType().getIdtype() < (long) 9) {   //plus
                        if (companyBalanceBankAccount != null) {
//                            //data balance (company)
//                            TblCompanyBalance dataBalance = session.find(TblCompanyBalance.class, (long) 1);
//                            dataBalance.setBalanceNominal(dataBalance.getBalanceNominal().add(reservationPayment.getUnitNominal())
//                                    .subtract(paymentDiscount).subtract(paymentCharge));
//                            dataBalance.setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
//                            dataBalance.setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
//                            session.update(dataBalance);
//                            //data balance (company) : bank account
//                            companyBalanceBankAccount = session.find(TblCompanyBalanceBankAccount.class, companyBalanceBankAccount.getIdrelation());
//                            companyBalanceBankAccount.setCompanyBalanceBankAccountNominal(companyBalanceBankAccount.getCompanyBalanceBankAccountNominal().add(reservationPayment.getUnitNominal())
//                                    .subtract(paymentDiscount).subtract(paymentCharge));
//                            companyBalanceBankAccount.setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
//                            companyBalanceBankAccount.setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
//                            session.update(companyBalanceBankAccount);
//                            //data company balance (cash-flow log)
//                            LogCompanyBalanceCashFlow logCompanyBalanceCashFlow = new LogCompanyBalanceCashFlow();
//                            logCompanyBalanceCashFlow.setTblCompanyBalanceByIdsenderCompanyBalance(null);
//                            logCompanyBalanceCashFlow.setTblBankAccountByIdsenderCbbankAccount(customerBankAccount);
//                            logCompanyBalanceCashFlow.setTblCompanyBalanceByIdreceiverCompanyBalance(dataBalance);
//                            logCompanyBalanceCashFlow.setTblBankAccountByIdreceiverCbbankAccount(companyBalanceBankAccount.getTblBankAccount());
//                            logCompanyBalanceCashFlow.setTransferNominal(reservationPayment.getUnitNominal()
//                                    .subtract(paymentDiscount).subtract(paymentCharge));
//                            logCompanyBalanceCashFlow.setHistoryDate(Timestamp.valueOf(LocalDateTime.now()));
//                            logCompanyBalanceCashFlow.setTblEmployee(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
//                            logCompanyBalanceCashFlow.setHistoryStystemNote("Pembayaran reservasi oleh " + reservation.getTblCustomer().getTblPeople().getFullName() + ", \nsebesar : "
//                                    + ClassFormatter.currencyFormat.format(reservationPayment.getUnitNominal()) + " ("
//                                    + reservationPayment.getRefFinanceTransactionPaymentType().getTypeName() + ")");
//                            session.saveOrUpdate(logCompanyBalanceCashFlow);
                        }
                    }
//                    if (reservationPayment.getRefFinanceTransactionPaymentType().getIdtype() == (long) 16) {   //minus
//                        if (companyBalanceBankAccount != null) {
//                            //data balance (company)
//                            TblCompanyBalance dataBalance = session.find(TblCompanyBalance.class, (long) 1);
//                            dataBalance.setBalanceNominal(dataBalance.getBalanceNominal() - reservationPayment.getUnitNominal());
//                            dataBalance.setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
//                            dataBalance.setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
//                            session.update(dataBalance);
//                            //data balance (company) : bank account
//                            companyBalanceBankAccount = session.find(TblCompanyBalanceBankAccount.class, companyBalanceBankAccount.getIdrelation());
//                            companyBalanceBankAccount.setCompanyBalanceBankAccountNominal(companyBalanceBankAccount.getCompanyBalanceBankAccountNominal() - reservationPayment.getUnitNominal());
//                            companyBalanceBankAccount.setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
//                            companyBalanceBankAccount.setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
//                            session.update(companyBalanceBankAccount);
//                        }
//                    }
                    //auto settle
                    if ((reservationPayment.getRefFinanceTransactionPaymentType().getIdtype() < (long) 1
                            && reservationPayment.getRefFinanceTransactionPaymentType().getIdtype() > (long) 8)
                            || companyBalanceBankAccount == null) { //is debt, payment -> debt (data hasn't been used)
                        //data reservation payment
                        reservationPayment.setSettleDate(Timestamp.valueOf(LocalDateTime.now()));
                        session.update(reservationPayment);
                    }
                    //open close company balance - detail (cashier)
                    TblOpenCloseCashierBalance dataOpenCloseCashierBalance = getDataOpenCloseCashierBalanceIsActive();
                    if (dataOpenCloseCashierBalance != null) {
                        TblOpenCloseCashierBalanceDetail dataOpenCloseCashierBalanceDetail = new TblOpenCloseCashierBalanceDetail();
                        dataOpenCloseCashierBalanceDetail.setTblOpenCloseCashierBalance(dataOpenCloseCashierBalance);
                        dataOpenCloseCashierBalanceDetail.setTblReservationPayment(reservationPayment);
                        dataOpenCloseCashierBalanceDetail.setDetailData(getDetailData(reservationPayment));
                        dataOpenCloseCashierBalanceDetail.setCreateDate(Timestamp.valueOf(LocalDateTime.now()));
                        dataOpenCloseCashierBalanceDetail.setTblEmployeeByCreateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                        dataOpenCloseCashierBalanceDetail.setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                        dataOpenCloseCashierBalanceDetail.setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                        dataOpenCloseCashierBalanceDetail.setRefRecordStatus(session.find(RefRecordStatus.class, 1));
                        session.saveOrUpdate(dataOpenCloseCashierBalanceDetail);
                    }
                }
                //data log
                dataLog += "-----------------------------------------------------------------------\n";
                LogSystem logSystem = new LogSystem();
                logSystem.setLogDateTime(Timestamp.valueOf(LocalDateTime.now()));
                logSystem.setTblSystemUser(session.find(TblSystemUser.class, ClassSession.currentUser.getIduser()));
                logSystem.setTblEmployee(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                logSystem.setLogHeader(headerLog);
                logSystem.setLogDetail(dataLog);
                logSystem.setHostName(ClassComp.getHostName());
                session.saveOrUpdate(logSystem);
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
    public boolean updateDataReservationRoomTypeDetailChangeRoom(
            TblReservation reservation,
            TblReservationRoomTypeDetail reservationRoomTypeDetailOld,
            TblReservationRoomTypeDetail reservationRoomTypeDetailNew,
            List<TblReservationRoomTypeDetailRoomPriceDetail> rrtdrpds,
            List<TblReservationAdditionalItem> rais,
            List<TblReservationAdditionalService> rass,
            List<TblReservationRoomTypeDetailRoomPriceDetailChangeRoomHistory> changeRoomHistories,
            LocalDateTime currentDateForChangeRoomProcess,
            Time defaultCheckInTime
    ) {
        errMsg = "";
        if (ClassSession.checkUserSession()) {  //check user current session
            try {
                session = HibernateUtil.getSessionFactory().getCurrentSession();
                session.beginTransaction();
                //data log
                String headerLog = "BUAT DATA PINDAH KAMAR (TIPE RESERVASI KAMAR)";
                String dataLog = "";
                Query rs;
                //data reservation
                reservation.setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                reservation.setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                session.update(reservation);
                //data log
                dataLog += "No. Reservasi : " + reservation.getCodeReservation() + "\n";
                dataLog += "Tgl. Reservasi : " + ClassFormatter.dateFormate.format(reservation.getReservationDate()) + "\n";
                dataLog += "Nama Customer : " + reservation.getTblCustomer().getTblPeople().getFullName() + "\n";
                dataLog += "Travel Agent : " + (reservation.getTblPartner() != null ? reservation.getTblPartner().getPartnerName() : "-") + "\n";
                dataLog += "-----------------------------------------------------------------------\n";
                //data reservation room type (update:old)
                reservationRoomTypeDetailOld.setCheckOutDateTime(Timestamp.valueOf(currentDateForChangeRoomProcess));
                if (LocalDateTime.of(reservationRoomTypeDetailOld.getCheckInDateTime().getYear() + 1900,
                        reservationRoomTypeDetailOld.getCheckInDateTime().getMonth() + 1,
                        reservationRoomTypeDetailOld.getCheckInDateTime().getDate(),
                        reservationRoomTypeDetailOld.getCheckInDateTime().getHours(),
                        reservationRoomTypeDetailOld.getCheckInDateTime().getMinutes(),
                        0).isEqual(LocalDateTime.of(reservationRoomTypeDetailOld.getCheckOutDateTime().getYear() + 1900,
                                        reservationRoomTypeDetailOld.getCheckOutDateTime().getMonth() + 1,
                                        reservationRoomTypeDetailOld.getCheckOutDateTime().getDate(),
                                        reservationRoomTypeDetailOld.getCheckOutDateTime().getHours(),
                                        reservationRoomTypeDetailOld.getCheckOutDateTime().getMinutes(),
                                        0))
                        && !IsReservationRoomTypeDetailHaveAdditonalService(reservationRoomTypeDetailOld, rass)) {
                    if (reservationRoomTypeDetailOld.getTblReservationCheckInOut() == null
                            || reservationRoomTypeDetailOld.getTblReservationCheckInOut().getRefReservationCheckInOutStatus().getIdstatus() == 0) {    //Ready to Check In = '0'     
                        //data reservation room type detail : delete..
                        reservationRoomTypeDetailOld.setRefRecordStatus(session.find(RefRecordStatus.class, 2));
                        //data reservation room type detail - room service (delete)
                        rs = session.getNamedQuery("deleteAllTblReservationRoomTypeDetailRoomServiceByIDRoomTypeDetail")
                                .setParameter("idRoomTypeDetail", reservationRoomTypeDetailOld.getIddetail())
                                .setParameter("idEmployee", ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee());
                        errMsg = (String) rs.uniqueResult();
                        if (!errMsg.equals("")) {
                            if (session.getTransaction().isActive()) {
                                session.getTransaction().rollback();
                            }
                            return false;
                        }
                        //data reservation room type detail - item (delete)
                        rs = session.getNamedQuery("deleteAllTblReservationRoomTypeDetailItemByIDRoomTypeDetail")
                                .setParameter("idRoomTypeDetail", reservationRoomTypeDetailOld.getIddetail())
                                .setParameter("idEmployee", ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee());
                        errMsg = (String) rs.uniqueResult();
                        if (!errMsg.equals("")) {
                            if (session.getTransaction().isActive()) {
                                session.getTransaction().rollback();
                            }
                            return false;
                        }
                        //data reservation room type detail - travel agent discount detail, reservation travel agent discount detail (delete:old)
                        rs = session.getNamedQuery("deleteAllTblReservationRoomTypeDetailTravelAgentDiscountDetailByIDRoomTypeDetail")
                                .setParameter("idRoomTypeDetail", reservationRoomTypeDetailOld.getIddetail())
                                .setParameter("idEmployee", ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee());
                        errMsg = (String) rs.uniqueResult();
                        if (!errMsg.equals("")) {
                            if (session.getTransaction().isActive()) {
                                session.getTransaction().rollback();
                            }
                            return false;
                        }
                    } else {
                        //update room status
                        if (reservationRoomTypeDetailOld.getTblReservationCheckInOut() != null
                                && reservationRoomTypeDetailOld.getTblReservationCheckInOut().getTblRoom() != null
                                && reservationRoomTypeDetailOld.getTblReservationCheckInOut().getTblRoom().getRefRoomStatus() != null) {
                            if (reservationRoomTypeDetailOld.getTblReservationCheckInOut().getTblRoom().getRefRoomStatus().getIdstatus() == 1) {  //Occupied Clean = '1'
                                reservationRoomTypeDetailOld.getTblReservationCheckInOut().getTblRoom().setRefRoomStatus(session.find(RefRoomStatus.class, 3)); //Vacant Clean = '3'
                                session.update(reservationRoomTypeDetailOld.getTblReservationCheckInOut().getTblRoom());
                            } else {
                                if (reservationRoomTypeDetailOld.getTblReservationCheckInOut().getTblRoom().getRefRoomStatus().getIdstatus() == 2) {  //Occupied Dirty = '2'
                                    reservationRoomTypeDetailOld.getTblReservationCheckInOut().getTblRoom().setRefRoomStatus(session.find(RefRoomStatus.class, 4)); //Vacant Dirty = '4'
                                    session.update(reservationRoomTypeDetailOld.getTblReservationCheckInOut().getTblRoom());
                                }
                            }
                        }
                        //delete all unused data additional (rrtd : old)
                        deleteAllReservationAddtionalByIDReservationRoomTypeDetailAndAfterOrEqualsDate(
                                reservationRoomTypeDetailOld.getIddetail(),
                                currentDateForChangeRoomProcess,
                                defaultCheckInTime);
                    }
                } else {
                    if (reservationRoomTypeDetailOld.getTblReservationCheckInOut() == null
                            || reservationRoomTypeDetailOld.getTblReservationCheckInOut().getRefReservationCheckInOutStatus().getIdstatus() == 0) {     //Ready to Check In = '0'
                        //data reservation check in/out (insert:old)
                        TblReservationCheckInOut dataCheckInOut = new TblReservationCheckInOut();
                        dataCheckInOut.setTblRoom(null);
                        dataCheckInOut.setCheckInDateTime(null);
                        dataCheckInOut.setCheckOutDateTime(null);
                        dataCheckInOut.setAdultNumber(0);
                        dataCheckInOut.setChildNumber(0);
                        dataCheckInOut.setCardUsed(0);
                        dataCheckInOut.setCardMissed(0);
                        dataCheckInOut.setBrokenCardCharge(new BigDecimal("0"));
                        dataCheckInOut.setCardAdditional(0);
                        dataCheckInOut.setAdditionalCardCharge(new BigDecimal("0"));
                        dataCheckInOut.setRefReservationCheckInOutStatus(session.find(RefReservationCheckInOutStatus.class, 4)); //Tidak Berlaku = '4'
                        dataCheckInOut.setCreateDate(Timestamp.valueOf(LocalDateTime.now()));
                        dataCheckInOut.setTblEmployeeByCreateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                        dataCheckInOut.setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                        dataCheckInOut.setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                        dataCheckInOut.setRefRecordStatus(session.find(RefRecordStatus.class, 1));
                        session.saveOrUpdate(dataCheckInOut);
                        //data reservation room type detail
                        reservationRoomTypeDetailOld.setTblReservationCheckInOut(dataCheckInOut);
                        //delete all unused data additional (rrtd : old)
                        deleteAllReservationAddtionalByIDReservationRoomTypeDetailAndAfterOrEqualsDate(
                                reservationRoomTypeDetailOld.getIddetail(),
                                currentDateForChangeRoomProcess,
                                defaultCheckInTime);
                    } else {
                        //delete all unused data additional (rrtd : old)
                        deleteAllReservationAddtionalByIDReservationRoomTypeDetailAndAfterOrEqualsDate(
                                reservationRoomTypeDetailOld.getIddetail(),
                                currentDateForChangeRoomProcess,
                                defaultCheckInTime);
                    }
                }
                reservationRoomTypeDetailOld.setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                reservationRoomTypeDetailOld.setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                session.update(reservationRoomTypeDetailOld);
                //data reservation room type detail - room price detail, reservation room price detail (delete:old)
                rs = session.getNamedQuery("deleteAllTblReservationRoomTypeDetailRoomPriceDetailByIDRoomTypeDetailAndDetailDateAfter")
                        .setParameter("idRoomTypeDetail", reservationRoomTypeDetailOld.getIddetail())
                        .setParameter("detailDate", Date.valueOf(currentDateForChangeRoomProcess.toLocalDate()))
                        .setParameter("idEmployee", ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee());
                errMsg = (String) rs.uniqueResult();
                if (!errMsg.equals("")) {
                    if (session.getTransaction().isActive()) {
                        session.getTransaction().rollback();
                    }
                    return false;
                }
                //data reservation room type (insert:new)
                if (checkRoomAvailable(reservationRoomTypeDetailNew, null)) {
                    //data reservation room type detail - check in/out
                    if (reservationRoomTypeDetailNew.getTblReservationCheckInOut() != null) {
                        reservationRoomTypeDetailNew.getTblReservationCheckInOut().setCreateDate(Timestamp.valueOf(LocalDateTime.now()));
                        reservationRoomTypeDetailNew.getTblReservationCheckInOut().setTblEmployeeByCreateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                        reservationRoomTypeDetailNew.getTblReservationCheckInOut().setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                        reservationRoomTypeDetailNew.getTblReservationCheckInOut().setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                        reservationRoomTypeDetailNew.getTblReservationCheckInOut().setRefRecordStatus(session.find(RefRecordStatus.class, 1));
                        session.saveOrUpdate(reservationRoomTypeDetailNew.getTblReservationCheckInOut());
                    }
                    //data reservation room type (insert:new)
                    reservationRoomTypeDetailNew.setCreateDate(Timestamp.valueOf(LocalDateTime.now()));
                    reservationRoomTypeDetailNew.setTblEmployeeByCreateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                    reservationRoomTypeDetailNew.setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                    reservationRoomTypeDetailNew.setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                    reservationRoomTypeDetailNew.setRefRecordStatus(session.find(RefRecordStatus.class, 1));
                    session.saveOrUpdate(reservationRoomTypeDetailNew);
                    //data reservation room type detail - room service (insert)
                    List<TblRoomTypeRoomService> roomTypeRoomServices = getAllRoomTypeRoomServiceByIDRoomType(reservationRoomTypeDetailNew.getTblRoomType().getIdroomType());
                    for (TblRoomTypeRoomService roomTypeRoomService : roomTypeRoomServices) {
                        TblReservationRoomTypeDetailRoomService reservationRoomTypeDetailRoomService = new TblReservationRoomTypeDetailRoomService();
                        reservationRoomTypeDetailRoomService.setTblReservationRoomTypeDetail(reservationRoomTypeDetailNew);
                        reservationRoomTypeDetailRoomService.setTblRoomService(roomTypeRoomService.getTblRoomService());
                        reservationRoomTypeDetailRoomService.setRoomServiceCost(roomTypeRoomService.getTblRoomService().getPrice());
                        reservationRoomTypeDetailRoomService.setPeopleNumber(roomTypeRoomService.getPeopleNumber());
                        reservationRoomTypeDetailRoomService.setCreateDate(Timestamp.valueOf(LocalDateTime.now()));
                        reservationRoomTypeDetailRoomService.setTblEmployeeByCreateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                        reservationRoomTypeDetailRoomService.setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                        reservationRoomTypeDetailRoomService.setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                        reservationRoomTypeDetailRoomService.setRefRecordStatus(session.find(RefRecordStatus.class, 1));
                        session.saveOrUpdate(reservationRoomTypeDetailRoomService);
                    }
                    //data reservation room type detail - item (insert)
                    List<TblRoomTypeItem> roomTypeItems = getAllRoomTypeItemByIDRoomType(reservationRoomTypeDetailNew.getTblRoomType().getIdroomType());
                    for (TblRoomTypeItem roomTypeItem : roomTypeItems) {
                        TblReservationRoomTypeDetailItem reservationRoomTypeDetailItem = new TblReservationRoomTypeDetailItem();
                        reservationRoomTypeDetailItem.setTblReservationRoomTypeDetail(reservationRoomTypeDetailNew);
                        reservationRoomTypeDetailItem.setTblItem(roomTypeItem.getTblItem());
                        reservationRoomTypeDetailItem.setItemAdditionalCharge(roomTypeItem.getTblItem().getAdditionalCharge());
                        reservationRoomTypeDetailItem.setItemBrokenCharge(roomTypeItem.getTblItem().getBrokenCharge());
                        reservationRoomTypeDetailItem.setItemQuantity(roomTypeItem.getItemQuantity());
                        reservationRoomTypeDetailItem.setCreateDate(Timestamp.valueOf(LocalDateTime.now()));
                        reservationRoomTypeDetailItem.setTblEmployeeByCreateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                        reservationRoomTypeDetailItem.setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                        reservationRoomTypeDetailItem.setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                        reservationRoomTypeDetailItem.setRefRecordStatus(session.find(RefRecordStatus.class, 1));
                        session.saveOrUpdate(reservationRoomTypeDetailItem);
                    }
                    //insert all used data additional (rrtd : new)
                    insertAllReservationAddtionalReservationRoomTypeDetail(reservationRoomTypeDetailNew);
                } else {
                    if (session.getTransaction().isActive()) {
                        session.getTransaction().rollback();
                    }
                    errMsg = "Jumlah kamar tidak mencukupi..!";
                    return false;
                }
                //data reservation room type detail - room price detail (insert:new)
                for (TblReservationRoomTypeDetailRoomPriceDetail rrtdrpd : rrtdrpds) {
                    //data reservation room price detail (insert:new)
                    rrtdrpd.getTblReservationRoomPriceDetail().setCreateDate(reservationRoomTypeDetailOld.getCreateDate());
                    rrtdrpd.getTblReservationRoomPriceDetail().setTblEmployeeByCreateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                    rrtdrpd.getTblReservationRoomPriceDetail().setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                    rrtdrpd.getTblReservationRoomPriceDetail().setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                    rrtdrpd.getTblReservationRoomPriceDetail().setRefRecordStatus(session.find(RefRecordStatus.class, 1));
                    session.saveOrUpdate(rrtdrpd.getTblReservationRoomPriceDetail());
                    //data reservation room type detail - room price detail (insert:new)
                    rrtdrpd.setCreateDate(reservationRoomTypeDetailOld.getCreateDate());
                    rrtdrpd.setTblEmployeeByCreateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                    rrtdrpd.setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                    rrtdrpd.setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                    rrtdrpd.setRefRecordStatus(session.find(RefRecordStatus.class, 1));
                    session.saveOrUpdate(rrtdrpd);
                    //data guarantee letter (if old data use 'guarantee payment'), <update old data>
                    List<TblGuaranteeLetterItemDetail> dataGuaranteeLetterItemDetails = getAllDataGuaranteeLetterItemDetailByIDReservationAndCodeRTDAndDetailDate(
                            reservationRoomTypeDetailOld.getTblReservation().getIdreservation(),
                            reservationRoomTypeDetailOld.getCodeDetail(),
                            rrtdrpd.getTblReservationRoomPriceDetail().getDetailDate());
                    for (TblGuaranteeLetterItemDetail dataGuaranteeLetterItemDetail : dataGuaranteeLetterItemDetails) {
                        dataGuaranteeLetterItemDetail.setCodeRtd(rrtdrpd.getTblReservationRoomTypeDetail().getCodeDetail());
                        dataGuaranteeLetterItemDetail.setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                        dataGuaranteeLetterItemDetail.setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                        session.update(dataGuaranteeLetterItemDetail);
                    }
                }
                //data log
                dataLog += "Reservasi Kamar : \n";
                dataLog += "- " + reservationRoomTypeDetailNew.getCodeDetail() + " - " + reservationRoomTypeDetailNew.getCheckInDateTime() + " s/d " + reservationRoomTypeDetailNew.getCheckOutDateTime() + " \t "
                        + (getDataRoom(reservationRoomTypeDetailNew)) + " \n";
                for (TblReservationRoomTypeDetailRoomPriceDetail rrtdrpd : rrtdrpds) {
                    if (rrtdrpd.getTblReservationRoomTypeDetail().getIddetail()
                            == reservationRoomTypeDetailNew.getIddetail()) {
                        dataLog += "\t - " + ClassFormatter.dateFormate.format(rrtdrpd.getTblReservationRoomPriceDetail().getDetailDate()) + " \t "
                                + "Harga : " + ClassFormatter.currencyFormat.cFormat(rrtdrpd.getTblReservationRoomPriceDetail().getDetailPrice()) + " \t "
                                + "Diskon : " + ClassFormatter.decimalFormat.cFormat(rrtdrpd.getTblReservationRoomPriceDetail().getDetailDiscountPercentage()) + "% (" + getDataDiscountEvent(rrtdrpd.getTblReservationRoomPriceDetail()) + ") \t "
                                + "Compliment : " + ClassFormatter.currencyFormat.cFormat(rrtdrpd.getTblReservationRoomPriceDetail().getDetailComplimentary()) + " \t "
                                + " \n";
                    }
                }
                dataLog += "-----------------------------------------------------------------------\n";
                //data reservation additonal item (old:delete)
                rs = session.getNamedQuery("deleteAllTblReservationAdditionalItemByIDRoomTypeDetailAndAdditionalDateAfter")
                        .setParameter("idRoomTypeDetail", reservationRoomTypeDetailOld.getIddetail())
                        .setParameter("additionalDate", Date.valueOf(currentDateForChangeRoomProcess.toLocalDate()))
                        .setParameter("idEmployee", ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee());
                errMsg = (String) rs.uniqueResult();
                if (!errMsg.equals("")) {
                    if (session.getTransaction().isActive()) {
                        session.getTransaction().rollback();
                    }
                    return false;
                }
                //data reservation additonal item - reserved (old:delete)
                rs = session.getNamedQuery("deleteAllTblReservationAdditionalItemReservedByIDRoomTypeDetailAndReservedDateAfter")
                        .setParameter("idRoomTypeDetail", reservationRoomTypeDetailOld.getIddetail())
                        .setParameter("reservedDate", Date.valueOf(currentDateForChangeRoomProcess.toLocalDate()))
                        .setParameter("idEmployee", ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee());
                errMsg = (String) rs.uniqueResult();
                if (!errMsg.equals("")) {
                    if (session.getTransaction().isActive()) {
                        session.getTransaction().rollback();
                    }
                    return false;
                }
                //data log
                dataLog += "Tambahan Barang : \n";
                //data reservation additonal item (insert:new)
                for (TblReservationAdditionalItem rai : rais) {
//                    rai.setCreateDate(Timestamp.valueOf(LocalDateTime.now()));
//                    rai.setTblEmployeeByCreateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                    rai.setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                    rai.setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                    rai.setRefRecordStatus(session.find(RefRecordStatus.class, 1));
                    session.saveOrUpdate(rai);
                    //data reservation additonal item - reserved
                    TblReservationAdditionalItemReserved reservationAdditionalItemReserved = getReservationAdditionalItemReservedByIDReservationRoomTypeDetailAndIDItemAndReservedDate(
                            rai.getTblReservationRoomTypeDetail().getIddetail(),
                            rai.getTblItem().getIditem(),
                            rai.getAdditionalDate());
                    if (reservationAdditionalItemReserved != null) {
                        reservationAdditionalItemReserved.setReservedQuantity(reservationAdditionalItemReserved.getReservedQuantity().add(rai.getItemQuantity()));
                        reservationAdditionalItemReserved.setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                        reservationAdditionalItemReserved.setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                        session.update(reservationAdditionalItemReserved);
                    } else {
                        reservationAdditionalItemReserved = new TblReservationAdditionalItemReserved();
                        reservationAdditionalItemReserved.setTblReservationRoomTypeDetail(rai.getTblReservationRoomTypeDetail());
                        reservationAdditionalItemReserved.setTblItem(rai.getTblItem());
                        reservationAdditionalItemReserved.setReservedDate(rai.getAdditionalDate());
                        reservationAdditionalItemReserved.setReservedQuantity(rai.getItemQuantity());
                        reservationAdditionalItemReserved.setInUsedQuantity(getLastInUseStock(
                                reservationRoomTypeDetailOld,
                                reservationAdditionalItemReserved.getTblItem(),
                                reservationAdditionalItemReserved.getReservedDate()));
                        reservationAdditionalItemReserved.setDoneQuantity(getLastDoneStock(
                                reservationRoomTypeDetailOld,
                                reservationAdditionalItemReserved.getTblItem(),
                                reservationAdditionalItemReserved.getReservedDate()));
                        reservationAdditionalItemReserved.setRefReservationAdditionalItemReservedStatus(session.find(RefReservationAdditionalItemReservedStatus.class, 1));     //Reserved = '1'
//                        reservationAdditionalItemReserved.setCreateDate(Timestamp.valueOf(LocalDateTime.now()));
//                        reservationAdditionalItemReserved.setTblEmployeeByCreateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                        reservationAdditionalItemReserved.setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                        reservationAdditionalItemReserved.setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                        reservationAdditionalItemReserved.setRefRecordStatus(session.find(RefRecordStatus.class, 1));
                        session.saveOrUpdate(reservationAdditionalItemReserved);
                    }
                    //data log
                    dataLog += "- " + ClassFormatter.dateFormate.format(rai.getAdditionalDate()) + " - " + rai.getTblItem().getItemName() + " \t "
                            + "Harga : " + ClassFormatter.currencyFormat.cFormat(rai.getItemCharge()) + " \t "
                            + "Diskon : " + ClassFormatter.decimalFormat.cFormat(rai.getDiscountPercentage()) + "% (" + getDataDiscountEvent(rai) + ") \t "
                            + "Jumlah : " + ClassFormatter.decimalFormat.cFormat(rai.getItemQuantity()) + " \n";
                }
                //data log
                dataLog += "-----------------------------------------------------------------------\n";
                //data reservation additonal service (old:delete)
                rs = session.getNamedQuery("deleteAllTblReservationAdditionalServiceByIDRoomTypeDetailAndAdditionalDateAfterAndIDRoomServiceNotSpecific")
                        .setParameter("idRoomTypeDetail", reservationRoomTypeDetailOld.getIddetail())
                        .setParameter("additionalDate", Date.valueOf(currentDateForChangeRoomProcess.toLocalDate()))
                        .setParameter("idEmployee", ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee());
                errMsg = (String) rs.uniqueResult();
                if (!errMsg.equals("")) {
                    if (session.getTransaction().isActive()) {
                        session.getTransaction().rollback();
                    }
                    return false;
                }
                //data log
                dataLog += "Tambahan Layanan : \n";
                //data reservation additonal service (insert:new)
                for (TblReservationAdditionalService ras : rass) {
                    if (ras.getTblRoomService().getIdroomService() == 4) { //Lainnya(Bonus Voucher) = '4'
                        ras.setCreateDate(Timestamp.valueOf(LocalDateTime.now()));
                        ras.setTblEmployeeByCreateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                    }
                    ras.setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                    ras.setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                    ras.setRefRecordStatus(session.find(RefRecordStatus.class, 1));
                    session.saveOrUpdate(ras);
                    //data log
                    dataLog += "- " + ClassFormatter.dateFormate.format(ras.getAdditionalDate()) + " - " + ras.getTblRoomService().getServiceName() + " \t "
                            + "Harga : " + ClassFormatter.currencyFormat.cFormat(ras.getPrice()) + " \t "
                            + "Diskon : " + ClassFormatter.decimalFormat.cFormat(ras.getDiscountPercentage()) + "% (" + getDataDiscountEvent(ras) + ") \n";
                }
                //data log
                dataLog += "-----------------------------------------------------------------------\n";
                dataLog += "Data Pindah Kamar - History : \n";
                //data reservation room type detail - room price detail - change room history (insert:new)
                for (TblReservationRoomTypeDetailRoomPriceDetailChangeRoomHistory changeRoomHistory : changeRoomHistories) {
                    if (reservationRoomTypeDetailOld.getTblReservationCheckInOut() != null) {
                        //data reservation room type detail - room price detail - change room history (insert:new)
                        changeRoomHistory.setCreateDate(Timestamp.valueOf(LocalDateTime.now()));
                        changeRoomHistory.setTblEmployeeByCreateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                        changeRoomHistory.setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                        changeRoomHistory.setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                        changeRoomHistory.setRefRecordStatus(session.find(RefRecordStatus.class, 1));
                        session.saveOrUpdate(changeRoomHistory);
                        dataLog += "- Dari tipe kamar '" + changeRoomHistory.getTblReservationRoomTypeDetailRoomPriceDetailByIdrelationOld().getTblReservationRoomTypeDetail().getTblRoomType().getRoomTypeName()
                                + "' menjadi '" + changeRoomHistory.getTblReservationRoomTypeDetailRoomPriceDetailByIdrelationNew().getTblReservationRoomTypeDetail().getTblRoomType().getRoomTypeName() + "' \n";
                    }
                }
                //data log
                dataLog += "-----------------------------------------------------------------------\n";
                LogSystem logSystem = new LogSystem();
                logSystem.setLogDateTime(Timestamp.valueOf(LocalDateTime.now()));
                logSystem.setTblSystemUser(session.find(TblSystemUser.class, ClassSession.currentUser.getIduser()));
                logSystem.setTblEmployee(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                logSystem.setLogHeader(headerLog);
                logSystem.setLogDetail(dataLog);
                logSystem.setHostName(ClassComp.getHostName());
                session.saveOrUpdate(logSystem);
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
    public boolean updateDataReservationRoomTypeDetailChangeRoomOutIn(
            TblReservation reservation,
            TblReservationRoomTypeDetail reservationRoomTypeDetailOld,
            TblReservationRoomTypeDetail reservationRoomTypeDetailNew,
            List<TblReservationRoomTypeDetailRoomPriceDetail> rrtdrpds,
            List<TblReservationAdditionalItem> rais,
            List<TblReservationAdditionalService> rass,
            List<TblReservationRoomTypeDetailRoomPriceDetailChangeRoomHistory> changeRoomHistories,
            List<TblReservationRoomTypeDetailChildDetail> rrtdChildDetails,
            LocalDateTime currentDateForChangeRoomProcess,
            Time defaultCheckInTime
    ) {
        errMsg = "";
        if (ClassSession.checkUserSession()) {  //check user current session
            try {
                session = HibernateUtil.getSessionFactory().getCurrentSession();
                session.beginTransaction();
                //data log
                String headerLog = "BUAT DATA PINDAH KAMAR (TIPE RESERVASI KAMAR)";
                String dataLog = "";
                Query rs;
                //data reservation
                reservation.setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                reservation.setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                session.update(reservation);
                //data log
                dataLog += "No. Reservasi : " + reservation.getCodeReservation() + "\n";
                dataLog += "Tgl. Reservasi : " + ClassFormatter.dateFormate.format(reservation.getReservationDate()) + "\n";
                dataLog += "Nama Customer : " + reservation.getTblCustomer().getTblPeople().getFullName() + "\n";
                dataLog += "Travel Agent : " + (reservation.getTblPartner() != null ? reservation.getTblPartner().getPartnerName() : "-") + "\n";
                dataLog += "-----------------------------------------------------------------------\n";
                //data reservation room type (update:old)
                reservationRoomTypeDetailOld.setCheckOutDateTime(Timestamp.valueOf(currentDateForChangeRoomProcess));
                if (LocalDateTime.of(reservationRoomTypeDetailOld.getCheckInDateTime().getYear() + 1900,
                        reservationRoomTypeDetailOld.getCheckInDateTime().getMonth() + 1,
                        reservationRoomTypeDetailOld.getCheckInDateTime().getDate(),
                        reservationRoomTypeDetailOld.getCheckInDateTime().getHours(),
                        reservationRoomTypeDetailOld.getCheckInDateTime().getMinutes(),
                        0).isEqual(LocalDateTime.of(reservationRoomTypeDetailOld.getCheckOutDateTime().getYear() + 1900,
                                        reservationRoomTypeDetailOld.getCheckOutDateTime().getMonth() + 1,
                                        reservationRoomTypeDetailOld.getCheckOutDateTime().getDate(),
                                        reservationRoomTypeDetailOld.getCheckOutDateTime().getHours(),
                                        reservationRoomTypeDetailOld.getCheckOutDateTime().getMinutes(),
                                        0))
                        && !IsReservationRoomTypeDetailHaveAdditonalService(reservationRoomTypeDetailOld, rass)) {
                    if (reservationRoomTypeDetailOld.getTblReservationCheckInOut() == null
                            || reservationRoomTypeDetailOld.getTblReservationCheckInOut().getRefReservationCheckInOutStatus().getIdstatus() == 0) {    //Ready to Check In = '0'     
                        //data reservation room type detail : delete..
                        reservationRoomTypeDetailOld.setRefRecordStatus(session.find(RefRecordStatus.class, 2));
                        //data reservation room type detail - room service (delete)
                        rs = session.getNamedQuery("deleteAllTblReservationRoomTypeDetailRoomServiceByIDRoomTypeDetail")
                                .setParameter("idRoomTypeDetail", reservationRoomTypeDetailOld.getIddetail())
                                .setParameter("idEmployee", ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee());
                        errMsg = (String) rs.uniqueResult();
                        if (!errMsg.equals("")) {
                            if (session.getTransaction().isActive()) {
                                session.getTransaction().rollback();
                            }
                            return false;
                        }
                        //data reservation room type detail - item (delete)
                        rs = session.getNamedQuery("deleteAllTblReservationRoomTypeDetailItemByIDRoomTypeDetail")
                                .setParameter("idRoomTypeDetail", reservationRoomTypeDetailOld.getIddetail())
                                .setParameter("idEmployee", ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee());
                        errMsg = (String) rs.uniqueResult();
                        if (!errMsg.equals("")) {
                            if (session.getTransaction().isActive()) {
                                session.getTransaction().rollback();
                            }
                            return false;
                        }
                        //data reservation room type detail - travel agent discount detail, reservation travel agent discount detail (delete:old)
                        rs = session.getNamedQuery("deleteAllTblReservationRoomTypeDetailTravelAgentDiscountDetailByIDRoomTypeDetail")
                                .setParameter("idRoomTypeDetail", reservationRoomTypeDetailOld.getIddetail())
                                .setParameter("idEmployee", ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee());
                        errMsg = (String) rs.uniqueResult();
                        if (!errMsg.equals("")) {
                            if (session.getTransaction().isActive()) {
                                session.getTransaction().rollback();
                            }
                            return false;
                        }
                    } else {
                        //data reservation check in/out (update:old)
                        reservationRoomTypeDetailOld.getTblReservationCheckInOut().setCheckOutDateTime(Timestamp.valueOf(LocalDateTime.now()));
                        reservationRoomTypeDetailOld.getTblReservationCheckInOut().setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                        reservationRoomTypeDetailOld.getTblReservationCheckInOut().setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                        session.saveOrUpdate(reservationRoomTypeDetailOld.getTblReservationCheckInOut());
                        if (reservationRoomTypeDetailOld.getTblReservationCheckInOut().getRefReservationCheckInOutStatus().getIdstatus() == 3) {  //Checked Out = '3'
                            //data room
                            reservationRoomTypeDetailOld.getTblReservationCheckInOut().getTblRoom().setRefRoomStatus(session.get(RefRoomStatus.class, 4)); //Vacant Dirty = '4'
                            //data room status change history
                            TblRoomStatusChangeHistory dataRoomStatusChangeHistory = new TblRoomStatusChangeHistory();
                            dataRoomStatusChangeHistory.setTblRoom(reservationRoomTypeDetailOld.getTblReservationCheckInOut().getTblRoom());
                            dataRoomStatusChangeHistory.setTblEmployeeByIdemployee(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                            dataRoomStatusChangeHistory.setTblRoomStatusDetail(null);
                            dataRoomStatusChangeHistory.setChangeRoomStatusDate(Timestamp.valueOf(LocalDateTime.now()));
                            dataRoomStatusChangeHistory.setRefRoomStatusByRoomStatusOld(getOldRoomStatus(reservationRoomTypeDetailOld.getTblReservationCheckInOut().getTblRoom().getIdroom()));
                            dataRoomStatusChangeHistory.setRefRoomStatusByRoomStatusNew(reservationRoomTypeDetailOld.getTblReservationCheckInOut().getTblRoom().getRefRoomStatus());
                            dataRoomStatusChangeHistory.setCreateDate(Timestamp.valueOf(LocalDateTime.now()));
                            dataRoomStatusChangeHistory.setTblEmployeeByCreateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                            dataRoomStatusChangeHistory.setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                            dataRoomStatusChangeHistory.setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                            dataRoomStatusChangeHistory.setRefRecordStatus(session.find(RefRecordStatus.class, 1));
                            session.saveOrUpdate(dataRoomStatusChangeHistory);
                            //data room
                            reservationRoomTypeDetailOld.getTblReservationCheckInOut().getTblRoom().setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                            reservationRoomTypeDetailOld.getTblReservationCheckInOut().getTblRoom().setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                            session.update(reservationRoomTypeDetailOld.getTblReservationCheckInOut().getTblRoom());
                            //data addtioanal item - reserved (release : 'done')
                            List<TblReservationAdditionalItemReserved> dataReservationAdditionalItemReserveds = getAllReservationAdditionalItemReservedByIDReservationRoomTypeDetail(reservationRoomTypeDetailOld.getIddetail());
                            for (TblReservationAdditionalItemReserved dataReservationAdditionalItemReserved : dataReservationAdditionalItemReserveds) {
                                dataReservationAdditionalItemReserved.setRefReservationAdditionalItemReservedStatus(session.find(RefReservationAdditionalItemReservedStatus.class, 2)); //Done = '2'
                                dataReservationAdditionalItemReserved.setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                                dataReservationAdditionalItemReserved.setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                                session.update(dataReservationAdditionalItemReserved);
                            }
                            //data reservation bill (deposit)
                            TblReservationBill dataReservationBill = getReservationBillByIDReservationAndIDReservationBillType(reservation.getIdreservation(),
                                    2); //Deposit = '2'
                            if (dataReservationBill == null) {  //create and save reservation bill
                                dataReservationBill = new TblReservationBill();
                                dataReservationBill.setCodeBill(ClassCoder.getCode("Reservation Bill", session));
                                dataReservationBill.setTblReservation(reservation);
                                dataReservationBill.setServiceChargePercentage(new BigDecimal("0"));
                                dataReservationBill.setTaxPercentage(new BigDecimal("0"));
                                dataReservationBill.setRefReservationBillType(session.find(RefReservationBillType.class, 2));
                                dataReservationBill.setCreateDate(Timestamp.valueOf(LocalDateTime.now()));
                                dataReservationBill.setTblEmployeeByCreateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                                dataReservationBill.setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                                dataReservationBill.setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                                dataReservationBill.setRefRecordStatus(session.find(RefRecordStatus.class, 1));
                                session.saveOrUpdate(dataReservationBill);
                            }
                            //data reservation payment (close-deposit)
                            TblReservationPayment dataReservationPayment = new TblReservationPayment();
                            dataReservationPayment.setCodePayment(ClassCoder.getCode("Reservation Payment", session));
                            dataReservationPayment.setTblReservationBill(dataReservationBill);
                            dataReservationPayment.setPaymentDate(Timestamp.valueOf(LocalDateTime.now()));
//            dataReservationPayment.setUnitNominal((reservationRoomTypeDetail.getTblReservationCheckInOut().getCardUsed()
//                    - reservationRoomTypeDetail.getTblReservationCheckInOut().getCardMissed())
//                    * reservationRoomTypeDetail.getTblReservationCheckInOut().getBrokenCardCharge());
                            dataReservationPayment.setUnitNominal((new BigDecimal(reservationRoomTypeDetailOld.getTblReservationCheckInOut().getCardUsed()))
                                    .multiply(reservationRoomTypeDetailOld.getTblReservationCheckInOut().getBrokenCardCharge()));
                            dataReservationPayment.setRoundingValue(new BigDecimal("0"));
                            dataReservationPayment.setTblEmployeeByIdcashier(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                            dataReservationPayment.setRefFinanceTransactionPaymentType(session.find(RefFinanceTransactionPaymentType.class, 14));
                            dataReservationPayment.setRefReservationBillType(session.find(RefReservationBillType.class, 2));    //deposit = '2'
                            dataReservationPayment.setSettleDate(Timestamp.valueOf(LocalDateTime.now()));
                            dataReservationPayment.setCreateDate(Timestamp.valueOf(LocalDateTime.now()));
                            dataReservationPayment.setTblEmployeeByCreateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                            dataReservationPayment.setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                            dataReservationPayment.setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                            dataReservationPayment.setRefRecordStatus(session.find(RefRecordStatus.class, 1));
                            session.saveOrUpdate(dataReservationPayment);
//                            //data log
//                            dataLog += "Pengembalian Deposit Kartu : " + ClassFormatter.currencyFormat.cFormat(dataReservationPayment.getUnitNominal())
//                                    + " (" + reservationRoomTypeDetail.getTblReservationCheckInOut().getCardUsed()
//                                    + " kartu @" + ClassFormatter.currencyFormat.cFormat(reservationRoomTypeDetail.getTblReservationCheckInOut().getBrokenCardCharge()) + ")\n";
//                            dataLog += "-----------------------------------------------------------------------\n";
                            //data reservation payment with deposit
                            TblReservationPaymentWithDeposit dataReservationPaymentWithDeposit = new TblReservationPaymentWithDeposit();
                            dataReservationPaymentWithDeposit.setTblReservationPayment(dataReservationPayment);
                            dataReservationPaymentWithDeposit.setTblReservationCheckInOut(reservationRoomTypeDetailOld.getTblReservationCheckInOut());
                            dataReservationPaymentWithDeposit.setCreateDate(Timestamp.valueOf(LocalDateTime.now()));
                            dataReservationPaymentWithDeposit.setTblEmployeeByCreateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                            dataReservationPaymentWithDeposit.setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                            dataReservationPaymentWithDeposit.setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                            dataReservationPaymentWithDeposit.setRefRecordStatus(session.find(RefRecordStatus.class, 1));
                            session.saveOrUpdate(dataReservationPaymentWithDeposit);
                            //@@@%%%
                            //data deposit balance (update:-)
                            TblCompanyBalance dataDepositBalance = session.find(TblCompanyBalance.class, (long) 4); //Kas Deposit = '4'
                            dataDepositBalance.setBalanceNominal(dataDepositBalance.getBalanceNominal().subtract(dataReservationPayment.getUnitNominal()));
                            dataDepositBalance.setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                            dataDepositBalance.setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                            session.update(dataDepositBalance);
                            //data company balance (cash-flow log)
                            LogCompanyBalanceCashFlow logCompanyBalanceCashFlow = new LogCompanyBalanceCashFlow();
                            logCompanyBalanceCashFlow.setTblCompanyBalanceByIdsenderCompanyBalance(dataDepositBalance);
                            logCompanyBalanceCashFlow.setTblBankAccountByIdsenderCbbankAccount(null);
                            logCompanyBalanceCashFlow.setTblCompanyBalanceByIdreceiverCompanyBalance(null);
                            logCompanyBalanceCashFlow.setTblBankAccountByIdreceiverCbbankAccount(null);
                            logCompanyBalanceCashFlow.setTransferNominal(dataReservationPayment.getUnitNominal());
                            logCompanyBalanceCashFlow.setHistoryDate(Timestamp.valueOf(LocalDateTime.now()));
                            logCompanyBalanceCashFlow.setTblEmployee(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                            logCompanyBalanceCashFlow.setHistoryStystemNote("Pengembalian deposit kartu kamar kepada " + reservation.getTblCustomer().getTblPeople().getFullName() + ", \nsebesar : "
                                    + ClassFormatter.currencyFormat.format(dataReservationPayment.getUnitNominal()) + " ("
                                    + dataReservationPayment.getRefFinanceTransactionPaymentType().getTypeName() + ")");
                            session.saveOrUpdate(logCompanyBalanceCashFlow);
                            //data company balance (cashier)
                            TblCompanyBalance dataCashierBalance = session.find(TblCompanyBalance.class, (long) 3); //Kas Kasir = '3'
                            //open close company balance
                            TblOpenCloseCashierBalance dataOpenCloseCashierBalance = getDataOpenCloseCashierBalanceIsActive();
                            //open close company balance - detail (cashier)
                            if (dataOpenCloseCashierBalance != null) {
                                TblOpenCloseCashierBalanceDetail dataOpenCloseCashierBalanceDetail = new TblOpenCloseCashierBalanceDetail();
                                dataOpenCloseCashierBalanceDetail.setTblOpenCloseCashierBalance(dataOpenCloseCashierBalance);
                                dataOpenCloseCashierBalanceDetail.setTblReservationPayment(dataReservationPayment);
                                dataOpenCloseCashierBalanceDetail.setDetailData(getDetailData(dataReservationPayment));
                                dataOpenCloseCashierBalanceDetail.setCreateDate(Timestamp.valueOf(LocalDateTime.now()));
                                dataOpenCloseCashierBalanceDetail.setTblEmployeeByCreateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                                dataOpenCloseCashierBalanceDetail.setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                                dataOpenCloseCashierBalanceDetail.setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                                dataOpenCloseCashierBalanceDetail.setRefRecordStatus(session.find(RefRecordStatus.class, 1));
                                session.saveOrUpdate(dataOpenCloseCashierBalanceDetail);
                            }
                            //if missed card number > 0
                            if (reservationRoomTypeDetailOld.getTblReservationCheckInOut().getCardMissed() > 0) {
                                //data reservation payment (cash)
                                TblReservationPayment dataReservationPaymentForMissedCard = new TblReservationPayment();
                                dataReservationPaymentForMissedCard.setCodePayment(ClassCoder.getCode("Reservation Payment", session));
                                dataReservationPaymentForMissedCard.setPaymentDate(Timestamp.valueOf(LocalDateTime.now()));
                                dataReservationPaymentForMissedCard.setUnitNominal((new BigDecimal(reservationRoomTypeDetailOld.getTblReservationCheckInOut().getCardMissed()))
                                        .multiply(reservationRoomTypeDetailOld.getTblReservationCheckInOut().getBrokenCardCharge()));
                                dataReservationPaymentForMissedCard.setRoundingValue(new BigDecimal("0"));
                                dataReservationPaymentForMissedCard.setTblEmployeeByIdcashier(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                                dataReservationPaymentForMissedCard.setRefFinanceTransactionPaymentType(session.find(RefFinanceTransactionPaymentType.class, 0));   //Tunai = '0'
                                dataReservationPaymentForMissedCard.setRefReservationBillType(session.find(RefReservationBillType.class, 2));   //deposit = '2'
                                dataReservationPaymentForMissedCard.setCreateDate(Timestamp.valueOf(LocalDateTime.now()));
                                dataReservationPaymentForMissedCard.setTblEmployeeByCreateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                                dataReservationPaymentForMissedCard.setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                                dataReservationPaymentForMissedCard.setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                                dataReservationPaymentForMissedCard.setRefRecordStatus(session.find(RefRecordStatus.class, 1));
                                session.saveOrUpdate(dataReservationPaymentForMissedCard);
                                //@@@%%%
                                //data cashier balance (update:+)
                                dataCashierBalance.setBalanceNominal(dataCashierBalance.getBalanceNominal().add(dataReservationPaymentForMissedCard.getUnitNominal()));
                                dataCashierBalance.setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                                dataCashierBalance.setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                                session.update(dataCashierBalance);
                                //data item : guest card (perlengkapan - guest)
                                TblItem itemGuestCard;
                                SysDataHardCode sdhItemGuestCard = session.find(SysDataHardCode.class, (long) 1);   //IDGuestCard = '1'
                                if (sdhItemGuestCard == null
                                        || sdhItemGuestCard.getDataHardCodeValue() == null) {
                                    if (session.getTransaction().isActive()) {
                                        session.getTransaction().rollback();
                                    }
                                    errMsg = "Data Kartu Kamar - Tidak Ditemukan..!";
                                    return false;
                                } else {
                                    itemGuestCard = session.find(TblItem.class, Long.valueOf(sdhItemGuestCard.getDataHardCodeValue()));
                                    if (itemGuestCard == null) {
                                        if (session.getTransaction().isActive()) {
                                            session.getTransaction().rollback();
                                        }
                                        errMsg = "Data Kartu Kamar - Tidak Ditemukan..!";
                                        return false;
                                    }
                                }
                                //data location : front offce
                                TblLocation locationFO;
                                SysDataHardCode sdhLocationFrontOffice = session.find(SysDataHardCode.class, (long) 2);   //IDFrontOffice = '2'
                                if (sdhLocationFrontOffice == null
                                        || sdhLocationFrontOffice.getDataHardCodeValue() == null) {
                                    if (session.getTransaction().isActive()) {
                                        session.getTransaction().rollback();
                                    }
                                    errMsg = "Data Lokasi Front Office - Tidak Ditemukan..!";
                                    return false;
                                } else {
                                    locationFO = session.find(TblLocation.class, Long.valueOf(sdhLocationFrontOffice.getDataHardCodeValue()));
                                    if (locationFO == null) {
                                        if (session.getTransaction().isActive()) {
                                            session.getTransaction().rollback();
                                        }
                                        errMsg = "Data Lokasi Front Office - Tidak Ditemukan..!";
                                        return false;
                                    }
                                }
                                //data item-location : guest card - front office
                                TblItemLocation itemLocation = getItemLocationByIDLocationAndIDItem(locationFO.getIdlocation(), itemGuestCard.getIditem());
                                if (itemLocation == null
                                        || reservationRoomTypeDetailOld.getTblReservationCheckInOut().getCardMissed() > itemLocation.getItemQuantity().intValue()) {
                                    if (session.getTransaction().isActive()) {
                                        session.getTransaction().rollback();
                                    }
                                    errMsg = "Stok Kartu Kamar - Tidak Cukup..!";
                                    return false;
                                }
                                TblLocationOfBin bin = getBin();
                                if (bin != null) {
                                    if (itemLocation.getItemQuantity()
                                            .compareTo(new BigDecimal("0")) == 1) {
                                        //data item mutation history
                                        TblItemMutationHistory dataMutation = new TblItemMutationHistory();
                                        dataMutation.setItemQuantity(new BigDecimal(String.valueOf(reservationRoomTypeDetailOld.getTblReservationCheckInOut().getCardMissed())));
                                        dataMutation.setTblItem(itemLocation.getTblItem());
                                        dataMutation.setTblLocationByIdlocationOfSource(itemLocation.getTblLocation());
                                        dataMutation.setTblLocationByIdlocationOfDestination(bin.getTblLocation());
                                        dataMutation.setMutationDate(Timestamp.valueOf(LocalDateTime.now()));
//                            dataMutation.setTblEmployeeByIdemployee(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                                        dataMutation.setRefItemMutationType(session.find(RefItemMutationType.class, 2));
                                        dataMutation.setRefItemObsoleteBy(session.find(RefItemObsoleteBy.class, 0));
//                            dataMutation.setTblEmployeeByIdemployee(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                                        dataMutation.setCreateDate(Timestamp.valueOf(LocalDateTime.now()));
                                        dataMutation.setTblEmployeeByCreateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                                        dataMutation.setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                                        dataMutation.setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                                        dataMutation.setRefRecordStatus(session.find(RefRecordStatus.class, 1));
                                        session.saveOrUpdate(dataMutation);
                                        //data item location (source)
                                        itemLocation.setItemQuantity(itemLocation.getItemQuantity().subtract(dataMutation.getItemQuantity()));
                                        itemLocation.setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                                        itemLocation.setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                                        session.update(itemLocation);
                                        //data item location (destination) : bin
                                        TblItemLocation destinationItemLocation = getItemLocationByIDLocationAndIDItem(dataMutation.getTblLocationByIdlocationOfDestination().getIdlocation(),
                                                dataMutation.getTblItem().getIditem());
                                        if (destinationItemLocation == null) {
                                            destinationItemLocation = new TblItemLocation();
                                            destinationItemLocation.setTblItem(itemLocation.getTblItem());
                                            destinationItemLocation.setTblLocation(getBin().getTblLocation());
                                            destinationItemLocation.setItemQuantity(new BigDecimal("0"));
                                            destinationItemLocation.setCreateDate(Timestamp.valueOf(LocalDateTime.now()));
                                            destinationItemLocation.setTblEmployeeByCreateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                                            destinationItemLocation.setRefRecordStatus(session.find(RefRecordStatus.class, 1));
                                        }
                                        destinationItemLocation.setItemQuantity(destinationItemLocation.getItemQuantity().add(dataMutation.getItemQuantity()));
                                        destinationItemLocation.setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                                        destinationItemLocation.setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                                        session.saveOrUpdate(destinationItemLocation);
                                    }
                                    session.evict(bin);
                                } else {
                                    if (session.getTransaction().isActive()) {
                                        session.getTransaction().rollback();
                                    }
                                    errMsg = "Data Bin - Tidak Ditemukan..!";
                                    return false;
                                }
//                                //data log
//                                dataLog += "Denda Kartu Hilang/Rusak : " + ClassFormatter.currencyFormat.cFormat(dataReservationPaymentForMissedCard.getUnitNominal())
//                                        + " (" + reservationRoomTypeDetailOld.getTblReservationCheckInOut().getCardMissed()
//                                        + " kartu @" + ClassFormatter.currencyFormat.cFormat(reservationRoomTypeDetailOld.getTblReservationCheckInOut().getBrokenCardCharge()) + ")\n";
//                                dataLog += "-----------------------------------------------------------------------\n";
//                                dataLog += "Kurangi Stok Kartu : " + reservationRoomTypeDetailOld.getTblReservationCheckInOut().getCardMissed() + "(jumlah kartu) \n"
//                                        + "- Data Kartu : " + itemGuestCard.getCodeItem() + " - " + itemGuestCard.getItemName() + " \n"
//                                        + "- Data Lokasi Kartu : " + getDataLocationOfWarehouseName(locationFO) + " \n";
//                                dataLog += "-----------------------------------------------------------------------\n";
                                //open close company balance - detail (cashier)
                                if (dataOpenCloseCashierBalance != null) {
                                    TblOpenCloseCashierBalanceDetail dataOpenCloseCashierBalanceDetailMissedCard = new TblOpenCloseCashierBalanceDetail();
                                    dataOpenCloseCashierBalanceDetailMissedCard.setTblOpenCloseCashierBalance(dataOpenCloseCashierBalance);
                                    dataOpenCloseCashierBalanceDetailMissedCard.setTblReservationPayment(dataReservationPaymentForMissedCard);
                                    dataOpenCloseCashierBalanceDetailMissedCard.setDetailData(getDetailData(dataReservationPaymentForMissedCard));
                                    dataOpenCloseCashierBalanceDetailMissedCard.setCreateDate(Timestamp.valueOf(LocalDateTime.now()));
                                    dataOpenCloseCashierBalanceDetailMissedCard.setTblEmployeeByCreateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                                    dataOpenCloseCashierBalanceDetailMissedCard.setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                                    dataOpenCloseCashierBalanceDetailMissedCard.setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                                    dataOpenCloseCashierBalanceDetailMissedCard.setRefRecordStatus(session.find(RefRecordStatus.class, 1));
                                    session.saveOrUpdate(dataOpenCloseCashierBalanceDetailMissedCard);
                                }
                            }
                        }
                        //update room status
                        if (reservationRoomTypeDetailOld.getTblReservationCheckInOut() != null
                                && reservationRoomTypeDetailOld.getTblReservationCheckInOut().getTblRoom() != null
                                && reservationRoomTypeDetailOld.getTblReservationCheckInOut().getTblRoom().getRefRoomStatus() != null) {
                            if (reservationRoomTypeDetailOld.getTblReservationCheckInOut().getTblRoom().getRefRoomStatus().getIdstatus() == 1) {  //Occupied Clean = '1'
                                reservationRoomTypeDetailOld.getTblReservationCheckInOut().getTblRoom().setRefRoomStatus(session.find(RefRoomStatus.class, 3)); //Vacant Clean = '3'
                                session.update(reservationRoomTypeDetailOld.getTblReservationCheckInOut().getTblRoom());
                            } else {
                                if (reservationRoomTypeDetailOld.getTblReservationCheckInOut().getTblRoom().getRefRoomStatus().getIdstatus() == 2) {  //Occupied Dirty = '2'
                                    reservationRoomTypeDetailOld.getTblReservationCheckInOut().getTblRoom().setRefRoomStatus(session.find(RefRoomStatus.class, 4)); //Vacant Dirty = '4'
                                    session.update(reservationRoomTypeDetailOld.getTblReservationCheckInOut().getTblRoom());
                                }
                            }
                        }
                        //delete all unused data additional (rrtd : old)
                        deleteAllReservationAddtionalByIDReservationRoomTypeDetailAndAfterOrEqualsDate(
                                reservationRoomTypeDetailOld.getIddetail(),
                                currentDateForChangeRoomProcess,
                                defaultCheckInTime);
                    }
                } else {
                    if (reservationRoomTypeDetailOld.getTblReservationCheckInOut() == null
                            || reservationRoomTypeDetailOld.getTblReservationCheckInOut().getRefReservationCheckInOutStatus().getIdstatus() == 0) {     //Ready to Check In = '0'
                        //data reservation check in/out (insert:old)
                        TblReservationCheckInOut dataCheckInOut = new TblReservationCheckInOut();
                        dataCheckInOut.setTblRoom(null);
                        dataCheckInOut.setCheckInDateTime(null);
                        dataCheckInOut.setCheckOutDateTime(null);
                        dataCheckInOut.setAdultNumber(0);
                        dataCheckInOut.setChildNumber(0);
                        dataCheckInOut.setCardUsed(0);
                        dataCheckInOut.setCardMissed(0);
                        dataCheckInOut.setBrokenCardCharge(new BigDecimal("0"));
                        dataCheckInOut.setCardAdditional(0);
                        dataCheckInOut.setAdditionalCardCharge(new BigDecimal("0"));
                        dataCheckInOut.setRefReservationCheckInOutStatus(session.find(RefReservationCheckInOutStatus.class, 4)); //Tidak Berlaku = '4'
                        dataCheckInOut.setCreateDate(Timestamp.valueOf(LocalDateTime.now()));
                        dataCheckInOut.setTblEmployeeByCreateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                        dataCheckInOut.setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                        dataCheckInOut.setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                        dataCheckInOut.setRefRecordStatus(session.find(RefRecordStatus.class, 1));
                        session.saveOrUpdate(dataCheckInOut);
                        //data reservation room type detail
                        reservationRoomTypeDetailOld.setTblReservationCheckInOut(dataCheckInOut);
                        //delete all unused data additional (rrtd : old)
                        deleteAllReservationAddtionalByIDReservationRoomTypeDetailAndAfterOrEqualsDate(
                                reservationRoomTypeDetailOld.getIddetail(),
                                currentDateForChangeRoomProcess,
                                defaultCheckInTime);
                    } else {
                        //data reservation check in/out (update:old)
                        reservationRoomTypeDetailOld.getTblReservationCheckInOut().setCheckOutDateTime(Timestamp.valueOf(LocalDateTime.now()));
                        reservationRoomTypeDetailOld.getTblReservationCheckInOut().setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                        reservationRoomTypeDetailOld.getTblReservationCheckInOut().setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                        session.saveOrUpdate(reservationRoomTypeDetailOld.getTblReservationCheckInOut());
                        if (reservationRoomTypeDetailOld.getTblReservationCheckInOut().getRefReservationCheckInOutStatus().getIdstatus() == 3) {  //Checked Out = '3'
                            //data room
                            reservationRoomTypeDetailOld.getTblReservationCheckInOut().getTblRoom().setRefRoomStatus(session.get(RefRoomStatus.class, 4)); //Vacant Dirty = '4'
                            //data room status change history
                            TblRoomStatusChangeHistory dataRoomStatusChangeHistory = new TblRoomStatusChangeHistory();
                            dataRoomStatusChangeHistory.setTblRoom(reservationRoomTypeDetailOld.getTblReservationCheckInOut().getTblRoom());
                            dataRoomStatusChangeHistory.setTblEmployeeByIdemployee(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                            dataRoomStatusChangeHistory.setTblRoomStatusDetail(null);
                            dataRoomStatusChangeHistory.setChangeRoomStatusDate(Timestamp.valueOf(LocalDateTime.now()));
                            dataRoomStatusChangeHistory.setRefRoomStatusByRoomStatusOld(getOldRoomStatus(reservationRoomTypeDetailOld.getTblReservationCheckInOut().getTblRoom().getIdroom()));
                            dataRoomStatusChangeHistory.setRefRoomStatusByRoomStatusNew(reservationRoomTypeDetailOld.getTblReservationCheckInOut().getTblRoom().getRefRoomStatus());
                            dataRoomStatusChangeHistory.setCreateDate(Timestamp.valueOf(LocalDateTime.now()));
                            dataRoomStatusChangeHistory.setTblEmployeeByCreateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                            dataRoomStatusChangeHistory.setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                            dataRoomStatusChangeHistory.setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                            dataRoomStatusChangeHistory.setRefRecordStatus(session.find(RefRecordStatus.class, 1));
                            session.saveOrUpdate(dataRoomStatusChangeHistory);
                            //data room
                            reservationRoomTypeDetailOld.getTblReservationCheckInOut().getTblRoom().setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                            reservationRoomTypeDetailOld.getTblReservationCheckInOut().getTblRoom().setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                            session.update(reservationRoomTypeDetailOld.getTblReservationCheckInOut().getTblRoom());
                            //data addtioanal item - reserved (release : 'done')
                            List<TblReservationAdditionalItemReserved> dataReservationAdditionalItemReserveds = getAllReservationAdditionalItemReservedByIDReservationRoomTypeDetail(reservationRoomTypeDetailOld.getIddetail());
                            for (TblReservationAdditionalItemReserved dataReservationAdditionalItemReserved : dataReservationAdditionalItemReserveds) {
                                dataReservationAdditionalItemReserved.setRefReservationAdditionalItemReservedStatus(session.find(RefReservationAdditionalItemReservedStatus.class, 2)); //Done = '2'
                                dataReservationAdditionalItemReserved.setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                                dataReservationAdditionalItemReserved.setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                                session.update(dataReservationAdditionalItemReserved);
                            }
                            //data reservation bill (deposit)
                            TblReservationBill dataReservationBill = getReservationBillByIDReservationAndIDReservationBillType(reservation.getIdreservation(),
                                    2); //Deposit = '2'
                            if (dataReservationBill == null) {  //create and save reservation bill
                                dataReservationBill = new TblReservationBill();
                                dataReservationBill.setCodeBill(ClassCoder.getCode("Reservation Bill", session));
                                dataReservationBill.setTblReservation(reservation);
                                dataReservationBill.setServiceChargePercentage(new BigDecimal("0"));
                                dataReservationBill.setTaxPercentage(new BigDecimal("0"));
                                dataReservationBill.setRefReservationBillType(session.find(RefReservationBillType.class, 2));
                                dataReservationBill.setCreateDate(Timestamp.valueOf(LocalDateTime.now()));
                                dataReservationBill.setTblEmployeeByCreateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                                dataReservationBill.setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                                dataReservationBill.setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                                dataReservationBill.setRefRecordStatus(session.find(RefRecordStatus.class, 1));
                                session.saveOrUpdate(dataReservationBill);
                            }
                            //data reservation payment (close-deposit)
                            TblReservationPayment dataReservationPayment = new TblReservationPayment();
                            dataReservationPayment.setCodePayment(ClassCoder.getCode("Reservation Payment", session));
                            dataReservationPayment.setTblReservationBill(dataReservationBill);
                            dataReservationPayment.setPaymentDate(Timestamp.valueOf(LocalDateTime.now()));
//            dataReservationPayment.setUnitNominal((reservationRoomTypeDetail.getTblReservationCheckInOut().getCardUsed()
//                    - reservationRoomTypeDetail.getTblReservationCheckInOut().getCardMissed())
//                    * reservationRoomTypeDetail.getTblReservationCheckInOut().getBrokenCardCharge());
                            dataReservationPayment.setUnitNominal((new BigDecimal(reservationRoomTypeDetailOld.getTblReservationCheckInOut().getCardUsed()))
                                    .multiply(reservationRoomTypeDetailOld.getTblReservationCheckInOut().getBrokenCardCharge()));
                            dataReservationPayment.setRoundingValue(new BigDecimal("0"));
                            dataReservationPayment.setTblEmployeeByIdcashier(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                            dataReservationPayment.setRefFinanceTransactionPaymentType(session.find(RefFinanceTransactionPaymentType.class, 14));
                            dataReservationPayment.setRefReservationBillType(session.find(RefReservationBillType.class, 2));    //deposit = '2'
                            dataReservationPayment.setSettleDate(Timestamp.valueOf(LocalDateTime.now()));
                            dataReservationPayment.setCreateDate(Timestamp.valueOf(LocalDateTime.now()));
                            dataReservationPayment.setTblEmployeeByCreateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                            dataReservationPayment.setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                            dataReservationPayment.setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                            dataReservationPayment.setRefRecordStatus(session.find(RefRecordStatus.class, 1));
                            session.saveOrUpdate(dataReservationPayment);
//                            //data log
//                            dataLog += "Pengembalian Deposit Kartu : " + ClassFormatter.currencyFormat.cFormat(dataReservationPayment.getUnitNominal())
//                                    + " (" + reservationRoomTypeDetailOld.getTblReservationCheckInOut().getCardUsed()
//                                    + " kartu @" + ClassFormatter.currencyFormat.cFormat(reservationRoomTypeDetailOld.getTblReservationCheckInOut().getBrokenCardCharge()) + ")\n";
//                            dataLog += "-----------------------------------------------------------------------\n";
                            //data reservation payment with deposit
                            TblReservationPaymentWithDeposit dataReservationPaymentWithDeposit = new TblReservationPaymentWithDeposit();
                            dataReservationPaymentWithDeposit.setTblReservationPayment(dataReservationPayment);
                            dataReservationPaymentWithDeposit.setTblReservationCheckInOut(reservationRoomTypeDetailOld.getTblReservationCheckInOut());
                            dataReservationPaymentWithDeposit.setCreateDate(Timestamp.valueOf(LocalDateTime.now()));
                            dataReservationPaymentWithDeposit.setTblEmployeeByCreateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                            dataReservationPaymentWithDeposit.setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                            dataReservationPaymentWithDeposit.setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                            dataReservationPaymentWithDeposit.setRefRecordStatus(session.find(RefRecordStatus.class, 1));
                            session.saveOrUpdate(dataReservationPaymentWithDeposit);
                            //@@@%%%
                            //data deposit balance (update:-)
                            TblCompanyBalance dataDepositBalance = session.find(TblCompanyBalance.class, (long) 4); //Kas Deposit = '4'
                            dataDepositBalance.setBalanceNominal(dataDepositBalance.getBalanceNominal().subtract(dataReservationPayment.getUnitNominal()));
                            dataDepositBalance.setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                            dataDepositBalance.setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                            session.update(dataDepositBalance);
                            //data company balance (cash-flow log)
                            LogCompanyBalanceCashFlow logCompanyBalanceCashFlow = new LogCompanyBalanceCashFlow();
                            logCompanyBalanceCashFlow.setTblCompanyBalanceByIdsenderCompanyBalance(dataDepositBalance);
                            logCompanyBalanceCashFlow.setTblBankAccountByIdsenderCbbankAccount(null);
                            logCompanyBalanceCashFlow.setTblCompanyBalanceByIdreceiverCompanyBalance(null);
                            logCompanyBalanceCashFlow.setTblBankAccountByIdreceiverCbbankAccount(null);
                            logCompanyBalanceCashFlow.setTransferNominal(dataReservationPayment.getUnitNominal());
                            logCompanyBalanceCashFlow.setHistoryDate(Timestamp.valueOf(LocalDateTime.now()));
                            logCompanyBalanceCashFlow.setTblEmployee(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                            logCompanyBalanceCashFlow.setHistoryStystemNote("Pengembalian deposit kartu kamar kepada " + reservation.getTblCustomer().getTblPeople().getFullName() + ", \nsebesar : "
                                    + ClassFormatter.currencyFormat.format(dataReservationPayment.getUnitNominal()) + " ("
                                    + dataReservationPayment.getRefFinanceTransactionPaymentType().getTypeName() + ")");
                            session.saveOrUpdate(logCompanyBalanceCashFlow);
                            //data company balance (cashier)
                            TblCompanyBalance dataCashierBalance = session.find(TblCompanyBalance.class, (long) 3); //Kas Kasir = '3'
                            //open close company balance
                            TblOpenCloseCashierBalance dataOpenCloseCashierBalance = getDataOpenCloseCashierBalanceIsActive();
                            //open close company balance - detail (cashier)
                            if (dataOpenCloseCashierBalance != null) {
                                TblOpenCloseCashierBalanceDetail dataOpenCloseCashierBalanceDetail = new TblOpenCloseCashierBalanceDetail();
                                dataOpenCloseCashierBalanceDetail.setTblOpenCloseCashierBalance(dataOpenCloseCashierBalance);
                                dataOpenCloseCashierBalanceDetail.setTblReservationPayment(dataReservationPayment);
                                dataOpenCloseCashierBalanceDetail.setDetailData(getDetailData(dataReservationPayment));
                                dataOpenCloseCashierBalanceDetail.setCreateDate(Timestamp.valueOf(LocalDateTime.now()));
                                dataOpenCloseCashierBalanceDetail.setTblEmployeeByCreateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                                dataOpenCloseCashierBalanceDetail.setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                                dataOpenCloseCashierBalanceDetail.setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                                dataOpenCloseCashierBalanceDetail.setRefRecordStatus(session.find(RefRecordStatus.class, 1));
                                session.saveOrUpdate(dataOpenCloseCashierBalanceDetail);
                            }
                            //if missed card number > 0
                            if (reservationRoomTypeDetailOld.getTblReservationCheckInOut().getCardMissed() > 0) {
                                //data reservation payment (cash)
                                TblReservationPayment dataReservationPaymentForMissedCard = new TblReservationPayment();
                                dataReservationPaymentForMissedCard.setCodePayment(ClassCoder.getCode("Reservation Payment", session));
                                dataReservationPaymentForMissedCard.setPaymentDate(Timestamp.valueOf(LocalDateTime.now()));
                                dataReservationPaymentForMissedCard.setUnitNominal((new BigDecimal(reservationRoomTypeDetailOld.getTblReservationCheckInOut().getCardMissed()))
                                        .multiply(reservationRoomTypeDetailOld.getTblReservationCheckInOut().getBrokenCardCharge()));
                                dataReservationPaymentForMissedCard.setRoundingValue(new BigDecimal("0"));
                                dataReservationPaymentForMissedCard.setTblEmployeeByIdcashier(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                                dataReservationPaymentForMissedCard.setRefFinanceTransactionPaymentType(session.find(RefFinanceTransactionPaymentType.class, 0));   //Tunai = '0'
                                dataReservationPaymentForMissedCard.setRefReservationBillType(session.find(RefReservationBillType.class, 2));   //deposit = '2'
                                dataReservationPaymentForMissedCard.setCreateDate(Timestamp.valueOf(LocalDateTime.now()));
                                dataReservationPaymentForMissedCard.setTblEmployeeByCreateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                                dataReservationPaymentForMissedCard.setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                                dataReservationPaymentForMissedCard.setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                                dataReservationPaymentForMissedCard.setRefRecordStatus(session.find(RefRecordStatus.class, 1));
                                session.saveOrUpdate(dataReservationPaymentForMissedCard);
                                //@@@%%%
                                //data cashier balance (update:+)
                                dataCashierBalance.setBalanceNominal(dataCashierBalance.getBalanceNominal().add(dataReservationPaymentForMissedCard.getUnitNominal()));
                                dataCashierBalance.setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                                dataCashierBalance.setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                                session.update(dataCashierBalance);
                                //data item : guest card (perlengkapan - guest)
                                TblItem itemGuestCard;
                                SysDataHardCode sdhItemGuestCard = session.find(SysDataHardCode.class, (long) 1);   //IDGuestCard = '1'
                                if (sdhItemGuestCard == null
                                        || sdhItemGuestCard.getDataHardCodeValue() == null) {
                                    if (session.getTransaction().isActive()) {
                                        session.getTransaction().rollback();
                                    }
                                    errMsg = "Data Kartu Kamar - Tidak Ditemukan..!";
                                    return false;
                                } else {
                                    itemGuestCard = session.find(TblItem.class, Long.valueOf(sdhItemGuestCard.getDataHardCodeValue()));
                                    if (itemGuestCard == null) {
                                        if (session.getTransaction().isActive()) {
                                            session.getTransaction().rollback();
                                        }
                                        errMsg = "Data Kartu Kamar - Tidak Ditemukan..!";
                                        return false;
                                    }
                                }
                                //data location : front offce
                                TblLocation locationFO;
                                SysDataHardCode sdhLocationFrontOffice = session.find(SysDataHardCode.class, (long) 2);   //IDFrontOffice = '2'
                                if (sdhLocationFrontOffice == null
                                        || sdhLocationFrontOffice.getDataHardCodeValue() == null) {
                                    if (session.getTransaction().isActive()) {
                                        session.getTransaction().rollback();
                                    }
                                    errMsg = "Data Lokasi Front Office - Tidak Ditemukan..!";
                                    return false;
                                } else {
                                    locationFO = session.find(TblLocation.class, Long.valueOf(sdhLocationFrontOffice.getDataHardCodeValue()));
                                    if (locationFO == null) {
                                        if (session.getTransaction().isActive()) {
                                            session.getTransaction().rollback();
                                        }
                                        errMsg = "Data Lokasi Front Office - Tidak Ditemukan..!";
                                        return false;
                                    }
                                }
                                //data item-location : guest card - front office
                                TblItemLocation itemLocation = getItemLocationByIDLocationAndIDItem(locationFO.getIdlocation(), itemGuestCard.getIditem());
                                if (itemLocation == null
                                        || reservationRoomTypeDetailOld.getTblReservationCheckInOut().getCardMissed() > itemLocation.getItemQuantity().intValue()) {
                                    if (session.getTransaction().isActive()) {
                                        session.getTransaction().rollback();
                                    }
                                    errMsg = "Stok Kartu Kamar - Tidak Cukup..!";
                                    return false;
                                }
                                TblLocationOfBin bin = getBin();
                                if (bin != null) {
                                    if (itemLocation.getItemQuantity()
                                            .compareTo(new BigDecimal("0")) == 1) {
                                        //data item mutation history
                                        TblItemMutationHistory dataMutation = new TblItemMutationHistory();
                                        dataMutation.setItemQuantity(new BigDecimal(String.valueOf(reservationRoomTypeDetailOld.getTblReservationCheckInOut().getCardMissed())));
                                        dataMutation.setTblItem(itemLocation.getTblItem());
                                        dataMutation.setTblLocationByIdlocationOfSource(itemLocation.getTblLocation());
                                        dataMutation.setTblLocationByIdlocationOfDestination(bin.getTblLocation());
                                        dataMutation.setMutationDate(Timestamp.valueOf(LocalDateTime.now()));
//                            dataMutation.setTblEmployeeByIdemployee(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                                        dataMutation.setRefItemMutationType(session.find(RefItemMutationType.class, 2));
                                        dataMutation.setRefItemObsoleteBy(session.find(RefItemObsoleteBy.class, 0));
//                            dataMutation.setTblEmployeeByIdemployee(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                                        dataMutation.setCreateDate(Timestamp.valueOf(LocalDateTime.now()));
                                        dataMutation.setTblEmployeeByCreateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                                        dataMutation.setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                                        dataMutation.setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                                        dataMutation.setRefRecordStatus(session.find(RefRecordStatus.class, 1));
                                        session.saveOrUpdate(dataMutation);
                                        //data item location (source)
                                        itemLocation.setItemQuantity(itemLocation.getItemQuantity().subtract(dataMutation.getItemQuantity()));
                                        itemLocation.setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                                        itemLocation.setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                                        session.update(itemLocation);
                                        //data item location (destination) : bin
                                        TblItemLocation destinationItemLocation = getItemLocationByIDLocationAndIDItem(dataMutation.getTblLocationByIdlocationOfDestination().getIdlocation(),
                                                dataMutation.getTblItem().getIditem());
                                        if (destinationItemLocation == null) {
                                            destinationItemLocation = new TblItemLocation();
                                            destinationItemLocation.setTblItem(itemLocation.getTblItem());
                                            destinationItemLocation.setTblLocation(getBin().getTblLocation());
                                            destinationItemLocation.setItemQuantity(new BigDecimal("0"));
                                            destinationItemLocation.setCreateDate(Timestamp.valueOf(LocalDateTime.now()));
                                            destinationItemLocation.setTblEmployeeByCreateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                                            destinationItemLocation.setRefRecordStatus(session.find(RefRecordStatus.class, 1));
                                        }
                                        destinationItemLocation.setItemQuantity(destinationItemLocation.getItemQuantity().add(dataMutation.getItemQuantity()));
                                        destinationItemLocation.setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                                        destinationItemLocation.setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                                        session.saveOrUpdate(destinationItemLocation);
                                    }
                                    session.evict(bin);
                                } else {
                                    if (session.getTransaction().isActive()) {
                                        session.getTransaction().rollback();
                                    }
                                    errMsg = "Data Bin - Tidak Ditemukan..!";
                                    return false;
                                }
//                                //data log
//                                dataLog += "Denda Kartu Hilang/Rusak : " + ClassFormatter.currencyFormat.cFormat(dataReservationPaymentForMissedCard.getUnitNominal())
//                                        + " (" + reservationRoomTypeDetailOld.getTblReservationCheckInOut().getCardMissed()
//                                        + " kartu @" + ClassFormatter.currencyFormat.cFormat(reservationRoomTypeDetailOld.getTblReservationCheckInOut().getBrokenCardCharge()) + ")\n";
//                                dataLog += "-----------------------------------------------------------------------\n";
//                                dataLog += "Kurangi Stok Kartu : " + reservationRoomTypeDetailOld.getTblReservationCheckInOut().getCardMissed() + "(jumlah kartu) \n"
//                                        + "- Data Kartu : " + itemGuestCard.getCodeItem() + " - " + itemGuestCard.getItemName() + " \n"
//                                        + "- Data Lokasi Kartu : " + getDataLocationOfWarehouseName(locationFO) + " \n";
//                                dataLog += "-----------------------------------------------------------------------\n";
                                //open close company balance - detail (cashier)
                                if (dataOpenCloseCashierBalance != null) {
                                    TblOpenCloseCashierBalanceDetail dataOpenCloseCashierBalanceDetailMissedCard = new TblOpenCloseCashierBalanceDetail();
                                    dataOpenCloseCashierBalanceDetailMissedCard.setTblOpenCloseCashierBalance(dataOpenCloseCashierBalance);
                                    dataOpenCloseCashierBalanceDetailMissedCard.setTblReservationPayment(dataReservationPaymentForMissedCard);
                                    dataOpenCloseCashierBalanceDetailMissedCard.setDetailData(getDetailData(dataReservationPaymentForMissedCard));
                                    dataOpenCloseCashierBalanceDetailMissedCard.setCreateDate(Timestamp.valueOf(LocalDateTime.now()));
                                    dataOpenCloseCashierBalanceDetailMissedCard.setTblEmployeeByCreateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                                    dataOpenCloseCashierBalanceDetailMissedCard.setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                                    dataOpenCloseCashierBalanceDetailMissedCard.setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                                    dataOpenCloseCashierBalanceDetailMissedCard.setRefRecordStatus(session.find(RefRecordStatus.class, 1));
                                    session.saveOrUpdate(dataOpenCloseCashierBalanceDetailMissedCard);
                                }
                            }
                        }
                        //update room status
                        if (reservationRoomTypeDetailOld.getTblReservationCheckInOut() != null
                                && reservationRoomTypeDetailOld.getTblReservationCheckInOut().getTblRoom() != null
                                && reservationRoomTypeDetailOld.getTblReservationCheckInOut().getTblRoom().getRefRoomStatus() != null) {
                            if (reservationRoomTypeDetailOld.getTblReservationCheckInOut().getTblRoom().getRefRoomStatus().getIdstatus() == 1) {  //Occupied Clean = '1'
                                reservationRoomTypeDetailOld.getTblReservationCheckInOut().getTblRoom().setRefRoomStatus(session.find(RefRoomStatus.class, 3)); //Vacant Clean = '3'
                                session.update(reservationRoomTypeDetailOld.getTblReservationCheckInOut().getTblRoom());
                            } else {
                                if (reservationRoomTypeDetailOld.getTblReservationCheckInOut().getTblRoom().getRefRoomStatus().getIdstatus() == 2) {  //Occupied Dirty = '2'
                                    reservationRoomTypeDetailOld.getTblReservationCheckInOut().getTblRoom().setRefRoomStatus(session.find(RefRoomStatus.class, 4)); //Vacant Dirty = '4'
                                    session.update(reservationRoomTypeDetailOld.getTblReservationCheckInOut().getTblRoom());
                                }
                            }
                        }
                        //delete all unused data additional (rrtd : old)
                        deleteAllReservationAddtionalByIDReservationRoomTypeDetailAndAfterOrEqualsDate(
                                reservationRoomTypeDetailOld.getIddetail(),
                                currentDateForChangeRoomProcess,
                                defaultCheckInTime);
                    }
                }
                reservationRoomTypeDetailOld.setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                reservationRoomTypeDetailOld.setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                session.update(reservationRoomTypeDetailOld);
                //data reservation room type detail - room price detail, reservation room price detail (delete:old)
                rs = session.getNamedQuery("deleteAllTblReservationRoomTypeDetailRoomPriceDetailByIDRoomTypeDetailAndDetailDateAfter")
                        .setParameter("idRoomTypeDetail", reservationRoomTypeDetailOld.getIddetail())
                        .setParameter("detailDate", Date.valueOf(currentDateForChangeRoomProcess.toLocalDate()))
                        .setParameter("idEmployee", ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee());
                errMsg = (String) rs.uniqueResult();
                if (!errMsg.equals("")) {
                    if (session.getTransaction().isActive()) {
                        session.getTransaction().rollback();
                    }
                    return false;
                }
                //data reservation room type (insert:new)
                if (checkRoomAvailable(reservationRoomTypeDetailNew, null)) {
                    //data reservation room type detail - check in/out
                    if (reservationRoomTypeDetailNew.getTblReservationCheckInOut() != null) {
                        reservationRoomTypeDetailNew.getTblReservationCheckInOut().setCreateDate(Timestamp.valueOf(LocalDateTime.now()));
                        reservationRoomTypeDetailNew.getTblReservationCheckInOut().setTblEmployeeByCreateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                        reservationRoomTypeDetailNew.getTblReservationCheckInOut().setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                        reservationRoomTypeDetailNew.getTblReservationCheckInOut().setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                        reservationRoomTypeDetailNew.getTblReservationCheckInOut().setRefRecordStatus(session.find(RefRecordStatus.class, 1));
                        session.saveOrUpdate(reservationRoomTypeDetailNew.getTblReservationCheckInOut());
                        if (reservationRoomTypeDetailNew.getTblReservationCheckInOut().getRefReservationCheckInOutStatus().getIdstatus() == 1) {  //Checked In = '1'
                            //data reservation check in/out (update:old)
                            reservationRoomTypeDetailNew.getTblReservationCheckInOut().setCheckInDateTime(Timestamp.valueOf(LocalDateTime.now()));
                            //data room
                            reservationRoomTypeDetailNew.getTblReservationCheckInOut().getTblRoom().setRefRoomStatus(session.get(RefRoomStatus.class, 1)); //Occupied Clean = '1'
                            //data room status change history
                            TblRoomStatusChangeHistory dataRoomStatusChangeHistory = new TblRoomStatusChangeHistory();
                            dataRoomStatusChangeHistory.setTblRoom(reservationRoomTypeDetailNew.getTblReservationCheckInOut().getTblRoom());
                            dataRoomStatusChangeHistory.setTblEmployeeByIdemployee(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                            dataRoomStatusChangeHistory.setTblRoomStatusDetail(null);
                            dataRoomStatusChangeHistory.setChangeRoomStatusDate(Timestamp.valueOf(LocalDateTime.now()));
                            dataRoomStatusChangeHistory.setRefRoomStatusByRoomStatusOld(getOldRoomStatus(reservationRoomTypeDetailNew.getTblReservationCheckInOut().getTblRoom().getIdroom()));
                            dataRoomStatusChangeHistory.setRefRoomStatusByRoomStatusNew(reservationRoomTypeDetailNew.getTblReservationCheckInOut().getTblRoom().getRefRoomStatus());
                            dataRoomStatusChangeHistory.setCreateDate(Timestamp.valueOf(LocalDateTime.now()));
                            dataRoomStatusChangeHistory.setTblEmployeeByCreateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                            dataRoomStatusChangeHistory.setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                            dataRoomStatusChangeHistory.setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                            dataRoomStatusChangeHistory.setRefRecordStatus(session.find(RefRecordStatus.class, 1));
                            session.saveOrUpdate(dataRoomStatusChangeHistory);
                            //data room
                            reservationRoomTypeDetailNew.getTblReservationCheckInOut().getTblRoom().setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                            reservationRoomTypeDetailNew.getTblReservationCheckInOut().getTblRoom().setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                            session.update(reservationRoomTypeDetailNew.getTblReservationCheckInOut().getTblRoom());
                            //data reservation bill (deposit)
                            TblReservationBill dataReservationBill = getReservationBillByIDReservationAndIDReservationBillType(reservation.getIdreservation(),
                                    2); //Deposit = '2'
                            if (dataReservationBill == null) {  //create and save reservation bill
                                dataReservationBill = new TblReservationBill();
                                dataReservationBill.setCodeBill(ClassCoder.getCode("Reservation Bill", session));
                                dataReservationBill.setTblReservation(reservation);
                                dataReservationBill.setServiceChargePercentage(new BigDecimal("0"));
                                dataReservationBill.setTaxPercentage(new BigDecimal("0"));
                                dataReservationBill.setRefReservationBillType(session.find(RefReservationBillType.class, 2));
                                dataReservationBill.setCreateDate(Timestamp.valueOf(LocalDateTime.now()));
                                dataReservationBill.setTblEmployeeByCreateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                                dataReservationBill.setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                                dataReservationBill.setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                                dataReservationBill.setRefRecordStatus(session.find(RefRecordStatus.class, 1));
                                session.saveOrUpdate(dataReservationBill);
                            }
                            //data reservation payment (open-deposit)
                            TblReservationPayment dataReservationPayment = new TblReservationPayment();
                            dataReservationPayment.setCodePayment(ClassCoder.getCode("Reservation Payment", session));
                            dataReservationPayment.setTblReservationBill(dataReservationBill);
                            dataReservationPayment.setPaymentDate(Timestamp.valueOf(LocalDateTime.now()));
                            dataReservationPayment.setUnitNominal((new BigDecimal(reservationRoomTypeDetailNew.getTblReservationCheckInOut().getCardUsed()))
                                    .multiply(reservationRoomTypeDetailNew.getTblReservationCheckInOut().getBrokenCardCharge()));
                            dataReservationPayment.setRoundingValue(new BigDecimal("0"));
                            dataReservationPayment.setTblEmployeeByIdcashier(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                            dataReservationPayment.setRefFinanceTransactionPaymentType(session.find(RefFinanceTransactionPaymentType.class, 13));
                            dataReservationPayment.setRefReservationBillType(session.find(RefReservationBillType.class, 2));
                            dataReservationPayment.setSettleDate(Timestamp.valueOf(LocalDateTime.now()));
                            dataReservationPayment.setCreateDate(Timestamp.valueOf(LocalDateTime.now()));
                            dataReservationPayment.setTblEmployeeByCreateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                            dataReservationPayment.setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                            dataReservationPayment.setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                            dataReservationPayment.setRefRecordStatus(session.find(RefRecordStatus.class, 1));
                            session.saveOrUpdate(dataReservationPayment);
//                            //data log
//                            dataLog += "Deposit Kartu : " + ClassFormatter.currencyFormat.cFormat(dataReservationPayment.getUnitNominal())
//                                    + " (" + reservationRoomTypeDetailNew.getTblReservationCheckInOut().getCardUsed()
//                                    + " kartu @" + ClassFormatter.currencyFormat.cFormat(reservationRoomTypeDetailNew.getTblReservationCheckInOut().getBrokenCardCharge()) + ")\n";
//                            dataLog += "-----------------------------------------------------------------------\n";
                            //data reservation payment with deposit
                            TblReservationPaymentWithDeposit dataReservationPaymentWithDeposit = new TblReservationPaymentWithDeposit();
                            dataReservationPaymentWithDeposit.setTblReservationPayment(dataReservationPayment);
                            dataReservationPaymentWithDeposit.setTblReservationCheckInOut(reservationRoomTypeDetailNew.getTblReservationCheckInOut());
                            dataReservationPaymentWithDeposit.setCreateDate(Timestamp.valueOf(LocalDateTime.now()));
                            dataReservationPaymentWithDeposit.setTblEmployeeByCreateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                            dataReservationPaymentWithDeposit.setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                            dataReservationPaymentWithDeposit.setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                            dataReservationPaymentWithDeposit.setRefRecordStatus(session.find(RefRecordStatus.class, 1));
                            session.saveOrUpdate(dataReservationPaymentWithDeposit);
                            //@@@%%%
                            //data deposit balance (update:+)
                            TblCompanyBalance dataBalance = session.find(TblCompanyBalance.class, (long) 4);    //Deposit = '4'
                            dataBalance.setBalanceNominal(dataBalance.getBalanceNominal().add(dataReservationPayment.getUnitNominal()));
                            dataBalance.setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                            dataBalance.setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                            session.update(dataBalance);
                            //data company balance (cash-flow log)
                            LogCompanyBalanceCashFlow logCompanyBalanceCashFlow = new LogCompanyBalanceCashFlow();
                            logCompanyBalanceCashFlow.setTblCompanyBalanceByIdsenderCompanyBalance(null);
                            logCompanyBalanceCashFlow.setTblBankAccountByIdsenderCbbankAccount(null);
                            logCompanyBalanceCashFlow.setTblCompanyBalanceByIdreceiverCompanyBalance(dataBalance);
                            logCompanyBalanceCashFlow.setTblBankAccountByIdreceiverCbbankAccount(null);
                            logCompanyBalanceCashFlow.setTransferNominal(dataReservationPayment.getUnitNominal());
                            logCompanyBalanceCashFlow.setHistoryDate(Timestamp.valueOf(LocalDateTime.now()));
                            logCompanyBalanceCashFlow.setTblEmployee(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                            logCompanyBalanceCashFlow.setHistoryStystemNote("Deposit kartu kamar oleh " + reservation.getTblCustomer().getTblPeople().getFullName() + ", \nsebesar : "
                                    + ClassFormatter.currencyFormat.format(dataReservationPayment.getUnitNominal()) + " ("
                                    + dataReservationPayment.getRefFinanceTransactionPaymentType().getTypeName() + ")");
                            session.saveOrUpdate(logCompanyBalanceCashFlow);
//                            //data log
//                            LogSystem logSystem = new LogSystem();
//                            logSystem.setLogDateTime(Timestamp.valueOf(LocalDateTime.now()));
//                            logSystem.setTblSystemUser(session.find(TblSystemUser.class, ClassSession.currentUser.getIduser()));
//                            logSystem.setTblEmployee(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
//                            logSystem.setLogHeader(headerLog);
//                            logSystem.setLogDetail(dataLog);
//                            logSystem.setHostName(ClassComp.getHostName());
//                            session.saveOrUpdate(logSystem);
                            //open close company balance - detail (cashier)
                            TblOpenCloseCashierBalance dataOpenCloseCashierBalance = getDataOpenCloseCashierBalanceIsActive();
                            if (dataOpenCloseCashierBalance != null) {
                                TblOpenCloseCashierBalanceDetail dataOpenCloseCashierBalanceDetail = new TblOpenCloseCashierBalanceDetail();
                                dataOpenCloseCashierBalanceDetail.setTblOpenCloseCashierBalance(dataOpenCloseCashierBalance);
                                dataOpenCloseCashierBalanceDetail.setTblReservationPayment(dataReservationPayment);
                                dataOpenCloseCashierBalanceDetail.setDetailData(getDetailData(dataReservationPayment));
                                dataOpenCloseCashierBalanceDetail.setCreateDate(Timestamp.valueOf(LocalDateTime.now()));
                                dataOpenCloseCashierBalanceDetail.setTblEmployeeByCreateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                                dataOpenCloseCashierBalanceDetail.setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                                dataOpenCloseCashierBalanceDetail.setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                                dataOpenCloseCashierBalanceDetail.setRefRecordStatus(session.find(RefRecordStatus.class, 1));
                                session.saveOrUpdate(dataOpenCloseCashierBalanceDetail);
                            }
                        }
                    }
                    //data reservation room type (insert:new)
                    reservationRoomTypeDetailNew.setCreateDate(Timestamp.valueOf(LocalDateTime.now()));
                    reservationRoomTypeDetailNew.setTblEmployeeByCreateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                    reservationRoomTypeDetailNew.setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                    reservationRoomTypeDetailNew.setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                    reservationRoomTypeDetailNew.setRefRecordStatus(session.find(RefRecordStatus.class, 1));
                    session.saveOrUpdate(reservationRoomTypeDetailNew);
                    //data reservation room type detail - room service (insert)
                    List<TblRoomTypeRoomService> roomTypeRoomServices = getAllRoomTypeRoomServiceByIDRoomType(reservationRoomTypeDetailNew.getTblRoomType().getIdroomType());
                    for (TblRoomTypeRoomService roomTypeRoomService : roomTypeRoomServices) {
                        TblReservationRoomTypeDetailRoomService reservationRoomTypeDetailRoomService = new TblReservationRoomTypeDetailRoomService();
                        reservationRoomTypeDetailRoomService.setTblReservationRoomTypeDetail(reservationRoomTypeDetailNew);
                        reservationRoomTypeDetailRoomService.setTblRoomService(roomTypeRoomService.getTblRoomService());
                        reservationRoomTypeDetailRoomService.setRoomServiceCost(roomTypeRoomService.getTblRoomService().getPrice());
                        reservationRoomTypeDetailRoomService.setPeopleNumber(roomTypeRoomService.getPeopleNumber());
                        reservationRoomTypeDetailRoomService.setCreateDate(Timestamp.valueOf(LocalDateTime.now()));
                        reservationRoomTypeDetailRoomService.setTblEmployeeByCreateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                        reservationRoomTypeDetailRoomService.setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                        reservationRoomTypeDetailRoomService.setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                        reservationRoomTypeDetailRoomService.setRefRecordStatus(session.find(RefRecordStatus.class, 1));
                        session.saveOrUpdate(reservationRoomTypeDetailRoomService);
                    }
                    //data reservation room type detail - item (insert)
                    List<TblRoomTypeItem> roomTypeItems = getAllRoomTypeItemByIDRoomType(reservationRoomTypeDetailNew.getTblRoomType().getIdroomType());
                    for (TblRoomTypeItem roomTypeItem : roomTypeItems) {
                        TblReservationRoomTypeDetailItem reservationRoomTypeDetailItem = new TblReservationRoomTypeDetailItem();
                        reservationRoomTypeDetailItem.setTblReservationRoomTypeDetail(reservationRoomTypeDetailNew);
                        reservationRoomTypeDetailItem.setTblItem(roomTypeItem.getTblItem());
                        reservationRoomTypeDetailItem.setItemAdditionalCharge(roomTypeItem.getTblItem().getAdditionalCharge());
                        reservationRoomTypeDetailItem.setItemBrokenCharge(roomTypeItem.getTblItem().getBrokenCharge());
                        reservationRoomTypeDetailItem.setItemQuantity(roomTypeItem.getItemQuantity());
                        reservationRoomTypeDetailItem.setCreateDate(Timestamp.valueOf(LocalDateTime.now()));
                        reservationRoomTypeDetailItem.setTblEmployeeByCreateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                        reservationRoomTypeDetailItem.setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                        reservationRoomTypeDetailItem.setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                        reservationRoomTypeDetailItem.setRefRecordStatus(session.find(RefRecordStatus.class, 1));
                        session.saveOrUpdate(reservationRoomTypeDetailItem);
                    }
                    //insert all used data additional (rrtd : new)
                    insertAllReservationAddtionalReservationRoomTypeDetail(reservationRoomTypeDetailNew);
                    //data reservation room type detail - childs info
                    for (TblReservationRoomTypeDetailChildDetail rrtdChildDetail : rrtdChildDetails) {
                        rrtdChildDetail.setCreateDate(Timestamp.valueOf(LocalDateTime.now()));
                        rrtdChildDetail.setTblEmployeeByCreateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                        rrtdChildDetail.setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                        rrtdChildDetail.setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                        rrtdChildDetail.setRefRecordStatus(session.find(RefRecordStatus.class, 1));
                        session.saveOrUpdate(rrtdChildDetail);
                    }
                } else {
                    if (session.getTransaction().isActive()) {
                        session.getTransaction().rollback();
                    }
                    errMsg = "Jumlah kamar tidak mencukupi..!";
                    return false;
                }
                //data reservation room type detail - room price detail (insert:new)
                for (TblReservationRoomTypeDetailRoomPriceDetail rrtdrpd : rrtdrpds) {
                    //data reservation room price detail (insert:new)
                    rrtdrpd.getTblReservationRoomPriceDetail().setCreateDate(reservationRoomTypeDetailOld.getCreateDate());
                    rrtdrpd.getTblReservationRoomPriceDetail().setTblEmployeeByCreateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                    rrtdrpd.getTblReservationRoomPriceDetail().setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                    rrtdrpd.getTblReservationRoomPriceDetail().setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                    rrtdrpd.getTblReservationRoomPriceDetail().setRefRecordStatus(session.find(RefRecordStatus.class, 1));
                    session.saveOrUpdate(rrtdrpd.getTblReservationRoomPriceDetail());
                    //data reservation room type detail - room price detail (insert:new)
                    rrtdrpd.setCreateDate(reservationRoomTypeDetailOld.getCreateDate());
                    rrtdrpd.setTblEmployeeByCreateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                    rrtdrpd.setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                    rrtdrpd.setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                    rrtdrpd.setRefRecordStatus(session.find(RefRecordStatus.class, 1));
                    session.saveOrUpdate(rrtdrpd);
                    //data guarantee letter (if old data use 'guarantee payment'), <update old data>
                    List<TblGuaranteeLetterItemDetail> dataGuaranteeLetterItemDetails = getAllDataGuaranteeLetterItemDetailByIDReservationAndCodeRTDAndDetailDate(
                            reservationRoomTypeDetailOld.getTblReservation().getIdreservation(),
                            reservationRoomTypeDetailOld.getCodeDetail(),
                            rrtdrpd.getTblReservationRoomPriceDetail().getDetailDate());
                    for (TblGuaranteeLetterItemDetail dataGuaranteeLetterItemDetail : dataGuaranteeLetterItemDetails) {
                        dataGuaranteeLetterItemDetail.setCodeRtd(rrtdrpd.getTblReservationRoomTypeDetail().getCodeDetail());
                        dataGuaranteeLetterItemDetail.setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                        dataGuaranteeLetterItemDetail.setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                        session.update(dataGuaranteeLetterItemDetail);
                    }
                }
                //data log
                dataLog += "Reservasi Kamar : \n";
                dataLog += "- " + reservationRoomTypeDetailNew.getCodeDetail() + " - " + reservationRoomTypeDetailNew.getCheckInDateTime() + " s/d " + reservationRoomTypeDetailNew.getCheckOutDateTime() + " \t "
                        + (getDataRoom(reservationRoomTypeDetailNew)) + " \n";
                for (TblReservationRoomTypeDetailRoomPriceDetail rrtdrpd : rrtdrpds) {
                    if (rrtdrpd.getTblReservationRoomTypeDetail().getIddetail()
                            == reservationRoomTypeDetailNew.getIddetail()) {
                        dataLog += "\t - " + ClassFormatter.dateFormate.format(rrtdrpd.getTblReservationRoomPriceDetail().getDetailDate()) + " \t "
                                + "Harga : " + ClassFormatter.currencyFormat.cFormat(rrtdrpd.getTblReservationRoomPriceDetail().getDetailPrice()) + " \t "
                                + "Diskon : " + ClassFormatter.decimalFormat.cFormat(rrtdrpd.getTblReservationRoomPriceDetail().getDetailDiscountPercentage()) + "% (" + getDataDiscountEvent(rrtdrpd.getTblReservationRoomPriceDetail()) + ") \t "
                                + "Compliment : " + ClassFormatter.currencyFormat.cFormat(rrtdrpd.getTblReservationRoomPriceDetail().getDetailComplimentary()) + " \t "
                                + " \n";
                    }
                }
                dataLog += "-----------------------------------------------------------------------\n";
                //data reservation additonal item (old:delete)
                rs = session.getNamedQuery("deleteAllTblReservationAdditionalItemByIDRoomTypeDetailAndAdditionalDateAfter")
                        .setParameter("idRoomTypeDetail", reservationRoomTypeDetailOld.getIddetail())
                        .setParameter("additionalDate", Date.valueOf(currentDateForChangeRoomProcess.toLocalDate()))
                        .setParameter("idEmployee", ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee());
                errMsg = (String) rs.uniqueResult();
                if (!errMsg.equals("")) {
                    if (session.getTransaction().isActive()) {
                        session.getTransaction().rollback();
                    }
                    return false;
                }
                //data reservation additonal item - reserved (old:delete)
                rs = session.getNamedQuery("deleteAllTblReservationAdditionalItemReservedByIDRoomTypeDetailAndReservedDateAfter")
                        .setParameter("idRoomTypeDetail", reservationRoomTypeDetailOld.getIddetail())
                        .setParameter("reservedDate", Date.valueOf(currentDateForChangeRoomProcess.toLocalDate()))
                        .setParameter("idEmployee", ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee());
                errMsg = (String) rs.uniqueResult();
                if (!errMsg.equals("")) {
                    if (session.getTransaction().isActive()) {
                        session.getTransaction().rollback();
                    }
                    return false;
                }
                //data log
                dataLog += "Tambahan Barang : \n";
                //data reservation additonal item (insert:new)
                for (TblReservationAdditionalItem rai : rais) {
//                    rai.setCreateDate(Timestamp.valueOf(LocalDateTime.now()));
//                    rai.setTblEmployeeByCreateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                    rai.setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                    rai.setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                    rai.setRefRecordStatus(session.find(RefRecordStatus.class, 1));
                    session.saveOrUpdate(rai);
                    //data reservation additonal item - reserved
                    TblReservationAdditionalItemReserved reservationAdditionalItemReserved = getReservationAdditionalItemReservedByIDReservationRoomTypeDetailAndIDItemAndReservedDate(
                            rai.getTblReservationRoomTypeDetail().getIddetail(),
                            rai.getTblItem().getIditem(),
                            rai.getAdditionalDate());
                    if (reservationAdditionalItemReserved != null) {
                        reservationAdditionalItemReserved.setReservedQuantity(reservationAdditionalItemReserved.getReservedQuantity().add(rai.getItemQuantity()));
                        reservationAdditionalItemReserved.setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                        reservationAdditionalItemReserved.setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                        session.update(reservationAdditionalItemReserved);
                    } else {
                        reservationAdditionalItemReserved = new TblReservationAdditionalItemReserved();
                        reservationAdditionalItemReserved.setTblReservationRoomTypeDetail(rai.getTblReservationRoomTypeDetail());
                        reservationAdditionalItemReserved.setTblItem(rai.getTblItem());
                        reservationAdditionalItemReserved.setReservedDate(rai.getAdditionalDate());
                        reservationAdditionalItemReserved.setReservedQuantity(rai.getItemQuantity());
                        reservationAdditionalItemReserved.setInUsedQuantity(getLastInUseStock(
                                reservationRoomTypeDetailOld,
                                reservationAdditionalItemReserved.getTblItem(),
                                reservationAdditionalItemReserved.getReservedDate()));
                        reservationAdditionalItemReserved.setDoneQuantity(getLastDoneStock(
                                reservationRoomTypeDetailOld,
                                reservationAdditionalItemReserved.getTblItem(),
                                reservationAdditionalItemReserved.getReservedDate()));
                        reservationAdditionalItemReserved.setRefReservationAdditionalItemReservedStatus(session.find(RefReservationAdditionalItemReservedStatus.class, 1));     //Reserved = '1'
//                        reservationAdditionalItemReserved.setCreateDate(Timestamp.valueOf(LocalDateTime.now()));
//                        reservationAdditionalItemReserved.setTblEmployeeByCreateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                        reservationAdditionalItemReserved.setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                        reservationAdditionalItemReserved.setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                        reservationAdditionalItemReserved.setRefRecordStatus(session.find(RefRecordStatus.class, 1));
                        session.saveOrUpdate(reservationAdditionalItemReserved);
                    }
                    //data log
                    dataLog += "- " + ClassFormatter.dateFormate.format(rai.getAdditionalDate()) + " - " + rai.getTblItem().getItemName() + " \t "
                            + "Harga : " + ClassFormatter.currencyFormat.cFormat(rai.getItemCharge()) + " \t "
                            + "Diskon : " + ClassFormatter.decimalFormat.cFormat(rai.getDiscountPercentage()) + "% (" + getDataDiscountEvent(rai) + ") \t "
                            + "Jumlah : " + ClassFormatter.decimalFormat.cFormat(rai.getItemQuantity()) + " \n";
                }
                //data log
                dataLog += "-----------------------------------------------------------------------\n";
                //data reservation additonal service (old:delete)
                rs = session.getNamedQuery("deleteAllTblReservationAdditionalServiceByIDRoomTypeDetailAndAdditionalDateAfterAndIDRoomServiceNotSpecific")
                        .setParameter("idRoomTypeDetail", reservationRoomTypeDetailOld.getIddetail())
                        .setParameter("additionalDate", Date.valueOf(currentDateForChangeRoomProcess.toLocalDate()))
                        .setParameter("idEmployee", ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee());
                errMsg = (String) rs.uniqueResult();
                if (!errMsg.equals("")) {
                    if (session.getTransaction().isActive()) {
                        session.getTransaction().rollback();
                    }
                    return false;
                }
                //data log
                dataLog += "Tambahan Layanan : \n";
                //data reservation additonal service (insert:new)
                for (TblReservationAdditionalService ras : rass) {
                    if (ras.getTblRoomService().getIdroomService() == 4) { //Lainnya(Bonus Voucher) = '4'
                        ras.setCreateDate(Timestamp.valueOf(LocalDateTime.now()));
                        ras.setTblEmployeeByCreateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                    }
                    ras.setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                    ras.setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                    ras.setRefRecordStatus(session.find(RefRecordStatus.class, 1));
                    session.saveOrUpdate(ras);
                    //data log
                    dataLog += "- " + ClassFormatter.dateFormate.format(ras.getAdditionalDate()) + " - " + ras.getTblRoomService().getServiceName() + " \t "
                            + "Harga : " + ClassFormatter.currencyFormat.cFormat(ras.getPrice()) + " \t "
                            + "Diskon : " + ClassFormatter.decimalFormat.cFormat(ras.getDiscountPercentage()) + "% (" + getDataDiscountEvent(ras) + ") \n";
                }
                //data log
                dataLog += "-----------------------------------------------------------------------\n";
                dataLog += "Data Pindah Kamar - History : \n";
                //data reservation room type detail - room price detail - change room history (insert:new)
                for (TblReservationRoomTypeDetailRoomPriceDetailChangeRoomHistory changeRoomHistory : changeRoomHistories) {
                    if (reservationRoomTypeDetailOld.getTblReservationCheckInOut() != null) {
                        //data reservation room type detail - room price detail - change room history (insert:new)
                        changeRoomHistory.setCreateDate(Timestamp.valueOf(LocalDateTime.now()));
                        changeRoomHistory.setTblEmployeeByCreateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                        changeRoomHistory.setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                        changeRoomHistory.setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                        changeRoomHistory.setRefRecordStatus(session.find(RefRecordStatus.class, 1));
                        session.saveOrUpdate(changeRoomHistory);
                        dataLog += "- Dari tipe kamar '" + changeRoomHistory.getTblReservationRoomTypeDetailRoomPriceDetailByIdrelationOld().getTblReservationRoomTypeDetail().getTblRoomType().getRoomTypeName()
                                + "' menjadi '" + changeRoomHistory.getTblReservationRoomTypeDetailRoomPriceDetailByIdrelationNew().getTblReservationRoomTypeDetail().getTblRoomType().getRoomTypeName() + "' \n";
                    }
                }
                //data log
                dataLog += "-----------------------------------------------------------------------\n";
                LogSystem logSystem = new LogSystem();
                logSystem.setLogDateTime(Timestamp.valueOf(LocalDateTime.now()));
                logSystem.setTblSystemUser(session.find(TblSystemUser.class, ClassSession.currentUser.getIduser()));
                logSystem.setTblEmployee(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                logSystem.setLogHeader(headerLog);
                logSystem.setLogDetail(dataLog);
                logSystem.setHostName(ClassComp.getHostName());
                session.saveOrUpdate(logSystem);
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

    //just check new-service : lainnya (bonus voucher) -> for old data(rrtd), another service has been check with another function
    private boolean IsReservationRoomTypeDetailHaveAdditonalService(TblReservationRoomTypeDetail dataRRTD,
            List<TblReservationAdditionalService> dataAdditonalServices) {
        boolean haveAdditonalService = false;
        for (TblReservationAdditionalService dataAdditonalService : dataAdditonalServices) {
            if (dataAdditonalService.getTblReservationRoomTypeDetail().equals(dataRRTD)) {
                haveAdditonalService = true;
                break;
            }
        }
        return haveAdditonalService;
    }

    private List<TblReservationAdditionalItem> getAllReservationAdditionalItemByIDReservationRoomTypeDetailAndIDReservationBillType(
            long idReservationRoomTypeDetail,
            int idReservationBillType) {
        List<TblReservationAdditionalItem> list = new ArrayList<>();
        if (idReservationRoomTypeDetail != 0L) {
            list = session.getNamedQuery("findAllTblReservationAdditionalItemByIDReservationRoomTypeDetailAndReservationBillType")
                    .setParameter("idReservationRoomTypeDetail", idReservationRoomTypeDetail)
                    .setParameter("idReservationBillType", idReservationBillType)
                    .list();
        }
        return list;
    }

    private void deleteAllReservationAddtionalByIDReservationRoomTypeDetailAndAfterOrEqualsDate(
            long idRRTD,
            LocalDateTime currentDateForChangeRoomProcess,
            Time defaultCheckInTime) {
        //data reservation room type detail - room service (delete), --> bill type = 'Include', after or equals 'currentDateForChangeRoomProcess'
        List<TblReservationAdditionalService> additionalServiceIncludes = getAllReservationAdditionalServiceByIDReservationRoomTypeDetailAndIDReservationBillType(
                idRRTD,
                4); //Include = '4'
        for (TblReservationAdditionalService additionalServiceInclude : additionalServiceIncludes) {
            if (!(LocalDateTime.of(additionalServiceInclude.getAdditionalDate().getYear() + 1900,
                    additionalServiceInclude.getAdditionalDate().getMonth() + 1,
                    additionalServiceInclude.getAdditionalDate().getDate(),
                    defaultCheckInTime.getHours(),
                    defaultCheckInTime.getMinutes(),
                    0)).isBefore(currentDateForChangeRoomProcess)) {
                additionalServiceInclude.setRefRecordStatus(session.find(RefRecordStatus.class, 2));
                session.update(additionalServiceInclude);
            }
        }
        //data reservation room type detail - item (delete), --> bill type = 'Include', after or equals 'currentDateForChangeRoomProcess'
        List<TblReservationAdditionalItem> additionalItemIncludes = getAllReservationAdditionalItemByIDReservationRoomTypeDetailAndIDReservationBillType(
                idRRTD,
                4); //Include = '4'
        for (TblReservationAdditionalItem additionalItemInclude : additionalItemIncludes) {
            if (!(LocalDateTime.of(additionalItemInclude.getAdditionalDate().getYear() + 1900,
                    additionalItemInclude.getAdditionalDate().getMonth() + 1,
                    additionalItemInclude.getAdditionalDate().getDate(),
                    defaultCheckInTime.getHours(),
                    defaultCheckInTime.getMinutes(),
                    0)).isBefore(currentDateForChangeRoomProcess)) {
                additionalItemInclude.setRefRecordStatus(session.find(RefRecordStatus.class, 2));
                session.update(additionalItemInclude);
            }
        }
    }

    private void insertAllReservationAddtionalReservationRoomTypeDetail(TblReservationRoomTypeDetail dataRRTD) {
        LocalDate countDate = LocalDate.of(dataRRTD.getCheckInDateTime().getYear() + 1900,
                dataRRTD.getCheckInDateTime().getMonth() + 1,
                dataRRTD.getCheckInDateTime().getDate());
        LocalDate lastDate = LocalDate.of(dataRRTD.getCheckOutDateTime().getYear() + 1900,
                dataRRTD.getCheckOutDateTime().getMonth() + 1,
                dataRRTD.getCheckOutDateTime().getDate());
        while (countDate.isBefore(lastDate)) {
            //data reservation additonal service (include) : insert
            List<TblRoomTypeRoomService> dataRTRSs = getAllRoomTypeRoomServiceByIDRoomType(dataRRTD.getTblRoomType().getIdroomType());
            for (TblRoomTypeRoomService dataRTRS : dataRTRSs) {
                if (dataRTRS.getAddAsAdditionalService()) {
                    if (dataRTRS.getPeopleNumber() == 0) {
                        //data additional service
                        TblReservationAdditionalService dataAdditionalService = new TblReservationAdditionalService();
                        dataAdditionalService.setTblReservationRoomTypeDetail(dataRRTD);
                        dataAdditionalService.setTblRoomService(new TblRoomService(dataRTRS.getTblRoomService()));
                        dataAdditionalService.setAdditionalDate(Date.valueOf(countDate));
                        dataAdditionalService.setPrice(new BigDecimal("0"));
                        dataAdditionalService.setDiscountPercentage(new BigDecimal("0"));
                        dataAdditionalService.setRefReservationBillType(session.find(RefReservationBillType.class, 4));  //Include = '4'
//                        dataAdditionalService.setNote("Jumlah Orang : -");
                        dataAdditionalService.setCreateDate(Timestamp.valueOf(LocalDateTime.now()));
                        dataAdditionalService.setTblEmployeeByCreateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                        dataAdditionalService.setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                        dataAdditionalService.setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                        dataAdditionalService.setRefRecordStatus(session.find(RefRecordStatus.class, 1));
                        session.saveOrUpdate(dataAdditionalService);
                    } else {
                        for (int peopleNumber = 0; peopleNumber < dataRTRS.getPeopleNumber(); peopleNumber++) {
                            //data additional service
                            TblReservationAdditionalService dataAdditionalService = new TblReservationAdditionalService();
                            dataAdditionalService.setTblReservationRoomTypeDetail(dataRRTD);
                            dataAdditionalService.setTblRoomService(new TblRoomService(dataRTRS.getTblRoomService()));
                            dataAdditionalService.setAdditionalDate(Date.valueOf(countDate));
                            dataAdditionalService.setPrice(new BigDecimal("0"));
                            dataAdditionalService.setDiscountPercentage(new BigDecimal("0"));
                            dataAdditionalService.setRefReservationBillType(session.find(RefReservationBillType.class, 4));  //Include = '4'
//                            dataAdditionalService.setNote("Jumlah Orang : 1");
                            dataAdditionalService.setCreateDate(Timestamp.valueOf(LocalDateTime.now()));
                            dataAdditionalService.setTblEmployeeByCreateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                            dataAdditionalService.setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                            dataAdditionalService.setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                            dataAdditionalService.setRefRecordStatus(session.find(RefRecordStatus.class, 1));
                            session.saveOrUpdate(dataAdditionalService);
                        }
                    }
                }
            }
            //data reservation additonal item (include) : insert
            List<TblRoomTypeItem> dataRTIs = getAllRoomTypeItemByIDRoomType(dataRRTD.getTblRoomType().getIdroomType());
            for (TblRoomTypeItem dataRTI : dataRTIs) {
                if (dataRTI.getAddAsAdditionalItem()) {
                    //data additional item
                    TblReservationAdditionalItem dataAddtionalItem = new TblReservationAdditionalItem();
                    dataAddtionalItem.setTblReservationRoomTypeDetail(dataRRTD);
                    dataAddtionalItem.setTblItem(new TblItem(dataRTI.getTblItem()));
                    dataAddtionalItem.setAdditionalDate(Date.valueOf(countDate));
                    dataAddtionalItem.setItemCharge(new BigDecimal("0"));
                    dataAddtionalItem.setItemQuantity(dataRTI.getItemQuantity());
                    dataAddtionalItem.setDiscountPercentage(new BigDecimal("0"));
                    dataAddtionalItem.setTaxable(dataAddtionalItem.getTblItem().getTaxable());
                    dataAddtionalItem.setRefReservationBillType(session.find(RefReservationBillType.class, 4));  //Include = '4'
                    dataAddtionalItem.setCreateDate(Timestamp.valueOf(LocalDateTime.now()));
                    dataAddtionalItem.setTblEmployeeByCreateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                    dataAddtionalItem.setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                    dataAddtionalItem.setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                    dataAddtionalItem.setRefRecordStatus(session.find(RefRecordStatus.class, 1));
                    session.saveOrUpdate(dataAddtionalItem);
                }
            }
            //increment countDate
            countDate = countDate.plusDays(1);
        }
    }

    private List<TblReservationAdditionalService> getAllReservationAdditionalServiceByIDReservationRoomTypeDetailAndIDReservationBillType(
            long idReservationRoomTypeDetail,
            int idReservationBillType) {
        List<TblReservationAdditionalService> list = new ArrayList<>();
        if (idReservationRoomTypeDetail != 0L) {
            list = session.getNamedQuery("findAllTblReservationAdditionalServiceByIDReservationRoomTypeDetailAndReservationBillType")
                    .setParameter("idReservationRoomTypeDetail", idReservationRoomTypeDetail)
                    .setParameter("idReservationBillType", idReservationBillType)
                    .list();
        }
        return list;
    }

    private List<TblGuaranteeLetterItemDetail> getAllDataGuaranteeLetterItemDetailByIDReservationAndCodeRTDAndDetailDate(
            long idReservation,
            String codeRTD,
            java.util.Date detailDate) {
        List<TblGuaranteeLetterItemDetail> list = new ArrayList<>();
        if (idReservation != 0L
                && detailDate != null) {
            list = session.getNamedQuery("findAllTblGuaranteeLetterItemDetailByIDReservationAndCodeRTDAndDetailDate")
                    .setParameter("idReservation", idReservation)
                    .setParameter("codeRTD", codeRTD)
                    .setParameter("detailDate", detailDate)
                    .list();
        }
        return list;
    }

    private BigDecimal getLastInUseStock(TblReservationRoomTypeDetail dataRRTD,
            TblItem dataItem,
            java.util.Date reservedDate) {
        BigDecimal result = new BigDecimal("0");
        if (dataRRTD != null
                && dataItem != null
                && reservedDate != null) {
            List<TblReservationAdditionalItemReserved> list = session.getNamedQuery("findAllTblReservationAdditionalItemReservedByIDReservationRoomTypeDetailAndIDItemAndReservedDateAndByIDRecordStatus")
                    .setParameter("idRRTD", dataRRTD.getIddetail())
                    .setParameter("idItem", dataItem.getIditem())
                    .setParameter("reservedDate", reservedDate)
                    .setParameter("idRecordStatus", 2) //data has been deleted ('2')
                    .list();
            for (TblReservationAdditionalItemReserved data : list) {
                result = result.add(data.getInUsedQuantity());
            }
        }
        return result;
    }

    private BigDecimal getLastDoneStock(TblReservationRoomTypeDetail dataRRTD,
            TblItem dataItem,
            java.util.Date reservedDate) {
        BigDecimal result = new BigDecimal("0");
        if (dataRRTD != null
                && dataItem != null
                && reservedDate != null) {
            List<TblReservationAdditionalItemReserved> list = session.getNamedQuery("findAllTblReservationAdditionalItemReservedByIDReservationRoomTypeDetailAndIDItemAndReservedDateAndByIDRecordStatus")
                    .setParameter("idRRTD", dataRRTD.getIddetail())
                    .setParameter("idItem", dataItem.getIditem())
                    .setParameter("reservedDate", reservedDate)
                    .setParameter("idRecordStatus", 2) //data has been deleted ('2')
                    .list();
            for (TblReservationAdditionalItemReserved data : list) {
                result = result.add(data.getDoneQuantity());
            }
        }
        return result;
    }

    @Override
    public boolean updateDataReservationRoomTypeDetailCheckInOutChangeRoom(
            TblReservation reservation,
            TblReservationRoomTypeDetail reservationRoomTypeDetail) {
        errMsg = "";
        if (ClassSession.checkUserSession()) {  //check user current session
            try {
                session = HibernateUtil.getSessionFactory().getCurrentSession();
                session.beginTransaction();
                //data reservation
                reservation.setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                reservation
                        .setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                session.update(reservation);

                //data reservation room type detail
                reservationRoomTypeDetail.setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                reservationRoomTypeDetail.setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                session.update(reservationRoomTypeDetail);

                //data reservation check in/out
                reservationRoomTypeDetail.getTblReservationCheckInOut()
                        .setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                reservationRoomTypeDetail.getTblReservationCheckInOut()
                        .setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                session.update(reservationRoomTypeDetail.getTblReservationCheckInOut());
                session.getTransaction()
                        .commit();
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
    public List<TblReservationRoomTypeDetailChildDetail> getAllDataReservationRoomTypeDetailChildDetailByIDReservationRoomTypeDetail(long idRRTD) {
        errMsg = "";
        List<TblReservationRoomTypeDetailChildDetail> list = new ArrayList<>();
        if (ClassSession.checkUserSession()) {  //check user current session
            try {
                session = HibernateUtil.getSessionFactory().getCurrentSession();
                session.beginTransaction();
                list = session.getNamedQuery("findAllTblReservationRoomTypeDetailChildDetailByIDReservationRoomTypeDetail")
                        .setParameter("idReservationRoomTypeDetail", idRRTD)
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
    public boolean deleteDataReservation(TblReservation reservation) {
        errMsg = "";
        if (ClassSession.checkUserSession()) {  //check user current session
            try {
                session = HibernateUtil.getSessionFactory().getCurrentSession();
                session.beginTransaction();
                if (reservation.getRefRecordStatus().getIdstatus() == 1) {
                    reservation.setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                    reservation
                            .setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                    reservation.setRefRecordStatus(session.find(RefRecordStatus.class, 2));
                    session.update(reservation);
                } else {
                    if (session.getTransaction().isActive()) {
                        session.getTransaction().rollback();
                    }
                    errMsg = "Data tidak dapat dihapus!!";
                    return false;
                }

                session.getTransaction()
                        .commit();
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
    public TblReservationCanceled insertDataReservationCanceled(
            TblReservationCanceled reservationCanceled,
            TblReservationPayment reservationPayment,
            List<TblReservationRoomTypeDetailRoomPriceDetail> rrtdrpds,
            List<TblReservationAdditionalItem> rais,
            List<TblReservationAdditionalService> rass,
            List<TblReservationBrokenItem> rbis) {
        errMsg = "";
        if (ClassSession.checkUserSession()) {  //check user current session
            try {
                session = HibernateUtil.getSessionFactory().getCurrentSession();
                session.beginTransaction();
                //data log
                String headerLog = "BUAT DATA PEMBATALAN RESERVASI";
                String dataLog = "";
                //data reservation
                reservationCanceled
                        .getTblReservation().setRefReservationStatus(session.find(RefReservationStatus.class, 6));
                reservationCanceled.getTblReservation()
                        .setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                reservationCanceled.getTblReservation()
                        .setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                session.update(reservationCanceled.getTblReservation());
                //data log
                dataLog += "No. Reservasi : " + reservationCanceled.getTblReservation().getCodeReservation() + "\n";
                dataLog += "Tgl. Reservasi : " + ClassFormatter.dateFormate.format(reservationCanceled.getTblReservation().getReservationDate()) + "\n";
                dataLog += "Nama Customer : " + reservationCanceled.getTblReservation().getTblCustomer().getTblPeople().getFullName() + "\n";
                dataLog += "Travel Agent : " + (reservationCanceled.getTblReservation().getTblPartner() != null ? reservationCanceled.getTblReservation().getTblPartner().getPartnerName() : "-") + "\n";
                dataLog += "-----------------------------------------------------------------------\n";

                //data reservation room type detail - room price detail
                for (TblReservationRoomTypeDetailRoomPriceDetail rrtdrpd : rrtdrpds) {
                    session.update(rrtdrpd.getTblReservationRoomPriceDetail());
                }
                //data reservation additional item
                for (TblReservationAdditionalItem rai : rais) {
                    session.update(rai);
                }
                //data reservation additonal service
                for (TblReservationAdditionalService ras : rass) {
                    session.update(ras);
                }
                //data reservation broken item
                for (TblReservationBrokenItem rbi : rbis) {
                    session.update(rbi);
                }

                //data reservation payment - with guarantee payment - is debt
                List<TblReservationPaymentWithGuaranteePayment> rpwgpIsDebts = getAllReservationPaymentWithGuaranteePaymentIsDebtByIDReservation(reservationCanceled.getTblReservation().getIdreservation());
                for (TblReservationPaymentWithGuaranteePayment rpwgpIsDebt : rpwgpIsDebts) {
                    rpwgpIsDebt.getTblHotelReceivable().setRefFinanceTransactionStatus(session.find(RefFinanceTransactionStatus.class, 4)); //Dibatalkan = '4'
                    rpwgpIsDebt.getTblHotelReceivable().setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                    rpwgpIsDebt.getTblHotelReceivable().setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                    session.update(rpwgpIsDebt.getTblHotelReceivable());
                }

                //data reservation canceled
                reservationCanceled.setCancelDateTime(Timestamp.valueOf(LocalDateTime.now()));
                reservationCanceled.setCreateDate(Timestamp.valueOf(LocalDateTime.now()));
                reservationCanceled.setTblEmployeeByCreateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                reservationCanceled.setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                reservationCanceled.setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                reservationCanceled.setRefRecordStatus(session.find(RefRecordStatus.class, 1));
                session.saveOrUpdate(reservationCanceled);
                //data log
                dataLog += "Alasan Pembatalan : " + reservationCanceled.getCancelNote() + "\n";
                dataLog += "-----------------------------------------------------------------------\n";

                //data reservation bill (canceling fee)
                reservationPayment.getTblReservationBill()
                        .setCodeBill(ClassCoder.getCode("Reservation Bill", session));
                reservationPayment.getTblReservationBill()
                        .setCreateDate(Timestamp.valueOf(LocalDateTime.now()));
                reservationPayment.getTblReservationBill()
                        .setTblEmployeeByCreateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                reservationPayment.getTblReservationBill()
                        .setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                reservationPayment.getTblReservationBill()
                        .setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                reservationPayment.getTblReservationBill()
                        .setRefRecordStatus(session.find(RefRecordStatus.class, 1));
                session.saveOrUpdate(reservationPayment.getTblReservationBill());
                //data log
                dataLog += "No. Tagihan (" + reservationPayment.getTblReservationBill().getRefReservationBillType().getTypeName() + ") : " + reservationPayment.getTblReservationBill().getCodeBill() + "\n";
                dataLog += "Service Charge (%) : " + ClassFormatter.decimalFormat.cFormat((reservationPayment.getTblReservationBill().getServiceChargePercentage().multiply(new BigDecimal("100")))) + "% \n";
                dataLog += "Pajak (%) : " + ClassFormatter.decimalFormat.cFormat((reservationPayment.getTblReservationBill().getTaxPercentage().multiply(new BigDecimal("100")))) + "% \n";
                dataLog += "Tipe Diskon : " + (reservationPayment.getTblReservationBill().getRefReservationBillDiscountType() != null ? reservationPayment.getTblReservationBill().getRefReservationBillDiscountType().getTypeName() : "-") + " \n";
                dataLog += "-----------------------------------------------------------------------\n";
                dataLog += "Transaksi Pembayaran : \n";
                //data reservation payment (canceling fee), auto settle
                reservationPayment.setCodePayment(ClassCoder.getCode("Reservation Payment", session));
                reservationPayment.setSettleDate(Timestamp.valueOf(LocalDateTime.now()));
                reservationPayment.setCreateDate(Timestamp.valueOf(LocalDateTime.now()));
                reservationPayment.setTblEmployeeByCreateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                reservationPayment.setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                reservationPayment.setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                reservationPayment.setRefRecordStatus(session.find(RefRecordStatus.class, 1));
                session.saveOrUpdate(reservationPayment);
                //data log
                dataLog += "- " + reservationPayment.getCodePayment() + " - " + ClassFormatter.dateTimeFormate.format(reservationPayment.getPaymentDate()) + " \t "
                        + "Nominal : " + ClassFormatter.currencyFormat.cFormat(reservationPayment.getUnitNominal()) + " \t "
                        + "Tipe : " + reservationPayment.getRefFinanceTransactionPaymentType().getTypeName() + " \n";
                //@@@%%%
                //data company balance (update:-)
                TblCompanyBalance dataBalance = session.find(TblCompanyBalance.class, (long) 3);    //Kas Kasir = '3'

                dataBalance.setBalanceNominal(dataBalance.getBalanceNominal().subtract(reservationPayment.getUnitNominal()));
                dataBalance.setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                dataBalance.setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                session.update(dataBalance);

                //data company balance (cash-flow log)
                LogCompanyBalanceCashFlow logCompanyBalanceCashFlow = new LogCompanyBalanceCashFlow();
                logCompanyBalanceCashFlow.setTblCompanyBalanceByIdsenderCompanyBalance(dataBalance);
                logCompanyBalanceCashFlow.setTblBankAccountByIdsenderCbbankAccount(null);
                logCompanyBalanceCashFlow.setTblCompanyBalanceByIdreceiverCompanyBalance(null);
                logCompanyBalanceCashFlow.setTblBankAccountByIdreceiverCbbankAccount(null);
                logCompanyBalanceCashFlow.setTransferNominal(reservationPayment.getUnitNominal());
                logCompanyBalanceCashFlow.setHistoryDate(Timestamp.valueOf(LocalDateTime.now()));
                logCompanyBalanceCashFlow.setTblEmployee(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                logCompanyBalanceCashFlow.setHistoryStystemNote("Pengembalian pembayaran reservasi kepada " + reservationCanceled.getTblReservation().getTblCustomer().getTblPeople().getFullName() + ", \nsebesar : "
                        + ClassFormatter.currencyFormat.format(reservationPayment.getUnitNominal()) + " ("
                        + reservationPayment.getRefFinanceTransactionPaymentType().getTypeName() + ")");
                session.saveOrUpdate(logCompanyBalanceCashFlow);

                //open close company balance - detail (cashier)
                TblOpenCloseCashierBalance dataOpenCloseCashierBalance = getDataOpenCloseCashierBalanceIsActive();
                if (dataOpenCloseCashierBalance
                        != null) {
                    TblOpenCloseCashierBalanceDetail dataOpenCloseCashierBalanceDetail = new TblOpenCloseCashierBalanceDetail();
                    dataOpenCloseCashierBalanceDetail.setTblOpenCloseCashierBalance(dataOpenCloseCashierBalance);
                    dataOpenCloseCashierBalanceDetail.setTblReservationPayment(reservationPayment);
                    dataOpenCloseCashierBalanceDetail.setDetailData(getDetailData(reservationPayment));
                    dataOpenCloseCashierBalanceDetail.setCreateDate(Timestamp.valueOf(LocalDateTime.now()));
                    dataOpenCloseCashierBalanceDetail.setTblEmployeeByCreateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                    dataOpenCloseCashierBalanceDetail.setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                    dataOpenCloseCashierBalanceDetail.setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                    dataOpenCloseCashierBalanceDetail.setRefRecordStatus(session.find(RefRecordStatus.class, 1));
                    session.saveOrUpdate(dataOpenCloseCashierBalanceDetail);
                }
                //data log
                dataLog += "-----------------------------------------------------------------------\n";

                //data reservation additonal item  - reserved
                List<TblReservationRoomTypeDetail> reservationRoomTypeDetails = getAllReservationRoomTypeDetailByIDReservation(reservationCanceled.getTblReservation().getIdreservation());
                for (TblReservationRoomTypeDetail reservationRoomTypeDetail : reservationRoomTypeDetails) {
                    //data reservation additonal item  - reserved (delete)
                    List<TblReservationAdditionalItemReserved> dataReservationAdditionalItemReserveds = getAllReservationAdditionalItemReservedByIDReservationRoomTypeDetail(
                            reservationRoomTypeDetail.getIddetail());
                    for (TblReservationAdditionalItemReserved dataReservationAdditionalItemReserved : dataReservationAdditionalItemReserveds) {
                        if (dataReservationAdditionalItemReserved.getRefReservationAdditionalItemReservedStatus().getIdstatus() == 1) {   //Reserved = '1'
                            dataReservationAdditionalItemReserved.setRefReservationAdditionalItemReservedStatus(session.find(RefReservationAdditionalItemReservedStatus.class, 3)); //Canceled = '3'
                            dataReservationAdditionalItemReserved.setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                            dataReservationAdditionalItemReserved.setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                            session.update(dataReservationAdditionalItemReserved);
                        }
                    }
                }

                //data log 
                LogSystem logSystem = new LogSystem();

                logSystem.setLogDateTime(Timestamp.valueOf(LocalDateTime.now()));
                logSystem.setTblSystemUser(session.find(TblSystemUser.class, ClassSession.currentUser.getIduser()));
                logSystem.setTblEmployee(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                logSystem.setLogHeader(headerLog);

                logSystem.setLogDetail(dataLog);

                logSystem.setHostName(ClassComp.getHostName());
                session.saveOrUpdate(logSystem);

                session.getTransaction()
                        .commit();
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
        return reservationCanceled;
    }

    private List<TblReservationRoomTypeDetail> getAllReservationRoomTypeDetailByIDReservation(long idReservation) {
        List<TblReservationRoomTypeDetail> list = session.getNamedQuery("findAllTblReservationRoomTypeDetailByIDReservation")
                .setParameter("idReservation", idReservation)
                .list();
        return list;
    }

    private List<TblReservationAdditionalItemReserved> getAllReservationAdditionalItemReservedByIDReservationRoomTypeDetail(
            long idRRTD) {
        List<TblReservationAdditionalItemReserved> list = session.getNamedQuery("findAllTblReservationAdditionalItemReservedByIDReservationRoomTypeDetail")
                .setParameter("idRRTD", idRRTD)
                .list();
        return list;
    }

    private List<TblReservationPaymentWithGuaranteePayment> getAllReservationPaymentWithGuaranteePaymentIsDebtByIDReservation(long idReservation) {
        //Querying
        String query = "from TblReservationPaymentWithGuaranteePayment t where "
                + "t.tblReservationPayment.tblReservationBill.tblReservation.idreservation='" + idReservation + "' and "
                + "t.isDebt='true' and "
                + "t.tblHotelReceivable is not null and "
                + "(t.refRecordStatus.idstatus=1 or t.refRecordStatus.idstatus=3)";
        List<TblReservationPaymentWithGuaranteePayment> list = (List<TblReservationPaymentWithGuaranteePayment>) session.createQuery(query).list();
        return list;
    }

    @Override
    public TblReservationCancelingFee insertDataReservationReschedule(
            TblReservation reservation,
            TblReservationCancelingFee reservationCancelingFee,
            List<TblReservationRescheduleCanceled> reservationRescheduleCanceleds,
            List<TblReservationRoomTypeDetail> newRRTDs,
            List<TblReservationRoomTypeDetailRoomPriceDetail> newRRTDRPDs,
            List<TblReservationRoomTypeDetailTravelAgentDiscountDetail> newRRTDADDs,
            List<TblReservationAdditionalItem> newRAIs,
            List<TblReservationAdditionalService> newRASs) {
        errMsg = "";
        TblReservationCancelingFee tblReservationCancelingFee = reservationCancelingFee;
        if (ClassSession.checkUserSession()) {  //check user current session
            try {
                session = HibernateUtil.getSessionFactory().getCurrentSession();
                session.beginTransaction();
                Query rs;
                //data log
                String headerLog = "BUAT DATA RESERVASI ULANG";
                String dataLog = "";
                //data reservation
                reservation.setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                reservation.setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                session.update(reservation);
                //data log
                dataLog += "No. Reservasi : " + reservation.getCodeReservation() + "\n";
                dataLog += "Tgl. Reservasi : " + ClassFormatter.dateFormate.format(reservation.getReservationDate()) + "\n";
                dataLog += "Nama Customer : " + reservation.getTblCustomer().getTblPeople().getFullName() + "\n";
                dataLog += "Travel Agent : " + (reservation.getTblPartner() != null ? reservation.getTblPartner().getPartnerName() : "-") + "\n";
                dataLog += "-----------------------------------------------------------------------\n";
                //data reservation room type detail - with details (delete)
                List<TblReservationRoomTypeDetail> oldRRTDs = getAllReservationRoomTypeDetailByIDReservation(reservation.getIdreservation());
                for (TblReservationRoomTypeDetail oldRRTD : oldRRTDs) {
                    //data reservation room type detail - room price detail (delete)
                    List<TblReservationRoomTypeDetailRoomPriceDetail> oldRRTDRPDs = getAllReservationRoomTypeDetailRoomPriceDetailByIDReservationRoomTypeDetail(oldRRTD.getIddetail());
                    for (TblReservationRoomTypeDetailRoomPriceDetail oldRRTDRPD : oldRRTDRPDs) {
                        //data reservation room price detail (delete)
                        TblReservationRoomPriceDetail oldRRPD = (TblReservationRoomPriceDetail) session.find(TblReservationRoomPriceDetail.class, oldRRTDRPD.getTblReservationRoomPriceDetail().getIddetail());
                        if (oldRRPD != null) {
                            oldRRPD.setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                            oldRRPD.setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                            oldRRPD.setRefRecordStatus(session.find(RefRecordStatus.class, 2));   //deleted
                            session.update(oldRRPD);
                        }
                        oldRRTDRPD.setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                        oldRRTDRPD.setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                        oldRRTDRPD.setRefRecordStatus(session.find(RefRecordStatus.class, 2));   //deleted
                        session.update(oldRRTDRPD);
                    }
                    //data reservation room type detail - travel agent discount (delete)
                    List<TblReservationRoomTypeDetailTravelAgentDiscountDetail> oldRRTDTADDs = getAllReservationRoomTypeDetailTravelAgentDiscountDetailByIDReservationRoomTypeDetail(oldRRTD.getIddetail());
                    for (TblReservationRoomTypeDetailTravelAgentDiscountDetail oldRRTDTADD : oldRRTDTADDs) {
                        //data reservation travel agent discount (delete)
                        TblReservationTravelAgentDiscountDetail oldRTADD = (TblReservationTravelAgentDiscountDetail) session.find(TblReservationTravelAgentDiscountDetail.class, oldRRTDTADD.getTblReservationTravelAgentDiscountDetail().getIddetail());
                        if (oldRTADD != null) {
                            oldRTADD.setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                            oldRTADD.setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                            oldRTADD.setRefRecordStatus(session.find(RefRecordStatus.class, 2));   //deleted
                            session.update(oldRTADD);
                        }
                        oldRRTDTADD.setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                        oldRRTDTADD.setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                        oldRRTDTADD.setRefRecordStatus(session.find(RefRecordStatus.class, 2));   //deleted
                        session.update(oldRRTDTADD);
                    }
                    //data reservation additional item (delete)
                    List<TblReservationAdditionalItem> oldRAIs = getAllReservationAdditionalItemByIDReservationRoomTypeDetail(oldRRTD.getIddetail());
                    for (TblReservationAdditionalItem oldRAI : oldRAIs) {
                        oldRAI.setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                        oldRAI.setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                        oldRAI.setRefRecordStatus(session.find(RefRecordStatus.class, 2));   //deleted
                        session.update(oldRAI);
                    }
                    //data reservation additional item - reserved (delete)
                    List<TblReservationAdditionalItemReserved> oldRAIRs = getAllReservationAdditionalItemReservedByIDReservationRoomTypeDetail(oldRRTD.getIddetail());
                    for (TblReservationAdditionalItemReserved oldRAIR : oldRAIRs) {
                        oldRAIR.setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                        oldRAIR.setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                        oldRAIR.setRefRecordStatus(session.find(RefRecordStatus.class, 2));   //deleted
                        session.update(oldRAIR);
                    }
                    //data reservation additional service (delete)
                    List<TblReservationAdditionalService> oldRASs = getAllReservationAdditionalServiceByIDReservationRoomTypeDetail(oldRRTD.getIddetail());
                    for (TblReservationAdditionalService oldRAS : oldRASs) {
                        oldRAS.setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                        oldRAS.setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                        oldRAS.setRefRecordStatus(session.find(RefRecordStatus.class, 2));   //deleted
                        session.update(oldRAS);
                    }
                    //data reservation room type detail - item (delete)
                    rs = session.getNamedQuery("deleteAllTblReservationRoomTypeDetailItemByIDRoomTypeDetail")
                            .setParameter("idRoomTypeDetail", oldRRTD.getIddetail())
                            .setParameter("idEmployee", ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee());
                    errMsg = (String) rs.uniqueResult();
                    if (!errMsg.equals("")) {
                        if (session.getTransaction().isActive()) {
                            session.getTransaction().rollback();
                        }
                        return null;
                    }
                    //data reservation room type detail - service (delete)
                    rs = session.getNamedQuery("deleteAllTblReservationRoomTypeDetailRoomServiceByIDRoomTypeDetail")
                            .setParameter("idRoomTypeDetail", oldRRTD.getIddetail())
                            .setParameter("idEmployee", ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee());
                    errMsg = (String) rs.uniqueResult();
                    if (!errMsg.equals("")) {
                        if (session.getTransaction().isActive()) {
                            session.getTransaction().rollback();
                        }
                        return null;
                    }
                    //data reservation room type detail (delete)
                    oldRRTD.setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                    oldRRTD.setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                    oldRRTD.setRefRecordStatus(session.find(RefRecordStatus.class, 2));   //deleted
                    session.update(oldRRTD);
                }
                //data reservation canceling fee
                tblReservationCancelingFee.setTblEmployeeByCreateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                tblReservationCancelingFee.setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                tblReservationCancelingFee.setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                tblReservationCancelingFee.setRefRecordStatus(session.find(RefRecordStatus.class, 1));
                session.saveOrUpdate(tblReservationCancelingFee);
                //data log
                dataLog += "Nominal Cancellation Fee : " + ClassFormatter.currencyFormat.cFormat(tblReservationCancelingFee.getCancelingFeeNominal()) + "\n";
                dataLog += "-----------------------------------------------------------------------\n";
                //data reschedule canceled
                for (TblReservationRescheduleCanceled reservationRescheduleCanceled : reservationRescheduleCanceleds) {
                    reservationRescheduleCanceled.setTblEmployeeByCreateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                    reservationRescheduleCanceled.setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                    reservationRescheduleCanceled.setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                    reservationRescheduleCanceled.setRefRecordStatus(session.find(RefRecordStatus.class, 1));
                    session.saveOrUpdate(reservationRescheduleCanceled);
                }
                //data reservation room type detail
                int countID = 1;
                for (TblReservationRoomTypeDetail rrtd : newRRTDs) {
                    System.out.println("id : " + rrtd);
                    String codeDetail = "";
                    for (int i = 0; i < 5; i++) {
                        codeDetail += "0";
                    }
                    codeDetail += String.valueOf(countID);
                    codeDetail = codeDetail.substring(codeDetail.length() - 5);
                    if (checkRoomAvailable(rrtd, reservation.getTblPartner())) {
                        //data reservation room type detail - check in/out
                        if (rrtd.getTblReservationCheckInOut() != null) {
                            rrtd.getTblReservationCheckInOut().setCreateDate(Timestamp.valueOf(LocalDateTime.now()));
                            rrtd.getTblReservationCheckInOut().setTblEmployeeByCreateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                            rrtd.getTblReservationCheckInOut().setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                            rrtd.getTblReservationCheckInOut().setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                            rrtd.getTblReservationCheckInOut().setRefRecordStatus(session.find(RefRecordStatus.class, 1));
                            session.saveOrUpdate(rrtd.getTblReservationCheckInOut());
                        }
                        //data reservation room type detail
                        rrtd.setCodeDetail(codeDetail);
                        rrtd.setCreateDate(Timestamp.valueOf(LocalDateTime.now()));
                        rrtd.setTblEmployeeByCreateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                        rrtd.setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                        rrtd.setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                        rrtd.setRefRecordStatus(session.find(RefRecordStatus.class, 1));
                        session.saveOrUpdate(rrtd);
                        //data reservation room type detail - room service
                        List<TblRoomTypeRoomService> roomTypeRoomServices = getAllRoomTypeRoomServiceByIDRoomType(rrtd.getTblRoomType().getIdroomType());
                        for (TblRoomTypeRoomService roomTypeRoomService : roomTypeRoomServices) {
                            TblReservationRoomTypeDetailRoomService reservationRoomTypeDetailRoomService = new TblReservationRoomTypeDetailRoomService();
                            reservationRoomTypeDetailRoomService.setTblReservationRoomTypeDetail(rrtd);
                            reservationRoomTypeDetailRoomService.setTblRoomService(roomTypeRoomService.getTblRoomService());
                            reservationRoomTypeDetailRoomService.setRoomServiceCost(roomTypeRoomService.getTblRoomService().getPrice());
                            reservationRoomTypeDetailRoomService.setPeopleNumber(roomTypeRoomService.getPeopleNumber());
                            reservationRoomTypeDetailRoomService.setCreateDate(Timestamp.valueOf(LocalDateTime.now()));
                            reservationRoomTypeDetailRoomService.setTblEmployeeByCreateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                            reservationRoomTypeDetailRoomService.setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                            reservationRoomTypeDetailRoomService.setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                            reservationRoomTypeDetailRoomService.setRefRecordStatus(session.find(RefRecordStatus.class, 1));
                            session.saveOrUpdate(reservationRoomTypeDetailRoomService);
                        }
                        //data reservation room type detail - item
                        List<TblRoomTypeItem> roomTypeItems = getAllRoomTypeItemByIDRoomType(rrtd.getTblRoomType().getIdroomType());
                        for (TblRoomTypeItem roomTypeItem : roomTypeItems) {
                            TblReservationRoomTypeDetailItem reservationRoomTypeDetailItem = new TblReservationRoomTypeDetailItem();
                            reservationRoomTypeDetailItem.setTblReservationRoomTypeDetail(rrtd);
                            reservationRoomTypeDetailItem.setTblItem(roomTypeItem.getTblItem());
                            reservationRoomTypeDetailItem.setItemAdditionalCharge(roomTypeItem.getTblItem().getAdditionalCharge());
                            reservationRoomTypeDetailItem.setItemBrokenCharge(roomTypeItem.getTblItem().getBrokenCharge());
                            reservationRoomTypeDetailItem.setItemQuantity(roomTypeItem.getItemQuantity());
                            reservationRoomTypeDetailItem.setCreateDate(Timestamp.valueOf(LocalDateTime.now()));
                            reservationRoomTypeDetailItem.setTblEmployeeByCreateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                            reservationRoomTypeDetailItem.setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                            reservationRoomTypeDetailItem.setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                            reservationRoomTypeDetailItem.setRefRecordStatus(session.find(RefRecordStatus.class, 1));
                            session.saveOrUpdate(reservationRoomTypeDetailItem);
                        }
                    } else {
                        if (session.getTransaction().isActive()) {
                            session.getTransaction().rollback();
                        }
                        errMsg = "Jumlah kamar tidak mencukupi..!";
                        return null;
                    }
                    countID++;
                }
                //data reservation room type detail - room price detail
                for (TblReservationRoomTypeDetailRoomPriceDetail rrtdrpd : newRRTDRPDs) {
                    //data reservation room price detail
                    rrtdrpd.getTblReservationRoomPriceDetail().setCreateDate(Timestamp.valueOf(LocalDateTime.now()));
                    rrtdrpd.getTblReservationRoomPriceDetail().setTblEmployeeByCreateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                    rrtdrpd.getTblReservationRoomPriceDetail().setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                    rrtdrpd.getTblReservationRoomPriceDetail().setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                    rrtdrpd.getTblReservationRoomPriceDetail().setRefRecordStatus(session.find(RefRecordStatus.class, 1));
                    session.saveOrUpdate(rrtdrpd.getTblReservationRoomPriceDetail());
                    //data reservation room type detail - room price detail
                    rrtdrpd.setCreateDate(Timestamp.valueOf(LocalDateTime.now()));
                    rrtdrpd.setTblEmployeeByCreateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                    rrtdrpd.setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                    rrtdrpd.setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                    rrtdrpd.setRefRecordStatus(session.find(RefRecordStatus.class, 1));
                    session.saveOrUpdate(rrtdrpd);
                }
                //data reservation room type detail - travel agent discount detail
                for (TblReservationRoomTypeDetailTravelAgentDiscountDetail rrtdtadd : newRRTDADDs) {
                    //data reservation travel agent discount detail
                    rrtdtadd.getTblReservationTravelAgentDiscountDetail().setCreateDate(Timestamp.valueOf(LocalDateTime.now()));
                    rrtdtadd.getTblReservationTravelAgentDiscountDetail().setTblEmployeeByCreateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                    rrtdtadd.getTblReservationTravelAgentDiscountDetail().setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                    rrtdtadd.getTblReservationTravelAgentDiscountDetail().setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                    rrtdtadd.getTblReservationTravelAgentDiscountDetail().setRefRecordStatus(session.find(RefRecordStatus.class, 1));
                    session.saveOrUpdate(rrtdtadd.getTblReservationTravelAgentDiscountDetail());
                    //data reservation room type detail - travel agent discount detail
                    rrtdtadd.setCreateDate(Timestamp.valueOf(LocalDateTime.now()));
                    rrtdtadd.setTblEmployeeByCreateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                    rrtdtadd.setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                    rrtdtadd.setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                    rrtdtadd.setRefRecordStatus(session.find(RefRecordStatus.class, 1));
                    session.saveOrUpdate(rrtdtadd);
                }
                //data log
                dataLog += "Reservasi Kamar (Baru) : \n";
                for (TblReservationRoomTypeDetail rrtd : newRRTDs) {
                    dataLog += "- " + rrtd.getCodeDetail() + " - " + rrtd.getCheckInDateTime() + " s/d " + rrtd.getCheckOutDateTime() + " \t "
                            + (getDataRoom(rrtd)) + " \n";
                    for (TblReservationRoomTypeDetailRoomPriceDetail rrtdrpd : newRRTDRPDs) {
                        if (rrtdrpd.getTblReservationRoomTypeDetail().getIddetail()
                                == rrtd.getIddetail()) {
                            dataLog += "\t - " + ClassFormatter.dateFormate.format(rrtdrpd.getTblReservationRoomPriceDetail().getDetailDate()) + " \t "
                                    + "Harga : " + ClassFormatter.currencyFormat.cFormat(rrtdrpd.getTblReservationRoomPriceDetail().getDetailPrice()) + " \t "
                                    + "Diskon : " + ClassFormatter.decimalFormat.cFormat(rrtdrpd.getTblReservationRoomPriceDetail().getDetailDiscountPercentage()) + "% (" + getDataDiscountEvent(rrtdrpd.getTblReservationRoomPriceDetail()) + ") \t "
                                    + "Compliment : " + ClassFormatter.currencyFormat.cFormat(rrtdrpd.getTblReservationRoomPriceDetail().getDetailComplimentary()) + " \t "
                                    + getDataTravelAgentCommission(rrtd, newRRTDADDs) + " \n";
                        }
                    }
                }
                //data log
                dataLog += "-----------------------------------------------------------------------\n";
                dataLog += "Tambahan Layanan (Baru) : \n";
                //data reservation additional service
                for (TblReservationAdditionalService ras : newRASs) {
                    //data reservation additional service
                    ras.setCreateDate(Timestamp.valueOf(LocalDateTime.now()));
                    ras.setTblEmployeeByCreateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                    ras.setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                    ras.setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                    ras.setRefRecordStatus(session.find(RefRecordStatus.class, 1));
                    session.saveOrUpdate(ras);
                    //data log
                    dataLog += "- " + ClassFormatter.dateFormate.format(ras.getAdditionalDate()) + " - " + ras.getTblRoomService().getServiceName() + " \t "
                            + "Harga : " + ClassFormatter.currencyFormat.cFormat(ras.getPrice()) + " \t "
                            + "Diskon : " + ClassFormatter.decimalFormat.cFormat(ras.getDiscountPercentage()) + "% (" + getDataDiscountEvent(ras) + ") \n";
                }
                //data log
                dataLog += "-----------------------------------------------------------------------\n";
                dataLog += "Tambahan Barang (Baru) : \n";
                //data reservation additional item
                for (TblReservationAdditionalItem rai : newRAIs) {
                    //data reservation additional item
                    rai.setCreateDate(Timestamp.valueOf(LocalDateTime.now()));
                    rai.setTblEmployeeByCreateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                    rai.setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                    rai.setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                    rai.setRefRecordStatus(session.find(RefRecordStatus.class, 1));
                    session.saveOrUpdate(rai);
                    //data log
                    dataLog += "- " + ClassFormatter.dateFormate.format(rai.getAdditionalDate()) + " - " + rai.getTblItem().getItemName() + " \t "
                            + "Harga : " + ClassFormatter.currencyFormat.cFormat(rai.getItemCharge()) + " \t "
                            + "Diskon : " + ClassFormatter.decimalFormat.cFormat(rai.getDiscountPercentage()) + "% (" + getDataDiscountEvent(rai) + ") \t "
                            + "Jumlah : " + ClassFormatter.decimalFormat.cFormat(rai.getItemQuantity()) + " \n";
                }
                //data log
                dataLog += "-----------------------------------------------------------------------\n";
                //data reservation payment - with guarantee payment - is debt (update)
                List<TblReservationPaymentWithGuaranteePayment> rpwgpIsDebts = getAllReservationPaymentWithGuaranteePaymentIsDebtByIDReservation(reservation.getIdreservation());
                for (TblReservationPaymentWithGuaranteePayment rpwgpIsDebt : rpwgpIsDebts) {
                    //data reservation payment - with guarantee payment -> hotel receivable
                    rpwgpIsDebt.getTblHotelReceivable().setRefFinanceTransactionStatus(session.find(RefFinanceTransactionStatus.class, 4)); //Dibatalkan = '4'
                    rpwgpIsDebt.getTblHotelReceivable().setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                    rpwgpIsDebt.getTblHotelReceivable().setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                    session.update(rpwgpIsDebt.getTblHotelReceivable());
                    //data guarantee letter item detail (delete)
                    List<TblGuaranteeLetterItemDetail> oldGLIDs = getAllGuaranteeLetterItemDetailByIDGuaranteeLetter(rpwgpIsDebt.getIddetail());
                    for (TblGuaranteeLetterItemDetail oldGLID : oldGLIDs) {
                        oldGLID.setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                        oldGLID.setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                        oldGLID.setRefRecordStatus(session.find(RefRecordStatus.class, 2));   //deleted
                        session.update(oldGLID);
                    }
                    //data reservation payment - with guarantee payment (delete)
                    rpwgpIsDebt.setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                    rpwgpIsDebt.setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                    rpwgpIsDebt.setRefRecordStatus(session.find(RefRecordStatus.class, 2));   //deleted
                    session.update(rpwgpIsDebt);
                    //data reservation payment (delete)
                    rpwgpIsDebt.getTblReservationPayment().setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                    rpwgpIsDebt.getTblReservationPayment().setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                    rpwgpIsDebt.getTblReservationPayment().setRefRecordStatus(session.find(RefRecordStatus.class, 2));   //deleted
                    session.update(rpwgpIsDebt.getTblReservationPayment());

                }
                //data log
                dataLog += "-----------------------------------------------------------------------\n";
                //data log 
                LogSystem logSystem = new LogSystem();

                logSystem.setLogDateTime(Timestamp.valueOf(LocalDateTime.now()));
                logSystem.setTblSystemUser(session.find(TblSystemUser.class, ClassSession.currentUser.getIduser()));
                logSystem.setTblEmployee(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                logSystem.setLogHeader(headerLog);

                logSystem.setLogDetail(dataLog);

                logSystem.setHostName(ClassComp.getHostName());
                session.saveOrUpdate(logSystem);

                session.getTransaction()
                        .commit();
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
        return tblReservationCancelingFee;
    }

    private List<TblReservationRoomTypeDetailRoomPriceDetail> getAllReservationRoomTypeDetailRoomPriceDetailByIDReservationRoomTypeDetail(long idRRTD) {
        List<TblReservationRoomTypeDetailRoomPriceDetail> list = session.getNamedQuery("findAllTblReservationRoomTypeDetailRoomPriceDetailByIDReservationRoomTypeDetail")
                .setParameter("idReservationRoomTypeDetail", idRRTD)
                .list();
        return list;
    }

    private List<TblReservationRoomTypeDetailTravelAgentDiscountDetail> getAllReservationRoomTypeDetailTravelAgentDiscountDetailByIDReservationRoomTypeDetail(long idRRTD) {
        List<TblReservationRoomTypeDetailTravelAgentDiscountDetail> list = session.getNamedQuery("findAllTblReservationRoomTypeDetailTravelAgentDiscountDetailByIDRoomTypeDetail")
                .setParameter("idRoomTypeDetail", idRRTD)
                .list();
        return list;
    }

    private List<TblReservationAdditionalItem> getAllReservationAdditionalItemByIDReservationRoomTypeDetail(long idRRTD) {
        List<TblReservationAdditionalItem> list = session.getNamedQuery("findAllTblReservationAdditionalItemByIDReservationRoomTypeDetail")
                .setParameter("idReservationRoomTypeDetail", idRRTD)
                .list();
        return list;
    }

    private List<TblReservationAdditionalService> getAllReservationAdditionalServiceByIDReservationRoomTypeDetail(long idRRTD) {
        List<TblReservationAdditionalService> list = session.getNamedQuery("findAllTblReservationAdditionalServiceByIDReservationRoomTypeDetail")
                .setParameter("idReservationRoomTypeDetail", idRRTD)
                .list();
        return list;
    }

    private List<TblGuaranteeLetterItemDetail> getAllGuaranteeLetterItemDetailByIDGuaranteeLetter(long idGuaranteeLetter) {
        List<TblGuaranteeLetterItemDetail> list = session.getNamedQuery("findAllTblGuaranteeLetterItemDetailByIDGuaranteeLetter")
                .setParameter("idGuaranteeLetter", idGuaranteeLetter)
                .list();
        return list;
    }

    @Override
    public TblReservationCanceled insertDataReservationCanceled(TblReservationCanceled reservationCanceled,
            TblReservation reservation,
            TblReservationCancelingFee reservationCancelingFee,
            List<TblReservationRescheduleCanceled> reservationRescheduleCanceleds,
            TblReservationPayment reservationPayment,
            TblReservationPaymentWithTransfer rpWithTransfer,
            TblReservationPaymentWithBankCard rpWithBankCard,
            TblReservationPaymentWithCekGiro rpWithCekGiro,
            TblReservationPaymentWithGuaranteePayment rpWithGuaranteePayment,
            List<TblGuaranteeLetterItemDetail> guaranteeLetterItemDetails) {
        errMsg = "";
        if (ClassSession.checkUserSession()) {  //check user current session
            try {
                session = HibernateUtil.getSessionFactory().getCurrentSession();
                session.beginTransaction();
                Query rs;
                //data log
                String headerLog = "BUAT DATA PEMBATALAN RESERVASI";
                String dataLog = "";
                //data reservation
                reservation.setRefReservationStatus(session.find(RefReservationStatus.class, 6));   //Batal = '6'
                reservation.setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                reservation.setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                session.update(reservation);
                //data log
                dataLog += "No. Reservasi : " + reservation.getCodeReservation() + "\n";
                dataLog += "Tgl. Reservasi : " + ClassFormatter.dateFormate.format(reservation.getReservationDate()) + "\n";
                dataLog += "Nama Customer : " + reservation.getTblCustomer().getTblPeople().getFullName() + "\n";
                dataLog += "Travel Agent : " + (reservation.getTblPartner() != null ? reservation.getTblPartner().getPartnerName() : "-") + "\n";
                dataLog += "-----------------------------------------------------------------------\n";
                //data reservation customer (change type customer -> blacklist?)
                reservation.getTblCustomer().setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                reservation.getTblCustomer().setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                session.update(reservation.getTblCustomer());
                //data reservation room type detail - with details (delete)
                List<TblReservationRoomTypeDetail> oldRRTDs = getAllReservationRoomTypeDetailByIDReservation(reservation.getIdreservation());
                for (TblReservationRoomTypeDetail oldRRTD : oldRRTDs) {
                    //data reservation room type detail - room price detail (delete)
                    List<TblReservationRoomTypeDetailRoomPriceDetail> oldRRTDRPDs = getAllReservationRoomTypeDetailRoomPriceDetailByIDReservationRoomTypeDetail(oldRRTD.getIddetail());
                    for (TblReservationRoomTypeDetailRoomPriceDetail oldRRTDRPD : oldRRTDRPDs) {
                        //data reservation room price detail (delete)
                        TblReservationRoomPriceDetail oldRRPD = (TblReservationRoomPriceDetail) session.find(TblReservationRoomPriceDetail.class, oldRRTDRPD.getTblReservationRoomPriceDetail().getIddetail());
                        if (oldRRPD != null) {
                            oldRRPD.setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                            oldRRPD.setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                            oldRRPD.setRefRecordStatus(session.find(RefRecordStatus.class, 2));   //deleted
                            session.update(oldRRPD);
                        }
                        oldRRTDRPD.setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                        oldRRTDRPD.setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                        oldRRTDRPD.setRefRecordStatus(session.find(RefRecordStatus.class, 2));   //deleted
                        session.update(oldRRTDRPD);
                    }
                    //data reservation room type detail - travel agent discount (delete)
                    List<TblReservationRoomTypeDetailTravelAgentDiscountDetail> oldRRTDTADDs = getAllReservationRoomTypeDetailTravelAgentDiscountDetailByIDReservationRoomTypeDetail(oldRRTD.getIddetail());
                    for (TblReservationRoomTypeDetailTravelAgentDiscountDetail oldRRTDTADD : oldRRTDTADDs) {
                        //data reservation travel agent discount (delete)
                        TblReservationTravelAgentDiscountDetail oldRTADD = (TblReservationTravelAgentDiscountDetail) session.find(TblReservationTravelAgentDiscountDetail.class, oldRRTDTADD.getTblReservationTravelAgentDiscountDetail().getIddetail());
                        if (oldRTADD != null) {
                            oldRTADD.setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                            oldRTADD.setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                            oldRTADD.setRefRecordStatus(session.find(RefRecordStatus.class, 2));   //deleted
                            session.update(oldRTADD);
                        }
                        oldRRTDTADD.setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                        oldRRTDTADD.setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                        oldRRTDTADD.setRefRecordStatus(session.find(RefRecordStatus.class, 2));   //deleted
                        session.update(oldRRTDTADD);
                    }
                    //data reservation additional item (delete)
                    List<TblReservationAdditionalItem> oldRAIs = getAllReservationAdditionalItemByIDReservationRoomTypeDetail(oldRRTD.getIddetail());
                    for (TblReservationAdditionalItem oldRAI : oldRAIs) {
                        oldRAI.setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                        oldRAI.setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                        oldRAI.setRefRecordStatus(session.find(RefRecordStatus.class, 2));   //deleted
                        session.update(oldRAI);
                    }
                    //data reservation additional item - reserved (delete)
                    List<TblReservationAdditionalItemReserved> oldRAIRs = getAllReservationAdditionalItemReservedByIDReservationRoomTypeDetail(oldRRTD.getIddetail());
                    for (TblReservationAdditionalItemReserved oldRAIR : oldRAIRs) {
                        oldRAIR.setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                        oldRAIR.setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                        oldRAIR.setRefRecordStatus(session.find(RefRecordStatus.class, 2));   //deleted
                        session.update(oldRAIR);
                    }
                    //data reservation additional service (delete)
                    List<TblReservationAdditionalService> oldRASs = getAllReservationAdditionalServiceByIDReservationRoomTypeDetail(oldRRTD.getIddetail());
                    for (TblReservationAdditionalService oldRAS : oldRASs) {
                        oldRAS.setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                        oldRAS.setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                        oldRAS.setRefRecordStatus(session.find(RefRecordStatus.class, 2));   //deleted
                        session.update(oldRAS);
                    }
                    //data reservation room type detail - item (delete)
                    rs = session.getNamedQuery("deleteAllTblReservationRoomTypeDetailItemByIDRoomTypeDetail")
                            .setParameter("idRoomTypeDetail", oldRRTD.getIddetail())
                            .setParameter("idEmployee", ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee());
                    errMsg = (String) rs.uniqueResult();
                    if (!errMsg.equals("")) {
                        if (session.getTransaction().isActive()) {
                            session.getTransaction().rollback();
                        }
                        return null;
                    }
                    //data reservation room type detail - service (delete)
                    rs = session.getNamedQuery("deleteAllTblReservationRoomTypeDetailRoomServiceByIDRoomTypeDetail")
                            .setParameter("idRoomTypeDetail", oldRRTD.getIddetail())
                            .setParameter("idEmployee", ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee());
                    errMsg = (String) rs.uniqueResult();
                    if (!errMsg.equals("")) {
                        if (session.getTransaction().isActive()) {
                            session.getTransaction().rollback();
                        }
                        return null;
                    }
                    //data reservation room type detail (delete)
                    oldRRTD.setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                    oldRRTD.setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                    oldRRTD.setRefRecordStatus(session.find(RefRecordStatus.class, 2));   //deleted
                    session.update(oldRRTD);
                }
                //data reservation canceling fee
                reservationCancelingFee.setTblEmployeeByCreateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                reservationCancelingFee.setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                reservationCancelingFee.setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                reservationCancelingFee.setRefRecordStatus(session.find(RefRecordStatus.class, 1));
                session.saveOrUpdate(reservationCancelingFee);
                //data log
                dataLog += "Nominal Cancellation Fee : " + ClassFormatter.currencyFormat.cFormat(reservationCancelingFee.getCancelingFeeNominal()) + "\n";
                dataLog += "-----------------------------------------------------------------------\n";
                //data reschedule canceled
                for (TblReservationRescheduleCanceled reservationRescheduleCanceled : reservationRescheduleCanceleds) {
                    reservationRescheduleCanceled.setTblEmployeeByCreateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                    reservationRescheduleCanceled.setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                    reservationRescheduleCanceled.setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                    reservationRescheduleCanceled.setRefRecordStatus(session.find(RefRecordStatus.class, 1));
                    session.saveOrUpdate(reservationRescheduleCanceled);
                }
                //data reservation payment - with guarantee payment - is debt (update)
                List<TblReservationPaymentWithGuaranteePayment> rpwgpIsDebts = getAllReservationPaymentWithGuaranteePaymentIsDebtByIDReservation(reservation.getIdreservation());
                for (TblReservationPaymentWithGuaranteePayment rpwgpIsDebt : rpwgpIsDebts) {
                    //data reservation payment - with guarantee payment -> hotel receivable
                    rpwgpIsDebt.getTblHotelReceivable().setRefFinanceTransactionStatus(session.find(RefFinanceTransactionStatus.class, 4)); //Dibatalkan = '4'
                    rpwgpIsDebt.getTblHotelReceivable().setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                    rpwgpIsDebt.getTblHotelReceivable().setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                    session.update(rpwgpIsDebt.getTblHotelReceivable());
                    //data guarantee letter item detail (delete)
                    List<TblGuaranteeLetterItemDetail> oldGLIDs = getAllGuaranteeLetterItemDetailByIDGuaranteeLetter(rpwgpIsDebt.getIddetail());
                    for (TblGuaranteeLetterItemDetail oldGLID : oldGLIDs) {
                        oldGLID.setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                        oldGLID.setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                        oldGLID.setRefRecordStatus(session.find(RefRecordStatus.class, 2));   //deleted
                        session.update(oldGLID);
                    }
                    //data reservation payment - with guarantee payment (delete)
                    rpwgpIsDebt.setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                    rpwgpIsDebt.setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                    rpwgpIsDebt.setRefRecordStatus(session.find(RefRecordStatus.class, 2));   //deleted
                    session.update(rpwgpIsDebt);
                    //data reservation payment (delete)
                    rpwgpIsDebt.getTblReservationPayment().setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                    rpwgpIsDebt.getTblReservationPayment().setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                    rpwgpIsDebt.getTblReservationPayment().setRefRecordStatus(session.find(RefRecordStatus.class, 2));   //deleted
                    session.update(rpwgpIsDebt.getTblReservationPayment());

                }
                //data reservation canceled
                reservationCanceled.setCancelDateTime(Timestamp.valueOf(LocalDateTime.now()));
                reservationCanceled.setCreateDate(Timestamp.valueOf(LocalDateTime.now()));
                reservationCanceled.setTblEmployeeByCreateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                reservationCanceled.setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                reservationCanceled.setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                reservationCanceled.setRefRecordStatus(session.find(RefRecordStatus.class, 1));
                session.saveOrUpdate(reservationCanceled);
                //data log
                dataLog += "Alasan Pembatalan : " + reservationCanceled.getCancelNote() + "\n";
                dataLog += "-----------------------------------------------------------------------\n";
                dataLog += "Transaksi Pembayaran : \n";
                //data payment discount & charge
                BigDecimal paymentDiscount = new BigDecimal("0");
                BigDecimal paymentCharge = new BigDecimal("0");
                reservationPayment.setCodePayment(ClassCoder.getCode("Reservation Payment", session));
                reservationPayment.setCreateDate(Timestamp.valueOf(LocalDateTime.now()));
                reservationPayment.setTblEmployeeByCreateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                reservationPayment.setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                reservationPayment.setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                reservationPayment.setRefRecordStatus(session.find(RefRecordStatus.class, 1));
                session.saveOrUpdate(reservationPayment);
                //data log
                dataLog += "- " + reservationPayment.getCodePayment() + " - " + ClassFormatter.dateTimeFormate.format(reservationPayment.getPaymentDate()) + " \t "
                        + "Nominal : " + ClassFormatter.currencyFormat.cFormat(reservationPayment.getUnitNominal()) + " \t "
                        + "Tipe : " + reservationPayment.getRefFinanceTransactionPaymentType().getTypeName() + " \n";
                //@@@%%%
                //data bank account (customer)
                TblBankAccount customerBankAccount = null;
                //data company balance - bank account
                TblCompanyBalanceBankAccount companyBalanceBankAccount = null;
                //data reservation transaction payment - with transfer
                if (rpWithTransfer != null) { //transfer
                    rpWithTransfer.setCreateDate(Timestamp.valueOf(LocalDateTime.now()));
                    rpWithTransfer.setTblEmployeeByCreateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                    rpWithTransfer.setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                    rpWithTransfer.setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                    rpWithTransfer.setRefRecordStatus(session.find(RefRecordStatus.class, 1));
                    session.saveOrUpdate(rpWithTransfer);
                    //@@@%%%
                    //data bank account (customer)
                    customerBankAccount = rpWithTransfer.getTblBankAccountBySenderBankAccount();
                    //data company balance - bank account
                    companyBalanceBankAccount = getCompanyBalanceBankAccountByIDCompanyBalanceAndIDBankAccount((long) 1, rpWithTransfer.getTblBankAccountByReceiverBankAccount().getIdbankAccount());    //hotel balance = '1'
                }
                //data reservation transaction payment - with bank card
                if (rpWithBankCard != null) { //bank card
                    rpWithBankCard.setRefEdctransactionStatus(session.find(RefEdctransactionStatus.class, 0)); //Sale = '0'
                    rpWithBankCard.setCreateDate(Timestamp.valueOf(LocalDateTime.now()));
                    rpWithBankCard.setTblEmployeeByCreateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                    rpWithBankCard.setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                    rpWithBankCard.setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                    rpWithBankCard.setRefRecordStatus(session.find(RefRecordStatus.class, 1));
                    session.saveOrUpdate(rpWithBankCard);
                    //@@@%%%
                    //data company balance - bank account
                    companyBalanceBankAccount = getCompanyBalanceBankAccountByIDCompanyBalanceAndIDBankAccount((long) 1, rpWithBankCard.getTblBankAccount().getIdbankAccount());    //hotel balance = '1'
                    //minus payment discount n payment charge (MDR)
                    if (companyBalanceBankAccount != null) {
//                                    companyBalanceBankAccount.setCompanyBalanceBankAccountNominal(companyBalanceBankAccount.getCompanyBalanceBankAccountNominal()
//                                            .subtract(reservationPaymentWithBankCard.getPaymentDiscount())
//                                            .subtract(reservationPaymentWithBankCard.getPaymentCharge()));
                        paymentDiscount = paymentDiscount.add(rpWithBankCard.getPaymentDiscount());
                        paymentCharge = paymentCharge.add(rpWithBankCard.getPaymentCharge());
                    }
                }
                //data reservation transaction payment - with cek/giro
                if (rpWithCekGiro != null) {  //cek - giro
                    rpWithCekGiro.setCreateDate(Timestamp.valueOf(LocalDateTime.now()));
                    rpWithCekGiro.setTblEmployeeByCreateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                    rpWithCekGiro.setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                    rpWithCekGiro.setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                    rpWithCekGiro.setRefRecordStatus(session.find(RefRecordStatus.class, 1));
                    session.saveOrUpdate(rpWithCekGiro);
                    //@@@%%%
                    //data bank account (customer)
                    customerBankAccount = rpWithCekGiro.getTblBankAccountBySenderBankAccount();
                    //data company balance - bank account
                    companyBalanceBankAccount = getCompanyBalanceBankAccountByIDCompanyBalanceAndIDBankAccount((long) 1, rpWithCekGiro.getTblBankAccountByReceiverBankAccount().getIdbankAccount());    //hotel balance = '1'
                }
                //data reservation transaction payment - with guarantee payment
                if (rpWithGuaranteePayment != null) {
                    rpWithGuaranteePayment.setCreateDate(Timestamp.valueOf(LocalDateTime.now()));
                    rpWithGuaranteePayment.setTblEmployeeByCreateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                    rpWithGuaranteePayment.setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                    rpWithGuaranteePayment.setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                    rpWithGuaranteePayment.setRefRecordStatus(session.find(RefRecordStatus.class, 1));
                    session.saveOrUpdate(rpWithGuaranteePayment);
                    //data guarantee letter - item detail
                    for (TblGuaranteeLetterItemDetail guaranteeLetterItemDetail : guaranteeLetterItemDetails) {
                        guaranteeLetterItemDetail.setCreateDate(Timestamp.valueOf(LocalDateTime.now()));
                        guaranteeLetterItemDetail.setTblEmployeeByCreateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                        guaranteeLetterItemDetail.setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                        guaranteeLetterItemDetail.setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                        guaranteeLetterItemDetail.setRefRecordStatus(session.find(RefRecordStatus.class, 1));
                        session.saveOrUpdate(guaranteeLetterItemDetail);
                    }
                    //@@@%%%
                    if (rpWithGuaranteePayment.getIsDebt()) {
                        //data bank account (customer)
                        customerBankAccount = null;
                        //data company balance - bank account
                        companyBalanceBankAccount = null;
                    } else {
                        //data bank account (customer)
                        customerBankAccount = rpWithGuaranteePayment.getTblBankAccountBySenderBankAccount();
                        //data company balance - bank account
                        companyBalanceBankAccount = getCompanyBalanceBankAccountByIDCompanyBalanceAndIDBankAccount((long) 1, rpWithGuaranteePayment.getTblBankAccountByReceiverBankAccount().getIdbankAccount());    //hotel balance = '1'
                    }
                    //data reservation with guarantee letter - hotel receivable
                    if (rpWithGuaranteePayment.getIsDebt()) {
                        //data hotel invoice
                        TblHotelInvoice hotelInvoice = new TblHotelInvoice();
                        hotelInvoice.setCodeHotelInvoice(ClassCoder.getCode("Hotel Invoice", session));
                        hotelInvoice.setTblPartner(rpWithGuaranteePayment.getTblPartner());
                        hotelInvoice.setTblSupplier(null);
                        hotelInvoice.setRefHotelInvoiceType(session.find(RefHotelInvoiceType.class, 1));    //Receivable = '1'
                        hotelInvoice.setCreateDate(Timestamp.valueOf(LocalDateTime.now()));
                        hotelInvoice.setTblEmployeeByCreateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                        hotelInvoice.setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                        hotelInvoice.setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                        hotelInvoice.setRefRecordStatus(session.find(RefRecordStatus.class, 1));
                        session.saveOrUpdate(hotelInvoice);
                        //data hotel receivable
                        TblHotelReceivable hotelReceivable = new TblHotelReceivable();
                        hotelReceivable.setTblHotelInvoice(hotelInvoice);
                        hotelReceivable.setHotelReceivableNominal(reservationPayment.getUnitNominal());
                        hotelReceivable.setRefHotelReceivableType(session.find(RefHotelReceivableType.class, 1));   //Guarantee Letter = '1'
                        hotelReceivable.setRefFinanceTransactionStatus(session.find(RefFinanceTransactionStatus.class, 0));    //Belum Dibayar = '0'
                        hotelReceivable.setCreateDate(Timestamp.valueOf(LocalDateTime.now()));
                        hotelReceivable.setTblEmployeeByCreateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                        hotelReceivable.setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                        hotelReceivable.setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                        hotelReceivable.setRefRecordStatus(session.find(RefRecordStatus.class, 1));
                        session.saveOrUpdate(hotelReceivable);
                        //data reservation with guarantee payment - (hotel receivable)
                        rpWithGuaranteePayment.setTblHotelReceivable(hotelReceivable);
                        rpWithGuaranteePayment.setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                        rpWithGuaranteePayment.setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                        session.saveOrUpdate(rpWithGuaranteePayment);
                    }
                }

                //@@@%%%
                //data balance (cashier)
                if (reservationPayment.getRefFinanceTransactionPaymentType().getIdtype() == (long) 0) {   //plus
//                                || reservationPayment.getRefFinanceTransactionPaymentType().getIdtype() == (long) 13) {   //plus
                    //data balance (company)
                    TblCompanyBalance dataBalance = session.find(TblCompanyBalance.class, (long) 3);
                    dataBalance.setBalanceNominal(dataBalance.getBalanceNominal().add(reservationPayment.getUnitNominal()));
                    dataBalance.setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                    dataBalance.setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                    session.update(dataBalance);
                    //data company balance (cash-flow log)
                    LogCompanyBalanceCashFlow logCompanyBalanceCashFlow = new LogCompanyBalanceCashFlow();
                    logCompanyBalanceCashFlow.setTblCompanyBalanceByIdsenderCompanyBalance(null);
                    logCompanyBalanceCashFlow.setTblBankAccountByIdsenderCbbankAccount(null);
                    logCompanyBalanceCashFlow.setTblCompanyBalanceByIdreceiverCompanyBalance(session.find(TblCompanyBalance.class, (long) 3));  //Kasir = '3'
                    logCompanyBalanceCashFlow.setTblBankAccountByIdreceiverCbbankAccount(null);
                    logCompanyBalanceCashFlow.setTransferNominal(reservationPayment.getUnitNominal());
                    logCompanyBalanceCashFlow.setHistoryDate(Timestamp.valueOf(LocalDateTime.now()));
                    logCompanyBalanceCashFlow.setTblEmployee(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                    logCompanyBalanceCashFlow.setHistoryStystemNote("Pembayaran reservasi oleh " + reservation.getTblCustomer().getTblPeople().getFullName() + ", \nsebesar : "
                            + ClassFormatter.currencyFormat.format(reservationPayment.getUnitNominal()) + " ("
                            + reservationPayment.getRefFinanceTransactionPaymentType().getTypeName() + ")");
                    session.saveOrUpdate(logCompanyBalanceCashFlow);
                }
                if (reservationPayment.getRefFinanceTransactionPaymentType().getIdtype() == (long) 16) {   //minus (return)
                    //data balance (company)
                    TblCompanyBalance dataBalance = session.find(TblCompanyBalance.class, (long) 3);
                    dataBalance.setBalanceNominal(dataBalance.getBalanceNominal().subtract(reservationPayment.getUnitNominal()));
                    dataBalance.setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                    dataBalance.setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                    session.update(dataBalance);
                    //data company balance (cash-flow log)
                    LogCompanyBalanceCashFlow logCompanyBalanceCashFlow = new LogCompanyBalanceCashFlow();
                    logCompanyBalanceCashFlow.setTblCompanyBalanceByIdsenderCompanyBalance(dataBalance);
                    logCompanyBalanceCashFlow.setTblBankAccountByIdsenderCbbankAccount(null);
                    logCompanyBalanceCashFlow.setTblCompanyBalanceByIdreceiverCompanyBalance(null);
                    logCompanyBalanceCashFlow.setTblBankAccountByIdreceiverCbbankAccount(null);
                    logCompanyBalanceCashFlow.setTransferNominal(reservationPayment.getUnitNominal());
                    logCompanyBalanceCashFlow.setHistoryDate(Timestamp.valueOf(LocalDateTime.now()));
                    logCompanyBalanceCashFlow.setTblEmployee(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                    logCompanyBalanceCashFlow.setHistoryStystemNote("Pengembalian pembayaran reservasi kepada " + reservationCanceled.getTblReservation().getTblCustomer().getTblPeople().getFullName() + ", \nsebesar : "
                            + ClassFormatter.currencyFormat.format(reservationPayment.getUnitNominal()) + " ("
                            + reservationPayment.getRefFinanceTransactionPaymentType().getTypeName() + ")");
                    session.saveOrUpdate(logCompanyBalanceCashFlow);
                }
                //data balance (company)
                if (reservationPayment.getRefFinanceTransactionPaymentType().getIdtype() > (long) 0
                        && reservationPayment.getRefFinanceTransactionPaymentType().getIdtype() < (long) 9) {   //plus
                    if (companyBalanceBankAccount != null) {

                    }
                }
                //auto settle
                if ((reservationPayment.getRefFinanceTransactionPaymentType().getIdtype() < (long) 1
                        && reservationPayment.getRefFinanceTransactionPaymentType().getIdtype() > (long) 8)
                        || companyBalanceBankAccount == null) { //is debt, payment -> debt (data hasn't been used)
                    //data reservation payment
                    reservationPayment.setSettleDate(Timestamp.valueOf(LocalDateTime.now()));
                    session.update(reservationPayment);
                }
                //open close company balance - detail (cashier)
                TblOpenCloseCashierBalance dataOpenCloseCashierBalance = getDataOpenCloseCashierBalanceIsActive();
                if (dataOpenCloseCashierBalance != null) {
                    TblOpenCloseCashierBalanceDetail dataOpenCloseCashierBalanceDetail = new TblOpenCloseCashierBalanceDetail();
                    dataOpenCloseCashierBalanceDetail.setTblOpenCloseCashierBalance(dataOpenCloseCashierBalance);
                    dataOpenCloseCashierBalanceDetail.setTblReservationPayment(reservationPayment);
                    dataOpenCloseCashierBalanceDetail.setDetailData(getDetailData(reservationPayment));
                    dataOpenCloseCashierBalanceDetail.setCreateDate(Timestamp.valueOf(LocalDateTime.now()));
                    dataOpenCloseCashierBalanceDetail.setTblEmployeeByCreateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                    dataOpenCloseCashierBalanceDetail.setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                    dataOpenCloseCashierBalanceDetail.setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                    dataOpenCloseCashierBalanceDetail.setRefRecordStatus(session.find(RefRecordStatus.class, 1));
                    session.saveOrUpdate(dataOpenCloseCashierBalanceDetail);
                }

                //data log 
                LogSystem logSystem = new LogSystem();

                logSystem.setLogDateTime(Timestamp.valueOf(LocalDateTime.now()));
                logSystem.setTblSystemUser(session.find(TblSystemUser.class, ClassSession.currentUser.getIduser()));
                logSystem.setTblEmployee(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                logSystem.setLogHeader(headerLog);

                logSystem.setLogDetail(dataLog);

                logSystem.setHostName(ClassComp.getHostName());
                session.saveOrUpdate(logSystem);

                session.getTransaction()
                        .commit();
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
        return reservationCanceled;
    }

    //--------------------------------------------------------------------------
    @Override
    public TblReservation getReservation(long id) {
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
    public List<TblReservation> getAllDataReservation() {
        errMsg = "";
        List<TblReservation> list = new ArrayList<>();
        if (ClassSession.checkUserSession()) {  //check user current session
            try {
                session = HibernateUtil.getSessionFactory().getCurrentSession();
                session.beginTransaction();
                list = session.getNamedQuery("findAllTblReservation").list();
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
    public RefReservationStatus getReservationStatus(int id) {
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

//    @Override
//    public RefReservationStatus getReservationStatusByStatusName(String name) {
//        //@@@---
//        errMsg = "";
//        List<RefReservationStatus> list = new ArrayList<>();
//        try {
//            session = HibernateUtil.getSessionFactory().getCurrentSession();
//            session.beginTransaction();
//            list = session.getNamedQuery("findAllRefReservationStatusByStatusName")
//                    .setParameter("statusName", name)
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
    public List<RefReservationStatus> getAllDataReservationStatus() {
        errMsg = "";
        List<RefReservationStatus> list = new ArrayList<>();
        if (ClassSession.checkUserSession()) {  //check user current session
            try {
                session = HibernateUtil.getSessionFactory().getCurrentSession();
                session.beginTransaction();
                list = session.getNamedQuery("findAllRefReservationStatus").list();
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
    public TblCustomer insertDataCustomer(TblCustomer customer, String imgExtention,
            List<TblCustomerBankAccount> customerBankAccount) {
        errMsg = "";
        TblCustomer tblCustomer = customer;
        if (ClassSession.checkUserSession()) {  //check user current session
            try {
                session = HibernateUtil.getSessionFactory().getCurrentSession();
                session.beginTransaction();
                //data people
                tblCustomer.getTblPeople().setCreateDate(Timestamp.valueOf(LocalDateTime.now()));
                tblCustomer.getTblPeople().setTblEmployeeByCreateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                tblCustomer.getTblPeople().setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                tblCustomer.getTblPeople().setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                tblCustomer.getTblPeople().setRefRecordStatus(session.find(RefRecordStatus.class, 1));
                session.saveOrUpdate(tblCustomer.getTblPeople());
                tblCustomer.getTblPeople().setImageUrl(tblCustomer.getTblPeople().getIdpeople() + "." + imgExtention);
                session.update(tblCustomer.getTblPeople());
                //data customer
                tblCustomer.setCodeCustomer(ClassCoder.getCode("Customer", session));
                tblCustomer.setRegistrationDate(Timestamp.valueOf(LocalDateTime.now()));
                tblCustomer.setRefCustomerType(session.find(RefCustomerType.class, 0));
                tblCustomer.setRefCustomerStatus(session.find(RefCustomerStatus.class, 0));
                tblCustomer.setCreateDate(Timestamp.valueOf(LocalDateTime.now()));
                tblCustomer.setTblEmployeeByCreateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                tblCustomer.setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                tblCustomer.setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                tblCustomer.setRefRecordStatus(session.find(RefRecordStatus.class, 1));
                session.saveOrUpdate(tblCustomer);
                //save or update data bank account n customer bank acccount
                for (TblCustomerBankAccount cba : customerBankAccount) {
                    //data bank account
                    cba.getTblBankAccount().setCreateDate(Timestamp.valueOf(LocalDateTime.now()));
                    cba.getTblBankAccount().setTblEmployeeByCreateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                    cba.getTblBankAccount().setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                    cba.getTblBankAccount().setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                    cba.getTblBankAccount().setRefRecordStatus(session.find(RefRecordStatus.class, 1));
                    session.saveOrUpdate(cba.getTblBankAccount());
                    //data customer bank account
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
    public boolean updateDataCustomer(TblCustomer customer, String imgExtention,
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
                    cba.getTblBankAccount().setCreateDate(Timestamp.valueOf(LocalDateTime.now()));
                    cba.getTblBankAccount().setTblEmployeeByCreateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                    cba.getTblBankAccount().setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                    cba.getTblBankAccount().setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                    cba.getTblBankAccount().setRefRecordStatus(session.find(RefRecordStatus.class, 1));
                    session.saveOrUpdate(cba.getTblBankAccount());
                    //data customer bank account
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

    @Override
    public TblCustomerBankAccount insertDataCustomerBankAccount(TblCustomerBankAccount customerBankAccount) {
        errMsg = "";
        TblCustomerBankAccount tblCustomerBankAccount = customerBankAccount;
        if (ClassSession.checkUserSession()) {  //check user current session
            try {
                session = HibernateUtil.getSessionFactory().getCurrentSession();
                session.beginTransaction();
                //save data bank account n customer bank acccount
                //data bank account
                tblCustomerBankAccount.getTblBankAccount().setCreateDate(Timestamp.valueOf(LocalDateTime.now()));
                tblCustomerBankAccount.getTblBankAccount().setTblEmployeeByCreateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                tblCustomerBankAccount.getTblBankAccount().setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                tblCustomerBankAccount.getTblBankAccount().setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                tblCustomerBankAccount.getTblBankAccount().setRefRecordStatus(session.find(RefRecordStatus.class, 1));
                session.saveOrUpdate(tblCustomerBankAccount.getTblBankAccount());
                //data customer bank account
                tblCustomerBankAccount.setRefBankAccountHolderStatus(session.find(RefBankAccountHolderStatus.class, 0));
                tblCustomerBankAccount.setCreateDate(Timestamp.valueOf(LocalDateTime.now()));
                tblCustomerBankAccount.setTblEmployeeByCreateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                tblCustomerBankAccount.setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                tblCustomerBankAccount.setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                tblCustomerBankAccount.setRefRecordStatus(session.find(RefRecordStatus.class, 1));
                session.saveOrUpdate(tblCustomerBankAccount);
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
        return tblCustomerBankAccount;
    }

    @Override
    public TblCustomerBankAccount getDataCustomerBankAccount(long id) {
        errMsg = "";
        TblCustomerBankAccount data = null;
        if (ClassSession.checkUserSession()) {  //check user current session
            try {
                session = HibernateUtil.getSessionFactory().getCurrentSession();
                session.beginTransaction();
                data = (TblCustomerBankAccount) session.find(TblCustomerBankAccount.class, id);
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
    public List<TblCustomerBankAccount> getAllDataCustomerBankAccountByIDCustomer(long idCustomer) {
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

    @Override
    public List<RefCountry> getAllDataPeopleCountry() {
        errMsg = "";
        List<RefCountry> list = new ArrayList<>();
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
                errMsg = e.getMessage();
            } finally {
                //session.close();
            }
        }
        return list;
    }

    @Override
    public boolean updateDataCustomerDeposit(TblCustomer customer, TblReservationPayment depositTransaction) {
        errMsg = "";
        if (ClassSession.checkUserSession()) {  //check user current session
            try {
                session = HibernateUtil.getSessionFactory().getCurrentSession();
                session.beginTransaction();
                //data people
                customer.getTblPeople().setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                customer.getTblPeople().setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                session.update(customer.getTblPeople());
                //data customer
                customer.setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                customer.setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                session.update(customer);
                //data transaction payment (open deposit (cash))
                depositTransaction.setCreateDate(Timestamp.valueOf(LocalDateTime.now()));
                depositTransaction.setTblEmployeeByCreateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                depositTransaction.setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                depositTransaction.setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                depositTransaction.setRefRecordStatus(session.find(RefRecordStatus.class, 1));
                session.saveOrUpdate(depositTransaction);
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
    public TblReservationBill getReservationBill(long id) {
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
    public TblReservationBill getReservationBillByIDReservation(long idReservation) {
        errMsg = "";
        List<TblReservationBill> list = new ArrayList<>();
        if (ClassSession.checkUserSession()) {  //check user current session
            try {
                session = HibernateUtil.getSessionFactory().getCurrentSession();
                session.beginTransaction();
                list = session.getNamedQuery("findAllTblReservationBillByIDReservationAndIDReservationBillType")
                        .setParameter("idReservation", idReservation)
                        .setParameter("idType", 0)
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
    public List<TblReservationBill> getAllDataReservationBill() {
        errMsg = "";
        List<TblReservationBill> list = new ArrayList<>();
        if (ClassSession.checkUserSession()) {  //check user current session
            try {
                session = HibernateUtil.getSessionFactory().getCurrentSession();
                session.beginTransaction();
                list = session.getNamedQuery("findAllTblReservationBill").list();
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
    public TblReservationRoomTypeDetail getReservationRoomTypeDetail(long id) {
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

    @Override
    public List<TblReservationRoomTypeDetail> getAllDataReservationRoomTypeDetail() {
        errMsg = "";
        List<TblReservationRoomTypeDetail> list = new ArrayList<>();
        if (ClassSession.checkUserSession()) {  //check user current session
            try {
                session = HibernateUtil.getSessionFactory().getCurrentSession();
                session.beginTransaction();
                list = session.getNamedQuery("findAllTblReservationRoomTypeDetail").list();
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
    public List<TblReservationRoomTypeDetail> getAllDataReservationRoomTypeDetailByIDReservation(long idReservation) {
        errMsg = "";
        List<TblReservationRoomTypeDetail> list = new ArrayList<>();
        if (ClassSession.checkUserSession()) {  //check user current session
            try {
                session = HibernateUtil.getSessionFactory().getCurrentSession();
                session.beginTransaction();
                list = session.getNamedQuery("findAllTblReservationRoomTypeDetailByIDReservation")
                        .setParameter("idReservation", idReservation)
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
    public RefReservationOrderByType getReservationOrderByType(int id) {
        errMsg = "";
        RefReservationOrderByType data = null;
        if (ClassSession.checkUserSession()) {  //check user current session
            try {
                session = HibernateUtil.getSessionFactory().getCurrentSession();
                session.beginTransaction();
                data = (RefReservationOrderByType) session.find(RefReservationOrderByType.class, id);
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
    public List<RefReservationOrderByType> getAllDataReservationOrderByType() {
        errMsg = "";
        List<RefReservationOrderByType> list = new ArrayList<>();
        if (ClassSession.checkUserSession()) {  //check user current session
            try {
                session = HibernateUtil.getSessionFactory().getCurrentSession();
                session.beginTransaction();
                list = session.getNamedQuery("findAllRefReservationOrderByType").list();
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
    public TblRoomType getRoomType(long id) {
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

    @Override
    public TblTravelAgentRoomType getDataTravelAgentRoomTypeByIDRoomTypeAndIDPartnerAndAvailableDate(long idRoomType,
            long idPartner,
            Date availableDate) {
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

    @Override
    public List<TblTravelAgentRoomType> getAllDataTravelAgentRoomTypeByIDRoomTypeAndIDPartnerAndAvailableDate(
            long idRoomType,
            long idPartner,
            Date availableDate) {
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
    public TblReservationRoomTypeDetailRoomPriceDetail getReservationRoomTypeDetailRoomPriceDetail(long id) {
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
    public TblReservationRoomTypeDetailRoomPriceDetail getReservationRoomTypeDetailRoomPriceDetailByIDRoomPriceDetail(long idRoomPriceDetail) {
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
    public TblReservationTravelAgentDiscountDetail getReservationTravelAgentDiscountDetail(long id) {
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
    public TblReservationRoomTypeDetailTravelAgentDiscountDetail getReservationRoomTypeDetailTravelAgentDiscountDetailByIDReservationRoomTypeDetail(long idRoomTypeDetail) {
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
    @Override
    public TblReservationCalendarBar getReservationCalendarBARByCalendarDate(Date calendarDate) {
        errMsg = "";
        List<TblReservationCalendarBar> list = new ArrayList<>();
        if (ClassSession.checkUserSession()) {  //check user current session
            try {
                session = HibernateUtil.getSessionFactory().getCurrentSession();
                session.beginTransaction();
                list = session.getNamedQuery("findAllTblReservationCalendarBarByCalendarDate")
                        .setParameter("calendarDate", calendarDate)
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
    public TblReservationDefaultBar getReservationDefaultBARByDayOfWeek(int dayOfWeek) {
        errMsg = "";
        List<TblReservationDefaultBar> list = new ArrayList<>();
        if (ClassSession.checkUserSession()) {  //check user current session
            try {
                session = HibernateUtil.getSessionFactory().getCurrentSession();
                session.beginTransaction();
                list = session.getNamedQuery("findAllTblReservationDefaultBarByDayOfWeek")
                        .setParameter("dayOfWeek", dayOfWeek)
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
    public TblReservationBar getReservationBAR(long id) {
        errMsg = "";
        TblReservationBar data = null;
        if (ClassSession.checkUserSession()) {  //check user current session
            try {
                session = HibernateUtil.getSessionFactory().getCurrentSession();
                session.beginTransaction();
                data = (TblReservationBar) session.find(TblReservationBar.class, id);
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
    public TblReservationCheckInOut getReservationCheckInOut(long id) {
        errMsg = "";
        TblReservationCheckInOut data = null;
        if (ClassSession.checkUserSession()) {  //check user current session
            try {
                session = HibernateUtil.getSessionFactory().getCurrentSession();
                session.beginTransaction();
                data = (TblReservationCheckInOut) session.find(TblReservationCheckInOut.class, id);
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
    public List<TblReservationCheckInOut> getAllDataReservationCheckInOut() {
        errMsg = "";
        List<TblReservationCheckInOut> list = new ArrayList<>();
        if (ClassSession.checkUserSession()) {  //check user current session
            try {
                session = HibernateUtil.getSessionFactory().getCurrentSession();
                session.beginTransaction();
                list = session.getNamedQuery("findAllTblReservationCheckInOut").list();
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
    public RefReservationCheckInOutStatus getReservationCheckInOutStatus(int id) {
        errMsg = "";
        RefReservationCheckInOutStatus data = null;
        if (ClassSession.checkUserSession()) {  //check user current session
            try {
                session = HibernateUtil.getSessionFactory().getCurrentSession();
                session.beginTransaction();
                data = (RefReservationCheckInOutStatus) session.find(RefReservationCheckInOutStatus.class, id);
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
    public TblRoom getRoom(long id) {
        errMsg = "";
        TblRoom data = null;
        if (ClassSession.checkUserSession()) {  //check user current session
            try {
                session = HibernateUtil.getSessionFactory().getCurrentSession();
                session.beginTransaction();
                data = (TblRoom) session.find(TblRoom.class, id);
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
    public List<TblRoom> getAllDataRoom() {
        errMsg = "";
        List<TblRoom> list = new ArrayList<>();
        if (ClassSession.checkUserSession()) {  //check user current session
            try {
                session = HibernateUtil.getSessionFactory().getCurrentSession();
                session.beginTransaction();
                list = session.getNamedQuery("findAllTblRoom").list();
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
    public RefRoomStatus getRoomStatus(int id) {
        errMsg = "";
        RefRoomStatus data = null;
        if (ClassSession.checkUserSession()) {  //check user current session
            try {
                session = HibernateUtil.getSessionFactory().getCurrentSession();
                session.beginTransaction();
                data = (RefRoomStatus) session.find(RefRoomStatus.class, id);
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
    public RefRoomCleanStatus getRoomCleanStatus(int id) {
        errMsg = "";
        RefRoomCleanStatus data = null;
        if (ClassSession.checkUserSession()) {  //check user current session
            try {
                session = HibernateUtil.getSessionFactory().getCurrentSession();
                session.beginTransaction();
                data = (RefRoomCleanStatus) session.find(RefRoomCleanStatus.class, id);
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
    public TblReservationAdditionalItem getReservationAdditionalItem(long id) {
        errMsg = "";
        TblReservationAdditionalItem data = null;
        if (ClassSession.checkUserSession()) {  //check user current session
            try {
                session = HibernateUtil.getSessionFactory().getCurrentSession();
                session.beginTransaction();
                data = (TblReservationAdditionalItem) session.find(TblReservationAdditionalItem.class, id);
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
    public List<TblReservationAdditionalItem> getAllDataReservationAdditionalItem() {
        errMsg = "";
        List<TblReservationAdditionalItem> list = new ArrayList<>();
        if (ClassSession.checkUserSession()) {  //check user current session
            try {
                session = HibernateUtil.getSessionFactory().getCurrentSession();
                session.beginTransaction();
                list = session.getNamedQuery("findAllTblReservationAdditionalItem").list();
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
    public List<TblReservationAdditionalItem> getAllDataReservationAdditionalItemByIDReservationRoomTypeDetail(long idReservationRoomTypeDetail) {
        errMsg = "";
        List<TblReservationAdditionalItem> list = new ArrayList<>();
        if (ClassSession.checkUserSession()) {  //check user current session
            try {
                session = HibernateUtil.getSessionFactory().getCurrentSession();
                session.beginTransaction();
                list = session.getNamedQuery("findAllTblReservationAdditionalItemByIDReservationRoomTypeDetail")
                        .setParameter("idReservationRoomTypeDetail", idReservationRoomTypeDetail)
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
    public RefItemType getDataItemType(int id) {
        errMsg = "";
        RefItemType data = null;
        if (ClassSession.checkUserSession()) {  //check user current session
            try {
                session = HibernateUtil.getSessionFactory().getCurrentSession();
                session.beginTransaction();
                data = (RefItemType) session.find(RefItemType.class, id);
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
    public List<RefItemType> getAllDataItemType() {
        errMsg = "";
        List<RefItemType> list = new ArrayList<>();
        if (ClassSession.checkUserSession()) {  //check user current session
            try {
                session = HibernateUtil.getSessionFactory().getCurrentSession();
                session.beginTransaction();
                list = session.getNamedQuery("findAllRefItemType").list();
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
    public RefItemGuestType getDataItemGuestType(int id) {
        errMsg = "";
        RefItemGuestType data = null;
        if (ClassSession.checkUserSession()) {  //check user current session
            try {
                session = HibernateUtil.getSessionFactory().getCurrentSession();
                session.beginTransaction();
                data = (RefItemGuestType) session.find(RefItemGuestType.class, id);
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
    public List<RefItemGuestType> getAllDataItemGuestType() {
        errMsg = "";
        List<RefItemGuestType> list = new ArrayList<>();
        if (ClassSession.checkUserSession()) {  //check user current session
            try {
                session = HibernateUtil.getSessionFactory().getCurrentSession();
                session.beginTransaction();
                list = session.getNamedQuery("findAllRefItemGuestType").list();
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
    public TblReservationAdditionalService getReservationAdditionalService(long id) {
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
    public List<TblReservationAdditionalService> getAllDataReservationAdditionalServiceByIDReservationRoomTypeDetail(long idReservationRoomTypeDetail) {
        errMsg = "";
        List<TblReservationAdditionalService> list = new ArrayList<>();
        if (ClassSession.checkUserSession()) {  //check user current session
            try {
                session = HibernateUtil.getSessionFactory().getCurrentSession();
                session.beginTransaction();
                list = session.getNamedQuery("findAllTblReservationAdditionalServiceByIDReservationRoomTypeDetail")
                        .setParameter("idReservationRoomTypeDetail", idReservationRoomTypeDetail)
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
    public TblRoomService getRoomService(long id) {
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

    //--------------------------------------------------------------------------
    @Override
    public TblReservationBrokenItem getReservationBrokenItem(long id) {
        errMsg = "";
        TblReservationBrokenItem data = null;
        if (ClassSession.checkUserSession()) {  //check user current session
            try {
                session = HibernateUtil.getSessionFactory().getCurrentSession();
                session.beginTransaction();
                data = (TblReservationBrokenItem) session.find(TblReservationBrokenItem.class, id);
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
    public List<TblReservationBrokenItem> getAllDataReservationBrokenItem() {
        errMsg = "";
        List<TblReservationBrokenItem> list = new ArrayList<>();
        if (ClassSession.checkUserSession()) {  //check user current session
            try {
                session = HibernateUtil.getSessionFactory().getCurrentSession();
                session.beginTransaction();
                list = session.getNamedQuery("findAllTblReservationBrokenItem").list();
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
    public List<TblReservationBrokenItem> getAllDataReservationBrokenItemByIDReservationRoomTypeDetail(long idReservationRoomTypeDetail) {
        errMsg = "";
        List<TblReservationBrokenItem> list = new ArrayList<>();
        if (ClassSession.checkUserSession()) {  //check user current session
            try {
                session = HibernateUtil.getSessionFactory().getCurrentSession();
                session.beginTransaction();
                list = session.getNamedQuery("findAllTblReservationBrokenItemByIDReservationRoomTypeDetail")
                        .setParameter("idReservationRoomTypeDetail", idReservationRoomTypeDetail)
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
    public TblItem getDataItem(long id) {
        errMsg = "";
        TblItem data = null;
        if (ClassSession.checkUserSession()) {  //check user current session
            try {
                session = HibernateUtil.getSessionFactory().getCurrentSession();
                session.beginTransaction();
                data = (TblItem) session.find(TblItem.class, id);
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
    public List<TblItem> getAllDataItem() {
        errMsg = "";
        List<TblItem> list = new ArrayList<>();
        if (ClassSession.checkUserSession()) {  //check user current session
            try {
                session = HibernateUtil.getSessionFactory().getCurrentSession();
                session.beginTransaction();
                list = session.getNamedQuery("findAllTblItem")
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
    public List<TblReservationRoomTypeDetailRoomPriceDetail> getAllDataReservationRoomTypeDetailRoomPriceDetailByIDReservationRoomTypeDetail(long idReservationRoomTypeDetail) {
        errMsg = "";
        List<TblReservationRoomTypeDetailRoomPriceDetail> list = new ArrayList<>();
        if (ClassSession.checkUserSession()) {  //check user current session
            try {
                session = HibernateUtil.getSessionFactory().getCurrentSession();
                session.beginTransaction();
                list = session.getNamedQuery("findAllTblReservationRoomTypeDetailRoomPriceDetailByIDReservationRoomTypeDetail")
                        .setParameter("idReservationRoomTypeDetail", idReservationRoomTypeDetail)
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
    public List<TblReservationRoomTypeDetailRoomPriceDetail> getAllDataReservationRoomTypeDetailRoomPriceDetailByIDRoomPriceDetail(long idRoomPriceDetail) {
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
        return list;
    }

    //--------------------------------------------------------------------------
    @Override
    public TblReservationRoomPriceDetail getReservationRoomPriceDetail(long id) {
        errMsg = "";
        TblReservationRoomPriceDetail data = null;
        if (ClassSession.checkUserSession()) {  //check user current session
            try {
                session = HibernateUtil.getSessionFactory().getCurrentSession();
                session.beginTransaction();
                data = (TblReservationRoomPriceDetail) session.find(TblReservationRoomPriceDetail.class, id);
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
    public List<TblReservationRoomPriceDetail> getAllDataReservationRoomPriceDetail() {
        errMsg = "";
        List<TblReservationRoomPriceDetail> list = new ArrayList<>();
        if (ClassSession.checkUserSession()) {  //check user current session
            try {
                session = HibernateUtil.getSessionFactory().getCurrentSession();
                session.beginTransaction();
                list = session.getNamedQuery("findAllTblReservationRoomPriceDetail").list();
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
    public TblReservationPayment getReservationPayment(long id) {
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
    public List<TblReservationPayment> getAllDataReservationPayment() {
        errMsg = "";
        List<TblReservationPayment> list = new ArrayList<>();
        if (ClassSession.checkUserSession()) {  //check user current session
            try {
                session = HibernateUtil.getSessionFactory().getCurrentSession();
                session.beginTransaction();
                list = session.getNamedQuery("findAllTblReservationPayment").list();
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
    public List<TblReservationPayment> getAllDataReservationPaymentByIDReservationBill(long idReservationBill) {
        errMsg = "";
        List<TblReservationPayment> list = new ArrayList<>();
        if (ClassSession.checkUserSession()) {  //check user current session
            try {
                session = HibernateUtil.getSessionFactory().getCurrentSession();
                session.beginTransaction();
                list = session.getNamedQuery("findAllTblReservationPaymentByIDReservationBillAndIDReservationBillType")
                        .setParameter("idReservationBill", idReservationBill)
                        .setParameter("idType", 0)
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
    public List<TblReservationPayment> getAllDataCheckOutPaymentByIDReservationBill(long idReservationBill) {
        errMsg = "";
        List<TblReservationPayment> list = new ArrayList<>();
        if (ClassSession.checkUserSession()) {  //check user current session
            try {
                session = HibernateUtil.getSessionFactory().getCurrentSession();
                session.beginTransaction();
                list = session.getNamedQuery("findAllTblReservationPaymentByIDReservationBillAndIDReservationBillType")
                        .setParameter("idReservationBill", idReservationBill)
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
    public RefFinanceTransactionPaymentType getFinanceTransactionPaymentType(int id) {
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
    public TblReservationPaymentWithTransfer getReservationPaymentWithTransferByIDPayment(long idPayment) {
        errMsg = "";
        List<TblReservationPaymentWithTransfer> list = new ArrayList<>();
        if (ClassSession.checkUserSession()) {  //check user current session
            try {
                session = HibernateUtil.getSessionFactory().getCurrentSession();
                session.beginTransaction();
                list = session.getNamedQuery("findAllTblReservationPaymentWithTransferByIDReservationPayment")
                        .setParameter("idReservationPayment", idPayment)
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
    public TblReservationPaymentWithBankCard getReservationPaymentWithBankCardByIDPayment(long idPayment) {
        errMsg = "";
        List<TblReservationPaymentWithBankCard> list = new ArrayList<>();
        if (ClassSession.checkUserSession()) {  //check user current session
            try {
                session = HibernateUtil.getSessionFactory().getCurrentSession();
                session.beginTransaction();
                list = session.getNamedQuery("findAllTblReservationPaymentWithBankCardByIDReservationPayment")
                        .setParameter("idReservationPayment", idPayment)
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
    public TblReservationPaymentWithCekGiro getReservationPaymentWithCekGiroByIDPayment(long idPayment) {
        errMsg = "";
        List<TblReservationPaymentWithCekGiro> list = new ArrayList<>();
        if (ClassSession.checkUserSession()) {  //check user current session
            try {
                session = HibernateUtil.getSessionFactory().getCurrentSession();
                session.beginTransaction();
                list = session.getNamedQuery("findAllTblReservationPaymentWithCekGiroByIDReservationPayment")
                        .setParameter("idReservationPayment", idPayment)
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
    public TblReservationPaymentWithGuaranteePayment getReservationPaymentWithGuaranteePaymentByIDPayment(long idPayment) {
        errMsg = "";
        List<TblReservationPaymentWithGuaranteePayment> list = new ArrayList<>();
        if (ClassSession.checkUserSession()) {  //check user current session
            try {
                session = HibernateUtil.getSessionFactory().getCurrentSession();
                session.beginTransaction();
                list = session.getNamedQuery("findAllTblReservationPaymentWithGuaranteePaymentByIDReservationPayment")
                        .setParameter("idReservationPayment", idPayment)
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
    public TblReservationPaymentWithReservationVoucher getReservationPaymentWithReservationVoucherByIDPayment(long idPayment) {
        errMsg = "";
        List<TblReservationPaymentWithReservationVoucher> list = new ArrayList<>();
        if (ClassSession.checkUserSession()) {  //check user current session
            try {
                session = HibernateUtil.getSessionFactory().getCurrentSession();
                session.beginTransaction();
                list = session.getNamedQuery("findAllTblReservationPaymentWithReservationVoucherByIDReservationPayment")
                        .setParameter("idReservationPayment", idPayment)
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
    public TblGuaranteeLetterItemDetail getDataGuaranteeLetterItemDetail(long id) {
        errMsg = "";
        TblGuaranteeLetterItemDetail data = null;
        if (ClassSession.checkUserSession()) {  //check user current session
            try {
                session = HibernateUtil.getSessionFactory().getCurrentSession();
                session.beginTransaction();
                data = (TblGuaranteeLetterItemDetail) session.find(TblGuaranteeLetterItemDetail.class, id);
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
    public List<TblGuaranteeLetterItemDetail> getAllDataGuaranteeLetterItemDetail() {
        errMsg = "";
        List<TblGuaranteeLetterItemDetail> list = new ArrayList<>();
        if (ClassSession.checkUserSession()) {  //check user current session
            try {
                session = HibernateUtil.getSessionFactory().getCurrentSession();
                session.beginTransaction();
                list = session.getNamedQuery("findAllTblGuaranteeLetterItemDetail").list();
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
    public RefReservationBillDiscountType getReservationBillDiscountType(int id) {
        errMsg = "";
        RefReservationBillDiscountType data = null;
        if (ClassSession.checkUserSession()) {  //check user current session
            try {
                session = HibernateUtil.getSessionFactory().getCurrentSession();
                session.beginTransaction();
                data = (RefReservationBillDiscountType) session.find(RefReservationBillDiscountType.class, id);
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
    public List<RefReservationBillDiscountType> getAllDataReservationBillDiscountType() {
        errMsg = "";
        List<RefReservationBillDiscountType> list = new ArrayList<>();
        if (ClassSession.checkUserSession()) {  //check user current session
            try {
                session = HibernateUtil.getSessionFactory().getCurrentSession();
                session.beginTransaction();
                list = session.getNamedQuery("findAllRefReservationBillDiscountType").list();
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
    public List<TblPartner> getAllDataPartnerTravelAgent() {
        errMsg = "";
        List<TblPartner> list = new ArrayList<>();
        if (ClassSession.checkUserSession()) {  //check user current session
            try {
                session = HibernateUtil.getSessionFactory().getCurrentSession();
                session.beginTransaction();
                list = session.getNamedQuery("findAllTblPartnerByIDPartnerType")
                        .setParameter("idType", 0)
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
    public List<TblPartner> getAllDataPartnerCorporate() {
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

    @Override
    public List<TblPartner> getAllDataPartnerGovernment() {
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

    @Override
    public TblPartnerBankAccount getDataPartnerBankAccount(long id) {
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
    public TblTravelAgent getTravelAgentByIDPartner(long idPartner) {
        errMsg = "";
        List<TblTravelAgent> list = new ArrayList<>();
        if (ClassSession.checkUserSession()) {  //check user current session
            try {
                session = HibernateUtil.getSessionFactory().getCurrentSession();
                session.beginTransaction();
                list = session.getNamedQuery("findAllTblTravelAgentByIDPartner")
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
    public TblReservationVoucher getReservationVoucher(long id) {
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
    public List<TblReservationVoucher> getAllDataReservationVoucher() {
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

    @Override
    public RefVoucherStatus getVoucherStatus(int id) {
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

    @Override
    public List<TblBankEdcBankNetworkCard> getAllDataBankEDCBankNetworkCardByIDEDC(long idEDC) {
        errMsg = "";
        List<TblBankEdcBankNetworkCard> list = new ArrayList<>();
        if (ClassSession.checkUserSession()) {  //check user current session
            try {
                session = HibernateUtil.getSessionFactory().getCurrentSession();
                session.beginTransaction();
                list = session.getNamedQuery("findAllTblBankEdcBankNetworkCardByIDBankEdc")
                        .setParameter("idBankEDC", idEDC)
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
    public TblBankEdcBankNetworkCard getDataBankEDCBankNetworkCardByIDEDCAndIDNetworkCard(long idEDC, long idNetworkCard) {
        errMsg = "";
        List<TblBankEdcBankNetworkCard> list = new ArrayList<>();
        if (ClassSession.checkUserSession()) {  //check user current session
            try {
                session = HibernateUtil.getSessionFactory().getCurrentSession();
                session.beginTransaction();
                list = session.getNamedQuery("findAllTblBankEdcBankNetworkCardByIDBankEdcAndIDBankNetworkCard")
                        .setParameter("idBankEDC", idEDC)
                        .setParameter("idBankNetworkCard", idNetworkCard)
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
    public TblBankEdcBankNetworkCard getDataBankEDCBankNetworkCardByIDEDCAndIDNetworkCardAndIDBankCardTypeAndOnUs(
            long idEDC,
            long idNetworkCard,
            int idBankCardType,
            boolean onUs) {
        errMsg = "";
        List<TblBankEdcBankNetworkCard> list = new ArrayList<>();
        if (ClassSession.checkUserSession()) {  //check user current session
            try {
                session = HibernateUtil.getSessionFactory().getCurrentSession();
                session.beginTransaction();
                list = session.getNamedQuery("findAllTblBankEdcBankNetworkCardByIDBankEdcAndIDBankNetworkCardAndIDBankCardTypeAndOnUs")
                        .setParameter("idBankEDC", idEDC)
                        .setParameter("idBankNetworkCard", idNetworkCard)
                        .setParameter("idBankCardType", idBankCardType)
                        .setParameter("onUs", onUs)
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
//    @Override
//    public RefReservationBillType getDataReservationBillTypeByTypeName(String typeName) {
//        //@@@---
//        errMsg = "";
//        List<RefReservationBillType> list = new ArrayList<>();
//        try {
//            session = HibernateUtil.getSessionFactory().getCurrentSession();
//            session.beginTransaction();
//            list = session.getNamedQuery("findAllRefReservationBillTypeByTypeName")
//                    .setParameter("typeName", typeName)
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
    public RefReservationBillType getDataReservationBillType(int id) {
        errMsg = "";
        RefReservationBillType data = null;
        if (ClassSession.checkUserSession()) {  //check user current session
            try {
                session = HibernateUtil.getSessionFactory().getCurrentSession();
                session.beginTransaction();
                data = (RefReservationBillType) session.find(RefReservationBillType.class, id);
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
    public List<RefReservationBillType> getAllDataReservationBillType() {
        errMsg = "";
        List<RefReservationBillType> list = new ArrayList<>();
        if (ClassSession.checkUserSession()) {  //check user current session
            try {
                session = HibernateUtil.getSessionFactory().getCurrentSession();
                session.beginTransaction();
                list = session.getNamedQuery("findAllRefReservationBillType").list();
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
    public List<TblHotelEvent> getAllDataHotelEventByDateInRangeOrderByMaxRoomDiscountPercentage(java.util.Date date) {
        errMsg = "";
        List<TblHotelEvent> list = new ArrayList<>();
        if (ClassSession.checkUserSession()) {  //check user current session
            try {
                session = HibernateUtil.getSessionFactory().getCurrentSession();
                session.beginTransaction();
                list = session.getNamedQuery("findAllTblHotelEventByDateInRangeOrderByMaxRoomDiscountPercentage")
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
    public List<TblBankEventCard> getAllDataBankEventCardByDateInRangeAndIDBankCardAllowNULLAndIDBankOrderByMaxRoomDiscountPercentage(
            java.util.Date date,
            long idBankCard,
            long idBank) {
        errMsg = "";
        List<TblBankEventCard> list = new ArrayList<>();
        if (ClassSession.checkUserSession()) {  //check user current session
            try {
                session = HibernateUtil.getSessionFactory().getCurrentSession();
                session.beginTransaction();
                list = session.getNamedQuery("findAllTblBankEventCardByDateInRangeAndIDBankCardAllowNULLAndIDBankOrderByMaxRoomDiscountPercentage")
                        .setParameter("detailDate", date)
                        .setParameter("idBankCard", idBankCard)
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

    @Override
    public List<TblHotelEvent> getAllDataHotelEventByDateInRangeOrderByMaxItemDiscountPercentage(java.util.Date date) {
        errMsg = "";
        List<TblHotelEvent> list = new ArrayList<>();
        if (ClassSession.checkUserSession()) {  //check user current session
            try {
                session = HibernateUtil.getSessionFactory().getCurrentSession();
                session.beginTransaction();
                list = session.getNamedQuery("findAllTblHotelEventByDateInRangeOrderByMaxItemDiscountPercentage")
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
    public List<TblBankEventCard> getAllDataBankEventCardByDateInRangeAndIDBankCardAllowNULLAndIDBankOrderByMaxItemDiscountPercentage(
            java.util.Date date,
            long idBankCard,
            long idBank) {
        errMsg = "";
        List<TblBankEventCard> list = new ArrayList<>();
        if (ClassSession.checkUserSession()) {  //check user current session
            try {
                session = HibernateUtil.getSessionFactory().getCurrentSession();
                session.beginTransaction();
                list = session.getNamedQuery("findAllTblBankEventCardByDateInRangeAndIDBankCardAllowNULLAndIDBankOrderByMaxItemDiscountPercentage")
                        .setParameter("detailDate", date)
                        .setParameter("idBankCard", idBankCard)
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

    @Override
    public List<TblHotelEvent> getAllDataHotelEventByDateInRangeOrderByMaxServiceDiscountPercentage(java.util.Date date) {
        errMsg = "";
        List<TblHotelEvent> list = new ArrayList<>();
        if (ClassSession.checkUserSession()) {  //check user current session
            try {
                session = HibernateUtil.getSessionFactory().getCurrentSession();
                session.beginTransaction();
                list = session.getNamedQuery("findAllTblHotelEventByDateInRangeOrderByMaxServiceDiscountPercentage")
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
    public List<TblBankEventCard> getAllDataBankEventCardByDateInRangeAndIDBankCardAllowNULLAndIDBankOrderByMaxServiceDiscountPercentage(
            java.util.Date date,
            long idBankCard,
            long idBank) {
        errMsg = "";
        List<TblBankEventCard> list = new ArrayList<>();
        if (ClassSession.checkUserSession()) {  //check user current session
            try {
                session = HibernateUtil.getSessionFactory().getCurrentSession();
                session.beginTransaction();
                list = session.getNamedQuery("findAllTblBankEventCardByDateInRangeAndIDBankCardAllowNULLAndIDBankOrderByMaxServiceDiscountPercentage")
                        .setParameter("detailDate", date)
                        .setParameter("idBankCard", idBankCard)
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
    public RefReservationDiscountType getReservationDiscountType(int id) {
        errMsg = "";
        RefReservationDiscountType data = null;
        if (ClassSession.checkUserSession()) {  //check user current session
            try {
                session = HibernateUtil.getSessionFactory().getCurrentSession();
                session.beginTransaction();
                data = (RefReservationDiscountType) session.find(RefReservationDiscountType.class, id);
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
    public TblFloor getDataFloor(long id) {
        errMsg = "";
        TblFloor data = null;
        if (ClassSession.checkUserSession()) {  //check user current session
            try {
                session = HibernateUtil.getSessionFactory().getCurrentSession();
                session.beginTransaction();
                data = (TblFloor) session.find(TblFloor.class, id);
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
    public List<TblFloor> getAllDataFloor() {
        errMsg = "";
        List<TblFloor> list = new ArrayList<>();
        if (ClassSession.checkUserSession()) {  //check user current session
            try {
                session = HibernateUtil.getSessionFactory().getCurrentSession();
                session.beginTransaction();
                list = session.getNamedQuery("findAllTblFloor").list();
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
    public List<TblRoomTypeItem> getAllDataRoomTypeItemByIDRoomType(long idRoomType) {
        errMsg = "";
        List<TblRoomTypeItem> list = new ArrayList<>();
        if (ClassSession.checkUserSession()) {  //check user current session
            try {
                session = HibernateUtil.getSessionFactory().getCurrentSession();
                session.beginTransaction();
                list = session.getNamedQuery("findAllTblRoomTypeItemByIDRoomType")
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
    public List<TblRoomTypeRoomService> getAllDataRoomTypeRoomServiceByIDRoomType(long idRoomType) {
        errMsg = "";
        List<TblRoomTypeRoomService> list = new ArrayList<>();
        if (ClassSession.checkUserSession()) {  //check user current session
            try {
                session = HibernateUtil.getSessionFactory().getCurrentSession();
                session.beginTransaction();
                list = session.getNamedQuery("findAllTblRoomTypeRoomServiceByIDRoomType")
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

    //--------------------------------------------------------------------------
    @Override
    public List<TblReservationRoomTypeDetailRoomPriceDetailChangeRoomHistory> getAllDataReservationRoomTypeDetailRoomPriceDetailChangeRoomHistoryByIDReservationRoomTypeDetailNew(long idReservationROomTypeDetailNew) {
        errMsg = "";
        List<TblReservationRoomTypeDetailRoomPriceDetailChangeRoomHistory> list = new ArrayList<>();
        if (ClassSession.checkUserSession()) {  //check user current session
            try {
                session = HibernateUtil.getSessionFactory().getCurrentSession();
                session.beginTransaction();
                list = session.getNamedQuery("findAllTblReservationRoomTypeDetailRoomPriceDetailChangeRoomHistoryByIDReservationRoomTypeDetailNew")
                        .setParameter("idReservationRoomTypeDetailNew", idReservationROomTypeDetailNew)
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
    public TblLocation getDataLocation(long id) {
        errMsg = "";
        TblLocation data = null;
        if (ClassSession.checkUserSession()) {  //check user current session
            try {
                session = HibernateUtil.getSessionFactory().getCurrentSession();
                session.beginTransaction();
                data = (TblLocation) session.find(TblLocation.class, id);
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
    public RefLocationType getDataLocationType(int id) {
        errMsg = "";
        RefLocationType data = null;
        if (ClassSession.checkUserSession()) {  //check user current session
            try {
                session = HibernateUtil.getSessionFactory().getCurrentSession();
                session.beginTransaction();
                data = (RefLocationType) session.find(RefLocationType.class, id);
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
    public TblRoom getDataRoomByIDLocation(long idLocation) {
        errMsg = "";
        List<TblRoom> list = new ArrayList<>();
        if (ClassSession.checkUserSession()) {  //check user current session
            try {
                session = HibernateUtil.getSessionFactory().getCurrentSession();
                session.beginTransaction();
                list = session.getNamedQuery("findAllTblRoomByIdLocation")
                        .setParameter("id", idLocation)
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
    public TblLocationOfWarehouse getDataWarehouseByIDLocation(long idLocation) {
        errMsg = "";
        List<TblLocationOfWarehouse> list = new ArrayList<>();
        if (ClassSession.checkUserSession()) {  //check user current session
            try {
                session = HibernateUtil.getSessionFactory().getCurrentSession();
                session.beginTransaction();
                list = session.getNamedQuery("findAllTblLocationOfWarehouseByIdLocation")
                        .setParameter("id", idLocation)
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
    public TblLocationOfLaundry getDataLaundryByIDLocation(long idLocation) {
        errMsg = "";
        List<TblLocationOfLaundry> list = new ArrayList<>();
        if (ClassSession.checkUserSession()) {  //check user current session
            try {
                session = HibernateUtil.getSessionFactory().getCurrentSession();
                session.beginTransaction();
                list = session.getNamedQuery("findAllTblLocationOfLaundryByIdLocation")
                        .setParameter("id", idLocation)
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
    public TblSupplier getDataSupplierByIDLocation(long idLocation) {
        errMsg = "";
        List<TblSupplier> list = new ArrayList<>();
        if (ClassSession.checkUserSession()) {  //check user current session
            try {
                session = HibernateUtil.getSessionFactory().getCurrentSession();
                session.beginTransaction();
                list = session.getNamedQuery("findAllTblSupplierByIdLocation")
                        .setParameter("id", idLocation)
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
    public TblLocationOfBin getDataBinByIDLocation(long idLocation) {
        errMsg = "";
        List<TblLocationOfBin> list = new ArrayList<>();
        if (ClassSession.checkUserSession()) {  //check user current session
            try {
                session = HibernateUtil.getSessionFactory().getCurrentSession();
                session.beginTransaction();
                list = session.getNamedQuery("findAllTblLocationOfBinByIdLocation")
                        .setParameter("id", idLocation)
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
    public TblReservationCanceled getDataReservationCanceledByIDReservation(long idReservation) {
        errMsg = "";
        List<TblReservationCanceled> list = new ArrayList<>();
        if (ClassSession.checkUserSession()) {  //check user current session
            try {
                session = HibernateUtil.getSessionFactory().getCurrentSession();
                session.beginTransaction();
                list = session.getNamedQuery("findAllTblReservationCanceledByIDReservation")
                        .setParameter("idReservation", idReservation)
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
    public TblReservationPayment getDataReservationPaymentByIDReservationAndIDReservationBillType(long idReservation,
            int idReservationBillType) {
        errMsg = "";
        List<TblReservationPayment> list = new ArrayList<>();
        if (ClassSession.checkUserSession()) {  //check user current session
            try {
                session = HibernateUtil.getSessionFactory().getCurrentSession();
                session.beginTransaction();
                list = session.getNamedQuery("findAllTblReservationPaymentByIDReservationAndIDReservationBillType")
                        .setParameter("idReservation", idReservation)
                        .setParameter("idReservationBillType", idReservationBillType)
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
    public LogSystem getDataLogSystem(long id) {
        errMsg = "";
        LogSystem data = null;
        if (ClassSession.checkUserSession()) {  //check user current session
            try {
                session = HibernateUtil.getSessionFactory().getCurrentSession();
                session.beginTransaction();
                data = (LogSystem) session.find(LogSystem.class, id);
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
    public List<LogSystem> getAllDataLogSystem() {
        errMsg = "";
        List<LogSystem> list = new ArrayList<>();
        if (ClassSession.checkUserSession()) {  //check user current session
            try {
                session = HibernateUtil.getSessionFactory().getCurrentSession();
                session.beginTransaction();
                list = session.getNamedQuery("findAllLogSystem").list();
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
    public TblSystemUser getDataUser(long id) {
        errMsg = "";
        TblSystemUser data = null;
        if (ClassSession.checkUserSession()) {  //check user current session
            try {
                session = HibernateUtil.getSessionFactory().getCurrentSession();
                session.beginTransaction();
                data = (TblSystemUser) session.find(TblSystemUser.class, id);
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
    public TblUnit getDataUnit(long id) {
        errMsg = "";
        TblUnit data = null;
        if (ClassSession.checkUserSession()) {  //check user current session
            try {
                session = HibernateUtil.getSessionFactory().getCurrentSession();
                session.beginTransaction();
                data = (TblUnit) session.find(TblUnit.class, id);
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
    public List<TblItemLocation> getAllDataItemLocationByIDItem(long idItem) {
        errMsg = "";
        List<TblItemLocation> list = new ArrayList();
        if (ClassSession.checkUserSession()) {  //check user current session
            try {
                session = HibernateUtil.getSessionFactory().getCurrentSession();
                session.beginTransaction();
                list = session.getNamedQuery("findAllTblItemLocationByIdItem")
                        .setParameter("idItem", idItem)
                        .list();

                session.getTransaction().commit();
            } catch (Exception e) {
                if (session.getTransaction().isActive()) {
                    session.getTransaction().rollback();
                }
                errMsg = e.getMessage();
            }
        }
        return list;
    }

    @Override
    public List<TblReservationAdditionalItemReserved> getAllDataReservationAdditionalItemReservedByIDItemAndIDRAIRStatus(
            long idItem,
            int idRAIRStatus) {
        errMsg = "";
        List<TblReservationAdditionalItemReserved> list = new ArrayList<>();
        if (ClassSession.checkUserSession()) {  //check user current session
            try {
                session = HibernateUtil.getSessionFactory().getCurrentSession();
                session.beginTransaction();
                list = session.getNamedQuery("findAllTblReservationAdditionalItemReservedByIDItemAndIDRAIRStatus")
                        .setParameter("idItem", idItem)
                        .setParameter("idRAIRStatus", idRAIRStatus)
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
    public List<TblReservationAdditionalItemReserved> getAllDataReservationAdditionalItemReservedByIDItemAndReservedDateAndIDRAIRStatus(
            long idItem,
            Date reservedDate,
            int idRAIRStatus) {
        errMsg = "";
        List<TblReservationAdditionalItemReserved> list = new ArrayList<>();
        if (ClassSession.checkUserSession()) {  //check user current session
            try {
                session = HibernateUtil.getSessionFactory().getCurrentSession();
                session.beginTransaction();
                list = session.getNamedQuery("findAllTblReservationAdditionalItemReservedByIDItemAndReservedDateAndIDRAIRStatus")
                        .setParameter("idItem", idItem)
                        .setParameter("reservedDate", reservedDate)
                        .setParameter("idRAIRStatus", idRAIRStatus)
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
    public List<TblRoomTypeItem> getAllDataRoomTypeItemByIDRoomTypeAndIDItem(long idRoomType,
            long idItem) {
        errMsg = "";
        List<TblRoomTypeItem> list = new ArrayList<>();
        if (ClassSession.checkUserSession()) {  //check user current session
            try {
                session = HibernateUtil.getSessionFactory().getCurrentSession();
                session.beginTransaction();
                list = session.getNamedQuery("findAllTblRoomTypeItemByIDRoomTypeAndIDItem")
                        .setParameter("idRoomType", idRoomType)
                        .setParameter("idItem", idItem)
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
    public List<TblReservationAdditionalItemReserved> getAllDataReservationAdditionalItemReservedByIDItemAndReservedDateInRangedAndIDRAIRStatus(
            long idItem,
            Date startReservedDate,
            Date endReservedDate,
            int idRAIRStatus) {
        errMsg = "";
        List<TblReservationAdditionalItemReserved> list = new ArrayList<>();
        if (ClassSession.checkUserSession()) {  //check user current session
            try {
                session = HibernateUtil.getSessionFactory().getCurrentSession();
                session.beginTransaction();
                list = session.getNamedQuery("findAllTblReservationAdditionalItemReservedByIDItemAndReservedDateInRangedAndIDRAIRStatus")
                        .setParameter("idItem", idItem)
                        .setParameter("startReservedDate", startReservedDate)
                        .setParameter("endReservedDate", endReservedDate)
                        .setParameter("idRAIRStatus", idRAIRStatus)
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
    public List<TblReservationAdditionalItemReserved> getAllDataReservationAdditionalItemReservedByIDItemAndReservedDateInAfterAndIDRAIRStatus(
            long idItem,
            Date reservedDate,
            int idRAIRStatus) {
        errMsg = "";
        List<TblReservationAdditionalItemReserved> list = new ArrayList<>();
        if (ClassSession.checkUserSession()) {  //check user current session
            try {
                session = HibernateUtil.getSessionFactory().getCurrentSession();
                session.beginTransaction();
                list = session.getNamedQuery("findAllTblReservationAdditionalItemReservedByIDItemAndReservedDateInAfterAndIDRAIRStatus")
                        .setParameter("idItem", idItem)
                        .setParameter("reservedDate", reservedDate)
                        .setParameter("idRAIRStatus", idRAIRStatus)
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

    //--------------------------------------------------------------------------
    @Override
    public TblReservationRescheduleCanceled getDataReservationRescheduleCanceled(long id) {
        errMsg = "";
        TblReservationRescheduleCanceled data = null;
        if (ClassSession.checkUserSession()) {  //check user current session
            try {
                session = HibernateUtil.getSessionFactory().getCurrentSession();
                session.beginTransaction();
                data = (TblReservationRescheduleCanceled) session.find(TblReservationRescheduleCanceled.class, id);
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
    public List<TblReservationRescheduleCanceled> getAllDataReservationRescheduleCanceledByIDReservation(long idReservation) {
        errMsg = "";
        List<TblReservationRescheduleCanceled> list = new ArrayList<>();
        if (ClassSession.checkUserSession()) {  //check user current session
            try {
                session = HibernateUtil.getSessionFactory().getCurrentSession();
                session.beginTransaction();
                list = session.getNamedQuery("findAllTblReservationRescheduleCanceledByIDReservation")
                        .setParameter("idReservation", idReservation)
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
    public TblReservationCancelingFee getDataReservationCancelingFee(long id) {
        errMsg = "";
        TblReservationCancelingFee data = null;
        if (ClassSession.checkUserSession()) {  //check user current session
            try {
                session = HibernateUtil.getSessionFactory().getCurrentSession();
                session.beginTransaction();
                data = (TblReservationCancelingFee) session.find(TblReservationCancelingFee.class, id);
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
    public List<TblReservationCancelingFee> getAllDataReservationCancelingFeeByIDReservation(long idReservation) {
        errMsg = "";
        List<TblReservationCancelingFee> list = new ArrayList<>();
        if (ClassSession.checkUserSession()) {  //check user current session
            try {
                session = HibernateUtil.getSessionFactory().getCurrentSession();
                session.beginTransaction();
                list = session.getNamedQuery("findAllTblReservationCancelingFeeByIDReservation")
                        .setParameter("idReservation", idReservation)
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
    public TblItemTypeHk getDataItemTypeHK(long id) {
        errMsg = "";
        TblItemTypeHk data = null;
        if (ClassSession.checkUserSession()) {  //check user current session
            try {
                session = HibernateUtil.getSessionFactory().getCurrentSession();
                session.beginTransaction();
                data = (TblItemTypeHk) session.find(TblItemTypeHk.class, id);
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
    public List<TblItemTypeHk> getAllDataItemTypeHK() {
        errMsg = "";
        List<TblItemTypeHk> list = new ArrayList<>();
        if (ClassSession.checkUserSession()) {  //check user current session
            try {
                session = HibernateUtil.getSessionFactory().getCurrentSession();
                session.beginTransaction();
                list = session.getNamedQuery("findAllTblItemTypeHk")
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
    public TblItemTypeWh getDataItemTypeWH(long id) {
        errMsg = "";
        TblItemTypeWh data = null;
        if (ClassSession.checkUserSession()) {  //check user current session
            try {
                session = HibernateUtil.getSessionFactory().getCurrentSession();
                session.beginTransaction();
                data = (TblItemTypeWh) session.find(TblItemTypeWh.class, id);
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
    public List<TblItemTypeWh> getAllDataItemTypeWH() {
        errMsg = "";
        List<TblItemTypeWh> list = new ArrayList<>();
        if (ClassSession.checkUserSession()) {  //check user current session
            try {
                session = HibernateUtil.getSessionFactory().getCurrentSession();
                session.beginTransaction();
                list = session.getNamedQuery("findAllTblItemTypeWh")
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
    public RefEndOfDayDataStatus getDataEndOfDayDataStatus(int id) {
        errMsg = "";
        RefEndOfDayDataStatus data = null;
        if (ClassSession.checkUserSession()) {  //check user current session
            try {
                session = HibernateUtil.getSessionFactory().getCurrentSession();
                session.beginTransaction();
                data = (RefEndOfDayDataStatus) session.find(RefEndOfDayDataStatus.class, id);
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
    public String getErrorMessage() {
        return errMsg;
    }

}
