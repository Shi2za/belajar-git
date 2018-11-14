/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hotelfx.persistence;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

/**
 *
 * @author ANDRI
 */
public class DBConnectionSetting {

    private final static String dataConfigurationPath = "resources/Configuration/DatabaseConnection.cfg";

    /*
     * DATABASE PROVIDER :
     * MYSQL => mysql
     * SQL-SERVER => sqlserver
     */
    private final String dbProvider = "sqlserver";          //SQL-SERVER

    /*
     * HOSTNAME : ANDRI-PC
     */
    private StringProperty hostName = new SimpleStringProperty();

    public String getHostName() {
        return hostName.get();
    }

    public void setHostName(String value) {
        hostName.set(value);
    }

    public StringProperty hostNameProperty() {
        return hostName;
    }

    /*
     * PORT : 1433
     */
    private StringProperty port = new SimpleStringProperty();

    public String getPort() {
        return port.get();
    }

    public void setPort(String value) {
        port.set(value);
    }

    public StringProperty portProperty() {
        return port;
    }

    /*
     * DATABASENAME : Hotel
     */
    private StringProperty dbDatabaseName = new SimpleStringProperty();

    public String getDbDatabaseName() {
        return dbDatabaseName.get();
    }

    public void setDbDatabaseName(String value) {
        dbDatabaseName.set(value);
    }

    public StringProperty dbDatabaseNameProperty() {
        return dbDatabaseName;
    }

    /*
     * USERNAME : lenorahotel
     */
    private StringProperty dbUsername = new SimpleStringProperty();

    public String getDbUsername() {
        return dbUsername.get();
    }

    public void setDbUsername(String value) {
        dbUsername.set(value);
    }

    public StringProperty dbUsernameProperty() {
        return dbUsername;
    }

    /*
     * PASSWORD : lenorahotelsystem
     */
    private StringProperty dbPassword = new SimpleStringProperty();

    public String getDbPassword() {
        return dbPassword.get();
    }

    public void setDbPassword(String value) {
        dbPassword.set(value);
    }

    public StringProperty dbPasswordProperty() {
        return dbPassword;
    }

    private String errMsg = "";

    public String getErrMsg() {
        return errMsg;
    }

    public String getURL() {
        switch (dbProvider) {
            case "sqlserver":
                return "jdbc:sqlserver://"
                        + hostName.get()
                        + ":"
                        + port.get()
                        + ";DatabaseName="
                        + dbDatabaseName.get();
            case "mysql":
                return "jdbc:mysql://"
                        + hostName.get()
                        + ":"
                        + port.get()
                        + "/"
                        + dbDatabaseName.get();
        }
        return null;
    }

    public boolean loadDataConnection() {
        errMsg = "";
        String result = loadDataConfiguration(dataConfigurationPath);
        String[] attempt = result.split("\n");
        if (attempt.length == 5) {
            hostName.set(attempt[0]);
            port.set(attempt[1]);
            dbDatabaseName.set(attempt[2]);
            dbUsername.set(attempt[3]);
            dbPassword.setValue(attempt[4]);
            return true;
        } else {
            errMsg = "Data not match!";
            return false;
        }
    }

    public boolean saveFileDataConnection() {
        if (saveDataConfig(
                dataConfigurationPath,
                hostName.get(),
                port.get(),
                dbDatabaseName.get(),
                dbUsername.get(),
                dbPassword.get())) {
            loadDataConnection();
            return true;
        } else {
            return false;
        }
    }

    /*
     * FileStream (in)
     */
    private String loadDataConfiguration(String configFileName) {
        String line;
        String result = "";
        try {
            FileReader fileReader = new FileReader(configFileName);
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            while ((line = bufferedReader.readLine()) != null) {
                result += line + "\n";
            }
            bufferedReader.close();
        } catch (FileNotFoundException ex) {
            result = "";
            errMsg
                    = "Unable to open file '"
                    + configFileName + "'";
            System.out.println(ex.toString());
        } catch (IOException ex) {
            result = "";
            errMsg
                    = "Error reading file '"
                    + configFileName + "'";
            System.out.println(ex.toString());
        }
        return result;
    }

    /*
     * FileStream (out)
     */
    private boolean saveDataConfig(String configFileName,
            String hostName,
            String port,
            String databaseName,
            String userName,
            String password) {
        boolean save = true;
        try {
            FileWriter fileWriter = new FileWriter(configFileName);
            BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
            bufferedWriter.write(hostName);
            bufferedWriter.newLine();
            bufferedWriter.write(port);
            bufferedWriter.newLine();
            bufferedWriter.write(databaseName);
            bufferedWriter.newLine();
            bufferedWriter.write(userName);
            bufferedWriter.newLine();
            bufferedWriter.write(password);
            bufferedWriter.close();
        } catch (IOException ex) {
            save = false;
            errMsg
                    = "Error writing to file '"
                    + configFileName + "'";
            System.out.println(ex.toString());
        }
        return save;
    }

    /*
     * TEST - CONNECTION TO DATABSE
     */
    public boolean testConnectionToDB() {
        errMsg = "";

        //Configuration for session factory
        Configuration conf = new Configuration();
        conf.configure(HibernateUtil.DEFAULT_CONFIGURATION_PATH);
        HibernateUtil.configurationSynch(conf, this);
        //try create session factory from configuration
        SessionFactory sessionFactory = null;
        try {
            sessionFactory = conf.buildSessionFactory();
            return true;
        } catch (Exception ex) {
            errMsg = "Initial SessionFactory creation failed : \n"
                    + ex.toString();
            return false;
        } finally {
            if ((sessionFactory != null) && !sessionFactory.isClosed()) {
                sessionFactory.close();
            }
        }
    }
}
