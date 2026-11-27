package dao;

import modelo.GastoCategoria;
import modelo.RangoFinanciero;
import modelo.ResumenMensual;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/** Números de las tarjetas y gráficos de Resumen y Finanzas (leen las vistas). */
public class ReporteDAO extends BaseDAO {

    private static ResumenMensual mapearResumen(ResultSet rs) throws SQLException {
        return new ResumenMensual(rs.getInt("anio"), rs.getInt("mes"), rs.getBigDecimal("ingresos"),
                rs.getBigDecimal("gastos"), rs.getBigDecimal("beneficio_neto"), rs.getBigDecimal("por_cobrar"),
                rs.getInt("pagos_pendientes"));
    }

    /** Tarjetas del mes. Si el mes no tiene movimientos devuelve todo en cero. */
    public ResumenMensual resumenMes(int anio, int mes) {
        ResumenMensual r = uno("SELECT anio, mes, ingresos, gastos, beneficio_neto, por_cobrar, pagos_pendientes "
                + "FROM vw_resumen_mensual WHERE anio = ? AND mes = ?", ReporteDAO::mapearResumen, anio, mes);
        return r != null ? r : ResumenMensual.vacio(anio, mes);
    }

    /** Lo mismo, pasando cualquier fecha del mes (sobrecarga). */
    public ResumenMensual resumenMes(LocalDate fecha) {
        return resumenMes(fecha.getYear(), fecha.getMonthValue());
    }

    /** El mes actual (sobrecarga). */
    public ResumenMensual resumenMes() {
        return resumenMes(LocalDate.now());
    }

    /** Barras "Ingresos vs gastos": los últimos n meses con datos, del más viejo al más nuevo. */
    public List<ResumenMensual> ultimosMeses(int n) {
        List<ResumenMensual> lista = new ArrayList<>(consultar(
                "SELECT anio, mes, ingresos, gastos, beneficio_neto, por_cobrar, pagos_pendientes "
                + "FROM vw_resumen_mensual ORDER BY anio DESC, mes DESC LIMIT ?", ReporteDAO::mapearResumen, n));
        Collections.reverse(lista);
        return lista;
    }

    /** Tarjeta "Gastos por categoría" con su porcentaje, de mayor a menor. */
    public List<GastoCategoria> gastosPorCategoria(int anio, int mes) {
        List<GastoCategoria> base = consultar(
                "SELECT categoria, total FROM vw_gastos_por_categoria WHERE anio = ? AND mes = ? ORDER BY total DESC",
                rs -> new GastoCategoria(rs.getString("categoria"), rs.getBigDecimal("total"), BigDecimal.ZERO),
                anio, mes);
        BigDecimal suma = BigDecimal.ZERO;
        for (GastoCategoria g : base) {
            suma = suma.add(g.getTotal());
        }
        List<GastoCategoria> lista = new ArrayList<>();
        for (GastoCategoria g : base) {
            BigDecimal pct = suma.signum() == 0 ? BigDecimal.ZERO
                    : g.getTotal().multiply(BigDecimal.valueOf(100)).divide(suma, 1, RoundingMode.HALF_UP);
            lista.add(new GastoCategoria(g.getCategoria(), g.getTotal(), pct));
        }
        return lista;
    }

    /** Gráfico de Resumen: ingresos y gastos del mes en tramos de 5 días (1-5, 6-10, ...). */
    public List<RangoFinanciero> porTramosDeCincoDias(int anio, int mes) {
        LocalDate desde = LocalDate.of(anio, mes, 1);
        int ultimoDia = desde.lengthOfMonth();
        return consultar(
                "SELECT FLOOR((DAY(fecha) - 1) / 5) AS tramo, "
                + "COALESCE(SUM(CASE WHEN tipo = 'Ingreso' THEN importe ELSE 0 END), 0) AS ingresos, "
                + "COALESCE(SUM(CASE WHEN tipo = 'Gasto' THEN importe ELSE 0 END), 0) AS gastos "
                + "FROM movimientos WHERE fecha >= ? AND fecha < ? GROUP BY tramo ORDER BY tramo",
                rs -> {
                    int t = rs.getInt("tramo");
                    return new RangoFinanciero(t * 5 + 1, Math.min(t * 5 + 5, ultimoDia),
                            rs.getBigDecimal("ingresos"), rs.getBigDecimal("gastos"));
                },
                desde, desde.plusMonths(1));
    }
}
