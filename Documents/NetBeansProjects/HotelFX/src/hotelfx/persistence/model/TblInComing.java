package hotelfx.persistence.model;
// Generated Oct 11, 2018 9:25:10 AM by Hibernate Tools 4.3.1

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
 * TblInComing generated by hbm2java
 */
public class TblInComing implements java.io.Serializable {

    private final LongProperty idic;
    private final ObjectProperty<RefRecordStatus> refRecordStatus;
    private final ObjectProperty<TblEmployee> tblEmployeeByLastUpdateBy;
    private final ObjectProperty<TblEmployee> tblEmployeeByIdsender;
    private final ObjectProperty<TblEmployee> tblEmployeeByCreateBy;
    private final ObjectProperty<TblLocation> tblLocationByIdlocationDestination;
    private final ObjectProperty<TblLocation> tblLocationByIdlocationSource;
    private final StringProperty codeIc;
    private final ObjectProperty<Date> icdate;
    private final StringProperty icnote;
    private final ObjectProperty<Date> createDate;
    private final ObjectProperty<Date> lastUpdateDate;

    private Set tblInComingDetails;

    public TblInComing() {
        this.idic = new SimpleLongProperty();
        this.refRecordStatus = new SimpleObjectProperty<>();
        this.tblEmployeeByLastUpdateBy = new SimpleObjectProperty<>();
        this.tblEmployeeByIdsender = new SimpleObjectProperty<>();
        this.tblEmployeeByCreateBy = new SimpleObjectProperty<>();
        this.tblLocationByIdlocationDestination = new SimpleObjectProperty<>();
        this.tblLocationByIdlocationSource = new SimpleObjectProperty<>();
        this.codeIc = new SimpleStringProperty();
        this.icdate = new SimpleObjectProperty<>();
        this.icnote = new SimpleStringProperty();
        this.createDate = new SimpleObjectProperty<>();
        this.lastUpdateDate = new SimpleObjectProperty<>();

        this.tblInComingDetails = new HashSet(0);
    }

    public TblInComing(long idic) {
        this();
        idicProperty().set(idic);
    }

    public TblInComing(long idic, RefRecordStatus refRecordStatus, TblEmployee tblEmployeeByLastUpdateBy, TblEmployee tblEmployeeByIdsender, TblEmployee tblEmployeeByCreateBy, TblLocation tblLocationByIdlocationDestination, TblLocation tblLocationByIdlocationSource, String codeIc, Date icdate, String icnote, Date createDate, Date lastUpdateDate, Set tblInComingDetails) {
        this();
        idicProperty().set(idic);
        refRecordStatusProperty().set(refRecordStatus);
        tblEmployeeByLastUpdateByProperty().set(tblEmployeeByLastUpdateBy);
        tblEmployeeByIdsenderProperty().set(tblEmployeeByIdsender);
        tblEmployeeByCreateByProperty().set(tblEmployeeByCreateBy);
        tblLocationByIdlocationDestinationProperty().set(tblLocationByIdlocationDestination);
        tblLocationByIdlocationSourceProperty().set(tblLocationByIdlocationSource);
        codeIcProperty().set(codeIc);
        icdateProperty().set(icdate);
        icnoteProperty().set(icnote);
        createDateProperty().set(createDate);
        lastUpdateDateProperty().set(lastUpdateDate);

        this.tblInComingDetails = tblInComingDetails;
    }

    public TblInComing(TblInComing tblInComing) {
        this();
        idicProperty().set(tblInComing.getIdic());
        refRecordStatusProperty().set(tblInComing.getRefRecordStatus());
        tblEmployeeByLastUpdateByProperty().set(tblInComing.getTblEmployeeByLastUpdateBy());
        tblEmployeeByIdsenderProperty().set(tblInComing.getTblEmployeeByIdsender());
        tblEmployeeByCreateByProperty().set(tblInComing.getTblEmployeeByCreateBy());
        tblLocationByIdlocationDestinationProperty().set(tblInComing.getTblLocationByIdlocationDestination());
        tblLocationByIdlocationSourceProperty().set(tblInComing.getTblLocationByIdlocationSource());
        codeIcProperty().set(tblInComing.getCodeIc());
        icdateProperty().set(tblInComing.getIcdate());
        icnoteProperty().set(tblInComing.getIcnote());
        createDateProperty().set(tblInComing.getCreateDate());
        lastUpdateDateProperty().set(tblInComing.getLastUpdateDate());

        this.tblInComingDetails = tblInComing.getTblInComingDetails();
    }

    public final LongProperty idicProperty() {
        return this.idic;
    }

    public long getIdic() {
        return idicProperty().get();
    }

    public void setIdic(long idic) {
        idicProperty().set(idic);
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

    public final ObjectProperty<TblEmployee> tblEmployeeByLastUpdateByProperty() {
        return this.tblEmployeeByLastUpdateBy;
    }

    public TblEmployee getTblEmployeeByLastUpdateBy() {
        return tblEmployeeByLastUpdateByProperty().get();
    }

    public void setTblEmployeeByLastUpdateBy(TblEmployee tblEmployeeByLastUpdateBy) {
        tblEmployeeByLastUpdateByProperty().set(tblEmployeeByLastUpdateBy);
    }

    public final ObjectProperty<TblEmployee> tblEmployeeByIdsenderProperty() {
        return this.tblEmployeeByIdsender;
    }

    public TblEmployee getTblEmployeeByIdsender() {
        return tblEmployeeByIdsenderProperty().get();
    }

    public void setTblEmployeeByIdsender(TblEmployee tblEmployeeByIdsender) {
        tblEmployeeByIdsenderProperty().set(tblEmployeeByIdsender);
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

    public final ObjectProperty<TblLocation> tblLocationByIdlocationDestinationProperty() {
        return this.tblLocationByIdlocationDestination;
    }

    public TblLocation getTblLocationByIdlocationDestination() {
        return tblLocationByIdlocationDestinationProperty().get();
    }

    public void setTblLocationByIdlocationDestination(TblLocation tblLocationByIdlocationDestination) {
        tblLocationByIdlocationDestinationProperty().set(tblLocationByIdlocationDestination);
    }

    public final ObjectProperty<TblLocation> tblLocationByIdlocationSourceProperty() {
        return this.tblLocationByIdlocationSource;
    }

    public TblLocation getTblLocationByIdlocationSource() {
        return tblLocationByIdlocationSourceProperty().get();
    }

    public void setTblLocationByIdlocationSource(TblLocation tblLocationByIdlocationSource) {
        tblLocationByIdlocationSourceProperty().set(tblLocationByIdlocationSource);
    }

    public final StringProperty codeIcProperty() {
        return this.codeIc;
    }

    public String getCodeIc() {
        return codeIcProperty().get();
    }

    public void setCodeIc(String codeIc) {
        codeIcProperty().set(codeIc);
    }

    public final ObjectProperty<Date> icdateProperty() {
        return this.icdate;
    }

    public Date getIcdate() {
        return icdateProperty().get();
    }

    public void setIcdate(Date icdate) {
        icdateProperty().set(icdate);
    }

    public final StringProperty icnoteProperty() {
        return this.icnote;
    }

    public String getIcnote() {
        return icnoteProperty().get();
    }

    public void setIcnote(String icnote) {
        icnoteProperty().set(icnote);
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

    public Set getTblInComingDetails() {
        return this.tblInComingDetails;
    }

    public void setTblInComingDetails(Set tblInComingDetails) {
        this.tblInComingDetails = tblInComingDetails;
    }

}
