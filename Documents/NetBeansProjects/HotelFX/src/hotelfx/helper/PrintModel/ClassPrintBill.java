/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hotelfx.helper.PrintModel;

import java.util.List;

/**
 *
 * @author Andreas
 */
public class ClassPrintBill {
    
   String codeInvoice;
   String invoiceDate;
   String cashier;
   String customerName;
   String arrivalDate;
   String departureDate;
   String emailHotel;
   String summary;
   String note;
   List<ClassPrintBillDetail>listBonReservasiDetail;

    public ClassPrintBill() {
    }

    public String getCodeInvoice() {
        return codeInvoice;
    }

    public void setCodeInvoice(String codeInvoice) {
        this.codeInvoice = codeInvoice;
    }

    public String getInvoiceDate() {
        return invoiceDate;
    }

    public void setInvoiceDate(String invoiceDate) {
        this.invoiceDate = invoiceDate;
    }

    public String getCashier() {
        return cashier;
    }

    public void setCashier(String cashier) {
        this.cashier = cashier;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getArrivalDate() {
        return arrivalDate;
    }

    public void setArrivalDate(String arrivalDate) {
        this.arrivalDate = arrivalDate;
    }

    public String getDepartureDate() {
        return departureDate;
    }

    public void setDepartureDate(String departureDate) {
        this.departureDate = departureDate;
    }

    public String getEmailHotel() {
        return emailHotel;
    }

    public void setEmailHotel(String emailHotel) {
        this.emailHotel = emailHotel;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public List<ClassPrintBillDetail> getListBonReservasiDetail() {
        return listBonReservasiDetail;
    }

    public void setListBonReservasiDetail(List<ClassPrintBillDetail> listBonReservasiDetail) {
        this.listBonReservasiDetail = listBonReservasiDetail;
    }
}
