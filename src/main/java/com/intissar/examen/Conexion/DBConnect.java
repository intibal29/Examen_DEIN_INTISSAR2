package com.intissar.examen.Conexion;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DBConnect {
    // Propiedades de conexión
    private static String driver;
    private static String url;
    private static String user;
    private static String password;

    // Conexión única para toda la aplicación
    private static Connection conexion = null;

    // Logger para manejar errores
    private static final Logger LOGGER = Logger.getLogger(DBConnect.class.getName());

    // Bloque estático para cargar la configuración
    static {
        cargarConfiguracion();
    }

    // Método para cargar la configuración desde el archivo properties
    private static void cargarConfiguracion() {
        Properties propiedades = new Properties();

        try (InputStream input = DBConnect.class.getClassLoader()
                .getResourceAsStream("configuracion.properties")) {

            if (input == null) {
                LOGGER.severe("No se puede encontrar el archivo de configuración");
                throw new IOException("Archivo de configuración no encontrado");
            }

            // Cargar propiedades
            propiedades.load(input);

            // Obtener valores
            driver = propiedades.getProperty("db.driver");
            url = propiedades.getProperty("db.url");
            user = propiedades.getProperty("db.user");
            password = propiedades.getProperty("db.password");

        } catch (IOException ex) {
            LOGGER.log(Level.SEVERE, "Error al cargar configuración", ex);
        }
    }

    // Método para obtener la conexión
    public static Connection getConexion() throws SQLException {
        // Si la conexión no existe o está cerrada, crear una nueva
        if (conexion == null || conexion.isClosed()) {
            try {
                // Cargar el driver
                Class.forName(driver);

                // Establecer la conexión
                conexion = DriverManager.getConnection(url, user, password);

            } catch (ClassNotFoundException ex) {
                LOGGER.log(Level.SEVERE, "Driver de base de datos no encontrado", ex);
                throw new SQLException("Error al cargar el driver de base de datos", ex);
            } catch (SQLException ex) {
                LOGGER.log(Level.SEVERE, "Error al establecer conexión", ex);
                throw ex;
            }
        }

        return conexion;
    }

    // Método para cerrar la conexión
    public static void cerrarConexion() {
        if (conexion != null) {
            try {
                if (!conexion.isClosed()) {
                    conexion.close();
                }
            } catch (SQLException ex) {
                LOGGER.log(Level.SEVERE, "Error al cerrar la conexión", ex);
            }
        }
    }

    // Método para probar la conexión
    public static boolean probarConexion() {
        try {
            Connection testConexion = getConexion();
            return testConexion != null && !testConexion.isClosed();
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Error al probar conexión", ex);
            return false;
        }
    }

    // Constructor privado para evitar instanciación
    private DBConnect() {
        throw new IllegalStateException("Utility class");
    }
}