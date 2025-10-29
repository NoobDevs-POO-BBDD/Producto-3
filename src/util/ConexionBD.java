package util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConexionBD {

    // Datos de conexión (se cambiara según la BD)
    private static final String URL = "jdbc:mysql://localhost:3306/mi_base_de_datos";
    private static final String USER = "usuario_mysql";
    private static final String PASSWORD = "contraseña_mysql";

    // Metodo que devuelve la conexión
    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}