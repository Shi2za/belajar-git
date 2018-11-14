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
import hotelfx.persistence.HibernateUtil;
import hotelfx.helper.ClassInputLocation;
import hotelfx.helper.ClassSession;
import hotelfx.persistence.model.RefItemGuestType;
import hotelfx.persistence.model.RefItemMutationType;
import hotelfx.persistence.model.RefItemObsoleteBy;
import hotelfx.persistence.model.RefItemType;
import hotelfx.persistence.model.RefLocationType;
import hotelfx.persistence.model.RefRecordStatus;
import hotelfx.persistence.model.RefStoreRequestStatus;
import hotelfx.persistence.model.SysDataHardCode;
import hotelfx.persistence.model.TblBuilding;
import hotelfx.persistence.model.TblEmployee;
import hotelfx.persistence.model.TblFixedTangibleAsset;
import hotelfx.persistence.model.TblFloor;
import hotelfx.persistence.model.TblGroup;
import hotelfx.persistence.model.TblInComing;
import hotelfx.persistence.model.TblInComingDetail;
import hotelfx.persistence.model.TblInComingDetailItemMutationHistory;
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
import hotelfx.persistence.model.TblOutGoing;
import hotelfx.persistence.model.TblOutGoingDetail;
import hotelfx.persistence.model.TblOutGoingDetailItemMutationHistory;
import hotelfx.persistence.model.TblPeople;
import hotelfx.persistence.model.TblPropertyBarcode;
import hotelfx.persistence.model.TblRoom;
import hotelfx.persistence.model.TblStoreRequest;
import hotelfx.persistence.model.TblStoreRequestDetail;
import hotelfx.persistence.model.TblStoreRequestDetailItemMutationHistory;
import hotelfx.persistence.model.TblSupplier;
import hotelfx.persistence.model.TblUnit;
import hotelfx.view.feature_warehouse.warehouse_in_coming.WarehouseInComingController;
import hotelfx.view.feature_warehouse.warehouse_out_going.WarehouseOutGoingController;
import hotelfx.view.feature_warehouse.warehouse_out_going.WarehouseOutGoingV2Controller;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import org.hibernate.Session;
import org.hibernate.query.Query;

/**
 *
 * @author ANDRI
 */
public class FWarehouseManagerImpl implements FWarehouseManager {

    private Session session;

    private String errMsg;

    public FWarehouseManagerImpl() {

    }

