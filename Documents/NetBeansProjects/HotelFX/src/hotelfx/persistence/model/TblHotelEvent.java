package hotelfx.persistence.model;
// Generated Oct 29, 2018 4:55:34 PM by Hibernate Tools 4.3.1

import java.math.BigDecimal;
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
 * TblHotelEvent generated by hbm2java
 */
public class TblHotelEvent implements java.io.Serializable {

    private final LongProperty idevent;
    private final ObjectProperty<RefRecordStatus> refRecordStatus;
    private final ObjectProperty<TblEmployee> tblEmployeeByCreateBy;
    private final ObjectProperty<TblEmployee> tblEmployeeByLastUpdateBy;
    private final StringProperty codeEvent;
    private final StringProperty eventName;
    private final IntegerProperty dayOfWeek;
    private final ObjectProperty<Date> beginEventDate;
    private final ObjectProperty<Date> endEventDate;
    private final StringProperty eventNote;
    private final ObjectProperty<BigDecimal> minTransaction;
    private final ObjectProperty<BigDecimal> maxTransaction;
    private final ObjectProperty<BigDecimal> discountPercentage;
    private final ObjectProperty<BigDecimal> roomDiscountPercentage;
    private final ObjectProperty<BigDecimal> itemDiscountPercentage;
    private final ObjectProperty<BigDecimal> serviceDiscountPercentage;
    private final ObjectProperty<BigDecimal> discountNominal;
    private final ObjectProperty<Date> createDate;
    private final ObjectProperty<Date> lastUpdateDate;

    private Set tblReservationRescheduleCanceleds;
    private Set tblReservationAdditionalServices;
    private Set tblReservationRoomPriceDetails;
    private Set tblReservationAdditionalItems;

    public TblHotelEvent() {
        this.idevent = new SimpleLongProperty();
        this.refRecordStatus = new SimpleObjectProperty<>();
        this.tblEmployeeByCreateBy = new SimpleObjectProperty<>();
        this.tblEmployeeByLastUpdateBy = new SimpleObjectProperty<>();
        this.codeEvent = new SimpleStringProperty();
        this.eventName = new SimpleStringProperty();
        this.dayOfWeek = new SimpleIntegerProperty();
        this.beginEventDate = new SimpleObjectProperty<>();
        this.endEventDate = new SimpleObjectProperty<>();
        this.eventNote = new SimpleStringProperty();
        this.minTransaction = new SimpleObjectProperty<>();
        this.maxTransaction = new SimpleObjectProperty<>();
        this.discountPercentage = new SimpleObjectProperty<>();
        this.roomDiscountPercentage = new SimpleObjectProperty<>();
        this.itemDiscountPercentage = new SimpleObjectProperty<>();
        this.serviceDiscountPercentage = new SimpleObjectProperty<>();
        this.discountNominal = new SimpleObjectProperty<>();
        this.createDate = new SimpleObjectProperty<>();
        this.lastUpdateDate = new SimpleObjectProperty<>();

        this.tblReservationRescheduleCanceleds = new HashSet(0);
        this.tblReservationAdditionalServices = new HashSet(0);
        this.tblReservationRoomPriceDetails = new HashSet(0);
        this.tblReservationAdditionalItems = new HashSet(0);
    }

    public TblHotelEvent(long idevent) {
        this();
        ideventProperty().set(idevent);
    }

