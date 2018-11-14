package hotelfx.persistence.model;
// Generated Aug 21, 2017 1:49:32 PM by Hibernate Tools 4.3.1

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
 * TblReservationSeason generated by hbm2java
 */
public class TblReservationSeason implements java.io.Serializable {

    private final LongProperty idseason;
    private final ObjectProperty<RefRecordStatus> refRecordStatus;
    private final ObjectProperty<TblEmployee> tblEmployeeByCreateBy;
    private final ObjectProperty<TblEmployee> tblEmployeeByLastUpdateBy;
    private final StringProperty codeSeason;
    private final StringProperty seasonName;
    private final StringProperty seasonNote;
    private final ObjectProperty<Date> createDate;
    private final ObjectProperty<Date> lastUpdateDate;

    private Set tblReservationCalendarBars;

    public TblReservationSeason() {
        this.idseason = new SimpleLongProperty();
        this.refRecordStatus = new SimpleObjectProperty<>();
        this.tblEmployeeByCreateBy = new SimpleObjectProperty<>();
        this.tblEmployeeByLastUpdateBy = new SimpleObjectProperty<>();
        this.codeSeason = new SimpleStringProperty();
        this.seasonName = new SimpleStringProperty();
        this.seasonNote = new SimpleStringProperty();
        this.createDate = new SimpleObjectProperty<>();
        this.lastUpdateDate = new SimpleObjectProperty<>();

        this.tblReservationCalendarBars = new HashSet(0);
    }

    public TblReservationSeason(long idseason) {
        this();
        idseasonProperty().set(idseason);
    }

    public TblReservationSeason(long idseason, RefRecordStatus refRecordStatus, TblEmployee tblEmployeeByCreateBy, TblEmployee tblEmployeeByLastUpdateBy, String codeSeason, String seasonName, String seasonNote, Date createDate, Date lastUpdateDate, Set tblReservationCalendarBars) {
        this();
        idseasonProperty().set(idseason);
        refRecordStatusProperty().set(refRecordStatus);
        tblEmployeeByCreateByProperty().set(tblEmployeeByCreateBy);
        tblEmployeeByLastUpdateByProperty().set(tblEmployeeByLastUpdateBy);
        codeSeasonProperty().set(codeSeason);
        seasonNameProperty().set(seasonName);
        seasonNoteProperty().set(seasonNote);
        createDateProperty().set(createDate);
        lastUpdateDateProperty().set(lastUpdateDate);

        this.tblReservationCalendarBars = tblReservationCalendarBars;
    }

    public TblReservationSeason(TblReservationSeason tblReservationSeason) {
        this();
        idseasonProperty().set(tblReservationSeason.getIdseason());
        refRecordStatusProperty().set(tblReservationSeason.getRefRecordStatus());
        tblEmployeeByCreateByProperty().set(tblReservationSeason.getTblEmployeeByCreateBy());
        tblEmployeeByLastUpdateByProperty().set(tblReservationSeason.getTblEmployeeByLastUpdateBy());
        codeSeasonProperty().set(tblReservationSeason.getCodeSeason());
        seasonNameProperty().set(tblReservationSeason.getSeasonName());
        seasonNoteProperty().set(tblReservationSeason.getSeasonNote());
        createDateProperty().set(tblReservationSeason.getCreateDate());
        lastUpdateDateProperty().set(tblReservationSeason.getLastUpdateDate());

        this.tblReservationCalendarBars = tblReservationSeason.getTblReservationCalendarBars();
    }

    public final LongProperty idseasonProperty() {
        return this.idseason;
    }

    public long getIdseason() {
        return idseasonProperty().get();
    }

    public void setIdseason(long idseason) {
        idseasonProperty().set(idseason);
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

    public final StringProperty codeSeasonProperty() {
        return this.codeSeason;
    }

    public String getCodeSeason() {
        return codeSeasonProperty().get();
    }

    public void setCodeSeason(String codeSeason) {
        codeSeasonProperty().set(codeSeason);
    }

    public final StringProperty seasonNameProperty() {
        return this.seasonName;
    }

    public String getSeasonName() {
        return seasonNameProperty().get();
    }

    public void setSeasonName(String seasonName) {
        seasonNameProperty().set(seasonName);
    }

    public final StringProperty seasonNoteProperty() {
        return this.seasonNote;
    }

    public String getSeasonNote() {
        return seasonNoteProperty().get();
    }

    public void setSeasonNote(String seasonNote) {
        seasonNoteProperty().set(seasonNote);
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

    public Set getTblReservationCalendarBars() {
        return this.tblReservationCalendarBars;
    }

    public void setTblReservationCalendarBars(Set tblReservationCalendarBars) {
        this.tblReservationCalendarBars = tblReservationCalendarBars;
    }

    @Override
    public String toString() {
        return getSeasonName();
    }

}
