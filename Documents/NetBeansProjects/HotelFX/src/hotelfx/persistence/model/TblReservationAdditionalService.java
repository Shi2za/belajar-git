package hotelfx.persistence.model;
// Generated Oct 24, 2018 9:41:20 AM by Hibernate Tools 4.3.1

import java.math.BigDecimal;
import java.util.Date;

import javafx.beans.property.LongProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleLongProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 * TblReservationAdditionalService generated by hbm2java
 */
public class TblReservationAdditionalService implements java.io.Serializable {

    private final LongProperty idadditional;
    private final ObjectProperty<RefEndOfDayDataStatus> refEndOfDayDataStatus;
    private final ObjectProperty<RefRecordStatus> refRecordStatus;
    private final ObjectProperty<RefReservationBillType> refReservationBillType;
    private final ObjectProperty<TblBankEventCard> tblBankEventCard;
    private final ObjectProperty<TblEmployee> tblEmployeeByCreateBy;
    private final ObjectProperty<TblEmployee> tblEmployeeByLastUpdateBy;
    private final ObjectProperty<TblHotelEvent> tblHotelEvent;
    private final ObjectProperty<TblHotelPayable> tblHotelPayable;
    private final ObjectProperty<TblReservationRoomTypeDetail> tblReservationRoomTypeDetail;
    private final ObjectProperty<TblRoomService> tblRoomService;
    private final ObjectProperty<Date> additionalDate;
    private final ObjectProperty<BigDecimal> price;
    private final ObjectProperty<BigDecimal> discountPercentage;
    private final StringProperty restoTransactionNumber;
    private final StringProperty note;
    private final ObjectProperty<Date> createDate;
    private final ObjectProperty<Date> lastUpdateDate;

    public TblReservationAdditionalService() {
        this.idadditional = new SimpleLongProperty();
        this.refEndOfDayDataStatus = new SimpleObjectProperty<>();
        this.refRecordStatus = new SimpleObjectProperty<>();
        this.refReservationBillType = new SimpleObjectProperty<>();
        this.tblBankEventCard = new SimpleObjectProperty<>();
        this.tblEmployeeByCreateBy = new SimpleObjectProperty<>();
        this.tblEmployeeByLastUpdateBy = new SimpleObjectProperty<>();
        this.tblHotelEvent = new SimpleObjectProperty<>();
        this.tblHotelPayable = new SimpleObjectProperty<>();
        this.tblReservationRoomTypeDetail = new SimpleObjectProperty<>();
        this.tblRoomService = new SimpleObjectProperty<>();
        this.additionalDate = new SimpleObjectProperty<>();
        this.price = new SimpleObjectProperty<>();
        this.discountPercentage = new SimpleObjectProperty<>();
        this.restoTransactionNumber = new SimpleStringProperty();
        this.note = new SimpleStringProperty();
        this.createDate = new SimpleObjectProperty<>();
        this.lastUpdateDate = new SimpleObjectProperty<>();

    }

    public TblReservationAdditionalService(long idadditional) {
        this();
        idadditionalProperty().set(idadditional);
    }

    public TblReservationAdditionalService(long idadditional, RefEndOfDayDataStatus refEndOfDayDataStatus, RefRecordStatus refRecordStatus, RefReservationBillType refReservationBillType, TblBankEventCard tblBankEventCard, TblEmployee tblEmployeeByCreateBy, TblEmployee tblEmployeeByLastUpdateBy, TblHotelEvent tblHotelEvent, TblHotelPayable tblHotelPayable, TblReservationRoomTypeDetail tblReservationRoomTypeDetail, TblRoomService tblRoomService, Date additionalDate, BigDecimal price, BigDecimal discountPercentage, String restoTransactionNumber, String note, Date createDate, Date lastUpdateDate) {
        this();
        idadditionalProperty().set(idadditional);
        refEndOfDayDataStatusProperty().set(refEndOfDayDataStatus);
        refRecordStatusProperty().set(refRecordStatus);
        refReservationBillTypeProperty().set(refReservationBillType);
        tblBankEventCardProperty().set(tblBankEventCard);
        tblEmployeeByCreateByProperty().set(tblEmployeeByCreateBy);
        tblEmployeeByLastUpdateByProperty().set(tblEmployeeByLastUpdateBy);
        tblHotelEventProperty().set(tblHotelEvent);
        tblHotelPayableProperty().set(tblHotelPayable);
        tblReservationRoomTypeDetailProperty().set(tblReservationRoomTypeDetail);
        tblRoomServiceProperty().set(tblRoomService);
        additionalDateProperty().set(additionalDate);
        priceProperty().set(price);
        discountPercentageProperty().set(discountPercentage);
        restoTransactionNumberProperty().set(restoTransactionNumber);
        noteProperty().set(note);
        createDateProperty().set(createDate);
        lastUpdateDateProperty().set(lastUpdateDate);

    }

