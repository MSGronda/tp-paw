package ar.edu.itba.paw.models;

import java.util.List;

public class Carrera {
    private final int id;
    private final String nombre;
    private final List<Integer> idMaterias;

    public Carrera(int id, String nombre, List<Integer> idMaterias) {
        this.id = id;
        this.nombre = nombre;
        this.idMaterias = idMaterias;
    }

    public int getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public List<Integer> getIdMaterias() {
        return idMaterias;
    }
}
