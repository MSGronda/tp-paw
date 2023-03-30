package ar.edu.itba.paw.models;

import java.util.List;

public class Materia {
    private final int id;
    private final String nombre;
    private final String depto;
    private final List<Integer> correlativas;
    private final List<Integer> idProfesores;
    private final List<Integer> idCarreras;

    public Materia(int id, String nombre, String depto, List<Integer> correlativas, List<Integer> idProfesores, List<Integer> idCarreras) {
        this.id = id;
        this.nombre = nombre;
        this.depto = depto;
        this.correlativas = correlativas;
        this.idProfesores = idProfesores;
        this.idCarreras = idCarreras;
    }

    public int getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public String getDepto() {
        return depto;
    }

    public List<Integer> getCorrelativas() {
        return correlativas;
    }

    public List<Integer> getIdProfesores() {
        return idProfesores;
    }

    public List<Integer> getIdCarreras() {
        return idCarreras;
    }
}
