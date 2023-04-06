package ar.edu.itba.paw.models;

import java.util.List;

public class Materia {
    private final long id;
    private final String nombre;
    private final String depto;
    private List<Long> correlativas;
    private List<Long> idProfesores;
    private List<Long> idCarreras;
    private final Integer creditos;

    public Materia(long id, String nombre, String depto, List<Long> correlativas, List<Long> idProfesores, List<Long> idCarreras, int creditos) {
        this.id = id;
        this.nombre = nombre;
        this.depto = depto;
        this.correlativas = correlativas;
        this.idProfesores = idProfesores;
        this.idCarreras = idCarreras;
        this.creditos = creditos;
    }

    public Materia(long id, String nombre, String depto, int creditos) {
        this.id = id;
        this.nombre = nombre;
        this.depto = depto;
        this.creditos = creditos;
    }

    public long getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public String getDepto() {
        return depto;
    }

    public List<Long> getCorrelativas() {
        return correlativas;
    }

    public List<Long> getIdProfesores() {
        return idProfesores;
    }

    public List<Long> getIdCarreras() {
        return idCarreras;
    }

    public Integer getCreditos(){ return creditos; }
}
