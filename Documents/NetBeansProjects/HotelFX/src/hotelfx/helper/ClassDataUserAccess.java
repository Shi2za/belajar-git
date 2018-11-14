/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hotelfx.helper;

import hotelfx.persistence.model.RefRecordStatus;
import hotelfx.persistence.model.TblSystemRoleSystemFeature;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;

/**
 *
 * @author Andreas
 */
public class ClassDataUserAccess implements Serializable{
   private final ObjectProperty<TblSystemRoleSystemFeature> systemRoleFeature;
   private final BooleanProperty isView;
   private final BooleanProperty isCreate;
   private final BooleanProperty isUpdate;
   private final BooleanProperty isApprove;
   private final BooleanProperty isDelete;
   private final BooleanProperty isPrint;
   private List<TblSystemRoleSystemFeature>updateRole;

   public ClassDataUserAccess() {
   this.systemRoleFeature = new SimpleObjectProperty<>();
   this.isView = new SimpleBooleanProperty();
   this.isCreate = new SimpleBooleanProperty();
   this.isUpdate = new SimpleBooleanProperty();
   this.isApprove = new SimpleBooleanProperty();
   this.isDelete = new SimpleBooleanProperty();
   this.isPrint = new SimpleBooleanProperty();
   this.updateRole = new ArrayList();
   }

