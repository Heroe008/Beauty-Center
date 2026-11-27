package modelo;

import java.math.BigDecimal;

/**
 * Un servicio del salón (manicure, pedicure...). toString devuelve el nombre para usarlo en un JComboBox.
 */
public class Servicio {

    private int id;
    private String nombre;
    private int duracionMin;
    private BigDecimal precio;
    private boolean activo;

    // ---------- Constructores (sobrecargados) ----------

    public Servicio() {
    }

    /** Para crear un objeto nuevo (sin id; lo asigna MySQL). */
    public Servicio(String nombre, int duracionMin, BigDecimal precio) {
        this.nombre = nombre;
        this.duracionMin = duracionMin;
        this.precio = precio;
    }

    /** Con todos los datos (así se arma al leer de la base de datos). */
    public Servicio(int id, String nombre, int duracionMin, BigDecimal precio, boolean activo) {
        this.id = id;
        this.nombre = nombre;
        this.duracionMin = duracionMin;
        this.precio = precio;
        this.activo = activo;
    }

    // ---------- Getters y setters ----------

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public int getDuracionMin() {
        return duracionMin;
    }

    public void setDuracionMin(int duracionMin) {
        this.duracionMin = duracionMin;
    }

    public BigDecimal getPrecio() {
        return precio;
    }

    public void setPrecio(BigDecimal precio) {
        this.precio = precio;
    }

    public boolean isActivo() {
        return activo;
    }

    public void setActivo(boolean activo) {
        this.activo = activo;
    }

    @Override
    public String toString() {
        return nombre;
    }
}
