package com.intissar.examen.DAO;



import com.intissar.examen.Modelo.Producto;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProductoDAO {
    private DBConnector dbConnector;

    // Constructor que recibe el DBConnector
    public ProductoDAO(DBConnector dbConnector) {
        this.dbConnector = dbConnector;
    }

    // Método para obtener todos los productos
    public List<Producto> obtenerTodos() {
        List<Producto> productos = new ArrayList<>();
        String sql = "SELECT * FROM productos";

        try (Connection conn = dbConnector.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Producto producto = new Producto(
                        rs.getString("codigo"),
                        rs.getString("nombre"),
                        rs.getFloat("precio"),
                        rs.getInt("disponible") == 1,
                        rs.getBlob("imagen")
                );
                productos.add(producto);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return productos;
    }

    // Método para insertar un nuevo producto
    public boolean insertarProducto(Producto producto) {
        String sql = "INSERT INTO productos (codigo, nombre, precio, disponible, imagen) VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = dbConnector.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, producto.getCodigo());
            pstmt.setString(2, producto.getNombre());
            pstmt.setFloat(3, producto.getPrecio());
            pstmt.setInt(4, producto.isDisponible() ? 1 : 0);
            pstmt.setBlob(5, producto.getImagen());
            pstmt.executeUpdate();
            return true;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Método para actualizar un producto existente
    public boolean actualizarProducto(Producto producto) {
        String sql = "UPDATE productos SET nombre = ?, precio = ?, disponible = ?, imagen = ? WHERE codigo = ?";

        try (Connection conn = dbConnector.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, producto.getNombre());
            pstmt.setFloat(2, producto.getPrecio());
            pstmt.setInt(3, producto.isDisponible() ? 1 : 0);
            pstmt.setBlob(4, producto.getImagen());
            pstmt.setString(5, producto.getCodigo());
            pstmt.executeUpdate();
            return true;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Método para eliminar un producto
    public boolean eliminarProducto(String codigo) {
        String sql = "DELETE FROM productos WHERE codigo = ?";

        try (Connection conn = dbConnector.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, codigo);
            pstmt.executeUpdate();
            return true;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
