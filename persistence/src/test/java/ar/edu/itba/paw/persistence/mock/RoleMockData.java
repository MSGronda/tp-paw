package ar.edu.itba.paw.persistence.mock;

import ar.edu.itba.paw.models.Role;

public class RoleMockData {
    public static final int USER_ROLE_ID = 1;
    public static final String USER_ROLE_NAME = "USER";

    public static final int EDITOR_ROLE_ID = 2;
    public static final String EDITOR_ROLE_NAME = "EDITOR";

    public static Role getUserRole() {
        return new Role(USER_ROLE_ID, USER_ROLE_NAME);
    }

    public static Role getEditorRole() {
        return new Role(EDITOR_ROLE_ID, EDITOR_ROLE_NAME);
    }
}
