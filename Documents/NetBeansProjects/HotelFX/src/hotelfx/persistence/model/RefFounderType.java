package hotelfx.persistence.model;
// Generated Mar 29, 2018 3:28:56 PM by Hibernate Tools 4.3.1

import java.util.HashSet;
import java.util.Set;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 * RefFounderType generated by hbm2java
 */
public class RefFounderType implements java.io.Serializable {

    private final IntegerProperty idtype;
    private final StringProperty typeName;
    private final StringProperty typeNote;

    private Set tblFoundInformations;

    public RefFounderType() {
        this.idtype = new SimpleIntegerProperty();
        this.typeName = new SimpleStringProperty();
        this.typeNote = new SimpleStringProperty();

        this.tblFoundInformations = new HashSet(0);
    }

    public RefFounderType(int idtype) {
        this();
        idtypeProperty().set(idtype);
    }

    public RefFounderType(int idtype, String typeName, String typeNote, Set tblFoundInformations) {
        this();
        idtypeProperty().set(idtype);
        typeNameProperty().set(typeName);
        typeNoteProperty().set(typeNote);

        this.tblFoundInformations = tblFoundInformations;
    }

    public RefFounderType(RefFounderType refFounderType) {
        this();
        idtypeProperty().set(refFounderType.getIdtype());
        typeNameProperty().set(refFounderType.getTypeName());
        typeNoteProperty().set(refFounderType.getTypeNote());

        this.tblFoundInformations = refFounderType.getTblFoundInformations();
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

    public Set getTblFoundInformations() {
        return this.tblFoundInformations;
    }

    public void setTblFoundInformations(Set tblFoundInformations) {
        this.tblFoundInformations = tblFoundInformations;
    }

    @Override
    public String toString(){
        return getTypeName();
    }
    
}