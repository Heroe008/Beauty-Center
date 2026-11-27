package modelo;

import java.time.LocalDate;
import java.time.LocalDateTime;

/** Cliente del salón. codigo, visitas, ultimaVisita y estado los calcula la vista vw_clientes. */
public class Cliente extends Persona {

    private String codigo;
    private String telefono;
    private String email;
    private String notas;
    private LocalDateTime creadoEn;
    private int visitas;
    private LocalDate ultimaVisita;
    private String estado;

    // ---------- Constructores (sobrecargados) ----------

    public Cliente() {
    }

    /** Para crear un cliente nuevo (sin id; lo asigna MySQL). */
    public Cliente(String nombre, String telefono, String email, String notas) {
        super(0, nombre);
        this.telefono = telefono;
        this.email = email;
        this.notas = notas;
    }

    /** Con todos los datos (así se arma al leer de la base de datos). */
    public Cliente(int id, String codigo, String nombre, String telefono, String email, String notas,
                   LocalDateTime creadoEn, int visitas, LocalDate ultimaVisita, String estado) {
        super(id, nombre);
        this.codigo = codigo;
        this.telefono = telefono;
        this.email = email;
        this.notas = notas;
        this.creadoEn = creadoEn;
        this.visitas = visitas;
        this.ultimaVisita = ultimaVisita;
        this.estado = estado;
    }

    // ---------- Polimorfismo ----------

    @Override
    public String getTipo() {
        return "Cliente";
    }

    /** Sobrescribe el de Persona para incluir el código (CLI-001). */
    @Override
    public String descripcion() {
        return codigo == null ? super.descripcion() : codigo + " · " + getNombre();
    }

    // ---------- Getters y setters ----------

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getNotas() {
        return notas;
    }

    public void setNotas(String notas) {
        this.notas = notas;
    }

    public LocalDateTime getCreadoEn() {
        return creadoEn;
    }

    public void setCreadoEn(LocalDateTime creadoEn) {
        this.creadoEn = creadoEn;
    }

    public int getVisitas() {
        return visitas;
    }

    public void setVisitas(int visitas) {
        this.visitas = visitas;
    }

    public LocalDate getUltimaVisita() {
        return ultimaVisita;
    }

    public void setUltimaVisita(LocalDate ultimaVisita) {
        this.ultimaVisita = ultimaVisita;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }
}
