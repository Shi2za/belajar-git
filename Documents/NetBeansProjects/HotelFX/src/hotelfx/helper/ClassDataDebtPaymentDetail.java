/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hotelfx.helper;

import hotelfx.persistence.model.TblEmployee;
import java.math.BigDecimal;
import java.sql.Date;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 *
 * @author Andreas
 */
public class ClassDataDebtPaymentDetail {
      private final ObjectProperty<Date> dateDebt;
      private final StringProperty description;
      private final ObjectProperty<TblEmployee> employee;
      private final ObjectProperty<BigDecimal> debtNominal;
      private final ObjectProperty<BigDecimal> paymentNominal;
      private final ObjectProperty<BigDecimal> balance;


      public ClassDataDebtPaymentDetail() {
      this.dateDebt = new SimpleObjectProperty<>();
      this.description = new SimpleStringProperty();
      this.employee = new SimpleObjectProperty<>();
      this.debtNominal = new SimpleObjectProperty<>();
      this.paymentNominal = new SimpleObjectProperty<>();
      this.balance = new SimpleObjectProperty<>();

      }

      public ClassDataDebtPaymentDetail(Date dateDebt, String description, TblEmployee employee, BigDecimal debtNominal, BigDecimal paymentNominal, BigDecimal balance) {
      this();
      dateDebtProperty().set(dateDebt);
      descriptionProperty().set(description);
      employeeProperty().set(employee);
      debtNominalProperty().set(debtNominal);
      paymentNominalProperty().set(paymentNominal);
      balanceProperty().set(balance);
      }

      public final ObjectProperty<Date> dateDebtProperty() {
              return this.dateDebt;
      }

      public Date getDateDebt() {
              return dateDebtProperty().get();
      }

      public void setDateDebt(Date dateDebt) {
              dateDebtProperty().set(dateDebt);
      }

      public final StringProperty descriptionProperty() {
              return this.description;
      }

      public String getDescription() {
              return descriptionProperty().get();
      }

      public void setDescription(String description) {
              descriptionProperty().set(description);
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

      public final ObjectProperty<BigDecimal> debtNominalProperty() {
              return this.debtNominal;
      }

      public BigDecimal getDebtNominal() {
              return debtNominalProperty().get();
      }

      public void setDebtNominal(BigDecimal debtNominal) {
              debtNominalProperty().set(debtNominal);
      }

      public final ObjectProperty<BigDecimal> paymentNominalProperty() {
              return this.paymentNominal;
      }

      public BigDecimal getPaymentNominal() {
              return paymentNominalProperty().get();
      }

      public void setPaymentNominal(BigDecimal paymentNominal) {
              paymentNominalProperty().set(paymentNominal);
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
