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
import hotelfx.persistence.model.RefItemMutationType;
import hotelfx.persistence.model.RefLocationType;
import hotelfx.persistence.model.RefRecordStatus;
import hotelfx.persistence.model.SysDataHardCode;
import hotelfx.persistence.model.TblBrokenItem;
import hotelfx.persistence.model.TblBrokenItemDetail;
import hotelfx.persistence.model.TblBrokenItemDetailItemMutationHistory;
import hotelfx.persistence.model.TblEmployee;
import hotelfx.persistence.model.TblGroup;
import hotelfx.persistence.model.TblItem;
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
import hotelfx.persistence.model.TblRoom;
import hotelfx.persistence.model.TblUnit;
import hotelfx.view.feature_broken_item.BrokenItemController;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import org.hibernate.Session;

/**
 *
 * @author ABC-Programmer
 */
public class FBrokenItemManagerImpl implements FBrokenItemManager {

    private Session session;

    private String errMsg;

    public FBrokenItemManagerImpl() {

    }

    @Override
    public TblBrokenItem insertDataBrokenItem(TblBrokenItem bi,
            List<BrokenItemController.BIDetailItemMutationHistory> bidimhs) {
        errMsg = "";
        TblBrokenItem tblBrokenItem = bi;
        if (ClassSession.checkUserSession()) {  //check user current session
            try {
                session = HibernateUtil.getSessionFactory().getCurrentSession();
                session.beginTransaction();
                //data broken item
                tblBrokenItem.setCodeBi(ClassCoder.getCode("Barang Rusak", session));
                tblBrokenItem.setBidate(Timestamp.valueOf(LocalDateTime.now()));
                tblBrokenItem.setCreateDate(Timestamp.valueOf(LocalDateTime.now()));
                tblBrokenItem.setTblEmployeeByCreateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                tblBrokenItem.setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                tblBrokenItem.setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                tblBrokenItem.setRefRecordStatus(session.find(RefRecordStatus.class, 1));
                session.saveOrUpdate(tblBrokenItem);
                //data details
                for (BrokenItemController.BIDetailItemMutationHistory bidimh : bidimhs) {
                    //data broken item detail
                    bidimh.getDataBIDIMH().getTblBrokenItemDetail().setCreateDate(Timestamp.valueOf(LocalDateTime.now()));
                    bidimh.getDataBIDIMH().getTblBrokenItemDetail().setTblEmployeeByCreateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                    bidimh.getDataBIDIMH().getTblBrokenItemDetail().setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                    bidimh.getDataBIDIMH().getTblBrokenItemDetail().setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                    bidimh.getDataBIDIMH().getTblBrokenItemDetail().setRefRecordStatus(session.find(RefRecordStatus.class, 1));
                    session.saveOrUpdate(bidimh.getDataBIDIMH().getTblBrokenItemDetail());
                    //data item mutation history
//                    bidimh.getDataBIDIMH().getTblItemMutationHistory().setTblEmployeeByIdemployee(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                    bidimh.getDataBIDIMH().getTblItemMutationHistory().setMutationDate(Timestamp.valueOf(LocalDateTime.now()));
                    bidimh.getDataBIDIMH().getTblItemMutationHistory().setRefItemMutationType(session.find(RefItemMutationType.class, (int) 0)); //Dipindahkan = '0'
                    bidimh.getDataBIDIMH().getTblItemMutationHistory().setRefRecordStatus(session.find(RefRecordStatus.class, (int) 1));
                    bidimh.getDataBIDIMH().getTblItemMutationHistory().setTblEmployeeByCreateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                    bidimh.getDataBIDIMH().getTblItemMutationHistory().setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                    bidimh.getDataBIDIMH().getTblItemMutationHistory().setCreateDate(Timestamp.valueOf(LocalDateTime.now()));
                    bidimh.getDataBIDIMH().getTblItemMutationHistory().setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                    session.saveOrUpdate(bidimh.getDataBIDIMH().getTblItemMutationHistory());
                    //data item location (source, -)
                    TblItemLocation sourceItemLocation = getItemLocationByIDItemAndLocation(
                            bidimh.getDataBIDIMH().getTblItemMutationHistory().getTblItem().getIditem(),
                            bidimh.getDataBIDIMH().getTblItemMutationHistory().getTblLocationByIdlocationOfSource().getIdlocation());
                    if (sourceItemLocation == null) {
                        errMsg = "Barang (" + bidimh.getDataBIDIMH().getTblItemMutationHistory().getTblItem().getItemName() + ") tidak ditemukan..!";
                        if (session.getTransaction().isActive()) {
                            session.getTransaction().rollback();
                        }
                        return null;
                    }
                    sourceItemLocation.setItemQuantity(sourceItemLocation.getItemQuantity().subtract(bidimh.getDataBIDIMH().getTblItemMutationHistory().getItemQuantity()));
                    sourceItemLocation.setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                    sourceItemLocation.setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                    if (sourceItemLocation.getItemQuantity()
                            .compareTo(new BigDecimal("0")) == -1) {   //data item quantity < 0
                        errMsg = "Stok Barang (" + bidimh.getDataBIDIMH().getTblItemMutationHistory().getTblItem().getItemName() + ") tidak mencukupi..!";
                        if (session.getTransaction().isActive()) {
                            session.getTransaction().rollback();
                        }
                        return null;
                    }
                    session.update(sourceItemLocation);
                    //data item location (destination, +)
                    TblItemLocation destinationItemLocation = getItemLocationByIDItemAndLocation(
                            bidimh.getDataBIDIMH().getTblItemMutationHistory().getTblItem().getIditem(),
                            bidimh.getDataBIDIMH().getTblItemMutationHistory().getTblLocationByIdlocationOfDestination().getIdlocation());
                    if (destinationItemLocation != null) {
                        destinationItemLocation.setItemQuantity(destinationItemLocation.getItemQuantity().add(bidimh.getDataBIDIMH().getTblItemMutationHistory().getItemQuantity()));
                        destinationItemLocation.setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                        destinationItemLocation.setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                        session.update(destinationItemLocation);
                    } else {
                        destinationItemLocation = new TblItemLocation();
                        destinationItemLocation.setTblItem(bidimh.getDataBIDIMH().getTblItemMutationHistory().getTblItem());
                        destinationItemLocation.setTblLocation(bidimh.getDataBIDIMH().getTblItemMutationHistory().getTblLocationByIdlocationOfDestination());
                        destinationItemLocation.setItemQuantity(bidimh.getDataBIDIMH().getTblItemMutationHistory().getItemQuantity());
                        destinationItemLocation.setRefRecordStatus(session.find(RefRecordStatus.class, (int) 1));
                        destinationItemLocation.setTblEmployeeByCreateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                        destinationItemLocation.setCreateDate(Timestamp.valueOf(LocalDateTime.now()));
                        destinationItemLocation.setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                        destinationItemLocation.setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                        session.saveOrUpdate(destinationItemLocation);
                    }
                    //data broken item detail - item mutation history
                    bidimh.getDataBIDIMH().setRefRecordStatus(session.find(RefRecordStatus.class, (int) 1));
                    bidimh.getDataBIDIMH().setTblEmployeeByCreateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                    bidimh.getDataBIDIMH().setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                    bidimh.getDataBIDIMH().setCreateDate(Timestamp.valueOf(LocalDateTime.now()));
                    bidimh.getDataBIDIMH().setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                    session.saveOrUpdate(bidimh.getDataBIDIMH());
                    //data item mutation history - property
                    for (BrokenItemController.IMHPropertyBarcodeSelected impbs : bidimh.getDataIMHPBSs()) {
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
                    for (TblItemMutationHistoryItemExpiredDate imhied : bidimh.getDataIMHIEDs()) {
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
        return tblBrokenItem;
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

    @Override
    public TblBrokenItem getDataBrokenItem(long id) {
        errMsg = "";
        TblBrokenItem data = null;
        if (ClassSession.checkUserSession()) {  //check user current session
            try {
                session = HibernateUtil.getSessionFactory().getCurrentSession();
                session.beginTransaction();
                data = (TblBrokenItem) session.find(TblBrokenItem.class, id);
                session.getTransaction().commit();
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
    public List<TblBrokenItem> getAllDataBrokenItem() {
        errMsg = "";
        List<TblBrokenItem> list = new ArrayList();
        if (ClassSession.checkUserSession()) {  //check user current session
            try {
                session = HibernateUtil.getSessionFactory().getCurrentSession();
                session.beginTransaction();
                list = session.getNamedQuery("findAllTblBrokenItem").list();
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
    public List<TblBrokenItemDetail> getAllDataBrokenItemDetailByIDBrokenItem(long idBrokenItem) {
        errMsg = "";
        List<TblBrokenItemDetail> list = new ArrayList<>();
        if (ClassSession.checkUserSession()) {  //check user current session
            try {
                session = HibernateUtil.getSessionFactory().getCurrentSession();
                session.beginTransaction();
                list = session.getNamedQuery("findAllTblBrokenItemDetailByIDBrokenItem")
                        .setParameter("idBrokenItem", idBrokenItem)
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
    public TblBrokenItemDetailItemMutationHistory getDataBrokenItemDetailItemMutationHistoryByIDBrokenItemDetail(long idBrokenItemDetail) {
        errMsg = "";
        List<TblBrokenItemDetailItemMutationHistory> list = new ArrayList<>();
        if (ClassSession.checkUserSession()) {  //check user current session
            try {
                session = HibernateUtil.getSessionFactory().getCurrentSession();
                session.beginTransaction();
                list = session.getNamedQuery("findAllTblBrokenItemDetailItemMutationHistoryByIDBrokenItemDetail")
                        .setParameter("idBrokenItemDetail", idBrokenItemDetail)
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
    public TblLocationOfBin getDataBinByIdLocation(long idLocation) {
        errMsg = "";
        List<TblLocationOfBin> list = new ArrayList();
        if (ClassSession.checkUserSession()) {  //check user current session
            try {
                session = HibernateUtil.getSessionFactory().getCurrentSession();
                session.beginTransaction();
                list = session.getNamedQuery("findAllTblLocationOfBinByIdLocation")
                        .setParameter("id", idLocation).list();
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
    public TblLocation getDataLocationByIDLocationType(int idLocationType) {
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
                list = session.getNamedQuery("findAllTblLocation")
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
