package pl.lodz.p.ks.it.neighbourlyhelp.clientmodule.endpoint;

import org.springframework.security.access.annotation.Secured;
import pl.lodz.p.ks.it.neighbourlyhelp.advertmodule.dto.response.LoyaltyPointsDto;
import pl.lodz.p.ks.it.neighbourlyhelp.clientmodule.domain.enums.AccessLevel;
import pl.lodz.p.ks.it.neighbourlyhelp.clientmodule.dto.response.BasicAccountInformationDto;
import pl.lodz.p.ks.it.neighbourlyhelp.clientmodule.dto.response.ModeratorDataDto;
import pl.lodz.p.ks.it.neighbourlyhelp.clientmodule.dto.response.RolesDto;
import pl.lodz.p.ks.it.neighbourlyhelp.exception.AppBaseException;

import java.util.List;

public interface RoleEndpoint {

    /**
     * Odbiera uprawnienia użytkownikowi.
     *
     * @param email      identyfikator użytkownika
     * @param accessLevel poziom dostępu do odebrania
     * @param ifMatch     value of etag
     * @throws AppBaseException gdy nie udało się zaktualizować danych
     */
    @Secured("ROLE_ADMIN")
    void revokeAccessLevel(String email, AccessLevel accessLevel, String ifMatch) throws AppBaseException;

    /**
     * Przyznaje uprawnienia użytkownikowi.
     *
     * @param email      identyfikator użytkownika
     * @param accessLevel poziom dostępu do przyznania
     * @param ifMatch     value of etag
     * @throws AppBaseException gdy nie udało się zaktualizować danych
     */
    @Secured("ROLE_ADMIN")
    void grantAccessLevel(String email, AccessLevel accessLevel, String ifMatch) throws AppBaseException;

    RolesDto getUserRole() throws AppBaseException;

    RolesDto getUserRole(String email) throws AppBaseException;

    ModeratorDataDto getModeratorData(String email) throws AppBaseException;

    List<BasicAccountInformationDto> getAllFreeModeratorsList();

    List<BasicAccountInformationDto> getModeratorsAssignedToCity(Long cityId);

    LoyaltyPointsDto getLoyaltyPointsBalance() throws AppBaseException;
}