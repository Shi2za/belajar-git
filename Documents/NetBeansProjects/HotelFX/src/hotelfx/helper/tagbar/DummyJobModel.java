/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hotelfx.helper.tagbar;

import hotelfx.persistence.model.TblJob;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;

/**
 *
 * @author ANDRI
 */
public class DummyJobModel extends BaseModel{
    
    private final ObjectProperty<TblJob> dataJob = new SimpleObjectProperty<>();
    
    public DummyJobModel(TblJob dataJob){
        super(dataJob.getIdjob());
        this.dataJob.set(dataJob);
    }
    
    public ObjectProperty<TblJob> dataJobProperty(){
        return dataJob;
    }
    
    public TblJob getDataJob(){
        return dataJobProperty().get();
    }
    
    public void setDataJob(TblJob dataJob){
        dataJobProperty().set(dataJob);
    }
    
    @Override
    public String toString(){
        return getDataJob().getJobName();
    }

    @Override
    public String toString(StringType stringType) {
        String string = toString();
        if(stringType != null) switch (stringType){
            case ID:
                string = getId().toString();
            case NAME:
                string = getDataJob().getJobName();
            case ID_NAME:
                string = getId() + " / " + getDataJob().getJobName();
            default:
                break;
        }
        return string;
    }
    
}
