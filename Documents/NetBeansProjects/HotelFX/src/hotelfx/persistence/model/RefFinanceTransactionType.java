package hotelfx.persistence.model;
// Generated Dec 11, 2017 10:28:33 AM by Hibernate Tools 4.3.1

import java.util.HashSet;
import java.util.Set;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 * RefFinanceTransactionType generated by hbm2java
 */
public class RefFinanceTransactionType implements java.io.Serializable {

    private final IntegerProperty idtype;
    private final StringProperty typeName;
    private final StringProperty typeNote;

    private Set tblFinanceTransactions;
    private Set tblHotelFinanceTransactions;

    public RefFinanceTransactionType() {
        this.idtype = new SimpleIntegerProperty();
        this.typeName = new SimpleStringProperty();
        this.typeNote = new SimpleStringProperty();

        this.tblFinanceTransactions = new HashSet(0);
        this.tblHotelFinanceTransactions = new HashSet(0);
    }

    public RefFinanceTransactionType(int idtype) {
        this();
        idtypeProperty().set(idtype);
    }

    public RefFinanceTransactionType(int idtype, String typeName, String typeNote, Set tblFinanceTransactions, Set tblHotelFinanceTransactions) {
        this();
        idtypeProperty().set(idtype);
        typeNameProperty().set(typeName);
        typeNoteProperty().set(typeNote);

        this.tblFinanceTransactions = tblFinanceTransactions;
        this.tblHotelFinanceTransactions = tblHotelFinanceTransactions;
    }

    public RefFinanceTransactionType(RefFinanceTransactionType refFinanceTransactionType) {
        this();
        idtypeProperty().set(refFinanceTransactionType.getIdtype());
        typeNameProperty().set(refFinanceTransactionType.getTypeName());
        typeNoteProperty().set(refFinanceTransactionType.getTypeNote());

        this.tblFinanceTransactions = refFinanceTransactionType.getTblFinanceTransactions();
        this.tblHotelFinanceTransactions = refFinanceTransactionType.getTblHotelFinanceTransactions();
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

    public Set getTblFinanceTransactions() {
        return this.tblFinanceTransactions;
    }

    public void setTblFinanceTransactions(Set tblFinanceTransactions) {
        this.tblFinanceTransactions = tblFinanceTransactions;
    }

    public Set getTblHotelFinanceTransactions() {
        return this.tblHotelFinanceTransactions;
    }

    public void setTblHotelFinanceTransactions(Set tblHotelFinanceTransactions) {
        this.tblHotelFinanceTransactions = tblHotelFinanceTransactions;
    }

}