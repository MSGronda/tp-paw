package ar.edu.itba.paw.models;

import javax.persistence.*;
import java.time.Instant;
import java.time.LocalDateTime;

@Entity
@Table(name = "recoverytoken")
public class RecoveryToken {
    @Id
    private String token;

    @OneToOne(optional = false)
    @JoinColumn(name = "userid")
    private User user;

    @Column(name = "created", nullable = false)
    private Instant creationTime;

    protected RecoveryToken() {}

    public RecoveryToken(final String token, final User user) {
        this.token = token;
        this.user = user;
        this.creationTime = Instant.now();
    }

    public String getToken() {
        return token;
    }

    public User getUser() {
        return user;
    }

    public Instant getCreationTime() {
        return creationTime;
    }
}
