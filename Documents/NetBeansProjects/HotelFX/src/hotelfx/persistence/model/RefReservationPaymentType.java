package hotelfx.persistence.model;
// Generated Aug 31, 2017 4:11:59 PM by Hibernate Tools 4.3.1

import java.util.HashSet;
import java.util.Set;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 * RefReservationPaymentType generated by hbm2java
 */
public class RefReservationPaymentType implements java.io.Serializable {

    private final IntegerProperty idtype;
    private final StringProperty typeName;
    private final StringProperty typeNote;

    private Set tblReservationRoomTypeDetails;

    public RefReservationPaymentType() {
        this.idtype = new SimpleIntegerProperty();
        this.typeName = new SimpleStringProperty();
        this.typeNote = new SimpleStringProperty();

        this.tblReservationRoomTypeDetails = new HashSet(0);
    }

    public RefReservationPaymentType(int idtype) {
        this();
        idtypeProperty().set(idtype);
    }

    public RefReservationPaymentType(int idtype, String typeName, String typeNote, Set tblReservationRoomTypeDetails) {
        this();
        idtypeProperty().set(idtype);
        typeNameProperty().set(typeName);
        typeNoteProperty().set(typeNote);

        this.tblReservationRoomTypeDetails = tblReservationRoomTypeDetails;
    }

    public RefReservationPaymentType(RefReservationPaymentType refReservationPaymentType) {
        this();
        idtypeProperty().set(refReservationPaymentType.getIdtype());
        typeNameProperty().set(refReservationPaymentType.getTypeName());
        typeNoteProperty().set(refReservationPaymentType.getTypeNote());

        this.tblReservationRoomTypeDetails = refReservationPaymentType.getTblReservationRoomTypeDetails();
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

    public Set getTblReservationRoomTypeDetails() {
        return this.tblReservationRoomTypeDetails;
    }

    public void setTblReservationRoomTypeDetails(Set tblReservationRoomTypeDetails) {
        this.tblReservationRoomTypeDetails = tblReservationRoomTypeDetails;
    }

}
