package hotelfx.persistence.model;
// Generated Sep 7, 2017 1:18:06 PM by Hibernate Tools 4.3.1

import java.util.HashSet;
import java.util.Set;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 * RefReservationBillDiscountType generated by hbm2java
 */
public class RefReservationBillDiscountType implements java.io.Serializable {

    private final IntegerProperty idtype;
    private final StringProperty typeName;
    private final StringProperty typeNote;

    private Set tblReservationBills;

    public RefReservationBillDiscountType() {
        this.idtype = new SimpleIntegerProperty();
        this.typeName = new SimpleStringProperty();
        this.typeNote = new SimpleStringProperty();

        this.tblReservationBills = new HashSet(0);
    }

    public RefReservationBillDiscountType(int idtype) {
        this();
        idtypeProperty().set(idtype);
    }

    public RefReservationBillDiscountType(int idtype, String typeName, String typeNote, Set tblReservationBills) {
        this();
        idtypeProperty().set(idtype);
        typeNameProperty().set(typeName);
        typeNoteProperty().set(typeNote);

        this.tblReservationBills = tblReservationBills;
    }

    public RefReservationBillDiscountType(RefReservationBillDiscountType refReservationBillDiscountType) {
        this();
        idtypeProperty().set(refReservationBillDiscountType.getIdtype());
        typeNameProperty().set(refReservationBillDiscountType.getTypeName());
        typeNoteProperty().set(refReservationBillDiscountType.getTypeNote());

        this.tblReservationBills = refReservationBillDiscountType.getTblReservationBills();
    }

    public final IntegerProperty idtypeProperty() {
        return this.idtype;
    }

    public int getIdtype() {
        return idtypeProperty().get();
    }

    public void setIdtype(int idtype) {
        idtypeProperty().set(idtype);
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

    public final StringProperty typeNoteProperty() {
        return this.typeNote;
    }

    public String getTypeNote() {
        return typeNoteProperty().get();
    }

    public void setTypeNote(String typeNote) {
        typeNoteProperty().set(typeNote);
    }

    public Set getTblReservationBills() {
        return this.tblReservationBills;
    }

    public void setTblReservationBills(Set tblReservationBills) {
        this.tblReservationBills = tblReservationBills;
    }

}