  public ClassDataUserAccess(TblSystemRoleSystemFeature systemRoleFeature, boolean isView, boolean isCreate, boolean isUpdate, boolean isApprove, boolean isDelete, boolean isPrint,List<TblSystemRoleSystemFeature>dataUpdateRole) 
  {
    this();
    systemRoleFeatureProperty().set(systemRoleFeature);
    isViewProperty().set(isView);
    isCreateProperty().set(isCreate);
    isUpdateProperty().set(isUpdate);
    isApproveProperty().set(isApprove);
    isDeleteProperty().set(isDelete);
    isPrintProperty().set(isPrint);
    
    isViewProperty().addListener((obs,oldVal,newVal)->{
    boolean found = checkRoleUpdate(dataUpdateRole,systemRoleFeature);
    if(newVal==true)
    {
      if(found == false)
      {
        systemRoleFeature.setRefRecordStatus(new RefRecordStatus(1));
        dataUpdateRole.add(systemRoleFeature);
        System.out.println("DATA UPDATE:"+dataUpdateRole.size());
      }
      else
      {
          systemRoleFeature.setRefRecordStatus(new RefRecordStatus(1));
//        for(TblSystemRoleSystemFeature getDataUpdateRole : dataUpdateRole)
//        {
//          getDataUpdateRole.setRefRecordStatus(new RefRecordStatus(1));
//        }
      }
    }
    else
    {
      if(found==false)
      {
        systemRoleFeature.setRefRecordStatus(new RefRecordStatus(2));   
        dataUpdateRole.add(systemRoleFeature);
        System.out.println("DATA UPDATE:"+dataUpdateRole.size());
      }
      else
      {
          systemRoleFeature.setRefRecordStatus(new RefRecordStatus(2));
//        for(TblSystemRoleSystemFeature getDataUpdateRole : dataUpdateRole)
//        {
//          getDataUpdateRole.setRefRecordStatus(new RefRecordStatus(2));
//        }
      }
      
      setIsCreate(false);
      setIsUpdate(false);
      setIsApprove(false);
      setIsDelete(false);
      setIsPrint(false);
    }
    //set unsaving data input -> 'true'
    ClassSession.unSavingDataInput.set(true);
  });
    
  isCreateProperty().addListener((obs,oldVal,newVal)->{
      boolean found = checkRoleUpdate(dataUpdateRole,systemRoleFeature);
      if(newVal==true)
     { 
       if(found == false)
      {
        systemRoleFeature.setRefRecordStatus(new RefRecordStatus(1));
        systemRoleFeature.setCreateData(Boolean.TRUE);
        dataUpdateRole.add(systemRoleFeature);
        System.out.println("DATA UPDATE:"+dataUpdateRole.size());
      }
      else
      {
          systemRoleFeature.setCreateData(Boolean.TRUE);
//        for(TblSystemRoleSystemFeature getDataUpdateRole : dataUpdateRole)
//        {
//          getDataUpdateRole.setCreateData(Boolean.TRUE);
//        }
      }
      setIsView(true);
     }
     else
     {
       if(found == false)
       {
        systemRoleFeature.setCreateData(Boolean.FALSE);
        dataUpdateRole.add(systemRoleFeature);
        System.out.println("DATA UPDATE:"+dataUpdateRole.size());
       }
      else
      {
          systemRoleFeature.setCreateData(Boolean.FALSE);
//        for(TblSystemRoleSystemFeature getDataUpdateRole : dataUpdateRole)
//        {
//          getDataUpdateRole.setCreateData(Boolean.FALSE);
//        }
      }
     }
      //set unsaving data input -> 'true'
      ClassSession.unSavingDataInput.set(true);
  });
  
  isUpdateProperty().addListener((obs,oldVal,newVal)->{
     boolean found = checkRoleUpdate(dataUpdateRole,systemRoleFeature);
      if(newVal==true)
     { 
       if(found == false)
      {
        systemRoleFeature.setRefRecordStatus(new RefRecordStatus(1));
        systemRoleFeature.setUpdateData(Boolean.TRUE);
        dataUpdateRole.add(systemRoleFeature);
        System.out.println("DATA UPDATE:"+dataUpdateRole.size());
      }
      else
      {
          systemRoleFeature.setUpdateData(Boolean.TRUE);
//        for(TblSystemRoleSystemFeature getDataUpdateRole : dataUpdateRole)
//        {
//          getDataUpdateRole.setUpdateData(Boolean.TRUE);
//        }
      }
      setIsView(true);
     }
     else
     {
       if(found == false)
       {
        systemRoleFeature.setUpdateData(Boolean.FALSE);
        dataUpdateRole.add(systemRoleFeature);
        System.out.println("DATA UPDATE:"+dataUpdateRole.size());
       }
      else
      {
          systemRoleFeature.setUpdateData(Boolean.FALSE);
//        for(TblSystemRoleSystemFeature getDataUpdateRole : dataUpdateRole)
//        {
//          getDataUpdateRole.setUpdateData(Boolean.FALSE);
//        }
      }
     }
      //set unsaving data input -> 'true'
      ClassSession.unSavingDataInput.set(true);
  });
  
  isApproveProperty().addListener((obs,oldVal,newVal)->{
      boolean found = checkRoleUpdate(dataUpdateRole,systemRoleFeature);
      if(newVal==true)
     { 
       if(found == false)
      {
        systemRoleFeature.setRefRecordStatus(new RefRecordStatus(1));
        systemRoleFeature.setApproveData(Boolean.TRUE);
        dataUpdateRole.add(systemRoleFeature);
        System.out.println("DATA UPDATE:"+dataUpdateRole.size());
      }
      else
      {
          systemRoleFeature.setApproveData(Boolean.TRUE);
//        for(TblSystemRoleSystemFeature getDataUpdateRole : dataUpdateRole)
//        {
//          getDataUpdateRole.setApproveData(Boolean.TRUE);
//        }
      }
      setIsView(true);
     }
     else
     {
       if(found == false)
       {
        systemRoleFeature.setApproveData(Boolean.FALSE);
        dataUpdateRole.add(systemRoleFeature);
        System.out.println("DATA UPDATE:"+dataUpdateRole.size());
       }
      else
      {
          systemRoleFeature.setApproveData(Boolean.FALSE);
//        for(TblSystemRoleSystemFeature getDataUpdateRole : dataUpdateRole)
//        {
//          getDataUpdateRole.setApproveData(Boolean.FALSE);
//        }
      }
     }
      //set unsaving data input -> 'true'
      ClassSession.unSavingDataInput.set(true);
  });
  
  isDeleteProperty().addListener((obs,oldVal,newVal)->{
     boolean found = checkRoleUpdate(dataUpdateRole,systemRoleFeature);
      if(newVal==true)
     { 
       if(found == false)
      {
        systemRoleFeature.setRefRecordStatus(new RefRecordStatus(1));
        systemRoleFeature.setDeleteData(Boolean.TRUE);
        dataUpdateRole.add(systemRoleFeature);
        System.out.println("DATA UPDATE:"+dataUpdateRole.size());
      }
      else
      {
          systemRoleFeature.setDeleteData(Boolean.TRUE);
//        for(TblSystemRoleSystemFeature getDataUpdateRole : dataUpdateRole)
//        {
//          getDataUpdateRole.setDeleteData(Boolean.TRUE);
//        }
      }
      setIsView(true);
     }
     else
     {
       if(found == false)
       {
        systemRoleFeature.setDeleteData(Boolean.FALSE);
        dataUpdateRole.add(systemRoleFeature);
        System.out.println("DATA UPDATE:"+dataUpdateRole.size());
       }
      else
      {
          systemRoleFeature.setDeleteData(Boolean.FALSE);
//        for(TblSystemRoleSystemFeature getDataUpdateRole : dataUpdateRole)
//        {
//          getDataUpdateRole.setDeleteData(Boolean.FALSE);
//        }
      }
     }
      //set unsaving data input -> 'true'
      ClassSession.unSavingDataInput.set(true);
  });
  
  isPrintProperty().addListener((obs,oldVal,newVal)->{
     boolean found = checkRoleUpdate(dataUpdateRole,systemRoleFeature);
      if(newVal==true)
     { 
       if(found == false)
      {
        systemRoleFeature.setRefRecordStatus(new RefRecordStatus(1));
        systemRoleFeature.setPrintData(Boolean.TRUE);
        dataUpdateRole.add(systemRoleFeature);
        System.out.println("DATA UPDATE:"+dataUpdateRole.size());
      }
      else
      {
          systemRoleFeature.setPrintData(Boolean.TRUE);
//        for(TblSystemRoleSystemFeature getDataUpdateRole : dataUpdateRole)
//        {
//          getDataUpdateRole.setPrintData(Boolean.TRUE);
//        }
      }
      setIsView(true);
     }
     else
     {
       if(found == false)
       {
        systemRoleFeature.setPrintData(Boolean.FALSE);
        dataUpdateRole.add(systemRoleFeature);
        System.out.println("DATA UPDATE:"+dataUpdateRole.size());
       }
      else
      {
          systemRoleFeature.setPrintData(Boolean.FALSE);
//        for(TblSystemRoleSystemFeature getDataUpdateRole : dataUpdateRole)
//        {
//          getDataUpdateRole.setPrintData(Boolean.FALSE);
//        }
      }
     }
      //set unsaving data input -> 'true'
      ClassSession.unSavingDataInput.set(true);
  });
  /*isUpdateProperty().addListener((obs,oldVal,newVal)->{
     if(newVal==true)
     {
       setIsView(true);
       if(dataUpdateRole.size()==0)
       {
         for(TblSystemRoleSystemFeature getDataUpdateRole : dataUpdateRole)
         {
           getDataUpdateRole.setUpdateData(Boolean.TRUE);
         }
       }
       else
       {
         for(int i = 0; i<dataUpdateRole.size();i++)
         {
          TblSystemRoleSystemFeature getDataUpdateRole = dataUpdateRole.get(i);
          if(getDataUpdateRole.getTblSystemFeature().getIdfeature()==systemRoleFeature.getTblSystemFeature().getIdfeature())
          {
           getDataUpdateRole.setUpdateData(Boolean.TRUE);
           break;
          }
        }
       }
     }
     else
     {
       for(int i = 0; i<dataUpdateRole.size();i++)
        {
          TblSystemRoleSystemFeature getDataUpdateRole = dataUpdateRole.get(i);
          if(getDataUpdateRole.getTblSystemFeature().getIdfeature()==systemRoleFeature.getTblSystemFeature().getIdfeature())
          {
            getDataUpdateRole.setUpdateData(Boolean.FALSE);
          }
        }
     }
  });
  
  isDeleteProperty().addListener((obs,oldVal,newVal)->{
     if(newVal==true)
     {
       setIsView(true);
       if(dataUpdateRole.size()==0)
       {
         for(TblSystemRoleSystemFeature getDataUpdateRole : dataUpdateRole)
         {
           getDataUpdateRole.setDeleteData(Boolean.TRUE);
         }
       }
       else
       {
         for(int i = 0; i<dataUpdateRole.size();i++)
         {
          TblSystemRoleSystemFeature getDataUpdateRole = dataUpdateRole.get(i);
          if(getDataUpdateRole.getTblSystemFeature().getIdfeature()==systemRoleFeature.getTblSystemFeature().getIdfeature())
          {
           getDataUpdateRole.setDeleteData(Boolean.TRUE);
           break;
          }
        }
       }
     }
     else
     {
       for(int i = 0; i<dataUpdateRole.size();i++)
        {
          TblSystemRoleSystemFeature getDataUpdateRole = dataUpdateRole.get(i);
          if(getDataUpdateRole.getTblSystemFeature().getIdfeature()==systemRoleFeature.getTblSystemFeature().getIdfeature())
          {
            getDataUpdateRole.setDeleteData(Boolean.FALSE);
          }
        }
     }
  });
  
  isPrintProperty().addListener((obs,oldVal,newVal)->{
     if(newVal==true)
     {
       setIsView(true);
       if(dataUpdateRole.size()==0)
       {
         for(TblSystemRoleSystemFeature getDataUpdateRole : dataUpdateRole)
         {
           getDataUpdateRole.setPrintData(Boolean.TRUE);
         }
       }
       else
       {
         for(int i = 0; i<dataUpdateRole.size();i++)
         {
          TblSystemRoleSystemFeature getDataUpdateRole = dataUpdateRole.get(i);
          if(getDataUpdateRole.getTblSystemFeature().getIdfeature()==systemRoleFeature.getTblSystemFeature().getIdfeature())
          {
           getDataUpdateRole.setPrintData(Boolean.TRUE);
           break;
          }
        }
       }
     }
     else
     {
       for(int i = 0; i<dataUpdateRole.size();i++)
        {
          TblSystemRoleSystemFeature getDataUpdateRole = dataUpdateRole.get(i);
          if(getDataUpdateRole.getTblSystemFeature().getIdfeature()==systemRoleFeature.getTblSystemFeature().getIdfeature())
          {
            getDataUpdateRole.setPrintData(Boolean.FALSE);
          }
        }
     }
  });*/
  
  this.updateRole = dataUpdateRole;
}

