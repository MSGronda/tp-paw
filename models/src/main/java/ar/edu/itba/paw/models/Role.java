package ar.edu.itba.paw.models;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "roles")
public class Role {
    @Id
    private Long id;

    @Column(length = 100, nullable = false, unique = true)
    private String name;

    public Role(final Long id, final String name) {
        this.id = id;
        this.name = name;
    }

    protected Role() {}

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Role)) return false;
        Role role = (Role) o;
        return Objects.equals(getId(), role.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId());
    }

    public enum RoleEnum {
        USER (1, "USER"),

        EDITOR (2, "EDITOR");

        private final long id;
        private final String name;
        RoleEnum(long id, String name) {
            this.id = id;
            this.name = name;
        }
        public long getId() {
            return id;
        }
        public String getName() {
            return name;
        }
    }
}
