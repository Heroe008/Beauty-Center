package dao;

import modelo.Cliente;
import modelo.Describible;
import modelo.GastoCategoria;
import modelo.Movimiento;
import modelo.ResumenMensual;

import java.util.ArrayList;
import java.util.List;

/**
 * Para probar que todo conecta: clic derecho en este archivo -> Run File.
 * Imprime en la consola lo que debería salir con los datos de prueba.
 * (Antes agrega el jar de MySQL Connector/J al proyecto y revisa Conexion.java.)
 */
public class PruebaDAO {

    public static void main(String[] args) {
        boolean ok = Conexion.probar();
        System.out.println("Conexión: " + (ok ? "OK" : "FALLÓ, revisa Conexion.java"));
        if (!ok) {
            return;
        }

        ClienteDAO clientes = new ClienteDAO();
        System.out.println("Clientes: " + clientes.contar() + " en total");           // 24
        System.out.println("  " + clientes.estadisticas());                            // 24 / 20 / 4 / 3
        for (Cliente c : clientes.listar(3, 0)) {
            System.out.println("  " + c.descripcion() + " · " + c.getVisitas() + " visitas · " + c.getEstado());
        }

        System.out.println("Citas: " + new CitaDAO().estadisticas());                  // 3 canceladas / 15 / 5
        System.out.println("Inventario: " + new ProductoDAO().estadisticas());         // 24 / 4 / 20 / 3 / 1

        ReporteDAO reportes = new ReporteDAO();
        ResumenMensual nov = reportes.resumenMes(2026, 11);
        System.out.println("Noviembre: ingresos " + nov.getIngresos() + ", gastos " + nov.getGastos()
                + ", neto " + nov.getBeneficioNeto() + ", por cobrar " + nov.getPorCobrar()
                + " (" + nov.getPagosPendientes() + " pagos), margen " + nov.margen() + "%");
        for (GastoCategoria g : reportes.gastosPorCategoria(2026, 11)) {
            System.out.println("  " + g.getCategoria() + ": " + g.getTotal() + " (" + g.getPorcentaje() + "%)");
        }

        MovimientoDAO movimientos = new MovimientoDAO();
        System.out.println("Movimientos nov: " + movimientos.contar(2026, 11, MovimientoDAO.Filtro.TODOS)); // 48

        // Polimorfismo: la lista mezcla Ingreso y Gasto, y cada uno responde a su manera.
        for (Movimiento m : movimientos.listar(2026, 11, 4, 0)) {
            System.out.println("  " + m.getClass().getSimpleName() + " -> " + m.descripcion()
                    + " | con signo: " + m.getImporteConSigno());
        }

        // Cliente, Cita, Producto y Movimiento son Describible (interfaz).
        List<Describible> cosas = new ArrayList<>();
        cosas.addAll(clientes.listar(1, 0));
        cosas.addAll(new ProductoDAO().listar(1, 0));
        cosas.addAll(new CitaDAO().listar(1, 0));
        for (Describible d : cosas) {
            System.out.println("  Describible: " + d.descripcion());
        }

        System.out.println("¿Hay usuarios? " + new UsuarioDAO().hayUsuarios());
        Conexion.cerrarTodas();
    }
}
