package hotelfx.persistence.model;
// Generated Aug 21, 2017 1:49:32 PM by Hibernate Tools 4.3.1

import java.util.HashSet;
import java.util.Set;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 * RefReservationFacilityType generated by hbm2java
 */
public class RefReservationFacilityType implements java.io.Serializable {

    private final IntegerProperty idtype;
    private final StringProperty typeName;
    private final StringProperty typeNote;

    private Set tblReservationBreakfastVouchers;

    public RefReservationFacilityType() {
        this.idtype = new SimpleIntegerProperty();
        this.typeName = new SimpleStringProperty();
        this.typeNote = new SimpleStringProperty();

        this.tblReservationBreakfastVouchers = new HashSet(0);
    }

    public RefReservationFacilityType(int idtype) {
        this();
        idtypeProperty().set(idtype);
    }

    public RefReservationFacilityType(int idtype, String typeName, String typeNote, Set tblReservationBreakfastVouchers) {
        this();
        idtypeProperty().set(idtype);
        typeNameProperty().set(typeName);
        typeNoteProperty().set(typeNote);

        this.tblReservationBreakfastVouchers = tblReservationBreakfastVouchers;
    }

    public RefReservationFacilityType(RefReservationFacilityType refReservationFacilityType) {
        this();
        idtypeProperty().set(refReservationFacilityType.getIdtype());
        typeNameProperty().set(refReservationFacilityType.getTypeName());
        typeNoteProperty().set(refReservationFacilityType.getTypeNote());

        this.tblReservationBreakfastVouchers = refReservationFacilityType.getTblReservationBreakfastVouchers();
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

    public Set getTblReservationBreakfastVouchers() {
        return this.tblReservationBreakfastVouchers;
    }

    public void setTblReservationBreakfastVouchers(Set tblReservationBreakfastVouchers) {
        this.tblReservationBreakfastVouchers = tblReservationBreakfastVouchers;
    }

}