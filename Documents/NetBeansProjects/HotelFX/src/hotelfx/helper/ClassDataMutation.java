/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hotelfx.helper;

import hotelfx.persistence.model.TblItemMutationHistory;
import hotelfx.persistence.model.TblItemMutationHistoryItemExpiredDate;
import hotelfx.persistence.model.TblItemMutationHistoryPropertyBarcode;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;

/**
 *
 * @author Andreas
 */
public class ClassDataMutation {
private final ObjectProperty<TblItemMutationHistory> mutationHistory;
private final ObjectProperty<TblItemMutationHistoryPropertyBarcode> mutationHistoryPropertyBarcode;
private final ObjectProperty<TblItemMutationHistoryItemExpiredDate> mutationHistoryItemExpiredDate;


public ClassDataMutation() {
this.mutationHistory = new SimpleObjectProperty<>();
this.mutationHistoryPropertyBarcode = new SimpleObjectProperty<>();
this.mutationHistoryItemExpiredDate = new SimpleObjectProperty<>();
}


public ClassDataMutation(
        TblItemMutationHistory mutationHistory, 
        TblItemMutationHistoryPropertyBarcode mutationHistoryPropertyBarcode, 
        TblItemMutationHistoryItemExpiredDate mutationHistoryItemExpiredDate) {
this();
mutationHistoryProperty().set(mutationHistory);
mutationHistoryPropertyBarcodeProperty().set(mutationHistoryPropertyBarcode);
mutationHistoryItemExpiredDateProperty().set(mutationHistoryItemExpiredDate);
}

public final ObjectProperty<TblItemMutationHistory> mutationHistoryProperty() {
	return this.mutationHistory;
}

public TblItemMutationHistory getMutationHistory() {
	return mutationHistoryProperty().get();
}

public void setMutationHistory(TblItemMutationHistory mutationHistory) {
	mutationHistoryProperty().set(mutationHistory);
}

public final ObjectProperty<TblItemMutationHistoryPropertyBarcode> mutationHistoryPropertyBarcodeProperty() {
	return this.mutationHistoryPropertyBarcode;
}

public TblItemMutationHistoryPropertyBarcode getMutationHistoryPropertyBarcode() {
	return mutationHistoryPropertyBarcodeProperty().get();
}

public void setMutationHistoryPropertyBarcode(TblItemMutationHistoryPropertyBarcode mutationHistoryPropertyBarcode) {
	mutationHistoryPropertyBarcodeProperty().set(mutationHistoryPropertyBarcode);
}

public final ObjectProperty<TblItemMutationHistoryItemExpiredDate> mutationHistoryItemExpiredDateProperty() {
	return this.mutationHistoryItemExpiredDate;
}

public TblItemMutationHistoryItemExpiredDate getMutationHistoryItemExpiredDate() {
	return mutationHistoryItemExpiredDateProperty().get();
}

public void setMutationHistoryItemExpiredDate(TblItemMutationHistoryItemExpiredDate mutationHistoryItemExpiredDate) {
	mutationHistoryItemExpiredDateProperty().set(mutationHistoryItemExpiredDate);
}

}
