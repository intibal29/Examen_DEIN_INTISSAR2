package com.intissar.examen.Modelo;

import javafx.beans.binding.BooleanExpression;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.SimpleFloatProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.value.ObservableValue;

import java.sql.Blob;
import java.util.Objects;

/**
 * Representa un producto en la base de datos.
 */
public class Producto {
    private String codigo;
    private String nombre;
    private float precio;
    private boolean disponible;
    private Blob imagen;

    // Propiedades observables
    private SimpleStringProperty codigoProperty;
    private SimpleStringProperty nombreProperty;
    private SimpleFloatProperty precioProperty;
    private SimpleBooleanProperty disponibleProperty;

    /**
     * Constructor completo para inicializar un nuevo producto con todos los atributos.
     *
     * @param codigo Identificador único del producto
     * @param nombre Nombre del producto
     * @param precio Precio del producto
     * @param disponible Disponibilidad del producto
     * @param imagen Imagen del producto en formato Blob
     */
    public Producto(String codigo, String nombre, float precio, boolean disponible, Blob imagen) {
        this.codigo = codigo;
        this.nombre = nombre;
        this.precio = precio;
        this.disponible = disponible;
        this.imagen = imagen;

        // Inicializar propiedades observables
        this.codigoProperty = new SimpleStringProperty(codigo);
        this.nombreProperty = new SimpleStringProperty(nombre);
        this.precioProperty = new SimpleFloatProperty(precio);
        this.disponibleProperty = new SimpleBooleanProperty(disponible);
    }

    /**
     * Constructor sin parámetros para crear un producto vacío.
     */
    public Producto() {
        this.codigoProperty = new SimpleStringProperty();
        this.nombreProperty = new SimpleStringProperty();
        this.precioProperty = new SimpleFloatProperty();
        this.disponibleProperty = new SimpleBooleanProperty();
    }

    public Producto(String codigo, String nombre, float precio, boolean disponible, byte[] imagens) {
        this.codigo = codigo;
        this.nombre = nombre;
        this.precio = precio;
        this.disponible = disponible;

    }

    public Producto(String codigo, String nombre, double precio, String rutaImagen) {
        this.codigo = codigo;
        this.nombre = nombre;
        this.precio = (float) precio;

    }

    /**
     * Obtiene el código del producto.
     *
     * @return El código del producto.
     */
    public String getCodigo() {
        return codigo;
    }

    /**
     * Define el código del producto.
     *
     * @param codigo El nuevo código del producto.
     */
    public void setCodigo(String codigo) {
        this.codigo = codigo;
        this.codigoProperty.set(codigo); // Actualizar propiedad observable
    }

    /**
     * Obtiene el nombre del producto.
     *
     * @return El nombre del producto.
     */
    public String getNombre() {
        return nombre;
    }

    /**
     * Define el nombre del producto.
     *
     * @param nombre El nuevo nombre del producto.
     */
    public void setNombre(String nombre) {
        this.nombre = nombre;
        this.nombreProperty.set(nombre); // Actualizar propiedad observable
    }

    /**
     * Obtiene el precio del producto.
     *
     * @return El precio actual del producto.
     */
    public float getPrecio() {
        return precio;
    }

    /**
     * Define el precio del producto.
     *
     * @param precio El nuevo precio del producto.
     */
    public void setPrecio(float precio) {
        this.precio = precio;
        this.precioProperty.set(precio); // Actualizar propiedad observable
    }

    /**
     * Obtiene la disponibilidad del producto.
     *
     * @return true si el producto está disponible, false en caso contrario.
     */
    public boolean isDisponible() {
        return disponible;
    }

    /**
     * Establece la disponibilidad del producto.
     *
     * @param disponible La nueva disponibilidad del producto.
     *//*
    public void setDisponible(boolean disponible) {
        this.disponible = disponible;
        this.disponibleProperty.set(dispobible); // Actualizar propiedad observable
    }*/

    /**
     * Obtiene la imagen del producto en formato Blob.
     *
     * @return La imagen del producto.
     */
    public Blob getImagen() {
        return imagen;
    }

    /**
     * Define la imagen del producto.
     *
     * @ param imagen El nuevo Blob de imagen para el producto.
     */
    public void setImagen(Blob imagen) {
        this.imagen = imagen;
    }

    /**
     * Verifica si dos productos son iguales basándose en el código.
     *
     * @param o El objeto a comparar.
     * @return true si los códigos son iguales, false en caso contrario.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Producto producto = (Producto) o;
        return Objects.equals(codigo, producto.codigo);
    }

    /**
     * Calcula el hash basado en el código del producto.
     *
     * @return El valor de hash.
     */
    @Override
    public int hashCode() {
        return Objects.hash(codigo);
    }

    public ObservableValue<String> codigoProperty() {
        return codigoProperty;
    }

    public ObservableValue<String> nombreProperty() {
        return nombreProperty;
    }

    public ObservableValue<Float> precioProperty() {
        return precioProperty.asObject();
    }

    public BooleanExpression disponibleProperty() {
        return disponibleProperty;
    }
}