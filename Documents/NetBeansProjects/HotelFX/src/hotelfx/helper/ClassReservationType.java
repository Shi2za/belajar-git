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
public class ClassReservationType {
 private final IntegerProperty idType;
 private final StringProperty reservationName;


    public ClassReservationType() {
    this.idType = new SimpleIntegerProperty();
    this.reservationName = new SimpleStringProperty();

    }

    public ClassReservationType(Integer idType) {
    this();
     idTypeProperty().set(idType);
    }

    public ClassReservationType(Integer idType, String reservationName) {
      this();
      idTypeProperty().set(idType);
      reservationNameProperty().set(reservationName);
    }
    
    public ClassReservationType(ClassReservationType classReservationType) {
      this();
      idTypeProperty().set(classReservationType.getIdType());
      reservationNameProperty().set(classReservationType.getReservationName());
    }
     
    public final IntegerProperty idTypeProperty() {
            return this.idType;
    }

    public Integer getIdType() {
            return idTypeProperty().get();
    }

    public void setIdType(Integer idType) {
            idTypeProperty().set(idType);
    }

    public final StringProperty reservationNameProperty() {
            return this.reservationName;
    }

    public String getReservationName() {
            return reservationNameProperty().get();
    }

    public void setReservationName(String reservationName) {
            reservationNameProperty().set(reservationName);
    }

    @Override
    public String toString(){
        return getReservationName();
    }
    

}
