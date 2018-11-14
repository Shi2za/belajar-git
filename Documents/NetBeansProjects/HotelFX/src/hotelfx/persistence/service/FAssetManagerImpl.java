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
import hotelfx.persistence.model.RefFixedTangibleAssetType;
import hotelfx.persistence.model.RefRecordStatus;
import hotelfx.persistence.model.TblEmployee;
import hotelfx.persistence.model.TblFixedTangibleAsset;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import org.hibernate.Session;

/**
 *
 * @author Andreas
 */
public class FAssetManagerImpl implements FAssetManager {

    private Session session;

    private String errMessage;

    @Override
    public TblFixedTangibleAsset insertDataAsset(TblFixedTangibleAsset asset) {
        errMessage = "";
        TblFixedTangibleAsset tblasset = asset;
        if (ClassSession.checkUserSession()) {  //check user current session
        try {
            session = HibernateUtil.getSessionFactory().getCurrentSession();
            session.beginTransaction();
            tblasset.setCodeAsset(ClassCoder.getCode("Asset", session));
            tblasset.setCreateDate(Timestamp.valueOf(LocalDateTime.now()));
            tblasset.setTblEmployeeByCreateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
            tblasset.setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
            tblasset.setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
            tblasset.setRefRecordStatus(session.find(RefRecordStatus.class, 1));
            session.saveOrUpdate(tblasset);
            session.getTransaction().commit();
        } catch (Exception e) {
            if (session.getTransaction().isActive()) {
                session.getTransaction().rollback();
            }
            errMessage = e.getMessage();
            return null;
        }
        }else{
            return null;
        }
        return tblasset;
        /*Session session = HibernateUtil.getSessionFactory().getCurrentSession();
         session.beginTransaction();
         session.saveOrUpdate(asset);
         session.getTransaction().commit();*/
    }

    @Override
    public boolean updateDataAsset(TblFixedTangibleAsset asset) {
        errMessage = "";
        if (ClassSession.checkUserSession()) {  //check user current session
        try {
            session = HibernateUtil.getSessionFactory().getCurrentSession();
            session.beginTransaction();
            asset.setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
            asset.setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
            session.update(asset);
            session.getTransaction().commit();
        } catch (Exception e) {
            if (session.getTransaction().isActive()) {
                session.getTransaction().rollback();
            }
            errMessage = e.getMessage();
            return false;
        }
        }else{
            return false;
        }
        return true;
    }

    @Override
    public boolean deleteDataAsset(TblFixedTangibleAsset asset) {
        errMessage = "";
        if (ClassSession.checkUserSession()) {  //check user current session
        try {
            session = HibernateUtil.getSessionFactory().getCurrentSession();
            session.beginTransaction();
            if (asset.getRefRecordStatus().getIdstatus() == 1) {
                asset.setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                asset.setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                asset.setRefRecordStatus(session.find(RefRecordStatus.class, 2));
                session.update(asset);
            } else {
                if (session.getTransaction().isActive()) {
                    session.getTransaction().rollback();
                }
                errMessage = "Data tidak dapat dihapus!!";
                return false;
            }
            session.getTransaction().commit();
        } catch (Exception e) {
            if (session.getTransaction().isActive()) {
                session.getTransaction().rollback();
            }
            errMessage = e.getMessage();
            return false;
        }
        }else{
            return false;
        }
        return true;
    }

    @Override
    public List<TblFixedTangibleAsset> getAllDataAsset() {
        errMessage = "";
        List<TblFixedTangibleAsset> list = new ArrayList();
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
            errMessage = e.getMessage();

        }
        }
        return list;
    }

    @Override
    public TblFixedTangibleAsset getAsset(long id) {
        errMessage = "";
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
            errMessage = e.getMessage();
        }
        }
        return data;
    }

    @Override
    public List<RefFixedTangibleAssetType> getAllAssetType() {
        errMessage = "";
        List<RefFixedTangibleAssetType> list = new ArrayList();
        if (ClassSession.checkUserSession()) {  //check user current session
        try {
            session = HibernateUtil.getSessionFactory().getCurrentSession();
            session.beginTransaction();
            list = session.getNamedQuery("findAllRefFixedTangibleAssetType").list();
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
    public List<RefFixedTangibleAssetDepreciationStatus> getAllAssetDepreciationStatus() {
        errMessage = "";        
        List<RefFixedTangibleAssetDepreciationStatus> list = new ArrayList();
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
            errMessage = e.getMessage();
        }
        }
        return list;
    }

    //----------------------------------------------------
    @Override
    public String getErrorMessage() {
        return errMessage;
    }
    /*public static void main(String[] args) {
     try {
     TblFixedTangibleAsset asset = new TblFixedTangibleAsset();
     asset.setCodeAsset("A02");
     asset.setAssetName("TV1");
     Date d = new Date();
     SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
     System.out.println("Today's date is: "+dateFormat.format(d));
     String strd2 = "14/05/2017";
     Date d2 = dateFormat.parse(strd2);
     java.sql.Date dsql = new java.sql.Date(d2.getTime());
     System.out.println("convert to sql : "+dsql);
     asset.setBeginDate(dsql);
     long beginvalue = 3000000;
     asset.setBeginValue(beginvalue);
     asset.setCurrentValue(beginvalue);
     BigDecimal economic = new BigDecimal("10.000");
     asset.setEconomicLife(economic);
     asset.setAssetNote("Televisi");
     insertAsset(asset);
     } catch (ParseException ex) {
     Logger.getLogger(FAssetManagerImpl.class.getName()).log(Level.SEVERE, null, ex);
     }
     }*/

    /* public static void main(String[] args) {
     List<RefFixedTangibleAssetType>a = getAllAssetType();
     for(int i = 0; i<a.size();i++)
     {
     RefFixedTangibleAssetType as = a.get(i);
     System.out.println(as.getTypeName());
     }
     }*/
    
}
