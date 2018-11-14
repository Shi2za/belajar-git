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
import hotelfx.persistence.model.LogSystem;
import hotelfx.persistence.model.RefFinanceTransactionStatus;
import hotelfx.persistence.model.RefHotelInvoiceType;
import hotelfx.persistence.model.RefHotelPayableType;
import hotelfx.persistence.model.RefItemMutationType;
import hotelfx.persistence.model.RefItemObsoleteBy;
import hotelfx.persistence.model.RefItemType;
import hotelfx.persistence.model.RefLocationType;
import hotelfx.persistence.model.RefRecordStatus;
import hotelfx.persistence.model.RefReservationBillType;
import hotelfx.persistence.model.RefReservationCheckInOutStatus;
import hotelfx.persistence.model.RefReservationStatus;
import hotelfx.persistence.model.RefRoomCleanStatus;
import hotelfx.persistence.model.RefRoomStatus;
import hotelfx.persistence.model.SysCurrentHotelDate;
import hotelfx.persistence.model.SysDataHardCode;
import hotelfx.persistence.model.TblBuilding;
import hotelfx.persistence.model.TblCustomer;
import hotelfx.persistence.model.TblEmployee;
import hotelfx.persistence.model.TblFixedTangibleAsset;
import hotelfx.persistence.model.TblFloor;
import hotelfx.persistence.model.TblHotelInvoice;
import hotelfx.persistence.model.TblHotelPayable;
import hotelfx.persistence.model.TblItem;
import hotelfx.persistence.model.TblItemExpiredDate;
import hotelfx.persistence.model.TblItemLocation;
import hotelfx.persistence.model.TblItemLocationItemExpiredDate;
import hotelfx.persistence.model.TblItemLocationPropertyBarcode;
import hotelfx.persistence.model.TblItemMutationHistory;
import hotelfx.persistence.model.TblItemMutationHistoryItemExpiredDate;
import hotelfx.persistence.model.TblItemMutationHistoryPropertyBarcode;
import hotelfx.persistence.model.TblItemTypeHk;
import hotelfx.persistence.model.TblItemTypeWh;
import hotelfx.persistence.model.TblLocation;
import hotelfx.persistence.model.TblLocationOfBin;
import hotelfx.persistence.model.TblLocationOfLaundry;
import hotelfx.persistence.model.TblLocationOfWarehouse;
import hotelfx.persistence.model.TblPeople;
import hotelfx.persistence.model.TblPropertyBarcode;
import hotelfx.persistence.model.TblReservation;
import hotelfx.persistence.model.TblReservationAdditionalItem;
import hotelfx.persistence.model.TblReservationAdditionalItemReserved;
import hotelfx.persistence.model.TblReservationAdditionalService;
import hotelfx.persistence.model.TblReservationBrokenItem;
import hotelfx.persistence.model.TblReservationBrokenItemPropertyBarcode;
import hotelfx.persistence.model.TblReservationCheckInOut;
import hotelfx.persistence.model.TblReservationRoomPriceDetail;
import hotelfx.persistence.model.TblReservationRoomTypeDetail;
import hotelfx.persistence.model.TblReservationRoomTypeDetailItem;
import hotelfx.persistence.model.TblReservationRoomTypeDetailRoomPriceDetail;
import hotelfx.persistence.model.TblRoom;
import hotelfx.persistence.model.TblRoomCheck;
import hotelfx.persistence.model.TblRoomCheckHouseKeepingAttendance;
import hotelfx.persistence.model.TblRoomCheckHouseKeepingAttendanceDetail;
import hotelfx.persistence.model.TblRoomCheckItemMutationHistory;
import hotelfx.persistence.model.TblRoomService;
import hotelfx.persistence.model.TblRoomStatusChangeHistory;
import hotelfx.persistence.model.TblRoomStatusDetail;
import hotelfx.persistence.model.TblRoomType;
import hotelfx.persistence.model.TblRoomTypeItem;
import hotelfx.persistence.model.TblSupplier;
import hotelfx.persistence.model.TblSystemUser;
import hotelfx.persistence.model.TblUnit;
import hotelfx.view.feature_room_check.room_status.CustomerBrokenItemController;
import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import org.hibernate.Session;

/**
 *
 * @author ANDRI
 */
public class FRoomCheckManagerImpl implements FRoomCheckManager {

    private Session session;

    private String errMsg;

    public FRoomCheckManagerImpl() {

    }

