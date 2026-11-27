package dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

/**
 * Clase abstracta para los DAO de entidades. Ya implementa buscarPorId y listarTodos
 * una sola vez para todos; cada hija solo dice cómo se lee una fila (mapear), de
 * dónde se lee (consultaBase) y cómo inserta, actualiza y elimina.
 */
public abstract class DaoCrud<T> extends BaseDAO implements Crud<T> {

    /** Convierte la fila actual del ResultSet en un objeto. */
    protected abstract T mapear(ResultSet rs) throws SQLException;

    /** SELECT con las columnas y la tabla o vista, sin WHERE. */
    protected abstract String consultaBase();

    /** Condición que siempre se aplica (por ejemplo "activo = 1"). */
    protected String condicionBase() {
        return "1=1";
    }

    /** Orden por defecto de listarTodos. */
    protected String orden() {
        return " ORDER BY id";
    }

    @Override
    public T buscarPorId(int id) {
        return uno(consultaBase() + " WHERE " + condicionBase() + " AND id = ?", this::mapear, id);
    }

    @Override
    public List<T> listarTodos() {
        return consultar(consultaBase() + " WHERE " + condicionBase() + orden(), this::mapear);
    }
}
