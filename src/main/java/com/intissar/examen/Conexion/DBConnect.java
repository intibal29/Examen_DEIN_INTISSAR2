package com.intissar.examen.Conexion;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DBConnect {
    private final Connection connection;

    // Logger para manejar errores
    private static final Logger LOGGER = Logger.getLogger(DBConnect.class.getName());

    /**
     * Constructor que inicializa la conexión con la base de datos.
     *
     * @throws SQLException Si hay errores al establecer la conexión.
     */
    public DBConnect() throws SQLException {
        Properties configuracion = loadConfiguration();

        String url = buildConnectionUrl(configuracion);
        Properties connectionProps = new Properties();
        connectionProps.setProperty("user", configuracion.getProperty("user"));
        connectionProps.setProperty("password", configuracion.getProperty("password"));

        try {
            connection = DriverManager.getConnection(url, connectionProps);
            connection.setAutoCommit(true);

            // Debug info
            DatabaseMetaData metaData = connection.getMetaData();
            LOGGER.info("Conectado a la base de datos: " + metaData.getDatabaseProductName());
            LOGGER.info("Versión de la base de datos: " + metaData.getDatabaseProductVersion());
            LOGGER.info("Driver: " + metaData.getDriverName());
            LOGGER.info("Versión del driver: " + metaData.getDriverVersion());
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Error al conectar a la base de datos", ex);
            throw ex;
        }
    }

    /**
     * Carga la configuración desde el archivo properties.
     *
     * @return Propiedades de configuración para la conexión a la base de datos.
     */
    private static Properties loadConfiguration() {
        File configFile = new File("configuracion.properties");
        Properties properties = new Properties();

        try (FileInputStream configFileReader = new FileInputStream(configFile)) {
            properties.load(configFileReader);
        } catch (IOException ex) {
            LOGGER.log(Level.SEVERE, "No se pudo cargar el archivo de configuración", ex);
            throw new RuntimeException("Configuración no encontrada en " + configFile.getPath(), ex);
        }

        return properties;
    }

    /**
     * Construye la URL de conexión a la base de datos.
     *
     * @param configuracion Propiedades de configuración.
     * @return URL de conexión para la base de datos.
     */
    private String buildConnectionUrl(Properties configuracion) {
        return "jdbc:mariadb://" + configuracion.getProperty("address") + ":" + configuracion.getProperty("port")
                + "/" + configuracion.getProperty("database") + "?serverTimezone=Europe/Madrid";
    }

    /**
     * Devuelve la conexión activa.
     *
     * @return La conexión a la base de datos.
     */
    public Connection getConnection() {
        return connection;
    }

    /**
     * Cierra la conexión con la base de datos.
     *
     * @throws SQLException Si ocurre un error al cerrar la conexión.
     */
    public void closeConnection() throws SQLException {
        if (connection != null && !connection.isClosed()) {
            connection.close();
        }
    }

    /**
     * Método para probar la conexión.
     *
     * @return Verdadero si la conexión está activa.
     */
    public boolean testConnection() {
        try {
            return connection != null && !connection.isClosed();
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Error al probar la conexión", ex);
            return false;
        }
    }
}
