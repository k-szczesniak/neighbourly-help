package pl.lodz.p.ks.it.neighbourlyhelp.endpoint;

import org.springframework.security.access.annotation.Secured;
import pl.lodz.p.ks.it.neighbourlyhelp.domain.user.AccessLevel;
import pl.lodz.p.ks.it.neighbourlyhelp.exception.AppBaseException;

public interface RoleEndpoint {

    /**
     * Odbiera uprawnienia użytkownikowi.
     *
     * @param email      identyfikator użytkownika
     * @param accessLevel poziom dostępu do odebrania
     * @throws AppBaseException gdy nie udało się zaktualizować danych
     */
    @Secured("ROLE_ADMIN")
    void revokeAccessLevel(String email, AccessLevel accessLevel) throws AppBaseException;

    /**
     * Przyznaje uprawnienia użytkownikowi.
     *
     * @param email      identyfikator użytkownika
     * @param accessLevel poziom dostępu do przyznania
     * @throws AppBaseException gdy nie udało się zaktualizować danych
     */
    @Secured("ROLE_ADMIN")
    void grantAccessLevel(String email, AccessLevel accessLevel) throws AppBaseException;
}