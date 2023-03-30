package ar.edu.itba.paw.models;

public class User {
    private final String email;
    private final String password;
    private final String nombre;

    public User(String email, String password, String nombre) {
        this.email = email;
        this.password = password;
        this.nombre = nombre;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public String getNombre() {
        return nombre;
    }
}
