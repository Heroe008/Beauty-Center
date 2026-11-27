package modelo;

import java.time.LocalDate;
import java.time.LocalTime;

/**
 * Cita. estado: Pendiente, Confirmada, Cancelada o Completada.
 */
public class Cita implements Describible {

    private int id;
    private int clienteId;
    private String cliente;
    private int servicioId;
    private String servicio;
    private LocalDate fecha;
    private LocalTime hora;
    private int duracionMin;
    private String estado;
    private String notas;

    // ---------- Constructores (sobrecargados) ----------

    public Cita() {
    }

    /** Para crear un objeto nuevo (sin id; lo asigna MySQL). */
    public Cita(int clienteId, int servicioId, LocalDate fecha, LocalTime hora, int duracionMin, String estado, String notas) {
        this.clienteId = clienteId;
        this.servicioId = servicioId;
        this.fecha = fecha;
        this.hora = hora;
        this.duracionMin = duracionMin;
        this.estado = estado;
        this.notas = notas;
    }

    /** Con todos los datos (así se arma al leer de la base de datos). */
    public Cita(int id, int clienteId, String cliente, int servicioId, String servicio, LocalDate fecha, LocalTime hora, int duracionMin, String estado, String notas) {
        this.id = id;
        this.clienteId = clienteId;
        this.cliente = cliente;
        this.servicioId = servicioId;
        this.servicio = servicio;
        this.fecha = fecha;
        this.hora = hora;
        this.duracionMin = duracionMin;
        this.estado = estado;
        this.notas = notas;
    }

    // ---------- Getters y setters ----------

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getClienteId() {
        return clienteId;
    }

    public void setClienteId(int clienteId) {
        this.clienteId = clienteId;
    }

    public String getCliente() {
        return cliente;
    }

    public void setCliente(String cliente) {
        this.cliente = cliente;
    }

    public int getServicioId() {
        return servicioId;
    }

    public void setServicioId(int servicioId) {
        this.servicioId = servicioId;
    }

    public String getServicio() {
        return servicio;
    }

    public void setServicio(String servicio) {
        this.servicio = servicio;
    }

    public LocalDate getFecha() {
        return fecha;
    }

    public void setFecha(LocalDate fecha) {
        this.fecha = fecha;
    }

    public LocalTime getHora() {
        return hora;
    }

    public void setHora(LocalTime hora) {
        this.hora = hora;
    }

    public int getDuracionMin() {
        return duracionMin;
    }

    public void setDuracionMin(int duracionMin) {
        this.duracionMin = duracionMin;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getNotas() {
        return notas;
    }

    public void setNotas(String notas) {
        this.notas = notas;
    }

    @Override
    public String descripcion() {
        return cliente + " · " + servicio + " · " + fecha + " " + hora;
    }

    @Override
    public String toString() {
        return "Cita[" + "id=" + getId() + ", " + "clienteId=" + getClienteId() + ", " + "cliente=" + getCliente() + ", " + "servicioId=" + getServicioId() + ", " + "servicio=" + getServicio() + ", " + "fecha=" + getFecha() + ", " + "hora=" + getHora() + ", " + "duracionMin=" + getDuracionMin() + ", " + "estado=" + getEstado() + ", " + "notas=" + getNotas() + "]";
    }
}