  public boolean checkRoleUpdate(List<TblSystemRoleSystemFeature>dataUpdateRole,TblSystemRoleSystemFeature systemRoleFeature){
      boolean found = false;
      
        for(int i = 0; i<dataUpdateRole.size();i++)
        {
         if(dataUpdateRole.get(i).getTblSystemFeature().getIdfeature()
            == systemRoleFeature.getTblSystemFeature().getIdfeature())
         {
           found = true;
           System.out.println("Hasil found:"+found);
         } 
      }        
      return found;
  }
public final ObjectProperty<TblSystemRoleSystemFeature> systemRoleFeatureProperty() {
	return this.systemRoleFeature;
}

public TblSystemRoleSystemFeature getSystemRoleFeature() {
	return systemRoleFeatureProperty().get();
}

public void setSystemRoleFeature(TblSystemRoleSystemFeature systemRoleFeature) {
	systemRoleFeatureProperty().set(systemRoleFeature);
}

public final BooleanProperty isViewProperty() {
	return this.isView;
}

public boolean getIsView() {
	return isViewProperty().get();
}

public void setIsView(boolean isView) {
	isViewProperty().set(isView);
}

public final BooleanProperty isCreateProperty() {
	return this.isCreate;
}

public boolean getIsCreate() {
	return isCreateProperty().get();
}

public void setIsCreate(boolean isCreate) {
	isCreateProperty().set(isCreate);
}

public final BooleanProperty isUpdateProperty() {
	return this.isUpdate;
}

public boolean getIsUpdate() {
	return isUpdateProperty().get();
}

public void setIsUpdate(boolean isUpdate) {
	isUpdateProperty().set(isUpdate);
}

public final BooleanProperty isApproveProperty() {
	return this.isApprove;
}

public boolean getIsApprove() {
	return isApproveProperty().get();
}

public void setIsApprove(boolean isApprove) {
	isApproveProperty().set(isApprove);
}

public final BooleanProperty isDeleteProperty() {
	return this.isDelete;
}

public boolean getIsDelete() {
	return isDeleteProperty().get();
}

public void setIsDelete(boolean isDelete) {
	isDeleteProperty().set(isDelete);
}

public final BooleanProperty isPrintProperty() {
	return this.isPrint;
}

public boolean getIsPrint() {
	return isPrintProperty().get();
}

public void setIsPrint(boolean isPrint) {
	isPrintProperty().set(isPrint);
}

 public List<TblSystemRoleSystemFeature> getUpdateRole() {
        return updateRole;
    }

    public void setUpdateRole(List<TblSystemRoleSystemFeature> updateRole) {
        this.updateRole = updateRole;
    }
}
