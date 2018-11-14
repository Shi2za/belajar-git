/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hotelfx.persistence.service;

import hotelfx.helper.ClassCoder;
import hotelfx.helper.ClassSession;
import hotelfx.persistence.HibernateUtil;
import hotelfx.persistence.model.RefCountry;
import hotelfx.persistence.model.RefFounderType;
import hotelfx.persistence.model.RefLostFoundStatus;
import hotelfx.persistence.model.RefRecordStatus;
import hotelfx.persistence.model.TblCustomer;
import hotelfx.persistence.model.TblEmployee;
import hotelfx.persistence.model.TblFoundInformation;
import hotelfx.persistence.model.TblLostFoundInformationDetail;
import hotelfx.persistence.model.TblLostInformation;
import hotelfx.persistence.model.TblPeople;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import org.hibernate.Session;

/**
 *
 * @author Andreas
 */
public class FLostFoundInformationManagerImpl implements FLostFoundInformationManager{
    Session session;
    String errMessage;
    
    public boolean insertDataLostInformation(TblLostInformation tblLostInformation){
       errMessage = "";
       TblLostInformation dataLostInformation = tblLostInformation;
       
       if(ClassSession.checkUserSession()){
           try{
             session  = HibernateUtil.getSessionFactory().getCurrentSession();
             session.beginTransaction();
             //System.out.println("di service:"+dataLostInformation.getTblPeople().getFullName());
             dataLostInformation.setCodeLost(ClassCoder.getCode("Lost", session));
             dataLostInformation.setRefLostFoundStatus(session.find(RefLostFoundStatus.class,0));
             dataLostInformation.setTblEmployeeByCreatedBy(session.find(TblEmployee.class,ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
             dataLostInformation.setCreatedDate(Timestamp.valueOf(LocalDateTime.now()));
             dataLostInformation.setTblEmployeeByUpdatedBy(session.find(TblEmployee.class,ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
             dataLostInformation.setUpdatedDate(Timestamp.valueOf(LocalDateTime.now()));
             dataLostInformation.setRefRecordStatus(session.find(RefRecordStatus.class,1));
             session.saveOrUpdate(dataLostInformation);
             session.getTransaction().commit();
           }
           catch(Exception e){
              if(session.getTransaction().isActive()){
                 session.getTransaction().rollback();
              }
              errMessage = e.getMessage();
              return false;
           }
        }
       else{
          return false;
       }
       return true;
    }
    
     public boolean insertDataPeopleInformation(TblPeople tblPeople){
       errMessage = "";
       TblPeople dataPeople = tblPeople;
       
       if(ClassSession.checkUserSession()){
           try{
             session  = HibernateUtil.getSessionFactory().getCurrentSession();
             session.beginTransaction();
             //dataPeople.setRefLostFoundStatus(session.find(RefLostFoundStatus.class,0));
             dataPeople.setTblEmployeeByCreateBy(session.find(TblEmployee.class,ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
             dataPeople.setCreateDate(Timestamp.valueOf(LocalDateTime.now()));
             dataPeople.setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class,ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
             dataPeople.setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
             dataPeople.setRefRecordStatus(session.find(RefRecordStatus.class,1));
             session.saveOrUpdate(dataPeople);
             session.getTransaction().commit();
           }
           catch(Exception e){
              if(session.getTransaction().isActive()){
                 session.getTransaction().rollback();
              }
              errMessage = e.getMessage();
              return false;
           }
        }
       else{
          return false;
       }
       return true;
    }
    
    public boolean updateDataLostInformation(TblLostInformation tblLostInformation){
       errMessage = "";
       if(ClassSession.checkUserSession()){
           try{
              session = HibernateUtil.getSessionFactory().getCurrentSession();
              session.beginTransaction();
              tblLostInformation.setTblEmployeeByUpdatedBy(session.find(TblEmployee.class,ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
              tblLostInformation.setUpdatedDate(Timestamp.valueOf(LocalDateTime.now()));
              tblLostInformation.setRefRecordStatus(session.find(RefRecordStatus.class,1));
              session.update(tblLostInformation);
              session.getTransaction().commit();
           }
           catch(Exception e){
               if(session.getTransaction().isActive()){
                 session.getTransaction().rollback();
               }
               errMessage = e.getMessage();
               return false;
           }
        }
       else{
          return false;
       }
       return true;
    }
    
    public boolean deleteDataLostInformation(TblLostInformation tblLostInformation){
       errMessage = "";
       if(ClassSession.checkUserSession()){
          try{
             session = HibernateUtil.getSessionFactory().getCurrentSession();
             session.beginTransaction();
             tblLostInformation.setTblEmployeeByUpdatedBy(session.find(TblEmployee.class,ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
             tblLostInformation.setUpdatedDate(Timestamp.valueOf(LocalDateTime.now()));
             tblLostInformation.setRefRecordStatus(session.find(RefRecordStatus.class,2));
             session.update(tblLostInformation);
             session.getTransaction().commit();
           }
          catch(Exception e){
               if(session.getTransaction().isActive()){
                 session.getTransaction().rollback();
                }
               errMessage = e.getMessage();
               return false;
            }
        }
       else{
         return false;
       }
       return true;
    }
    
    public List<TblLostInformation>getAllDataLostInformation(){
       errMessage = "";
       List<TblLostInformation>list = new ArrayList();
       if(ClassSession.checkUserSession()){
           try{
              session = HibernateUtil.getSessionFactory().getCurrentSession();
              session.beginTransaction();
              list = session.getNamedQuery("findAllTblLostInformation").list();
              session.getTransaction().commit();
            }
           catch(Exception e){
               if(session.getTransaction().isActive()){
                 session.getTransaction().rollback();
               }
               errMessage = e.getMessage();
            }
        }
       return list;
    }
    
    public List<TblLostInformation>getAllDataLostInformationReturn(){
      errMessage = "";
      List<TblLostInformation>list = new ArrayList();
      
       if(ClassSession.checkUserSession()){
            try{
              session = HibernateUtil.getSessionFactory().getCurrentSession();
              session.beginTransaction();
              list = session.getNamedQuery("findAllTblLostInformationReturn").list();
              session.getTransaction().commit();
            }
            catch(Exception e){
               if(session.getTransaction().isActive()){
                  session.getTransaction().rollback();
               }
            }
       }
       return list;
    }
    
    public TblLostInformation getDataLostInformation(long id){
       errMessage = "";
       TblLostInformation data = new TblLostInformation();
       
       if(ClassSession.checkUserSession()){
           try{
             session = HibernateUtil.getSessionFactory().getCurrentSession();
             session.beginTransaction();
             data = (TblLostInformation)session.find(TblLostInformation.class, id);
             session.getTransaction().commit();
            }
           catch(Exception e){
               if(session.getTransaction().isActive()){
                 session.getTransaction().rollback();
               }
             errMessage = e.getMessage();
            }
        }
       
       return data;
    }
    
    //FOUND INFORMATION
    public boolean insertDataFounderInformation(TblFoundInformation tblFoundInformation){
       TblFoundInformation dataFoundInformation = tblFoundInformation;
       errMessage = "";
       
       if(ClassSession.checkUserSession()){
           try{
             session = HibernateUtil.getSessionFactory().getCurrentSession();
             session.beginTransaction();
             dataFoundInformation.setCodeFound(ClassCoder.getCode("Found", session));
             dataFoundInformation.setRefLostFoundStatus(session.find(RefLostFoundStatus.class,1));
             dataFoundInformation.setTblEmployeeByCreatedBy(session.find(TblEmployee.class,ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
             dataFoundInformation.setCreatedDate(Timestamp.valueOf(LocalDateTime.now()));
             dataFoundInformation.setTblEmployeeByUpdatedBy(session.find(TblEmployee.class,ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
             dataFoundInformation.setUpdatedDate(Timestamp.valueOf(LocalDateTime.now()));
             dataFoundInformation.setRefRecordStatus(session.find(RefRecordStatus.class,1));
             session.saveOrUpdate(dataFoundInformation);
             session.getTransaction().commit();
           }
           catch(Exception e){
               if(session.getTransaction().isActive()){
                  session.getTransaction().rollback();
               }
               errMessage = e.getMessage();
               return false;
           }
       }
       else{
         return false;  
       }
       return true;
    }
    
    public boolean updateDataFoundInformation(TblFoundInformation tblFoundInformation){
       errMessage = "";
       
       if(ClassSession.checkUserSession()){
          try{
             session = HibernateUtil.getSessionFactory().openSession();
             session.beginTransaction();
             tblFoundInformation.setTblEmployeeByUpdatedBy(session.find(TblEmployee.class,ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
             tblFoundInformation.setUpdatedDate(Timestamp.valueOf(LocalDateTime.now()));
             tblFoundInformation.setRefRecordStatus(session.find(RefRecordStatus.class,1));
             session.update(tblFoundInformation);
             session.getTransaction().commit();
           }
           catch(Exception e){
               if(session.getTransaction().isActive()){
                  session.getTransaction().rollback();
               }
               errMessage = e.getMessage();
               return false;
           }
       }
       else{
          return false;
       }
       return true;
    }
    
    public boolean deleteDataFoundInformation(TblFoundInformation tblFoundInformation){
       errMessage = "";
       
       if(ClassSession.checkUserSession()){
           try{
             session = HibernateUtil.getSessionFactory().getCurrentSession();
             session.beginTransaction();
             tblFoundInformation.setTblEmployeeByUpdatedBy(session.find(TblEmployee.class,ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
             tblFoundInformation.setUpdatedDate(Timestamp.valueOf(LocalDateTime.now()));
             tblFoundInformation.setRefRecordStatus(session.find(RefRecordStatus.class,2));
             session.update(tblFoundInformation);
             session.getTransaction().commit();
           }
           catch(Exception e){
             if(session.getTransaction().isActive()){
                session.getTransaction().rollback();
             }
             errMessage = e.getMessage();
             return false;
           }
       }
       else{
          return false;
       }
       return true;
    }
    
    public List<RefFounderType>getAllDataFounderType(){
      errMessage = "";
      List<RefFounderType>list = new ArrayList();
      
      if(ClassSession.checkUserSession()){
           try{
             session = HibernateUtil.getSessionFactory().getCurrentSession();
             session.beginTransaction();
             list = session.getNamedQuery("findAllRefFounderType").list();
             session.getTransaction().commit();
            }
           catch(Exception e){
               if(session.getTransaction().isActive()){
                 session.getTransaction().rollback();
               }
               errMessage = e.getMessage();
            }
        }
      return list;
    }
    
    public List<TblFoundInformation>getAllDataFoundInformation(){
       errMessage = "";
       List<TblFoundInformation>list = new ArrayList();
       
       if(ClassSession.checkUserSession()){
           try{
             session = HibernateUtil.getSessionFactory().getCurrentSession();
             session.beginTransaction();
             list = session.getNamedQuery("findAllTblFoundInformation").list();
             session.getTransaction().commit();
           }
           catch(Exception e){
               if(session.getTransaction().isActive()){
                 session.getTransaction().rollback();
               }
               errMessage = e.getMessage();
           }
       }
       return list;
    }
    
     public List<TblFoundInformation>getAllDataFoundInformationReturn(){
      errMessage = "";
      List<TblFoundInformation>list = new ArrayList();
      
       if(ClassSession.checkUserSession()){
            try{
              session = HibernateUtil.getSessionFactory().getCurrentSession();
              session.beginTransaction();
              list = session.getNamedQuery("findAllTblFoundInformationReturn").list();
              session.getTransaction().commit();
            }
            catch(Exception e){
               if(session.getTransaction().isActive()){
                  session.getTransaction().rollback();
               }
            }
       }
       return list;
    }
     
    public TblFoundInformation getDataFoundInformation(long id){
        errMessage = "";
        TblFoundInformation data = new TblFoundInformation();
        
        if(ClassSession.checkUserSession()){
           try{
             session = HibernateUtil.getSessionFactory().getCurrentSession();
             session.beginTransaction();
             data = session.find(TblFoundInformation.class, id);
             session.getTransaction().commit();
           }
           catch(Exception e){
               if(session.getTransaction().isActive()){
                  session.getTransaction().rollback();
               }
           }
        }
        return data;
    }
    
    //RETURN
    public boolean insertDataReturnInformation(TblLostFoundInformationDetail tblLostFoundInformationDetail){
       errMessage = "";
       TblLostFoundInformationDetail dataLostFoundInformationDetail = tblLostFoundInformationDetail;
       
       if(ClassSession.checkUserSession()){
          try{
             session = HibernateUtil.getSessionFactory().getCurrentSession();
             session.beginTransaction();
             dataLostFoundInformationDetail.getTblLostInformation().setRefLostFoundStatus(session.find(RefLostFoundStatus.class,2));
             session.update(dataLostFoundInformationDetail.getTblLostInformation());
             dataLostFoundInformationDetail.getTblFoundInformation().setRefLostFoundStatus(session.find(RefLostFoundStatus.class,2));
             session.update(dataLostFoundInformationDetail.getTblFoundInformation());
             dataLostFoundInformationDetail.setTblEmployeeByCreatedBy(session.find(TblEmployee.class,ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
             dataLostFoundInformationDetail.setCreatedDate(Timestamp.valueOf(LocalDateTime.now()));
             dataLostFoundInformationDetail.setTblEmployeeByUpdatedBy(session.find(TblEmployee.class,ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
             dataLostFoundInformationDetail.setUpdatedDate(Timestamp.valueOf(LocalDateTime.now()));
             dataLostFoundInformationDetail.setRefRecordStatus(session.find(RefRecordStatus.class,1));
             session.saveOrUpdate(dataLostFoundInformationDetail);
             session.getTransaction().commit();
          }
          catch(Exception e){
               if(session.getTransaction().isActive()){
                 session.getTransaction().rollback();
               }
               errMessage = e.getMessage();
               return false;
           }
       }
       else{
          return false;
       }
       return true;
    }
    
    public List<TblLostFoundInformationDetail>getAllDataReturn(){
       errMessage = "";
       List<TblLostFoundInformationDetail>list = new ArrayList();
       
       if(ClassSession.checkUserSession()){
          try{
            session = HibernateUtil.getSessionFactory().getCurrentSession();
            session.beginTransaction();
            list = session.getNamedQuery("findAllTblLostFoundInformationDetail").list();
            session.getTransaction().commit();
          }
          catch(Exception e){
               if(session.getTransaction().isActive()){
                 session.getTransaction().rollback();
               }
              errMessage = e.getMessage();
           }
       }
       return list;
    }
    //------------------------------------------------------------------------
    public List<TblEmployee> getAllDataEmployee(long id){
      errMessage = "";
      List<TblEmployee>list = new ArrayList();
      
      if(ClassSession.checkUserSession()){
           try{
             session = HibernateUtil.getSessionFactory().getCurrentSession();
             session.beginTransaction();
             if(id==0){
               list = session.getNamedQuery("findAllTblEmployeeWithOutRecordStatus").list();
             }
             
             if(id==1){
               list = session.getNamedQuery("findAllTblEmployee").list();
             }
             session.getTransaction().commit();
            }
            catch(Exception e){
               if(session.getTransaction().isActive()){
                  session.getTransaction().rollback();
               }
               errMessage = e.getMessage();
            }
        }
        return list;
    }
    
     public List<TblPeople> getAllDataPeople(){
      errMessage = "";
      List<TblPeople>list = new ArrayList();
      
      if(ClassSession.checkUserSession()){
           try{
             session = HibernateUtil.getSessionFactory().getCurrentSession();
             session.beginTransaction();
             list = session.getNamedQuery("findAllTblPeople").list();
             session.getTransaction().commit();
            }
            catch(Exception e){
               if(session.getTransaction().isActive()){
                  session.getTransaction().rollback();
               }
               errMessage = e.getMessage();
            }
        }
        return list;
    }
     
    public List<RefCountry>getAllDataCountry(){
         errMessage = "";
      List<RefCountry>list = new ArrayList();
      
      if(ClassSession.checkUserSession()){
           try{
             session = HibernateUtil.getSessionFactory().getCurrentSession();
             session.beginTransaction();
             list = session.getNamedQuery("findAllRefCountry").list();
             session.getTransaction().commit();
            }
            catch(Exception e){
               if(session.getTransaction().isActive()){
                  session.getTransaction().rollback();
               }
               errMessage = e.getMessage();
            }
        }
        return list;
    }
    
    public TblPeople getDataPeople(long id){
       errMessage = "";
       TblPeople data = new TblPeople();
       
       if(ClassSession.checkUserSession()){
           try{
             session = HibernateUtil.getSessionFactory().getCurrentSession();
             session.beginTransaction();
             data = (TblPeople)session.find(TblPeople.class,id);
             session.getTransaction().commit();
           }
           catch(Exception e){
               if(session.getTransaction().isActive()){
                 session.getTransaction().rollback();
                }
              errMessage = e.getMessage();
            }
        }
       
       return data;
    }
    
    @Override
    public String getErrorMessage() {
        return errMessage;
    }
    
}
