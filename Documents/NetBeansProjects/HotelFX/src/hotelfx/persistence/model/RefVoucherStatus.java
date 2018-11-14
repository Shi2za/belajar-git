package hotelfx.persistence.model;
// Generated Aug 21, 2017 1:49:32 PM by Hibernate Tools 4.3.1

import java.util.HashSet;
import java.util.Set;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 * RefVoucherStatus generated by hbm2java
 */
public class RefVoucherStatus implements java.io.Serializable {

    private final IntegerProperty idstatus;
    private final StringProperty statusName;
    private final StringProperty statusNote;

    private Set tblReservationBreakfastVouchers;
    private Set tblReservationVouchers;

    public RefVoucherStatus() {
        this.idstatus = new SimpleIntegerProperty();
        this.statusName = new SimpleStringProperty();
        this.statusNote = new SimpleStringProperty();

        this.tblReservationBreakfastVouchers = new HashSet(0);
        this.tblReservationVouchers = new HashSet(0);
    }

    public RefVoucherStatus(int idstatus) {
        this();
        idstatusProperty().set(idstatus);
    }

    public RefVoucherStatus(int idstatus, String statusName, String statusNote, Set tblReservationBreakfastVouchers, Set tblReservationVouchers) {
        this();
        idstatusProperty().set(idstatus);
        statusNameProperty().set(statusName);
        statusNoteProperty().set(statusNote);

        this.tblReservationBreakfastVouchers = tblReservationBreakfastVouchers;
        this.tblReservationVouchers = tblReservationVouchers;
    }

    public RefVoucherStatus(RefVoucherStatus refVoucherStatus) {
        this();
        idstatusProperty().set(refVoucherStatus.getIdstatus());
        statusNameProperty().set(refVoucherStatus.getStatusName());
        statusNoteProperty().set(refVoucherStatus.getStatusNote());

        this.tblReservationBreakfastVouchers = refVoucherStatus.getTblReservationBreakfastVouchers();
        this.tblReservationVouchers = refVoucherStatus.getTblReservationVouchers();
    }

    public final IntegerProperty idstatusProperty() {
        return this.idstatus;
    }

    public int getIdstatus() {
        return idstatusProperty().get();
    }

    public void setIdstatus(int idstatus) {
        idstatusProperty().set(idstatus);
    }

    public final StringProperty statusNameProperty() {
        return this.statusName;
    }

    public String getStatusName() {
        return statusNameProperty().get();
    }

    public void setStatusName(String statusName) {
        statusNameProperty().set(statusName);
    }

    public final StringProperty statusNoteProperty() {
        return this.statusNote;
    }

    public String getStatusNote() {
        return statusNoteProperty().get();
    }

    public void setStatusNote(String statusNote) {
        statusNoteProperty().set(statusNote);
    }

    public Set getTblReservationBreakfastVouchers() {
        return this.tblReservationBreakfastVouchers;
    }

    public void setTblReservationBreakfastVouchers(Set tblReservationBreakfastVouchers) {
        this.tblReservationBreakfastVouchers = tblReservationBreakfastVouchers;
    }

    public Set getTblReservationVouchers() {
        return this.tblReservationVouchers;
    }

    public void setTblReservationVouchers(Set tblReservationVouchers) {
        this.tblReservationVouchers = tblReservationVouchers;
    }

}