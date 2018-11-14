/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hotelfx.persistence.service;

import hotelfx.persistence.model.LogSystem;
import hotelfx.persistence.model.RefBankAccountHolderStatus;
import hotelfx.persistence.model.RefBankCardType;
import hotelfx.persistence.model.RefCountry;
import hotelfx.persistence.model.RefCustomerType;
import hotelfx.persistence.model.RefFinanceTransactionPaymentType;
import hotelfx.persistence.model.RefItemGuestType;
import hotelfx.persistence.model.RefItemType;
import hotelfx.persistence.model.RefLocationType;
import hotelfx.persistence.model.RefPeopleGender;
import hotelfx.persistence.model.RefPeopleIdentifierType;
import hotelfx.persistence.model.RefPeopleReligion;
import hotelfx.persistence.model.RefPeopleStatus;
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
import hotelfx.persistence.model.TblItem;
import hotelfx.persistence.model.TblItemLocation;
import hotelfx.persistence.model.TblItemTypeHk;
import hotelfx.persistence.model.TblItemTypeWh;
import hotelfx.persistence.model.TblLocation;
import hotelfx.persistence.model.TblLocationOfBin;
import hotelfx.persistence.model.TblLocationOfLaundry;
import hotelfx.persistence.model.TblLocationOfWarehouse;
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
import hotelfx.persistence.model.TblReservationCheckInOut;
import hotelfx.persistence.model.TblReservationDefaultBar;
import hotelfx.persistence.model.TblReservationPayment;
import hotelfx.persistence.model.TblReservationPaymentWithBankCard;
import hotelfx.persistence.model.TblReservationPaymentWithCekGiro;
import hotelfx.persistence.model.TblReservationPaymentWithGuaranteePayment;
import hotelfx.persistence.model.TblReservationPaymentWithReservationVoucher;
import hotelfx.persistence.model.TblReservationPaymentWithTransfer;
import hotelfx.persistence.model.TblReservationRoomPriceDetail;
import hotelfx.persistence.model.TblReservationRoomTypeDetail;
import hotelfx.persistence.model.TblReservationRoomTypeDetailChildDetail;
import hotelfx.persistence.model.TblReservationRoomTypeDetailRoomPriceDetail;
import hotelfx.persistence.model.TblReservationRoomTypeDetailRoomPriceDetailChangeRoomHistory;
import hotelfx.persistence.model.TblReservationRoomTypeDetailTravelAgentDiscountDetail;
import hotelfx.persistence.model.TblReservationTravelAgentDiscountDetail;
import hotelfx.persistence.model.TblReservationVoucher;
import hotelfx.persistence.model.TblRoom;
import hotelfx.persistence.model.TblRoomService;
import hotelfx.persistence.model.TblRoomType;
import hotelfx.persistence.model.TblRoomTypeItem;
import hotelfx.persistence.model.TblRoomTypeRoomService;
import hotelfx.persistence.model.TblSupplier;
import hotelfx.persistence.model.TblSystemUser;
import hotelfx.persistence.model.TblTravelAgent;
import hotelfx.persistence.model.TblTravelAgentRoomType;
import hotelfx.persistence.model.TblUnit;
import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Time;
import java.time.LocalDateTime;
import java.util.List;

/**
 *
 * @author ANDRI
 */
public interface FReservationManager {

    public TblReservation insertDataReservation(
            TblReservation reservation,
            TblReservationBill reservationBill,
            TblReservationBill checkOutBill,
            List<TblReservationRoomTypeDetail> rrtds,
            List<TblReservationRoomTypeDetailRoomPriceDetail> rrtdrpds,
            List<TblReservationRoomTypeDetailTravelAgentDiscountDetail> rrtdtadds,
            List<TblReservationAdditionalItem> rais,
            List<TblReservationAdditionalService> rass
    );

