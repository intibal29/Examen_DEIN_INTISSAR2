package com.intissar.examen.DAO;

import com.intissar.examen.Conexion.DBConnect;
import com.intissar.examen.Modelo.Producto;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.sql.*;

/**
 * Clase DAO para realizar operaciones CRUD en la tabla de productos en la base de datos.
 */
public class ProductoDAO {

    /**
     * Obtiene un producto específico de la base de datos usando su código.
     * @param codigo El código único del producto.
     * @return El objeto Producto si se encuentra, o null si no existe.
     * @throws SQLException en caso de error de SQL.
     */
    public static Producto obtenerProductoPorCodigo(String codigo) throws SQLException {
        DBConnect conexion = new DBConnect();
        Producto producto = null;
        String consulta = "SELECT codigo, nombre, precio, disponible, imagen FROM productos WHERE codigo = ?";

        try (PreparedStatement stmt = conexion.getConnection().prepareStatement(consulta)) {
            stmt.setString(1, codigo);
            ResultSet resultado = stmt.executeQuery();

            // Si se encuentra el producto, se construye un objeto Producto con los datos obtenidos
            if (resultado.next()) {
                producto = new Producto(
                        resultado.getString("codigo"),
                        resultado.getString("nombre"),
                        resultado.getFloat("precio"),
                        resultado.getBoolean("disponible"),
                        resultado.getBytes("imagen")
                );
            }
            resultado.close();
            conexion.closeConnection();
        } catch (SQLException e) {
            System.err.println("Error al obtener el producto: " + e.getMessage());
        }
        return producto;
    }

    /**
     * Convierte un archivo de imagen a un objeto Blob para almacenar en la base de datos.
     * @param archivo Archivo de imagen que se va a convertir.
     * @return El Blob generado a partir del archivo.
     * @throws SQLException Si ocurre un error al manipular el Blob en la base de datos.
     * @throws IOException Si ocurre un error al leer el archivo.
     */
    public static Blob convertirArchivoABlob(File archivo) throws SQLException, IOException {
        DBConnect conexion = new DBConnect();
        try (Connection conn = conexion.getConnection();
             FileInputStream fis = new FileInputStream(archivo)) {

            Blob blob = conn.createBlob();
            byte[] buffer = new byte[1024];
            int leido;

            // Lee el archivo en el Blob en bloques de 1024 bytes
            try (var os = blob.setBinaryStream(1)) {
                while ((leido = fis.read(buffer)) != -1) {
                    os.write(buffer, 0, leido);
                }
            }
            return blob;
        }
    }

    /**
     * Obtiene todos los productos de la base de datos en una lista observable, útil para la vista en JavaFX.
     * @return Una lista observable con todos los productos de la base de datos.
     * @throws SQLException Si ocurre un error de SQL.
     */
    public static ObservableList<Producto> obtenerListadoProductos() throws SQLException {
        DBConnect conexion = new DBConnect();
        ObservableList<Producto> productos = FXCollections.observableArrayList();
        String consulta = "SELECT codigo, nombre, precio, disponible, imagen FROM productos";

        try (PreparedStatement stmt = conexion.getConnection().prepareStatement(consulta);
             ResultSet resultado = stmt.executeQuery()) {

            // Se iteran los resultados y se agregan los productos a la lista observable
            while (resultado.next()) {
                Producto producto = new Producto(
                        resultado.getString("codigo"),
                        resultado.getString("nombre"),
                        resultado.getFloat("precio"),
                        resultado.getBoolean("disponible"),
                        resultado.getBytes("imagen")
                );
                productos.add(producto);
            }
            conexion.closeConnection();
        } catch (SQLException e) {
            System.err.println("Error al cargar productos: " + e.getMessage());
        }

        return productos;
    }

