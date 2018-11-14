/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package javaapplication1;

import java.math.BigInteger;
import java.security.MessageDigest;

/**
 *
 * @author ABC-Programmer
 */
public class JavaApplication1 {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic her
        String password = "Superadminin";
        System.out.println("bbb");
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(password.getBytes(), 0, password.length());
            BigInteger hslMd = new BigInteger(1, md.digest());
            String pwd = String.format("%1$032X", hslMd);
            System.out.println("" + pwd);
        } catch (Exception ex) {
             ex.getMessage();
            //Logger.getLogger(FUserAccountManagerImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
}
