package ar.edu.itba.paw.services;

import ar.edu.itba.paw.models.Role;
import ar.edu.itba.paw.persistence.dao.RolesDao;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class RolesServiceImplTest {
    @Mock
    private RolesDao rolesDao;

    @InjectMocks
    private RolesServiceImpl rolesService;


    @Test
    public void testFindById() {
        final Role.RoleEnum roleEnum = Role.RoleEnum.USER;
        final Role role = new Role(roleEnum.getId(), roleEnum.getName());

        when(rolesDao.findById(roleEnum.getId())).thenReturn(Optional.of(role));

        final Optional<Role> roleOptional = rolesService.findById(roleEnum.getId());

        assertTrue(roleOptional.isPresent());
        assertEquals(roleEnum.getId(), roleOptional.get().getId());
        assertEquals(roleEnum.getName(), roleOptional.get().getName());
    }

    @Test
    public void testGetAll() {
        final Role.RoleEnum roleEnum = Role.RoleEnum.USER;
        final Role role = new Role(roleEnum.getId(), roleEnum.getName());

        when(rolesDao.getAll()).thenReturn(Collections.singletonList(role));

        final List<Role> actual = rolesService.getAll();

        assertEquals(1, actual.size());
        assertTrue(actual.contains(role));
    }

    @Test
    public void testFindByName() {
        final Role.RoleEnum roleEnum = Role.RoleEnum.USER;
        final Role role = new Role(roleEnum.getId(), roleEnum.getName());

        when(rolesDao.findByName(roleEnum.getName())).thenReturn(Optional.of(role));

        final Optional<Role> roleOptional = rolesService.findByName(roleEnum.getName());

        assertTrue(roleOptional.isPresent());
        assertEquals(roleEnum.getId(), roleOptional.get().getId());
        assertEquals(roleEnum.getName(), roleOptional.get().getName());
    }
}
