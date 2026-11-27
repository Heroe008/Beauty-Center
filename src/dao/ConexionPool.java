package dao;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Proxy;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * OPTIMIZACIÓN: en vez de abrir una conexión nueva a MySQL en cada consulta (lento),
 * se reutilizan unas pocas. Cuando el DAO hace connection.close() la conexión no se
 * cierra de verdad: vuelve al pool para el siguiente. El código de los DAO no cambia.
 */
final class ConexionPool {

    /** Cómo crear una conexión real (así el pool se puede probar sin MySQL). */
    @FunctionalInterface
    interface Fabrica {
        Connection crear() throws SQLException;
    }

    private final Fabrica fabrica;
    private final BlockingQueue<Connection> libres;

    ConexionPool(Fabrica fabrica, int maximoGuardadas) {
        this.fabrica = fabrica;
        this.libres = new ArrayBlockingQueue<>(maximoGuardadas);
    }

    /** Entrega una conexión del pool (o una nueva si no hay libres o estaban caídas). */
    Connection prestar() throws SQLException {
        Connection real;
        while ((real = libres.poll()) != null) {
            if (estaViva(real)) {
                return envolver(real);
            }
            cerrarSinError(real);
        }
        return envolver(fabrica.crear());
    }

    /** Cuántas conexiones están guardadas y libres (para pruebas). */
    int libres() {
        return libres.size();
    }

    /** Cierra de verdad todas las conexiones guardadas (al salir del programa). */
    void cerrarTodas() {
        Connection c;
        while ((c = libres.poll()) != null) {
            cerrarSinError(c);
        }
    }

    // La conexión que recibe el DAO es un "intermediario" que intercepta close().
    private Connection envolver(Connection real) {
        AtomicBoolean devuelta = new AtomicBoolean(false);
        return (Connection) Proxy.newProxyInstance(
                Connection.class.getClassLoader(),
                new Class<?>[]{Connection.class},
                (proxy, metodo, args) -> {
                    if (metodo.getName().equals("close")) {
                        if (devuelta.compareAndSet(false, true)) {
                            devolver(real);
                        }
                        return null;
                    }
                    try {
                        return metodo.invoke(real, args);
                    } catch (InvocationTargetException e) {
                        throw e.getCause();
                    }
                });
    }

    private void devolver(Connection real) {
        try {
            if (real.isClosed()) {
                return;
            }
            if (!real.getAutoCommit()) {
                real.rollback();
                real.setAutoCommit(true);
            }
            if (!libres.offer(real)) {   // el pool está lleno: se cierra de verdad
                real.close();
            }
        } catch (SQLException e) {
            cerrarSinError(real);
        }
    }

    private static boolean estaViva(Connection c) {
        try {
            return !c.isClosed() && c.isValid(1);
        } catch (SQLException e) {
            return false;
        }
    }

    private static void cerrarSinError(Connection c) {
        try {
            c.close();
        } catch (SQLException ignorado) {
            // ya estaba caída
        }
    }
}
