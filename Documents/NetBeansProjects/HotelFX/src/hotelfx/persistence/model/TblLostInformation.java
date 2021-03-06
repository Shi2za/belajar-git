package hotelfx.persistence.model;
// Generated Mar 29, 2018 3:28:56 PM by Hibernate Tools 4.3.1

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javafx.beans.property.LongProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleLongProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 * TblLostInformation generated by hbm2java
 */
public class TblLostInformation implements java.io.Serializable {

    private final LongProperty idlost;
    private final ObjectProperty<RefLostFoundStatus> refLostFoundStatus;
    private final ObjectProperty<RefRecordStatus> refRecordStatus;
    private final ObjectProperty<TblEmployee> tblEmployeeByCreatedBy;
    private final ObjectProperty<TblEmployee> tblEmployeeByUpdatedBy;
    private final ObjectProperty<TblPeople> tblPeople;
    private final StringProperty codeLost;
    private final ObjectProperty<Date> lostDate;
    private final StringProperty lostLocation;
    private final StringProperty itemName;
    private final StringProperty lostNote;
    private final ObjectProperty<Date> createdDate;
    private final ObjectProperty<Date> updatedDate;

    private Set tblLostFoundInformationDetails;

    public TblLostInformation() {
        this.idlost = new SimpleLongProperty();
        this.refLostFoundStatus = new SimpleObjectProperty<>();
        this.refRecordStatus = new SimpleObjectProperty<>();
        this.tblEmployeeByCreatedBy = new SimpleObjectProperty<>();
        this.tblEmployeeByUpdatedBy = new SimpleObjectProperty<>();
        this.tblPeople = new SimpleObjectProperty<>();
        this.codeLost = new SimpleStringProperty();
        this.lostDate = new SimpleObjectProperty<>();
        this.lostLocation = new SimpleStringProperty();
        this.itemName = new SimpleStringProperty();
        this.lostNote = new SimpleStringProperty();
        this.createdDate = new SimpleObjectProperty<>();
        this.updatedDate = new SimpleObjectProperty<>();

        this.tblLostFoundInformationDetails = new HashSet(0);
    }

    public TblLostInformation(long idlost) {
        this();
        idlostProperty().set(idlost);
    }

    public TblLostInformation(long idlost, RefLostFoundStatus refLostFoundStatus, RefRecordStatus refRecordStatus, TblEmployee tblEmployeeByCreatedBy, TblEmployee tblEmployeeByUpdatedBy, TblPeople tblPeople, String codeLost, Date lostDate, String lostLocation, String itemName, String lostNote, Date createdDate, Date updatedDate, Set tblLostFoundInformationDetails) {
        this();
        idlostProperty().set(idlost);
        refLostFoundStatusProperty().set(refLostFoundStatus);
        refRecordStatusProperty().set(refRecordStatus);
        tblEmployeeByCreatedByProperty().set(tblEmployeeByCreatedBy);
        tblEmployeeByUpdatedByProperty().set(tblEmployeeByUpdatedBy);
        tblPeopleProperty().set(tblPeople);
        codeLostProperty().set(codeLost);
        lostDateProperty().set(lostDate);
        lostLocationProperty().set(lostLocation);
        itemNameProperty().set(itemName);
        lostNoteProperty().set(lostNote);
        createdDateProperty().set(createdDate);
        updatedDateProperty().set(updatedDate);

        this.tblLostFoundInformationDetails = tblLostFoundInformationDetails;
    }

    public TblLostInformation(TblLostInformation tblLostInformation) {
        this();
        idlostProperty().set(tblLostInformation.getIdlost());
        refLostFoundStatusProperty().set(tblLostInformation.getRefLostFoundStatus());
        refRecordStatusProperty().set(tblLostInformation.getRefRecordStatus());
        tblEmployeeByCreatedByProperty().set(tblLostInformation.getTblEmployeeByCreatedBy());
        tblEmployeeByUpdatedByProperty().set(tblLostInformation.getTblEmployeeByUpdatedBy());
        tblPeopleProperty().set(tblLostInformation.getTblPeople());
        codeLostProperty().set(tblLostInformation.getCodeLost());
        lostDateProperty().set(tblLostInformation.getLostDate());
        lostLocationProperty().set(tblLostInformation.getLostLocation());
        itemNameProperty().set(tblLostInformation.getItemName());
        lostNoteProperty().set(tblLostInformation.getLostNote());
        createdDateProperty().set(tblLostInformation.getCreatedDate());
        updatedDateProperty().set(tblLostInformation.getUpdatedDate());

        this.tblLostFoundInformationDetails = tblLostInformation.getTblLostFoundInformationDetails();
    }

