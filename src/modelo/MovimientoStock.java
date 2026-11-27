package modelo;

import java.time.LocalDateTime;

/**
 * Entrada (reposición) o Salida (uso) de un producto.
 */
public class MovimientoStock {

    private int id;
    private int productoId;
    private String tipo;
    private int cantidad;
    private LocalDateTime fecha;
    private String nota;

    // ---------- Constructores (sobrecargados) ----------

    public MovimientoStock() {
    }

    /** Para crear un objeto nuevo (sin id; lo asigna MySQL). */
    public MovimientoStock(int productoId, String tipo, int cantidad, String nota) {
        this.productoId = productoId;
        this.tipo = tipo;
        this.cantidad = cantidad;
        this.nota = nota;
    }

    /** Con todos los datos (así se arma al leer de la base de datos). */
    public MovimientoStock(int id, int productoId, String tipo, int cantidad, LocalDateTime fecha, String nota) {
        this.id = id;
        this.productoId = productoId;
        this.tipo = tipo;
        this.cantidad = cantidad;
        this.fecha = fecha;
        this.nota = nota;
    }

    // ---------- Getters y setters ----------

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getProductoId() {
        return productoId;
    }

    public void setProductoId(int productoId) {
        this.productoId = productoId;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    public LocalDateTime getFecha() {
        return fecha;
    }

    public void setFecha(LocalDateTime fecha) {
        this.fecha = fecha;
    }

    public String getNota() {
        return nota;
    }

    public void setNota(String nota) {
        this.nota = nota;
    }

    @Override
    public String toString() {
        return "MovimientoStock[" + "id=" + getId() + ", " + "productoId=" + getProductoId() + ", " + "tipo=" + getTipo() + ", " + "cantidad=" + getCantidad() + ", " + "fecha=" + getFecha() + ", " + "nota=" + getNota() + "]";
    }
}
