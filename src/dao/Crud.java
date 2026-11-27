package dao;

import java.util.List;

/**
 * Interfaz: el contrato que cumplen todos los DAO de entidades.
 * T es el tipo de objeto que maneja (Cliente, Cita, Producto...).
 */
public interface Crud<T> {

    /** Guarda un objeto nuevo y devuelve el id que le puso MySQL. */
    int insertar(T objeto);

    /** Guarda los cambios de un objeto que ya existe. */
    boolean actualizar(T objeto);

    boolean eliminar(int id);

    /** Devuelve null si no existe. */
    T buscarPorId(int id);

    List<T> listarTodos();
}

