package hotelfx.persistence.model;
// Generated Nov 12, 2018 3:38:24 PM by Hibernate Tools 4.3.1

import java.util.HashSet;
import java.util.Set;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 * RefFinanceTransactionPaymentType generated by hbm2java
 */
public class RefFinanceTransactionPaymentType implements java.io.Serializable {

    private final IntegerProperty idtype;
    private final StringProperty typeName;
    private final StringProperty typeNote;

    private Set tblCalendarEmployeeDebts;
    private Set tblHotelFinanceTransactions;
    private Set tblCalendarEmployeePaymentHistories;
    private Set tblReservationPayments;
    private Set tblHotelExpenseTransactions;

    public RefFinanceTransactionPaymentType() {
        this.idtype = new SimpleIntegerProperty();
        this.typeName = new SimpleStringProperty();
        this.typeNote = new SimpleStringProperty();

        this.tblCalendarEmployeeDebts = new HashSet(0);
        this.tblHotelFinanceTransactions = new HashSet(0);
        this.tblCalendarEmployeePaymentHistories = new HashSet(0);
        this.tblReservationPayments = new HashSet(0);
        this.tblHotelExpenseTransactions = new HashSet(0);
    }

    public RefFinanceTransactionPaymentType(int idtype) {
        this();
        idtypeProperty().set(idtype);
    }

    public RefFinanceTransactionPaymentType(int idtype, String typeName, String typeNote, Set tblCalendarEmployeeDebts, Set tblHotelFinanceTransactions, Set tblCalendarEmployeePaymentHistories, Set tblReservationPayments, Set tblHotelExpenseTransactions) {
        this();
        idtypeProperty().set(idtype);
        typeNameProperty().set(typeName);
        typeNoteProperty().set(typeNote);

        this.tblCalendarEmployeeDebts = tblCalendarEmployeeDebts;
        this.tblHotelFinanceTransactions = tblHotelFinanceTransactions;
        this.tblCalendarEmployeePaymentHistories = tblCalendarEmployeePaymentHistories;
        this.tblReservationPayments = tblReservationPayments;
        this.tblHotelExpenseTransactions = tblHotelExpenseTransactions;
    }

    public RefFinanceTransactionPaymentType(RefFinanceTransactionPaymentType refFinanceTransactionPaymentType) {
        this();
        idtypeProperty().set(refFinanceTransactionPaymentType.getIdtype());
        typeNameProperty().set(refFinanceTransactionPaymentType.getTypeName());
        typeNoteProperty().set(refFinanceTransactionPaymentType.getTypeNote());

        this.tblCalendarEmployeeDebts = refFinanceTransactionPaymentType.getTblCalendarEmployeeDebts();
        this.tblHotelFinanceTransactions = refFinanceTransactionPaymentType.getTblHotelFinanceTransactions();
        this.tblCalendarEmployeePaymentHistories = refFinanceTransactionPaymentType.getTblCalendarEmployeePaymentHistories();
        this.tblReservationPayments = refFinanceTransactionPaymentType.getTblReservationPayments();
        this.tblHotelExpenseTransactions = refFinanceTransactionPaymentType.getTblHotelExpenseTransactions();
    }

    public final IntegerProperty idtypeProperty() {
        return this.idtype;
    }

    public int getIdtype() {
        return idtypeProperty().get();
    }

    public void setIdtype(int idtype) {
        idtypeProperty().set(idtype);
    }

    public final StringProperty typeNameProperty() {
        return this.typeName;
    }

    public String getTypeName() {
        return typeNameProperty().get();
    }

    public void setTypeName(String typeName) {
        typeNameProperty().set(typeName);
    }

    public final StringProperty typeNoteProperty() {
        return this.typeNote;
    }

    public String getTypeNote() {
        return typeNoteProperty().get();
    }

    public void setTypeNote(String typeNote) {
        typeNoteProperty().set(typeNote);
    }

    public Set getTblCalendarEmployeeDebts() {
        return this.tblCalendarEmployeeDebts;
    }

    public void setTblCalendarEmployeeDebts(Set tblCalendarEmployeeDebts) {
        this.tblCalendarEmployeeDebts = tblCalendarEmployeeDebts;
    }

    public Set getTblHotelFinanceTransactions() {
        return this.tblHotelFinanceTransactions;
    }

    public void setTblHotelFinanceTransactions(Set tblHotelFinanceTransactions) {
        this.tblHotelFinanceTransactions = tblHotelFinanceTransactions;
    }

    public Set getTblCalendarEmployeePaymentHistories() {
        return this.tblCalendarEmployeePaymentHistories;
    }

    public void setTblCalendarEmployeePaymentHistories(Set tblCalendarEmployeePaymentHistories) {
        this.tblCalendarEmployeePaymentHistories = tblCalendarEmployeePaymentHistories;
    }

    public Set getTblReservationPayments() {
        return this.tblReservationPayments;
    }

    public void setTblReservationPayments(Set tblReservationPayments) {
        this.tblReservationPayments = tblReservationPayments;
    }

    public Set getTblHotelExpenseTransactions() {
        return this.tblHotelExpenseTransactions;
    }

    public void setTblHotelExpenseTransactions(Set tblHotelExpenseTransactions) {
        this.tblHotelExpenseTransactions = tblHotelExpenseTransactions;
    }

    @Override
    public String toString() {
        return getTypeName();
    }
    
}