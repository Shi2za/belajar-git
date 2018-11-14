package hotelfx.persistence.model;
// Generated Dec 9, 2017 10:48:36 AM by Hibernate Tools 4.3.1

import java.util.HashSet;
import java.util.Set;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 * RefHotelPayableType generated by hbm2java
 */
public class RefHotelPayableType implements java.io.Serializable {

    private final IntegerProperty idtype;
    private final StringProperty typeName;
    private final StringProperty typeNote;

    private Set tblHotelPayables;

    public RefHotelPayableType() {
        this.idtype = new SimpleIntegerProperty();
        this.typeName = new SimpleStringProperty();
        this.typeNote = new SimpleStringProperty();

        this.tblHotelPayables = new HashSet(0);
    }

    public RefHotelPayableType(int idtype) {
        this();
        idtypeProperty().set(idtype);
    }

    public RefHotelPayableType(int idtype, String typeName, String typeNote, Set tblHotelPayables) {
        this();
        idtypeProperty().set(idtype);
        typeNameProperty().set(typeName);
        typeNoteProperty().set(typeNote);

        this.tblHotelPayables = tblHotelPayables;
    }

    public RefHotelPayableType(RefHotelPayableType refHotelPayableType) {
        this();
        idtypeProperty().set(refHotelPayableType.getIdtype());
        typeNameProperty().set(refHotelPayableType.getTypeName());
        typeNoteProperty().set(refHotelPayableType.getTypeNote());

        this.tblHotelPayables = refHotelPayableType.getTblHotelPayables();
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

    public Set getTblHotelPayables() {
        return this.tblHotelPayables;
    }

    public void setTblHotelPayables(Set tblHotelPayables) {
        this.tblHotelPayables = tblHotelPayables;
    }

}