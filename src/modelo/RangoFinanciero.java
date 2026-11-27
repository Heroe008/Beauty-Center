package modelo;

import java.math.BigDecimal;

/**
 * Barras del gráfico de Resumen: días desde-hasta del mes (1-5, 6-10, ...).
 */
public class RangoFinanciero {

    private int diaDesde;
    private int diaHasta;
    private BigDecimal ingresos;
    private BigDecimal gastos;

    // ---------- Constructores (sobrecargados) ----------

    public RangoFinanciero() {
    }

    /** Con todos los datos (así se arma al leer de la base de datos). */
    public RangoFinanciero(int diaDesde, int diaHasta, BigDecimal ingresos, BigDecimal gastos) {
        this.diaDesde = diaDesde;
        this.diaHasta = diaHasta;
        this.ingresos = ingresos;
        this.gastos = gastos;
    }

    // ---------- Getters y setters ----------

    public int getDiaDesde() {
        return diaDesde;
    }

    public void setDiaDesde(int diaDesde) {
        this.diaDesde = diaDesde;
    }

    public int getDiaHasta() {
        return diaHasta;
    }

    public void setDiaHasta(int diaHasta) {
        this.diaHasta = diaHasta;
    }

    public BigDecimal getIngresos() {
        return ingresos;
    }

    public void setIngresos(BigDecimal ingresos) {
        this.ingresos = ingresos;
    }

    public BigDecimal getGastos() {
        return gastos;
    }

    public void setGastos(BigDecimal gastos) {
        this.gastos = gastos;
    }

    @Override
    public String toString() {
        return "RangoFinanciero[" + "diaDesde=" + getDiaDesde() + ", " + "diaHasta=" + getDiaHasta() + ", " + "ingresos=" + getIngresos() + ", " + "gastos=" + getGastos() + "]";
    }
}
