package hotelfx.persistence.model;
// Generated Aug 21, 2017 1:49:32 PM by Hibernate Tools 4.3.1

import java.math.BigDecimal;
import java.util.Date;

import javafx.beans.property.LongProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleLongProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 * TblSettingPbb generated by hbm2java
 */
public class TblSettingPbb implements java.io.Serializable {

    private final LongProperty idsettingPbb;
    private final ObjectProperty<RefRecordStatus> refRecordStatus;
    private final ObjectProperty<TblEmployee> tblEmployeeByCreateBy;
    private final ObjectProperty<TblEmployee> tblEmployeeByLastUpdateBy;
    private final StringProperty codeSettingPbb;
    private final StringProperty pbbname;
    private final LongProperty njopland;
    private final LongProperty njopbuilding;
    private final LongProperty njoptkp;
    private final ObjectProperty<BigDecimal> njkppercentage;
    private final ObjectProperty<BigDecimal> pbbpercentage;
    private final StringProperty pbbnote;
    private final ObjectProperty<Date> createDate;
    private final ObjectProperty<Date> lastUpdateDate;

    public TblSettingPbb() {
        this.idsettingPbb = new SimpleLongProperty();
        this.refRecordStatus = new SimpleObjectProperty<>();
        this.tblEmployeeByCreateBy = new SimpleObjectProperty<>();
        this.tblEmployeeByLastUpdateBy = new SimpleObjectProperty<>();
        this.codeSettingPbb = new SimpleStringProperty();
        this.pbbname = new SimpleStringProperty();
        this.njopland = new SimpleLongProperty();
        this.njopbuilding = new SimpleLongProperty();
        this.njoptkp = new SimpleLongProperty();
        this.njkppercentage = new SimpleObjectProperty<>();
        this.pbbpercentage = new SimpleObjectProperty<>();
        this.pbbnote = new SimpleStringProperty();
        this.createDate = new SimpleObjectProperty<>();
        this.lastUpdateDate = new SimpleObjectProperty<>();

    }

    public TblSettingPbb(long idsettingPbb) {
        this();
        idsettingPbbProperty().set(idsettingPbb);
    }

    public TblSettingPbb(long idsettingPbb, RefRecordStatus refRecordStatus, TblEmployee tblEmployeeByCreateBy, TblEmployee tblEmployeeByLastUpdateBy, String codeSettingPbb, String pbbname, Long njopland, Long njopbuilding, Long njoptkp, BigDecimal njkppercentage, BigDecimal pbbpercentage, String pbbnote, Date createDate, Date lastUpdateDate) {
        this();
        idsettingPbbProperty().set(idsettingPbb);
        refRecordStatusProperty().set(refRecordStatus);
        tblEmployeeByCreateByProperty().set(tblEmployeeByCreateBy);
        tblEmployeeByLastUpdateByProperty().set(tblEmployeeByLastUpdateBy);
        codeSettingPbbProperty().set(codeSettingPbb);
        pbbnameProperty().set(pbbname);
        njoplandProperty().set(njopland);
        njopbuildingProperty().set(njopbuilding);
        njoptkpProperty().set(njoptkp);
        njkppercentageProperty().set(njkppercentage);
        pbbpercentageProperty().set(pbbpercentage);
        pbbnoteProperty().set(pbbnote);
        createDateProperty().set(createDate);
        lastUpdateDateProperty().set(lastUpdateDate);

    }

    public TblSettingPbb(TblSettingPbb tblSettingPbb) {
        this();
        idsettingPbbProperty().set(tblSettingPbb.getIdsettingPbb());
        refRecordStatusProperty().set(tblSettingPbb.getRefRecordStatus());
        tblEmployeeByCreateByProperty().set(tblSettingPbb.getTblEmployeeByCreateBy());
        tblEmployeeByLastUpdateByProperty().set(tblSettingPbb.getTblEmployeeByLastUpdateBy());
        codeSettingPbbProperty().set(tblSettingPbb.getCodeSettingPbb());
        pbbnameProperty().set(tblSettingPbb.getPbbname());
        njoplandProperty().set(tblSettingPbb.getNjopland());
        njopbuildingProperty().set(tblSettingPbb.getNjopbuilding());
        njoptkpProperty().set(tblSettingPbb.getNjoptkp());
        njkppercentageProperty().set(tblSettingPbb.getNjkppercentage());
        pbbpercentageProperty().set(tblSettingPbb.getPbbpercentage());
        pbbnoteProperty().set(tblSettingPbb.getPbbnote());
        createDateProperty().set(tblSettingPbb.getCreateDate());
        lastUpdateDateProperty().set(tblSettingPbb.getLastUpdateDate());

    }

    public final LongProperty idsettingPbbProperty() {
        return this.idsettingPbb;
    }

    public long getIdsettingPbb() {
        return idsettingPbbProperty().get();
    }

    public void setIdsettingPbb(long idsettingPbb) {
        idsettingPbbProperty().set(idsettingPbb);
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

    public final StringProperty codeSettingPbbProperty() {
        return this.codeSettingPbb;
    }

    public String getCodeSettingPbb() {
        return codeSettingPbbProperty().get();
    }

    public void setCodeSettingPbb(String codeSettingPbb) {
        codeSettingPbbProperty().set(codeSettingPbb);
    }

    public final StringProperty pbbnameProperty() {
        return this.pbbname;
    }

    public String getPbbname() {
        return pbbnameProperty().get();
    }

    public void setPbbname(String pbbname) {
        pbbnameProperty().set(pbbname);
    }

    public final LongProperty njoplandProperty() {
        return this.njopland;
    }

    public Long getNjopland() {
        return njoplandProperty().get();
    }

    public void setNjopland(Long njopland) {
        njoplandProperty().set(njopland);
    }

    public final LongProperty njopbuildingProperty() {
        return this.njopbuilding;
    }

    public Long getNjopbuilding() {
        return njopbuildingProperty().get();
    }

    public void setNjopbuilding(Long njopbuilding) {
        njopbuildingProperty().set(njopbuilding);
    }

    public final LongProperty njoptkpProperty() {
        return this.njoptkp;
    }

    public Long getNjoptkp() {
        return njoptkpProperty().get();
    }

    public void setNjoptkp(Long njoptkp) {
        njoptkpProperty().set(njoptkp);
    }

    public final ObjectProperty<BigDecimal> njkppercentageProperty() {
        return this.njkppercentage;
    }

    public BigDecimal getNjkppercentage() {
        return njkppercentageProperty().get();
    }

    public void setNjkppercentage(BigDecimal njkppercentage) {
        njkppercentageProperty().set(njkppercentage);
    }

    public final ObjectProperty<BigDecimal> pbbpercentageProperty() {
        return this.pbbpercentage;
    }

    public BigDecimal getPbbpercentage() {
        return pbbpercentageProperty().get();
    }

    public void setPbbpercentage(BigDecimal pbbpercentage) {
        pbbpercentageProperty().set(pbbpercentage);
    }

    public final StringProperty pbbnoteProperty() {
        return this.pbbnote;
    }

    public String getPbbnote() {
        return pbbnoteProperty().get();
    }

    public void setPbbnote(String pbbnote) {
        pbbnoteProperty().set(pbbnote);
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
