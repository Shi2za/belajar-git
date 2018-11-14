/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hotelfx.helper;

import hotelfx.persistence.model.LogCompanyBalanceCashFlow;
import java.io.Serializable;
import java.math.BigDecimal;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 *
 * @author Andreas
 */
public class ClassDataCompanyBalanceTransferReceived implements Serializable{
  private final ObjectProperty<LogCompanyBalanceCashFlow> companyBalanceCashFlow;
  private final StringProperty noteBalace;
  private final BooleanProperty isTransfer;
  private final BooleanProperty isReceived;
  private final ObjectProperty<BigDecimal> balance;


  public ClassDataCompanyBalanceTransferReceived() {
  this.companyBalanceCashFlow = new SimpleObjectProperty<>();
  this.noteBalace = new SimpleStringProperty();
  this.isTransfer = new SimpleBooleanProperty();
  this.isReceived = new SimpleBooleanProperty();
  this.balance = new SimpleObjectProperty<>();
  }

  public ClassDataCompanyBalanceTransferReceived(LogCompanyBalanceCashFlow companyBalanceCashFlow, String noteBalace, boolean isTransfer, boolean isReceived, BigDecimal balance) {
  this();
  companyBalanceCashFlowProperty().set(companyBalanceCashFlow);
  noteBalaceProperty().set(noteBalace);
  isTransferProperty().set(isTransfer);
  isReceivedProperty().set(isReceived);
  balanceProperty().set(balance);
  }

  public final ObjectProperty<LogCompanyBalanceCashFlow> companyBalanceCashFlowProperty() {
          return this.companyBalanceCashFlow;
  }

  public LogCompanyBalanceCashFlow getCompanyBalanceCashFlow() {
          return companyBalanceCashFlowProperty().get();
  }

  public void setCompanyBalanceCashFlow(LogCompanyBalanceCashFlow companyBalanceCashFlow) {
          companyBalanceCashFlowProperty().set(companyBalanceCashFlow);
  }

  public final StringProperty noteBalaceProperty() {
          return this.noteBalace;
  }

  public String getNoteBalace() {
          return noteBalaceProperty().get();
  }

  public void setNoteBalace(String noteBalace) {
          noteBalaceProperty().set(noteBalace);
  }

  public final BooleanProperty isTransferProperty() {
          return this.isTransfer;
  }

  public boolean getIsTransfer() {
          return isTransferProperty().get();
  }

  public void setIsTransfer(boolean isTransfer) {
          isTransferProperty().set(isTransfer);
  }

  public final BooleanProperty isReceivedProperty() {
          return this.isReceived;
  }

  public boolean getIsReceived() {
          return isReceivedProperty().get();
  }

  public void setIsReceived(boolean isReceived) {
          isReceivedProperty().set(isReceived);
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
