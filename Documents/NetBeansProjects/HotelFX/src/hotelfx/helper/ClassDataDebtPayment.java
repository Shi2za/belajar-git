/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hotelfx.helper;

import hotelfx.persistence.model.TblEmployee;
import java.io.Serializable;
import java.math.BigDecimal;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;

/**
 *
 * @author Andreas
 */
public class ClassDataDebtPayment implements Serializable{
    
  private final ObjectProperty<TblEmployee> employee;
  private final ObjectProperty<BigDecimal> debtTotal;
  private final ObjectProperty<BigDecimal> paymentTotal;
  private final ObjectProperty<BigDecimal> balance;

    public ClassDataDebtPayment() {
      this.employee = new SimpleObjectProperty<>();
      this.debtTotal = new SimpleObjectProperty<>();
      this.paymentTotal = new SimpleObjectProperty<>();
      this.balance = new SimpleObjectProperty<>();
    }

    public ClassDataDebtPayment(TblEmployee employee, BigDecimal debtTotal, BigDecimal paymentTotal, BigDecimal balance) {
     this();
     employeeProperty().set(employee);
     debtTotalProperty().set(debtTotal);
     paymentTotalProperty().set(paymentTotal);
     balanceProperty().set(balance);
    }

    public final ObjectProperty<TblEmployee> employeeProperty() {
            return this.employee;
    }

    public TblEmployee getEmployee() {
            return employeeProperty().get();
    }

    public void setEmployee(TblEmployee employee) {
            employeeProperty().set(employee);
    }

    public final ObjectProperty<BigDecimal> debtTotalProperty() {
            return this.debtTotal;
    }

    public BigDecimal getDebtTotal() {
            return debtTotalProperty().get();
    }

    public void setDebtTotal(BigDecimal debtTotal) {
            debtTotalProperty().set(debtTotal);
    }

    public final ObjectProperty<BigDecimal> paymentTotalProperty() {
            return this.paymentTotal;
    }

    public BigDecimal getPaymentTotal() {
            return paymentTotalProperty().get();
    }

    public void setPaymentTotal(BigDecimal paymentTotal) {
            paymentTotalProperty().set(paymentTotal);
    }

    public final ObjectProperty<BigDecimal> balanceProperty() {
            return this.balance;
    }

    public BigDecimal getBalance() {
            return balanceProperty().get();
    }

    public void setBalance(BigDecimal balance) {
            balanceProperty().set(balance);
    }
}
