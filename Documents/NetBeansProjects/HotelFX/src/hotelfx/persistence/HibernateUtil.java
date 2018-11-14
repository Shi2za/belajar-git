/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hotelfx.persistence;

import hotelfx.helper.ClassSession;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

/**
 * Hibernate Utility class with a convenient method to get Session Factory
 * object.
 *
 * @author ANDRI
 */
public class HibernateUtil {

    public static final String DEFAULT_CONFIGURATION_PATH = "/hotelfx/persistence/hibernate.cfg.xml";

    private static SessionFactory sessionFactory;

    static {
        System.out.println("start static");
        //set configuration from 'hibernate.cfg.xml'
        Configuration conf = new Configuration();
        conf.configure(DEFAULT_CONFIGURATION_PATH);

        //load data DB Connection from file configuration
        DBConnectionSetting dbConnection = new DBConnectionSetting();

        if (dbConnection.loadDataConnection()) {
            //configuration has been found, use it (changed value from previous configuration)
            System.out.println("test");
            configurationSynch(conf, dbConnection);
        }

        //create session factory from configuration
        sessionFactory = null;
        try {
            sessionFactory = conf.buildSessionFactory();
        } catch (Throwable ex) {
            // Log the exception. 
            System.err.println("Initial SessionFactory creation failed : " + ex);
//            throw new ExceptionInInitializerError(ex);
        }
        System.out.println("end static");
    }

    public static SessionFactory getSessionFactory() {
        return sessionFactory;
    }
    
//    public static SessionFactory getSessionFactory() {
//        if(ClassSession.checkUserSession()){
//            return sessionFactory;
//        }else{
//            return null;
//        }
//    }

    public static void configurationSynch(Configuration conf,
            DBConnectionSetting fileSetting) {
        conf.setProperty("hibernate.connection.url", fileSetting.getURL());
        conf.setProperty("hibernate.connection.username", fileSetting.getDbUsername());
        conf.setProperty("hibernate.connection.password", fileSetting.getDbPassword());
        conf.setProperty("hibernate.enable_lazy_load_no_trans", "true");
        conf.setProperty("hibernate.dialect", "org.hibernate.dialect.SQLServer2012Dialect");
    }

    /*
     * if you use sessionFactory.getCurrentSession(), 
        you'll obtain a "current session" which is bound to the lifecycle of the transaction 
        and will be automatically flushed and closed when the transaction ends (commit or rollback).
     * if you decide to use sessionFactory.openSession(), 
        you'll have to manage the session yourself 
        and to flush and close it "manually".
     */
}
