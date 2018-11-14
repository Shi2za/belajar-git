/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hotelfx.persistence.service;

import hotelfx.helper.ClassCoder;
import hotelfx.helper.ClassDataMutation;
import hotelfx.helper.ClassDataTransferItem;
import hotelfx.helper.ClassFormatter;
import hotelfx.helper.ClassInputLocation;
import hotelfx.helper.ClassSession;
import hotelfx.persistence.HibernateUtil;
import hotelfx.persistence.model.LogRoomTypePriceChangedHistory;
import hotelfx.persistence.model.RefItemMutationType;
import hotelfx.persistence.model.RefItemObsoleteBy;
import hotelfx.persistence.model.RefLocationType;
import hotelfx.persistence.model.RefRecordStatus;
import hotelfx.persistence.model.RefRoomCleanStatus;
import hotelfx.persistence.model.RefRoomStatus;
import hotelfx.persistence.model.SysDataHardCode;
import hotelfx.persistence.model.TblBuilding;
import hotelfx.persistence.model.TblEmployee;
import hotelfx.persistence.model.TblFloor;
import hotelfx.persistence.model.TblGroup;
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
import hotelfx.persistence.model.TblJob;
import hotelfx.persistence.model.TblLocation;
import hotelfx.persistence.model.TblLocationOfBin;
import hotelfx.persistence.model.TblLocationOfLaundry;
import hotelfx.persistence.model.TblLocationOfWarehouse;
import hotelfx.persistence.model.TblPeople;
import hotelfx.persistence.model.TblPropertyBarcode;
import hotelfx.persistence.model.TblRoom;
import hotelfx.persistence.model.TblRoomService;
import hotelfx.persistence.model.TblRoomType;
import hotelfx.persistence.model.TblRoomTypeItem;
import hotelfx.persistence.model.TblRoomTypeRoomService;
import hotelfx.persistence.model.TblSupplier;
import hotelfx.persistence.model.TblUnit;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import javafx.collections.ObservableList;
import org.hibernate.Session;
import org.hibernate.query.Query;

/**
 *
 * @author ANDRI
 */
public class FRoomManagerImpl implements FRoomManager {

    private Session session;

    private String errMsg;

    public FRoomManagerImpl() {

    }

