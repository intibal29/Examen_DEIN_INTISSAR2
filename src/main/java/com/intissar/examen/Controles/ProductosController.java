package com.intissar.examen.Controles;

import com.intissar.examen.DAO.ProductoDAO;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import com.intissar.examen.Modelo.Producto;

import java.io.File;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Controlador para manejar las interacciones con la vista de productos.
 * Permite realizar operaciones CRUD (Crear, Leer, Actualizar, Eliminar) sobre los productos.
 */
public class ProductosController {
    // Logger para registrar información y errores
    private static final Logger LOGGER = Logger.getLogger(ProductosController.class.getName());

    // Componentes FXML vinculados a la vista
    @FXML private TableView<Producto> tabla; // Tabla que muestra los productos
    @FXML private TableColumn<Producto, String> colCodigo; // Columna para el código del producto
    @FXML private TableColumn<Producto, String> colNombre; // Columna para el nombre del producto
    @FXML private TableColumn<Producto, Double> colPrecio; // Columna para el precio del producto
    @FXML private TextField txtCodigo; // Campo de texto para ingresar el código del producto
    @FXML private TextField txtNombre; // Campo de texto para ingresar el nombre del producto
    @FXML private TextField txtPrecio; // Campo de texto para ingresar el precio del producto
    @FXML private ImageView imagen; // Vista previa de la imagen del producto
    @FXML private Button btnCrear; // Botón para crear un nuevo producto
    @FXML private Button btnActualizar; // Botón para actualizar un producto existente

    // DAO para realizar operaciones en la base de datos
    private ProductoDAO productoDAO;

    // Ruta de la imagen seleccionada
    private String rutaImagen;

    /**
     * Inicializa el controlador, configura las columnas de la tabla y carga los productos desde la base de datos.
     */
    @FXML
    public void initialize() {
        productoDAO = new ProductoDAO(); // Inicializa el DAO para interactuar con la base de datos
        configurarColumnas(); // Configura las columnas de la tabla
        cargarProductos(); // Carga los productos desde la base de datos
    }

    /**
     * Configura las columnas de la tabla para mostrar la información del producto.
     */
    private void configurarColumnas() {
        colCodigo.setCellValueFactory(cellData -> cellData.getValue().codigoProperty());
        colNombre.setCellValueFactory(cellData -> cellData.getValue().nombreProperty());
        // colPrecio.setCellValueFactory(cellData -> cellData.getValue().precioProperty().clone());
    }

    /**
     * Carga los productos desde la base de datos y los muestra en la tabla.
     */
    private void cargarProductos() {
        try {
            tabla.getItems().clear(); // Limpia los productos existentes en la tabla
            tabla.getItems().addAll(productoDAO.obtenerTodosProductos()); // Carga y agrega los productos
        } catch (Exception e) {
            mostrarAlertaError("Error al cargar productos", e.getMessage()); // Muestra un mensaje de error en caso de fallo
        }
    }

    /**
     * Método para seleccionar una imagen del sistema de archivos y mostrarla en la vista previa.
     */
    @FXML
    private void seleccionarImagen() {
        FileChooser fileChooser = new FileChooser(); // Crea un selector de archivos
        fileChooser.setTitle("Seleccionar Imagen"); // Título del selector de archivos
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Archivos de Imagen", "*.png", "*.jpg", "*.gif", "*.bmp")
        );

