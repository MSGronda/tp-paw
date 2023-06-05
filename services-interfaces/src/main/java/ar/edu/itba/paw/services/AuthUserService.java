package ar.edu.itba.paw.services;

import ar.edu.itba.paw.models.User;

public interface AuthUserService {
    boolean isAuthenticated();
    Boolean isCurrentUserEditor();
    User getCurrentUser();
}
