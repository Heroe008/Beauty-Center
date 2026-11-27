package modelo;

/**
 * Sirve para categorias_producto y categorias_gasto (las dos son id + nombre).
 */
public class Categoria {

    private int id;
    private String nombre;

    // ---------- Constructores (sobrecargados) ----------

    public Categoria() {
    }

    /** Para crear un objeto nuevo (sin id; lo asigna MySQL). */
    public Categoria(String nombre) {
        this.nombre = nombre;
    }

    /** Con todos los datos (así se arma al leer de la base de datos). */
    public Categoria(int id, String nombre) {
        this.id = id;
        this.nombre = nombre;
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

    @Override
    public String toString() {
        return nombre;
    }
}
