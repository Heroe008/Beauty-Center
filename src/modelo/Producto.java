package modelo;

import java.math.BigDecimal;

/**
 * Producto del inventario. estado (Agotado, Stock bajo, Disponible) lo calcula la vista vw_productos.
 */
public class Producto implements Describible {

    private int id;
    private String codigo;
    private String nombre;
    private int categoriaId;
    private String categoria;
    private int stockActual;
    private int stockMinimo;
    private BigDecimal precioCosto;
    private String estado;

    // ---------- Constructores (sobrecargados) ----------

    public Producto() {
    }

    /** Para crear un objeto nuevo (sin id; lo asigna MySQL). */
    public Producto(String nombre, int categoriaId, int stockActual, int stockMinimo, BigDecimal precioCosto) {
        this.nombre = nombre;
        this.categoriaId = categoriaId;
        this.stockActual = stockActual;
        this.stockMinimo = stockMinimo;
        this.precioCosto = precioCosto;
    }

    /** Con todos los datos (así se arma al leer de la base de datos). */
    public Producto(int id, String codigo, String nombre, int categoriaId, String categoria, int stockActual, int stockMinimo, BigDecimal precioCosto, String estado) {
        this.id = id;
        this.codigo = codigo;
        this.nombre = nombre;
        this.categoriaId = categoriaId;
        this.categoria = categoria;
        this.stockActual = stockActual;
        this.stockMinimo = stockMinimo;
        this.precioCosto = precioCosto;
        this.estado = estado;
    }

    // ---------- Getters y setters ----------

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public int getCategoriaId() {
        return categoriaId;
    }

    public void setCategoriaId(int categoriaId) {
        this.categoriaId = categoriaId;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public int getStockActual() {
        return stockActual;
    }

    public void setStockActual(int stockActual) {
        this.stockActual = stockActual;
    }

    public int getStockMinimo() {
        return stockMinimo;
    }

    public void setStockMinimo(int stockMinimo) {
        this.stockMinimo = stockMinimo;
    }

    public BigDecimal getPrecioCosto() {
        return precioCosto;
    }

    public void setPrecioCosto(BigDecimal precioCosto) {
        this.precioCosto = precioCosto;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    @Override
    public String descripcion() {
        return codigo + " · " + nombre;
    }

    public boolean estaAgotado() {
        return stockActual == 0;
    }

    public boolean tieneStockBajo() {
        return stockActual > 0 && stockActual < stockMinimo;
    }

    @Override
    public String toString() {
        return "Producto[" + "id=" + getId() + ", " + "codigo=" + getCodigo() + ", " + "nombre=" + getNombre() + ", " + "categoriaId=" + getCategoriaId() + ", " + "categoria=" + getCategoria() + ", " + "stockActual=" + getStockActual() + ", " + "stockMinimo=" + getStockMinimo() + ", " + "precioCosto=" + getPrecioCosto() + ", " + "estado=" + getEstado() + "]";
    }
}
