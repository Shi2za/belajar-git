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
public class ClassPrintDetailCustomer {
    List<ClassPrintIdentitasCustomer>identitasCustomer;
    List<ClassPrintKontakCustomer> kontakCustomer;
    List<ClassPrintRekeningCustomer>rekeningCustomer;

    public ClassPrintDetailCustomer() {
    }

    public List<ClassPrintIdentitasCustomer> getIdentitasCustomer() {
        return identitasCustomer;
    }

    public void setIdentitasCustomer(List<ClassPrintIdentitasCustomer> identitasCustomer) {
        this.identitasCustomer = identitasCustomer;
    }

    public List<ClassPrintKontakCustomer> getKontakCustomer() {
        return kontakCustomer;
    }

    public void setKontakCustomer(List<ClassPrintKontakCustomer> kontakCustomer) {
        this.kontakCustomer = kontakCustomer;
    }

    public List<ClassPrintRekeningCustomer> getRekeningCustomer() {
        return rekeningCustomer;
    }

    public void setRekeningCustomer(List<ClassPrintRekeningCustomer> rekeningCustomer) {
        this.rekeningCustomer = rekeningCustomer;
    }
}
