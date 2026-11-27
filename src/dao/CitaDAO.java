package dao;

import modelo.Cita;
import modelo.CitaStats;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CitaDAO extends DaoCrud<Cita> {

    @Override
    protected String consultaBase() {
        return "SELECT id, cliente_id, cliente, servicio_id, servicio, fecha, hora, duracion_min, estado, notas "
             + "FROM vw_citas";
    }

    @Override
    protected String orden() {
        return " ORDER BY fecha, hora";
    }

    @Override
    protected Cita mapear(ResultSet rs) throws SQLException {
        return new Cita(rs.getInt("id"), rs.getInt("cliente_id"), rs.getString("cliente"),
                rs.getInt("servicio_id"), rs.getString("servicio"), fecha(rs, "fecha"), hora(rs, "hora"),
                rs.getInt("duracion_min"), rs.getString("estado"), rs.getString("notas"));
    }

    @Override
    public int insertar(Cita c) {
        validar(c);
        return insertar("INSERT INTO citas (cliente_id, servicio_id, fecha, hora, duracion_min, estado, notas) "
                        + "VALUES (?, ?, ?, ?, ?, ?, ?)",
                c.getClienteId(), c.getServicioId(), c.getFecha(), c.getHora(), c.getDuracionMin(),
                estadoOPendiente(c), vacioANull(c.getNotas()));
    }

    @Override
    public boolean actualizar(Cita c) {
        validar(c);
        return ejecutar("UPDATE citas SET cliente_id = ?, servicio_id = ?, fecha = ?, hora = ?, "
                        + "duracion_min = ?, estado = ?, notas = ? WHERE id = ?",
                c.getClienteId(), c.getServicioId(), c.getFecha(), c.getHora(), c.getDuracionMin(),
                estadoOPendiente(c), vacioANull(c.getNotas()), c.getId()) > 0;
    }

    /** Borra la cita. Si ya tiene un pago ligado, MySQL no deja y sale un mensaje; cancélala mejor. */
    @Override
    public boolean eliminar(int id) {
        return ejecutar("DELETE FROM citas WHERE id = ?", id) > 0;
    }

    /** Confirmar, cancelar o completar una cita. */
    public boolean cambiarEstado(int id, String estado) {
        exigir(List.of("Pendiente", "Confirmada", "Cancelada", "Completada").contains(estado),
                "Estado de cita no válido: " + estado);
        return ejecutar("UPDATE citas SET estado = ? WHERE id = ?", estado, id) > 0;
    }

    // ---------- Búsquedas (sobrecarga) ----------

    /**
     * Citas de la pantalla Citas (todas menos las ya completadas).
     * estado: "Todos", "Pendiente", "Confirmada" o "Cancelada".
     */
    public List<Cita> listar(String texto, String estado, int limite, int offset) {
        List<Object> p = new ArrayList<>();
        String sql = consultaBase() + where(texto, estado, p) + " ORDER BY fecha, hora LIMIT ? OFFSET ?";
        p.add(limite);
        p.add(offset);
        return consultar(sql, this::mapear, p.toArray());
    }

    public List<Cita> listar(int limite, int offset) {
        return listar("", "Todos", limite, offset);
    }

    public int contar(String texto, String estado) {
        List<Object> p = new ArrayList<>();
        return numero("SELECT COUNT(*) FROM vw_citas" + where(texto, estado, p), p.toArray());
    }

    public int contar() {
        return contar("", "Todos");
    }

    public CitaStats estadisticas() {
        return uno("SELECT COALESCE(SUM(estado = 'Cancelada'), 0), COALESCE(SUM(estado = 'Confirmada'), 0), "
                + "COALESCE(SUM(estado = 'Pendiente'), 0) FROM citas",
                rs -> new CitaStats(rs.getInt(1), rs.getInt(2), rs.getInt(3)));
    }

    /** Citas de hoy que siguen vigentes (Resumen: "Citas de hoy"). */
    public List<Cita> deHoy() {
        return consultar(consultaBase()
                + " WHERE fecha = CURDATE() AND estado IN ('Confirmada', 'Pendiente') ORDER BY hora", this::mapear);
    }

    /** Próximas citas vigentes desde hoy (Resumen: "Próximas citas"). */
    public List<Cita> proximas(int limite) {
        return consultar(consultaBase() + " WHERE fecha >= CURDATE() AND estado IN ('Confirmada', 'Pendiente') "
                + "ORDER BY fecha, hora LIMIT ?", this::mapear, limite);
    }

    /** Historial de un cliente (visitas completadas y citas futuras). */
    public List<Cita> deCliente(int clienteId) {
        return consultar(consultaBase() + " WHERE cliente_id = ? ORDER BY fecha DESC, hora DESC",
                this::mapear, clienteId);
    }

    // ---------- Internos ----------

    private static String where(String texto, String estado, List<Object> p) {
        StringBuilder w = new StringBuilder(" WHERE estado <> 'Completada'");
        if (hayTexto(texto)) {
            w.append(" AND (cliente LIKE ? OR servicio LIKE ?)");
            String like = contiene(texto);
            p.add(like);
            p.add(like);
        }
        if (hayTexto(estado) && !"Todos".equalsIgnoreCase(estado)) {
            w.append(" AND estado = ?");
            p.add(estado);
        }
        return w.toString();
    }

    private static String estadoOPendiente(Cita c) {
        return hayTexto(c.getEstado()) ? c.getEstado() : "Pendiente";
    }

    private static void validar(Cita c) {
        exigir(c.getClienteId() > 0, "Elige un cliente.");
        exigir(c.getServicioId() > 0, "Elige un servicio.");
        exigir(c.getFecha() != null, "La fecha es obligatoria.");
        exigir(c.getHora() != null, "La hora es obligatoria.");
        exigir(c.getDuracionMin() > 0, "La duración debe ser mayor que cero.");
    }
}
