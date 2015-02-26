package com.taesua.announceit.announceit;

import org.xmlpull.v1.sax2.Driver;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Created by Encarna on 26/02/2015.
 */
public class DBConnection {

    private static DBConnection instance = null;
    private static final String URL ="mysql4.000webhost.com";
    private static final String USER ="a5280432_announc";
    private static final String PASS ="announceittaesua157";
    private static Connection connection = null;

    private DBConnection() {}

    private Connection conectar() {
        Connection conn = null;
        try{
            /*(new Driver()).*/getClass();
            conn = DriverManager.getConnection(URL, USER, PASS);
        }catch(SQLException e){
            e.printStackTrace();
        }
        return conn;
    }

    public Connection getConnection(){
        if(connection == null)
            connection = conectar();
        return connection;
    }

    public DBConnection getInstance(){
        if(instance == null)
            instance = new DBConnection();
        return instance;
    }
}
