package dao;

import modelo.Servicio;

import java.sql.ResultSet;
import java.sql.SQLException;

public class ServicioDAO extends DaoCrud<Servicio> {

    @Override
    protected String consultaBase() {
        return "SELECT id, nombre, duracion_min, precio, activo FROM servicios";
    }

    /** Solo los servicios activos (los "eliminados" quedan guardados pero no se ofrecen). */
    @Override
    protected String condicionBase() {
        return "activo = 1";
    }

    @Override
    protected String orden() {
        return " ORDER BY nombre";
    }

    @Override
    protected Servicio mapear(ResultSet rs) throws SQLException {
        return new Servicio(rs.getInt("id"), rs.getString("nombre"), rs.getInt("duracion_min"),
                rs.getBigDecimal("precio"), rs.getBoolean("activo"));
    }

    @Override
    public int insertar(Servicio s) {
        validar(s);
        return insertar("INSERT INTO servicios (nombre, duracion_min, precio) VALUES (?, ?, ?)",
                s.getNombre().trim(), s.getDuracionMin(), s.getPrecio());
    }

    @Override
    public boolean actualizar(Servicio s) {
        validar(s);
        return ejecutar("UPDATE servicios SET nombre = ?, duracion_min = ?, precio = ? WHERE id = ?",
                s.getNombre().trim(), s.getDuracionMin(), s.getPrecio(), s.getId()) > 0;
    }

    /** Se desactiva en vez de borrar para no perder las citas viejas que lo usan. */
    @Override
    public boolean eliminar(int id) {
        return ejecutar("UPDATE servicios SET activo = 0 WHERE id = ?", id) > 0;
    }

    private static void validar(Servicio s) {
        exigir(hayTexto(s.getNombre()), "El nombre del servicio es obligatorio.");
        exigir(s.getDuracionMin() > 0, "La duración debe ser mayor que cero.");
        exigir(s.getPrecio() != null && s.getPrecio().signum() >= 0, "El precio no puede ser negativo.");
    }
}
