package hotelfx.persistence.model;
// Generated Aug 21, 2017 1:49:32 PM by Hibernate Tools 4.3.1

import java.util.HashSet;
import java.util.Set;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 * RefMemorandumInvoiceStatus generated by hbm2java
 */
public class RefMemorandumInvoiceStatus implements java.io.Serializable {

    private final IntegerProperty idstatus;
    private final StringProperty statusName;
    private final StringProperty statusNote;

    private Set tblMemorandumInvoices;

    public RefMemorandumInvoiceStatus() {
        this.idstatus = new SimpleIntegerProperty();
        this.statusName = new SimpleStringProperty();
        this.statusNote = new SimpleStringProperty();

        this.tblMemorandumInvoices = new HashSet(0);
    }

    public RefMemorandumInvoiceStatus(int idstatus) {
        this();
        idstatusProperty().set(idstatus);
    }

    public RefMemorandumInvoiceStatus(int idstatus, String statusName, String statusNote, Set tblMemorandumInvoices) {
        this();
        idstatusProperty().set(idstatus);
        statusNameProperty().set(statusName);
        statusNoteProperty().set(statusNote);
        this.tblMemorandumInvoices = tblMemorandumInvoices;
    }

    public RefMemorandumInvoiceStatus(RefMemorandumInvoiceStatus refMemorandumInvoiceStatus) {
        this();
        idstatusProperty().set(refMemorandumInvoiceStatus.getIdstatus());
        statusNameProperty().set(refMemorandumInvoiceStatus.getStatusName());
        statusNoteProperty().set(refMemorandumInvoiceStatus.getStatusNote());

        this.tblMemorandumInvoices = refMemorandumInvoiceStatus.getTblMemorandumInvoices();
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

    public Set getTblMemorandumInvoices() {
        return this.tblMemorandumInvoices;
    }

    public void setTblMemorandumInvoices(Set tblMemorandumInvoices) {
        this.tblMemorandumInvoices = tblMemorandumInvoices;
    }

}
