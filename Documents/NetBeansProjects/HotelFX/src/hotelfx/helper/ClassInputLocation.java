/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hotelfx.helper;

import hotelfx.persistence.model.TblLocationOfBin;
import hotelfx.persistence.model.TblLocationOfLaundry;
import hotelfx.persistence.model.TblLocationOfWarehouse;
import hotelfx.persistence.model.TblRoom;
import hotelfx.persistence.model.TblSupplier;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;

/**
 *
 * @author Andreas
 */
public class ClassInputLocation implements java.io.Serializable {
  private final ObjectProperty<TblLocationOfWarehouse> warehouse;
private final ObjectProperty<TblRoom> room;
private final ObjectProperty<TblLocationOfLaundry> laundry;
private final ObjectProperty<TblSupplier> supplier;
private final ObjectProperty<TblLocationOfBin> bin;


public ClassInputLocation() {
this.warehouse = new SimpleObjectProperty<>();
this.room = new SimpleObjectProperty<>();
this.laundry = new SimpleObjectProperty<>();
this.supplier = new SimpleObjectProperty<>();
this.bin = new SimpleObjectProperty<>();

}


public ClassInputLocation(TblLocationOfWarehouse warehouse, TblRoom room, TblLocationOfLaundry laundry, TblSupplier supplier, TblLocationOfBin bin) {
this();
warehouseProperty().set(warehouse);
roomProperty().set(room);
laundryProperty().set(laundry);
supplierProperty().set(supplier);
binProperty().set(bin);

}

public final ObjectProperty<TblLocationOfWarehouse> warehouseProperty() {
	return this.warehouse;
}

public TblLocationOfWarehouse getWarehouse() {
	return warehouseProperty().get();
}

public void setWarehouse(TblLocationOfWarehouse warehouse) {
	warehouseProperty().set(warehouse);
}

public final ObjectProperty<TblRoom> roomProperty() {
	return this.room;
}

public TblRoom getRoom() {
	return roomProperty().get();
}

public void setRoom(TblRoom room) {
	roomProperty().set(room);
}

public final ObjectProperty<TblLocationOfLaundry> laundryProperty() {
	return this.laundry;
}

public TblLocationOfLaundry getLaundry() {
	return laundryProperty().get();
}

public void setLaundry(TblLocationOfLaundry laundry) {
	laundryProperty().set(laundry);
}

public final ObjectProperty<TblSupplier> supplierProperty() {
	return this.supplier;
}

public TblSupplier getSupplier() {
	return supplierProperty().get();
}

public void setSupplier(TblSupplier supplier) {
	supplierProperty().set(supplier);
}

public final ObjectProperty<TblLocationOfBin> binProperty() {
	return this.bin;
}

public TblLocationOfBin getBin() {
	return binProperty().get();
}

public void setBin(TblLocationOfBin bin) {
	binProperty().set(bin);
}

//@Override
//public String toString(){
//  return getWarehouse().getWarehouseName();
//}
    
}