    //--------------------------------------------------------------------------
    public boolean updateDataReservation(TblReservation reservation);

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
            List<TblGuaranteeLetterItemDetail> guaranteeLetterItemDetails
    );

    public boolean insertDataReservationRoomTypeDetails(
            TblReservation reservation,
            TblReservationBill reservationBill,
            List<TblReservationRoomTypeDetail> reservationRoomTypeDetails,
            List<TblReservationRoomTypeDetailRoomPriceDetail> reservationRoomTypeDetailRoomPriceDetails,
            List<TblReservationRoomTypeDetailTravelAgentDiscountDetail> reservationRoomTypeDetailTravelAgentDiscountDetails,
            List<TblReservationAdditionalItem> rais,
            List<TblReservationAdditionalService> rass
    );

    public boolean deleteDataReservationRoomTypeDetail(
            TblReservation reservation,
            TblReservationBill reservationBill,
            TblReservationRoomTypeDetail reservationRoomTypeDetail
    );

    public boolean updateDataReservationRoomTypeDetailCheckIn(
            TblReservation reservation,
            TblReservationRoomTypeDetail reservationRoomTypeDetail,
            List<TblReservationRoomTypeDetailChildDetail> reservationChildInfos
    );

    public boolean updateDataReservationRoomTypeDetailCheckOut(
            TblReservation reservation,
            TblReservationRoomTypeDetail reservationRoomTypeDetail,
            TblReservationBill checkOutBill,
            BigDecimal checkOutBillNominal
    );

    public boolean updateDataReservationRoomTypeDetailMultiCheckOut(
            TblReservation reservation,
            List<TblReservationRoomTypeDetail> reservationRoomTypeDetails,
            TblReservationBill checkOutBill,
            BigDecimal checkOutBillNominal
    );

    public boolean updateDataReservationRoomTypeDetailExtendRoom(
            TblReservation reservation,
            TblReservationBill reservationBill,
            TblReservationRoomTypeDetail reservationRoomTypeDetail,
            List<TblReservationRoomTypeDetailRoomPriceDetail> reservationRoomTypeDetailRoomPriceDetails,
            List<TblReservationAdditionalItem> rais,
            List<TblReservationAdditionalService> rass
    );
    
    public boolean insertDataReservationAddtionalItems(
            TblReservation reservation,
            List<TblReservationAdditionalItem> reservationAdditionalItems
    );

    public boolean deleteDataReservationAddtionalItem(
            TblReservation reservation,
            TblReservationAdditionalItem reservationAdditionalItem
    );

    public boolean insertDataReservationAddtionalServices(
            TblReservation reservation,
            List<TblReservationAdditionalService> reservationAdditionalServices
    );

    public boolean deleteDataReservationAddtionalService(
            TblReservation reservation,
            TblReservationAdditionalService reservationAdditionalService
    );

    public boolean updateDataReservationRoomTypeDetailEarlyCheckIn(
            TblReservation reservation,
            TblReservationRoomTypeDetail reservationRoomTypeDetail,
            List<TblReservationRoomTypeDetailChildDetail> reservationChildInfos,
            TblReservationAdditionalService reservationAdditionalService
    );

    public boolean updateDataReservationRoomTypeDetailLateCheckOut(
            TblReservation reservation,
            TblReservationRoomTypeDetail reservationRoomTypeDetail,
            TblReservationAdditionalService reservationAdditionalService,
            TblReservationBill checkOutBill,
            BigDecimal checkOutBillNominal
    );

    public boolean updateDataReservationRoomTypeDetailCheckInAddCardUsedNumber(
            TblReservation reservation,
            TblReservationRoomTypeDetail reservationRoomTypeDetail,
            int additionalCardUsedNumber
    );

    public boolean updateDataReservationBillDiscountType(
            TblReservation reservation,
            TblReservationBill reservationBill,
            TblReservationBill checkOutBill,
            List<TblReservationRoomTypeDetail> rrtds,
            List<TblReservationRoomTypeDetailRoomPriceDetail> rrtdrpds,
            List<TblReservationRoomTypeDetailTravelAgentDiscountDetail> rrtdtadds,
            List<TblReservationAdditionalItem> rais,
            List<TblReservationAdditionalService> rass
    );

    public boolean updateDataReservationRoomTypeDetailChangeRoomType(
            TblReservation reservation,
            TblReservationRoomTypeDetail reservationRoomTypeDetail
    );

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
    );

    public boolean updateDataReservationRoomTypeDetailCheckInOutChangeRoom(
            TblReservation reservation,
            TblReservationRoomTypeDetail reservationRoomTypeDetail
    );

    //--------------------------------------------------------------------------
    public boolean deleteDataReservation(TblReservation reservation);

    public TblReservationCanceled insertDataReservationCanceled(TblReservationCanceled reservationCanceled,
            TblReservationPayment reservationPayment);

    //--------------------------------------------------------------------------
    public TblReservation getReservation(long id);

    public List<TblReservation> getAllDataReservation();

    //--------------------------------------------------------------------------
    public RefReservationStatus getReservationStatus(int id);