    @Override
    public TblLocationOfWarehouse insertDataWarehouse(TblLocationOfWarehouse warehouse) {
        TblLocationOfWarehouse tblWarehouse = warehouse;
        errMsg = "";
        if (ClassSession.checkUserSession()) {  //check user current session
            try {
                session = HibernateUtil.getSessionFactory().getCurrentSession();
                session.beginTransaction();
                //data location
                tblWarehouse.getTblLocation().setRefRecordStatus(session.find(RefRecordStatus.class, 1));
                tblWarehouse.getTblLocation().setTblEmployeeByCreateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                tblWarehouse.getTblLocation().setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                tblWarehouse.getTblLocation().setCreateDate(Timestamp.valueOf(LocalDateTime.now()));
                tblWarehouse.getTblLocation().setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                tblWarehouse.getTblLocation().setRefLocationType(session.find(RefLocationType.class, 1));
                session.saveOrUpdate(tblWarehouse.getTblLocation());
                //data warehouse
                tblWarehouse.setRefRecordStatus(session.find(RefRecordStatus.class, 1));
                tblWarehouse.getTblLocation().setCodeLocation("L" + tblWarehouse.getTblLocation().getIdlocation());
                tblWarehouse.setTblEmployeeByCreateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                tblWarehouse.setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                tblWarehouse.setCreateDate(Timestamp.valueOf(LocalDateTime.now()));
                tblWarehouse.setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                session.saveOrUpdate(tblWarehouse);
                //data item - location (warehouse - all items)
                List<TblItem> dataItems = getAllItem();
                for (TblItem dataItem : dataItems) {
                    TblItemLocation dataItemLocation = new TblItemLocation();
                    dataItemLocation.setTblItem(dataItem);
                    dataItemLocation.setTblLocation(tblWarehouse.getTblLocation());
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
        return tblWarehouse;
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
    public boolean updateDataWarehouse(TblLocationOfWarehouse warehouse) {
        errMsg = "";
        if (ClassSession.checkUserSession()) {  //check user current session
            try {
                session = HibernateUtil.getSessionFactory().getCurrentSession();
                session.beginTransaction();
                //data location
                warehouse.getTblLocation().setRefRecordStatus(session.find(RefRecordStatus.class, 1));
                warehouse.getTblLocation().setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                warehouse.getTblLocation().setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                session.update(warehouse.getTblLocation());
                //data warehouse
                warehouse.setRefRecordStatus(session.find(RefRecordStatus.class, 1));
                warehouse.setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                warehouse.setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                session.update(warehouse);
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
    public boolean deleteDataWarehouse(TblLocationOfWarehouse warehouse) {
        errMsg = "";
        if (ClassSession.checkUserSession()) {  //check user current session
            try {
                session = HibernateUtil.getSessionFactory().getCurrentSession();
                session.beginTransaction();
                if (warehouse.getRefRecordStatus().getIdstatus() == 1) {
                    //data location
                    warehouse.getTblLocation().setRefRecordStatus(session.find(RefRecordStatus.class, 2));
                    warehouse.getTblLocation().setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                    warehouse.getTblLocation().setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                    session.update(warehouse.getTblLocation());
                    //data warehouse
                    warehouse.setRefRecordStatus(session.find(RefRecordStatus.class, 2));
                    warehouse.setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                    warehouse.setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                    session.update(warehouse);
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
    public TblLocationOfWarehouse getWarehouse(long id) {
        errMsg = "";
        TblLocationOfWarehouse data = null;
        if (ClassSession.checkUserSession()) {  //check user current session
            try {
                session = HibernateUtil.getSessionFactory().getCurrentSession();
                session.beginTransaction();
                data = (TblLocationOfWarehouse) session.find(TblLocationOfWarehouse.class, id);
                session.getTransaction().commit();
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

    //sella
    @Override
    public TblLocationOfWarehouse getDataWarehouseByIdLocation(long id) {
        errMsg = "";
        List<TblLocationOfWarehouse> list = new ArrayList();
        if (ClassSession.checkUserSession()) {  //check user current session
            try {
                session = HibernateUtil.getSessionFactory().getCurrentSession();
                session.beginTransaction();
                list = session.getNamedQuery("findAllTblLocationOfWarehouseByIdLocation")
                        .setParameter("id", id).list();
                session.getTransaction().commit();
            } catch (Exception e) {
                if (session.getTransaction().isActive()) {
                    session.getTransaction().rollback();
                }
                errMsg = e.getMessage();
            }
        }
        return list.isEmpty() ? null : list.get(0);
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
            }
        }
        return data;
    }

    @Override
    public TblJob getDataJob(long id) {
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
                list = session.getNamedQuery("findAllTblJob")
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
                list = session.getNamedQuery("findAllTblGroup")
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
    public List<TblLocation> getAllDataLocation() {
        errMsg = "";
        List<TblLocation> list = new ArrayList<>();
        if (ClassSession.checkUserSession()) {  //check user current session
            try {
                session = HibernateUtil.getSessionFactory().getCurrentSession();
                session.beginTransaction();
                list = session.getNamedQuery("findAllTblLocationByLocationTypeId")
                        .setParameter("typeId", 2)
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
    public List<TblLocation> getAllDataLocationByIDLocationType(int idLocationType) {
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
        return list;
    }

    @Override
    public List<TblItemLocation> getAllDataItemLocation(long id) {
        errMsg = "";
        List<TblItemLocation> list = new ArrayList();
        if (ClassSession.checkUserSession()) {  //check user current session
            try {
                session = HibernateUtil.getSessionFactory().getCurrentSession();
                session.beginTransaction();
                list = session.getNamedQuery("findAllTblItemLocationByIdLocationAndIdType")
                        .setParameter("idLoc", id)
                        .setParameter("idType", 1)
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
            }
        }
        return data;
    }

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

//    @Override
//    public List<TblLocation> getAllDataDestinationLocation(long id) {
//        errMsg = "";
//        List<TblLocation> list = new ArrayList();
//        if (ClassSession.checkUserSession()) {  //check user current session
//            try {
//                session = HibernateUtil.getSessionFactory().getCurrentSession();
//                session.beginTransaction();
//                list = session.getNamedQuery("findAllTblLocationByLocationTypeId")
//                        .setParameter("typeId", id).list();
//                session.getTransaction().commit();
//            } catch (Exception e) {
//                if (session.getTransaction().isActive()) {
//                    session.getTransaction().rollback();
//                }
//                errMsg = e.getMessage();
//            }
//        }
//        return list;
//    }
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
    public boolean deleteDataItemLocationPropertyBarcode(TblItemLocationPropertyBarcode itemLocationPropertyBarcode) {
        errMsg = "";
        if (ClassSession.checkUserSession()) {  //check user current session
            try {
                session = HibernateUtil.getSessionFactory().getCurrentSession();
                session.beginTransaction();
                if (itemLocationPropertyBarcode.getRefRecordStatus().getIdstatus() == 1) {
                    itemLocationPropertyBarcode.setRefRecordStatus(session.find(RefRecordStatus.class, 2));
                    itemLocationPropertyBarcode.setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
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

//    @Override
//    public boolean updateDataWarehouseTransferItem(ClassDataTransferItem transferItem,TblItemMutationHistory mutation,ClassInputLocation destinationLocation){
//      errMsg = "";
//      TblItemMutationHistory tblMutation = mutation;
//      
//      
//      RefItemMutationType mutationTypeMoved = null;
//      RefItemMutationType mutationTypeBin = null;
//      
//      TblItemLocation sourceItemLocation = transferItem.getTblItemLocation();
//      
//            tblMutation.setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
//      TblItemLocation destinationItemLocation = null;
//      List<TblItemLocation>getDataDestinationItemLocation = null;
//      List<TblItemLocationPropertyBarcode>getDataDestinationItemLocationPropertyBarcode = null;
//      
//      TblItemMutationHistoryPropertyBarcode tblMutationPropertyBarcode= null;
//      TblItemLocationPropertyBarcode destinationItemLocationBarcode = null;
//      TblItemLocationPropertyBarcode sourceItemLocationBarcode = null;
//      
//       //System.out.println("Masuk>>"+propertyBarcode.getTblPropertyBarcode().getCodeBarcode());
//      try
//      {
//        session = HibernateUtil.getSessionFactory().getCurrentSession();
//        session.beginTransaction();
//        tblMutation.setRefRecordStatus(session.find(RefRecordStatus.class,1));
//        tblMutation.setTblEmployeeByCreateBy(session.find(TblEmployee.class,ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
//        tblMutation.setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class,ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
//        tblMutation.setCreateDate(Timestamp.valueOf(LocalDateTime.now()));
//        tblMutation.setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
//          //System.out.println("Item Quantity>>"+mutation.getItemQuantity());
//          
// //insert table Mutation History
// //-------------------------------------------------
// 
//       session.saveOrUpdate(tblMutation);
//       tblMutation.setTblItem(sourceItemLocation.getTblItem());
//        tblMutation.setTblLocationByIdlocationOfSource(sourceItemLocation.getTblLocation());
//        mutationTypeMoved = (RefItemMutationType)session.find(RefItemMutationType.class,0);
//        mutationTypeBin = (RefItemMutationType)session.find(RefItemMutationType.class,2);
//      if(tblMutation.getTblLocationByIdlocationOfDestination().getRefLocationType().getIdtype()!=4)
//        {
//          tblMutation.setRefItemMutationType(mutationTypeMoved);
//        }
//        else
//        {
//           tblMutation.setRefItemMutationType(mutationTypeBin);
//        }
//        //Date date = new Date();
//        tblMutation.setMutationDate(Timestamp.valueOf(LocalDateTime.now()));
//        
//        switch(tblMutation.getTblLocationByIdlocationOfDestination().getRefLocationType().getIdtype()){
//            case 0:
//              tblMutation.setTblLocationByIdlocationOfDestination(destinationLocation.getRoom().getTblLocation());
//            break;
//            case 1:
//              tblMutation.setTblLocationByIdlocationOfDestination(destinationLocation.getWarehouse().getTblLocation());
//            break;
//            case 2:
//               tblMutation.setTblLocationByIdlocationOfDestination(destinationLocation.getLaundry().getTblLocation());
//            break;
//        }
//        
//            
//            tblMutation.setTblEmployeeByIdemployee(session.find(TblEmployee.class,ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
//        
// //Substract item in source Location 
//       sourceItemLocation.setItemQuantity(sourceItemLocation.getItemQuantity().subtract(tblMutation.getItemQuantity()));
//       sourceItemLocation.setRefRecordStatus(session.find(RefRecordStatus.class,1));
//      sourceItemLocation.setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class,ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
//      sourceItemLocation.setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
//        session.update(sourceItemLocation);
//        
////add item to location destination, but has done transfer  
//        getDataDestinationItemLocation  = session.getNamedQuery("findAllItemLocationByIdLocationAndIdItem").setParameter("idLocation",tblMutation.getTblLocationByIdlocationOfDestination().getIdlocation()).setParameter("idItem",sourceItemLocation.getTblItem().getIditem()).list();
//        //System.out.println(">>"+getDataDestinationItemLocation.size());
//        
//        for(TblItemLocation getDestinationItemLocation : getDataDestinationItemLocation){
//          if(getDestinationItemLocation.getTblItem().getIditem()==sourceItemLocation.getTblItem().getIditem() && getDestinationItemLocation.getTblLocation().getIdlocation()==tblMutation.getTblLocationByIdlocationOfDestination().getIdlocation()){
//            destinationItemLocation = getDestinationItemLocation;
//            destinationItemLocation.setItemQuantity(destinationItemLocation.getItemQuantity().add(tblMutation.getItemQuantity()));
//            session.update(destinationItemLocation);
//          }
//        }
//        
////add item to location destination, but has not transfer
//        if(getDataDestinationItemLocation.size()==0)
//         {
//            destinationItemLocation = new TblItemLocation();
//            destinationItemLocation.setTblItem(mutation.getTblItem());
//            destinationItemLocation.setTblLocation(mutation.getTblLocationByIdlocationOfDestination());
//            destinationItemLocation.setItemQuantity(mutation.getItemQuantity());
//            destinationItemLocation.setRefRecordStatus(session.find(RefRecordStatus.class,1));
//            destinationItemLocation.setTblEmployeeByCreateBy(session.find(TblEmployee.class,ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
//            destinationItemLocation.setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class,ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
//            destinationItemLocation.setCreateDate(Timestamp.valueOf(LocalDateTime.now()));
//            destinationItemLocation.setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
//            session.saveOrUpdate(destinationItemLocation);
//        }
////------------------------------------------------------------------------------        
////transfer item property barcode        
//       if(transferItem.getIsItem()==false){
//          System.out.println("tblMutationPropertyBarcode:"+transferItem.getTblItemLocationPropertyBarcode().getTblPropertyBarcode().getCodeBarcode());
//          tblMutationPropertyBarcode = new TblItemMutationHistoryPropertyBarcode();
//          tblMutationPropertyBarcode.setRefRecordStatus(session.find(RefRecordStatus.class,1));
//          tblMutationPropertyBarcode.setTblEmployeeByCreateBy(session.find(TblEmployee.class,ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
//          tblMutationPropertyBarcode.setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class,ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
//          tblMutationPropertyBarcode.setCreateDate(Timestamp.valueOf(LocalDateTime.now()));
//          tblMutationPropertyBarcode.setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
//          session.saveOrUpdate(tblMutationPropertyBarcode);
//          tblMutationPropertyBarcode.setTblItemMutationHistory(tblMutation);
//          tblMutationPropertyBarcode.setTblPropertyBarcode(transferItem.getTblItemLocationPropertyBarcode().getTblPropertyBarcode());
//          
//          //session.saveOrUpdate(tblMutationPropertyBarcode);
//          
//         sourceItemLocationBarcode = transferItem.getTblItemLocationPropertyBarcode();
//         sourceItemLocationBarcode.setTblItemLocation(sourceItemLocation);
//         sourceItemLocationBarcode.setRefRecordStatus(session.find(RefRecordStatus.class,1));
//         sourceItemLocationBarcode.setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class,ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
//         sourceItemLocationBarcode.setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
//         session.update(sourceItemLocationBarcode);
//         
//     getDataDestinationItemLocationPropertyBarcode = session.getNamedQuery("findAllTblItemLocationPropertyBarcodeByItemLocationAndPropertyBarcode").setParameter("idrelation",destinationItemLocation.getIdrelation()).setParameter("idproperty",transferItem.getTblItemLocationPropertyBarcode().getTblPropertyBarcode().getIdbarcode()).list();
//          
//     for(TblItemLocationPropertyBarcode getDestinationItemPropertyBarcode : getDataDestinationItemLocationPropertyBarcode){
//               if(destinationItemLocation.getIdrelation()==getDestinationItemPropertyBarcode.getTblItemLocation().getIdrelation() && transferItem.getTblItemLocationPropertyBarcode().getTblPropertyBarcode().getIdbarcode()== getDestinationItemPropertyBarcode.getTblPropertyBarcode().getIdbarcode()){
//                
//                destinationItemLocationBarcode = getDestinationItemPropertyBarcode;
//                destinationItemLocationBarcode.setTblItemLocation(destinationItemLocation);
//                destinationItemLocationBarcode.setRefRecordStatus(session.find(RefRecordStatus.class,1));
//                destinationItemLocationBarcode.setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class,ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
//                destinationItemLocationBarcode.setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
//                session.update(destinationItemLocationBarcode);
//              }
//         }
//       
//      if(getDataDestinationItemLocationPropertyBarcode.size()==0){
//         destinationItemLocationBarcode = new TblItemLocationPropertyBarcode();
//         destinationItemLocationBarcode.setTblItemLocation(destinationItemLocation);
//         destinationItemLocationBarcode.setTblPropertyBarcode(transferItem.getTblItemLocationPropertyBarcode().getTblPropertyBarcode());
//         destinationItemLocationBarcode.setRefRecordStatus(session.find(RefRecordStatus.class,1));
//         destinationItemLocationBarcode.setTblEmployeeByCreateBy(session.find(TblEmployee.class,ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
//         destinationItemLocationBarcode.setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class,ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
//         destinationItemLocationBarcode.setCreateDate(Timestamp.valueOf(LocalDateTime.now()));
//         destinationItemLocationBarcode.setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
//         session.saveOrUpdate(destinationItemLocationBarcode); 
//       }
//       
//       
//}       
//        session.getTransaction().commit();
//      }
//      catch(Exception e)
//      {
//        if(session.getTransaction().isActive())
//        {
//          session.getTransaction().rollback();
//        }
//        errMsg = e.getMessage();
//        return false;
//      }
//      return true;
//    }
    /* @Override
     public boolean updateDataWarehouseTransferItem(TblItemLocation sourceItemLocation,TblItemMutationHistory mutation,ClassInputLocation destinationLocation,TblItemLocationPropertyBarcode propertyBarcode){
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
     if(sourceItemLocation.getTblItem().getPropertyStatus()){
     //System.out.println("tblMutationPropertyBarcode:"+propertyBarcode.getTblPropertyBarcode().getCodeBarcode());
     tblMutationPropertyBarcode = new TblItemMutationHistoryPropertyBarcode();
     session.saveOrUpdate(tblMutationPropertyBarcode);
     tblMutationPropertyBarcode.setTblItemMutationHistory(tblMutation);
     tblMutationPropertyBarcode.setTblPropertyBarcode(propertyBarcode.getTblPropertyBarcode());
          
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
    /*@Override
     public boolean updateDataWarehouseTransferItem(TblItemLocation sourceItemLocation,TblItemMutationHistory mutation,ClassInputLocation destinationLocation,ObservableList<TblPropertyBarcode>propertyBarcode){
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
     if(sourceItemLocation.getTblItem().getPropertyStatus()){
     int count = 0;
     for(int i = 0; i<propertyBarcode.size();i++){
     TblPropertyBarcode destinationItemBarcode = propertyBarcode.get(i);
    
     if(destinationItemBarcode.getIsMoved()==true){
     tblMutationPropertyBarcode = new TblItemMutationHistoryPropertyBarcode();
     tblMutationPropertyBarcode.setTblItemMutationHistory(tblMutation);
     tblMutationPropertyBarcode.setTblPropertyBarcode(destinationItemBarcode);
     session.saveOrUpdate(tblMutationPropertyBarcode);
          
     sourceItemLocationBarcode = new TblItemLocationPropertyBarcode();
     sourceItemLocationBarcode.setTblItemLocation(sourceItemLocation);
     session.update(sourceItemLocation);
         
     getDataDestinationItemLocationPropertyBarcode = session.getNamedQuery("findAllTblItemLocationPropertyBarcodeByItemLocationAndPropertyBarcode").setParameter("idrelation",destinationItemLocation.getIdrelation()).setParameter("idproperty",destinationItemBarcode.getIdbarcode()).list();
          
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
     }*/
    //-----------------transfer item---------------------------------------------------------
    @Override
    public boolean updateDataWarehouseTransferItem(ClassDataTransferItem transferItem,
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
    public TblItemLocation getDataItemLocationByIDItemAndIDLocation(long idItem, long idLoc) {
        errMsg = "";
        List<TblItemLocation> list = new ArrayList();
        if (ClassSession.checkUserSession()) {  //check user current session
            try {
                session = HibernateUtil.getSessionFactory().getCurrentSession();
                session.beginTransaction();
                list = session.getNamedQuery("findAllItemLocationByIdLocationAndIdItem")
                        .setParameter("idItem", idItem)
                        .setParameter("idLocation", idLoc)
                        .list();
                session.getTransaction().commit();
            } catch (Exception e) {
                if (session.getTransaction().isActive()) {
                    session.getTransaction().rollback();
                }
                errMsg = e.getMessage();
            }
        }
        return list.isEmpty() ? null : list.get(0);
    }

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

    @Override
    public List<TblRoom> getAllDataRoom() {
        errMsg = "";
        List<TblRoom> list = new ArrayList();
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
            }
        }
        return list;
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
    public List<ClassDataMutation> getAllDataMutationHistory() {
        errMsg = "";
        List<ClassDataMutation> list = new ArrayList();
        if (ClassSession.checkUserSession()) {  //check user current session
            try {
                session = HibernateUtil.getSessionFactory().getCurrentSession();
                session.beginTransaction();
                List<TblItemMutationHistory> listMutationHistory = session.getNamedQuery("findAllTblItemMutationHistoryByLocationSource").setParameter("idType", 1).list();
                List<TblItemMutationHistoryPropertyBarcode> listMutationHistoryPropertyBarcode = session.getNamedQuery("findAllTblItemMutationHistoryPropertyBarcodeByIdLocation").setParameter("idType", 1).list();

                for (TblItemMutationHistory dataItemMutation : listMutationHistory) {
                    //System.out.println("hsl Mutation>>"+dataItemMutation.getTblItem().getItemName());
                    if (dataItemMutation.getTblItem().getPropertyStatus()) {    //Property
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
//                //System.out.println("hsl history Property Barcode>>"+dataItemMutationPropertyBarcode.getTblItemMutationHistory().getTblItem().getItemName());
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
//                if (!dataItemMutation.getTblItem().getPropertyStatus()) {
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
                //errMsg = e.getMessage();
            }
        }
        return list;
    }

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
            }
        }
        return data;
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

    //@Override
    /*public static List<Object[]>getAllDataLocationWarehouse(){
     //errMsg = "";
     Session session = HibernateUtil.getSessionFactory().getCurrentSession();
     List<Object[]>list = new ArrayList();
     try
     {
     session.beginTransaction();
     list = session.getNamedQuery("findAllNameWarehouse").list();
     session.getTransaction().commit();
     }
     catch(Exception e)
     {
     if(session.getTransaction().isActive())
     {
     session.getTransaction().rollback();
     }
     // errMsg = e.getMessage();
     }
     return list;
     }*/
    @Override
    public List<ClassDataTransferItem> getAllDataTransferItem(long id) {
        errMsg = "";
        List<ClassDataTransferItem> list = new ArrayList();
        if (ClassSession.checkUserSession()) {  //check user current session
            try {
                session = HibernateUtil.getSessionFactory().getCurrentSession();
                session.beginTransaction();
                List<TblItemLocation> listItemLocation = session.getNamedQuery("findAllTblItemLocationByIdLocationAndIdType").setParameter("idType", 1).setParameter("idLoc", id).list();
                List<TblItemLocationPropertyBarcode> listItemLocationPropertyBarcode = session.getNamedQuery("findAllTblItemLocationPropertyBarcodeByIdLocationAndIdType").setParameter("idType", 1).setParameter("idLoc", id).list();

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
                    if (!dataItemLocation.getTblItem().getPropertyStatus() //!Property
                            && !dataItemLocation.getTblItem().getPropertyStatus()) {   //!Consumable
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
                 }
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
    }

    @Override
    public List<TblSupplier> getSupplierByIdLocation(long id) {
        errMsg = "";
        List<TblSupplier> list = new ArrayList();
        if (ClassSession.checkUserSession()) {  //check user current session
            try {
                session = HibernateUtil.getSessionFactory().getCurrentSession();
                session.beginTransaction();
                list = session.getNamedQuery("findAllTblSupplierByIdLocation")
                        .setParameter("id", id).list();
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

    /*public static void getDataItemTransfer(){
     //errMsg = "";
     List<ClassListTransferItem>list = new ArrayList();
     Session session = HibernateUtil.getSessionFactory().getCurrentSession();
     session.beginTransaction();
     try
     {
     Query EmplyeeQuery = session.createSQLQuery("{CALL GetItemLocation(:idType)}")
     .addEntity(TblItemLocationPropertyBarcode.class)
     .addEntity(TblPropertyBarcode.class)
     .setParameter("idType",1);
     List result = EmplyeeQuery.list();
     for(int i=0; i<result.size(); i++){
     TblItemLocationPropertyBarcode stock = (TblItemLocationPropertyBarcode)result.get(i);
     System.out.println(stock.getTblPropertyBarcode().getCodeBarcode());
     }
     session.getTransaction().commit();
     }
     catch(Exception e){
     if(session.getTransaction().isActive())
     {
     session.getTransaction().rollback();
     }  
     //errMsg = e.getMessage();
     }
     }
     /* @Override
     public TblItemLocationPropertyBarcode getDataItemPropertyBarcode(long id){
     errMsg = "";
     TblItemLocationPropertyBarcode data = null;
     try
     {
     session = HibernateUtil.getSessionFactory().getCurrentSession();
     session.beginTransaction();
     data = (TblItemLocationPropertyBarcode)session.find(TblItemLocationPropertyBarcode.class, id);
     session.getTransaction().commit();
     }
     catch(Exception e){
     if(session.getTransaction().isActive())
     {
     session.getTransaction().rollback();
     }  
     errMsg = e.getMessage();
     }
     return data;
     }*/
    //--------------------------------------------------------------------------
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
    public TblStoreRequest insertDataStoreRequest(TblStoreRequest sr,
            List<TblStoreRequestDetail> srds) {
        errMsg = "";
        TblStoreRequest tblStoreRequest = sr;
        if (ClassSession.checkUserSession()) {  //check user current session
            try {
                session = HibernateUtil.getSessionFactory().getCurrentSession();
                session.beginTransaction();
                //data store request
                tblStoreRequest.setCodeSr(ClassCoder.getCode("Store Request", session));
                tblStoreRequest.setSrdate(Timestamp.valueOf(LocalDateTime.now()));
                tblStoreRequest.setCreatedDate(Timestamp.valueOf(LocalDateTime.now()));
                tblStoreRequest.setTblEmployeeByCreatedBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                tblStoreRequest.setRefStoreRequestStatus(session.find(RefStoreRequestStatus.class, 0));    //0 = 'Created'
                tblStoreRequest.setCreateDate(Timestamp.valueOf(LocalDateTime.now()));
                tblStoreRequest.setTblEmployeeByCreateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                tblStoreRequest.setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                tblStoreRequest.setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                tblStoreRequest.setRefRecordStatus(session.find(RefRecordStatus.class, 1));
                session.saveOrUpdate(tblStoreRequest);
                //data store request detail
                for (TblStoreRequestDetail srd : srds) {
                    srd.setCreateDate(Timestamp.valueOf(LocalDateTime.now()));
                    srd.setTblEmployeeByCreateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                    srd.setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                    srd.setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                    srd.setRefRecordStatus(session.find(RefRecordStatus.class, 1));
                    session.saveOrUpdate(srd);
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
        return tblStoreRequest;
    }

    @Override
    public boolean updateDataStoreRequest(TblStoreRequest sr,
            List<TblStoreRequestDetail> srds) {
        errMsg = "";
        if (ClassSession.checkUserSession()) {  //check user current session
            try {
                session = HibernateUtil.getSessionFactory().getCurrentSession();
                session.beginTransaction();
                //data store request
//                sr.setCreatedDate(Timestamp.valueOf(LocalDateTime.now()));
//                sr.setTblEmployeeByCreatedBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                sr.setRefStoreRequestStatus(session.find(RefStoreRequestStatus.class, 0)); //need approve again, 0 = 'Created'
                sr.setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                sr.setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                session.update(sr);
                //data store request detail
                //delete all
                Query rs = session.getNamedQuery("deleteAllTblStoreRequestDetailByIDStoreRequest")
                        .setParameter("idStoreRequest", sr.getIdsr())
                        .setParameter("idEmployee", ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee());
                errMsg = (String) rs.uniqueResult();
                if (!errMsg.equals("")) {
                    if (session.getTransaction().isActive()) {
                        session.getTransaction().rollback();
                    }
                    return false;
                }
                //save or update data store request detail
                for (TblStoreRequestDetail srd : srds) {
                    srd.setCreateDate(Timestamp.valueOf(LocalDateTime.now()));
                    srd.setTblEmployeeByCreateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                    srd.setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                    srd.setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                    srd.setRefRecordStatus(session.find(RefRecordStatus.class, 1));
                    session.saveOrUpdate(srd);
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

//    //for 'out going' action
//    @Override
//    public boolean updateApprovedDataStoreRequest(TblStoreRequest sr,
//            List<WarehouseOutGoingController.SRDetailProperty> srdps) {
//        errMsg = "";
//        if (ClassSession.checkUserSession()) {  //check user current session
//            try {
//                session = HibernateUtil.getSessionFactory().getCurrentSession();
//                session.beginTransaction();
//                //data store request
//                sr.setApprovedDate(Timestamp.valueOf(LocalDateTime.now()));
//                sr.setTblEmployeeByApprovedBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
//                sr.setReceivedDate(Timestamp.valueOf(LocalDateTime.now()));
//                sr.setRefStoreRequestStatus(session.find(RefStoreRequestStatus.class, 6)); //Selesai = '6'
//                sr.setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
//                sr.setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
//                session.update(sr);
//                for (WarehouseOutGoingController.SRDetailProperty srdp : srdps) {
//                    //data item mutation history
//                    TblItemMutationHistory itemMutationHistory = new TblItemMutationHistory();
//                    itemMutationHistory.setTblItem(srdp.getDataSRDetail().getTblItem());
//                    itemMutationHistory.setItemQuantity(srdp.getDataSRDetail().getItemQuantity());
//                    itemMutationHistory.setTblLocationByIdlocationOfSource(sr.getTblLocationByIdlocationSource());
//                    itemMutationHistory.setTblLocationByIdlocationOfDestination(sr.getTblLocationByIdlocationDestination());
//                    itemMutationHistory.setTblEmployeeByIdemployee(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
//                    itemMutationHistory.setMutationDate(Timestamp.valueOf(LocalDateTime.now()));
//                    itemMutationHistory.setRefItemMutationType(session.find(RefItemMutationType.class, (int) 0)); //Dipindahkan = '0'
//                    itemMutationHistory.setRefRecordStatus(session.find(RefRecordStatus.class, (int) 1));
//                    itemMutationHistory.setTblEmployeeByCreateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
//                    itemMutationHistory.setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
//                    itemMutationHistory.setCreateDate(Timestamp.valueOf(LocalDateTime.now()));
//                    itemMutationHistory.setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
//                    session.saveOrUpdate(itemMutationHistory);
//                    //Item Location - Source (-)
//                    TblItemLocation sourceItemLocation = getItemLocationByIDItemAndLocation(itemMutationHistory.getTblItem().getIditem(),
//                            sr.getTblLocationByIdlocationSource().getIdlocation());
//                    if (sourceItemLocation != null) {
//                        sourceItemLocation.setItemQuantity(sourceItemLocation.getItemQuantity().subtract(itemMutationHistory.getItemQuantity()));
//                        sourceItemLocation.setRefRecordStatus(session.find(RefRecordStatus.class, 1));
//                        sourceItemLocation.setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
//                        sourceItemLocation.setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
//                        session.update(sourceItemLocation);
//                    }
//                    //Item Location - Destination (+)
//                    TblItemLocation destinationItemLocation = getItemLocationByIDItemAndLocation(itemMutationHistory.getTblItem().getIditem(),
//                            sr.getTblLocationByIdlocationDestination().getIdlocation());
//                    if (destinationItemLocation != null) {
//                        destinationItemLocation.setItemQuantity(destinationItemLocation.getItemQuantity().add(itemMutationHistory.getItemQuantity()));
//                        destinationItemLocation.setRefRecordStatus(session.find(RefRecordStatus.class, 1));
//                        destinationItemLocation.setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
//                        destinationItemLocation.setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
//                        session.update(destinationItemLocation);
//                    } else {
//                        //do something here..
//                    }
//                    //data item mutation history - property
//                    for (TblPropertyBarcode property : srdp.getDataProperties()) {
//                        //data item mutation history - property
//                        TblItemMutationHistoryPropertyBarcode itemMutationHistoryProperty = new TblItemMutationHistoryPropertyBarcode();
//                        itemMutationHistoryProperty.setTblItemMutationHistory(itemMutationHistory);
//                        itemMutationHistoryProperty.setTblPropertyBarcode(property);
//                        itemMutationHistoryProperty.setRefRecordStatus(session.find(RefRecordStatus.class, (int) 1));
//                        itemMutationHistoryProperty.setTblEmployeeByCreateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
//                        itemMutationHistoryProperty.setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
//                        itemMutationHistoryProperty.setCreateDate(Timestamp.valueOf(LocalDateTime.now()));
//                        itemMutationHistoryProperty.setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
//                        session.saveOrUpdate(itemMutationHistoryProperty);
//                        //Item Location - property barcode - Source (deleted)
//                        if (sourceItemLocation != null) {
//                            TblItemLocationPropertyBarcode sourceItemLocationPropertyBarcode = getItemLocationPropertyBarcodeByIDItemLocationAndDIPropertyBarcode(
//                                    sourceItemLocation.getIdrelation(),
//                                    property.getIdbarcode());
//                            if (sourceItemLocationPropertyBarcode != null) {
//                                sourceItemLocationPropertyBarcode.setRefRecordStatus(session.find(RefRecordStatus.class, (int) 2));
//                                sourceItemLocationPropertyBarcode.setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
//                                sourceItemLocationPropertyBarcode.setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
//                                session.saveOrUpdate(sourceItemLocationPropertyBarcode);
//                            }
//                        }
//                        //Item Location - property barcode - Destination (actived)
//                        if (destinationItemLocation != null) {
//                            TblItemLocationPropertyBarcode destinationItemLocationPropertyBarcode = getItemLocationPropertyBarcodeByIDItemLocationAndDIPropertyBarcode(
//                                    destinationItemLocation.getIdrelation(),
//                                    property.getIdbarcode());
//                            if (destinationItemLocationPropertyBarcode != null) {
//                                destinationItemLocationPropertyBarcode.setRefRecordStatus(session.find(RefRecordStatus.class, (int) 1));
//                                destinationItemLocationPropertyBarcode.setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
//                                destinationItemLocationPropertyBarcode.setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
//                                session.saveOrUpdate(destinationItemLocationPropertyBarcode);
//                            } else {    //create new data
//                                destinationItemLocationPropertyBarcode = new TblItemLocationPropertyBarcode();
//                                destinationItemLocationPropertyBarcode.setTblItemLocation(destinationItemLocation);
//                                destinationItemLocationPropertyBarcode.setTblPropertyBarcode(property);
//                                destinationItemLocationPropertyBarcode.setRefRecordStatus(session.find(RefRecordStatus.class, (int) 1));
//                                destinationItemLocationPropertyBarcode.setTblEmployeeByCreateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
//                                destinationItemLocationPropertyBarcode.setCreateDate(Timestamp.valueOf(LocalDateTime.now()));
//                                destinationItemLocationPropertyBarcode.setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
//                                destinationItemLocationPropertyBarcode.setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
//                                session.saveOrUpdate(destinationItemLocationPropertyBarcode);
//                            }
//                        }
//                    }
//
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

    //Out Going
    @Override
    public boolean updateApprovedOGDataStoreRequest(TblStoreRequest sr,
            List<WarehouseOutGoingV2Controller.SRDetailItemMutationHistory> srdimhs) {
        errMsg = "";
        if (ClassSession.checkUserSession()) {  //check user current session
            try {
                session = HibernateUtil.getSessionFactory().getCurrentSession();
                session.beginTransaction();
                //data store request
                sr.setApprovedDate(Timestamp.valueOf(LocalDateTime.now()));
                sr.setTblEmployeeByApprovedBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                sr.setReceivedDate(Timestamp.valueOf(LocalDateTime.now()));
                sr.setRefStoreRequestStatus(session.find(RefStoreRequestStatus.class, 6)); //Selesai = '6'
                sr.setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                sr.setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                session.update(sr);
                for (WarehouseOutGoingV2Controller.SRDetailItemMutationHistory srdimh : srdimhs) {
                    //data store request detail
                    srdimh.getDataSRDIMH().getTblStoreRequestDetail().setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                    srdimh.getDataSRDIMH().getTblStoreRequestDetail().setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                    session.update(srdimh.getDataSRDIMH().getTblStoreRequestDetail());
                    //data item mutation history
//                    srdimh.getDataSRDIMH().getTblItemMutationHistory().setTblEmployeeByIdemployee(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                    srdimh.getDataSRDIMH().getTblItemMutationHistory().setMutationDate(Timestamp.valueOf(LocalDateTime.now()));
                    srdimh.getDataSRDIMH().getTblItemMutationHistory().setRefItemMutationType(session.find(RefItemMutationType.class, (int) 0)); //Dipindahkan = '0'
                    srdimh.getDataSRDIMH().getTblItemMutationHistory().setRefRecordStatus(session.find(RefRecordStatus.class, (int) 1));
                    srdimh.getDataSRDIMH().getTblItemMutationHistory().setTblEmployeeByCreateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                    srdimh.getDataSRDIMH().getTblItemMutationHistory().setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                    srdimh.getDataSRDIMH().getTblItemMutationHistory().setCreateDate(Timestamp.valueOf(LocalDateTime.now()));
                    srdimh.getDataSRDIMH().getTblItemMutationHistory().setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                    session.saveOrUpdate(srdimh.getDataSRDIMH().getTblItemMutationHistory());
                    //data item location (source, -)
                    TblItemLocation sourceItemLocation = getItemLocationByIDItemAndLocation(
                            srdimh.getDataSRDIMH().getTblItemMutationHistory().getTblItem().getIditem(),
                            srdimh.getDataSRDIMH().getTblItemMutationHistory().getTblLocationByIdlocationOfSource().getIdlocation());
                    if (sourceItemLocation == null) {
                        errMsg = "Barang (" + srdimh.getDataSRDIMH().getTblItemMutationHistory().getTblItem().getItemName() + ") tidak ditemukan..!";
                        if (session.getTransaction().isActive()) {
                            session.getTransaction().rollback();
                        }
                        return false;
                    }
                    sourceItemLocation.setItemQuantity(sourceItemLocation.getItemQuantity().subtract(srdimh.getDataSRDIMH().getTblItemMutationHistory().getItemQuantity()));
                    sourceItemLocation.setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                    sourceItemLocation.setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                    if (sourceItemLocation.getItemQuantity()
                            .compareTo(new BigDecimal("0")) == -1) {   //data item quantity < 0
                        errMsg = "Stok Barang (" + srdimh.getDataSRDIMH().getTblItemMutationHistory().getTblItem().getItemName() + ") tidak mencukupi..!";
                        if (session.getTransaction().isActive()) {
                            session.getTransaction().rollback();
                        }
                        return false;
                    }
                    session.update(sourceItemLocation);
                    //data item location (destination, +)
                    TblItemLocation destinationItemLocation = getItemLocationByIDItemAndLocation(
                            srdimh.getDataSRDIMH().getTblItemMutationHistory().getTblItem().getIditem(),
                            srdimh.getDataSRDIMH().getTblItemMutationHistory().getTblLocationByIdlocationOfDestination().getIdlocation());
                    if (destinationItemLocation != null) {
                        destinationItemLocation.setItemQuantity(destinationItemLocation.getItemQuantity().add(srdimh.getDataSRDIMH().getTblItemMutationHistory().getItemQuantity()));
                        destinationItemLocation.setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                        destinationItemLocation.setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                        session.update(destinationItemLocation);
                    } else {
                        destinationItemLocation = new TblItemLocation();
                        destinationItemLocation.setTblItem(srdimh.getDataSRDIMH().getTblItemMutationHistory().getTblItem());
                        destinationItemLocation.setTblLocation(srdimh.getDataSRDIMH().getTblItemMutationHistory().getTblLocationByIdlocationOfDestination());
                        destinationItemLocation.setItemQuantity(srdimh.getDataSRDIMH().getTblItemMutationHistory().getItemQuantity());
                        destinationItemLocation.setRefRecordStatus(session.find(RefRecordStatus.class, (int) 1));
                        destinationItemLocation.setTblEmployeeByCreateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                        destinationItemLocation.setCreateDate(Timestamp.valueOf(LocalDateTime.now()));
                        destinationItemLocation.setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                        destinationItemLocation.setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                        session.saveOrUpdate(destinationItemLocation);
                    }
                    //data store request detail - item mutation history
                    srdimh.getDataSRDIMH().setRefRecordStatus(session.find(RefRecordStatus.class, (int) 1));
                    srdimh.getDataSRDIMH().setTblEmployeeByCreateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                    srdimh.getDataSRDIMH().setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                    srdimh.getDataSRDIMH().setCreateDate(Timestamp.valueOf(LocalDateTime.now()));
                    srdimh.getDataSRDIMH().setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                    session.saveOrUpdate(srdimh.getDataSRDIMH());
                    //data item mutation history - property
                    for (WarehouseOutGoingV2Controller.IMHPropertyBarcodeSelected impbs : srdimh.getDataIMHPBSs()) {
                        if (impbs.isSelected()) { //selected
                            impbs.getDataItemMutationHistoryPropertyBarcode().setRefRecordStatus(session.find(RefRecordStatus.class, (int) 1));
                            impbs.getDataItemMutationHistoryPropertyBarcode().setTblEmployeeByCreateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                            impbs.getDataItemMutationHistoryPropertyBarcode().setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                            impbs.getDataItemMutationHistoryPropertyBarcode().setCreateDate(Timestamp.valueOf(LocalDateTime.now()));
                            impbs.getDataItemMutationHistoryPropertyBarcode().setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                            session.saveOrUpdate(impbs.getDataItemMutationHistoryPropertyBarcode());
                            //data item location - property barcode (source, remove)
                            TblItemLocationPropertyBarcode sourceItemLocationPropertyBarcode = getItemLocationPropertyBarcodeByIDItemLocationAndDIPropertyBarcode(
                                    sourceItemLocation.getIdrelation(),
                                    impbs.getDataItemMutationHistoryPropertyBarcode().getTblPropertyBarcode().getIdbarcode());
                            if (sourceItemLocationPropertyBarcode == null) {
                                errMsg = "Barang (" + impbs.getDataItemMutationHistoryPropertyBarcode().getTblPropertyBarcode().getTblItem().getItemName()
                                        + " - " + impbs.getDataItemMutationHistoryPropertyBarcode().getTblPropertyBarcode().getCodeBarcode()
                                        + ") tidak ditemukan..!";
                                if (session.getTransaction().isActive()) {
                                    session.getTransaction().rollback();
                                }
                                return false;
                            }
                            sourceItemLocationPropertyBarcode.setRefRecordStatus(session.find(RefRecordStatus.class, (int) 2));
                            sourceItemLocationPropertyBarcode.setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                            sourceItemLocationPropertyBarcode.setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                            session.saveOrUpdate(sourceItemLocationPropertyBarcode);
                            //data item location - property barcode (destination, add)
                            TblItemLocationPropertyBarcode destinationItemLocationPropertyBarcode = getItemLocationPropertyBarcodeByIDItemLocationAndDIPropertyBarcode(
                                    destinationItemLocation.getIdrelation(),
                                    impbs.getDataItemMutationHistoryPropertyBarcode().getTblPropertyBarcode().getIdbarcode());
                            if (destinationItemLocationPropertyBarcode != null) {
                                destinationItemLocationPropertyBarcode.setRefRecordStatus(session.find(RefRecordStatus.class, (int) 1));
                                destinationItemLocationPropertyBarcode.setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                                destinationItemLocationPropertyBarcode.setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                                session.saveOrUpdate(destinationItemLocationPropertyBarcode);
                            } else {    //create new data
                                destinationItemLocationPropertyBarcode = new TblItemLocationPropertyBarcode();
                                destinationItemLocationPropertyBarcode.setTblItemLocation(destinationItemLocation);
                                destinationItemLocationPropertyBarcode.setTblPropertyBarcode(impbs.getDataItemMutationHistoryPropertyBarcode().getTblPropertyBarcode());
                                destinationItemLocationPropertyBarcode.setRefRecordStatus(session.find(RefRecordStatus.class, (int) 1));
                                destinationItemLocationPropertyBarcode.setTblEmployeeByCreateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                                destinationItemLocationPropertyBarcode.setCreateDate(Timestamp.valueOf(LocalDateTime.now()));
                                destinationItemLocationPropertyBarcode.setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                                destinationItemLocationPropertyBarcode.setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                                session.saveOrUpdate(destinationItemLocationPropertyBarcode);
                            }
                        }
                    }
                    //data item mutation history - item expired date
                    for (TblItemMutationHistoryItemExpiredDate imhied : srdimh.getDataIMHIEDs()) {
                        imhied.setRefRecordStatus(session.find(RefRecordStatus.class, (int) 1));
                        imhied.setTblEmployeeByCreateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                        imhied.setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                        imhied.setCreateDate(Timestamp.valueOf(LocalDateTime.now()));
                        imhied.setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                        session.saveOrUpdate(imhied);
                        //data item location - item expired date (source, -)
                        TblItemLocationItemExpiredDate sourceItemLocationItemExpiredDate = getItemLocationItemExpiredDateByIDItemLocationAndIDItemExpiredDate(
                                sourceItemLocation.getIdrelation(),
                                imhied.getTblItemExpiredDate().getIditemExpiredDate());
                        if (sourceItemLocationItemExpiredDate == null) {
                            errMsg = "Barang (" + imhied.getTblItemExpiredDate().getTblItem().getItemName()
                                    + " - " + ClassFormatter.dateFormate.format(imhied.getTblItemExpiredDate().getItemExpiredDate())
                                    + ") tidak ditemukan..!";
                            if (session.getTransaction().isActive()) {
                                session.getTransaction().rollback();
                            }
                            return false;
                        }
                        sourceItemLocationItemExpiredDate.setItemQuantity(sourceItemLocationItemExpiredDate.getItemQuantity().subtract(imhied.getItemQuantity()));
                        sourceItemLocationItemExpiredDate.setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                        sourceItemLocationItemExpiredDate.setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                        if (sourceItemLocationItemExpiredDate.getItemQuantity()
                                .compareTo(new BigDecimal("0")) == -1) {   //data item quantity < 0
                            errMsg = "Stok Barang (" + imhied.getTblItemExpiredDate().getTblItem().getItemName()
                                    + " - " + ClassFormatter.dateFormate.format(imhied.getTblItemExpiredDate().getItemExpiredDate())
                                    + ") tidak mencukupi..!";
                            if (session.getTransaction().isActive()) {
                                session.getTransaction().rollback();
                            }
                            return false;
                        }
                        session.update(sourceItemLocationItemExpiredDate);
                        //data item location - item expired date (destination, +)
                        TblItemLocationItemExpiredDate destinationItemLocationItemExpiredDate = getItemLocationItemExpiredDateByIDItemLocationAndIDItemExpiredDate(
                                destinationItemLocation.getIdrelation(),
                                imhied.getTblItemExpiredDate().getIditemExpiredDate());
                        if (destinationItemLocationItemExpiredDate != null) {
                            destinationItemLocationItemExpiredDate.setItemQuantity(destinationItemLocationItemExpiredDate.getItemQuantity().add(imhied.getItemQuantity()));
                            destinationItemLocationItemExpiredDate.setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                            destinationItemLocationItemExpiredDate.setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                            session.update(destinationItemLocationItemExpiredDate);
                        } else {
                            destinationItemLocationItemExpiredDate = new TblItemLocationItemExpiredDate();
                            destinationItemLocationItemExpiredDate.setTblItemLocation(destinationItemLocation);
                            destinationItemLocationItemExpiredDate.setTblItemExpiredDate(imhied.getTblItemExpiredDate());
                            destinationItemLocationItemExpiredDate.setItemQuantity(imhied.getItemQuantity());
                            destinationItemLocationItemExpiredDate.setRefRecordStatus(session.find(RefRecordStatus.class, (int) 1));
                            destinationItemLocationItemExpiredDate.setTblEmployeeByCreateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                            destinationItemLocationItemExpiredDate.setCreateDate(Timestamp.valueOf(LocalDateTime.now()));
                            destinationItemLocationItemExpiredDate.setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                            destinationItemLocationItemExpiredDate.setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                            session.saveOrUpdate(destinationItemLocationItemExpiredDate);
                        }
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

    @Override
    public boolean deleteDataStoreRequest(TblStoreRequest sr) {
        errMsg = "";
        if (ClassSession.checkUserSession()) {  //check user current session
            try {
                session = HibernateUtil.getSessionFactory().getCurrentSession();
                session.beginTransaction();
                //data store request
                sr.setCanceledDate(Timestamp.valueOf(LocalDateTime.now()));
                sr.setTblEmployeeByCanceledBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                sr.setRefStoreRequestStatus(session.find(RefStoreRequestStatus.class, 2)); //2 = 'Canceled'
                sr.setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                sr.setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                session.update(sr);
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
    public TblStoreRequest getDataStoreRequest(long id) {
        errMsg = "";
        TblStoreRequest data = null;
        if (ClassSession.checkUserSession()) {  //check user current session
            try {
                session = HibernateUtil.getSessionFactory().getCurrentSession();
                session.beginTransaction();
                data = (TblStoreRequest) session.find(TblStoreRequest.class, id);
                session.getTransaction().commit();
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
    public List<TblStoreRequest> getAllDataStoreRequest() {
        errMsg = "";
        List<TblStoreRequest> list = new ArrayList<>();
        if (ClassSession.checkUserSession()) {  //check user current session
            try {
                session = HibernateUtil.getSessionFactory().getCurrentSession();
                session.beginTransaction();
                list = session.getNamedQuery("findAllTblStoreRequest")
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
    public List<TblStoreRequest> getAllDataStoreRequestByIDStoreRequestStatus(int idStoreRequestStatus) {
        errMsg = "";
        List<TblStoreRequest> list = new ArrayList<>();
        if (ClassSession.checkUserSession()) {  //check user current session
            try {
                session = HibernateUtil.getSessionFactory().getCurrentSession();
                session.beginTransaction();
                list = session.getNamedQuery("findAllTblStoreRequestByIDStoreRequestStatus")
                        .setParameter("idStoreRequestStatus", idStoreRequestStatus)
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
    public TblStoreRequestDetail getDataStoreRequestDetail(long id) {
        errMsg = "";
        TblStoreRequestDetail data = null;
        if (ClassSession.checkUserSession()) {  //check user current session
            try {
                session = HibernateUtil.getSessionFactory().getCurrentSession();
                session.beginTransaction();
                data = (TblStoreRequestDetail) session.find(TblStoreRequestDetail.class, id);
                session.getTransaction().commit();
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
    public TblStoreRequestDetail getDataStoreRequestDetailByIDStoreRequestAndIDItem(
            long idSR,
            long idItem) {
        errMsg = "";
        List<TblStoreRequestDetail> list = new ArrayList<>();
        if (ClassSession.checkUserSession()) {  //check user current session
            try {
                session = HibernateUtil.getSessionFactory().getCurrentSession();
                session.beginTransaction();
                list = session.getNamedQuery("findAllTblStoreRequestDetailByIDStoreRequestAndIDItem")
                        .setParameter("idStoreRequest", idSR)
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
    public List<TblStoreRequestDetail> getAllDataStoreRequestDetail() {
        errMsg = "";
        List<TblStoreRequestDetail> list = new ArrayList<>();
        if (ClassSession.checkUserSession()) {  //check user current session
            try {
                session = HibernateUtil.getSessionFactory().getCurrentSession();
                session.beginTransaction();
                list = session.getNamedQuery("findAllTblStoreRequestDetail")
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
    public List<TblStoreRequestDetail> getAllDataStoreRequestDetailByIDStoreRequest(long idSR) {
        errMsg = "";
        List<TblStoreRequestDetail> list = new ArrayList<>();
        if (ClassSession.checkUserSession()) {  //check user current session
            try {
                session = HibernateUtil.getSessionFactory().getCurrentSession();
                session.beginTransaction();
                list = session.getNamedQuery("findAllTblStoreRequestDetailByIDStoreRequest")
                        .setParameter("idStoreRequest", idSR)
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
    public RefStoreRequestStatus getDataStoreRequestStatus(int id) {
        errMsg = "";
        RefStoreRequestStatus data = null;
        if (ClassSession.checkUserSession()) {  //check user current session
            try {
                session = HibernateUtil.getSessionFactory().getCurrentSession();
                session.beginTransaction();
                data = (RefStoreRequestStatus) session.find(RefStoreRequestStatus.class, id);
                session.getTransaction().commit();
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
    public List<TblFixedTangibleAsset> getAllDataFixedTangibleAsset() {
        errMsg = "";
        List<TblFixedTangibleAsset> list = new ArrayList<>();
        if (ClassSession.checkUserSession()) {  //check user current session
            try {
                session = HibernateUtil.getSessionFactory().getCurrentSession();
                session.beginTransaction();
                list = session.getNamedQuery("findAllTblFixedTangibleAsset")
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
    public TblItemLocationPropertyBarcode getDataItemLocationPropertyBarcodeByIDItemLocationAndIDPropertyBarcode(
            long idItemLocation,
            long idPropertyBarcode) {
        errMsg = "";
        List<TblItemLocationPropertyBarcode> list = new ArrayList<>();
        if (ClassSession.checkUserSession()) {  //check user current session
            try {
                session = HibernateUtil.getSessionFactory().getCurrentSession();
                session.beginTransaction();
                list = session.getNamedQuery("findAllTblItemLocationPropertyBarcodeByIDItemLocationAndIDPropertyBarcode")
                        .setParameter("idItemLocation", idItemLocation)
                        .setParameter("idPropertyBarcode", idPropertyBarcode)
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
    public TblItemLocationItemExpiredDate getDataItemLocationItemExpiredDateByIDItemLocationAndIDItemExpiredDate(
            long idItemLocation,
            long idItemExpiredDate) {
        errMsg = "";
        List<TblItemLocationItemExpiredDate> list = new ArrayList<>();
        if (ClassSession.checkUserSession()) {  //check user current session
            try {
                session = HibernateUtil.getSessionFactory().getCurrentSession();
                session.beginTransaction();
                list = session.getNamedQuery("findAllTblItemLocationItemExpiredDateByIDItemLocationAndIDItemExpiredDate")
                        .setParameter("idItemLocation", idItemLocation)
                        .setParameter("idItemExpiredDate", idItemExpiredDate)
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
    public TblStoreRequestDetailItemMutationHistory getDataStoreRequestDetailItemMutationHistoryByIDStoreRequestDetail(long idStoreRequestDetail) {
        errMsg = "";
        List<TblStoreRequestDetailItemMutationHistory> list = new ArrayList<>();
        if (ClassSession.checkUserSession()) {  //check user current session
            try {
                session = HibernateUtil.getSessionFactory().getCurrentSession();
                session.beginTransaction();
                list = session.getNamedQuery("findAllTblStoreRequestDetailItemMutationHistoryByIDStoreRequestDetail")
                        .setParameter("idStoreRequestDetail", idStoreRequestDetail)
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
    public List<TblItemMutationHistoryPropertyBarcode> getAllDataItemMutationHistoryPropertyBarcodeByIDItemMutationHistory(long idItemMutation) {
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
        return list;
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
    public TblRoom getDataRoomByIDLocation(long idLocation) {
        errMsg = "";
        List<TblRoom> list = new ArrayList();
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
            }
        }
        return list.isEmpty() ? null : list.get(0);
    }

    @Override
    public TblLocationOfLaundry getDataLaundryByIDLocation(long idLocation) {
        errMsg = "";
        List<TblLocationOfLaundry> list = new ArrayList();
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
            }
        }
        return list.isEmpty() ? null : list.get(0);
    }

    //--------------------------------------------------------------------------
    @Override
    public TblOutGoing insertDataOutGoing(TblOutGoing og,
            List<WarehouseOutGoingController.OGDetailItemMutationHistory> ogdimhs) {
        errMsg = "";
        TblOutGoing tblOutGoing = og;
        if (ClassSession.checkUserSession()) {  //check user current session
            try {
                session = HibernateUtil.getSessionFactory().getCurrentSession();
                session.beginTransaction();
                //data out going
                tblOutGoing.setCodeOg(ClassCoder.getCode("Out Going", session));
                tblOutGoing.setOgdate(Timestamp.valueOf(LocalDateTime.now()));
                tblOutGoing.setCreateDate(Timestamp.valueOf(LocalDateTime.now()));
                tblOutGoing.setTblEmployeeByCreateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                tblOutGoing.setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                tblOutGoing.setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                tblOutGoing.setRefRecordStatus(session.find(RefRecordStatus.class, 1));
                session.saveOrUpdate(tblOutGoing);
                //data store request
                if (tblOutGoing.getTblStoreRequest() != null
                        && tblOutGoing.getTblStoreRequest().getIdsr() != 0L) {
                    tblOutGoing.getTblStoreRequest().setApprovedDate(Timestamp.valueOf(LocalDateTime.now()));
                    tblOutGoing.getTblStoreRequest().setTblEmployeeByApprovedBy(tblOutGoing.getTblEmployeeByCreateBy());
                    tblOutGoing.getTblStoreRequest().setReceivedDate(Timestamp.valueOf(LocalDateTime.now()));
                    tblOutGoing.getTblStoreRequest().setTblEmployeeByReceivedBy(tblOutGoing.getTblEmployeeByIdreceiver());
                    tblOutGoing.getTblStoreRequest().setRefStoreRequestStatus(session.find(RefStoreRequestStatus.class, 6)); //Selesai = '6'
                    tblOutGoing.getTblStoreRequest().setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                    tblOutGoing.getTblStoreRequest().setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                    session.saveOrUpdate(tblOutGoing.getTblStoreRequest());
                }
                for (WarehouseOutGoingController.OGDetailItemMutationHistory ogdimh : ogdimhs) {
                    //data out going detail
                    ogdimh.getDataOGDIMH().getTblOutGoingDetail().setCreateDate(Timestamp.valueOf(LocalDateTime.now()));
                    ogdimh.getDataOGDIMH().getTblOutGoingDetail().setTblEmployeeByCreateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                    ogdimh.getDataOGDIMH().getTblOutGoingDetail().setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                    ogdimh.getDataOGDIMH().getTblOutGoingDetail().setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                    ogdimh.getDataOGDIMH().getTblOutGoingDetail().setRefRecordStatus(session.find(RefRecordStatus.class, 1));
                    session.saveOrUpdate(ogdimh.getDataOGDIMH().getTblOutGoingDetail());
                    //data item mutation history
//                    ogdimh.getDataOGDIMH().getTblItemMutationHistory().setTblEmployeeByIdemployee(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                    ogdimh.getDataOGDIMH().getTblItemMutationHistory().setMutationDate(Timestamp.valueOf(LocalDateTime.now()));
                    ogdimh.getDataOGDIMH().getTblItemMutationHistory().setRefItemMutationType(session.find(RefItemMutationType.class, (int) 0)); //Dipindahkan = '0'
                    ogdimh.getDataOGDIMH().getTblItemMutationHistory().setRefRecordStatus(session.find(RefRecordStatus.class, (int) 1));
                    ogdimh.getDataOGDIMH().getTblItemMutationHistory().setTblEmployeeByCreateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                    ogdimh.getDataOGDIMH().getTblItemMutationHistory().setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                    ogdimh.getDataOGDIMH().getTblItemMutationHistory().setCreateDate(Timestamp.valueOf(LocalDateTime.now()));
                    ogdimh.getDataOGDIMH().getTblItemMutationHistory().setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                    session.saveOrUpdate(ogdimh.getDataOGDIMH().getTblItemMutationHistory());
                    //data item location (source, -)
                    TblItemLocation sourceItemLocation = getItemLocationByIDItemAndLocation(
                            ogdimh.getDataOGDIMH().getTblItemMutationHistory().getTblItem().getIditem(),
                            ogdimh.getDataOGDIMH().getTblItemMutationHistory().getTblLocationByIdlocationOfSource().getIdlocation());
                    if (sourceItemLocation == null) {
                        errMsg = "Barang (" + ogdimh.getDataOGDIMH().getTblItemMutationHistory().getTblItem().getItemName() + ") tidak ditemukan..!";
                        if (session.getTransaction().isActive()) {
                            session.getTransaction().rollback();
                        }
                        return null;
                    }
                    sourceItemLocation.setItemQuantity(sourceItemLocation.getItemQuantity().subtract(ogdimh.getDataOGDIMH().getTblItemMutationHistory().getItemQuantity()));
                    sourceItemLocation.setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                    sourceItemLocation.setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                    if (sourceItemLocation.getItemQuantity()
                            .compareTo(new BigDecimal("0")) == -1) {   //data item quantity < 0
                        errMsg = "Stok Barang (" + ogdimh.getDataOGDIMH().getTblItemMutationHistory().getTblItem().getItemName() + ") tidak mencukupi..!";
                        if (session.getTransaction().isActive()) {
                            session.getTransaction().rollback();
                        }
                        return null;
                    }
                    session.update(sourceItemLocation);
                    //data item location (destination, +)
                    TblItemLocation destinationItemLocation = getItemLocationByIDItemAndLocation(
                            ogdimh.getDataOGDIMH().getTblItemMutationHistory().getTblItem().getIditem(),
                            ogdimh.getDataOGDIMH().getTblItemMutationHistory().getTblLocationByIdlocationOfDestination().getIdlocation());
                    if (destinationItemLocation != null) {
                        destinationItemLocation.setItemQuantity(destinationItemLocation.getItemQuantity().add(ogdimh.getDataOGDIMH().getTblItemMutationHistory().getItemQuantity()));
                        destinationItemLocation.setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                        destinationItemLocation.setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                        session.update(destinationItemLocation);
                    } else {
                        destinationItemLocation = new TblItemLocation();
                        destinationItemLocation.setTblItem(ogdimh.getDataOGDIMH().getTblItemMutationHistory().getTblItem());
                        destinationItemLocation.setTblLocation(ogdimh.getDataOGDIMH().getTblItemMutationHistory().getTblLocationByIdlocationOfDestination());
                        destinationItemLocation.setItemQuantity(ogdimh.getDataOGDIMH().getTblItemMutationHistory().getItemQuantity());
                        destinationItemLocation.setRefRecordStatus(session.find(RefRecordStatus.class, (int) 1));
                        destinationItemLocation.setTblEmployeeByCreateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                        destinationItemLocation.setCreateDate(Timestamp.valueOf(LocalDateTime.now()));
                        destinationItemLocation.setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                        destinationItemLocation.setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                        session.saveOrUpdate(destinationItemLocation);
                    }
                    //data out going detail - item mutation history
                    ogdimh.getDataOGDIMH().setRefRecordStatus(session.find(RefRecordStatus.class, (int) 1));
                    ogdimh.getDataOGDIMH().setTblEmployeeByCreateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                    ogdimh.getDataOGDIMH().setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                    ogdimh.getDataOGDIMH().setCreateDate(Timestamp.valueOf(LocalDateTime.now()));
                    ogdimh.getDataOGDIMH().setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                    session.saveOrUpdate(ogdimh.getDataOGDIMH());
                    //data item mutation history - property
                    for (WarehouseOutGoingController.IMHPropertyBarcodeSelected impbs : ogdimh.getDataIMHPBSs()) {
                        if (impbs.isSelected()) { //selected
                            impbs.getDataItemMutationHistoryPropertyBarcode().setRefRecordStatus(session.find(RefRecordStatus.class, (int) 1));
                            impbs.getDataItemMutationHistoryPropertyBarcode().setTblEmployeeByCreateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                            impbs.getDataItemMutationHistoryPropertyBarcode().setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                            impbs.getDataItemMutationHistoryPropertyBarcode().setCreateDate(Timestamp.valueOf(LocalDateTime.now()));
                            impbs.getDataItemMutationHistoryPropertyBarcode().setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                            session.saveOrUpdate(impbs.getDataItemMutationHistoryPropertyBarcode());
                            //data item location - property barcode (source, remove)
                            TblItemLocationPropertyBarcode sourceItemLocationPropertyBarcode = getItemLocationPropertyBarcodeByIDItemLocationAndDIPropertyBarcode(
                                    sourceItemLocation.getIdrelation(),
                                    impbs.getDataItemMutationHistoryPropertyBarcode().getTblPropertyBarcode().getIdbarcode());
                            if (sourceItemLocationPropertyBarcode == null) {
                                errMsg = "Barang (" + impbs.getDataItemMutationHistoryPropertyBarcode().getTblPropertyBarcode().getTblItem().getItemName()
                                        + " - " + impbs.getDataItemMutationHistoryPropertyBarcode().getTblPropertyBarcode().getCodeBarcode()
                                        + ") tidak ditemukan..!";
                                if (session.getTransaction().isActive()) {
                                    session.getTransaction().rollback();
                                }
                                return null;
                            }
                            sourceItemLocationPropertyBarcode.setRefRecordStatus(session.find(RefRecordStatus.class, (int) 2));
                            sourceItemLocationPropertyBarcode.setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                            sourceItemLocationPropertyBarcode.setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                            session.saveOrUpdate(sourceItemLocationPropertyBarcode);
                            //data item location - property barcode (destination, add)
                            TblItemLocationPropertyBarcode destinationItemLocationPropertyBarcode = getItemLocationPropertyBarcodeByIDItemLocationAndDIPropertyBarcode(
                                    destinationItemLocation.getIdrelation(),
                                    impbs.getDataItemMutationHistoryPropertyBarcode().getTblPropertyBarcode().getIdbarcode());
                            if (destinationItemLocationPropertyBarcode != null) {
                                destinationItemLocationPropertyBarcode.setRefRecordStatus(session.find(RefRecordStatus.class, (int) 1));
                                destinationItemLocationPropertyBarcode.setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                                destinationItemLocationPropertyBarcode.setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                                session.saveOrUpdate(destinationItemLocationPropertyBarcode);
                            } else {    //create new data
                                destinationItemLocationPropertyBarcode = new TblItemLocationPropertyBarcode();
                                destinationItemLocationPropertyBarcode.setTblItemLocation(destinationItemLocation);
                                destinationItemLocationPropertyBarcode.setTblPropertyBarcode(impbs.getDataItemMutationHistoryPropertyBarcode().getTblPropertyBarcode());
                                destinationItemLocationPropertyBarcode.setRefRecordStatus(session.find(RefRecordStatus.class, (int) 1));
                                destinationItemLocationPropertyBarcode.setTblEmployeeByCreateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                                destinationItemLocationPropertyBarcode.setCreateDate(Timestamp.valueOf(LocalDateTime.now()));
                                destinationItemLocationPropertyBarcode.setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                                destinationItemLocationPropertyBarcode.setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                                session.saveOrUpdate(destinationItemLocationPropertyBarcode);
                            }
                        }
                    }
                    //data item mutation history - item expired date
                    for (TblItemMutationHistoryItemExpiredDate imhied : ogdimh.getDataIMHIEDs()) {
                        imhied.setRefRecordStatus(session.find(RefRecordStatus.class, (int) 1));
                        imhied.setTblEmployeeByCreateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                        imhied.setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                        imhied.setCreateDate(Timestamp.valueOf(LocalDateTime.now()));
                        imhied.setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                        session.saveOrUpdate(imhied);
                        //data item location - item expired date (source, -)
                        TblItemLocationItemExpiredDate sourceItemLocationItemExpiredDate = getItemLocationItemExpiredDateByIDItemLocationAndIDItemExpiredDate(
                                sourceItemLocation.getIdrelation(),
                                imhied.getTblItemExpiredDate().getIditemExpiredDate());
                        if (sourceItemLocationItemExpiredDate == null) {
                            errMsg = "Barang (" + imhied.getTblItemExpiredDate().getTblItem().getItemName()
                                    + " - " + ClassFormatter.dateFormate.format(imhied.getTblItemExpiredDate().getItemExpiredDate())
                                    + ") tidak ditemukan..!";
                            if (session.getTransaction().isActive()) {
                                session.getTransaction().rollback();
                            }
                            return null;
                        }
                        sourceItemLocationItemExpiredDate.setItemQuantity(sourceItemLocationItemExpiredDate.getItemQuantity().subtract(imhied.getItemQuantity()));
                        sourceItemLocationItemExpiredDate.setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                        sourceItemLocationItemExpiredDate.setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                        if (sourceItemLocationItemExpiredDate.getItemQuantity()
                                .compareTo(new BigDecimal("0")) == -1) {   //data item quantity < 0
                            errMsg = "Stok Barang (" + imhied.getTblItemExpiredDate().getTblItem().getItemName()
                                    + " - " + ClassFormatter.dateFormate.format(imhied.getTblItemExpiredDate().getItemExpiredDate())
                                    + ") tidak mencukupi..!";
                            if (session.getTransaction().isActive()) {
                                session.getTransaction().rollback();
                            }
                            return null;
                        }
                        session.update(sourceItemLocationItemExpiredDate);
                        //data item location - item expired date (destination, +)
                        TblItemLocationItemExpiredDate destinationItemLocationItemExpiredDate = getItemLocationItemExpiredDateByIDItemLocationAndIDItemExpiredDate(
                                destinationItemLocation.getIdrelation(),
                                imhied.getTblItemExpiredDate().getIditemExpiredDate());
                        if (destinationItemLocationItemExpiredDate != null) {
                            destinationItemLocationItemExpiredDate.setItemQuantity(destinationItemLocationItemExpiredDate.getItemQuantity().add(imhied.getItemQuantity()));
                            destinationItemLocationItemExpiredDate.setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                            destinationItemLocationItemExpiredDate.setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                            session.update(destinationItemLocationItemExpiredDate);
                        } else {
                            destinationItemLocationItemExpiredDate = new TblItemLocationItemExpiredDate();
                            destinationItemLocationItemExpiredDate.setTblItemLocation(destinationItemLocation);
                            destinationItemLocationItemExpiredDate.setTblItemExpiredDate(imhied.getTblItemExpiredDate());
                            destinationItemLocationItemExpiredDate.setItemQuantity(imhied.getItemQuantity());
                            destinationItemLocationItemExpiredDate.setRefRecordStatus(session.find(RefRecordStatus.class, (int) 1));
                            destinationItemLocationItemExpiredDate.setTblEmployeeByCreateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                            destinationItemLocationItemExpiredDate.setCreateDate(Timestamp.valueOf(LocalDateTime.now()));
                            destinationItemLocationItemExpiredDate.setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                            destinationItemLocationItemExpiredDate.setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                            session.saveOrUpdate(destinationItemLocationItemExpiredDate);
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
        return tblOutGoing;
    }

    @Override
    public TblOutGoing getDataOutGoing(long id) {
        errMsg = "";
        TblOutGoing data = null;
        if (ClassSession.checkUserSession()) {  //check user current session
            try {
                session = HibernateUtil.getSessionFactory().getCurrentSession();
                session.beginTransaction();
                data = (TblOutGoing) session.find(TblOutGoing.class, id);
                session.getTransaction().commit();
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
    public List<TblOutGoing> getAllDataOutGoing() {
        errMsg = "";
        List<TblOutGoing> list = new ArrayList();
        if (ClassSession.checkUserSession()) {  //check user current session
            try {
                session = HibernateUtil.getSessionFactory().getCurrentSession();
                session.beginTransaction();
                list = session.getNamedQuery("findAllTblOutGoing").list();
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
    public List<TblOutGoingDetail> getAllDataOutGoingDetailByIDOutGoing(long idOutGoing) {
        errMsg = "";
        List<TblOutGoingDetail> list = new ArrayList<>();
        if (ClassSession.checkUserSession()) {  //check user current session
            try {
                session = HibernateUtil.getSessionFactory().getCurrentSession();
                session.beginTransaction();
                list = session.getNamedQuery("findAllTblOutGoingDetailByIDOutGoing")
                        .setParameter("idOutGoing", idOutGoing)
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
    public TblOutGoingDetailItemMutationHistory getDataOutGoingDetailItemMutationHistoryByIDOutGoingDetail(long idOutGoingDetail) {
        errMsg = "";
        List<TblOutGoingDetailItemMutationHistory> list = new ArrayList<>();
        if (ClassSession.checkUserSession()) {  //check user current session
            try {
                session = HibernateUtil.getSessionFactory().getCurrentSession();
                session.beginTransaction();
                list = session.getNamedQuery("findAllTblOutGoingDetailItemMutationHistoryByIDOutGoingDetail")
                        .setParameter("idOutGoingDetail", idOutGoingDetail)
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
    public TblInComing insertDataInComing(TblInComing ic,
            List<WarehouseInComingController.ICDetailItemMutationHistory> icdimhs) {
        errMsg = "";
        TblInComing tblInComing = ic;
        if (ClassSession.checkUserSession()) {  //check user current session
            try {
                session = HibernateUtil.getSessionFactory().getCurrentSession();
                session.beginTransaction();
                //data out going
                tblInComing.setCodeIc(ClassCoder.getCode("In Coming", session));
                tblInComing.setIcdate(Timestamp.valueOf(LocalDateTime.now()));
                tblInComing.setCreateDate(Timestamp.valueOf(LocalDateTime.now()));
                tblInComing.setTblEmployeeByCreateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                tblInComing.setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                tblInComing.setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                tblInComing.setRefRecordStatus(session.find(RefRecordStatus.class, 1));
                session.saveOrUpdate(tblInComing);
                //data details
                for (WarehouseInComingController.ICDetailItemMutationHistory icdimh : icdimhs) {
                    //data out going detail
                    icdimh.getDataICDIMH().getTblInComingDetail().setCreateDate(Timestamp.valueOf(LocalDateTime.now()));
                    icdimh.getDataICDIMH().getTblInComingDetail().setTblEmployeeByCreateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                    icdimh.getDataICDIMH().getTblInComingDetail().setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                    icdimh.getDataICDIMH().getTblInComingDetail().setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                    icdimh.getDataICDIMH().getTblInComingDetail().setRefRecordStatus(session.find(RefRecordStatus.class, 1));
                    session.saveOrUpdate(icdimh.getDataICDIMH().getTblInComingDetail());
                    //data item mutation history
//                    icdimh.getDataICDIMH().getTblItemMutationHistory().setTblEmployeeByIdemployee(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                    icdimh.getDataICDIMH().getTblItemMutationHistory().setMutationDate(Timestamp.valueOf(LocalDateTime.now()));
                    icdimh.getDataICDIMH().getTblItemMutationHistory().setRefItemMutationType(session.find(RefItemMutationType.class, (int) 0)); //Dipindahkan = '0'
                    icdimh.getDataICDIMH().getTblItemMutationHistory().setRefRecordStatus(session.find(RefRecordStatus.class, (int) 1));
                    icdimh.getDataICDIMH().getTblItemMutationHistory().setTblEmployeeByCreateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                    icdimh.getDataICDIMH().getTblItemMutationHistory().setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                    icdimh.getDataICDIMH().getTblItemMutationHistory().setCreateDate(Timestamp.valueOf(LocalDateTime.now()));
                    icdimh.getDataICDIMH().getTblItemMutationHistory().setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                    session.saveOrUpdate(icdimh.getDataICDIMH().getTblItemMutationHistory());
                    //data item location (source, -)
                    TblItemLocation sourceItemLocation = getItemLocationByIDItemAndLocation(
                            icdimh.getDataICDIMH().getTblItemMutationHistory().getTblItem().getIditem(),
                            icdimh.getDataICDIMH().getTblItemMutationHistory().getTblLocationByIdlocationOfSource().getIdlocation());
                    if (sourceItemLocation == null) {
                        errMsg = "Barang (" + icdimh.getDataICDIMH().getTblItemMutationHistory().getTblItem().getItemName() + ") tidak ditemukan..!";
                        if (session.getTransaction().isActive()) {
                            session.getTransaction().rollback();
                        }
                        return null;
                    }
                    sourceItemLocation.setItemQuantity(sourceItemLocation.getItemQuantity().subtract(icdimh.getDataICDIMH().getTblItemMutationHistory().getItemQuantity()));
                    sourceItemLocation.setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                    sourceItemLocation.setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                    if (sourceItemLocation.getItemQuantity()
                            .compareTo(new BigDecimal("0")) == -1) {   //data item quantity < 0
                        errMsg = "Stok Barang (" + icdimh.getDataICDIMH().getTblItemMutationHistory().getTblItem().getItemName() + ") tidak mencukupi..!";
                        if (session.getTransaction().isActive()) {
                            session.getTransaction().rollback();
                        }
                        return null;
                    }
                    session.update(sourceItemLocation);
                    //data item location (destination, +)
                    TblItemLocation destinationItemLocation = getItemLocationByIDItemAndLocation(
                            icdimh.getDataICDIMH().getTblItemMutationHistory().getTblItem().getIditem(),
                            icdimh.getDataICDIMH().getTblItemMutationHistory().getTblLocationByIdlocationOfDestination().getIdlocation());
                    if (destinationItemLocation != null) {
                        destinationItemLocation.setItemQuantity(destinationItemLocation.getItemQuantity().add(icdimh.getDataICDIMH().getTblItemMutationHistory().getItemQuantity()));
                        destinationItemLocation.setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                        destinationItemLocation.setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                        session.update(destinationItemLocation);
                    } else {
                        destinationItemLocation = new TblItemLocation();
                        destinationItemLocation.setTblItem(icdimh.getDataICDIMH().getTblItemMutationHistory().getTblItem());
                        destinationItemLocation.setTblLocation(icdimh.getDataICDIMH().getTblItemMutationHistory().getTblLocationByIdlocationOfDestination());
                        destinationItemLocation.setItemQuantity(icdimh.getDataICDIMH().getTblItemMutationHistory().getItemQuantity());
                        destinationItemLocation.setRefRecordStatus(session.find(RefRecordStatus.class, (int) 1));
                        destinationItemLocation.setTblEmployeeByCreateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                        destinationItemLocation.setCreateDate(Timestamp.valueOf(LocalDateTime.now()));
                        destinationItemLocation.setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                        destinationItemLocation.setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                        session.saveOrUpdate(destinationItemLocation);
                    }
                    //data out going detail - item mutation history
                    icdimh.getDataICDIMH().setRefRecordStatus(session.find(RefRecordStatus.class, (int) 1));
                    icdimh.getDataICDIMH().setTblEmployeeByCreateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                    icdimh.getDataICDIMH().setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                    icdimh.getDataICDIMH().setCreateDate(Timestamp.valueOf(LocalDateTime.now()));
                    icdimh.getDataICDIMH().setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                    session.saveOrUpdate(icdimh.getDataICDIMH());
                    //data item mutation history - property
                    for (WarehouseInComingController.IMHPropertyBarcodeSelected impbs : icdimh.getDataIMHPBSs()) {
                        if (impbs.isSelected()) { //selected
                            impbs.getDataItemMutationHistoryPropertyBarcode().setRefRecordStatus(session.find(RefRecordStatus.class, (int) 1));
                            impbs.getDataItemMutationHistoryPropertyBarcode().setTblEmployeeByCreateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                            impbs.getDataItemMutationHistoryPropertyBarcode().setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                            impbs.getDataItemMutationHistoryPropertyBarcode().setCreateDate(Timestamp.valueOf(LocalDateTime.now()));
                            impbs.getDataItemMutationHistoryPropertyBarcode().setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                            session.saveOrUpdate(impbs.getDataItemMutationHistoryPropertyBarcode());
                            //data item location - property barcode (source, remove)
                            TblItemLocationPropertyBarcode sourceItemLocationPropertyBarcode = getItemLocationPropertyBarcodeByIDItemLocationAndDIPropertyBarcode(
                                    sourceItemLocation.getIdrelation(),
                                    impbs.getDataItemMutationHistoryPropertyBarcode().getTblPropertyBarcode().getIdbarcode());
                            if (sourceItemLocationPropertyBarcode == null) {
                                errMsg = "Barang (" + impbs.getDataItemMutationHistoryPropertyBarcode().getTblPropertyBarcode().getTblItem().getItemName()
                                        + " - " + impbs.getDataItemMutationHistoryPropertyBarcode().getTblPropertyBarcode().getCodeBarcode()
                                        + ") tidak ditemukan..!";
                                if (session.getTransaction().isActive()) {
                                    session.getTransaction().rollback();
                                }
                                return null;
                            }
                            sourceItemLocationPropertyBarcode.setRefRecordStatus(session.find(RefRecordStatus.class, (int) 2));
                            sourceItemLocationPropertyBarcode.setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                            sourceItemLocationPropertyBarcode.setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                            session.saveOrUpdate(sourceItemLocationPropertyBarcode);
                            //data item location - property barcode (destination, add)
                            TblItemLocationPropertyBarcode destinationItemLocationPropertyBarcode = getItemLocationPropertyBarcodeByIDItemLocationAndDIPropertyBarcode(
                                    destinationItemLocation.getIdrelation(),
                                    impbs.getDataItemMutationHistoryPropertyBarcode().getTblPropertyBarcode().getIdbarcode());
                            if (destinationItemLocationPropertyBarcode != null) {
                                destinationItemLocationPropertyBarcode.setRefRecordStatus(session.find(RefRecordStatus.class, (int) 1));
                                destinationItemLocationPropertyBarcode.setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                                destinationItemLocationPropertyBarcode.setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                                session.saveOrUpdate(destinationItemLocationPropertyBarcode);
                            } else {    //create new data
                                destinationItemLocationPropertyBarcode = new TblItemLocationPropertyBarcode();
                                destinationItemLocationPropertyBarcode.setTblItemLocation(destinationItemLocation);
                                destinationItemLocationPropertyBarcode.setTblPropertyBarcode(impbs.getDataItemMutationHistoryPropertyBarcode().getTblPropertyBarcode());
                                destinationItemLocationPropertyBarcode.setRefRecordStatus(session.find(RefRecordStatus.class, (int) 1));
                                destinationItemLocationPropertyBarcode.setTblEmployeeByCreateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                                destinationItemLocationPropertyBarcode.setCreateDate(Timestamp.valueOf(LocalDateTime.now()));
                                destinationItemLocationPropertyBarcode.setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                                destinationItemLocationPropertyBarcode.setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                                session.saveOrUpdate(destinationItemLocationPropertyBarcode);
                            }
                        }
                    }
                    //data item mutation history - item expired date
                    for (TblItemMutationHistoryItemExpiredDate imhied : icdimh.getDataIMHIEDs()) {
                        imhied.setRefRecordStatus(session.find(RefRecordStatus.class, (int) 1));
                        imhied.setTblEmployeeByCreateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                        imhied.setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                        imhied.setCreateDate(Timestamp.valueOf(LocalDateTime.now()));
                        imhied.setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                        session.saveOrUpdate(imhied);
                        //data item location - item expired date (source, -)
                        TblItemLocationItemExpiredDate sourceItemLocationItemExpiredDate = getItemLocationItemExpiredDateByIDItemLocationAndIDItemExpiredDate(
                                sourceItemLocation.getIdrelation(),
                                imhied.getTblItemExpiredDate().getIditemExpiredDate());
                        if (sourceItemLocationItemExpiredDate == null) {
                            errMsg = "Barang (" + imhied.getTblItemExpiredDate().getTblItem().getItemName()
                                    + " - " + ClassFormatter.dateFormate.format(imhied.getTblItemExpiredDate().getItemExpiredDate())
                                    + ") tidak ditemukan..!";
                            if (session.getTransaction().isActive()) {
                                session.getTransaction().rollback();
                            }
                            return null;
                        }
                        sourceItemLocationItemExpiredDate.setItemQuantity(sourceItemLocationItemExpiredDate.getItemQuantity().subtract(imhied.getItemQuantity()));
                        sourceItemLocationItemExpiredDate.setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                        sourceItemLocationItemExpiredDate.setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                        if (sourceItemLocationItemExpiredDate.getItemQuantity()
                                .compareTo(new BigDecimal("0")) == -1) {   //data item quantity < 0
                            errMsg = "Stok Barang (" + imhied.getTblItemExpiredDate().getTblItem().getItemName()
                                    + " - " + ClassFormatter.dateFormate.format(imhied.getTblItemExpiredDate().getItemExpiredDate())
                                    + ") tidak mencukupi..!";
                            if (session.getTransaction().isActive()) {
                                session.getTransaction().rollback();
                            }
                            return null;
                        }
                        session.update(sourceItemLocationItemExpiredDate);
                        //data item location - item expired date (destination, +)
                        TblItemLocationItemExpiredDate destinationItemLocationItemExpiredDate = getItemLocationItemExpiredDateByIDItemLocationAndIDItemExpiredDate(
                                destinationItemLocation.getIdrelation(),
                                imhied.getTblItemExpiredDate().getIditemExpiredDate());
                        if (destinationItemLocationItemExpiredDate != null) {
                            destinationItemLocationItemExpiredDate.setItemQuantity(destinationItemLocationItemExpiredDate.getItemQuantity().add(imhied.getItemQuantity()));
                            destinationItemLocationItemExpiredDate.setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                            destinationItemLocationItemExpiredDate.setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                            session.update(destinationItemLocationItemExpiredDate);
                        } else {
                            destinationItemLocationItemExpiredDate = new TblItemLocationItemExpiredDate();
                            destinationItemLocationItemExpiredDate.setTblItemLocation(destinationItemLocation);
                            destinationItemLocationItemExpiredDate.setTblItemExpiredDate(imhied.getTblItemExpiredDate());
                            destinationItemLocationItemExpiredDate.setItemQuantity(imhied.getItemQuantity());
                            destinationItemLocationItemExpiredDate.setRefRecordStatus(session.find(RefRecordStatus.class, (int) 1));
                            destinationItemLocationItemExpiredDate.setTblEmployeeByCreateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                            destinationItemLocationItemExpiredDate.setCreateDate(Timestamp.valueOf(LocalDateTime.now()));
                            destinationItemLocationItemExpiredDate.setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                            destinationItemLocationItemExpiredDate.setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                            session.saveOrUpdate(destinationItemLocationItemExpiredDate);
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
        return tblInComing;
    }

    @Override
    public TblInComing getDataInComing(long id) {
        errMsg = "";
        TblInComing data = null;
        if (ClassSession.checkUserSession()) {  //check user current session
            try {
                session = HibernateUtil.getSessionFactory().getCurrentSession();
                session.beginTransaction();
                data = (TblInComing) session.find(TblInComing.class, id);
                session.getTransaction().commit();
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
    public List<TblInComing> getAllDataInComing() {
        errMsg = "";
        List<TblInComing> list = new ArrayList();
        if (ClassSession.checkUserSession()) {  //check user current session
            try {
                session = HibernateUtil.getSessionFactory().getCurrentSession();
                session.beginTransaction();
                list = session.getNamedQuery("findAllTblInComing").list();
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
    public List<TblInComingDetail> getAllDataInComingDetailByIDInComing(long idInComing) {
        errMsg = "";
        List<TblInComingDetail> list = new ArrayList<>();
        if (ClassSession.checkUserSession()) {  //check user current session
            try {
                session = HibernateUtil.getSessionFactory().getCurrentSession();
                session.beginTransaction();
                list = session.getNamedQuery("findAllTblInComingDetailByIDInComing")
                        .setParameter("idInComing", idInComing)
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
    public TblInComingDetailItemMutationHistory getDataInComingDetailItemMutationHistoryByIDInComingDetail(long idInComingDetail) {
        errMsg = "";
        List<TblInComingDetailItemMutationHistory> list = new ArrayList<>();
        if (ClassSession.checkUserSession()) {  //check user current session
            try {
                session = HibernateUtil.getSessionFactory().getCurrentSession();
                session.beginTransaction();
                list = session.getNamedQuery("findAllTblInComingDetailItemMutationHistoryByIDInComingDetail")
                        .setParameter("idInComingDetail", idInComingDetail)
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
    public String getErrorMessage() {
        return errMsg;
    }

//public static void main(String[] args) {
    //getAllDataMutationHistory();
   /* List<ClassDataTransferItem>tblTransferItem = getAllDataTransferItem();
     for(ClassDataTransferItem x : tblTransferItem){
     System.out.println("Hasil:"+x.getTblItemLocation().getTblItem().getItemName());  
     }*/
    //getDataItemTransfer();
     /* List<TblLocation>tblLocation = getAllDataDestinationLocation(1); 
     for(int i = 0; i<tblLocation.size();i++)
     {
     TblLocation x = tblLocation.get(i);
         
     System.out.println(x.getTblLocationOfWarehouses().size());
     Iterator iter = x.getTblLocationOfWarehouses().iterator();
     while (iter.hasNext()) {
     System.out.println(((TblLocationOfWarehouse)iter.next()).getWarehouseName());
     }   
     }*/
    /* List<Object[]>x = getAllDataLocationWarehouse();
     for(Object[] row : x){
     TblLocation e = (TblLocation) row[0];
     System.out.println("Location"+e.getLocationName());
     TblLocationOfWarehouse a = (TblLocationOfWarehouse) row[1];
     System.out.println("Address Info::"+a.getWarehouseName());
     }
      
     /*List<TblItemLocation>tblItem = getDataItemLocationByItemAndLocation(4,1);
     for(int i = 0; i<tblItem.size();i++)
     {
     TblItemLocation x = tblItem.get(i);
     System.out.println(x.getTblItem().getItemName()+x.getTblLocation().getCodeLocation()+ x.getTblLocation().getRefLocationType().getTypeName());
     }
      
     List<TblItemLocationPropertyBarcode>tblItemLoc = getDataItemLocationPropertyBarcodeByItemLocationAndPropertyBarcode(1,1);
     for(int i = 0; i<tblItemLoc.size();i++)
     {
     TblItemLocationPropertyBarcode x = tblItemLoc.get(i);
     System.out.println(x.getTblItemLocation().getIdrelation()+x.getTblPropertyBarcode().getCodeBarcode());
     }*/
//}
}
