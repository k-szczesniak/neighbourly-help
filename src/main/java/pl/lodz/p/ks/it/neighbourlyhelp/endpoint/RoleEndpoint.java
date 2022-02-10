package pl.lodz.p.ks.it.neighbourlyhelp.endpoint;

import pl.lodz.p.ks.it.neighbourlyhelp.domain.user.AccessLevel;
import pl.lodz.p.ks.it.neighbourlyhelp.exception.AppBaseException;

import javax.annotation.security.RolesAllowed;

public interface RoleEndpoint {

    /**
     * Odbiera uprawnienia użytkownikowi.
     *
     * @param email      identyfikator użytkownika
     * @param accessLevel poziom dostępu do odebrania
     * @throws AppBaseException gdy nie udało się zaktualizować danych
     */
    @RolesAllowed("deleteAccessLevel")
    void revokeAccessLevel(String email, AccessLevel accessLevel) throws AppBaseException;

    /**
     * Przyznaje uprawnienia użytkownikowi.
     *
     * @param email      identyfikator użytkownika
     * @param accessLevel poziom dostępu do przyznania
     * @throws AppBaseException gdy nie udało się zaktualizować danych
     */
    @RolesAllowed("addAccessLevel")
    void grantAccessLevel(String email, AccessLevel accessLevel) throws AppBaseException;
}