//    public RefReservationStatus getReservationStatusByStatusName(String name);
    public List<RefReservationStatus> getAllDataReservationStatus();

    //--------------------------------------------------------------------------
    public TblCustomer insertDataCustomer(TblCustomer customer, String imgExtention,
            List<TblCustomerBankAccount> customerBankAccount);

    public boolean updateDataCustomer(TblCustomer customer, String imgExtention,
            List<TblCustomerBankAccount> customerBankAccount);

    public TblCustomer getCustomer(long id);

    public List<TblCustomer> getAllDataCustomer();

    public RefCustomerType getCustomerType(int id);

    public List<RefCustomerType> getAllDataCustomerType();

    public TblCustomerBankAccount insertDataCustomerBankAccount(TblCustomerBankAccount customerBankAccount);

    public TblCustomerBankAccount getDataCustomerBankAccount(long id);

    public List<TblCustomerBankAccount> getAllDataCustomerBankAccountByIDCustomer(long idCustomer);

    public TblBankAccount getBankAccount(long idBankAccount);

    public List<RefBankAccountHolderStatus> getAllDataBankAccountHolderStatus();

    public TblPeople getPeople(long id);

    public List<TblPeople> getAllDataPeople();

    public List<RefPeopleIdentifierType> getAllDataPeopleIdentifierType();

    public List<RefPeopleGender> getAllDataPeopleGender();

    public List<RefPeopleReligion> getAllDataPeopleReligion();

    public List<RefPeopleStatus> getAllDataPeopleStatus();

    public List<RefCountry> getAllDataPeopleCountry();

    public boolean updateDataCustomerDeposit(TblCustomer customer, TblReservationPayment depositTransaction);

    //--------------------------------------------------------------------------
    public TblReservationBill getReservationBill(long id);

    public TblReservationBill getReservationBillByIDReservation(long idReservation);

    public TblReservationBill getCheckOutBillByIDReservation(long idReservation);

    public List<TblReservationBill> getAllDataReservationBill();

    //--------------------------------------------------------------------------
    public TblReservationRoomTypeDetail getReservationRoomTypeDetail(long id);

    public List<TblReservationRoomTypeDetail> getAllDataReservationRoomTypeDetail();

    public List<TblReservationRoomTypeDetail> getAllDataReservationRoomTypeDetailByIDReservation(long idReservation);

    //--------------------------------------------------------------------------
    public RefReservationOrderByType getReservationOrderByType(int id);

    public List<RefReservationOrderByType> getAllDataReservationOrderByType();

    //--------------------------------------------------------------------------
    public TblRoomType getRoomType(long id);

    public List<TblRoomType> getAllDataRoomType();

    //--------------------------------------------------------------------------
    public List<TblRoom> getAllDataRoomByIDRoomType(long idRoomType);

    public TblTravelAgentRoomType getDataTravelAgentRoomTypeByIDRoomTypeAndIDPartnerAndAvailableDate(long idRoomType,
            long idPartner,
            Date availableDate);

    public List<TblTravelAgentRoomType> getAllDataTravelAgentRoomTypeByIDRoomTypeAndAvailableDate(long idRoomType, Date availableDate);

    //--------------------------------------------------------------------------
    public List<TblReservationRoomPriceDetail> getAllDataReservationRoomPriceDetailByDetailDate(Date date);

    public TblReservationRoomTypeDetailRoomPriceDetail getReservationRoomTypeDetailRoomPriceDetail(long id);

    public TblReservationRoomTypeDetailRoomPriceDetail getReservationRoomTypeDetailRoomPriceDetailByIDRoomPriceDetail(long idRoomPriceDetail);

    //--------------------------------------------------------------------------
    public TblReservationTravelAgentDiscountDetail getReservationTravelAgentDiscountDetail(long id);

    public TblReservationRoomTypeDetailTravelAgentDiscountDetail getReservationRoomTypeDetailTravelAgentDiscountDetailByIDReservationRoomTypeDetail(long idRoomTypeDetail);

    public List<TblReservationRoomTypeDetailTravelAgentDiscountDetail> getAllDataReservationRoomTypeDetailTravelAgentDiscountDetailByIDReservationRoomTypeDetail(long idRoomTypeDetail);

    //--------------------------------------------------------------------------
    public TblReservationCalendarBar getReservationCalendarBARByCalendarDate(Date calendarDate);

    public TblReservationDefaultBar getReservationDefaultBARByDayOfWeek(int dayOfWeek);

    public TblReservationBar getReservationBAR(long id);

    //--------------------------------------------------------------------------
    public TblReservationCheckInOut getReservationCheckInOut(long id);

    public List<TblReservationCheckInOut> getAllDataReservationCheckInOut();

    //--------------------------------------------------------------------------
    public RefReservationCheckInOutStatus getReservationCheckInOutStatus(int id);

    //--------------------------------------------------------------------------
    public TblRoom getRoom(long id);

    public List<TblRoom> getAllDataRoom();

    //--------------------------------------------------------------------------
    public RefRoomStatus getRoomStatus(int id);

    public RefRoomCleanStatus getRoomCleanStatus(int id);

    //--------------------------------------------------------------------------
    public TblReservationAdditionalItem getReservationAdditionalItem(long id);

    public List<TblReservationAdditionalItem> getAllDataReservationAdditionalItem();

    public List<TblReservationAdditionalItem> getAllDataReservationAdditionalItemByIDReservationRoomTypeDetail(long idReservationRoomTypeDetail);

    //--------------------------------------------------------------------------

    public RefItemType getDataItemType(int id);

    public List<RefItemType> getAllDataItemType();

    public RefItemGuestType getDataItemGuestType(int id);

    public List<RefItemGuestType> getAllDataItemGuestType();

    //--------------------------------------------------------------------------
    public TblReservationAdditionalService getReservationAdditionalService(long id);

    public List<TblReservationAdditionalService> getAllDataReservationAdditionalService();

    public List<TblReservationAdditionalService> getAllDataReservationAdditionalServiceByIDReservationRoomTypeDetail(long idReservationRoomTypeDetail);

    //--------------------------------------------------------------------------
    public TblRoomService getRoomService(long id);

    public List<TblRoomService> getAllDataRoomService();

    //--------------------------------------------------------------------------
    public TblReservationBrokenItem getReservationBrokenItem(long id);

    public List<TblReservationBrokenItem> getAllDataReservationBrokenItem();

    public List<TblReservationBrokenItem> getAllDataReservationBrokenItemByIDReservationRoomTypeDetail(long idReservationRoomTypeDetail);

    public TblItem getDataItem(long id);
    
    public List<TblItem> getAllDataItem();

    //--------------------------------------------------------------------------
    public List<TblReservationRoomTypeDetailRoomPriceDetail> getAllDataReservationRoomTypeDetailRoomPriceDetailByIDReservationRoomTypeDetail(long idReservationRoomTypeDetail);

    //--------------------------------------------------------------------------
    public TblReservationRoomPriceDetail getReservationRoomPriceDetail(long id);

    public List<TblReservationRoomPriceDetail> getAllDataReservationRoomPriceDetail();

    //--------------------------------------------------------------------------
    public TblReservationPayment getReservationPayment(long id);

    public List<TblReservationPayment> getAllDataReservationPayment();

    public List<TblReservationPayment> getAllDataReservationPaymentByIDReservationBill(long idReservationBill);

    public List<TblReservationPayment> getAllDataCheckOutPaymentByIDReservationBill(long idReservationBill);

    //--------------------------------------------------------------------------
    public RefFinanceTransactionPaymentType getFinanceTransactionPaymentType(int id);

    public List<RefFinanceTransactionPaymentType> getAllDataFinanceTransactionPaymentType();

    //--------------------------------------------------------------------------
    public TblReservationPaymentWithTransfer getReservationPaymentWithTransferByIDPayment(long idPayment);

    public TblReservationPaymentWithBankCard getReservationPaymentWithBankCardByIDPayment(long idPayment);

    public TblReservationPaymentWithCekGiro getReservationPaymentWithCekGiroByIDPayment(long idPayment);

    public TblReservationPaymentWithGuaranteePayment getReservationPaymentWithGuaranteePaymentByIDPayment(long idPayment);

    public TblReservationPaymentWithReservationVoucher getReservationPaymentWithReservationVoucherByIDPayment(long idPayment);

    //--------------------------------------------------------------------------
    public TblGuaranteeLetterItemDetail getDataGuaranteeLetterItemDetail(long id);

    public List<TblGuaranteeLetterItemDetail> getAllDataGuaranteeLetterItemDetail();

    public List<TblGuaranteeLetterItemDetail> getAllDataGuaranteeLetterItemDetailByIDGuaranteeLetter(long idGuaranteeLetter);

    //--------------------------------------------------------------------------
    public TblCompanyBalance getDataCompanyBalance(long id);

    public List<TblCompanyBalanceBankAccount> getAllDataCompanyBalanceBankAccount(long idCompanyBalance);

    //--------------------------------------------------------------------------
    public RefReservationBillDiscountType getReservationBillDiscountType(int id);

    public List<RefReservationBillDiscountType> getAllDataReservationBillDiscountType();

    //--------------------------------------------------------------------------
    public TblHotelEvent getHotelEvent(long id);

    public List<TblHotelEvent> getAllDataHotelEvent();

    //--------------------------------------------------------------------------
    public TblBankEventCard getBankEventCard(long id);

    public List<TblBankEventCard> getAllDataBankEventCard();

    //--------------------------------------------------------------------------
    public TblPartner getPartner(long id);

    public List<TblPartner> getAllDataPartnerTravelAgent();

    public List<TblPartner> getAllDataPartnerCorporate();

    public List<TblPartner> getAllDataPartnerGovernment();

    public TblPartnerBankAccount insertDataPartnerBankAccount(TblPartnerBankAccount partnerBankAccount);

    public TblPartnerBankAccount getDataPartnerBankAccount(long id);

    public List<TblPartnerBankAccount> getAllDataPartnerBankAccountByIDPartner(long idPartner);

    //--------------------------------------------------------------------------
    public TblTravelAgent getTravelAgent(long id);

    public TblTravelAgent getTravelAgentByIDPartner(long idPartner);

    public List<TblTravelAgent> getAllDataTravelAgent();

    //--------------------------------------------------------------------------
    public TblReservationVoucher getReservationVoucher(long id);

    public List<TblReservationVoucher> getAllDataReservationVoucher();

    public RefVoucherStatus getVoucherStatus(int id);

    //--------------------------------------------------------------------------
    public TblBankEdc getBankEDC(long id);

    public List<TblBankEdc> getAllDataBankEDC();

    public TblBankNetworkCard getBankNetworkCard(long id);

    public List<TblBankNetworkCard> getAllDataBankNetworkCard();

    public List<TblBankEdcBankNetworkCard> getAllDataBankEDCBankNetworkCardByIDEDC(long idEDC);

    public TblBankEdcBankNetworkCard getDataBankEDCBankNetworkCardByIDEDCAndIDNetworkCard(long idEDC, long idNetworkCard);

    public TblBank getBank(long id);

    public List<TblBank> getAllDataBank();

    //--------------------------------------------------------------------------
