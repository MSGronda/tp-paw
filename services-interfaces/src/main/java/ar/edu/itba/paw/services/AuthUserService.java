package ar.edu.itba.paw.services;

import ar.edu.itba.paw.models.User;
import ar.edu.itba.paw.models.exceptions.UserNotFoundException;

public interface AuthUserService {
    boolean isAuthenticated();
    User getCurrentUser() throws UserNotFoundException;
}
