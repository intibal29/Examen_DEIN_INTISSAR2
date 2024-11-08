package com.intissar.examen;

import com.intissar.examen.Conexion.DBConnect;
import com.intissar.examen.DAO.DaoProducto;
import com.intissar.examen.Modelo.Producto;

import java.sql.SQLException;
import java.util.List;

public class MainApp {
    public static void main(String[] args) {
        try {
            DBConnect dbConnect = new DBConnect();
            DaoProducto DaoProducto = new DaoProducto(dbConnect);

            // Ejemplo de uso: Obtener todos los productos
            List<Producto> productos = DaoProducto.obtenerTodos();
            productos.forEach(producto -> System.out.println(producto.getNombre()));

            // Cerrar conexión
            dbConnect.closeConnection();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
