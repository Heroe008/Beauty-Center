package modelo;

/**
 * Clase abstracta: lo que tienen en común las personas del sistema.
 * No se puede hacer "new Persona()"; se usan sus hijas Cliente y Usuario.
 */
public abstract class Persona implements Describible {

    private int id;
    private String nombre;

    protected Persona() {
    }

    protected Persona(int id, String nombre) {
        this.id = id;
        this.nombre = nombre;
    }

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

    /** Método abstracto: cada hija dice qué tipo de persona es ("Cliente", "Usuario"). */
    public abstract String getTipo();

    @Override
    public String descripcion() {
        return getTipo() + ": " + nombre;
    }

    @Override
    public String toString() {
        return descripcion();
    }
}