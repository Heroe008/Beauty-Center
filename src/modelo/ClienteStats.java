package modelo;

/**
 * Números de las tarjetas de la pantalla Clientes.
 */
public class ClienteStats {

    private int registrados;
    private int activos;
    private int inactivos;
    private int nuevosEsteMes;

    // ---------- Constructores (sobrecargados) ----------

    public ClienteStats() {
    }

    /** Con todos los datos (así se arma al leer de la base de datos). */
    public ClienteStats(int registrados, int activos, int inactivos, int nuevosEsteMes) {
        this.registrados = registrados;
        this.activos = activos;
        this.inactivos = inactivos;
        this.nuevosEsteMes = nuevosEsteMes;
    }

    // ---------- Getters y setters ----------

    public int getRegistrados() {
        return registrados;
    }

    public void setRegistrados(int registrados) {
        this.registrados = registrados;
    }

    public int getActivos() {
        return activos;
    }

    public void setActivos(int activos) {
        this.activos = activos;
    }

    public int getInactivos() {
        return inactivos;
    }

    public void setInactivos(int inactivos) {
        this.inactivos = inactivos;
    }

    public int getNuevosEsteMes() {
        return nuevosEsteMes;
    }

    public void setNuevosEsteMes(int nuevosEsteMes) {
        this.nuevosEsteMes = nuevosEsteMes;
    }

    @Override
    public String toString() {
        return "ClienteStats[" + "registrados=" + getRegistrados() + ", " + "activos=" + getActivos() + ", " + "inactivos=" + getInactivos() + ", " + "nuevosEsteMes=" + getNuevosEsteMes() + "]";
    }
}
