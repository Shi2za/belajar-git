package hotelfx.persistence.model;
// Generated Oct 26, 2018 12:41:59 PM by Hibernate Tools 4.3.1

import java.util.HashSet;
import java.util.Set;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 * RefBankCardType generated by hbm2java
 */
public class RefBankCardType implements java.io.Serializable {

    private final IntegerProperty idtype;
    private final StringProperty typeName;
    private final StringProperty typeNote;

    private Set tblBankCards;
    private Set tblBankEdcBankNetworkCards;

    public RefBankCardType() {
        this.idtype = new SimpleIntegerProperty();
        this.typeName = new SimpleStringProperty();
        this.typeNote = new SimpleStringProperty();

        this.tblBankCards = new HashSet(0);
        this.tblBankEdcBankNetworkCards = new HashSet(0);
    }

    public RefBankCardType(int idtype) {
        this();
        idtypeProperty().set(idtype);
    }

    public RefBankCardType(int idtype, String typeName, String typeNote, Set tblBankCards, Set tblBankEdcBankNetworkCards) {
        this();
        idtypeProperty().set(idtype);
        typeNameProperty().set(typeName);
        typeNoteProperty().set(typeNote);

        this.tblBankCards = tblBankCards;
        this.tblBankEdcBankNetworkCards = tblBankEdcBankNetworkCards;
    }

    public RefBankCardType(RefBankCardType refBankCardType) {
        this();
        idtypeProperty().set(refBankCardType.getIdtype());
        typeNameProperty().set(refBankCardType.getTypeName());
        typeNoteProperty().set(refBankCardType.getTypeNote());

        this.tblBankCards = refBankCardType.getTblBankCards();
        this.tblBankEdcBankNetworkCards = refBankCardType.getTblBankEdcBankNetworkCards();
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

    public Set getTblBankCards() {
        return this.tblBankCards;
    }

    public void setTblBankCards(Set tblBankCards) {
        this.tblBankCards = tblBankCards;
    }

    public Set getTblBankEdcBankNetworkCards() {
        return this.tblBankEdcBankNetworkCards;
    }

    public void setTblBankEdcBankNetworkCards(Set tblBankEdcBankNetworkCards) {
        this.tblBankEdcBankNetworkCards = tblBankEdcBankNetworkCards;
    }

    @Override
    public String toString(){
        return getTypeName();
    }
    
}
