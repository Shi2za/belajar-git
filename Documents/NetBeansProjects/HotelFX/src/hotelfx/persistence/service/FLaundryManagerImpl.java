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
import hotelfx.persistence.model.TblSupplier;
import hotelfx.persistence.model.TblUnit;
import hotelfx.view.feature_laundry.laundry_in_coming.LaundryInComingController;
import hotelfx.view.feature_laundry.laundry_out_going.LaundryOutGoingController;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import org.hibernate.Session;
import org.hibernate.query.Query;

/**
 *
 * @author Andreas
 */
public class FLaundryManagerImpl implements FLaundryManager {

    Session session;
    String errMessage;

    @Override
    public TblLocationOfLaundry insertDataLaundry(TblLocationOfLaundry laundry) {
        errMessage = "";
        TblLocationOfLaundry tblLaundry = laundry;
        if (ClassSession.checkUserSession()) {  //check user current session
            try {
                session = HibernateUtil.getSessionFactory().getCurrentSession();
                session.beginTransaction();
                //data location
                tblLaundry.getTblLocation().setRefRecordStatus(session.find(RefRecordStatus.class, 1));
                tblLaundry.getTblLocation().setTblEmployeeByCreateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                tblLaundry.getTblLocation().setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                tblLaundry.getTblLocation().setCreateDate(Timestamp.valueOf(LocalDateTime.now()));
                tblLaundry.getTblLocation().setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                tblLaundry.getTblLocation().setRefLocationType(session.find(RefLocationType.class, 2));
                session.saveOrUpdate(tblLaundry.getTblLocation());
                //data laundry
                tblLaundry.setRefRecordStatus(session.find(RefRecordStatus.class, 1));
                tblLaundry.getTblLocation().setCodeLocation("L" + tblLaundry.getTblLocation().getIdlocation());
                tblLaundry.setTblEmployeeByCreateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                tblLaundry.setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                tblLaundry.setCreateDate(Timestamp.valueOf(LocalDateTime.now()));
                tblLaundry.setLastUpdateDate(java.sql.Timestamp.valueOf(LocalDateTime.now()));
                session.saveOrUpdate(tblLaundry);
                //data item - location (laundry - all items)
                List<TblItem> dataItems = getAllItem();
                for (TblItem dataItem : dataItems) {
                    TblItemLocation dataItemLocation = new TblItemLocation();
                    dataItemLocation.setTblItem(dataItem);
                    dataItemLocation.setTblLocation(tblLaundry.getTblLocation());
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
                errMessage = e.getMessage();
                return null;
            } finally {
                //session.close();
            }
        } else {
            return null;
        }
        return tblLaundry;
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
    public boolean updateDataLaundry(TblLocationOfLaundry laundry) {
        errMessage = "";
        if (ClassSession.checkUserSession()) {  //check user current session
            try {
                session = HibernateUtil.getSessionFactory().getCurrentSession();
                session.beginTransaction();
                //data location
                laundry.getTblLocation().setRefRecordStatus(session.find(RefRecordStatus.class, 1));
                laundry.getTblLocation().setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                laundry.getTblLocation().setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                session.update(laundry.getTblLocation());
                //data laundry
                laundry.setRefRecordStatus(session.find(RefRecordStatus.class, 1));
                laundry.setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                laundry.setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                session.update(laundry);
                session.getTransaction().commit();
            } catch (Exception e) {
                if (session.getTransaction().isActive()) {
                    session.getTransaction().rollback();
                }
                errMessage = e.getMessage();
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
    public boolean deleteDataLaundry(TblLocationOfLaundry laundry) {
        errMessage = "";
        if (ClassSession.checkUserSession()) {  //check user current session
            try {
                session = HibernateUtil.getSessionFactory().getCurrentSession();
                session.beginTransaction();
                if (laundry.getRefRecordStatus().getIdstatus() == 1) {
                    //data location
                    laundry.getTblLocation().setRefRecordStatus(session.find(RefRecordStatus.class, 2));
                    laundry.getTblLocation().setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                    laundry.getTblLocation().setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                    session.update(laundry.getTblLocation());
                    //data laundry
                    laundry.setRefRecordStatus(session.find(RefRecordStatus.class, 2));
                    laundry.setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                    laundry.setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                    session.update(laundry);
                }

                session.getTransaction().commit();
            } catch (Exception e) {
                if (session.getTransaction().isActive()) {
                    session.getTransaction().rollback();
                }
                errMessage = e.getMessage();
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
    public List<TblLocationOfLaundry> getAllDataLaundry() {
        errMessage = "";
        List<TblLocationOfLaundry> list = new ArrayList();
        if (ClassSession.checkUserSession()) {  //check user current session
            try {
                session = HibernateUtil.getSessionFactory().getCurrentSession();
                session.beginTransaction();
                list = session.getNamedQuery("findAllTblLocationOfLaundry").list();
                session.getTransaction().commit();
            } catch (Exception e) {
                //e.printStackTrace();
                if (session.getTransaction().isActive()) {
                    session.getTransaction().rollback();
                }
                errMessage = e.getMessage();
            }
        }
        return list;
    }

    @Override
    public TblLocationOfLaundry getLaundry(long id) {
        errMessage = "";
        TblLocationOfLaundry data = null;
        if (ClassSession.checkUserSession()) {  //check user current session
            try {
                session = HibernateUtil.getSessionFactory().getCurrentSession();
                session.beginTransaction();
                data = (TblLocationOfLaundry) session.find(TblLocationOfLaundry.class, id);
                session.getTransaction().commit();
            } catch (Exception e) {
                if (session.getTransaction().isActive()) {
                    session.getTransaction().rollback();
                }
                errMessage = e.getMessage();
            } finally {
                //session.close();
            }
        }
        return data;
    }

    //sella
    @Override
    public TblLocationOfLaundry getDataLaundryByIdLocation(long id) {
        errMessage = "";
        List<TblLocationOfLaundry> list = new ArrayList();
        if (ClassSession.checkUserSession()) {  //check user current session
            try {
                session = HibernateUtil.getSessionFactory().getCurrentSession();
                session.beginTransaction();
                list = session.getNamedQuery("findAllTblLocationOfLaundryByIdLocation")
                        .setParameter("id", id).list();
                session.getTransaction().commit();
            } catch (Exception e) {
                if (session.getTransaction().isActive()) {
                    session.getTransaction().rollback();
                }
                errMessage = e.getMessage();
            }
        }
        return list.isEmpty() ? null : list.get(0);
    }

    @Override
    public TblLocation getLocation(long id) {
        errMessage = "";
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
                errMessage = e.getMessage();
            } finally {
                //session.close();
            }
        }
        return data;
    }

    @Override
    public TblLocation getLocationByIDLocationType(int idLocationType) {
        errMessage = "";
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
                errMessage = e.getMessage();
            } finally {
                //session.close();
            }
        }
        return list.isEmpty() ? null : list.get(0);
    }

    @Override
    public List<TblFloor> getAllDataFloor() {
        errMessage = "";
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
                errMessage = e.getMessage();
            } finally {
                //session.close();
            }
        }
        return list;
    }

    @Override
    public List<TblBuilding> getAllDataBuilding() {
        errMessage = "";
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
                errMessage = e.getMessage();
            } finally {
                //session.close();
            }
        }
        return list;
    }