    public final LongProperty idlostProperty() {
        return this.idlost;
    }

    public long getIdlost() {
        return idlostProperty().get();
    }

    public void setIdlost(long idlost) {
        idlostProperty().set(idlost);
    }

    public final ObjectProperty<RefLostFoundStatus> refLostFoundStatusProperty() {
        return this.refLostFoundStatus;
    }

    public RefLostFoundStatus getRefLostFoundStatus() {
        return refLostFoundStatusProperty().get();
    }

    public void setRefLostFoundStatus(RefLostFoundStatus refLostFoundStatus) {
        refLostFoundStatusProperty().set(refLostFoundStatus);
    }

    public final ObjectProperty<RefRecordStatus> refRecordStatusProperty() {
        return this.refRecordStatus;
    }

    public RefRecordStatus getRefRecordStatus() {
        return refRecordStatusProperty().get();
    }

    public void setRefRecordStatus(RefRecordStatus refRecordStatus) {
        refRecordStatusProperty().set(refRecordStatus);
    }

    public final ObjectProperty<TblEmployee> tblEmployeeByCreatedByProperty() {
        return this.tblEmployeeByCreatedBy;
    }

    public TblEmployee getTblEmployeeByCreatedBy() {
        return tblEmployeeByCreatedByProperty().get();
    }

    public void setTblEmployeeByCreatedBy(TblEmployee tblEmployeeByCreatedBy) {
        tblEmployeeByCreatedByProperty().set(tblEmployeeByCreatedBy);
    }

    public final ObjectProperty<TblEmployee> tblEmployeeByUpdatedByProperty() {
        return this.tblEmployeeByUpdatedBy;
    }

    public TblEmployee getTblEmployeeByUpdatedBy() {
        return tblEmployeeByUpdatedByProperty().get();
    }

    public void setTblEmployeeByUpdatedBy(TblEmployee tblEmployeeByUpdatedBy) {
        tblEmployeeByUpdatedByProperty().set(tblEmployeeByUpdatedBy);
    }

    public final ObjectProperty<TblPeople> tblPeopleProperty() {
        return this.tblPeople;
    }

    public TblPeople getTblPeople() {
        return tblPeopleProperty().get();
    }

    public void setTblPeople(TblPeople tblPeople) {
        tblPeopleProperty().set(tblPeople);
    }

    public final StringProperty codeLostProperty() {
        return this.codeLost;
    }

    public String getCodeLost() {
        return codeLostProperty().get();
    }

    public void setCodeLost(String codeLost) {
        codeLostProperty().set(codeLost);
    }

    public final ObjectProperty<Date> lostDateProperty() {
        return this.lostDate;
    }

    public Date getLostDate() {
        return lostDateProperty().get();
    }

    public void setLostDate(Date lostDate) {
        lostDateProperty().set(lostDate);
    }

    public final StringProperty lostLocationProperty() {
        return this.lostLocation;
    }

    public String getLostLocation() {
        return lostLocationProperty().get();
    }

    public void setLostLocation(String lostLocation) {
        lostLocationProperty().set(lostLocation);
    }

    public final StringProperty itemNameProperty() {
        return this.itemName;
    }

    public String getItemName() {
        return itemNameProperty().get();
    }

    public void setItemName(String itemName) {
        itemNameProperty().set(itemName);
    }

    public final StringProperty lostNoteProperty() {
        return this.lostNote;
    }

    public String getLostNote() {
        return lostNoteProperty().get();
    }

    public void setLostNote(String lostNote) {
        lostNoteProperty().set(lostNote);
    }

    public final ObjectProperty<Date> createdDateProperty() {
        return this.createdDate;
    }

    public Date getCreatedDate() {
        return createdDateProperty().get();
    }

    public void setCreatedDate(Date createdDate) {
        createdDateProperty().set(createdDate);
    }

    public final ObjectProperty<Date> updatedDateProperty() {
        return this.updatedDate;
    }

    public Date getUpdatedDate() {
        return updatedDateProperty().get();
    }

    public void setUpdatedDate(Date updatedDate) {
        updatedDateProperty().set(updatedDate);
    }

    public Set getTblLostFoundInformationDetails() {
        return this.tblLostFoundInformationDetails;
    }

    public void setTblLostFoundInformationDetails(Set tblLostFoundInformationDetails) {
        this.tblLostFoundInformationDetails = tblLostFoundInformationDetails;
    }

    @Override
    public String toString(){
        return getCodeLost();
    }
    
}
