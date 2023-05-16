package ar.edu.itba.paw.services;

import ar.edu.itba.paw.models.User;
import ar.edu.itba.paw.services.exceptions.UserEmailNotFoundException;

public interface AuthUserService {
    boolean isAuthenticated();
    Boolean isCurrentUserEditor();
    User getCurrentUser();
}
