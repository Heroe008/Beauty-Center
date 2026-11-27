package dao;

import modelo.Categoria;
import modelo.Producto;
import modelo.ProductoStats;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ProductoDAO extends DaoCrud<Producto> {

    @Override
    protected String consultaBase() {
        return "SELECT id, codigo, nombre, categoria_id, categoria, stock_actual, stock_minimo, precio_costo, estado "
             + "FROM vw_productos";
    }

    @Override
    protected String orden() {
        return " ORDER BY FIELD(estado, 'Agotado', 'Stock bajo', 'Disponible'), nombre";
    }

    @Override
    protected Producto mapear(ResultSet rs) throws SQLException {
        return new Producto(rs.getInt("id"), rs.getString("codigo"), rs.getString("nombre"),
                rs.getInt("categoria_id"), rs.getString("categoria"), rs.getInt("stock_actual"),
                rs.getInt("stock_minimo"), rs.getBigDecimal("precio_costo"), rs.getString("estado"));
    }

    @Override
    public int insertar(Producto p) {
        validar(p);
        return insertar("INSERT INTO productos (nombre, categoria_id, stock_actual, stock_minimo, precio_costo) "
                        + "VALUES (?, ?, ?, ?, ?)",
                p.getNombre().trim(), p.getCategoriaId(), p.getStockActual(), p.getStockMinimo(), costo(p));
    }

    /**
     * Cambia los datos del producto. Para sumar o restar stock usa MovimientoStockDAO
     * (así queda el historial); stockActual aquí solo sirve para corregir un conteo.
     */
    @Override
    public boolean actualizar(Producto p) {
        validar(p);
        return ejecutar("UPDATE productos SET nombre = ?, categoria_id = ?, stock_actual = ?, "
                        + "stock_minimo = ?, precio_costo = ? WHERE id = ?",
                p.getNombre().trim(), p.getCategoriaId(), p.getStockActual(), p.getStockMinimo(), costo(p),
                p.getId()) > 0;
    }

    /** Eliminación lógica. */
    @Override
    public boolean eliminar(int id) {
        return ejecutar("UPDATE productos SET eliminado = 1 WHERE id = ?", id) > 0;
    }

    // ---------- Búsquedas (sobrecarga) ----------

    /**
     * categoriaId: null = todas las categorías.
     * estado: "Todos", "Disponible", "Stock bajo" o "Agotado".
     * Primero salen los agotados y los de stock bajo.
     */
    public List<Producto> listar(String texto, Integer categoriaId, String estado, int limite, int offset) {
        List<Object> p = new ArrayList<>();
        String sql = consultaBase() + where(texto, categoriaId, estado, p) + orden() + " LIMIT ? OFFSET ?";
        p.add(limite);
        p.add(offset);
        return consultar(sql, this::mapear, p.toArray());
    }

    public List<Producto> listar(int limite, int offset) {
        return listar("", null, "Todos", limite, offset);
    }

    public int contar(String texto, Integer categoriaId, String estado) {
        List<Object> p = new ArrayList<>();
        return numero("SELECT COUNT(*) FROM vw_productos" + where(texto, categoriaId, estado, p), p.toArray());
    }

    public int contar() {
        return contar("", null, "Todos");
    }

    /** Resumen: "Inventario que necesita atención". */
    public List<Producto> necesitanAtencion() {
        return consultar(consultaBase() + " WHERE estado <> 'Disponible' "
                + "ORDER BY FIELD(estado, 'Agotado', 'Stock bajo'), nombre", this::mapear);
    }

    public ProductoStats estadisticas() {
        return uno("SELECT COUNT(*), COUNT(DISTINCT categoria_id), "
                + "COALESCE(SUM(estado = 'Disponible'), 0), COALESCE(SUM(estado = 'Stock bajo'), 0), "
                + "COALESCE(SUM(estado = 'Agotado'), 0) FROM vw_productos",
                rs -> new ProductoStats(rs.getInt(1), rs.getInt(2), rs.getInt(3), rs.getInt(4), rs.getInt(5)));
    }

    // ---------- Categorías ----------

    public List<Categoria> listarCategorias() {
        return consultar("SELECT id, nombre FROM categorias_producto ORDER BY nombre",
                rs -> new Categoria(rs.getInt("id"), rs.getString("nombre")));
    }

    public int crearCategoria(String nombre) {
        exigir(hayTexto(nombre), "El nombre de la categoría es obligatorio.");
        return insertar("INSERT INTO categorias_producto (nombre) VALUES (?)", nombre.trim());
    }

    // ---------- Internos ----------

    private static String where(String texto, Integer categoriaId, String estado, List<Object> p) {
        StringBuilder w = new StringBuilder(" WHERE 1=1");
        if (hayTexto(texto)) {
            w.append(" AND (nombre LIKE ? OR codigo LIKE ?)");
            String like = contiene(texto);
            p.add(like);
            p.add(like);
        }
        if (categoriaId != null) {
            w.append(" AND categoria_id = ?");
            p.add(categoriaId);
        }
        if (hayTexto(estado) && !"Todos".equalsIgnoreCase(estado)) {
            w.append(" AND estado = ?");
            p.add(estado);
        }
        return w.toString();
    }

    private static BigDecimal costo(Producto p) {
        return p.getPrecioCosto() == null ? BigDecimal.ZERO : p.getPrecioCosto();
    }

    private static void validar(Producto p) {
        exigir(hayTexto(p.getNombre()), "El nombre del producto es obligatorio.");
        exigir(p.getCategoriaId() > 0, "Elige una categoría.");
        exigir(p.getStockActual() >= 0, "El stock no puede ser negativo.");
        exigir(p.getStockMinimo() >= 0, "El stock mínimo no puede ser negativo.");
        exigir(p.getPrecioCosto() == null || p.getPrecioCosto().signum() >= 0, "El costo no puede ser negativo.");
    }
}
