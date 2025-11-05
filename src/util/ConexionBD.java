package util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConexionBD {

    // Datos de conexión
    private static final String URL = "jdbc:mysql://localhost:3306/tienda_online";
    private static final String USER = "root";
    private static final String PASSWORD = "root123";

    // Metodo que devuelve la conexión
    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}