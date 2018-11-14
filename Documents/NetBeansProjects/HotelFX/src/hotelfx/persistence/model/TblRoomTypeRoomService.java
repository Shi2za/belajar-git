package hotelfx.persistence.model;
// Generated May 2, 2018 1:18:04 PM by Hibernate Tools 4.3.1

import java.util.Date;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.LongProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleLongProperty;
import javafx.beans.property.SimpleObjectProperty;

/**
 * TblRoomTypeRoomService generated by hbm2java
 */
public class TblRoomTypeRoomService implements java.io.Serializable {

    private final LongProperty idrelation;
    private final ObjectProperty<RefRecordStatus> refRecordStatus;
    private final ObjectProperty<TblEmployee> tblEmployeeByLastUpdateBy;
    private final ObjectProperty<TblEmployee> tblEmployeeByCreateBy;
    private final ObjectProperty<TblRoomService> tblRoomService;
    private final ObjectProperty<TblRoomType> tblRoomType;
    private final IntegerProperty peopleNumber;
    private final BooleanProperty addAsAdditionalService;
    private final ObjectProperty<Date> createDate;
    private final ObjectProperty<Date> lastUpdateDate;

    public TblRoomTypeRoomService() {
        this.idrelation = new SimpleLongProperty();
        this.refRecordStatus = new SimpleObjectProperty<>();
        this.tblEmployeeByLastUpdateBy = new SimpleObjectProperty<>();
        this.tblEmployeeByCreateBy = new SimpleObjectProperty<>();
        this.tblRoomService = new SimpleObjectProperty<>();
        this.tblRoomType = new SimpleObjectProperty<>();
        this.peopleNumber = new SimpleIntegerProperty();
        this.addAsAdditionalService = new SimpleBooleanProperty();
        this.createDate = new SimpleObjectProperty<>();
        this.lastUpdateDate = new SimpleObjectProperty<>();

    }

    public TblRoomTypeRoomService(long idrelation) {
        this();
        idrelationProperty().set(idrelation);
    }

    public TblRoomTypeRoomService(long idrelation, RefRecordStatus refRecordStatus, TblEmployee tblEmployeeByLastUpdateBy, TblEmployee tblEmployeeByCreateBy, TblRoomService tblRoomService, TblRoomType tblRoomType, Integer peopleNumber, Boolean addAsAdditionalService, Date createDate, Date lastUpdateDate) {
        this();
        idrelationProperty().set(idrelation);
        refRecordStatusProperty().set(refRecordStatus);
        tblEmployeeByLastUpdateByProperty().set(tblEmployeeByLastUpdateBy);
        tblEmployeeByCreateByProperty().set(tblEmployeeByCreateBy);
        tblRoomServiceProperty().set(tblRoomService);
        tblRoomTypeProperty().set(tblRoomType);
        peopleNumberProperty().set(peopleNumber);
        addAsAdditionalServiceProperty().set(addAsAdditionalService);
        createDateProperty().set(createDate);
        lastUpdateDateProperty().set(lastUpdateDate);

    }

    public TblRoomTypeRoomService(TblRoomTypeRoomService tblRoomTypeRoomService) {
        this();
        idrelationProperty().set(tblRoomTypeRoomService.getIdrelation());
        refRecordStatusProperty().set(tblRoomTypeRoomService.getRefRecordStatus());
        tblEmployeeByLastUpdateByProperty().set(tblRoomTypeRoomService.getTblEmployeeByLastUpdateBy());
        tblEmployeeByCreateByProperty().set(tblRoomTypeRoomService.getTblEmployeeByCreateBy());
        tblRoomServiceProperty().set(tblRoomTypeRoomService.getTblRoomService());
        tblRoomTypeProperty().set(tblRoomTypeRoomService.getTblRoomType());
        peopleNumberProperty().set(tblRoomTypeRoomService.getPeopleNumber());
        addAsAdditionalServiceProperty().set(tblRoomTypeRoomService.getAddAsAdditionalService());
        createDateProperty().set(tblRoomTypeRoomService.getCreateDate());
        lastUpdateDateProperty().set(tblRoomTypeRoomService.getLastUpdateDate());

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

    public final ObjectProperty<TblEmployee> tblEmployeeByLastUpdateByProperty() {
        return this.tblEmployeeByLastUpdateBy;
    }

    public TblEmployee getTblEmployeeByLastUpdateBy() {
        return tblEmployeeByLastUpdateByProperty().get();
    }

    public void setTblEmployeeByLastUpdateBy(TblEmployee tblEmployeeByLastUpdateBy) {
        tblEmployeeByLastUpdateByProperty().set(tblEmployeeByLastUpdateBy);
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

    public final ObjectProperty<TblRoomService> tblRoomServiceProperty() {
        return this.tblRoomService;
    }

    public TblRoomService getTblRoomService() {
        return tblRoomServiceProperty().get();
    }

    public void setTblRoomService(TblRoomService tblRoomService) {
        tblRoomServiceProperty().set(tblRoomService);
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

    public final IntegerProperty peopleNumberProperty() {
        return this.peopleNumber;
    }

    public Integer getPeopleNumber() {
        return peopleNumberProperty().get();
    }

    public void setPeopleNumber(Integer peopleNumber) {
        peopleNumberProperty().set(peopleNumber);
    }

    public final BooleanProperty addAsAdditionalServiceProperty() {
        return this.addAsAdditionalService;
    }

    public Boolean getAddAsAdditionalService() {
        return addAsAdditionalServiceProperty().get();
    }

    public void setAddAsAdditionalService(Boolean addAsAdditionalService) {
        addAsAdditionalServiceProperty().set(addAsAdditionalService);
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
