package dao;

import modelo.Cliente;
import modelo.ClienteStats;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ClienteDAO extends DaoCrud<Cliente> {

    // ---------- Lo que pide DaoCrud ----------

    @Override
    protected String consultaBase() {
        return "SELECT id, codigo, nombre, telefono, email, notas, creado_en, visitas, ultima_visita, estado "
             + "FROM vw_clientes";
    }

    @Override
    protected String orden() {
        return " ORDER BY nombre";
    }

    @Override
    protected Cliente mapear(ResultSet rs) throws SQLException {
        return new Cliente(rs.getInt("id"), rs.getString("codigo"), rs.getString("nombre"),
                rs.getString("telefono"), rs.getString("email"), rs.getString("notas"),
                fechaHora(rs, "creado_en"), rs.getInt("visitas"), fecha(rs, "ultima_visita"),
                rs.getString("estado"));
    }

    // ---------- Lo que pide Crud ----------

    /** Devuelve el id nuevo. */
    @Override
    public int insertar(Cliente c) {
        validar(c);
        return insertar("INSERT INTO clientes (nombre, telefono, email, notas) VALUES (?, ?, ?, ?)",
                c.getNombre().trim(), vacioANull(c.getTelefono()), vacioANull(c.getEmail()), vacioANull(c.getNotas()));
    }

    @Override
    public boolean actualizar(Cliente c) {
        validar(c);
        return ejecutar("UPDATE clientes SET nombre = ?, telefono = ?, email = ?, notas = ? "
                        + "WHERE id = ? AND eliminado = 0",
                c.getNombre().trim(), vacioANull(c.getTelefono()), vacioANull(c.getEmail()),
                vacioANull(c.getNotas()), c.getId()) > 0;
    }

    /** Eliminación lógica: el cliente desaparece de las listas pero sus citas y pagos se conservan. */
    @Override
    public boolean eliminar(int id) {
        return ejecutar("UPDATE clientes SET eliminado = 1 WHERE id = ?", id) > 0;
    }

    // ---------- Búsquedas: el mismo método con distintos parámetros (sobrecarga) ----------

    /** Página de la tabla con búsqueda y estado ("Todos", "Activo" o "Inactivo"). */
    public List<Cliente> listar(String texto, String estado, int limite, int offset) {
        List<Object> p = new ArrayList<>();
        String sql = consultaBase() + where(texto, estado, p)
                + " ORDER BY ultima_visita IS NULL, ultima_visita DESC, id DESC LIMIT ? OFFSET ?";
        p.add(limite);
        p.add(offset);
        return consultar(sql, this::mapear, p.toArray());
    }

    /** Página sin filtros. */
    public List<Cliente> listar(int limite, int offset) {
        return listar("", "Todos", limite, offset);
    }

    /** Búsqueda por texto, sin paginar (para un buscador rápido). */
    public List<Cliente> listar(String texto) {
        return listar(texto, "Todos", Integer.MAX_VALUE, 0);
    }

    /** Total con esos filtros (para "Mostrando 1-6 de 24" y la paginación). */
    public int contar(String texto, String estado) {
        List<Object> p = new ArrayList<>();
        return numero("SELECT COUNT(*) FROM vw_clientes" + where(texto, estado, p), p.toArray());
    }

    /** Total de clientes. */
    public int contar() {
        return contar("", "Todos");
    }

    public ClienteStats estadisticas() {
        return uno("SELECT COUNT(*), "
                + "COALESCE(SUM(estado = 'Activo'), 0), "
                + "COALESCE(SUM(estado = 'Inactivo'), 0), "
                + "COALESCE(SUM(YEAR(creado_en) = YEAR(CURDATE()) AND MONTH(creado_en) = MONTH(CURDATE())), 0) "
                + "FROM vw_clientes",
                rs -> new ClienteStats(rs.getInt(1), rs.getInt(2), rs.getInt(3), rs.getInt(4)));
    }

    // ---------- Internos ----------

    private static String where(String texto, String estado, List<Object> p) {
        StringBuilder w = new StringBuilder(" WHERE 1=1");
        if (hayTexto(texto)) {
            w.append(" AND (nombre LIKE ? OR telefono LIKE ? OR codigo LIKE ?)");
            String like = contiene(texto);
            p.add(like);
            p.add(like);
            p.add(like);
        }
        if (hayTexto(estado) && !"Todos".equalsIgnoreCase(estado)) {
            w.append(" AND estado = ?");
            p.add(estado);
        }
        return w.toString();
    }

    private static void validar(Cliente c) {
        exigir(hayTexto(c.getNombre()), "El nombre del cliente es obligatorio.");
    }
}
