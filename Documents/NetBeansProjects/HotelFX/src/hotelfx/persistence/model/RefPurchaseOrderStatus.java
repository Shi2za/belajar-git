package hotelfx.persistence.model;
// Generated Aug 21, 2017 1:49:32 PM by Hibernate Tools 4.3.1

import java.util.HashSet;
import java.util.Set;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 * RefPurchaseOrderStatus generated by hbm2java
 */
public class RefPurchaseOrderStatus implements java.io.Serializable {

    private final IntegerProperty idstatus;
    private final StringProperty statusName;
    private final StringProperty statusNote;

    private Set tblPurchaseOrders;

    public RefPurchaseOrderStatus() {
        this.idstatus = new SimpleIntegerProperty();
        this.statusName = new SimpleStringProperty();
        this.statusNote = new SimpleStringProperty();

        this.tblPurchaseOrders = new HashSet(0);
    }

    public RefPurchaseOrderStatus(int idstatus) {
        this();
        idstatusProperty().set(idstatus);
    }

    public RefPurchaseOrderStatus(int idstatus, String statusName, String statusNote, Set tblPurchaseOrders) {
        this();
        idstatusProperty().set(idstatus);
        statusNameProperty().set(statusName);
        statusNoteProperty().set(statusNote);

        this.tblPurchaseOrders = tblPurchaseOrders;
    }

    public RefPurchaseOrderStatus(RefPurchaseOrderStatus refPurchaseOrderStatus) {
        this();
        idstatusProperty().set(refPurchaseOrderStatus.getIdstatus());
        statusNameProperty().set(refPurchaseOrderStatus.getStatusName());
        statusNoteProperty().set(refPurchaseOrderStatus.getStatusNote());

        this.tblPurchaseOrders = refPurchaseOrderStatus.getTblPurchaseOrders();
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

    public Set getTblPurchaseOrders() {
        return this.tblPurchaseOrders;
    }

    public void setTblPurchaseOrders(Set tblPurchaseOrders) {
        this.tblPurchaseOrders = tblPurchaseOrders;
    }

}
