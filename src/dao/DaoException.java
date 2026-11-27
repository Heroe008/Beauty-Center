package dao;

/**
 * Error de base de datos con un mensaje que se le puede mostrar al usuario
 * (JOptionPane.showMessageDialog(this, e.getMessage())). Es unchecked para no
 * llenar la UI de try/catch obligatorios.
 */
public class DaoException extends RuntimeException {
    private static final long serialVersionUID = 1L;


    public DaoException(String mensaje, Throwable causa) {
        super(mensaje, causa);
    }

    public DaoException(String mensaje) {
        super(mensaje);
    }
}
