package modelo;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

/** Dinero que entra. Estados: Cobrado, o Pendiente (= "Por cobrar"). */
public class Ingreso extends Movimiento {

    public Ingreso() {
    }

    /** Ingreso nuevo. */
    public Ingreso(String concepto, LocalDate fecha, String metodoPago, BigDecimal importe,
                   String estado, Integer clienteId, String notas) {
        super(concepto, fecha, metodoPago, importe, estado, clienteId, notas);
    }

    /** Con todos los datos (al leer de la base de datos). */
    public Ingreso(int id, String codigo, String concepto, LocalDate fecha, String metodoPago,
                   BigDecimal importe, String estado, Integer clienteId, Integer citaId, String notas) {
        super(id, codigo, concepto, fecha, metodoPago, importe, estado, clienteId, citaId, notas);
    }

    @Override
    public String getTipo() {
        return "Ingreso";
    }

    @Override
    public List<String> getEstadosValidos() {
        return List.of("Cobrado", "Pendiente");
    }

    @Override
    public int getSigno() {
        return 1;
    }
}
