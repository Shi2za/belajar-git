/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hotelfx.helper.tagbar;

import java.io.Serializable;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 *
 * @author Andreas
 */
public abstract class BaseModel implements Serializable{

    private ObjectProperty id;
    private StringProperty code;

    public BaseModel() {
        this.id = new SimpleObjectProperty(this, "id");
        this.code = new SimpleStringProperty(this, "code");
    }

    public BaseModel(Object id) {
        this.id = new SimpleObjectProperty(this, "id", id);
        this.code = new SimpleStringProperty(this, "code");
    }

    public final ObjectProperty idProperty() {
        return id;
    }

    public final StringProperty codeProperty() {
        return code;
    }

    public final Object getId() {
        return idProperty().get();
    }

    public final String getCode() {
        return codeProperty().get();
    }

    public final void setId(Object id) {
        idProperty().set(id);
    }

    public final void setCode(String code) {
        codeProperty().set(code);
    }

    @Override
    public String toString() {
        return getCode()!=null && !getCode().equalsIgnoreCase("") ? getCode() : String.valueOf(getId());
    }

    public abstract String toString(BaseModel.StringType stringType);

    public static enum StringType {
        ID, NAME, ID_NAME, NAME_QTY;
    }
}
