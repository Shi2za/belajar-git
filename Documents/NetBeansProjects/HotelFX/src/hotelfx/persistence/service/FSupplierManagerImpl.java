/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hotelfx.persistence.service;

import hotelfx.helper.ClassCoder;
import hotelfx.helper.ClassDataMutation;
import hotelfx.helper.ClassSession;
import hotelfx.persistence.HibernateUtil;
import hotelfx.persistence.model.RefBankAccountHolderStatus;
import hotelfx.persistence.model.RefItemType;
import hotelfx.persistence.model.RefLocationType;
import hotelfx.persistence.model.RefRecordStatus;
import hotelfx.persistence.model.TblBank;
import hotelfx.persistence.model.TblBankAccount;
import hotelfx.persistence.model.TblEmployee;
import hotelfx.persistence.model.TblItem;
import hotelfx.persistence.model.TblItemExpiredDate;
import hotelfx.persistence.model.TblItemMutationHistory;
import hotelfx.persistence.model.TblItemMutationHistoryItemExpiredDate;
import hotelfx.persistence.model.TblItemMutationHistoryPropertyBarcode;
import hotelfx.persistence.model.TblItemTypeHk;
import hotelfx.persistence.model.TblItemTypeWh;
import hotelfx.persistence.model.TblLocation;
import hotelfx.persistence.model.TblLocationOfBin;
import hotelfx.persistence.model.TblLocationOfLaundry;
import hotelfx.persistence.model.TblLocationOfWarehouse;
import hotelfx.persistence.model.TblPropertyBarcode;
import hotelfx.persistence.model.TblRoom;
import hotelfx.persistence.model.TblSupplier;
import hotelfx.persistence.model.TblSupplierBankAccount;
import hotelfx.persistence.model.TblSupplierItem;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import org.hibernate.Session;
import org.hibernate.query.Query;

/**
 *
 * @author ANDRI
 */
public class FSupplierManagerImpl implements FSupplierManager {

    private Session session;

    private String errMsg;

    public FSupplierManagerImpl() {

    }

