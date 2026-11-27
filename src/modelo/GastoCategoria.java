package modelo;

import java.math.BigDecimal;

/**
 * Una fila de la tarjeta Gastos por categoría.
 */
public class GastoCategoria {

    private String categoria;
    private BigDecimal total;
    private BigDecimal porcentaje;

    // ---------- Constructores (sobrecargados) ----------

    public GastoCategoria() {
    }

    /** Con todos los datos (así se arma al leer de la base de datos). */
    public GastoCategoria(String categoria, BigDecimal total, BigDecimal porcentaje) {
        this.categoria = categoria;
        this.total = total;
        this.porcentaje = porcentaje;
    }

    // ---------- Getters y setters ----------

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public BigDecimal getTotal() {
        return total;
    }

    public void setTotal(BigDecimal total) {
        this.total = total;
    }

    public BigDecimal getPorcentaje() {
        return porcentaje;
    }

    public void setPorcentaje(BigDecimal porcentaje) {
        this.porcentaje = porcentaje;
    }

    @Override
    public String toString() {
        return "GastoCategoria[" + "categoria=" + getCategoria() + ", " + "total=" + getTotal() + ", " + "porcentaje=" + getPorcentaje() + "]";
    }
}
