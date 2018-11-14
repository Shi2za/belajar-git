/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hotelfx.persistence.service;

import hotelfx.helper.ClassSession;
import hotelfx.persistence.HibernateUtil;
import hotelfx.persistence.model.RefRecordStatus;
import hotelfx.persistence.model.TblEmployee;
import hotelfx.persistence.model.TblEmployeeWarningLetterType;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import org.hibernate.Session;

/**
 *
 * @author Andreas
 */
public class FWarningLetterManagerImpl implements FWarningLetterManager {
   Session session;
   String errMessage;
   
   public boolean insertDataWarningLetter(TblEmployeeWarningLetterType employeeWarningLetter){
       TblEmployeeWarningLetterType tblEmployeeWarningLetter = employeeWarningLetter;
       errMessage = "";
       if(ClassSession.checkUserSession()){
           try{
              session = HibernateUtil.getSessionFactory().getCurrentSession();
              session.beginTransaction();
              tblEmployeeWarningLetter.setCreatedDate(Timestamp.valueOf(LocalDateTime.now()));
              tblEmployeeWarningLetter.setLastUpdatedDate(Timestamp.valueOf(LocalDateTime.now()));
              tblEmployeeWarningLetter.setTblEmployeeByCreatedBy(session.find(TblEmployee.class,ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
              tblEmployeeWarningLetter.setTblEmployeeByLastUpdatedBy(session.find(TblEmployee.class,ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
              tblEmployeeWarningLetter.setRefRecordStatus(session.find(RefRecordStatus.class,1));
              session.saveOrUpdate(tblEmployeeWarningLetter);
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
   
   public boolean updateDataWarningLetter(TblEmployeeWarningLetterType employeeWarningLetterType){
     errMessage = "";
       if(ClassSession.checkUserSession()){
           try{
              session = HibernateUtil.getSessionFactory().getCurrentSession();
              session.beginTransaction();
              employeeWarningLetterType.setTblEmployeeByLastUpdatedBy(session.find(TblEmployee.class,ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
              employeeWarningLetterType.setLastUpdatedDate(Timestamp.valueOf(LocalDateTime.now()));
              employeeWarningLetterType.setRefRecordStatus(session.find(RefRecordStatus.class,1));
              session.update(employeeWarningLetterType);
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
   
   public boolean deleteDataWarningLetterType(TblEmployeeWarningLetterType employeeWarningLetterType){
     errMessage = "";
       if(ClassSession.checkUserSession()){
            try{
              session = HibernateUtil.getSessionFactory().getCurrentSession();
              session.beginTransaction();
              employeeWarningLetterType.setTblEmployeeByLastUpdatedBy(session.find(TblEmployee.class,ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
              employeeWarningLetterType.setLastUpdatedDate(Timestamp.valueOf(LocalDateTime.now()));
              employeeWarningLetterType.setRefRecordStatus(session.find(RefRecordStatus.class,2));
              session.update(employeeWarningLetterType);
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
   
   public List<TblEmployeeWarningLetterType>getAllDataWarningLetter(){
     List<TblEmployeeWarningLetterType>list = new ArrayList();
     errMessage = "";
       if(ClassSession.checkUserSession()){
           try{
              session = HibernateUtil.getSessionFactory().getCurrentSession();
              session.beginTransaction();
              list = session.getNamedQuery("findAllTblEmployeeWarningLetterType").list();
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
   
   public TblEmployeeWarningLetterType getDataWarningLetter(long id){
     TblEmployeeWarningLetterType data = null;
     errMessage = "";
       if(ClassSession.checkUserSession()){
           try{
             session = HibernateUtil.getSessionFactory().getCurrentSession();
             session.beginTransaction();
             data = session.find(TblEmployeeWarningLetterType.class, id);
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
   
   public String getErrorMessage(){
      return errMessage;
   }
}
