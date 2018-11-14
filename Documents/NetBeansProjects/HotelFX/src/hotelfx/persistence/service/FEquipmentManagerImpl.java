/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hotelfx.persistence.service;

import hotelfx.helper.ClassCoder;
import hotelfx.helper.ClassSession;
import hotelfx.persistence.HibernateUtil;
import hotelfx.persistence.model.RefItemExpiredDateStatus;
import hotelfx.persistence.model.RefItemGuestType;
import hotelfx.persistence.model.RefRecordStatus;
import hotelfx.persistence.model.SysDataHardCode;
import hotelfx.persistence.model.TblEmployee;
import hotelfx.persistence.model.TblItem;
import hotelfx.persistence.model.TblItemExpiredDate;
import hotelfx.persistence.model.TblItemLocation;
import hotelfx.persistence.model.TblLocation;
import hotelfx.persistence.model.TblLocationOfBin;
import hotelfx.persistence.model.TblLocationOfLaundry;
import hotelfx.persistence.model.TblLocationOfWarehouse;
import hotelfx.persistence.model.TblRoom;
import hotelfx.persistence.model.TblStockOpnameItemExpiredDate;
import hotelfx.persistence.model.TblStockOpnameItemExpiredDateDetail;
import hotelfx.persistence.model.TblSupplier;
import hotelfx.persistence.model.TblUnit;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.hibernate.Session;

/**
 *
 * @author ANDRI
 */
public class FEquipmentManagerImpl implements FEquipmentManager {

    private Session session;

    private String errMsg;

    public FEquipmentManagerImpl() {

    }

