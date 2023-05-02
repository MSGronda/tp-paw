package ar.edu.itba.paw.models;

import java.util.Objects;

public class Roles {
    private final Long id;

    private final String role;

    public Roles(Long id, String role) {
        this.id = id;
        this.role = role;
    }

    public Long getId() {
        return id;
    }

    public String getRole() {
        return role;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Roles roles = (Roles) o;
        return Objects.equals(id, roles.id) && Objects.equals(role, roles.role);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, role);
    }
}
