/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hotelfx.helper;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 *
 * @author ANDRI
 */
public class ClassFolderManager {
    
    public static final String imageSystemRootPath = System.getProperty("user.dir") + "/resources/Image";
    
    public static final String imageClientRootPath = System.getProperty("user.dir") + "/resources/Client/Image";
    
    public static final String reportSystemRootPath = System.getProperty("user.dir") + "/resources/Report";
    
    public static final String reportClientRootPath = System.getProperty("user.dir") + "/resources/Client/Report";
    
    public static void copyImage(String sourcePath, String fileName, String typeDesDir) throws IOException {
        File sourceDir = new File(sourcePath);
        File destinationDir = new File((typeDesDir.equals("System") ? imageSystemRootPath : imageClientRootPath) + "/" + fileName);
        InputStream in = new FileInputStream(sourceDir);
        OutputStream out = new FileOutputStream(destinationDir);
        // Copy the bits from instream to outstream
        byte[] buf = new byte[1024];
        int len;
        while ((len = in.read(buf)) > 0) {
            out.write(buf, 0, len);
        }
        in.close();
        out.close();
    }
    
    public String saveReport(File sourceLocation, String name, String typeDesDir) throws IOException {
        File destinationDir = new File((typeDesDir.equals("System") ? reportSystemRootPath : reportClientRootPath));
        if (!destinationDir.exists()) {
            destinationDir.mkdirs();
        }
        destinationDir = new File(destinationDir + "\\" + name + ".pdf");
        InputStream in = new FileInputStream(sourceLocation);
        OutputStream out = new FileOutputStream(destinationDir);
        // Copy the bits from instream to outstream
        byte[] buf = new byte[1024];
        int len;
        while ((len = in.read(buf)) > 0) {
            out.write(buf, 0, len);
        }
        in.close();
        out.close();
        return name;
    }
}
