/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hotelfx.helper;

import hotelfx.persistence.model.SysPasswordDeleteDebt;
import hotelfx.persistence.model.TblCalendarEmployeeDebt;
import java.io.Serializable;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;

/**
 *
 * @author Andreas
 */
public class ClassDataPasswordDeleteDebt implements Serializable{
    private final ObjectProperty<TblCalendarEmployeeDebt> employeeDebt;
    private final ObjectProperty<SysPasswordDeleteDebt> passwordDeleteDebt;


    public ClassDataPasswordDeleteDebt() {
        this.employeeDebt = new SimpleObjectProperty<>();
        this.passwordDeleteDebt = new SimpleObjectProperty<>();
    }

    public ClassDataPasswordDeleteDebt(TblCalendarEmployeeDebt employeeDebt, SysPasswordDeleteDebt passwordDeleteDebt) {
        this();
        employeeDebtProperty().set(employeeDebt);
        passwordDeleteDebtProperty().set(passwordDeleteDebt);

    }
    
    public ClassDataPasswordDeleteDebt(ClassDataPasswordDeleteDebt classDataPasswordDeleteDebt){
        this();
        employeeDebtProperty().set(classDataPasswordDeleteDebt.getEmployeeDebt());
        passwordDeleteDebtProperty().set(classDataPasswordDeleteDebt.getPasswordDeleteDebt());
    }
    
    public final ObjectProperty<TblCalendarEmployeeDebt> employeeDebtProperty() {
            return this.employeeDebt;
    }

    public TblCalendarEmployeeDebt getEmployeeDebt() {
            return employeeDebtProperty().get();
    }

    public void setEmployeeDebt(TblCalendarEmployeeDebt employeeDebt) {
            employeeDebtProperty().set(employeeDebt);
    }

    public final ObjectProperty<SysPasswordDeleteDebt> passwordDeleteDebtProperty() {
            return this.passwordDeleteDebt;
    }

    public SysPasswordDeleteDebt getPasswordDeleteDebt() {
            return passwordDeleteDebtProperty().get();
    }

    public void setPasswordDeleteDebt(SysPasswordDeleteDebt passwordDeleteDebt) {
            passwordDeleteDebtProperty().set(passwordDeleteDebt);
    }
}
