package ar.edu.itba.paw.models;

import java.util.List;

public class Profesor {
    private final long id;
    private final String nombre;
    private List<Long> idMaterias;

    public Profesor(long id, String nombre){
        this.id = id;
        this.nombre = nombre;
    }
    public Profesor(long id, String nombre, List<Long> idMaterias) {
        this.id = id;
        this.nombre = nombre;
        this.idMaterias = idMaterias;
    }

    public long getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public List<Long> getIdMaterias() {
        return idMaterias;
    }

    public void setIdMaterias(List<Long> idMaterias) {
        this.idMaterias = idMaterias;
    }
}
