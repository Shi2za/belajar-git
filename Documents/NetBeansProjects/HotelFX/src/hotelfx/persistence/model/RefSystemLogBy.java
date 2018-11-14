package hotelfx.persistence.model;
// Generated Apr 4, 2018 1:25:55 PM by Hibernate Tools 4.3.1

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 * RefSystemLogBy generated by hbm2java
 */
public class RefSystemLogBy implements java.io.Serializable {

    private final IntegerProperty idtype;
    private final StringProperty typeName;
    private final StringProperty typeNote;

    public RefSystemLogBy() {
        this.idtype = new SimpleIntegerProperty();
        this.typeName = new SimpleStringProperty();
        this.typeNote = new SimpleStringProperty();

    }

    public RefSystemLogBy(int idtype) {
        this();
        idtypeProperty().set(idtype);
    }

    public RefSystemLogBy(int idtype, String typeName, String typeNote) {
        this();
        idtypeProperty().set(idtype);
        typeNameProperty().set(typeName);
        typeNoteProperty().set(typeNote);

    }

    public RefSystemLogBy(RefSystemLogBy refSystemLogBy) {
        this();
        idtypeProperty().set(refSystemLogBy.getIdtype());
        typeNameProperty().set(refSystemLogBy.getTypeName());
        typeNoteProperty().set(refSystemLogBy.getTypeNote());

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
