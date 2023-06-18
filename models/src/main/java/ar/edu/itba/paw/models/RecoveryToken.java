package ar.edu.itba.paw.models;

import javax.persistence.*;
import java.time.LocalTime;

@Entity
@Table(name = "recoverytoken")
public class RecoveryToken {
    @Id
    private String token;

    @OneToOne(optional = false)
    @JoinColumn(name = "userid")
    private User user;

    @Column(name = "created", nullable = false)
    private LocalTime creationTime;

    protected RecoveryToken() {}

    public RecoveryToken(final String token, final User user) {
        this.token = token;
        this.user = user;
        this.creationTime = LocalTime.now();
    }

    public String getToken() {
        return token;
    }

    public User getUser() {
        return user;
    }

    public LocalTime getCreationTime() {
        return creationTime;
    }
}
