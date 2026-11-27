package modelo;

/**
 * Números de las tarjetas de la pantalla Citas.
 */
public class CitaStats {

    private int canceladas;
    private int confirmadas;
    private int pendientes;

    // ---------- Constructores (sobrecargados) ----------

    public CitaStats() {
    }

    /** Con todos los datos (así se arma al leer de la base de datos). */
    public CitaStats(int canceladas, int confirmadas, int pendientes) {
        this.canceladas = canceladas;
        this.confirmadas = confirmadas;
        this.pendientes = pendientes;
    }

    // ---------- Getters y setters ----------

    public int getCanceladas() {
        return canceladas;
    }

    public void setCanceladas(int canceladas) {
        this.canceladas = canceladas;
    }

    public int getConfirmadas() {
        return confirmadas;
    }

    public void setConfirmadas(int confirmadas) {
        this.confirmadas = confirmadas;
    }

    public int getPendientes() {
        return pendientes;
    }

    public void setPendientes(int pendientes) {
        this.pendientes = pendientes;
    }

    @Override
    public String toString() {
        return "CitaStats[" + "canceladas=" + getCanceladas() + ", " + "confirmadas=" + getConfirmadas() + ", " + "pendientes=" + getPendientes() + "]";
    }
}