    @Override
    public TblBuilding getDataBuilding(long id) {
        errMessage = "";
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
                errMessage = e.getMessage();
            }
        }
        return data;
    }

    @Override
    public TblFloor getDataFloor(long id) {
        errMessage = "";
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
                errMessage = e.getMessage();
            }
        }
        return data;
    }

    @Override
    public TblJob getJob(long id) {
        errMessage = "";
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
                errMessage = e.getMessage();
            } finally {
                //session.close();
            }
        }
        return data;
    }

    @Override
    public List<TblJob> getAllDataJob() {
        errMessage = "";
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
                errMessage = e.getMessage();
            } finally {
                //session.close();
            }
        }
        return list;
    }

    @Override
    public TblGroup getDataGroup(long id) {
        errMessage = "";
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
                errMessage = e.getMessage();
            } finally {
                //session.close();
            }
        }
        return data;
    }

    @Override
    public List<TblGroup> getAllDataGroup() {
        errMessage = "";
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
                errMessage = e.getMessage();
            } finally {
                //session.close();
            }
        }
        return list;
    }

    //-------------------------------------------------
    @Override
    public List<TblItemLocation> getAllDataItemLocation(long id) {
        errMessage = "";
        List<TblItemLocation> list = new ArrayList();
        if (ClassSession.checkUserSession()) {  //check user current session
            try {
                session = HibernateUtil.getSessionFactory().getCurrentSession();
                session.beginTransaction();
                list = session.getNamedQuery("findAllTblItemLocationByIdLocationAndIdType").setParameter("idLoc", id).setParameter("idType", 2)
                        .list();
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
    public RefLocationType getDataLocationType(int id) {
        errMessage = "";
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
                errMessage = e.getMessage();
            }
        }
        return data;
    }

    @Override
    public List<RefLocationType> getAllDataTypeLocation() {
        errMessage = "";
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
                errMessage = e.getMessage();
            }
        }
        return list;
    }

    @Override
    public List<TblLocation> getAllDataDestinationLocation(long id) {
        errMessage = "";
        List<TblLocation> list = new ArrayList();
        if (ClassSession.checkUserSession()) {  //check user current session
            try {
                session = HibernateUtil.getSessionFactory().getCurrentSession();
                session.beginTransaction();
                list = session.getNamedQuery("findAllTblLocationByLocationTypeId").setParameter("typeId", id).list();
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
    public List<RefItemObsoleteBy> getAllDataObsoleteBy() {
        errMessage = "";
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
                errMessage = e.getMessage();
            }
        }
        return list;
    }

//      @Override
//    public boolean updateDataLaundryTransferItem(ClassDataTransferItem transferItem,TblItemMutationHistory mutation,ClassInputLocation destinationLocation){
//       errMessage = "";
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
//        errMessage = e.getMessage();
//        return false;
//      }
//      return true;
//    }
    @Override
    public boolean updateDataLaundryTransferItem(ClassDataTransferItem transferItem,
            TblItemMutationHistory mutation,
            ClassInputLocation destinationLocation,
            String status) {
        errMessage = "";
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
                errMessage = e.getMessage();
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
        errMessage = "";
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
                    errMessage = e.getMessage();
                    return false;
                }
            }
        } else {
            return false;
        }
        return true;
    }
    /*@Override
     public boolean updateDataLaundryTransferItem(TblItemLocation sourceItemLocation,TblItemMutationHistory mutation,ClassInputLocation destinationLocation,TblItemLocationPropertyBarcode propertyBarcode){
     errMessage = "";
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
     errMessage = e.getMessage();
     return false;
     }
     return true;
     }*/

    @Override
    public TblItemLocation getDataItemLocation(long id) {
        errMessage = "";
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
                errMessage = e.getMessage();
            }
        }
        return data;
    }

    @Override
    public TblItem getDataItem(long id) {
        errMessage = "";
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
                errMessage = e.getMessage();
            }
        }
        return data;
    }

    @Override
    public List<TblItem> getAllDataItem() {
        errMessage = "";
        List<TblItem> list = new ArrayList();
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
                errMessage = e.getMessage();
            }
        }
        return list;
    }

    @Override
    public List<TblPropertyBarcode> getAllDataPropertyBarcodeByIdItem(long id) {
        errMessage = "";
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
                errMessage = e.getMessage();
            }
        }
        return list;
    }

    @Override
    public List<TblRoom> getAllDataRoom() {
        errMessage = "";
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
                errMessage = e.getMessage();
            }
        }
        return list;
    }

    @Override
    public List<TblLocationOfWarehouse> getAllDataWarehouse() {
        errMessage = "";
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
                errMessage = e.getMessage();
            } finally {
                //session.close();
            }
        }
        return list;
    }

    @Override
    public List<ClassDataTransferItem> getAllDataTransferItem(long id) {
        errMessage = "";
        List<ClassDataTransferItem> list = new ArrayList();
        if (ClassSession.checkUserSession()) {  //check user current session
            try {
                session = HibernateUtil.getSessionFactory().getCurrentSession();
                session.beginTransaction();
                List<TblItemLocation> listItemLocation = session.getNamedQuery("findAllTblItemLocationByIdLocationAndIdType").setParameter("idType", 2).setParameter("idLoc", id).list();
                List<TblItemLocationPropertyBarcode> listItemLocationPropertyBarcode = session.getNamedQuery("findAllTblItemLocationPropertyBarcodeByIdLocationAndIdType").setParameter("idType", 2).setParameter("idLoc", id).list();

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
                errMessage = e.getMessage();
            }
        }
        return list;
    }

    @Override
    public TblItemLocationPropertyBarcode getDataItemLocationPropertyBarcode(long id) {
        errMessage = "";
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
                errMessage = e.getMessage();
            }
        }
        return data;
    }

    @Override
    public List<TblItemLocationPropertyBarcode> getAllDataItemLocationPropertyBarcodeByIDItemLocation(long idItemLocation) {
        errMessage = "";
        List<TblItemLocationPropertyBarcode> list = new ArrayList<>();
        if (ClassSession.checkUserSession()) {  //check user current session
            try {
                session = HibernateUtil.getSessionFactory().getCurrentSession();
                session.beginTransaction();
                list = session.getNamedQuery("findAllTblItemLocationPropertyBarcodeByIDItemLocation")
                        .setParameter("idItemLocation", idItemLocation)
                        .list();
                session.getTransaction().commit();
            } catch (Exception e) {
                if (session.getTransaction().isActive()) {
                    session.getTransaction().rollback();
                }
                errMessage = e.getMessage();
            } finally {
                //session.close();
            }
        }
        return list;
    }

    @Override
    public TblPropertyBarcode getDataPropertyBarcode(long id) {
        errMessage = "";
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
                errMessage = e.getMessage();
            }
        }
        return data;
    }

    @Override
    public List<ClassDataMutation> getAllDataMutationHistory() {
        errMessage = "";
        List<ClassDataMutation> list = new ArrayList();
        if (ClassSession.checkUserSession()) {  //check user current session        
            try {
                session = HibernateUtil.getSessionFactory().getCurrentSession();
                session.beginTransaction();
                List<TblItemMutationHistory> listMutationHistory = session.getNamedQuery("findAllTblItemMutationHistoryByLocationSource").setParameter("idType", 2).list();
                List<TblItemMutationHistoryPropertyBarcode> listMutationHistoryPropertyBarcode = session.getNamedQuery("findAllTblItemMutationHistoryPropertyBarcodeByIdLocation").setParameter("idType", 2).list();

                for (TblItemMutationHistory dataItemMutation : listMutationHistory) {
                    //System.out.println("hsl Mutation>>"+dataItemMutation.getTblItem().getItemName());
                    if (dataItemMutation.getTblItem().getPropertyStatus()) {  //Aset
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
//                //System.out.println("hsl Mutation>>"+dataItemMutation.getTblItem().getItemName());
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
                //errMessage = e.getMessage();
            }
        }
        return list;
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

    @Override
    public List<TblSupplier> getAllDataSupplier() {
        errMessage = "";
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
                errMessage = e.getMessage();
            }
        }
        return list;
    }

    @Override
    public List<TblLocationOfBin> getAllDataBin() {
        errMessage = "";
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
                errMessage = e.getMessage();
            }
        }
        return list;
    }

    @Override
    public RefItemMutationType getMutationType(int id) {
        errMessage = "";
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
                errMessage = e.getMessage();
            } finally {
                //session.close();
            }
        }
        return data;
    }

    //--------------------------------------------------------------------------
    @Override
    public TblItemLocationItemExpiredDate getDataItemLocationItemExpiredDate(long id) {
        errMessage = "";
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
                errMessage = e.getMessage();
            } finally {
                //session.close();
            }
        }
        return data;
    }

    @Override
    public List<TblItemLocationItemExpiredDate> getAllDataItemLocationItemExpiredDateByIDItemLocation(long idItemLocation) {
        errMessage = "";
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
                errMessage = e.getMessage();
            } finally {
                //session.close();
            }
        }
        return list;
    }

    @Override
    public TblItemExpiredDate getDataItemExpiredDate(long id) {
        errMessage = "";
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
                errMessage = e.getMessage();
            } finally {
                //session.close();
            }
        }
        return data;
    }

    @Override
    public List<TblItemExpiredDate> getAllDataItemExpiredDateByIDItem(long idItem) {
        errMessage = "";
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
                errMessage = e.getMessage();
            } finally {
                //session.close();
            }
        }
        return list;
    }

    //--------------------------------------------------------------------------
    @Override
    public List<TblItemMutationHistory> getAllDataItemMutationHistory() {
        errMessage = "";
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
                errMessage = e.getMessage();
            } finally {
                //session.close();
            }
        }
        return list;
    }

    @Override
    public TblItemMutationHistoryPropertyBarcode getDataItemMutationHistoryPropertyBarcodeByIDItemMutationHistory(long idItemMutation) {
        errMessage = "";
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
                errMessage = e.getMessage();
            } finally {
                //session.close();
            }
        }
        return list.isEmpty() ? null : list.get(0);
    }

    @Override
    public List<TblItemMutationHistoryItemExpiredDate> getAllDataItemMutationHistoryItemExpiredDateByIDItemMutationHistory(
            long idItemMutation) {
        errMessage = "";
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
                errMessage = e.getMessage();
            } finally {
                //session.close();
            }
        }
        return list;
    }

    //--------------------------------------------------------------------------
    @Override
    public TblEmployee getDataEmployee(long id) {
        errMessage = "";
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
                errMessage = e.getMessage();
            } finally {
                //session.close();
            }

        }
        return data;
    }

    @Override
    public List<TblEmployee> getAllDataEmployee() {
        errMessage = "";
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
                errMessage = e.getMessage();
            } finally {
                //session.close();
            }
        }
        return list;
    }

    @Override
    public TblPeople getDataPeople(long id) {
        errMessage = "";
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
                errMessage = e.getMessage();
            } finally {
                //session.close();
            }

        }
        return data;
    }

    //--------------------------------------------------------------------------
    @Override
    public TblRoom getDataRoomByIDLocation(long idLocation) {
        errMessage = "";
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
                errMessage = e.getMessage();
            }
        }
        return list.isEmpty() ? null : list.get(0);
    }

    @Override
    public TblLocationOfLaundry getDataLaundryByIDLocation(long idLocation) {
        errMessage = "";
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
                errMessage = e.getMessage();
            }
        }
        return list.isEmpty() ? null : list.get(0);
    }

    //sella
    @Override
    public TblLocationOfWarehouse getDataWarehouseByIdLocation(long id) {
        errMessage = "";
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
                errMessage = e.getMessage();
            }
        }
        return list.isEmpty() ? null : list.get(0);
    }

    //--------------------------------------------------------------------------
    @Override
    public TblInComing insertDataInComing(TblInComing ic,
            List<LaundryInComingController.ICDetailItemMutationHistory> icdimhs) {
        errMessage = "";
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
                for (LaundryInComingController.ICDetailItemMutationHistory icdimh : icdimhs) {
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
                        errMessage = "Barang (" + icdimh.getDataICDIMH().getTblItemMutationHistory().getTblItem().getItemName() + ") tidak ditemukan..!";
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
                        errMessage = "Stok Barang (" + icdimh.getDataICDIMH().getTblItemMutationHistory().getTblItem().getItemName() + ") tidak mencukupi..!";
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
                    for (LaundryInComingController.IMHPropertyBarcodeSelected impbs : icdimh.getDataIMHPBSs()) {
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
                                errMessage = "Barang (" + impbs.getDataItemMutationHistoryPropertyBarcode().getTblPropertyBarcode().getTblItem().getItemName()
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
                            errMessage = "Barang (" + imhied.getTblItemExpiredDate().getTblItem().getItemName()
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
                            errMessage = "Stok Barang (" + imhied.getTblItemExpiredDate().getTblItem().getItemName()
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
                errMessage = e.getMessage();
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
        errMessage = "";
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
                errMessage = e.getMessage();
            } finally {
                //session.close();
            }

        }
        return data;
    }

    @Override
    public List<TblInComing> getAllDataInComing() {
        errMessage = "";
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
                errMessage = e.getMessage();
            }
        }
        return list;
    }

    //--------------------------------------------------------------------------
    @Override
    public List<TblInComingDetail> getAllDataInComingDetailByIDInComing(long idInComing) {
        errMessage = "";
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
                errMessage = e.getMessage();
            } finally {
                //session.close();
            }
        }
        return list;
    }

    @Override
    public TblInComingDetailItemMutationHistory getDataInComingDetailItemMutationHistoryByIDInComingDetail(long idInComingDetail) {
        errMessage = "";
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
                errMessage = e.getMessage();
            } finally {
                //session.close();
            }
        }
        return list.isEmpty() ? null : list.get(0);
    }

    //--------------------------------------------------------------------------
    @Override
    public SysDataHardCode getDataSysDataHardCode(long id) {
        errMessage = "";
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
                errMessage = e.getMessage();
            } finally {
                //session.close();
            }
        }
        return data;
    }

    @Override
    public List<SysDataHardCode> getAllDataSysDataHardCode() {
        errMessage = "";
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
                errMessage = e.getMessage();
            } finally {
                //session.close();
            }
        }
        return list;
    }

    //--------------------------------------------------------------------------
    @Override
    public TblUnit getDataUnit(long id) {
        errMessage = "";
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
                errMessage = e.getMessage();
            } finally {
                //session.close();
            }
        }
        return data;
    }

    @Override
    public RefItemType getDataItemType(int id) {
        errMessage = "";
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
                errMessage = e.getMessage();
            } finally {
                //session.close();
            }
        }
        return data;
    }

    @Override
    public RefItemGuestType getDataItemGuestType(int id) {
        errMessage = "";
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
                errMessage = e.getMessage();
            } finally {
                //session.close();
            }

        }
        return data;
    }

    //--------------------------------------------------------------------------
    @Override
    public TblItemTypeHk getDataItemTypeHK(long id) {
        errMessage = "";
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
                errMessage = e.getMessage();
            } finally {
                //session.close();
            }

        }
        return data;
    }

    @Override
    public List<TblItemTypeHk> getAllDataItemTypeHK() {
        errMessage = "";
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
                errMessage = e.getMessage();
            } finally {
                //session.close();
            }
        }
        return list;
    }

    @Override
    public TblItemTypeWh getDataItemTypeWH(long id) {
        errMessage = "";
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
                errMessage = e.getMessage();
            } finally {
                //session.close();
            }

        }
        return data;
    }

    @Override
    public List<TblItemTypeWh> getAllDataItemTypeWH() {
        errMessage = "";
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
                errMessage = e.getMessage();
            } finally {
                //session.close();
            }
        }
        return list;
    }

    //--------------------------------------------------------------------------
    @Override
    public List<TblLocation> getAllDataLocationByIDLocationType(int idLocationType) {
        errMessage = "";
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
                errMessage = e.getMessage();
            } finally {
                //session.close();
            }
        }
        return list;
    }

    //--------------------------------------------------------------------------
    @Override
    public TblItemLocation getDataItemLocationByIDItemAndIDLocation(long idItem, long idLoc) {
        errMessage = "";
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
                errMessage = e.getMessage();
            }
        }
        return list.isEmpty() ? null : list.get(0);
    }

    //--------------------------------------------------------------------------
    @Override
    public TblItemLocation getDataItemLocationByIDLocationAndIDItem(
            long idLocation,
            long idItem) {
        errMessage = "";
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
                errMessage = e.getMessage();
            } finally {
                //session.close();
            }
        }
        return list.isEmpty() ? null : list.get(0);
    }

    @Override
    public TblItemLocationPropertyBarcode getDataItemLocationPropertyBarcodeByIDItemLocationAndIDPropertyBarcode(
            long idItemLocation,
            long idPropertyBarcode) {
        errMessage = "";
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
                errMessage = e.getMessage();
            } finally {
                //session.close();
            }
        }
        return list.isEmpty() ? null : list.get(0);
    }

    @Override
    public TblItemLocationItemExpiredDate getDataItemLocationItemExpiredDateByIDItemLocationAndIDItemExpiredDate(
            long idItemLocation,
            long idItemExpiredDate) {
        errMessage = "";
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
                errMessage = e.getMessage();
            } finally {
                //session.close();
            }
        }
        return list.isEmpty() ? null : list.get(0);
    }

    //--------------------------------------------------------------------------
    @Override
    public TblItemMutationHistory getDataItemMutationHistory(long id) {
        errMessage = "";
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
                errMessage = e.getMessage();
            } finally {
                //session.close();
            }

        }
        return data;
    }

    @Override
    public List<TblItemMutationHistoryPropertyBarcode> getAllDataItemMutationHistoryPropertyBarcodeByIDItemMutationHistory(long idItemMutation) {
        errMessage = "";
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
                errMessage = e.getMessage();
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
        errMessage = "";
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
                errMessage = e.getMessage();
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
        errMessage = "";
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
                errMessage = (String) rs.uniqueResult();
                if (!errMessage.equals("")) {
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
                errMessage = e.getMessage();
                return false;
            } finally {
                //session.close();
            }
        } else {
            return false;
        }
        return true;
    }

