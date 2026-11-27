package modelo;

import java.math.BigDecimal;

/**
 * Totales de un mes (tarjetas de Resumen y Finanzas).
 */
public class ResumenMensual {

    private int anio;
    private int mes;
    private BigDecimal ingresos;
    private BigDecimal gastos;
    private BigDecimal beneficioNeto;
    private BigDecimal porCobrar;
    private int pagosPendientes;

    // ---------- Constructores (sobrecargados) ----------

    public ResumenMensual() {
    }

    /** Con todos los datos (así se arma al leer de la base de datos). */
    public ResumenMensual(int anio, int mes, BigDecimal ingresos, BigDecimal gastos, BigDecimal beneficioNeto, BigDecimal porCobrar, int pagosPendientes) {
        this.anio = anio;
        this.mes = mes;
        this.ingresos = ingresos;
        this.gastos = gastos;
        this.beneficioNeto = beneficioNeto;
        this.porCobrar = porCobrar;
        this.pagosPendientes = pagosPendientes;
    }

    // ---------- Getters y setters ----------

    public int getAnio() {
        return anio;
    }

    public void setAnio(int anio) {
        this.anio = anio;
    }

    public int getMes() {
        return mes;
    }

    public void setMes(int mes) {
        this.mes = mes;
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

    public BigDecimal getBeneficioNeto() {
        return beneficioNeto;
    }

    public void setBeneficioNeto(BigDecimal beneficioNeto) {
        this.beneficioNeto = beneficioNeto;
    }

    public BigDecimal getPorCobrar() {
        return porCobrar;
    }

    public void setPorCobrar(BigDecimal porCobrar) {
        this.porCobrar = porCobrar;
    }

    public int getPagosPendientes() {
        return pagosPendientes;
    }

    public void setPagosPendientes(int pagosPendientes) {
        this.pagosPendientes = pagosPendientes;
    }

    /** Resumen en cero para un mes sin movimientos. */
    public static ResumenMensual vacio(int anio, int mes) {
        return new ResumenMensual(anio, mes, BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO,
                BigDecimal.ZERO, 0);
    }

    /** Gastos como % de los ingresos. */
    public BigDecimal porcentajeGastos() {
        return porcentaje(gastos, ingresos);
    }

    /** Margen de beneficio en %. */
    public BigDecimal margen() {
        return porcentaje(beneficioNeto, ingresos);
    }

    private static BigDecimal porcentaje(BigDecimal parte, BigDecimal total) {
        if (total.signum() == 0) {
            return BigDecimal.ZERO;
        }
        return parte.multiply(BigDecimal.valueOf(100)).divide(total, 1, java.math.RoundingMode.HALF_UP);
    }

    @Override
    public String toString() {
        return "ResumenMensual[" + "anio=" + getAnio() + ", " + "mes=" + getMes() + ", " + "ingresos=" + getIngresos() + ", " + "gastos=" + getGastos() + ", " + "beneficioNeto=" + getBeneficioNeto() + ", " + "porCobrar=" + getPorCobrar() + ", " + "pagosPendientes=" + getPagosPendientes() + "]";
    }
}