    @Override
    public TblSupplier insertDataSupplier(TblSupplier supplier,
            List<TblSupplierBankAccount> supplierBankAccount,
            List<TblSupplierItem> supplierItem) {
        errMsg = "";
        TblSupplier tblSupplier = supplier;
        if (ClassSession.checkUserSession()) {  //check user current session
            try {
                session = HibernateUtil.getSessionFactory().getCurrentSession();
                session.beginTransaction();
                //data location
                tblSupplier.getTblLocation().setRefLocationType(session.find(RefLocationType.class, 3));
                tblSupplier.getTblLocation().setCreateDate(Timestamp.valueOf(LocalDateTime.now()));
                tblSupplier.getTblLocation().setTblEmployeeByCreateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                tblSupplier.getTblLocation().setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                tblSupplier.getTblLocation().setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                tblSupplier.getTblLocation().setRefRecordStatus(session.find(RefRecordStatus.class, 1));
                session.saveOrUpdate(tblSupplier.getTblLocation());
                //data supplier
                tblSupplier.setCodeSupplier(ClassCoder.getCode("Supplier", session));
                tblSupplier.setCreateDate(Timestamp.valueOf(LocalDateTime.now()));
                tblSupplier.setTblEmployeeByCreateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                tblSupplier.setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                tblSupplier.setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                tblSupplier.setRefRecordStatus(session.find(RefRecordStatus.class, 1));
                session.saveOrUpdate(tblSupplier);
            //data supplier bank account
                //save or update data bank account n supplier bank acccount
                for (TblSupplierBankAccount sba : supplierBankAccount) {
                    //data bank account
                    sba.getTblBankAccount().setCreateDate(Timestamp.valueOf(LocalDateTime.now()));
                    sba.getTblBankAccount().setTblEmployeeByCreateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                    sba.getTblBankAccount().setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                    sba.getTblBankAccount().setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                    sba.getTblBankAccount().setRefRecordStatus(session.find(RefRecordStatus.class, 1));
                    session.saveOrUpdate(sba.getTblBankAccount());
                    //data supplier bank account
                    sba.setRefBankAccountHolderStatus(session.find(RefBankAccountHolderStatus.class, 0));
                    sba.setCreateDate(Timestamp.valueOf(LocalDateTime.now()));
                    sba.setTblEmployeeByCreateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                    sba.setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                    sba.setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                    sba.setRefRecordStatus(session.find(RefRecordStatus.class, 1));
                    session.saveOrUpdate(sba);
                }
                //data supplier - item (insert)
                for (TblSupplierItem data : supplierItem) {
                    data.setCreateDate(Timestamp.valueOf(LocalDateTime.now()));
                    data.setTblEmployeeByCreateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                    data.setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                    data.setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                    data.setRefRecordStatus(session.find(RefRecordStatus.class, 1));
                    session.saveOrUpdate(data);
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
        return tblSupplier;
    }

    @Override
    public boolean updateDataSupplier(TblSupplier supplier,
            List<TblSupplierBankAccount> supplierBankAccount,
            List<TblSupplierItem> supplierItem) {
        errMsg = "";
        if (ClassSession.checkUserSession()) {  //check user current session
            try {
                session = HibernateUtil.getSessionFactory().getCurrentSession();
                session.beginTransaction();
                //data location
                supplier.getTblLocation().setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                supplier.getTblLocation().setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                session.update(supplier.getTblLocation());
                //data supplier
                supplier.setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                supplier.setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                session.update(supplier);
            //data supplier bank account
                //delete all
                Query rs = session.getNamedQuery("deleteAllTblSupplierBankAccount")
                        .setParameter("idSupplier", supplier.getIdsupplier())
                        .setParameter("idEmployee", ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee());
                errMsg = (String) rs.uniqueResult();
                if (!errMsg.equals("")) {
                    if (session.getTransaction().isActive()) {
                        session.getTransaction().rollback();
                    }
                    return false;
                }
                //save or update data bank account n supplier bank acccount
                for (TblSupplierBankAccount sba : supplierBankAccount) {
                    //data bank account
                    sba.getTblBankAccount().setCreateDate(Timestamp.valueOf(LocalDateTime.now()));
                    sba.getTblBankAccount().setTblEmployeeByCreateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                    sba.getTblBankAccount().setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                    sba.getTblBankAccount().setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                    sba.getTblBankAccount().setRefRecordStatus(session.find(RefRecordStatus.class, 1));
                    session.saveOrUpdate(sba.getTblBankAccount());
                    //data supplier bank account
                    sba.setRefBankAccountHolderStatus(session.find(RefBankAccountHolderStatus.class, 0));
                    sba.setCreateDate(Timestamp.valueOf(LocalDateTime.now()));
                    sba.setTblEmployeeByCreateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                    sba.setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                    sba.setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                    sba.setRefRecordStatus(session.find(RefRecordStatus.class, 1));
                    session.saveOrUpdate(sba);
                }
            //data supplier - item
                //delete all
                rs = session.getNamedQuery("deleteAllTblSupplierItem")
                        .setParameter("idSupplier", supplier.getIdsupplier())
                        .setParameter("idEmployee", ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee());
                errMsg = (String) rs.uniqueResult();
                if (!errMsg.equals("")) {
                    if (session.getTransaction().isActive()) {
                        session.getTransaction().rollback();
                    }
                    return false;
                }
                //data supplier - item (insert)
                for (TblSupplierItem data : supplierItem) {
                    data.setCreateDate(Timestamp.valueOf(LocalDateTime.now()));
                    data.setTblEmployeeByCreateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                    data.setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                    data.setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                    data.setRefRecordStatus(session.find(RefRecordStatus.class, 1));
                    session.saveOrUpdate(data);
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
    public boolean deleteDataSupplier(TblSupplier supplier) {
        errMsg = "";
        if (ClassSession.checkUserSession()) {  //check user current session
            try {
                session = HibernateUtil.getSessionFactory().getCurrentSession();
                session.beginTransaction();
                if (supplier.getRefRecordStatus().getIdstatus() == 1) {
                    //data location
                    supplier.getTblLocation().setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                    supplier.getTblLocation().setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                    supplier.getTblLocation().setRefRecordStatus(session.find(RefRecordStatus.class, 2));
                    session.update(supplier.getTblLocation());
                    //data supplier
                    supplier.setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                    supplier.setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                    supplier.setRefRecordStatus(session.find(RefRecordStatus.class, 2));
                    session.update(supplier);
                    //data supplier - bank account
                    Query rs = session.getNamedQuery("deleteAllTblSupplierBankAccount")
                            .setParameter("idSupplier", supplier.getIdsupplier())
                            .setParameter("idEmployee", ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee());
                    errMsg = (String) rs.uniqueResult();
                    if (!errMsg.equals("")) {
                        if (session.getTransaction().isActive()) {
                            session.getTransaction().rollback();
                        }
                        return false;
                    }
                    //data supplier - item
                    rs = session.getNamedQuery("deleteAllTblSupplierItem")
                            .setParameter("idSupplier", supplier.getIdsupplier())
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
    public TblSupplier getSupplier(long id) {
        errMsg = "";
        TblSupplier data = null;
        if (ClassSession.checkUserSession()) {  //check user current session
            try {
                session = HibernateUtil.getSessionFactory().getCurrentSession();
                session.beginTransaction();
                data = (TblSupplier) session.find(TblSupplier.class, id);
                session.getTransaction().commit();
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
    public List<TblSupplier> getAllDataSupplier() {
        errMsg = "";
        List<TblSupplier> list = new ArrayList<>();
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
            } finally {
                //session.close();
            }
        }
        return list;
    }

    //--------------------------------------------------------------------------
    @Override
    public TblSupplierBankAccount getDataSupplierBankAccount(long id) {
        errMsg = "";
        TblSupplierBankAccount data = null;
        if (ClassSession.checkUserSession()) {  //check user current session
            try {
                session = HibernateUtil.getSessionFactory().getCurrentSession();
                session.beginTransaction();
                data = (TblSupplierBankAccount) session.find(TblSupplierBankAccount.class, id);
                session.getTransaction().commit();
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
    public List<TblSupplierBankAccount> getAllDataSupplierBankAccountByIDSupplier(long idSupplier) {
        errMsg = "";
        List<TblSupplierBankAccount> list = new ArrayList<>();
        if (ClassSession.checkUserSession()) {  //check user current session
            try {
                session = HibernateUtil.getSessionFactory().getCurrentSession();
                session.beginTransaction();
                list = session.getNamedQuery("findAllTblSupplierBankAccountByIDSupplier")
                        .setParameter("idSupplier", idSupplier)
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
    public TblBank getDataBank(long id) {
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
    @Override
    public TblBankAccount getDataBankAccount(long id) {
        errMsg = "";
        TblBankAccount data = null;
        if (ClassSession.checkUserSession()) {  //check user current session
            try {
                session = HibernateUtil.getSessionFactory().getCurrentSession();
                session.beginTransaction();
                data = (TblBankAccount) session.find(TblBankAccount.class, id);
                session.getTransaction().commit();
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
    public List<TblBankAccount> getAllDataBankAccount() {
        errMsg = "";
        List<TblBankAccount> list = new ArrayList<>();
        if (ClassSession.checkUserSession()) {  //check user current session
            try {
                session = HibernateUtil.getSessionFactory().getCurrentSession();
                session.beginTransaction();
                list = session.getNamedQuery("findAllTblBankAccount").list();
                session.getTransaction().commit();
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
    public TblSupplierItem getDataSupplierItem(long id) {
        errMsg = "";
        TblSupplierItem data = null;
        if (ClassSession.checkUserSession()) {  //check user current session
            try {
                session = HibernateUtil.getSessionFactory().getCurrentSession();
                session.beginTransaction();
                data = (TblSupplierItem) session.find(TblSupplierItem.class, id);
                session.getTransaction().commit();
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
    public List<TblSupplierItem> getAllDataSupplierItemByIDSupplier(long idSupplier) {
        errMsg = "";
        List<TblSupplierItem> list = new ArrayList<>();
        if (ClassSession.checkUserSession()) {  //check user current session
            try {
                session = HibernateUtil.getSessionFactory().getCurrentSession();
                session.beginTransaction();
                list = session.getNamedQuery("findAllTblSupplierItemByIDSupplier")
                        .setParameter("idSupplier", idSupplier)
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
    public List<TblLocation> getAllDataLocation() {
        errMsg = "";
        List<TblLocation> list = new ArrayList<>();
        if (ClassSession.checkUserSession()) {  //check user current session
            try {
                session = HibernateUtil.getSessionFactory().getCurrentSession();
                session.beginTransaction();
                list = session.getNamedQuery("findAllTblLocationByLocationTypeName")
                        .setParameter("typeName", "Supplier")
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
    public List<ClassDataMutation> getAllDataMutationHistory() {
        errMsg = "";
        List<ClassDataMutation> list = new ArrayList();
        if (ClassSession.checkUserSession()) {  //check user current session
            try {
                session = HibernateUtil.getSessionFactory().getCurrentSession();
                session.beginTransaction();
                List<TblItemMutationHistory> listMutationHistory = session.getNamedQuery("findAllTblItemMutationHistoryByLocationDestination").setParameter("idType", 3).list();
                List<TblItemMutationHistoryPropertyBarcode> listMutationHistoryPropertyBarcode = session.getNamedQuery("findAllTblItemMutationHistoryPropertyBarcodeByIdLocationDestination").setParameter("idType", 3).list();

                for (TblItemMutationHistory dataItemMutation : listMutationHistory) {
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
//                if (!dataItemMutation.getTblItem().getPropertyStatus()) {  //Property
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
    
    //--------------------------------------------------------------------------
    @Override
    public TblItemTypeHk getDataItemTypeHK(long id){
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
    public List<TblItemTypeHk> getAllDataItemTypeHK(){
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
    public TblItemTypeWh getDataItemTypeWH(long id){
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
    public List<TblItemTypeWh> getAllDataItemTypeWH(){
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
    public TblPropertyBarcode getDataPropertyBarcode(long id){
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
    public TblItemExpiredDate getDataItemExpiredDate(long id){
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
    
    //--------------------------------------------------------------------------
    @Override
    public List<TblItemMutationHistory> getAllDataItemMutationHistory(){
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
    public TblItemMutationHistoryPropertyBarcode getDataItemMutationHistoryPropertyBarcodeByIDItemMutationHistory(long idItemMutation){
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
            long idItemMutation){
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
    public String getErrorMessage() {
        return errMsg;
    }

//    private String getCode(String dataName) {
//        String result = "";
//        List<SysCode> list = session.getNamedQuery("findAllSysCodeByDataName")
//                .setParameter("dataName", dataName)
//                .list();
//        if (list.size() == 1) {
//            //set code
//            for (int i = 0; i < ClassDataHardcode.defaultCodeDigitNumber; i++) {
//                result += "0";
//            }
//            result += String.valueOf(list.get(0).getCodeLastNumber());
//            result = result.substring(result.length() - ClassDataHardcode.defaultCodeDigitNumber);
//            result = list.get(0).getCodePrefix() + result;
//            //update last number (data code)
//            list.get(0).setCodeLastNumber(list.get(0).getCodeLastNumber() + 1);
//            session.update(list.get(0));
//        }
//        return result;
//    }
}
