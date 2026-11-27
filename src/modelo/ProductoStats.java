package modelo;

/**
 * Números de las tarjetas de la pantalla Inventario.
 */
public class ProductoStats {

    private int registrados;
    private int categorias;
    private int disponibles;
    private int stockBajo;
    private int agotados;

    // ---------- Constructores (sobrecargados) ----------

    public ProductoStats() {
    }

    /** Con todos los datos (así se arma al leer de la base de datos). */
    public ProductoStats(int registrados, int categorias, int disponibles, int stockBajo, int agotados) {
        this.registrados = registrados;
        this.categorias = categorias;
        this.disponibles = disponibles;
        this.stockBajo = stockBajo;
        this.agotados = agotados;
    }

    // ---------- Getters y setters ----------

    public int getRegistrados() {
        return registrados;
    }

    public void setRegistrados(int registrados) {
        this.registrados = registrados;
    }

    public int getCategorias() {
        return categorias;
    }

    public void setCategorias(int categorias) {
        this.categorias = categorias;
    }

    public int getDisponibles() {
        return disponibles;
    }

    public void setDisponibles(int disponibles) {
        this.disponibles = disponibles;
    }

    public int getStockBajo() {
        return stockBajo;
    }

    public void setStockBajo(int stockBajo) {
        this.stockBajo = stockBajo;
    }

    public int getAgotados() {
        return agotados;
    }

    public void setAgotados(int agotados) {
        this.agotados = agotados;
    }

    @Override
    public String toString() {
        return "ProductoStats[" + "registrados=" + getRegistrados() + ", " + "categorias=" + getCategorias() + ", " + "disponibles=" + getDisponibles() + ", " + "stockBajo=" + getStockBajo() + ", " + "agotados=" + getAgotados() + "]";
    }
}