/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hotelfx.persistence.service;

import hotelfx.helper.ClassDataPasswordDeleteDebt;
import hotelfx.helper.ClassSession;
import hotelfx.persistence.HibernateUtil;
import hotelfx.persistence.model.LogCompanyBalanceCashFlow;
import hotelfx.persistence.model.RefEmployeeType;
import hotelfx.persistence.model.RefFinanceTransactionPaymentType;
import hotelfx.persistence.model.RefRecordStatus;
import hotelfx.persistence.model.SysCalendar;
import hotelfx.persistence.model.SysPasswordDeleteDebt;
import hotelfx.persistence.model.TblBank;
import hotelfx.persistence.model.TblCalendarEmployeeDebt;
import hotelfx.persistence.model.TblCalendarEmployeePaymentDebtHistory;
import hotelfx.persistence.model.TblCalendarEmployeePaymentHistory;
import hotelfx.persistence.model.TblCompanyBalance;
import hotelfx.persistence.model.TblCompanyBalanceBankAccount;
import hotelfx.persistence.model.TblEmployee;
import hotelfx.persistence.model.TblEmployeeBankAccount;
import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import org.hibernate.Session;
import org.hibernate.query.Query;

/**
 *
 * @author Andreas
 */
public class FDebtManagerImpl implements FDebtManager {
    private Session session;
    private String errMessage;
    
