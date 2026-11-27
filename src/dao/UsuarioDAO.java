package dao;

import modelo.Usuario;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

/** Usuarios y login. No implementa Crud porque crear un usuario necesita también la clave. */
public class UsuarioDAO extends BaseDAO {

    private static Usuario mapear(ResultSet rs) throws SQLException {
        return new Usuario(rs.getInt("id"), rs.getString("nombre"), rs.getString("usuario"),
                rs.getString("rol"), rs.getBoolean("activo"));
    }

    /** true si ya hay algún usuario. Si es false, al arrancar se crea el administrador inicial. */
    public boolean hayUsuarios() {
        return numero("SELECT COUNT(*) FROM usuarios") > 0;
    }

    /**
     * Login. Devuelve el usuario si el nombre y la clave son correctos y está activo;
     * si no, Optional.empty() (sin decir cuál de los dos falló).
     */
    public Optional<Usuario> autenticar(String usuario, String clave) {
        if (!hayTexto(usuario) || clave == null || clave.isEmpty()) {
            return Optional.empty();
        }
        String[] hashGuardado = new String[1];
        Usuario u = uno("SELECT id, nombre, usuario, rol, activo, password_hash FROM usuarios "
                        + "WHERE usuario = ? AND activo = 1",
                rs -> {
                    hashGuardado[0] = rs.getString("password_hash");
                    return mapear(rs);
                }, usuario.trim());
        if (u == null) {
            Seguridad.verificar(clave, "pbkdf2$1$AAAA$AAAA");   // gasta tiempo parecido aunque el usuario no exista
            return Optional.empty();
        }
        return Seguridad.verificar(clave, hashGuardado[0]) ? Optional.of(u) : Optional.empty();
    }

    /** Crea un usuario; la clave se guarda con hash, nunca en texto. rol: "Administrador" o "Empleado". */
    public int crear(String nombre, String usuario, String clave, String rol) {
        exigir(hayTexto(nombre), "El nombre es obligatorio.");
        exigir(hayTexto(usuario), "El usuario es obligatorio.");
        exigir(clave != null && clave.length() >= 6, "La clave debe tener al menos 6 caracteres.");
        exigir("Administrador".equals(rol) || "Empleado".equals(rol), "Rol no válido.");
        return insertar("INSERT INTO usuarios (nombre, usuario, password_hash, rol) VALUES (?, ?, ?, ?)",
                nombre.trim(), usuario.trim(), Seguridad.hashear(clave), rol);
    }

    /** Crea un Empleado (sobrecarga con el rol por defecto). */
    public int crear(String nombre, String usuario, String clave) {
        return crear(nombre, usuario, clave, "Empleado");
    }

    public boolean cambiarClave(int id, String claveNueva) {
        exigir(claveNueva != null && claveNueva.length() >= 6, "La clave debe tener al menos 6 caracteres.");
        return ejecutar("UPDATE usuarios SET password_hash = ? WHERE id = ?", Seguridad.hashear(claveNueva), id) > 0;
    }

    public boolean desactivar(int id) {
        return ejecutar("UPDATE usuarios SET activo = 0 WHERE id = ?", id) > 0;
    }

    public List<Usuario> listar() {
        return consultar("SELECT id, nombre, usuario, rol, activo FROM usuarios ORDER BY nombre", UsuarioDAO::mapear);
    }
}
