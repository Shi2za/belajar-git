/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hotelfx.persistence.service;

import hotelfx.helper.ClassSession;
import hotelfx.persistence.HibernateUtil;
import hotelfx.persistence.model.RefEmployeeLeaveType;
import hotelfx.persistence.model.RefEmployeeType;
import hotelfx.persistence.model.RefRecordStatus;
import hotelfx.persistence.model.SysCalendar;
import hotelfx.persistence.model.TblCalendarEmployeeLeave;
import hotelfx.persistence.model.TblCalendarEmployeeSchedule;
import hotelfx.persistence.model.TblEmployee;
import java.sql.Date;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import org.hibernate.Session;

/**
 *
 * @author Andreas
 */
public class FLeaveManagerImpl implements FLeaveManager {
   private Session session;
   private String errMessage;
   
  @Override
  public boolean insertDataEmployeeLeave(List<TblCalendarEmployeeLeave>listEmployeeLeave,TblCalendarEmployeeLeave employeeLeave,String status){
    errMessage = "";
    TblCalendarEmployeeLeave tblEmployeeLeave = employeeLeave;
    if (ClassSession.checkUserSession()) {  //check user current session
    try
    {
      session = HibernateUtil.getSessionFactory().getCurrentSession();
      session.beginTransaction();
        
            tblEmployeeLeave.getTblEmployeeByIdemployee().setCurrentLeaveAmount(employeeLeave.getTblEmployeeByIdemployee().getCurrentLeaveAmount());
            tblEmployeeLeave.getTblEmployeeByIdemployee().setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
            tblEmployeeLeave.getTblEmployeeByIdemployee().setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
            tblEmployeeLeave.getTblEmployeeByIdemployee().setRefRecordStatus(session.find(RefRecordStatus.class, 1));
            session.update(tblEmployeeLeave.getTblEmployeeByIdemployee());
            if(status.equalsIgnoreCase("one day")){
              tblEmployeeLeave.setCreateDate(Timestamp.valueOf(LocalDateTime.now()));
              tblEmployeeLeave.setTblEmployeeByCreateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
              tblEmployeeLeave.setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
              tblEmployeeLeave.setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
              tblEmployeeLeave.setRefRecordStatus(session.find(RefRecordStatus.class, 1)); 
              session.saveOrUpdate(tblEmployeeLeave);
            }
            else{
                for(TblCalendarEmployeeLeave getEmployeeLeave : listEmployeeLeave){
                  getEmployeeLeave.setCutLeaveStatus(employeeLeave.getCutLeaveStatus());
                  getEmployeeLeave.setCreateDate(Timestamp.valueOf(LocalDateTime.now()));
                  getEmployeeLeave.setTblEmployeeByCreateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                  getEmployeeLeave.setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                  getEmployeeLeave.setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                  getEmployeeLeave.setRefRecordStatus(session.find(RefRecordStatus.class, 1)); 
                  session.saveOrUpdate(getEmployeeLeave);
                }  
            }
       
      session.getTransaction().commit();
    }
    catch(Exception e){
      if(session.getTransaction().isActive()){
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
  public boolean insertDataEmployeeAllLeave(List<TblCalendarEmployeeLeave>listEmployeeLeave,int amountLeave,TblCalendarEmployeeLeave dataEmployeeLeave,String status){
    errMessage = "";
    
    List<TblEmployee>listEmployee = new ArrayList();
    List<SysCalendar>listCalendar = new ArrayList();
    //List<TblCalendarEmployeeLeave>listEmployeeLeave = new ArrayList();
    List<SysCalendar>listCalendarMoreOneDay = new ArrayList();
    if (ClassSession.checkUserSession()) {  //check user current session
    try
    {
      session = HibernateUtil.getSessionFactory().getCurrentSession();
      session.beginTransaction();
        System.out.println("di luar : aaaaaa");
       // listEmployee = session.getNamedQuery("findAllTblEmployeeByTypeEmployee").setParameter("idType",0).list();
        System.out.println("employee : aaaaaa");
        
        for(TblCalendarEmployeeLeave getEmployeeLeave : listEmployeeLeave){
           if(getEmployeeLeave.getTblEmployeeByIdemployee().getCurrentLeaveAmount()==0 || getEmployeeLeave.getTblEmployeeByIdemployee().getCurrentLeaveAmount()<amountLeave){
              getEmployeeLeave.getTblEmployeeByIdemployee().setCurrentLeaveAmount(0);
            }
            else{
                if(getEmployeeLeave.getCutLeaveStatus().booleanValue()==false){
                   amountLeave = 0;
                }
               
               if(status.equals("one more day")){
                    if(getEmployeeLeave.getCutLeaveStatus().booleanValue()==true){
                      getEmployeeLeave.getTblEmployeeByIdemployee().setCurrentLeaveAmount(getEmployeeLeave.getTblEmployeeByIdemployee().getCurrentLeaveAmount()-1);
                    }
                    else{
                      amountLeave = 0; 
                      getEmployeeLeave.getTblEmployeeByIdemployee().setCurrentLeaveAmount(getEmployeeLeave.getTblEmployeeByIdemployee().getCurrentLeaveAmount()-amountLeave);
                    }
               }
               else{
                 getEmployeeLeave.getTblEmployeeByIdemployee().setCurrentLeaveAmount(getEmployeeLeave.getTblEmployeeByIdemployee().getCurrentLeaveAmount()-amountLeave);
               }
               
               System.out.println("amount leave:"+amountLeave);
               
            }
            
            //getEmployeeLeave.getTblEmployeeByIdemployee().setTblEmployeeByCreateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
            getEmployeeLeave.getTblEmployeeByIdemployee().setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
            getEmployeeLeave.getTblEmployeeByIdemployee().setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
            getEmployeeLeave.getTblEmployeeByIdemployee().setRefRecordStatus(session.find(RefRecordStatus.class, 1)); 
            session.update(getEmployeeLeave.getTblEmployeeByIdemployee());
            
            getEmployeeLeave.setNote(dataEmployeeLeave.getNote());
            getEmployeeLeave.setTblEmployeeByCreateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
            getEmployeeLeave.setCreateDate(Timestamp.valueOf(LocalDateTime.now()));
            getEmployeeLeave.setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
            getEmployeeLeave.setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
            getEmployeeLeave.setRefRecordStatus(session.find(RefRecordStatus.class, 1)); 
            session.saveOrUpdate(getEmployeeLeave);
        }
       /* for(TblEmployee getEmployee : listEmployee){
            //update employee leave in table employee
            
            if(getEmployee.getCurrentLeaveAmount()==0 || getEmployee.getCurrentLeaveAmount()<amountLeave){
              getEmployee.setCurrentLeaveAmount(0);
            }
            else{
              getEmployee.setCurrentLeaveAmount(getEmployee.getCurrentLeaveAmount()-amountLeave);
            }
            
            getEmployee.setTblEmployeeByCreateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
            getEmployee.setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
            getEmployee.setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
            getEmployee.setRefRecordStatus(session.find(RefRecordStatus.class, 1)); 
            session.update(getEmployee);
            //insert employee leave
            
            if(status.equalsIgnoreCase("one day")){
                TblCalendarEmployeeLeave employeeLeave = new TblCalendarEmployeeLeave();
                employeeLeave.setTblEmployeeByIdemployee(getEmployee);
                listCalendar = session.getNamedQuery("findAllSysCalendarByDate").setParameter("date",dateLeave).list();
                if(listCalendar.size()!=0){
                    for(SysCalendar getCalendar : listCalendar){
                      employeeLeave.setSysCalendar(getCalendar);
                    }
                }
                else{
                  employeeLeave.setSysCalendar(new SysCalendar());
                  employeeLeave.getSysCalendar().setCalendarDate(dateLeave);
                  employeeLeave.setTblEmployeeByCreateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                  employeeLeave.setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                  employeeLeave.setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                  employeeLeave.setRefRecordStatus(session.find(RefRecordStatus.class, 1)); 
                  session.saveOrUpdate(employeeLeave.getSysCalendar());
                }
                employeeLeave.setCutLeaveStatus(dataLeave.getCutLeaveStatus());
                employeeLeave.setNote(dataLeave.getNote());
                employeeLeave.setRefEmployeeLeaveType(dataLeave.getRefEmployeeLeaveType());
                employeeLeave.setTblEmployeeByCreateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                employeeLeave.setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                employeeLeave.setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                employeeLeave.setRefRecordStatus(session.find(RefRecordStatus.class, 1));
                session.saveOrUpdate(employeeLeave);
            }
            else{
                listCalendarMoreOneDay = session.getNamedQuery("findAllSysCalendar").setParameter("startDate",startLeave).setParameter("endDate",endLeave).list();
                boolean found = false;
                for(Date date=startLeave;!date.after(endLeave);date = Date.valueOf(date.toLocalDate().plusDays(1))){
                    TblCalendarEmployeeLeave employeeLeave = new TblCalendarEmployeeLeave();
                    employeeLeave.setTblEmployeeByIdemployee(getEmployee);
                    System.out.println("hal date>>"+date);
                    for(SysCalendar getCalendar : listCalendarMoreOneDay){
                        if(getCalendar.getCalendarDate().equals(date)){
                            System.out.println("date:"+date);
                            employeeLeave.setSysCalendar(getCalendar);
                            found = true;
                            break;
                        }
                    }
                    
                    if(found==false){
                      //employeeLeave.setSysCalendar(new SysCalendar());
                      employeeLeave.getSysCalendar().setCalendarDate(date);
                      employeeLeave.getSysCalendar().setTblEmployeeByCreateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                      employeeLeave.getSysCalendar().setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                      employeeLeave.getSysCalendar().setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                      employeeLeave.getSysCalendar().setRefRecordStatus(session.find(RefRecordStatus.class, 1)); 
                      session.saveOrUpdate(employeeLeave.getSysCalendar());     
                    }
                    employeeLeave.setCutLeaveStatus(dataLeave.getCutLeaveStatus());
                    employeeLeave.setNote(dataLeave.getNote());
                    employeeLeave.setRefEmployeeLeaveType(dataLeave.getRefEmployeeLeaveType());
                    employeeLeave.setTblEmployeeByCreateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                    employeeLeave.setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                    employeeLeave.setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                    employeeLeave.setRefRecordStatus(session.find(RefRecordStatus.class, 1));
                    session.saveOrUpdate(employeeLeave);
                }
            }
            
        }
     // tblLeave.getTblEmployeeByIdemployee().setLeaveAmount(leave.getTblEmployeeByIdemployee().getLeaveAmount()-count);*/
      session.getTransaction().commit();
    }
    catch(Exception e){
      if(session.getTransaction().isActive()){
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
   public boolean updateDataLeave(TblCalendarEmployeeLeave employeeLeave,Date date){
     errMessage = "";
     if (ClassSession.checkUserSession()) {  //check user current session
      try{
         session = HibernateUtil.getSessionFactory().getCurrentSession();
         session.beginTransaction();
    
           if(employeeLeave.getRefEmployeeLeaveType().getIdtype()==0){
              List<SysCalendar>listSysCalendar = session.getNamedQuery("findAllSysCalendarByDate").setParameter("date",date).list();
               if(listSysCalendar.size()!=0){
                   for(SysCalendar getSysCalendar : listSysCalendar){
                     employeeLeave.setSysCalendar(getSysCalendar);
                    }
                }
                else
                {
                  employeeLeave.getSysCalendar().setCalendarDate(date);
                  employeeLeave.getSysCalendar().setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                  employeeLeave.getSysCalendar().setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                  employeeLeave.getSysCalendar().setRefRecordStatus(session.find(RefRecordStatus.class, 1));
                  session.saveOrUpdate(employeeLeave.getSysCalendar());
                }
            }
            else{
             employeeLeave.getTblEmployeeByIdemployee().setCurrentLeaveAmount(employeeLeave.getTblEmployeeByIdemployee().getCurrentLeaveAmount());
             employeeLeave.getTblEmployeeByIdemployee().setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
             employeeLeave.getTblEmployeeByIdemployee().setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
             employeeLeave.getTblEmployeeByIdemployee().setRefRecordStatus(session.find(RefRecordStatus.class, 1));
             session.update(employeeLeave.getTblEmployeeByIdemployee()); 
            }
         employeeLeave.setRefRecordStatus(session.find(RefRecordStatus.class,1));
         employeeLeave.setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
         employeeLeave.setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
     
         session.update(employeeLeave);
         session.getTransaction().commit();
        }
        catch(Exception e){
            if(session.getTransaction().isActive()){
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
  public boolean deleteDataLeave(TblCalendarEmployeeLeave employeeLeave){
    errMessage = "";
    if (ClassSession.checkUserSession()) {  //check user current session
    try{
      session = HibernateUtil.getSessionFactory().getCurrentSession();
      session.beginTransaction();
        if (employeeLeave.getRefRecordStatus().getIdstatus() == 1){
            employeeLeave.getTblEmployeeByIdemployee().setCurrentLeaveAmount(employeeLeave.getTblEmployeeByIdemployee().getCurrentLeaveAmount());
            employeeLeave.getTblEmployeeByIdemployee().setRefRecordStatus(session.find(RefRecordStatus.class, 1));
            employeeLeave.getTblEmployeeByIdemployee().setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
            employeeLeave.getTblEmployeeByIdemployee().setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
            session.update(employeeLeave.getTblEmployeeByIdemployee());
            employeeLeave.setRefRecordStatus(session.find(RefRecordStatus.class, 2));
            employeeLeave.setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
            employeeLeave.setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
            session.update(employeeLeave);
        }
        session.getTransaction().commit();
    }
    catch(Exception e){
      if(session.getTransaction().isActive()){
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
   public List<TblCalendarEmployeeLeave>getAllDataLeave(){
     errMessage = "";
     List<TblCalendarEmployeeLeave>list = new ArrayList();
     if (ClassSession.checkUserSession()) {  //check user current session
     try
     {
       session = HibernateUtil.getSessionFactory().getCurrentSession();
       session.beginTransaction();
       list = session.getNamedQuery("findAllTblCalendarEmployeeLeave").list();
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
   
   @Override
   public List<RefEmployeeLeaveType>getAllLeaveType(){
     errMessage = "";
     List<RefEmployeeLeaveType>list = new ArrayList();
     if (ClassSession.checkUserSession()) {  //check user current session
     try
     {
       session = HibernateUtil.getSessionFactory().getCurrentSession();
       session.beginTransaction();
       list = session.getNamedQuery("findAllRefEmployeeLeaveType").list();
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
   
   @Override
   public List<RefEmployeeType>getAllEmployeeType(){
     errMessage = "";
     List<RefEmployeeType>list = new ArrayList();
     if (ClassSession.checkUserSession()) {  //check user current session
     try
     {
       session = HibernateUtil.getSessionFactory().getCurrentSession();
       session.beginTransaction();
       list = session.getNamedQuery("findAllRefEmployeeType").list();
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
   
    @Override
  public List<TblEmployee>getAllDataEmployee(RefEmployeeType employeeType){
    errMessage = "";
    List<TblEmployee>list = new ArrayList();
    if (ClassSession.checkUserSession()) {  //check user current session
    try
    {
      session = HibernateUtil.getSessionFactory().getCurrentSession();
      session.beginTransaction();
      if(employeeType.getIdtype()==3){
        list = session.getNamedQuery("findAllTblEmployee").list();
      }
      else{
       list = session.getNamedQuery("findAllTblEmployeeByTypeEmployee").setParameter("idType",employeeType.getIdtype()).list();
      }
      
      session.getTransaction().commit();
    }
    catch(Exception e)
    {
      if(session.getTransaction().isActive()){
       session.getTransaction().rollback();
      }
      errMessage = e.getMessage();
    }
    }
    return list;
  }
  
   @Override
   public List<TblCalendarEmployeeSchedule>getAllDataEmployeeScheduleByIdEmployeeAndDate(long id,Date date){
      errMessage = "";
      List<TblCalendarEmployeeSchedule>list = new ArrayList();
      int count = 0;  
      if (ClassSession.checkUserSession()) {  //check user current session
      try
      {
        System.out.println("id:"+id);
        System.out.println("date:"+date);
        session = HibernateUtil.getSessionFactory().getCurrentSession();
        session.beginTransaction();
        list = session.getNamedQuery("findAllTblCalendarEmployeeScheduleByIdEmployeeOneDay").setParameter("idEmployee",id).setParameter("date",date).list();
        System.out.println("list:"+list.size());
        session.getTransaction().commit();
      }
      catch(Exception e)
      {
        System.out.println(e.getMessage());
        if(session.getTransaction().isActive()){
         session.getTransaction().rollback();
       }
       errMessage = e.getMessage();
      }
      }
    return list;    
  }
   
   @Override
  public List<TblCalendarEmployeeSchedule>getAllDataEmployeeScheduleByIdEmployee(long id){
      errMessage = "";
      List<TblCalendarEmployeeSchedule>list = new ArrayList();
      int count = 0;  
      if (ClassSession.checkUserSession()) {  //check user current session
      try
      {
        session = HibernateUtil.getSessionFactory().getCurrentSession();
        session.beginTransaction();
        list = session.getNamedQuery("findAllTblCalendarEmployeeScheduleByIdEmployeeAndMonth").setParameter("idEmployee",id).setParameter("date",Date.valueOf(LocalDate.now().minusMonths(1))).list();
       
        session.getTransaction().commit();
      }
      catch(Exception e)
      {
        if(session.getTransaction().isActive()){
         session.getTransaction().rollback();
       }
       errMessage = e.getMessage();
      }
      }
    return list;    
  }
     
  public List<TblCalendarEmployeeSchedule>sortEmployeeSchedule(List<TblCalendarEmployeeSchedule>list){
        for(int i = 0; i<list.size()-1;i++){
          TblCalendarEmployeeSchedule employeeSchedule = new TblCalendarEmployeeSchedule();
            if(list.get(i).getSysCalendar().getCalendarDate().before(list.get(i+1).getSysCalendar().getCalendarDate())){
              employeeSchedule = list.get(i);
            }
            
            list.set(i,list.get(i+1));
            list.set(i+1,employeeSchedule);
        }
      return list;
  }
  /*public List<TblCalendarEmployeeSchedule>getAllDataEmployeeScheduleByIdEmployee(long id,String status,Date date,Date startDate,Date endDate){
      List<TblCalendarEmployeeSchedule>list = new ArrayList();
      errMessage = "";
      int count = 0;
      try
      {
        session = HibernateUtil.getSessionFactory().getCurrentSession();
        session.beginTransaction();
        if(status.equals("one day")){
            System.out.println("d dlm>>"+status);
          list = session.getNamedQuery("findAllTblCalendarEmployeeScheduleByIdEmployeeOneDay").setParameter("idEmployee",id).setParameter("date",date).list();
        }
        else{
          list = session.getNamedQuery("findAllTblCalendarEmployeeScheduleByIdEmployeeOneMoreDay").setParameter("idEmployee",id).setParameter("startDate",startDate).setParameter("endDate",endDate).list();
        }
        
        session.getTransaction().commit();
      }
      catch(Exception e)
      {
        if(session.getTransaction().isActive()){
         session.getTransaction().rollback();
       }
       errMessage = e.getMessage();
       return null;
    }
    return list;    
  }*/
  
  @Override
  public List<TblCalendarEmployeeLeave>getAllDataEmployeeLeaveByDateAndLeaveType(Date date){
    errMessage = "";
    List<TblCalendarEmployeeLeave>list = new ArrayList();
    if (ClassSession.checkUserSession()) {  //check user current session
    try
    {
      session = HibernateUtil.getSessionFactory().getCurrentSession();
      session.beginTransaction();
      
       list = session.getNamedQuery("findAllTblCalendarEmployeeLeaveBySysCalendarAndTypeLeave").setParameter("date",date).list();
      
      session.getTransaction().commit();
    }
    catch(Exception e)
    {
      if(session.getTransaction().isActive()){
       session.getTransaction().rollback();
      }
      errMessage = e.getMessage();
    }
    }
    return list;
  }
  
   @Override
  public List<TblCalendarEmployeeLeave>getAllDataEmployeeLeaveByDateAndIdEmployee(TblEmployee employee,Date date){
    errMessage = "";
    List<TblCalendarEmployeeLeave>list = new ArrayList();
    if (ClassSession.checkUserSession()) {  //check user current session
    try
    {
      session = HibernateUtil.getSessionFactory().getCurrentSession();
      session.beginTransaction();
      
       list = session.getNamedQuery("findAllTblCalendarEmployeeLeaveBySysCalendarAndIdEmployee").setParameter("date",date).setParameter("idEmployee",employee.getIdemployee()).list();
      
      session.getTransaction().commit();
    }
    catch(Exception e)
    {
      if(session.getTransaction().isActive()){
       session.getTransaction().rollback();
      }
      errMessage = e.getMessage();
    }
    }
    return list;
  }
  
  @Override
  public List<TblCalendarEmployeeLeave>getAllDataEmployeeLeaveByStartDateEndDateAndIdEmployee(TblEmployee employee,Date startDate,Date endDate){
    errMessage = "";
    List<TblCalendarEmployeeLeave>list = new ArrayList();
    if (ClassSession.checkUserSession()) {  //check user current session
    try
    {
      session = HibernateUtil.getSessionFactory().getCurrentSession();
      session.beginTransaction();
      
      list = session.getNamedQuery("findAllTblCalendarEmployeeLeaveByStartDateEndDateAndIdEmployee").setParameter("startDate",startDate).setParameter("endDate",endDate).setParameter("idEmployee",employee.getIdemployee()).list();
      
      session.getTransaction().commit();
    }
    catch(Exception e)
    {
      if(session.getTransaction().isActive()){
       session.getTransaction().rollback();
      }
      errMessage = e.getMessage();
    }
    }
    return list;
  }
  
  @Override
  public List<TblCalendarEmployeeLeave>getAllDataEmployeeLeaveByStartDateEndDateAndTypeLeave(Date startDate,Date endDate){
    errMessage = "";
    List<TblCalendarEmployeeLeave>list = new ArrayList();
    if (ClassSession.checkUserSession()) {  //check user current session
    try
    {
      session = HibernateUtil.getSessionFactory().getCurrentSession();
      session.beginTransaction();
      
      list = session.getNamedQuery("findAllTblCalendarEmployeeLeaveByStartDateEndDateAndTypeLeave").setParameter("startDate",startDate).setParameter("endDate",endDate).list();
      
      session.getTransaction().commit();
    }
    catch(Exception e)
    {
      if(session.getTransaction().isActive()){
       session.getTransaction().rollback();
      }
      errMessage = e.getMessage();
    }
    }
    return list;
  }
  
   @Override
  public List<SysCalendar>getAllSysCalendar(Date date,Date startLeave,Date endLeave,String status){
      errMessage = "";
      List<SysCalendar>listCalendar = new ArrayList();
      if (ClassSession.checkUserSession()) {  //check user current session
      try{
        session = HibernateUtil.getSessionFactory().getCurrentSession();
        session.beginTransaction();
        if(status.equalsIgnoreCase("one day")){
          listCalendar = session.getNamedQuery("findAllSysCalendarByDate").setParameter("date",date).list();
        }
        else{
          listCalendar = session.getNamedQuery("findAllSysCalendar").setParameter("startDate",startLeave).setParameter("endDate",endLeave).list();
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
      return listCalendar;
    }
  
   @Override
   public TblCalendarEmployeeLeave getDataEmployeeLeave(long id){
    errMessage = "";
    TblCalendarEmployeeLeave data = null;
    if (ClassSession.checkUserSession()) {  //check user current session
    try
    {
      session = HibernateUtil.getSessionFactory().getCurrentSession();
      session.beginTransaction();
      data = (TblCalendarEmployeeLeave)session.find(TblCalendarEmployeeLeave.class, id);
   
      session.getTransaction().commit();
    }
    catch(Exception e)
    {
      if(session.getTransaction().isActive()){
       session.getTransaction().rollback();
      }
      errMessage = e.getMessage();
    }
    }
    return data;
  }
   
   @Override
  public List<TblCalendarEmployeeLeave> getAllDataLeaveByDateAndIdEmployee(SysCalendar calendar,TblEmployee employee){
    errMessage = "";
    List<TblCalendarEmployeeLeave>list = new ArrayList();
    if (ClassSession.checkUserSession()) {  //check user current session
    try
    {
      session = HibernateUtil.getSessionFactory().getCurrentSession();
      session.beginTransaction();
      list = session.getNamedQuery("findAllTblCalendarEmployeeLeaveByDateAndIdEmployee").setParameter("idCalendar",calendar.getIdcalendar()).setParameter("idEmployee",employee.getIdemployee()).list();
      //data = (TblCalendarEmployeeLeave)session.find(TblCalendarEmployeeLeave.class, id);
   
      session.getTransaction().commit();
    }
    catch(Exception e)
    {
      if(session.getTransaction().isActive()){
       session.getTransaction().rollback();
      }
      errMessage = e.getMessage();
    }
    }
    return list;
  }
  
   @Override
    public List<SysCalendar>getCalendarByDate(String status,Date date,Date startDate,Date endDate){
     errMessage = "";
     List<SysCalendar>listDate = new ArrayList();
     if (ClassSession.checkUserSession()) {  //check user current session
        try{
          session = HibernateUtil.getSessionFactory().getCurrentSession();
          session.beginTransaction();
            if(status.equals("one day")){
                if(date!=null){
                  listDate = session.getNamedQuery("findAllSysCalendarByDate").setParameter("date",date).list();
                } 
            }
            else{
                if(startDate!=null && endDate!=null){
                 listDate = session.getNamedQuery("findAllSysCalendar").setParameter("startDate",startDate).setParameter("endDate",endDate).list();
                }
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
        return listDate;
    }
    
   @Override
    public String getErrorMessage() {
        return errMessage;
    }
    
    /*public static void main(String[] args) {
       List<TblCalendarEmployeeSchedule>list = getAllDataEmployeeScheduleByIdEmployee(4);
       for(TblCalendarEmployeeSchedule getSchedule : list){
           System.out.println(getSchedule.getSysCalendar().getCalendarDate() + " "+getSchedule.getTblEmployeeByIdemployee().getCodeEmployee());
       }
    }*/
}
