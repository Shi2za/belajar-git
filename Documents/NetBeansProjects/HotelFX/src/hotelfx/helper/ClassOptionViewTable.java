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
public class ClassOptionViewTable {
 private final IntegerProperty idOption;
 private final StringProperty optionName;


  public ClassOptionViewTable() {
    this.idOption = new SimpleIntegerProperty();
    this.optionName = new SimpleStringProperty();
  }

  public ClassOptionViewTable(int idOption, String optionName) {
   this();
   idOptionProperty().set(idOption);
   optionNameProperty().set(optionName);
  }

public final IntegerProperty idOptionProperty() {
	return this.idOption;
}

public int getIdOption() {
	return idOptionProperty().get();
}

public void setIdOption(int idOption) {
	idOptionProperty().set(idOption);
}

public final StringProperty optionNameProperty() {
	return this.optionName;
}

public String getOptionName() {
	return optionNameProperty().get();
}

public void setOptionName(String optionName) {
	optionNameProperty().set(optionName);
}

 @Override
 public String toString(){
    return getOptionName();
}

}