    public TblReservationAdditionalService(TblReservationAdditionalService tblReservationAdditionalService) {
        this();
        idadditionalProperty().set(tblReservationAdditionalService.getIdadditional());
        refEndOfDayDataStatusProperty().set(tblReservationAdditionalService.getRefEndOfDayDataStatus());
        refRecordStatusProperty().set(tblReservationAdditionalService.getRefRecordStatus());
        refReservationBillTypeProperty().set(tblReservationAdditionalService.getRefReservationBillType());
        tblBankEventCardProperty().set(tblReservationAdditionalService.getTblBankEventCard());
        tblEmployeeByCreateByProperty().set(tblReservationAdditionalService.getTblEmployeeByCreateBy());
        tblEmployeeByLastUpdateByProperty().set(tblReservationAdditionalService.getTblEmployeeByLastUpdateBy());
        tblHotelEventProperty().set(tblReservationAdditionalService.getTblHotelEvent());
        tblHotelPayableProperty().set(tblReservationAdditionalService.getTblHotelPayable());
        tblReservationRoomTypeDetailProperty().set(tblReservationAdditionalService.getTblReservationRoomTypeDetail());
        tblRoomServiceProperty().set(tblReservationAdditionalService.getTblRoomService());
        additionalDateProperty().set(tblReservationAdditionalService.getAdditionalDate());
        priceProperty().set(tblReservationAdditionalService.getPrice());
        discountPercentageProperty().set(tblReservationAdditionalService.getDiscountPercentage());
        restoTransactionNumberProperty().set(tblReservationAdditionalService.getRestoTransactionNumber());
        noteProperty().set(tblReservationAdditionalService.getNote());
        createDateProperty().set(tblReservationAdditionalService.getCreateDate());
        lastUpdateDateProperty().set(tblReservationAdditionalService.getLastUpdateDate());

    }

    public final LongProperty idadditionalProperty() {
        return this.idadditional;
    }

    public long getIdadditional() {
        return idadditionalProperty().get();
    }

    public void setIdadditional(long idadditional) {
        idadditionalProperty().set(idadditional);
    }

    public final ObjectProperty<RefEndOfDayDataStatus> refEndOfDayDataStatusProperty() {
        return this.refEndOfDayDataStatus;
    }

    public RefEndOfDayDataStatus getRefEndOfDayDataStatus() {
        return refEndOfDayDataStatusProperty().get();
    }

    public void setRefEndOfDayDataStatus(RefEndOfDayDataStatus refEndOfDayDataStatus) {
        refEndOfDayDataStatusProperty().set(refEndOfDayDataStatus);
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

    public final ObjectProperty<RefReservationBillType> refReservationBillTypeProperty() {
        return this.refReservationBillType;
    }

    public RefReservationBillType getRefReservationBillType() {
        return refReservationBillTypeProperty().get();
    }

    public void setRefReservationBillType(RefReservationBillType refReservationBillType) {
        refReservationBillTypeProperty().set(refReservationBillType);
    }

    public final ObjectProperty<TblBankEventCard> tblBankEventCardProperty() {
        return this.tblBankEventCard;
    }

    public TblBankEventCard getTblBankEventCard() {
        return tblBankEventCardProperty().get();
    }

    public void setTblBankEventCard(TblBankEventCard tblBankEventCard) {
        tblBankEventCardProperty().set(tblBankEventCard);
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

    public final ObjectProperty<TblHotelEvent> tblHotelEventProperty() {
        return this.tblHotelEvent;
    }

    public TblHotelEvent getTblHotelEvent() {
        return tblHotelEventProperty().get();
    }

    public void setTblHotelEvent(TblHotelEvent tblHotelEvent) {
        tblHotelEventProperty().set(tblHotelEvent);
    }

    public final ObjectProperty<TblHotelPayable> tblHotelPayableProperty() {
        return this.tblHotelPayable;
    }

    public TblHotelPayable getTblHotelPayable() {
        return tblHotelPayableProperty().get();
    }

    public void setTblHotelPayable(TblHotelPayable tblHotelPayable) {
        tblHotelPayableProperty().set(tblHotelPayable);
    }

    public final ObjectProperty<TblReservationRoomTypeDetail> tblReservationRoomTypeDetailProperty() {
        return this.tblReservationRoomTypeDetail;
    }

    public TblReservationRoomTypeDetail getTblReservationRoomTypeDetail() {
        return tblReservationRoomTypeDetailProperty().get();
    }

    public void setTblReservationRoomTypeDetail(TblReservationRoomTypeDetail tblReservationRoomTypeDetail) {
        tblReservationRoomTypeDetailProperty().set(tblReservationRoomTypeDetail);
    }

    public final ObjectProperty<TblRoomService> tblRoomServiceProperty() {
        return this.tblRoomService;
    }

    public TblRoomService getTblRoomService() {
        return tblRoomServiceProperty().get();
    }

    public void setTblRoomService(TblRoomService tblRoomService) {
        tblRoomServiceProperty().set(tblRoomService);
    }

    public final ObjectProperty<Date> additionalDateProperty() {
        return this.additionalDate;
    }

    public Date getAdditionalDate() {
        return additionalDateProperty().get();
    }

    public void setAdditionalDate(Date additionalDate) {
        additionalDateProperty().set(additionalDate);
    }

    public final ObjectProperty<BigDecimal> priceProperty() {
        return this.price;
    }

    public BigDecimal getPrice() {
        return priceProperty().get();
    }

    public void setPrice(BigDecimal price) {
        priceProperty().set(price);
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

    public final StringProperty restoTransactionNumberProperty() {
        return this.restoTransactionNumber;
    }

    public String getRestoTransactionNumber() {
        return restoTransactionNumberProperty().get();
    }

    public void setRestoTransactionNumber(String restoTransactionNumber) {
        restoTransactionNumberProperty().set(restoTransactionNumber);
    }

    public final StringProperty noteProperty() {
        return this.note;
    }

    public String getNote() {
        return noteProperty().get();
    }

    public void setNote(String note) {
        noteProperty().set(note);
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

}
