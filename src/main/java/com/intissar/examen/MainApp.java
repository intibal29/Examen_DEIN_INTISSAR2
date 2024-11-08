package com.intissar.examen;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Clase donde se ejecuta la aplicación principal
 * @autor Israel de Lamo Blas
 */
public class MainApp extends Application {

    private static final Logger LOGGER = Logger.getLogger(MainApp.class.getName());

    /**
     * Método principal donde se carga y se muestra la ventana de la aplicación
     *
     * @param stage la ventana principal de la aplicación
     * @throws IOException si ocurre un error al cargar el archivo FXML
     */
    @Override
    public void start(Stage stage) throws IOException {
        // Cargar el archivo FXML
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/intissar/examen/xml/main.fxml"));
        Scene scene = new Scene(fxmlLoader.load());

        // Configurar el escenario
        stage.setTitle("Gestión de Productos");
        stage.setResizable(true);
        stage.setScene(scene);

        // Establecer el icono de la aplicación
        try {
            stage.getIcons().add(new Image(getClass().getResourceAsStream("/com/intissar/examen/Imagenes/bollos.png")));
        } catch (Exception e) {
            LOGGER.log(Level.WARNING, "No se pudo cargar el icono de la aplicación", e);
        }

        // Mostrar la ventana
        stage.show();
    }

    /**
     * Función main donde se lanza la aplicación
     *
     * @param args parámetros por consola
     */
    public static void main(String[] args) {
        launch(args);
    }
}
