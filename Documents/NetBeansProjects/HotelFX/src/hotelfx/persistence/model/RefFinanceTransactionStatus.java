package hotelfx.persistence.model;
// Generated Dec 11, 2017 3:30:19 PM by Hibernate Tools 4.3.1

import java.util.HashSet;
import java.util.Set;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 * RefFinanceTransactionStatus generated by hbm2java
 */
public class RefFinanceTransactionStatus implements java.io.Serializable {

    private final IntegerProperty idstatus;
    private final StringProperty statusName;
    private final StringProperty statusNote;

    private Set tblHotelReceivables;
    private Set tblHotelPayables;

    public RefFinanceTransactionStatus() {
        this.idstatus = new SimpleIntegerProperty();
        this.statusName = new SimpleStringProperty();
        this.statusNote = new SimpleStringProperty();

        this.tblHotelReceivables = new HashSet(0);
        this.tblHotelPayables = new HashSet(0);
    }

    public RefFinanceTransactionStatus(int idstatus) {
        this();
        idstatusProperty().set(idstatus);
    }

    public RefFinanceTransactionStatus(int idstatus, String statusName, String statusNote, Set tblHotelReceivables, Set tblHotelPayables) {
        this();
        idstatusProperty().set(idstatus);
        statusNameProperty().set(statusName);
        statusNoteProperty().set(statusNote);

        this.tblHotelReceivables = tblHotelReceivables;
        this.tblHotelPayables = tblHotelPayables;
    }

    public RefFinanceTransactionStatus(RefFinanceTransactionStatus refFinanceTransactionStatus) {
        this();
        idstatusProperty().set(refFinanceTransactionStatus.getIdstatus());
        statusNameProperty().set(refFinanceTransactionStatus.getStatusName());
        statusNoteProperty().set(refFinanceTransactionStatus.getStatusNote());

        this.tblHotelReceivables = refFinanceTransactionStatus.getTblHotelReceivables();
        this.tblHotelPayables = refFinanceTransactionStatus.getTblHotelPayables();
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

    public Set getTblHotelReceivables() {
        return this.tblHotelReceivables;
    }

    public void setTblHotelReceivables(Set tblHotelReceivables) {
        this.tblHotelReceivables = tblHotelReceivables;
    }

    public Set getTblHotelPayables() {
        return this.tblHotelPayables;
    }

    public void setTblHotelPayables(Set tblHotelPayables) {
        this.tblHotelPayables = tblHotelPayables;
    }

}