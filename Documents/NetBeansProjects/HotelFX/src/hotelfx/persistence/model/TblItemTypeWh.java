package hotelfx.persistence.model;
// Generated Sep 3, 2018 11:08:35 AM by Hibernate Tools 4.3.1

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
 * TblItemTypeWh generated by hbm2java
 */
public class TblItemTypeWh implements java.io.Serializable {

    private final LongProperty iditemTypeWh;
    private final ObjectProperty<RefRecordStatus> refRecordStatus;
    private final ObjectProperty<TblEmployee> tblEmployeeByCreateBy;
    private final ObjectProperty<TblEmployee> tblEmployeeByLastUpdateBy;
    private final StringProperty codeItemTypeWh;
    private final StringProperty itemTypeWhname;
    private final StringProperty itemTypeWhnote;
    private final ObjectProperty<Date> createDate;
    private final ObjectProperty<Date> lastUpdateDate;

    private Set tblItems;

    public TblItemTypeWh() {
        this.iditemTypeWh = new SimpleLongProperty();
        this.refRecordStatus = new SimpleObjectProperty<>();
        this.tblEmployeeByCreateBy = new SimpleObjectProperty<>();
        this.tblEmployeeByLastUpdateBy = new SimpleObjectProperty<>();
        this.codeItemTypeWh = new SimpleStringProperty();
        this.itemTypeWhname = new SimpleStringProperty();
        this.itemTypeWhnote = new SimpleStringProperty();
        this.createDate = new SimpleObjectProperty<>();
        this.lastUpdateDate = new SimpleObjectProperty<>();

        this.tblItems = new HashSet(0);
    }

    public TblItemTypeWh(long iditemTypeWh) {
        this();
        iditemTypeWhProperty().set(iditemTypeWh);
    }

    public TblItemTypeWh(long iditemTypeWh, RefRecordStatus refRecordStatus, TblEmployee tblEmployeeByCreateBy, TblEmployee tblEmployeeByLastUpdateBy, String codeItemTypeWh, String itemTypeWhname, String itemTypeWhnote, Date createDate, Date lastUpdateDate, Set tblItems) {
        this();
        iditemTypeWhProperty().set(iditemTypeWh);
        refRecordStatusProperty().set(refRecordStatus);
        tblEmployeeByCreateByProperty().set(tblEmployeeByCreateBy);
        tblEmployeeByLastUpdateByProperty().set(tblEmployeeByLastUpdateBy);
        codeItemTypeWhProperty().set(codeItemTypeWh);
        itemTypeWhnameProperty().set(itemTypeWhname);
        itemTypeWhnoteProperty().set(itemTypeWhnote);
        createDateProperty().set(createDate);
        lastUpdateDateProperty().set(lastUpdateDate);

        this.tblItems = tblItems;
    }

    public TblItemTypeWh(TblItemTypeWh tblItemTypeWh) {
        this();
        iditemTypeWhProperty().set(tblItemTypeWh.getIditemTypeWh());
        refRecordStatusProperty().set(tblItemTypeWh.getRefRecordStatus());
        tblEmployeeByCreateByProperty().set(tblItemTypeWh.getTblEmployeeByCreateBy());
        tblEmployeeByLastUpdateByProperty().set(tblItemTypeWh.getTblEmployeeByLastUpdateBy());
        codeItemTypeWhProperty().set(tblItemTypeWh.getCodeItemTypeWh());
        itemTypeWhnameProperty().set(tblItemTypeWh.getItemTypeWhname());
        itemTypeWhnoteProperty().set(tblItemTypeWh.getItemTypeWhnote());
        createDateProperty().set(tblItemTypeWh.getCreateDate());
        lastUpdateDateProperty().set(tblItemTypeWh.getLastUpdateDate());

        this.tblItems = tblItemTypeWh.getTblItems();
    }

    public final LongProperty iditemTypeWhProperty() {
        return this.iditemTypeWh;
    }

    public long getIditemTypeWh() {
        return iditemTypeWhProperty().get();
    }

    public void setIditemTypeWh(long iditemTypeWh) {
        iditemTypeWhProperty().set(iditemTypeWh);
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

    public final ObjectProperty<TblEmployee> tblEmployeeByCreateByProperty() {
        return this.tblEmployeeByCreateBy;
    }

    public TblEmployee getTblEmployeeByCreateBy() {
        return tblEmployeeByCreateByProperty().get();
    }

    public void setTblEmployeeByCreateBy(TblEmployee tblEmployeeByCreateBy) {
        tblEmployeeByCreateByProperty().set(tblEmployeeByCreateBy);
    }

    public final ObjectProperty<TblEmployee> tblEmployeeByLastUpdateByProperty() {
        return this.tblEmployeeByLastUpdateBy;
    }

    public TblEmployee getTblEmployeeByLastUpdateBy() {
        return tblEmployeeByLastUpdateByProperty().get();
    }

    public void setTblEmployeeByLastUpdateBy(TblEmployee tblEmployeeByLastUpdateBy) {
        tblEmployeeByLastUpdateByProperty().set(tblEmployeeByLastUpdateBy);
    }

    public final StringProperty codeItemTypeWhProperty() {
        return this.codeItemTypeWh;
    }

    public String getCodeItemTypeWh() {
        return codeItemTypeWhProperty().get();
    }

    public void setCodeItemTypeWh(String codeItemTypeWh) {
        codeItemTypeWhProperty().set(codeItemTypeWh);
    }

    public final StringProperty itemTypeWhnameProperty() {
        return this.itemTypeWhname;
    }

    public String getItemTypeWhname() {
        return itemTypeWhnameProperty().get();
    }

    public void setItemTypeWhname(String itemTypeWhname) {
        itemTypeWhnameProperty().set(itemTypeWhname);
    }

    public final StringProperty itemTypeWhnoteProperty() {
        return this.itemTypeWhnote;
    }

    public String getItemTypeWhnote() {
        return itemTypeWhnoteProperty().get();
    }

    public void setItemTypeWhnote(String itemTypeWhnote) {
        itemTypeWhnoteProperty().set(itemTypeWhnote);
    }

    public final ObjectProperty<Date> createDateProperty() {
        return this.createDate;
    }

    public Date getCreateDate() {
        return createDateProperty().get();
    }

    public void setCreateDate(Date createDate) {
        createDateProperty().set(createDate);
    }

    public final ObjectProperty<Date> lastUpdateDateProperty() {
        return this.lastUpdateDate;
    }

    public Date getLastUpdateDate() {
        return lastUpdateDateProperty().get();
    }

    public void setLastUpdateDate(Date lastUpdateDate) {
        lastUpdateDateProperty().set(lastUpdateDate);
    }

    public Set getTblItems() {
        return this.tblItems;
    }

    public void setTblItems(Set tblItems) {
        this.tblItems = tblItems;
    }

    @Override
    public String toString(){
        return getItemTypeWhname();
    }
    
}
