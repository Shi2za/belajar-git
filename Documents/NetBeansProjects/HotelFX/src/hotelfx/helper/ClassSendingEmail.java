/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hotelfx.helper;

import hotelfx.persistence.model.SysDataHardCode;
import hotelfx.persistence.service.FLoginManager;
import hotelfx.persistence.service.FLoginManagerImpl;
import java.io.File;
import java.util.Date;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

/**
 *
 * @author Andreas
 */
public class ClassSendingEmail {
    public static boolean sendEmail(ClassDataEmail dataEmail){
        
          try {
            String host = getDataEmailHotel((long)35);
            String user = getDataEmailHotel((long)16);
            String pass = getDataEmailHotel((long)37);
            String to = dataEmail.getReceived();
            String from = dataEmail.getSender();
            String subject = dataEmail.getSubject();
            String messageText = dataEmail.getDetail();
                   
            Multipart multipart = new MimeMultipart();
            BodyPart messageBodyPart = new MimeBodyPart();
            messageBodyPart.setContent(messageText,"text/html; charset=utf-8");
            
            multipart.addBodyPart(messageBodyPart);
            
            // Part two is attachment
            BodyPart attachBodyPart = new MimeBodyPart();
            DataSource source = new FileDataSource(dataEmail.getFilePath());
            attachBodyPart.setDataHandler(new DataHandler(source));
            attachBodyPart.setFileName(new File(dataEmail.getFilePath()).getName());
            multipart.addBodyPart(attachBodyPart);
            boolean sessionDebug = false;
            
            Properties props = System.getProperties();

            props.put("mail.smtp.starttls.enable", "true");
            props.put("mail.smtp.host", host);
            props.put("mail.smtp.port",getDataEmailHotel((long)36));
            props.put("mail.smtp.auth", "true");
            props.put("mail.smtp.starttls.required", "true");
            props.put("mail.smtp.ssl.trust", host);

            java.security.Security.addProvider(new com.sun.net.ssl.internal.ssl.Provider());
            Session mailSession = Session.getDefaultInstance(props, null);
            mailSession.setDebug(sessionDebug);
            Message msg = new MimeMessage(mailSession);
            msg.setFrom(new InternetAddress(from));
            InternetAddress[] address = {new InternetAddress(to)};
            msg.setRecipients(Message.RecipientType.TO, address);
            msg.setSubject(subject);
            msg.setSentDate(new Date());
            msg.setContent(multipart);

            Transport transport = mailSession.getTransport("smtp");
            transport.connect(host, user, pass);
            transport.sendMessage(msg, msg.getAllRecipients());
            transport.close();
            System.out.println("message send successfully");
        } catch (MessagingException ex) {
            Logger.getLogger(ClassSendingEmail.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
        return true;
    }
    
      public static String getDataEmailHotel(long id) {
        //service
        FLoginManager fLoginManager = new FLoginManagerImpl();

        String data = "";
        
        SysDataHardCode sdhEmailHotel = fLoginManager.getDataSysDataHardCode(id);
   

        if (sdhEmailHotel != null && sdhEmailHotel.getDataHardCodeValue() != null) {
            data = sdhEmailHotel.getDataHardCodeValue();
        }

        return data;
    }
}
