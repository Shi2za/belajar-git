package hotelfx.persistence.model;
// Generated Aug 21, 2017 1:49:32 PM by Hibernate Tools 4.3.1

import java.util.HashSet;
import java.util.Set;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 * RefPartnerRateInChargeType generated by hbm2java
 */
public class RefPartnerRateInChargeType implements java.io.Serializable {

    private final IntegerProperty idtype;
    private final StringProperty typeName;
    private final StringProperty typeNote;

    private Set tblPartnerAgreements;

    public RefPartnerRateInChargeType() {
        this.idtype = new SimpleIntegerProperty();
        this.typeName = new SimpleStringProperty();
        this.typeNote = new SimpleStringProperty();

        this.tblPartnerAgreements = new HashSet(0);
    }

    public RefPartnerRateInChargeType(int idtype) {
        this();
        idtypeProperty().set(idtype);
    }

    public RefPartnerRateInChargeType(int idtype, String typeName, String typeNote, Set tblPartnerAgreements) {
        this();
        idtypeProperty().set(idtype);
        typeNameProperty().set(typeName);
        typeNoteProperty().set(typeNote);
        this.tblPartnerAgreements = tblPartnerAgreements;
    }

    public RefPartnerRateInChargeType(RefPartnerRateInChargeType refPartnerRateInChargeType) {
        this();
        idtypeProperty().set(refPartnerRateInChargeType.getIdtype());
        typeNameProperty().set(refPartnerRateInChargeType.getTypeName());
        typeNoteProperty().set(refPartnerRateInChargeType.getTypeNote());

        this.tblPartnerAgreements = refPartnerRateInChargeType.getTblPartnerAgreements();
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

    public Set getTblPartnerAgreements() {
        return this.tblPartnerAgreements;
    }

    public void setTblPartnerAgreements(Set tblPartnerAgreements) {
        this.tblPartnerAgreements = tblPartnerAgreements;
    }

}
