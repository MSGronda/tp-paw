package ar.edu.itba.paw.services;

import ar.edu.itba.paw.models.User;
import ar.edu.itba.paw.models.exceptions.UserNotFoundException;

public interface AuthUserService {
    boolean isAuthenticated();
    Boolean isCurrentUserEditor();
    User getCurrentUser() throws UserNotFoundException;
}
