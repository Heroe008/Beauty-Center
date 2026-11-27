package dao;

import modelo.MovimientoStock;

import java.util.List;

/**
 * Entradas y salidas de inventario. Al insertar, el trigger de MySQL
 * (trg_movstock_ai) suma o resta el stock del producto automáticamente.
 */
public class MovimientoStockDAO extends BaseDAO {

    /** Botón "Reponer". */
    public int reponer(int productoId, int cantidad, String nota) {
        return registrar(productoId, "Entrada", cantidad, nota);
    }

    /** Reponer sin nota (sobrecarga). */
    public int reponer(int productoId, int cantidad) {
        return reponer(productoId, cantidad, "Reposición");
    }

    /** Uso de material. Si no hay suficiente stock, MySQL lo rechaza y sale un mensaje. */
    public int consumir(int productoId, int cantidad, String nota) {
        return registrar(productoId, "Salida", cantidad, nota);
    }

    public int consumir(int productoId, int cantidad) {
        return consumir(productoId, cantidad, null);
    }

    private int registrar(int productoId, String tipo, int cantidad, String nota) {
        exigir(productoId > 0, "Elige un producto.");
        exigir(cantidad > 0, "La cantidad debe ser mayor que cero.");
        return insertar("INSERT INTO movimientos_stock (producto_id, tipo, cantidad, nota) VALUES (?, ?, ?, ?)",
                productoId, tipo, cantidad, vacioANull(nota));
    }

    public List<MovimientoStock> historial(int productoId, int limite) {
        return consultar("SELECT id, producto_id, tipo, cantidad, fecha, nota FROM movimientos_stock "
                        + "WHERE producto_id = ? ORDER BY fecha DESC, id DESC LIMIT ?",
                rs -> new MovimientoStock(rs.getInt("id"), rs.getInt("producto_id"), rs.getString("tipo"),
                        rs.getInt("cantidad"), fechaHora(rs, "fecha"), rs.getString("nota")),
                productoId, limite);
    }
}
