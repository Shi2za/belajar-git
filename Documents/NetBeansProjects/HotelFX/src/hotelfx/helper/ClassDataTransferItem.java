/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hotelfx.helper;

import hotelfx.persistence.model.TblItemLocation;
import hotelfx.persistence.model.TblItemLocationItemExpiredDate;
import hotelfx.persistence.model.TblItemLocationPropertyBarcode;
import java.io.Serializable;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;

/**
 *
 * @author Andreas
 */
public class ClassDataTransferItem implements Serializable{
   private final ObjectProperty<TblItemLocation> tblItemLocation;
private final ObjectProperty<TblItemLocationPropertyBarcode> tblItemLocationPropertyBarcode;
private final ObjectProperty<TblItemLocationItemExpiredDate> tblItemLocationItemExpiredDate;


public ClassDataTransferItem() {
this.tblItemLocation = new SimpleObjectProperty<>();
this.tblItemLocationPropertyBarcode = new SimpleObjectProperty<>();
this.tblItemLocationItemExpiredDate = new SimpleObjectProperty<>();
}


public ClassDataTransferItem(TblItemLocation tblItemLocation, TblItemLocationPropertyBarcode tblItemLocationPropertyBarcode, TblItemLocationItemExpiredDate tblItemLocationItemExpiredDate) {
this();
tblItemLocationProperty().set(tblItemLocation);
tblItemLocationPropertyBarcodeProperty().set(tblItemLocationPropertyBarcode);
tblItemLocationItemExpiredDateProperty().set(tblItemLocationItemExpiredDate);
}

public final ObjectProperty<TblItemLocation> tblItemLocationProperty() {
	return this.tblItemLocation;
}

public TblItemLocation getTblItemLocation() {
	return tblItemLocationProperty().get();
}

public void setTblItemLocation(TblItemLocation tblItemLocation) {
	tblItemLocationProperty().set(tblItemLocation);
}

public final ObjectProperty<TblItemLocationPropertyBarcode> tblItemLocationPropertyBarcodeProperty() {
	return this.tblItemLocationPropertyBarcode;
}

public TblItemLocationPropertyBarcode getTblItemLocationPropertyBarcode() {
	return tblItemLocationPropertyBarcodeProperty().get();
}

public void setTblItemLocationPropertyBarcode(TblItemLocationPropertyBarcode tblItemLocationPropertyBarcode) {
	tblItemLocationPropertyBarcodeProperty().set(tblItemLocationPropertyBarcode);
}

public final ObjectProperty<TblItemLocationItemExpiredDate> tblItemLocationItemExpiredDateProperty() {
	return this.tblItemLocationItemExpiredDate;
}

public TblItemLocationItemExpiredDate getTblItemLocationItemExpiredDate() {
	return tblItemLocationItemExpiredDateProperty().get();
}

public void setTblItemLocationItemExpiredDate(TblItemLocationItemExpiredDate tblItemLocationItemExpiredDate) {
	tblItemLocationItemExpiredDateProperty().set(tblItemLocationItemExpiredDate);
}

}