    /**
     * Actualiza los datos de un producto en la base de datos.
     * @param producto Instancia del producto con los datos actualizados.
     * @return true si la actualización fue exitosa, false en caso contrario.
     * @throws SQLException en caso de error SQL.
     */
    public static boolean actualizarProducto(Producto producto) throws SQLException {
        DBConnect conexion = new DBConnect();
        String consulta = "UPDATE productos SET nombre = ?, precio = ?, disponible = ?, imagen = ? WHERE codigo = ?";

        try (PreparedStatement stmt = conexion.getConnection().prepareStatement(consulta)) {
            stmt.setString(1, producto.getNombre());
            stmt.setFloat(2, producto.getPrecio());
            stmt.setBoolean(3, producto.isDisponible());
            // stmt.setBytes(4, producto.getImagen()); // Manejo de la imagen
            stmt.setString(5, producto.getCodigo());

            int filasModificadas = stmt.executeUpdate();
            conexion.closeConnection();
            return filasModificadas > 0; // Devuelve true si al menos una fila fue modificada
        } catch (SQLException e) {
            System.err.println("Error al actualizar producto: " + e.getMessage());
            return false;
        }
    }

    /**
     * Inserta un nuevo producto en la base de datos.
     * @param producto El producto a insertar.
     * @return ID del producto insertado si es exitoso, -1 si ocurre algún error.
     * @throws SQLException en caso de error SQL.
     */
    public static int insertarProducto(Producto producto) throws SQLException {
        DBConnect conexion = new DBConnect();
        String consulta = "INSERT INTO productos (codigo, nombre, precio, disponible, imagen) VALUES (?, ?, ?, ?, ?)";

        try (PreparedStatement stmt = conexion.getConnection().prepareStatement(consulta, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, producto.getCodigo());
            stmt.setString(2, producto.getNombre());
            stmt.setFloat(3, producto.getPrecio());
            stmt.setBoolean(4, producto.isDisponible());
            // stmt.setBytes(5, producto.getImagen()); // Imagen del producto

            int filasInsertadas = stmt.executeUpdate();
            if (filasInsertadas > 0) {
                ResultSet clavesGeneradas = stmt.getGeneratedKeys();
                if (clavesGeneradas.next()) {
                    int id = clavesGeneradas.getInt(1); // Obtiene el ID generado
                    clavesGeneradas.close();
                    conexion.closeConnection();
                    return id;
                }
            }
            conexion.closeConnection();
            return -1;
        } catch (SQLException e) {
            System.err.println("Error al insertar producto: " + e.getMessage());
            return -1;
        }
    }

    /**
     * Elimina un producto de la base de datos usando su código.
     * @param codigo Código único del producto a eliminar.
     * @return true si el producto fue eliminado, false en caso contrario.
     * @throws SQLException en caso de error SQL.
     */
    public static boolean eliminarProducto(String codigo) throws SQLException {
        DBConnect conexion = new DBConnect();
        String consulta = "DELETE FROM productos WHERE codigo = ?";

        try (PreparedStatement stmt = conexion.getConnection().prepareStatement(consulta)) {
            stmt.setString(1, codigo);
            int filasEliminadas = stmt.executeUpdate();
            conexion.closeConnection();
            return filasEliminadas > 0; // Devuelve true si al menos una fila fue eliminada
        } catch (SQLException e) {
            System.err.println("Error al eliminar producto: " + e.getMessage());
            return false;
        }
    }

    /**
     * Método auxiliar que llama a obtenerListadoProductos para obtener todos los productos.
     * @return Lista observable de todos los productos.
     * @throws SQLException en caso de error SQL.
     */
    public static ObservableList<Producto> obtenerTodosProductos() throws SQLException {
        return obtenerListadoProductos();
    }

    /**
     * Agrega un nuevo producto a la base de datos usando insertarProducto.
     * @param nuevoProducto El producto a agregar.
     * @return true si se insertó correctamente, false en caso de error.
     * @throws SQLException en caso de error SQL.
     */
    public static boolean agregarProducto(Producto nuevoProducto) throws SQLException {
        return insertarProducto(nuevoProducto) != -1;
    }
}
