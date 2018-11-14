/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hotelfx.helper;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 *
 * @author Andreas
 */
public class ClassDataDebtStatus {
    
   private final IntegerProperty idStatus;
   private final StringProperty statusName;


   public ClassDataDebtStatus() {
   this.idStatus = new SimpleIntegerProperty();
   this.statusName = new SimpleStringProperty();
   }

   public ClassDataDebtStatus(int idStatus, String statusName) {
   this();
   idStatusProperty().set(idStatus);
   statusNameProperty().set(statusName);

   }

   public final IntegerProperty idStatusProperty() {
           return this.idStatus;
   }

   public int getIdStatus() {
           return idStatusProperty().get();
   }

   public void setIdStatus(int idStatus) {
           idStatusProperty().set(idStatus);
   }

   public final StringProperty statusNameProperty() {
           return this.statusName;
   }

   public String getStatusName() {
           return statusNameProperty().get();
   }

   public void setStatusName(String statusName) {
           statusNameProperty().set(statusName);
   }

}
