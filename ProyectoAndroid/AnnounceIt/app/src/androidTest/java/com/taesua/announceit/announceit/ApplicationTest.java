package com.taesua.announceit.announceit;

import android.app.Application;
import android.test.ApplicationTestCase;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * <a href="http://d.android.com/tools/testing/testing_android.html">Testing Fundamentals</a>
 */
public class ApplicationTest extends ApplicationTestCase<Application> {
    public ApplicationTest() {
        super(Application.class);
    }

    /**
     * Created by Encarna on 26/02/2015.
     */
    public static class DBConnection {

        private static DBConnection instance = null;
        private static final String URL = "";
        private static final String USER = "";
        private static final String PASS = "";
        private static Connection connection = null;

        private DBConnection() {}

        public static DBConnection getInstance() {
            if(instance == null)
                instance = new DBConnection();
            return instance;
        }

        private Connection conectar() {
            Connection conn = null;
            try {
                getClass();
                conn = DriverManager.getConnection(URL, USER, PASS);
            } catch(SQLException e) {
                e.printStackTrace();
            }
            return conn;
        }

        //Connection conn = com.taesua.announceit.announceit.ApplicationTest.DBConnection.getInstance().getConnection();
    }
}