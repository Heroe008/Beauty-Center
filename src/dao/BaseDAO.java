package dao;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Time;
import java.sql.Timestamp;
import java.sql.Types;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Lo que comparten todos los DAO: ejecutar SQL con parámetros (?), convertir
 * errores de MySQL en mensajes entendibles y leer fechas/nulos sin dolor.
 */
abstract class BaseDAO {

    @FunctionalInterface
    interface FilaAObjeto<T> {
        T mapear(ResultSet rs) throws SQLException;
    }

    /** SELECT que devuelve varias filas. */
    protected <T> List<T> consultar(String sql, FilaAObjeto<T> mapa, Object... params) {
        try (Connection c = Conexion.obtener(); PreparedStatement ps = c.prepareStatement(sql)) {
            enlazar(ps, params);
            try (ResultSet rs = ps.executeQuery()) {
                List<T> lista = new ArrayList<>();
                while (rs.next()) lista.add(mapa.mapear(rs));
                return lista;
            }
        } catch (SQLException e) {
            throw traducir(e);
        }
    }

    /** SELECT que devuelve una fila, o null si no hay. */
    protected <T> T uno(String sql, FilaAObjeto<T> mapa, Object... params) {
        List<T> lista = consultar(sql, mapa, params);
        return lista.isEmpty() ? null : lista.get(0);
    }

    /** SELECT COUNT(*) (o cualquier número en la primera columna). */
    protected int numero(String sql, Object... params) {
        Integer n = uno(sql, rs -> rs.getInt(1), params);
        return n == null ? 0 : n;
    }

    /** INSERT / UPDATE / DELETE. Devuelve cuántas filas cambió. */
    protected int ejecutar(String sql, Object... params) {
        try (Connection c = Conexion.obtener(); PreparedStatement ps = c.prepareStatement(sql)) {
            enlazar(ps, params);
            return ps.executeUpdate();
        } catch (SQLException e) {
            throw traducir(e);
        }
    }

    /** INSERT que devuelve el id que le puso MySQL. */
    protected int insertar(String sql, Object... params) {
        try (Connection c = Conexion.obtener();
             PreparedStatement ps = c.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            enlazar(ps, params);
            ps.executeUpdate();
            try (ResultSet keys = ps.getGeneratedKeys()) {
                return keys.next() ? keys.getInt(1) : 0;
            }
        } catch (SQLException e) {
            throw traducir(e);
        }
    }

    // ------------------------------------------------------------ utilidades

    private static void enlazar(PreparedStatement ps, Object[] params) throws SQLException {
        for (int i = 0; i < params.length; i++) {
            Object p = params[i];
            if (p == null) {
                ps.setNull(i + 1, Types.NULL);
            } else if (p instanceof LocalDate d) {
                ps.setDate(i + 1, Date.valueOf(d));
            } else if (p instanceof LocalTime t) {
                ps.setTime(i + 1, Time.valueOf(t));
            } else if (p instanceof LocalDateTime dt) {
                ps.setTimestamp(i + 1, Timestamp.valueOf(dt));
            } else {
                ps.setObject(i + 1, p);
            }
        }
    }

    /** Convierte el error técnico de MySQL en un mensaje para el usuario. */
    protected static DaoException traducir(SQLException e) {
        String msg;
        switch (e.getErrorCode()) {
            case 1451 -> msg = "No se puede eliminar: hay otros registros que dependen de este.";
            case 1452 -> msg = "El cliente, servicio o categoría indicado no existe.";
            case 1062 -> msg = "Ya existe un registro con ese valor.";
            case 3819, 4025 -> msg = "Un valor no está permitido (por ejemplo, stock negativo o importe en cero).";
            case 1048 -> msg = "Falta un dato obligatorio.";
            case 1045 -> msg = "Usuario o clave de MySQL incorrectos (revisa Conexion.java).";
            case 1049 -> msg = "La base de datos beauty_center no existe. Ejecuta 01_estructura.sql.";
            default -> msg = e.getSQLState() != null && e.getSQLState().startsWith("08")
                    ? "No se pudo conectar con MySQL. ¿Está encendido el servidor?"
                    : "Error de base de datos: " + e.getMessage();
        }
        return new DaoException(msg, e);
    }

    protected static LocalDate fecha(ResultSet rs, String col) throws SQLException {
        Date d = rs.getDate(col);
        return d == null ? null : d.toLocalDate();
    }

    protected static LocalTime hora(ResultSet rs, String col) throws SQLException {
        Time t = rs.getTime(col);
        return t == null ? null : t.toLocalTime();
    }

    protected static LocalDateTime fechaHora(ResultSet rs, String col) throws SQLException {
        Timestamp t = rs.getTimestamp(col);
        return t == null ? null : t.toLocalDateTime();
    }

    /** Integer que puede ser NULL en la BD (getInt devolvería 0). */
    protected static Integer entero(ResultSet rs, String col) throws SQLException {
        int v = rs.getInt(col);
        return rs.wasNull() ? null : v;
    }

    /** Texto vacío o solo espacios -> null; si no, el texto sin espacios sobrantes. */
    protected static String vacioANull(String s) {
        return (s == null || s.isBlank()) ? null : s.trim();
    }

    /** Para LIKE: "%texto%". */
    protected static String contiene(String texto) {
        return "%" + texto.trim() + "%";
    }

    protected static boolean hayTexto(String s) {
        return s != null && !s.isBlank();
    }

    protected static void exigir(boolean condicion, String mensaje) {
        if (!condicion) throw new IllegalArgumentException(mensaje);
    }
}
