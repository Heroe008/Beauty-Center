package modelo;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

/** Dinero que sale. Estados: Pagado o Pendiente. Siempre lleva categoría (Nómina, Alquiler...). */
public class Gasto extends Movimiento {

    private Integer categoriaGastoId;
    private String categoriaGasto;

    public Gasto() {
    }

    /** Gasto nuevo. */
    public Gasto(String concepto, LocalDate fecha, String metodoPago, BigDecimal importe,
                 String estado, Integer categoriaGastoId, String notas) {
        super(concepto, fecha, metodoPago, importe, estado, null, notas);
        this.categoriaGastoId = categoriaGastoId;
    }

    /** Con todos los datos (al leer de la base de datos). */
    public Gasto(int id, String codigo, String concepto, LocalDate fecha, String metodoPago,
                 BigDecimal importe, String estado, Integer clienteId, Integer citaId, String notas,
                 Integer categoriaGastoId, String categoriaGasto) {
        super(id, codigo, concepto, fecha, metodoPago, importe, estado, clienteId, citaId, notas);
        this.categoriaGastoId = categoriaGastoId;
        this.categoriaGasto = categoriaGasto;
    }

    @Override
    public String getTipo() {
        return "Gasto";
    }

    @Override
    public List<String> getEstadosValidos() {
        return List.of("Pagado", "Pendiente");
    }

    @Override
    public int getSigno() {
        return -1;
    }

    @Override
    public Integer getCategoriaGastoId() {
        return categoriaGastoId;
    }

    public void setCategoriaGastoId(Integer categoriaGastoId) {
        this.categoriaGastoId = categoriaGastoId;
    }

    @Override
    public String getCategoriaGasto() {
        return categoriaGasto;
    }

    public void setCategoriaGasto(String categoriaGasto) {
        this.categoriaGasto = categoriaGasto;
    }

    /** Además de lo común, un gasto debe tener categoría. */
    @Override
    public void validar() {
        super.validar();
        exigir(categoriaGastoId != null, "Elige la categoría del gasto.");
    }
}
