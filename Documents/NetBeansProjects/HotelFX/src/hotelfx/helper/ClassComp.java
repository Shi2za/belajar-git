/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hotelfx.helper;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author ANDRI
 */
public class ClassComp {
 
    public static String getBiosID(){
        try {
            return readID("bios", "serialnumber");
        } catch (IOException ex) {
            Logger.getLogger(ClassComp.class.getName()).log(Level.SEVERE, null, ex);
        }
        return "Unknown";
    }
    
    public static InetAddress getInetAddress() {
        try {
            return InetAddress.getLocalHost();
        } catch (Exception ex) {
            System.out.println(ex.toString());
        }
        return null;
    }

    public static byte[] getAddress() {
        InetAddress add = getInetAddress();
        if (add != null) {
            return add.getAddress();
        } else {
            return null;
        }
    }

    public static String getHostName() {
        InetAddress add = getInetAddress();
        if (add != null) {
            return add.getHostName();
        } else {
            return "Unknown";
        }
    }

    public static String getHostAddress() {
        InetAddress add = getInetAddress();
        if (add != null) {
            return add.getHostAddress();
        } else {
            return "Unknown";
        }
    }

    public static String getCanonicalHostName() {
        InetAddress add = getInetAddress();
        if (add != null) {
            return add.getCanonicalHostName();
        } else {
            return "Unknown";
        }
    }

    private static String readID(String name, String attribute) throws IOException{
        String result = "";
        Process process = Runtime.getRuntime().exec(new String[] { "wmic", name, "get", attribute});
        process.getOutputStream().close();
        try (BufferedReader input = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
            String line;
            while ((line = input.readLine()) != null) {
                result += line;
            }
        }
        return result.toUpperCase().replace(" ", "").replace(attribute.toUpperCase(), "");
    }
    
}
