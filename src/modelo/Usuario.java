package modelo;

/** Usuario que entra al sistema. Nunca lleva la contraseña ni su hash. rol: Administrador | Empleado. */
public class Usuario extends Persona {

    private String usuario;
    private String rol;
    private boolean activo;

    public Usuario() {
    }

    /** Usuario nuevo, activo por defecto. */
    public Usuario(String nombre, String usuario, String rol) {
        super(0, nombre);
        this.usuario = usuario;
        this.rol = rol;
        this.activo = true;
    }

    /** Con todos los datos (al leer de la base de datos). */
    public Usuario(int id, String nombre, String usuario, String rol, boolean activo) {
        super(id, nombre);
        this.usuario = usuario;
        this.rol = rol;
        this.activo = activo;
    }

    @Override
    public String getTipo() {
        return "Usuario";
    }

    public boolean esAdministrador() {
        return "Administrador".equals(rol);
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public String getRol() {
        return rol;
    }

    public void setRol(String rol) {
        this.rol = rol;
    }

    public boolean isActivo() {
        return activo;
    }

    public void setActivo(boolean activo) {
        this.activo = activo;
    }
}
