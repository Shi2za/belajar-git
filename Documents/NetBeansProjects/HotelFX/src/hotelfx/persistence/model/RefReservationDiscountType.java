package hotelfx.persistence.model;
// Generated Jan 10, 2018 4:39:21 PM by Hibernate Tools 4.3.1

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 * RefReservationDiscountType generated by hbm2java
 */
public class RefReservationDiscountType implements java.io.Serializable {

    private final IntegerProperty idtype;
    private final StringProperty typeName;
    private final StringProperty typeNote;

    public RefReservationDiscountType() {
        this.idtype = new SimpleIntegerProperty();
        this.typeName = new SimpleStringProperty();
        this.typeNote = new SimpleStringProperty();

    }

    public RefReservationDiscountType(int idtype) {
        this();
        idtypeProperty().set(idtype);
    }

    public RefReservationDiscountType(int idtype, String typeName, String typeNote) {
        this();
        idtypeProperty().set(idtype);
        typeNameProperty().set(typeName);
        typeNoteProperty().set(typeNote);

    }

    public RefReservationDiscountType(RefReservationDiscountType refReservationDiscountType) {
        this();
        idtypeProperty().set(refReservationDiscountType.getIdtype());
        typeNameProperty().set(refReservationDiscountType.getTypeName());
        typeNoteProperty().set(refReservationDiscountType.getTypeNote());

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

}
