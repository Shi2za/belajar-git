/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hotelfx.helper;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 *
 * @author Andreas
 */
public class ClassMemorandumInvoiceBonusType {
  
    private final IntegerProperty idtype;
    private final StringProperty typeName;

    public ClassMemorandumInvoiceBonusType() {
    this.idtype = new SimpleIntegerProperty();
    this.typeName = new SimpleStringProperty();
    }

    public ClassMemorandumInvoiceBonusType(int idType) {
    this();
    idtypeProperty().set(idType);
    }

    public ClassMemorandumInvoiceBonusType(int idtype, String typeName) {
    this();
    idtypeProperty().set(idtype);
    typeNameProperty().set(typeName);
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
}
