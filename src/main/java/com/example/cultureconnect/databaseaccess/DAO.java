package com.example.cultureconnect.databaseaccess;
import java.sql.*;
public class DAO {
    Connection con;

    public DAO() {
        try{
            con = DriverManager.getConnection("jdbc:sqlserver://10.176.111.34:1433;database=CultureConnectDB;userName=CSe2023t_t_1;password=CSe2023tT1#23;encrypt=true;trustServerCertificate=true");
        } catch (SQLException e) {
            System.err.println("Can't connect to Database: " + e.getErrorCode() + e.getMessage());
        }
        System.out.println("Forbundet til databasen... ");
    }
}