//    private TblItemLocation getItemLocationByIDItemAndLocation(long idItem,
//            long idLocation) {
//        List<TblItemLocation> list = session.getNamedQuery("findAllItemLocationByIdLocationAndIdItem")
//                .setParameter("idItem", idItem)
//                .setParameter("idLocation", idLocation)
//                .list();
//        return list.isEmpty() ? null : list.get(0);
//    }
//
//    private TblItemLocationPropertyBarcode getItemLocationPropertyBarcodeByIDItemLocationAndDIPropertyBarcode(long idItemLocation,
//            long idPropertyBarcode) {
//        List<TblItemLocationPropertyBarcode> list = session.getNamedQuery("findAllTblItemLocationPropertyBarcodeByItemLocationAndPropertyBarcode")
//                .setParameter("idrelation", idItemLocation)
//                .setParameter("idproperty", idPropertyBarcode)
//                .list();
//        return list.isEmpty() ? null : list.get(0);
//    }
    @Override
    public boolean deleteDataStoreRequest(TblStoreRequest sr) {
        errMessage = "";
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
                errMessage = e.getMessage();
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
        errMessage = "";
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
                errMessage = e.getMessage();
            } finally {
                //session.close();
            }
        }
        return data;
    }

    @Override
    public List<TblStoreRequest> getAllDataStoreRequest() {
        errMessage = "";
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
                errMessage = e.getMessage();
            } finally {
                //session.close();
            }
        }
        return list;
    }

    @Override
    public List<TblStoreRequest> getAllDataStoreRequestByIDStoreRequestStatus(int idStoreRequestStatus) {
        errMessage = "";
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
                errMessage = e.getMessage();
            } finally {
                //session.close();
            }
        }
        return list;
    }

    //--------------------------------------------------------------------------
    @Override
    public TblStoreRequestDetail getDataStoreRequestDetail(long id) {
        errMessage = "";
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
                errMessage = e.getMessage();
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
        errMessage = "";
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
                errMessage = e.getMessage();
            } finally {
                //session.close();
            }
        }
        return list.isEmpty() ? null : list.get(0);
    }

    @Override
    public List<TblStoreRequestDetail> getAllDataStoreRequestDetail() {
        errMessage = "";
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
                errMessage = e.getMessage();
            } finally {
                //session.close();
            }
        }
        return list;
    }

    @Override
    public List<TblStoreRequestDetail> getAllDataStoreRequestDetailByIDStoreRequest(long idSR) {
        errMessage = "";
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
                errMessage = e.getMessage();
            } finally {
                //session.close();
            }
        }
        return list;
    }

    //--------------------------------------------------------------------------

    @Override
    public TblOutGoing insertDataOutGoing(TblOutGoing og,
            List<LaundryOutGoingController.OGDetailItemMutationHistory> ogdimhs) {
        errMessage = "";
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
                for (LaundryOutGoingController.OGDetailItemMutationHistory ogdimh : ogdimhs) {
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
                        errMessage = "Barang (" + ogdimh.getDataOGDIMH().getTblItemMutationHistory().getTblItem().getItemName() + ") tidak ditemukan..!";
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
                        errMessage = "Stok Barang (" + ogdimh.getDataOGDIMH().getTblItemMutationHistory().getTblItem().getItemName() + ") tidak mencukupi..!";
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
                    for (LaundryOutGoingController.IMHPropertyBarcodeSelected impbs : ogdimh.getDataIMHPBSs()) {
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
                                errMessage = "Barang (" + impbs.getDataItemMutationHistoryPropertyBarcode().getTblPropertyBarcode().getTblItem().getItemName()
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
                            errMessage = "Barang (" + imhied.getTblItemExpiredDate().getTblItem().getItemName()
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
                            errMessage = "Stok Barang (" + imhied.getTblItemExpiredDate().getTblItem().getItemName()
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
                errMessage = e.getMessage();
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
        errMessage = "";
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
                errMessage = e.getMessage();
            } finally {
                //session.close();
            }

        }
        return data;
    }

    @Override
    public List<TblOutGoing> getAllDataOutGoing() {
        errMessage = "";
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
                errMessage = e.getMessage();
            }
        }
        return list;
    }

    //--------------------------------------------------------------------------
    @Override
    public List<TblOutGoingDetail> getAllDataOutGoingDetailByIDOutGoing(long idOutGoing) {
        errMessage = "";
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
                errMessage = e.getMessage();
            } finally {
                //session.close();
            }
        }
        return list;
    }

    @Override
    public TblOutGoingDetailItemMutationHistory getDataOutGoingDetailItemMutationHistoryByIDOutGoingDetail(long idOutGoingDetail) {
        errMessage = "";
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
                errMessage = e.getMessage();
            } finally {
                //session.close();
            }
        }
        return list.isEmpty() ? null : list.get(0);
    }

    //--------------------------------------------------------------------------
    @Override
    public String getErrorMessage() {
        return errMessage;
    }

}
