package hotelfx.persistence.model;
// Generated Aug 25, 2018 8:41:00 AM by Hibernate Tools 4.3.1

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.LongProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleLongProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 * TblRoom generated by hbm2java
 */
public class TblRoom implements java.io.Serializable {

    private final LongProperty idroom;
    private final ObjectProperty<RefRecordStatus> refRecordStatus;
    private final ObjectProperty<RefRoomStatus> refRoomStatus;
    private final ObjectProperty<TblEmployee> tblEmployeeByCreateBy;
    private final ObjectProperty<TblEmployee> tblEmployeeByLastUpdateBy;
    private final ObjectProperty<TblLocation> tblLocation;
    private final ObjectProperty<TblRoomType> tblRoomType;
    private final StringProperty codeRoom;
    private final StringProperty roomName;
    private final IntegerProperty smokingStatus;
    private final StringProperty roomNote;
    private final StringProperty roomStatusNote;
    private final ObjectProperty<Date> createDate;
    private final ObjectProperty<Date> lastUpdateDate;

    private Set tblReservationCheckInOuts;
    private Set tblRoomStatusDetails;
    private Set tblRoomCheckHouseKeepingAttendances;
    private Set tblRoomChecks;
    private Set tblRoomStatusChangeHistories;

    public TblRoom() {
        this.idroom = new SimpleLongProperty();
        this.refRecordStatus = new SimpleObjectProperty<>();
        this.refRoomStatus = new SimpleObjectProperty<>();
        this.tblEmployeeByCreateBy = new SimpleObjectProperty<>();
        this.tblEmployeeByLastUpdateBy = new SimpleObjectProperty<>();
        this.tblLocation = new SimpleObjectProperty<>();
        this.tblRoomType = new SimpleObjectProperty<>();
        this.codeRoom = new SimpleStringProperty();
        this.roomName = new SimpleStringProperty();
        this.smokingStatus = new SimpleIntegerProperty();
        this.roomNote = new SimpleStringProperty();
        this.roomStatusNote = new SimpleStringProperty();
        this.createDate = new SimpleObjectProperty<>();
        this.lastUpdateDate = new SimpleObjectProperty<>();

        this.tblReservationCheckInOuts = new HashSet(0);
        this.tblRoomStatusDetails = new HashSet(0);
        this.tblRoomCheckHouseKeepingAttendances = new HashSet(0);
        this.tblRoomChecks = new HashSet(0);
        this.tblRoomStatusChangeHistories = new HashSet(0);
    }

    public TblRoom(long idroom, TblLocation tblLocation) {
        this();
        idroomProperty().set(idroom);
        tblLocationProperty().set(tblLocation);
    }

    public TblRoom(long idroom, RefRecordStatus refRecordStatus, RefRoomStatus refRoomStatus, TblEmployee tblEmployeeByCreateBy, TblEmployee tblEmployeeByLastUpdateBy, TblLocation tblLocation, TblRoomType tblRoomType, String codeRoom, String roomName, Integer smokingStatus, String roomNote, String roomStatusNote, Date createDate, Date lastUpdateDate, Set tblReservationCheckInOuts, Set tblRoomStatusDetails, Set tblRoomCheckHouseKeepingAttendances, Set tblRoomChecks, Set tblRoomStatusChangeHistories) {
        this();
        idroomProperty().set(idroom);
        refRecordStatusProperty().set(refRecordStatus);
        refRoomStatusProperty().set(refRoomStatus);
        tblEmployeeByCreateByProperty().set(tblEmployeeByCreateBy);
        tblEmployeeByLastUpdateByProperty().set(tblEmployeeByLastUpdateBy);
        tblLocationProperty().set(tblLocation);
        tblRoomTypeProperty().set(tblRoomType);
        codeRoomProperty().set(codeRoom);
        roomNameProperty().set(roomName);
        smokingStatusProperty().set(smokingStatus);
        roomNoteProperty().set(roomNote);
        roomStatusNoteProperty().set(roomStatusNote);
        createDateProperty().set(createDate);
        lastUpdateDateProperty().set(lastUpdateDate);

        this.tblReservationCheckInOuts = tblReservationCheckInOuts;
        this.tblRoomStatusDetails = tblRoomStatusDetails;
        this.tblRoomCheckHouseKeepingAttendances = tblRoomCheckHouseKeepingAttendances;
        this.tblRoomChecks = tblRoomChecks;
        this.tblRoomStatusChangeHistories = tblRoomStatusChangeHistories;
    }

