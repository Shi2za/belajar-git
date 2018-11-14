/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hotelfx.helper;

import hotelfx.persistence.model.SysCode;
import hotelfx.persistence.model.SysDataHardCode;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.util.List;
import org.hibernate.Session;

/**
 *
 * @author ANDRI
 */
public class ClassCoder {
    
    public static String getCode(String dataName, Session session){
        String result = "";
        int defaultCodeDigitNumber = 0;
        SysDataHardCode sdhDefaultCodeDigitNumber = session.find(SysDataHardCode.class,(long) 5); //DefaultCodeDigitNumber = '5'
        if(sdhDefaultCodeDigitNumber != null
                && sdhDefaultCodeDigitNumber.getDataHardCodeValue() != null){
            defaultCodeDigitNumber = Integer.parseInt(sdhDefaultCodeDigitNumber.getDataHardCodeValue());
        }
        List<SysCode> list = session.getNamedQuery("findAllSysCodeByDataName")
                .setParameter("dataName", dataName)
                .list();
        if(list.size() == 1){
            //set code
            for(int i=0; i<defaultCodeDigitNumber; i++){
                result += "0";
            }
            result += String.valueOf(list.get(0).getCodeLastNumber());
            result = result.substring(result.length() - defaultCodeDigitNumber);
            result = list.get(0).getCodePrefix() + result;
            //update last number (data code)
            list.get(0).setCodeLastNumber(list.get(0).getCodeLastNumber() + 1);
            session.update(list.get(0));
        }
        return result;
    }
 
    public static String MD5(String password) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(password.getBytes(), 0, password.length());
            BigInteger hslMd = new BigInteger(1, md.digest());
            String pwd = String.format("%1$032X", hslMd);
            return pwd;
        } catch (Exception ex) {
            return ex.getMessage();
            //Logger.getLogger(FUserAccountManagerImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
}
