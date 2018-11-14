package hotelfx.persistence.model;
// Generated Nov 1, 2018 9:11:08 AM by Hibernate Tools 4.3.1

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javafx.beans.property.LongProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleLongProperty;
import javafx.beans.property.SimpleObjectProperty;

/**
 * TblRoomCheckHouseKeepingAttendance generated by hbm2java
 */
public class TblRoomCheckHouseKeepingAttendance implements java.io.Serializable {

    private final LongProperty idrelation;
    private final ObjectProperty<RefRecordStatus> refRecordStatus;
    private final ObjectProperty<RefRoomStatus> refRoomStatusByRoomStatusBefore;
    private final ObjectProperty<RefRoomStatus> refRoomStatusByRoomStatusAfter;
    private final ObjectProperty<TblEmployee> tblEmployeeByLastUpdatedBy;
    private final ObjectProperty<TblEmployee> tblEmployeeByCreatedBy;
    private final ObjectProperty<TblEmployee> tblEmployeeByIdchecker;
    private final ObjectProperty<TblRoom> tblRoom;
    private final ObjectProperty<Date> createdDate;
    private final ObjectProperty<Date> lastUpdatedDate;

    private Set tblRoomCheckHouseKeepingAttendanceDetails;

    public TblRoomCheckHouseKeepingAttendance() {
        this.idrelation = new SimpleLongProperty();
        this.refRecordStatus = new SimpleObjectProperty<>();
        this.refRoomStatusByRoomStatusBefore = new SimpleObjectProperty<>();
        this.refRoomStatusByRoomStatusAfter = new SimpleObjectProperty<>();
        this.tblEmployeeByLastUpdatedBy = new SimpleObjectProperty<>();
        this.tblEmployeeByCreatedBy = new SimpleObjectProperty<>();
        this.tblEmployeeByIdchecker = new SimpleObjectProperty<>();
        this.tblRoom = new SimpleObjectProperty<>();
        this.createdDate = new SimpleObjectProperty<>();
        this.lastUpdatedDate = new SimpleObjectProperty<>();

        this.tblRoomCheckHouseKeepingAttendanceDetails = new HashSet(0);
    }

    public TblRoomCheckHouseKeepingAttendance(long idrelation) {
        this();
        idrelationProperty().set(idrelation);
    }

    public TblRoomCheckHouseKeepingAttendance(long idrelation, RefRecordStatus refRecordStatus, RefRoomStatus refRoomStatusByRoomStatusBefore, RefRoomStatus refRoomStatusByRoomStatusAfter, TblEmployee tblEmployeeByLastUpdatedBy, TblEmployee tblEmployeeByCreatedBy, TblEmployee tblEmployeeByIdchecker, TblRoom tblRoom, Date createdDate, Date lastUpdatedDate, Set tblRoomCheckHouseKeepingAttendanceDetails) {
        this();
        idrelationProperty().set(idrelation);
        refRecordStatusProperty().set(refRecordStatus);
        refRoomStatusByRoomStatusBeforeProperty().set(refRoomStatusByRoomStatusBefore);
        refRoomStatusByRoomStatusAfterProperty().set(refRoomStatusByRoomStatusAfter);
        tblEmployeeByLastUpdatedByProperty().set(tblEmployeeByLastUpdatedBy);
        tblEmployeeByCreatedByProperty().set(tblEmployeeByCreatedBy);
        tblEmployeeByIdcheckerProperty().set(tblEmployeeByIdchecker);
        tblRoomProperty().set(tblRoom);
        createdDateProperty().set(createdDate);
        lastUpdatedDateProperty().set(lastUpdatedDate);

        this.tblRoomCheckHouseKeepingAttendanceDetails = tblRoomCheckHouseKeepingAttendanceDetails;
    }

