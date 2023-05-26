package ar.edu.itba.paw.models;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "roles")
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "roles_id_seq")
    @SequenceGenerator(sequenceName = "roles_id_seq", name = "roles_id_seq", allocationSize = 1)
    private Long id;

    @Column(length = 100, nullable = false, unique = true)
    private String name;

    public Role(final Long id, final String name) {
        this.id = id;
        this.name = name;
    }

    Role() {}

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Role roles = (Role) o;
        return Objects.equals(id, roles.id) && Objects.equals(name, roles.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name);
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