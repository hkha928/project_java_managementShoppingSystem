/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package javafxapplication5;
import java.sql.*;
/**
 *
 * @author hkha9
 */
public class MyConnection {
    public static Connection getConnection() {
        Connection con = null;
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            con = DriverManager.getConnection("jdbc:mysql://localhost:3306/qlbh","root","root");
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
        return con;
    }
}
