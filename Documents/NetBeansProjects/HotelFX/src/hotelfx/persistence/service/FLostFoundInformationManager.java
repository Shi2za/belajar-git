/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hotelfx.persistence.service;

import hotelfx.persistence.model.RefCountry;
import hotelfx.persistence.model.RefFounderType;
import hotelfx.persistence.model.TblCustomer;
import hotelfx.persistence.model.TblEmployee;
import hotelfx.persistence.model.TblFoundInformation;
import hotelfx.persistence.model.TblLostFoundInformationDetail;
import hotelfx.persistence.model.TblLostInformation;
import hotelfx.persistence.model.TblPeople;
import java.util.List;

/**
 *
 * @author Andreas
 */
public interface FLostFoundInformationManager {
   
  //LOST INFORMATION
   public boolean insertDataLostInformation(TblLostInformation tblLostInformation);
   public boolean insertDataPeopleInformation(TblPeople tblPeople);
   public boolean updateDataLostInformation(TblLostInformation tblLostInformation);
   public boolean deleteDataLostInformation(TblLostInformation tblLostInformation);
   public List<TblLostInformation>getAllDataLostInformation();
   public List<TblLostInformation>getAllDataLostInformationReturn();
   public TblLostInformation getDataLostInformation(long id);
   
   //FOUND INFORMATION
    public boolean insertDataFounderInformation(TblFoundInformation tblFoundInformation);
    public boolean updateDataFoundInformation(TblFoundInformation tblFoundInformation);
    public boolean deleteDataFoundInformation(TblFoundInformation tblFoundInformation);
    public List<RefFounderType>getAllDataFounderType();
    public List<TblFoundInformation>getAllDataFoundInformation();
    public List<TblFoundInformation>getAllDataFoundInformationReturn();
    public TblFoundInformation getDataFoundInformation(long id);
    
    //RETURN
     public boolean insertDataReturnInformation(TblLostFoundInformationDetail tblLostFoundInformationDetail);
     public List<TblLostFoundInformationDetail>getAllDataReturn();
   //-----------------------------------------------------------------------------
   public List<TblEmployee> getAllDataEmployee(long id);
   public List<TblPeople> getAllDataPeople();
   public TblPeople getDataPeople(long id);
  
   //public List<TblCustomer> getAllDataCustomer();
   public List<RefCountry>getAllDataCountry();
   
   //-----------------------------------------------------------
    public String getErrorMessage();

}
