package pl.lodz.p.ks.it.neighbourlyhelp.methodsecurity;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import pl.lodz.p.ks.it.neighbourlyhelp.RoleBeanToTest;

import static org.junit.Assert.assertEquals;

/**
 * Method to refactor after implementation of business methods
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test")
public class MethodSecurityTest {

    @Autowired
    RoleBeanToTest roleBeanToTest;

    @Test(expected = AuthenticationCredentialsNotFoundException.class)
    public void givenNoSecurity_whenCallGetUsername_thenReturnException() {
        String userName = roleBeanToTest.getUsername();
        assertEquals("john", userName);
    }

    @Test
    @WithMockUser(username = "john", roles = { "ADMIN" })
    public void givenRoleAdmin_whenCallGetUsername_thenReturnUsername() {
        String userName = roleBeanToTest.getUsername();
        assertEquals("john", userName);
    }

    @Test(expected = AccessDeniedException.class)
    @WithMockUser(username = "john", roles = { "CLIENT" })
    public void givenRoleAdmin_whenCallGetUsername_thenReturnAccessDenied() {
        roleBeanToTest.getUsername();
    }
}