    @Override
    public boolean insertDataDebt(TblCalendarEmployeeDebt employeeDebt){
       errMessage = "";
       if (ClassSession.checkUserSession()) {  //check user current session
       try{
         session = HibernateUtil.getSessionFactory().getCurrentSession();
         session.beginTransaction();
        /* List<SysCalendar>listCalendar = session.getNamedQuery("findAllSysCalendarByDate").setParameter("date", date).list();
           if(!listCalendar.isEmpty()){
              for(SysCalendar getCalendar : listCalendar){
                  employeeDebt.setSysCalendar(getCalendar);
                }  
            } */
            employeeDebt.setEmployeeDebtStatus("Dibuat");
            employeeDebt.setEmployeeDebtPaymentStatus("Belum Dibayar");
            employeeDebt.setCreatedDate(Timestamp.valueOf(LocalDateTime.now()));
            employeeDebt.setTblEmployeeByCreatedBy(session.find(TblEmployee.class,ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
            employeeDebt.setLastUpdatedDate(Timestamp.valueOf(LocalDateTime.now()));
            employeeDebt.setTblEmployeeByLastUpdatedBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
            employeeDebt.setRefRecordStatus(session.find(RefRecordStatus.class, 1));
            session.saveOrUpdate(employeeDebt);
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
    public boolean updateDataDebt(TblCalendarEmployeeDebt employeeDebt){
        errMessage = "";
        if (ClassSession.checkUserSession()) {  //check user current session
      try{
         session = HibernateUtil.getSessionFactory().getCurrentSession();
         session.beginTransaction();
        /* List<SysCalendar>listCalendar = session.getNamedQuery("findAllSysCalendarByDate").setParameter("date", date).list();
           if(!listCalendar.isEmpty()){
               for(SysCalendar getCalendar : listCalendar){
                  employeeDebt.setSysCalendar(getCalendar);
                }  
            } */
            
            employeeDebt.setLastUpdatedDate(Timestamp.valueOf(LocalDateTime.now()));
            employeeDebt.setTblEmployeeByLastUpdatedBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
            employeeDebt.setRefRecordStatus(session.find(RefRecordStatus.class, 1));
            session.update(employeeDebt);
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
    public boolean updateSettingPassword(SysPasswordDeleteDebt passwordDeleteOldDebt,SysPasswordDeleteDebt passwordDeleteNewDebt){
       errMessage = "";
       if (ClassSession.checkUserSession()) {  //check user current session
       try{
         session = HibernateUtil.getSessionFactory().getCurrentSession();
         session.beginTransaction();
         List<SysPasswordDeleteDebt>list = session.getNamedQuery("findAllSysPasswordDeleteDebtByPassword").setParameter("passwordValue",passwordDeleteOldDebt.getPasswordValue()).list();
         for(SysPasswordDeleteDebt getOldPassword : list){
            getOldPassword.setPasswordValue(passwordDeleteNewDebt.getPasswordValue());
            getOldPassword.setTblEmployeeByLastUpdatedBy(session.find(TblEmployee.class,ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
            getOldPassword.setLastUpdatedDate(Timestamp.valueOf(LocalDateTime.now()));
            getOldPassword.setRefRecordStatus(session.find(RefRecordStatus.class,1));
            session.update(getOldPassword);
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
    public boolean approvedDataDebt(TblCalendarEmployeeDebt employeeDebt){
       errMessage = "";
       TblCalendarEmployeePaymentHistory employeePaymentDebt = new TblCalendarEmployeePaymentHistory();
       LogCompanyBalanceCashFlow cashFlow = new LogCompanyBalanceCashFlow();
       if (ClassSession.checkUserSession()) {  //check user current session
       try{
         session = HibernateUtil.getSessionFactory().getCurrentSession();
         session.beginTransaction();
         employeeDebt.getTblCompanyBalance().setBalanceNominal(employeeDebt.getTblCompanyBalance().getBalanceNominal());
         employeeDebt.getTblCompanyBalance().setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
         employeeDebt.getTblCompanyBalance().setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
         employeeDebt.getTblCompanyBalance().setRefRecordStatus(session.find(RefRecordStatus.class, 1));
         session.update(employeeDebt.getTblCompanyBalance());
         if(employeeDebt.getTblCompanyBalanceBankAccount()!=null){
             employeeDebt.getTblCompanyBalanceBankAccount().setCompanyBalanceBankAccountNominal(employeeDebt.getTblCompanyBalanceBankAccount().getCompanyBalanceBankAccountNominal().subtract(employeeDebt.getEmployeeDebtNominal()));
             employeeDebt.getTblCompanyBalanceBankAccount().setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
             employeeDebt.getTblCompanyBalanceBankAccount().setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class,ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
             employeeDebt.getTblCompanyBalanceBankAccount().setRefRecordStatus(session.find(RefRecordStatus.class,1));
             session.update(employeeDebt.getTblCompanyBalanceBankAccount());
         }
         employeeDebt.setApprovedDate(Timestamp.valueOf(LocalDateTime.now()));
         employeeDebt.setLastUpdatedDate(Timestamp.valueOf(LocalDateTime.now()));
         employeeDebt.setTblEmployeeByLastUpdatedBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
         employeeDebt.setRefRecordStatus(session.find(RefRecordStatus.class, 1));
         session.update(employeeDebt);
         
       /*  employeePaymentDebt.setHistoryDate(Timestamp.valueOf(LocalDateTime.now()));
         employeePaymentDebt.setEmployeePaymentDebtNominal(BigDecimal.ZERO);
         employeePaymentDebt.setTblEmployeeByIdemployee(employeeDebt.getTblEmployeeByIdemployee());
         employeePaymentDebt.setTblCompanyBalance(employeeDebt.getTblCompanyBalance());
           System.out.println("aaa");
         employeePaymentDebt.setTblCompanyBalanceBankAccount(employeeDebt.getTblCompanyBalance().getIdbalance()==1 ? employeeDebt.getTblCompanyBalanceBankAccount() : null);
           System.out.println("bbb");
         employeePaymentDebt.setTblEmployeeByCreatedBy(session.find(TblEmployee.class,ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
         employeePaymentDebt.setCreatedDate(Timestamp.valueOf(LocalDateTime.now()));
         employeePaymentDebt.setTblEmployeeByLastUpdatedBy(session.find(TblEmployee.class,ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
         employeePaymentDebt.setLastUpdatedDate(Timestamp.valueOf(LocalDateTime.now()));
         employeePaymentDebt.setRefRecordStatus(session.find(RefRecordStatus.class,1));
         session.save(employeePaymentDebt); */
         
         cashFlow.setHistoryDate(Timestamp.valueOf(LocalDateTime.now()));
         cashFlow.setHistoryNote("Kas Bon");
           System.out.println("jjj");
         cashFlow.setTblCompanyBalanceByIdsenderCompanyBalance(employeeDebt.getTblCompanyBalance());
           System.out.println("kkk");
         cashFlow.setTblBankAccountByIdsenderCbbankAccount(employeeDebt.getTblCompanyBalanceBankAccount()!=null ? employeeDebt.getTblCompanyBalanceBankAccount().getTblBankAccount() : null);
           System.out.println("lll");
         cashFlow.setTransferNominal(employeeDebt.getEmployeeDebtNominal());
           System.out.println("mmm");
         cashFlow.setTblEmployee(session.find(TblEmployee.class,ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
         session.save(cashFlow);
         
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
      public boolean paymentDataDebt(TblCalendarEmployeePaymentHistory employeePaymentDebt){
       errMessage = "";
       LogCompanyBalanceCashFlow cashFlow = new LogCompanyBalanceCashFlow();
       
       if (ClassSession.checkUserSession()) {  //check user current session
       try{
         session = HibernateUtil.getSessionFactory().getCurrentSession();
         session.beginTransaction();
       /*  List<SysCalendar>listCalendar = session.getNamedQuery("findAllSysCalendarByDate").setParameter("date", date).list();
           if(!listCalendar.isEmpty()){
              for(SysCalendar getCalendar : listCalendar){
                  employeePaymentDebt.setSysCalendar(getCalendar);
                }  
            } */
           System.out.println("hsl company balance:"+employeePaymentDebt.getTblCompanyBalance().getBalanceName());
          /*  if(total
                    .compareTo(new BigDecimal("0"))==0){
             employeePaymentDebt.getTblCalendarEmployeeDebt().setEmployeeDebtPaymentStatus("Sudah Dibayar");
            }
            else{
             employeePaymentDebt.getTblCalendarEmployeeDebt().setEmployeeDebtPaymentStatus("Dibayar Sebagian");
            } 
         employeePaymentDebt.getTblCalendarEmployeeDebt().setLastUpdatedDate(Timestamp.valueOf(LocalDateTime.now()));
         employeePaymentDebt.getTblCalendarEmployeeDebt().setTblEmployeeByLastUpdatedBy(session.find(TblEmployee.class,ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
         employeePaymentDebt.getTblCalendarEmployeeDebt().setRefRecordStatus(session.find(RefRecordStatus.class,1));
         session.update(employeePaymentDebt.getTblCalendarEmployeeDebt()); */
           
       //UPDATE COMPANY BALANCE
         employeePaymentDebt.getTblCompanyBalance().setBalanceNominal(employeePaymentDebt.getTblCompanyBalance().getBalanceNominal().add(employeePaymentDebt.getEmployeePaymentDebtNominal()));
         employeePaymentDebt.getTblCompanyBalance().setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
         employeePaymentDebt.getTblCompanyBalance().setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class,ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
         employeePaymentDebt.getTblCompanyBalance().setRefRecordStatus(session.find(RefRecordStatus.class,1));
         session.update(employeePaymentDebt.getTblCompanyBalance());
      
         //SAVE CASH FLOW
         cashFlow.setHistoryDate(Timestamp.valueOf(LocalDateTime.now()));
         cashFlow.setTblCompanyBalanceByIdreceiverCompanyBalance(employeePaymentDebt.getTblCompanyBalance());
         cashFlow.setTblEmployee(session.find(TblEmployee.class,ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
         cashFlow.setTransferNominal(employeePaymentDebt.getEmployeePaymentDebtNominal());  
         cashFlow.setHistoryNote("Pembayaran Hutang Karyawan :"+employeePaymentDebt.getTblEmployeeByIdemployee().getCodeEmployee()+"-"+employeePaymentDebt.getTblEmployeeByIdemployee().getTblPeople().getFullName());
         
           if(employeePaymentDebt.getTblCompanyBalance().getIdbalance()==1){
             employeePaymentDebt.getTblCompanyBalanceBankAccount().setCompanyBalanceBankAccountNominal(employeePaymentDebt.getTblCompanyBalanceBankAccount().getCompanyBalanceBankAccountNominal().add(employeePaymentDebt.getEmployeePaymentDebtNominal()));
             employeePaymentDebt.getTblCompanyBalanceBankAccount().setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
             employeePaymentDebt.getTblCompanyBalanceBankAccount().setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class,ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
             employeePaymentDebt.getTblCompanyBalanceBankAccount().setRefRecordStatus(session.find(RefRecordStatus.class,1));
          
             cashFlow.setTblBankAccountByIdreceiverCbbankAccount(employeePaymentDebt.getTblCompanyBalanceBankAccount().getTblBankAccount());
             cashFlow.setTblBankAccountByIdsenderCbbankAccount(employeePaymentDebt.getTblEmployeeBankAccount().getTblBankAccount());
             
             session.update(employeePaymentDebt.getTblCompanyBalanceBankAccount());
            }
         /*employeePaymentDebt.getTblCompanyBalanceBankAccount().setCompanyBalanceBankAccountNominal(employeePaymentDebt.getTblCompanyBalanceBankAccount().getCompanyBalanceBankAccountNominal()+employeePaymentDebt.getEmployeePaymentDebtNominal());
         employeePaymentDebt.getTblCompanyBalanceBankAccount().setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
         employeePaymentDebt.getTblCompanyBalanceBankAccount().setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class,ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
         employeePaymentDebt.getTblCompanyBalanceBankAccount().setRefRecordStatus(session.find(RefRecordStatus.class,1));
         session.update(employeePaymentDebt.getTblCompanyBalanceBankAccount());*/
         session.save(cashFlow);
         
      //SAVE PAYMENT HISTORY   
         employeePaymentDebt.setHistoryDate(Timestamp.valueOf(LocalDateTime.now()));
         employeePaymentDebt.setCreatedDate(Timestamp.valueOf(LocalDateTime.now()));
         employeePaymentDebt.setLastUpdatedDate(Timestamp.valueOf(LocalDateTime.now()));
         employeePaymentDebt.setTblEmployeeByCreatedBy(session.find(TblEmployee.class,ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
         employeePaymentDebt.setTblEmployeeByLastUpdatedBy(session.find(TblEmployee.class,ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
         employeePaymentDebt.setRefRecordStatus(session.find(RefRecordStatus.class,1));
         session.save(employeePaymentDebt);
         
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
           
  /*  public boolean paymentDataDebt(TblCalendarEmployeePaymentDebtHistory employeePaymentDebt,BigDecimal total,Date date){
       errMessage = "";
       if (ClassSession.checkUserSession()) {  //check user current session
       try{
         session = HibernateUtil.getSessionFactory().getCurrentSession();
         session.beginTransaction();
         List<SysCalendar>listCalendar = session.getNamedQuery("findAllSysCalendarByDate").setParameter("date", date).list();
           if(!listCalendar.isEmpty()){
              for(SysCalendar getCalendar : listCalendar){
                  employeePaymentDebt.setSysCalendar(getCalendar);
                }  
            }
           System.out.println("hsl company balance:"+employeePaymentDebt.getTblCompanyBalance().getBalanceName());
            if(total
                    .compareTo(new BigDecimal("0"))==0){
             employeePaymentDebt.getTblCalendarEmployeeDebt().setEmployeeDebtPaymentStatus("Sudah Dibayar");
            }
            else{
             employeePaymentDebt.getTblCalendarEmployeeDebt().setEmployeeDebtPaymentStatus("Dibayar Sebagian");
            }
         employeePaymentDebt.getTblCalendarEmployeeDebt().setLastUpdatedDate(Timestamp.valueOf(LocalDateTime.now()));
         employeePaymentDebt.getTblCalendarEmployeeDebt().setTblEmployeeByLastUpdatedBy(session.find(TblEmployee.class,ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
         employeePaymentDebt.getTblCalendarEmployeeDebt().setRefRecordStatus(session.find(RefRecordStatus.class,1));
         session.update(employeePaymentDebt.getTblCalendarEmployeeDebt());
         employeePaymentDebt.getTblCompanyBalance().setBalanceNominal(employeePaymentDebt.getTblCompanyBalance().getBalanceNominal());
         employeePaymentDebt.getTblCompanyBalance().setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
         employeePaymentDebt.getTblCompanyBalance().setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class,ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
         employeePaymentDebt.getTblCompanyBalance().setRefRecordStatus(session.find(RefRecordStatus.class,1));
         session.update(employeePaymentDebt.getTblCompanyBalance());
           if(employeePaymentDebt.getTblCompanyBalance().getIdbalance()==1){
             employeePaymentDebt.getTblCompanyBalanceBankAccount().setCompanyBalanceBankAccountNominal(employeePaymentDebt.getTblCompanyBalanceBankAccount().getCompanyBalanceBankAccountNominal().add(employeePaymentDebt.getEmployeePaymentDebtNominal()));
             employeePaymentDebt.getTblCompanyBalanceBankAccount().setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
             employeePaymentDebt.getTblCompanyBalanceBankAccount().setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class,ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
             employeePaymentDebt.getTblCompanyBalanceBankAccount().setRefRecordStatus(session.find(RefRecordStatus.class,1));
             session.update(employeePaymentDebt.getTblCompanyBalanceBankAccount());
            }
         /*employeePaymentDebt.getTblCompanyBalanceBankAccount().setCompanyBalanceBankAccountNominal(employeePaymentDebt.getTblCompanyBalanceBankAccount().getCompanyBalanceBankAccountNominal()+employeePaymentDebt.getEmployeePaymentDebtNominal());
         employeePaymentDebt.getTblCompanyBalanceBankAccount().setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
         employeePaymentDebt.getTblCompanyBalanceBankAccount().setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class,ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
         employeePaymentDebt.getTblCompanyBalanceBankAccount().setRefRecordStatus(session.find(RefRecordStatus.class,1));
         session.update(employeePaymentDebt.getTblCompanyBalanceBankAccount());*/
    /*     employeePaymentDebt.setCreatedDate(Timestamp.valueOf(LocalDateTime.now()));
         employeePaymentDebt.setLastUpdatedDate(Timestamp.valueOf(LocalDateTime.now()));
         employeePaymentDebt.setTblEmployeeByCreatedBy(session.find(TblEmployee.class,ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
         employeePaymentDebt.setTblEmployeeByLastUpdatedBy(session.find(TblEmployee.class,ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
         employeePaymentDebt.setRefRecordStatus(session.find(RefRecordStatus.class,1));
         session.saveOrUpdate(employeePaymentDebt);
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
    } */
    
    public boolean rejectedDataDebt(TblCalendarEmployeeDebt employeeDebt){
      errMessage = "";
      
       if(ClassSession.checkUserSession()){
           try{
              session = HibernateUtil.getSessionFactory().getCurrentSession();
              session.beginTransaction();
              employeeDebt.setRejectDate(Timestamp.valueOf(LocalDateTime.now()));
              employeeDebt.setLastUpdatedDate(Date.valueOf(LocalDate.now()));
              employeeDebt.setTblEmployeeByLastUpdatedBy(session.find(TblEmployee.class,ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
              employeeDebt.setRefRecordStatus(session.find(RefRecordStatus.class,1));
              session.update(employeeDebt);
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
    
    @Override
    public boolean deleteDataDebt(TblCalendarEmployeeDebt employeeDebt,ClassDataPasswordDeleteDebt dataPasswordDeleteDebt){
      errMessage = "";
      if (ClassSession.checkUserSession()) {  //check user current session
      try{
           session = HibernateUtil.getSessionFactory().getCurrentSession();
           session.beginTransaction();
           if(employeeDebt.getEmployeeDebtStatus().equals("Disetujui") && dataPasswordDeleteDebt!=null){
             dataPasswordDeleteDebt.getEmployeeDebt().getTblCompanyBalance().setBalanceNominal(dataPasswordDeleteDebt.getEmployeeDebt().getTblCompanyBalance().getBalanceNominal());
             dataPasswordDeleteDebt.getEmployeeDebt().getTblCompanyBalance().setCreateDate(Timestamp.valueOf(LocalDateTime.now()));
             dataPasswordDeleteDebt.getEmployeeDebt().getTblCompanyBalance().setTblEmployeeByCreateBy(session.find(TblEmployee.class,ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
             dataPasswordDeleteDebt.getEmployeeDebt().getTblCompanyBalance().setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
             dataPasswordDeleteDebt.getEmployeeDebt().getTblCompanyBalance().setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class,ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
             dataPasswordDeleteDebt.getEmployeeDebt().getTblCompanyBalance().setRefRecordStatus(session.find(RefRecordStatus.class,1));
             session.update(dataPasswordDeleteDebt.getEmployeeDebt().getTblCompanyBalance());
               if(dataPasswordDeleteDebt.getEmployeeDebt().getTblCompanyBalance().getIdbalance()==1){
                 dataPasswordDeleteDebt.getEmployeeDebt().getTblCompanyBalanceBankAccount().setCompanyBalanceBankAccountNominal(dataPasswordDeleteDebt.getEmployeeDebt().getTblCompanyBalanceBankAccount().getCompanyBalanceBankAccountNominal().add(dataPasswordDeleteDebt.getEmployeeDebt().getEmployeeDebtNominal()));
                 dataPasswordDeleteDebt.getEmployeeDebt().getTblCompanyBalanceBankAccount().setCreateDate(Timestamp.valueOf(LocalDateTime.now()));
                 dataPasswordDeleteDebt.getEmployeeDebt().getTblCompanyBalanceBankAccount().setTblEmployeeByCreateBy(session.find(TblEmployee.class,ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                 dataPasswordDeleteDebt.getEmployeeDebt().getTblCompanyBalanceBankAccount().setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                 dataPasswordDeleteDebt.getEmployeeDebt().getTblCompanyBalanceBankAccount().setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class,ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                 dataPasswordDeleteDebt.getEmployeeDebt().getTblCompanyBalanceBankAccount().setRefRecordStatus(session.find(RefRecordStatus.class,1));
                 session.update(dataPasswordDeleteDebt.getEmployeeDebt().getTblCompanyBalanceBankAccount());
                }
            }
         employeeDebt.setLastUpdatedDate(Timestamp.valueOf(LocalDateTime.now()));
         employeeDebt.setTblEmployeeByLastUpdatedBy(session.find(TblEmployee.class,ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
         employeeDebt.setRefRecordStatus(session.find(RefRecordStatus.class,2));
         session.update(employeeDebt);
         session.getTransaction().commit();
        }
      catch(Exception e){
           if(session.getTransaction().isActive()){
             session.getTransaction().rollback();
             return false;
           }
        }
      }else{
          return false;
      }
      return true;
    }
    
    /*public boolean deleteDataDebtApproved(ClassDataPasswordDeleteDebt dataPasswordDeleteDebt){
       errMessage = "";
       try{
            session = HibernateUtil.getSessionFactory().getCurrentSession();
            session.beginTransaction();
            dataPasswordDeleteDebt.getEmployeeDebt().getTblCompanyBalance().setBalanceNominal(dataPasswordDeleteDebt.getEmployeeDebt().getTblCompanyBalance().getBalanceNominal()+dataPasswordDeleteDebt.getEmployeeDebt().getEmployeeDebtNominal());
            dataPasswordDeleteDebt.getEmployeeDebt().getTblCompanyBalance().setCreateDate(Timestamp.valueOf(LocalDateTime.now()));
            dataPasswordDeleteDebt.getEmployeeDebt().getTblCompanyBalance().setTblEmployeeByCreateBy(session.find(TblEmployee.class,ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
            dataPasswordDeleteDebt.getEmployeeDebt().getTblCompanyBalance().setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
            dataPasswordDeleteDebt.getEmployeeDebt().getTblCompanyBalance().setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class,ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
            dataPasswordDeleteDebt.getEmployeeDebt().getTblCompanyBalance().setRefRecordStatus(session.find(RefRecordStatus.class,1));
            session.update(dataPasswordDeleteDebt.getEmployeeDebt().getTblCompanyBalance());
           if(dataPasswordDeleteDebt.getEmployeeDebt().getTblCompanyBalance().getIdbalance()==1){
             dataPasswordDeleteDebt.getEmployeeDebt().getTblCompanyBalanceBankAccount().setCompanyBalanceBankAccountNominal(dataPasswordDeleteDebt.getEmployeeDebt().getTblCompanyBalanceBankAccount().getCompanyBalanceBankAccountNominal()+dataPasswordDeleteDebt.getEmployeeDebt().getEmployeeDebtNominal());
             dataPasswordDeleteDebt.getEmployeeDebt().getTblCompanyBalanceBankAccount().setCreateDate(Timestamp.valueOf(LocalDateTime.now()));
             dataPasswordDeleteDebt.getEmployeeDebt().getTblCompanyBalanceBankAccount().setTblEmployeeByCreateBy(session.find(TblEmployee.class,ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
             dataPasswordDeleteDebt.getEmployeeDebt().getTblCompanyBalanceBankAccount().setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
             dataPasswordDeleteDebt.getEmployeeDebt().getTblCompanyBalanceBankAccount().setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class,ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
             dataPasswordDeleteDebt.getEmployeeDebt().getTblCompanyBalanceBankAccount().setRefRecordStatus(session.find(RefRecordStatus.class,1));
             session.update(dataPasswordDeleteDebt.getEmployeeDebt().getTblCompanyBalanceBankAccount());
           }
         dataPasswordDeleteDebt.getEmployeeDebt().setEmployeeDebtStatus("Ditolak");
         dataPasswordDeleteDebt.getEmployeeDebt().setCreatedDate(Timestamp.valueOf(LocalDateTime.now()));
         dataPasswordDeleteDebt.getEmployeeDebt().setTblEmployeeByCreatedBy(session.find(TblEmployee.class,ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
         dataPasswordDeleteDebt.getEmployeeDebt().setLastUpdatedDate(Timestamp.valueOf(LocalDateTime.now()));
         dataPasswordDeleteDebt.getEmployeeDebt().setTblEmployeeByLastUpdatedBy(session.find(TblEmployee.class,ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
         dataPasswordDeleteDebt.getEmployeeDebt().setRefRecordStatus(session.find(RefRecordStatus.class,2));
         session.update(dataPasswordDeleteDebt.getEmployeeDebt());
         session.getTransaction().commit();
       }
       catch(Exception e){
           if(session.getTransaction().isActive()){
             session.getTransaction().rollback();
           }
          errMessage = e.getMessage();
          return false;
       }
       return true;
    }*/
    
    @Override
    public List<TblCalendarEmployeeDebt>getAllDataDebt(){
       errMessage = "";
       List<TblCalendarEmployeeDebt>list = new ArrayList();
       if (ClassSession.checkUserSession()) {  //check user current session
       try{
         session = HibernateUtil.getSessionFactory().getCurrentSession();
         session.beginTransaction();
         list = session.getNamedQuery("findAllTblCalendarEmployeeDebt").list();
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
    
    public List<TblCalendarEmployeeDebt>getAllDataDebtByIdEmployee(TblEmployee employee){
       errMessage = "";
       List<TblCalendarEmployeeDebt>list = new ArrayList();
       if (ClassSession.checkUserSession()) {  //check user current session
            try{
              session = HibernateUtil.getSessionFactory().getCurrentSession();
              session.beginTransaction();
              list = session.getNamedQuery("findAllTblCalendarEmployeeDebtByIdEmployee").setParameter("idEmployee",employee.getIdemployee()).list();
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
    
    public List<TblCalendarEmployeePaymentDebtHistory>getAllDataEmployeePaymentDebtByIdEmployee(TblEmployee employee){
       errMessage = "";
       List<TblCalendarEmployeePaymentDebtHistory>list = new ArrayList();
       if (ClassSession.checkUserSession()) {  //check user current session
            try{
              session = HibernateUtil.getSessionFactory().getCurrentSession();
              session.beginTransaction();
              list = session.getNamedQuery("findAllTblCalendarEmployeePaymentDebtHistoryByIdEmployee").setParameter("idEmployee",employee.getIdemployee()).list();
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
    public List<RefEmployeeType> getAllEmployeeType(){
       errMessage = ""; 
       List<RefEmployeeType>list = new ArrayList();
       if (ClassSession.checkUserSession()) {  //check user current session
       try{
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
    public List<TblEmployee>getAllEmployeeName(RefEmployeeType employeeType){
       errMessage = "";
       List<TblEmployee>list = new ArrayList();
       if (ClassSession.checkUserSession()) {  //check user current session
       try{
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
    public List<TblCompanyBalance>getAllCompanyBalance(){
       errMessage = "";
       List<TblCompanyBalance>list = new ArrayList();
       if (ClassSession.checkUserSession()) {  //check user current session
       try{
         session = HibernateUtil.getSessionFactory().getCurrentSession();
         session.beginTransaction();
         list = session.getNamedQuery("findAllTblCompanyBalance").list();
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
    public List<TblCompanyBalanceBankAccount>getAllCompanyBalanceBankAccount(TblCompanyBalance companyBalance){
       errMessage = "";
       List<TblCompanyBalanceBankAccount>list = new ArrayList();
       if (ClassSession.checkUserSession()) {  //check user current session
       try{
         session = HibernateUtil.getSessionFactory().getCurrentSession();
         session.beginTransaction();
         list = session.getNamedQuery("findAllTblCompanyBalanceBankAccountByIDCompanyBalance").setParameter("idCompanyBalance",companyBalance.getIdbalance()).list();
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
    public List<TblCalendarEmployeePaymentHistory>getAllEmployeePaymentDebt(){
       errMessage = "";
       List<TblCalendarEmployeePaymentHistory>list = new ArrayList();
       if (ClassSession.checkUserSession()) {  //check user current session
       try{
         session = HibernateUtil.getSessionFactory().getCurrentSession();
         session.beginTransaction();
         list = session.getNamedQuery("findAllTblCalendarEmployeePaymentHistory").list();
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
    public List<TblCalendarEmployeePaymentDebtHistory>getAllEmployeePaymentDebtByIdEmployeeDebt(TblCalendarEmployeeDebt employeeDebt){
        errMessage = "";
        List<TblCalendarEmployeePaymentDebtHistory>list = new ArrayList();
        if (ClassSession.checkUserSession()) {  //check user current session
        try{
          session = HibernateUtil.getSessionFactory().getCurrentSession();
          session.beginTransaction();
          list = session.getNamedQuery("findAllTblCalendarEmployeePaymentDebtHistoryByIdEmployeeDebt").setParameter("idEmployeeDebt",employeeDebt.getIdemployeeDebt()).list();
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
    public List<TblCalendarEmployeePaymentHistory>getAllEmployeePaymentByIdEmployee(TblEmployee employee){
        errMessage = "";
        List<TblCalendarEmployeePaymentHistory>list = new ArrayList();
        if (ClassSession.checkUserSession()) {  //check user current session
        try{
          session = HibernateUtil.getSessionFactory().getCurrentSession();
          session.beginTransaction();
          list = session.getNamedQuery("findAllTblCalendarEmployeePaymentHistoryByIdEmployee").setParameter("idEmployee",employee.getIdemployee()).list();
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
    public List<RefFinanceTransactionPaymentType>getAllPaymentType(){
        errMessage = "";
        List<RefFinanceTransactionPaymentType>list = new ArrayList();
        if (ClassSession.checkUserSession()) {  //check user current session
        try{
          session = HibernateUtil.getSessionFactory().getCurrentSession();
          session.beginTransaction();
          list = session.getNamedQuery("findAllRefFinanceTransactionPaymentType").list();
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

    //--------------------------------------------------------------------------
    
    public boolean insertBankAccountSender(TblEmployeeBankAccount employeeBankAccount){
      errMessage = "";
      TblEmployeeBankAccount data = employeeBankAccount;
      if(ClassSession.checkUserSession()){
         try{
           session = HibernateUtil.getSessionFactory().getCurrentSession();
           session.beginTransaction();
           data.setTblBankAccount(data.getTblBankAccount());
           data.getTblBankAccount().setRefRecordStatus(session.find(RefRecordStatus.class,1));
           data.getTblBankAccount().setTblEmployeeByCreateBy(session.find(TblEmployee.class,ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
           data.getTblBankAccount().setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class,ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
           data.getTblBankAccount().setCreateDate(Timestamp.valueOf(LocalDateTime.now()));
           data.getTblBankAccount().setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
           session.save(data.getTblBankAccount());
           data.setCreateDate(Timestamp.valueOf(LocalDateTime.now()));
           data.setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
           data.setRefRecordStatus(session.find(RefRecordStatus.class,1));
           data.setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class,ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
           data.setTblEmployeeByCreateBy(session.find(TblEmployee.class,ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
           session.save(data);
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
    
      @Override
    public List<TblEmployeeBankAccount>getAllBankAccountSender(TblEmployee employee){
        errMessage = "";
        List<TblEmployeeBankAccount>list = new ArrayList();
        if (ClassSession.checkUserSession()) {  //check user current session
        try{
          session = HibernateUtil.getSessionFactory().getCurrentSession();
          session.beginTransaction();
          list = session.getNamedQuery("findAllTblEmployeeBankAccountByIDEmployee").setParameter("idEmployee",employee.getIdemployee()).list();
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
    public List<TblBank>getAllDataBank(){
        errMessage = "";
        List<TblBank>list = new ArrayList();
        if (ClassSession.checkUserSession()) {  //check user current session
        try{
          session = HibernateUtil.getSessionFactory().getCurrentSession();
          session.beginTransaction();
          list = session.getNamedQuery("findAllTblBank").list();
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
    public boolean checkPassword(String password){
       //List<SysPasswordDeleteDebt>list = new ArrayList();
       errMessage = "";
       String passwordCheck = "";
       boolean check = false;
       if (ClassSession.checkUserSession()) {  //check user current session
       try{
         session = HibernateUtil.getSessionFactory().getCurrentSession();
         session.beginTransaction();
         Query q = session.getNamedQuery("checkOldPassword").setParameter("password",password);
         passwordCheck = (String)q.uniqueResult();
         if(passwordCheck.equals("exist")){
           check = true;  
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
       return check;
    }
    
    @Override
    public TblCalendarEmployeeDebt getEmployeeDebt(long id){
       errMessage = "";
       TblCalendarEmployeeDebt data = null;
       if (ClassSession.checkUserSession()) {  //check user current session
       try{
         session = HibernateUtil.getSessionFactory().getCurrentSession();
         session.beginTransaction();
         data = session.find(TblCalendarEmployeeDebt.class, id);
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
    public TblCompanyBalance getCompanyBalance(long id){
       errMessage = "";
       TblCompanyBalance data = null;
       if (ClassSession.checkUserSession()) {  //check user current session
           try{
              session = HibernateUtil.getSessionFactory().getCurrentSession();
              session.beginTransaction();
              data = session.find(TblCompanyBalance.class, id);
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
    public String getErrorMessage(){
       return errMessage; 
    }
}