        // Muestra el cuadro de diálogo para seleccionar un archivo
        File archivoSeleccionado = fileChooser.showOpenDialog(null);
        if (archivoSeleccionado != null) { // Si se seleccionó un archivo
            try {
                // Carga la imagen seleccionada y la muestra en el ImageView
                Image imagenSeleccionada = new Image(archivoSeleccionado.toURI().toString());
                imagen.setImage(imagenSeleccionada);
                rutaImagen = archivoSeleccionado.getAbsolutePath(); // Guarda la ruta de la imagen
            } catch (Exception e) {
                mostrarAlertaError("Error al cargar imagen", "No se pudo cargar la imagen seleccionada");
            }
        }
    }

    /**
     * Muestra un mensaje de alerta en caso de error.
     * @param titulo El título de la alerta.
     * @param mensaje El mensaje de la alerta.
     */
    private void mostrarAlertaError(String titulo, String mensaje) {
        Alert alerta = new Alert(Alert.AlertType.ERROR);
        alerta.setTitle(titulo);
        alerta.setHeaderText(null);
        alerta.setContentText(mensaje);
        alerta.showAndWait();
    }

    /**
     * Método para crear un nuevo producto y agregarlo a la base de datos.
     */
    @FXML
    private void crear() {
        if (validarDatos()) {
            try {
                // Crea un nuevo objeto Producto con los datos ingresados por el usuario
                Producto nuevoProducto = new Producto(
                        txtCodigo.getText(),
                        txtNombre.getText(),
                        Double.parseDouble(txtPrecio.getText()),
                        rutaImagen
                );

                // Agrega el producto a la base de datos
                if (productoDAO.agregarProducto(nuevoProducto)) {
                    cargarProductos(); // Recarga los productos
                    limpiar(); // Limpia los campos de entrada
                }
            } catch (Exception e) {
                mostrarAlertaError("Error al crear producto", e.getMessage());
            }
        }
    }

    /**
     * Valida que los datos ingresados sean correctos.
     * @return true si los datos son válidos, false en caso contrario.
     */
    private boolean validarDatos() {
        // Lógica de validación (puede ser ampliada según las necesidades)
        return !txtCodigo.getText().isEmpty() && !txtNombre.getText().isEmpty() && !txtPrecio.getText().isEmpty();
    }

    /**
     * Método para actualizar un producto existente en la base de datos.
     */
    @FXML
    private void actualizar() {
        if (validarDatos()) {
            try {
                // Crea un objeto Producto con los datos actualizados
                Producto productoActualizado = new Producto(
                        txtCodigo.getText(),
                        txtNombre.getText(),
                        Double.parseDouble(txtPrecio.getText()),
                        rutaImagen
                );

                // Actualiza el producto en la base de datos
                if (productoDAO.actualizarProducto(productoActualizado)) {
                    cargarProductos(); // Recarga los productos
                    limpiar(); // Limpia los campos de entrada
                }
            } catch (Exception e) {
                mostrarAlertaError("Error al actualizar producto", e.getMessage());
            }
        }
    }

    /**
     * Elimina un producto de la base de datos luego de confirmar la acción.
     */
    private void eliminarProducto() {
        Producto productoSeleccionado = tabla.getSelectionModel().getSelectedItem(); // Obtiene el producto seleccionado
        if (productoSeleccionado != null) {
            // Muestra un cuadro de diálogo de confirmación
            Alert confirmacion = new Alert(Alert.AlertType.CONFIRMATION);
            confirmacion.setTitle("Confirmar Eliminación");
            confirmacion.setHeaderText("¿Está seguro de eliminar este producto?");
            confirmacion.setContentText("Esta acción no se puede deshacer");

            Optional<ButtonType> resultado = confirmacion.showAndWait(); // Espera la respuesta del usuario
            if (resultado.isPresent() && resultado.get() == ButtonType.OK) {
                try {
                    // Elimina el producto de la base de datos
                    if (productoDAO.eliminarProducto(productoSeleccionado.getCodigo())) {
                        cargarProductos(); // Recarga los productos
                        limpiar(); // Limpia los campos de entrada
                    }
                } catch (Exception e) {
                    mostrarAlertaError("Error al eliminar producto", e.getMessage());
                }
            }
        }
    }

    /**
     * Muestra la imagen completa del producto seleccionado en una nueva ventana.
     */
    private void mostrarImagenCompleta() {
        Producto productoSeleccionado = tabla.getSelectionModel().getSelectedItem();
        if (productoSeleccionado != null && productoSeleccionado.getImagen() != null) {
            try {
                // Crea una nueva ventana para mostrar la imagen completa
                Stage stage = new Stage();
                ImageView imageView = new ImageView(new Image(new File(String.valueOf(productoSeleccionado.getImagen())).toURI().toString()));
                imageView.setFitWidth(300);
                imageView.setFitHeight(300);
                imageView.setPreserveRatio(true);

                Scene scene = new Scene(new StackPane(imageView));
                stage.setTitle("Imagen Completa");
                stage.setScene(scene);
                stage.setResizable(false);
                stage.show();
            } catch (Exception e) {
                mostrarAlertaError("Error al mostrar imagen", "No se pudo cargar la imagen");
            }
        }
    }

    /**
     * Limpia los campos de entrada y restablece la interfaz.
     */
    @FXML
    private void limpiar() {
        txtCodigo.clear();
        txtNombre.clear();
        txtPrecio.clear();
        imagen.setImage(null); // Elimina la imagen previa
        rutaImagen = null; // Resetea la ruta de la imagen
        btnCrear.setDisable(false); // Habilita el botón de crear
        btnActualizar.setDisable(true); // Deshabilita el botón de actualizar
        tabla.getSelectionModel().clearSelection(); // Limpia la selección en la tabla
    }

    /**
     * Método no implementado, puede ser utilizado para mostrar información acerca de la aplicación.
     * @param actionEvent Evento de acción.
     */
    /**
     * Muestra una ventana con información sobre la aplicación.
     * @param actionEvent Evento de acción que dispara el método.
     */
    public void mostrarAcercaDe(ActionEvent actionEvent) {
        // Crear una alerta de tipo INFORMACIÓN
        Alert alerta = new Alert(Alert.AlertType.INFORMATION);
        alerta.setTitle("Acerca de la Aplicación");
        alerta.setHeaderText("Detalles de la Aplicación");

        // Contenido del mensaje
        String mensaje = "Nombre de la Aplicación: Gestor de Productos\n" +
                "Versión: 1.0.0\n" +
                "Desarrollado por: Tu Nombre o Empresa\n" +
                "© 2024 Todos los derechos reservados.";
        alerta.setContentText(mensaje);

        // Mostrar la alerta
        alerta.showAndWait();
    }
}