    public TblHotelEvent(long idevent, RefRecordStatus refRecordStatus, TblEmployee tblEmployeeByCreateBy, TblEmployee tblEmployeeByLastUpdateBy, String codeEvent, String eventName, Integer dayOfWeek, Date beginEventDate, Date endEventDate, String eventNote, BigDecimal minTransaction, BigDecimal maxTransaction, BigDecimal discountPercentage, BigDecimal roomDiscountPercentage, BigDecimal itemDiscountPercentage, BigDecimal serviceDiscountPercentage, BigDecimal discountNominal, Date createDate, Date lastUpdateDate, Set tblReservationRescheduleCanceleds, Set tblReservationAdditionalServices, Set tblReservationRoomPriceDetails, Set tblReservationAdditionalItems) {
        this();
        ideventProperty().set(idevent);
        refRecordStatusProperty().set(refRecordStatus);
        tblEmployeeByCreateByProperty().set(tblEmployeeByCreateBy);
        tblEmployeeByLastUpdateByProperty().set(tblEmployeeByLastUpdateBy);
        codeEventProperty().set(codeEvent);
        eventNameProperty().set(eventName);
        dayOfWeekProperty().set(dayOfWeek);
        beginEventDateProperty().set(beginEventDate);
        endEventDateProperty().set(endEventDate);
        eventNoteProperty().set(eventNote);
        minTransactionProperty().set(minTransaction);
        maxTransactionProperty().set(maxTransaction);
        discountPercentageProperty().set(discountPercentage);
        roomDiscountPercentageProperty().set(roomDiscountPercentage);
        itemDiscountPercentageProperty().set(itemDiscountPercentage);
        serviceDiscountPercentageProperty().set(serviceDiscountPercentage);
        discountNominalProperty().set(discountNominal);
        createDateProperty().set(createDate);
        lastUpdateDateProperty().set(lastUpdateDate);

        this.tblReservationRescheduleCanceleds = tblReservationRescheduleCanceleds;
        this.tblReservationAdditionalServices = tblReservationAdditionalServices;
        this.tblReservationRoomPriceDetails = tblReservationRoomPriceDetails;
        this.tblReservationAdditionalItems = tblReservationAdditionalItems;
    }

    public TblHotelEvent(TblHotelEvent tblHotelEvent) {
        this();
        ideventProperty().set(tblHotelEvent.getIdevent());
        refRecordStatusProperty().set(tblHotelEvent.getRefRecordStatus());
        tblEmployeeByCreateByProperty().set(tblHotelEvent.getTblEmployeeByCreateBy());
        tblEmployeeByLastUpdateByProperty().set(tblHotelEvent.getTblEmployeeByLastUpdateBy());
        codeEventProperty().set(tblHotelEvent.getCodeEvent());
        eventNameProperty().set(tblHotelEvent.getEventName());
        dayOfWeekProperty().set(tblHotelEvent.getDayOfWeek());
        beginEventDateProperty().set(tblHotelEvent.getBeginEventDate());
        endEventDateProperty().set(tblHotelEvent.getEndEventDate());
        eventNoteProperty().set(tblHotelEvent.getEventNote());
        minTransactionProperty().set(tblHotelEvent.getMinTransaction());
        maxTransactionProperty().set(tblHotelEvent.getMaxTransaction());
        discountPercentageProperty().set(tblHotelEvent.getDiscountPercentage());
        roomDiscountPercentageProperty().set(tblHotelEvent.getRoomDiscountPercentage());
        itemDiscountPercentageProperty().set(tblHotelEvent.getItemDiscountPercentage());
        serviceDiscountPercentageProperty().set(tblHotelEvent.getServiceDiscountPercentage());
        discountNominalProperty().set(tblHotelEvent.getDiscountNominal());
        createDateProperty().set(tblHotelEvent.getCreateDate());
        lastUpdateDateProperty().set(tblHotelEvent.getLastUpdateDate());

        this.tblReservationRescheduleCanceleds = tblHotelEvent.getTblReservationRescheduleCanceleds();
        this.tblReservationAdditionalServices = tblHotelEvent.getTblReservationAdditionalServices();
        this.tblReservationRoomPriceDetails = tblHotelEvent.getTblReservationRoomPriceDetails();
        this.tblReservationAdditionalItems = tblHotelEvent.getTblReservationAdditionalItems();
    }

    public final LongProperty ideventProperty() {
        return this.idevent;
    }

    public long getIdevent() {
        return ideventProperty().get();
    }

