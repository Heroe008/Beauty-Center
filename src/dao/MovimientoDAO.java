package dao;

import modelo.Categoria;
import modelo.Gasto;
import modelo.Ingreso;
import modelo.Movimiento;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Ingresos y gastos (pantalla Finanzas). Al leer, crea un Ingreso o un Gasto según
 * la columna tipo; al guardar, funciona igual con cualquiera de los dos (polimorfismo).
 */
public class MovimientoDAO extends DaoCrud<Movimiento> {

    /** Las pestañas de la tabla de Finanzas. */
    public enum Filtro { TODOS, INGRESOS, GASTOS, POR_COBRAR }

    @Override
    protected String consultaBase() {
        return "SELECT id, codigo, tipo, concepto, fecha, metodo_pago, importe, estado, cliente_id, cita_id, "
             + "categoria_gasto_id, categoria_gasto, notas FROM vw_movimientos";
    }

    @Override
    protected String orden() {
        return " ORDER BY fecha DESC, id DESC";
    }

    @Override
    protected Movimiento mapear(ResultSet rs) throws SQLException {
        int id = rs.getInt("id");
        String codigo = rs.getString("codigo");
        String concepto = rs.getString("concepto");
        LocalDate fecha = fecha(rs, "fecha");
        String metodo = rs.getString("metodo_pago");
        var importe = rs.getBigDecimal("importe");
        String estado = rs.getString("estado");
        Integer clienteId = entero(rs, "cliente_id");
        Integer citaId = entero(rs, "cita_id");
        String notas = rs.getString("notas");
        if ("Gasto".equals(rs.getString("tipo"))) {
            return new Gasto(id, codigo, concepto, fecha, metodo, importe, estado, clienteId, citaId, notas,
                    entero(rs, "categoria_gasto_id"), rs.getString("categoria_gasto"));
        }
        return new Ingreso(id, codigo, concepto, fecha, metodo, importe, estado, clienteId, citaId, notas);
    }

    /** Sirve para un Ingreso o un Gasto: cada uno se valida a su manera (método validar() de la clase). */
    @Override
    public int insertar(Movimiento m) {
        m.validar();
        return insertar("INSERT INTO movimientos (tipo, concepto, fecha, metodo_pago, importe, estado, "
                        + "cliente_id, cita_id, categoria_gasto_id, notas) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)",
                m.getTipo(), m.getConcepto().trim(), m.getFecha(), m.getMetodoPago(), m.getImporte(), m.getEstado(),
                m.getClienteId(), m.getCitaId(), m.getCategoriaGastoId(), vacioANull(m.getNotas()));
    }

    @Override
    public boolean actualizar(Movimiento m) {
        m.validar();
        return ejecutar("UPDATE movimientos SET tipo = ?, concepto = ?, fecha = ?, metodo_pago = ?, importe = ?, "
                        + "estado = ?, cliente_id = ?, cita_id = ?, categoria_gasto_id = ?, notas = ? WHERE id = ?",
                m.getTipo(), m.getConcepto().trim(), m.getFecha(), m.getMetodoPago(), m.getImporte(), m.getEstado(),
                m.getClienteId(), m.getCitaId(), m.getCategoriaGastoId(), vacioANull(m.getNotas()), m.getId()) > 0;
    }

    @Override
    public boolean eliminar(int id) {
        return ejecutar("DELETE FROM movimientos WHERE id = ?", id) > 0;
    }

    /** "Registrar pago": un ingreso Pendiente pasa a Cobrado y un gasto Pendiente a Pagado. */
    public boolean registrarPago(int id) {
        return ejecutar("UPDATE movimientos SET estado = CASE tipo WHEN 'Ingreso' THEN 'Cobrado' ELSE 'Pagado' END "
                + "WHERE id = ? AND estado = 'Pendiente'", id) > 0;
    }

    // ---------- Búsquedas del mes (sobrecarga) ----------

    /** Movimientos de un mes con búsqueda opcional y pestaña (Filtro). */
    public List<Movimiento> listar(int anio, int mes, String texto, Filtro filtro, int limite, int offset) {
        List<Object> p = new ArrayList<>();
        String sql = consultaBase() + where(anio, mes, texto, filtro, p) + orden() + " LIMIT ? OFFSET ?";
        p.add(limite);
        p.add(offset);
        return consultar(sql, this::mapear, p.toArray());
    }

    /** Todos los movimientos del mes, sin búsqueda. */
    public List<Movimiento> listar(int anio, int mes, int limite, int offset) {
        return listar(anio, mes, "", Filtro.TODOS, limite, offset);
    }

    /** Total con esos filtros; llámalo con cada Filtro para los números de las pestañas. */
    public int contar(int anio, int mes, String texto, Filtro filtro) {
        List<Object> p = new ArrayList<>();
        return numero("SELECT COUNT(*) FROM vw_movimientos" + where(anio, mes, texto, filtro, p), p.toArray());
    }

    public int contar(int anio, int mes, Filtro filtro) {
        return contar(anio, mes, "", filtro);
    }

    // ---------- Categorías de gasto ----------

    public List<Categoria> listarCategoriasGasto() {
        return consultar("SELECT id, nombre FROM categorias_gasto ORDER BY nombre",
                rs -> new Categoria(rs.getInt("id"), rs.getString("nombre")));
    }

    public int crearCategoriaGasto(String nombre) {
        exigir(hayTexto(nombre), "El nombre de la categoría es obligatorio.");
        return insertar("INSERT INTO categorias_gasto (nombre) VALUES (?)", nombre.trim());
    }

    // ---------- Internos ----------

    private static String where(int anio, int mes, String texto, Filtro filtro, List<Object> p) {
        LocalDate desde = LocalDate.of(anio, mes, 1);
        p.add(desde);
        p.add(desde.plusMonths(1));
        StringBuilder w = new StringBuilder(" WHERE fecha >= ? AND fecha < ?");
        if (hayTexto(texto)) {
            w.append(" AND (concepto LIKE ? OR codigo LIKE ?)");
            String like = contiene(texto);
            p.add(like);
            p.add(like);
        }
        switch (filtro == null ? Filtro.TODOS : filtro) {
            case INGRESOS -> w.append(" AND tipo = 'Ingreso'");
            case GASTOS -> w.append(" AND tipo = 'Gasto'");
            case POR_COBRAR -> w.append(" AND tipo = 'Ingreso' AND estado = 'Pendiente'");
            default -> { }
        }
        return w.toString();
    }
}
