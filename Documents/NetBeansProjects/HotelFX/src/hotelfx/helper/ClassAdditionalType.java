/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hotelfx.helper;

import java.io.Serializable;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 *
 * @author Andreas
 */
public class ClassAdditionalType implements Serializable {
 
  private final IntegerProperty idType;
  private final StringProperty typeName;
    
   public ClassAdditionalType() {
     this.idType = new SimpleIntegerProperty();
     this.typeName = new SimpleStringProperty();
   }

  public ClassAdditionalType(int idType) {
     this();
     idTypeProperty().set(idType);
   }

  public ClassAdditionalType(int idType, String typeName) {
     this();
     idTypeProperty().set(idType);
     typeNameProperty().set(typeName);
   }
  
  public ClassAdditionalType(ClassAdditionalType classAdditionalType){
     this();
     idTypeProperty().set(classAdditionalType.getIdType());
     typeNameProperty().set(classAdditionalType.getTypeName());
  }
  
   public final IntegerProperty idTypeProperty() {
      return this.idType;
   }

    public int getIdType() {
      return idTypeProperty().get();
    }

    public void setIdType(int idType) {
            idTypeProperty().set(idType);
    }

    public final StringProperty typeNameProperty() {
            return this.typeName;
    }

    public String getTypeName() {
            return typeNameProperty().get();
    }

    public void setTypeName(String typeName) {
            typeNameProperty().set(typeName);
    }



   
   
}