    @Override
    public TblRoomCheck insertDataRoomCheck(TblRoomCheck roomCheck,
            List<TblRoomCheckItemMutationHistory> dataIns,
            List<TblRoomCheckItemMutationHistory> dataOuts,
            List<TblItemMutationHistoryPropertyBarcode> itemMutationHistoryPropertyBarcodes,
            List<TblItemMutationHistoryItemExpiredDate> itemMutationHistoryItemExpiredDates) {
        errMsg = "";
        TblRoomCheck tblRoomCheck = roomCheck;
        if (ClassSession.checkUserSession()) {  //check user current session
            try {
                session = HibernateUtil.getSessionFactory().getCurrentSession();
                session.beginTransaction();
                //sys data hardcode (default checkin time)
                Time defaultCheckInTime = new Time(0, 0, 0);
                SysDataHardCode sdhDefaultCheckInTime = session.get(SysDataHardCode.class, (long) 6);    //DefaultCheckInTime = '6'
                if (sdhDefaultCheckInTime != null
                        && sdhDefaultCheckInTime.getDataHardCodeValue() != null) {
                    String[] dataCheckInTime = sdhDefaultCheckInTime.getDataHardCodeValue().split(":");
                    defaultCheckInTime = new Time(Integer.parseInt(dataCheckInTime[0]),
                            Integer.parseInt(dataCheckInTime[1]),
                            Integer.parseInt(dataCheckInTime[2]));
                }
                //data room status change history
                TblRoomStatusChangeHistory dataRoomStatusChangeHistory = new TblRoomStatusChangeHistory();
                dataRoomStatusChangeHistory.setTblRoom(tblRoomCheck.getTblRoom());
                dataRoomStatusChangeHistory.setTblEmployeeByIdemployee(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                dataRoomStatusChangeHistory.setTblRoomStatusDetail(null);
                dataRoomStatusChangeHistory.setChangeRoomStatusDate(Timestamp.valueOf(LocalDateTime.now()));
                dataRoomStatusChangeHistory.setRefRoomStatusByRoomStatusOld(getOldRoomStatus(tblRoomCheck.getTblRoom().getIdroom()));
                dataRoomStatusChangeHistory.setRefRoomStatusByRoomStatusNew(tblRoomCheck.getTblRoom().getRefRoomStatus());
                dataRoomStatusChangeHistory.setCreateDate(Timestamp.valueOf(LocalDateTime.now()));
                dataRoomStatusChangeHistory.setTblEmployeeByCreateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                dataRoomStatusChangeHistory.setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                dataRoomStatusChangeHistory.setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                dataRoomStatusChangeHistory.setRefRecordStatus(session.find(RefRecordStatus.class, 1));
                session.saveOrUpdate(dataRoomStatusChangeHistory);
                //data room (update-room-status)
                tblRoomCheck.getTblRoom().setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                tblRoomCheck.getTblRoom().setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                session.update(tblRoomCheck.getTblRoom());
                //data room check
                tblRoomCheck.setTblRoomStatusChangeHistory(dataRoomStatusChangeHistory);
                tblRoomCheck.setCreateDate(Timestamp.valueOf(LocalDateTime.now()));
                tblRoomCheck.setTblEmployeeByCreateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                tblRoomCheck.setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                tblRoomCheck.setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                tblRoomCheck.setRefRecordStatus(session.find(RefRecordStatus.class, 1));
                session.saveOrUpdate(tblRoomCheck);
//            //data room check - item mutation (auto-out : consumable), not property, must run before create data (in), to location of bin
//            List<TblItemLocation> tempItemLocations = session.getNamedQuery("findAllTblItemLocationByIdLocation")
//                    .setParameter("idLocation", roomCheck.getTblRoom().getTblLocation().getIdlocation())
//                    .list();
//            for (TblItemLocation tempDataItemLocation : tempItemLocations) {
//                if (tempDataItemLocation.getTblItem().getConsumable()) {
//                    //data item mutation
//                    TblRoomCheckItemMutationHistory dataOut = new TblRoomCheckItemMutationHistory();
//                    dataOut.setTblRoomCheck(tblRoomCheck);
//                    dataOut.setTblItemMutationHistory(new TblItemMutationHistory());
//                    dataOut.getTblItemMutationHistory().setTblItem(session.find(TblItem.class, tempDataItemLocation.getTblItem().getIditem()));
//                    dataOut.getTblItemMutationHistory().setTblLocationByIdlocationOfSource(session.find(TblLocation.class, tempDataItemLocation.getTblLocation().getIdlocation()));
//                    dataOut.getTblItemMutationHistory().setTblLocationByIdlocationOfDestination(session.find(TblLocation.class, getFirstDataBin().getTblLocation().getIdlocation()));
//                    dataOut.getTblItemMutationHistory().setItemQuantity(tempDataItemLocation.getItemQuantity());
//                    dataOut.getTblItemMutationHistory().setRefItemMutationType(session.find(RefItemMutationType.class, 1));
//                    dataOut.getTblItemMutationHistory().setRefItemObsoleteBy(session.find(RefItemObsoleteBy.class, 3));
//                    dataOut.getTblItemMutationHistory().setMutationDate(Timestamp.valueOf(LocalDateTime.now()));
//                    dataOut.getTblItemMutationHistory().setCreateDate(Timestamp.valueOf(LocalDateTime.now()));
//                    dataOut.getTblItemMutationHistory().setTblEmployeeByCreateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
//                    dataOut.getTblItemMutationHistory().setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
//                    dataOut.getTblItemMutationHistory().setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
//                    dataOut.getTblItemMutationHistory().setRefRecordStatus(session.find(RefRecordStatus.class, 1));
//                    session.saveOrUpdate(dataOut.getTblItemMutationHistory());
//                    //data item location (source, udpate:minus)
//                    tempDataItemLocation.setItemQuantity(tempDataItemLocation.getItemQuantity().subtract(dataOut.getTblItemMutationHistory().getItemQuantity()));
//                    tempDataItemLocation.setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
//                    tempDataItemLocation.setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
//                    session.update(tempDataItemLocation);
//                    //data item location (destination, udpate:plus)
//                    TblItemLocation tempItemDestinationLocation = getItemLocationByIDLocationAndIDItem(dataOut.getTblItemMutationHistory().getTblLocationByIdlocationOfDestination().getIdlocation(),
//                            dataOut.getTblItemMutationHistory().getTblItem().getIditem());
//                    if (tempItemDestinationLocation != null) {    //update
//                        tempItemDestinationLocation.setItemQuantity(tempItemDestinationLocation.getItemQuantity().add(dataOut.getTblItemMutationHistory().getItemQuantity()));
//                        tempItemDestinationLocation.setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
//                        tempItemDestinationLocation.setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
//                        session.update(tempItemDestinationLocation);
//                    } else {  //create
//                        tempItemDestinationLocation = new TblItemLocation();
//                        tempItemDestinationLocation.setTblItem(session.find(TblItem.class, dataOut.getTblItemMutationHistory().getTblItem().getIditem()));
//                        tempItemDestinationLocation.setTblLocation(session.find(TblLocation.class, dataOut.getTblItemMutationHistory().getTblLocationByIdlocationOfDestination().getIdlocation()));
//                        tempItemDestinationLocation.setItemQuantity(dataOut.getTblItemMutationHistory().getItemQuantity());
//                        tempItemDestinationLocation.setCreateDate(Timestamp.valueOf(LocalDateTime.now()));
//                        tempItemDestinationLocation.setTblEmployeeByCreateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
//                        tempItemDestinationLocation.setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
//                        tempItemDestinationLocation.setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
//                        tempItemDestinationLocation.setRefRecordStatus(session.find(RefRecordStatus.class, 1));
//                        session.saveOrUpdate(tempItemDestinationLocation);
//                    }
//                    //data room check - item mutation
//                    dataOut.setCreateDate(Timestamp.valueOf(LocalDateTime.now()));
//                    dataOut.setTblEmployeeByCreateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
//                    dataOut.setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
//                    dataOut.setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
//                    dataOut.setRefRecordStatus(session.find(RefRecordStatus.class, 1));
//                    session.saveOrUpdate(dataOut);
//                }
//            }
                //data room check - item mutation (in)
                for (TblRoomCheckItemMutationHistory dataIn : dataIns) {
                    //data item mutation
                    dataIn.getTblItemMutationHistory().setMutationDate(Timestamp.valueOf(LocalDateTime.now()));
//                    dataIn.getTblItemMutationHistory().setTblEmployeeByIdemployee(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                    dataIn.getTblItemMutationHistory().setCreateDate(Timestamp.valueOf(LocalDateTime.now()));
                    dataIn.getTblItemMutationHistory().setTblEmployeeByCreateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                    dataIn.getTblItemMutationHistory().setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                    dataIn.getTblItemMutationHistory().setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                    dataIn.getTblItemMutationHistory().setRefRecordStatus(session.find(RefRecordStatus.class, 1));
                    session.saveOrUpdate(dataIn.getTblItemMutationHistory());
                    //data item location (update:stock_number)
                    TblItemLocation tempItemSourceLocation = getItemLocationByIDLocationAndIDItem(dataIn.getTblItemMutationHistory().getTblLocationByIdlocationOfSource().getIdlocation(),
                            dataIn.getTblItemMutationHistory().getTblItem().getIditem());
                    if (tempItemSourceLocation != null) {
                        if ((tempItemSourceLocation.getItemQuantity().subtract(dataIn.getTblItemMutationHistory().getItemQuantity()))
                                .compareTo(new BigDecimal("0")) > -1) {
                            //data item location (source, udpate:minus)
                            tempItemSourceLocation.setItemQuantity(tempItemSourceLocation.getItemQuantity().subtract(dataIn.getTblItemMutationHistory().getItemQuantity()));
                            tempItemSourceLocation.setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                            tempItemSourceLocation.setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                            session.update(tempItemSourceLocation);
                            //data item location (destination, udpate:plus)
                            TblItemLocation tempItemDestinationLocation = getItemLocationByIDLocationAndIDItem(dataIn.getTblItemMutationHistory().getTblLocationByIdlocationOfDestination().getIdlocation(),
                                    dataIn.getTblItemMutationHistory().getTblItem().getIditem());
                            if (tempItemDestinationLocation != null) {    //update
                                tempItemDestinationLocation.setItemQuantity(tempItemDestinationLocation.getItemQuantity().add(dataIn.getTblItemMutationHistory().getItemQuantity()));
                                tempItemDestinationLocation.setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                                tempItemDestinationLocation.setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                                session.update(tempItemDestinationLocation);
                            } else {  //create
                                tempItemDestinationLocation = new TblItemLocation();
                                tempItemDestinationLocation.setTblItem(session.find(TblItem.class, dataIn.getTblItemMutationHistory().getTblItem().getIditem()));
                                tempItemDestinationLocation.setTblLocation(session.find(TblLocation.class, dataIn.getTblItemMutationHistory().getTblLocationByIdlocationOfDestination().getIdlocation()));
                                tempItemDestinationLocation.setItemQuantity(dataIn.getTblItemMutationHistory().getItemQuantity());
                                tempItemDestinationLocation.setCreateDate(Timestamp.valueOf(LocalDateTime.now()));
                                tempItemDestinationLocation.setTblEmployeeByCreateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                                tempItemDestinationLocation.setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                                tempItemDestinationLocation.setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                                tempItemDestinationLocation.setRefRecordStatus(session.find(RefRecordStatus.class, 1));
                                session.saveOrUpdate(tempItemDestinationLocation);
                            }
                            //data item mutation - property barcode
                            if (dataIn.getTblItemMutationHistory().getTblItem().getPropertyStatus()) { //Property
                                for (TblItemMutationHistoryPropertyBarcode itemMutationHistoryPropertyBarcode : itemMutationHistoryPropertyBarcodes) {
                                    if (dataIn.getTblItemMutationHistory().equals(itemMutationHistoryPropertyBarcode.getTblItemMutationHistory())) {
                                        itemMutationHistoryPropertyBarcode.setCreateDate(Timestamp.valueOf(LocalDateTime.now()));
                                        itemMutationHistoryPropertyBarcode.setTblEmployeeByCreateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                                        itemMutationHistoryPropertyBarcode.setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                                        itemMutationHistoryPropertyBarcode.setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                                        itemMutationHistoryPropertyBarcode.setRefRecordStatus(session.find(RefRecordStatus.class, 1));
                                        session.saveOrUpdate(itemMutationHistoryPropertyBarcode);
                                        //data item location property barcode (update:relation)
                                        TblItemLocationPropertyBarcode tempItemSourceLocationBarcode = getItemLocationPropertyBarcodeByIDItemLocationAndIDPropertyBarcode(tempItemSourceLocation.getIdrelation(),
                                                itemMutationHistoryPropertyBarcode.getTblPropertyBarcode().getIdbarcode());
                                        if (tempItemSourceLocationBarcode != null) {
                                            //data item location property barcode (source, udpate:delete)
                                            tempItemSourceLocationBarcode.setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                                            tempItemSourceLocationBarcode.setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                                            tempItemSourceLocationBarcode.setRefRecordStatus(session.find(RefRecordStatus.class, 2));
                                            session.update(tempItemSourceLocationBarcode);
                                            //data item location property barcode (destination, update:insert)
                                            TblItemLocationPropertyBarcode tempItemDestinationLocationPropertyBarcode = new TblItemLocationPropertyBarcode();
                                            tempItemDestinationLocationPropertyBarcode.setTblItemLocation(session.find(TblItemLocation.class, tempItemDestinationLocation.getIdrelation()));
                                            tempItemDestinationLocationPropertyBarcode.setTblPropertyBarcode(session.find(TblPropertyBarcode.class, itemMutationHistoryPropertyBarcode.getTblPropertyBarcode().getIdbarcode()));
                                            tempItemDestinationLocationPropertyBarcode.setCreateDate(Timestamp.valueOf(LocalDateTime.now()));
                                            tempItemDestinationLocationPropertyBarcode.setTblEmployeeByCreateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                                            tempItemDestinationLocationPropertyBarcode.setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                                            tempItemDestinationLocationPropertyBarcode.setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                                            tempItemDestinationLocationPropertyBarcode.setRefRecordStatus(session.find(RefRecordStatus.class, 1));
                                            session.saveOrUpdate(tempItemDestinationLocationPropertyBarcode);
                                            session.update(tempItemSourceLocationBarcode);
//                                            session.evict(tempItemDestinationLocationPropertyBarcode);
                                        } else {  //failed
                                            if (session.getTransaction().isActive()) {
                                                session.getTransaction().rollback();
                                            }
                                            errMsg = "Data Barang - Tidak Ditemukan..!";
                                            return null;
                                        }
                                        break;
                                    }
                                }
                            }
                            //data item mutation - item expired date
                            if (dataIn.getTblItemMutationHistory().getTblItem().getConsumable()) {    //Consumable
                                for (TblItemMutationHistoryItemExpiredDate itemMutationHistoryItemExpiredDate : itemMutationHistoryItemExpiredDates) {
                                    if (dataIn.getTblItemMutationHistory().equals(itemMutationHistoryItemExpiredDate.getTblItemMutationHistory())) {
                                        itemMutationHistoryItemExpiredDate.setItemQuantity(dataIn.getTblItemMutationHistory().getItemQuantity());
                                        itemMutationHistoryItemExpiredDate.setCreateDate(Timestamp.valueOf(LocalDateTime.now()));
                                        itemMutationHistoryItemExpiredDate.setTblEmployeeByCreateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                                        itemMutationHistoryItemExpiredDate.setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                                        itemMutationHistoryItemExpiredDate.setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                                        itemMutationHistoryItemExpiredDate.setRefRecordStatus(session.find(RefRecordStatus.class, 1));
                                        session.saveOrUpdate(itemMutationHistoryItemExpiredDate);
                                        //data item location item expired date (update:relation)
                                        TblItemLocationItemExpiredDate tempItemSourceLocationExpiredDate = getItemLocationItemExpiredDateByIDItemLocationAndIDItemExpiredDate(
                                                tempItemSourceLocation.getIdrelation(),
                                                itemMutationHistoryItemExpiredDate.getTblItemExpiredDate().getIditemExpiredDate());
                                        if (tempItemSourceLocationExpiredDate == null) {
                                            tempItemSourceLocationExpiredDate = new TblItemLocationItemExpiredDate();
                                            tempItemSourceLocationExpiredDate.setTblItemLocation(session.find(TblItemLocation.class, tempItemSourceLocation.getIdrelation()));
                                            tempItemSourceLocationExpiredDate.setTblItemExpiredDate(session.find(TblItemExpiredDate.class, itemMutationHistoryItemExpiredDate.getTblItemExpiredDate().getIditemExpiredDate()));
                                            tempItemSourceLocationExpiredDate.setItemQuantity(new BigDecimal("0"));
                                            tempItemSourceLocationExpiredDate.setCreateDate(Timestamp.valueOf(LocalDateTime.now()));
                                            tempItemSourceLocationExpiredDate.setTblEmployeeByCreateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                                            tempItemSourceLocationExpiredDate.setRefRecordStatus(session.find(RefRecordStatus.class, 1));
                                        }
                                        //data item location item exprired date (source, udpate:+)
                                        tempItemSourceLocationExpiredDate.setItemQuantity(tempItemSourceLocationExpiredDate.getItemQuantity().add(itemMutationHistoryItemExpiredDate.getItemQuantity()));
                                        tempItemSourceLocationExpiredDate.setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                                        tempItemSourceLocationExpiredDate.setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                                        session.saveOrUpdate(tempItemSourceLocationExpiredDate);
                                        //data item location item expired date (destination, update:-)
                                        TblItemLocationItemExpiredDate tempItemDestinationLocationExpiredDate = getItemLocationItemExpiredDateByIDItemLocationAndIDItemExpiredDate(
                                                tempItemDestinationLocation.getIdrelation(),
                                                itemMutationHistoryItemExpiredDate.getTblItemExpiredDate().getIditemExpiredDate());
                                        if (tempItemDestinationLocationExpiredDate != null) {
                                            if ((tempItemDestinationLocationExpiredDate.getItemQuantity().subtract(itemMutationHistoryItemExpiredDate.getItemQuantity()))
                                                    .compareTo(new BigDecimal("0")) > -1) {
                                                tempItemDestinationLocationExpiredDate.setItemQuantity(tempItemDestinationLocationExpiredDate.getItemQuantity().subtract(itemMutationHistoryItemExpiredDate.getItemQuantity()));
                                                tempItemDestinationLocationExpiredDate.setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                                                tempItemDestinationLocationExpiredDate.setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                                                session.saveOrUpdate(tempItemDestinationLocationExpiredDate);
                                            } else {  //failed
                                                if (session.getTransaction().isActive()) {
                                                    session.getTransaction().rollback();
                                                }
                                                errMsg = "Stok Barang - Tidak Mencukupi..!";
                                                return null;
                                            }
                                        } else {  //failed
                                            if (session.getTransaction().isActive()) {
                                                session.getTransaction().rollback();
                                            }
                                            errMsg = "Data Barang - Tidak Ditemukan..!";
                                            return null;
                                        }
                                        break;
                                    }
                                }
                            }
//                            session.evict(tempItemDestinationLocation);
                        } else {  //failed
                            if (session.getTransaction().isActive()) {
                                session.getTransaction().rollback();
                            }
                            errMsg = "Stok Barang - Tidak Mencukupi..!";
                            return null;
                        }
//                        session.evict(tempItemSourceLocation);
                    } else {  //failed
                        if (session.getTransaction().isActive()) {
                            session.getTransaction().rollback();
                        }
                        errMsg = "Stok Barang - Tidak Mencukupi..!";
                        return null;
                    }
                    //data room check - item mutation
                    dataIn.setCreateDate(Timestamp.valueOf(LocalDateTime.now()));
                    dataIn.setTblEmployeeByCreateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                    dataIn.setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                    dataIn.setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                    dataIn.setRefRecordStatus(session.find(RefRecordStatus.class, 1));
                    session.saveOrUpdate(dataIn);
                }
                //data room check - item mutation (out)
                for (TblRoomCheckItemMutationHistory dataOut : dataOuts) {
                    //data item mutation
                    dataOut.getTblItemMutationHistory().setMutationDate(Timestamp.valueOf(LocalDateTime.now()));
//                    dataOut.getTblItemMutationHistory().setTblEmployeeByIdemployee(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                    dataOut.getTblItemMutationHistory().setCreateDate(Timestamp.valueOf(LocalDateTime.now()));
                    dataOut.getTblItemMutationHistory().setTblEmployeeByCreateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                    dataOut.getTblItemMutationHistory().setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                    dataOut.getTblItemMutationHistory().setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                    dataOut.getTblItemMutationHistory().setRefRecordStatus(session.find(RefRecordStatus.class, 1));
                    session.saveOrUpdate(dataOut.getTblItemMutationHistory());
                    //data item location (update:stock_number)
                    TblItemLocation tempItemSourceLocation = getItemLocationByIDLocationAndIDItem(dataOut.getTblItemMutationHistory().getTblLocationByIdlocationOfSource().getIdlocation(),
                            dataOut.getTblItemMutationHistory().getTblItem().getIditem());
                    if (tempItemSourceLocation != null) {
                        if ((tempItemSourceLocation.getItemQuantity().subtract(dataOut.getTblItemMutationHistory().getItemQuantity()))
                                .compareTo(new BigDecimal("0")) > -1) {
                            //data item location (source, udpate:minus)
                            tempItemSourceLocation.setItemQuantity(tempItemSourceLocation.getItemQuantity().subtract(dataOut.getTblItemMutationHistory().getItemQuantity()));
                            tempItemSourceLocation.setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                            tempItemSourceLocation.setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                            session.update(tempItemSourceLocation);
                            //data item location (destination, udpate:plus)
                            TblItemLocation tempItemDestinationLocation = getItemLocationByIDLocationAndIDItem(dataOut.getTblItemMutationHistory().getTblLocationByIdlocationOfDestination().getIdlocation(),
                                    dataOut.getTblItemMutationHistory().getTblItem().getIditem());
                            if (tempItemDestinationLocation != null) {    //update
                                tempItemDestinationLocation.setItemQuantity(tempItemDestinationLocation.getItemQuantity().add(dataOut.getTblItemMutationHistory().getItemQuantity()));
                                tempItemDestinationLocation.setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                                tempItemDestinationLocation.setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                                session.update(tempItemDestinationLocation);
                            } else {  //create
                                tempItemDestinationLocation = new TblItemLocation();
                                tempItemDestinationLocation.setTblItem(session.find(TblItem.class, dataOut.getTblItemMutationHistory().getTblItem().getIditem()));
                                tempItemDestinationLocation.setTblLocation(session.find(TblLocation.class, dataOut.getTblItemMutationHistory().getTblLocationByIdlocationOfDestination().getIdlocation()));
                                tempItemDestinationLocation.setItemQuantity(dataOut.getTblItemMutationHistory().getItemQuantity());
                                tempItemDestinationLocation.setCreateDate(Timestamp.valueOf(LocalDateTime.now()));
                                tempItemDestinationLocation.setTblEmployeeByCreateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                                tempItemDestinationLocation.setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                                tempItemDestinationLocation.setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                                tempItemDestinationLocation.setRefRecordStatus(session.find(RefRecordStatus.class, 1));
                                session.saveOrUpdate(tempItemDestinationLocation);
                            }
                            //data room type detail & current reservation date
                            TblReservationRoomTypeDetail dataReservationRoomTypeDetail;  //@ctive?
                            Date currentReservationDate;
                            //data reservation broken item (reservation - customer)
                            TblReservationBrokenItem reservationBrokenItem = null;
                            if (dataOut.getTblItemMutationHistory().getRefItemMutationType().getIdtype() == 2 //Rusak = '2'
                                    && dataOut.getTblItemMutationHistory().getRefItemObsoleteBy() != null) {
                                if (dataOut.getTblItemMutationHistory().getRefItemObsoleteBy().getIdobsoleteBy() == 0) {    //Customer = '0'
                                    //prev
                                    dataReservationRoomTypeDetail = getPreviousRRTD(roomCheck.getTblRoom(), Date.valueOf((LocalDate.now()).minusDays(1)));
                                    //current
                                    if (dataReservationRoomTypeDetail == null) {
                                        dataReservationRoomTypeDetail = getCurrentRRTD(roomCheck.getTblRoom(), Date.valueOf(LocalDate.now()));
                                    }
                                    //check data reservation - room
                                    if (dataReservationRoomTypeDetail == null) {
                                        if (session.getTransaction().isActive()) {
                                            session.getTransaction().rollback();
                                        }
                                        errMsg = "Data Reservasi Kamar - Tidak Ditemukan..!";
                                        return null;
                                    }
////                            if (Time.valueOf(LocalTime.now()).before(ClassDataHardcode.defaultCheckInTime)) { //before check in time (before)
//                                    currentReservationDate = Date.valueOf((LocalDate.of(roomCheck.getCheckDateTime().getYear() + 1900, roomCheck.getCheckDateTime().getMonth() + 1, roomCheck.getCheckDateTime().getDate()).minusDays(1)));
//                                    if ((new Time(roomCheck.getCheckDateTime().getHours(), roomCheck.getCheckDateTime().getMinutes(), 0)).before(defaultCheckInTime)) { //before check in time (before)
//                                        dataReservationRoomTypeDetail = getReservationRoomTypeDetail(roomCheck.getTblRoom().getIdroom(),
//                                                //                                      Date.valueOf((LocalDate.now()).minusDays(1)));
//                                                currentReservationDate);
//                                        if (dataReservationRoomTypeDetail == null) {
//                                            if (session.getTransaction().isActive()) {
//                                                session.getTransaction().rollback();
//                                            }
//                                            errMsg = "Data Reservasi Kamar - Tidak Ditemukan..!";
//                                            return null;
//                                        }
//                                        if (dataReservationRoomTypeDetail.getTblReservation().getRefReservationStatus().getIdstatus()
//                                                == 4) {   //reservation status : check out
//                                            if (session.getTransaction().isActive()) {
//                                                session.getTransaction().rollback();
//                                            }
//                                            errMsg = "Data Reservasi Kamar - Tidak Sesuai (status:checkout)..!";
//                                            return null;
//                                        }
//                                    } else {  //after check in time (current)
//                                        currentReservationDate = Date.valueOf(LocalDate.of(roomCheck.getCheckDateTime().getYear() + 1900, roomCheck.getCheckDateTime().getMonth() + 1, roomCheck.getCheckDateTime().getDate()));
//                                        dataReservationRoomTypeDetail = getReservationRoomTypeDetail(roomCheck.getTblRoom().getIdroom(),
//                                                //                                        Date.valueOf(LocalDate.now()));
//                                                currentReservationDate);
//                                        if (dataReservationRoomTypeDetail == null) {
//                                            //check (before)
//                                            dataReservationRoomTypeDetail = getReservationRoomTypeDetail(roomCheck.getTblRoom().getIdroom(),
//                                                    //                                            Date.valueOf((LocalDate.now()).minusDays(1)));
//                                                    Date.valueOf((LocalDate.of(roomCheck.getCheckDateTime().getYear() + 1900, roomCheck.getCheckDateTime().getMonth() + 1, roomCheck.getCheckDateTime().getDate()).minusDays(1))));
//                                        }
//                                        if (dataReservationRoomTypeDetail == null) {
//                                            if (session.getTransaction().isActive()) {
//                                                session.getTransaction().rollback();
//                                            }
//                                            errMsg = "Data Reservasi Kamar - Tidak Ditemukan..!";
//                                            return null;
//                                        }
//                                        if (dataReservationRoomTypeDetail.getTblReservation().getRefReservationStatus().getIdstatus()
//                                                == 4) {   //reservation status : check out
//                                            if (session.getTransaction().isActive()) {
//                                                session.getTransaction().rollback();
//                                            }
//                                            errMsg = "Data Reservasi Kamar - Tidak Sesuai (status:checkout)..!";
//                                            return null;
//                                        }
//                                    }
                                    //data reservation broken item
                                    reservationBrokenItem = new TblReservationBrokenItem();
                                    reservationBrokenItem.setTblReservationRoomTypeDetail(dataReservationRoomTypeDetail);
                                    reservationBrokenItem.setTblItem(dataOut.getTblItemMutationHistory().getTblItem());
                                    reservationBrokenItem.setItemCharge(dataOut.getTblItemMutationHistory().getTblItem().getBrokenCharge());
                                    reservationBrokenItem.setItemQuantity(dataOut.getTblItemMutationHistory().getItemQuantity());
                                    reservationBrokenItem.setCreateDate(Timestamp.valueOf(LocalDateTime.now()));
                                    reservationBrokenItem.setTblEmployeeByCreateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                                    reservationBrokenItem.setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                                    reservationBrokenItem.setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                                    reservationBrokenItem.setRefRecordStatus(session.find(RefRecordStatus.class, 1));
                                    session.saveOrUpdate(reservationBrokenItem);
                                }
                            }
                            //data item mutation - property barcode
                            if (dataOut.getTblItemMutationHistory().getTblItem().getPropertyStatus()) {    //Property
                                for (TblItemMutationHistoryPropertyBarcode itemMutationHistoryPropertyBarcode : itemMutationHistoryPropertyBarcodes) {
                                    if (dataOut.getTblItemMutationHistory().equals(itemMutationHistoryPropertyBarcode.getTblItemMutationHistory())) {
                                        itemMutationHistoryPropertyBarcode.setCreateDate(Timestamp.valueOf(LocalDateTime.now()));
                                        itemMutationHistoryPropertyBarcode.setTblEmployeeByCreateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                                        itemMutationHistoryPropertyBarcode.setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                                        itemMutationHistoryPropertyBarcode.setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                                        itemMutationHistoryPropertyBarcode.setRefRecordStatus(session.find(RefRecordStatus.class, 1));
                                        session.saveOrUpdate(itemMutationHistoryPropertyBarcode);
                                        //data item location property barcode (update:relation)
                                        TblItemLocationPropertyBarcode tempItemSourceLocationBarcode = getItemLocationPropertyBarcodeByIDItemLocationAndIDPropertyBarcode(tempItemSourceLocation.getIdrelation(),
                                                itemMutationHistoryPropertyBarcode.getTblPropertyBarcode().getIdbarcode());
                                        if (tempItemSourceLocationBarcode != null) {
                                            //data item location property barcode (source, udpate:delete)
                                            tempItemSourceLocationBarcode.setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                                            tempItemSourceLocationBarcode.setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                                            tempItemSourceLocationBarcode.setRefRecordStatus(session.find(RefRecordStatus.class, 2));
                                            session.update(tempItemSourceLocationBarcode);
                                            //data item location property barcode (destination, update:insert)
                                            TblItemLocationPropertyBarcode tempItemDestinationLocationPropertyBarcode = new TblItemLocationPropertyBarcode();
                                            tempItemDestinationLocationPropertyBarcode.setTblItemLocation(session.find(TblItemLocation.class, tempItemDestinationLocation.getIdrelation()));
                                            tempItemDestinationLocationPropertyBarcode.setTblPropertyBarcode(session.find(TblPropertyBarcode.class, itemMutationHistoryPropertyBarcode.getTblPropertyBarcode().getIdbarcode()));
                                            tempItemDestinationLocationPropertyBarcode.setCreateDate(Timestamp.valueOf(LocalDateTime.now()));
                                            tempItemDestinationLocationPropertyBarcode.setTblEmployeeByCreateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                                            tempItemDestinationLocationPropertyBarcode.setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                                            tempItemDestinationLocationPropertyBarcode.setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                                            tempItemDestinationLocationPropertyBarcode.setRefRecordStatus(session.find(RefRecordStatus.class, 1));
                                            session.saveOrUpdate(tempItemDestinationLocationPropertyBarcode);
                                            session.update(tempItemSourceLocationBarcode);
//                                            session.evict(tempItemDestinationLocationPropertyBarcode);
                                        } else {  //failed
                                            if (session.getTransaction().isActive()) {
                                                session.getTransaction().rollback();
                                            }
                                            errMsg = "Data Barang - Tidak Ditemukan..!";
                                            return null;
                                        }
                                        //data reservation broken item property barcode
                                        if (reservationBrokenItem != null) {  //if broken with customer
                                            TblReservationBrokenItemPropertyBarcode reservationBrokenItemPropertyBarcode = new TblReservationBrokenItemPropertyBarcode();
                                            reservationBrokenItemPropertyBarcode.setTblReservationBrokenItem(reservationBrokenItem);
                                            reservationBrokenItemPropertyBarcode.setTblPropertyBarcode(session.find(TblPropertyBarcode.class, itemMutationHistoryPropertyBarcode.getTblPropertyBarcode().getIdbarcode()));
                                            reservationBrokenItemPropertyBarcode.setCreateDate(Timestamp.valueOf(LocalDateTime.now()));
                                            reservationBrokenItemPropertyBarcode.setTblEmployeeByCreateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                                            reservationBrokenItemPropertyBarcode.setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                                            reservationBrokenItemPropertyBarcode.setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                                            reservationBrokenItemPropertyBarcode.setRefRecordStatus(session.find(RefRecordStatus.class, 1));
                                            session.saveOrUpdate(reservationBrokenItemPropertyBarcode);
                                        }
                                        break;
                                    }
                                }
//                                //data reservation additional item - resereved (updating, just for 'asset')
//                                if (dataReservationRoomTypeDetail != null
//                                        && currentReservationDate != null) {
//                                    TblReservationAdditionalItemReserved reservationAdditionalItemReserved = getReservationAdditionalItemReservedByIDReservationRoomTypeDetailAndIDItemAndReservedDate(
//                                            dataReservationRoomTypeDetail.getIddetail(),
//                                            dataOut.getTblItemMutationHistory().getTblItem().getIditem(),
//                                            currentReservationDate);
//                                    if (reservationAdditionalItemReserved != null
//                                            && reservationAdditionalItemReserved.getRefReservationAdditionalItemReservedStatus().getIdstatus() == 1) {  //Reserved = '1'
//                                        if ((dataOut.getTblItemMutationHistory().getItemQuantity().add(reservationAdditionalItemReserved.getDoneQuantity()))
//                                                .compareTo(reservationAdditionalItemReserved.getReservedQuantity()) > -1) {
//                                            reservationAdditionalItemReserved.setDoneQuantity(reservationAdditionalItemReserved.getReservedQuantity());
//                                            reservationAdditionalItemReserved.setRefReservationAdditionalItemReservedStatus(session.find(RefReservationAdditionalItemReservedStatus.class, 2)); //Done = '2'
//                                            reservationAdditionalItemReserved.setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
//                                            reservationAdditionalItemReserved.setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
//                                            session.update(reservationAdditionalItemReserved);
//                                        } else {
//                                            reservationAdditionalItemReserved.setDoneQuantity(reservationAdditionalItemReserved.getDoneQuantity().add(dataOut.getTblItemMutationHistory().getItemQuantity()));
//                                            reservationAdditionalItemReserved.setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
//                                            reservationAdditionalItemReserved.setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
//                                            session.update(reservationAdditionalItemReserved);
//                                        }
//                                    }
//                                }
                            }
                            //data item mutation - item expired date
                            if (dataOut.getTblItemMutationHistory().getTblItem().getConsumable()) {    //Consumable
                                for (TblItemMutationHistoryItemExpiredDate itemMutationHistoryItemExpiredDate : itemMutationHistoryItemExpiredDates) {
                                    if (dataOut.getTblItemMutationHistory().equals(itemMutationHistoryItemExpiredDate.getTblItemMutationHistory())) {
                                        itemMutationHistoryItemExpiredDate.setItemQuantity(dataOut.getTblItemMutationHistory().getItemQuantity());
                                        itemMutationHistoryItemExpiredDate.setCreateDate(Timestamp.valueOf(LocalDateTime.now()));
                                        itemMutationHistoryItemExpiredDate.setTblEmployeeByCreateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                                        itemMutationHistoryItemExpiredDate.setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                                        itemMutationHistoryItemExpiredDate.setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                                        itemMutationHistoryItemExpiredDate.setRefRecordStatus(session.find(RefRecordStatus.class, 1));
                                        session.saveOrUpdate(itemMutationHistoryItemExpiredDate);
                                        //data item location item expired date (update:relation)
                                        TblItemLocationItemExpiredDate tempItemSourceLocationExpiredDate = getItemLocationItemExpiredDateByIDItemLocationAndIDItemExpiredDate(
                                                tempItemSourceLocation.getIdrelation(),
                                                itemMutationHistoryItemExpiredDate.getTblItemExpiredDate().getIditemExpiredDate());
                                        if (tempItemSourceLocationExpiredDate != null) {
                                            if ((tempItemSourceLocationExpiredDate.getItemQuantity().subtract(itemMutationHistoryItemExpiredDate.getItemQuantity()))
                                                    .compareTo(new BigDecimal("0")) > -1) {
                                                //data item location item exprired date (source, udpate:-)
                                                tempItemSourceLocationExpiredDate.setItemQuantity(tempItemSourceLocationExpiredDate.getItemQuantity().subtract(itemMutationHistoryItemExpiredDate.getItemQuantity()));
                                                tempItemSourceLocationExpiredDate.setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                                                tempItemSourceLocationExpiredDate.setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                                                session.update(tempItemSourceLocationExpiredDate);
                                                //data item location item expired date (destination, update:+)
                                                TblItemLocationItemExpiredDate tempItemDestinationLocationExpiredDate = getItemLocationItemExpiredDateByIDItemLocationAndIDItemExpiredDate(
                                                        tempItemDestinationLocation.getIdrelation(),
                                                        itemMutationHistoryItemExpiredDate.getTblItemExpiredDate().getIditemExpiredDate());
                                                if (tempItemDestinationLocationExpiredDate == null) {
                                                    tempItemDestinationLocationExpiredDate = new TblItemLocationItemExpiredDate();
                                                    tempItemDestinationLocationExpiredDate.setTblItemLocation(session.find(TblItemLocation.class, tempItemDestinationLocation.getIdrelation()));
                                                    tempItemDestinationLocationExpiredDate.setTblItemExpiredDate(session.find(TblItemExpiredDate.class, itemMutationHistoryItemExpiredDate.getTblItemExpiredDate().getIditemExpiredDate()));
                                                    tempItemDestinationLocationExpiredDate.setItemQuantity(new BigDecimal("0"));
                                                    tempItemDestinationLocationExpiredDate.setCreateDate(Timestamp.valueOf(LocalDateTime.now()));
                                                    tempItemDestinationLocationExpiredDate.setTblEmployeeByCreateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                                                    tempItemDestinationLocationExpiredDate.setRefRecordStatus(session.find(RefRecordStatus.class, 1));
                                                }
                                                tempItemDestinationLocationExpiredDate.setItemQuantity(tempItemDestinationLocationExpiredDate.getItemQuantity().add(itemMutationHistoryItemExpiredDate.getItemQuantity()));
                                                tempItemDestinationLocationExpiredDate.setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                                                tempItemDestinationLocationExpiredDate.setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                                                session.saveOrUpdate(tempItemDestinationLocationExpiredDate);
                                            } else {  //failed
                                                if (session.getTransaction().isActive()) {
                                                    session.getTransaction().rollback();
                                                }
                                                errMsg = "Stok Barang - Tidak Mencukupi..!";
                                                return null;
                                            }
                                        } else {  //failed
                                            if (session.getTransaction().isActive()) {
                                                session.getTransaction().rollback();
                                            }
                                            errMsg = "Data Barang - Tidak Ditemukan..!";
                                            return null;
                                        }
                                        break;
                                    }
                                }
                            }
//                            session.evict(tempItemDestinationLocation);
                        } else {  //failed
                            if (session.getTransaction().isActive()) {
                                session.getTransaction().rollback();
                            }
                            errMsg = "Stok Barang - Tidak Mencukupi..!";
                            return null;
                        }
//                        session.evict(tempItemSourceLocation);
                    } else {  //failed
                        if (session.getTransaction().isActive()) {
                            session.getTransaction().rollback();
                        }
                        errMsg = "Stok Barang - Tidak Mencukupi..!";
                        return null;
                    }
                    //data room check - item mutation
                    dataOut.setCreateDate(Timestamp.valueOf(LocalDateTime.now()));
                    dataOut.setTblEmployeeByCreateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                    dataOut.setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                    dataOut.setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                    dataOut.setRefRecordStatus(session.find(RefRecordStatus.class, 1));
                    session.saveOrUpdate(dataOut);
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
        return tblRoomCheck;
    }

    private TblItemLocation getItemLocationByIDLocationAndIDItem(long idLocation, long idItem) {
        List<TblItemLocation> list = session.getNamedQuery("findAllItemLocationByIdLocationAndIdItem")
                .setParameter("idLocation", idLocation)
                .setParameter("idItem", idItem)
                .list();
        return list.isEmpty() ? null : list.get(0);
    }

    private TblItemLocationPropertyBarcode getItemLocationPropertyBarcodeByIDItemLocationAndIDPropertyBarcode(long idItemLocation, long idPropertyBarcode) {
        List<TblItemLocationPropertyBarcode> list = session.getNamedQuery("findAllTblItemLocationPropertyBarcodeByItemLocationAndPropertyBarcode")
                .setParameter("idrelation", idItemLocation)
                .setParameter("idproperty", idPropertyBarcode)
                .list();
        return list.isEmpty() ? null : list.get(0);
    }

    private TblReservationRoomTypeDetail getReservationRoomTypeDetail(long idRoom, Date detailDate) {   //@ctive?
        List<TblReservationRoomTypeDetailRoomPriceDetail> list = session.getNamedQuery("findAllTblReservationRoomTypeDetailRoomPriceDetailByIDRoomAndDetailDate")
                .setParameter("idRoom", idRoom)
                .setParameter("detailDate", detailDate)
                .list();
        System.out.println("idRoom : " + idRoom + " --- " + "detailDate : " + detailDate);
        if (list.isEmpty()) {
            System.out.println("zzzzzz");
            return null;
        }
        //if one data reservation room type detail per room (room type) & date
        return session.find(TblReservationRoomTypeDetail.class, list.get(0).getTblReservationRoomTypeDetail().getIddetail());
    }

    private RefRoomStatus getOldRoomStatus(long idRoom) {
        TblRoom dataRoom = session.find(TblRoom.class, idRoom);
        RefRoomStatus dataRoomStatus = session.find(RefRoomStatus.class, dataRoom.getRefRoomStatus().getIdstatus());
        session.evict(dataRoom);
        return dataRoomStatus;
    }

//    private TblLocationOfBin getFirstDataBin() {
//        List<TblLocationOfBin> list = session.getNamedQuery("findAllTblLocationOfBin")
//                .list();
//        session.getTransaction().commit();
//        return list.isEmpty() ? null : list.get(0);
//    }
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
    public boolean updateDataRoomCheck(TblRoomCheck roomCheck,
            List<TblRoomCheckItemMutationHistory> dataIns,
            List<TblRoomCheckItemMutationHistory> dataOuts,
            List<TblItemMutationHistoryPropertyBarcode> itemMutationHistoryPropertyBarcodes,
            List<TblItemMutationHistoryItemExpiredDate> itemMutationHistoryItemExpiredDates) {
        errMsg = "";
        if (ClassSession.checkUserSession()) {  //check user current session
            try {
                session = HibernateUtil.getSessionFactory().getCurrentSession();
                session.beginTransaction();
                //sys data hardcode (default checkin time)
                Time defaultCheckInTime = new Time(0, 0, 0);
                SysDataHardCode sdhDefaultCheckInTime = session.get(SysDataHardCode.class, (long) 6);    //DefaultCheckInTime = '6'
                if (sdhDefaultCheckInTime != null
                        && sdhDefaultCheckInTime.getDataHardCodeValue() != null) {
                    String[] dataCheckInTime = sdhDefaultCheckInTime.getDataHardCodeValue().split(":");
                    defaultCheckInTime = new Time(Integer.parseInt(dataCheckInTime[0]),
                            Integer.parseInt(dataCheckInTime[1]),
                            Integer.parseInt(dataCheckInTime[2]));
                }
                //data room check
                roomCheck.setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                roomCheck.setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                session.update(roomCheck);
//            //data room (update-room-status)
//            roomCheck.getTblRoom().setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
//            roomCheck.getTblRoom().setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
//            session.update(roomCheck.getTblRoom());
                //delete data room check - item mutation
                //@@@
                //data room check - item mutation (in)
                for (TblRoomCheckItemMutationHistory dataIn : dataIns) {
                    //data item mutation
                    if (dataIn.getTblItemMutationHistory().getCreateDate() == null) {
                        dataIn.getTblItemMutationHistory().setMutationDate(Timestamp.valueOf(LocalDateTime.now()));
//                        dataIn.getTblItemMutationHistory().setTblEmployeeByIdemployee(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                        dataIn.getTblItemMutationHistory().setCreateDate(Timestamp.valueOf(LocalDateTime.now()));
                        dataIn.getTblItemMutationHistory().setTblEmployeeByCreateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                        dataIn.getTblItemMutationHistory().setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                        dataIn.getTblItemMutationHistory().setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                        dataIn.getTblItemMutationHistory().setRefRecordStatus(session.find(RefRecordStatus.class, 1));
                        session.saveOrUpdate(dataIn.getTblItemMutationHistory());
                        //data item location (update:stock_number)
                        TblItemLocation tempItemSourceLocation = getItemLocationByIDLocationAndIDItem(dataIn.getTblItemMutationHistory().getTblLocationByIdlocationOfSource().getIdlocation(),
                                dataIn.getTblItemMutationHistory().getTblItem().getIditem());
                        if (tempItemSourceLocation != null) {
                            if ((tempItemSourceLocation.getItemQuantity().subtract(dataIn.getTblItemMutationHistory().getItemQuantity()))
                                    .compareTo(new BigDecimal("0")) > -1) {
                                //data item location (source, udpate:minus)
                                tempItemSourceLocation.setItemQuantity(tempItemSourceLocation.getItemQuantity().subtract(dataIn.getTblItemMutationHistory().getItemQuantity()));
                                tempItemSourceLocation.setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                                tempItemSourceLocation.setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                                session.update(tempItemSourceLocation);
                                //data item location (destination, udpate:plus)
                                TblItemLocation tempItemDestinationLocation = getItemLocationByIDLocationAndIDItem(dataIn.getTblItemMutationHistory().getTblLocationByIdlocationOfDestination().getIdlocation(),
                                        dataIn.getTblItemMutationHistory().getTblItem().getIditem());
                                if (tempItemDestinationLocation != null) {    //update
                                    tempItemDestinationLocation.setItemQuantity(tempItemDestinationLocation.getItemQuantity().add(dataIn.getTblItemMutationHistory().getItemQuantity()));
                                    tempItemDestinationLocation.setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                                    tempItemDestinationLocation.setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                                    session.update(tempItemDestinationLocation);
                                } else {  //create
                                    tempItemDestinationLocation = new TblItemLocation();
                                    tempItemDestinationLocation.setTblItem(session.find(TblItem.class, dataIn.getTblItemMutationHistory().getTblItem().getIditem()));
                                    tempItemDestinationLocation.setTblLocation(session.find(TblLocation.class, dataIn.getTblItemMutationHistory().getTblLocationByIdlocationOfDestination().getIdlocation()));
                                    tempItemDestinationLocation.setItemQuantity(dataIn.getTblItemMutationHistory().getItemQuantity());
                                    tempItemDestinationLocation.setCreateDate(Timestamp.valueOf(LocalDateTime.now()));
                                    tempItemDestinationLocation.setTblEmployeeByCreateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                                    tempItemDestinationLocation.setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                                    tempItemDestinationLocation.setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                                    tempItemDestinationLocation.setRefRecordStatus(session.find(RefRecordStatus.class, 1));
                                    session.saveOrUpdate(tempItemDestinationLocation);
                                }
                                //data item mutation - property barcode
                                if (dataIn.getTblItemMutationHistory().getTblItem().getPropertyStatus()) { //Property
                                    for (TblItemMutationHistoryPropertyBarcode itemMutationHistoryPropertyBarcode : itemMutationHistoryPropertyBarcodes) {
                                        if (dataIn.getTblItemMutationHistory().equals(itemMutationHistoryPropertyBarcode.getTblItemMutationHistory())) {
                                            itemMutationHistoryPropertyBarcode.setCreateDate(Timestamp.valueOf(LocalDateTime.now()));
                                            itemMutationHistoryPropertyBarcode.setTblEmployeeByCreateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                                            itemMutationHistoryPropertyBarcode.setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                                            itemMutationHistoryPropertyBarcode.setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                                            itemMutationHistoryPropertyBarcode.setRefRecordStatus(session.find(RefRecordStatus.class, 1));
                                            session.saveOrUpdate(itemMutationHistoryPropertyBarcode);
                                            //data item location property barcode (update:relation)
                                            TblItemLocationPropertyBarcode tempItemSourceLocationBarcode = getItemLocationPropertyBarcodeByIDItemLocationAndIDPropertyBarcode(tempItemSourceLocation.getIdrelation(),
                                                    itemMutationHistoryPropertyBarcode.getTblPropertyBarcode().getIdbarcode());
                                            if (tempItemSourceLocationBarcode != null) {
                                                //data item location property barcode (source, udpate:delete)
                                                tempItemSourceLocationBarcode.setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                                                tempItemSourceLocationBarcode.setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                                                tempItemSourceLocationBarcode.setRefRecordStatus(session.find(RefRecordStatus.class, 2));
                                                session.update(tempItemSourceLocationBarcode);
                                                //data item location property barcode (destination, update:insert)
                                                TblItemLocationPropertyBarcode tempItemDestinationLocationPropertyBarcode = new TblItemLocationPropertyBarcode();
                                                tempItemDestinationLocationPropertyBarcode.setTblItemLocation(session.find(TblItemLocation.class, tempItemDestinationLocation.getIdrelation()));
                                                tempItemDestinationLocationPropertyBarcode.setTblPropertyBarcode(session.find(TblPropertyBarcode.class, itemMutationHistoryPropertyBarcode.getTblPropertyBarcode().getIdbarcode()));
                                                tempItemDestinationLocationPropertyBarcode.setCreateDate(Timestamp.valueOf(LocalDateTime.now()));
                                                tempItemDestinationLocationPropertyBarcode.setTblEmployeeByCreateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                                                tempItemDestinationLocationPropertyBarcode.setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                                                tempItemDestinationLocationPropertyBarcode.setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                                                tempItemDestinationLocationPropertyBarcode.setRefRecordStatus(session.find(RefRecordStatus.class, 1));
                                                session.saveOrUpdate(tempItemDestinationLocationPropertyBarcode);
                                                session.update(tempItemSourceLocationBarcode);
                                            } else {  //failed
                                                if (session.getTransaction().isActive()) {
                                                    session.getTransaction().rollback();
                                                }
                                                errMsg = "Data Barang - Tidak Ditemukan..!";
                                                return false;
                                            }
                                            break;
                                        }
                                    }
                                }
                                //data item mutation - item expired date
                                if (dataIn.getTblItemMutationHistory().getTblItem().getConsumable()) {    //Consumable
                                    for (TblItemMutationHistoryItemExpiredDate itemMutationHistoryItemExpiredDate : itemMutationHistoryItemExpiredDates) {
                                        if (dataIn.getTblItemMutationHistory().equals(itemMutationHistoryItemExpiredDate.getTblItemMutationHistory())) {
                                            itemMutationHistoryItemExpiredDate.setItemQuantity(dataIn.getTblItemMutationHistory().getItemQuantity());
                                            itemMutationHistoryItemExpiredDate.setCreateDate(Timestamp.valueOf(LocalDateTime.now()));
                                            itemMutationHistoryItemExpiredDate.setTblEmployeeByCreateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                                            itemMutationHistoryItemExpiredDate.setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                                            itemMutationHistoryItemExpiredDate.setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                                            itemMutationHistoryItemExpiredDate.setRefRecordStatus(session.find(RefRecordStatus.class, 1));
                                            session.saveOrUpdate(itemMutationHistoryItemExpiredDate);
                                            //data item location item expired date (update:relation)
                                            TblItemLocationItemExpiredDate tempItemSourceLocationExpiredDate = getItemLocationItemExpiredDateByIDItemLocationAndIDItemExpiredDate(
                                                    tempItemSourceLocation.getIdrelation(),
                                                    itemMutationHistoryItemExpiredDate.getTblItemExpiredDate().getIditemExpiredDate());
                                            if (tempItemSourceLocationExpiredDate == null) {
                                                tempItemSourceLocationExpiredDate = new TblItemLocationItemExpiredDate();
                                                tempItemSourceLocationExpiredDate.setTblItemLocation(session.find(TblItemLocation.class, tempItemSourceLocation.getIdrelation()));
                                                tempItemSourceLocationExpiredDate.setTblItemExpiredDate(session.find(TblItemExpiredDate.class, itemMutationHistoryItemExpiredDate.getTblItemExpiredDate().getIditemExpiredDate()));
                                                tempItemSourceLocationExpiredDate.setItemQuantity(new BigDecimal("0"));
                                                tempItemSourceLocationExpiredDate.setCreateDate(Timestamp.valueOf(LocalDateTime.now()));
                                                tempItemSourceLocationExpiredDate.setTblEmployeeByCreateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                                                tempItemSourceLocationExpiredDate.setRefRecordStatus(session.find(RefRecordStatus.class, 1));
                                            }
                                            //data item location item exprired date (source, udpate:+)
                                            tempItemSourceLocationExpiredDate.setItemQuantity(tempItemSourceLocationExpiredDate.getItemQuantity().add(itemMutationHistoryItemExpiredDate.getItemQuantity()));
                                            tempItemSourceLocationExpiredDate.setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                                            tempItemSourceLocationExpiredDate.setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                                            session.saveOrUpdate(tempItemSourceLocationExpiredDate);
                                            //data item location item expired date (destination, update:-)
                                            TblItemLocationItemExpiredDate tempItemDestinationLocationExpiredDate = getItemLocationItemExpiredDateByIDItemLocationAndIDItemExpiredDate(
                                                    tempItemDestinationLocation.getIdrelation(),
                                                    itemMutationHistoryItemExpiredDate.getTblItemExpiredDate().getIditemExpiredDate());
                                            if (tempItemDestinationLocationExpiredDate != null) {
                                                if ((tempItemDestinationLocationExpiredDate.getItemQuantity().subtract(itemMutationHistoryItemExpiredDate.getItemQuantity()))
                                                        .compareTo(new BigDecimal("0")) > -1) {
                                                    tempItemDestinationLocationExpiredDate.setItemQuantity(tempItemDestinationLocationExpiredDate.getItemQuantity().subtract(itemMutationHistoryItemExpiredDate.getItemQuantity()));
                                                    tempItemDestinationLocationExpiredDate.setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                                                    tempItemDestinationLocationExpiredDate.setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                                                    session.saveOrUpdate(tempItemDestinationLocationExpiredDate);
                                                } else {  //failed
                                                    if (session.getTransaction().isActive()) {
                                                        session.getTransaction().rollback();
                                                    }
                                                    errMsg = "Stok Barang - Tidak Mencukupi..!";
                                                    return false;
                                                }
                                            } else {  //failed
                                                if (session.getTransaction().isActive()) {
                                                    session.getTransaction().rollback();
                                                }
                                                errMsg = "Data Barang - Tidak Ditemukan..!";
                                                return false;
                                            }
                                            break;
                                        }
                                    }
                                }
                            } else {  //failed
                                if (session.getTransaction().isActive()) {
                                    session.getTransaction().rollback();
                                }
                                errMsg = "Stok Barang - Tidak Mencukupi..!";
                                return false;
                            }
                        } else {  //failed
                            if (session.getTransaction().isActive()) {
                                session.getTransaction().rollback();
                            }
                            errMsg = "Stok Barang - Tidak Mencukupi..!";
                            return false;
                        }
                        //data room check - item mutation
                        dataIn.setCreateDate(Timestamp.valueOf(LocalDateTime.now()));
                        dataIn.setTblEmployeeByCreateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                        dataIn.setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                        dataIn.setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                        dataIn.setRefRecordStatus(session.find(RefRecordStatus.class, 1));
                        session.saveOrUpdate(dataIn);
                    }
                }
                //data room check - item mutation (out)
                for (TblRoomCheckItemMutationHistory dataOut : dataOuts) {
                    //data item mutation
                    if (dataOut.getTblItemMutationHistory().getCreateDate() == null) {
                        dataOut.getTblItemMutationHistory().setMutationDate(Timestamp.valueOf(LocalDateTime.now()));
//                        dataOut.getTblItemMutationHistory().setTblEmployeeByIdemployee(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                        dataOut.getTblItemMutationHistory().setCreateDate(Timestamp.valueOf(LocalDateTime.now()));
                        dataOut.getTblItemMutationHistory().setTblEmployeeByCreateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                        dataOut.getTblItemMutationHistory().setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                        dataOut.getTblItemMutationHistory().setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                        dataOut.getTblItemMutationHistory().setRefRecordStatus(session.find(RefRecordStatus.class, 1));
                        session.saveOrUpdate(dataOut.getTblItemMutationHistory());
                        //data item location (update:stock_number)
                        TblItemLocation tempItemSourceLocation = getItemLocationByIDLocationAndIDItem(dataOut.getTblItemMutationHistory().getTblLocationByIdlocationOfSource().getIdlocation(),
                                dataOut.getTblItemMutationHistory().getTblItem().getIditem());
                        if (tempItemSourceLocation != null) {
                            if ((tempItemSourceLocation.getItemQuantity().subtract(dataOut.getTblItemMutationHistory().getItemQuantity()))
                                    .compareTo(new BigDecimal("0")) > -1) {
                                //data item location (source, udpate:minus)
                                tempItemSourceLocation.setItemQuantity(tempItemSourceLocation.getItemQuantity().subtract(dataOut.getTblItemMutationHistory().getItemQuantity()));
                                tempItemSourceLocation.setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                                tempItemSourceLocation.setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                                session.update(tempItemSourceLocation);
                                //data item location (destination, udpate:plus)
                                TblItemLocation tempItemDestinationLocation = getItemLocationByIDLocationAndIDItem(dataOut.getTblItemMutationHistory().getTblLocationByIdlocationOfDestination().getIdlocation(),
                                        dataOut.getTblItemMutationHistory().getTblItem().getIditem());
                                if (tempItemDestinationLocation != null) {    //update
                                    tempItemDestinationLocation.setItemQuantity(tempItemDestinationLocation.getItemQuantity().add(dataOut.getTblItemMutationHistory().getItemQuantity()));
                                    tempItemDestinationLocation.setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                                    tempItemDestinationLocation.setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                                    session.update(tempItemDestinationLocation);
                                } else {  //create
                                    tempItemDestinationLocation = new TblItemLocation();
                                    tempItemDestinationLocation.setTblItem(session.find(TblItem.class, dataOut.getTblItemMutationHistory().getTblItem().getIditem()));
                                    tempItemDestinationLocation.setTblLocation(session.find(TblLocation.class, dataOut.getTblItemMutationHistory().getTblLocationByIdlocationOfDestination().getIdlocation()));
                                    tempItemDestinationLocation.setItemQuantity(dataOut.getTblItemMutationHistory().getItemQuantity());
                                    tempItemDestinationLocation.setCreateDate(Timestamp.valueOf(LocalDateTime.now()));
                                    tempItemDestinationLocation.setTblEmployeeByCreateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                                    tempItemDestinationLocation.setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                                    tempItemDestinationLocation.setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                                    tempItemDestinationLocation.setRefRecordStatus(session.find(RefRecordStatus.class, 1));
                                    session.saveOrUpdate(tempItemDestinationLocation);
                                }
                                //data room type detail & current reservation date
                                TblReservationRoomTypeDetail dataReservationRoomTypeDetail;  //@ctive?
                                Date currentReservationDate;
                                //data reservation broken item (reservation - customer)
                                TblReservationBrokenItem reservationBrokenItem = null;
                                if (dataOut.getTblItemMutationHistory().getRefItemMutationType().getIdtype() == 2 //Rusak = '2'
                                        && dataOut.getTblItemMutationHistory().getRefItemObsoleteBy() != null
                                        && dataOut.getTblItemMutationHistory().getRefItemObsoleteBy().getIdobsoleteBy() == 0) { //Customer = '0'
                                    //prev
                                    dataReservationRoomTypeDetail = getPreviousRRTD(roomCheck.getTblRoom(), Date.valueOf((LocalDate.now()).minusDays(1)));
                                    //current
                                    if (dataReservationRoomTypeDetail == null) {
                                        dataReservationRoomTypeDetail = getCurrentRRTD(roomCheck.getTblRoom(), Date.valueOf(LocalDate.now()));
                                    }
                                    //check data reservation - room
                                    if (dataReservationRoomTypeDetail == null) {
                                        if (session.getTransaction().isActive()) {
                                            session.getTransaction().rollback();
                                        }
                                        errMsg = "Data Reservasi Kamar - Tidak Ditemukan..!";
                                        return false;
                                    }
//                                    //data room type detail
////                                if (Time.valueOf(LocalTime.now()).before(ClassDataHardcode.defaultCheckInTime)) { //before check in time (before)
//                                    currentReservationDate = Date.valueOf((LocalDate.of(roomCheck.getCheckDateTime().getYear() + 1900, roomCheck.getCheckDateTime().getMonth() + 1, roomCheck.getCheckDateTime().getDate()).minusDays(1)));
//                                    if ((new Time(roomCheck.getCheckDateTime().getHours(), roomCheck.getCheckDateTime().getMinutes(), 0)).before(defaultCheckInTime)) { //before check in time (before)
//                                        dataReservationRoomTypeDetail = getReservationRoomTypeDetail(roomCheck.getTblRoom().getIdroom(),
//                                                //                                            Date.valueOf((LocalDate.now()).minusDays(1)));
//                                                currentReservationDate);
//                                        if (dataReservationRoomTypeDetail == null) {
//                                            if (session.getTransaction().isActive()) {
//                                                session.getTransaction().rollback();
//                                            }
//                                            errMsg = "Data Reservasi Kamar - Tidak Mencukupi..!";
//                                            return false;
//                                        }
//                                        if (dataReservationRoomTypeDetail.getTblReservation().getRefReservationStatus().getIdstatus()
//                                                == 4) {   //reservation status : check out
//                                            if (session.getTransaction().isActive()) {
//                                                session.getTransaction().rollback();
//                                            }
//                                            errMsg = "Data Reservasi Kamar - Tidak Sesuai (status:checkout)..!";
//                                            return false;
//                                        }
//                                    } else {  //after check in time (current)
//                                        currentReservationDate = Date.valueOf(LocalDate.of(roomCheck.getCheckDateTime().getYear() + 1900, roomCheck.getCheckDateTime().getMonth() + 1, roomCheck.getCheckDateTime().getDate()));
//                                        dataReservationRoomTypeDetail = getReservationRoomTypeDetail(roomCheck.getTblRoom().getIdroom(),
//                                                //                                            Date.valueOf(LocalDate.now()));
//                                                currentReservationDate);
//                                        if (dataReservationRoomTypeDetail == null) {
//                                            //check (before)
//                                            dataReservationRoomTypeDetail = getReservationRoomTypeDetail(roomCheck.getTblRoom().getIdroom(),
//                                                    //                                                Date.valueOf((LocalDate.now()).minusDays(1)));
//                                                    Date.valueOf((LocalDate.of(roomCheck.getCheckDateTime().getYear() + 1900, roomCheck.getCheckDateTime().getMonth() + 1, roomCheck.getCheckDateTime().getDate()).minusDays(1))));
//                                        }
//                                        if (dataReservationRoomTypeDetail == null) {
//                                            if (session.getTransaction().isActive()) {
//                                                session.getTransaction().rollback();
//                                            }
//                                            errMsg = "Data Reservasi Kamar - Tidak Mencukupi..!";
//                                            return false;
//                                        }
//                                        if (dataReservationRoomTypeDetail.getTblReservation().getRefReservationStatus().getIdstatus()
//                                                == 4) {   //reservation status : check out
//                                            if (session.getTransaction().isActive()) {
//                                                session.getTransaction().rollback();
//                                            }
//                                            errMsg = "Data Reservasi Kamar - Tidak Sesuai (status:checkout)..!";
//                                            return false;
//                                        }
//                                    }
                                    //data reservation broken item
                                    reservationBrokenItem = new TblReservationBrokenItem();
                                    reservationBrokenItem.setTblReservationRoomTypeDetail(dataReservationRoomTypeDetail);
                                    reservationBrokenItem.setTblItem(dataOut.getTblItemMutationHistory().getTblItem());
                                    reservationBrokenItem.setItemCharge(dataOut.getTblItemMutationHistory().getTblItem().getBrokenCharge());
                                    reservationBrokenItem.setItemQuantity(dataOut.getTblItemMutationHistory().getItemQuantity());
                                    reservationBrokenItem.setCreateDate(Timestamp.valueOf(LocalDateTime.now()));
                                    reservationBrokenItem.setTblEmployeeByCreateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                                    reservationBrokenItem.setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                                    reservationBrokenItem.setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                                    reservationBrokenItem.setRefRecordStatus(session.find(RefRecordStatus.class, 1));
                                    session.saveOrUpdate(reservationBrokenItem);
                                }
                                //data item mutation - property barcode
                                if (dataOut.getTblItemMutationHistory().getTblItem().getPropertyStatus()) {    //Property
                                    for (TblItemMutationHistoryPropertyBarcode itemMutationHistoryPropertyBarcode : itemMutationHistoryPropertyBarcodes) {
                                        if (dataOut.getTblItemMutationHistory().equals(itemMutationHistoryPropertyBarcode.getTblItemMutationHistory())) {
                                            itemMutationHistoryPropertyBarcode.setCreateDate(Timestamp.valueOf(LocalDateTime.now()));
                                            itemMutationHistoryPropertyBarcode.setTblEmployeeByCreateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                                            itemMutationHistoryPropertyBarcode.setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                                            itemMutationHistoryPropertyBarcode.setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                                            itemMutationHistoryPropertyBarcode.setRefRecordStatus(session.find(RefRecordStatus.class, 1));
                                            session.saveOrUpdate(itemMutationHistoryPropertyBarcode);
                                            //data item location property barcode (update:relation)
                                            TblItemLocationPropertyBarcode tempItemSourceLocationBarcode = getItemLocationPropertyBarcodeByIDItemLocationAndIDPropertyBarcode(tempItemSourceLocation.getIdrelation(),
                                                    itemMutationHistoryPropertyBarcode.getTblPropertyBarcode().getIdbarcode());
                                            if (tempItemSourceLocationBarcode != null) {
                                                //data item location property barcode (source, udpate:delete)
                                                tempItemSourceLocationBarcode.setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                                                tempItemSourceLocationBarcode.setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                                                tempItemSourceLocationBarcode.setRefRecordStatus(session.find(RefRecordStatus.class, 2));
                                                session.update(tempItemSourceLocationBarcode);
                                                //data item location property barcode (destination, update:insert)
                                                TblItemLocationPropertyBarcode tempItemDestinationLocationPropertyBarcode = new TblItemLocationPropertyBarcode();
                                                tempItemDestinationLocationPropertyBarcode.setTblItemLocation(session.find(TblItemLocation.class, tempItemDestinationLocation.getIdrelation()));
                                                tempItemDestinationLocationPropertyBarcode.setTblPropertyBarcode(session.find(TblPropertyBarcode.class, itemMutationHistoryPropertyBarcode.getTblPropertyBarcode().getIdbarcode()));
                                                tempItemDestinationLocationPropertyBarcode.setCreateDate(Timestamp.valueOf(LocalDateTime.now()));
                                                tempItemDestinationLocationPropertyBarcode.setTblEmployeeByCreateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                                                tempItemDestinationLocationPropertyBarcode.setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                                                tempItemDestinationLocationPropertyBarcode.setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                                                tempItemDestinationLocationPropertyBarcode.setRefRecordStatus(session.find(RefRecordStatus.class, 1));
                                                session.saveOrUpdate(tempItemDestinationLocationPropertyBarcode);
                                            } else {  //failed
                                                if (session.getTransaction().isActive()) {
                                                    session.getTransaction().rollback();
                                                }
                                                errMsg = "Data Barang - Tidak Ditemukan..!";
                                                return false;
                                            }
                                            //data reservation broken item property barcode
                                            if (reservationBrokenItem != null) {  //if broken with customer
                                                TblReservationBrokenItemPropertyBarcode reservationBrokenItemPropertyBarcode = new TblReservationBrokenItemPropertyBarcode();
                                                reservationBrokenItemPropertyBarcode.setTblReservationBrokenItem(reservationBrokenItem);
                                                reservationBrokenItemPropertyBarcode.setTblPropertyBarcode(session.find(TblPropertyBarcode.class, itemMutationHistoryPropertyBarcode.getTblPropertyBarcode().getIdbarcode()));
                                                reservationBrokenItemPropertyBarcode.setCreateDate(Timestamp.valueOf(LocalDateTime.now()));
                                                reservationBrokenItemPropertyBarcode.setTblEmployeeByCreateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                                                reservationBrokenItemPropertyBarcode.setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                                                reservationBrokenItemPropertyBarcode.setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                                                reservationBrokenItemPropertyBarcode.setRefRecordStatus(session.find(RefRecordStatus.class, 1));
                                                session.saveOrUpdate(reservationBrokenItemPropertyBarcode);
                                            }
                                            break;
                                        }
//                                        //data reservation additional item - resereved (updating, just for 'asset')
//                                        if (dataReservationRoomTypeDetail != null
//                                                && currentReservationDate != null) {
//                                            TblReservationAdditionalItemReserved reservationAdditionalItemReserved = getReservationAdditionalItemReservedByIDReservationRoomTypeDetailAndIDItemAndReservedDate(
//                                                    dataReservationRoomTypeDetail.getIddetail(),
//                                                    dataOut.getTblItemMutationHistory().getTblItem().getIditem(),
//                                                    currentReservationDate);
//                                            if (reservationAdditionalItemReserved != null
//                                                    && reservationAdditionalItemReserved.getRefReservationAdditionalItemReservedStatus().getIdstatus() == 1) {  //Reserved = '1'
//                                                if ((dataOut.getTblItemMutationHistory().getItemQuantity().add(reservationAdditionalItemReserved.getDoneQuantity()))
//                                                        .compareTo(reservationAdditionalItemReserved.getReservedQuantity()) > -1) {
//                                                    reservationAdditionalItemReserved.setDoneQuantity(reservationAdditionalItemReserved.getReservedQuantity());
//                                                    reservationAdditionalItemReserved.setRefReservationAdditionalItemReservedStatus(session.find(RefReservationAdditionalItemReservedStatus.class, 2)); //Done = '2'
//                                                    reservationAdditionalItemReserved.setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
//                                                    reservationAdditionalItemReserved.setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
//                                                    session.update(reservationAdditionalItemReserved);
//                                                } else {
//                                                    reservationAdditionalItemReserved.setDoneQuantity(reservationAdditionalItemReserved.getDoneQuantity().add(dataOut.getTblItemMutationHistory().getItemQuantity()));
//                                                    reservationAdditionalItemReserved.setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
//                                                    reservationAdditionalItemReserved.setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
//                                                    session.update(reservationAdditionalItemReserved);
//                                                }
//                                            }
//                                        }
                                    }
                                }
                                //data item mutation - item expired date
                                if (dataOut.getTblItemMutationHistory().getTblItem().getConsumable()) {    //Consumable
                                    for (TblItemMutationHistoryItemExpiredDate itemMutationHistoryItemExpiredDate : itemMutationHistoryItemExpiredDates) {
                                        if (dataOut.getTblItemMutationHistory().equals(itemMutationHistoryItemExpiredDate.getTblItemMutationHistory())) {
                                            itemMutationHistoryItemExpiredDate.setItemQuantity(dataOut.getTblItemMutationHistory().getItemQuantity());
                                            itemMutationHistoryItemExpiredDate.setCreateDate(Timestamp.valueOf(LocalDateTime.now()));
                                            itemMutationHistoryItemExpiredDate.setTblEmployeeByCreateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                                            itemMutationHistoryItemExpiredDate.setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                                            itemMutationHistoryItemExpiredDate.setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                                            itemMutationHistoryItemExpiredDate.setRefRecordStatus(session.find(RefRecordStatus.class, 1));
                                            session.saveOrUpdate(itemMutationHistoryItemExpiredDate);
                                            //data item location item expired date (update:relation)
                                            TblItemLocationItemExpiredDate tempItemSourceLocationExpiredDate = getItemLocationItemExpiredDateByIDItemLocationAndIDItemExpiredDate(
                                                    tempItemSourceLocation.getIdrelation(),
                                                    itemMutationHistoryItemExpiredDate.getTblItemExpiredDate().getIditemExpiredDate());
                                            if (tempItemSourceLocationExpiredDate != null) {
                                                if ((tempItemSourceLocationExpiredDate.getItemQuantity().subtract(itemMutationHistoryItemExpiredDate.getItemQuantity()))
                                                        .compareTo(new BigDecimal("0")) > -1) {
                                                    //data item location item exprired date (source, udpate:-)
                                                    tempItemSourceLocationExpiredDate.setItemQuantity(tempItemSourceLocationExpiredDate.getItemQuantity().subtract(itemMutationHistoryItemExpiredDate.getItemQuantity()));
                                                    tempItemSourceLocationExpiredDate.setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                                                    tempItemSourceLocationExpiredDate.setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                                                    session.update(tempItemSourceLocationExpiredDate);
                                                    //data item location item expired date (destination, update:+)
                                                    TblItemLocationItemExpiredDate tempItemDestinationLocationExpiredDate = getItemLocationItemExpiredDateByIDItemLocationAndIDItemExpiredDate(
                                                            tempItemDestinationLocation.getIdrelation(),
                                                            itemMutationHistoryItemExpiredDate.getTblItemExpiredDate().getIditemExpiredDate());
                                                    if (tempItemDestinationLocationExpiredDate == null) {
                                                        tempItemDestinationLocationExpiredDate = new TblItemLocationItemExpiredDate();
                                                        tempItemDestinationLocationExpiredDate.setTblItemLocation(session.find(TblItemLocation.class, tempItemDestinationLocation.getIdrelation()));
                                                        tempItemDestinationLocationExpiredDate.setTblItemExpiredDate(session.find(TblItemExpiredDate.class, itemMutationHistoryItemExpiredDate.getTblItemExpiredDate().getIditemExpiredDate()));
                                                        tempItemDestinationLocationExpiredDate.setItemQuantity(new BigDecimal("0"));
                                                        tempItemDestinationLocationExpiredDate.setCreateDate(Timestamp.valueOf(LocalDateTime.now()));
                                                        tempItemDestinationLocationExpiredDate.setTblEmployeeByCreateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                                                        tempItemDestinationLocationExpiredDate.setRefRecordStatus(session.find(RefRecordStatus.class, 1));
                                                    }
                                                    tempItemDestinationLocationExpiredDate.setItemQuantity(tempItemDestinationLocationExpiredDate.getItemQuantity().add(itemMutationHistoryItemExpiredDate.getItemQuantity()));
                                                    tempItemDestinationLocationExpiredDate.setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                                                    tempItemDestinationLocationExpiredDate.setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                                                    session.saveOrUpdate(tempItemDestinationLocationExpiredDate);
                                                } else {  //failed
                                                    if (session.getTransaction().isActive()) {
                                                        session.getTransaction().rollback();
                                                    }
                                                    errMsg = "Stok Barang - Tidak Mencukupi..!";
                                                    return false;
                                                }
                                            } else {  //failed
                                                if (session.getTransaction().isActive()) {
                                                    session.getTransaction().rollback();
                                                }
                                                errMsg = "Data Barang - Tidak Ditemukan..!";
                                                return false;
                                            }
                                            break;
                                        }
                                    }
                                }
                            } else {  //failed
                                if (session.getTransaction().isActive()) {
                                    session.getTransaction().rollback();
                                }
                                errMsg = "Stok Barang - Tidak Mencukupi..!";
                                return false;
                            }
                        } else {  //failed
                            if (session.getTransaction().isActive()) {
                                session.getTransaction().rollback();
                            }
                            errMsg = "Stok Item - Tidak Mencukupi..!";
                            return false;
                        }
                        //data room check - item mutation
                        dataOut.setCreateDate(Timestamp.valueOf(LocalDateTime.now()));
                        dataOut.setTblEmployeeByCreateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                        dataOut.setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                        dataOut.setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                        dataOut.setRefRecordStatus(session.find(RefRecordStatus.class, 1));
                        session.saveOrUpdate(dataOut);
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

    private TblReservationRoomTypeDetail getPreviousRRTD(
            TblRoom dataRoom,
            Date date) {
        if (dataRoom != null
                && date != null) {
            List<TblReservationRoomPriceDetail> dataReservationRoomPriceDetails = getAllReservationRoomPriceDetailByDetailDate(date);
            for (TblReservationRoomPriceDetail dataReservationRoomPriceDetail : dataReservationRoomPriceDetails) {
                TblReservationRoomTypeDetailRoomPriceDetail dataRelation = getReservationRoomTypeDetailRoomPriceDetailByIDRoomPriceDetailX(dataReservationRoomPriceDetail.getIddetail());
                if (dataRelation != null
                        && dataRelation.getTblReservationRoomTypeDetail() != null) {
                    TblReservationRoomTypeDetail rrtd = session.find(TblReservationRoomTypeDetail.class, dataRelation.getTblReservationRoomTypeDetail().getIddetail());
                    if (rrtd != null
                            && rrtd.getTblReservationCheckInOut() != null
                            && rrtd.getTblReservationCheckInOut().getTblRoom() != null
                            && rrtd.getTblReservationCheckInOut().getRefReservationCheckInOutStatus() != null) {
//                        //data checkin-out
//                        rrtd.setTblReservationCheckInOut(parentController.getFRoomCheckManager().getDataReservationCheckInOut(rrtd.getTblReservationCheckInOut().getIdcheckInOut()));
//                        //data room
//                        rrtd.getTblReservationCheckInOut().setTblRoom(parentController.getFRoomCheckManager().getDataRoom(rrtd.getTblReservationCheckInOut().getTblRoom().getIdroom()));
//                        //data checkin/out - status
//                        rrtd.getTblReservationCheckInOut().setRefReservationCheckInOutStatus(parentController.getFRoomCheckManager().getDataReservationCheckInOutStatus(rrtd.getTblReservationCheckInOut().getRefReservationCheckInOutStatus().getIdstatus()));
                        if (rrtd.getTblReservationCheckInOut().getTblRoom().getIdroom() == dataRoom.getIdroom()
                                && (rrtd.getTblReservationCheckInOut().getRefReservationCheckInOutStatus().getIdstatus() == 1 //Checked In = '1'
                                || rrtd.getTblReservationCheckInOut().getRefReservationCheckInOutStatus().getIdstatus() == 2)) {  //Ready to Check Out = '2'
//                            //data reservation
//                            rrtd.setTblReservation(parentController.getFRoomCheckManager().getDataReservation(rrtd.getTblReservation().getIdreservation()));
                            if (rrtd.getTblReservation().getRefReservationStatus().getIdstatus() == 1 //Reserved = '1'
                                    || rrtd.getTblReservation().getRefReservationStatus().getIdstatus() == 2) {  //Booked = '2'                                                                
                                return rrtd;
                            }
                        }
                    }
                }
            }
        }
        return null;
    }

    private TblReservationRoomTypeDetail getCurrentRRTD(
            TblRoom dataRoom,
            Date date) {
        if (dataRoom != null
                && date != null) {
            List<TblReservationRoomPriceDetail> dataReservationRoomPriceDetails = getAllReservationRoomPriceDetailByDetailDate(date);
            for (TblReservationRoomPriceDetail dataReservationRoomPriceDetail : dataReservationRoomPriceDetails) {
                TblReservationRoomTypeDetailRoomPriceDetail dataRelation = getReservationRoomTypeDetailRoomPriceDetailByIDRoomPriceDetailX(dataReservationRoomPriceDetail.getIddetail());
                if (dataRelation != null
                        && dataRelation.getTblReservationRoomTypeDetail() != null) {
                    TblReservationRoomTypeDetail rrtd = session.find(TblReservationRoomTypeDetail.class, dataRelation.getTblReservationRoomTypeDetail().getIddetail());
                    if (rrtd != null
                            && rrtd.getTblReservationCheckInOut() != null
                            && rrtd.getTblReservationCheckInOut().getTblRoom() != null
                            && rrtd.getTblReservationCheckInOut().getRefReservationCheckInOutStatus() != null) {
//                        //data checkin-out
//                        rrtd.setTblReservationCheckInOut(parentController.getFRoomCheckManager().getDataReservationCheckInOut(rrtd.getTblReservationCheckInOut().getIdcheckInOut()));
//                        //data room
//                        rrtd.getTblReservationCheckInOut().setTblRoom(parentController.getFRoomCheckManager().getDataRoom(rrtd.getTblReservationCheckInOut().getTblRoom().getIdroom()));
//                        //data checkin/out - status
//                        rrtd.getTblReservationCheckInOut().setRefReservationCheckInOutStatus(parentController.getFRoomCheckManager().getDataReservationCheckInOutStatus(rrtd.getTblReservationCheckInOut().getRefReservationCheckInOutStatus().getIdstatus()));
                        if (rrtd.getTblReservationCheckInOut().getTblRoom().getIdroom() == dataRoom.getIdroom()
                                && (rrtd.getTblReservationCheckInOut().getRefReservationCheckInOutStatus().getIdstatus() == 1 //Checked In = '1'
                                || rrtd.getTblReservationCheckInOut().getRefReservationCheckInOutStatus().getIdstatus() == 0)) {  //Ready to Check In = '0'
//                            //data reservation
//                            rrtd.setTblReservation(parentController.getFRoomCheckManager().getDataReservation(rrtd.getTblReservation().getIdreservation()));
//                            //data reservation - status
//                            rrtd.getTblReservation().setRefReservationStatus(parentController.getFRoomCheckManager().getDataReservationStatus(rrtd.getTblReservation().getRefReservationStatus().getIdstatus()));
                            if (rrtd.getTblReservation().getRefReservationStatus().getIdstatus() == 1 //Reserved = '1'
                                    || rrtd.getTblReservation().getRefReservationStatus().getIdstatus() == 2) {  //Booked = '2'                                
                                return rrtd;
                            }
                        }
                    }
                }
            }
        }
        return null;
    }

    public List<TblReservationRoomPriceDetail> getAllReservationRoomPriceDetailByDetailDate(Date date) {
        List<TblReservationRoomPriceDetail> list = session.getNamedQuery("findAllTblRoomPriceDetailByDetailDate")
                .setParameter("detailDate", date)
                .list();
        return list;
    }

    public TblReservationRoomTypeDetailRoomPriceDetail getReservationRoomTypeDetailRoomPriceDetailByIDRoomPriceDetailX(long idRoomPriceDetail) {
        List<TblReservationRoomTypeDetailRoomPriceDetail> list = session.getNamedQuery("findAllTblReservationRoomTypeDetailRoomPriceDetailByIDRoomPriceDetail")
                .setParameter("idRoomPriceDetail", idRoomPriceDetail)
                .list();
        return list.isEmpty() ? null : list.get(0);
    }

    private TblItemLocationItemExpiredDate getItemLocationItemExpiredDateByIDItemLocationAndIDItemExpiredDate(
            long idItemLocation,
            long idItemExpiredDate) {
        List<TblItemLocationItemExpiredDate> list = session.getNamedQuery("findAllTblItemLocationItemExpiredDateByIDItemLocationAndIDItemExpiredDate")
                .setParameter("idItemLocation", idItemLocation)
                .setParameter("idItemExpiredDate", idItemExpiredDate)
                .list();
        return list.isEmpty() ? null : list.get(0);
    }

    @Override
    public boolean deleteDataRoomCheck(TblRoomCheck roomCheck) {
        errMsg = "";
        if (ClassSession.checkUserSession()) {  //check user current session
            try {
                session = HibernateUtil.getSessionFactory().getCurrentSession();
                session.beginTransaction();
                if (roomCheck.getRefRecordStatus().getIdstatus() == 1) {
                    roomCheck.setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                    roomCheck.setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                    roomCheck.setRefRecordStatus(session.find(RefRecordStatus.class, 2));
                    session.update(roomCheck);
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
    public TblRoomCheck getDataRoomCheck(long id) {
        errMsg = "";
        TblRoomCheck data = null;
        if (ClassSession.checkUserSession()) {  //check user current session
            try {
                session = HibernateUtil.getSessionFactory().getCurrentSession();
                session.beginTransaction();
                data = (TblRoomCheck) session.find(TblRoomCheck.class, id);
                session.getTransaction().commit();
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
    public List<TblRoomCheck> getAllDataRoomCheck() {
        errMsg = "";
        List<TblRoomCheck> list = new ArrayList<>();
        if (ClassSession.checkUserSession()) {  //check user current session
            try {
                session = HibernateUtil.getSessionFactory().getCurrentSession();
                session.beginTransaction();
                list = session.getNamedQuery("findAllTblRoomCheck").list();
                session.getTransaction().commit();
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
    public TblRoom getDataRoom(long id) {
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
    public RefRoomStatus getDataRoomStatus(int id) {
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
    public List<RefRoomStatus> getAllDataRoomStatus() {
        errMsg = "";
        List<RefRoomStatus> list = new ArrayList<>();
        if (ClassSession.checkUserSession()) {  //check user current session
            try {
                session = HibernateUtil.getSessionFactory().getCurrentSession();
                session.beginTransaction();
                list = session.getNamedQuery("findAllRefRoomStatus").list();
                session.getTransaction().commit();
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
    public RefRoomCleanStatus getDataRoomCleanStatus(int id) {
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

    //--------------------------------------------------------------------------
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

    @Override
    public TblLocationOfBin getDataBin() {
        errMsg = "";
        List<TblLocationOfBin> list = new ArrayList<>();
        if (ClassSession.checkUserSession()) {  //check user current session
            try {
                session = HibernateUtil.getSessionFactory().getCurrentSession();
                session.beginTransaction();
                list = session.getNamedQuery("findAllTblLocationOfBin")
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
    public List<TblRoomCheckItemMutationHistory> getAllDataRoomCheckItemMutationHistoryByIDRoomCheck(long idRoomCheck) {
        errMsg = "";
        List<TblRoomCheckItemMutationHistory> list = new ArrayList<>();
        if (ClassSession.checkUserSession()) {  //check user current session
            try {
                session = HibernateUtil.getSessionFactory().getCurrentSession();
                session.beginTransaction();
                list = session.getNamedQuery("findAllTblRoomCheckItemMutationHistoryByIDRoomCheck")
                        .setParameter("idRoomCheck", idRoomCheck)
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
    public TblItemMutationHistory getDataItemMutationHistory(long id) {
        errMsg = "";
        TblItemMutationHistory data = null;
        if (ClassSession.checkUserSession()) {  //check user current session
            try {
                session = HibernateUtil.getSessionFactory().getCurrentSession();
                session.beginTransaction();
                data = (TblItemMutationHistory) session.find(TblItemMutationHistory.class, id);
                session.getTransaction().commit();
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
                list = session.getNamedQuery("findAllTblItem").list();
                session.getTransaction().commit();
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
    public TblBuilding getDataBuilding(long id) {
        errMsg = "";
        TblBuilding data = null;
        if (ClassSession.checkUserSession()) {  //check user current session
            try {
                session = HibernateUtil.getSessionFactory().getCurrentSession();
                session.beginTransaction();
                data = (TblBuilding) session.find(TblBuilding.class, id);
                session.getTransaction().commit();
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
    public RefItemMutationType getDataMutationType(int id) {
        errMsg = "";
        RefItemMutationType data = null;
        if (ClassSession.checkUserSession()) {  //check user current session
            try {
                session = HibernateUtil.getSessionFactory().getCurrentSession();
                session.beginTransaction();
                data = (RefItemMutationType) session.find(RefItemMutationType.class, id);
                session.getTransaction().commit();
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
    public RefItemObsoleteBy getDataObsoleteBy(int id) {
        errMsg = "";
        RefItemObsoleteBy data = null;
        if (ClassSession.checkUserSession()) {  //check user current session
            try {
                session = HibernateUtil.getSessionFactory().getCurrentSession();
                session.beginTransaction();
                data = (RefItemObsoleteBy) session.find(RefItemObsoleteBy.class, id);
                session.getTransaction().commit();
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
    public List<RefLocationType> getAllDataLocationType() {
        errMsg = "";
        List<RefLocationType> list = new ArrayList<>();
        if (ClassSession.checkUserSession()) {  //check user current session
            try {
                session = HibernateUtil.getSessionFactory().getCurrentSession();
                session.beginTransaction();
                list = session.getNamedQuery("findAllRefLocationType").list();
                session.getTransaction().commit();
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
    public List<TblLocationOfWarehouse> getAllDataWarehouse() {
        errMsg = "";
        List<TblLocationOfWarehouse> list = new ArrayList<>();
        if (ClassSession.checkUserSession()) {  //check user current session
            try {
                session = HibernateUtil.getSessionFactory().getCurrentSession();
                session.beginTransaction();
                list = session.getNamedQuery("findAllTblLocationOfWarehouse").list();
                session.getTransaction().commit();
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
    public List<TblLocationOfLaundry> getAllDataLaundry() {
        errMsg = "";
        List<TblLocationOfLaundry> list = new ArrayList<>();
        if (ClassSession.checkUserSession()) {  //check user current session
            try {
                session = HibernateUtil.getSessionFactory().getCurrentSession();
                session.beginTransaction();
                list = session.getNamedQuery("findAllTblLocationOfLaundry").list();
                session.getTransaction().commit();
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
    public List<TblLocationOfBin> getAllDataBin() {
        errMsg = "";
        List<TblLocationOfBin> list = new ArrayList<>();
        if (ClassSession.checkUserSession()) {  //check user current session
            try {
                session = HibernateUtil.getSessionFactory().getCurrentSession();
                session.beginTransaction();
                list = session.getNamedQuery("findAllTblLocationOfBin").list();
                session.getTransaction().commit();
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
    public List<TblItemLocation> getAllDataItemLocationByIDLocation(long idLocation) {
        errMsg = "";
        List<TblItemLocation> list = new ArrayList<>();
        if (ClassSession.checkUserSession()) {  //check user current session
            try {
                session = HibernateUtil.getSessionFactory().getCurrentSession();
                session.beginTransaction();
                list = session.getNamedQuery("findAllTblItemLocationByIdLocation")
                        .setParameter("idLocation", idLocation)
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
    public TblPropertyBarcode getDataPropertyBarcode(long id) {
        errMsg = "";
        TblPropertyBarcode data = null;
        if (ClassSession.checkUserSession()) {  //check user current session
            try {
                session = HibernateUtil.getSessionFactory().getCurrentSession();
                session.beginTransaction();
                data = (TblPropertyBarcode) session.find(TblPropertyBarcode.class, id);
                session.getTransaction().commit();
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
    public TblFixedTangibleAsset getDataFixedTangibleAsset(long id) {
        errMsg = "";
        TblFixedTangibleAsset data = null;
        if (ClassSession.checkUserSession()) {  //check user current session
            try {
                session = HibernateUtil.getSessionFactory().getCurrentSession();
                session.beginTransaction();
                data = (TblFixedTangibleAsset) session.find(TblFixedTangibleAsset.class, id);
                session.getTransaction().commit();
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
    public TblItemLocation getDataItemLocationByIDLocationAndIDItem(
            long idLocation,
            long idItem) {
        errMsg = "";
        List<TblItemLocation> list = new ArrayList<>();
        if (ClassSession.checkUserSession()) {  //check user current session
            try {
                session = HibernateUtil.getSessionFactory().getCurrentSession();
                session.beginTransaction();
                list = session.getNamedQuery("findAllItemLocationByIdLocationAndIdItem")
                        .setParameter("idLocation", idLocation)
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
        return list.isEmpty() ? null : list.get(0);
    }

    @Override
    public List<TblItemLocation> getAllDataItemLocationByIDLocationTypeAndItemQuantityNotZero(int idLocationType) {
        errMsg = "";
        List<TblItemLocation> list = new ArrayList<>();
        if (ClassSession.checkUserSession()) {  //check user current session
            try {
                session = HibernateUtil.getSessionFactory().getCurrentSession();
                session.beginTransaction();
                list = session.getNamedQuery("findAllTblItemLocationByIDLocationType")
                        .setParameter("idLocationType", idLocationType)
                        .list();
                //remove zero value
                for (int i = list.size() - 1; i > -1; i--) {
                    if (list.get(i).getItemQuantity().compareTo(new BigDecimal("0")) < 1) {
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

    @Override
    public List<TblItemLocationPropertyBarcode> getAllDataItemLocationPropertyBarcodeByIDItemLocation(long idItemLocation) {
        errMsg = "";
        List<TblItemLocationPropertyBarcode> list = new ArrayList<>();
        if (ClassSession.checkUserSession()) {  //check user current session
            try {
                session = HibernateUtil.getSessionFactory().getCurrentSession();
                session.beginTransaction();
                list = session.getNamedQuery("findAllTblItemLocationPropertyBarcodeByIdItemLocation")
                        .setParameter("idItemLocation", idItemLocation)
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
    public List<TblItemMutationHistoryPropertyBarcode> getAllDataItemMutationHistoryPropertyBarcodeByIDItemMutationHistory(long idItemMutationHistory) {
        errMsg = "";
        List<TblItemMutationHistoryPropertyBarcode> list = new ArrayList<>();
        if (ClassSession.checkUserSession()) {  //check user current session
            try {
                session = HibernateUtil.getSessionFactory().getCurrentSession();
                session.beginTransaction();
                list = session.getNamedQuery("findAllTblItemMutationHistoryPropertyBarcodeByIdItemMutationHistory")
                        .setParameter("idItemMutationHistory", idItemMutationHistory)
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
    public List<RefItemMutationType> getAllDataItemMutationType() {
        errMsg = "";
        List<RefItemMutationType> list = new ArrayList<>();
        if (ClassSession.checkUserSession()) {  //check user current session
            try {
                session = HibernateUtil.getSessionFactory().getCurrentSession();
                session.beginTransaction();
                list = session.getNamedQuery("findAllRefItemMutationType").list();
                session.getTransaction().commit();
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
    public List<RefItemObsoleteBy> getAllDataItemObsoleteBy() {
        errMsg = "";
        List<RefItemObsoleteBy> list = new ArrayList<>();
        if (ClassSession.checkUserSession()) {  //check user current session
            try {
                session = HibernateUtil.getSessionFactory().getCurrentSession();
                session.beginTransaction();
                list = session.getNamedQuery("findAllRefItemObsoleteBy").list();
                session.getTransaction().commit();
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
    public TblItemMutationHistoryItemExpiredDate getDataItemMutationHistoryItemExpiredDate(long id) {
        errMsg = "";
        TblItemMutationHistoryItemExpiredDate data = null;
        if (ClassSession.checkUserSession()) {  //check user current session
            try {
                session = HibernateUtil.getSessionFactory().getCurrentSession();
                session.beginTransaction();
                data = (TblItemMutationHistoryItemExpiredDate) session.find(TblItemMutationHistoryItemExpiredDate.class, id);
                session.getTransaction().commit();
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
    public List<TblItemMutationHistoryItemExpiredDate> getAllDataItemMutationHistoryItemExpiredDate() {
        errMsg = "";
        List<TblItemMutationHistoryItemExpiredDate> list = new ArrayList<>();
        if (ClassSession.checkUserSession()) {  //check user current session
            try {
                session = HibernateUtil.getSessionFactory().getCurrentSession();
                session.beginTransaction();
                list = session.getNamedQuery("findAllTblItemMutationHistoryItemExpiredDate").list();
                session.getTransaction().commit();
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
    public List<TblItemLocationItemExpiredDate> getAllDataItemLocationItemExpiredDateByIDItemLocation(long idItemLocation) {
        errMsg = "";
        List<TblItemLocationItemExpiredDate> list = new ArrayList<>();
        if (ClassSession.checkUserSession()) {  //check user current session
            try {
                session = HibernateUtil.getSessionFactory().getCurrentSession();
                session.beginTransaction();
                list = session.getNamedQuery("findAllTblItemLocationItemExpiredDateByIdItemLocation")
                        .setParameter("idItemLocation", idItemLocation)
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
    public List<TblItemMutationHistoryItemExpiredDate> getAllDataItemMutationHistoryItemExpiredDateByIDItemMutationHistory(long idItemMutationHistory) {
        errMsg = "";
        List<TblItemMutationHistoryItemExpiredDate> list = new ArrayList<>();
        if (ClassSession.checkUserSession()) {  //check user current session
            try {
                session = HibernateUtil.getSessionFactory().getCurrentSession();
                session.beginTransaction();
                list = session.getNamedQuery("findAllTblItemMutationHistoryItemExpiredDateByIDItemMutationHistory")
                        .setParameter("idMutation", idItemMutationHistory)
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
    public TblItemExpiredDate getDataItemExpiredDate(long id) {
        errMsg = "";
        TblItemExpiredDate data = null;
        if (ClassSession.checkUserSession()) {  //check user current session
            try {
                session = HibernateUtil.getSessionFactory().getCurrentSession();
                session.beginTransaction();
                data = (TblItemExpiredDate) session.find(TblItemExpiredDate.class, id);
                session.getTransaction().commit();
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
    public List<TblItemExpiredDate> getAllDataItemExpiredDate() {
        errMsg = "";
        List<TblItemExpiredDate> list = new ArrayList<>();
        if (ClassSession.checkUserSession()) {  //check user current session
            try {
                session = HibernateUtil.getSessionFactory().getCurrentSession();
                session.beginTransaction();
                list = session.getNamedQuery("findAllTblItemExpiredDate").list();
                session.getTransaction().commit();
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
    public List<TblItemExpiredDate> getAllDataItemExpiredDateByIDItem(long idItem) {
        errMsg = "";
        List<TblItemExpiredDate> list = new ArrayList<>();
        if (ClassSession.checkUserSession()) {  //check user current session
            try {
                session = HibernateUtil.getSessionFactory().getCurrentSession();
                session.beginTransaction();
                list = session.getNamedQuery("findAllTblItemExpiredDateByIDItem")
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

    //--------------------------------------------------------------------------
    @Override
    public boolean updateDataRoomStatus(TblRoom room) {
        errMsg = "";
        if (ClassSession.checkUserSession()) {  //check user current session
            try {
                session = HibernateUtil.getSessionFactory().getCurrentSession();
                session.beginTransaction();
                //data room status change history
                TblRoomStatusChangeHistory dataRoomStatusChangeHistory = new TblRoomStatusChangeHistory();
                dataRoomStatusChangeHistory.setTblRoom(room);
                dataRoomStatusChangeHistory.setTblEmployeeByIdemployee(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                dataRoomStatusChangeHistory.setTblRoomStatusDetail(null);
                dataRoomStatusChangeHistory.setChangeRoomStatusDate(Timestamp.valueOf(LocalDateTime.now()));
                dataRoomStatusChangeHistory.setRefRoomStatusByRoomStatusOld(getOldRoomStatus(room.getIdroom()));
                dataRoomStatusChangeHistory.setRefRoomStatusByRoomStatusNew(room.getRefRoomStatus());
                dataRoomStatusChangeHistory.setChangeRoomStatusNote(room.getRoomStatusNote());
                dataRoomStatusChangeHistory.setCreateDate(Timestamp.valueOf(LocalDateTime.now()));
                dataRoomStatusChangeHistory.setTblEmployeeByCreateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                dataRoomStatusChangeHistory.setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                dataRoomStatusChangeHistory.setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                dataRoomStatusChangeHistory.setRefRecordStatus(session.find(RefRecordStatus.class, 1));
                session.saveOrUpdate(dataRoomStatusChangeHistory);
                //data room check - house keeping attendance
                TblRoomCheckHouseKeepingAttendance rchka = getRoomCheckHouseKeepingAttendanceByIDRoomAndIDRoomStatusAfterIsNull(room.getIdroom());
                if (rchka != null) {
                    rchka.setRefRoomStatusByRoomStatusAfter(room.getRefRoomStatus());
                    rchka.setLastUpdatedDate(Timestamp.valueOf(LocalDateTime.now()));
                    rchka.setTblEmployeeByLastUpdatedBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                    session.update(rchka);
                }
                //data room
                room.setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                room.setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                session.update(room);
                session.getTransaction().commit();
            } catch (Exception e) {
                if (session.getTransaction().isActive()) {
                    session.getTransaction().rollback();
                }
                errMsg = e.getMessage();
                return false;
            }
        } else {
            return false;
        }
        return true;
    }

    public TblRoomCheckHouseKeepingAttendance getRoomCheckHouseKeepingAttendanceByIDRoomAndIDRoomStatusAfterIsNull(long idRoom) {
        List<TblRoomCheckHouseKeepingAttendance> list = session.getNamedQuery("findAllTblRoomCheckHouseKeepingAttendanceByIDRoomAndIDRoomStatusAfterIsNull")
                .setParameter("idRoom", idRoom)
                .list();
        return list.isEmpty() ? null : list.get(0);
    }

    @Override
    public boolean updateDataRoomStatusDetail(TblRoom room,
            TblRoomStatusDetail roomStatusDetail) {
        errMsg = "";
        if (ClassSession.checkUserSession()) {  //check user current session
            try {
                session = HibernateUtil.getSessionFactory().getCurrentSession();
                session.beginTransaction();
                //data room status change history
                TblRoomStatusChangeHistory dataRoomStatusChangeHistory = new TblRoomStatusChangeHistory();
                dataRoomStatusChangeHistory.setTblRoom(room);
                dataRoomStatusChangeHistory.setTblEmployeeByIdemployee(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                dataRoomStatusChangeHistory.setTblRoomStatusDetail(roomStatusDetail);
                dataRoomStatusChangeHistory.setChangeRoomStatusDate(Timestamp.valueOf(LocalDateTime.now()));
                dataRoomStatusChangeHistory.setRefRoomStatusByRoomStatusOld(getOldRoomStatus(room.getIdroom()));
                dataRoomStatusChangeHistory.setRefRoomStatusByRoomStatusNew(room.getRefRoomStatus());
                dataRoomStatusChangeHistory.setCreateDate(Timestamp.valueOf(LocalDateTime.now()));
                dataRoomStatusChangeHistory.setTblEmployeeByCreateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                dataRoomStatusChangeHistory.setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                dataRoomStatusChangeHistory.setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                dataRoomStatusChangeHistory.setRefRecordStatus(session.find(RefRecordStatus.class, 1));
                session.saveOrUpdate(dataRoomStatusChangeHistory);
                //data room
                room.setRoomStatusNote(ClassFormatter.dateTimeFormate.format(roomStatusDetail.getRoomStatusDetailDate())
                        + " - "
                        + roomStatusDetail.getRoomStatusDetail());
                room.setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                room.setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                session.update(room);
                session.getTransaction().commit();
            } catch (Exception e) {
                if (session.getTransaction().isActive()) {
                    session.getTransaction().rollback();
                }
                errMsg = e.getMessage();
                return false;
            }
        } else {
            return false;
        }
        return true;
    }

    //--------------------------------------------------------------------------
    @Override
    public TblRoomStatusDetail insertDataRoomStatusDetail(TblRoomStatusDetail roomStatusDetail) {
        errMsg = "";
        TblRoomStatusDetail tblRoomStatusDetail = roomStatusDetail;
        if (ClassSession.checkUserSession()) {  //check user current session
            try {
                session = HibernateUtil.getSessionFactory().getCurrentSession();
                session.beginTransaction();
                tblRoomStatusDetail.setCreateDate(Timestamp.valueOf(LocalDateTime.now()));
                tblRoomStatusDetail.setTblEmployeeByCreateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                tblRoomStatusDetail.setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                tblRoomStatusDetail.setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                tblRoomStatusDetail.setRefRecordStatus(session.find(RefRecordStatus.class, 1));
                session.saveOrUpdate(tblRoomStatusDetail);
                session.getTransaction().commit();
            } catch (Exception e) {
                if (session.getTransaction().isActive()) {
                    session.getTransaction().rollback();
                }
                errMsg = e.getMessage();
                return null;
            }
        } else {
            return null;
        }
        return tblRoomStatusDetail;
    }

    @Override
    public boolean updateDataRoomStatusDetail(TblRoomStatusDetail roomStatusDetail) {
        errMsg = "";
        if (ClassSession.checkUserSession()) {  //check user current session
            try {
                session = HibernateUtil.getSessionFactory().getCurrentSession();
                session.beginTransaction();
                roomStatusDetail.setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                roomStatusDetail.setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                session.update(roomStatusDetail);
                session.getTransaction().commit();
            } catch (Exception e) {
                if (session.getTransaction().isActive()) {
                    session.getTransaction().rollback();
                }
                errMsg = e.getMessage();
                return false;
            }
        } else {
            return false;
        }
        return true;
    }

    @Override
    public boolean deleteDataRoomStatusDetail(TblRoomStatusDetail roomStatusDetail) {
        errMsg = "";
        if (ClassSession.checkUserSession()) {  //check user current session
            try {
                session = HibernateUtil.getSessionFactory().getCurrentSession();
                session.beginTransaction();
                if (roomStatusDetail.getRefRecordStatus().getIdstatus() == 1) {
                    roomStatusDetail.setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                    roomStatusDetail.setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                    roomStatusDetail.setRefRecordStatus(session.find(RefRecordStatus.class, 2));
                    session.update(roomStatusDetail);
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
            }
        } else {
            return false;
        }
        return true;
    }

    @Override
    public TblRoomStatusDetail getDataRoomStatusDetail(long id) {
        errMsg = "";
        TblRoomStatusDetail data = null;
        if (ClassSession.checkUserSession()) {  //check user current session
            try {
                session = HibernateUtil.getSessionFactory().getCurrentSession();
                session.beginTransaction();
                data = (TblRoomStatusDetail) session.find(TblRoomStatusDetail.class, id);
                session.getTransaction().commit();
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
    public List<TblRoomStatusDetail> getAllDataRoomStatusDetail() {
        errMsg = "";
        List<TblRoomStatusDetail> list = new ArrayList<>();
        if (ClassSession.checkUserSession()) {  //check user current session
            try {
                session = HibernateUtil.getSessionFactory().getCurrentSession();
                session.beginTransaction();
                list = session.getNamedQuery("findAllTblRoomStatusDetail").list();
                session.getTransaction().commit();
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
    public List<TblRoomStatusDetail> getAllDataRoomStatusDetailByIDRoom(long idRoom) {
        errMsg = "";
        List<TblRoomStatusDetail> list = new ArrayList<>();
        if (ClassSession.checkUserSession()) {  //check user current session
            try {
                session = HibernateUtil.getSessionFactory().getCurrentSession();
                session.beginTransaction();
                list = session.getNamedQuery("findAllTblRoomStatusDetailByIDRoom")
                        .setParameter("idRoom", idRoom)
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
    public List<TblRoomStatusDetail> getAllDataRoomStatusDetailByIDRoomAndDetailDateBetween(long idRoom,
            Timestamp beginDate,
            Timestamp endDate) {
        errMsg = "";
        List<TblRoomStatusDetail> list = new ArrayList<>();
        if (ClassSession.checkUserSession()) {  //check user current session
            try {
                session = HibernateUtil.getSessionFactory().getCurrentSession();
                session.beginTransaction();
                list = session.getNamedQuery("findAllTblRoomStatusDetailByIDRoomAndDetailDateBetween")
                        .setParameter("idRoom", idRoom)
                        .setParameter("beginDate", beginDate)
                        .setParameter("endDate", endDate)
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
    public TblRoomStatusChangeHistory getDataRoomStatusChangeHistory(long id) {
        errMsg = "";
        TblRoomStatusChangeHistory data = null;
        if (ClassSession.checkUserSession()) {  //check user current session
            try {
                session = HibernateUtil.getSessionFactory().getCurrentSession();
                session.beginTransaction();
                data = (TblRoomStatusChangeHistory) session.find(TblRoomStatusChangeHistory.class, id);
                session.getTransaction().commit();
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
    public List<TblRoomStatusChangeHistory> getAllDataRoomStatusChangeHistory() {
        errMsg = "";
        List<TblRoomStatusChangeHistory> list = new ArrayList<>();
        if (ClassSession.checkUserSession()) {  //check user current session
            try {
                session = HibernateUtil.getSessionFactory().getCurrentSession();
                session.beginTransaction();
                list = session.getNamedQuery("findAllTblRoomStatusChangeHistory").list();
                session.getTransaction().commit();
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

    @Override
    public TblReservationCheckInOut getDataReservationCheckInOut(long id) {
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
    public TblCustomer getDataCustomer(long id) {
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
    public RefReservationCheckInOutStatus getDataReservationCheckInOutStatus(int id) {
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

    @Override
    public List<TblReservationRoomTypeDetailItem> getAllDataReservationRoomTypeDetailItemByIDReservationRoomTypeDetail(long idRRTD) {
        errMsg = "";
        List<TblReservationRoomTypeDetailItem> list = new ArrayList<>();
        if (ClassSession.checkUserSession()) {  //check user current session
            try {
                session = HibernateUtil.getSessionFactory().getCurrentSession();
                session.beginTransaction();
                list = session.getNamedQuery("findAllTblReservationRoomTypeDetailItemByIDReservationRoomTypeDetail")
                        .setParameter("idRRTD", idRRTD)
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
    public List<TblReservationAdditionalItem> getAllDataReservationAdditionalItemByIDReservationRoomTypeDetailAndAdditionalDate(
            long idRRTD,
            Date additionalDate) {
        errMsg = "";
        List<TblReservationAdditionalItem> list = new ArrayList<>();
        if (ClassSession.checkUserSession()) {  //check user current session
            try {
                session = HibernateUtil.getSessionFactory().getCurrentSession();
                session.beginTransaction();
                list = session.getNamedQuery("findAllTblReservationAdditionalItemByIDReservationRoomTypeDetailAndAdditionalDate")
                        .setParameter("idReservationRoomTypeDetail", idRRTD)
                        .setParameter("additionalDate", additionalDate)
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
    public List<TblRoom> getAllDataRoomByIDRoomStatus(long idRoomStatus) {
        errMsg = "";
        List<TblRoom> list = new ArrayList<>();
        if (ClassSession.checkUserSession()) {  //check user current session
            try {
                session = HibernateUtil.getSessionFactory().getCurrentSession();
                session.beginTransaction();
                list = session.getNamedQuery("findAllTblRoomByIDRoomStatus")
                        .setParameter("idRoomStatus", idRoomStatus)
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
    public SysCurrentHotelDate getDataCurrentHotelDate(int id) {
        errMsg = "";
        SysCurrentHotelDate data = null;
        if (ClassSession.checkUserSession()) {  //check user current session
            try {
                session = HibernateUtil.getSessionFactory().getCurrentSession();
                session.beginTransaction();
                data = (SysCurrentHotelDate) session.find(SysCurrentHotelDate.class, id);
                session.getTransaction().commit();
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
    public boolean updateDataRRTDReservationCheckInOutRoom(List<TblReservationRoomTypeDetail> rrtds) {
        errMsg = "";
        if (ClassSession.checkUserSession()) {  //check user current session
            try {
                session = HibernateUtil.getSessionFactory().getCurrentSession();
                session.beginTransaction();
                for (TblReservationRoomTypeDetail rrtd : rrtds) {
                    //data rrtd - check in/out (room)
                    rrtd.getTblReservationCheckInOut().setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                    rrtd.getTblReservationCheckInOut().setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                    session.update(rrtd.getTblReservationCheckInOut());
                    //data rrtd
                    rrtd.setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                    rrtd.setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                    session.update(rrtd);
                }
                session.getTransaction().commit();
            } catch (Exception e) {
                if (session.getTransaction().isActive()) {
                    session.getTransaction().rollback();
                }
                errMsg = e.getMessage();
                return false;
            }
        } else {
            return false;
        }
        return true;
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
    public TblReservationBrokenItem insertDataReservationBrokenItem(
            TblReservationBrokenItem brokenItem,
            CustomerBrokenItemController.ClassItemLocation classItemLocation) {
        errMsg = "";
        TblReservationBrokenItem tblReservationBrokenItem = brokenItem;
        if (ClassSession.checkUserSession()) {  //check user current session
            try {
                session = HibernateUtil.getSessionFactory().getCurrentSession();
                session.beginTransaction();
                //data reservation broken item
                tblReservationBrokenItem.setItemCharge(tblReservationBrokenItem.getTblItem().getBrokenCharge());
                tblReservationBrokenItem.setCreateDate(Timestamp.valueOf(LocalDateTime.now()));
                tblReservationBrokenItem.setTblEmployeeByCreateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                tblReservationBrokenItem.setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                tblReservationBrokenItem.setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                tblReservationBrokenItem.setRefRecordStatus(session.find(RefRecordStatus.class, 1));
                session.saveOrUpdate(tblReservationBrokenItem);
                //data location bin (first)
                TblLocation locationFirtsBin = getDataLocationFirstBin();
                if (locationFirtsBin == null) {    //Location of 'Digunakan' is not found...
                    errMsg = "Terjadi kesalahan pada lokasi (StockOpname)..!";
                    if (session.getTransaction().isActive()) {
                        session.getTransaction().rollback();
                    }
                    return null;
                }
                //data item mutation history
                TblItemMutationHistory dataMutation = new TblItemMutationHistory();
                dataMutation.setTblItem(tblReservationBrokenItem.getTblItem());
                dataMutation.setTblLocationByIdlocationOfSource(classItemLocation.getDataItemLocation().getTblLocation());
                dataMutation.setTblLocationByIdlocationOfDestination(locationFirtsBin);
                dataMutation.setItemQuantity(tblReservationBrokenItem.getItemQuantity());
                dataMutation.setRefItemMutationType(session.find(RefItemMutationType.class, 2));    //Rusak = '2'
                dataMutation.setRefItemObsoleteBy(session.find(RefItemObsoleteBy.class, 0));    //Customer = '0'
                dataMutation.setMutationDate(Timestamp.valueOf(LocalDateTime.now()));
                dataMutation.setCreateDate(Timestamp.valueOf(LocalDateTime.now()));
                dataMutation.setTblEmployeeByCreateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                dataMutation.setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                dataMutation.setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                dataMutation.setRefRecordStatus(session.find(RefRecordStatus.class, 1));
                session.saveOrUpdate(dataMutation);
                //data item location (used, -)
                classItemLocation.getDataItemLocation().setItemQuantity(classItemLocation.getDataItemLocation().getItemQuantity().subtract(dataMutation.getItemQuantity()));
                classItemLocation.getDataItemLocation().setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                classItemLocation.getDataItemLocation().setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                if (classItemLocation.getDataItemLocation().getItemQuantity()
                        .compareTo(new BigDecimal("0")) == -1) {   //data item quantity < 0
                    errMsg = "Stok Barang (" + dataMutation.getTblItem().getItemName() + ") tidak mencukupi..!";
                    if (session.getTransaction().isActive()) {
                        session.getTransaction().rollback();
                    }
                    return null;
                }
                session.update(classItemLocation.getDataItemLocation());
                //data item location (bin, +)
                TblItemLocation dataItemLocationBin = getItemLocationByIDLocationAndIDItem(
                        dataMutation.getTblLocationByIdlocationOfDestination().getIdlocation(),
                        dataMutation.getTblItem().getIditem());
                if (dataItemLocationBin != null) {
                    dataItemLocationBin.setItemQuantity(dataItemLocationBin.getItemQuantity().add(dataMutation.getItemQuantity()));
                    dataItemLocationBin.setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                    dataItemLocationBin.setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                    session.update(dataItemLocationBin);
                } else {
                    dataItemLocationBin = new TblItemLocation();
                    dataItemLocationBin.setTblItem(dataMutation.getTblItem());
                    dataItemLocationBin.setTblLocation(dataMutation.getTblLocationByIdlocationOfDestination());
                    dataItemLocationBin.setItemQuantity(dataMutation.getItemQuantity());
                    dataItemLocationBin.setCreateDate(Timestamp.valueOf(LocalDateTime.now()));
                    dataItemLocationBin.setTblEmployeeByCreateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                    dataItemLocationBin.setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                    dataItemLocationBin.setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                    dataItemLocationBin.setRefRecordStatus(session.find(RefRecordStatus.class, 1));
                    session.saveOrUpdate(dataItemLocationBin);
                }
                //data property barcode
                if (classItemLocation.getDataItemLocationPropertyBarcode() != null) { //property barcode
                    //data item mutation history - property barcode
                    TblItemMutationHistoryPropertyBarcode dataMutationPropertyBarcode = new TblItemMutationHistoryPropertyBarcode();
                    dataMutationPropertyBarcode.setTblItemMutationHistory(dataMutation);
                    dataMutationPropertyBarcode.setTblPropertyBarcode(classItemLocation.getDataItemLocationPropertyBarcode().getTblPropertyBarcode());
                    dataMutationPropertyBarcode.setCreateDate(Timestamp.valueOf(LocalDateTime.now()));
                    dataMutationPropertyBarcode.setTblEmployeeByCreateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                    dataMutationPropertyBarcode.setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                    dataMutationPropertyBarcode.setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                    dataMutationPropertyBarcode.setRefRecordStatus(session.find(RefRecordStatus.class, 1));
                    session.saveOrUpdate(dataMutationPropertyBarcode);
                    //data iten location - property barcode (used, remove)
                    classItemLocation.getDataItemLocationPropertyBarcode().setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                    classItemLocation.getDataItemLocationPropertyBarcode().setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                    classItemLocation.getDataItemLocationPropertyBarcode().setRefRecordStatus(session.find(RefRecordStatus.class, 2));
                    session.update(classItemLocation.getDataItemLocationPropertyBarcode());
                    //data iten location - property barcode (bin, add)
                    TblItemLocationPropertyBarcode dataItemLocationPropertyBarcodeBin = getItemLocationPropertyBarcodeByIDItemLocationAndIDPropertyBarcode(
                            dataItemLocationBin.getIdrelation(),
                            classItemLocation.getDataItemLocationPropertyBarcode().getTblPropertyBarcode().getIdbarcode());
                    if (dataItemLocationPropertyBarcodeBin != null) {
                        dataItemLocationPropertyBarcodeBin.setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                        dataItemLocationPropertyBarcodeBin.setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                        dataItemLocationPropertyBarcodeBin.setRefRecordStatus(session.find(RefRecordStatus.class, 1));
                        session.update(dataItemLocationPropertyBarcodeBin);
                    } else {
                        dataItemLocationPropertyBarcodeBin = new TblItemLocationPropertyBarcode();
                        dataItemLocationPropertyBarcodeBin.setTblPropertyBarcode(classItemLocation.getDataItemLocationPropertyBarcode().getTblPropertyBarcode());
                        dataItemLocationPropertyBarcodeBin.setTblItemLocation(dataItemLocationBin);
                        dataItemLocationPropertyBarcodeBin.setCreateDate(Timestamp.valueOf(LocalDateTime.now()));
                        dataItemLocationPropertyBarcodeBin.setTblEmployeeByCreateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                        dataItemLocationPropertyBarcodeBin.setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                        dataItemLocationPropertyBarcodeBin.setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                        dataItemLocationPropertyBarcodeBin.setRefRecordStatus(session.find(RefRecordStatus.class, 1));
                        session.saveOrUpdate(dataItemLocationPropertyBarcodeBin);
                    }
                }
                //data item expired date
                if (classItemLocation.getDataItemLocationItemExpiredDate() != null) { //item expired date
                    //data item mutation history - item expired date
                    TblItemMutationHistoryItemExpiredDate dataMutationItemExpiredDate = new TblItemMutationHistoryItemExpiredDate();
                    dataMutationItemExpiredDate.setTblItemMutationHistory(dataMutation);
                    dataMutationItemExpiredDate.setTblItemExpiredDate(classItemLocation.getDataItemLocationItemExpiredDate().getTblItemExpiredDate());
                    dataMutationItemExpiredDate.setItemQuantity(classItemLocation.getDataItemLocationItemExpiredDate().getItemQuantity());
                    dataMutationItemExpiredDate.setCreateDate(Timestamp.valueOf(LocalDateTime.now()));
                    dataMutationItemExpiredDate.setTblEmployeeByCreateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                    dataMutationItemExpiredDate.setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                    dataMutationItemExpiredDate.setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                    dataMutationItemExpiredDate.setRefRecordStatus(session.find(RefRecordStatus.class, 1));
                    session.saveOrUpdate(dataMutationItemExpiredDate);
                    //data item location - item expired date (used, -)
                    classItemLocation.getDataItemLocationItemExpiredDate().setItemQuantity(classItemLocation.getDataItemLocationItemExpiredDate().getItemQuantity().subtract((dataMutationItemExpiredDate.getItemQuantity())));
                    classItemLocation.getDataItemLocationItemExpiredDate().setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                    classItemLocation.getDataItemLocationItemExpiredDate().setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                    if (classItemLocation.getDataItemLocationItemExpiredDate().getItemQuantity()
                            .compareTo(new BigDecimal("0")) == -1) {   //data item quantity < 0
                        errMsg = "Stok Barang (" + dataMutation.getTblItem().getItemName()
                                + " - " + ClassFormatter.dateFormate.format(dataMutationItemExpiredDate.getTblItemExpiredDate().getItemExpiredDate())
                                + ") tidak mencukupi..!";
                        if (session.getTransaction().isActive()) {
                            session.getTransaction().rollback();
                        }
                        return null;
                    }
                    session.update(classItemLocation.getDataItemLocationItemExpiredDate());
                    //data item location - item expired date (bin, +)
                    TblItemLocationItemExpiredDate dataItemLocationItemExpiredDateSO = getItemLocationItemExpiredDateByIDItemLocationAndIDItemExpiredDate(
                            dataItemLocationBin.getIdrelation(),
                            classItemLocation.getDataItemLocationItemExpiredDate().getTblItemExpiredDate().getIditemExpiredDate());
                    if (dataItemLocationItemExpiredDateSO != null) {
                        dataItemLocationItemExpiredDateSO.setItemQuantity(dataItemLocationItemExpiredDateSO.getItemQuantity().add((dataMutationItemExpiredDate.getItemQuantity())));
                        dataItemLocationItemExpiredDateSO.setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                        dataItemLocationItemExpiredDateSO.setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                        session.update(dataItemLocationItemExpiredDateSO);
                    } else {
                        dataItemLocationItemExpiredDateSO = new TblItemLocationItemExpiredDate();
                        dataItemLocationItemExpiredDateSO.setTblItemExpiredDate(classItemLocation.getDataItemLocationItemExpiredDate().getTblItemExpiredDate());
                        dataItemLocationItemExpiredDateSO.setTblItemLocation(dataItemLocationBin);
                        dataItemLocationItemExpiredDateSO.setItemQuantity(dataMutationItemExpiredDate.getItemQuantity());
                        dataItemLocationItemExpiredDateSO.setCreateDate(Timestamp.valueOf(LocalDateTime.now()));
                        dataItemLocationItemExpiredDateSO.setTblEmployeeByCreateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                        dataItemLocationItemExpiredDateSO.setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                        dataItemLocationItemExpiredDateSO.setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                        dataItemLocationItemExpiredDateSO.setRefRecordStatus(session.find(RefRecordStatus.class, 1));
                        session.saveOrUpdate(dataItemLocationItemExpiredDateSO);
                    }
                }
                session.getTransaction().commit();
            } catch (Exception e) {
                if (session.getTransaction().isActive()) {
                    session.getTransaction().rollback();
                }
                errMsg = e.getMessage();
                return null;
            }
        } else {
            return null;
        }
        return tblReservationBrokenItem;
    }

    private TblLocation getDataLocationFirstBin() {
        List<TblLocation> list = session.getNamedQuery("findAllTblLocationByIDLocationType")
                .setParameter("idLocationType", 4) //Bin = '4'
                .list();
        return list.isEmpty() ? null : list.get(0);
    }

    @Override
    public TblLocation getDataLocationByIDLocationTypeAndFirstData(int idLocationType) {
        errMsg = "";
        List<TblLocation> list = new ArrayList<>();
        if (ClassSession.checkUserSession()) {  //check user current session
            try {
                session = HibernateUtil.getSessionFactory().getCurrentSession();
                session.beginTransaction();
                list = session.getNamedQuery("findAllTblLocationByIDLocationType")
                        .setParameter("idLocationType", idLocationType)
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
    public TblReservationAdditionalService insertDataReservationAdditionalService(TblReservationAdditionalService ras) {
        errMsg = "";
        TblReservationAdditionalService tblReservationAdditionalService = ras;
        if (ClassSession.checkUserSession()) {  //check user current session
            try {
                session = HibernateUtil.getSessionFactory().getCurrentSession();
                session.beginTransaction();
                //data log
                String headerLog = "BUAT DATA TAMBAHAN SERVICE (RESERVASI)";
                String dataLog = "";
                //data reservation
                ras.getTblReservationRoomTypeDetail().getTblReservation().setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                ras.getTblReservationRoomTypeDetail().getTblReservation().setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                session.update(ras.getTblReservationRoomTypeDetail().getTblReservation());
                //data log
                dataLog += "No. Reservasi : " + ras.getTblReservationRoomTypeDetail().getTblReservation().getCodeReservation() + "\n";
                dataLog += "Tgl. Reservasi : " + ClassFormatter.dateFormate.format(ras.getTblReservationRoomTypeDetail().getTblReservation().getReservationDate()) + "\n";
                dataLog += "Nama Customer : " + ras.getTblReservationRoomTypeDetail().getTblReservation().getTblCustomer().getTblPeople().getFullName() + "\n";
                dataLog += "Travel Agent : " + (ras.getTblReservationRoomTypeDetail().getTblReservation().getTblPartner() != null ? ras.getTblReservationRoomTypeDetail().getTblReservation().getTblPartner().getPartnerName() : "-") + "\n";
                dataLog += "-----------------------------------------------------------------------\n";
                dataLog += "Data Tambahan Barang : " + ras.getTblReservationRoomTypeDetail().getTblReservation().getCodeReservation() + "\n";
                //data hotel invoice
                TblHotelInvoice hotelInvoice = new TblHotelInvoice();
//                hotelInvoice.setCodeHotelInvoice(ClassCoder.getCode("Hotel Invoice", session));
                hotelInvoice.setCodeHotelInvoice(ras.getRestoTransactionNumber());
                hotelInvoice.setTblPartner(null);
                hotelInvoice.setTblSupplier(null);
                hotelInvoice.setRefHotelInvoiceType(session.find(RefHotelInvoiceType.class, 0));    //Payable = '0'
                hotelInvoice.setCreateDate(Timestamp.valueOf(LocalDateTime.now()));
                hotelInvoice.setTblEmployeeByCreateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                hotelInvoice.setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                hotelInvoice.setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                hotelInvoice.setRefRecordStatus(session.find(RefRecordStatus.class, 1));
                session.saveOrUpdate(hotelInvoice);
                //data hotel payable
                TblHotelPayable hotelPayable = new TblHotelPayable();
                hotelPayable.setTblHotelInvoice(hotelInvoice);
                hotelPayable.setHotelPayableNominal(ras.getPrice());
                hotelPayable.setRefHotelPayableType(session.find(RefHotelPayableType.class, 2));   //Resto = '2'
                hotelPayable.setRefFinanceTransactionStatus(session.find(RefFinanceTransactionStatus.class, 0));    //Belum Dibayar = '0'
                hotelPayable.setCreateDate(Timestamp.valueOf(LocalDateTime.now()));
                hotelPayable.setTblEmployeeByCreateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                hotelPayable.setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                hotelPayable.setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                hotelPayable.setRefRecordStatus(session.find(RefRecordStatus.class, 1));
                session.saveOrUpdate(hotelPayable);
                //data reservation additonal service
                ras.setTblHotelPayable(hotelPayable);
                ras.setCreateDate(Timestamp.valueOf(LocalDateTime.now()));
                ras.setTblEmployeeByCreateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                ras.setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                ras.setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                ras.setRefRecordStatus(session.find(RefRecordStatus.class, 1));
                session.saveOrUpdate(ras);
                //data log
                dataLog += "- " + ClassFormatter.dateFormate.format(ras.getAdditionalDate()) + " - " + ras.getTblRoomService().getServiceName() + " \t "
                        + "Harga : " + ClassFormatter.currencyFormat.cFormat(ras.getPrice()) + " \t "
                        + "Diskon : " + ClassFormatter.decimalFormat.cFormat(ras.getDiscountPercentage()) + "% (" + getDataDiscountEvent(ras) + ") \t "
                        + "Jumlah : " + ClassFormatter.decimalFormat.cFormat(new BigDecimal("1")) + " \n";

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
                return null;
            } finally {
                //session.close();
            }
        } else {
            return null;
        }
        return tblReservationAdditionalService;
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

    //--------------------------------------------------------------------------

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

    //------------------WORK SHEET ATTENDANCE ----------------------------------
    @Override
    public List<TblRoomCheckHouseKeepingAttendance> getAllHouseKeepingAttendance() {
        List<TblRoomCheckHouseKeepingAttendance> list = new ArrayList();
        if (ClassSession.checkUserSession()) {
            try {
                session = HibernateUtil.getSessionFactory().getCurrentSession();
                session.beginTransaction();
                list = session.getNamedQuery("findAllTblRoomCheckHouseKeepingAttendance").list();
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
    public boolean insertWorkSheetAttendance(List<TblRoomCheckHouseKeepingAttendance> listWorkSheet) {
        if (ClassSession.checkUserSession()) {
            try {
                session = HibernateUtil.getSessionFactory().getCurrentSession();
                session.beginTransaction();
                for (TblRoomCheckHouseKeepingAttendance getHouseKeepingAttendance : listWorkSheet) {
                    getHouseKeepingAttendance.setTblEmployeeByCreatedBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                    getHouseKeepingAttendance.setTblEmployeeByLastUpdatedBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                    getHouseKeepingAttendance.setCreatedDate(Timestamp.valueOf(LocalDateTime.now()));
                    getHouseKeepingAttendance.setLastUpdatedDate(Timestamp.valueOf(LocalDateTime.now()));
                    getHouseKeepingAttendance.setRefRecordStatus(session.find(RefRecordStatus.class, 1));
                    session.saveOrUpdate(getHouseKeepingAttendance);
                }
                session.getTransaction().commit();
            } catch (Exception e) {
                if (session.getTransaction().isActive()) {
                    session.getTransaction().rollback();
                }
                errMsg = e.getMessage();
                return false;
            }
        } else {
            return false;
        }
        return true;
    }

    //--------------------------------------------------------------------------
    @Override
    public TblRoomCheckHouseKeepingAttendance getDataRoomCheckHouseKeepingAttendanceByIDRoomAndIDRoomStatusAfterIsNull(long idRoom) {
        errMsg = "";
        List<TblRoomCheckHouseKeepingAttendance> list = new ArrayList<>();
        if (ClassSession.checkUserSession()) {  //check user current session
            try {
                session = HibernateUtil.getSessionFactory().getCurrentSession();
                session.beginTransaction();
                list = session.getNamedQuery("findAllTblRoomCheckHouseKeepingAttendanceByIDRoomAndIDRoomStatusAfterIsNull")
                        .setParameter("idRoom", idRoom)
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

    public boolean updateItemCodeWorkSheet(List<TblItem>list){
      errMsg = "";
      if(ClassSession.checkUserSession()){
           try{
              session = HibernateUtil.getSessionFactory().getCurrentSession();
              session.beginTransaction();
              for(TblItem item : list){
                session.update(item);
              }
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
    
    //---------------------------------------------------------------
    
     public List<TblReservationAdditionalItem> getAllDataReservationAdditionalItemByIDReservationRoomTypeDetailAdditionalDateAndBillType(
            long idRRTD,
            Date additionalDate) {
        errMsg = "";
        List<TblReservationAdditionalItem> list = new ArrayList<>();
        if (ClassSession.checkUserSession()) {  //check user current session
            try {
                session = HibernateUtil.getSessionFactory().getCurrentSession();
                session.beginTransaction();
                list = session.getNamedQuery("findAllTblReservationAdditionalItemByIDReservationRoomTypeDetailAdditionalDateAndBillType")
                        .setParameter("idReservationRoomTypeDetail", idRRTD)
                        .setParameter("additionalDate", additionalDate)
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
     
    public boolean insertHouseKeepingAttendanceDetail(List<TblRoomCheckHouseKeepingAttendanceDetail> listRoomCheckHouseKeepingAttendanceDetail,TblRoomCheckHouseKeepingAttendance tblRoomCheckHouseKeepingAttendance){
       errMsg = "";
       if(ClassSession.checkUserSession()){
           try{
             session = HibernateUtil.getSessionFactory().getCurrentSession();
             session.beginTransaction();
             tblRoomCheckHouseKeepingAttendance.getTblRoom().setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
             tblRoomCheckHouseKeepingAttendance.getTblRoom().setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class,ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
             tblRoomCheckHouseKeepingAttendance.getTblRoom().setRefRecordStatus(session.find(RefRecordStatus.class,1));
             session.update(tblRoomCheckHouseKeepingAttendance.getTblRoom());
             
             tblRoomCheckHouseKeepingAttendance.setLastUpdatedDate(Timestamp.valueOf(LocalDateTime.now()));
             tblRoomCheckHouseKeepingAttendance.setTblEmployeeByLastUpdatedBy(session.find(TblEmployee.class,ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
             tblRoomCheckHouseKeepingAttendance.setRefRecordStatus(session.find(RefRecordStatus.class,1));
             session.update(tblRoomCheckHouseKeepingAttendance);
             
               for(TblRoomCheckHouseKeepingAttendanceDetail getRoomCheckHouseKeepingAttendanceDetail : listRoomCheckHouseKeepingAttendanceDetail){
                 System.out.println("nama house keeping :"+getRoomCheckHouseKeepingAttendanceDetail.getTblRoomCheckHouseKeepingAttendance().getTblEmployeeByIdchecker());
                 getRoomCheckHouseKeepingAttendanceDetail.setCreatedDate(Timestamp.valueOf(LocalDateTime.now()));
                 getRoomCheckHouseKeepingAttendanceDetail.setLastUpdatedDate(Timestamp.valueOf(LocalDateTime.now()));
                 getRoomCheckHouseKeepingAttendanceDetail.setTblEmployeeByCreatedBy(session.find(TblEmployee.class,ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                 getRoomCheckHouseKeepingAttendanceDetail.setTblEmployeeByLastUpdatedBy(session.find(TblEmployee.class,ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                 getRoomCheckHouseKeepingAttendanceDetail.setRefRecordStatus(session.find(RefRecordStatus.class,1));
                 session.save(getRoomCheckHouseKeepingAttendanceDetail);
               }
             
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
    
    //--------------------------------------------------------------------------
    @Override
    public String getErrorMessage() {
        return errMsg;
    }

}
