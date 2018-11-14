/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hotelfx.persistence.service;

import hotelfx.persistence.model.RefBankAccountHolderStatus;
import hotelfx.persistence.model.RefCountry;
import hotelfx.persistence.model.RefCustomerStatus;
import hotelfx.persistence.model.RefCustomerType;
import hotelfx.persistence.model.RefPeopleGender;
import hotelfx.persistence.model.RefPeopleIdentifierType;
import hotelfx.persistence.model.RefPeopleReligion;
import hotelfx.persistence.model.RefPeopleStatus;
import hotelfx.persistence.model.SysDataHardCode;
import hotelfx.persistence.model.TblBank;
import hotelfx.persistence.model.TblBankAccount;
import hotelfx.persistence.model.TblCustomer;
import hotelfx.persistence.model.TblCustomerBankAccount;
import hotelfx.persistence.model.TblPeople;
import java.sql.Date;
import java.util.List;

/**
 *
 * @author ANDRI
 */
public interface FCustomerManager {
    
    public TblCustomer insertDataCustomer(TblCustomer customer, 
            String imgExtention,
            List<TblCustomerBankAccount> customerBankAccount);
    
    public boolean updateDataCustomer(TblCustomer customer, 
            String imgExtention,
            List<TblCustomerBankAccount> customerBankAccount);
    
    public boolean deleteDataCustomer(TblCustomer customer);
    
    public TblCustomer getCustomer(long id);
    
    public List<TblCustomer> getAllDataCustomer();
    
    //--------------------------------------------------------------------------
    
    public TblPeople getPeople(long id);
    
    public List<TblPeople> getAllDataPeople();
    
    //--------------------------------------------------------------------------
    
    public RefCustomerType getCustomerType(int id);
    
    public List<RefCustomerType> getAllDataCustomerType();
    
    //--------------------------------------------------------------------------
    
    public RefCustomerStatus getCustomerStatus(int id);
    
    public List<RefCustomerStatus> getAllDataCustomerStatus();
    
    //--------------------------------------------------------------------------
    
    public RefPeopleIdentifierType getPeopleIdentifierType(int id);
    
    public List<RefPeopleIdentifierType> getAllDataPeopleIdentifierType();
    
    //--------------------------------------------------------------------------
    
    public RefPeopleGender getPeopleGender(int id);
    
    public List<RefPeopleGender> getAllDataPeopleGender();
    
    //--------------------------------------------------------------------------
    
    public RefPeopleReligion getPeopleReligion(int id);
    
    public List<RefPeopleReligion> getAllDataPeopleReligion();
    
    //--------------------------------------------------------------------------
    
    public RefPeopleStatus getPeopleStatus(int id);
    
    public List<RefPeopleStatus> getAllDataPeopleStatus();
    
    //--------------------------------------------------------------------------
    
    public RefCountry getDataPeopleCountry(int id);
    
    public List<RefCountry> getAllDataPeopleCountry();
    
    //--------------------------------------------------------------------------
    
    public TblCustomerBankAccount getCustomerBankAccount(long idCustpmerBakAccount);
    
    public List<TblCustomerBankAccount> getAllDataCustomerBankAccount(long idCustomer);
    
    //--------------------------------------------------------------------------
    
    public TblBankAccount getBankAccount(long idBankAccount);
    
    public List<TblBankAccount> getAllDataBankAccount();
    
    //--------------------------------------------------------------------------
    
    public RefBankAccountHolderStatus getBankAccountHolderStatus(int id);
    
    public List<RefBankAccountHolderStatus> getAllDataBankAccountHolderStatus();
    
    //--------------------------------------------------------------------------
    
    public TblBank getDataBank(long id);
    
    public List<TblBank> getAllDataBank();
    
    //--------------------------------------------------------------------------
    
   public List<TblCustomer>getAllDataCustomerForPrint(Date startDate,Date endDate,RefCustomerType customerType,TblCustomer customerName,RefCountry country);
    
   //--------------------------------------------------------------------------
    public SysDataHardCode getDataSysDataHardCode(long id);
   
    //--------------------------------------------------------------------------
    public String getErrorMessage();
    
}
