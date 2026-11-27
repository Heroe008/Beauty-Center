package dao;


import java.sql.Connection;
import java.sql.SQLException;

/**
 * Entrega conexiones a los DAO. Las crea con tu clase conexion.Conexion
 * (ahí están la URL, el usuario y la clave) y las reutiliza con un pool
 * (ver ConexionPool), así no se abre una conexión nueva en cada consulta.
 *
 * Requiere el jar "MySQL Connector/J" en las librerías del proyecto.
 */
public final class Conexion {

    private static final ConexionPool POOL = new ConexionPool(Conexion::crearConexion, 4);

    private Conexion() {
    }

    /** Abre una conexión nueva usando tu clase conexion.Conexion (devuelve null si falla). */
    private static Connection crearConexion() throws SQLException {
        Connection c = conexion.Conexion.getConexion();
        if (c == null) {
            // SQLState "08001" = no se pudo conectar; los DAO lo convierten en un mensaje claro
            throw new SQLException("No se pudo conectar con MySQL", "08001");
        }
        return c;
    }

    /** Conexión lista para usar. Ciérrala con try-with-resources (los DAO ya lo hacen): vuelve al pool. */
    public static Connection obtener() throws SQLException {
        return POOL.prestar();
    }

    /** true si se puede conectar. Útil para avisar al arrancar el programa. */
    public static boolean probar() {
        try (Connection c = obtener()) {
            return c.isValid(3);
        } catch (SQLException e) {
            return false;
        }
    }

    /** Llamar al cerrar la aplicación para soltar las conexiones guardadas. */
    public static void cerrarTodas() {
        POOL.cerrarTodas();
    }
}