    public TblRoomCheckHouseKeepingAttendance(TblRoomCheckHouseKeepingAttendance tblRoomCheckHouseKeepingAttendance) {
        this();
        idrelationProperty().set(tblRoomCheckHouseKeepingAttendance.getIdrelation());
        refRecordStatusProperty().set(tblRoomCheckHouseKeepingAttendance.getRefRecordStatus());
        refRoomStatusByRoomStatusBeforeProperty().set(tblRoomCheckHouseKeepingAttendance.getRefRoomStatusByRoomStatusBefore());
        refRoomStatusByRoomStatusAfterProperty().set(tblRoomCheckHouseKeepingAttendance.getRefRoomStatusByRoomStatusAfter());
        tblEmployeeByLastUpdatedByProperty().set(tblRoomCheckHouseKeepingAttendance.getTblEmployeeByLastUpdatedBy());
        tblEmployeeByCreatedByProperty().set(tblRoomCheckHouseKeepingAttendance.getTblEmployeeByCreatedBy());
        tblEmployeeByIdcheckerProperty().set(tblRoomCheckHouseKeepingAttendance.getTblEmployeeByIdchecker());
        tblRoomProperty().set(tblRoomCheckHouseKeepingAttendance.getTblRoom());
        createdDateProperty().set(tblRoomCheckHouseKeepingAttendance.getCreatedDate());
        lastUpdatedDateProperty().set(tblRoomCheckHouseKeepingAttendance.getLastUpdatedDate());

        this.tblRoomCheckHouseKeepingAttendanceDetails = tblRoomCheckHouseKeepingAttendance.getTblRoomCheckHouseKeepingAttendanceDetails();
    }

    public final LongProperty idrelationProperty() {
        return this.idrelation;
    }

    public long getIdrelation() {
        return idrelationProperty().get();
    }

    public void setIdrelation(long idrelation) {
        idrelationProperty().set(idrelation);
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

    public final ObjectProperty<RefRoomStatus> refRoomStatusByRoomStatusBeforeProperty() {
        return this.refRoomStatusByRoomStatusBefore;
    }

    public RefRoomStatus getRefRoomStatusByRoomStatusBefore() {
        return refRoomStatusByRoomStatusBeforeProperty().get();
    }

    public void setRefRoomStatusByRoomStatusBefore(RefRoomStatus refRoomStatusByRoomStatusBefore) {
        refRoomStatusByRoomStatusBeforeProperty().set(refRoomStatusByRoomStatusBefore);
    }

    public final ObjectProperty<RefRoomStatus> refRoomStatusByRoomStatusAfterProperty() {
        return this.refRoomStatusByRoomStatusAfter;
    }

    public RefRoomStatus getRefRoomStatusByRoomStatusAfter() {
        return refRoomStatusByRoomStatusAfterProperty().get();
    }

    public void setRefRoomStatusByRoomStatusAfter(RefRoomStatus refRoomStatusByRoomStatusAfter) {
        refRoomStatusByRoomStatusAfterProperty().set(refRoomStatusByRoomStatusAfter);
    }

    public final ObjectProperty<TblEmployee> tblEmployeeByLastUpdatedByProperty() {
        return this.tblEmployeeByLastUpdatedBy;
    }

    public TblEmployee getTblEmployeeByLastUpdatedBy() {
        return tblEmployeeByLastUpdatedByProperty().get();
    }

    public void setTblEmployeeByLastUpdatedBy(TblEmployee tblEmployeeByLastUpdatedBy) {
        tblEmployeeByLastUpdatedByProperty().set(tblEmployeeByLastUpdatedBy);
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

    public final ObjectProperty<TblEmployee> tblEmployeeByIdcheckerProperty() {
        return this.tblEmployeeByIdchecker;
    }

    public TblEmployee getTblEmployeeByIdchecker() {
        return tblEmployeeByIdcheckerProperty().get();
    }

    public void setTblEmployeeByIdchecker(TblEmployee tblEmployeeByIdchecker) {
        tblEmployeeByIdcheckerProperty().set(tblEmployeeByIdchecker);
    }

    public final ObjectProperty<TblRoom> tblRoomProperty() {
        return this.tblRoom;
    }

    public TblRoom getTblRoom() {
        return tblRoomProperty().get();
    }

    public void setTblRoom(TblRoom tblRoom) {
        tblRoomProperty().set(tblRoom);
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

    public final ObjectProperty<Date> lastUpdatedDateProperty() {
        return this.lastUpdatedDate;
    }

    public Date getLastUpdatedDate() {
        return lastUpdatedDateProperty().get();
    }

    public void setLastUpdatedDate(Date lastUpdatedDate) {
        lastUpdatedDateProperty().set(lastUpdatedDate);
    }

    public Set getTblRoomCheckHouseKeepingAttendanceDetails() {
        return this.tblRoomCheckHouseKeepingAttendanceDetails;
    }

    public void setTblRoomCheckHouseKeepingAttendanceDetails(Set tblRoomCheckHouseKeepingAttendanceDetails) {
        this.tblRoomCheckHouseKeepingAttendanceDetails = tblRoomCheckHouseKeepingAttendanceDetails;
    }

}