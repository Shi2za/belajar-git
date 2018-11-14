/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hotelfx.helper;

import hotelfx.persistence.model.TblLocation;
import hotelfx.persistence.model.TblLocationOfLaundry;
import hotelfx.persistence.model.TblLocationOfWarehouse;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 *
 * @author Andreas
 */
public class ClassLocationName {
    
    private final ObjectProperty<TblLocation> location;
    private final StringProperty codeLocation;
    private final StringProperty nameLocation;


    public ClassLocationName() {
    this.location = new SimpleObjectProperty<>();
    this.codeLocation = new SimpleStringProperty();
    this.nameLocation = new SimpleStringProperty();
    }

    public ClassLocationName(TblLocation location, String codeLocation, String nameLocation) {
    this();
    locationProperty().set(location);
    codeLocationProperty().set(codeLocation);
    nameLocationProperty().set(nameLocation);
    }

    public final ObjectProperty<TblLocation> locationProperty() {
            return this.location;
    }

    public TblLocation getLocation() {
            return locationProperty().get();
    }

    public void setLocation(TblLocation location) {
            locationProperty().set(location);
    }

    public final StringProperty codeLocationProperty() {
            return this.codeLocation;
    }

    public String getCodeLocation() {
            return codeLocationProperty().get();
    }

    public void setCodeLocation(String codeLocation) {
            codeLocationProperty().set(codeLocation);
    }

    public final StringProperty nameLocationProperty() {
            return this.nameLocation;
    }

    public String getNameLocation() {
            return nameLocationProperty().get();
    }

    public void setNameLocation(String nameLocation) {
            nameLocationProperty().set(nameLocation);
    }
    
    @Override
    public String toString(){
       return getNameLocation();
    }
}