    public TblRoom(TblRoom tblRoom) {
        this();
        idroomProperty().set(tblRoom.getIdroom());
        refRecordStatusProperty().set(tblRoom.getRefRecordStatus());
        refRoomStatusProperty().set(tblRoom.getRefRoomStatus());
        tblEmployeeByCreateByProperty().set(tblRoom.getTblEmployeeByCreateBy());
        tblEmployeeByLastUpdateByProperty().set(tblRoom.getTblEmployeeByLastUpdateBy());
        tblLocationProperty().set(tblRoom.getTblLocation());
        tblRoomTypeProperty().set(tblRoom.getTblRoomType());
        codeRoomProperty().set(tblRoom.getCodeRoom());
        roomNameProperty().set(tblRoom.getRoomName());
        smokingStatusProperty().set(tblRoom.getSmokingStatus());
        roomNoteProperty().set(tblRoom.getRoomNote());
        roomStatusNoteProperty().set(tblRoom.getRoomStatusNote());
        createDateProperty().set(tblRoom.getCreateDate());
        lastUpdateDateProperty().set(tblRoom.getLastUpdateDate());

        this.tblReservationCheckInOuts = tblRoom.getTblReservationCheckInOuts();
        this.tblRoomStatusDetails = tblRoom.getTblRoomStatusDetails();
        this.tblRoomCheckHouseKeepingAttendances = tblRoom.getTblRoomCheckHouseKeepingAttendances();
        this.tblRoomChecks = tblRoom.getTblRoomChecks();
        this.tblRoomStatusChangeHistories = tblRoom.getTblRoomStatusChangeHistories();
    }

    public final LongProperty idroomProperty() {
        return this.idroom;
    }

    public long getIdroom() {
        return idroomProperty().get();
    }

    public void setIdroom(long idroom) {
        idroomProperty().set(idroom);
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

    public final ObjectProperty<RefRoomStatus> refRoomStatusProperty() {
        return this.refRoomStatus;
    }

    public RefRoomStatus getRefRoomStatus() {
        return refRoomStatusProperty().get();
    }

    public void setRefRoomStatus(RefRoomStatus refRoomStatus) {
        refRoomStatusProperty().set(refRoomStatus);
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

    public final ObjectProperty<TblLocation> tblLocationProperty() {
        return this.tblLocation;
    }

    public TblLocation getTblLocation() {
        return tblLocationProperty().get();
    }

    public void setTblLocation(TblLocation tblLocation) {
        tblLocationProperty().set(tblLocation);
    }

    public final ObjectProperty<TblRoomType> tblRoomTypeProperty() {
        return this.tblRoomType;
    }

    public TblRoomType getTblRoomType() {
        return tblRoomTypeProperty().get();
    }

    public void setTblRoomType(TblRoomType tblRoomType) {
        tblRoomTypeProperty().set(tblRoomType);
    }

    public final StringProperty codeRoomProperty() {
        return this.codeRoom;
    }

    public String getCodeRoom() {
        return codeRoomProperty().get();
    }

    public void setCodeRoom(String codeRoom) {
        codeRoomProperty().set(codeRoom);
    }

    public final StringProperty roomNameProperty() {
        return this.roomName;
    }

    public String getRoomName() {
        return roomNameProperty().get();
    }

    public void setRoomName(String roomName) {
        roomNameProperty().set(roomName);
    }

    public final IntegerProperty smokingStatusProperty() {
        return this.smokingStatus;
    }

    public Integer getSmokingStatus() {
        return smokingStatusProperty().get();
    }

    public void setSmokingStatus(Integer smokingStatus) {
        smokingStatusProperty().set(smokingStatus);
    }

    public final StringProperty roomNoteProperty() {
        return this.roomNote;
    }

    public String getRoomNote() {
        return roomNoteProperty().get();
    }

    public void setRoomNote(String roomNote) {
        roomNoteProperty().set(roomNote);
    }

    public final StringProperty roomStatusNoteProperty() {
        return this.roomStatusNote;
    }

    public String getRoomStatusNote() {
        return roomStatusNoteProperty().get();
    }

    public void setRoomStatusNote(String roomStatusNote) {
        roomStatusNoteProperty().set(roomStatusNote);
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

    public Set getTblReservationCheckInOuts() {
        return this.tblReservationCheckInOuts;
    }

    public void setTblReservationCheckInOuts(Set tblReservationCheckInOuts) {
        this.tblReservationCheckInOuts = tblReservationCheckInOuts;
    }

    public Set getTblRoomStatusDetails() {
        return this.tblRoomStatusDetails;
    }

    public void setTblRoomStatusDetails(Set tblRoomStatusDetails) {
        this.tblRoomStatusDetails = tblRoomStatusDetails;
    }

    public Set getTblRoomCheckHouseKeepingAttendances() {
        return this.tblRoomCheckHouseKeepingAttendances;
    }

    public void setTblRoomCheckHouseKeepingAttendances(Set tblRoomCheckHouseKeepingAttendances) {
        this.tblRoomCheckHouseKeepingAttendances = tblRoomCheckHouseKeepingAttendances;
    }

    public Set getTblRoomChecks() {
        return this.tblRoomChecks;
    }

    public void setTblRoomChecks(Set tblRoomChecks) {
        this.tblRoomChecks = tblRoomChecks;
    }

    public Set getTblRoomStatusChangeHistories() {
        return this.tblRoomStatusChangeHistories;
    }

    public void setTblRoomStatusChangeHistories(Set tblRoomStatusChangeHistories) {
        this.tblRoomStatusChangeHistories = tblRoomStatusChangeHistories;
    }
    
    @Override
    public String toString() {
        return getRoomName();
    }

}