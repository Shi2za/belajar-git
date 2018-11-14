/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hotelfx.persistence.service;

import hotelfx.helper.ClassCoder;
import hotelfx.helper.ClassSession;
import hotelfx.persistence.HibernateUtil;
import hotelfx.persistence.model.RefItemGuestType;
import hotelfx.persistence.model.RefRecordStatus;
import hotelfx.persistence.model.TblEmployee;
import hotelfx.persistence.model.TblItem;
import hotelfx.persistence.model.TblItemLocation;
import hotelfx.persistence.model.TblLocation;
import hotelfx.persistence.model.TblLocationOfBin;
import hotelfx.persistence.model.TblLocationOfLaundry;
import hotelfx.persistence.model.TblLocationOfWarehouse;
import hotelfx.persistence.model.TblRoom;
import hotelfx.persistence.model.TblSupplier;
import hotelfx.persistence.model.TblUnit;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import org.hibernate.Session;

/**
 *
 * @author Andreas
 */
public class FToolsManagerImpl implements FToolsManager {

    String errMessage;
    Session session;

    @Override
    public TblItem insertDataTools(TblItem tools) {
        errMessage = "";
        TblItem tblItem = tools;
        if (ClassSession.checkUserSession()) {  //check user current session
            try {
                session = HibernateUtil.getSessionFactory().getCurrentSession();
                session.beginTransaction();
                //data tool
                tblItem.setCodeItem(ClassCoder.getCode("Tools", session));
                tblItem.setHotelDiscountable(false);
                tblItem.setCardDiscountable(false);
                tblItem.setTaxable(true);
                tblItem.setLeasedStatus(true);
                tblItem.setPropertyStatus(false);
                tblItem.setTblEmployeeByCreateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                tblItem.setCreateDate(Timestamp.valueOf(LocalDateTime.now()));
                tblItem.setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                tblItem.setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                tblItem.setRefRecordStatus(session.find(RefRecordStatus.class, 1));
                session.saveOrUpdate(tblItem);
                //data item - location (tool - all location(warehouse, laundry, room))
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
                errMessage = e.getMessage();
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
    public boolean updateDataTools(TblItem item) {
        errMessage = "";
        if (ClassSession.checkUserSession()) {  //check user current session
            try {
                session = HibernateUtil.getSessionFactory().getCurrentSession();
                session.beginTransaction();
                item.setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                item.setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                item.setRefRecordStatus(session.find(RefRecordStatus.class, 1));
                session.update(item);
                session.getTransaction().commit();
            } catch (Exception e) {
                if (session.getTransaction().isActive()) {
                    session.getTransaction().rollback();
                }
                errMessage = e.getMessage();
                return false;
            }
        } else {
            return false;
        }
        return true;
    }

    @Override
    public boolean deleteDataTools(TblItem tblItem) {
        errMessage = "";
        if (ClassSession.checkUserSession()) {  //check user current session
            try {
                session = HibernateUtil.getSessionFactory().getCurrentSession();
                session.beginTransaction();
                tblItem.setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                tblItem.setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                tblItem.setRefRecordStatus(session.find(RefRecordStatus.class, 2));
                session.update(tblItem);
                session.getTransaction().commit();
            } catch (Exception e) {
                if (session.getTransaction().isActive()) {
                    session.getTransaction().rollback();
                }
                errMessage = e.getMessage();
                return false;
            }
        } else {
            return false;
        }
        return true;
    }

    @Override
    public List<TblItem> getAllDataTools() {
        errMessage = "";
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
                errMessage = e.getMessage();
            }
        }
        return list;
    }

    @Override
    public List<TblItemLocation> getAllDataToolsStock() {
        errMessage = "";
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
                errMessage = e.getMessage();
            }
        }
        return list;
    }

    @Override
    public List<TblItemLocation> getAllDataItemLocationByIDItem(long idItem) {
        errMessage = "";
        List<TblItemLocation> list = new ArrayList();
        if (ClassSession.checkUserSession()) {  //check user current session
            try {
                session = HibernateUtil.getSessionFactory().getCurrentSession();
                list = session.getNamedQuery("findAllTblItemLocationByIdItem").setParameter("idItem", idItem).list();
                session.getTransaction().commit();
            } catch (Exception e) {
                if (session.getTransaction().isActive()) {
                    session.getTransaction().rollback();
                }
                errMessage = e.getMessage();
            }
        }
        return list;
    }

    @Override
    public List<TblUnit> getAllDataUnit() {
        errMessage = "";
        List<TblUnit> list = new ArrayList();
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
                errMessage = e.getMessage();
            }
        }
        return list;
    }

    @Override
    public List<RefItemGuestType> getAllGuestType() {
        errMessage = "";
        List<RefItemGuestType> list = new ArrayList();
        if (ClassSession.checkUserSession()) {  //check user current session
            try {
                session = HibernateUtil.getSessionFactory().getCurrentSession();
                session.beginTransaction();
                list = session.getNamedQuery("findAllRefItemGuestType").list();
                session.getTransaction().commit();
            } catch (Exception e) {
                if (session.getTransaction().isActive()) {
                    session.beginTransaction().rollback();
                }
                errMessage = e.getMessage();
            }
        }
        return list;
    }

    @Override
    public TblItem getTools(long id) {
        errMessage = "";
        TblItem data = new TblItem();
        if (ClassSession.checkUserSession()) {  //check user current session
            try {
                session = HibernateUtil.getSessionFactory().getCurrentSession();
                session.beginTransaction();
                data = session.find(TblItem.class, id);
                session.getTransaction().commit();
            } catch (Exception e) {
                if (session.getTransaction().isActive()) {
                    session.getTransaction().rollback();
                }
                errMessage = e.getMessage();
            }
        }
        return data;
    }

    @Override
    public List<TblLocationOfWarehouse> getWarehouseByIdLocation(long id) {
        errMessage = "";
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
                errMessage = e.getMessage();
            }
        }
        return list;
    }

    @Override
    public List<TblRoom> getRoomByIdLocation(long id) {
        errMessage = "";
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
                errMessage = e.getMessage();
            }
        }
        return list;
    }

    @Override
    public List<TblLocationOfLaundry> getLaundryByIdLocation(long id) {
        errMessage = "";
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
                errMessage = e.getMessage();
            }
        }
        return list;
    }

    @Override
    public List<TblSupplier> getSupplierByIdLocation(long id) {
        errMessage = "";
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
                errMessage = e.getMessage();
            }
        }
        return list;
    }

    @Override
    public List<TblLocationOfBin> getBinByIdLocation(long id) {
        errMessage = "";
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
                errMessage = e.getMessage();
            }
        }
        return list;
    }

    public String getErrMessage() {
        return errMessage;
    }
}
