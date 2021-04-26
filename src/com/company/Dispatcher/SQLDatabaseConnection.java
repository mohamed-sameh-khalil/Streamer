package com.company.Dispatcher;

import io.github.cdimascio.dotenv.Dotenv;

import java.sql.*;
import java.util.ArrayList;

public class SQLDatabaseConnection {
    String connectionUrl;
    Connection con;

    public SQLDatabaseConnection () {
        Dotenv dotenv = Dotenv.configure().directory(System.getProperty("user.dir") + "/").load();
        connectionUrl = "jdbc:sqlserver://" + dotenv.get("SQL_DATABASE_SERVER") + ":" + dotenv.get("SQL_DATABASE_PORT") + ";" +
                        "databaseName=" + dotenv.get("SQL_DATABASE_NAME") + ";" +
                        "user=" + dotenv.get("SQL_DATABASE_USER") + ";" +
                        "password=" + dotenv.get("SQL_DATABASE_PASSWORD");

        try {
            con = DriverManager.getConnection(connectionUrl);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public ArrayList<Camera> getCameras () {
        ResultSet rs = null;

        try {
            Statement stmt = con.createStatement();
            String SQL = "SELECT Cameras.Camera_ID, Places.IP, Cameras.PORT " +
                         "FROM Places " +
                         "INNER JOIN Cameras ON Places.Place_ID=Cameras.Place_ID";

            rs = stmt.executeQuery(SQL);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return fill_cameras (rs);
    }

    private ArrayList<Camera> fill_cameras (ResultSet rs) {
        ArrayList<Camera> cameras = new ArrayList<>();

        try {
            while (rs.next()) {
                int Camera_ID = rs.getInt("Camera_ID");
                String IP = rs.getString("IP");
                String PORT = rs.getString("PORT");

                cameras.add(new Camera(Camera_ID, IP, PORT));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return cameras;
    }

    public void printResultSet (ResultSet rs) {
        try {
            // Iterate through the data in the result set and display it.
            ResultSetMetaData rsmd = rs.getMetaData();
            int columnsNumber = rsmd.getColumnCount();
            while (rs.next()) {
                for (int i = 1; i <= columnsNumber; i++) {
                    if (i > 1) System.out.print(",  ");
                    String columnValue = rs.getString(i);
                    System.out.print(columnValue + " " + rsmd.getColumnName(i));
                }
                System.out.println("");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}