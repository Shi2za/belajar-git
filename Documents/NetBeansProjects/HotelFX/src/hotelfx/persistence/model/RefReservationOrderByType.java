package hotelfx.persistence.model;
// Generated Aug 31, 2017 4:11:59 PM by Hibernate Tools 4.3.1

import java.util.HashSet;
import java.util.Set;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 * RefReservationOrderByType generated by hbm2java
 */
public class RefReservationOrderByType implements java.io.Serializable {

    private final IntegerProperty idtype;
    private final StringProperty typeName;
    private final StringProperty typeNote;

    private Set tblReservationRoomTypeDetails;

    public RefReservationOrderByType() {
        this.idtype = new SimpleIntegerProperty();
        this.typeName = new SimpleStringProperty();
        this.typeNote = new SimpleStringProperty();

        this.tblReservationRoomTypeDetails = new HashSet(0);
    }

    public RefReservationOrderByType(int idtype) {
        this();
        idtypeProperty().set(idtype);
    }

    public RefReservationOrderByType(int idtype, String typeName, String typeNote, Set tblReservationRoomTypeDetails) {
        this();
        idtypeProperty().set(idtype);
        typeNameProperty().set(typeName);
        typeNoteProperty().set(typeNote);

        this.tblReservationRoomTypeDetails = tblReservationRoomTypeDetails;
    }

    public RefReservationOrderByType(RefReservationOrderByType refReservationOrderByType) {
        this();
        idtypeProperty().set(refReservationOrderByType.getIdtype());
        typeNameProperty().set(refReservationOrderByType.getTypeName());
        typeNoteProperty().set(refReservationOrderByType.getTypeNote());

        this.tblReservationRoomTypeDetails = refReservationOrderByType.getTblReservationRoomTypeDetails();
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

    @Override
    public String toString(){
        return getTypeName();
    }
    
}