    @Override
    public TblRoom insertDataRoom(TblRoom room) {
        errMsg = "";
        TblRoom tblRoom = room;
        if (ClassSession.checkUserSession()) {  //check user current session
            try {
                session = HibernateUtil.getSessionFactory().getCurrentSession();
                session.beginTransaction();
                //data location
                tblRoom.getTblLocation().setRefLocationType(session.find(RefLocationType.class, 0));
                tblRoom.getTblLocation().setCreateDate(Timestamp.valueOf(LocalDateTime.now()));
                tblRoom.getTblLocation().setTblEmployeeByCreateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                tblRoom.getTblLocation().setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                tblRoom.getTblLocation().setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                tblRoom.getTblLocation().setRefRecordStatus(session.find(RefRecordStatus.class, 1));
                session.saveOrUpdate(tblRoom.getTblLocation());
                //data room
                tblRoom.setCreateDate(Timestamp.valueOf(LocalDateTime.now()));
                tblRoom.setTblEmployeeByCreateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                tblRoom.setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                tblRoom.setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                tblRoom.setRefRecordStatus(session.find(RefRecordStatus.class, 1));
                session.saveOrUpdate(tblRoom);
                //data item - location (room - all items)
                List<TblItem> dataItems = getAllItem();
                for (TblItem dataItem : dataItems) {
                    TblItemLocation dataItemLocation = new TblItemLocation();
                    dataItemLocation.setTblItem(dataItem);
                    dataItemLocation.setTblLocation(tblRoom.getTblLocation());
                    dataItemLocation.setItemQuantity(new BigDecimal("0"));
                    dataItemLocation.setCreateDate(Timestamp.valueOf(LocalDateTime.now()));
                    dataItemLocation.setTblEmployeeByCreateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                    dataItemLocation.setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                    dataItemLocation.setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                    dataItemLocation.setRefRecordStatus(session.find(RefRecordStatus.class, 1));
                    session.saveOrUpdate(dataItemLocation);
                    if (dataItem.getConsumable()) {   //consumable
                        //data item - location - item expired date (all item expired date(s))
                        List<TblItemExpiredDate> itemExpiredDates = getAllItemExpiredDateByIDItem(dataItem.getIditem());
                        for (TblItemExpiredDate itemExpiredDate : itemExpiredDates) {
                            TblItemLocationItemExpiredDate dataItemLocationItemExpiredDate = new TblItemLocationItemExpiredDate();
                            dataItemLocationItemExpiredDate.setTblItemLocation(dataItemLocation);
                            dataItemLocationItemExpiredDate.setTblItemExpiredDate(itemExpiredDate);
                            dataItemLocationItemExpiredDate.setItemQuantity(new BigDecimal("0"));
                            dataItemLocationItemExpiredDate.setCreateDate(Timestamp.valueOf(LocalDateTime.now()));
                            dataItemLocationItemExpiredDate.setTblEmployeeByCreateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                            dataItemLocationItemExpiredDate.setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                            dataItemLocationItemExpiredDate.setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                            dataItemLocationItemExpiredDate.setRefRecordStatus(session.find(RefRecordStatus.class, 1));
                            session.saveOrUpdate(dataItemLocationItemExpiredDate);
                        }
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
        return tblRoom;
    }

    private List<TblItem> getAllItem() {
        List<TblItem> list = session.getNamedQuery("findAllTblItem")
                .list();
        return list;
    }

    private List<TblItemExpiredDate> getAllItemExpiredDateByIDItem(long idItem) {
        List<TblItemExpiredDate> list = session.getNamedQuery("findAllTblItemExpiredDateByIDItem")
                .setParameter("idItem", idItem)
                .list();
        return list;
    }

    @Override
    public boolean updateDataRoom(TblRoom room) {
        errMsg = "";
        if (ClassSession.checkUserSession()) {  //check user current session
            try {
                session = HibernateUtil.getSessionFactory().getCurrentSession();
                session.beginTransaction();
                //data location
                room.getTblLocation().setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                room.getTblLocation().setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                session.update(room.getTblLocation());
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
            } finally {
                //session.close();
            }
        } else {
            return false;
        }
        return true;
    }

    @Override
    public boolean deleteDataRoom(TblRoom room) {
        errMsg = "";
        if (ClassSession.checkUserSession()) {  //check user current session
            try {
                session = HibernateUtil.getSessionFactory().getCurrentSession();
                session.beginTransaction();
                if (room.getRefRecordStatus().getIdstatus() == 1) {
                    //data location
                    room.getTblLocation().setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                    room.getTblLocation().setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                    room.getTblLocation().setRefRecordStatus(session.find(RefRecordStatus.class, 2));
                    session.update(room.getTblLocation());
                    //data room
                    room.setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                    room.setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                    room.setRefRecordStatus(session.find(RefRecordStatus.class, 2));
                    session.update(room);
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

    @Override
    public TblBuilding getBuilding(long id) {
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
    public TblFloor getFloor(long id) {
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

    @Override
    public TblJob getJob(long id) {
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

    @Override
    public List<TblJob> getAllDataJob() {
        errMsg = "";
        List<TblJob> list = new ArrayList<>();
        if (ClassSession.checkUserSession()) {  //check user current session
            try {
                session = HibernateUtil.getSessionFactory().getCurrentSession();
                session.beginTransaction();
                list = session.getNamedQuery("findAllTblJob").list();
                session.getTransaction().commit();
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
    public TblGroup getDataGroup(long id) {
        errMsg = "";
        TblGroup data = null;
        if (ClassSession.checkUserSession()) {  //check user current session
            try {
                session = HibernateUtil.getSessionFactory().getCurrentSession();
                session.beginTransaction();
                data = (TblGroup) session.find(TblGroup.class, id);
                session.getTransaction().commit();
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
    public List<TblGroup> getAllDataGroup() {
        errMsg = "";
        List<TblGroup> list = new ArrayList<>();
        if (ClassSession.checkUserSession()) {  //check user current session
            try {
                session = HibernateUtil.getSessionFactory().getCurrentSession();
                session.beginTransaction();
                list = session.getNamedQuery("findAllTblGroup").list();
                session.getTransaction().commit();
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
    public List<TblBuilding> getAllDataBuilding() {
        errMsg = "";
        List<TblBuilding> list = new ArrayList<>();
        if (ClassSession.checkUserSession()) {  //check user current session
            try {
                session = HibernateUtil.getSessionFactory().getCurrentSession();
                session.beginTransaction();
                list = session.getNamedQuery("findAllTblBuilding").list();
                session.getTransaction().commit();
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
    public TblRoomType insertDataRoomType(TblRoomType roomType,
            List<TblRoomTypeItem> fabList,
            List<TblRoomTypeItem> amenityList,
            List<TblRoomTypeItem> propertyBarcodeList,
            List<TblRoomTypeRoomService> roomServiceList) {
        errMsg = "";
        TblRoomType tblRoomType = roomType;
        if (ClassSession.checkUserSession()) {  //check user current session
            try {
                session = HibernateUtil.getSessionFactory().getCurrentSession();
                session.beginTransaction();
                //data room type
                tblRoomType.setHotelDiscountable(false);
                tblRoomType.setCardDiscountable(false);
                tblRoomType.setCreateDate(Timestamp.valueOf(LocalDateTime.now()));
                tblRoomType.setTblEmployeeByCreateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                tblRoomType.setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                tblRoomType.setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                tblRoomType.setRefRecordStatus(session.find(RefRecordStatus.class, 1));
                session.saveOrUpdate(tblRoomType);
                //data room type - food and beverage
                for (TblRoomTypeItem l : fabList) {
                    l.setCreateDate(Timestamp.valueOf(LocalDateTime.now()));
                    l.setTblEmployeeByCreateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                    l.setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                    l.setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                    l.setRefRecordStatus(session.find(RefRecordStatus.class, 1));
                    session.saveOrUpdate(l);
                }
                //data room type- amenity
                for (TblRoomTypeItem l : amenityList) {
                    l.setCreateDate(Timestamp.valueOf(LocalDateTime.now()));
                    l.setTblEmployeeByCreateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                    l.setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                    l.setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                    l.setRefRecordStatus(session.find(RefRecordStatus.class, 1));
                    session.saveOrUpdate(l);
                }
                //data room type - property barcode
                for (TblRoomTypeItem l : propertyBarcodeList) {
                    l.setCreateDate(Timestamp.valueOf(LocalDateTime.now()));
                    l.setTblEmployeeByCreateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                    l.setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                    l.setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                    l.setRefRecordStatus(session.find(RefRecordStatus.class, 1));
                    session.saveOrUpdate(l);
                }
                //data room type - room service
                for (TblRoomTypeRoomService l : roomServiceList) {
                    l.setCreateDate(Timestamp.valueOf(LocalDateTime.now()));
                    l.setTblEmployeeByCreateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                    l.setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                    l.setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                    l.setRefRecordStatus(session.find(RefRecordStatus.class, 1));
                    session.saveOrUpdate(l);
                }
                //room type - price changed - history
                LogRoomTypePriceChangedHistory logRoomTypePriceChangedHistory = new LogRoomTypePriceChangedHistory();
                logRoomTypePriceChangedHistory.setTblRoomType(tblRoomType);
                logRoomTypePriceChangedHistory.setRoomTypePrice(tblRoomType.getRoomTypePrice());
                logRoomTypePriceChangedHistory.setHistoryDate(Timestamp.valueOf(LocalDateTime.now()));
                logRoomTypePriceChangedHistory.setTblEmployee(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                logRoomTypePriceChangedHistory.setHistoryNote("");
                session.saveOrUpdate(logRoomTypePriceChangedHistory);
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
        return tblRoomType;
    }

    @Override
    public TblRoomType insertDataRoomType(TblRoomType roomType,
            List<TblRoomTypeItem> itemList,
            List<TblRoomTypeRoomService> roomServiceList) {
        errMsg = "";
        TblRoomType tblRoomType = roomType;
        if (ClassSession.checkUserSession()) {  //check user current session
            try {
                session = HibernateUtil.getSessionFactory().getCurrentSession();
                session.beginTransaction();
                //room type - price changed - history
                String historyNote = "";
                //data room type
                tblRoomType.setHotelDiscountable(false);
                tblRoomType.setCardDiscountable(false);
                tblRoomType.setCreateDate(Timestamp.valueOf(LocalDateTime.now()));
                tblRoomType.setTblEmployeeByCreateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                tblRoomType.setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                tblRoomType.setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                tblRoomType.setRefRecordStatus(session.find(RefRecordStatus.class, 1));
                session.saveOrUpdate(tblRoomType);
                //room type - price changed - history
                historyNote += "Fasilitas : \n";
                //data room type - item
                for (TblRoomTypeItem l : itemList) {
                    l.setCreateDate(Timestamp.valueOf(LocalDateTime.now()));
                    l.setTblEmployeeByCreateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                    l.setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                    l.setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                    l.setRefRecordStatus(session.find(RefRecordStatus.class, 1));
                    session.saveOrUpdate(l);
                    //room type - price changed - history
                    historyNote += "- " + l.getTblItem().getItemName()
                            + " (" + l.getTblItem().getCodeItem() + ") : "
                            + ClassFormatter.decimalFormat.cFormat(l.getItemQuantity())
                            + " @" + ClassFormatter.currencyFormat.cFormat(l.getTblItem().getItemCostOfGoodsSold()) + "\n";
                }
                //room type - price changed - history
                historyNote += "\n";
                historyNote += "Layanan : \n";
                //data room type - room service
                for (TblRoomTypeRoomService l : roomServiceList) {
                    l.setCreateDate(Timestamp.valueOf(LocalDateTime.now()));
                    l.setTblEmployeeByCreateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                    l.setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                    l.setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                    l.setRefRecordStatus(session.find(RefRecordStatus.class, 1));
                    session.saveOrUpdate(l);
                    //room type - price changed - history
                    historyNote += "- " + l.getTblRoomService().getServiceName()
                            + " : " + ClassFormatter.currencyFormat.cFormat(l.getTblRoomService().getPrice()) + " : \n";
                }
                //room type - price changed - history
                LogRoomTypePriceChangedHistory logRoomTypePriceChangedHistory = new LogRoomTypePriceChangedHistory();
                logRoomTypePriceChangedHistory.setTblRoomType(tblRoomType);
                logRoomTypePriceChangedHistory.setRoomTypePrice(tblRoomType.getRoomTypePrice());
                logRoomTypePriceChangedHistory.setHistoryDate(Timestamp.valueOf(LocalDateTime.now()));
                logRoomTypePriceChangedHistory.setTblEmployee(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                logRoomTypePriceChangedHistory.setHistoryNote(historyNote);
                session.saveOrUpdate(logRoomTypePriceChangedHistory);
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
        return tblRoomType;
    }

    @Override
    public boolean updateDataRoomType(TblRoomType roomType,
            List<TblRoomTypeItem> fabList,
            List<TblRoomTypeItem> amenityList,
            List<TblRoomTypeItem> propertyBarcodeList,
            List<TblRoomTypeRoomService> roomServiceList) {
        errMsg = "";
        if (ClassSession.checkUserSession()) {  //check user current session
            try {
                session = HibernateUtil.getSessionFactory().getCurrentSession();
                session.beginTransaction();
                //data room type
                roomType.setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                roomType.setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                session.update(roomType);
                //delete all data room type - item
                Query rs = session.getNamedQuery("deleteAllTblRoomTypeItem")
                        .setParameter("idRoomType", roomType.getIdroomType())
                        .setParameter("idEmployee", ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee());
                errMsg = (String) rs.uniqueResult();
                if (!errMsg.equals("")) {
                    if (session.getTransaction().isActive()) {
                        session.getTransaction().rollback();
                    }
                    return false;
                }
                //data room type - food and beverage
                for (TblRoomTypeItem l : fabList) {
                    l.setCreateDate(Timestamp.valueOf(LocalDateTime.now()));
                    l.setTblEmployeeByCreateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                    l.setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                    l.setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                    l.setRefRecordStatus(session.find(RefRecordStatus.class, 1));
                    session.saveOrUpdate(l);
                }
                //data room type- amenity
                for (TblRoomTypeItem l : amenityList) {
                    l.setCreateDate(Timestamp.valueOf(LocalDateTime.now()));
                    l.setTblEmployeeByCreateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                    l.setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                    l.setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                    l.setRefRecordStatus(session.find(RefRecordStatus.class, 1));
                    session.saveOrUpdate(l);
                }
                //data room type - property barcode
                for (TblRoomTypeItem l : propertyBarcodeList) {
                    l.setCreateDate(Timestamp.valueOf(LocalDateTime.now()));
                    l.setTblEmployeeByCreateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                    l.setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                    l.setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                    l.setRefRecordStatus(session.find(RefRecordStatus.class, 1));
                    session.saveOrUpdate(l);
                }
                //delete all data room type - room service
                rs = session.getNamedQuery("deleteAllTblRoomTypeRoomService")
                        .setParameter("idRoomType", roomType.getIdroomType())
                        .setParameter("idEmployee", ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee());
                errMsg = (String) rs.uniqueResult();
                if (!errMsg.equals("")) {
                    if (session.getTransaction().isActive()) {
                        session.getTransaction().rollback();
                    }
                    return false;
                }
                //data room type - room service
                for (TblRoomTypeRoomService l : roomServiceList) {
                    l.setCreateDate(Timestamp.valueOf(LocalDateTime.now()));
                    l.setTblEmployeeByCreateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                    l.setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                    l.setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                    l.setRefRecordStatus(session.find(RefRecordStatus.class, 1));
                    session.saveOrUpdate(l);
                }
                //room type - price changed - history
                LogRoomTypePriceChangedHistory logRoomTypePriceChangedHistory = new LogRoomTypePriceChangedHistory();
                logRoomTypePriceChangedHistory.setTblRoomType(roomType);
                logRoomTypePriceChangedHistory.setRoomTypePrice(roomType.getRoomTypePrice());
                logRoomTypePriceChangedHistory.setHistoryDate(Timestamp.valueOf(LocalDateTime.now()));
                logRoomTypePriceChangedHistory.setTblEmployee(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                logRoomTypePriceChangedHistory.setHistoryNote("");
                session.saveOrUpdate(logRoomTypePriceChangedHistory);
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
    public boolean updateDataRoomType(TblRoomType roomType,
            List<TblRoomTypeItem> itemList,
            List<TblRoomTypeRoomService> roomServiceList) {
        errMsg = "";
        if (ClassSession.checkUserSession()) {  //check user current session
            try {
                session = HibernateUtil.getSessionFactory().getCurrentSession();
                session.beginTransaction();
                //room type - price changed - history
                String historyNote = "";
                //data room type
                roomType.setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                roomType.setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                session.update(roomType);
                //delete all data room type - item
                Query rs = session.getNamedQuery("deleteAllTblRoomTypeItem")
                        .setParameter("idRoomType", roomType.getIdroomType())
                        .setParameter("idEmployee", ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee());
                errMsg = (String) rs.uniqueResult();
                if (!errMsg.equals("")) {
                    if (session.getTransaction().isActive()) {
                        session.getTransaction().rollback();
                    }
                    return false;
                }
                //room type - price changed - history
                historyNote += "Fasilitas : \n";
                //data room type - item
                for (TblRoomTypeItem l : itemList) {
                    l.setCreateDate(Timestamp.valueOf(LocalDateTime.now()));
                    l.setTblEmployeeByCreateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                    l.setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                    l.setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                    l.setRefRecordStatus(session.find(RefRecordStatus.class, 1));
                    session.saveOrUpdate(l);
                    //room type - price changed - history
                    historyNote += "- " + l.getTblItem().getItemName()
                            + " (" + l.getTblItem().getCodeItem() + ") : "
                            + ClassFormatter.decimalFormat.cFormat(l.getItemQuantity())
                            + " @" + ClassFormatter.currencyFormat.cFormat(l.getTblItem().getItemCostOfGoodsSold()) + "\n";
                }
                //delete all data room type - room service
                rs = session.getNamedQuery("deleteAllTblRoomTypeRoomService")
                        .setParameter("idRoomType", roomType.getIdroomType())
                        .setParameter("idEmployee", ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee());
                errMsg = (String) rs.uniqueResult();
                if (!errMsg.equals("")) {
                    if (session.getTransaction().isActive()) {
                        session.getTransaction().rollback();
                    }
                    return false;
                }
                //room type - price changed - history
                historyNote += "\n";
                historyNote += "Layanan : \n";
                //data room type - room service
                for (TblRoomTypeRoomService l : roomServiceList) {
                    l.setCreateDate(Timestamp.valueOf(LocalDateTime.now()));
                    l.setTblEmployeeByCreateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                    l.setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                    l.setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                    l.setRefRecordStatus(session.find(RefRecordStatus.class, 1));
                    session.saveOrUpdate(l);
                    //room type - price changed - history
                    historyNote += "- " + l.getTblRoomService().getServiceName()
                            + " : " + ClassFormatter.currencyFormat.cFormat(l.getTblRoomService().getPrice()) + " : \n";
                }
                //room type - price changed - history
                LogRoomTypePriceChangedHistory logRoomTypePriceChangedHistory = new LogRoomTypePriceChangedHistory();
                logRoomTypePriceChangedHistory.setTblRoomType(roomType);
                logRoomTypePriceChangedHistory.setRoomTypePrice(roomType.getRoomTypePrice());
                logRoomTypePriceChangedHistory.setHistoryDate(Timestamp.valueOf(LocalDateTime.now()));
                logRoomTypePriceChangedHistory.setTblEmployee(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                logRoomTypePriceChangedHistory.setHistoryNote(historyNote);
                session.saveOrUpdate(logRoomTypePriceChangedHistory);
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
    public boolean deleteDataRoomType(TblRoomType roomType) {
        errMsg = "";
        if (ClassSession.checkUserSession()) {  //check user current session
            try {
                session = HibernateUtil.getSessionFactory().getCurrentSession();
                session.beginTransaction();
                if (roomType.getRefRecordStatus().getIdstatus() == 1) {
                    //data room type
                    roomType.setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                    roomType.setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                    roomType.setRefRecordStatus(session.find(RefRecordStatus.class, 2));
                    session.update(roomType);
                    //data room type - item
                    Query rs = session.getNamedQuery("deleteAllTblRoomTypeItem")
                            .setParameter("idRoomType", roomType.getIdroomType())
                            .setParameter("idEmployee", ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee());
                    errMsg = (String) rs.uniqueResult();
                    if (!errMsg.equals("")) {
                        if (session.getTransaction().isActive()) {
                            session.getTransaction().rollback();
                        }
                        return false;
                    }
                    //data room type - room service
                    rs = session.getNamedQuery("deleteAllTblRoomTypeRoomService")
                            .setParameter("idRoomType", roomType.getIdroomType())
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
    public TblRoomService insertDataRoomService(TblRoomService roomService) {
        errMsg = "";
        TblRoomService tblRoomService = roomService;
        if (ClassSession.checkUserSession()) {  //check user current session
            try {
                session = HibernateUtil.getSessionFactory().getCurrentSession();
                session.beginTransaction();
                tblRoomService.setCodeRoomService(ClassCoder.getCode("Room Service", session));
                tblRoomService.setHotelDiscountable(false);
                tblRoomService.setCardDiscountable(false);
                tblRoomService.setCreateDate(Timestamp.valueOf(LocalDateTime.now()));
                tblRoomService.setTblEmployeeByCreateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                tblRoomService.setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                tblRoomService.setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                tblRoomService.setRefRecordStatus(session.find(RefRecordStatus.class, 1));
                session.saveOrUpdate(tblRoomService);
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
        return tblRoomService;
    }

    @Override
    public boolean updateDataRoomService(TblRoomService roomService) {
        errMsg = "";
        if (ClassSession.checkUserSession()) {  //check user current session
            try {
                session = HibernateUtil.getSessionFactory().getCurrentSession();
                session.beginTransaction();
                roomService.setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                roomService.setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                session.update(roomService);
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
    public boolean deleteDataRoomService(TblRoomService roomService) {
        errMsg = "";
        if (ClassSession.checkUserSession()) {  //check user current session
            try {
                session = HibernateUtil.getSessionFactory().getCurrentSession();
                session.beginTransaction();
                if (roomService.getRefRecordStatus().getIdstatus() == 1) {
                    roomService.setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                    roomService.setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                    roomService.setRefRecordStatus(session.find(RefRecordStatus.class, 2));
                    session.update(roomService);
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
    public TblLocation getLocation(long id) {
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
    public TblLocation getLocationByIDLocationType(int idLocationType) {
        errMsg = "";
        List<TblLocation> list = new ArrayList<>();
        if (ClassSession.checkUserSession()) {  //check user current session
            try {
                session = HibernateUtil.getSessionFactory().getCurrentSession();
                session.beginTransaction();
                list = session.getNamedQuery("findAllTblLocationByLocationTypeId")
                        .setParameter("typeId", idLocationType)
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
    public List<TblLocation> getAllDataLocation() {
        errMsg = "";
        List<TblLocation> list = new ArrayList<>();
        if (ClassSession.checkUserSession()) {  //check user current session
            try {
                session = HibernateUtil.getSessionFactory().getCurrentSession();
                session.beginTransaction();
                list = session.getNamedQuery("findAllTblLocationByLocationTypeName")
                        .setParameter("typeName", "Room (Bedroom)")
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

    //--------------------------------------------------------------------------
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

    @Override
    public List<RefRoomCleanStatus> getAllDataRoomCleanStatus() {
        errMsg = "";
        List<RefRoomCleanStatus> list = new ArrayList<>();
        if (ClassSession.checkUserSession()) {  //check user current session
            try {
                session = HibernateUtil.getSessionFactory().getCurrentSession();
                session.beginTransaction();
                list = session.getNamedQuery("findAllRefRoomCleanStatus").list();
                session.getTransaction().commit();
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
    public TblRoomTypeItem getRoomTypeFABList(long idRoomTypeItem) {
        errMsg = "";
        TblRoomTypeItem data = null;
        if (ClassSession.checkUserSession()) {  //check user current session
            try {
                session = HibernateUtil.getSessionFactory().getCurrentSession();
                session.beginTransaction();
                data = (TblRoomTypeItem) session.find(TblRoomTypeItem.class, idRoomTypeItem);
                session.getTransaction().commit();
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
    public List<TblRoomTypeItem> getAllDataRoomTypeFABList(long idRoomType) {
        errMsg = "";
        List<TblRoomTypeItem> list = new ArrayList<>();
        if (ClassSession.checkUserSession()) {  //check user current session
            try {
                session = HibernateUtil.getSessionFactory().getCurrentSession();
                session.beginTransaction();
                list = session.getNamedQuery("findAllTblRoomTypeItemByIDRoomType")
                        .setParameter("idRoomType", idRoomType)
                        .list();
                for (int i = list.size() - 1; i > -1; i--) {
                    if (!list.get(i).getTblItem().getPropertyStatus()) {   //!Property
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
    public TblRoomTypeItem getRoomTypeAmenityList(long idRoomTypeItem) {
        errMsg = "";
        TblRoomTypeItem data = null;
        if (ClassSession.checkUserSession()) {  //check user current session
            try {
                session = HibernateUtil.getSessionFactory().getCurrentSession();
                session.beginTransaction();
                data = (TblRoomTypeItem) session.find(TblRoomTypeItem.class, idRoomTypeItem);
                session.getTransaction().commit();
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
    public List<TblRoomTypeItem> getAllDataRoomTypeAmenityList(long idRoomType) {
        errMsg = "";
        List<TblRoomTypeItem> list = new ArrayList<>();
        if (ClassSession.checkUserSession()) {  //check user current session
            try {
                session = HibernateUtil.getSessionFactory().getCurrentSession();
                session.beginTransaction();
                list = session.getNamedQuery("findAllTblRoomTypeItemByIDRoomType")
                        .setParameter("idRoomType", idRoomType)
                        .list();
                for (int i = list.size() - 1; i > -1; i--) {
                    if (!list.get(i).getTblItem().getPropertyStatus()) { //!Property
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
    public TblRoomTypeItem getRoomTypePropertyBarcodeList(long idRoomTypeItem) {
        errMsg = "";
        TblRoomTypeItem data = null;
        if (ClassSession.checkUserSession()) {  //check user current session
            try {
                session = HibernateUtil.getSessionFactory().getCurrentSession();
                session.beginTransaction();
                data = (TblRoomTypeItem) session.find(TblRoomTypeItem.class, idRoomTypeItem);
                session.getTransaction().commit();
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
    public List<TblRoomTypeItem> getAllDataRoomTypePropertyBarcodeList(long idRoomType) {
        errMsg = "";
        List<TblRoomTypeItem> list = new ArrayList<>();
        if (ClassSession.checkUserSession()) {  //check user current session
            try {
                session = HibernateUtil.getSessionFactory().getCurrentSession();
                session.beginTransaction();
                list = session.getNamedQuery("findAllTblRoomTypeItemByIDRoomType")
                        .setParameter("idRoomType", idRoomType)
                        .list();
                for (int i = list.size() - 1; i > -1; i--) {
                    if (!list.get(i).getTblItem().getPropertyStatus()) {   //!Property
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
    public TblRoomTypeItem getRoomTypeItemList(long idRoomTypeItem) {
        errMsg = "";
        TblRoomTypeItem data = null;
        if (ClassSession.checkUserSession()) {  //check user current session
            try {
                session = HibernateUtil.getSessionFactory().getCurrentSession();
                session.beginTransaction();
                data = (TblRoomTypeItem) session.find(TblRoomTypeItem.class, idRoomTypeItem);
                session.getTransaction().commit();
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
    public List<TblRoomTypeItem> getAllDataRoomTypeItemList(long idRoomType) {
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
    public TblRoomTypeRoomService getRoomTypeRoomServiceList(long idRoomTypeRoomService) {
        errMsg = "";
        TblRoomTypeRoomService data = null;
        if (ClassSession.checkUserSession()) {  //check user current session
            try {
                session = HibernateUtil.getSessionFactory().getCurrentSession();
                session.beginTransaction();
                data = (TblRoomTypeRoomService) session.find(TblRoomTypeRoomService.class, idRoomTypeRoomService);
                session.getTransaction().commit();
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
    public List<TblRoomTypeRoomService> getAllDataRoomTypeRoomServiceList(long idRoomType) {
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
    public TblItem getItemFoodAndBeverage(long id) {
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
    public List<TblItem> getAllDataItemFoodAndBeverage() {
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
    public TblItem getItemAmenity(long id) {
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
    public List<TblItem> getAllDataItemAmenity() {
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
    public TblItem getItemPropertyBarcode(long id) {
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
    public List<TblItem> getAllDataItemPropertyBarcode() {
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
    public TblPropertyBarcode getPropertyBarcode(long id) {
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
    public List<TblPropertyBarcode> getAllDataPropertyBarcode() {
        errMsg = "";
        List<TblPropertyBarcode> list = new ArrayList<>();
        if (ClassSession.checkUserSession()) {  //check user current session
            try {
                session = HibernateUtil.getSessionFactory().getCurrentSession();
                session.beginTransaction();
                list = session.getNamedQuery("findAllTblPropertyBarcode").list();
                session.getTransaction().commit();
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
    public List<TblPropertyBarcode> getAllDataPropertyBarcodeByIdItem(long id) {
        errMsg = "";
        List<TblPropertyBarcode> list = new ArrayList();
        if (ClassSession.checkUserSession()) {  //check user current session
            try {
                session = HibernateUtil.getSessionFactory().getCurrentSession();
                session.beginTransaction();
                list = session.getNamedQuery("findAllTblPropertyBarcodeByIdItem").setParameter("id", id)
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
    //--------------------------------------------------------------------------

    @Override
    public List<RefLocationType> getAllDataTypeLocation() {
        errMsg = "";
        List<RefLocationType> list = new ArrayList();
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
            }
        }
        return list;
    }

    //-----------------------------------------------------------------
    @Override
    public List<TblItemLocation> getAllDataItemLocation(long id) {
        errMsg = "";
        List<TblItemLocation> list = new ArrayList();
        if (ClassSession.checkUserSession()) {  //check user current session
            try {
                session = HibernateUtil.getSessionFactory().getCurrentSession();
                session.beginTransaction();
                list = session.getNamedQuery("findAllTblItemLocationByIdLocationAndIdType").setParameter("idLoc", id).setParameter("idType", 0)
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
    public TblItemLocation getDataItemLocation(long id) {
        errMsg = "";
        TblItemLocation data = null;
        if (ClassSession.checkUserSession()) {  //check user current session
            try {
                session = HibernateUtil.getSessionFactory().getCurrentSession();
                session.beginTransaction();
                data = (TblItemLocation) session.find(TblItemLocation.class, id);
                session.getTransaction().commit();
            } catch (Exception e) {
                if (session.getTransaction().isActive()) {
                    session.getTransaction().rollback();
                }
                errMsg = e.getMessage();
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

    //-----------------------------------------------------------------
    @Override
    public List<RefItemObsoleteBy> getAllDataObsoleteBy() {
        errMsg = "";
        List<RefItemObsoleteBy> list = new ArrayList();
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
            }
        }
        return list;
    }

    @Override
    public List<TblItemLocation> getDataItemLocationByItemAndLocation(long idLoc, long idItem) {
        errMsg = "";
        List<TblItemLocation> list = new ArrayList();
        if (ClassSession.checkUserSession()) {  //check user current session
            try {
                session = HibernateUtil.getSessionFactory().getCurrentSession();
                session.beginTransaction();
                list = session.getNamedQuery("findAllItemLocationByIdLocationAndIdItem").setParameter("idLocation", idLoc).setParameter("idItem", idItem).list();
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
    public boolean updateDataRoomTransferItem(TblItemLocation sourceItemLocation,
            TblItemMutationHistory mutation,
            ClassInputLocation destinationLocation,
            ObservableList<TblPropertyBarcode> propertyBarcode) {
        errMsg = "";
        TblItemMutationHistory tblMutation = mutation;
        RefItemMutationType mutationTypeMoved = null;
        RefItemMutationType mutationTypeBin = null;
        TblItemLocation destinationItemLocation = null;
        List<TblItemLocation> getDataDestinationItemLocation = null;
        List<TblItemLocationPropertyBarcode> getDataDestinationItemLocationPropertyBarcode = null;

        TblItemMutationHistoryPropertyBarcode tblMutationPropertyBarcode = null;
        TblItemLocationPropertyBarcode destinationItemLocationBarcode = null;

        if (ClassSession.checkUserSession()) {  //check user current session
            try {
                session = HibernateUtil.getSessionFactory().getCurrentSession();
                session.beginTransaction();

                //insert table Mutation History
                //-------------------------------------------------
                session.saveOrUpdate(tblMutation);
                tblMutation.setTblItem(sourceItemLocation.getTblItem());
                tblMutation.setTblLocationByIdlocationOfSource(sourceItemLocation.getTblLocation());
                mutationTypeMoved = (RefItemMutationType) session.find(RefItemMutationType.class, 0);
                mutationTypeBin = (RefItemMutationType) session.find(RefItemMutationType.class, 2);
                if (tblMutation.getTblLocationByIdlocationOfDestination().getRefLocationType().getIdtype() != 4) {
                    tblMutation.setRefItemMutationType(mutationTypeMoved);
                } else {
                    tblMutation.setRefItemMutationType(mutationTypeBin);
                }
                Timestamp date = Timestamp.valueOf(LocalDateTime.now());
                tblMutation.setMutationDate(date);

                switch (tblMutation.getTblLocationByIdlocationOfDestination().getRefLocationType().getIdtype()) {
                    case 0:
                        tblMutation.setTblLocationByIdlocationOfDestination(destinationLocation.getRoom().getTblLocation());
                        break;
                    case 1:
                        tblMutation.setTblLocationByIdlocationOfDestination(destinationLocation.getWarehouse().getTblLocation());
                        break;
                    case 2:
                        tblMutation.setTblLocationByIdlocationOfDestination(destinationLocation.getLaundry().getTblLocation());
                        break;
                }

                //Substract item in source Location 
                sourceItemLocation.setItemQuantity(sourceItemLocation.getItemQuantity().subtract(tblMutation.getItemQuantity()));
                session.update(sourceItemLocation);

                //add item to location destination, but has done transfer  
                getDataDestinationItemLocation = session.getNamedQuery("findAllItemLocationByIdLocationAndIdItem").setParameter("idLocation", tblMutation.getTblLocationByIdlocationOfDestination().getIdlocation()).setParameter("idItem", sourceItemLocation.getTblItem().getIditem()).list();
                //System.out.println(">>"+getDataDestinationItemLocation.size());

                for (TblItemLocation getDestinationItemLocation : getDataDestinationItemLocation) {
                    if (getDestinationItemLocation.getTblItem().getIditem() == sourceItemLocation.getTblItem().getIditem() && getDestinationItemLocation.getTblLocation().getIdlocation() == tblMutation.getTblLocationByIdlocationOfDestination().getIdlocation()) {
                        destinationItemLocation = getDestinationItemLocation;
                        destinationItemLocation.setItemQuantity(destinationItemLocation.getItemQuantity().add(tblMutation.getItemQuantity()));
                        session.update(destinationItemLocation);
                    }
                }

                //add item to location destination, but has not transfer
                if (getDataDestinationItemLocation.size() == 0) {
                    destinationItemLocation = new TblItemLocation();
                    destinationItemLocation.setTblItem(mutation.getTblItem());
                    destinationItemLocation.setTblLocation(mutation.getTblLocationByIdlocationOfDestination());
                    destinationItemLocation.setItemQuantity(mutation.getItemQuantity());
                    session.saveOrUpdate(destinationItemLocation);
                }
                //------------------------------------------------------------------------------        
                //transfer item property barcode        
                if (sourceItemLocation.getTblItem().getPropertyStatus()) {    //Property
                    int count = 0;
                    for (int i = 0; i < propertyBarcode.size(); i++) {
                        TblPropertyBarcode destinationItemBarcode = propertyBarcode.get(i);
//                        if (destinationItemBarcode.getIsMoved() == true) {

                        tblMutationPropertyBarcode = new TblItemMutationHistoryPropertyBarcode();
                        tblMutationPropertyBarcode.setTblItemMutationHistory(tblMutation);
                        tblMutationPropertyBarcode.setTblPropertyBarcode(destinationItemBarcode);
                        session.saveOrUpdate(tblMutationPropertyBarcode);

                        getDataDestinationItemLocationPropertyBarcode = session.getNamedQuery("findAllTblItemLocationPropertyBarcodeByItemLocationAndPropertyBarcode").setParameter("idrelation", destinationItemLocation.getIdrelation()).setParameter("idproperty", destinationItemBarcode).list();

                        for (TblItemLocationPropertyBarcode getDestinationItemPropertyBarcode : getDataDestinationItemLocationPropertyBarcode) {
                            if (destinationItemLocation.getIdrelation() == getDestinationItemPropertyBarcode.getTblItemLocation().getIdrelation() && destinationItemBarcode.getIdbarcode() == getDestinationItemPropertyBarcode.getTblPropertyBarcode().getIdbarcode()) {
                                destinationItemLocationBarcode = getDestinationItemPropertyBarcode;
                                destinationItemLocationBarcode.setTblItemLocation(destinationItemLocation);
                                session.update(destinationItemLocationBarcode);
                            }
                        }

                        if (getDataDestinationItemLocationPropertyBarcode.size() == 0) {
                            destinationItemLocationBarcode = new TblItemLocationPropertyBarcode();
                            destinationItemLocationBarcode.setTblItemLocation(destinationItemLocation);
                            destinationItemLocationBarcode.setTblPropertyBarcode(destinationItemBarcode);
                            session.saveOrUpdate(destinationItemLocationBarcode);
                        }
//                        }
                    }
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
    public List<TblLocationOfLaundry> getAllDataLaundry() {
        errMsg = "";
        List<TblLocationOfLaundry> list = new ArrayList();
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

    //---------------------------------------------
    @Override
    public List<ClassDataMutation> getAllDataMutationHistory() {
        errMsg = "";
        List<ClassDataMutation> list = new ArrayList();
        if (ClassSession.checkUserSession()) {  //check user current session
            try {
                session = HibernateUtil.getSessionFactory().getCurrentSession();
                session.beginTransaction();
                List<TblItemMutationHistory> listMutationHistory = session.getNamedQuery("findAllTblItemMutationHistoryByLocationSource").setParameter("idType", 0).list();
                List<TblItemMutationHistoryPropertyBarcode> listMutationHistoryPropertyBarcode = session.getNamedQuery("findAllTblItemMutationHistoryPropertyBarcodeByIdLocation").setParameter("idType", 0).list();

                for (TblItemMutationHistory dataItemMutation : listMutationHistory) {
                    //System.out.println("hsl Mutation>>"+dataItemMutation.getTblItem().getItemName());
                    if (dataItemMutation.getTblItem().getPropertyStatus()) {  //Property
                        for (TblItemMutationHistoryPropertyBarcode dataItemMutationPropertyBarcode : listMutationHistoryPropertyBarcode) {
                            if (dataItemMutationPropertyBarcode.getTblItemMutationHistory().getIdmutation()
                                    == dataItemMutation.getIdmutation()) {
                                //System.out.println("hsl history Property Barcode>>"+dataItemMutationPropertyBarcode.getTblItemMutationHistory().getTblItem().getItemName());
                                ClassDataMutation getClassDataMutationPropertyBarcode = new ClassDataMutation();
                                getClassDataMutationPropertyBarcode.setMutationHistoryPropertyBarcode(dataItemMutationPropertyBarcode);
                                getClassDataMutationPropertyBarcode.setMutationHistory(dataItemMutationPropertyBarcode.getTblItemMutationHistory());
                                TblPropertyBarcode propertyBarcode = session.find(TblPropertyBarcode.class, dataItemMutationPropertyBarcode.getTblPropertyBarcode().getIdbarcode());
                                getClassDataMutationPropertyBarcode.getMutationHistoryPropertyBarcode().getTblPropertyBarcode().setCodeBarcode(propertyBarcode.getCodeBarcode());
                                list.add(getClassDataMutationPropertyBarcode);
                                break;
                            }
                        }
                    } else {
                        ClassDataMutation getClassDataMutation = new ClassDataMutation();
                        getClassDataMutation.setMutationHistory(dataItemMutation);
                        getClassDataMutation.getMutationHistory().setTblLocationByIdlocationOfSource(session.find(TblLocation.class, getClassDataMutation.getMutationHistory().getTblLocationByIdlocationOfSource().getIdlocation()));
                        //getClassDataTransferItem.setTblItemLocationPropertyBarcode(null);
                        list.add(getClassDataMutation);
                    }
                }

//            for (TblItemMutationHistoryPropertyBarcode dataItemMutationPropertyBarcode : listMutationHistoryPropertyBarcode) {
//                ClassDataMutation getClassDataMutationPropertyBarcode = new ClassDataMutation();
//                getClassDataMutationPropertyBarcode.setMutationHistoryPropertyBarcode(dataItemMutationPropertyBarcode);
//                getClassDataMutationPropertyBarcode.setMutationHistory(dataItemMutationPropertyBarcode.getTblItemMutationHistory());
//                TblPropertyBarcode propertyBarcode = session.find(TblPropertyBarcode.class, dataItemMutationPropertyBarcode.getTblPropertyBarcode().getIdbarcode());
//                getClassDataMutationPropertyBarcode.getMutationHistoryPropertyBarcode().getTblPropertyBarcode().setCodeBarcode(propertyBarcode.getCodeBarcode());
//                getClassDataMutationPropertyBarcode.setIsItem(false);
//                list.add(getClassDataMutationPropertyBarcode);
//            }
//
//            for (TblItemMutationHistory dataItemMutation : listMutationHistory) {
//                if (!dataItemMutation.getTblItem().getPropertyStatus()) {  //!Property
//                    ClassDataMutation getClassDataMutation = new ClassDataMutation();
//                    getClassDataMutation.setMutationHistory(dataItemMutation);
//                    getClassDataMutation.getMutationHistory().setTblLocationByIdlocationOfSource(session.find(TblLocation.class, getClassDataMutation.getMutationHistory().getTblLocationByIdlocationOfSource().getIdlocation()));
//                    getClassDataMutation.setIsItem(true);
//                    //getClassDataTransferItem.setTblItemLocationPropertyBarcode(null);
//                    list.add(getClassDataMutation);
//                }
//            }

                /*for(ClassDataMutation dataMutation : list){
                 System.out.println("Hasil Mutation>>"+dataMutation.getMutationHistory().getTblItem().getItemName());
                 }*/
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
    public List<TblLocationOfWarehouse> getWarehouseByIdLocation(long id) {
        errMsg = "";
        List<TblLocationOfWarehouse> list = new ArrayList();
        if (ClassSession.checkUserSession()) {  //check user current session
            try {
                session = HibernateUtil.getSessionFactory().getCurrentSession();
                session.beginTransaction();
                list = session.getNamedQuery("findAllTblLocationOfWarehouseByIdLocation").setParameter("id", id).list();
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
    public List<TblRoom> getRoomByIdLocation(long id) {
        errMsg = "";
        List<TblRoom> list = new ArrayList();
        if (ClassSession.checkUserSession()) {  //check user current session
            try {
                session = HibernateUtil.getSessionFactory().getCurrentSession();
                session.beginTransaction();
                list = session.getNamedQuery("findAllTblRoomByIdLocation").setParameter("id", id).list();
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
    public List<TblLocationOfLaundry> getLaundryByIdLocation(long id) {
        errMsg = "";
        List<TblLocationOfLaundry> list = new ArrayList();
        if (ClassSession.checkUserSession()) {  //check user current session
            try {
                session = HibernateUtil.getSessionFactory().getCurrentSession();
                session.beginTransaction();
                list = session.getNamedQuery("findAllTblLocationOfLaundryByIdLocation").setParameter("id", id).list();

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

    /* @Override
     public boolean updateDataRoomTransferItem(TblItemLocation sourceItemLocation,TblItemMutationHistory mutation,ClassInputLocation destinationLocation,ObservableList<TblPropertyBarcode>propertyBarcode){
     errMsg = "";
     TblItemMutationHistory tblMutation = mutation;
     RefItemMutationType mutationTypeMoved = null;
     RefItemMutationType mutationTypeBin = null;
     TblItemLocation destinationItemLocation = null;
     List<TblItemLocation>getDataDestinationItemLocation = null;
     List<TblItemLocationPropertyBarcode>getDataDestinationItemLocationPropertyBarcode = null;
      
     TblItemMutationHistoryPropertyBarcode tblMutationPropertyBarcode= null;
     TblItemLocationPropertyBarcode destinationItemLocationBarcode = null;
      
     try
     {
     session = HibernateUtil.getSessionFactory().getCurrentSession();
     session.beginTransaction();
        
          
     //insert table Mutation History
     //-------------------------------------------------
     session.saveOrUpdate(tblMutation);
     tblMutation.setTblItem(sourceItemLocation.getTblItem());
     tblMutation.setTblLocationByIdlocationOfSource(sourceItemLocation.getTblLocation());
     mutationTypeMoved = (RefItemMutationType)session.find(RefItemMutationType.class,0);
     mutationTypeBin = (RefItemMutationType)session.find(RefItemMutationType.class,2);
     if(tblMutation.getTblLocationByIdlocationOfDestination().getRefLocationType().getIdtype()!=4)
     {
     tblMutation.setRefItemMutationType(mutationTypeMoved);
     }
     else
     {
     tblMutation.setRefItemMutationType(mutationTypeBin);
     }
     Date date = new Date();
     tblMutation.setMutationDate(date);
        
     switch(tblMutation.getTblLocationByIdlocationOfDestination().getRefLocationType().getIdtype()){
     case 0:
     tblMutation.setTblLocationByIdlocationOfDestination(destinationLocation.getRoom().getTblLocation());
     break;
     case 1:
     tblMutation.setTblLocationByIdlocationOfDestination(destinationLocation.getWarehouse().getTblLocation());
     break;
     case 2:
     tblMutation.setTblLocationByIdlocationOfDestination(destinationLocation.getLaundry().getTblLocation());
     break;
     }
        
     //Substract item in source Location 
     sourceItemLocation.setItemQuantity(sourceItemLocation.getItemQuantity().subtract(tblMutation.getItemQuantity()));
     session.update(sourceItemLocation);
        
     //add item to location destination, but has done transfer  
     getDataDestinationItemLocation  = session.getNamedQuery("findAllItemLocationByIdLocationAndIdItem").setParameter("idLocation",tblMutation.getTblLocationByIdlocationOfDestination().getIdlocation()).setParameter("idItem",sourceItemLocation.getTblItem().getIditem()).list();
     //System.out.println(">>"+getDataDestinationItemLocation.size());
        
     for(TblItemLocation getDestinationItemLocation : getDataDestinationItemLocation){
     if(getDestinationItemLocation.getTblItem().getIditem()==sourceItemLocation.getTblItem().getIditem() && getDestinationItemLocation.getTblLocation().getIdlocation()==tblMutation.getTblLocationByIdlocationOfDestination().getIdlocation()){
     destinationItemLocation = getDestinationItemLocation;
     destinationItemLocation.setItemQuantity(destinationItemLocation.getItemQuantity().add(tblMutation.getItemQuantity()));
     session.update(destinationItemLocation);
     }
     }
        
     //add item to location destination, but has not transfer
     if(getDataDestinationItemLocation.size()==0)
     {
     destinationItemLocation = new TblItemLocation();
     destinationItemLocation.setTblItem(mutation.getTblItem());
     destinationItemLocation.setTblLocation(mutation.getTblLocationByIdlocationOfDestination());
     destinationItemLocation.setItemQuantity(mutation.getItemQuantity());
     session.saveOrUpdate(destinationItemLocation);
     }
     //------------------------------------------------------------------------------        
     //transfer item property barcode        
     if(sourceItemLocation.getTblItem().getPropertyStatus()){   //Property
     int count = 0;
     for(int i = 0; i<propertyBarcode.size();i++){
     TblPropertyBarcode destinationItemBarcode = propertyBarcode.get(i);
     if(destinationItemBarcode.getIsMoved()==true){
          
     tblMutationPropertyBarcode = new TblItemMutationHistoryPropertyBarcode();
     tblMutationPropertyBarcode.setTblItemMutationHistory(tblMutation);
     tblMutationPropertyBarcode.setTblPropertyBarcode(destinationItemBarcode);
     session.saveOrUpdate(tblMutationPropertyBarcode);
          
     getDataDestinationItemLocationPropertyBarcode = session.getNamedQuery("findAllTblItemLocationPropertyBarcodeByItemLocationAndPropertyBarcode").setParameter("idrelation",destinationItemLocation.getIdrelation()).setParameter("idproperty",destinationItemBarcode).list();
          
     for(TblItemLocationPropertyBarcode getDestinationItemPropertyBarcode : getDataDestinationItemLocationPropertyBarcode){
     if(destinationItemLocation.getIdrelation()==getDestinationItemPropertyBarcode.getTblItemLocation().getIdrelation() && destinationItemBarcode.getIdbarcode()== getDestinationItemPropertyBarcode.getTblPropertyBarcode().getIdbarcode()){
     destinationItemLocationBarcode = getDestinationItemPropertyBarcode;
     destinationItemLocationBarcode.setTblItemLocation(destinationItemLocation);
     session.update(destinationItemLocationBarcode);
     }
     }
       
     if(getDataDestinationItemLocationPropertyBarcode.size()==0){
     destinationItemLocationBarcode = new TblItemLocationPropertyBarcode();
     destinationItemLocationBarcode.setTblItemLocation(destinationItemLocation);
     destinationItemLocationBarcode.setTblPropertyBarcode(destinationItemBarcode);
     session.saveOrUpdate(destinationItemLocationBarcode); 
     }
     } 
     }    
     }
        
     session.getTransaction().commit();
     }
     catch(Exception e)
     {
     if(session.getTransaction().isActive())
     {
     session.getTransaction().rollback();
     }
     errMsg = e.getMessage();
     return false;
     }
     return true;
     }
    
     @Override
     public List<TblLocationOfLaundry>getAllDataLaundry(){
     errMsg = "";
     session = HibernateUtil.getSessionFactory().getCurrentSession();
     List<TblLocationOfLaundry>list = new ArrayList();
     try
     {
     session.beginTransaction();
     list = session.getNamedQuery("findAllTblLocationOfLaundry").list();
     session.getTransaction().commit();
     }
     catch(Exception e)
     {
     if(session.getTransaction().isActive())
     {
     session.getTransaction().rollback();
     }
     errMsg = e.getMessage();
     }
     return list;
     }
     
     @Override
     public List<TblLocationOfWarehouse> getAllDataWarehouse() {
     errMsg = "";
     List<TblLocationOfWarehouse> list = new ArrayList<>();
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
     return list;
     }
   
     public List<ClassDataTransferItem> getAllDataTransferItem(long id){
     List<ClassDataTransferItem>list = new ArrayList();
     errMsg = "";
     try
     {
     session = HibernateUtil.getSessionFactory().getCurrentSession();
     session.beginTransaction();
     List<TblItemLocation>listItemLocation = session.getNamedQuery("findAllTblItemLocationByIdLocationAndIdType").setParameter("idType",1).setParameter("idLoc",id).list();
     List<TblItemLocationPropertyBarcode>listItemLocationPropertyBarcode = session.getNamedQuery("findAllTblItemLocationPropertyBarcodeByIdLocationAndIdType").setParameter("idType",1).setParameter("idLoc",id).list();
         
      
     for(TblItemLocationPropertyBarcode dataItemLocationPropertyBarcode : listItemLocationPropertyBarcode){
     //System.out.println("hsl Item Location Property Barcode>>"+dataItemLocationPropertyBarcode.getTblItemLocation().getTblItem().getItemName());
     ClassDataTransferItem getClassDataTransferItemPropertyBarcode = new ClassDataTransferItem();
     getClassDataTransferItemPropertyBarcode.setTblItemLocation(dataItemLocationPropertyBarcode.getTblItemLocation());
     TblPropertyBarcode propertyBarcode = session.find(TblPropertyBarcode.class,dataItemLocationPropertyBarcode.getTblPropertyBarcode().getIdbarcode());
     getClassDataTransferItemPropertyBarcode.setTblItemLocationPropertyBarcode(dataItemLocationPropertyBarcode);
     getClassDataTransferItemPropertyBarcode.getTblItemLocationPropertyBarcode().getTblPropertyBarcode().setCodeBarcode(propertyBarcode.getCodeBarcode());
     list.add(getClassDataTransferItemPropertyBarcode);
     }
           
     for(TblItemLocation dataItemLocation : listItemLocation){
     if(!dataItemLocation.getTblItem().getPropertyStatus()){ //!Property
     ClassDataTransferItem getClassDataTransferItem = new ClassDataTransferItem();
     getClassDataTransferItem.setTblItemLocation(dataItemLocation);
                
     //getClassDataTransferItem.setTblItemLocationPropertyBarcode(null);
     list.add(getClassDataTransferItem);
     }
     }
             
     //System.out.println("hsl Item Location Property Barcode>>"+dataItemLocationPropertyBarcode.getTblItemLocation().getTblItem().getItemName());
              
              
          
          
     /*  for(ClassDataTransferItem dataItemTransfer : list){
         
     System.out.println("Hasil Transfer Item>>"+dataItemTransfer.getTblItemLocation().getTblItem().getItemName());
     /* if(dataItemTransfer.getTblItemLocationPropertyBarcode()==null){
     dataItemTransfer.getTblItemLocationPropertyBarcode().getTblPropertyBarcode().setCodeBarcode("null");
     }*/
    /*if(dataItemTransfer.getTblItemLocationPropertyBarcode()==null){
     System.out.println("Kosong");   
     }
     else{
     System.out.println("Hasil Item Barcode>>"+dataItemTransfer.getTblItemLocationPropertyBarcode().getTblPropertyBarcode().getCodeBarcode());  
     }
           
          
     }
          
     //list.add(getClassDataTransferItem);
     session.getTransaction().commit();
     }
     catch(Exception e)
     {
     errMsg = e.getMessage();
     }
     return list;
     }*/
    /* @Override
     public boolean updateDataRoomTransferItem(TblItemLocation sourceItemLocation,TblItemMutationHistory mutation,ClassInputLocation destinationLocation,TblItemLocationPropertyBarcode propertyBarcode){
     errMsg = "";
     TblItemMutationHistory tblMutation = mutation;
     RefItemMutationType mutationTypeMoved = null;
     RefItemMutationType mutationTypeBin = null;
      
     TblItemLocation destinationItemLocation = null;
     List<TblItemLocation>getDataDestinationItemLocation = null;
     List<TblItemLocationPropertyBarcode>getDataDestinationItemLocationPropertyBarcode = null;
      
     TblItemMutationHistoryPropertyBarcode tblMutationPropertyBarcode= null;
     TblItemLocationPropertyBarcode destinationItemLocationBarcode = null;
     TblItemLocationPropertyBarcode sourceItemLocationBarcode = null;
      
     //System.out.println("Masuk>>"+propertyBarcode.getTblPropertyBarcode().getCodeBarcode());
     try
     {
     session = HibernateUtil.getSessionFactory().getCurrentSession();
     session.beginTransaction();
        
     //System.out.println("Item Quantity>>"+mutation.getItemQuantity());
          
     //insert table Mutation History
     //-------------------------------------------------
     session.saveOrUpdate(tblMutation);
     tblMutation.setTblItem(sourceItemLocation.getTblItem());
     tblMutation.setTblLocationByIdlocationOfSource(sourceItemLocation.getTblLocation());
     mutationTypeMoved = (RefItemMutationType)session.find(RefItemMutationType.class,0);
     mutationTypeBin = (RefItemMutationType)session.find(RefItemMutationType.class,2);
     if(tblMutation.getTblLocationByIdlocationOfDestination().getRefLocationType().getIdtype()!=4)
     {
     tblMutation.setRefItemMutationType(mutationTypeMoved);
     }
     else
     {
     tblMutation.setRefItemMutationType(mutationTypeBin);
     }
     Date date = new Date();
     tblMutation.setMutationDate(date);
        
     switch(tblMutation.getTblLocationByIdlocationOfDestination().getRefLocationType().getIdtype()){
     case 0:
     tblMutation.setTblLocationByIdlocationOfDestination(destinationLocation.getRoom().getTblLocation());
     break;
     case 1:
     tblMutation.setTblLocationByIdlocationOfDestination(destinationLocation.getWarehouse().getTblLocation());
     break;
     case 2:
     tblMutation.setTblLocationByIdlocationOfDestination(destinationLocation.getLaundry().getTblLocation());
     break;
     }
        
     //Substract item in source Location 
     sourceItemLocation.setItemQuantity(sourceItemLocation.getItemQuantity().subtract(tblMutation.getItemQuantity()));
     session.update(sourceItemLocation);
        
     //add item to location destination, but has done transfer  
     getDataDestinationItemLocation  = session.getNamedQuery("findAllItemLocationByIdLocationAndIdItem").setParameter("idLocation",tblMutation.getTblLocationByIdlocationOfDestination().getIdlocation()).setParameter("idItem",sourceItemLocation.getTblItem().getIditem()).list();
     //System.out.println(">>"+getDataDestinationItemLocation.size());
        
     for(TblItemLocation getDestinationItemLocation : getDataDestinationItemLocation){
     if(getDestinationItemLocation.getTblItem().getIditem()==sourceItemLocation.getTblItem().getIditem() && getDestinationItemLocation.getTblLocation().getIdlocation()==tblMutation.getTblLocationByIdlocationOfDestination().getIdlocation()){
     destinationItemLocation = getDestinationItemLocation;
     destinationItemLocation.setItemQuantity(destinationItemLocation.getItemQuantity().add(tblMutation.getItemQuantity()));
     session.update(destinationItemLocation);
     }
     }
        
     //add item to location destination, but has not transfer
     if(getDataDestinationItemLocation.size()==0)
     {
     destinationItemLocation = new TblItemLocation();
     destinationItemLocation.setTblItem(mutation.getTblItem());
     destinationItemLocation.setTblLocation(mutation.getTblLocationByIdlocationOfDestination());
     destinationItemLocation.setItemQuantity(mutation.getItemQuantity());
     session.saveOrUpdate(destinationItemLocation);
     }
     //------------------------------------------------------------------------------        
     //transfer item property barcode        
     if(sourceItemLocation.getTblItem().getPropertyStatus()){ //Property
     //System.out.println("tblMutationPropertyBarcode:"+propertyBarcode.getTblPropertyBarcode().getCodeBarcode());
     tblMutationPropertyBarcode = new TblItemMutationHistoryPropertyBarcode();
     session.saveOrUpdate(tblMutationPropertyBarcode);
     tblMutationPropertyBarcode.setTblItemMutationHistory(tblMutation);
     tblMutationPropertyBarcode.setTblPropertyBarcode(propertyBarcode.getTblPropertyBarcode());
          
        
     //System.out.println("hsl>>"+tblMutationPropertyBarcode.getTblPropertyBarcode().getCodeBarcode());
     //session.saveOrUpdate(tblMutationPropertyBarcode);
          
     sourceItemLocationBarcode = new TblItemLocationPropertyBarcode();
     sourceItemLocationBarcode.setTblItemLocation(sourceItemLocation);
     session.update(sourceItemLocation);
         
     getDataDestinationItemLocationPropertyBarcode = session.getNamedQuery("findAllTblItemLocationPropertyBarcodeByItemLocationAndPropertyBarcode").setParameter("idrelation",destinationItemLocation.getIdrelation()).setParameter("idproperty",propertyBarcode.getTblPropertyBarcode().getIdbarcode()).list();
          
     for(TblItemLocationPropertyBarcode getDestinationItemPropertyBarcode : getDataDestinationItemLocationPropertyBarcode){
     if(destinationItemLocation.getIdrelation()==getDestinationItemPropertyBarcode.getTblItemLocation().getIdrelation() && propertyBarcode.getTblPropertyBarcode().getIdbarcode()== getDestinationItemPropertyBarcode.getTblPropertyBarcode().getIdbarcode()){
                
     destinationItemLocationBarcode = getDestinationItemPropertyBarcode;
     destinationItemLocationBarcode.setTblItemLocation(destinationItemLocation);
     session.update(destinationItemLocationBarcode);
     }
     }
       
     if(getDataDestinationItemLocationPropertyBarcode.size()==0){
     destinationItemLocationBarcode = new TblItemLocationPropertyBarcode();
     destinationItemLocationBarcode.setTblItemLocation(destinationItemLocation);
     destinationItemLocationBarcode.setTblPropertyBarcode(propertyBarcode.getTblPropertyBarcode());
     session.saveOrUpdate(destinationItemLocationBarcode); 
     }
       
       
     }       
     session.getTransaction().commit();
     }
     catch(Exception e)
     {
     if(session.getTransaction().isActive())
     {
     session.getTransaction().rollback();
     }
     errMsg = e.getMessage();
     return false;
     }
     return true;
     }*/
//    @Override
//    public boolean updateDataRoomTransferItem(ClassDataTransferItem transferItem, TblItemMutationHistory mutation, ClassInputLocation destinationLocation) {
//        errMsg = "";
//        TblItemMutationHistory tblMutation = mutation;
//
//        RefItemMutationType mutationTypeMoved = null;
//        RefItemMutationType mutationTypeBin = null;
//
//        TblItemLocation sourceItemLocation = transferItem.getTblItemLocation();
//
//        tblMutation.setLastUpdateDate(java.sql.Timestamp.valueOf(LocalDateTime.now()));
//        TblItemLocation destinationItemLocation = null;
//        List<TblItemLocation> getDataDestinationItemLocation = null;
//        List<TblItemLocationPropertyBarcode> getDataDestinationItemLocationPropertyBarcode = null;
//
//        TblItemMutationHistoryPropertyBarcode tblMutationPropertyBarcode = null;
//        TblItemLocationPropertyBarcode destinationItemLocationBarcode = null;
//        TblItemLocationPropertyBarcode sourceItemLocationBarcode = null;
//
//        //System.out.println("Masuk>>"+propertyBarcode.getTblPropertyBarcode().getCodeBarcode());
//        try {
//            session = HibernateUtil.getSessionFactory().getCurrentSession();
//            session.beginTransaction();
//            tblMutation.setRefRecordStatus(session.find(RefRecordStatus.class, 1));
//            tblMutation.setTblEmployeeByCreateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
//            tblMutation.setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
//            tblMutation.setCreateDate(java.sql.Timestamp.valueOf(LocalDateTime.now()));
//            tblMutation.setLastUpdateDate(java.sql.Timestamp.valueOf(LocalDateTime.now()));
//          //System.out.println("Item Quantity>>"+mutation.getItemQuantity());
//
// //insert table Mutation History
//            //-------------------------------------------------
//            session.saveOrUpdate(tblMutation);
//            tblMutation.setTblItem(sourceItemLocation.getTblItem());
//            tblMutation.setTblLocationByIdlocationOfSource(sourceItemLocation.getTblLocation());
//            mutationTypeMoved = (RefItemMutationType) session.find(RefItemMutationType.class, 0);
//            mutationTypeBin = (RefItemMutationType) session.find(RefItemMutationType.class, 2);
//            if (tblMutation.getTblLocationByIdlocationOfDestination().getRefLocationType().getIdtype() != 4) {
//                tblMutation.setRefItemMutationType(mutationTypeMoved);
//            } else {
//                tblMutation.setRefItemMutationType(mutationTypeBin);
//            }
//            //Date date = new Date();
//            tblMutation.setMutationDate(java.sql.Timestamp.valueOf(LocalDateTime.now()));
//
//            switch (tblMutation.getTblLocationByIdlocationOfDestination().getRefLocationType().getIdtype()) {
//                case 0:
//                    tblMutation.setTblLocationByIdlocationOfDestination(destinationLocation.getRoom().getTblLocation());
//                    break;
//                case 1:
//                    tblMutation.setTblLocationByIdlocationOfDestination(destinationLocation.getWarehouse().getTblLocation());
//                    break;
//                case 2:
//                    tblMutation.setTblLocationByIdlocationOfDestination(destinationLocation.getLaundry().getTblLocation());
//                    break;
//            }
//
//            tblMutation.setTblEmployeeByIdemployee(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
//
//            //Substract item in source Location 
//            sourceItemLocation.setItemQuantity(sourceItemLocation.getItemQuantity().subtract(tblMutation.getItemQuantity()));
//            sourceItemLocation.setRefRecordStatus(session.find(RefRecordStatus.class, 1));
//            sourceItemLocation.setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
//            sourceItemLocation.setLastUpdateDate(java.sql.Timestamp.valueOf(LocalDateTime.now()));
//            session.update(sourceItemLocation);
//
////add item to location destination, but has done transfer  
//            getDataDestinationItemLocation = session.getNamedQuery("findAllItemLocationByIdLocationAndIdItem").setParameter("idLocation", tblMutation.getTblLocationByIdlocationOfDestination().getIdlocation()).setParameter("idItem", sourceItemLocation.getTblItem().getIditem()).list();
//        //System.out.println(">>"+getDataDestinationItemLocation.size());
//
//            for (TblItemLocation getDestinationItemLocation : getDataDestinationItemLocation) {
//                if (getDestinationItemLocation.getTblItem().getIditem() == sourceItemLocation.getTblItem().getIditem() && getDestinationItemLocation.getTblLocation().getIdlocation() == tblMutation.getTblLocationByIdlocationOfDestination().getIdlocation()) {
//                    destinationItemLocation = getDestinationItemLocation;
//                    destinationItemLocation.setItemQuantity(destinationItemLocation.getItemQuantity().add(tblMutation.getItemQuantity()));
//                    session.update(destinationItemLocation);
//                }
//            }
//
////add item to location destination, but has not transfer
//            if (getDataDestinationItemLocation.size() == 0) {
//                destinationItemLocation = new TblItemLocation();
//                destinationItemLocation.setTblItem(mutation.getTblItem());
//                destinationItemLocation.setTblLocation(mutation.getTblLocationByIdlocationOfDestination());
//                destinationItemLocation.setItemQuantity(mutation.getItemQuantity());
//                destinationItemLocation.setRefRecordStatus(session.find(RefRecordStatus.class, 1));
//                destinationItemLocation.setTblEmployeeByCreateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
//                destinationItemLocation.setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
//                destinationItemLocation.setCreateDate(java.sql.Timestamp.valueOf(LocalDateTime.now()));
//                destinationItemLocation.setLastUpdateDate(java.sql.Timestamp.valueOf(LocalDateTime.now()));
//                session.saveOrUpdate(destinationItemLocation);
//            }
////------------------------------------------------------------------------------        
////transfer item property barcode        
//            if (transferItem.getIsItem() == false) {
//                System.out.println("tblMutationPropertyBarcode:" + transferItem.getTblItemLocationPropertyBarcode().getTblPropertyBarcode().getCodeBarcode());
//                tblMutationPropertyBarcode = new TblItemMutationHistoryPropertyBarcode();
//                tblMutationPropertyBarcode.setRefRecordStatus(session.find(RefRecordStatus.class, 1));
//                tblMutationPropertyBarcode.setTblEmployeeByCreateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
//                tblMutationPropertyBarcode.setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
//                tblMutationPropertyBarcode.setCreateDate(java.sql.Timestamp.valueOf(LocalDateTime.now()));
//                tblMutationPropertyBarcode.setLastUpdateDate(java.sql.Timestamp.valueOf(LocalDateTime.now()));
//                session.saveOrUpdate(tblMutationPropertyBarcode);
//                tblMutationPropertyBarcode.setTblItemMutationHistory(tblMutation);
//                tblMutationPropertyBarcode.setTblPropertyBarcode(transferItem.getTblItemLocationPropertyBarcode().getTblPropertyBarcode());
//
//          //session.saveOrUpdate(tblMutationPropertyBarcode);
//                sourceItemLocationBarcode = transferItem.getTblItemLocationPropertyBarcode();
//                sourceItemLocationBarcode.setTblItemLocation(sourceItemLocation);
//                sourceItemLocationBarcode.setRefRecordStatus(session.find(RefRecordStatus.class, 1));
//                sourceItemLocationBarcode.setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
//                sourceItemLocationBarcode.setLastUpdateDate(java.sql.Timestamp.valueOf(LocalDateTime.now()));
//                session.update(sourceItemLocationBarcode);
//
//                getDataDestinationItemLocationPropertyBarcode = session.getNamedQuery("findAllTblItemLocationPropertyBarcodeByItemLocationAndPropertyBarcode").setParameter("idrelation", destinationItemLocation.getIdrelation()).setParameter("idproperty", transferItem.getTblItemLocationPropertyBarcode().getTblPropertyBarcode().getIdbarcode()).list();
//
//                for (TblItemLocationPropertyBarcode getDestinationItemPropertyBarcode : getDataDestinationItemLocationPropertyBarcode) {
//                    if (destinationItemLocation.getIdrelation() == getDestinationItemPropertyBarcode.getTblItemLocation().getIdrelation() && transferItem.getTblItemLocationPropertyBarcode().getTblPropertyBarcode().getIdbarcode() == getDestinationItemPropertyBarcode.getTblPropertyBarcode().getIdbarcode()) {
//
//                        destinationItemLocationBarcode = getDestinationItemPropertyBarcode;
//                        destinationItemLocationBarcode.setTblItemLocation(destinationItemLocation);
//                        destinationItemLocationBarcode.setRefRecordStatus(session.find(RefRecordStatus.class, 1));
//                        destinationItemLocationBarcode.setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
//                        destinationItemLocationBarcode.setLastUpdateDate(java.sql.Timestamp.valueOf(LocalDateTime.now()));
//                        session.update(destinationItemLocationBarcode);
//                    }
//                }
//
//                if (getDataDestinationItemLocationPropertyBarcode.size() == 0) {
//                    destinationItemLocationBarcode = new TblItemLocationPropertyBarcode();
//                    destinationItemLocationBarcode.setTblItemLocation(destinationItemLocation);
//                    destinationItemLocationBarcode.setTblPropertyBarcode(transferItem.getTblItemLocationPropertyBarcode().getTblPropertyBarcode());
//                    destinationItemLocationBarcode.setRefRecordStatus(session.find(RefRecordStatus.class, 1));
//                    destinationItemLocationBarcode.setTblEmployeeByCreateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
//                    destinationItemLocationBarcode.setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
//                    destinationItemLocationBarcode.setCreateDate(java.sql.Timestamp.valueOf(LocalDateTime.now()));
//                    destinationItemLocationBarcode.setLastUpdateDate(java.sql.Timestamp.valueOf(LocalDateTime.now()));
//                    session.saveOrUpdate(destinationItemLocationBarcode);
//                }
//
//            }
//            session.getTransaction().commit();
//        } catch (Exception e) {
//            if (session.getTransaction().isActive()) {
//                session.getTransaction().rollback();
//            }
//            errMsg = e.getMessage();
//            return false;
//        }
//        return true;
//    }
    @Override
    public boolean updateDataRoomTransferItem(ClassDataTransferItem transferItem,
            TblItemMutationHistory mutation,
            ClassInputLocation destinationLocation,
            String status) {
        errMsg = "";
        if (ClassSession.checkUserSession()) {  //check user current session
            try {
                session = HibernateUtil.getSessionFactory().getCurrentSession();
                session.beginTransaction();
                //data item mutation history
                TblItemMutationHistory tblMutation = mutation;
                tblMutation.setTblItem(transferItem.getTblItemLocation().getTblItem());
                tblMutation.setTblLocationByIdlocationOfSource(transferItem.getTblItemLocation().getTblLocation());
                //data item mutation (destionation)
                if (status.equalsIgnoreCase("Transfer")) {  //transfer
                    switch (tblMutation.getTblLocationByIdlocationOfDestination().getRefLocationType().getIdtype()) {
                        case 0:
                            tblMutation.setTblLocationByIdlocationOfDestination(destinationLocation.getRoom().getTblLocation());
                            break;
                        case 1:
                            tblMutation.setTblLocationByIdlocationOfDestination(destinationLocation.getWarehouse().getTblLocation());
                            break;
                        case 2:
                            tblMutation.setTblLocationByIdlocationOfDestination(destinationLocation.getLaundry().getTblLocation());
                            break;
                        case 3:
                            tblMutation.setTblLocationByIdlocationOfDestination(destinationLocation.getSupplier().getTblLocation());
                            break;
                        case 4:
                            tblMutation.setTblLocationByIdlocationOfDestination(destinationLocation.getBin().getTblLocation());
                            break;
                    }
                } else {  //broken

                }
//                tblMutation.setTblEmployeeByIdemployee(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                tblMutation.setMutationDate(Timestamp.valueOf(LocalDateTime.now()));
                tblMutation.setRefRecordStatus(session.find(RefRecordStatus.class, (int) 1));
                tblMutation.setTblEmployeeByCreateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                tblMutation.setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                tblMutation.setCreateDate(Timestamp.valueOf(LocalDateTime.now()));
                tblMutation.setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                session.saveOrUpdate(tblMutation);
                //data item location (source, -)
                transferItem.getTblItemLocation().setItemQuantity(transferItem.getTblItemLocation().getItemQuantity().subtract(tblMutation.getItemQuantity()));
                transferItem.getTblItemLocation().setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                transferItem.getTblItemLocation().setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                session.update(transferItem.getTblItemLocation());
                //data item location (source, +)
                TblItemLocation destinationItemLocation = getItemLocationByIDItemAndLocation(
                        tblMutation.getTblItem().getIditem(),
                        tblMutation.getTblLocationByIdlocationOfDestination().getIdlocation());
                if (destinationItemLocation == null) {
                    destinationItemLocation = new TblItemLocation();
                    destinationItemLocation.setTblItem(tblMutation.getTblItem());
                    destinationItemLocation.setTblLocation(tblMutation.getTblLocationByIdlocationOfDestination());
                    destinationItemLocation.setItemQuantity(tblMutation.getItemQuantity());
                    destinationItemLocation.setRefRecordStatus(session.find(RefRecordStatus.class, (int) 1));
                    destinationItemLocation.setTblEmployeeByCreateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                    destinationItemLocation.setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                    destinationItemLocation.setCreateDate(Timestamp.valueOf(LocalDateTime.now()));
                    destinationItemLocation.setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                    session.saveOrUpdate(destinationItemLocation);
                } else {
                    destinationItemLocation.setItemQuantity(destinationItemLocation.getItemQuantity().add(tblMutation.getItemQuantity()));
                    destinationItemLocation.setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                    destinationItemLocation.setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                    session.saveOrUpdate(destinationItemLocation);
                }
                //data item mutation history - property barcode
                if (transferItem.getTblItemLocationPropertyBarcode() != null) {
                    TblItemMutationHistoryPropertyBarcode tblMutationPropertyBarcode = new TblItemMutationHistoryPropertyBarcode();
                    tblMutationPropertyBarcode.setTblItemMutationHistory(tblMutation);
                    tblMutationPropertyBarcode.setTblPropertyBarcode(transferItem.getTblItemLocationPropertyBarcode().getTblPropertyBarcode());
                    tblMutationPropertyBarcode.setRefRecordStatus(session.find(RefRecordStatus.class, 1));
                    tblMutationPropertyBarcode.setTblEmployeeByCreateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                    tblMutationPropertyBarcode.setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                    tblMutationPropertyBarcode.setCreateDate(Timestamp.valueOf(LocalDateTime.now()));
                    tblMutationPropertyBarcode.setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                    session.saveOrUpdate(tblMutationPropertyBarcode);
                    //data item location - property barcode (source, remove)
                    TblItemLocationPropertyBarcode sourceItemLocationBarcode = transferItem.getTblItemLocationPropertyBarcode();
                    sourceItemLocationBarcode.setRefRecordStatus(session.find(RefRecordStatus.class, 2));
                    sourceItemLocationBarcode.setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                    sourceItemLocationBarcode.setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                    session.update(sourceItemLocationBarcode);
                    //data item location - property barcode (destination, add)
                    TblItemLocationPropertyBarcode destinationItemLocationBarcode = getItemLocationPropertyBarcodeByIDItemLocationAndDIPropertyBarcode(
                            destinationItemLocation.getIdrelation(),
                            tblMutationPropertyBarcode.getTblPropertyBarcode().getIdbarcode());
                    if (destinationItemLocationBarcode != null) {
                        destinationItemLocationBarcode.setRefRecordStatus(session.find(RefRecordStatus.class, 1));
                        destinationItemLocationBarcode.setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                        destinationItemLocationBarcode.setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                        session.saveOrUpdate(destinationItemLocationBarcode);
                    } else {
                        destinationItemLocationBarcode = new TblItemLocationPropertyBarcode();
                        destinationItemLocationBarcode.setTblItemLocation(destinationItemLocation);
                        destinationItemLocationBarcode.setTblPropertyBarcode(tblMutationPropertyBarcode.getTblPropertyBarcode());
                        destinationItemLocationBarcode.setRefRecordStatus(session.find(RefRecordStatus.class, 1));
                        destinationItemLocationBarcode.setTblEmployeeByCreateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                        destinationItemLocationBarcode.setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                        destinationItemLocationBarcode.setCreateDate(Timestamp.valueOf(LocalDateTime.now()));
                        destinationItemLocationBarcode.setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                        session.saveOrUpdate(destinationItemLocationBarcode);
                    }
                }
                //data item mutation history - item expired date
                if (transferItem.getTblItemLocationItemExpiredDate() != null) {
                    TblItemMutationHistoryItemExpiredDate imhItemExpiredDate = new TblItemMutationHistoryItemExpiredDate();
                    imhItemExpiredDate.setTblItemMutationHistory(tblMutation);
                    imhItemExpiredDate.setTblItemExpiredDate(transferItem.getTblItemLocationItemExpiredDate().getTblItemExpiredDate());
                    imhItemExpiredDate.setItemQuantity(tblMutation.getItemQuantity());
                    imhItemExpiredDate.setCreateDate(Timestamp.valueOf(LocalDateTime.now()));
                    imhItemExpiredDate.setTblEmployeeByCreateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                    imhItemExpiredDate.setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                    imhItemExpiredDate.setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                    imhItemExpiredDate.setRefRecordStatus(session.find(RefRecordStatus.class, 1));
                    session.saveOrUpdate(imhItemExpiredDate);
                    //item location - item expired date (source, -)
                    TblItemLocationItemExpiredDate iliedSource = transferItem.getTblItemLocationItemExpiredDate();
                    iliedSource.setItemQuantity(iliedSource.getItemQuantity().subtract(imhItemExpiredDate.getItemQuantity()));
                    iliedSource.setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                    iliedSource.setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                    session.update(iliedSource);
                    //item location - item expired date (destination, +)
                    TblItemLocationItemExpiredDate iliedDestination = getItemLocationItemExpiredDateByIDItemLocationAndIDItemExpiredDate(
                            destinationItemLocation.getIdrelation(),
                            imhItemExpiredDate.getTblItemExpiredDate().getIditemExpiredDate());
                    if (iliedDestination == null) {
                        iliedDestination = new TblItemLocationItemExpiredDate();
                        iliedDestination.setTblItemLocation(destinationItemLocation);
                        iliedDestination.setTblItemExpiredDate(imhItemExpiredDate.getTblItemExpiredDate());
                        iliedDestination.setItemQuantity(tblMutation.getItemQuantity());
                        iliedDestination.setCreateDate(Timestamp.valueOf(LocalDateTime.now()));
                        iliedDestination.setTblEmployeeByCreateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                        iliedDestination.setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                        iliedDestination.setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                        iliedDestination.setRefRecordStatus(session.find(RefRecordStatus.class, 1));
                        session.saveOrUpdate(iliedDestination);
                    } else {
                        iliedDestination.setItemQuantity(iliedDestination.getItemQuantity().add(imhItemExpiredDate.getItemQuantity()));
                        iliedDestination.setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                        iliedDestination.setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                        session.saveOrUpdate(iliedDestination);
                    }
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

    private TblItemLocation getItemLocationByIDItemAndLocation(long idItem,
            long idLocation) {
        List<TblItemLocation> list = session.getNamedQuery("findAllItemLocationByIdLocationAndIdItem")
                .setParameter("idItem", idItem)
                .setParameter("idLocation", idLocation)
                .list();
        return list.isEmpty() ? null : list.get(0);
    }

    private TblItemLocationPropertyBarcode getItemLocationPropertyBarcodeByIDItemLocationAndDIPropertyBarcode(long idItemLocation,
            long idPropertyBarcode) {
        List<TblItemLocationPropertyBarcode> list = session.getNamedQuery("findAllTblItemLocationPropertyBarcodeByItemLocationAndPropertyBarcode")
                .setParameter("idrelation", idItemLocation)
                .setParameter("idproperty", idPropertyBarcode)
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

    private TblItemLocationItemExpiredDate getItemLocationItemExpiredDateByIDItemLocationAndItemExpiredDate(
            long idItemLocation,
            long idItemExpiredDate) {
        List<TblItemLocationItemExpiredDate> list = session.getNamedQuery("findAllTblItemLocationItemExpiredDateByIDItemLocationAndIDItemExpiredDate")
                .setParameter("idItemLocation", idItemLocation)
                .setParameter("idItemExpiredDate", idItemExpiredDate)
                .list();
        return list.isEmpty() ? null : list.get(0);
    }

    @Override
    public boolean deleteDataItemLocationPropertyBarcode(TblItemLocationPropertyBarcode itemLocationPropertyBarcode) {
        errMsg = "";
        if (ClassSession.checkUserSession()) {  //check user current session
            try {
                session = HibernateUtil.getSessionFactory().getCurrentSession();
                session.beginTransaction();
                if (itemLocationPropertyBarcode.getRefRecordStatus().getIdstatus() == 1) {
                    itemLocationPropertyBarcode.setRefRecordStatus(session.find(RefRecordStatus.class, 2));
                    itemLocationPropertyBarcode.setLastUpdateDate(java.sql.Timestamp.valueOf(LocalDateTime.now()));
                    itemLocationPropertyBarcode.setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                    session.update(itemLocationPropertyBarcode);
                }
                session.getTransaction().commit();
            } catch (Exception e) {
                if (session.getTransaction().isActive()) {
                    errMsg = e.getMessage();
                    return false;
                }
            }
        } else {
            return false;
        }
        return true;
    }

    @Override
    public List<ClassDataTransferItem> getAllDataTransferItem(long id) {
        errMsg = "";
        List<ClassDataTransferItem> list = new ArrayList();
        if (ClassSession.checkUserSession()) {  //check user current session
            try {
                session = HibernateUtil.getSessionFactory().getCurrentSession();
                session.beginTransaction();
                List<TblItemLocation> listItemLocation = session.getNamedQuery("findAllTblItemLocationByIdLocationAndIdType").setParameter("idType", 0).setParameter("idLoc", id).list();
                List<TblItemLocationPropertyBarcode> listItemLocationPropertyBarcode = session.getNamedQuery("findAllTblItemLocationPropertyBarcodeByIdLocationAndIdType").setParameter("idType", 0).setParameter("idLoc", id).list();

                for (TblItemLocationPropertyBarcode dataItemLocationPropertyBarcode : listItemLocationPropertyBarcode) {
                    //System.out.println("hsl Item Location Property Barcode>>"+dataItemLocationPropertyBarcode.getTblItemLocation().getTblItem().getItemName());
                    ClassDataTransferItem getClassDataTransferItemPropertyBarcode = new ClassDataTransferItem();
                    getClassDataTransferItemPropertyBarcode.setTblItemLocation(dataItemLocationPropertyBarcode.getTblItemLocation());
                    TblPropertyBarcode propertyBarcode = session.find(TblPropertyBarcode.class, dataItemLocationPropertyBarcode.getTblPropertyBarcode().getIdbarcode());
                    getClassDataTransferItemPropertyBarcode.setTblItemLocationPropertyBarcode(dataItemLocationPropertyBarcode);
                    getClassDataTransferItemPropertyBarcode.getTblItemLocationPropertyBarcode().getTblPropertyBarcode().setCodeBarcode(propertyBarcode.getCodeBarcode());
                    list.add(getClassDataTransferItemPropertyBarcode);
                }

                for (TblItemLocation dataItemLocation : listItemLocation) {
                    if (!dataItemLocation.getTblItem().getPropertyStatus()) {  //!Property
                        ClassDataTransferItem getClassDataTransferItem = new ClassDataTransferItem();
                        getClassDataTransferItem.setTblItemLocation(dataItemLocation);
                        //getClassDataTransferItem.setTblItemLocationPropertyBarcode(null);
                        list.add(getClassDataTransferItem);
                    }
                }

            //System.out.println("hsl Item Location Property Barcode>>"+dataItemLocationPropertyBarcode.getTblItemLocation().getTblItem().getItemName());
            /*  for(ClassDataTransferItem dataItemTransfer : list){
         
                 System.out.println("Hasil Transfer Item>>"+dataItemTransfer.getTblItemLocation().getTblItem().getItemName());
                 /* if(dataItemTransfer.getTblItemLocationPropertyBarcode()==null){
                 dataItemTransfer.getTblItemLocationPropertyBarcode().getTblPropertyBarcode().setCodeBarcode("null");
                 }*/
                /*if(dataItemTransfer.getTblItemLocationPropertyBarcode()==null){
                 System.out.println("Kosong");   
                 }
                 else{
                 System.out.println("Hasil Item Barcode>>"+dataItemTransfer.getTblItemLocationPropertyBarcode().getTblPropertyBarcode().getCodeBarcode());  
                 }
           
          
                 }*/
                //list.add(getClassDataTransferItem);
                session.getTransaction().commit();
            } catch (Exception e) {
                errMsg = e.getMessage();
            }
        }
        return list;
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public TblItemLocationPropertyBarcode getDataItemLocationPropertyBarcode(long id) {
        errMsg = "";
        TblItemLocationPropertyBarcode data = null;
        if (ClassSession.checkUserSession()) {  //check user current session
            try {
                session = HibernateUtil.getSessionFactory().getCurrentSession();
                session.beginTransaction();
                data = (TblItemLocationPropertyBarcode) session.find(TblItemLocationPropertyBarcode.class, id);
                session.getTransaction().commit();
            } catch (Exception e) {
                if (session.getTransaction().isActive()) {
                    session.getTransaction().rollback();
                }
                errMsg = e.getMessage();
            }
        }
        return data;
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
            }
        }
        return data;
    }

    @Override
    public List<TblSupplier> getSupplierByIdLocation(long id) {
        errMsg = "";
        List<TblSupplier> list = new ArrayList();
        if (ClassSession.checkUserSession()) {  //check user current session
            try {
                session = HibernateUtil.getSessionFactory().getCurrentSession();
                session.beginTransaction();
                list = session.getNamedQuery("findAllTblSupplierByIdLocation").setParameter("id", id).list();

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
    public List<TblLocationOfBin> getBinByIdLocation(long id) {
        errMsg = "";
        List<TblLocationOfBin> list = new ArrayList();
        if (ClassSession.checkUserSession()) {  //check user current session
            try {
                session = HibernateUtil.getSessionFactory().getCurrentSession();
                session.beginTransaction();
                list = session.getNamedQuery("findAllTblLocationOfBinByIdLocation").setParameter("id", id).list();

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
    public List<TblSupplier> getAllDataSupplier() {
        errMsg = "";
        List<TblSupplier> list = new ArrayList();
        if (ClassSession.checkUserSession()) {  //check user current session
            try {
                session = HibernateUtil.getSessionFactory().getCurrentSession();
                session.beginTransaction();
                list = session.getNamedQuery("findAllTblSupplier").list();
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
    public List<TblLocationOfBin> getAllDataBin() {
        errMsg = "";
        List<TblLocationOfBin> list = new ArrayList();
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
            }
        }
        return list;
    }

    @Override
    public RefItemMutationType getMutationType(int id) {
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

    //---------------------------------------------
    @Override
    public TblItemLocationItemExpiredDate getDataItemLocationItemExpiredDate(long id) {
        errMsg = "";
        TblItemLocationItemExpiredDate data = null;
        if (ClassSession.checkUserSession()) {  //check user current session
            try {
                session = HibernateUtil.getSessionFactory().getCurrentSession();
                session.beginTransaction();
                data = (TblItemLocationItemExpiredDate) session.find(TblItemLocationItemExpiredDate.class, id);
                session.getTransaction().commit();
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
    public List<TblItemLocationItemExpiredDate> getAllDataItemLocationItemExpiredDateByIDItemLocation(long idItemLocation) {
        errMsg = "";
        List<TblItemLocationItemExpiredDate> list = new ArrayList<>();
        if (ClassSession.checkUserSession()) {  //check user current session
            try {
                session = HibernateUtil.getSessionFactory().getCurrentSession();
                session.beginTransaction();
                list = session.getNamedQuery("findAllTblItemLocationItemExpiredDateByIDItemLocation")
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
    public List<TblItemMutationHistory> getAllDataItemMutationHistory() {
        errMsg = "";
        List<TblItemMutationHistory> list = new ArrayList<>();
        if (ClassSession.checkUserSession()) {  //check user current session
            try {
                session = HibernateUtil.getSessionFactory().getCurrentSession();
                session.beginTransaction();
                list = session.getNamedQuery("findAllTblItemMutationHistory")
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
    public TblItemMutationHistoryPropertyBarcode getDataItemMutationHistoryPropertyBarcodeByIDItemMutationHistory(long idItemMutation) {
        errMsg = "";
        List<TblItemMutationHistoryPropertyBarcode> list = new ArrayList<>();
        if (ClassSession.checkUserSession()) {  //check user current session
            try {
                session = HibernateUtil.getSessionFactory().getCurrentSession();
                session.beginTransaction();
                list = session.getNamedQuery("findAllTblItemMutationHistoryPropertyBarcodeByIDItemMutationHistory")
                        .setParameter("idItemMutationHistory", idItemMutation)
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
    public List<TblItemMutationHistoryItemExpiredDate> getAllDataItemMutationHistoryItemExpiredDateByIDItemMutationHistory(
            long idItemMutation) {
        errMsg = "";
        List<TblItemMutationHistoryItemExpiredDate> list = new ArrayList<>();
        if (ClassSession.checkUserSession()) {  //check user current session
            try {
                session = HibernateUtil.getSessionFactory().getCurrentSession();
                session.beginTransaction();
                list = session.getNamedQuery("findAllTblItemMutationHistoryItemExpiredDateByIDItemMutationHistory")
                        .setParameter("idItemMutationHistory", idItemMutation)
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
                list = session.getNamedQuery("findAllTblEmployee")
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