    @Override
    public TblItem insertDataEquipment(TblItem equipment) {
        errMsg = "";
        TblItem tblItem = equipment;
        if (ClassSession.checkUserSession()) {  //check user current session
            try {
                session = HibernateUtil.getSessionFactory().getCurrentSession();
                session.beginTransaction();
                //data equipment
                tblItem.setCodeItem(ClassCoder.getCode("Equipment", session));
//            tblItem.setConsumable(true);
                tblItem.setHotelDiscountable(false);
                tblItem.setCardDiscountable(false);
                tblItem.setTaxable(true);
                tblItem.setLeasedStatus(true);
                tblItem.setPropertyStatus(false);
                tblItem.setCreateDate(Timestamp.valueOf(LocalDateTime.now()));
                tblItem.setTblEmployeeByCreateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                tblItem.setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                tblItem.setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                tblItem.setRefRecordStatus(session.find(RefRecordStatus.class, 1));
                session.saveOrUpdate(tblItem);
                //data item - location (equipment - all location(warehouse, laundry, room))
                List<TblLocation> dataLocations = getAllLocationWarehouseLaundryRoom();
                for (TblLocation dataLocation : dataLocations) {
                    TblItemLocation dataItemLocation = new TblItemLocation();
                    dataItemLocation.setTblItem(tblItem);
                    dataItemLocation.setTblLocation(dataLocation);
                    dataItemLocation.setItemQuantity(new BigDecimal("0"));
                    dataItemLocation.setCreateDate(Timestamp.valueOf(LocalDateTime.now()));
                    dataItemLocation.setTblEmployeeByCreateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                    dataItemLocation.setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                    dataItemLocation.setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                    dataItemLocation.setRefRecordStatus(session.find(RefRecordStatus.class, 1));
                    session.saveOrUpdate(dataItemLocation);
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
        return tblItem;
    }

    private List<TblLocation> getAllLocationWarehouseLaundryRoom() {
        List<TblLocation> list = session.getNamedQuery("findAllTblLocation")
                .list();
        for(int i=list.size()-1; i>-1; i--){
            if(list.get(i).getRefLocationType().getIdtype() != 0    //Kamar = '0'
                    && list.get(i).getRefLocationType().getIdtype() != 1    //Gudang = '1'
                    && list.get(i).getRefLocationType().getIdtype() != 2){  //Laundry = '2'
                list.remove(i);
            }
        }
        return list;
    }
    
    @Override
    public boolean updateDataEquipment(TblItem equipment) {
        errMsg = "";
        if (ClassSession.checkUserSession()) {  //check user current session
            try {
                session = HibernateUtil.getSessionFactory().getCurrentSession();
                session.beginTransaction();
                equipment.setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                equipment.setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                session.update(equipment);
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
    public boolean deleteDataEquipment(TblItem equipment) {
        errMsg = "";
        if (ClassSession.checkUserSession()) {  //check user current session
            try {
                session = HibernateUtil.getSessionFactory().getCurrentSession();
                session.beginTransaction();
                if (equipment.getRefRecordStatus().getIdstatus() == 1) {
                    equipment.setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                    equipment.setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                    equipment.setRefRecordStatus(session.find(RefRecordStatus.class, 2));
                    session.update(equipment);
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
    public TblItem getEquipment(long id) {
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
    public List<TblItem> getAllDataEquipment() {
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

    @Override
    public List<TblItemLocation> getAllDataEquipmentStock() {
        errMsg = "";
        List<TblItemLocation> list = new ArrayList();
        if (ClassSession.checkUserSession()) {  //check user current session
            try {
                session = HibernateUtil.getSessionFactory().getCurrentSession();
                session.beginTransaction();
                list = session.getNamedQuery("findAllTblItemLocation").list();
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
    public TblUnit getUnit(long id) {
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

    @Override
    public List<TblUnit> getAllDataUnit() {
        errMsg = "";
        List<TblUnit> list = new ArrayList<>();
        if (ClassSession.checkUserSession()) {  //check user current session
            try {
                session = HibernateUtil.getSessionFactory().getCurrentSession();
                session.beginTransaction();
                list = session.getNamedQuery("findAllTblUnit").list();
                session.getTransaction().commit();
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

    //--------------------------------------------------------------------------
    @Override
    public List<TblItemExpiredDate> getAllDataItemExpiredDate() {
        errMsg = "";
        List<TblItemExpiredDate> list = new ArrayList<>();
        if (ClassSession.checkUserSession()) {  //check user current session
            try {
                session = HibernateUtil.getSessionFactory().getCurrentSession();
                session.beginTransaction();
                list = session.getNamedQuery("findAllTblItemExpiredDate")
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
    public List<TblItemExpiredDate> getAllDataItemExpiredDateByIDItemAndItemExpiredDate(long idItem,
            Date itemExpiredDate) {
        errMsg = "";
        List<TblItemExpiredDate> list = new ArrayList<>();
        if (ClassSession.checkUserSession()) {  //check user current session
            try {
                session = HibernateUtil.getSessionFactory().getCurrentSession();
                session.beginTransaction();
                list = session.getNamedQuery("findAllTblItemExpiredDateByIDItemAndItemExpiredDate")
                        .setParameter("idItem", idItem)
                        .setParameter("itemExpiredDate", itemExpiredDate)
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
    public List<TblItemExpiredDate> getAllDataItemExpiredDateByIDItemAndItemExpiredDateAndItemExpiredDateStatus(long idItem,
            Date itemExpiredDate,
            int idItemExpiredDateStatus) {
        errMsg = "";
        List<TblItemExpiredDate> list = new ArrayList<>();
//        if (ClassSession.checkUserSession()) {  //check user current session
//            try {
//                session = HibernateUtil.getSessionFactory().getCurrentSession();
//                session.beginTransaction();
//                list = session.getNamedQuery("findAllTblItemExpiredDateByIDItemAndItemExpiredDateAndItemExpiredDateStatus")
//                        .setParameter("idItem", idItem)
//                        .setParameter("itemExpiredDate", itemExpiredDate)
//                        .setParameter("idItemExpiredDateStatus", idItemExpiredDateStatus)
//                        .list();
//                session.getTransaction().commit();
//            } catch (Exception e) {
//                if (session.getTransaction().isActive()) {
//                    session.getTransaction().rollback();
//                }
//                errMsg = e.getMessage();
//            } finally {
//                //session.close();
//            }
//        }
        return list;
    }

    @Override
    public RefItemExpiredDateStatus getDataItemExpiredDateStatus(int id) {
        errMsg = "";
        RefItemExpiredDateStatus data = null;
        if (ClassSession.checkUserSession()) {  //check user current session
            try {
                session = HibernateUtil.getSessionFactory().getCurrentSession();
                session.beginTransaction();
                data = (RefItemExpiredDateStatus) session.find(RefItemExpiredDateStatus.class, id);
                session.getTransaction().commit();
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
    public List<RefItemGuestType> getAllDataGuest() {
        errMsg = "";
        List<RefItemGuestType> list = new ArrayList();
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
                return list;
            }
        }
        return list;
    }

    //---------------------------------------------------------------------------
    @Override
    public SysDataHardCode getDataSysDataHardCode(long id) {
        errMsg = "";
        SysDataHardCode data = null;
        if (ClassSession.checkUserSession()) {  //check user current sessio
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
        if (ClassSession.checkUserSession()) {  //check user current sessio
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
    public TblStockOpnameItemExpiredDate insertDataStockOpnameItemExpiredDate(
            TblStockOpnameItemExpiredDate stockOpnameItemExpiredDate,
            List<TblStockOpnameItemExpiredDateDetail> stockOpnameItemExpiredDateDetails) {
        errMsg = "";
        TblStockOpnameItemExpiredDate tblStockOpnameItemExpiredDate = stockOpnameItemExpiredDate;
        if (ClassSession.checkUserSession()) {  //check user current session
            try {
                session = HibernateUtil.getSessionFactory().getCurrentSession();
                session.beginTransaction();
                //data stock opname - item expired date
                tblStockOpnameItemExpiredDate.setCodeStockOpnameIed(ClassCoder.getCode("Stock Opname IED", session));
                tblStockOpnameItemExpiredDate.setTblEmployeeByIdemployee(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                tblStockOpnameItemExpiredDate.setCreateDate(Timestamp.valueOf(LocalDateTime.now()));
                tblStockOpnameItemExpiredDate.setTblEmployeeByCreateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                tblStockOpnameItemExpiredDate.setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                tblStockOpnameItemExpiredDate.setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                tblStockOpnameItemExpiredDate.setRefRecordStatus(session.find(RefRecordStatus.class, 1));
                session.saveOrUpdate(tblStockOpnameItemExpiredDate);
                //data stock opname - item expired date - detail (missed)
                for (TblStockOpnameItemExpiredDateDetail stockOpnameItemExpiredDateDetail : stockOpnameItemExpiredDateDetails) {
                    //data  stock opname - item expired date - detail (insert)
                    stockOpnameItemExpiredDateDetail.setCreateDate(Timestamp.valueOf(LocalDateTime.now()));
                    stockOpnameItemExpiredDateDetail.setTblEmployeeByCreateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                    stockOpnameItemExpiredDateDetail.setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                    stockOpnameItemExpiredDateDetail.setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                    stockOpnameItemExpiredDateDetail.setRefRecordStatus(session.find(RefRecordStatus.class, 1));
                    session.saveOrUpdate(stockOpnameItemExpiredDateDetail);
                    //data item expired date (update : 'stockopname')
//                    stockOpnameItemExpiredDateDetail.getTblItemExpiredDate().setRefItemExpiredDateStatus(session.find(RefItemExpiredDateStatus.class, 5));  //Stock Opname = '5'
                    stockOpnameItemExpiredDateDetail.getTblItemExpiredDate().setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                    stockOpnameItemExpiredDateDetail.getTblItemExpiredDate().setTblEmployeeByLastUdapteBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                    session.update(stockOpnameItemExpiredDateDetail.getTblItemExpiredDate());
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
        return tblStockOpnameItemExpiredDate;
    }

    //--------------------------------------------------------------------------
    
    @Override
    public String getErrorMessage() {
        return errMsg;
    }

}