    public void setIdevent(long idevent) {
        ideventProperty().set(idevent);
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

    public final StringProperty codeEventProperty() {
        return this.codeEvent;
    }

    public String getCodeEvent() {
        return codeEventProperty().get();
    }

    public void setCodeEvent(String codeEvent) {
        codeEventProperty().set(codeEvent);
    }

    public final StringProperty eventNameProperty() {
        return this.eventName;
    }

    public String getEventName() {
        return eventNameProperty().get();
    }

    public void setEventName(String eventName) {
        eventNameProperty().set(eventName);
    }

    public final IntegerProperty dayOfWeekProperty() {
        return this.dayOfWeek;
    }

    public Integer getDayOfWeek() {
        return dayOfWeekProperty().get();
    }

    public void setDayOfWeek(Integer dayOfWeek) {
        dayOfWeekProperty().set(dayOfWeek);
    }

    public final ObjectProperty<Date> beginEventDateProperty() {
        return this.beginEventDate;
    }

    public Date getBeginEventDate() {
        return beginEventDateProperty().get();
    }

    public void setBeginEventDate(Date beginEventDate) {
        beginEventDateProperty().set(beginEventDate);
    }

    public final ObjectProperty<Date> endEventDateProperty() {
        return this.endEventDate;
    }

    public Date getEndEventDate() {
        return endEventDateProperty().get();
    }

    public void setEndEventDate(Date endEventDate) {
        endEventDateProperty().set(endEventDate);
    }

    public final StringProperty eventNoteProperty() {
        return this.eventNote;
    }

    public String getEventNote() {
        return eventNoteProperty().get();
    }

    public void setEventNote(String eventNote) {
        eventNoteProperty().set(eventNote);
    }

    public final ObjectProperty<BigDecimal> minTransactionProperty() {
        return this.minTransaction;
    }

    public BigDecimal getMinTransaction() {
        return minTransactionProperty().get();
    }

    public void setMinTransaction(BigDecimal minTransaction) {
        minTransactionProperty().set(minTransaction);
    }

    public final ObjectProperty<BigDecimal> maxTransactionProperty() {
        return this.maxTransaction;
    }

    public BigDecimal getMaxTransaction() {
        return maxTransactionProperty().get();
    }

    public void setMaxTransaction(BigDecimal maxTransaction) {
        maxTransactionProperty().set(maxTransaction);
    }

    public final ObjectProperty<BigDecimal> discountPercentageProperty() {
        return this.discountPercentage;
    }

    public BigDecimal getDiscountPercentage() {
        return discountPercentageProperty().get();
    }

    public void setDiscountPercentage(BigDecimal discountPercentage) {
        discountPercentageProperty().set(discountPercentage);
    }

    public final ObjectProperty<BigDecimal> roomDiscountPercentageProperty() {
        return this.roomDiscountPercentage;
    }

    public BigDecimal getRoomDiscountPercentage() {
        return roomDiscountPercentageProperty().get();
    }

    public void setRoomDiscountPercentage(BigDecimal roomDiscountPercentage) {
        roomDiscountPercentageProperty().set(roomDiscountPercentage);
    }

    public final ObjectProperty<BigDecimal> itemDiscountPercentageProperty() {
        return this.itemDiscountPercentage;
    }

    public BigDecimal getItemDiscountPercentage() {
        return itemDiscountPercentageProperty().get();
    }

    public void setItemDiscountPercentage(BigDecimal itemDiscountPercentage) {
        itemDiscountPercentageProperty().set(itemDiscountPercentage);
    }

    public final ObjectProperty<BigDecimal> serviceDiscountPercentageProperty() {
        return this.serviceDiscountPercentage;
    }

    public BigDecimal getServiceDiscountPercentage() {
        return serviceDiscountPercentageProperty().get();
    }

    public void setServiceDiscountPercentage(BigDecimal serviceDiscountPercentage) {
        serviceDiscountPercentageProperty().set(serviceDiscountPercentage);
    }

    public final ObjectProperty<BigDecimal> discountNominalProperty() {
        return this.discountNominal;
    }

    public BigDecimal getDiscountNominal() {
        return discountNominalProperty().get();
    }

    public void setDiscountNominal(BigDecimal discountNominal) {
        discountNominalProperty().set(discountNominal);
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

    public Set getTblReservationRescheduleCanceleds() {
        return this.tblReservationRescheduleCanceleds;
    }

    public void setTblReservationRescheduleCanceleds(Set tblReservationRescheduleCanceleds) {
        this.tblReservationRescheduleCanceleds = tblReservationRescheduleCanceleds;
    }

    public Set getTblReservationAdditionalServices() {
        return this.tblReservationAdditionalServices;
    }

    public void setTblReservationAdditionalServices(Set tblReservationAdditionalServices) {
        this.tblReservationAdditionalServices = tblReservationAdditionalServices;
    }

    public Set getTblReservationRoomPriceDetails() {
        return this.tblReservationRoomPriceDetails;
    }

    public void setTblReservationRoomPriceDetails(Set tblReservationRoomPriceDetails) {
        this.tblReservationRoomPriceDetails = tblReservationRoomPriceDetails;
    }

    public Set getTblReservationAdditionalItems() {
        return this.tblReservationAdditionalItems;
    }

    public void setTblReservationAdditionalItems(Set tblReservationAdditionalItems) {
        this.tblReservationAdditionalItems = tblReservationAdditionalItems;
    }
    
    @Override
    public String toString() {
        return getEventName();
    }

}
