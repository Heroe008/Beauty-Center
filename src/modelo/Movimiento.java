package modelo;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

/**
 * Clase abstracta: lo común de un Ingreso y un Gasto (pantalla Finanzas).
 * Cada hija define con métodos abstractos su tipo, sus estados válidos y su signo.
 * metodoPago: Efectivo | Tarjeta | Transferencia.
 */
public abstract class Movimiento implements Describible {

    private int id;
    private String codigo;
    private String concepto;
    private LocalDate fecha;
    private String metodoPago;
    private BigDecimal importe;
    private String estado;
    private Integer clienteId;     // puede ser null
    private Integer citaId;        // puede ser null
    private String notas;

    // ---------- Constructores (sobrecargados) ----------

    protected Movimiento() {
    }

    /** Para un movimiento nuevo (sin id ni código). */
    protected Movimiento(String concepto, LocalDate fecha, String metodoPago, BigDecimal importe,
                         String estado, Integer clienteId, String notas) {
        this.concepto = concepto;
        this.fecha = fecha;
        this.metodoPago = metodoPago;
        this.importe = importe;
        this.estado = estado;
        this.clienteId = clienteId;
        this.notas = notas;
    }

    /** Con todos los datos (al leer de la base de datos). */
    protected Movimiento(int id, String codigo, String concepto, LocalDate fecha, String metodoPago,
                         BigDecimal importe, String estado, Integer clienteId, Integer citaId, String notas) {
        this(concepto, fecha, metodoPago, importe, estado, clienteId, notas);
        this.id = id;
        this.codigo = codigo;
        this.citaId = citaId;
    }

    // ---------- Métodos abstractos: cada hija los define distinto ----------

    /** "Ingreso" o "Gasto" (así se guarda en la columna tipo). */
    public abstract String getTipo();

    /** Estados permitidos: un ingreso Cobrado/Pendiente, un gasto Pagado/Pendiente. */
    public abstract List<String> getEstadosValidos();

    /** +1 para ingresos, -1 para gastos. */
    public abstract int getSigno();

    // ---------- Métodos que las hijas pueden sobrescribir ----------

    /** Solo los gastos tienen categoría; los ingresos devuelven null. */
    public Integer getCategoriaGastoId() {
        return null;
    }

    public String getCategoriaGasto() {
        return null;
    }

    /** Lanza IllegalArgumentException con un mensaje claro si algo no está bien. */
    public void validar() {
        exigir(concepto != null && !concepto.isBlank(), "El concepto es obligatorio.");
        exigir(fecha != null, "La fecha es obligatoria.");
        exigir(importe != null && importe.signum() > 0, "El importe debe ser mayor que cero.");
        exigir(List.of("Efectivo", "Tarjeta", "Transferencia").contains(metodoPago), "Elige un método de pago.");
        exigir(getEstadosValidos().contains(estado),
                "Un " + getTipo().toLowerCase() + " solo puede estar " + String.join(" o ", getEstadosValidos()) + ".");
    }

    protected static void exigir(boolean condicion, String mensaje) {
        if (!condicion) {
            throw new IllegalArgumentException(mensaje);
        }
    }

    // ---------- Métodos concretos ----------

    /** Positivo para ingresos y negativo para gastos. */
    public BigDecimal getImporteConSigno() {
        return importe.multiply(BigDecimal.valueOf(getSigno()));
    }

    public boolean estaPendiente() {
        return "Pendiente".equals(estado);
    }

    @Override
    public String descripcion() {
        return codigo + " · " + getTipo() + " · " + concepto + " · " + importe;
    }

    @Override
    public String toString() {
        return descripcion();
    }

    // ---------- Getters y setters ----------

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getConcepto() {
        return concepto;
    }

    public void setConcepto(String concepto) {
        this.concepto = concepto;
    }

    public LocalDate getFecha() {
        return fecha;
    }

    public void setFecha(LocalDate fecha) {
        this.fecha = fecha;
    }

    public String getMetodoPago() {
        return metodoPago;
    }

    public void setMetodoPago(String metodoPago) {
        this.metodoPago = metodoPago;
    }

    public BigDecimal getImporte() {
        return importe;
    }

    public void setImporte(BigDecimal importe) {
        this.importe = importe;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public Integer getClienteId() {
        return clienteId;
    }

    public void setClienteId(Integer clienteId) {
        this.clienteId = clienteId;
    }

    public Integer getCitaId() {
        return citaId;
    }

    public void setCitaId(Integer citaId) {
        this.citaId = citaId;
    }

    public String getNotas() {
        return notas;
    }

    public void setNotas(String notas) {
        this.notas = notas;
    }
}