//    public RefReservationBillType getDataReservationBillTypeByTypeName(String typeName);
    public RefReservationBillType getDataReservationBillType(int id);

    public List<RefReservationBillType> getAllDataReservationBillType();

    //--------------------------------------------------------------------------
    public List<TblHotelEvent> getAllDataHotelEventByDateInRangeOrderByMaxRoomDiscountPercentage(java.util.Date date);

    public List<TblBankEventCard> getAllDataBankEventCardByDateInRangeAndIDBankCardAllowNULLAndIDBankOrderByMaxRoomDiscountPercentage(java.util.Date date, long idBankCard, long idBank);

    public List<TblHotelEvent> getAllDataHotelEventByDateInRangeOrderByMaxItemDiscountPercentage(java.util.Date date);

    public List<TblBankEventCard> getAllDataBankEventCardByDateInRangeAndIDBankCardAllowNULLAndIDBankOrderByMaxItemDiscountPercentage(java.util.Date date, long idBankCard, long idBank);

    public List<TblHotelEvent> getAllDataHotelEventByDateInRangeOrderByMaxServiceDiscountPercentage(java.util.Date date);

    public List<TblBankEventCard> getAllDataBankEventCardByDateInRangeAndIDBankCardAllowNULLAndIDBankOrderByMaxServiceDiscountPercentage(java.util.Date date, long idBankCard, long idBank);

    //--------------------------------------------------------------------------
    public RefReservationDiscountType getReservationDiscountType(int id);

    //--------------------------------------------------------------------------
    public TblBankCard getDataBankCard(long id);

    public List<TblBankCard> getAllDataBankCard();

    public RefBankCardType getDataBankCardType(int id);

    public List<RefBankCardType> getAllDataBankCardType();

    //--------------------------------------------------------------------------
    public TblFloor getDataFloor(long id);

    public List<TblFloor> getAllDataFloor();

    //--------------------------------------------------------------------------
    public List<TblRoomTypeItem> getAllDataRoomTypeItemByIDRoomType(long idRoomType);

    public List<TblRoomTypeRoomService> getAllDataRoomTypeRoomServiceByIDRoomType(long idRoomType);

    //--------------------------------------------------------------------------
    public List<TblReservationRoomTypeDetailRoomPriceDetailChangeRoomHistory> getAllDataReservationRoomTypeDetailRoomPriceDetailChangeRoomHistoryByIDReservationRoomTypeDetailNew(long idReservationROomTypeDetailNew);

    //--------------------------------------------------------------------------
    public TblLocation getDataLocation(long id);

    public RefLocationType getDataLocationType(int id);

    public TblRoom getDataRoomByIDLocation(long idLocation);

    public TblLocationOfWarehouse getDataWarehouseByIDLocation(long idLcoation);

    public TblLocationOfLaundry getDataLaundryByIDLocation(long idLocation);

    public TblSupplier getDataSupplierByIDLocation(long idLocation);

    public TblLocationOfBin getDataBinByIDLocation(long idLocation);

    //--------------------------------------------------------------------------
    public SysDataHardCode getDataSysDataHardCode(long id);

    public List<SysDataHardCode> getAllDataSysDataHardCode();

    //--------------------------------------------------------------------------
    public TblReservationCanceled getDataReservationCanceledByIDReservation(long idReservation);

    public TblReservationPayment getDataReservationPaymentByIDReservationAndIDReservationBillType(long idReservation,
            int idReservationBillType);

    //--------------------------------------------------------------------------
    public LogSystem getDataLogSystem(long id);

    public List<LogSystem> getAllDataLogSystem();

    //--------------------------------------------------------------------------
    public TblSystemUser getDataUser(long id);

    public TblEmployee getDataEmployee(long id);

    //--------------------------------------------------------------------------    
    public TblUnit getDataUnit(long id);

    //--------------------------------------------------------------------------    
    public List<TblItemLocation> getAllDataItemLocationByIDItem(long idItem);

    public List<TblReservationAdditionalItemReserved> getAllDataReservationAdditionalItemReservedByIDItemAndIDRAIRStatus(
            long idItem,
            int idRAIRStatus);
    
    public List<TblReservationAdditionalItemReserved> getAllDataReservationAdditionalItemReservedByIDItemAndReservedDateAndIDRAIRStatus(
            long idItem,
            Date reservedDate,
            int idRAIRStatus);

    public List<TblRoomTypeItem> getAllDataRoomTypeItemByIDRoomTypeAndIDItem(long idRoomType, long idItem);
    
    public List<TblReservationAdditionalItemReserved> getAllDataReservationAdditionalItemReservedByIDItemAndReservedDateInRangedAndIDRAIRStatus(
            long idItem,
            Date startReservedDate,
            Date endReservedDate,
            int idRAIRStatus);
    
    public List<TblReservationAdditionalItemReserved> getAllDataReservationAdditionalItemReservedByIDItemAndReservedDateInAfterAndIDRAIRStatus(
            long idItem,
            Date reservedDate,
            int idRAIRStatus);
    
    //--------------------------------------------------------------------------
    
    public List<TblHotelFinanceTransactionHotelReceivable> getAllDataHotelFinanceTransactionHotelReceivableByIDHotelReceivable(long idHotelReceivable);
    
    //--------------------------------------------------------------------------
        
    public TblItemTypeHk getDataItemTypeHK(long id);
    
    public List<TblItemTypeHk> getAllDataItemTypeHK();
    
    public TblItemTypeWh getDataItemTypeWH(long id);
    
    public List<TblItemTypeWh> getAllDataItemTypeWH();
    
    //--------------------------------------------------------------------------
    
    public String getErrorMessage();

}
