/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hotelfx.persistence.service;

import hotelfx.helper.ClassCoder;
import hotelfx.helper.ClassSession;
import hotelfx.persistence.HibernateUtil;
import hotelfx.persistence.model.RefFixedTangibleAssetDepreciationStatus;
import hotelfx.persistence.model.RefItemGuestType;
import hotelfx.persistence.model.RefItemType;
import hotelfx.persistence.model.RefRecordStatus;
import hotelfx.persistence.model.TblEmployee;
import hotelfx.persistence.model.TblFixedTangibleAsset;
import hotelfx.persistence.model.TblItem;
import hotelfx.persistence.model.TblItemLocation;
import hotelfx.persistence.model.TblItemLocationPropertyBarcode;
import hotelfx.persistence.model.TblLocation;
import hotelfx.persistence.model.TblLocationOfBin;
import hotelfx.persistence.model.TblLocationOfLaundry;
import hotelfx.persistence.model.TblLocationOfWarehouse;
import hotelfx.persistence.model.TblPropertyBarcode;
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
 * @author ANDRI
 */
public class FPropertyBarcodeManagerImpl implements FPropertyBarcodeManager {

    private Session session;

    private String errMsg;

    public FPropertyBarcodeManagerImpl() {

    }

    @Override
    public TblPropertyBarcode insertDataPropertyBarcode(TblPropertyBarcode propertyBarcode) {
        errMsg = "";
        TblPropertyBarcode tblPropertyBarcode = propertyBarcode;
        if (ClassSession.checkUserSession()) {  //check user current session
            try {
                session = HibernateUtil.getSessionFactory().getCurrentSession();
                session.beginTransaction();
//            //check code barcode has been used
//            List<TblPropertyBarcode> list = session.getNamedQuery("findAllTblPropertyBarcodeByCodeBarcode")
//                    .setParameter("codeBarcode", tblPropertyBarcode.getCodeBarcode())
//                    .list();
//            if (!list.isEmpty()) {
//                if (session.getTransaction().isActive()) {
//                    session.getTransaction().rollback();
//                }
//                errMsg = "Duplicated Barcode Number!!";
//                return null;
//            }
//            //data item
//            tblPropertyBarcode.getTblItem().setConsumable(false);
//            tblPropertyBarcode.getTblItem().setCreateDate(Timestamp.valueOf(LocalDateTime.now()));
//            tblPropertyBarcode.getTblItem().setTblEmployeeByCreateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
//            tblPropertyBarcode.getTblItem().setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
//            tblPropertyBarcode.getTblItem().setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
//            tblPropertyBarcode.getTblItem().setRefRecordStatus(session.find(RefRecordStatus.class, 1));
//            session.saveOrUpdate(tblPropertyBarcode.getTblItem());
                //data asset
                tblPropertyBarcode.getTblFixedTangibleAsset().setCodeAsset(ClassCoder.getCode("Asset", session));
                tblPropertyBarcode.getTblFixedTangibleAsset().setAssetName(tblPropertyBarcode.getTblItem().getItemName());
                tblPropertyBarcode.getTblFixedTangibleAsset().setCreateDate(Timestamp.valueOf(LocalDateTime.now()));
                tblPropertyBarcode.getTblFixedTangibleAsset().setTblEmployeeByCreateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                tblPropertyBarcode.getTblFixedTangibleAsset().setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                tblPropertyBarcode.getTblFixedTangibleAsset().setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                tblPropertyBarcode.getTblFixedTangibleAsset().setRefRecordStatus(session.find(RefRecordStatus.class, 1));
                session.saveOrUpdate(tblPropertyBarcode.getTblFixedTangibleAsset());
                //data property barcode
                tblPropertyBarcode.setCodeBarcode(ClassCoder.getCode("Property Barcode", session));
                tblPropertyBarcode.setCreateDate(Timestamp.valueOf(LocalDateTime.now()));
                tblPropertyBarcode.setTblEmployeeByCreateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                tblPropertyBarcode.setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                tblPropertyBarcode.setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                tblPropertyBarcode.setRefRecordStatus(session.find(RefRecordStatus.class, 1));
                session.saveOrUpdate(tblPropertyBarcode);
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
        return tblPropertyBarcode;
    }

    @Override
    public boolean updateDataPropertyBarcode(TblPropertyBarcode propertyBarcode) {
        errMsg = "";
        if (ClassSession.checkUserSession()) {  //check user current session
            try {
                session = HibernateUtil.getSessionFactory().getCurrentSession();
                session.beginTransaction();
//            //check code barcode has been used
//            List<TblPropertyBarcode> list = session.getNamedQuery("findAllTblPropertyBarcodeByCodeBarcode")
//                    .setParameter("codeBarcode", propertyBarcode.getCodeBarcode())
//                    .list();
//            if (!list.isEmpty() 
//                    && list.get(0).getIdbarcode()!= propertyBarcode.getIdbarcode()) {
//                if (session.getTransaction().isActive()) {
//                    session.getTransaction().rollback();
//                }
//                errMsg = "Duplicated Barcode Number!!";
//                return false;
//            }else{
//                session.evict(list.get(0));
//            }
                //property barcode
                propertyBarcode.setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                propertyBarcode.setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                session.update(propertyBarcode);
//            //item
//            propertyBarcode.getTblItem().setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
//            propertyBarcode.getTblItem().setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
//            session.update(propertyBarcode.getTblItem());
                //asset
                propertyBarcode.getTblFixedTangibleAsset().setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                propertyBarcode.getTblFixedTangibleAsset().setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                session.update(propertyBarcode.getTblFixedTangibleAsset());
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
    public boolean deleteDataPropertyBarcode(TblPropertyBarcode propertyBarcode) {
        errMsg = "";
        if (ClassSession.checkUserSession()) {  //check user current session
            try {
                session = HibernateUtil.getSessionFactory().getCurrentSession();
                session.beginTransaction();
                if (propertyBarcode.getRefRecordStatus().getIdstatus() == 1) {
                    //poperty barcode
                    propertyBarcode.setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                    propertyBarcode.setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                    propertyBarcode.setRefRecordStatus(session.find(RefRecordStatus.class, 2));
                    session.update(propertyBarcode);
//                //item
//                propertyBarcode.getTblItem().setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
//                propertyBarcode.getTblItem().setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
//                propertyBarcode.getTblItem().setRefRecordStatus(session.find(RefRecordStatus.class, 2));
//                session.update(propertyBarcode.getTblItem());
                    //asset
                    propertyBarcode.getTblFixedTangibleAsset().setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                    propertyBarcode.getTblFixedTangibleAsset().setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                    propertyBarcode.getTblFixedTangibleAsset().setRefRecordStatus(session.find(RefRecordStatus.class, 2));
                    session.update(propertyBarcode.getTblFixedTangibleAsset());
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
                list = session.getNamedQuery("findAllTblPropertyBarcode")
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
    public List<TblItemLocationPropertyBarcode> getAllDataPropertyBarcodeStock() {
        errMsg = "";
        List<TblItemLocationPropertyBarcode> list = new ArrayList();
        if (ClassSession.checkUserSession()) {  //check user current session
            try {
                session = HibernateUtil.getSessionFactory().getCurrentSession();
                session.beginTransaction();
                list = session.getNamedQuery("findAllTblItemLocationPropertyBarcode").list();
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
    public TblItem insertDataItem(TblItem property) {
        errMsg = "";
        TblItem tblItem = property;
        if (ClassSession.checkUserSession()) {  //check user current session
            try {
                session = HibernateUtil.getSessionFactory().getCurrentSession();
                session.beginTransaction();
                //data property
                tblItem.setCodeItem(ClassCoder.getCode("Property", session));
                tblItem.setPropertyStatus(true);
                tblItem.setConsumable(false);
                tblItem.setHotelDiscountable(false);
                tblItem.setCardDiscountable(false);
                tblItem.setTaxable(true);
                tblItem.setCreateDate(Timestamp.valueOf(LocalDateTime.now()));
                tblItem.setTblEmployeeByCreateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                tblItem.setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                tblItem.setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                tblItem.setRefRecordStatus(session.find(RefRecordStatus.class, 1));
                session.saveOrUpdate(tblItem);
                //data item - location (property - all location(warehouse, laundry, room))
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
    public boolean updateDataItem(TblItem property) {
        errMsg = "";
        if (ClassSession.checkUserSession()) {  //check user current session
            try {
                session = HibernateUtil.getSessionFactory().getCurrentSession();
                session.beginTransaction();
                property.setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                property.setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                session.update(property);
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
    public boolean deleteDataItem(TblItem property) {
        errMsg = "";
        if (ClassSession.checkUserSession()) {  //check user current session
            try {
                session = HibernateUtil.getSessionFactory().getCurrentSession();
                session.beginTransaction();
                if (property.getRefRecordStatus().getIdstatus() == 1) {
                    property.setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                    property.setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                    property.setRefRecordStatus(session.find(RefRecordStatus.class, 2));
                    session.update(property);
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
    public TblItem getItem(long id) {
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
                List<TblItem> asset = session.getNamedQuery("findAllTblItem")
                        .list();
                list.addAll(asset);
                session.getTransaction().commit();
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
    public List<TblItemLocation> getAllDataItemStock() {
        errMsg = "";
        List<TblItemLocation> list = new ArrayList();
        if (ClassSession.checkUserSession()) {  //check user current session
            try {
                session = HibernateUtil.getSessionFactory().getCurrentSession();
                session.beginTransaction();
                List<TblItemLocation> asset = session.getNamedQuery("findAllTblItemLocation").list();
                list.addAll(asset);
                session.getTransaction().commit();
            } catch (Exception e) {
                if (session.getTransaction().isActive()) {
                    session.getTransaction().rollback();
                }
            }
        }
        return list;
    }

    //--------------------------------------------------------------------------
    @Override
    public TblFixedTangibleAsset getFixedTangibleAsset(long id) {
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
                list = session.getNamedQuery("findAllTblFixedTangibleAsset").list();
                session.getTransaction().commit();
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
    public RefItemType getItemType(int id) {
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
                for (int i = list.size() - 1; i > -1; i--) {
                    if (!(list.get(i).getIdtype() == 2)) {   //Aset = '2'
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
    public RefFixedTangibleAssetDepreciationStatus getFixedTangibleAssetDepreciationStatus(int id) {
        errMsg = "";
        RefFixedTangibleAssetDepreciationStatus data = null;
        if (ClassSession.checkUserSession()) {  //check user current session
            try {
                session = HibernateUtil.getSessionFactory().getCurrentSession();
                session.beginTransaction();
                data = (RefFixedTangibleAssetDepreciationStatus) session.find(RefFixedTangibleAssetDepreciationStatus.class, id);
                session.getTransaction().commit();
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
    public List<RefFixedTangibleAssetDepreciationStatus> getAllDataFixedTangibleAssetDepreciationStatus() {
        errMsg = "";
        List<RefFixedTangibleAssetDepreciationStatus> list = new ArrayList<>();
        if (ClassSession.checkUserSession()) {  //check user current session
            try {
                session = HibernateUtil.getSessionFactory().getCurrentSession();
                session.beginTransaction();
                list = session.getNamedQuery("findAllRefFixedTangibleAssetDepreciationStatus").list();
                session.getTransaction().commit();
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
    public List<RefItemGuestType> getAllGuestType() {
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
    public TblLocation getDataLocation(long id){
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
    public TblItemLocation getDataItemLocation(long id){
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
            } finally {
                //session.close();
            }
        }
        return data;
    }
    
    @Override
    public TblItemLocationPropertyBarcode getDataItemLocationPropertyBarcodeByIDPropertyBarcode(long idPropertyBarcode){
        errMsg = "";
        List<TblItemLocationPropertyBarcode> list = new ArrayList();
        if (ClassSession.checkUserSession()) {  //check user current session
            try {
                session = HibernateUtil.getSessionFactory().getCurrentSession();
                session.beginTransaction();
                list = session.getNamedQuery("findAllTblItemLocationPropertyBarcodeByIDPropertyBarcode")
                        .setParameter("idPropertyBarcode", idPropertyBarcode)
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
    public String getErrorMessage() {
        return errMsg;
    }

}
